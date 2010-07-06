package org.geoserver.wfs.v1_1;

import java.net.URLEncoder;

import javax.xml.namespace.QName;

import junit.framework.Test;

import org.geoserver.catalog.Catalog;
import org.geoserver.catalog.DataStoreInfo;
import org.geoserver.catalog.FeatureTypeInfo;
import org.geoserver.config.GeoServer;
import org.geoserver.data.test.MockData;
import org.geoserver.wfs.WFSInfo;
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
    protected void setUpInternal()throws Exception{
      getTestData().disableDataStore(MockData.CITE_PREFIX);  
    }
    
    @Override
    protected void populateDataDirectory(MockData dataDirectory) throws Exception {
        super.populateDataDirectory(dataDirectory);
        dataDirectory.disableDataStore(MockData.CITE_PREFIX);
    }

    public void testDateMappings() throws Exception {

        String xml = "<wfs:DescribeFeatureType " + "service=\"WFS\" " + "version=\"1.1.0\" "
                + "xmlns:wfs=\"http://www.opengis.net/wfs\" " + "xmlns:sf=\""
                + MockData.PRIMITIVEGEOFEATURE.getNamespaceURI() + "\">" + " <wfs:TypeName>sf:"
                + MockData.PRIMITIVEGEOFEATURE.getLocalPart() + "</wfs:TypeName>"
                + "</wfs:DescribeFeatureType>";

        Document doc = postAsDOM("wfs", xml);
        // print( doc );
        assertEquals("xsd:schema", doc.getDocumentElement().getNodeName());

        NodeList elements = doc.getElementsByTagName("xsd:element");
        boolean date = false;
        boolean dateTime = false;

        for (int i = 0; i < elements.getLength(); i++) {
            Element e = (Element) elements.item(i);
            if ("dateProperty".equals(e.getAttribute("name"))) {
                date = "xsd:date".equals(e.getAttribute("type"));
            }
            if ("dateTimeProperty".equals(e.getAttribute("name"))) {
                dateTime = "xsd:dateTime".equals(e.getAttribute("type"));
            }

        }

        assertTrue(date);
        assertTrue(dateTime);

    }

    public void testNoNamespaceDeclaration() throws Exception {
        String xml = "<wfs:DescribeFeatureType " + "service=\"WFS\" " + "version=\"1.1.0\" "
                + "xmlns:wfs=\"http://www.opengis.net/wfs\">" + " <wfs:TypeName>sf:"
                + MockData.PRIMITIVEGEOFEATURE.getLocalPart() + "</wfs:TypeName>"
                + "</wfs:DescribeFeatureType>";

        Document doc = postAsDOM("wfs", xml);
        // print( doc );

        // with previous code missing namespace would have resulted in a service exception
        assertEquals("xsd:schema", doc.getDocumentElement().getNodeName());
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

    /**
     * See http://jira.codehaus.org/browse/GEOS-3306
     * 
     * @throws Exception
     */
    public void testUerSuppliedTypeNameNamespace() throws Exception {
        final QName typeName = MockData.POLYGONS;
        String path = "ows?service=WFS&version=1.1.0&request=DescribeFeatureType&typeName=myPrefix:"
                + typeName.getLocalPart() + "&namespace=xmlns(myPrefix%3D"
                + URLEncoder.encode(typeName.getNamespaceURI(), "UTF-8") + ")";
        Document doc = getAsDOM(path);
        //print(doc);
        assertEquals("xsd:schema", doc.getDocumentElement().getNodeName());
    }

    /**
     * See http://jira.codehaus.org/browse/GEOS-3306
     * 
     * @throws Exception
     */
    public void testUerSuppliedTypeNameDefaultNamespace() throws Exception {
        final QName typeName = MockData.POLYGONS;
        String path = "ows?service=WFS&version=1.1.0&request=DescribeFeatureType&typeName="
                + typeName.getLocalPart() + "&namespace=xmlns("
                + URLEncoder.encode(typeName.getNamespaceURI(), "UTF-8") + ")";
        Document doc = getAsDOM(path);
        //print(doc);
        assertEquals("xsd:schema", doc.getDocumentElement().getNodeName());
    }

    /**
     * See http://jira.codehaus.org/browse/GEOS-3306
     * 
     * @throws Exception
     */
    public void testCiteCompliance() throws Exception {
        final QName typeName = MockData.STREAMS;
        Catalog catalog = getCatalog();
        catalog.setDefaultNamespace(catalog.getNamespaceByURI(typeName.getNamespaceURI()));
        FeatureTypeInfo typeInfo = catalog.getFeatureTypeByName(typeName.getNamespaceURI(), typeName.getLocalPart());
        typeInfo.setEnabled(true);
        catalog.save(typeInfo);
        DataStoreInfo store = typeInfo.getStore();
        store.setEnabled(true);
        catalog.save(store);
        
        
        String path = "ows?service=WFS&version=1.1.0&request=DescribeFeatureType&typeName="
            + typeName.getLocalPart();
        Document doc;
    
        //first, non cite compliant mode should find the type even if namespace is not specified
        GeoServer geoServer = getGeoServer();
        WFSInfo service = geoServer.getService(WFSInfo.class);
        service.setCiteCompliant(false);
        geoServer.save(service);
        doc = getAsDOM(path);
        //print(doc);
        assertEquals("xsd:schema", doc.getDocumentElement().getNodeName());

        //then, in cite compliance more, it should not find the type name
        service.setCiteCompliant(true);
        geoServer.save(service);
        doc = getAsDOM(path);
        //print(doc);
        assertEquals("ows:ExceptionReport", doc.getDocumentElement().getNodeName());
    }
}
