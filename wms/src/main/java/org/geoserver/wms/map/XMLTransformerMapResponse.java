package org.geoserver.wms.map;

import java.io.IOException;
import java.io.OutputStream;

import javax.xml.transform.TransformerException;

import org.geoserver.ows.Response;
import org.geoserver.platform.Operation;
import org.geoserver.platform.ServiceException;
import org.geoserver.wms.response.Map;
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
            throw new ServiceException(operation.getId() + " operation failed.", e);
        }
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
     * @return {@link Map#getResponseHeaders()}: 2xn string array containing string-pairs of HTTP
     *         headers/values
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

}
