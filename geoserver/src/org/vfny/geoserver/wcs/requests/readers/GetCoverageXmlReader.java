package org.vfny.geoserver.wcs.requests.readers;

import java.io.IOException;
import java.io.Reader;

import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

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
 * @author $Author: Simone Giannecchini (simboss_ml@tiscali.it) $ (last modification)
 */
public class GetCoverageXmlReader extends XmlRequestReader {
    public GetCoverageXmlReader() {
    }

    public Request read(Reader reader, HttpServletRequest req) throws WcsException {
        // translate string into a proper SAX input source
        InputSource requestSource = new InputSource(reader);

        // instantiante parsers and content handlers
        CoverageHandler contentHandler = new CoverageHandler();

        // read in XML file and parse to content handler
        try {
            LOGGER.finest("about to create parser");

            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();
            ParserAdapter adapter = new ParserAdapter(parser.getParser());
            LOGGER.finest("setting the content handler");
            LOGGER.finest("content handler = " + contentHandler);
            adapter.setContentHandler(contentHandler);
            LOGGER.finest("about to parse");
            LOGGER.finest("calling parse on " + requestSource);
            adapter.parse(requestSource);
            LOGGER.fine("just parsed: " + requestSource);
        } catch (SAXException e) {
            e.printStackTrace(System.out);
            throw new WcsException(e,
                "XML getCoverage request SAX parsing error",
                XmlRequestReader.class.getName());
        } catch (IOException e) {
            throw new WcsException(e, "XML get coverage request input error",
                XmlRequestReader.class.getName());
        } catch (ParserConfigurationException e) {
            throw new WcsException(e, "Some sort of issue creating parser",
                XmlRequestReader.class.getName());
        }

        Request r = contentHandler.getRequest(req);
        return r;
    }
}
