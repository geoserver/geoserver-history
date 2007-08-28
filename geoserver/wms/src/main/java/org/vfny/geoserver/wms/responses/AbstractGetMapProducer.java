/* Copyright (c) 2001, 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wms.responses;

import org.geotools.renderer.GTRenderer;
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
    protected String format;
    protected String mime;

    public AbstractGetMapProducer(String format, String mime) {
        this.format = format;
        this.mime = mime;
    }

    public AbstractGetMapProducer() {
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
     *         wrtting to the output stream. Do not confuse this with
     *         getMimeType().
     */
    public String getContentEncoding() {
        return null;
    }

    /**
     * Gets the content type. This is set by the request, should only be called
     * after execute. GetMapResponse should handle this though.
     *
     * @return The mime type that this response will generate.
     *
     * @throws IllegalStateException
     *             DOCUMENT ME!
     */
    public String getContentType() throws java.lang.IllegalStateException {
        return mime;
    }

    public void setContentType(String mime) {
        this.mime = mime;
    }

    public String getOutputFormat() {
        return format;
    }

    public void setOutputFormat(String outputFormat) {
        this.format = outputFormat;
    }
    
	public String getContentDisposition() {
		return null;
	}
}
