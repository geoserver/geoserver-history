/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wms.responses.featureinfo;

import org.geoserver.data.test.MockData;
import org.geoserver.wms.WMSTestSupport;
import org.geotools.data.FeatureSource;
import org.geotools.feature.Feature;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.vfny.geoserver.wms.responses.featureInfo.FeatureTimeTemplate;


public class FeatureTimeTemplateTest extends WMSTestSupport {
    Feature feature;

    protected void setUp() throws Exception {
        super.setUp();

        FeatureSource source = getFeatureSource(MockData.PRIMITIVEGEOFEATURE);
        FeatureCollection features = source.getFeatures();
        FeatureIterator iterator = features.features();

        while (iterator.hasNext()) {
            Feature f = iterator.next();

            if (f.getAttribute("dateProperty") != null) {
                feature = f;

                break;
            }
        }

        features.close(iterator);
    }

    public void testEmpty() throws Exception {
        FeatureTimeTemplate template = new FeatureTimeTemplate();
        String[] result = template.execute(feature);

        assertEquals(0, result.length);
    }

    public void testTimestamp() throws Exception {
        setupTemplate(MockData.PRIMITIVEGEOFEATURE, "time.ftl", "${dateProperty.value}");

        FeatureTimeTemplate template = new FeatureTimeTemplate();
        String[] result = template.execute(feature);

        assertEquals(1, result.length);
        assertNotNull(result[0]);
    }

    public void testTimeSpan() throws Exception {
        setupTemplate(MockData.PRIMITIVEGEOFEATURE, "time.ftl",
            "${dateProperty.value}||${dateProperty.value}");

        FeatureTimeTemplate template = new FeatureTimeTemplate();
        String[] result = template.execute(feature);

        assertEquals(2, result.length);
        assertNotNull(result[0]);
        assertNotNull(result[1]);
    }

    public void testTimeSpanOpenEndedStart() throws Exception {
        setupTemplate(MockData.PRIMITIVEGEOFEATURE, "time.ftl", "||${dateProperty.value}");

        FeatureTimeTemplate template = new FeatureTimeTemplate();
        String[] result = template.execute(feature);

        assertEquals(2, result.length);
        assertNull(result[0]);
        assertNotNull(result[1]);
    }

    public void testTimeSpanOpenEndedEnd() throws Exception {
        setupTemplate(MockData.PRIMITIVEGEOFEATURE, "time.ftl", "${dateProperty.value}||");

        FeatureTimeTemplate template = new FeatureTimeTemplate();
        String[] result = template.execute(feature);

        assertEquals(2, result.length);
        assertNotNull(result[0]);
        assertNull(result[1]);
    }
}
