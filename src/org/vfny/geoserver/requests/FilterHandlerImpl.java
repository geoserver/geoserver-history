/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.requests;

import org.geotools.filter.*;
import org.geotools.filter.Filter;
import org.vfny.geoserver.*;
import org.vfny.geoserver.requests.wfs.*;
import org.xml.sax.*;
import org.xml.sax.helpers.*;
import java.util.logging.*;


/**
 * Minimal class to implement the FilterHandler interface.
 *
 * @author Rob Hranac, TOPP
 * @version $Id: FilterHandlerImpl.java,v 1.3 2003/12/16 18:46:08 cholmesny Exp $
 */
public class FilterHandlerImpl extends XMLFilterImpl implements ContentHandler,
    FilterHandler {
    /** Class logger */
    private static Logger LOGGER = Logger.getLogger(
            "org.vfny.geoserver.requests");

    /** Tracks current filter */
    private Filter currentFilter = null;

    /**
     * Empty constructor.
     */
    public FilterHandlerImpl() {
        super();
    }

    /**
     * Recieves the filter from the filter parsing children.
     *
     * @param filter (OGC WFS) Filter from (SAX) filter..
     */
    public void filter(Filter filter) {
        LOGGER.finest("found filter: " + filter.toString());
        currentFilter = filter;
    }

    /**
     * Gives filter to whoever wants it.
     *
     * @return (OGC WFS) Filter from (SAX) filter..
     */
    public Filter getFilter() {
        return currentFilter;
    }
}
