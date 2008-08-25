/* Copyright (c) 2001, 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wms.responses;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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
    private String requestedOutputFormat;

    private final String mime;

    private final List <String> outputFormatNames;

    protected AbstractGetMapProducer(final String mime, final String outputFormat) {
        this( mime, new String[]{ outputFormat } );
    }

    protected AbstractGetMapProducer(final String mime, final String[] outputFormats) {
        this.mime = mime;
        if(outputFormats == null){
            outputFormatNames = Collections.emptyList();
        }else{
            outputFormatNames = Collections.unmodifiableList(new ArrayList<String>(Arrays.asList(outputFormats)));
        }
    }

    protected AbstractGetMapProducer() {
        this(null, (String[])null);
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
        if (!getOutputFormatNames().contains(outputFormat)) {
            throw new IllegalArgumentException(outputFormat + " is not a recognized output "
                    + "format for this GetMapProducer");
        }
        this.requestedOutputFormat = outputFormat;
    }

    /**
     * @see GetMapProducer#getContentDisposition()
     * @return {@code null}, subclasses should override as needed
     */
    public String getContentDisposition() {
        return null;
    }

    /**
     * @see GetMapProducer#getOutputFormatNames()
     */
    @SuppressWarnings("unchecked")
    public List<String> getOutputFormatNames() {
        return outputFormatNames;
    }
}
