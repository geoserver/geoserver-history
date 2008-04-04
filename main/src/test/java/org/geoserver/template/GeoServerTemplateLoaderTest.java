/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.template;

import junit.framework.TestCase;
import org.geoserver.platform.GeoServerResourceLoader;
import org.geoserver.test.GeoServerTestSupport;
import org.springframework.web.context.support.GenericWebApplicationContext;
import org.vfny.geoserver.global.GeoserverDataDirectory;

import com.mockrunner.mock.web.MockServletConfig;
import com.mockrunner.mock.web.MockServletContext;

import java.io.File;
import java.io.IOException;


public class GeoServerTemplateLoaderTest extends GeoServerTestSupport {
    public void test() throws Exception {
        File data = getTestData().getDataDirectoryRoot();
        
        File templates = new File(data, "templates");
        
        File featureTypes = new File(data, "featureTypes");
        
        File featureType1 = new File(featureTypes, "ft1");
        featureType1.mkdir();

        File featureType2 = new File(featureTypes, "ft2");
        featureType2.mkdir();

        GeoServerTemplateLoader templateLoader = new GeoServerTemplateLoader(getClass());

        //test a path relative to templates
        File expected = new File(templates, "1.ftl");
        expected.createNewFile();

        File actual = (File) templateLoader.findTemplateSource("1.ftl");
        assertEquals(expected.getCanonicalPath(), actual.getCanonicalPath());

        //test a path relative to featureTypes
        expected = new File(featureType1, "2.ftl");
        expected.createNewFile();

        actual = (File) templateLoader.findTemplateSource("ft1/2.ftl");
        assertEquals(expected.getCanonicalPath(), actual.getCanonicalPath());

        actual = (File) templateLoader.findTemplateSource("2.ftl");
        assertNull(actual);

        //test loading relative to class
        Object source = templateLoader.findTemplateSource("FeatureSimple.ftl");
        assertNotNull(source);
        assertFalse(source instanceof File);
        templateLoader.getReader(source, "UTF-8");
        
    }

    void delete(File file) throws IOException {
        if (file.isDirectory()) {
            File[] files = file.listFiles();

            for (int i = 0; i < files.length; i++) {
                delete(files[i]);
            }
        }

        file.delete();
    }
}
