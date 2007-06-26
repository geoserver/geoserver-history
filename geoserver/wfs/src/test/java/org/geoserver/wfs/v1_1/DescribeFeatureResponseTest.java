package org.geoserver.wfs.v1_1;

import java.io.ByteArrayOutputStream;
import java.io.StringReader;

import org.geoserver.data.test.MockData;
import org.geoserver.util.ReaderUtils;
import org.geoserver.wfs.WFSTestSupport;
import org.geoserver.wfs.xml.v1_1_0.XmlSchemaEncoder;
import org.vfny.geoserver.global.Data;
import org.vfny.geoserver.global.FeatureTypeInfo;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class DescribeFeatureResponseTest extends WFSTestSupport {

    public void testSingle() throws Exception {
        Data catalog = getCatalog();
        FeatureTypeInfo meta = catalog.getFeatureTypeInfo(MockData.BASIC_POLYGONS);

        ByteArrayOutputStream output = new ByteArrayOutputStream();

        XmlSchemaEncoder response = new XmlSchemaEncoder(getWFS(), catalog, getResourceLoader());
        response.write(new FeatureTypeInfo[] { meta }, output, null);

        Element schema = ReaderUtils.parse(new StringReader(new String(output
                .toByteArray())));
        assertEquals("xsd:schema", schema.getNodeName());

        NodeList types = schema.getElementsByTagName("xsd:complexType");
        assertEquals(1, types.getLength());
    }

    public void testWithDifferntNamespaces() throws Exception {

        FeatureTypeInfo meta1 = getCatalog().getFeatureTypeInfo(MockData.BASIC_POLYGONS);
        FeatureTypeInfo meta2 = getCatalog().getFeatureTypeInfo(MockData.POLYGONS);
        
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        XmlSchemaEncoder response = 
            new XmlSchemaEncoder(getWFS(), getCatalog(), getResourceLoader());
        response.write(new FeatureTypeInfo[] { meta1, meta2 }, output, null);

        Element schema = ReaderUtils.parse(new StringReader(new String(output
                .toByteArray())));
        assertEquals("xsd:schema", schema.getNodeName());

        NodeList imprts = schema.getElementsByTagName("xsd:import");
        assertEquals(2, imprts.getLength());

    }

}
