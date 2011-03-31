/* Copyright (c) 2010 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.gwc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.geoserver.catalog.Catalog;
import org.geoserver.catalog.LayerGroupInfo;
import org.geoserver.catalog.LayerInfo;
import org.geoserver.gwc.layer.GeoServerTileLayer;
import org.geoserver.ows.Dispatcher;
import org.geotools.util.logging.Logging;
import org.geowebcache.GeoWebCacheException;
import org.geowebcache.config.Configuration;
import org.geowebcache.config.meta.ServiceInformation;
import org.geowebcache.grid.GridSetBroker;
import org.geowebcache.layer.TileLayer;

public class CatalogConfiguration implements Configuration {
    private static Logger log = Logging.getLogger("org.geoserver.gwc.GWCCatalogListener");

    private final Catalog catalog;

    private final Dispatcher gsDispatcher;

    private final Map<String, TileLayer> layers;

    private final List<String> mimeFormats;

    private final GWCConfigPersister gsGwcConfigPersister;

    private final GridSetBroker gridSetBroker;

    /**
     * 
     * @param cat
     * @param gridSetBroker
     * @param gsDispatcher
     */
    public CatalogConfiguration(final GWCConfigPersister gsGwcConfigPersister,
            final GridSetBroker gridSetBroker, final Catalog cat, final Dispatcher gsDispatcher) {

        this.gsGwcConfigPersister = gsGwcConfigPersister;
        this.gridSetBroker = gridSetBroker;
        this.catalog = cat;
        this.gsDispatcher = gsDispatcher;

        layers = new HashMap<String, TileLayer>();

        // REVISIT: these are the old default mime formats available for all geoserver layers. We're
        // gonna make that an opt-in configuration option on a per layer basis and default to png
        // only for vector layers and jpeg only for coverages. Not sure about layer groups though
        mimeFormats = new ArrayList<String>(5);
        mimeFormats.add("image/png");
        mimeFormats.add("image/gif");
        mimeFormats.add("image/png8");
        mimeFormats.add("image/jpeg");
        mimeFormats.add("application/vnd.google-earth.kml+xml");
    }

    public GWCConfigPersister getConfigPersister() {
        return this.gsGwcConfigPersister;
    }

    /**
     * 
     * @see org.geowebcache.config.Configuration#getIdentifier()
     */
    public String getIdentifier() throws GeoWebCacheException {
        return "GeoServer Catalog Configuration";
    }

    public Catalog getCatalog() {
        return catalog;
    }

    /**
     * 
     * @see org.geowebcache.config.Configuration#getServiceInformation()
     */
    public ServiceInformation getServiceInformation() throws GeoWebCacheException {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @see org.geowebcache.config.Configuration#isRuntimeStatsEnabled()
     */
    public boolean isRuntimeStatsEnabled() {
        return true;
    }

    /**
     * 
     * @see org.geowebcache.config.Configuration#getTileLayers(boolean)
     */
    public synchronized List<TileLayer> getTileLayers(boolean reload) throws GeoWebCacheException {
        if (reload) {
            layers.clear();
        }
        if (layers.isEmpty()) {
            // Adding normal layers
            for (LayerInfo li : catalog.getLayers()) {
                createLayer(li);
            }

            // Adding layer groups
            for (LayerGroupInfo lgi : catalog.getLayerGroups()) {
                createLayer(lgi);
            }
        }
        log.fine("Responding with " + layers.size()
                + " to getTileLayers() request from TileLayerDispatcher");

        return new ArrayList<TileLayer>(layers.values());
    }

    /**
     * 
     * @param li
     * @return
     */
    public TileLayer createLayer(LayerInfo li) {
        log.info(" ---- Creating GeoServerTileLayer for " + li.getName());
        GeoServerTileLayer geoServerTileLayer = new GeoServerTileLayer(this, li);
        geoServerTileLayer.initialize(gridSetBroker);
        String layerName = geoServerTileLayer.getName();
        layers.put(layerName, geoServerTileLayer);
        return geoServerTileLayer;
    }

    public TileLayer createLayer(LayerGroupInfo lgi) {
        log.info(" ---- Creating GeoServerTileLayer for " + lgi.getName());
        GeoServerTileLayer geoServerTileLayer = new GeoServerTileLayer(this, lgi);
        geoServerTileLayer.initialize(gridSetBroker);
        String layerName = geoServerTileLayer.getName();
        layers.put(layerName, geoServerTileLayer);
        return geoServerTileLayer;
    }

    public synchronized void removeLayer(String layerName) {
        layers.remove(layerName);
    }

    public Dispatcher getDispatcher() {
        return gsDispatcher;
    }
}
