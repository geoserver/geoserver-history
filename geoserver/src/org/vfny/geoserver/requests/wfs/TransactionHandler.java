/*
 *    Geotools2 - OpenSource mapping toolkit
 *    http://geotools.org
 *    (C) 2002, Geotools Project Managment Committee (PMC)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 *
 */
/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.requests.wfs;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.geotools.feature.Feature;
import org.geotools.filter.Filter;
import org.geotools.filter.FilterHandler;
import org.geotools.gml.GMLHandlerFeature;
import org.vfny.geoserver.WfsException;
import org.vfny.geoserver.responses.wfs.WfsTransactionException;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.XMLFilterImpl;

import com.vividsolutions.jts.geom.Geometry;


/**
 * Uses SAX to extact a Transactional request from and incoming XML stream.
 *
 * @author Rob Hranac, TOPP
 * @author Chris Holmes, TOPP
 * @version $Id: TransactionHandler.java,v 1.3 2004/01/12 21:01:25 dmzwiers Exp $
 */
public class TransactionHandler extends XMLFilterImpl implements ContentHandler,
    FilterHandler, GMLHandlerFeature {
    //private static final short UNKNOWN = 0;
    private static final State UNKNOWN = new State("UNKNOWN");

    //private static final short INSERT = 1;
    private static final State INSERT = new State("Insert");

    //private static final short DELETE = 2;
    private static final State DELETE = new State("Delete");

    //private static final short UPDATE = 3;
    private static final State UPDATE = new State("Update");

    //private static final short PROPERTY_NAME = 4;
    private static final State PROPERTY_NAME = new State("Name");

    //private static final short VALUE = 5;
    private static final State VALUE = new State("Value");

    //private static final short PROPERTY = 6;
    private static final State PROPERTY = new State("Property");

    //private static final short LOCKID = 7;
    private static final State LOCKID = new State("LockId");

    /** Class logger */
    private static Logger LOGGER = Logger.getLogger(
            "org.vfny.geoserver.requests.wfs");

    /** Internal transaction request for construction. */
    private TransactionRequest request = new TransactionRequest();

    /** Tracks current sub request */
    private SubTransactionRequest subRequest = null;

    /** Tracks tag we are currently inside: helps maintain state. */
    private State state = UNKNOWN;

    /** holds the property name for an update request. */
    private String curPropertyName;

    /** holds the property value for an update request. */
    private Object curPropertyValue;

    /** holds the current lockId */
    private String curLockId = new String();

    /** holds the list of features for an insert request. */
    private List curFeatures;

    /**
     * Flag to alert signal we are within a Property element.  The state
     * thing was not giving enough information.
     */
    private boolean inProperty = false;

    /**
     * Empty constructor.
     */
    public TransactionHandler() {
        super();
    }

    /**
     * Returns the Transaction request.
     *
     * @return The request constructed by this handler.
     */
    public TransactionRequest getRequest() {
        return request;
    }

    /**
     * Convert to the State representation of the element we're on.
     *
     * @param stateName the localName of an element.
     *
     * @return the State representation of the localName.
     */
    private static State toState(String stateName) {
        if (INSERT.isTag(stateName)) {
            return INSERT;
        }

        if (DELETE.isTag(stateName)) {
            return DELETE;
        }

        if (UPDATE.isTag(stateName)) {
            return UPDATE;
        }

        if (PROPERTY_NAME.isTag(stateName)) {
            return PROPERTY_NAME;
        }

        if (VALUE.isTag(stateName)) {
            return VALUE;
        }

        if (PROPERTY.isTag(stateName)) {
            return PROPERTY;
        }

        if (LOCKID.isTag(stateName)) {
            return LOCKID;
        }

        return UNKNOWN;
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

        // at start of element, set insidetag flag to whatever tag we're inside
        state = toState(localName);

        // if at a query element, empty the current query, set insideQuery
        //  flag, and get query typeNames
        if ((state == DELETE) || (state == UPDATE) || (state == INSERT)) {
            if (state == DELETE) {
                subRequest = new DeleteRequest();
            } else if (state == UPDATE) {
                subRequest = new UpdateRequest();
            } else if (state == INSERT) {
                subRequest = new InsertRequest();
                curFeatures = new ArrayList();
            }

            for (int i = 0, n = atts.getLength(); i < n; i++) {
                String name = atts.getLocalName(i);
                String value = atts.getValue(i);
                LOGGER.finest("found attribute '" + name + "'=" + value);

                if (name.equals("typeName")) {
                    subRequest.setTypeName(value);
                } else if (name.equals("handle")) {
                    subRequest.setHandle(value);
                }
            }
        } else if (localName.equals("Transaction")) {
            for (int i = 0; i < atts.getLength(); i++) {
                if (atts.getLocalName(i).equals("handle")) {
                    LOGGER.finest("found handle: " + atts.getValue(i));
                    request.setHandle(atts.getValue(i));
                } else if (atts.getLocalName(i).equals("releaseAction")) {
                    LOGGER.finest("found releaseAction: " + atts.getValue(i));

                    try {
                        request.setReleaseAction(atts.getValue(i));
                    } catch (WfsTransactionException e) {
                        throw new SAXException(e);
                    }
                }
            }
        } else if (state == PROPERTY) {
            inProperty = true;
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
        state = toState(localName);

        // set insideQuery flag as we leave the query and add the query to the
        //  return list
        if ((state == DELETE) || (state == UPDATE) || (state == INSERT)) {
            if (subRequest.getClass().equals(InsertRequest.class)) {
                try {
                    ((InsertRequest) subRequest).addFeatures((Feature[]) curFeatures
                        .toArray(new Feature[0]));
                } catch (WfsException we) {
                    throw new SAXException("Problem adding features: "
                        + we.getMessage(), we);
                }

                curFeatures = new ArrayList();
            }

            request.addSubRequest(subRequest);
        } else if (state == PROPERTY) {
            LOGGER.finer("ending property");

            if (subRequest.getClass().equals(UpdateRequest.class)) {
                ((UpdateRequest) subRequest).addProperty(curPropertyName,
                    curPropertyValue);
                LOGGER.finer("setting update property " + curPropertyName
                    + " to " + curPropertyValue);
                curPropertyName = new String();
                curPropertyValue = null;
                inProperty = false;
            } else {
                throw new SAXException("<property> element should only occur "
                    + "within a <update> element.");
            }
        } else if (state == LOCKID) {
            request.setLockId(curLockId);
            curLockId = new String();
        }

        //REVISIT: should we be able to handle to Transaction Requests with
        //this?  Because right now we don't, all sub requests will get
        //stacked into one, and the handles, lockIds and releaseActions will
        //get all confused.  Maybe have getRequest return an array of requests?
        state = UNKNOWN;
    }

    /**
     * Checks if inside parsed element and adds its contents to appropriate
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
        // if inside a property element, add the element
        if (state == PROPERTY_NAME) {
            String s = new String(ch, start, length);
            LOGGER.finest("found property name: " + s);
            curPropertyName = s.trim();

            //if curProperty is not null then there is a geometry there.
        } else if ((state == VALUE) && (curPropertyValue == null)) {
            String s = new String(ch, start, length);
            curPropertyValue = s.trim();
        } else if (state == LOCKID) {
            String s = new String(ch, start, length);
            curLockId = s.trim();
        }
    }

    /**
     * Gets a filter and adds it to the appropriate query (or queries).
     *
     * @param filter (OGC WFS) Filter from (SAX) filter.
     *
     * @throws RuntimeException If trying to add a filter to an insert
     *         subrequest.
     */
    public void filter(Filter filter) throws RuntimeException {
        try {
            subRequest.setFilter(filter);
        } catch (WfsException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Gets a feature and adds it to the list of current features, to be added
     * to the insert request when it finishes.  This class is called by
     * children filters, needed to implement GMLHandlerFilter.
     *
     * @param feature to be added to the request.
     */
    public void feature(Feature feature) {
        curFeatures.add(feature);
        LOGGER.finest("feature added: " + feature);
    }

    /**
     * If no children claim the geometry it comes here, and is used if we are
     * looking for a value for a property element.
     *
     * @param geometry The geometry to set as a property.
     */
    public void geometry(Geometry geometry) {
        LOGGER.finer("recieved geometry " + geometry);

        if (inProperty) {
            curPropertyValue = geometry;
        }
    }
}


/**
 * Represents state in Transaction Handler
 */
class State {
    String state;

    public State(String tag) {
        state = tag;
    }

    public boolean isTag(String tag) {
        return state.equals(tag);
    }

    public String toString() {
        return state;
    }
}
