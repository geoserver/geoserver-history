/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.requests.wfs;

import org.geotools.filter.*;
import org.geotools.filter.Filter;
import org.vfny.geoserver.*;
import org.vfny.geoserver.requests.*;
import org.xml.sax.*;
import org.xml.sax.helpers.*;
import java.util.logging.*;


/**
 * Uses SAX to extact a GetFeature query from and incoming GetFeature request
 * XML stream.
 * 
 * <p>
 * Note that this Handler extension ignores Filters completely and must be
 * chained as a parent to the PredicateFilter method in order to recognize
 * them. If it is not chained, it will still generate valid queries, but with
 * no filtering whatsoever.
 * </p>
 *
 * @author Rob Hranac, TOPP
 * @version $Id: FeatureHandler.java,v 1.1.2.1 2003/11/04 22:48:26 cholmesny Exp $
 */
public class FeatureHandler extends XMLFilterImpl implements ContentHandler,
    FilterHandler {
    /** Class logger */
    private static Logger LOGGER = Logger.getLogger(
            "org.vfny.geoserver.requests.wfs");

    /** Internal get feature request for construction. */
    private FeatureRequest request = new FeatureRequest();

    /** Tracks tag we are currently inside: helps maintain state. */
    private String insideTag = new String();

    /** Boolean to flag whether or not we are inside a query */
    private boolean insideQuery = false;

    /** Tracks current query */
    private Query currentQuery = new Query();

    /**
     * Empty constructor.
     */
    public FeatureHandler() {
        super();
    }

    /**
     * Returns the GetFeature request.
     *
     * @return The request read by this handler.
     */
    public FeatureRequest getRequest() {
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
        } else if (insideTag.startsWith("GetFeature")) {
            if (insideTag.equals("GetFeatureWithLock")) {
                request = new FeatureWithLockRequest();
            } else {
                request = new FeatureRequest();
            }

            for (int i = 0; i < atts.getLength(); i++) {
                String curAtt = atts.getLocalName(i);

                if (curAtt.equals("maxFeatures")) {
                    LOGGER.finest("found max features: " + atts.getValue(i));
                    request.setMaxFeatures(atts.getValue(i));
                } else if (curAtt.equals("outputFormat")) {
                    LOGGER.finest("found outputFormat: " + atts.getValue(i));
                    request.setOutputFormat(atts.getValue(i));
                } else if (curAtt.equals("expiry")
                        && request instanceof FeatureWithLockRequest) {
                    int expiry = -1;

                    try {
                        expiry = Integer.parseInt(atts.getValue(i));
                    } catch (NumberFormatException e) {
                        throw new SAXException("expiry should parse to an "
                            + "integer", e);
                    }

                    ((FeatureWithLockRequest) request).setExpiry(expiry);
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

        // set insideQuery flag as we leave the query and add the query to the
        //  return list
        if (localName.equals("Query")) {
            LOGGER.finest("adding query: " + currentQuery.toString());
            insideQuery = false;
            request.addQuery(currentQuery);
        }
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
        if (insideTag.equals("PropertyName")) {
            LOGGER.finest("found property name: " + s);
            currentQuery.addPropertyName(s);
        }
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
}
