/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.requests.readers;

import java.io.IOException;
import java.io.Reader;
import java.util.logging.Logger;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.geotools.filter.Filter;
import org.geotools.filter.FilterFilter;
import org.geotools.gml.GMLFilterDocument;
import org.geotools.gml.GMLFilterGeometry;
import org.vfny.geoserver.ServiceException;
import org.vfny.geoserver.WfsException;
import org.vfny.geoserver.requests.FilterHandlerImpl;
import org.vfny.geoserver.requests.Request;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.ParserAdapter;


/**
 * This utility reads in XML requests and returns them as appropriate request
 * objects.
 *
 * @author Rob Hranac, TOPP
 * @author Chris Holmes, TOPP
 * @author Gabriel Roldán
 * @version $Id: XmlRequestReader.java,v 1.6 2004/02/09 23:29:47 dmzwiers Exp $
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
