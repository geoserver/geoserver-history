package org.geoserver.wfs.response;

import junit.framework.Test;
import junit.framework.TestResult;

import org.geoserver.test.GeoServerTestSupport;

public class Ogr2OgrWfsTest extends GeoServerTestSupport {

    public static Test suite() {
        return new OneTimeTestSetup(new Ogr2OgrWfsTest());
    }
    
    @Override
    public void run(TestResult result) {
        if (!Ogr2OgrTestUtil.isOgrAvailable())
            System.out.println("Skipping ogr2ogr wfs tests, ogr2ogr could not be found, " + getName());
        else
            super.run(result);
    }

    public void testSimpleRequest() throws Exception {
//        String request = "wfs?request=GetFeature&typename=" + getLayerId(MockData.BUILDINGS) + "&version=1.0.0&service=wfs&outputFormat=mapinfo-tab";
//        MockHttpServletResponse resp = getAsServletResponse(request);
//        assertEquals("application/zip", resp.getContentType());
//        
//        // check we got the expected entries
//        ZipInputStream zis = new ZipInputStream(new ByteArrayInputStream( resp.getOutputStreamContent().getBytes()));
//        Set<String> entryNames = new TreeSet<String>();
//        ZipEntry entry = null;
//        while((entry = zis.getNextEntry()) != null)
//            entry.getName();
//        zis.close();
//        
//        System.out.println(entryNames);
    }
}
