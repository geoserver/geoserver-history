package org.geoserver.wfs;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.util.Arrays;

import org.geoserver.util.ReaderUtils;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class GetFeatureTest extends WFSTestSupport {

	GetFeature getFeature;
	
	protected void setUp() throws Exception {
		super.setUp();
		
		//register the GetFeature operation
		getFeature = new GetFeature( catalog, wfs );
		getFeature.setApplicationContext( context );
		
		//register the gml2 producer
		context.getBeanFactory().registerSingleton( 
			"gml2FeatureProducer", new GML2FeatureProducer( getFeature ) 
		);
	}
	
	public void testGetFeature() throws Exception {
		getFeature.setMaxFeatures( 10 );
		getFeature.setOutputFormat( GML2FeatureProducer.formatName );
		
		Query query = new Query();
		query.setPropertyNames( Arrays.asList( new String[] {"ID","the_geom"} ) );
		query.setTypeName( BASIC_POLYGONS_TYPE );
		getFeature.addQuery( query );
		
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		getFeature.setOutputStream( outputStream );
		
		getFeature.getFeature();
		
		Element fcElement = ReaderUtils.parse( 
			new InputStreamReader( new ByteArrayInputStream( outputStream.toByteArray() ) )
		);
		assertEquals( "wfs:FeatureCollection", fcElement.getNodeName() );
		
		NodeList featureMemberElements = 
			fcElement.getElementsByTagName( "gml:featureMember" );
		assertEquals( 3, featureMemberElements.getLength() );
	}
	
}
