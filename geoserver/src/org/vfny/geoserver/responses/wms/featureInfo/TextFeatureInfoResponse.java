/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.responses.wms.featureInfo;

import com.vividsolutions.jts.geom.Geometry;
import org.geotools.data.FeatureReader;
import org.geotools.data.FeatureResults;
import org.geotools.feature.AttributeType;
import org.geotools.feature.Feature;
import org.geotools.feature.FeatureType;
import org.geotools.feature.IllegalAttributeException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * Generates a FeatureInfoResponse of type text.  This simply reports the
 * attributes of the feature requested as a text string.  This class just
 * performs the writeTo, the GetFeatureInfoDelegate and abstract feature info
 * class handle the rest.
 *
 * @author James Macgill, PSU
 * @version $Id: TextFeatureInfoResponse.java,v 1.3 2004/07/19 22:31:40 jmacgill Exp $
 */
public class TextFeatureInfoResponse extends AbstractFeatureInfoResponse {
    /**
     *
     */
    public TextFeatureInfoResponse() {
        format = "text/plain";
        supportedFormats = Collections.singletonList("text/plain");
    }

    /**
     * Writes the feature information to the client in text/plain format.
     *
     * @param out The output stream to write to.
     *
     * @throws org.vfny.geoserver.ServiceException DOCUMENT ME!
     * @throws java.io.IOException DOCUMENT ME!
     */
    public void writeTo(OutputStream out)
        throws org.vfny.geoserver.ServiceException, java.io.IOException {
        Charset charSet = getRequest().getGeoServer().getCharSet();
        OutputStreamWriter osw = new OutputStreamWriter(out, charSet);

        //                                 getRequest().getGeoServer().getCharSet());
        PrintWriter writer = new PrintWriter(osw);

        try {
            for (int i = 0; i < results.size(); i++) {
                FeatureResults fr = (FeatureResults) results.get(i);

                FeatureReader reader = fr.reader();

                while (reader.hasNext()) {
                    Feature f = reader.next();

                    FeatureType schema = f.getFeatureType();
                    writer.println("Found " + fr.getCount() + " in "
                        + schema.getTypeName());

                    AttributeType[] types = schema.getAttributeTypes();
                    writer.println("------");

                    for (int j = 0; j < types.length; j++) {
                        if (Geometry.class.isAssignableFrom(types[j].getType())) {
                            writer.println(types[j].getName() + " = [GEOMETRY]");
                        } else {
                            writer.println(types[j].getName() + " = "
                                + f.getAttribute(types[j].getName()));
                        }
                    }
                }
            }
        } catch (IllegalAttributeException ife) {
            writer.println("Unable to generate information " + ife);
        }

        writer.flush();
    }
}
