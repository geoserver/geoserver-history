/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.util.requests;

import java.util.logging.Logger;

import org.vfny.geoserver.servlets.Dispatcher;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.XMLFilterImpl;


/**
 * Uses SAX to extact a GetFeature query from and incoming GetFeature request
 * XML stream.
 * 
 * <p>
 * Note that this Handler extension ignores Filters completely and must be
 * chained as a parent to the PredicateFilter method in order to recognize
 * them.  If it is not chained, it will still generate valid queries, but with
 * no filtering whatsoever.
 * </p>
 *
 * @author Chris Holmes, TOPP
 * @author $Author: Alessio Fabiani (alessio.fabiani@gmail.com) $ (last modification)
 * @author $Author: Simone Giannecchini (simboss_ml@tiscali.it) $ (last modification)
 * @version $Id: DispatcherHandler.java,v 1.5 2004/07/15 21:13:12 jmacgill Exp $
 */
public class DispatcherHandler extends XMLFilterImpl implements ContentHandler {
    /** Class logger */
    private static Logger LOGGER = Logger.getLogger(
            "org.vfny.geoserver.requests");

    /** Stores internal request type */
    private int requestType = Dispatcher.UNKNOWN;

    /** Stores internal service type. */
    private int serviceType = Dispatcher.UNKNOWN;

    /** Flags whether or not type has been set */
    private boolean gotType = false;

    /**
     * Gets the request type.  See Dispatcher for the available types.
     *
     * @return an int of the request type.
     */
    public int getRequestType() {
        return requestType;
    }

    /**
     * Gets the service type, for now either WMS or WFS types of Dispatcher.
     *
     * @return an int of the service type.
     */
    public int getServiceType() {
        return serviceType;
    }

    /**
     * Notes the start of the element and checks for request type.
     *
     * @param namespaceURI URI for namespace appended to element.
     * @param localName Local name of element.
     * @param rawName Raw name of element.
     * @param atts Element attributes.
     *
     * @throws SAXException DOCUMENT ME!
     */
    public void startElement(String namespaceURI, String localName,
        String rawName, Attributes atts) throws SAXException {
        if (!gotType) {
            // if at a query element, empty the current query, set insideQuery flag, and get query typeNames
            if (localName.equals("GetCapabilities")) {
                this.requestType = Dispatcher.GET_CAPABILITIES_REQUEST;
            } else if (localName.equals("DescribeCoverage")) {
                this.requestType = Dispatcher.DESCRIBE_COVERAGE_REQUEST;
            } else if (localName.equals("GetCoverage")) {
                this.requestType = Dispatcher.GET_COVERAGE_REQUEST;
            } else if (localName.equals("DescribeFeatureType")) {
                this.requestType = Dispatcher.DESCRIBE_FEATURE_TYPE_REQUEST;
            } else if (localName.equals("GetFeature")) {
                this.requestType = Dispatcher.GET_FEATURE_REQUEST;
            } else if (localName.equals("Transaction")) {
                this.requestType = Dispatcher.TRANSACTION_REQUEST;
            } else if (localName.equals("GetFeatureWithLock")) {
                this.requestType = Dispatcher.GET_FEATURE_LOCK_REQUEST;
            } else if (localName.equals("LockFeature")) {
                this.requestType = Dispatcher.LOCK_REQUEST;
            } else if (localName.equals("GetMap")) {
                this.requestType = Dispatcher.GET_MAP_REQUEST;
            } else if (localName.equals("GetFeatureInfo")) {
                this.requestType = Dispatcher.GET_FEATURE_INFO_REQUEST;
            } else {
                this.requestType = Dispatcher.UNKNOWN;
            }
        }

        for (int i = 0, n = atts.getLength(); i < n; i++) {
            if (atts.getLocalName(i).equals("service")) {
                String service = atts.getValue(i);

                if (service.equals("WCS")) {
                    this.serviceType = Dispatcher.WCS_SERVICE;
                } else if (service.equals("WFS")) {
                    this.serviceType = Dispatcher.WFS_SERVICE;
                } else if (service.equals("WMS")) {
                    this.serviceType = Dispatcher.WMS_SERVICE;
                }
            } else {
                this.serviceType = Dispatcher.UNKNOWN;
            }
        }

        gotType = true;
    }
}
