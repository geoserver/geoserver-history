/* Copyright (c) 2001 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root 
 * application directory.
 */
package org.vfny.geoserver.requests;

import java.io.*;
import java.util.*;
import java.util.logging.Logger;
import org.xml.sax.ContentHandler;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.XMLFilterImpl;

/**
 * Uses SAX to extact a DescribeFeatureType query from and incoming GetFeature 
 * request XML stream.
 *
 * <p>Note that this Handler extension ignores Filters completely and must be 
 * chained as a parent to the PredicateFilter method in order to recognize 
 * them.  If it is not chained, it will still generate valid queries, but 
 * with no filtering whatsoever.</p>
 * 
 * @author Rob Hranac, TOPP
 * @version $VERSION$
 */
class DescribeHandler 
    extends XMLFilterImpl 
    implements ContentHandler {

    /** Class logger */
    private static Logger LOGGER = 
        Logger.getLogger("org.vfny.geoserver.requests");

    /** Internal GetCapabilities request for construction. */
    private DescribeRequest request = 
        new DescribeRequest();    

    /** Local variable to track current tag */
    private String currentTag = new String();
    
    
    /**
     * Returns the GetCapabilities request.
     * @returns GetCapabilities request.
     */ 
    public DescribeRequest getRequest() { return request; }

    /*************************************************************************
     *  Standard SAX content handler methods                                 *
     *************************************************************************/
    /**
     * Notes the start of the element and sets the current tag.
     *
     * @param namespaceURI URI for namespace appended to element.
     * @param localName Local name of element.
     * @param rawName Raw name of element.
     * @param atts Element attributes.
     */ 
    public void startElement(String namespaceURI, 
                             String localName, 
                             String rawName, 
                             Attributes atts)
            throws SAXException {
            
        LOGGER.finest("found start element: " + localName);
        currentTag = localName;
	if(currentTag.equals("DescribeFeatureType")) {
            for(int i = 0; i < atts.getLength(); i++) {
                if( atts.getLocalName(i).equals("outputFormat") ) {
                    LOGGER.finest("found outputFormat: " + atts.getValue(i));
                    request.setOutputFormat(atts.getValue(i));
                }
            }
	}
    }


    /**
     * Notes the end of the element and sets the current tag.
     *
     * @param namespaceURI URI for namespace appended to element.
     * @param localName Local name of element.
     * @param rawName Raw name of element.
     */ 
    public void endElement(String namespaceURI, 
                           String localName, 
                           String rawName)
        throws SAXException {
        
        LOGGER.finest("found end element: " + localName);
        currentTag = "";
    }
    

    /**
     * Checks if inside type name and adds to feature type list, if so.
     *
     * @param ch URI for namespace appended to element.
     * @param start Local name of element.
     * @param length Raw name of element.
     */ 
    public void characters(char[] ch, int start, int length)
                throws SAXException {
        
        String s = new String(ch, start, length);
        if(currentTag.equals("TypeName")) {
            request.addFeatureType(s);
            LOGGER.finest("added type name: " + s);
        }
    }
}
