/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.responses.wms.featureInfo;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.geotools.data.FeatureReader;
import org.geotools.data.FeatureResults;
import org.geotools.feature.AttributeType;
import org.geotools.feature.Feature;
import org.geotools.feature.FeatureType;
import org.geotools.feature.IllegalAttributeException;

import com.vividsolutions.jts.geom.Geometry;


/**
 * Generates a map using the geotools jai rendering classes.  Uses the Lite
 * renderer, loading the data on the fly, which is quite nice.  Thanks Andrea
 * and Gabriel.  The word is that we should eventually switch over to
 * StyledMapRenderer and do some fancy stuff with caching layers, but  I think
 * we are a ways off with its maturity to try that yet.  So Lite treats us
 * quite well, as it is stateless and therefor loads up nice and fast.
 *
 * @author Chris Holmes, TOPP
 * @version $Id: TextFeatureInfoResponse.java,v 1.3 2004/07/19 22:31:40 jmacgill Exp $
 */
public class TextFeatureInfoResponse extends AbstractFeatureInfoResponse {
    
    /**
     *
     */
    public TextFeatureInfoResponse() {
        format = "text/plain";
        supportedFormats = new ArrayList();
        supportedFormats.add(format);
    }
    
    /**
     * Evaluates if this Map producer can generate the map format specified by
     * <code>mapFormat</code>
     *
     * @param mapFormat the mime type of the output map format requiered
     *
     * @return true if class can produce a map in the passed format
     */
    public boolean canProduce(String mapFormat) {
        return getSupportedFormats().contains(mapFormat);
    }
    
    public List getSupportedFormats(){        
        if (supportedFormats == null) {
            supportedFormats = Collections.singletonList("text/plain");
        }
        return supportedFormats;
    }
    
    /**
     * Writes the image to the client.
     *
     * @param out The output stream to write to.
     *
     * @throws org.vfny.geoserver.ServiceException DOCUMENT ME!
     * @throws java.io.IOException DOCUMENT ME!
     */
    public void writeTo(OutputStream out)
    throws org.vfny.geoserver.ServiceException, java.io.IOException {
        PrintWriter writer = new PrintWriter(out);
        try {
            for(int i = 0; i < results.size(); i++){
                FeatureResults fr = (FeatureResults)results.get(i);
                
                FeatureReader reader = fr.reader();
                while(reader.hasNext()){
                    Feature f = reader.next();
                    
                    FeatureType schema = f.getFeatureType();
                    writer.println("Found " + fr.getCount() + " in " + schema.getTypeName());
                    AttributeType[] types = schema.getAttributeTypes();
                    writer.println("------");
                    
                    for (int j = 0; j < types.length; j++) {
                        if (Geometry.class.isAssignableFrom(types[j].getType())) {
                            writer.println(types[j].getName() + " = [GEOMETRY]");
                        } else {
                            writer.println(types[j].getName() + " = " +
                            f.getAttribute(types[j].getName()));
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
