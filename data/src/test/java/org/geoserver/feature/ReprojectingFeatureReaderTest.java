package org.geoserver.feature;

import java.util.Iterator;

import junit.framework.TestCase;

import org.geotools.data.FeatureReader;
import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.Feature;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureType;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureFactoryImpl;
import org.geotools.feature.simple.SimpleSchema;
import org.geotools.feature.simple.SimpleTypeBuilder;
import org.geotools.feature.simple.SimpleTypeFactoryImpl;
import org.geotools.geometry.jts.GeometryCoordinateSequenceTransformer;
import org.geotools.referencing.CRS;
import org.geotools.referencing.FactoryFinder;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform2D;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Point;

public class ReprojectingFeatureReaderTest extends TestCase {

	CoordinateReferenceSystem src,target;
	FeatureCollection fc;
	
	GeometryCoordinateSequenceTransformer transformer; 
	
	protected void setUp() throws Exception {
		src = CRS.parseWKT( 
			"GEOGCS[\"WGS 84\",DATUM[\"WGS_1984\",SPHEROID[\"WGS 84\",6378137,298.257223563,AUTHORITY[\"EPSG\",\"7030\"]],AUTHORITY[\"EPSG\",\"6326\"]],PRIMEM[\"Greenwich\",0,AUTHORITY[\"EPSG\",\"8901\"]],UNIT[\"degree\",0.01745329251994328,AUTHORITY[\"EPSG\",\"9122\"]],AUTHORITY[\"EPSG\",\"4326\"]]" 
		);
		
		SimpleTypeBuilder typeBuilder = new SimpleTypeBuilder( new SimpleTypeFactoryImpl() );
		typeBuilder.load( new SimpleSchema() );
		
		typeBuilder.setName( "test" );
		typeBuilder.setNamespaceURI( "test" );
		typeBuilder.setCRS( src );
		typeBuilder.addAttribute( "defaultGeom", Point.class );
		typeBuilder.addAttribute( "someAtt", Integer.class );
		typeBuilder.addAttribute( "otherGeom", LineString.class );
		typeBuilder.setGeometryName( "defaultGeom" );
		
		FeatureType featureType = typeBuilder.feature();
		
		SimpleFeatureBuilder builder = new SimpleFeatureBuilder( new SimpleFeatureFactoryImpl() );
		
		
		GeometryFactory gf = new GeometryFactory();
		fc = new DefaultFeatureCollection( "test", featureType ){};
		
		double x = -140;
		double y = 45;
		for ( int i = 0; i < 5; i++ ) {
			builder.init();
			builder.setType( featureType );
			
			Point point = gf.createPoint( new Coordinate( x+i, y+i ) );
			point.setUserData( src );
			
			builder.add( point );
			builder.add( new Integer( i ) );
			
			LineString line = gf.createLineString( new Coordinate[] { new Coordinate( x+i, y+i ), new Coordinate( x+i+1, y+i+1 ) } );
			line.setUserData( src );
			builder.add( line );
			
			fc.add( builder.feature( i + "" ) );
		}
		
		target = CRS.parseWKT(
			"PROJCS[\"BC_Albers\",GEOGCS[\"GCS_North_American_1983\",DATUM[\"North_American_Datum_1983\",SPHEROID[\"GRS_1980\",6378137,298.257222101],TOWGS84[0,0,0]],PRIMEM[\"Greenwich\",0],UNIT[\"Degree\",0.017453292519943295]],PROJECTION[\"Albers_Conic_Equal_Area\"],PARAMETER[\"False_Easting\",1000000],PARAMETER[\"False_Northing\",0],PARAMETER[\"Central_Meridian\",-126],PARAMETER[\"Standard_Parallel_1\",50],PARAMETER[\"Standard_Parallel_2\",58.5],PARAMETER[\"Latitude_Of_Origin\",45],UNIT[\"Meter\",1],AUTHORITY[\"EPSG\",\"42102\"]]"	
		);
		
		MathTransform2D tx = (MathTransform2D) FactoryFinder.getCoordinateOperationFactory(null)
			.createOperation(src,target).getMathTransform();
		transformer = new GeometryCoordinateSequenceTransformer();
		transformer.setMathTransform( tx );
	}

	public void testNormal() throws Exception {
	
		Iterator reproject = new ReprojectingFeatureCollection( fc, target ).iterator();
		Iterator reader = fc.iterator();
		
		
		while( reader.hasNext() ) {
			Feature normal = (Feature) reader.next();
			Feature reprojected = (Feature) reproject.next();
			
			Point p1 = (Point) normal.getAttribute( "defaultGeom" );
			p1 = (Point) transformer.transform( p1 );
			
			Point p2 = (Point) reprojected.getAttribute( "defaultGeom" );
			assertTrue( p1.equals( p2 ) );
			
			LineString l1 = (LineString) normal.getAttribute( "otherGeom" );
			l1 = (LineString) transformer.transform( l1 );
			
			LineString l2 = (LineString) reprojected.getAttribute( "otherGeom" );
			assertTrue( l1.equals( l2 ) );
		}
		
	}
	
	public void testWithDifferentCRS() throws Exception {
		CoordinateReferenceSystem src1 = CRS.parseWKT(
			"PROJCS[\"Victorian Government (Vicgrid94)\",GEOGCS[\"GDA94\",DATUM[\"Geocentric_Datum_of_Australia_1994\",SPHEROID[\"GRS 1980\",6378137,298.257222101,AUTHORITY[\"EPSG\",\"7019\"]],TOWGS84[0,0,0],AUTHORITY[\"EPSG\",\"6283\"]],PRIMEM[\"Greenwich\",0,AUTHORITY[\"EPSG\",\"8901\"]],UNIT[\"degree\",0.01745329251994328,AUTHORITY[\"EPSG\",\"9122\"]],AUTHORITY[\"EPSG\",\"4283\"]],PROJECTION[\"Lambert_Conformal_Conic_2SP\"],PARAMETER[\"central_meridian\",145.0],PARAMETER[\"latitude_of_origin\",-37.0],PARAMETER[\"standard_parallel_1\",-36.0],PARAMETER[\"standard_parallel_2\",-38.0],PARAMETER[\"false_easting\",2500000.0],PARAMETER[\"false_northing\",2500000.0],UNIT[\"Meter\",1],AUTHORITY[\"EPSG\",\"3111\"]]"
		);
			
		MathTransform2D tx = (MathTransform2D) FactoryFinder.getCoordinateOperationFactory(null)
		.createOperation(src1,target).getMathTransform();
		GeometryCoordinateSequenceTransformer transformer1 = new GeometryCoordinateSequenceTransformer();
		transformer1.setMathTransform( tx );
		
		int i = 0;
		for ( Iterator itr = fc.iterator(); itr.hasNext(); i++) {
			Feature f = (Feature) itr.next();
			if ( i % 2 != 0 ) continue;
			
			for ( int j = 0; j < f.getNumberOfAttributes(); j++ ) {
				Object object = f.getAttribute( j );
				if ( object instanceof Geometry ) {
					((Geometry) object).setUserData( src1 );
				}
			}
		}
		
		Iterator reproject = new ReprojectingFeatureCollection( fc, target ).iterator();
		
		Iterator reader = fc.iterator();
		
		i = 0;
		
		while( reader.hasNext() ) {
			Feature normal = (Feature) reader.next();
			Feature reprojected = (Feature) reproject.next();
			
			if ( i % 2 != 0 ) {
				Point p1 = (Point) normal.getAttribute( "defaultGeom" );
				p1 = (Point) transformer.transform( p1 );
				
				Point p2 = (Point) reprojected.getAttribute( "defaultGeom" );
				assertTrue( p1.equals( p2 ) );
				
				LineString l1 = (LineString) normal.getAttribute( "otherGeom" );
				l1 = (LineString) transformer.transform( l1 );
				
				LineString l2 = (LineString) reprojected.getAttribute( "otherGeom" );
				assertTrue( l1.equals( l2 ) );
			}
			else {
				Point p1 = (Point) normal.getAttribute( "defaultGeom" );
				p1 = (Point) transformer1.transform( p1 );
				
				Point p2 = (Point) reprojected.getAttribute( "defaultGeom" );
				assertTrue( p1.equals( p2 ) );
				
				LineString l1 = (LineString) normal.getAttribute( "otherGeom" );
				l1 = (LineString) transformer1.transform( l1 );
				
				LineString l2 = (LineString) reprojected.getAttribute( "otherGeom" );
				assertTrue( l1.equals( l2 ) );
			}
			
			i++;
		}
	}

	public void testReprojectDefaultFalse() throws Exception {
		
		int i = 0;
		for ( Iterator itr = fc.iterator(); itr.hasNext(); i++) {
			Feature f = (Feature) itr.next();
			if ( i % 2 != 0 ) continue;
			
			for ( int j = 0; j < f.getNumberOfAttributes(); j++ ) {
				Object object = f.getAttribute( j );
				if ( object instanceof Geometry ) {
					((Geometry) object).setUserData( null );
				}
			}
		}
		
		Iterator reproject = new ReprojectingFeatureCollection( fc, target ).iterator();
		
		Iterator reader = fc.iterator();
		
		i = 0;
		
		while( reader.hasNext() ) {
			Feature normal = (Feature) reader.next();
			Feature reprojected = (Feature) reproject.next();
			
			if ( i % 2 != 0 ) {
				Point p1 = (Point) normal.getAttribute( "defaultGeom" );
				p1 = (Point) transformer.transform( p1 );
				
				Point p2 = (Point) reprojected.getAttribute( "defaultGeom" );
				assertTrue( p1.equals( p2 ) );
				
				LineString l1 = (LineString) normal.getAttribute( "otherGeom" );
				l1 = (LineString) transformer.transform( l1 );
				
				LineString l2 = (LineString) reprojected.getAttribute( "otherGeom" );
				assertTrue( l1.equals( l2 ) );
			}
			else {
				Point p1 = (Point) normal.getAttribute( "defaultGeom" );
				p1 = (Point) transformer.transform( p1 );
				
				Point p2 = (Point) reprojected.getAttribute( "defaultGeom" );
				assertFalse( p1.equals( p2 ) );
				
				LineString l1 = (LineString) normal.getAttribute( "otherGeom" );
				l1 = (LineString) transformer.transform( l1 );
				
				LineString l2 = (LineString) reprojected.getAttribute( "otherGeom" );
				assertFalse( l1.equals( l2 ) );
			}
			
			i++;
		}
	}
	
	public void testReprojectDefaultTrue() throws Exception {
		

		int i = 0;
		for ( Iterator itr = fc.iterator(); itr.hasNext(); i++) {
			Feature f = (Feature) itr.next();
			if ( i % 2 != 0 ) continue;
			for ( int j = 0; j < f.getNumberOfAttributes(); j++ ) {
				Object object = f.getAttribute( j );
				if ( object instanceof Geometry ) {
					((Geometry) object).setUserData( null );
				}
			}
		}
		
		ReprojectingFeatureCollection rfc = new ReprojectingFeatureCollection( fc, target );
		rfc.setDefaultSource( src );
		
		Iterator reproject = rfc.iterator();
		Iterator reader = fc.iterator();
		
		while( reader.hasNext() ) {
			Feature normal = (Feature) reader.next();
			Feature reprojected = (Feature) reproject.next();
			
			Point p1 = (Point) normal.getAttribute( "defaultGeom" );
			p1 = (Point) transformer.transform( p1 );
			
			Point p2 = (Point) reprojected.getAttribute( "defaultGeom" );
			assertTrue( p1.equals( p2 ) );
			
			LineString l1 = (LineString) normal.getAttribute( "otherGeom" );
			l1 = (LineString) transformer.transform( l1 );
			
			LineString l2 = (LineString) reprojected.getAttribute( "otherGeom" );
			assertTrue( l1.equals( l2 ) );
		}
		
	}
	
	
}
