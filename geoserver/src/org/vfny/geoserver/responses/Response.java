/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.responses;

import org.vfny.geoserver.ServiceException;
import org.vfny.geoserver.requests.Request;
import java.io.*;


/**
 * The Response interface serves as a common denominator for all service
 * operations that generates content.
 *
 * <p>
 * The work flow for this kind of objects is divided in two parts: the first is
 * executing a request and the second writing the result to an OuputStream.<br>
 * Executing the request means taking a Request object and, based on it's set
 * of request parameters, do any heavy processing necessary to produce the
 * response. Once the execution has been made, the Response object should be
 * ready to send the response content to an output stream with minimal risk of
 * generating an exception. Anyway, it is not required, even recomended, that
 * the execution process generates the response content itself; just that it
 * performs any query or processing that should generate a trapable error.
 * This is specially usefull for streamed responses such as wfs GetFeature or
 * WMS GetMap, where the execution process can be used to parse parameters,
 * execute queries upon the corresponding data sources and leave things ready
 * to generate a streamed response when the consumer calls writeTo.
 * </p>
 *
 * <p></p>
 *
 * @author Gabriel Roldán
 * @version $Id: Response.java,v 1.1.2.4 2003/11/14 20:39:14 groldan Exp $
 */
public interface Response {
    /**
     * Excecutes a request. If this method finalizes without throwing an
     * Exception, the Response instance should be ready to write the response
     * content through the writeTo method with the minimal posible risk of
     * failure other than not beeing able to write to the output stream due to
     * external reassons
     *
     * <p>
     * We should clarify when a ServiceException is thrown? I would assume that
     * a "failed" request should still result in a Response that we could
     * write out.
     * </p>
     *
     * @param request a Request object that implementations should cast to it's
     *        Request specialization, wich must contain the parsed and ready
     *        to use parameters sent by the calling client. In general, such a
     *        Request will be created by either a KVP or XML request reader;
     *        resulting in a Request object more usefull than a set of raw
     *        parameters, as can be the list of feature types requested as a
     *        set of FeatureTypeConfig objects rather than just a list of
     *        String type names
     *
     * @throws ServiceException
     */
    public void execute(Request request) throws ServiceException;

    /**
     * MIME type of this Response - example <code>"text/xml"</code>.
     *
     * <p>
     * thinked to be called after excecute(), this method must return the MIME
     * type of the response content that will be writen when writeTo were
     * called
     * </p>
     *
     * <p>
     * an implementation of this interface is required to throw an
     * IllegalStateException if execute has not been called yet, to indicate
     * that an inconsistence in the work flow that may result in an
     * inconsistence between the response content and the content type
     * declared for it, if such an implementation can return different
     * contents based on the request that has originated it. i.e. a WMS GetMap
     * response will return different content encodings based on the FORMAT
     * requested, so it would be impossible to it knowing the exact MIME
     * response type if it has not processed the request yet.
     * </p>
     *
     * <p>
     * There is some MIME stuff in JDK for reference:
     *
     * <ul>
     * <li>
     * java.awt.datatransfer package
     * </li>
     * <li>
     * javax.mail.internet
     * </li>
     * <li>
     * and a few other places as well.
     * </li>
     * </ul>
     * </p>
     *
     * @return the MIME type of the generated or ready to generate response
     *         content
     *
     * @throws IllegalStateException if this method is called and execute has
     *         not been called yet
     */
    public String getContentType() throws IllegalStateException;

    /**
     * Writes this respone to the provided output stream.
     *
     * <p>
     * To implememt streaming, execution is sometimes delayed until the write
     * opperation (for example of this see FeatureResponse). Hopefully this is
     * okay? GR:the idea for minimize risk error at writing time, is that
     * execute performs any needed query/processing, leaving to this method
     * just the risk of encountering an uncaught or IO exception. i.e.
     * FeatureResponse should execute the queries inside the execute method,
     * and have a set of FeatureReader's (or results) ready to be streamed
     * here. This approach fits well with the Chirs' idea of configuring
     * geoserver for speed or full conformance, wich ends in just writing
     * directly to the http response output stream or to a
     * ByteArrayOutputStream
     * </p>
     * JG: Consider using a Writer here? GR: I don't think so, because not all
     * responses will be char sequences, such as an image in a WMS GetImage
     * response.
     *
     * @param out
     *
     * @throws ServiceException wrapping of any unchecked exception or other
     * predictable exception except an IO error while writing to <code>out</code>
     * @throws IOException ONLY if an error occurs trying to write content
     * to the passed OutputStream. By this way, we'll can control the very
     * common situation of a java.net.SocketException product of the client
     * closing the connection (like a user pressing it's refresh browser button
     * many times)
     */
    public void writeTo(OutputStream out) throws ServiceException, IOException;
}
