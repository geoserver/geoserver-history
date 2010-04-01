package org.geoserver.gss;

import static org.custommonkey.xmlunit.XMLAssert.*;
import static org.geoserver.gss.DefaultGeoServerSynchronizationService.*;

import java.io.IOException;
import java.util.Collections;

import org.geotools.data.FeatureSource;
import org.geotools.data.Query;
import org.geotools.data.VersioningDataStore;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;
import org.w3c.dom.Document;

import com.mockrunner.mock.web.MockHttpServletResponse;

public class PostDiffTest extends GSSTestSupport {
    
    @Override
    protected void setUpInternal() throws Exception {
        super.setUpInternal();
    }
    
    public void testUnknownLayer() throws Exception {
        MockHttpServletResponse response = postAsServletResponse(root(true), loadTextResource("PostDiffUnknown.xml"));
        validate(response);
        Document dom = dom(response);
        // print(dom);
        checkOws10Exception(dom, "InvalidParameterValue");
    }
    
    public void testInvalidFromVersion() throws Exception {
        MockHttpServletResponse response = postAsServletResponse(root(true), loadTextResource("PostDiffInvalidFrom.xml"));
        validate(response);
        Document dom = dom(response);
        checkOws10Exception(dom, "InvalidParameterValue");
    }
    
    public void testEmpty() throws Exception {
        MockHttpServletResponse response = postAsServletResponse(root(true), loadTextResource("PostDiffEmpty.xml"));
        validate(response);
        Document dom = dom(response);
        
        // basic response checks
        assertXpathExists("/gss:PostDiffResponse", dom);
        assertXpathEvaluatesTo("true", "/gss:PostDiffResponse/@success", dom);
        
        // check the lasts known Central revision is updated
        VersioningDataStore synch = (VersioningDataStore) getCatalog().getDataStoreByName("synch").getDataStore(null);
        FeatureSource<SimpleFeatureType, SimpleFeature> fs = synch.getFeatureSource(SYNCH_HISTORY);
        FeatureCollection fc = fs.getFeatures(ff.equals(ff.property("table_name"), ff.literal("restricted")));
        FeatureIterator fi = fc.features();
        SimpleFeature f = (SimpleFeature) fi.next();
        fi.close();
        
        assertEquals(12l, f.getAttribute("central_revision"));
    }

    public void testFirstSynch() throws Exception {
        // grab the datastore so that we can assess the initial situation
        VersioningDataStore ds = (VersioningDataStore) getCatalog().getDataStoreByName("synch").getDataStore(null);
        FeatureSource<SimpleFeatureType, SimpleFeature> restricted = ds.getFeatureSource("restricted");
        assertEquals(4, restricted.getCount(Query.ALL));
        
        MockHttpServletResponse response = postAsServletResponse(root(true), loadTextResource("PostDiffInitial.xml"));
        validate(response);
        
        Document dom = dom(response);
        // print(dom);
        
        // basic response checks
        assertXpathExists("/gss:PostDiffResponse", dom);
        assertXpathEvaluatesTo("true", "/gss:PostDiffResponse/@success", dom);
        
        // check from the datastore we actually have applied the diff
        // we should stil have 4 features (1 added, 1 removed, one modified)
        assertEquals(4, restricted.getCount(Query.ALL));
        SimpleFeature deleted = getFeatureById(restricted, "restricted.d91fe390-bdc7-4b22-9316-2cd6c8737ef5");
        assertNull(deleted);
        SimpleFeature updated = getFeatureById(restricted, "restricted.be7cafea-d0b7-4257-9b9c-1ed3de3f7ef4");
        assertNotNull(updated);
        assertEquals(-48l, updated.getAttribute("cat"));  
        SimpleFeature added = getFeatureById(restricted, "restricted.e9cba212-d79d-4569-aa0a-48f6b80539ee");
        assertNotNull(added);
        assertEquals(123l, added.getAttribute("cat"));
    }
    
    /**
     * Retrieves a single feature by id from the feature source. Returns null if no feature with that id is found
     */
    SimpleFeature getFeatureById(FeatureSource<SimpleFeatureType, SimpleFeature> fs, String id) throws IOException {
        Filter filter = ff.id(Collections.singleton(ff.featureId(id)));
        FeatureIterator<SimpleFeature> fi = null;
        try {
            fi = fs.getFeatures(filter).features();
            if(fi.hasNext()) {
                return fi.next();
            } else {
                return null;
            }
        } finally {
            fi.close();
        }
    }
}
