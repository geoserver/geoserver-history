package org.geoserver.wms;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;


public class CapabilitiesTest extends WMSTestSupport {
    
    public CapabilitiesTest() {
        super();
    }

    public void testCapabilities() throws Exception {
        Document dom = getAsDOM("wms?request=getCapabilities");
        Element e = dom.getDocumentElement();
        assertEquals("WMT_MS_Capabilities", e.getLocalName());
    }
    
    protected Document dom(InputStream input) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance(); 
        factory.setNamespaceAware( true );
        factory.setValidating( false );
       
        DocumentBuilder builder = factory.newDocumentBuilder();
        builder.setEntityResolver(new EmptyResolver());
        Document dom = builder.parse( input );

        return dom;
    }
    
    /**
     * Resolves everything to an empty xml document, useful for skipping errors due to missing
     * dtds and the like
     * @author Andrea Aime - TOPP
     */
    static class EmptyResolver implements org.xml.sax.EntityResolver {
        public InputSource resolveEntity(String publicId, String systemId)
                throws org.xml.sax.SAXException, IOException {
            StringReader reader = new StringReader(
                    "<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            InputSource source = new InputSource(reader);
            source.setPublicId(publicId);
            source.setSystemId(systemId);

            return source;
        }
    }
}
