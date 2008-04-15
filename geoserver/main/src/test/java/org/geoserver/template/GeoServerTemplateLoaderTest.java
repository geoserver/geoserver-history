/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.template;

import java.io.File;
import java.io.IOException;

import junit.framework.TestCase;

import org.geoserver.platform.GeoServerResourceLoader;
import org.springframework.web.context.support.GenericWebApplicationContext;
import org.vfny.geoserver.global.GeoserverDataDirectory;

public class GeoServerTemplateLoaderTest extends TestCase {
    
    public void test() throws Exception {
        File data = File.createTempFile(getName(), "template");
        data.delete();
        data.mkdir();

        System.setProperty("GEOSERVER_DATA_DIR", data.getAbsolutePath());

        File templates = new File(data, "templates");
        templates.mkdir();

        File featureTypes = new File(data, "featureTypes");
        featureTypes.mkdir();

        File featureType1 = new File(featureTypes, "ft1");
        featureType1.mkdir();

        File featureType2 = new File(featureTypes, "ft2");
        featureType2.mkdir();

        try {
            GeoServerResourceLoader loader = new GeoServerResourceLoader(data);
            GenericWebApplicationContext context = new GenericWebApplicationContext();
            context.getBeanFactory().registerSingleton("resourceLoader", loader);

            GeoserverDataDirectory.init(context);

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

            // Removed this for the moment, I need to setup a mock catalog in
            // order to test again feature type specific template loading
            // templateLoader.setFeatureType("ft1");
            // actual = (File) templateLoader.findTemplateSource("2.ftl");
            // assertEquals(expected.getCanonicalPath(),
            // actual.getCanonicalPath());

            //test loading relative to class
            Object source = templateLoader.findTemplateSource("FeatureSimple.ftl");
            assertNotNull(source);
            assertFalse(source instanceof File);
            templateLoader.getReader(source, "UTF-8");
        } finally {
            delete(data);
        }
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
