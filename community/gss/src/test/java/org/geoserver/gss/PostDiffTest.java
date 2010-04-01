package org.geoserver.gss;

import static java.util.Collections.*;
import static org.custommonkey.xmlunit.XMLAssert.*;
import static org.geoserver.gss.DefaultGeoServerSynchronizationService.*;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.custommonkey.xmlunit.exceptions.XpathException;
import org.geotools.data.FeatureSource;
import org.geotools.data.Query;
import org.geotools.data.VersioningDataStore;
import org.geotools.data.VersioningFeatureStore;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Id;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.mockrunner.mock.web.MockHttpServletResponse;

public class PostDiffTest extends GSSTestSupport {

    @Override
    protected void setUpInternal() throws Exception {
        super.setUpInternal();
    }
    
    public void testConfictSchema() throws IOException {
         SimpleFeatureType ft = synchStore.getSchema("synch_conflicts");
         // we expect the pk to be exposed, as it's a multi column one
         assertEquals(7, ft.getAttributeCount());
    }

    public void testUnknownLayer() throws Exception {
        MockHttpServletResponse response = postAsServletResponse(root(true),
                loadTextResource("PostDiffUnknown.xml"));
        validate(response);
        Document dom = dom(response);
        // print(dom);
        checkOws10Exception(dom, "InvalidParameterValue");
    }

    public void testInvalidFromVersion() throws Exception {
        MockHttpServletResponse response = postAsServletResponse(root(true),
                loadTextResource("PostDiffInvalidFrom.xml"));
        validate(response);
        Document dom = dom(response);
        checkOws10Exception(dom, "InvalidParameterValue");
    }

    public void testEmpty() throws Exception {
        MockHttpServletResponse response = postAsServletResponse(root(true),
                loadTextResource("PostDiffEmpty.xml"));
        checkPositiveResponse(response);

        // check the lasts known Central revision is updated
        VersioningDataStore synch = (VersioningDataStore) getCatalog().getDataStoreByName("synch")
                .getDataStore(null);
        FeatureSource<SimpleFeatureType, SimpleFeature> fs = synch.getFeatureSource(SYNCH_HISTORY);
        FeatureCollection fc = fs.getFeatures(ff.equals(ff.property("table_name"), ff
                .literal("restricted")));
        FeatureIterator fi = fc.features();
        SimpleFeature f = (SimpleFeature) fi.next();
        fi.close();

        assertEquals(12l, f.getAttribute("central_revision"));
    }

    public void testFirstSynch() throws Exception {
        // grab the datastore so that we can assess the initial situation
        FeatureSource<SimpleFeatureType, SimpleFeature> restricted = synchStore
                .getFeatureSource("restricted");
        assertEquals(4, restricted.getCount(Query.ALL));

        // get the response and do the basic checks
        MockHttpServletResponse response = postAsServletResponse(root(true),
                loadTextResource("PostDiffInitial.xml"));
        checkPositiveResponse(response);
        checkPostDiffInitialChanges(restricted);

        // check there are no conflicts
        assertEquals(0, gss.getActiveConflicts("restricted").size());
    }

    public void testDeleteConflict() throws Exception {
        // grab the datastore so that we can make some changes that will generate conflicts
        VersioningFeatureStore restricted = (VersioningFeatureStore) synchStore.getFeatureSource("restricted");
        restricted.removeFeatures(ff.id(singleton(ff.featureId("restricted.be7cafea-d0b7-4257-9b9c-1ed3de3f7ef4"))));
        assertEquals(3, restricted.getCount(Query.ALL));
     
        // get the response and do the basic checks
        MockHttpServletResponse response = postAsServletResponse(root(true),
                loadTextResource("PostDiffInitial.xml"));
        checkPositiveResponse(response);
        checkPostDiffInitialChanges(restricted);
        
        // check we actually have stored the deletion conflict (the deleted feature has been updated
        // by central
        FeatureCollection<SimpleFeatureType, SimpleFeature> activeConflicts = gss.getActiveConflicts("restricted");
        assertEquals(1, activeConflicts.size());
        FeatureIterator<SimpleFeature> fi = activeConflicts.features();
        SimpleFeature f = fi.next();
        fi.close();
        
        assertEquals("restricted", f.getAttribute("table_name"));
        assertEquals("be7cafea-d0b7-4257-9b9c-1ed3de3f7ef4", f.getAttribute("feature_id"));
        assertEquals(false, f.getAttribute("resolved"));
        assertNull(f.getAttribute("local_feature"));
    }
    
    public void testUpdateConflict() throws Exception {
        // grab the datastore so that we can make some changes that will generate conflicts
        VersioningFeatureStore restricted = (VersioningFeatureStore) synchStore.getFeatureSource("restricted");
        SimpleFeatureType schema = restricted.getSchema();
        Id fidFilter = ff.id(singleton(ff.featureId("restricted.be7cafea-d0b7-4257-9b9c-1ed3de3f7ef4")));
        restricted.modifyFeatures(schema.getDescriptor("cat"), 123456, fidFilter);
        assertEquals(4, restricted.getCount(Query.ALL));
     
        // get the response and do the basic checks
        MockHttpServletResponse response = postAsServletResponse(root(true),
                loadTextResource("PostDiffInitial.xml"));
        checkPositiveResponse(response);
        checkPostDiffInitialChanges(restricted);
        
        // check we actually have stored the update conflict 
        FeatureCollection<SimpleFeatureType, SimpleFeature> activeConflicts = gss.getActiveConflicts("restricted");
        assertEquals(1, activeConflicts.size());
        FeatureIterator<SimpleFeature> fi = activeConflicts.features();
        SimpleFeature f = fi.next();
        fi.close();
        
        assertEquals("restricted", f.getAttribute("table_name"));
        assertEquals("be7cafea-d0b7-4257-9b9c-1ed3de3f7ef4", f.getAttribute("feature_id"));
        assertEquals(false, f.getAttribute("resolved"));
        assertNotNull(f.getAttribute("local_feature"));
        // TODO: make sure the stored feature is the value before the rollback
        
        SimpleFeature preConflict = gss.fromGML3((String) f.getAttribute("local_feature"));
        assertEquals(123456l, preConflict.getAttribute("cat"));
    }
    
    /**
     * Checks the changes in PostDiffInitial.xml have all been applied successfully
     * @param restricted
     * @throws IOException
     */
    void checkPostDiffInitialChanges(
            FeatureSource<SimpleFeatureType, SimpleFeature> restricted) throws IOException {
        // check from the datastore we actually have applied the diff
        // we should stil have 4 features (1 added, 1 removed, one modified)
        assertEquals(4, restricted.getCount(Query.ALL));
        SimpleFeature deleted = gss.getFeatureById(restricted,
                "restricted.d91fe390-bdc7-4b22-9316-2cd6c8737ef5");
        assertNull(deleted);
        SimpleFeature updated = gss.getFeatureById(restricted,
                "restricted.be7cafea-d0b7-4257-9b9c-1ed3de3f7ef4");
        assertNotNull(updated);
        assertEquals(-48l, updated.getAttribute("cat"));
        SimpleFeature added = gss.getFeatureById(restricted,
                "restricted.e9cba212-d79d-4569-aa0a-48f6b80539ee");
        assertNotNull(added);
        assertEquals(123l, added.getAttribute("cat"));
    }

    /**
     * Makes sure the response is a valid and positive PostDiff one
     * @param response
     * @throws Exception
     * @throws IOException
     * @throws SAXException
     * @throws ParserConfigurationException
     * @throws XpathException
     */
    Document checkPositiveResponse(MockHttpServletResponse response) throws Exception,
            IOException, SAXException, ParserConfigurationException, XpathException {
        validate(response);
        Document dom = dom(response);
        // print(dom);

        // basic response checks
        assertXpathExists("/gss:PostDiffResponse", dom);
        assertXpathEvaluatesTo("true", "/gss:PostDiffResponse/@success", dom);
        
        return dom;
    }
}
