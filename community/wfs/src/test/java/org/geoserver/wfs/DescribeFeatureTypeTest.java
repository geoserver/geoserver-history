package org.geoserver.wfs;

import java.io.ByteArrayOutputStream;
import java.io.StringReader;

import javax.xml.namespace.QName;

import net.opengis.wfs.DescribeFeatureTypeType;
import net.opengis.wfs.WFSFactory;

import org.geoserver.data.feature.FeatureTypeInfo;
import org.geoserver.data.test.MockGeoServerDataDirectory;
import org.geoserver.util.ReaderUtils;
import org.geoserver.wfs.xml.v1_0_0.XmlSchemaEncoder;
import org.w3c.dom.Element;

public class DescribeFeatureTypeTest extends WFSTestSupport {

	public void testDescribeFeatureType() throws Exception {
		
		DescribeFeatureTypeType request = WFSFactory.eINSTANCE.createDescribeFeatureTypeType();
		
		request.getTypeName().add( MockGeoServerDataDirectory.BASIC_POLYGONS );
		request.setOutputFormat( "XMLSCHEMA" );
		
		FeatureTypeInfo[] infos = webFeatureService.describeFeatureType( request );
		
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		new XmlSchemaEncoder( wfs, catalog ).encode( infos, output );
		
		String result = new String( output.toByteArray() );
		Element schemaDoc = ReaderUtils.parse( new StringReader ( result ) );
		assertEquals( 1, schemaDoc.getElementsByTagName( "xs:complexType" ).getLength() );
		
		Element ctElement = 
			 (Element) schemaDoc.getElementsByTagName( "xs:complexType" ).item( 0 );
		assertEquals( MockGeoServerDataDirectory.BASIC_POLYGONS.getLocalPart() + "_Type", ctElement.getAttribute("name") );
	
		assertEquals( 
			2, ctElement.getElementsByTagName("xs:element").getLength() 
		);
		Element geomElement = 
			(Element) ctElement.getElementsByTagName( "xs:element" ).item( 0 );
		Element idElement = 
			(Element) ctElement.getElementsByTagName( "xs:element" ).item( 1 );
		
		assertEquals( "the_geom", geomElement.getAttribute( "name" ) );
		assertEquals( "ID", idElement.getAttribute( "name" ) );
	
		Element eElement = 
			(Element) schemaDoc.getElementsByTagName( "xs:element" ).item( 2 );
		assertEquals( MockGeoServerDataDirectory.BASIC_POLYGONS.getLocalPart(), eElement.getAttribute( "name" ) );
	}
}
