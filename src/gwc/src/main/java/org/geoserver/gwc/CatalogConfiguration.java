/* Copyright (c) 2010 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.gwc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.geoserver.catalog.Catalog;
import org.geoserver.catalog.LayerGroupInfo;
import org.geoserver.catalog.LayerInfo;
import org.geoserver.gwc.layer.GeoServerTileLayer;
import org.geotools.util.logging.Logging;
import org.geowebcache.GeoWebCacheException;
import org.geowebcache.config.Configuration;
import org.geowebcache.config.meta.ServiceInformation;
import org.geowebcache.grid.GridSetBroker;
import org.geowebcache.layer.TileLayer;
import org.geowebcache.layer.TileLayerDispatcher;

/**
 * A GWC's {@link Configuration} implementation that provides {@link TileLayer}s directly from the
 * GeoServer {@link Catalog}'s {@link LayerInfo}s and {@link LayerGroupInfo}s.
 * <p>
 * The sole responsibility of the class is to
 * 
 * @see #createLayer(LayerInfo)
 * @see #createLayer(LayerGroupInfo)
 * @see #getTileLayers(boolean)
 * @see CatalogStyleChangeListener
 */
public class CatalogConfiguration implements Configuration {

    private static Logger log = Logging.getLogger(CatalogConfiguration.class);

    private final Map<String, GeoServerTileLayer> layers;

    private final List<String> mimeFormats;

    private final GWC mediator;

    /**
     * 
     * @param mediator
     */
    public CatalogConfiguration(final GWC mediator) {

        this.mediator = mediator;

        this.layers = new HashMap<String, GeoServerTileLayer>();

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

    /**
     * 
     * @see org.geowebcache.config.Configuration#getIdentifier()
     */
    public String getIdentifier() throws GeoWebCacheException {
        return "GeoServer Catalog Configuration";
    }

    /**
     * @see org.geowebcache.config.Configuration#getServiceInformation()
     * @return {@code null}
     */
    public ServiceInformation getServiceInformation() throws GeoWebCacheException {
        return null;
    }

    /**
     * @see org.geowebcache.config.Configuration#isRuntimeStatsEnabled()
     */
    public boolean isRuntimeStatsEnabled() {
        return true;
    }

    /**
     * Returns the list of {@link GeoServerTileLayer} objects matching the GeoServer ones.
     * <p>
     * The list is built once and kept, as it is used by {@link TileLayerDispatcher}. Whenever a
     * layer is added or removed something else needs to notify
     * {@link TileLayerDispatcher#add(TileLayer)} or {@link TileLayerDispatcher#remove(String)} and
     * make sure the {@link GeoServerTileLayer} is created/deleted using this class.
     * </p>
     * <p>
     * If the internal list of {@link GeoServerTileLayer} objects matching 1-1 the GeoServer layers
     * and layergorups is empty, or {@code reload == true}, creates a list of
     * {@link GeoServerTileLayer} for each geoserver layer(group).
     * </p>
     * 
     * @see org.geowebcache.config.Configuration#getTileLayers(boolean)
     * @see #createLayer(LayerInfo)
     * @see #createLayer(LayerGroupInfo)
     * @see GWC#createLayer(LayerInfo)
     * @see GWC#removeLayer(String)
     */
    public synchronized List<GeoServerTileLayer> getTileLayers(boolean reload)
            throws GeoWebCacheException {
        if (reload) {
            layers.clear();
        }
        if (layers.isEmpty()) {
            log.fine("Creating GeoServerTileLayers for the available GeoServer LayerInfos");
            // Adding normal layers
            for (LayerInfo li : mediator.getLayerInfos()) {
                createLayer(li);
            }

            log.fine("Creating GeoServerTileLayers for the available GeoServer LayerGroups");
            // Adding layer groups
            for (LayerGroupInfo lgi : mediator.getLayerGroups()) {
                createLayer(lgi);
            }
        }
        if (log.isLoggable(Level.FINE)) {
            log.fine("Responding with " + layers.size()
                    + " to getTileLayers() request from TileLayerDispatcher");
        }

        return new ArrayList<GeoServerTileLayer>(layers.values());
    }

    /**
     * 
     * @param li
     * @return
     */
    public GeoServerTileLayer createLayer(LayerInfo li) {
        log.finer("Creating GeoServerTileLayer for LayerInfo " + li.getName());
        GeoServerTileLayer geoServerTileLayer = new GeoServerTileLayer(mediator, li);
        postCreate(geoServerTileLayer);
        return geoServerTileLayer;
    }

    public GeoServerTileLayer createLayer(LayerGroupInfo lgi) {
        log.finer("Creating GeoServerTileLayer for LayerGroup " + lgi.getName());
        GeoServerTileLayer geoServerTileLayer = new GeoServerTileLayer(mediator, lgi);
        postCreate(geoServerTileLayer);
        return geoServerTileLayer;
    }

    private void postCreate(GeoServerTileLayer geoServerTileLayer) {
        GridSetBroker gridSetBroker = mediator.getGridSetBroker();
        geoServerTileLayer.initialize(gridSetBroker);
        String layerName = geoServerTileLayer.getName();
        layers.put(layerName, geoServerTileLayer);
    }

    /**
     * @param layerName
     * @return
     */
    public synchronized GeoServerTileLayer removeLayer(String layerName) {
        GeoServerTileLayer removed = layers.remove(layerName);
        return removed;
    }

    public void add(GeoServerTileLayer tileLayer) {
        postCreate(tileLayer);
    }

    public synchronized GeoServerTileLayer getLayerById(String id) {
        List<GeoServerTileLayer> registered = new ArrayList<GeoServerTileLayer>(layers.values());
        for (GeoServerTileLayer layer : registered) {
            if (id.equals(layer.getId())) {
                return layer;
            }
        }
        return null;
    }

}
