/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wms.responses;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.transform.TransformerException;

import org.vfny.geoserver.Request;
import org.vfny.geoserver.Response;
import org.vfny.geoserver.ServiceException;
import org.vfny.geoserver.global.GeoServer;
import org.vfny.geoserver.global.Service;
import org.vfny.geoserver.wms.WmsException;
import org.vfny.geoserver.wms.requests.DescribeLayerRequest;
import org.vfny.geoserver.wms.responses.helpers.DescribeLayerTransformer;


/**
 * Executes a <code>DescribeLayer</code> WMS request.
 *
 * <p>
 * Recieves a <code>DescribeLayerRequest</code> object holding the references to
 * the requested layers and utilizes a transformer based on the org.geotools.xml.transform
 * framework to encode the response.
 * </p>
 *
 * @author Gabriel Roldan, Axios Engineering
 * @version $Id$
 */
public class DescribeLayerResponse implements Response {
    /** DOCUMENT ME! */
    private static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger(DescribeLayerResponse.class.getPackage()
                                                                                     .getName());
    public static final String DESCLAYER_MIME_TYPE = "application/vnd.ogc.wms_xml";

    /** the request holding the required FeatureTypeInfo's */
    private DescribeLayerRequest request;

    /** the transformer wich takes care of xmlencoding the
     * DescribeLayer response
     */
    private DescribeLayerTransformer transformer;

    /** the raw XML content ready to be sent to the client */
    private byte[] content;

    /**
     * Returns any extra headers that this service might want to set in the HTTP response object.
     * @see org.vfny.geoserver.Response#getResponseHeaders()
     */
    public HashMap getResponseHeaders() {
        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @param request DOCUMENT ME!
     *
     * @throws ServiceException DOCUMENT ME!
     * @throws WmsException DOCUMENT ME!
     */
    public void execute(Request request) throws ServiceException {
        this.request = (DescribeLayerRequest) request;

        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine(new StringBuffer("executing request ").append(request).toString());
        }

        this.transformer = new DescribeLayerTransformer(this.request.getBaseUrl(), request.getServiceConfig().getGeoServer(), request.getVersion());
        this.transformer.setNamespaceDeclarationEnabled(false);
        Charset encoding = this.request.getWMS().getCharSet();
        this.transformer.setEncoding(encoding);

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            transformer.transform(this.request, out);
        } catch (TransformerException e) {
            throw new WmsException(e);
        }

        this.content = out.toByteArray();
    }

    /**
     * Writes this respone to the provided output stream.
     *
     * @param out DOCUMENT ME!
     *
     * @throws ServiceException never.
     * @throws IOException if it is thrown while writing the response content
     *         to <code>out</code>.
     * @throws IllegalStateException if <code>execute()</code> has not been
     *         called or  does not succeed (i.e.: <code>this.content ==
     *         null</code>).
     */
    public void writeTo(OutputStream out) throws ServiceException, IOException {
        if (this.content == null) {
            throw new IllegalStateException("execute() has not been called or does not succeed.");
        }

        out.write(this.content);
    }

    /**
     * Do nothing, since <code>execute()</code> took care of obtaining the
     * response, and after that nothing remains to be done but sending the
     * response content to the client.
     *
     * @param gs
     */
    public void abort(Service gs) {
    }

    /**
     * Returns the fixed <code>"application/vnd.ogc.wms_xml"</code> MIME type
     * of this response, as specified in SLD 1.0 spec, section 6.7.
     *
     * @param gs the geoserver instance config. Not used here.
     *
     * @return <code>"application/vnd.ogc.wms_xml"</code> as the response MIME
     *         type
     *
     * @throws IllegalStateException DOCUMENT ME!
     */
    public String getContentType(GeoServer gs) throws IllegalStateException {
        return DESCLAYER_MIME_TYPE;
    }

    /**
     * Returns <code>null</code> since no special encoding is applyed to the
     * response content.
     *
     * @return <code>null</code>
     */
    public String getContentEncoding() {
        return null;
    }

    /* (non-Javadoc)
     * @see org.vfny.geoserver.Response#getContentDisposition()
     */
    public String getContentDisposition() {
        // TODO Auto-generated method stub
        return null;
    }
}
