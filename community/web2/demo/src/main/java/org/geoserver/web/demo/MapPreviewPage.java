/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.web.demo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.geoserver.catalog.Catalog;
import org.geoserver.catalog.LayerGroupInfo;
import org.geoserver.catalog.LayerInfo;
import org.geoserver.catalog.ResourceInfo;
import org.geoserver.web.GeoServerApplication;
import org.geoserver.web.GeoServerBasePage;
import org.geoserver.web.wicket.GeoServerPagingNavigator;
import org.geoserver.wms.DefaultWebMapService;
import org.geoserver.wms.MapLayerInfo;
import org.geoserver.wms.WMS;
import org.geotools.util.logging.Logging;
import org.vfny.geoserver.wms.GetMapProducer;
import org.vfny.geoserver.wms.requests.GetMapRequest;

import com.vividsolutions.jts.geom.Envelope;

/**
 * Lists the available WMS layers, allows for a map preview
 * 
 * @author Andrea Aime - TOPP
 */
public class MapPreviewPage extends GeoServerBasePage {
    private static final Logger LOGGER = Logging.getLogger(MapPreviewPage.class);
    private static final int MAX_ROWS = 25;

    @SuppressWarnings("serial")
    public MapPreviewPage() {
        // TODO: This should list Layers, not Resources. (Should it exist
        // per-workspace? just have
        // the layers grouped by workspace in a single page?)
        IModel resourceListModel = new LoadableDetachableModel() {
            public Object load() {
                List<String> result = new ArrayList<String>();

                // gather layer and group names
                List<LayerInfo> layers = getCatalog().getLayers();
                for (LayerInfo layer : layers) {
                    ResourceInfo resource = layer.getResource();
                    if (layer.isEnabled() && resource.isEnabled()) {
                        
                        String layerName = resource.getPrefixedName(); 
                        
                        // make sure we can display the item
                        try {
                            GetMapRequest request = buildFakeGetMap(layerName);
                            DefaultWebMapService.autoSetBoundsAndSize(request);
                            
                            // ok, won't bomb out trying to setup the page
                            result.add(layerName);
                        } catch(Exception e) {
                            LOGGER.log(Level.FINE, "Layer " + layerName + " cannot be displayed due to projection settings");
                        }
                        
                    }
                }
                List<LayerGroupInfo> groups = getCatalog().getLayerGroups();
                for (LayerGroupInfo group : groups) {
                    result.add(group.getName());
                }

                // alphabetical sort
                Collections.sort(result);
                return result;
            }
        };

        final List<String> formats = getAvailableFormats();

        PageableListView layers = new PageableListView("layer", resourceListModel, MAX_ROWS) {
            public void populateItem(ListItem item) {
                // alternate bg color
                item.add(new SimpleAttributeModifier("class", item.getIndex() % 2 == 0 ? "even"
                        : "odd"));

                final String layerName = (String) item.getModelObject();

                GetMapRequest request = buildFakeGetMap(layerName);
                DefaultWebMapService.autoSetBoundsAndSize(request);
                final String linkTemplate = buildWmsLinkTemplate(request);

                final String OL_FORMAT = "application/openlayers";
                item.add(new ExternalLink("layerLink", linkTemplate.replaceAll("\\$\\{format\\}",
                        OL_FORMAT)).setContextRelative(true).add(new Label("label", layerName)));

                item.add(new ListView("formatLink", formats) {
                    public void populateItem(ListItem item) {
                        String format = (String) item.getModel().getObject();
                        item.add(new ExternalLink("link", linkTemplate.replaceAll(
                                "\\$\\{format\\}", format)).setContextRelative(true).add(
                                new Label("label", format)));
                    }
                });
            }
        };
        WebMarkupContainer cnt = new WebMarkupContainer("layerContainer");
        cnt.setOutputMarkupId(true);
        cnt.add(layers);
        add(cnt);

        // add pagers
        final GeoServerPagingNavigator topPager = new GeoServerPagingNavigator("topNav", layers);
        final GeoServerPagingNavigator bottomPager = new GeoServerPagingNavigator("bottomNav",
                layers);
        add(topPager);
        add(bottomPager);
        if (layers.size() < MAX_ROWS) {
            topPager.setVisible(false);
            bottomPager.setVisible(false);
        }
    }

    /**
     * Builds a fake GetMap request
     * 
     * @param prefixedName
     * @return
     */
    protected GetMapRequest buildFakeGetMap(String prefixedName) {
        GetMapRequest gm = new GetMapRequest(new WMS(getGeoServer()));
        Catalog catalog = getCatalog();
        List<MapLayerInfo> layers = expandLayers(prefixedName, catalog);
        gm.setLayers(layers.toArray(new MapLayerInfo[layers.size()]));
        gm.setFormat("application/openlayers");
        return gm;
    }

    /**
     * Expands the specified name into a list of layer info names
     * 
     * @param prefixedName
     * @param catalog
     * @return
     */
    private List<MapLayerInfo> expandLayers(String prefixedName, Catalog catalog) {
        List<MapLayerInfo> layers = new ArrayList<MapLayerInfo>();
        
        LayerInfo layer = catalog.getLayerByName( prefixedName );
        if ( layer != null ) {
            layers.add( new MapLayerInfo( layer ) );
        }
        else {
            //check for a layer grouping
            LayerGroupInfo lg = catalog.getLayerGroupByName( prefixedName );
            if ( lg != null ) {
                for ( LayerInfo l : lg.getLayers() ) {
                    layers.add( new MapLayerInfo( l ) );
                }
            }
        }
        return layers;
    }

    
    /**
     * Returns the map layer info for the specified layer, or null if the layer
     * is not known
     * @param layerName
     * @return
     */
    public MapLayerInfo getMapLayerInfo(String layerName, Catalog catalog) {
        
        LayerInfo layerInfo = catalog.getLayerByName(layerName);
        
        if(layerInfo != null){
            return new MapLayerInfo(layerInfo);
        }
        return null;
    }

    
    /**
     * Given a request and a target format, builds the WMS request
     * 
     * @param request
     * @param string
     * @return
     */
    protected String buildWmsLinkTemplate(GetMapRequest request) {
        final Envelope bbox = request.getBbox();
        if (bbox == null) {
            System.out.println("No bbox for layer " + request.getLayers()[0].getName());
            return "";
        }
        return "wms?service=WMS&version=1.1.0&request=GetMap" //
                + "&layers=" + request.getLayers()[0].getName() //
                + "&styles=" //
                + "&format=${format}" //
                + "&bbox=" + bbox.getMinX() + "," + bbox.getMinY() //
                + "," + bbox.getMaxX() + "," + bbox.getMaxY() //
                + "&width=" + request.getWidth() //
                + "&height=" + request.getHeight() + "&srs=" + request.getSRS();
    }

    private List<String> getAvailableFormats() {
        List<String> formats = new ArrayList<String>();

        final GeoServerApplication application = getGeoServerApplication();
        for (GetMapProducer producer : application.getBeansOfType(GetMapProducer.class)) {
            formats.add(producer.getOutputFormat());
        }
        Collections.sort(formats);

        return formats;
    }
}
