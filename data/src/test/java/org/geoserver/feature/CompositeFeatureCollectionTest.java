package org.geoserver.feature;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.Feature;
import org.geotools.feature.FeatureType;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureFactoryImpl;
import org.geotools.feature.simple.SimpleTypeBuilder;
import org.geotools.feature.simple.SimpleTypeFactoryImpl;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;

import junit.framework.TestCase;

public class CompositeFeatureCollectionTest extends TestCase {

	CompositeFeatureCollection collection;
	
	protected void setUp() throws Exception {
		List collections = new ArrayList();
		
		SimpleTypeBuilder typeBuilder = new SimpleTypeBuilder( new SimpleTypeFactoryImpl() );
		SimpleFeatureBuilder builder = new SimpleFeatureBuilder( new SimpleFeatureFactoryImpl() );
		
		for ( int i = 1; i < 5; i++ ) {
			typeBuilder.init();
			typeBuilder.setName( "test" + i );
			typeBuilder.setNamespaceURI( "http://www.geotools.org/test" + i );
			typeBuilder.addAttribute( "foo" + i, Integer.class );
			typeBuilder.addAttribute( "geom" + i, Point.class );
			FeatureType type = typeBuilder.feature();
			
			DefaultFeatureCollection collection = new DefaultFeatureCollection( "fc" + i, null ){};
			for ( int j = 1; j < 10; j++ ) {
				builder.init();
				builder.setType( type );
				builder.add( new Integer( i*j ) );
				builder.add( new GeometryFactory().createPoint( new Coordinate( i*j, i*j ) ) );
				
				Feature feature = builder.feature( "fid" + i*j );	
				collection.add( feature );
			}
			
			collections.add( collection );
		}
		
		DefaultFeatureCollection empty = new DefaultFeatureCollection( "empty", null ){};
		collections.add( 2, empty );
		collections.add( empty );
		collection = new CompositeFeatureCollection( collections );
	}
	
	public void testIterator() {
		Iterator itr = collection.iterator();
		
		for ( int i = 1; i < 5; i++ ) {
			for ( int j = 1; j < 10; j++ ) {
				assertTrue( itr.hasNext() );
				itr.next();
			}
		}
		
		assertFalse( itr.hasNext() );
		collection.close( itr );
	}
	
	public void testCount() throws Exception {
		assertEquals( 4*9, collection.getCount() );
	}
}
