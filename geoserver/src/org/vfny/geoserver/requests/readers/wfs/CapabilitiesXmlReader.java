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

import org.vfny.geoserver.WfsException;
import org.vfny.geoserver.requests.CapabilitiesHandler;
import org.vfny.geoserver.requests.Request;
import org.vfny.geoserver.requests.readers.XmlRequestReader;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.ParserAdapter;


/**
 * reads a GetCapabilities request from an XML stream
 *
 * @author Rob Hranac, TOPP
 * @author Chris Holmes, TOPP
 * @version $Id: CapabilitiesXmlReader.java,v 1.2.2.2 2003/12/31 23:36:44 dmzwiers Exp $
 *
 * @task TODO: see if it must be refactored to read WMSConfig GetCapabilities too
 */
public class CapabilitiesXmlReader extends XmlRequestReader {
    /**
     * Reads the Capabilities XML request into a CapabilitiesRequest object.
     *
     * @param reader The plain POST text from the client.
     *
     * @return The read CapabilitiesRequest object.
     *
     * @throws WfsException For any problems reading the request
     */
    public Request read(Reader reader) throws WfsException {
        InputSource requestSource = new InputSource(reader);

        // instantiante parsers and content handlers
        CapabilitiesHandler currentRequest = new CapabilitiesHandler();

        // read in XML file and parse to content handler
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();
            ParserAdapter adapter = new ParserAdapter(parser.getParser());

            adapter.setContentHandler(currentRequest);
            adapter.parse(requestSource);
            LOGGER.fine("just parsed: " + requestSource);
        } catch (SAXException e) {
            throw new WfsException(e, "XML capabilities request parsing error",
                getClass().getName());
        } catch (IOException e) {
            throw new WfsException(e, "XML capabilities request input error",
                getClass().getName());
        } catch (ParserConfigurationException e) {
            throw new WfsException(e, "Some sort of issue creating parser",
                getClass().getName());
        }

        return currentRequest.getRequest();
    }
}
