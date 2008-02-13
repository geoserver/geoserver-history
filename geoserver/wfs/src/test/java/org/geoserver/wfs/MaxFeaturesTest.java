package org.geoserver.wfs;

import org.geoserver.data.test.MockData;
import org.vfny.geoserver.global.Data;
import org.vfny.geoserver.global.FeatureTypeInfo;
import org.vfny.geoserver.global.GeoServer;
import org.vfny.geoserver.global.dto.GeoServerDTO;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

public class MaxFeaturesTest extends WFSTestSupport {

    private Data catalog;

    protected void setUp() throws Exception {
        super.setUp();
        // set global max to 5
        GeoServer gs = (GeoServer) applicationContext.getBean("geoServer");
        GeoServerDTO dto = (GeoServerDTO) gs.toDTO();
        dto.setMaxFeatures(5);
        gs.load(dto);
        
        catalog = (Data) applicationContext.getBean("catalog");
    }

    public void testGlobalMax() throws Exception {
        // fifteen has 15 elements, but global max is 5
        Document doc = getAsDOM("wfs?request=GetFeature&typename=cdf:Fifteen" +
        		"&version=1.0.0&service=wfs");
        assertEquals("wfs:FeatureCollection", doc.getDocumentElement().getNodeName());

        NodeList featureMembers = doc.getElementsByTagName("gml:featureMember");
        assertEquals(5, featureMembers.getLength());
    }
    
    public void testLocalMax() throws Exception {
        // setup different max on local
        FeatureTypeInfo info = catalog.getFeatureTypeInfo(MockData.FIFTEEN);
        info.setMaxFeatures(3);
        
        // fifteen has 15 elements, but global max is 5 and local is 3
        Document doc = getAsDOM("wfs?request=GetFeature&typename=cdf:Fifteen" +
        		"&version=1.0.0&service=wfs");
        assertEquals("wfs:FeatureCollection", doc.getDocumentElement().getNodeName());

        NodeList featureMembers = doc.getElementsByTagName("gml:featureMember");
        assertEquals(3, featureMembers.getLength());
    }
    
    public void testLocalMaxBigger() throws Exception {
        // setup different max on local
        FeatureTypeInfo info = catalog.getFeatureTypeInfo(MockData.FIFTEEN);
        info.setMaxFeatures(10);
        
        // fifteen has 15 elements, but global max is 5 and local is 10
        Document doc = getAsDOM("wfs?request=GetFeature&typename=cdf:Fifteen" +
        		"&version=1.0.0&service=wfs");
        assertEquals("wfs:FeatureCollection", doc.getDocumentElement().getNodeName());

        NodeList featureMembers = doc.getElementsByTagName("gml:featureMember");
        assertEquals(5, featureMembers.getLength());
    }
    
    public void testCombinedLocalMaxes() throws Exception {
        // fifteen has 15 features, basic polygons 3
        FeatureTypeInfo info = catalog.getFeatureTypeInfo(MockData.FIFTEEN);
        info.setMaxFeatures(2);
        info = catalog.getFeatureTypeInfo(MockData.BASIC_POLYGONS);
        info.setMaxFeatures(2);
        
        Document doc = getAsDOM("wfs?request=GetFeature&typename=cdf:Fifteen,cite:BasicPolygons" +
        		"&version=1.0.0&service=wfs");
        assertEquals("wfs:FeatureCollection", doc.getDocumentElement().getNodeName());

        assertEquals(4, doc.getElementsByTagName("gml:featureMember").getLength());
        assertEquals(2, doc.getElementsByTagName("cdf:Fifteen").getLength());
        assertEquals(2, doc.getElementsByTagName("cite:BasicPolygons").getLength());
    }
    
    public void testCombinedLocalMaxesBigger() throws Exception {
        // fifteen has 15 features, basic polygons 3
        FeatureTypeInfo info = catalog.getFeatureTypeInfo(MockData.FIFTEEN);
        info.setMaxFeatures(4);
        info = catalog.getFeatureTypeInfo(MockData.BASIC_POLYGONS);
        info.setMaxFeatures(2);
        
        Document doc = getAsDOM("wfs?request=GetFeature&typename=cdf:Fifteen,cite:BasicPolygons" +
        		"&version=1.0.0&service=wfs");
        assertEquals("wfs:FeatureCollection", doc.getDocumentElement().getNodeName());

        assertEquals(5, doc.getElementsByTagName("gml:featureMember").getLength());
        assertEquals(4, doc.getElementsByTagName("cdf:Fifteen").getLength());
        assertEquals(1, doc.getElementsByTagName("cite:BasicPolygons").getLength());
    }
    
    public void testCombinedLocalMaxesBiggerRequestOverride() throws Exception {
        // fifteen has 15 features, basic polygons 3
        FeatureTypeInfo info = catalog.getFeatureTypeInfo(MockData.FIFTEEN);
        info.setMaxFeatures(3);
        info = catalog.getFeatureTypeInfo(MockData.BASIC_POLYGONS);
        info.setMaxFeatures(2);
        
        Document doc = getAsDOM("wfs?request=GetFeature&typename=cdf:Fifteen,cite:BasicPolygon" +
        		"s&version=1.0.0&service=wfs&maxFeatures=4");
        assertEquals("wfs:FeatureCollection", doc.getDocumentElement().getNodeName());

        assertEquals(4, doc.getElementsByTagName("gml:featureMember").getLength());
        assertEquals(3, doc.getElementsByTagName("cdf:Fifteen").getLength());
        assertEquals(1, doc.getElementsByTagName("cite:BasicPolygons").getLength());
    }
    
    public void testMaxFeaturesBreak() throws Exception {
        // see http://jira.codehaus.org/browse/GEOS-1489
        FeatureTypeInfo info = catalog.getFeatureTypeInfo(MockData.FIFTEEN);
        info.setMaxFeatures(3);
        info = catalog.getFeatureTypeInfo(MockData.BASIC_POLYGONS);
        info.setMaxFeatures(2);
        
        Document doc = getAsDOM("wfs?request=GetFeature&typename=cdf:Fifteen,cite:BasicPolygon" +
                "s&version=1.0.0&service=wfs&maxFeatures=3");
        assertEquals("wfs:FeatureCollection", doc.getDocumentElement().getNodeName());

        assertEquals(3, doc.getElementsByTagName("gml:featureMember").getLength());
        assertEquals(3, doc.getElementsByTagName("cdf:Fifteen").getLength());
        assertEquals(0, doc.getElementsByTagName("cite:BasicPolygons").getLength());
    }
    
    
}
