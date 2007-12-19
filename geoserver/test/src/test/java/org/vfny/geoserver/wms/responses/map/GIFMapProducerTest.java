/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wms.responses.map;

import org.geotools.image.ImageWorker;
import org.vfny.geoserver.wms.responses.DefaultRasterMapProducer;
import org.vfny.geoserver.wms.responses.DefaultRasterMapProducerTest;
import org.vfny.geoserver.wms.responses.map.gif.GIFMapProducer;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import javax.imageio.ImageIO;


/**
 * @task TODO: do some decent testing
 *
 * @author Gabriel Roldan, Axios Engineering
 * @version $Id$
 */
public class GIFMapProducerTest extends DefaultRasterMapProducerTest {
    /**
     *
     */
    public GIFMapProducerTest() {
        super();
    }

    protected DefaultRasterMapProducer getProducerInstance() {
        return new GIFMapProducer("image/gif", null); //DJB: set content enconding correctly
    }

    /**
     * DOCUMENT ME!
     *
     * @param testName DOCUMENT ME!
     * @param producer DOCUMENT ME!
     */
    protected void assertNotBlank(String testName, DefaultRasterMapProducer producer) {
        RenderedImage image = producer.getImage();

        BufferedImage product = null;
        File tmpGif = null;

        try {
            tmpGif = File.createTempFile(testName, ".gif");
            tmpGif.deleteOnExit();

            //tmpGif.deleteOnExit();
            OutputStream out = new FileOutputStream(tmpGif);
            producer.writeTo(out);
            out.flush();
            out.close();

            product = ImageWorker.loadPlanarImageImage(tmpGif.getAbsolutePath(), null, 0, true)
                                 .getAsBufferedImage();
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }

        int w = image.getWidth();
        int h = image.getHeight();

        assertNotNull(product);
        assertEquals(w, product.getWidth());
        assertEquals(h, product.getHeight());

        System.out.println("width=" + w + ", height=" + h);

        showImage(testName, product);
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(GIFMapProducerTest.class);
    }
}
