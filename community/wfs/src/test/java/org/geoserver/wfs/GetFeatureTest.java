package org.geoserver.wfs;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.util.Arrays;

import javax.xml.namespace.QName;

import net.opengis.wfs.FeatureCollectionType;
import net.opengis.wfs.GetFeatureType;
import net.opengis.wfs.QueryType;
import net.opengis.wfs.WFSFactory;

import org.geoserver.util.ReaderUtils;
import org.geotools.filter.FilterFactory;
import org.geotools.filter.FilterFactoryFinder;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class GetFeatureTest extends WFSTestSupport {

	public void testGetFeature() throws Exception {
		FilterFactory filterFactory = FilterFactoryFinder.createFilterFactory();
		
		GetFeatureType request = WFSFactory.eINSTANCE.createGetFeatureType();
		request.setMaxFeatures( BigInteger.valueOf( 10 ) );
		request.setOutputFormat( GML2FeatureProducer.formatName );
		
		QueryType query = WFSFactory.eINSTANCE.createQueryType();
		query.getPropertyName().add( "ID"  );
		query.getPropertyName().add( "the_geom" );
		query.setTypeName( 
			Arrays.asList( new QName[] { new QName( CITE_URI, "BasicPolygons", CITE_PREFIX ) } )	
		);
		request.getQuery().add( query );
		
		FeatureCollectionType results = webFeatureService.getFeature( request );
		
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
