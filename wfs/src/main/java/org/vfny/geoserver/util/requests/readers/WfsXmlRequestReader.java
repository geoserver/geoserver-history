package org.vfny.geoserver.util.requests.readers;

import java.io.IOException;
import java.io.Reader;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.geotools.filter.FilterFilter;
import org.geotools.gml.GMLFilterDocument;
import org.geotools.gml.GMLFilterGeometry;
import org.opengis.filter.Filter;
import org.vfny.geoserver.servlets.AbstractService;
import org.vfny.geoserver.util.requests.FilterHandlerImpl;
import org.vfny.geoserver.wfs.WfsException;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.ParserAdapter;

public abstract class WfsXmlRequestReader extends XmlRequestReader {

	
	/**
	 * Constructs the new wfs xml reader.
	 * 
	 * @param service Reference to the service handing a reuqest.
	 */
	public WfsXmlRequestReader(AbstractService service) {
		super(service);
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
        throws WfsException {
        // translate string into a proper SAX input source
        InputSource requestSource = new InputSource(rawRequest);

        // instantiante parsers and content handlers
        FilterHandlerImpl contentHandler = new FilterHandlerImpl();
        FilterFilter filterParser = new FilterFilter(contentHandler, null);
        GMLFilterGeometry geometryFilter = new GMLFilterGeometry(filterParser);
        GMLFilterDocument documentFilter = new GMLFilterDocument(geometryFilter);

        // read in XML file and parse to content handler
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();
            ParserAdapter adapter = new ParserAdapter(parser.getParser());

            adapter.setContentHandler(documentFilter);
            adapter.parse(requestSource);
            LOGGER.fine("just parsed: " + requestSource);
        } catch (SAXException e) {
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

        LOGGER.fine("passing filter: " + contentHandler.getFilter());

        return contentHandler.getFilter();
    }

}
