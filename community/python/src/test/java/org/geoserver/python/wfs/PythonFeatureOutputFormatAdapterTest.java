package org.geoserver.python.wfs;

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.geoserver.platform.GeoServerResourceLoader;
import org.geoserver.python.Python;
import org.junit.BeforeClass;
import org.junit.Test;

public class PythonFeatureOutputFormatAdapterTest {

    static Python py;
    static PythonFeatureOutputFormatAdapter adapter;
    
    @BeforeClass
    public static void setUpData() throws Exception {
        GeoServerResourceLoader loader = new GeoServerResourceLoader(new File("target"));
        py = new Python(loader);
        
        File f = new File("target", "foo_outputformat.py");
        FileUtils.copyURLToFile(PythonFeatureOutputFormatAdapterTest.class.getResource("foo_outputformat.py"), f);
        adapter = new PythonFeatureOutputFormatAdapter(f, py);
    }
    
    @Test
    public void testGetName() {
        assertEquals("Foo", adapter.getName());
    }
    
    @Test
    public void testGetMimeType() {
        assertEquals("text/plain", adapter.getMimeType());
    }
}
