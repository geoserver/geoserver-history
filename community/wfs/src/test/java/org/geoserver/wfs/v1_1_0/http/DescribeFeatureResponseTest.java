package org.geoserver.wfs.v1_1_0.http;

import java.io.ByteArrayOutputStream;
import java.io.StringReader;

import org.geoserver.data.feature.FeatureTypeInfo;
import org.geoserver.util.ReaderUtils;
import org.geoserver.wfs.WFSTestSupport;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class DescribeFeatureResponseTest extends WFSTestSupport {

	public void testSingle() throws Exception {
		FeatureTypeInfo meta = catalog.featureType( CITE_PREFIX, BASIC_POLYGONS_TYPE );
		
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		
		XmlSchemaEncoder response = new XmlSchemaEncoder( wfs, catalog );
		response.encode( new FeatureTypeInfo[]{ meta  }, output );
		
		Element schema = 
			ReaderUtils.parse( new StringReader( new String( output.toByteArray() ) ) );
		assertEquals( "xsd:schema", schema.getNodeName() );
		
		NodeList types = schema.getElementsByTagName( "xsd:complexType" );
		assertEquals( 1, types.getLength() );
	}
	
	public void testWithDifferntNamespaces() throws Exception {
		
		FeatureTypeInfo meta1 = catalog.featureType( CITE_PREFIX, BASIC_POLYGONS_TYPE );
		FeatureTypeInfo meta2 = catalog.featureType( CGF_PREFIX, POLYGONS_TYPE );
		
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		
		XmlSchemaEncoder response = new XmlSchemaEncoder( wfs, catalog );
		response.encode( new FeatureTypeInfo[]{ meta1, meta2 }, output );
		
		Element schema = 
			ReaderUtils.parse( new StringReader( new String( output.toByteArray() ) ) );
		assertEquals( "xsd:schema", schema.getNodeName() );
		
		NodeList imprts = schema.getElementsByTagName( "xsd:import" );
		assertEquals( 2, imprts.getLength() );
	
	}
	
	
}
