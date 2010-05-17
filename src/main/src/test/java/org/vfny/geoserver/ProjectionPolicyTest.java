package org.vfny.geoserver;

import java.util.HashMap;
import java.util.Map;

import org.geoserver.catalog.FeatureTypeInfo;
import org.geoserver.catalog.ProjectionPolicy;
import org.geoserver.data.test.MockData;
import org.geoserver.test.GeoServerTestSupport;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.referencing.CRS;
import org.opengis.feature.Feature;

public class ProjectionPolicyTest extends GeoServerTestSupport {

    @Override
    protected void populateDataDirectory(MockData dataDirectory) throws Exception {
        Map props = new HashMap();
        props.put(MockData.KEY_SRS_HANDLINGS, ProjectionPolicy.FORCE_DECLARED);
        props.put(MockData.KEY_SRS_NUMBER, 4269);
        dataDirectory.addWellKnownType(MockData.BASIC_POLYGONS, props);
        
        props.put(MockData.KEY_SRS_HANDLINGS, ProjectionPolicy.REPROJECT_TO_DECLARED);
        props.put(MockData.KEY_SRS_NUMBER, 3003);
        dataDirectory.addWellKnownType(MockData.LAKES, props);
        
        props.put(MockData.KEY_SRS_HANDLINGS, ProjectionPolicy.NONE);
        props.put(MockData.KEY_SRS_NUMBER, 3004);
        dataDirectory.addWellKnownType(MockData.LINES, props);
    }
    
    public void testForce() throws Exception {
        FeatureTypeInfo fti = getCatalog().getFeatureTypeByName(MockData.BASIC_POLYGONS.getLocalPart());
        assertEquals("EPSG:4269", fti.getSRS());
        assertEquals(ProjectionPolicy.FORCE_DECLARED, fti.getProjectionPolicy());
        FeatureCollection fc = fti.getFeatureSource(null, null).getFeatures();
        assertEquals(CRS.decode("EPSG:4269"), fc.getSchema().getCoordinateReferenceSystem());
        FeatureIterator fi = fc.features();
        Feature f = fi.next();
        fi.close();
        assertEquals(CRS.decode("EPSG:4269"), f.getType().getCoordinateReferenceSystem());
    }
    
    public void testReproject() throws Exception {
        FeatureTypeInfo fti = getCatalog().getFeatureTypeByName(MockData.LAKES.getLocalPart());
        assertEquals("EPSG:3003", fti.getSRS());
        assertEquals(ProjectionPolicy.REPROJECT_TO_DECLARED, fti.getProjectionPolicy());
        FeatureCollection fc = fti.getFeatureSource(null, null).getFeatures();
        assertEquals(CRS.decode("EPSG:3003"), fc.getSchema().getCoordinateReferenceSystem());
        FeatureIterator fi = fc.features();
        Feature f = fi.next();
        fi.close();
        assertEquals(CRS.decode("EPSG:3003"), f.getType().getCoordinateReferenceSystem());
    }
    
    public void testLeaveNative() throws Exception {
        FeatureTypeInfo fti = getCatalog().getFeatureTypeByName(MockData.LINES.getLocalPart());
        assertEquals("EPSG:3004", fti.getSRS());
        assertEquals(ProjectionPolicy.NONE, fti.getProjectionPolicy());
        FeatureCollection fc = fti.getFeatureSource(null, null).getFeatures();
        assertEquals(CRS.decode("EPSG:32615"), fc.getSchema().getCoordinateReferenceSystem());
        FeatureIterator fi = fc.features();
        Feature f = fi.next();
        fi.close();
        assertEquals(CRS.decode("EPSG:32615"), f.getType().getCoordinateReferenceSystem());
    }
}