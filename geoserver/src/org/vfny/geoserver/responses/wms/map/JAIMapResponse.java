/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.responses.wms.map;

import com.vividsolutions.jts.geom.Envelope;
import org.geotools.data.*;
import org.geotools.feature.FeatureCollection;
import org.geotools.map.*;
import org.geotools.renderer.*;
import org.geotools.renderer.lite.LiteRenderer;
import org.geotools.styling.*;
import org.vfny.geoserver.WmsException;
import org.vfny.geoserver.config.*;
import org.vfny.geoserver.requests.wms.GetMapRequest;
import org.vfny.geoserver.responses.*;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;
import java.util.logging.*;
import javax.imageio.*;
import javax.imageio.stream.ImageOutputStream;


/**
 * Generates a map using the geotools jai rendering classes.
 *
 * @author Chris Holmes, TOPP
 * @version $Id: JAIMapResponse.java,v 1.3 2003/12/17 22:33:35 cholmesny Exp $
 */
public class JAIMapResponse extends GetMapDelegate {
    /** DOCUMENT ME! */
    private static final Logger LOGGER = Logger.getLogger(
            "org.vfny.geoserver.responses.wms.map");
    private static List supportedFormats = Arrays.asList(new String[] {
                "image/png", "image/x-portable-graymap", "image/jpeg",
                "image/jpeg2000", "image/x-png", "image/tiff",
                "image/vnd.wap.wbmp", "image/x-portable-pixmap",
                "image/x-portable-bitmap", "image/bmp",
                "image/x-portable-anymap"
            });
    private BufferedImage image;
    private String format;

    //    Java2DRenderer renderer = new Java2DRenderer();
    Renderer renderer = new LiteRenderer();

    public JAIMapResponse() {
    }

    /**
     * evaluates if this Map producer can generate the map format specified by
     * <code>mapFormat</code>
     *
     * @param mapFormat the mime type of the output map format requiered
     *
     * @return true if class can produce a map in the passed format
     */
    public boolean canProduce(String mapFormat) {
        return supportedFormats.contains(mapFormat);
    }

    /**
     * The formats this delegate supports.  Includes png, x-portable-graymap,
     * jpeg, jpeg2000, x-png, tiff, vnd.wap.wbmp, x-portable-pixmap,
     * x-portable-bitmap, bmp and x-portable-anymap.
     *
     * @return The list of the supported formats.
     */
    public List getSupportedFormats() {
        return supportedFormats;
    }

    /**
     * Writes the image to the client.
     *
     * @param out The output stream to write to.  
     *
     * @throws org.vfny.geoserver.ServiceException DOCUMENT ME!
     * @throws java.io.IOException DOCUMENT ME!
     */
    public void writeTo(OutputStream out)
        throws org.vfny.geoserver.ServiceException, java.io.IOException {
        formatImageOutputStream(format, image, out);
    }

    /**
     * Transforms the rendered image into the appropriate format, streaming to
     * the output stream.
     *
     * @param format The name of the format
     * @param image The image to be formatted.
     * @param outStream The stream to write to.
     *
     * @throws WmsException 
     * @throws IOException DOCUMENT ME!
     */
    public void formatImageOutputStream(String format, BufferedImage image,
        OutputStream outStream) throws WmsException, IOException {
        if (format.equalsIgnoreCase("jpeg")) {
            format = "image/jpeg";
        }

        Iterator it = ImageIO.getImageWritersByMIMEType(format);

        if (!it.hasNext()) {
            throw new WmsException( //WMSException.WMSCODE_INVALIDFORMAT,
                "Format not supported: " + format);
        }

        ImageWriter writer = (ImageWriter) it.next();
        ImageOutputStream ioutstream = null;

        ioutstream = ImageIO.createImageOutputStream(outStream);
        writer.setOutput(ioutstream);
        writer.write(image);
        writer.dispose();
        ioutstream.close();
    }

    /**
     * Halts the loading.  Right now unimplemented.
     */
    public void abort() {
    }

    /**
     * Gets the content type.  This is set by the request, should only be
     * called after execute.  GetMapResponse should handle this though.
     *
     * @return The mime type that this response will generate.
     *
     * @throws java.lang.IllegalStateException DOCUMENT ME!
     */
    public String getContentType() throws java.lang.IllegalStateException {
        //Return a default?  Format is not set until execute is called...
        return format;
    }

    /**
     * Performs the execute request using geotools rendering.
     *
     * @param requestedLayers The information on the types requested.
     * @param resultLayers The results of the queries to generate maps with.
     * @param styles The styles to be used on the results.
     *
     * @throws WmsException For any problems.
     *
     * @task TODO: Update to feature streaming and latest api, Map is
     *       deprecated.
     */
    protected void execute(FeatureTypeConfig[] requestedLayers,
        FeatureResults[] resultLayers, Style[] styles)
        throws WmsException {
        GetMapRequest request = getRequest();
        this.format = request.getFormat();

        int width = request.getWidth();
        int height = request.getHeight();

        try {
            LOGGER.fine("setting up map");

            org.geotools.map.Map map = new DefaultMap();
            Style[] layerstyle = null;
            StyleBuilder sb = new StyleBuilder();

            for (int i = 0; i < requestedLayers.length; i++) {
                Style style = styles[i];
                FeatureCollection fc = resultLayers[i].collection();
                map.addFeatureTable(fc, style);
            }

            LOGGER.fine("map setup");

            //Renderer renderer = new LiteRenderer();
            BufferedImage image = new BufferedImage(width, height,
                    BufferedImage.TYPE_INT_RGB);
            Envelope env = request.getBbox();

            //LOGGER.fine("setting up renderer");
            java.awt.Graphics g = image.getGraphics();
            g.setColor(request.getBgColor());

            if (!request.isTransparent()) {
                g.fillRect(0, 0, width, height);
            }

            synchronized (renderer) {
                renderer.setOutput(image.getGraphics(),
                    new java.awt.Rectangle(width, height));
                LOGGER.fine("calling renderer");

                Date start = new Date();
                map.render(renderer, env);

                Date end = new Date();
                LOGGER.fine("returning image after render time of "
                    + (end.getTime() - start.getTime()));

                //renderer = null;
            }

            map = null;
            this.image = image;
        } catch (Exception exp) {
            exp.printStackTrace();
            throw new WmsException(null, "Internal error : " + exp.getMessage());
        }
    }
}
