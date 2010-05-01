/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wms.responses.legend.jai;

import java.io.IOException;
import java.io.OutputStream;

import org.geoserver.platform.ServiceException;
import org.vfny.geoserver.wms.WmsException;
import org.vfny.geoserver.wms.requests.GetLegendGraphicRequest;
import org.vfny.geoserver.wms.responses.DefaultRasterLegendProducer;
import org.vfny.geoserver.wms.responses.helpers.JAISupport;


/**
 * Producer of legend graphics in all the formats available through JAI.
 *
 * @author Gabriel Roldan, Axios Engineering
 * @version $Id$
 */
class JaiLegendGraphicProducer extends DefaultRasterLegendProducer {
    /** holds the desired output format MIME type */
    private String outputFormat;

    /**
     * Creates a new JAI based legend producer for creating
     * <code>outputFormat</code> type images.
     *
     * @param outputFormat DOCUMENT ME!
     */
    JaiLegendGraphicProducer(String outputFormat) {
        super();
        this.outputFormat = outputFormat;
    }

    /**
     * Overrides to force request.isTransparent() to false when the output
     * format is <code>image/jpeg</code>.
     * 
     * @see DefaultRasterLegendProducer#produceLegendGraphic(GetLegendGraphicRequest)
     */
    public void produceLegendGraphic(GetLegendGraphicRequest request)
    throws WmsException {
        //HACK: should we provide a jpeg specific legend producer just
        //like for GetMap?
        if(outputFormat.startsWith("image/jpeg")){
            //no transparency in jpeg
            request.setTransparent(false);
        }
        super.produceLegendGraphic(request);
    }
    
    /**
     * Encodes the image created by the superclss to the format specified at
     * the constructor and sends it to <code>out</code>.
     *
     * @see org.vfny.geoserver.wms.responses.GetLegendGraphicProducer#writeTo(java.io.OutputStream)
     */
    public void writeTo(OutputStream out) throws IOException, ServiceException {
        JAISupport.encode(this.outputFormat, super.getLegendGraphic(), out);
    }

    /**
     * Returns the MIME type in which the legend graphic will be encoded.
     *
     * @see org.vfny.geoserver.wms.responses.GetLegendGraphicProducer#getContentType()
     */
    public String getContentType() throws IllegalStateException {
        return this.outputFormat;
    }
}
