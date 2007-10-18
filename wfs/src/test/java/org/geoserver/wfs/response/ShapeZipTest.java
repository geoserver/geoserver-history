package org.geoserver.wfs.response;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import net.opengis.wfs.FeatureCollectionType;
import net.opengis.wfs.WfsFactory;

import org.geoserver.data.test.MockData;
import org.geoserver.wfs.WFSTestSupport;
import org.geotools.data.FeatureSource;

public class ShapeZipTest extends WFSTestSupport {
    
    public void testNoNativeProjection() throws Exception {
        FeatureSource fs = getCatalog().getFeatureTypeInfo(MockData.BASIC_POLYGONS).getFeatureSource(true);
        ShapeZipOutputFormat zip = new ShapeZipOutputFormat();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        FeatureCollectionType fct = WfsFactory.eINSTANCE.createFeatureCollectionType();
        fct.getFeature().add(fs.getFeatures());
        zip.write(fct, bos, null);
        
        ZipInputStream zis = new ZipInputStream(new ByteArrayInputStream(bos.toByteArray()));
        ZipEntry entry = null;
        Set names = new HashSet(Arrays.asList(new String[] {".shp", ".shx", ".dbf", ".prj"}));
        while((entry = zis.getNextEntry()) != null) {
            final String name = entry.getName();
            final String extension = name.substring(name.length() - 4, name.length());
            assertTrue(names.contains(extension));
            names.remove(extension);
            zis.closeEntry();
        }
        zis.close();
         
    }
}
