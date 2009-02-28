/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wcs.requests.readers;

import java.io.IOException;
import java.io.Reader;
import java.util.logging.Level;

import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.geoserver.wcs.WCSInfo;
import org.vfny.geoserver.Request;
import org.vfny.geoserver.util.requests.readers.XmlRequestReader;
import org.vfny.geoserver.wcs.WcsException;
import org.vfny.geoserver.wcs.requests.CoverageHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.ParserAdapter;


/**
 * DOCUMENT ME!
 *
 * @author $Author: Alessio Fabiani (alessio.fabiani@gmail.com) $ (last modification)
 * @author $Author: Simone Giannecchini (simboss1@gmail.com) $ (last modification)
 */
public class GetCoverageXmlReader extends XmlRequestReader {
    public GetCoverageXmlReader(WCSInfo wcs) {
        super(wcs);
    }

    public Request read(Reader reader, HttpServletRequest req)
        throws WcsException {
        // translate string into a proper SAX input source
        InputSource requestSource = new InputSource(reader);

        // instantiante parsers and content handlers
        CoverageHandler contentHandler = new CoverageHandler((WCSInfo)getService());

        // read in XML file and parse to content handler
        try {
            if (LOGGER.isLoggable(Level.FINEST)) {
                LOGGER.finest("about to create parser");
            }

            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();
            ParserAdapter adapter = new ParserAdapter(parser.getParser());

            if (LOGGER.isLoggable(Level.FINEST)) {
                LOGGER.finest("setting the content handler");
            }

            if (LOGGER.isLoggable(Level.FINEST)) {
                LOGGER.finest("content handler = " + contentHandler);
            }

            adapter.setContentHandler(contentHandler);

            if (LOGGER.isLoggable(Level.FINEST)) {
                LOGGER.finest("about to parse");
            }

            if (LOGGER.isLoggable(Level.FINEST)) {
                LOGGER.finest("calling parse on " + requestSource);
            }

            adapter.parse(requestSource);

            if (LOGGER.isLoggable(Level.FINEST)) {
                LOGGER.fine("just parsed: " + requestSource);
            }
        } catch (SAXException e) {
            throw new WcsException(e, "XML getCoverage request SAX parsing error",
                XmlRequestReader.class.getName());
        } catch (IOException e) {
            throw new WcsException(e, "XML get coverage request input error",
                XmlRequestReader.class.getName());
        } catch (ParserConfigurationException e) {
            throw new WcsException(e, "Some sort of issue creating parser",
                XmlRequestReader.class.getName());
        }

        Request r = contentHandler.getRequest(req);

        if (r.getService() != null) {
            final String service = r.getService();

            if (!service.trim().toUpperCase().startsWith("WCS")) {
                throw new WcsException("SERVICE parameter is wrong.");
            }
        } else {
            throw new WcsException("SERVICE parameter is mandatory.");
        }

        if (r.getVersion() != null) {
            final String version = r.getVersion();

            if (!version.equals("1.0.0")) {
                throw new WcsException("VERSION parameter is wrong.");
            }
        } else {
            throw new WcsException("VERSION parameter is mandatory.");
        }

        if (r.getRequest() != null) {
            final String requestType = r.getRequest();

            if (!requestType.equalsIgnoreCase("GetCoverage")) {
                throw new WcsException("REQUEST parameter is wrong.");
            }
        } else {
            throw new WcsException("REQUEST parameter is mandatory.");
        }

        return r;
    }
}
