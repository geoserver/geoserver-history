/** 
 * Copyright (c) 2001 - 2009 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 * 
 * @author Arne Kepp / OpenGeo
 */
package org.geoserver.gwc;

import java.util.logging.Logger;

import org.geoserver.catalog.Catalog;
import org.geoserver.catalog.CatalogException;
import org.geoserver.catalog.LayerGroupInfo;
import org.geoserver.catalog.LayerInfo;
import org.geoserver.catalog.event.CatalogAddEvent;
import org.geoserver.catalog.event.CatalogListener;
import org.geoserver.catalog.event.CatalogModifyEvent;
import org.geoserver.catalog.event.CatalogPostModifyEvent;
import org.geoserver.catalog.event.CatalogRemoveEvent;
import org.geoserver.gwc.layer.GeoServerTileLayer;
import org.geotools.util.logging.Logging;

/**
 * Listens to {@link Catalog}'s layer added/removed events and adds/removes
 * {@link GeoServerTileLayer}s to/from the {@link CatalogConfiguration}
 * 
 * @author Arne Kepp
 * @author Gabriel Roldan
 */
public class CatalogLayerEventListener implements CatalogListener {

    private static Logger log = Logging.getLogger(CatalogLayerEventListener.class);

    private final GWC gwc;

    public CatalogLayerEventListener(final GWC gwc) {
        this.gwc = gwc;
    }

    /**
     * @see org.geoserver.catalog.event.CatalogListener#handleAddEvent(org.geoserver.catalog.event.CatalogAddEvent)
     */
    public void handleAddEvent(CatalogAddEvent event) throws CatalogException {
        Object obj = event.getSource();
        // We only handle layers here. Layer groups are initially empty
        if (obj instanceof LayerInfo) {
            log.finer("Handling add event: " + obj);
            LayerInfo layerInfo = (LayerInfo) obj;
            gwc.createLayer(layerInfo);
        } else if (obj instanceof LayerGroupInfo) {
            LayerGroupInfo lgi = (LayerGroupInfo) obj;
            gwc.createLayer(lgi);
        }
    }

    /**
     * @see org.geoserver.catalog.event.CatalogListener#handleModifyEvent(org.geoserver.catalog.event.CatalogModifyEvent)
     * @see #handlePostModifyEvent
     */
    public void handleModifyEvent(CatalogModifyEvent event) throws CatalogException {
        // Not dealing with this one just yet
    }

    /**
     * In case the event refers to the addition or removal of a {@link LayerInfo} or
     * {@link LayerGroupInfo} adds or removes the corresponding {@link GeoServerTileLayer} through
     * {@link GWC#createLayer}.
     * <p>
     * Note this method does not discriminate whether the change in the layer or layergroup deserves
     * a change in its matching TileLayer, it just re-creates the TileLayer
     * </p>
     * 
     * @see org.geoserver.catalog.event.CatalogListener#handlePostModifyEvent(org.geoserver.catalog.event.CatalogPostModifyEvent)
     */
    public void handlePostModifyEvent(CatalogPostModifyEvent event) throws CatalogException {
        Object obj = event.getSource();
        if (obj instanceof LayerInfo) {
            log.finer("Handling modify event for " + obj);
            LayerInfo li = (LayerInfo) obj;
            gwc.createLayer(li);
        } else if (obj instanceof LayerGroupInfo) {
            log.finer("Handling modify event for " + obj);
            LayerGroupInfo lgInfo = (LayerGroupInfo) obj;
            gwc.createLayer(lgInfo);
        }
    }

    /**
     * 
     * @see org.geoserver.catalog.event.CatalogListener#handleRemoveEvent(org.geoserver.catalog.event.CatalogRemoveEvent)
     * @see GWC#removeLayer(String)
     */
    public void handleRemoveEvent(CatalogRemoveEvent event) throws CatalogException {
        Object obj = event.getSource();

        String prefixedName = null;

        if (obj instanceof LayerGroupInfo) {
            LayerGroupInfo lgInfo = (LayerGroupInfo) obj;
            prefixedName = lgInfo.getName();
        } else if (obj instanceof LayerInfo) {
            LayerInfo layerInfo = (LayerInfo) obj;
            prefixedName = layerInfo.getResource().getPrefixedName();
        }

        if (null != prefixedName) {
            gwc.removeLayer(prefixedName);
        }
    }

    /**
     * 
     * @see org.geoserver.catalog.event.CatalogListener#reloaded()
     */
    public void reloaded() {
        gwc.reload();
    }

}
