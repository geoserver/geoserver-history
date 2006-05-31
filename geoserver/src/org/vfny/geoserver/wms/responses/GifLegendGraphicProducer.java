/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wms.responses;

import java.io.IOException;
import java.io.OutputStream;

import java.awt.Color;

import org.vfny.geoserver.ServiceException;
import org.vfny.geoserver.wms.responses.map.gif.Gif89Encoder;


/**
 * Producer of legend graphics in image/gif format.
 *
 * @author Gabriel Roldan, Axios Engineering
 * @version $Id$
 */
public class GifLegendGraphicProducer extends DefaultRasterLegendProducer {
    /** DOCUMENT ME! */
    static final String MIME_TYPE = "image/gif";

    /**
     * Creates a new producer of legends in gif format.
     */
    public GifLegendGraphicProducer() {
        super();
    }

    /**
     * Encodes on the fly the image generated on {@linkPlain
     * DefaultRasterLegendProducer#produceLegendGraphic(GetLegendGraphicRequest)}
     * to <code>out</code> in "image/gif" format.
     *
     * @see org.vfny.geoserver.wms.responses.GetLegendGraphicProducer#writeTo(java.io.OutputStream)
     */
    public void writeTo(OutputStream out) throws IOException, ServiceException {
	//use same default background 
	Gif89Encoder gifenc = new Gif89Encoder(super.getLegendGraphic(),BG_COLOR,2);// 2= colour reduction pixel sample factor (1=look at all pixels, but its slow)
            gifenc.setComments("produced by Geoserver");
           // gifenc.setTransparentIndex(transparent_index);
            gifenc.getFrameAt(0).setInterlaced(false);
            gifenc.encode(out);

    }

    /**
     * Returns the "image/gif" mime type since that is the only output format
     * this producer specializes on.
     *
     * @return <code>"image/gif"</code>
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
