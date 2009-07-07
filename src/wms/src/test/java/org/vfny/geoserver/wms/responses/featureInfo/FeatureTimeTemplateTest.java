package org.vfny.geoserver.wms.responses.featureinfo;

import junit.framework.Test;

import org.geoserver.data.test.MockData;
import org.geoserver.wms.WMSTestSupport;
import org.geotools.data.FeatureSource;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.vfny.geoserver.wms.responses.featureInfo.FeatureTimeTemplate;

public class FeatureTimeTemplateTest extends WMSTestSupport {

    static SimpleFeature feature;
    
    /**
     * This is a READ ONLY TEST so we can use one time setup
     */
    public static Test suite() {
        return new OneTimeTestSetup(new FeatureTimeTemplateTest());
    }
    
    
    protected void oneTimeSetUp() throws Exception {
        super.oneTimeSetUp();
    
        FeatureSource<SimpleFeatureType, SimpleFeature> source = getFeatureSource(MockData.PRIMITIVEGEOFEATURE);
        FeatureCollection<SimpleFeatureType, SimpleFeature> features = source.getFeatures();
        FeatureIterator <SimpleFeature> iterator = features.features();
        while( iterator.hasNext() ) {
            SimpleFeature f = iterator.next();
            if ( f.getAttribute("dateProperty") != null ) {
                feature = f;
                break;
            }
        }
        features.close(iterator);
    }
    
    public void testEmpty() throws Exception {
        FeatureTimeTemplate template = new FeatureTimeTemplate();
        String[] result = template.execute( feature );
        
        assertEquals( 0, result.length );
    }
    
    public void testTimestamp() throws Exception {
        setupTemplate(MockData.PRIMITIVEGEOFEATURE,"time.ftl","${dateProperty.value}");
        
        FeatureTimeTemplate template = new FeatureTimeTemplate();
        String[] result = template.execute( feature );
        
        assertEquals( 1, result.length );
        assertNotNull( result[0] );
    }
    
    public void testTimeSpan() throws Exception {
        setupTemplate(MockData.PRIMITIVEGEOFEATURE,"time.ftl","${dateProperty.value}||${dateProperty.value}");
        FeatureTimeTemplate template = new FeatureTimeTemplate();
        String[] result = template.execute( feature );
        
        assertEquals( 2, result.length );
        assertNotNull( result[0] );
        assertNotNull( result[1] );
    }
    
    public void testTimeSpanOpenEndedStart() throws Exception {
        setupTemplate(MockData.PRIMITIVEGEOFEATURE,"time.ftl","||${dateProperty.value}");
        FeatureTimeTemplate template = new FeatureTimeTemplate();
        String[] result = template.execute( feature );
        
        assertEquals( 2, result.length );
        assertNull( result[0] );
        assertNotNull( result[1] );
    }
    
    public void testTimeSpanOpenEndedEnd() throws Exception {
        setupTemplate(MockData.PRIMITIVEGEOFEATURE,"time.ftl","${dateProperty.value}||");
        FeatureTimeTemplate template = new FeatureTimeTemplate();
        String[] result = template.execute( feature );
        
        assertEquals( 2, result.length );
        assertNotNull( result[0] );
        assertNull( result[1] );
    }
}
