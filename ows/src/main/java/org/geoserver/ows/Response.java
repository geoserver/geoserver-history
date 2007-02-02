/* Copyright (c) 2001, 2003 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.ows;

import org.geoserver.platform.Operation;
import org.geoserver.platform.ServiceException;
import java.io.IOException;
import java.io.OutputStream;


/**
 * Response to an operation, which serializes the result of the operation to an
 * output stream.
 * <p>
 * A response must specify the following information:
 * <ul>
 *  <li>The type of object it is capable of serializing, the class is bound to.
 *  See {@link #getBinding()}.
 *  <li>The mime-type of the resulting response. See {@link #getMimeType()}.
 * </ul>
 * </p>
 * <p>
 * Optionally, a response may declare a well-known name for it. This well
 * known name corresponds to the "outputFormat" parameter which is supported
 * on many types of OWS request.
 * </p>
 *
 * @author Justin Deoliveira, The Open Planning Project, jdeolive@openplans.org
 *
 */
public abstract class Response {
    /**
     * Class of object to serialize
     */
    final Class binding;

    /**
     * The well known "outputFormat" of the response
     */
    final String outputFormat;

    /**
     * Constructor which specified the class this response is bound to.
     *
     * @param binding The class of object the response serializes.
     */
    public Response(Class binding) {
        this(binding, null);
    }

    /**
     * Constructor which specified the class this response is bound to, and a
     * common name for the type of response.
     *
     * @param binding The class of object the response serializes
     * @param outputFormat A common name for the response.
     */
    public Response(Class binding, String outputFormat) {
        this.binding = binding;
        this.outputFormat = outputFormat;

        if (binding == null) {
            throw new NullPointerException("binding may not be null");
        }
    }

    /**
     * @return The type of object the response can handle.
     */
    public final Class getBinding() {
        return binding;
    }

    /**
     * @return A common or well-known name for the response, may be <code>null</code>.
     */
    public final String getOutputFormat() {
        return outputFormat;
    }

    /**
     * Determines if the response can handle the operation being performed.
     * <p>
     * This method is called before {@link #write(Object, OutputStream, Operation)}.
     * </p>
     * <p>
     * Subclasses should override this method to perform additional checks
     * against the operation being performed. Example might be checking the
     * version of the service.
     * </p>
     * @param operation The operation being performed.
     *
     * @return <code>true</code> if the response can handle the operation,
     * otherwise <code>false</code>
     */
    public boolean canHandle(Operation operation) {
        return true;
    }

    /**
     * Returns the mime type to be uses when writing the response.
     * 
     * @param value The value to serialize
     * @param operation The operation being performed.
     *
     * 
     * @return The mime type of the response, must not be <code>null</code>
     */
    public abstract String getMimeType(Object value, Operation operation)
        throws ServiceException;


    /**
     * Serializes <code>value</code> to <code>output</code>.
     * <p>
     * The <code>operation</code> bean is provided for context.
     * </p>
     * @param value The value to serialize.
     * @param output The output stream.
     * @param operation The operation which resulted in <code>value</code>
     *
     * @throws IOException Any I/O errors that occur
     * @throws ServiceException Any service errors that occur
     */
    public abstract void write(Object value, OutputStream output, Operation operation)
        throws IOException, ServiceException;
}
