/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geotools.feature;

import java.io.IOException;

import org.geotools.data.DataSourceException;
import org.geotools.data.FeatureReader;
import org.geotools.data.FeatureResults;


/**
 * The FeatureCollectionIteration provides a depth first traversal of a
 * FeatureCollection which will call the provided call-back Handler. Because
 * of the complex nature of Features, which may have other Features (or even a
 * collection of Features) as attributes, the handler is repsonsible for
 * maintaining its own state as to where in the traversal it is recieving
 * events from. Many handlers will not need to worry about state.
 * 
 * <p>
 * <b>Implementation Notes:</b> The depth first visitation is implemented
 * through recursion. The limits to recursion depending on the settings in the
 * JVM, but some tests show a 2 argument recursive having a limit of ~50000
 * method calls with a stack size of 512k (the standard setting).
 * </p>
 *
 * @author Ian Schneider, USDA-ARS
 * @author Chris Holmes, TOPP
 * @author Gabriel Roldán, Dominion t.i.
 */
public class FeatureResultsIteration {
    /**
     * A callback handler for the iteration of the contents of a
     * FeatureCollection.
     */
    protected final Handler handler;

    /** The collection being iterated */
    private final FeatureResults results;

    /** DOCUMENT ME! */
    private final FeatureReader reader;

    /**
     * Create a new FeatureResultsIteration with the given handler and
     * collection.
     *
     * @param handler The handler to perform operations on this iteration.
     * @param features The collection to iterate over.
     *
     * @throws NullPointerException If handler or collection are null.
     * @throws IOException DOCUMENT ME!
     */
    public FeatureResultsIteration(Handler handler, FeatureResults features)
        throws NullPointerException, IOException {
        if (handler == null) {
            throw new NullPointerException("handler");
        }

        if (features == null) {
            throw new NullPointerException("collection");
        }

        this.handler = handler;
        this.results = features;
        this.reader = features.reader();
    }

    /**
     * A convienience method for obtaining a new iteration and calling iterate.
     *
     * @param handler The handler to perform operations on this iteration.
     * @param features The collection to iterate over.
     *
     * @throws IOException DOCUMENT ME!
     */
    public static void iteration(Handler handler, FeatureResults features)
        throws IOException {
        FeatureResultsIteration iteration = new FeatureResultsIteration(handler,
                features);
        iteration.iterate();
    }

    /**
     * Start the iteration.
     *
     * @throws IOException DOCUMENT ME!
     */
    public void iterate() throws IOException {
        walker(results, reader);
    }

    /**
     * Perform the iterative behavior on the given collection. This will alert
     * the handler with a <code>handleFeatureCollection</code> call, followed
     * by an <code> iterate()</code>, followed by a
     * <code>handler.endFeatureCollection()</code> call.
     *
     * @param results The collection to iterate upon.
     * @param reader DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     * @throws DataSourceException DOCUMENT ME!
     */
    protected void walker(FeatureResults results, FeatureReader reader)
        throws IOException {
        handler.handleFeatureResults(results, reader);

        try {
            iterate(reader);
        } catch (IllegalAttributeException ex) {
            throw new DataSourceException(ex.getMessage(), ex);
        }

        handler.endFeatureResults(results);
    }

    /**
     * Perform the actual iteration on the Iterator which is provided.
     *
     * @param reader The Iterator to iterate upon.
     *
     * @throws IOException DOCUMENT ME!
     * @throws IllegalAttributeException DOCUMENT ME!
     */
    protected void iterate(FeatureReader reader)
        throws IOException, IllegalAttributeException {
        while (reader.hasNext()) {
            walker((Feature) reader.next());
        }
    }

    /**
     * Perform the visitation of an individual Feature.
     *
     * @param feature The Feature to explore.
     *
     * @throws UnsupportedOperationException DOCUMENT ME!
     */
    protected void walker(Feature feature) {
        final FeatureType schema = feature.getFeatureType();
        final int cnt = schema.getAttributeCount();
        handler.handleFeature(feature);

        for (int i = 0; i < cnt; i++) {
            AttributeType type = schema.getAttributeType(i);

            // recurse if attribute type is another collection
            if (FeatureCollection.class.isAssignableFrom(type.getType())) {
                throw new UnsupportedOperationException(
                    "inner FeatureCollections not supported yet");

                //walker((FeatureCollection) feature.getAttribute(i));
            } else if (type.isNested()) {
                // recurse if attribute type is another feature
                walker((Feature) feature.getAttribute(i));
            } else {
                // normal handling
                handler.handleAttribute(type, feature.getAttribute(i));
            }
        }

        handler.endFeature(feature);
    }

    /**
     * A callback handler for the iteration of the contents of a
     * FeatureCollection.
     */
    public interface Handler {
        /**
         * The handler is visiting a FeatureResults.
         *
         * @param fr The currently visited FeatureResults.
         */
        void handleFeatureResults(FeatureResults fr, FeatureReader reader);

        /**
         * The handler is done visiting a FeatureResults
         *
         * @param fr The FeatureResults which was visited.
         */
        void endFeatureResults(FeatureResults fr);

        /**
         * The handler is visiting a Feature.
         *
         * @param f The Feature the handler is visiting.
         */
        void handleFeature(Feature f);

        /**
         * The handler is ending its visit with a Feature.
         *
         * @param f The Feature that was visited.
         */
        void endFeature(Feature f);

        /**
         * The handler is visiting an Attribute of a Feature.
         *
         * @param type The meta-data of the given attribute value.
         * @param value The attribute value, may be null.
         */
        void handleAttribute(AttributeType type, Object value);
    }
}
