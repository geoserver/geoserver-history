/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wms.requests.readers;

import java.io.Reader;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.geoserver.wms.WMS;
import org.geotools.filter.FilterFilter;
import org.geotools.gml.GMLFilterDocument;
import org.geotools.gml.GMLFilterGeometry;
import org.opengis.filter.Filter;
import org.vfny.geoserver.util.requests.FilterHandlerImpl;
import org.vfny.geoserver.util.requests.readers.XmlRequestReader;
import org.xml.sax.InputSource;
import org.xml.sax.helpers.ParserAdapter;


public abstract class WmsXmlRequestReader extends XmlRequestReader {
    
    /**
     * Constructs the new wfs xml reader.
     *
     * @param service Reference to the service handing a reuqest.
     */
     public WmsXmlRequestReader(WMS wms) {
         super(wms.getServiceInfo());
     }

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
        throws Exception {
        // translate string into a proper SAX input source
        InputSource requestSource = new InputSource(rawRequest);

        // instantiante parsers and content handlers
        FilterHandlerImpl contentHandler = new FilterHandlerImpl();
        FilterFilter filterParser = new FilterFilter(contentHandler, null);
        GMLFilterGeometry geometryFilter = new GMLFilterGeometry(filterParser);
        GMLFilterDocument documentFilter = new GMLFilterDocument(geometryFilter);

        // read in XML file and parse to content handler
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser parser = factory.newSAXParser();
        ParserAdapter adapter = new ParserAdapter(parser.getParser());

        adapter.setContentHandler(documentFilter);
        adapter.parse(requestSource);
        LOGGER.fine("just parsed: " + requestSource);
        LOGGER.fine("passing filter: " + contentHandler.getFilter());

        return contentHandler.getFilter();
    }
}
