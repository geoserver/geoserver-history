/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.requests.readers.wfs;

import org.geotools.gml.*;
import org.vfny.geoserver.requests.*;
import org.vfny.geoserver.requests.readers.*;
import org.vfny.geoserver.requests.wfs.*;
import org.vfny.geoserver.responses.wfs.*;
import org.xml.sax.*;
import org.xml.sax.helpers.*;
import java.io.*;
import javax.xml.parsers.*;


/**
 * reads in a Transaction WFS request from an XML stream
 *
 * @author Rob Hranac, TOPP
 * @author Chris Holmes, TOPP
 * @author Gabriel Roldán
 * @version 0.1
 */
public class TransactionXmlReader extends XmlRequestReader {
    /**
     * Reads the Transaction XML request into a TransactionRequest object.
     *
     * @param reader The plain POST text from the client.
     *
     * @return The read TransactionRequest object.
     *
     * @throws WfsTransactionException For any problems reading the request.
     */
    public Request read(Reader reader) throws WfsTransactionException {
        // translate string into a proper SAX input source
        InputSource requestSource = new InputSource(reader);

        // instantiante parsers and content handlers
        TransactionHandler contentHandler = new TransactionHandler();
        TransactionFilterHandler filterParser = new TransactionFilterHandler(contentHandler,
                null);
        TransactionFeatureHandler featureParser = new TransactionFeatureHandler(filterParser);
        GMLFilterGeometry geometryFilter = new GMLFilterGeometry(featureParser);
        GMLFilterDocument documentFilter = new GMLFilterDocument(geometryFilter);

        // read in XML file and parse to content handler
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();
            ParserAdapter adapter = new ParserAdapter(parser.getParser());

            adapter.setContentHandler(documentFilter);
            LOGGER.finest("about to start parsing");
            adapter.parse(requestSource);
            LOGGER.finer("just parsed: " + requestSource);
        } catch (SAXException e) {
            //e.getCause().printStackTrace(System.out);
            //e.printStackTrace(System.out);
            throw new WfsTransactionException(e,
                "XML transaction request SAX parsing error",
                getClass().getName());
        } catch (IOException e) {
            throw new WfsTransactionException(e,
                "XML transaction request input error", getClass().getName());
        } catch (ParserConfigurationException e) {
            throw new WfsTransactionException(e,
                "Some sort of issue creating parser", getClass().getName());
        }

        return contentHandler.getRequest();
    }
}
