/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.wms.xml;

import java.io.IOException;
import java.io.Reader;

import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.geoserver.wms.WMS;
import org.geoserver.wms.request.GetCapabilitiesRequest;
import org.vfny.geoserver.Request;
import org.vfny.geoserver.util.requests.readers.XmlRequestReader;
import org.vfny.geoserver.wms.WmsException;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.ParserAdapter;

/**
 * reads a WMS GetCapabilities request from an XML stream
 * 
 * @author Gabriel Roldan
 * @version $Id$
 * 
 * @task TODO: see if it must be refactored to read WMS GetCapabilities too
 */
public class CapabilitiesXmlReader extends XmlRequestReader {

    private WMS wmsConfig;

    /**
     * Creates the new reader.
     * 
     * @param wms
     *            The WMS service config.
     */
    public CapabilitiesXmlReader(WMS wms) {
        super(wms.getServiceInfo());
        this.wmsConfig = wms;
    }

    /**
     * Reads the Capabilities XML request into a CapabilitiesRequest object.
     * 
     * @param reader
     *            The plain POST text from the client.
     * 
     * @return The read CapabilitiesRequest object.
     * 
     * @throws WmsException
     *             For any problems reading the request
     */
    public Request read(Reader reader, HttpServletRequest req) throws WmsException {
        InputSource requestSource = new InputSource(reader);

        // instantiante parsers and content handlers
        GetCapabilitiesRequest request = new GetCapabilitiesRequest(wmsConfig);
        CapabilitiesHandler currentRequest = new CapabilitiesHandler(request);

        // read in XML file and parse to content handler
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();
            ParserAdapter adapter = new ParserAdapter(parser.getParser());
            adapter.setContentHandler(currentRequest);
            adapter.parse(requestSource);
            LOGGER.fine("just parsed: " + requestSource);
        } catch (SAXException e) {
            throw new WmsException(e, "XML capabilities request parsing error", getClass()
                    .getName());
        } catch (IOException e) {
            throw new WmsException(e, "XML capabilities request input error", getClass().getName());
        } catch (ParserConfigurationException e) {
            throw new WmsException(e, "Some sort of issue creating parser", getClass().getName());
        }

        Request r = currentRequest.getRequest(req);

        return r;
    }
}
