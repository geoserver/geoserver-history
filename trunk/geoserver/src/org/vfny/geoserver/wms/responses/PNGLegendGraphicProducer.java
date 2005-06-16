/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wms.responses;

import java.io.IOException;
import java.io.OutputStream;

import org.vfny.geoserver.ServiceException;
import org.vfny.geoserver.wms.responses.map.png.PngEncoder;
import org.vfny.geoserver.wms.responses.map.png.PngEncoderB;


/**
 * Producer of legend graphics in image/png format.
 *
 * @author Gabriel Roldan, Axios Engineering
 * @version $Id$
 */
public class PNGLegendGraphicProducer extends DefaultRasterLegendProducer {
    /** DOCUMENT ME! */
    static final String MIME_TYPE = "image/png";

    /**
     * Creates a new producer of legends in gif format.
     */
    public PNGLegendGraphicProducer() {
        super();
    }

    /**
     * Encodes on the fly the image generated on {@linkPlain
     * DefaultRasterLegendProducer#produceLegendGraphic(GetLegendGraphicRequest)}
     * to <code>out</code> in "image/png" format.
     *
     * @see org.vfny.geoserver.wms.responses.GetLegendGraphicProducer#writeTo(java.io.OutputStream)
     */
    public void writeTo(OutputStream out) throws IOException, ServiceException {
        PngEncoderB png =  new PngEncoderB( super.getLegendGraphic(), PngEncoder.ENCODE_ALPHA,	0, 1 ); // filter (0), and compression (1)
        byte[] pngbytes = png.pngEncode();	
        out.write( pngbytes );		 
        out.flush();
    }

    /**
     * Returns the "image/png" mime type since that is the only output format
     * this producer specializes on.
     *
     * @return <code>"image/png"</code>
     *
     * @throws IllegalStateException if <code>super.getLegendGraphic() ==
     *         null</code>, to respect the workflow.
     *
     * @see org.vfny.geoserver.wms.responses.GetLegendGraphicProducer#getContentType()
     */
    public String getContentType() throws IllegalStateException {
    	if(super.getLegendGraphic() == null)
    		throw new IllegalStateException("the image was not still produced");
        return MIME_TYPE;
    }
}
