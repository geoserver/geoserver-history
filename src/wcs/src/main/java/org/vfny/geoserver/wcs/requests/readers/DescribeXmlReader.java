/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
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

import org.geoserver.wcs.WCSInfo;
import org.vfny.geoserver.Request;
import org.vfny.geoserver.util.requests.readers.XmlRequestReader;
import org.vfny.geoserver.wcs.WcsException;
import org.vfny.geoserver.wcs.requests.DescribeHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.ParserAdapter;


/**
 * reads in a DescribeCoverageType WCS request from a XML stream
 *
 * @author Rob Hranac, TOPP
 * @author Chris Holmes, TOPP
 * @author $Author: Alessio Fabiani (alessio.fabiani@gmail.com) $ (last modification)
 * @author $Author: Simone Giannecchini (simboss1@gmail.com) $ (last modification)
 * @version 0.1
 */
public class DescribeXmlReader extends XmlRequestReader {
    /**
     * Creates a new DescribeXmlReader object.
     */
    public DescribeXmlReader(WCSInfo wcs) {
        super(wcs);
    }

    /**
     * DOCUMENT ME!
     *
     * @param reader DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws WCSException DOCUMENT ME!
     */
    public Request read(Reader reader, HttpServletRequest req)
        throws WcsException {
        /** create a describe Coverage type request class to return */
        InputSource requestSource = new InputSource(reader);

        // instantiante parsers and content handlers
        DescribeHandler contentHandler = new DescribeHandler((WCSInfo)getService());

        // read in XML file and parse to content handler
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();
            ParserAdapter adapter = new ParserAdapter(parser.getParser());

            adapter.setContentHandler(contentHandler);
            adapter.parse(requestSource);
            LOGGER.finer("just parsed: " + requestSource);
        } catch (SAXException e) {
            throw new WcsException(e, "XML describe request parsing error", getClass().getName());
        } catch (IOException e) {
            throw new WcsException(e, "XML describe request input error", getClass().getName());
        } catch (ParserConfigurationException e) {
            throw new WcsException(e, "Some sort of issue creating parser", getClass().getName());
        }

        LOGGER.finer("about to return ");
        LOGGER.finer("returning " + contentHandler.getRequest(req));

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

            if (!requestType.equalsIgnoreCase("DescribeCoverage")) {
                throw new WcsException("REQUEST parameter is wrong.");
            }
        } else {
            throw new WcsException("REQUEST parameter is mandatory.");
        }

        return r;
    }
}
