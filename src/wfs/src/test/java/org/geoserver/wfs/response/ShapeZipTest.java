package org.geoserver.wfs.response;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
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
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.feature.FeatureCollection;
import org.geotools.util.Version;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import com.vividsolutions.jts.geom.Geometry;

public class ShapeZipTest extends WFSTestSupport {

    private static final QName ALL_TYPES = new QName(MockData.CITE_URI, "AllTypes", MockData.CITE_PREFIX);
    private static final QName DOTS = new QName(MockData.CITE_URI, "dots.in.name", MockData.CITE_PREFIX);
    private static final QName ALL_DOTS = new QName(MockData.CITE_URI, "All.Types.Dots", MockData.CITE_PREFIX);
    private static final QName GEOMMID = new QName(MockData.CITE_URI, "geommid", MockData.CITE_PREFIX);
    private static final QName LONGNAMES = new QName(MockData.CITE_URI, "longnames", MockData.CITE_PREFIX);
    private static final QName NULLGEOM = new QName(MockData.CITE_URI, "nullgeom", MockData.CITE_PREFIX);
    private Operation op;
    private GetFeatureType gft;
    
    @Override
    protected void populateDataDirectory(MockData dataDirectory) throws Exception {
        super.populateDataDirectory(dataDirectory);
        Map params = new HashMap();
        params.put(MockData.KEY_SRS_NUMBER, "4326");
        dataDirectory.addPropertiesType(ALL_TYPES, ShapeZipTest.class.getResource("AllTypes.properties"), params);
        dataDirectory.addPropertiesType(DOTS, ShapeZipTest.class.getResource("dots.in.name.properties"), params);
        dataDirectory.addPropertiesType(ALL_DOTS, ShapeZipTest.class.getResource("All.Types.Dots.properties"), params);
        dataDirectory.addPropertiesType(GEOMMID, ShapeZipTest.class.getResource("geommid.properties"), params);
        dataDirectory.addPropertiesType(NULLGEOM, ShapeZipTest.class.getResource("nullgeom.properties"), params);
        dataDirectory.addPropertiesType(LONGNAMES, ShapeZipTest.class.getResource("longnames.properties"), params);
    }   

    @Override
    protected void setUpInternal() throws Exception {
        super.setUpInternal();
        gft = WfsFactory.eINSTANCE.createGetFeatureType();
        op = new Operation("GetFeature", new Service("WFS", null, new Version("1.0.0")), null, new Object[] {gft});
    }
    
    public void testNoNativeProjection() throws Exception {
        FeatureSource<SimpleFeatureType, SimpleFeature> fs;
        fs = getCatalog().getFeatureTypeInfo(MockData.BASIC_POLYGONS).getFeatureSource(true);
        ShapeZipOutputFormat zip = new ShapeZipOutputFormat();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        FeatureCollectionType fct = WfsFactory.eINSTANCE.createFeatureCollectionType();
        fct.getFeature().add(fs.getFeatures());
        zip.write(fct, bos, op);
        
        checkShapefileIntegrity(new String[] {"BasicPolygons"}, new ByteArrayInputStream(bos.toByteArray()));
    }
    
    public void testCharset() throws Exception {
        FeatureSource<SimpleFeatureType, SimpleFeature> fs;
        fs = getCatalog().getFeatureTypeInfo(MockData.BASIC_POLYGONS).getFeatureSource(true);
        ShapeZipOutputFormat zip = new ShapeZipOutputFormat();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        FeatureCollectionType fct = WfsFactory.eINSTANCE.createFeatureCollectionType();
        fct.getFeature().add(fs.getFeatures());
        
        // add the charset
        gft.getFormatOptions().put("CHARSET", Charset.forName("ISO-8859-15"));
        zip.write(fct, bos, op);
        
        checkShapefileIntegrity(new String[] {"BasicPolygons"}, new ByteArrayInputStream(bos.toByteArray()));
        assertEquals("ISO-8859-15", getCharset(new ByteArrayInputStream(bos.toByteArray())));
    }
    
    public void testMultiType() throws Exception {
        FeatureSource<SimpleFeatureType, SimpleFeature> fs;
        fs = getCatalog().getFeatureTypeInfo(ALL_TYPES).getFeatureSource(true);
        ShapeZipOutputFormat zip = new ShapeZipOutputFormat();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        FeatureCollectionType fct = WfsFactory.eINSTANCE.createFeatureCollectionType();
        fct.getFeature().add(fs.getFeatures());
        zip.write(fct, bos, op);
        
        final String[] expectedTypes = new String[] {"AllTypesPoint", "AllTypesMPoint", "AllTypesPolygon", "AllTypesLine"};
        checkShapefileIntegrity(expectedTypes, new ByteArrayInputStream(bos.toByteArray()));
        checkFieldsAreNotEmpty(new ByteArrayInputStream(bos.toByteArray()));
    }
    
    public void testMultiTypeDots() throws Exception {
        FeatureSource<SimpleFeatureType, SimpleFeature> fs;
        fs = getCatalog().getFeatureTypeInfo(ALL_DOTS).getFeatureSource(true);
        ShapeZipOutputFormat zip = new ShapeZipOutputFormat();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        FeatureCollectionType fct = WfsFactory.eINSTANCE.createFeatureCollectionType();
        fct.getFeature().add(fs.getFeatures());
        zip.write(fct, bos, op);
        
        final String[] expectedTypes = new String[] {"All_Types_DotsPoint", "All_Types_DotsMPoint", 
        		                                     "All_Types_DotsPolygon", "All_Types_DotsLine"};
        checkShapefileIntegrity(expectedTypes, new ByteArrayInputStream(bos.toByteArray()));
        checkFieldsAreNotEmpty(new ByteArrayInputStream(bos.toByteArray()));
    }
    
    public void testDots() throws Exception {
        FeatureSource<SimpleFeatureType, SimpleFeature> fs;
        fs = getCatalog().getFeatureTypeInfo(DOTS).getFeatureSource(true);
        ShapeZipOutputFormat zip = new ShapeZipOutputFormat();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        FeatureCollectionType fct = WfsFactory.eINSTANCE.createFeatureCollectionType();
        fct.getFeature().add(fs.getFeatures());
        zip.write(fct, bos, op);
        
        final String[] expectedTypes = new String[] {"dots_in_name"};
        checkShapefileIntegrity(expectedTypes, new ByteArrayInputStream(bos.toByteArray()));
        checkFieldsAreNotEmpty(new ByteArrayInputStream(bos.toByteArray()));
    }
    
    public void testGeometryInTheMiddle() throws Exception {
        // http://jira.codehaus.org/browse/GEOS-2732
        FeatureSource<SimpleFeatureType, SimpleFeature> fs;
        fs = getCatalog().getFeatureTypeInfo(GEOMMID).getFeatureSource(true);
        ShapeZipOutputFormat zip = new ShapeZipOutputFormat();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        FeatureCollectionType fct = WfsFactory.eINSTANCE.createFeatureCollectionType();
        fct.getFeature().add(fs.getFeatures());
        zip.write(fct, bos, op);
        checkFieldsAreNotEmpty(new ByteArrayInputStream(bos.toByteArray()));
    }
    
	public void testLongNames() throws Exception {
		FeatureSource<SimpleFeatureType, SimpleFeature> fs;
		fs = getCatalog().getFeatureTypeInfo(LONGNAMES).getFeatureSource(true);
		ShapeZipOutputFormat zip = new ShapeZipOutputFormat();
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		FeatureCollectionType fct = WfsFactory.eINSTANCE
				.createFeatureCollectionType();
		fct.getFeature().add(fs.getFeatures());
		zip.write(fct, bos, op);
		checkFieldsAreNotEmpty(new ByteArrayInputStream(bos.toByteArray()));
	}
	
    public void testNullGeometries() throws Exception {
        FeatureSource<SimpleFeatureType, SimpleFeature> fs;
        fs = getCatalog().getFeatureTypeInfo(NULLGEOM).getFeatureSource(true);
        ShapeZipOutputFormat zip = new ShapeZipOutputFormat();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        FeatureCollectionType fct = WfsFactory.eINSTANCE.createFeatureCollectionType();
        fct.getFeature().add(fs.getFeatures());
        zip.write(fct, bos, op);
        
        final String[] expectedTypes = new String[] {"nullgeom"};
        checkShapefileIntegrity(expectedTypes, new ByteArrayInputStream(bos.toByteArray()));
    }
    
	private File createTempFolder(String prefix) throws IOException {
		File temp = File.createTempFile(prefix, null);

		temp.delete();
		temp.mkdir();
		return temp;
	}

	private void copyStream(InputStream inStream, OutputStream outStream)
			throws IOException {
		int count = 0;
		byte[] buf = new byte[8192];
		while ((count = inStream.read(buf, 0, 8192)) != -1)
			outStream.write(buf, 0, count);
	}

	private void checkFieldsAreNotEmpty(InputStream in) throws IOException {
		ZipInputStream zis = new ZipInputStream(in);
		ZipEntry entry = null;

		File tempFolder = createTempFolder("shp_");
		String shapeFileName = "";
		while ((entry = zis.getNextEntry()) != null) {
			final String name = entry.getName();
			String outName = tempFolder.getAbsolutePath() + File.separatorChar
					+ name;
			// store .shp file name
			if (name.toLowerCase().endsWith("shp"))
				shapeFileName = outName;
			// copy each file to temp folder

			FileOutputStream outFile = new FileOutputStream(outName);
			copyStream(zis, outFile);
			outFile.close();
			zis.closeEntry();
		}
		zis.close();
		// create a datastore reading the uncompressed shapefile
		File shapeFile = new File(shapeFileName);
		ShapefileDataStore ds = new ShapefileDataStore(shapeFile.toURL());
		FeatureSource<SimpleFeatureType, SimpleFeature> fs = ds
				.getFeatureSource();
		FeatureCollection<SimpleFeatureType, SimpleFeature> fc = fs
				.getFeatures();
		Iterator<SimpleFeature> iter = fc.iterator();
		try {
			// check that every field has a not null or "empty" value
			while (iter.hasNext()) {
				SimpleFeature f = iter.next();
				for (Object attrValue : f.getAttributes()) {
					assertNotNull(attrValue);
					if (Geometry.class.isAssignableFrom(attrValue.getClass()))
						assertFalse("Empty geometry", ((Geometry) attrValue)
								.isEmpty());
					else
						assertFalse("Empty value for attribute", attrValue
								.toString().trim().equals(""));
				}

			}
		} finally {
			fc.close(iter);
			tempFolder.delete();
		}

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
