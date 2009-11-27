/** 
 * Copyright (c) 2001 - 2009 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 * 
 * @author Arne Kepp / OpenGeo
 */
package org.geoserver.gwc;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import org.geoserver.catalog.Catalog;
import org.geoserver.catalog.CatalogException;
import org.geoserver.catalog.LayerGroupInfo;
import org.geoserver.catalog.LayerInfo;
import org.geoserver.catalog.ResourceInfo;
import org.geoserver.catalog.StyleInfo;
import org.geoserver.catalog.event.CatalogAddEvent;
import org.geoserver.catalog.event.CatalogListener;
import org.geoserver.catalog.event.CatalogModifyEvent;
import org.geoserver.catalog.event.CatalogPostModifyEvent;
import org.geoserver.catalog.event.CatalogRemoveEvent;
import org.geoserver.catalog.impl.StyleInfoImpl;
import org.geoserver.ows.Dispatcher;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.geotools.util.logging.Logging;
import org.geowebcache.GeoWebCacheException;
import org.geowebcache.config.Configuration;
import org.geowebcache.config.meta.ServiceInformation;
import org.geowebcache.grid.BoundingBox;
import org.geowebcache.grid.GridSetBroker;
import org.geowebcache.grid.GridSubset;
import org.geowebcache.grid.GridSubsetFactory;
import org.geowebcache.layer.TileLayer;
import org.geowebcache.layer.TileLayerDispatcher;
import org.geowebcache.layer.wms.WMSGeoServerHelper;
import org.geowebcache.layer.wms.WMSLayer;
import org.geowebcache.util.ApplicationContextProvider;


/**
 * This class implements a GeoWebCache configuration object, i.e. 
 * a source of WMS layer definitions, and a GeoServer catalog listener
 * which is loaded on startup and listens for configuration changes.
 */
public class GWCCatalogListener implements CatalogListener, Configuration {

    private static Logger log = Logging.getLogger("org.geoserver.gwc.GWCCatalogListener");
    
    final protected Catalog cat;

    final protected Dispatcher gsDispatcher;
    
    final protected GridSetBroker gridSetBroker;
    
    final protected GWCCleanser cleanser;
    
    protected TileLayerDispatcher layerDispatcher;
    
    private List<String> mimeFormats = null;
    
    private int[] metaFactors = {4,4};
    
    private String wmsUrl = null;
    
    ArrayList<TileLayer> list;

    public GWCCatalogListener(
            Catalog cat,
            Dispatcher gsDispatcher,
            GridSetBroker gridSetBroker, 
            ApplicationContextProvider ctxProv,
            GWCCleanser cleanser) {
        
        this.cat = cat;
        
        this.gridSetBroker = gridSetBroker;
        
        this.gsDispatcher = gsDispatcher;
        
        this.cleanser = cleanser;
        
        mimeFormats = new ArrayList<String>(5);
        mimeFormats.add("image/png");
        mimeFormats.add("image/gif");
        mimeFormats.add("image/png8");
        mimeFormats.add("image/jpeg"); 
        mimeFormats.add("application/vnd.google-earth.kml+xml");
        
        cat.addListener(this);
        
        log.fine("GWCCatalogListener registered with catalog");
    }
    
    public void setTileLayerDispatcher(TileLayerDispatcher layerDispatcher) {
        log.fine("TileLayerDispatcher was set");
        this.layerDispatcher = layerDispatcher;
    }
    
    public void handleAddEvent(CatalogAddEvent event) throws CatalogException {
        Object obj = event.getSource();
        
        WMSLayer wmsLayer = null;
        
        // We only handle layers here. Layer groups are initially empty
        if(obj instanceof LayerInfo) {
            LayerInfo layerInfo = (LayerInfo) obj;
            wmsLayer = getLayer(layerInfo);
        }
        
        if (wmsLayer != null && this.list != null) {
            addToList(wmsLayer);
            layerDispatcher.getLayers();
            layerDispatcher.add(wmsLayer);
            log.finer(wmsLayer.getName() + " added to TileLayerDispatcher");
        }
    }

    public void handleModifyEvent(CatalogModifyEvent event) throws CatalogException {
        // Not dealing with this one just yet
    }

    public void handlePostModifyEvent(CatalogPostModifyEvent event) throws CatalogException {
        Object obj = event.getSource();
        
        if(obj instanceof StyleInfoImpl) {
           // TODO First pass only considers default styles,
           // which is all GWC will accept anyway
           StyleInfoImpl si = (StyleInfoImpl) obj;
           String styleName = si.getName();
           
           LinkedList<String> layerNameList = new LinkedList<String>();
           
           // First we collect all the layers that use this style
           Iterator<LayerInfo> liter = cat.getLayers().iterator();
           while(liter.hasNext()) {
               LayerInfo li = liter.next();
               if(li.getDefaultStyle().getName().equals(styleName)) {
                   String prefixedName = li.getResource().getPrefixedName();
                   layerNameList.add(prefixedName);
                   cleanser.deleteLayer(prefixedName);
               }
           }
           
           // Now we check for layer groups that are affected
           Iterator<LayerGroupInfo> lgiter = cat.getLayerGroups().iterator();
           while(lgiter.hasNext()) {
               LayerGroupInfo lgi = lgiter.next();
               boolean truncate = false;
               
               // First we check for referenced to affected layers
               liter = lgi.getLayers().iterator();
               while(! truncate && liter.hasNext()) {
                   LayerInfo li = liter.next();
                   if(layerNameList.contains(li.getResource().getPrefixedName())){
                       truncate = true;
                   }
               }
               
               // Finally we need to check whether the style is used explicitly
               if(! truncate) {
                   Iterator<StyleInfo> siiter = lgi.getStyles().iterator();
                   while(! truncate && siiter.hasNext()) {
                       StyleInfo si2 = siiter.next();
                       if(si2 != null && si2.getName().equals(si.getName())) {
                           truncate = true;
                       }
                   }
               }
               
               if(truncate) {
                   cleanser.deleteLayer(lgi.getName());
               }
               // Next layer group
           }
           
        } else {

        WMSLayer wmsLayer = null; //getLayer(obj);

        if(obj instanceof LayerInfo) {
            LayerInfo li = (LayerInfo) obj;
            wmsLayer = getLayer(li);
        } else
        if(obj instanceof LayerGroupInfo) {
            LayerGroupInfo lgInfo = (LayerGroupInfo) obj;
            wmsLayer = getLayer(lgInfo);
        }
        
        if (wmsLayer != null && this.list != null) {            
            updateList(wmsLayer);
            layerDispatcher.getLayers();
            layerDispatcher.update(wmsLayer);
            log.finer(wmsLayer.getName() + " updated on TileLayerDispatcher");
            cleanser.deleteLayer(wmsLayer.getName());
        }
        }
    }

    public void handleRemoveEvent(CatalogRemoveEvent event) throws CatalogException {
        Object obj = event.getSource();
       
        String prefixedName = null;
        
        if(obj instanceof LayerGroupInfo) {
            LayerGroupInfo lgInfo = (LayerGroupInfo) obj;
            prefixedName = lgInfo.getName();
        } else
        if(obj instanceof LayerInfo) {
            LayerInfo layerInfo = (LayerInfo) obj;
            prefixedName = layerInfo.getResource().getPrefixedName();
        }
        
        if(null != prefixedName) {
            removeFromList(prefixedName);
            layerDispatcher.remove(prefixedName);
            cleanser.deleteLayer(prefixedName);
        }
    }

    public void reloaded() {
        try {
            layerDispatcher.reInit();
        } catch (GeoWebCacheException gwce) {
            log.fine("Unable to reinit TileLayerDispatcher gwce.getMessage()");
        }
    }

    public String getIdentifier() throws GeoWebCacheException {
        return "GeoServer Catalog Listener";
    }

    public ServiceInformation getServiceInformation() throws GeoWebCacheException {
        // TODO Auto-generated method stub
        return null;
    }

    public List<TileLayer> getTileLayers(boolean reload) throws GeoWebCacheException {
        if(! reload && list != null) {
            return list;
        }
        
        list = new ArrayList<TileLayer>(cat.getLayers().size());
        
        // Adding vector layers
        Iterator<LayerInfo> lIter = cat.getLayers().iterator();
        while(lIter.hasNext()) {
            LayerInfo li = lIter.next();            
            TileLayer tl = getLayer(li.getResource());
            //System.out.println(tl.getName() + " layerinfo");
            list.add(tl);
        }
                
        // Adding layer groups 
        Iterator<LayerGroupInfo> lgIter = cat.getLayerGroups().iterator();
        while(lgIter.hasNext()) {
            LayerGroupInfo lgi = lgIter.next();
            
            TileLayer tl = getLayer(lgi);
            //System.out.println(tl.getName() + " layergroupinfo");
            list.add(tl);
        }
        
        log.fine("Responding with " + list.size() + " to getTileLayers() request from TileLayerDispatcher");
        
        return list;
    }
    
    synchronized private void updateList(WMSLayer wmsLayer) {
        if(this.list != null) {
            removeFromList(wmsLayer);
            addToList(wmsLayer);
        }
    }
    
    private void removeFromList(WMSLayer wmsLayer) {
        removeFromList(wmsLayer.getName());
    }
    
    synchronized private void removeFromList(String layerName) {
        if(this.list != null) {
            Iterator<TileLayer> iter = list.iterator();
            int i = 0;
            while(iter.hasNext()) {
                TileLayer tl = iter.next();
                if(tl != null && tl.getName().equals(layerName)) {
                    list.remove(i);
                    return;
                }
                i++;
            }
            log.finer("Did not find " + layerName + " in list.");
        }
    }
    
    synchronized private void addToList(WMSLayer wmsLayer) {
        if(this.list != null) {
            list.add(wmsLayer);
        }
    }
    
    private WMSLayer getLayer(LayerInfo li) {
        return getLayer(li.getResource());
    }
    
    private WMSLayer getLayer(LayerGroupInfo lgi) {
        ReferencedEnvelope latLonBounds = null;
        try {
            latLonBounds = lgi.getBounds().transform(CRS.decode("EPSG:4326"), true);
        } catch (Exception e) {
            log.warning(e.getMessage());
        }
        
        if(latLonBounds == null) {
            log.severe("GWCCatalogListener had problems getting or reprojecting " 
                    + lgi.getBounds() + " to EPSG:4326");
            
            return null;
        }
        
        WMSLayer retLayer = new WMSLayer(
                lgi.getName(),
                getWMSUrl(),
                null, // Styles 
                lgi.getName(), 
                mimeFormats, 
                getGrids(latLonBounds), 
                metaFactors,
                null,
                true);
        
        retLayer.setBackendTimeout(120);
        retLayer.setSourceHelper(new WMSGeoServerHelper(this.gsDispatcher));
        
        retLayer.initialize(gridSetBroker);
        return retLayer;
    }
    
    private WMSLayer getLayer(ResourceInfo fti) {
        WMSLayer retLayer = new WMSLayer(
                fti.getPrefixedName(),
                getWMSUrl(), 
                null, // Styles 
                fti.getPrefixedName(), 
                mimeFormats, 
                getGrids(fti.getLatLonBoundingBox()), 
                metaFactors,
                null,
                true);
        retLayer.setBackendTimeout(120);
        retLayer.setSourceHelper(new WMSGeoServerHelper(this.gsDispatcher));
        
        retLayer.initialize(gridSetBroker);
        return retLayer;
    }
    
//    private WMSLayer getLayer(CoverageInfo ci) {
//        WMSLayer retLayer = new WMSLayer(
//                ci.getPrefixedName(),
//                getWMSUrl(), 
//                null, // Styles 
//                ci.getPrefixedName(), 
//                mimeFormats, 
//                getGrids(ci.getLatLonBoundingBox()), 
//                metaFactors,
//                null, 
//                false);
//        
//        retLayer.setBackendTimeout(120);
//        retLayer.setSourceHelper(new WMSGeoServerHelper(this.gsDispatcher));
//        
//        retLayer.initialize(gridSetBroker);
//        return retLayer;   
//    }

    private String[] getWMSUrl() {
        String[] strs = { wmsUrl };
        return strs;
    }
    
    private Hashtable<String,GridSubset> getGrids(ReferencedEnvelope env) {
        double minX = env.getMinX();
        double minY = env.getMinY();
        double maxX = env.getMaxX();
        double maxY = env.getMaxY();

        BoundingBox bounds4326 = new BoundingBox(minX,minY,maxX,maxY);
 
        BoundingBox bounds900913 = new BoundingBox(
                longToSphericalMercatorX(minX),
                latToSphericalMercatorY(minY),
                longToSphericalMercatorX(maxX),
                latToSphericalMercatorY(maxY));
        
        Hashtable<String,GridSubset> grids = new Hashtable<String,GridSubset>(2);
        
        GridSubset gridSubset4326 = GridSubsetFactory.createGridSubSet(
                gridSetBroker.WORLD_EPSG4326, 
                bounds4326, 0, 25 );
        
        grids.put(gridSetBroker.WORLD_EPSG4326.getName(), gridSubset4326);
        
        GridSubset gridSubset900913 = GridSubsetFactory.createGridSubSet(
                gridSetBroker.WORLD_EPSG3857, 
                bounds900913, 0, 25 );
        
        grids.put(gridSetBroker.WORLD_EPSG3857.getName(), gridSubset900913);
               
        return grids;
    }
    
    private double longToSphericalMercatorX(double x) {
        return (x/180.0)*20037508.34;
    }
    
    private double latToSphericalMercatorY(double y) {        
        if(y > 85.05112) {
            y = 85.05112;
        }
        
        if(y < -85.05112) {
            y = -85.05112;
        }
        
        y = (Math.PI/180.0)*y;
        double tmp = Math.PI/4.0 + y/2.0; 
        return 20037508.34 * Math.log(Math.tan(tmp)) / Math.PI;
    }

}
