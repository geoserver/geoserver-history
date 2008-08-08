/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package com.orci.geoserver.wfs.getnearest;

import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import org.geotools.filter.Filter;
import org.geotools.filter.FilterHandler;
import org.vfny.geoserver.wfs.Query;
import org.vfny.geoserver.wfs.servlets.WFService;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.XMLFilterImpl;


/**
 * Uses SAX to extact a GetFeature query from and incoming GetFeature
 * request XML stream.<p>Note that this Handler extension ignores Filters
 * completely and must be chained as a parent to the PredicateFilter method in
 * order to recognize them. If it is not chained, it will still generate valid
 * queries, but with no filtering whatsoever.</p>
 *
 * @author Rob Hranac, TOPP
 * @version $Id: FeatureHandler.java,v 1.7 2004/02/13 19:30:39 dmzwiers Exp $
 */
public class GetNearestHandler extends XMLFilterImpl implements ContentHandler, FilterHandler {
    /** Class logger */
    private static Logger LOGGER = org.geotools.util.logging.Logging.getLogger("org.vfny.geoserver.requests.wfs");

    /** Service handling the request */
    private WFService service;

    /** Internal get feature request for construction. */
    private GetNearestRequest request = null;

    /** Tracks tag we are currently inside: helps maintain state. */
    private String insideTag = new String();

    /** Boolean to flag whether or not we are inside a query */
    private boolean insideQuery = false;

    /** Tracks current query */
    private Query currentQuery = new Query();

    /**
     * Collects string chunks in {@link #characters(char[], int, int)}
     * callback to be handled at the beggining of {@link #endElement(String,
     * String, String)}
     */
    private StringBuffer characters = new StringBuffer();

    /**
             * Empty constructor.
             */
    public GetNearestHandler(WFService service) {
        super();
        this.service = service;
        request = new GetNearestRequest(service);
    }

    /**
     * Returns the GetFeature request.
     *
     * @param req DOCUMENT ME!
     *
     * @return The request read by this handler.
     */
    public GetNearestRequest getRequest(HttpServletRequest req) {
        request.setHttpServletRequest(req);

        return request;
    }

    /**
     * Notes the start of the element and sets type names and query
     * attributes.
     *
     * @param namespaceURI URI for namespace appended to element.
     * @param localName Local name of element.
     * @param rawName Raw name of element.
     * @param atts Element attributes.
     *
     * @throws SAXException When the XML is not well formed.
     */
    public void startElement(String namespaceURI, String localName, String rawName, Attributes atts)
        throws SAXException {
        LOGGER.finest("at start element: " + localName);
        characters.setLength(0);

        // at start of element, set inside flag to whatever tag we are inside
        insideTag = localName;

        // if at a query element, empty the current query, set insideQuery
        //  flag, and get query typeNames
        if (insideTag.equals("Query")) {
            currentQuery = new Query();
            insideQuery = true;

            for (int i = 0, n = atts.getLength(); i < n; i++) {
                String name = atts.getLocalName(i);
                String value = atts.getValue(i);
                LOGGER.finest("found attribute '" + name + "'=" + value);

                if (name.equals("typeName")) {
                    currentQuery.setTypeName(value);
                } else if (name.equals("handle")) {
                    currentQuery.setHandle(value);
                }
            }
        } else if (insideTag.equals("GetNearestFeature")) {
            request = new GetNearestRequest(service);

            for (int i = 0; i < atts.getLength(); i++) {
                String curAtt = atts.getLocalName(i);

                if (curAtt.equals("outputFormat")) {
                    LOGGER.finest("found outputFormat: " + atts.getValue(i));
                    request.setOutputFormat(atts.getValue(i));
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
        handleCharacters();

        // as we leave query, set insideTag to "NULL" (otherwise the stupid
        //  characters method picks up external chars)
        insideTag = "NULL";

        // set insideQuery flag as we leave the query and add the query to the
        //  return list
        if (localName.equals("Query")) {
            LOGGER.finest("adding query: " + currentQuery.toString());
            insideQuery = false;
            request.addQuery(currentQuery);
        } else if (localName.equals("Point")) {
            request.setPoint(characters.toString());
        } else if (insideTag.equals("MaxRange")) {
            request.setMaxRange(characters.toString());
        }
    }

    /**
     * Checks if inside parsed element and adds its contents to the
     * appropriate variable.
     *
     * @param ch URI for namespace appended to element.
     * @param start Local name of element.
     * @param length Raw name of element.
     *
     * @throws SAXException When the XML is not well formed.
     */
    public void characters(char[] ch, int start, int length)
        throws SAXException {
        characters.append(ch, start, length);
    }

    /**
     * Gets a filter and adds it to the appropriate query (or queries).
     *
     * @param filter (OGC WFS) Filter from (SAX) filter.
     */
    public void filter(Filter filter) {
        LOGGER.finest("found filter: " + filter.toString());

        if (insideQuery) {
            LOGGER.finest("add filter " + filter + " to query: " + currentQuery);
            currentQuery.addFilter(filter);
        } else {
            LOGGER.finest("adding filter to all queries: " + filter);

            for (int i = 0, n = request.queries.size(); i < n; i++) {
                Query query = (Query) request.queries.get(i);
                Filter queryFilter = query.getFilter();

                if (queryFilter != null) {
                    query.addFilter(queryFilter.and(filter));
                } else {
                    query.addFilter(filter);
                }
            }
        }
    }

    /**
     * Handles the string chunks collected in {@link #characters}.
     */
    private void handleCharacters() {
        if (characters.length() == 0) {
            return;
        }

        // if inside a property element, add the element
        if (insideTag.equals("PropertyName")) {
            String name = characters.toString().trim();
            characters.setLength(0);
            LOGGER.finest("found property name: " + name);
            currentQuery.addPropertyName(name);
        }
    }
}
