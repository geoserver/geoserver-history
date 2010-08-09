/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wms.responses.map.svg;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.batik.svggen.SVGGeneratorContext;
import org.apache.batik.svggen.SVGGraphics2D;
import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.geoserver.config.GeoServer;
import org.geoserver.platform.ServiceException;
import org.geoserver.wms.DefaultWebMapService;
import org.geoserver.wms.WMS;
import org.geotools.map.MapContext;
import org.geotools.renderer.lite.StreamingRenderer;
import org.vfny.geoserver.wms.GetMapProducer;
import org.vfny.geoserver.wms.WMSMapContext;
import org.vfny.geoserver.wms.WmsException;
import org.vfny.geoserver.wms.responses.AbstractGetMapProducer;
import org.vfny.geoserver.wms.responses.MaxErrorEnforcer;
import org.vfny.geoserver.wms.responses.RenderExceptionStrategy;
import org.w3c.dom.Document;

import com.vividsolutions.jts.geom.Envelope;

/**
 * Renders svg using the Batik SVG Toolkit. An SVG context is created for a map
 * and then passed of to {@link org.geotools.renderer.lite.StreamingRenderer}.
 * 
 * @author Justin Deoliveira, The Open Planning Project
 * 
 */
class SVGBatikMapProducer extends AbstractGetMapProducer implements
		GetMapProducer {
	StreamingRenderer renderer;

	private WMS wmsConfig;

	public SVGBatikMapProducer(String mimeType, String[] outputFormats, WMS wms) {
	    super(mimeType, outputFormats);
		this.wmsConfig = wms;
	}
	
	public void abort(GeoServer gs) {
		if (renderer != null) {
			renderer.stopRendering();
		}
	}

	public void produceMap() throws WmsException {
		renderer = new StreamingRenderer();

		// optimized data loading was not here, but yet it seems sensible to
		// have it...
		Map rendererParams = new HashMap();
		rendererParams.put("optimizedDataLoadingEnabled", Boolean.TRUE);
		// we need the renderer to draw everything on the batik provided graphics object
		rendererParams.put(StreamingRenderer.OPTIMIZE_FTS_RENDERING_KEY, Boolean.FALSE);
		// render everything in vector form if possible
        rendererParams.put(StreamingRenderer.VECTOR_RENDERING_KEY, Boolean.TRUE);
		rendererParams.put("renderingBuffer", new Integer(mapContext
				.getBuffer()));
		if(DefaultWebMapService.isLineWidthOptimizationEnabled()) {
            rendererParams.put(StreamingRenderer.LINE_WIDTH_OPTIMIZATION_KEY, true);
        }
        renderer.setRendererHints(rendererParams);
		renderer.setContext(mapContext);
	}

	public void writeTo(OutputStream out) throws ServiceException, IOException {
	    try {
			MapContext map = renderer.getContext();
			double width = -1;
			double height = -1;

			if (map instanceof WMSMapContext) {
				WMSMapContext wmsMap = (WMSMapContext) map;
				width = wmsMap.getMapWidth();
				height = wmsMap.getMapHeight();
			} else {
				// guess a width and height based on the envelope
				Envelope area = map.getAreaOfInterest();

				if ((area.getHeight() > 0) && (area.getWidth() > 0)) {
					if (area.getHeight() >= area.getWidth()) {
						height = 600;
						width = height * (area.getWidth() / area.getHeight());
					} else {
						width = 800;
						height = width * (area.getHeight() / area.getWidth());
					}
				}
			}

			if ((height == -1) || (width == -1)) {
				throw new IOException("Could not determine map dimensions");
			}

			SVGGeneratorContext context = setupContext();
			SVGGraphics2D g = new SVGGraphics2D(context, true);

			g.setSVGCanvasSize(new Dimension((int) width, (int) height));

			// turn off/on anti aliasing
			if (wmsConfig.isSvgAntiAlias()) {
				g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
						RenderingHints.VALUE_ANTIALIAS_ON);
			} else {
				g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
						RenderingHints.VALUE_ANTIALIAS_OFF);
			}

			// enforce no more than x rendering errors
            int maxErrors = wmsConfig.getMaxRenderingErrors();
            MaxErrorEnforcer errorChecker = new MaxErrorEnforcer(renderer, maxErrors);

            // Add a render listener that ignores well known rendering exceptions and reports back non
            // ignorable ones
            final RenderExceptionStrategy nonIgnorableExceptionListener;
            nonIgnorableExceptionListener = new RenderExceptionStrategy(renderer);
            renderer.addRenderListener(nonIgnorableExceptionListener);

            renderer.paint(g, new Rectangle(g.getSVGCanvasSize()), mapContext.getRenderingArea(), mapContext.getRenderingTransform());
			
			// check if too many errors occurred
            if(errorChecker.exceedsMaxErrors()) {
                throw new WmsException("More than " + maxErrors + " rendering errors occurred, bailing out.", 
                        "internalError", errorChecker.getLastException());
            }

            //check if a non ignorable error occurred
            if(nonIgnorableExceptionListener.exceptionOccurred()){
                Exception renderError = nonIgnorableExceptionListener.getException();
                throw new WmsException("Rendering process failed", "internalError", renderError);
            }
            
			// This method of output does not output the DOCTYPE definiition
			// TODO: make a config option that toggles wether doctype is
			// written out.
			OutputFormat format = new OutputFormat();
			XMLSerializer serializer = new XMLSerializer(
					new OutputStreamWriter(out, "UTF-8"), format);

            // this method does output the DOCTYPE def
             g.stream(new OutputStreamWriter(out,"UTF-8"));
	    } catch(ParserConfigurationException e) {
	        throw new WmsException("Unexpected exception", "internalError", e);
        } finally {
            // free up memory
            renderer = null;
        }
    }


	private SVGGeneratorContext setupContext()
			throws FactoryConfigurationError, ParserConfigurationException {
		Document document = null;

		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

		DocumentBuilder db = dbf.newDocumentBuilder();

		// Create an instance of org.w3c.dom.Document
		String svgNamespaceURI = "http://www.w3.org/2000/svg";
		document = db.getDOMImplementation().createDocument(svgNamespaceURI,
				"svg", null);

		// Set up the context
		return SVGGeneratorContext.createDefault(document);
	}
}
