/** 
 * Copyright (c) 2001 - 2009 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 * 
 * @author Arne Kepp / OpenGeo
 */
package org.geoserver.gwc;

import java.util.List;
import java.util.logging.Logger;

import org.geoserver.catalog.Catalog;
import org.geoserver.catalog.CatalogException;
import org.geoserver.catalog.LayerGroupInfo;
import org.geoserver.catalog.LayerInfo;
import org.geoserver.catalog.StyleInfo;
import org.geoserver.catalog.event.CatalogAddEvent;
import org.geoserver.catalog.event.CatalogListener;
import org.geoserver.catalog.event.CatalogModifyEvent;
import org.geoserver.catalog.event.CatalogPostModifyEvent;
import org.geoserver.catalog.event.CatalogRemoveEvent;
import org.geotools.util.logging.Logging;

/**
 * A {@link CatalogListener} that keeps the GeoServer layers in sync with its GeoWebCache
 * counterparts.
 * 
 * @author Arne Kepp
 * @author Gabriel Roldan
 */
public class GWCCatalogListener implements CatalogListener {

    private static Logger log = Logging.getLogger("org.geoserver.gwc.GWCCatalogListener");

    private final Catalog gsCatalog;

    private final GWC gwc;

    public GWCCatalogListener(final Catalog cat, final GWC gwc) {

        this.gsCatalog = cat;
        this.gwc = gwc;

        cat.addListener(this);

        log.fine("GWCCatalogListener registered with catalog");
    }

    /**
     * @see org.geoserver.catalog.event.CatalogListener#handleAddEvent(org.geoserver.catalog.event.CatalogAddEvent)
     */
    public void handleAddEvent(CatalogAddEvent event) throws CatalogException {
        Object obj = event.getSource();
        log.finer("Handling add event: " + obj);
        // We only handle layers here. Layer groups are initially empty
        if (obj instanceof LayerInfo) {
            LayerInfo layerInfo = (LayerInfo) obj;
            gwc.createLayer(layerInfo);
        } else if (obj instanceof LayerGroupInfo) {
            LayerGroupInfo lgi = (LayerGroupInfo) obj;
            gwc.createLayer(lgi);
        }
    }

    /**
     * 
     * @see org.geoserver.catalog.event.CatalogListener#handleModifyEvent(org.geoserver.catalog.event.CatalogModifyEvent)
     */
    public void handleModifyEvent(CatalogModifyEvent event) throws CatalogException {
        // Not dealing with this one just yet
    }

    /**
     * 
     * @see org.geoserver.catalog.event.CatalogListener#handlePostModifyEvent(org.geoserver.catalog.event.CatalogPostModifyEvent)
     */
    public void handlePostModifyEvent(CatalogPostModifyEvent event) throws CatalogException {
        Object obj = event.getSource();
        log.finer("Handling modify event for " + obj);
        if (obj instanceof StyleInfo) {
            StyleInfo si = (StyleInfo) obj;
            handleStyleChange(si);
        } else if (obj instanceof LayerInfo) {
            LayerInfo li = (LayerInfo) obj;
            gwc.createLayer(li);
        } else if (obj instanceof LayerGroupInfo) {
            LayerGroupInfo lgInfo = (LayerGroupInfo) obj;
            gwc.createLayer(lgInfo);
        }

    }

    /**
     * Options are:
     * <ul>
     * <li>A {@link LayerInfo} has {@code modifiedStyle} as either its default or style or as one of
     * its alternate styles
     * <li>A {@link LayerGroupInfo} contains a layer using {@code modifiedStyle}
     * <li>{@code modifiedStyle} is explicitly assigned to a {@link LayerGroupInfo}
     * </ul>
     * 
     * @param modifiedStyle
     */
    private void handleStyleChange(final StyleInfo modifiedStyle) {
        final String styleName = modifiedStyle.getName();
        log.finer("Handling style modification: " + styleName);
        // First we collect all the layers that use this style
        for (LayerInfo affectedLayer : gsCatalog.getLayers(modifiedStyle)) {
            String prefixedName = affectedLayer.getResource().getPrefixedName();
            log.info("Truncating layer '" + prefixedName + "' due to a change in style '"
                    + styleName + "'");
            gwc.truncate(prefixedName, styleName);
        }

        // Now we check for layer groups that are affected
        for (LayerGroupInfo layerGroup : gsCatalog.getLayerGroups()) {

            final List<StyleInfo> explicitLayerGroupStyles = layerGroup.getStyles();
            final List<LayerInfo> groupLayers = layerGroup.getLayers();

            for (int layerN = 0; layerN < groupLayers.size(); layerN++) {

                LayerInfo childLayer = groupLayers.get(layerN);
                StyleInfo assignedLayerStyle = explicitLayerGroupStyles.get(layerN);
                if (assignedLayerStyle == null) {
                    assignedLayerStyle = childLayer.getDefaultStyle();
                }

                if (modifiedStyle.equals(assignedLayerStyle)) {
                    String layerGroupName = layerGroup.getName();
                    log.fine("Truncating layer group '" + layerGroupName
                            + "' due to a change in style '" + styleName + "'");
                    gwc.truncate(layerGroupName);
                    break;
                }
            }

        }
    }

    /**
     * 
     * @see org.geoserver.catalog.event.CatalogListener#handleRemoveEvent(org.geoserver.catalog.event.CatalogRemoveEvent)
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
