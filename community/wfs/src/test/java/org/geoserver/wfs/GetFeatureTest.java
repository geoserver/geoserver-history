package org.geoserver.wfs;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.namespace.QName;

import org.geoserver.util.ReaderUtils;
import org.geotools.filter.FilterFactory;
import org.geotools.filter.FilterFactoryFinder;
import org.opengis.filter.expression.PropertyName;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class GetFeatureTest extends WFSTestSupport {

	GetFeature getFeature;
	FilterFactory filterFactory;
	
	protected void setUp() throws Exception {
		super.setUp();
		
		filterFactory = FilterFactoryFinder.createFilterFactory();
		
		//register the GetFeature operation
		getFeature = new GetFeature( wfs, catalog );
		getFeature.setApplicationContext( context );
		getFeature.setFilterFactory( filterFactory );
		
		//register the gml2 producer
		context.getBeanFactory().registerSingleton( 
			"gml2FeatureProducer", new GML2FeatureProducer( wfs, catalog ) 
		);
	}
	
	public void testGetFeature() throws Exception {
		getFeature.setMaxFeatures( BigInteger.valueOf( 10 ) );
		getFeature.setOutputFormat( GML2FeatureProducer.formatName );
		
		List propertyNames = new ArrayList();
		propertyNames.add( filterFactory.property( "ID" ) );
		propertyNames.add( filterFactory.property( "the_geom" ) );
		getFeature.setPropertyName( Arrays.asList( new List[] { propertyNames } ) );
		
		getFeature.setTypeName( 
			Arrays.asList( new QName[] { new QName( CITE_URI, "BasicPolygons", CITE_PREFIX ) } ) 
		);
		
		GetFeatureResults results = getFeature.getFeature();
		
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		GML2FeatureProducer producer = new GML2FeatureProducer( wfs, catalog );
		producer.produce( GML2FeatureProducer.formatName, results, outputStream );
		
		Element fcElement = ReaderUtils.parse( 
			new InputStreamReader( new ByteArrayInputStream( outputStream.toByteArray() ) )
		);
		assertEquals( "wfs:FeatureCollection", fcElement.getNodeName() );
		
		NodeList featureMemberElements = 
			fcElement.getElementsByTagName( "gml:featureMember" );
		assertEquals( 3, featureMemberElements.getLength() );
	}
	
}
