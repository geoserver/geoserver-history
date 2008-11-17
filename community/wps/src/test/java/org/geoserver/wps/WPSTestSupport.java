package org.geoserver.wps;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.XMLConstants;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.dom.DOMSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.custommonkey.xmlunit.SimpleNamespaceContext;
import org.custommonkey.xmlunit.XMLUnit;
import org.custommonkey.xmlunit.XpathEngine;
import org.geoserver.test.GeoServerTestSupport;
import org.geoserver.wps.xml.WPSConfiguration;
import org.geotools.xml.DOMParser;
import org.geotools.xml.Parser;
import org.w3c.dom.Document;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class WPSTestSupport extends GeoServerTestSupport {

//    protected static final XpathEngine xpath;
//    protected static final Schema WPS_SCHEMA;
//    
    static {
//        try {
//            final SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
//            WPS_SCHEMA = factory.newSchema(new File("./schemas/wps/1.0.0/wpsAll.xsd"));
//        } catch(Exception e) {
//            throw new RuntimeException("Could not parse the WCS 1.1.1 schemas", e);
//        }
//
        // init xmlunit
        Map<String, String> namespaces = new HashMap<String, String>();
        namespaces.put("wps", "http://www.opengis.net/wps/1.0.0");
        namespaces.put("ows", "http://www.opengis.net/ows/1.1");
        namespaces.put("gml", "http://www.opengis.net/gml");
        namespaces.put("wfs", "http://www.opengis.net/wfs");
        namespaces.put("xlink", "http://www.w3.org/1999/xlink");
        
        XMLUnit.setXpathNamespaceContext(new SimpleNamespaceContext(namespaces));
        //xpath = XMLUnit.newXpathEngine();
    }
    
    protected String root() {
        return "wps?";
    }
    
    /**
     * Workaround for XSD bindings to encode the xml:lang attribute 
     * @throws TransformerException 
     * @throws ParserConfigurationException 
     */
    protected void checkValidationErrors(Document dom) throws Exception {
        
        Parser p = new Parser( new WPSConfiguration() );
        p.setValidating( true );
        p.parse( new DOMSource( dom ) );
    
        if ( !p.getValidationErrors().isEmpty() ) {
            for ( Iterator e = p.getValidationErrors().iterator(); e.hasNext(); ) {
                System.out.println( e.next().toString() );
            }
            fail( "Document did not validate.");
        }
    }
}
