package org.geoserver.feature;

import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.geoserver.data.test.MockData;
import org.geoserver.test.GeoServerTestSupport;
import org.geotools.data.DefaultQuery;
import org.geotools.data.Query;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.FeatureCollection;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.Id;
import org.opengis.filter.identity.FeatureId;

public class PagingFeatureSourceTest extends GeoServerTestSupport {

    PagingFeatureSource source;
    
    protected void setUp() throws Exception {
        super.setUp();
        
        source = new PagingFeatureSource( getFeatureSource( MockData.FIFTEEN ), 5, 5 );
    }
    
   public void testFeatureCollection() throws IOException {
       FeatureCollection fc = source.getFeatures();
       assertEquals( 5, fc.size() );

       Iterator i = fc.iterator();
       for ( int x = 0; x < 5; x++ ) {
           assertTrue( i.hasNext() );
           SimpleFeature f = (SimpleFeature) i.next();
           assertTrue( f.getID().endsWith( "." + (x+5) ) );
       }
       assertFalse( i.hasNext() );
       
       FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);
       
       Set<FeatureId> fids = new HashSet<FeatureId>();
       for ( int x = 0; x < 6; x++ ) {
           fids.add( ff.featureId(MockData.FIFTEEN.getLocalPart() + "." + x ) );    
       }
       Id filter = ff.id( fids );
       fc = source.getFeatures( filter );
       assertEquals( 1, fc.size() );
       
       i = fc.iterator();
       assertTrue( i.hasNext() );
       SimpleFeature feature = (SimpleFeature) i.next();
       assertTrue( feature.getID().endsWith(".5"));
       
       assertFalse( i.hasNext() );
   }
   
   public void testCount() throws Exception {
       assertEquals( 5, source.getCount(Query.ALL));
       
       FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);
       
       Set<FeatureId> fids = new HashSet<FeatureId>();
       for ( int x = 1; x < 7; x++ ) {
           fids.add( ff.featureId(MockData.FIFTEEN.getLocalPart() + "." + x ) );    
       }
       Id filter = ff.id( fids );
       DefaultQuery query = new DefaultQuery( MockData.FIFTEEN.getLocalPart(), filter );
       assertEquals( 1, source.getCount( query ) );
   }
   
}
