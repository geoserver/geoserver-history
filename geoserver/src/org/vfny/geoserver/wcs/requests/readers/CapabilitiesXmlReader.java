/* Copyright (c) 2005 NATO - Undersea Research Centre.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wcs.requests.readers;

import java.io.IOException;
import java.io.Reader;

import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.vfny.geoserver.Request;
import org.vfny.geoserver.util.requests.CapabilitiesHandler;
import org.vfny.geoserver.util.requests.readers.XmlRequestReader;
import org.vfny.geoserver.wcs.WcsException;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.ParserAdapter;

/**
 * reads a WCS GetCapabilities request from an XML stream
 * 
 * @author $Author: Alessio Fabiani (alessio.fabiani@gmail.com) $ (last modification)
 * @author $Author: Simone Giannecchini (simboss_ml@tiscali.it) $ (last modification)
 * @version $Id: CapabilitiesXmlReader.java,v 0.1 Feb 15, 2005 12:35:09 PM $
 */
public class CapabilitiesXmlReader extends XmlRequestReader {
    /**
     * Reads the Capabilities XML request into a CapabilitiesRequest object.
     *
     * @param reader The plain POST text from the client.
     *
     * @return The read CapabilitiesRequest object.
     *
     * @throws WmsException For any problems reading the request
     */
    public Request read(Reader reader, HttpServletRequest req) throws WcsException {
        InputSource requestSource = new InputSource(reader);

        // instantiante parsers and content handlers
        CapabilitiesHandler currentRequest = new CapabilitiesHandler("WCS");

        // read in XML file and parse to content handler
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();
            ParserAdapter adapter = new ParserAdapter(parser.getParser());
            adapter.setContentHandler(currentRequest);
            adapter.parse(requestSource);
            LOGGER.fine("just parsed: " + requestSource);
        } catch (SAXException e) {
            throw new WcsException(e, "XML capabilities request parsing error",
                getClass().getName());
        } catch (IOException e) {
            throw new WcsException(e, "XML capabilities request input error",
                getClass().getName());
        } catch (ParserConfigurationException e) {
            throw new WcsException(e, "Some sort of issue creating parser",
                getClass().getName());
        }

        Request r = currentRequest.getRequest(req);
        return r;
    }
}
