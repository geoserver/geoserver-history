/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.kml;

import java.io.File;
import java.io.FileOutputStream;
import java.util.zip.ZipFile;

import javax.xml.namespace.QName;

import junit.framework.Test;

import org.geoserver.data.test.MockData;
import org.geoserver.wms.WMSMapContext;
import org.geoserver.wms.WMSTestSupport;
import org.geoserver.wms.request.GetMapRequest;

public class KMZMapProducerTest extends WMSTestSupport {
    KMZMapOutputFormat mapProducer;

    /**
     * This is a READ ONLY TEST so we can use one time setup
     */
    public static Test suite() {
        return new OneTimeTestSetup(new KMZMapProducerTest());
    }

    protected void setUpInternal() throws Exception {
        super.setUpInternal();

        // create a map context
        WMSMapContext mapContext = new WMSMapContext();
        mapContext.addLayer(createMapLayer(MockData.BASIC_POLYGONS));
        mapContext.addLayer(createMapLayer(MockData.BUILDINGS));
        mapContext.setMapHeight(256);
        mapContext.setMapWidth(256);

        GetMapRequest getMapRequest = createGetMapRequest(new QName[] { MockData.BASIC_POLYGONS,
                MockData.BUILDINGS });
        mapContext.setRequest(getMapRequest);

        // create hte map producer
        mapProducer = new KMZMapOutputFormat(getWMS());
        mapProducer.setMapContext(mapContext);
        mapProducer.produceMap();
    }

    public void test() throws Exception {
        // create the kmz
        File temp = File.createTempFile("test", "kmz");
        temp.delete();
        temp.mkdir();
        temp.deleteOnExit();

        File zip = new File(temp, "kmz.zip");
        zip.deleteOnExit();

        FileOutputStream output = new FileOutputStream(zip);
        mapProducer.writeTo(output);

        output.flush();
        output.close();

        assertTrue(zip.exists());

        // unzip and test it
        ZipFile zipFile = new ZipFile(zip);

        assertNotNull(zipFile.getEntry("wms.kml"));
        assertNotNull(zipFile.getEntry("layer_0.png"));
        assertNotNull(zipFile.getEntry("layer_1.png"));

        zipFile.close();
    }

    public void testAbort() throws Exception {
        mapProducer.abort();
        FileOutputStream output = null;
        try {
            File temp = File.createTempFile("test", "kmz");
            temp.delete();
            temp.mkdir();
            temp.deleteOnExit();

            File zip = new File(temp, "kmz.zip");

            output = new FileOutputStream(zip);
            mapProducer.writeTo(output);
        } catch (NullPointerException we) {
            // TODO: Should be a WmsException, right?
            return;
        } finally {
            if (output != null)
                output.close();
        }

        fail();
    }

    public void testContentDisposition() {
        String contentDisposition = mapProducer.getContentDisposition();
        assertTrue(contentDisposition.startsWith("attachment; filename="));
        assertTrue(contentDisposition.endsWith(".kmz"));
    }
}
