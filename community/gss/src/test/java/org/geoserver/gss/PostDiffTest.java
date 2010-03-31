package org.geoserver.gss;

import static org.custommonkey.xmlunit.XMLAssert.*;

import org.geotools.data.Query;
import org.geotools.data.VersioningDataStore;
import org.w3c.dom.Document;

import com.mockrunner.mock.web.MockHttpServletResponse;

public class PostDiffTest extends GSSTestSupport {

    public void testFirstSynch() throws Exception {
        // grab the datastore so that we can assess the initial situation
        VersioningDataStore ds = (VersioningDataStore) getCatalog().getDataStoreByName("synch").getDataStore(null);
        assertEquals(4, ds.getFeatureSource("restricted").getCount(Query.ALL));
        
        MockHttpServletResponse response = postAsServletResponse(root(true), loadTextResource("PostDiffInitial.xml"));
        validate(response);
        
        Document dom = dom(response);
        print(dom);
        
        // basic response checks
        assertXpathExists("/gss:PostDiffResponse", dom);
        assertXpathEvaluatesTo("true", "/gss:PostDiffResponse/@success", dom);
        
        // check from the datastore we actually have applied the diff
        
    }
}
