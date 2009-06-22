package org.geoserver.wfs.response;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.xml.namespace.QName;

import net.opengis.wfs.FeatureCollectionType;
import net.opengis.wfs.GetFeatureType;
import net.opengis.wfs.WfsFactory;

import org.geoserver.data.test.MockData;
import org.geoserver.platform.Operation;
import org.geoserver.platform.Service;
import org.geoserver.wfs.WFSTestSupport;
import org.geotools.data.FeatureSource;
import org.geotools.util.Version;
import org.opengis.feature.Feature;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.FeatureType;

public class ShapeZipTest extends WFSTestSupport {

    private static final QName ALL_TYPES = new QName(MockData.CITE_URI, "AllTypes", MockData.CITE_PREFIX);
    private static final QName GEOMMID = new QName(MockData.CITE_URI, "geommid", MockData.CITE_PREFIX);
    private static final QName NULLGEOM = new QName(MockData.CITE_URI, "nullgeom", MockData.CITE_PREFIX);
    private Operation op;
    private GetFeatureType gft;
    
    @Override
    protected void populateDataDirectory(MockData dataDirectory) throws Exception {
        super.populateDataDirectory(dataDirectory);
        Map params = new HashMap();
        params.put(MockData.KEY_SRS_NUMBER, "4326");
        dataDirectory.addPropertiesType(ALL_TYPES, ShapeZipTest.class.getResource("AllTypes.properties"), params);
        dataDirectory.addPropertiesType(GEOMMID, ShapeZipTest.class.getResource("geommid.properties"), params);
        dataDirectory.addPropertiesType(NULLGEOM, ShapeZipTest.class.getResource("nullgeom.properties"), params);
    }   

    @Override
    protected void setUpInternal() throws Exception {
        super.setUpInternal();
        gft = WfsFactory.eINSTANCE.createGetFeatureType();
        op = new Operation("GetFeature", new Service("WFS", null, new Version("1.0.0")), null, new Object[] {gft});
    }
    
    public void testNoNativeProjection() throws Exception {
        FeatureSource<? extends FeatureType, ? extends Feature> fs;
        fs = getFeatureSource(MockData.BASIC_POLYGONS);
        ShapeZipOutputFormat zip = new ShapeZipOutputFormat();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        FeatureCollectionType fct = WfsFactory.eINSTANCE.createFeatureCollectionType();
        fct.getFeature().add(fs.getFeatures());
        zip.write(fct, bos, op);
        
        checkShapefileIntegrity(new String[] {"BasicPolygons"}, new ByteArrayInputStream(bos.toByteArray()));
    }
    
    public void testCharset() throws Exception {
        FeatureSource<? extends FeatureType, ? extends Feature> fs;
        fs = getFeatureSource(MockData.BASIC_POLYGONS);
        ShapeZipOutputFormat zip = new ShapeZipOutputFormat();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        FeatureCollectionType fct = WfsFactory.eINSTANCE.createFeatureCollectionType();
        fct.getFeature().add(fs.getFeatures());
        
        // add the charset
        Map options = new HashMap();
        options.put("CHARSET", Charset.forName("ISO-8859-15"));
        gft.setFormatOptions(options);
        zip.write(fct, bos, op);
        
        checkShapefileIntegrity(new String[] {"BasicPolygons"}, new ByteArrayInputStream(bos.toByteArray()));
        assertEquals("ISO-8859-15", getCharset(new ByteArrayInputStream(bos.toByteArray())));
    }
    
    public void testMultiType() throws Exception {
        FeatureSource<? extends FeatureType, ? extends Feature> fs;
        fs = getFeatureSource(ALL_TYPES);
        ShapeZipOutputFormat zip = new ShapeZipOutputFormat();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        FeatureCollectionType fct = WfsFactory.eINSTANCE.createFeatureCollectionType();
        fct.getFeature().add(fs.getFeatures());
        zip.write(fct, bos, op);
        
        final String[] expectedTypes = new String[] {"AllTypesPoint", "AllTypesMPoint", "AllTypesPolygon", "AllTypesLine"};
        checkShapefileIntegrity(expectedTypes, new ByteArrayInputStream(bos.toByteArray()));
    }
    
    public void testGeometryInTheMiddle() throws Exception {
        // http://jira.codehaus.org/browse/GEOS-2732
        FeatureSource<SimpleFeatureType, SimpleFeature> fs;
        fs = getFeatureSource(GEOMMID);
        ShapeZipOutputFormat zip = new ShapeZipOutputFormat();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        FeatureCollectionType fct = WfsFactory.eINSTANCE.createFeatureCollectionType();
        fct.getFeature().add(fs.getFeatures());
        zip.write(fct, bos, op);
    }
    
    public void testNullGeometries() throws Exception {
        FeatureSource<SimpleFeatureType, SimpleFeature> fs;
        fs = getFeatureSource(NULLGEOM);
        ShapeZipOutputFormat zip = new ShapeZipOutputFormat();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        FeatureCollectionType fct = WfsFactory.eINSTANCE.createFeatureCollectionType();
        fct.getFeature().add(fs.getFeatures());
        zip.write(fct, bos, op);
        
        final String[] expectedTypes = new String[] {"nullgeom"};
        checkShapefileIntegrity(expectedTypes, new ByteArrayInputStream(bos.toByteArray()));
    }

    private void checkShapefileIntegrity(String[] typeNames, final InputStream in) throws IOException {
        ZipInputStream zis = new ZipInputStream(in);
        ZipEntry entry = null;
        
        final String[] extensions = new String[] {".shp", ".shx", ".dbf", ".prj", ".cst"};
        Set names = new HashSet();
        for (String name : typeNames) {
            for (String extension : extensions) {
                names.add(name + extension);
            }
        }
        while((entry = zis.getNextEntry()) != null) {
            final String name = entry.getName();
            assertTrue("Missing " + name, names.contains(name));
            names.remove(name);
            zis.closeEntry();
        }
        zis.close();
    }
    
    private String getCharset(final InputStream in) throws IOException {
        ZipInputStream zis = new ZipInputStream(in);
        ZipEntry entry = null;
        byte[] bytes = new byte[1024];
        while((entry = zis.getNextEntry()) != null) {
            if(entry.getName().endsWith(".cst")) {
                zis.read(bytes);
            }
        }
        zis.close();
        
        if(bytes == null)
            return null;
        else
            return new String(bytes).trim();
    }
}
