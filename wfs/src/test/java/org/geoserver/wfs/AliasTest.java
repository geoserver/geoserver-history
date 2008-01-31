package org.geoserver.wfs;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.xml.namespace.QName;

import org.geoserver.data.test.MockData;
import org.vfny.geoserver.global.Data;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class AliasTest extends WFSTestSupport {

    private Data catalog;
    
    @Override
    protected void populateDataDirectory(MockData dataDirectory) throws Exception {
        addAliasedType(dataDirectory, MockData.FIFTEEN, "ft15");
    }

    private void addAliasedType(MockData dataDirectory, QName name, String alias)
            throws IOException {
        URL properties = MockData.class.getResource(name.getLocalPart() + ".properties");
        URL style = MockData.class.getResource(name.getLocalPart() + ".sld");
        String styleName = null;
        if(style != null) {
            styleName = name.getLocalPart();
            dataDirectory.addStyle(styleName, style);
        }
        dataDirectory.addPropertiesType(name, properties, styleName, alias);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        catalog = (Data) applicationContext.getBean("catalog");
    }

    public void testAliasFifteen() throws Exception {
        Document doc = getAsDOM("wfs?request=GetFeature&typename=cdf:ft15&version=1.0.0&service=wfs");
        assertEquals("wfs:FeatureCollection", doc.getDocumentElement().getNodeName());

        assertTrue(doc.getElementsByTagName("gml:featureMember").getLength() > 0);
        assertTrue(doc.getElementsByTagName("cdf:ft15").getLength() > 0);
    }
    
    public void testGetByFeatureId() throws Exception {
        Document doc = getAsDOM("wfs?request=GetFeature&typename=cdf:ft15&version=1.0.0&featureId=ft15.1");
        assertEquals("wfs:FeatureCollection", doc.getDocumentElement().getNodeName());

        assertEquals(1, doc.getElementsByTagName("gml:featureMember").getLength());
        final NodeList features = doc.getElementsByTagName("cdf:ft15");
        assertEquals(1, features.getLength());
        Node feature = features.item(0);
        final Node fidNode = feature.getAttributes().getNamedItem("fid");
        assertEquals("ft15.1", fidNode.getTextContent());
    }
    
    
}
