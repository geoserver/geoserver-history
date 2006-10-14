package org.geoserver.wfs;

import java.io.ByteArrayOutputStream;
import java.io.StringReader;
import java.util.ArrayList;

import javax.xml.namespace.QName;

import net.opengis.wfs.DescribeFeatureTypeType;
import net.opengis.wfs.WfsFactory;

import org.geoserver.data.feature.FeatureTypeInfo;
import org.geoserver.ows.Operation;
import org.geoserver.ows.Service;
import org.geoserver.util.ReaderUtils;
import org.geoserver.wfs.http.response.XmlSchemaEncoder;
import org.w3c.dom.Element;

public class DescribeFeatureTypeTest extends WFSTestSupport {

	public void testDescribeFeatureType() throws Exception {
		
		DescribeFeatureTypeType request = WfsFactory.eINSTANCE.createDescribeFeatureTypeType();
		
		request.getTypeName().add( qname( BASIC_POLYGONS_TYPE ) );
		request.setOutputFormat( "XMLSCHEMA" );
		
		FeatureTypeInfo[] infos = webFeatureService.describeFeatureType( request );
		
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		new XmlSchemaEncoder( wfs, catalog ).encode( infos, output );
		
		String result = new String( output.toByteArray() );
		Element schemaDoc = ReaderUtils.parse( new StringReader ( result ) );
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
	
	QName qname( String name ) {
		return new QName( CITE_URI, name, CITE_PREFIX );
	}
}
