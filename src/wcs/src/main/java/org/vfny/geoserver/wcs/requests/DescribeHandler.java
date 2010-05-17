/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wcs.requests;

import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import org.geoserver.wcs.WCSInfo;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.XMLFilterImpl;


/**
 * DOCUMENT ME!
 *
 * @author $Author: Alessio Fabiani (alessio.fabiani@gmail.com) $ (last modification)
 * @author $Author: Simone Giannecchini (simboss1@gmail.com) $ (last modification)
 */
public class DescribeHandler extends XMLFilterImpl implements ContentHandler {
    /** Class logger */
    private static Logger LOGGER = org.geotools.util.logging.Logging.getLogger("org.vfny.geoserver.requests.wcs");

    /**
     * Internal GetCapabilities request for construction.
     */
    private DescribeRequest request = null;

    /** Local variable to track current tag */
    private String currentTag = new String();

    public DescribeHandler(WCSInfo wcs) {
        super();
        request = new DescribeRequest(wcs);
    }

    /**
     * Returns the DescribeCoverage request.
     *
     * @return DescribeCoverage request.
     */
    public DescribeRequest getRequest(HttpServletRequest req) {
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

        if (currentTag.equals("DescribeCoverage")) {
            final int length = atts.getLength();

            for (int i = 0; i < length; i++) {
                if (atts.getLocalName(i).equals("outputFormat")) {
                    LOGGER.finest("found outputFormat: " + atts.getValue(i));
                    request.setOutputFormat(atts.getValue(i));
                } else if (atts.getLocalName(i).equals("service")) {
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
     * Checks if inside coverage name and adds to coverage list, if so.
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

        if (currentTag.equals("Coverage")) {
            request.addCoverage(s);
            LOGGER.finest("added coverage: " + s);
        }
    }
}
