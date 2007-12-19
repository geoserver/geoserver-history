/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.feature;

import com.vividsolutions.jts.geom.Envelope;
import org.geotools.data.FeatureSource;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Set of utility methods for {@link org.geotools.data.FeatureSource}.
 *
 * @author Justin Deoliveira, The Open Planning Project, jdeolive@openplans.org
 *
 */
public class FeatureSourceUtils {
    protected static final Logger LOGGER = Logger.getLogger("org.vfny.geoserver.feature");

    /**
     * Retreives the bounds for a feature source.
     * <p>
     * If the feautre source can calculate the bounds directly, those bounds
     * are returned. Otherwise, the underlying feature collection is retreived
     * and asked to calculate bounds. If that fails, an empty envelope is
     * returned.
     * </p>
     *
     * @param fs The feature source.
     *
     * @return The bounds.
     *
     * @throws IOException Execption calculating bounds on feature source.
     */
    public static Envelope getBoundingBoxEnvelope(FeatureSource fs)
        throws IOException {
        Envelope ev = fs.getBounds();

        if ((ev == null) || ev.isNull()) {
            try {
                ev = fs.getFeatures().getBounds();
            } catch (Throwable t) {
                LOGGER.log(Level.FINE,
                    "Could not compute the data bounding box. Returning an empty envelope", t);
                ev = new Envelope();
            }
        }

        return ev;
    }
}
