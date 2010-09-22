/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.wms.response;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.opengis.wfs.FeatureCollectionType;

import org.geoserver.ows.Response;
import org.geoserver.platform.Operation;
import org.geoserver.platform.ServiceException;
import org.geoserver.wms.GetFeatureInfo;
import org.geoserver.wms.request.GetFeatureInfoRequest;
import org.geotools.data.wms.response.GetMapResponse;
import org.springframework.util.Assert;
import org.vfny.geoserver.wms.WmsException;
import org.vfny.geoserver.wms.responses.featureInfo.GmlFeatureInfoResponse;
import org.vfny.geoserver.wms.responses.featureInfo.HTMLTableFeatureInfoResponse;
import org.vfny.geoserver.wms.responses.featureInfo.TextFeatureInfoResponse;

/**
 * A GetFeatureInfoResponse object is responsible for generating GetFeatureInfo content in the
 * format specified. The way the content is generated is independent of this class, wich will use a
 * delegate object based on the output format requested
 * 
 * @author Gabriel Roldan
 * @version $Id$
 */
public class GetFeatureInfoResponse extends Response {
    /** package logger */
    private static final Logger LOGGER = org.geotools.util.logging.Logging
            .getLogger(GetMapResponse.class.getPackage().getName());

    /** list of output format specialists */
    private static final List<GetFeatureInfoOutputFormat> delegates = new LinkedList<GetFeatureInfoOutputFormat>();

    /**
     * The list of all the supported output formats
     */
    private static final List<String> supportedMimeTypes = new LinkedList<String>();

    static {
        GetFeatureInfoOutputFormat producer;

        producer = new TextFeatureInfoResponse();
        supportedMimeTypes.addAll(producer.getSupportedFormats());
        delegates.add(producer);

        producer = new HTMLTableFeatureInfoResponse();
        supportedMimeTypes.addAll(producer.getSupportedFormats());
        delegates.add(producer);

        producer = new GmlFeatureInfoResponse();
        supportedMimeTypes.addAll(producer.getSupportedFormats());
        delegates.add(producer);
    }

    /**
     * Creates a new GetMapResponse object.
     */
    public GetFeatureInfoResponse() {
        super(GetFeatureInfoRequest.class);
    }

    /**
     * Asks the available GetFeatureInfoOutputFormats for the MIME type of the result that it will
     * generate or is ready to, and returns it
     * 
     * @param value
     *            a {@link FeatureCollectionType} as returned by {@link GetFeatureInfo}
     * 
     * @param operation
     *            the {@link GetFeatureInfo} operation that originated the {@code value}
     * 
     * @see org.geoserver.ows.Response#getMimeType(java.lang.Object,
     *      org.geoserver.platform.Operation)
     */
    @Override
    public String getMimeType(final Object value, final Operation operation)
            throws ServiceException {
        Assert.notNull(value, "value is null");
        Assert.notNull(operation, "operation is null");
        Assert.isTrue(value instanceof FeatureCollectionType, "unrecognized result type:");
        Assert.isTrue(operation.getParameters() != null && operation.getParameters().length == 1
                && operation.getParameters()[0] instanceof GetFeatureInfoRequest);

        GetFeatureInfoRequest request = (GetFeatureInfoRequest) operation.getParameters()[0];
        GetFeatureInfoOutputFormat outputFormat = getDelegate(request);

        return outputFormat.getContentType();

    }

    /**
     * @param value
     *            {@link FeatureCollectionType}
     * @param output
     *            where to encode the results to
     * @param operation
     *            {@link GetFeatureInfo}
     * @see org.geoserver.ows.Response#write(java.lang.Object, java.io.OutputStream,
     *      org.geoserver.platform.Operation)
     */
    @Override
    public void write(final Object value, final OutputStream output, final Operation operation)
            throws IOException, ServiceException {

        Assert.notNull(value, "value is null");
        Assert.notNull(operation, "operation is null");
        Assert.isTrue(value instanceof FeatureCollectionType, "unrecognized result type:");
        Assert.isTrue(operation.getParameters() != null && operation.getParameters().length == 1
                && operation.getParameters()[0] instanceof GetFeatureInfoRequest);

        GetFeatureInfoRequest request = (GetFeatureInfoRequest) operation.getParameters()[0];
        FeatureCollectionType results = (FeatureCollectionType) value;
        GetFeatureInfoOutputFormat outputFormat = getDelegate(request);

        outputFormat.write(results, request, output);
    }

    /**
     * Creates a GetMapDelegate specialized in generating the requested map format
     * 
     * @param request
     *            a request parameter object wich holds the processed request objects, such as
     *            layers, bbox, outpu format, etc.
     * 
     * @return A specialization of <code>GetMapDelegate</code> wich can produce the requested output
     *         map format
     * 
     * @throws WmsException
     *             if no specialization is configured for the output format specified in
     *             <code>request</code> or if it can't be instantiated
     */
    private static GetFeatureInfoOutputFormat getDelegate(GetFeatureInfoRequest request)
            throws ServiceException {
        String requestFormat = request.getInfoFormat();

        if (LOGGER.isLoggable(Level.FINER)) {
            LOGGER.finer(new StringBuffer("request format is ").append(requestFormat).toString());
        }

        GetFeatureInfoOutputFormat curDelegate = null;
        Class delegateClass = null;

        for (Iterator it = delegates.iterator(); it.hasNext();) {
            curDelegate = (GetFeatureInfoOutputFormat) it.next();

            if (curDelegate.canProduce(requestFormat)) {
                delegateClass = curDelegate.getClass();

                if (LOGGER.isLoggable(Level.FINER)) {
                    LOGGER.finer(new StringBuffer("found GetFeatureInfoOutputFormat ").append(
                            delegateClass).toString());
                }

                break;
            }
        }

        if (delegateClass == null) {
            // let's default to something sensible. If the parameter is empty we return a
            // TextFeatureInfoResponse, so let's do the same thing here. See the "hack" comment in
            // GetFeatureInfoKVPReader.java.
            delegateClass = TextFeatureInfoResponse.class;
        }

        try {
            curDelegate = (GetFeatureInfoOutputFormat) delegateClass.newInstance();
        } catch (Exception ex) {
            throw new ServiceException(ex,
                    "Cannot obtain the map generator for the requested format",
                    "GetMapResponse::getDelegate()");
        }

        return curDelegate;
    }

    /**
     * iterates over the registered Map producers and fills a list with all the map formats' MIME
     * types that the producers can handle
     * 
     * @return DOCUMENT ME!
     */
    public static List<String> getFormats() {
        return supportedMimeTypes;
    }

}
