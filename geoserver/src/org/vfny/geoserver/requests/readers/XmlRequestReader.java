/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.requests.readers;

import org.geotools.filter.*;
import org.geotools.filter.Filter;
import org.geotools.gml.*;
import org.vfny.geoserver.*;
import org.vfny.geoserver.requests.*;
import org.xml.sax.*;
import org.xml.sax.helpers.*;
import java.io.*;
import java.util.logging.*;
import javax.xml.parsers.*;


/**
 * This utility reads in XML requests and returns them as appropriate request
 * objects.
 *
 * @author Rob Hranac, TOPP
 * @author Chris Holmes, TOPP
 * @author Gabriel Roldán
 * @version $Id: XmlRequestReader.java,v 1.1.2.1 2003/11/04 22:44:41 cholmesny Exp $
 */
public abstract class XmlRequestReader {
    /** Class logger */
    protected static Logger LOGGER = Logger.getLogger(
            "org.vfny.geoserver.requests.readers");

    /**
     * DOCUMENT ME!
     *
     * @param reader DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws ServiceException DOCUMENT ME!
     */
    public abstract Request read(Reader reader) throws ServiceException;

    /**
     * Reads the Filter XML request into a geotools Feature object.
     *
     * @param rawRequest The plain POST text from the client.
     *
     * @return The geotools filter constructed from rawRequest.
     *
     * @throws WfsException For any problems reading the request.
     */
    public static Filter readFilter(Reader rawRequest)
        throws WfsException {
        // translate string into a proper SAX input source
        InputSource requestSource = new InputSource(rawRequest);

        // instantiante parsers and content handlers
        FilterHandlerImpl contentHandler = new FilterHandlerImpl();
        FilterFilter filterParser = new FilterFilter(contentHandler, null);
        GMLFilterGeometry geometryFilter = new GMLFilterGeometry(filterParser);
        GMLFilterDocument documentFilter = new GMLFilterDocument(geometryFilter);

        // read in XML file and parse to content handler
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();
            ParserAdapter adapter = new ParserAdapter(parser.getParser());

            adapter.setContentHandler(documentFilter);
            adapter.parse(requestSource);
            LOGGER.fine("just parsed: " + requestSource);
        } catch (SAXException e) {
            throw new WfsException(e,
                "XML getFeature request SAX parsing error",
                XmlRequestReader.class.getName());
        } catch (IOException e) {
            throw new WfsException(e, "XML get feature request input error",
                XmlRequestReader.class.getName());
        } catch (ParserConfigurationException e) {
            throw new WfsException(e, "Some sort of issue creating parser",
                XmlRequestReader.class.getName());
        }

        LOGGER.fine("passing filter: " + contentHandler.getFilter());

        return contentHandler.getFilter();
    }
}
