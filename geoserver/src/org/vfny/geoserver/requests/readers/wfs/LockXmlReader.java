/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.requests.readers.wfs;

import java.io.IOException;
import java.io.Reader;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.geotools.filter.FilterFilter;
import org.geotools.gml.GMLFilterDocument;
import org.geotools.gml.GMLFilterGeometry;
import org.vfny.geoserver.WfsException;
import org.vfny.geoserver.requests.Request;
import org.vfny.geoserver.requests.readers.XmlRequestReader;
import org.vfny.geoserver.requests.wfs.LockHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.ParserAdapter;


/**
 * reads in a Lock WFS request from an XML stream
 *
 * @author Rob Hranac, TOPP
 * @author Chris Holmes, TOPP
 * @version $Id: LockXmlReader.java,v 1.2.2.1 2003/12/30 23:00:37 dmzwiers Exp $
 */
public class LockXmlReader extends XmlRequestReader {
    /**
     * Creates a new LockXmlReader object.
     */
    public LockXmlReader() {
    }

    /**
     * Reads the Lock XML request into a LockRequest object.
     *
     * @param reader The plain POST text from the client.
     *
     * @return The read LockRequest object.
     *
     * @throws WfsException For any problems reading the request.
     */
    public Request read(Reader reader) throws WfsException {
        // translate string into a proper SAX input source
        InputSource requestSource = new InputSource(reader);

        // instantiante parsers and content handlers
        LockHandler contentHandler = new LockHandler();
        FilterFilter filterParser = new FilterFilter(contentHandler, null);
        GMLFilterGeometry geometryFilter = new GMLFilterGeometry(filterParser);
        GMLFilterDocument documentFilter = new GMLFilterDocument(geometryFilter);

        // read in XML file and parse to content handler
        try {
            LOGGER.finest("about to create parser");

            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();
            ParserAdapter adapter = new ParserAdapter(parser.getParser());
            LOGGER.finest("setting the content handler");
            LOGGER.finest("content handler = " + documentFilter);
            adapter.setContentHandler(documentFilter);
            LOGGER.finest("about to parse");
            LOGGER.finest("calling parse on " + requestSource);
            adapter.parse(requestSource);
            LOGGER.fine("just parsed: " + requestSource);
        } catch (SAXException e) {
            e.printStackTrace(System.out);
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

        return contentHandler.getRequest();
    }
}
