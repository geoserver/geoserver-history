/* Copyright (c) 2002 Vision for New York - www.vfny.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root application directory.
 */

package org.vfny.geoserver.requests;

import java.io.*;
import java.util.*;
import org.xml.sax.*;
import org.xml.sax.helpers.*;
import org.apache.xerces.parsers.SAXParser;
import org.apache.log4j.Category;
import org.geotools.filter.Filter;
import org.vfny.geoserver.db.jdbc.*;

/**
 * Uses SAX to extact a GetFeature query from and incoming GetFeature request XML stream.
 *
 * <p>Note that this Handler extension ignores Filters completely and must be chained
 * as a parent to the PredicateFilter method in order to recognize them.  If it is not
 * chained, it will still generate valid queries, but with no filtering whatsoever.</p>
 * 
 * @author Rob Hranac, Vision for New York
 * @version 0.9 beta, 11/01/01
 */
public class GetFeatureHandler extends GetFeatureRequest implements ContentHandler {


    /** Standard logging class */
    static Category _log = Category.getInstance(GetFeatureHandler.class.getName());
    
    
    /********************************************************
     Local tracking methods to deal with incoming XML stream
    ********************************************************/

    /** Tracks tag we are currently inside */
    private String insideTag = new String();
    
    /** Boolean to flag whether or not we are inside a query */
    private boolean insideQuery = false;
    
    // **********************************
    
    /** Tracks current query */
    private Query currentQuery = new Query();
    
    
    /********************************************
     Start of SAX Content Handler Methods
     most of these are unused at the moment
     no namespace awareness, yet
    ********************************************/

    
    /** Notes the document locator.	*/ 
    public void setDocumentLocator (Locator locator) {
    }
    
    
    /** Notes the start of the document.	*/ 
    public void startDocument()
        throws SAXException {
        //_log.info("start of document");
    }
    
    
    /** Notes the start of the document.	*/ 
    public void endDocument()
        throws SAXException {
        //_log.info( "at end of document");
    }
    

    /** Notes processing instructions.	*/ 
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
     * Notes the start of the element and sets type names and query attributes.
     *
     * @param namespaceURI URI for namespace appended to element.
     * @param localName Local name of element.
     * @param rawName Raw name of element.
     * @param atts Element attributes.
     */ 
    public void startElement(String namespaceURI, String localName, String rawName, Attributes atts)
        throws SAXException {
        
        // at start of element, set insidetag flag to whatever tag we are inside
        insideTag = localName;
        
        // if at a query element, empty the current query, set insideQuery flag, and get query typeNames
        if( insideTag.equals("Query") ) {
            currentQuery = new Query();
            insideQuery = true;
            for( int i=0; i < atts.getLength(); i++) {
                if( atts.getLocalName(i).equals("typeName")) {
                    _log.debug("found typename: " + atts.getValue(i));
                    currentQuery.setFeatureTypeName( atts.getValue(i));
                }
                if( atts.getLocalName(i).equals("handle")) {
                    _log.debug("found handle: " + atts.getValue(i));
                    currentQuery.setHandle( atts.getValue(i));
                }
            }
        }
        
        if( insideTag.equals("GetFeature") ) {
            for( int i=0; i < atts.getLength(); i++ ) {
                if( atts.getLocalName(i).equals("maxFeatures") ) {
                    _log.debug("found max features: " + atts.getValue(i));
                    this.setMaxFeatures( atts.getValue(i));
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
     */ 
    public void endElement(String namespaceURI, String localName, String rawName)
        throws SAXException {
        
        //_log.info("at end element: " + localName);

        // as we leave query, set insideTag to "NULL" (otherwise the stupid characters method picks up external chars)
        insideTag = "NULL";
        
        // set insideQuery flag as we leave the query and add the query to the return list
        if( localName.equals("Query") ) {
            insideQuery = false;
            queries.add(currentQuery);
            //currentQuery.empty();  // this method should probably occur here, but then it dereferences the list element
        }
    }
    
    
    /**
     * Checks if inside parsed element and adds its contents to the appropriate variable.
     *
     * @param ch URI for namespace appended to element.
     * @param start Local name of element.
     * @param length Raw name of element.
     */ 
    public void characters(char[] ch, int start, int length)
        throws SAXException {
        
        String s = new String(ch, start, length);
        
        // if inside a property element, add the element
        if( insideTag.equals("PropertyName") ) {
            //_log.info("found prop name: " + s);
            currentQuery.addPropertyName(s);
        }
        
    }
    

    /**
     * Notes ignorable whitespace.
     *
     * @param ch URI for namespace appended to element.
     * @param start Local name of element.
     * @param length Raw name of element.
     */ 
    public void ignorableWhitespace(char[] ch, int start, int length)
				throws SAXException {
    }
    
    
    /**
     * Notes skipped entity.
     *
     * @param name Name of skipped entity.
     *
     */ 
    public void skippedEntity(String name)
        throws SAXException {
    }


    /**
     * Gets filter.
     *
     * @param filter (OGC WFS) Filter from (SAX) filter..
     */ 
    public void filter(Filter filter) {
        if(insideQuery) {
            currentQuery.addFilter(filter);
        }
        else {
            for(int i = 0, n = queries.size(); i < n; i++) {
                ((Query) queries.get(i)).addFilter(filter);
            }
        }
    }    
}
