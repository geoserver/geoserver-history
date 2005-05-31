/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wfs.requests;

import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import org.geotools.filter.Filter;
import org.geotools.filter.FilterHandler;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.XMLFilterImpl;


/**
 * Uses SAX to extract a LockRequest from an incoming LockFeature
 * 
 * <p>
 * Note that this Handler extension ignores Filters completely and must be
 * chained as a parent to the PredicateFilter method in order to recognize
 * them. If it is not chained, it will still generate valid queries, but with
 * no filtering whatsoever.
 * </p>
 *
 * @author Chris Holmes, TOPP
 * @version $Id: LockHandler.java,v 1.7 2004/02/13 19:30:39 dmzwiers Exp $
 */
public class LockHandler extends XMLFilterImpl implements ContentHandler,
    FilterHandler {
    /** Class logger */
    private static Logger LOGGER = Logger.getLogger(
            "org.vfny.geoserver.requests");

    /** Internal lock request for construction. */
    private LockRequest request = new LockRequest();

    /* ***********************************************************************
     *  Local tracking methods to deal with incoming XML stream              *
     * ***********************************************************************/

    /** Tracks tag we are currently inside: helps maintain state. */
    private String insideTag = new String();

    /** Boolean to flag whether or not we are inside a query */
    private boolean insideLock = false;

    /** Tracks current query */
    private Filter curFilter;

    /** Tracks the current type name. */
    private String curTypeName;

    /** Tracks the current handle. */
    private String curHandle;

    /**
     * Empty constructor.
     */
    public LockHandler() {
        super();
    }

    /**
     * Returns the Lock request.
     *
     * @return The lock request found by this handler.
     */
    public LockRequest getRequest(HttpServletRequest req) {
    	request.setHttpServletRequest(req);
        return request;
    }

    /* ************************************************************************
     * Standard SAX content handler methods
     * ***********************************************************************/

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
        insideTag = localName;

        // if at a query element, empty the current query, set insideLock
        //  flag, and get query typeNames
        if (insideTag.equals("Lock")) {
            //currentLock = new Lock();
            insideLock = true;

            for (int i = 0, n = atts.getLength(); i < n; i++) {
                String name = atts.getLocalName(i);
                String value = atts.getValue(i);
                LOGGER.finest("found attribute '" + name + "'=" + value);

                if (name.equals("typeName")) {
                    curTypeName = value;
                } else if (name.equals("handle")) {
                    curHandle = value;
                }
            }
        } else if (insideTag.equals("LockFeature")) {
            for (int i = 0; i < atts.getLength(); i++) {
                if (atts.getLocalName(i).equals("expiry")) {
                    LOGGER.finest("foundExpiry: " + atts.getValue(i));

                    int expiry = -1;

                    try {
                        expiry = Integer.parseInt(atts.getValue(i));
                    } catch (NumberFormatException e) {
                        throw new SAXException("expiry should parse to an "
                            + "integer", e);
                    }

                    request.setExpiry(expiry);
                } else if (atts.getLocalName(i).equals("lockAction")) {
                    //REVIST: should this ignore case or not?
                    if (atts.getValue(i).equalsIgnoreCase("SOME")) {
                        request.setLockAll(false);

                        //default is true, so only set if it is some.
                    }
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

        // as we leave query, set insideTag to "NULL" (otherwise the stupid
        //  characters method picks up external chars)
        insideTag = "NULL";

        // set insideLock flag as we leave the query and add the query to the
        //  return list
        if (localName.equals("Lock")) {
            LOGGER.finest("adding lock: " + curTypeName + ", " + curHandle
                + ", " + curFilter);
            insideLock = false;
            request.addLock(curTypeName, curFilter, curHandle);
            curTypeName = null;
            curFilter = null;
            curHandle = null;
        }
    }

    /**
     * Gets a filter and adds it to the appropriate query (or queries).
     *
     * @param filter (OGC WFS) Filter from (SAX) filter.
     */
    public void filter(Filter filter) {
        LOGGER.finest("found filter: " + filter.toString());

        if (insideLock) {
            //LOGGER.finest("add filter " + filter + " to lock: "+currentLock);
            curFilter = filter;
        }

        //else {

        /* REVISIT: most likely do nothing here, as I don't think filters
           can apply to all lock requests.
           LOGGER.finest("adding filter to all queries: " + filter);
           for(int i = 0, n = request.queries.size(); i < n; i++) {
               Lock query = (Lock) request.queries.get(i);
               Filter queryFilter = query.getFilter();
               if(queryFilter != null) {
                   query.addFilter(queryFilter.and(filter));
               } else {
                   query.addFilter(filter);
               }
           }
         */

        //}
    }
}
