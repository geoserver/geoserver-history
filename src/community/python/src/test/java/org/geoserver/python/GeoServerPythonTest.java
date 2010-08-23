package org.geoserver.python;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import junit.framework.Test;

import org.geoserver.catalog.FeatureTypeInfo;
import org.geoserver.test.GeoServerTestSupport;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.h2.tools.DeleteDbFiles;
import org.opengis.referencing.operation.MathTransform;
import org.python.util.PythonInterpreter;

public class GeoServerPythonTest extends GeoServerTestSupport {

    /**
     * This is a READ ONLY TEST so we can use one time setup
     */
    public static Test suite() {
        return new OneTimeTestSetup(new GeoServerPythonTest());
    }
    
    static Python python;
    PythonInterpreter pi;
    ByteArrayOutputStream out;
    
    @Override
    protected void oneTimeSetUp() throws Exception {
        super.oneTimeSetUp();
        
        python = (Python) applicationContext.getBean("python");
        
        File f = new File( python.getLibRoot(), "bar.py");
        FileWriter w = new FileWriter(f);
        w.write("class Bar:\n  pass\n\n");
        w.close();
        
        f = new File( python.getScriptRoot(), "foo.py");
        w = new FileWriter(f);
        w.write("print 'foo'");
        w.close();
        
        f = new File( python.getScriptRoot(), "foo_import.py");
        w = new FileWriter(f);
        w.write("import bar;b = bar.Bar();print b.__class__.__name__");
        w.close();
        
        f = new File( python.getScriptRoot(), "foo_args.py");
        w = new FileWriter(f);
        w.write("print thearg");
        w.close();
    }
    
    void copy(File gsPyLib, String pymod) throws IOException {
        getTestData().copyTo(getClass().getClassLoader().getResourceAsStream("geoserver/" + pymod),
                new File(gsPyLib, pymod).getPath());
    }
    
    @Override
    protected void setUpInternal() throws Exception {
        super.setUpInternal();
        
        out = new ByteArrayOutputStream();
        pi = python.interpreter();
        pi.setOut(out);
    }
    
    public void testBasic() throws Exception {
        String result = getAsString("/python/scripts/foo.py");
        assertEquals( "foo", result );
    }
    
    public void testImport() throws Exception {
        String result = getAsString("/python/scripts/foo_import.py");
        assertEquals( "Bar", result );
    }
    
    public void testWithArguments() throws Exception {
        String result = getAsString("/python/scripts/foo_args.py?thearg=theval");
        assertEquals("theval", result);
    }
    
    public void testGeoServerPy() throws Exception {
        
        pi.exec("import sys");
        pi.exec("print sys.path");
        print();
        clear();
        
        pi.exec("from geoserver import Catalog");
        pi.exec("cat = Catalog('sf')");
        pi.exec("stores = cat.stores()");
        clear();
        pi.exec("print [str(s) for s in stores]");
        _assert("['sf']");
        clear();
        
        pi.exec("s = cat.get('sf')");
        pi.exec("print len(s.layers())");
        _assert("3");
        clear();
            
        pi.exec("print [str(l) for l in s.layers()]");
        _assert("['AggregateGeoFeature', 'GenericEntity', 'PrimitiveGeoFeature']");
        clear();
    }
    
    public void testGeoServerPySave() throws Exception {
        FeatureTypeInfo pg = 
            getCatalog().getFeatureTypeByName("sf", "PrimitiveGeoFeature");
        assertEquals("PrimitiveGeoFeature", pg.getTitle());
        pi.exec("from geoserver import Catalog");
        pi.exec("cat = Catalog('sf')");
        pi.exec("sf = cat.get('sf')");
        pi.exec("pg = sf.get('PrimitiveGeoFeature')");
        pi.exec("pg.meta.title = 'changed'");
        pi.exec("pg.save()");
        
        pg = getCatalog().getFeatureTypeByName("sf", "PrimitiveGeoFeature");
        assertEquals("changed", pg.getTitle());
    }
    
    public void testAddDataStore() throws Exception {
        assertNull(getCatalog().getDataStoreByName("gs", "Foo"));
        DeleteDbFiles.execute("target", "foo", true);
        
        pi.exec("from geoserver import Catalog");
        pi.exec("from geoscript.workspace import H2");
        pi.exec("from geoscript.geom import Point");
        pi.exec("cat = Catalog()");
        pi.exec("h2 = H2('foo', 'target')");
        pi.exec("l = h2.create('bar', [('geom', Point, 'epsg:4326')])");
        pi.exec("l.add([Point(10,10)])");
        pi.exec("l.add([Point(20,20)])");
        pi.exec("cat.add(h2, 'Foo')");
        assertNotNull(getCatalog().getDataStoreByName("gs", "Foo"));
        pi.exec("h2.close()");
    }
    
//    public void testWorkspaceDataStoreAdadpter() throws Exception {
//        pi.exec("from geoscript.workspace import H2");
//        pi.exec("h2 = H2('foo','target')");
//        
//        PyObject workspace = pi.get("h2");
//        WorkspaceDataStoreAdapter adapter = new WorkspaceDataStoreAdapter(workspace);
//        adapter.getTypeNames();
//    }
    
    void print() {
        System.out.println(new String(out.toByteArray()));
    }
    
    void clear() {
        out = new ByteArrayOutputStream();
        pi.setOut(out);
    }
    
    void _assert(String result) {
        assertEquals(result, new String(out.toByteArray()).trim());
    }
    
}
