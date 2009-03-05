package org.geoserver.catalog.rest;

import java.io.File;
import java.net.URL;

import org.apache.commons.io.FileUtils;
import org.geoserver.test.GeoServerTestSupport;

import com.mockrunner.mock.web.MockHttpServletResponse;

public class CoverageStoreFileUploadTest extends GeoServerTestSupport {

    public void testWorldImageUploadZipped() throws Exception {
        URL zip = getClass().getResource( "test-data/usa.zip" );
        byte[] bytes = FileUtils.readFileToByteArray( new File( zip.getFile() ) );
        
        MockHttpServletResponse response = 
            putAsServletResponse( "/rest/workspaces/gs/coveragestores/usa/file.worldimage", bytes, "application/zip");
        assertEquals( 201, response.getStatusCode() );
    }
}
