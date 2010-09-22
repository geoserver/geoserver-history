package org.geoserver.wms.map;

import java.io.IOException;
import java.io.OutputStream;

import javax.xml.transform.TransformerException;

import org.geoserver.ows.Response;
import org.geoserver.platform.Operation;
import org.geoserver.platform.ServiceException;
import org.geoserver.wms.WebMap;
import org.geotools.xml.transform.TransformerBase;
import org.springframework.util.Assert;

public class XMLTransformerMapResponse extends Response {

    public XMLTransformerMapResponse() {
        super(XMLTransformerMap.class);

    }

    /**
     * @param value
     *            a {@link XMLTransformerMap}
     * @param operation
     *            the operation descriptor
     * @see org.geoserver.ows.Response#getMimeType(java.lang.Object,
     *      org.geoserver.platform.Operation)
     */
    @Override
    public String getMimeType(Object value, Operation operation) throws ServiceException {
        return ((XMLTransformerMap) value).getMimeType();
    }

    @Override
    public void write(Object value, OutputStream output, Operation operation) throws IOException,
            ServiceException {
        Assert.isInstanceOf(XMLTransformerMap.class, value);

        XMLTransformerMap map = (XMLTransformerMap) value;
        TransformerBase transformer = map.getTransformer();
        Object transformerSubject = map.getTransformerSubject();
        try {
            transformer.transform(transformerSubject, output);
        } catch (TransformerException e) {
            // TransformerException do not respect the Exception.getCause() contract
            Throwable cause = e.getCause() != null ? e.getCause() : e.getException();
            // we need to propagate the RuntimeException
            if (cause instanceof RuntimeException) {
                throw (RuntimeException)cause;
            }
            throw new ServiceException(operation.getId() + " operation failed.",
                    cause != null ? cause : e);
        } finally {
            map.dispose();
        }
    }

    /**
     * Returns a 2xn array of Strings, each of which is an HTTP header pair to be set on the HTTP
     * Response. Can return null if there are no headers to be set on the response.
     * 
     * @param value
     *            must be a {@link WebMap}
     * @param operation
     *            The operation being performed.
     * 
     * @return {@link WebMap#getResponseHeaders()}: 2xn string array containing string-pairs of HTTP
     *         headers/values
     * @see Response#getHeaders(Object, Operation)
     * @see WebMap#getResponseHeaders()
     */
    @Override
    public String[][] getHeaders(Object value, Operation operation) throws ServiceException {
        Assert.isInstanceOf(WebMap.class, value);
        WebMap map = (WebMap) value;
        String[][] responseHeaders = map.getResponseHeaders();
        return responseHeaders;
    }

}
