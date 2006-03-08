/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.util.requests;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.geotools.filter.Filter;
import org.geotools.filter.FilterHandler;
import org.xml.sax.ContentHandler;
import org.xml.sax.helpers.XMLFilterImpl;


/**
 * Minimal class to implement the FilterHandler interface.
 *
 * @author Rob Hranac, TOPP
 * @version $Id: FilterHandlerImpl.java,v 1.7 2004/02/09 23:29:44 dmzwiers Exp $
 */
public class FilterHandlerImpl extends XMLFilterImpl implements ContentHandler,
    FilterHandler {
    /** Class logger */
    private static Logger LOGGER = Logger.getLogger(
            "org.vfny.geoserver.requests");

	/**
	 * Tracks current filter
	 * 
	 * @uml.property name="currentFilter"
	 * @uml.associationEnd multiplicity="(0 1)"
	 */
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
    	if (LOGGER.isLoggable(Level.FINEST)) {
    		LOGGER.finest(new StringBuffer("found filter: ").append(filter.toString()).toString());
    	}
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
