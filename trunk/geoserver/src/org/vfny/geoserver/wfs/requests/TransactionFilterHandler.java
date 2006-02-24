/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wfs.requests;

import java.util.logging.Logger;

import org.geotools.feature.Feature;
import org.geotools.feature.FeatureType;
import org.geotools.filter.FilterFilter;
import org.geotools.filter.FilterHandler;
import org.geotools.gml.GMLHandlerFeature;

import com.vividsolutions.jts.geom.Geometry;


/**
 * Uses SAX to extact a Transactional request from and incoming XML stream.
 * This class handles what should be done with geometries, if they should be
 * passed to become part of a Feature, or if they should be an attribute in
 * the Filter.  These set of classes are fairly clunky and could probably be
 * rewritten in geotools.
 *
 * @author Chris Holmes, TOPP
 * @version $Id: TransactionFilterHandler.java,v 1.6 2004/02/09 23:29:40 dmzwiers Exp $
 */
public class TransactionFilterHandler extends FilterFilter
    implements GMLHandlerFeature {
    /** Class logger */
    private static Logger LOGGER = Logger.getLogger(
            "org.vfny.geoserver.requests.wfs");
    private TransactionHandler parent;

    /**
     * Empty constructor.
     *
     * @param parent The handler to pass filters and features to.
     * @param schema The schema (not used, this needs to be redone in geotools)
     */
    public TransactionFilterHandler(TransactionHandler parent,
        FeatureType schema) {
        super((FilterHandler) parent, schema);
        this.parent = parent;
    }

    /**
     * Recieves the geometry from a child, and either passes it on to
     * FilterFilter to add to the filter, or if not in a filter then it goes
     * to the transaction handler.
     *
     * @param geometry called by a child filter when it can not deal with it.
     *
     * @task REVISIT: This whole transaction handling is quite messy with
     *       geometries possible in property, filter, or feature.  We may want
     *       to re-architect this, something with a more flexible dynamic
     *       handler, that creates subhandlers as it needs them, instead of
     *       each trying to figure out if it can deal with what the subhandler
     *       passes up.
     */
    public void geometry(Geometry geometry) {
        LOGGER.finest("filter handler got geometry");

        if (insideFilter) {
            LOGGER.finest("sending to filterfilter " + geometry);
            super.geometry(geometry);
        } else {
            LOGGER.finest("sending to transaction " + geometry);
            parent.geometry(geometry);
        }
    }

    /**
     * Recieves the feature for an insert request.  Just passes it up to the
     * transaction handler.
     *
     * @param feature The feature read by child parsers.
     */
    public void feature(Feature feature) {
        LOGGER.finer("sending feature to transaction " + feature);
        this.parent.feature(feature);
    }
}
