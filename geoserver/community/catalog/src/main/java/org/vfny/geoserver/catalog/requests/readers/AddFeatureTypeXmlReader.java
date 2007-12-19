/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.catalog.requests.readers;

import org.vfny.geoserver.Request;
import org.vfny.geoserver.catalog.CatalogException;
import org.vfny.geoserver.catalog.requests.AddFeatureTypeHandler;
import org.vfny.geoserver.catalog.servlets.CATALOGService;
import org.vfny.geoserver.util.requests.readers.XmlRequestReader;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.ParserAdapter;
import java.io.IOException;
import java.io.Reader;
import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;


/**
 * reads in a AddFeatureType CATALOG request from a XML stream
 *
 * @author $Author: Alessio Fabiani (GeoSolutions)
 */
public class AddFeatureTypeXmlReader extends XmlRequestReader {
    /**
     * Creates a new AddFeatureTypeXmlReader object.
     */
    public AddFeatureTypeXmlReader(CATALOGService service) {
        super(service);
    }

    /**
     * DOCUMENT ME!
     *
     * @param reader DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws CATALOGException DOCUMENT ME!
     */
    public Request read(Reader reader, HttpServletRequest req)
        throws CatalogException {
        /** create a AddFeatureType request class to return */
        InputSource requestSource = new InputSource(reader);

        // instantiante parsers and content handlers
        AddFeatureTypeHandler contentHandler = new AddFeatureTypeHandler((CATALOGService) getServiceRef());

        // read in XML file and parse to content handler
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();
            ParserAdapter adapter = new ParserAdapter(parser.getParser());

            adapter.setContentHandler(contentHandler);
            adapter.parse(requestSource);
            LOGGER.finer("just parsed: " + requestSource);
        } catch (SAXException e) {
            throw new CatalogException(e, "XML AddFeatureType request parsing error",
                getClass().getName());
        } catch (IOException e) {
            throw new CatalogException(e, "XML AddFeatureType request input error",
                getClass().getName());
        } catch (ParserConfigurationException e) {
            throw new CatalogException(e, "Some sort of issue creating parser", getClass().getName());
        }

        LOGGER.finer("about to return ");
        LOGGER.finer("returning " + contentHandler.getRequest(req));

        Request r = contentHandler.getRequest(req);

        if (r.getService() != null) {
            final String service = r.getService();

            if (!service.trim().toUpperCase().startsWith("CATALOG")) {
                throw new CatalogException("SERVICE parameter is wrong.");
            }
        } else {
            throw new CatalogException("SERVICE parameter is mandatory.");
        }

        if (r.getVersion() != null) {
            final String version = r.getVersion();

            if (!version.equals("1.0.0")) {
                throw new CatalogException("VERSION parameter is wrong.");
            }
        } else {
            throw new CatalogException("VERSION parameter is mandatory.");
        }

        if (r.getRequest() != null) {
            final String requestType = r.getRequest();

            if (!requestType.equalsIgnoreCase("AddFeatureType")) {
                throw new CatalogException("REQUEST parameter is wrong.");
            }
        } else {
            throw new CatalogException("REQUEST parameter is mandatory.");
        }

        return r;
    }
}
