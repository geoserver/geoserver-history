package org.geoserver.wps;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.XMLConstants;
import javax.xml.transform.dom.DOMSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.custommonkey.xmlunit.SimpleNamespaceContext;
import org.custommonkey.xmlunit.XMLUnit;
import org.custommonkey.xmlunit.XpathEngine;
import org.geoserver.test.GeoServerTestSupport;
import org.w3c.dom.Document;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class WPSTestSupport extends GeoServerTestSupport {

    protected static final XpathEngine xpath;
    protected static final Schema WPS_SCHEMA;
    
    static {
        try {
            final SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            WPS_SCHEMA = factory.newSchema(new File("./schemas/wps/1.0.0/wpsAll.xsd"));
        } catch(Exception e) {
            throw new RuntimeException("Could not parse the WCS 1.1.1 schemas", e);
        }

        // init xmlunit
        Map<String, String> namespaces = new HashMap<String, String>();
        namespaces.put("wps", "http://www.opengis.net/wps/1.0.0");
        namespaces.put("ows", "http://www.opengis.net/ows/1.1");
        namespaces.put("gml", "http://www.opengis.net/gml");
        namespaces.put("xlink", "http://www.w3.org/1999/xlink");
        
        XMLUnit.setXpathNamespaceContext(new SimpleNamespaceContext(namespaces));
        xpath = XMLUnit.newXpathEngine();
    }
    
    protected String root() {
        return "wps?";
    }
    
    /**
     * Workaround for XSD bindings to encode the xml:lang attribute 
     */
    protected void checkValidationErrors(Document dom, Schema schema, String... skips) throws SAXException, IOException {
        final Validator validator = schema.newValidator();
        final List<Exception> validationErrors = new ArrayList<Exception>();
        validator.setErrorHandler(new ErrorHandler() {
            
            public void warning(SAXParseException exception) throws SAXException {
                System.out.println(exception.getMessage());
            }

            public void fatalError(SAXParseException exception) throws SAXException {
                validationErrors.add(exception);
            }

            public void error(SAXParseException exception) throws SAXException {
                validationErrors.add(exception);
            }

          });
        validator.validate(new DOMSource(dom));
        int errorCount = 0;
        StringBuilder sb = new StringBuilder();
        if (validationErrors != null && validationErrors.size() > 0) {
            for (Exception ve : validationErrors) {
                boolean skipError = false;
                for (String skip : skips) {
                    if(ve.getMessage().contains(skip))
                        skipError = true;
                }
                if(!skipError) {
                    sb.append(ve.getMessage()).append("\n");
                    errorCount++;
                }
            }
        }
        if(errorCount > 0)
            fail(sb.toString());
    }
}
