package org.geoserver.wcs;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.geoserver.wcs.test.WCSTestSupport;
import org.w3c.dom.Document;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import com.mockrunner.mock.web.MockHttpServletResponse;

public class GetCapabilitiesTest extends WCSTestSupport {
    
    public void testBasicCapabilities() throws Exception {
        MockHttpServletResponse response = getAsServletResponse(BASEPATH + "?request=GetCapabilities&service=WCS");
        InputStream is = new ByteArrayInputStream( response.getOutputStreamContent().getBytes() );
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance(); 
        factory.setNamespaceAware( true );
        factory.setValidating(true);
        factory.setAttribute( "http://java.sun.com/xml/jaxp/properties/schemaLanguage", 
        "http://www.w3.org/2001/XMLSchema");
        
        
        DocumentBuilder builder = factory.newDocumentBuilder();
        builder.setErrorHandler(new ErrorHandler() {
        
            public void warning(SAXParseException exception) throws SAXException {
                System.out.println(exception.getMessage());
            }
        
            public void fatalError(SAXParseException exception) throws SAXException {
                exception.printStackTrace();
//                fail();
            }
        
            public void error(SAXParseException exception) throws SAXException {
                exception.printStackTrace();
//                fail();
            }
        
        });
        Document dom = builder.parse( is );

        print(dom);
    }
}
