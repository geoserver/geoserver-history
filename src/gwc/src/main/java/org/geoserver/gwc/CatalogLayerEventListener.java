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
 * Listens to {@link Catalog}'s layer added/removed events and adds/removes
 * {@link GeoServerTileLayer}s to/from the {@link CatalogConfiguration}
 * 
 * @author Arne Kepp
 * @author Gabriel Roldan
 */
public class CatalogLayerEventListener implements CatalogListener {

    private static Logger log = Logging.getLogger(CatalogLayerEventListener.class);

    private final GWC gwc;

    /**
     * Holds the CatalogModifyEvent from {@link #handleModifyEvent} to be taken after the change was
     * applied to the {@link Catalog} at {@link #handlePostModifyEvent} and check whether it is
     * necessary to perform any action on the cache based on the changed properties
     */
    private static ThreadLocal<CatalogModifyEvent> PRE_MODIFY_EVENT = new ThreadLocal<CatalogModifyEvent>();

    public CatalogLayerEventListener(final GWC gwc) {
        this.gwc = gwc;
    }

    /**
     * If either a {@link LayerInfo} or {@link LayerGroupInfo} has been added to the {@link Catalog}
     * , create a corresponding GWC TileLayer.
     * 
     * @see org.geoserver.catalog.event.CatalogListener#handleAddEvent
     * @see GWC#createLayer(LayerInfo)
     * @see GWC#createLayer(LayerGroupInfo)
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
        CatalogInfo source = event.getSource();
        if (source instanceof LayerInfo) {
            PRE_MODIFY_EVENT.set(event);
        } else if (source instanceof LayerGroupInfo) {
            PRE_MODIFY_EVENT.set(event);
        }
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
    @SuppressWarnings("unchecked")
    public void handlePostModifyEvent(final CatalogPostModifyEvent event) throws CatalogException {
        final Object obj = event.getSource();
        if (!(obj instanceof LayerInfo || obj instanceof LayerGroupInfo)) {
            return;
        }

        final CatalogModifyEvent preModifyEvent = PRE_MODIFY_EVENT.get();
        if (preModifyEvent == null) {
            throw new IllegalStateException(
                    "PostModifyEvent called without having called handlePreModify first?");
        }
        PRE_MODIFY_EVENT.remove();

        final List<String> changedProperties = preModifyEvent.getPropertyNames();
        final List<Object> oldValues = preModifyEvent.getOldValues();
        final List<Object> newValues = preModifyEvent.getNewValues();

        log.finer("Handling modify event for " + obj);
        if (obj instanceof LayerInfo) {
            // REVISIT: what about truncating the LayerGroups containing the modified layer?
            // checking the style applies of course
            final LayerInfo li = (LayerInfo) obj;
            final String layerName = li.getResource().getPrefixedName();
            final GeoServerTileLayer tileLayer = gwc.getTileLayerById(li.getId());
            {
                final String oldLayerName = tileLayer.getName();
                if (!oldLayerName.equals(layerName)) {
                    gwc.renameTileLayer(oldLayerName, layerName);
                }
            }
            boolean save = false;
            GeoServerTileLayerInfo info = tileLayer.getInfo();
            if (changedProperties.contains("defaultStyle")) {
                final int propIndex = changedProperties.indexOf("defaultStyle");
                final StyleInfo oldStyle = (StyleInfo) oldValues.get(propIndex);
                final StyleInfo newStyle = (StyleInfo) newValues.get(propIndex);
                final String oldStyleName = oldStyle.getName();
                final String newStyleName = newStyle.getName();
                if (!oldStyleName.equals(newStyleName)) {
                    save = true;
                    gwc.truncate(layerName, oldStyleName);
                    info.setDefaultStyle(newStyleName);
                }
            }
            if (changedProperties.contains("styles")) {
                final int propIndex = changedProperties.indexOf("styles");
                final Set<StyleInfo> oldStyles = (Set<StyleInfo>) oldValues.get(propIndex);
                final Set<StyleInfo> currentStyles = (Set<StyleInfo>) newValues.get(propIndex);
                Set<String> newStyleSet = new HashSet<String>(info.getCachedStyles());
                if (!oldStyles.equals(currentStyles)) {
                    Set<StyleInfo> removed = new HashSet<StyleInfo>(oldStyles);
                    removed.removeAll(currentStyles);

                    // remove any style detacched from the layer
                    for (StyleInfo deletedStyle : removed) {
                        String styleName = deletedStyle.getName();
                        newStyleSet.remove(styleName);
                        gwc.truncate(layerName, styleName);
                    }
                    // add new cached styles if tilelayer is configured to do so
                    if (info.isAutoCacheStyles()) {
                        Set<StyleInfo> added = new HashSet<StyleInfo>(currentStyles);
                        added.removeAll(oldStyles);
                        for (StyleInfo addedStyle : added) {
                            String styleName = addedStyle.getName();
                            newStyleSet.add(styleName);
                        }
                    }
                }
                // prune any tangling style from info
                Set<String> currentStyleNames = new HashSet<String>();
                for(StyleInfo current : currentStyles){
                    currentStyleNames.add(current.getName());
                }
                newStyleSet.retainAll(currentStyleNames);
                // recreate parameter filters if need be
                if (!newStyleSet.equals(info.getCachedStyles())) {
                    save = true;
                    String defaultStyle = li.getDefaultStyle().getName();
                    info.setCachedStyles(newStyleSet);
                    ParameterFilter newStyleParamsFilter = null;
                    if (newStyleSet.size() > 0) {
                        newStyleParamsFilter = GeoServerTileLayer.createStylesParameterFilters(
                                defaultStyle, newStyleSet);
                    }
                    tileLayer.setParameterFilters(Collections.singletonList(newStyleParamsFilter));
                }
            }
            if (save) {
                gwc.save(tileLayer);
            }
        } else if (obj instanceof LayerGroupInfo) {
            LayerGroupInfo lgInfo = (LayerGroupInfo) obj;
            GeoServerTileLayer tileLayer = gwc.getTileLayerById(lgInfo.getId());
            final String oldName = tileLayer.getName();
            final String newName = lgInfo.getName();
            if (!oldName.equals(newName)) {
                gwc.renameTileLayer(oldName, newName);
            }
            if (changedProperties.contains("layers") || changedProperties.contains("styles")) {
                if (!oldValues.equals(newValues)) {
                    log.info("Truncating TileLayer for layer group '" + newName
                            + "' due to a change in its layers or styles");
                    String layerName = lgInfo.getName();
                    gwc.truncate(layerName);
                }
            }
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
