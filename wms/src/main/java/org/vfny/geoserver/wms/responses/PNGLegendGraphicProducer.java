/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wms.responses;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;

import org.vfny.geoserver.ServiceException;
import org.vfny.geoserver.wms.responses.map.png.PngEncoder;
import org.vfny.geoserver.wms.responses.map.png.PngEncoderB;


/**
 * Producer of legend graphics in all the formats available through JAI.
 *
 * @author Gabriel Roldan, Axios Engineering
 * @version $Id: PNGLegendGraphicProducer.java 4107 2006-01-18 22:20:30Z jdeolive $
 */
class PNGLegendGraphicProducer extends DefaultRasterLegendProducer {


    /**
     * Creates a new JAI based legend producer for creating
     * <code>outputFormat</code> type images.
     *
     * @param outputFormat DOCUMENT ME!
     */
    PNGLegendGraphicProducer() {
        super();
    }

    /**
     * Encodes the image created by the superclss to the format specified at
     * the constructor and sends it to <code>out</code>.
     *
     * @see org.vfny.geoserver.wms.responses.GetLegendGraphicProducer#writeTo(java.io.OutputStream)
     */
    public void writeTo(OutputStream out) throws IOException, ServiceException 
	{
    	BufferedImage image = super.getLegendGraphic();
    	
        PngEncoderB png =  new PngEncoderB( image, PngEncoder.ENCODE_ALPHA,	0, 1 ); // filter (0), and compression (1)
        byte[] pngbytes = png.pngEncode();	
        
        out.write( pngbytes );		 
        out.flush();
    }

    /**
     * Returns the MIME type in which the legend graphic will be encoded.
     *
     * @see org.vfny.geoserver.wms.responses.GetLegendGraphicProducer#getContentType()
     */
    public String getContentType() throws IllegalStateException {
        return "image/png";
    }
}
