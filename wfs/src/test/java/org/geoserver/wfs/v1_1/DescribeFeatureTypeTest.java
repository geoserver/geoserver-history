package org.geoserver.wfs.v1_1;

import junit.framework.Test;

import org.geoserver.data.test.MockData;
import org.geoserver.wfs.WFSTestSupport;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class DescribeFeatureTypeTest extends WFSTestSupport {
    
    /**
     * This is a READ ONLY TEST so we can use one time setup
     */
    public static Test suite() {
        return new OneTimeTestSetup(new DescribeFeatureTypeTest());
    }
    
    @Override
    protected void populateDataDirectory(MockData dataDirectory) throws Exception {
        super.populateDataDirectory(dataDirectory);
        dataDirectory.disableDataStore(MockData.CITE_PREFIX);
    }

    public void testDateMappings() throws Exception {
        
        String xml = "<wfs:DescribeFeatureType " + "service=\"WFS\" "
        + "version=\"1.1.0\" "
        + "xmlns:wfs=\"http://www.opengis.net/wfs\" " 
        + "xmlns:sf=\"" + MockData.PRIMITIVEGEOFEATURE.getNamespaceURI() + "\">"
        + " <wfs:TypeName>sf:" + MockData.PRIMITIVEGEOFEATURE.getLocalPart() + "</wfs:TypeName>"
        + "</wfs:DescribeFeatureType>";

        Document doc = postAsDOM("wfs", xml);
        //print( doc );
        assertEquals("xsd:schema", doc.getDocumentElement()
                .getNodeName());
        
        NodeList elements = doc.getElementsByTagName("xsd:element");
        boolean date = false;
        boolean dateTime = false;
        
        for ( int i = 0; i < elements.getLength(); i++) {
            Element e = (Element) elements.item( i );
            if ( "dateProperty".equals( e.getAttribute("name")) ) {
                date = "xsd:date".equals( e.getAttribute("type" ) );
            }
            if ( "dateTimeProperty".equals( e.getAttribute("name")) ) {
                dateTime = "xsd:dateTime".equals( e.getAttribute("type" ) );
            }
            
        }
        
        assertTrue( date );
        assertTrue( dateTime );

    }
    
    public void testNoNamespaceDeclaration() throws Exception {
        String xml = "<wfs:DescribeFeatureType " + "service=\"WFS\" "
        + "version=\"1.1.0\" "
        + "xmlns:wfs=\"http://www.opengis.net/wfs\">"
        + " <wfs:TypeName>sf:" + MockData.PRIMITIVEGEOFEATURE.getLocalPart() + "</wfs:TypeName>"
        + "</wfs:DescribeFeatureType>";
        
        Document doc = postAsDOM("wfs", xml);
        //print( doc );
        
        // with previous code missing namespace would have resulted in a service exception
        assertEquals("xsd:schema", doc.getDocumentElement()
                .getNodeName());
    }
    
    public void testMultipleTypesImport() throws Exception {
        String xml = "<wfs:DescribeFeatureType " //
                + "service=\"WFS\" " //
                + "version=\"1.1.0\" " //
                + "xmlns:wfs=\"http://www.opengis.net/wfs\" " //
                + "xmlns:sf=\"" + MockData.PRIMITIVEGEOFEATURE.getNamespaceURI() + "\">" //
                + "<wfs:TypeName>sf:" + MockData.PRIMITIVEGEOFEATURE.getLocalPart() //
                + "</wfs:TypeName>" //
                + "<wfs:TypeName>sf:" + MockData.GENERICENTITY.getLocalPart() //
                + "</wfs:TypeName>" //
                + "</wfs:DescribeFeatureType>";
        Document doc = postAsDOM("wfs", xml);
        print(doc);
        assertEquals("xsd:schema", doc.getDocumentElement().getNodeName());
        NodeList nodes = doc.getDocumentElement().getChildNodes();
        boolean seenComplexType = false;
        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);
            if (node.getNodeName().equals("xsd:complexType")) {
                seenComplexType = true;
            } else if (seenComplexType && node.getNodeName().equals("xsd:import")) {
                fail("All xsd:import must occur before all xsd:complexType");
            }
        }
    }
    
}
