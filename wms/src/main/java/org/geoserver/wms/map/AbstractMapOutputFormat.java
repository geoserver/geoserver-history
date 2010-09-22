/* Copyright (c) 2001, 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.wms.map;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.geoserver.ows.Response;
import org.geoserver.ows.util.OwsUtils;
import org.geoserver.platform.Operation;
import org.geoserver.platform.ServiceException;
import org.geoserver.wms.GetMapOutputFormat;
import org.geoserver.wms.WMSMapContext;
import org.geoserver.wms.request.GetMapRequest;
import org.geoserver.wms.response.Map;
import org.geotools.map.MapLayer;
import org.springframework.util.Assert;

/**
 * 
 * @author Simone Giannecchini, GeoSolutions
 * @author Gabriel Roldan
 */
public abstract class AbstractMapOutputFormat extends Response implements GetMapOutputFormat {

    private final String mime;

    protected AbstractMapOutputFormat(final Class<? extends Map> responseBinding, final String mime) {
        this(responseBinding, mime, new String[] { mime });
    }

    @SuppressWarnings("unchecked")
    protected AbstractMapOutputFormat(final Class<? extends Map> responseBinding,
            final String mime, final String[] outputFormats) {
        this(responseBinding, mime, outputFormats == null ? Collections.EMPTY_SET
                : new HashSet<String>(Arrays.asList(outputFormats)));
    }

    protected AbstractMapOutputFormat(final Class<? extends Map> responseBinding,
            final String mime, Set<String> outputFormats) {
        // Call Response superclass constructor with the kind of request we can handle
        super(responseBinding, outputFormats);
        this.mime = mime;
        if (outputFormats == null) {
            outputFormats = Collections.emptySet();
        }
    }

    protected AbstractMapOutputFormat() {
        this(null, null, (String[]) null);
    }

    /**
     * Halts the loading. Right now just calls renderer.stopRendering.
     */
    // public void abort() {
    // this.abortRequested = true;
    //
    // if (this.renderer != null) {
    // this.renderer.stopRendering();
    // }
    // }

    /**
     * @see GetMapOutputFormat#getMimeType()
     */
    public String getMimeType() {
        return mime;
    }

    /**
     * @return {@link #getMimeType()}
     * @see org.geoserver.ows.Response#getMimeType(java.lang.Object,
     *      org.geoserver.platform.Operation)
     */
    @Override
    public String getMimeType(Object value, Operation operation) throws ServiceException {
        return getMimeType();
    }

    /**
     * @see GetMapOutputFormat#getOutputFormatNames()
     */
    public Set<String> getOutputFormatNames() {
        return super.getOutputFormats();
    }

    /**
     * Evaluates whether this response can handle the given operation by checking if the operation's
     * request is a {@link GetMapRequest} and the requested output format is contained in
     * {@link #getOutputFormatNames()}.
     * <p>
     * NOTE: requested MIME Types may come with parameters, like, for example:
     * {@code image/png;param1=value1}. This default canHandle implementation performs and exact
     * match check against the requested and supported format names. Subclasses may feel free to
     * override if needed.
     * </p>
     * 
     * @see org.geoserver.ows.Response#canHandle(org.geoserver.platform.Operation)
     */
    @Override
    public boolean canHandle(final Operation operation) {
        GetMapRequest request;
        Object[] parameters = operation.getParameters();
        request = (GetMapRequest) OwsUtils.parameter(parameters, GetMapRequest.class);
        if (request == null) {
            return false;
        }
        String outputFormat = request.getFormat();
        Set<String> outputFormats = getOutputFormats();
        boolean match = outputFormats.contains(outputFormat);
        return match;
    }

    /**
     * Returns a 2xn array of Strings, each of which is an HTTP header pair to be set on the HTTP
     * Response. Can return null if there are no headers to be set on the response.
     * 
     * @param value
     *            must be a {@link Map}
     * @param operation
     *            The operation being performed.
     * 
     * @return 2xn string array containing string-pairs of HTTP headers/values
     * @see Response#getHeaders(Object, Operation)
     * @see Map#getResponseHeaders()
     */
    @Override
    public String[][] getHeaders(Object value, Operation operation) throws ServiceException {
        Assert.isInstanceOf(Map.class, value);
        Map map = (Map) value;
        String[][] responseHeaders = map.getResponseHeaders();
        return responseHeaders;
    }

    /**
     * Utility method to build a standard content disposition header. It will concatenate the titles
     * of the various layers in the map context, or generate "geoserver" instead (in the event no
     * layer title is set). The file name will be followed by the extension provided, for example,
     * to generate layer.pdf extension will be ".pdf"
     * 
     * @param extension
     * @return
     */
    protected String getContentDisposition(WMSMapContext mapContext, String extension) {
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

}
