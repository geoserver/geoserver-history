/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.responses.wms.featureInfo;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Geometry;
import org.geotools.data.FeatureSource;
import org.geotools.data.Query;
import org.geotools.map.DefaultMapContext;
import org.geotools.map.DefaultMapLayer;
import org.geotools.map.MapContext;
import org.geotools.map.MapLayer;
import org.geotools.renderer.lite.LiteRenderer;
import org.geotools.styling.Style;
import org.vfny.geoserver.WmsException;
import org.vfny.geoserver.global.FeatureTypeInfo;
import org.vfny.geoserver.global.GeoServer;
import org.vfny.geoserver.requests.wms.GetMapRequest;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import org.geotools.data.FeatureReader;
import org.geotools.data.FeatureResults;
import org.geotools.data.FeatureSource;
import org.geotools.feature.AttributeType;
import org.geotools.feature.Feature;
import org.geotools.feature.FeatureType;
import org.geotools.feature.IllegalAttributeException;
import org.geotools.filter.AbstractFilter;
import org.geotools.filter.FilterFactory;
import org.geotools.filter.GeometryFilter;
import org.vfny.geoserver.requests.wms.GetFeatureInfoRequest;


/**
 * Generates a map using the geotools jai rendering classes.  Uses the Lite
 * renderer, loading the data on the fly, which is quite nice.  Thanks Andrea
 * and Gabriel.  The word is that we should eventually switch over to
 * StyledMapRenderer and do some fancy stuff with caching layers, but  I think
 * we are a ways off with its maturity to try that yet.  So Lite treats us
 * quite well, as it is stateless and therefor loads up nice and fast.
 *
 * @author Chris Holmes, TOPP
 * @version $Id: HTMLTableFeatureInfoResponse.java,v 1.1 2004/07/19 22:32:22 jmacgill Exp $
 */
public class HTMLTableFeatureInfoResponse extends AbstractFeatureInfoResponse {
    
    /**
     *
     */
    public HTMLTableFeatureInfoResponse() {
        format = "text/html";
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
        LOGGER.info("supported formats = " + supportedFormats);
        if (supportedFormats == null) {
            //LiteRenderer renderer = null;
            String[] mimeTypes = new String[]{"text/html"};
            supportedFormats = Arrays.asList(mimeTypes);
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
        writer.println("<html><body>");
        try {
            for(int i = 0; i < results.size(); i++){
                FeatureResults fr = (FeatureResults)results.get(i);
                FeatureType schema = fr.getSchema();
                
                writer.println("<table border='1'>");
                writer.println("<tr><th colspan="+ schema.getAttributeCount() +" scope='col'>" + schema.getTypeName() + " </th></tr>");
                writer.println("<tr>");
                for(int j = 0; j < schema.getAttributeCount(); j++){
                    writer.println("<td>" + schema.getAttributeType(j).getName() + "</td>");
                }
                writer.println("</tr>");
                
                
                //writer.println("Found " + fr.getCount() + " in " + schema.getTypeName());
                FeatureReader reader = fr.reader();
                
                while(reader.hasNext()){
                    Feature f = reader.next();
                    AttributeType[] types = schema.getAttributeTypes();
                    writer.println("<tr>");
                    for (int j = 0; j < types.length; j++) {
                        if (Geometry.class.isAssignableFrom(types[j].getType())) {
                            writer.println("<td>");
                            writer.println("[GEOMETRY]");
                            writer.println("</td>");
                        } else {
                            writer.println("<td>");
                            writer.print(f.getAttribute(types[j].getName()));
                            writer.println("</td>");
                        }
                    }
                    writer.println("</tr>");
                }
                writer.println("</table>");
                writer.println("<p>");
            }
        } catch (IllegalAttributeException ife) {
            writer.println("Unable to generate information " + ife);
        }
        
        writer.flush();
        
    }
    
    
    
    /**
     * returns the content encoding for the output data
     *
     * @return <code>null</code> since no special encoding is performed while
     *         wrtting to the output stream
     */
    public String getContentEncoding() {
        return format;
    }
    
}
