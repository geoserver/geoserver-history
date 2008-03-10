package org.getoserver.wfsv;

import org.w3c.dom.Document;
import static org.custommonkey.xmlunit.XMLAssert.*;

public class DescribeVersionedFeatureTypeTest extends WFSVTestSupport {

    @Override
    protected String getLogConfiguration() {
        return "/DEFAULT_LOGGING.properties";
    }
    
    public void testDescribeArcsitesPost10() throws Exception {
        String request = "<DescribeVersionedFeatureType\r\n" + 
        		"  version=\"1.0.0\"\r\n" + 
        		"  service=\"WFSV\" versioned=\"true\"\r\n" + 
        		"  xmlns=\"http://www.opengis.net/wfsv\"\r\n" + 
        		"  xmlns:wfs=\"http://www.opengis.net/wfs\"\r\n" + 
        		"  xmlns:topp=\"http://www.openplans.org/topp\"\r\n" + 
        		"  xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\r\n" + 
        		"  xsi:schemaLocation=\"http://www.opengis.net/wfsv http://localhost:8080/geoserver/schemas/wfs/1.0.0/WFS-versioning.xsd\">\r\n" + 
        		"    <wfs:TypeName>topp:archsites</wfs:TypeName>\r\n" + 
        		"</DescribeVersionedFeatureType>";
        Document dom = postAsDOM(root(), request);
        assertXpathEvaluatesTo("http://www.opengis.net/wfsv", "/xs:schema/xs:import/@namespace", dom);
        assertXpathEvaluatesTo("wfsv:AbstractVersionedFeatureType", "/xs:schema/xs:complexType/xs:complexContent/xs:extension/@base", dom);
    }
    
    public void testDescribeArcsitesGet10() throws Exception {
        String request = root() + "?service=wfsv&version=1.0.0&request=DescribeVersionedFeatureType&typeName=topp:archsites";
        Document dom = getAsDOM(request);
        assertXpathEvaluatesTo("http://www.opengis.net/wfsv", "/xs:schema/xs:import/@namespace", dom);
        assertXpathEvaluatesTo("wfsv:AbstractVersionedFeatureType", "/xs:schema/xs:complexType/xs:complexContent/xs:extension/@base", dom);
    }
    
    public void testDescribeArcsitesPost11() throws Exception {
        String request = "<DescribeVersionedFeatureType\r\n" + 
                "  version=\"1.1.0\"\r\n" + 
                "  service=\"WFSV\" versioned=\"true\"\r\n" + 
                "  xmlns=\"http://www.opengis.net/wfsv\"\r\n" + 
                "  xmlns:wfs=\"http://www.opengis.net/wfs\"\r\n" + 
                "  xmlns:topp=\"http://www.openplans.org/topp\"\r\n" + 
                "  xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\r\n" + 
                "  xsi:schemaLocation=\"http://www.opengis.net/wfsv http://localhost:8080/geoserver/schemas/wfs/1.1.0/wfsv.xsd\">\r\n" + 
                "    <wfs:TypeName>topp:archsites</wfs:TypeName>\r\n" + 
                "</DescribeVersionedFeatureType>";
        Document dom = postAsDOM(root(), request);
        assertXpathEvaluatesTo("http://www.opengis.net/wfsv", "/xs:schema/xs:import/@namespace", dom);
        assertXpathEvaluatesTo("wfsv:AbstractVersionedFeatureType", "/xs:schema/xs:complexType/xs:complexContent/xs:extension/@base", dom);
    }
    
    public void testDescribeArcsitesGet11() throws Exception {
        String request = root() + "?service=wfsv&version=1.1.0&request=DescribeVersionedFeatureType&typeName=topp:archsites";
        Document dom = getAsDOM(request);
        assertXpathEvaluatesTo("http://www.opengis.net/wfsv", "/xs:schema/xs:import/@namespace", dom);
        assertXpathEvaluatesTo("wfsv:AbstractVersionedFeatureType", "/xs:schema/xs:complexType/xs:complexContent/xs:extension/@base", dom);
    }
    
    
}
