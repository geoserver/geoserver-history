/*
 * Copyright (c) 2001 - 2008 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */

package org.geoserver.test;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import net.opengis.wfs.FeatureCollectionType;

import org.geoserver.platform.Operation;
import org.geoserver.platform.ServiceException;
import org.geoserver.wfs.WFSGetFeatureOutputFormat;
import org.geoserver.wfs.xml.v1_0_0.WFSConfiguration;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.opengis.feature.Feature;
import org.opengis.feature.type.FeatureType;

/**
 * A WFS output format that returns a plain text response listing the type and id of each feature,
 * and a count of the total number of features. Useful for testing, particularly when there is no
 * working encoder configuration for the features, so they cannot be encoded into XML with the
 * standard {@link WFSConfiguration}.
 * 
 * @author Ben Caradoc-Davies, CSIRO Exploration and Mining
 * @see <a
 *      href="http://geoserver.org/display/GEOSDOC/Creating+a+WFS+Output+Format">Creating&nbsp;a&nbsp;WFS&nbsp;Output&nbsp;Format</a>
 *      in the GeoServer developer tutorials.
 */
public class TextFeatureListOutputFormat extends WFSGetFeatureOutputFormat {

    /**
     * Constructor.
     */
    public TextFeatureListOutputFormat() {
        super("text-feature-list");
    }

    /**
     * Return response MIME type.
     * 
     * @see org.geoserver.wfs.WFSGetFeatureOutputFormat#getMimeType(java.lang.Object,
     *      org.geoserver.platform.Operation)
     */
    @Override
    public String getMimeType(Object value, Operation operation) throws ServiceException {
        return "text/plain";
    }

    /**
     * Write the WFS response body.
     * 
     * @see org.geoserver.wfs.WFSGetFeatureOutputFormat#write(net.opengis.wfs.FeatureCollectionType,
     *      java.io.OutputStream, org.geoserver.platform.Operation)
     */
    @SuppressWarnings("unchecked")
    @Override
    protected void write(FeatureCollectionType featureCollection, OutputStream output,
            Operation getFeature) throws IOException, ServiceException {
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(output));
        FeatureCollection<FeatureType, Feature> collection = (FeatureCollection<FeatureType, Feature>) featureCollection
                .getFeature().get(0);
        FeatureIterator<Feature> iterator = collection.features();
        try {
            int count = 0;
            while (iterator.hasNext()) {
                Feature feature = iterator.next();
                writer.write("type = ");
                writer.write(feature.getType().getName().getURI());
                writer.write(", id = ");
                writer.write(feature.getIdentifier().getID());
                writer.write("\r\n");
                count++;
            }
            writer.write("count = " + count);
            writer.write("\r\n");
        } finally {
            collection.close(iterator);
        }
        writer.flush();
    }

}
