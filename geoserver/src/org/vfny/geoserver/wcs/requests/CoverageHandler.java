/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wcs.requests;

import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.XMLFilterImpl;

import com.vividsolutions.jts.geom.Envelope;

/**
 * DOCUMENT ME!
 * 
 * @author $Author: Alessio Fabiani (alessio.fabiani@gmail.com) $ (last modification)
 * @author $Author: Simone Giannecchini (simboss_ml@tiscali.it) $ (last modification)
 */
public class CoverageHandler extends XMLFilterImpl implements ContentHandler {
    /** Class logger */
    private static Logger LOGGER = Logger.getLogger(
            "org.vfny.geoserver.requests.wcs");

    private CoverageRequest request = new CoverageRequest();

    private String currentTag = new String();
    
    private Double[] coordinates = new Double[4];

    /**
     * Empty constructor.
     */
    public CoverageHandler() {
        super();
    }

    public CoverageRequest getRequest(HttpServletRequest req) {
    	request.setHttpServletRequest(req);
        return request;
    }

    /**
     * Notes the start of the element and sets type names and query attributes.
     *
     * @param namespaceURI URI for namespace appended to element.
     * @param localName Local name of element.
     * @param rawName Raw name of element.
     * @param atts Element attributes.
     *
     * @throws SAXException When the XML is not well formed.
     */
    public void startElement(String namespaceURI, String localName,
        String rawName, Attributes atts) throws SAXException {
        LOGGER.finest("at start element: " + localName);

        // at start of element, set inside flag to whatever tag we are inside
        currentTag = localName;

      	if (currentTag.equals("GetCoverage")) {
            request = new CoverageRequest();

            for (int i = 0; i < atts.getLength(); i++) {
                String curAtt = atts.getLocalName(i);

                if (curAtt.equals("service")) {
                    request.setService(atts.getValue(i));
                } else if (curAtt.equals("version")) {
                    request.setVersion(atts.getValue(i));
                }
            }
        }
    }

    /**
     * Notes the end of the element exists query or bounding box.
     *
     * @param namespaceURI URI for namespace appended to element.
     * @param localName Local name of element.
     * @param rawName Raw name of element.
     *
     * @throws SAXException When the XML is not well formed.
     */
    public void endElement(String namespaceURI, String localName, String rawName)
        throws SAXException {
        LOGGER.finer("at end element: " + localName);
        currentTag = "";
    }

    /**
     * Checks if inside parsed element and adds its contents to the appropriate
     * variable.
     *
     * @param ch URI for namespace appended to element.
     * @param start Local name of element.
     * @param length Raw name of element.
     *
     * @throws SAXException When the XML is not well formed.
     */
    public void characters(char[] ch, int start, int length)
        throws SAXException {
        String s = new String(ch, start, length);

        // if inside a property element, add the element
        if (currentTag.equals("sourceCoverage")) {
            LOGGER.finest("found Coverage name: " + s);
            request.setCoverage(s);
        } else if (currentTag.equals("interpolationMethod")) {
            LOGGER.finest("found Interpolation Method: " + s);
            request.setInterpolation(s);
        } else if (currentTag.equals("format")) {
            LOGGER.finest("found Output Format: " + s);
            request.setOutputFormat(s);
        } else if (currentTag.equals("pos")) {
            LOGGER.finer("found Envelope Coordinate: " + s);
            if( coordinates[0] == null ) {
            	String[] coords = s.split(" ");
            	try {
            		double arg0 = Double.parseDouble(coords[0]);
            		double arg1 = Double.parseDouble(coords[1]);
            		
            		coordinates[0] = new Double(arg0);
            		coordinates[1] = new Double(arg1);
            	} catch(Exception e) {
            		coordinates[0] = null;
            		coordinates[1] = null;
            	}
            } else if ( coordinates[2] == null ) {
            	String[] coords = s.split(" ");
            	try {
            		double arg0 = Double.parseDouble(coords[0]);
            		double arg1 = Double.parseDouble(coords[1]);
            		
            		coordinates[2] = new Double(arg0);
            		coordinates[3] = new Double(arg1);

                    Envelope env = new Envelope(
                    		coordinates[0].doubleValue(), 
                    		coordinates[2].doubleValue(), 
                    		coordinates[1].doubleValue(), 
                    		coordinates[3].doubleValue());
                    request.setEnvelope(env);
            	} catch(Exception e) {
            		coordinates[2] = null;
            		coordinates[3] = null;
            	}
            }
        }
    }
}
