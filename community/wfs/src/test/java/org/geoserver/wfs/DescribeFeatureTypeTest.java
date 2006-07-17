package org.geoserver.wfs;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.geoserver.util.ReaderUtils;
import org.w3c.dom.Element;

public class DescribeFeatureTypeTest extends WFSTestSupport {

	public void testDescribeFeatureType() throws Exception {
		
		DescribeFeatureType op = new DescribeFeatureType( catalog, wfs );
		
		List types = new ArrayList();
		types.add( BASIC_POLYGONS_TYPE );
		op.setRequestedTypes( types );
		
		op.setOutputFormat( "XMLSCHEMA" );
		
		String result = op.describeFeatureType();
		assertNotNull( result );
		System.out.println( result );
		
		Element schemaDoc = ReaderUtils.parse( new StringReader( result ) );
		assertEquals( 1, schemaDoc.getElementsByTagName( "xs:complexType" ).getLength() );
		
		Element ctElement = 
			 (Element) schemaDoc.getElementsByTagName( "xs:complexType" ).item( 0 );
		assertEquals( BASIC_POLYGONS_TYPE + "_Type", ctElement.getAttribute("name") );
	
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
		assertEquals( BASIC_POLYGONS_TYPE, eElement.getAttribute( "name" ) );
	}
}
