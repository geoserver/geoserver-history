/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.requests.readers.wfs;

import java.io.IOException;
import java.io.Reader;

import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.vfny.geoserver.WfsException;
import org.vfny.geoserver.requests.Request;
import org.vfny.geoserver.requests.readers.XmlRequestReader;
import org.vfny.geoserver.requests.wfs.DescribeHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.ParserAdapter;


/**
 * reads in a DescribeFeatureType WFS request from a XML stream
 *
 * @author Rob Hranac, TOPP
 * @author Chris Holmes, TOPP
 * @version 0.1
 */
public class DescribeXmlReader extends XmlRequestReader {
    /**
     * Creates a new DescribeXmlReader object.
     */
    public DescribeXmlReader() {
    }

    /**
     * DOCUMENT ME!
     *
     * @param reader DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws WfsException DOCUMENT ME!
     */
    public Request read(Reader reader, HttpServletRequest req) throws WfsException {
        /** create a describe feature type request class to return */
        InputSource requestSource = new InputSource(reader);

        // instantiante parsers and content handlers
        DescribeHandler contentHandler = new DescribeHandler();

        // read in XML file and parse to content handler
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();
            ParserAdapter adapter = new ParserAdapter(parser.getParser());

            adapter.setContentHandler(contentHandler);
            adapter.parse(requestSource);
            LOGGER.finer("just parsed: " + requestSource);
        } catch (SAXException e) {
            throw new WfsException(e, "XML describe request parsing error",
                getClass().getName());
        } catch (IOException e) {
            throw new WfsException(e, "XML describe request input error",
                getClass().getName());
        } catch (ParserConfigurationException e) {
            throw new WfsException(e, "Some sort of issue creating parser",
                getClass().getName());
        }

        LOGGER.finer("about to return ");
        LOGGER.finer("returning " + contentHandler.getRequest());

        return contentHandler.getRequest();
    }
}
