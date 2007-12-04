/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.wfs;

import javax.xml.namespace.QName;

import org.geotools.feature.FeatureCollection;


/**
 * Event carrying information about a change that happened/that is about to
 * occur. The feature collection may be an in-memory one, or may be based on a
 * real data store with a filter.
 */
public class TransactionEvent {
    private TransactionEventType type;
    private FeatureCollection affectedFeatures;
    private QName layerName;

    public TransactionEvent(TransactionEventType type, QName layerName, FeatureCollection affectedFeatures) {
        this.type = type;
        this.layerName = layerName;
        this.affectedFeatures = affectedFeatures;
    }

    /**
     * The type of change occurring
     */
    public TransactionEventType getType() {
        return type;
    }

    /**
     * A collection of the features that are being manipulated. Accessible and usable only
     * when the event is being thrown, if you store the event and try to access the collection later
     * there is no guarantee it will still be usable.
     */
    public FeatureCollection getAffectedFeatures() {
        return affectedFeatures;
    }
    
    public QName getLayerName() {
        return layerName;
    }
}
