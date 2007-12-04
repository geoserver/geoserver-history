/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.catalog.requests;

import org.vfny.geoserver.catalog.servlets.CATALOGService;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.XMLFilterImpl;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;


/**
 * DOCUMENT ME!
 *
 * @author $Author: Alessio Fabiani (GeoSolutions)
 */
public class DeleteFeatureTypeHandler extends XMLFilterImpl implements ContentHandler {
    /** Class logger */
    private static Logger LOGGER = Logger.getLogger("org.vfny.geoserver.requests.catalog");

    /**
     * Internal DeleteFeature request for construction.
     */
    private DeleteFeatureTypeRequest request = null;

    /** Local variable to track current tag */
    private String currentTag = new String();

    public DeleteFeatureTypeHandler(CATALOGService service) {
        super();
        request = new DeleteFeatureTypeRequest(service);
    }

    /**
     * Returns the DeleteFeatureType request.
     *
     * @return DeleteFeatureType request.
     */
    public DeleteFeatureTypeRequest getRequest(HttpServletRequest req) {
        request.setHttpServletRequest(req);

        return request;
    }

    /* ***********************************************************************
     *  Standard SAX content handler methods                                 *
     * ***********************************************************************/

    /**
     * Notes the start of the element and sets the current tag.
     *
     * @param namespaceURI URI for namespace appended to element.
     * @param localName Local name of element.
     * @param rawName Raw name of element.
     * @param atts Element attributes.
     *
     * @throws SAXException For standard SAX errors.
     */
    public void startElement(String namespaceURI, String localName, String rawName, Attributes atts)
        throws SAXException {
        LOGGER.finest("found start element: " + localName);
        currentTag = localName;

        if (currentTag.equals("DeleteFeatureType")) {
            final int length = atts.getLength();

            for (int i = 0; i < length; i++) {
                if (atts.getLocalName(i).equals("service")) {
                    request.setService(atts.getValue(i));
                } else if (atts.getLocalName(i).equals("version")) {
                    request.setVersion(atts.getValue(i));
                }
            }
        }
    }

    /**
     * Notes the end of the element and sets the current tag.
     *
     * @param namespaceURI URI for namespace appended to element.
     * @param localName Local name of element.
     * @param rawName Raw name of element.
     *
     * @throws SAXException For standard SAX errors.
     */
    public void endElement(String namespaceURI, String localName, String rawName)
        throws SAXException {
        LOGGER.finest("found end element: " + localName);
        currentTag = "";
    }

    /**
     * Checks if inside data.
     *
     * @param ch URI for namespace appended to element.
     * @param start Local name of element.
     * @param length Raw name of element.
     *
     * @throws SAXException For standard SAX errors.
     */
    public void characters(char[] ch, int start, int length)
        throws SAXException {
        String s = new String(ch, start, length);

        if (currentTag.equals("Feature")) {
            request.setFeatureTypeId(s);
            LOGGER.finest("found feature: " + s);
        }
    }
}
