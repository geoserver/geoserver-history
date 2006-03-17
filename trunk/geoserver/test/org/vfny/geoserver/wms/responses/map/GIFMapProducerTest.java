/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wms.responses.map;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Date;

import javax.imageio.ImageIO;

import org.vfny.geoserver.wms.responses.DefaultRasterMapProducer;
import org.vfny.geoserver.wms.responses.DefaultRasterMapProducerTest;



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
    
    protected DefaultRasterMapProducer getProducerInstance(){
    	return new GIFMapProducer("image/gif"); //DJB: set content enconding correctly
    }
    

    /**
     * DOCUMENT ME!
     *
     * @param testName DOCUMENT ME!
     * @param producer DOCUMENT ME!
     */
    protected void assertNotBlank(String testName,
        DefaultRasterMapProducer producer){
    	
        BufferedImage image = producer.getImage();
        
        BufferedImage product = null;
        File tmpGif = null;
        long initT=0,finalT=0;
        try {
			tmpGif = new File("/tmp/" + testName + ".gif");
			//tmpGif.deleteOnExit();
			OutputStream out = new FileOutputStream(tmpGif);
			initT=System.currentTimeMillis();
			producer.writeTo(out);
			finalT=System.currentTimeMillis()-initT;
			System.out.println("####################     "+finalT+ "  ###########");
	
			out.flush();
			out.close();
			
			product = ImageIO.read(tmpGif);
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
				
		int w = image.getWidth();
        int h = image.getHeight();
        
		assertNotNull(product);
        assertEquals(w, product.getWidth());
        assertEquals(h, product.getHeight());
        
    

        showImage(testName, product);
    }
}
