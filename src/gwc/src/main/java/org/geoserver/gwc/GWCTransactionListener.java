/** 
 * Copyright (c) 2001 - 2009 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 * 
 * @author Arne Kepp / OpenGeo
 */
package org.geoserver.gwc;

import static org.geoserver.wfs.TransactionEventType.POST_UPDATE;
import static org.geoserver.wfs.TransactionEventType.PRE_DELETE;
import static org.geoserver.wfs.TransactionEventType.PRE_INSERT;
import static org.geoserver.wfs.TransactionEventType.PRE_UPDATE;

import java.util.HashSet;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.logging.Logger;

import javax.xml.namespace.QName;

import net.opengis.wfs.UpdateElementType;

import org.apache.commons.collections.map.LRUMap;
import org.geoserver.catalog.Catalog;
import org.geoserver.catalog.LayerGroupInfo;
import org.geoserver.catalog.LayerInfo;
import org.geoserver.catalog.NamespaceInfo;
import org.geoserver.wfs.TransactionEvent;
import org.geoserver.wfs.TransactionEventType;
import org.geoserver.wfs.TransactionListener;
import org.geoserver.wfs.UpdateElementHandler;
import org.geoserver.wfs.WFSException;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.util.logging.Logging;

/**
 * Listens to transactions (so far only issued by WFS) and truncates the cache for the affected area
 * of the layers involved in the transaction.
 * 
 * @author Arne Kepp
 * @author Gabriel Roldan
 * @version $Id$
 * 
 */
public class GWCTransactionListener implements TransactionListener {

    private static Logger log = Logging.getLogger("org.geoserver.gwc.GWCTransactionListener");

    final private Catalog cat;

    final private GWCCleanser cleanser;

    /**
     * Keeps track of the {@link TransactionEventType#PRE_UPDATE} affected bounds on a per
     * {@link UpdateElementType} request basis, so that the {@code POST_UPDATE} bounds are
     * aggregated to these ones before issuing a cache truncation.
     */
    private LRUMap updateBounds = new LRUMap();

    public GWCTransactionListener(final Catalog cat, final GWCCleanser cleanser) {
        this.cat = cat;
        this.cleanser = cleanser;
    }

    /**
     * <p>
     * Handles the following situations:
     * <ul>
     * </ul>
     * </p>
     * 
     * @see org.geoserver.wfs.TransactionListener#dataStoreChange(org.geoserver.wfs.TransactionEvent)
     */
    public void dataStoreChange(final TransactionEvent event) throws WFSException {

        final Set<String> affectedLayers = findAffectedCachedLayers(event);
        if (affectedLayers.isEmpty()) {
            // event didn't touch a cached layer
            return;
        }

        final TransactionEventType type = event.getType();

        final SimpleFeatureCollection affectedFeatures = event.getAffectedFeatures();

        if (PRE_INSERT == type || PRE_UPDATE == type || PRE_DELETE == type) {
            ReferencedEnvelope preBounds = affectedFeatures.getBounds();
        } else if (POST_UPDATE == type) {
            ReferencedEnvelope postBounds = affectedFeatures.getBounds();
        } else {
            // there exist no POST_DELETE, and POST_INSERT is never used
            throw new IllegalArgumentException("Unrecognized transaction event type: " + type);
        }

        for (String layerName : affectedLayers) {
            cleanser.deleteLayer(layerName);
        }

    }

    /**
     * Finds out which cached layers are affected by the given transaction event and returns their
     * names, or an empty set if no cached layer is affected by the transaction.
     * <p>
     * NOTE: so far it will always include a plain layer and any LayerGroup the layer is part of,
     * since the geoserver/gwc integration works by automatically making all geoserver layers
     * cacheable. But this might change in the near future, having the options to opt-out of caching
     * on a per layer basis, so beware this method may need to get smarter.
     * </p>
     */
    private Set<String> findAffectedCachedLayers(final TransactionEvent event) {

        final String layerName = getQualifiedLayerName(event);

        Set<String> affectedLayers = findLayerGroupsOf(layerName);

        affectedLayers.add(layerName);

        return affectedLayers;
    }

    private Set<String> findLayerGroupsOf(String layerName) {
        Set<String> affectedLayerGroups = new HashSet<String>();

        for (LayerGroupInfo lgi : cat.getLayerGroups()) {
            for (LayerInfo li : lgi.getLayers()) {
                if (li.getResource().getPrefixedName().equals(layerName)) {
                    affectedLayerGroups.add(lgi.getName());
                    break;
                }
            }
        }

        return affectedLayerGroups;
    }

    private String getQualifiedLayerName(final TransactionEvent event) {
        final String layerName;

        QName name = event.getLayerName();
        String namespaceURI = name.getNamespaceURI();
        NamespaceInfo namespaceInfo = cat.getNamespaceByURI(namespaceURI);
        if (namespaceInfo == null) {
            log.info("Can't find namespace info for layer " + name + ". Cache not truncated");
            throw new NoSuchElementException("Layer not found: " + name);
        }
        String prefix = namespaceInfo.getPrefix();
        layerName = prefix + ":" + name.getLocalPart();

        return layerName;
    }
}
