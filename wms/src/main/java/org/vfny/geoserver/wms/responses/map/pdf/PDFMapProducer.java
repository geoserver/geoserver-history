/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wms.responses.map.pdf;

import com.lowagie.text.Document;
import com.lowagie.text.FontFactory;
import com.lowagie.text.pdf.DefaultFontMapper;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfTemplate;
import com.lowagie.text.pdf.PdfWriter;
import com.vividsolutions.jts.geom.Envelope;
import org.geotools.renderer.GTRenderer;
import org.geotools.renderer.lite.RendererUtilities;
import org.geotools.renderer.lite.StreamingRenderer;
import org.vfny.geoserver.wms.GetMapProducer;
import org.vfny.geoserver.wms.WMSMapContext;
import org.vfny.geoserver.wms.WmsException;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Handles a GetMap request that spects a map in PDF format.
 *
 * @author Pierre-Emmanuel Balageas, ALCER (http://www.alcer.com)
 * @version $Id: hacking.xml 2976 2004-10-13 23:25:25Z cholmes $
 */
class PDFMapProducer implements GetMapProducer {
    /** A logger for this class. */
    private static final Logger LOGGER = Logger.getLogger(
            "org.vfny.geoserver.responses.wms.map.pdf");

    /** Which format to encode the image in if one is not supplied */
    private static final String DEFAULT_MAP_FORMAT = "application/pdf";

    /** The byte code generated by the execute method. */
    private ByteArrayOutputStream bos;

    /** The one to do the magic of rendering a map */
    private GTRenderer renderer;

    /**
     * Set in produceMap(...) from the requested output format, it's
     * holded just to be sure that method has been called before
     * getContentType() thus supporting the workflow contract of the request
     * processing
     */
    private String format = null;

    /**
     * set to <code>true</code> on <code>abort()</code> so
     * <code>produceMap</code> leaves the image being worked on to the garbage
     * collector.
     */
    private boolean abortRequested;

    /**
     * Holds the map context passed to produceMap, so subclasses can
     * use it if they need it from inside {@linkPlain
     * #formatImageOutputStream(String, BufferedImage, OutputStream)}
     */
    private WMSMapContext mapContext;

/**
         *
         */
    public PDFMapProducer() {
        this(DEFAULT_MAP_FORMAT);
    }

/**
         *
         */
    public PDFMapProducer(String outputFormat) {
        setOutputFormat(outputFormat);
    }

    /**
     * Sets the MIME type of the output image.
     *
     * @param format the desired output map format.
     */
    public void setOutputFormat(String format) {
        this.format = format;
    }

    /**
     * Writes the image to the client.
     *
     * @param out The output stream to write to.
     */
    public void writeTo(OutputStream out)
        throws org.vfny.geoserver.ServiceException, java.io.IOException {
        // write to out
        out.write(bos.toByteArray());
    }

    /**
     * Halts the loading.  Right now just calls renderer.stopRendering.
     */
    public void abort() {
        this.abortRequested = true;

        if (this.renderer != null) {
            this.renderer.stopRendering();
        }
    }

    /**
     * Gets the content type.  This is set by the request, should only
     * be called after execute.  GetMapResponse should handle this though.
     *
     * @return The mime type that this response will generate.
     *
     * @throws IllegalStateException DOCUMENT ME!
     */
    public String getContentType() throws java.lang.IllegalStateException {
        if (this.format == null) {
            throw new IllegalStateException(
                "the output map format was not yet specified");
        }

        return this.format;
    }

    /**
     * returns the content encoding for the output data (null for this
     * class)
     *
     * @return <code>null</code> since no special encoding is performed while
     *         wrtting to the output stream. Do not confuse this with
     *         getMimeType().
     */
    public String getContentEncoding() {
        return null;
    }

    /**
     * Performs the execute request using geotools rendering.
     *
     * @param map The information on the types requested.
     *
     * @throws WmsException For any problems.
     */
    public void produceMap(WMSMapContext map) throws WmsException {
        this.mapContext = map;

        final int width = map.getMapWidth();
        final int height = map.getMapHeight();

        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("setting up " + width + "x" + height + " image");
        }

        try {
            ByteArrayOutputStream curOs = new ByteArrayOutputStream();

            // step 1: creation of a document-object
            // width of document-object is width*72 inches
            // height of document-object is height*72 inches
            com.lowagie.text.Rectangle pageSize = new com.lowagie.text.Rectangle(width,
                    height);

            Document document = new Document(pageSize);
            document.setMargins(0, 0, 0, 0);

            // step 2: creation of the writer
            PdfWriter writer = PdfWriter.getInstance(document, curOs);

            // step 3: we open the document
            document.open();

            // step 4: we grab the ContentByte and do some stuff with it

            // we create a fontMapper and read all the fonts in the font directory
            DefaultFontMapper mapper = new DefaultFontMapper();
            FontFactory.registerDirectories();

            // we create a template and a Graphics2D object that corresponds with it
            PdfContentByte cb = writer.getDirectContent();
            PdfTemplate tp = cb.createTemplate(width, height);
            Graphics2D graphic = tp.createGraphics(width, height, mapper);

            // we set graphics options
            if (!map.isTransparent()) {
                graphic.setColor(map.getBgColor());
                graphic.fillRect(0, 0, width, height);
            } else {
                LOGGER.fine("setting to transparent");

                int type = AlphaComposite.SRC;
                graphic.setComposite(AlphaComposite.getInstance(type));

                Color c = new Color(map.getBgColor().getRed(),
                        map.getBgColor().getGreen(),
                        map.getBgColor().getBlue(), 0);
                graphic.setBackground(map.getBgColor());
                graphic.setColor(c);
                graphic.fillRect(0, 0, width, height);

                type = AlphaComposite.SRC_OVER;
                graphic.setComposite(AlphaComposite.getInstance(type));
            }

            Rectangle paintArea = new Rectangle(width, height);

            renderer = new StreamingRenderer();
            renderer.setContext(map);

            RenderingHints hints = new RenderingHints(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            renderer.setJava2DHints(hints);

            //we already do everything that the optimized data loading does...
            //if we set it to true then it does it all twice...
            Map rendererParams = new HashMap();
            rendererParams.put("optimizedDataLoadingEnabled", Boolean.TRUE);

            renderer.setRendererHints(rendererParams);

            Envelope dataArea = map.getAreaOfInterest();
            AffineTransform at = RendererUtilities.worldToScreenTransform(dataArea,
                    paintArea);

            if (this.abortRequested) {
                graphic.dispose();
                // step 5: we close the document
                document.close();

                return;
            }

            renderer.paint(graphic, paintArea, at);

            map = null;

            if (!this.abortRequested) {
                this.bos = curOs;
            }

            graphic.dispose();
            cb.addTemplate(tp, 0, 0);

            // step 5: we close the document
            document.close();
        } catch (Throwable t) {
            LOGGER.warning("UNCAUGHT exception: " + t.getMessage());

            WmsException wmse = new WmsException("UNCAUGHT exception: "
                    + t.getMessage());
            wmse.setStackTrace(t.getStackTrace());
            throw wmse;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected WMSMapContext getMapContext() {
        return this.mapContext;
    }

	public String getContentDisposition() {
		if (this.mapContext.getLayer(0) != null) {
			try {
				String title = this.mapContext.getLayer(0).getFeatureSource().getSchema().getTypeName();
				if (title != null && !title.equals("")) {
					return "inline; filename=" + title + ".pdf";
				}
			} catch (NullPointerException e) {
			}
		}
		return "inline; filename=geoserver.pdf";
	}
}
