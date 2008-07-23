/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.wfs;

import org.geotools.feature.FeatureCollection;


/**
 * Event carrying information about a change that happened/that is about to
 * occur. The feature collection may be an in-memory one, or may be based on a
 * real datastore with a filter.
 */
public class TransactionEvent {
    private TransactionEventType type;
    private FeatureCollection affectedFeatures;

    public TransactionEvent(TransactionEventType type, FeatureCollection affectedFeatures) {
        this.type = type;
        this.affectedFeatures = affectedFeatures;
    }

    /**
     * The type of change occurring
     */
    public TransactionEventType getType() {
        return type;
    }

    /**
     * A collection of the features that are being manipulated.
     */
    public FeatureCollection getAffectedFeatures() {
        return affectedFeatures;
    }
}
