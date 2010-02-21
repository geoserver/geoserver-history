/* Copyright (c) 2001, 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wms.responses;

import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;

import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.MapLayer;
import org.geotools.renderer.GTRenderer;
import org.geotools.renderer.lite.RendererUtilities;
import org.vfny.geoserver.wms.GetMapProducer;
import org.vfny.geoserver.wms.WMSMapContext;

/**
 * 
 * @author Simone Giannecchini, GeoSolutions
 * 
 */
public abstract class AbstractGetMapProducer implements GetMapProducer {
    /**
     * Holds the map context passed to produceMap, so subclasses can use it if
     * they need it from inside {@linkPlain #formatImageOutputStream(String,
     * BufferedImage, OutputStream)}
     */
    protected WMSMapContext mapContext;

    /**
     * set to <code>true</code> on <code>abort()</code> so
     * <code>produceMap</code> leaves the image being worked on to the garbage
     * collector.
     */
    protected boolean abortRequested;

    /**
     * The one to do the magic of rendering a map
     */
    protected GTRenderer renderer;

    /**
     * Set in produceMap(...) from the requested output format, it's holded just
     * to be sure that method has been called before getContentType() thus
     * supporting the workflow contract of the request processing
     */
    protected String requestedOutputFormat;

    private final String mime;

    /**
     * The list of GetCapabilities stated format names for this map producer.
     */
    private final Set<String> outputFormatNames;

    protected AbstractGetMapProducer(final String mime, final String outputFormat) {
        this(mime, new String[] { outputFormat });
    }

    protected AbstractGetMapProducer(final String mime, final String[] outputFormats) {
        this(mime,outputFormats != null ? Arrays.asList(outputFormats) : null);
    }
    
    protected AbstractGetMapProducer(final String mime, Collection<String> outputFormats ) {
        this.mime = mime;
        if( outputFormats == null ) {
            outputFormats = Collections.emptySet();
        }
        // Using a set that performs case insensitive look ups directly.
        Set<String> names = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);
        names.addAll(outputFormats);
        outputFormatNames = Collections.unmodifiableSet(names);
         
    }

    protected AbstractGetMapProducer() {
        this(null, (String[]) null);
    }

    /**
     * @see GetMapProducer#setMapContext(WMSMapContext)
     */
    public void setMapContext(WMSMapContext mapContext) {
        this.mapContext = mapContext;
    }

    /**
     * @see GetMapProducer#getMapContext()
     */
    public WMSMapContext getMapContext() {
        return this.mapContext;
    }

    /**
     * Halts the loading. Right now just calls renderer.stopRendering.
     */
    public void abort() {
        this.abortRequested = true;

        if (this.renderer != null) {
            this.renderer.stopRendering();
        }
    }

    /**
     * returns the content encoding for the output data (null for this class)
     * 
     * @return <code>null</code> since no special encoding is performed while
     *         writting to the output stream. Do not confuse this with
     *         getMimeType().
     */
    public String getContentEncoding() {
        return null;
    }

    /**
     * @see GetMapProducer#getContentType()
     */
    public String getContentType() {
        return mime;
    }

    /**
     * @see GetMapProducer#getOutputFormat()
     */
    public String getOutputFormat() {
        return requestedOutputFormat == null ? getContentType() : requestedOutputFormat;
    }

    /**
     * @see GetMapProducer#setOutputFormat(String)
     */
    public void setOutputFormat(final String outputFormat) {
        // this lookup is made in a case insensitive manner, see
        // outputFormatNames definition
        if (outputFormatNames.contains(outputFormat)) {
            this.requestedOutputFormat = outputFormat;
        }else{
            throw new IllegalArgumentException(outputFormat + " is not a recognized output "
                + "format for " + getClass().getSimpleName());
        }
    }

    /**
     * @see GetMapProducer#getContentDisposition()
     * @return {@code null}, subclasses should override as needed
     */
    public String getContentDisposition() {
        return null;
    }
    

    /**
     * Utility method to build a standard content disposition header.
     * It will concatenate the titles of the various layers in the map context,
     * or generate "geoserver" instead (in the event no layer title is set).
     * The file name will be followed by the extension provided, for example, 
     * to generate layer.pdf extension will be ".pdf"
     * @param extension
     * @return
     */
    protected String getContentDisposition(String extension) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < mapContext.getLayerCount(); i++) {
            MapLayer layer = mapContext.getLayer(i);
            String title = layer.getTitle();
            if (title != null && !title.equals("")) {
                sb.append(title).append("_");
            }
        }
        if (sb.length() > 0) {
            sb.setLength(sb.length() - 1);
            return "attachment; filename=" + sb.toString() + extension;
        }
        return "attachment; filename=geoserver" + extension;
    }

    /**
     * @see GetMapProducer#getOutputFormatNames()
     */
    @SuppressWarnings("unchecked")
    public Set<String> getOutputFormatNames() {
        return outputFormatNames;
    }
    
    /**
     * Returns the transformation going from the map area space to the screen space taking into
     * account map rotation
     * @return
     */
    protected AffineTransform getRenderingTransform() {
        Rectangle paintArea = new Rectangle(0, 0, mapContext.getMapWidth(), mapContext
                .getMapHeight());
        ReferencedEnvelope dataArea = mapContext.getAreaOfInterest();
        AffineTransform tx;
        if (mapContext.getAngle() != 0.0) {
            tx = new AffineTransform();
            tx.translate(paintArea.width / 2, paintArea.height / 2);
            tx.rotate(Math.toRadians(mapContext.getAngle()));
            tx.translate(-paintArea.width / 2, -paintArea.height / 2);
            tx.concatenate(RendererUtilities.worldToScreenTransform(dataArea, paintArea));
        } else {
            tx = RendererUtilities.worldToScreenTransform(dataArea, paintArea);
        }
        return tx;
    }

}
