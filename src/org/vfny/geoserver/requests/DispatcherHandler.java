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
import org.xml.sax.Locator;
import org.xml.sax.helpers.XMLFilterImpl;
import org.geotools.filter.Filter;
import org.geotools.filter.FilterHandler;
import org.vfny.geoserver.responses.WfsException;

/**
 * Uses SAX to extact a GetFeature query from and incoming GetFeature request 
 * XML stream.
 *
 * <p>Note that this Handler extension ignores Filters completely and must be 
 * chained as a parent to the PredicateFilter method in order to recognize 
 * them.  If it is not chained, it will still generate valid queries, but with 
 * no filtering whatsoever.</p>
 * 
 *@author Rob Hranac, TOPP
 *@version $Version$
 */
public class DispatcherHandler implements ContentHandler {


    /** Class logger */
    private static Logger LOGGER = 
        Logger.getLogger("org.vfny.geoserver.requests");

    /** Stores internal request type */
    private int requestType = 1;
    
    /** Flags whether or not type has been set */
    private boolean gotType = false;
    
    /** Map get capabilities request type */
    public static final int GET_CAPABILITIES_REQUEST = 1;
    
    /** Map describe feature type request type */
    public static final int DESCRIBE_FEATURE_TYPE_REQUEST = 2;
    
    /** Map get feature request type */
    public static final int GET_FEATURE_REQUEST = 3;
    
    /** Map unknown request type */
    public static final int UNKNOWN = -1;
    
    
    public int getRequestType() {
        return requestType;
    }
    
    
    /*********************************************
         Start of SAX Content Handler Methods
          most of these are unused at the moment
          no namespace awareness, yet
    *********************************************/
    
    /** Notes the document locator. */ 
    public void setDocumentLocator (Locator locator) {}
    
    
    /** Notes the start of the document.    */ 
    public void startDocument()
        throws SAXException {
    }
    
    
    /** Notes the end of the document.  */ 
    public void endDocument()
                throws SAXException {
    }
    
    
    /** Notes processing instructions.  */ 
    public void processingInstruction(String target, String data)
                throws SAXException {
    }
    
    
    /** Notes start of prefix mappings. */ 
    public void startPrefixMapping(String prefix, String uri) {
    }
    
    
    /** Notes end of prefix mappings. */ 
    public void endPrefixMapping(String prefix) {
    }
    
    
    /**
     * Notes the start of the element and checks for request type.
     *
     * @param namespaceURI URI for namespace appended to element.
     * @param localName Local name of element.
     * @param rawName Raw name of element.
     * @param atts Element attributes.
     */ 
    public void startElement(String namespaceURI, String localName, String rawName, Attributes atts)
        throws SAXException {
        
        //_log.info("got to start element: " + localName);
        
        if ( !gotType ) {
            // if at a query element, empty the current query, set insideQuery flag, and get query typeNames
            if( localName.equals("GetCapabilities") ) {
                this.requestType = GET_CAPABILITIES_REQUEST;
            }
            else if( localName.equals("DescribeFeatureType") ) {
                this.requestType = DESCRIBE_FEATURE_TYPE_REQUEST;
            }
            else if( localName.equals("GetFeature") ) {
                this.requestType = GET_FEATURE_REQUEST;
            }
            else {
                this.requestType = UNKNOWN;
            }
        }
        
        gotType = true;
    }
    
    
    /**
     * Notes the end of the element.
     *
     * @param namespaceURI URI for namespace appended to element.
     * @param localName Local name of element.
     * @param rawName Raw name of element.
     */ 
    public void endElement(String namespaceURI, String localName, String rawName)
        throws SAXException {
    }
    
    
     /**
      * Notes presence of a character sequence.
      *
      * @param ch URI for namespace appended to element.
      * @param start Local name of element.
      * @param length Raw name of element.
      */ 
    public void characters(char[] ch, int start, int length)
        throws SAXException {
    }
    
    
    /** Notes ignorable whitespace  */ 
    public void ignorableWhitespace(char[] ch, int start, int length)
        throws SAXException {
    }
    
    
    /** Notes a skipped entity. */ 
    public void skippedEntity(String name)
        throws SAXException {
    }    
}
