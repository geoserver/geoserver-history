package org.geoserver.wps;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.dom.DOMSource;

import org.custommonkey.xmlunit.SimpleNamespaceContext;
import org.custommonkey.xmlunit.XMLUnit;
import org.geoserver.test.GeoServerTestSupport;
import org.geoserver.wps.xml.WPSConfiguration;
import org.geotools.xml.Configuration;
import org.geotools.xml.Parser;
import org.w3c.dom.Document;
import org.xml.sax.SAXParseException;

public abstract class WPSTestSupport extends GeoServerTestSupport {

    static {
        // init xmlunit
        Map<String, String> namespaces = new HashMap<String, String>();
        namespaces.put("wps", "http://www.opengis.net/wps/1.0.0");
        namespaces.put("ows", "http://www.opengis.net/ows/1.1");
        namespaces.put("gml", "http://www.opengis.net/gml");
        namespaces.put("wfs", "http://www.opengis.net/wfs");
        namespaces.put("xlink", "http://www.w3.org/1999/xlink");
        
        XMLUnit.setXpathNamespaceContext(new SimpleNamespaceContext(namespaces));
    }
    
    protected String root() {
        return "wps?";
    }
    
    /**
     * Validates a document based on the WPS schema 
     * @throws TransformerException 
     * @throws ParserConfigurationException 
     */
    protected void checkValidationErrors(Document dom) throws Exception {
        checkValidationErrors(dom, new WPSConfiguration());
    }

    /**
     * Validates a document against the 
     * @param dom
     * @param configuration
     */
    protected void checkValidationErrors(Document dom, Configuration configuration) throws Exception {
        Parser p = new Parser(configuration);
        p.setValidating( true );
        p.parse( new DOMSource( dom ) );
    
        if ( !p.getValidationErrors().isEmpty() ) {
            for ( Iterator e = p.getValidationErrors().iterator(); e.hasNext(); ) {
                SAXParseException ex = (SAXParseException) e.next();
                System.out.println( ex.getLineNumber() + "," + ex.getColumnNumber() + " -" + ex.toString()  );
            }
            fail( "Document did not validate.");
        }
    }
}
