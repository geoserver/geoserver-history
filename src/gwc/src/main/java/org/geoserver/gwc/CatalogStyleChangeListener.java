/** 
 * Copyright (c) 2001 - 2009 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 * 
 * @author Arne Kepp / OpenGeo
 */
package org.geoserver.gwc;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import org.geoserver.catalog.Catalog;
import org.geoserver.catalog.CatalogException;
import org.geoserver.catalog.CatalogInfo;
import org.geoserver.catalog.LayerGroupInfo;
import org.geoserver.catalog.LayerInfo;
import org.geoserver.catalog.StyleInfo;
import org.geoserver.catalog.event.CatalogAddEvent;
import org.geoserver.catalog.event.CatalogListener;
import org.geoserver.catalog.event.CatalogModifyEvent;
import org.geoserver.catalog.event.CatalogPostModifyEvent;
import org.geoserver.catalog.event.CatalogRemoveEvent;
import org.geoserver.gwc.layer.GeoServerTileLayer;
import org.geoserver.gwc.layer.GeoServerTileLayerInfo;
import org.geotools.util.logging.Logging;
import org.geowebcache.filter.parameters.ParameterFilter;

/**
 * Listens to changes in {@link StyleInfo styles} for the GeoServer {@link Catalog} and applies the
 * needed {@link ParameterFilter} changes to the corresponding {@link GeoServerTileLayer}.
 * 
 * @author Arne Kepp
 * @author Gabriel Roldan
 */
public class CatalogStyleChangeListener implements CatalogListener {

    private static Logger log = Logging.getLogger(CatalogStyleChangeListener.class);

    private final GWC gwc;

    public CatalogStyleChangeListener(final GWC gwc) {
        this.gwc = gwc;
    }

    /**
     * @see org.geoserver.catalog.event.CatalogListener#handleAddEvent(org.geoserver.catalog.event.CatalogAddEvent)
     */
    public void handleAddEvent(CatalogAddEvent event) throws CatalogException {
        // no need to handle style additions, they are added before being attached to a layerinfo
    }

    /**
     * 
     * @see org.geoserver.catalog.event.CatalogListener#handleModifyEvent(org.geoserver.catalog.event.CatalogModifyEvent)
     */
    public void handleModifyEvent(CatalogModifyEvent event) throws CatalogException {
        CatalogInfo source = event.getSource();
        if (!(source instanceof StyleInfo)) {
            return;
        }
        List<String> propertyNames = event.getPropertyNames();
        if (propertyNames.contains("name")) {
            final int index = propertyNames.indexOf("name");
            final String oldName = (String) event.getOldValues().get(index);
            final String newName = (String) event.getNewValues().get(index);
            if (oldName.equals(newName)) {
                return;
            }
            List<GeoServerTileLayer> affectedLayers = gwc.getTileLayersForStyle(oldName);
            for (GeoServerTileLayer tl : affectedLayers) {
                GeoServerTileLayerInfo info = tl.getInfo();
                if (oldName.equals(info.getDefaultStyle())) {
                    // easy, default style has no parameter id
                    info.setDefaultStyle(newName);
                }
                Set<String> styleNames = new HashSet<String>(info.getCachedStyles());
                if (styleNames.contains(oldName)) {
                    // pity, we don't have a way to just rename a style in GWC
                    gwc.truncate(tl.getName(), oldName);
                    styleNames.remove(oldName);
                    styleNames.add(newName);
                }
                if (styleNames.size() > 0) {
                    String defaultStyle = info.getDefaultStyle();
                    ParameterFilter paramFilter = GeoServerTileLayer.createStylesParameterFilters(
                            defaultStyle, styleNames);
                    info.setCachedStyles(styleNames);
                    tl.setParameterFilters(Collections.singletonList(paramFilter));
                }else{
                    tl.setParameterFilters(null);
                }
                gwc.save(tl);
            }
        }
    }

    /**
     * Truncates all tile sets referring the modified {@link StyleInfo}
     * 
     * @see org.geoserver.catalog.event.CatalogListener#handlePostModifyEvent
     */
    public void handlePostModifyEvent(CatalogPostModifyEvent event) throws CatalogException {
        Object obj = event.getSource();
        if (obj instanceof StyleInfo) {
            StyleInfo si = (StyleInfo) obj;
            handleStyleChange(si);
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
        for (LayerInfo affectedLayer : gwc.getLayersInfosFor(modifiedStyle)) {
            // If the style name changes, we need to update the layer's parameter filter
            String prefixedName = affectedLayer.getResource().getPrefixedName();
            log.info("Truncating layer '" + prefixedName + "' due to a change in style '"
                    + styleName + "'");
            gwc.truncate(prefixedName, styleName);
        }

        // Now we check for layer groups that are affected
        for (LayerGroupInfo layerGroup : gwc.getLayerGroupsFor(modifiedStyle)) {
            String layerGroupName = layerGroup.getName();
            log.info("Truncating layer group '" + layerGroupName + "' due to a change in style '"
                    + styleName + "'");
            gwc.truncate(layerGroupName);
        }
    }

    /**
     * No need to do anything here, when a style is removed all the layers that reference it are
     * updated first
     * 
     * @see org.geoserver.catalog.event.CatalogListener#handleRemoveEvent
     */
    public void handleRemoveEvent(CatalogRemoveEvent event) throws CatalogException {
    }

    /**
     * 
     */
    public void reloaded() {
    }

}
