/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.vfny.geoserver.wms.responses.map.pdf;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.RenderedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.geoserver.platform.ServiceException;
import org.geoserver.wms.DefaultWebMapService;
import org.geoserver.wms.WMS;
import org.geoserver.wms.responses.MapDecorationLayout;
import org.geotools.renderer.lite.RendererUtilities;
import org.geotools.renderer.lite.StreamingRenderer;
import org.vfny.geoserver.wms.RasterMapProducer;
import org.vfny.geoserver.wms.WmsException;
import org.vfny.geoserver.wms.responses.AbstractRasterMapProducer;
import org.vfny.geoserver.wms.responses.DefaultRasterMapProducer;
import org.vfny.geoserver.wms.responses.MaxErrorEnforcer;
import org.vfny.geoserver.wms.responses.RenderExceptionStrategy;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.FontFactory;
import com.lowagie.text.pdf.DefaultFontMapper;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfGraphics2D;
import com.lowagie.text.pdf.PdfTemplate;
import com.lowagie.text.pdf.PdfWriter;
import com.vividsolutions.jts.geom.Envelope;

/**
 * Handles a GetMap request that spects a map in PDF format.
 * 
 * @author Pierre-Emmanuel Balageas, ALCER (http://www.alcer.com)
 * @author Simone Giannecchini - GeoSolutions
 * @version $Id$
 */
class PDFMapProducer extends AbstractRasterMapProducer implements
		RasterMapProducer {
	/** A logger for this class. */
	private static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger("org.vfny.geoserver.responses.wms.map.pdf");
	
	/**
	 * A kilobyte
	 */
	static final int KB = 1024;

    /** the only MIME type this map producer supports */
    static final String MIME_TYPE = "application/pdf";
    
    WMS wms;

	public PDFMapProducer(WMS wms) {
	    super(MIME_TYPE);
	    this.wms = wms;
	}

	/**
	 * Writes the image to the client.
	 * 
	 * @param out
	 *            The output stream to write to.
	 */
	public void writeTo(OutputStream out)
			throws ServiceException, java.io.IOException {
	    final int width = mapContext.getMapWidth();
        final int height = mapContext.getMapHeight();

        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("setting up " + width + "x" + height + " image");
        }

        try {
            // step 1: creation of a document-object
            // width of document-object is width*72 inches
            // height of document-object is height*72 inches
            com.lowagie.text.Rectangle pageSize = new com.lowagie.text.Rectangle(
                    width, height);

            Document document = new Document(pageSize);
            document.setMargins(0, 0, 0, 0);

            // step 2: creation of the writer
            PdfWriter writer = PdfWriter.getInstance(document, out);

            // step 3: we open the document
            document.open();

            // step 4: we grab the ContentByte and do some stuff with it

            // we create a fontMapper and read all the fonts in the font
            // directory
            DefaultFontMapper mapper = new DefaultFontMapper();
            FontFactory.registerDirectories();

            // we create a template and a Graphics2D object that corresponds
            // with it
            PdfContentByte cb = writer.getDirectContent();
            PdfTemplate tp = cb.createTemplate(width, height);
            PdfGraphics2D graphic = (PdfGraphics2D) tp.createGraphics(width, height, mapper);

            // we set graphics options
            if (!mapContext.isTransparent()) {
                graphic.setColor(mapContext.getBgColor());
                graphic.fillRect(0, 0, width, height);
            } else {
                if (LOGGER.isLoggable(Level.FINE)) {
                    LOGGER.fine("setting to transparent");
                }

                int type = AlphaComposite.SRC;
                graphic.setComposite(AlphaComposite.getInstance(type));

                Color c = new Color(mapContext.getBgColor().getRed(),
                        mapContext.getBgColor().getGreen(), mapContext
                                .getBgColor().getBlue(), 0);
                graphic.setBackground(mapContext.getBgColor());
                graphic.setColor(c);
                graphic.fillRect(0, 0, width, height);

                type = AlphaComposite.SRC_OVER;
                graphic.setComposite(AlphaComposite.getInstance(type));
            }

            Rectangle paintArea = new Rectangle(width, height);

            renderer = new StreamingRenderer();
            renderer.setContext(mapContext);
            // TODO: expose the generalization distance as a param
            // ((StreamingRenderer) renderer).setGeneralizationDistance(0);

            RenderingHints hints = new RenderingHints(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            renderer.setJava2DHints(hints);

            // we already do everything that the optimized data loading does...
            // if we set it to true then it does it all twice...
            Map rendererParams = new HashMap();
            rendererParams
                    .put("optimizedDataLoadingEnabled", new Boolean(true));
            rendererParams.put("renderingBuffer", new Integer(mapContext
                    .getBuffer()));
            // we need the renderer to draw everything on the batik provided graphics object
            rendererParams.put(StreamingRenderer.OPTIMIZE_FTS_RENDERING_KEY, Boolean.FALSE);
            // render everything in vector form if possible
            rendererParams.put(StreamingRenderer.VECTOR_RENDERING_KEY, Boolean.TRUE);
            if(DefaultWebMapService.isLineWidthOptimizationEnabled()) {
                rendererParams.put(StreamingRenderer.LINE_WIDTH_OPTIMIZATION_KEY, true);
            }
            renderer.setRendererHints(rendererParams);

            Envelope dataArea = mapContext.getAreaOfInterest();

            if (this.abortRequested) {
                graphic.dispose();
                // step 5: we close the document
                document.close();

                return;
            }
            
            // enforce no more than x rendering errors
            int maxErrors = wms.getMaxRenderingErrors();
            MaxErrorEnforcer errorChecker = new MaxErrorEnforcer(renderer, maxErrors);

            // Add a render listener that ignores well known rendering exceptions and reports back non
            // ignorable ones
            final RenderExceptionStrategy nonIgnorableExceptionListener;
            nonIgnorableExceptionListener = new RenderExceptionStrategy(renderer);
            renderer.addRenderListener(nonIgnorableExceptionListener);
            
            // enforce max memory usage
            int maxMemory = wms.getMaxRequestMemory() * KB;
            PDFMaxSizeEnforcer memoryChecker = new PDFMaxSizeEnforcer(renderer, graphic, maxMemory);
            
            // render the map
            renderer.paint(graphic, paintArea, dataArea, getRenderingTransform());
            
            // render the watermark
            MapDecorationLayout.Block watermark = 
                DefaultRasterMapProducer.getWatermark(this.mapContext.getRequest().getWMS().getServiceInfo());

            if (watermark != null) {
                MapDecorationLayout layout = new MapDecorationLayout();
                layout.paint(graphic, paintArea, this.mapContext);
            }
            
            //check if a non ignorable error occurred
            if(nonIgnorableExceptionListener.exceptionOccurred()){
                Exception renderError = nonIgnorableExceptionListener.getException();
                throw new WmsException("Rendering process failed", "internalError", renderError);
            }

            // check if too many errors occurred
            if(errorChecker.exceedsMaxErrors()) {
                throw new WmsException("More than " + maxErrors + " rendering errors occurred, bailing out", 
                        "internalError", errorChecker.getLastException());
            }
            
            // check we did not use too much memory
            if(memoryChecker.exceedsMaxSize()) {
                long kbMax = maxMemory / KB;
                throw new WmsException("Rendering request used more memory than the maximum allowed:" 
                        + kbMax + "KB");
            }

            graphic.dispose();
            cb.addTemplate(tp, 0, 0);

            // step 5: we close the document
            document.close();
            writer.flush();
            writer.close();
        } catch (DocumentException t) {
            throw new WmsException("Error setting up the PDF", "internalError", t);
        }
	}

    public void produceMap() throws WmsException {
        // do nothing here, we want to stream out directly
    }

    /**
     * Returns a sensible <filename>.pdf for the http attachment header
     */
    @Override
    public String getContentDisposition() {
        if (this.mapContext.getLayer(0) != null) {
            try {
                String title = this.mapContext.getLayer(0).getFeatureSource().getSchema().getName()
                        .getLocalPart();

                if ((title != null) && !title.equals("")) {
                    return "attachment; filename=" + title + ".pdf";
                }
            } catch (NullPointerException e) {
            }
        }

        return "attachment; filename=geoserver.pdf";
    }

    /**
     * Does nothing
     * 
     * @see RasterMapProducer#formatImageOutputStream(RenderedImage, OutputStream)
     */
    public void formatImageOutputStream(RenderedImage image, OutputStream outStream)
            throws WmsException, IOException {
        // do nothing
    }
}
