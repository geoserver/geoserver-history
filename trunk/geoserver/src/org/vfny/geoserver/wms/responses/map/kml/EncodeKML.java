/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wms.responses.map.kml;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.DirectColorModel;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import javax.imageio.stream.MemoryCacheImageOutputStream;
import javax.media.jai.GraphicsJAI;
import javax.media.jai.PlanarImage;

import org.geotools.data.DataUtilities;
import org.geotools.data.DefaultQuery;
import org.geotools.data.FeatureReader;
import org.geotools.data.FeatureSource;
import org.geotools.data.Query;
import org.geotools.feature.AttributeType;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureType;
import org.geotools.feature.GeometryAttributeType;
import org.geotools.filter.Filter;
import org.geotools.filter.FilterFactory;
import org.geotools.filter.FilterFactoryFinder;
import org.geotools.filter.FilterFactoryImpl;
import org.geotools.filter.GeometryFilter;
import org.geotools.filter.IllegalFilterException;
import org.geotools.filter.expression.BBoxExpression;
import org.geotools.filter.expression.Expression;
import org.geotools.map.MapContext;
import org.geotools.map.MapLayer;
import org.geotools.renderer.lite.RendererUtilities;
import org.geotools.renderer.lite.StreamingRenderer;
import org.geotools.resources.image.ImageUtilities;
import org.vfny.geoserver.wms.WMSMapContext;

import com.vividsolutions.jts.geom.Envelope;


/**
 * 
 */
public class EncodeKML {
    /** Standard Logger */
    private static final Logger LOGGER = Logger.getLogger(
            "org.vfny.geoserver.responses.wms.map.kml");
    
    /** Filter factory for creating bounding box filters */
    private FilterFactory filterFactory = FilterFactoryFinder.createFilterFactory();

    /** the XML and KML header */
    private static final String KML_HEADER =
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n\t"
            +"<kml xmlns=\"http://earth.google.com/kml/2.0\">\n";
    
    /** the KML closing element */
    private static final String KML_FOOTER = "</kml>\n";

	/**
	 * Map context document - layers, styles aoi etc.
	 * 
	 * @uml.property name="mapContext"
	 * @uml.associationEnd multiplicity="(1 1)"
	 */
	private WMSMapContext mapContext;

	/**
	 * Actualy writes the KML out
	 * 
	 * @uml.property name="writer"
	 * @uml.associationEnd multiplicity="(0 1)"
	 */
	private KMLWriter writer;

    
    /** Flag to be monotored by writer loops */
    private boolean abortProcess;
    
    /**
     * Creates a new EncodeKML object.
     *
     * @param mapContext A full description of the map to be encoded.
     */
    public EncodeKML(WMSMapContext mapContext) {
        this.mapContext = mapContext;
    }
    
    /**
     * Sets the abort flag.  Active encoding may be halted, but this is not garanteed.
     */
    public void abort() {
        abortProcess = true;
    }
    
    /**
     * Perform the actual encoding.  May return early if abort it called.
     *
     * @param out Ouput stream to send the data to.
     *
     * @throws IOException Thrown if anything goes wrong whilst writing
     */
    public void encode(final OutputStream out) throws IOException {
        Envelope env = this.mapContext.getAreaOfInterest();
        this.writer = new KMLWriter(out, mapContext);
        //once KML supports bbox queries against WMS this can be used to 
        //decimate the geometries based on zoom level.
        //writer.setMinCoordDistance(env.getWidth() / 1000);
        
        abortProcess = false;
        
        long t = System.currentTimeMillis();
        try {
            writeHeader();
            writeLayers(false);
            writeFooter();
            
            this.writer.flush();
            t = System.currentTimeMillis() - t;
            if (LOGGER.isLoggable(Level.FINE)) {
            	LOGGER.fine(new StringBuffer("KML generated, it took").append(t).append(" ms").toString());
            }
        } catch (IOException ioe) {
            if (abortProcess) {
                LOGGER.fine("KML encoding aborted");
                return;
            } else {
                throw ioe;
            }
        } catch (AbortedException ex) {
            return;
        }
    }
    
    /**
     * This method is used to encode kml + images and put all the stuff into a KMZ
     * file.
     * 
     * @param out
     * @throws IOException
     */
    public void encode2(final ZipOutputStream out) throws IOException {
        Envelope env = this.mapContext.getAreaOfInterest();
        this.writer = new KMLWriter(out, mapContext);
        
        abortProcess = false;
        
        long t = System.currentTimeMillis();
        try {
        	// first we produce the KML file containing the code and the PlaceMarks
			final ZipEntry e = new ZipEntry("wms.kml");
			out.putNextEntry(e);
            writeHeader();
            writeLayers(true);
            writeFooter();
            this.writer.flush();
            out.closeEntry();

            // then we produce and store all the layer images
            writeImages(out);
            
            t = System.currentTimeMillis() - t;
            if (LOGGER.isLoggable(Level.FINE)){
            	LOGGER.fine(new StringBuffer("KMZ generated, it took").append(t).append(" ms").toString());
            }
        } catch (IOException ioe) {
            if (abortProcess) {
            	if (LOGGER.isLoggable(Level.FINE))
            		LOGGER.fine("KMZ encoding aborted");
                return;
            } else {
                throw ioe;
            }
        } catch (AbortedException ex) {
            return;
        }
    }    

    /**
     * writes out standard KML header
     *
     * @throws IOException
     */
    private void writeHeader() throws IOException {
        writer.write(KML_HEADER);
    }
   
    /**
     * writes out standard KML footer
     *
     * @throws IOException DOCUMENT ME!
     */
    private void writeFooter() throws IOException {
        writer.write(KML_FOOTER);
    }
    
    
    /**
     * Processes each of the layers within the current mapContext in turn.
     * 
     * @throws IOException
     * @throws AbortedException
     *
     * @task TODO: Wrap each layer in a 'Folder' tag
     */
    private void writeLayers(final boolean kmz) throws IOException, AbortedException {
        MapLayer[] layers = mapContext.getLayers();
        int nLayers = layers.length;
        int defMaxDecimals = writer.getMaximunFractionDigits();
        
        FilterFactory fFac = new FilterFactoryImpl();
        
        writer.startDocument("GeoServer", null);
        for (int i = 0; i < nLayers; i++) {
            MapLayer layer = layers[i];
            FeatureReader featureReader = null;
            FeatureSource fSource = layer.getFeatureSource();
            FeatureType schema = fSource.getSchema();
            
            String[] attributes;
			
            AttributeType[] ats = schema.getAttributeTypes();
            final int length = ats.length;
            attributes = new String[length];
            for (int t = 0; t < length; t++) {
            	attributes[t] = ats[t].getName();
            }

            try {
            	Filter filter = null;
            	BBoxExpression rightBBox = filterFactory.createBBoxExpression(mapContext
                        .getAreaOfInterest());
                filter = createBBoxFilters(schema, attributes, rightBBox);
                
                // now build the query using only the attributes and the bounding
                // box needed
                DefaultQuery q = new DefaultQuery(schema.getTypeName());
                q.setFilter(filter);
                q.setPropertyNames(attributes);
                // now, if a definition query has been established for this layer, be
                // sure to respect it by combining it with the bounding box one.
                Query definitionQuery = layer.getQuery();
                
                if (definitionQuery != Query.ALL) {
                    if (q == Query.ALL) {
                        q = (DefaultQuery) definitionQuery;
                    } else {
                        q = (DefaultQuery) DataUtilities.mixQueries(definitionQuery, q, "KMLEncoder");
                    }
                }
                
                q.setCoordinateSystem(
                        layer.getFeatureSource().getSchema().getDefaultGeometry().getCoordinateSystem());
                
                featureReader = fSource.getFeatures(q).reader();
                FeatureCollection fc = fSource.getFeatures(q);
                writer.writeFeatures(fc, layer, i+1, kmz);
                	
                if (LOGGER.isLoggable(Level.FINE))
                	LOGGER.fine("finished writing");
            } catch (IOException ex) {
            	if (LOGGER.isLoggable(Level.INFO))
            		LOGGER.info(new StringBuffer("process failed: ").append(ex.getMessage()).toString());
                throw ex;
            } catch (AbortedException ae) {
            	if (LOGGER.isLoggable(Level.INFO))
            		LOGGER.info(new StringBuffer("process aborted: ").append(ae.getMessage()).toString());
                throw ae;
            } catch (Throwable t) {
            	if (LOGGER.isLoggable(Level.WARNING))
            		LOGGER.warning(new StringBuffer("UNCAUGHT exception: ").append(t.getMessage()).toString());
                
                IOException ioe = new IOException(new StringBuffer("UNCAUGHT exception: ").
                        append(t.getMessage()).toString());
                ioe.setStackTrace(t.getStackTrace());
                throw ioe;
            } finally {
                if (featureReader != null) {
                    try{
                        featureReader.close();
                    }catch(IOException ioe){
                        //featureReader was probably closed already.
                    }
                }
            }
        }
        writer.endDocument();
    }

    /**
     * This method produces and stores PNG images of all map layers using the StreamingRenderer and JAI Encoder.
     * 
     * @param outZ
     * @throws IOException
     * @throws AbortedException
     */
    private void writeImages(final ZipOutputStream outZ) throws IOException, AbortedException {
        MapLayer[] layers = this.mapContext.getLayers();
        int nLayers = layers.length;
        int defMaxDecimals = writer.getMaximunFractionDigits();
        
        FilterFactory fFac = new FilterFactoryImpl();
        
        for (int i = 0; i < nLayers; i++) {
            final MapLayer layer = layers[i];
            MapContext map = this.mapContext;
            map.clearLayerList();
            map.addLayer(layer);
    		final int width = this.mapContext.getMapWidth();
    		final int height = this.mapContext.getMapHeight();

    		if (LOGGER.isLoggable(Level.FINE)) {
    			LOGGER.fine(new StringBuffer("setting up ").append(width).append(
    					"x").append(height).append(" image").toString());
    		}
    		// simone: ARGB should be much better
    		BufferedImage curImage = new BufferedImage(width, height,
    				BufferedImage.TYPE_4BYTE_ABGR);

    		// simboss: this should help out with coverages
    		final Graphics2D graphic = GraphicsJAI.createGraphicsJAI(curImage
    				.createGraphics(), null);

    		if (LOGGER.isLoggable(Level.FINE)) {
    			LOGGER.fine("setting to transparent");
    		}
    		
    		int type = AlphaComposite.SRC;
    		graphic.setComposite(AlphaComposite.getInstance(type));
    		
    		Color c = new Color(this.mapContext.getBgColor().getRed(), this.mapContext.getBgColor()
    				.getGreen(), this.mapContext.getBgColor().getBlue(), 0);
    		graphic.setBackground(this.mapContext.getBgColor());
    		graphic.setColor(c);
    		graphic.fillRect(0, 0, width, height);
    		
    		type = AlphaComposite.SRC_OVER;
    		graphic.setComposite(AlphaComposite.getInstance(type));

    		Rectangle paintArea = new Rectangle(width, height);

    		final StreamingRenderer renderer = new StreamingRenderer();
    		renderer.setContext(map);

    		RenderingHints hints = new RenderingHints(
    				RenderingHints.KEY_ANTIALIASING,
    				RenderingHints.VALUE_ANTIALIAS_ON);
    		renderer.setJava2DHints(hints);

    		// we already do everything that the optimized data loading does...
    		// if we set it to true then it does it all twice...
    		Map rendererParams = new HashMap();
    		rendererParams.put("optimizedDataLoadingEnabled", Boolean.TRUE);

    		renderer.setRendererHints(rendererParams);

    		Envelope dataArea = map.getAreaOfInterest();
    		AffineTransform at = RendererUtilities.worldToScreenTransform(dataArea,
    				paintArea);
    		renderer.paint(graphic, paintArea, dataArea, at);
    		graphic.dispose();
    		
			// /////////////////////////////////////////////////////////////////
			//
			// Storing Image ...
			//
			// /////////////////////////////////////////////////////////////////
			final ZipEntry e = new ZipEntry("layer_"+(i+1)+".png");
			outZ.putNextEntry(e);
			final MemoryCacheImageOutputStream memOutStream = new MemoryCacheImageOutputStream(
					outZ);
			final PlanarImage encodedImage = PlanarImage
					.wrapRenderedImage(curImage);
			final PlanarImage finalImage = encodedImage.getColorModel() instanceof DirectColorModel?ImageUtilities
					.reformatColorModel2ComponentColorModel(encodedImage):encodedImage;
			final Iterator it = ImageIO.getImageWritersByMIMEType("image/png");
			ImageWriter imgWriter = null;
			if (!it.hasNext()) {
				throw new IllegalStateException("No PNG ImageWriter found");
			} else
				imgWriter = (ImageWriter) it.next();

			imgWriter.setOutput(memOutStream);
			imgWriter.write(null, new IIOImage(finalImage, null, null), null);
			memOutStream.flush();
			memOutStream.close();
			imgWriter.dispose();
			outZ.closeEntry();
        }
    }
    
    /**
     * Creates the bounding box filters (one for each geometric attribute) needed to query a
     * <code>MapLayer</code>'s feature source to return just the features for the target
     * rendering extent
     *
     * @param schema the layer's feature source schema
     * @param attributes set of needed attributes
     * @param bbox the expression holding the target rendering bounding box
     * @return an or'ed list of bbox filters, one for each geometric attribute in
     *         <code>attributes</code>. If there are just one geometric attribute, just returns
     *         its corresponding <code>GeometryFilter</code>.
     * @throws IllegalFilterException if something goes wrong creating the filter
     */
    private Filter createBBoxFilters( FeatureType schema, String[] attributes, BBoxExpression bbox )
    throws IllegalFilterException {
        Filter filter = null;
        
		final int length = attributes.length;
		for (int j = 0; j < length; j++) {
            AttributeType attType = schema.getAttributeType(attributes[j]);
            
            //DJB: added this for better error messages!
			if (attType == null) {
				if (LOGGER.isLoggable(Level.FINE))
					LOGGER.fine(new StringBuffer("Could not find '").append(
							attributes[j]).append("' in the FeatureType (")
							.append(schema.getTypeName()).append(")")
							.toString());
				throw new IllegalFilterException(new StringBuffer(
						"Could not find '").append(
						attributes[j] + "' in the FeatureType (").append(
						schema.getTypeName()).append(")").toString());
			}
            
            if (attType instanceof GeometryAttributeType) {
                GeometryFilter gfilter = filterFactory.createGeometryFilter(Filter.GEOMETRY_BBOX);
                
                // TODO: how do I get the full xpath of an attribute should
                // feature composition be used?
                Expression left = filterFactory
                        .createAttributeExpression(schema, attType.getName());
                gfilter.addLeftGeometry(left);
                gfilter.addRightGeometry(bbox);
                
                if (filter == null) {
                    filter = gfilter;
                } else {
                    filter = filter.or(gfilter);
                }
            }
        }
        
        return filter;
    }
}
