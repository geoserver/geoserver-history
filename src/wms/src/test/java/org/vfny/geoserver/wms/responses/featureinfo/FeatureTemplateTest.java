package org.vfny.geoserver.wms.responses.featureinfo;

import java.util.Iterator;

import junit.framework.Test;

import org.geoserver.data.test.MockData;
import org.geoserver.wms.WMSTestSupport;
import org.geotools.data.FeatureSource;
import org.geotools.feature.FeatureCollection;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.vfny.geoserver.wms.responses.featureInfo.FeatureTemplate;
import org.vfny.geoserver.wms.responses.featureinfo.dummy.Dummy;

public class FeatureTemplateTest extends WMSTestSupport {
    
    /**
     * This is a READ ONLY TEST so we can use one time setup
     */
    public static Test suite() {
        return new OneTimeTestSetup(new FeatureTemplateTest());
    }
    

    public void testWithDateAndBoolean() throws Exception {

        FeatureSource<SimpleFeatureType, SimpleFeature> source = getFeatureSource( MockData.PRIMITIVEGEOFEATURE );
        FeatureCollection<SimpleFeatureType, SimpleFeature> fc = source.getFeatures();
        Iterator i = fc.iterator();
        try {
            SimpleFeature f = (SimpleFeature) i.next();
            
            FeatureTemplate template = new FeatureTemplate();
            try {
                template.description( f );    
            }
            catch ( Exception e ) {
                e.printStackTrace();
                fail("template threw exception on null value");
            }
        }
        finally {
            fc.close( i );
        }
    }
     
    public void testRawValue() throws Exception {
        FeatureSource<SimpleFeatureType, SimpleFeature> source = getFeatureSource(MockData.PRIMITIVEGEOFEATURE);
        FeatureCollection<SimpleFeatureType, SimpleFeature> fc = source.getFeatures();
        Iterator i = fc.iterator();
        try {
            SimpleFeature f = (SimpleFeature) i.next();

            FeatureTemplate template = new FeatureTemplate();
            try {
                template.template(f, "rawValues.ftl", FeatureTemplateTest.class);
            } catch (Exception e) {
                e.printStackTrace();
                throw(e);
            }
        } finally {
            fc.close(i);
        }
    }

    public void testWithNull() throws Exception {
        
        FeatureSource<SimpleFeatureType, SimpleFeature> source = getFeatureSource( MockData.BASIC_POLYGONS );
        FeatureCollection<SimpleFeatureType, SimpleFeature> fc = source.getFeatures();
        Iterator i = fc.iterator();
        try {
            SimpleFeature f = (SimpleFeature) i.next();
            
            FeatureTemplate template = new FeatureTemplate();
            template.description( f );
            
            //set a value to null
            f.setAttribute(1,null);
            try {
                template.description( f );    
            }
            catch ( Exception e ) {
                e.printStackTrace();
                fail("template threw exception on null value");
            }
            
        }
        finally {
            fc.close( i );
        }
      
    }
    
    public void testAlternateLookup() throws Exception {
        
        FeatureSource<SimpleFeatureType, SimpleFeature> source = getFeatureSource( MockData.PRIMITIVEGEOFEATURE );
        FeatureCollection<SimpleFeatureType, SimpleFeature> fc = source.getFeatures();
        SimpleFeature f = fc.features().next();
        
        FeatureTemplate template = new FeatureTemplate();
        String result = template.template(f, "dummy.ftl", Dummy.class );
        
        assertEquals( "dummy", result );
    }
}
