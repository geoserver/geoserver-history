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
import org.geotools.filter.Filter;
import org.geotools.filter.FilterHandler;
import org.vfny.geoserver.responses.WfsException;

/**
 * Minimal class to implement the FilterHandler interface.
 * 
 * @author Rob Hranac, TOPP
 * @version $VERSION$
 */
public class FilterHandlerImpl 
    extends XMLFilterImpl  
    implements ContentHandler, FilterHandler {

    /** Class logger */
    private static Logger LOGGER = 
        Logger.getLogger("org.vfny.geoserver.requests");
       
    /** Tracks current filter */
    private Filter currentFilter = null;


    /** Empty constructor. */
    public FilterHandlerImpl () { super(); }
    
    
    /*************************************************************************
     *  Standard SAX content handler methods                                 *
     *************************************************************************/
    /**
     * Gets filter.
     * @param filter (OGC WFS) Filter from (SAX) filter..
     */ 
    public void filter(Filter filter) {
        LOGGER.finest("found filter: " + filter.toString());
        currentFilter = filter;
    }    

    /**
     * Gives filter.
     * @returns (OGC WFS) Filter from (SAX) filter..
     */ 
    public Filter getFilter() {
        return currentFilter;
    }    
}
