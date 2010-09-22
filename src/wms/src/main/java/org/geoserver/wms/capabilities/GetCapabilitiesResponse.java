package org.geoserver.wms.capabilities;

import java.io.IOException;
import java.io.OutputStream;

import javax.xml.transform.TransformerException;

import org.geoserver.ows.Response;
import org.geoserver.platform.Operation;
import org.geoserver.platform.ServiceException;
import org.geoserver.wms.GetCapabilitiesRequest;

/**
 * OWS {@link Response} bean to handle WMS {@link GetCapabilities} results
 * 
 * @author groldan
 * 
 */
public class GetCapabilitiesResponse extends Response {

    public GetCapabilitiesResponse() {
        super(GetCapabilitiesTransformer.class);
    }

    /**
     * @return {@code "application/vnd.ogc.wms_xml"}
     * @see org.geoserver.ows.Response#getMimeType(java.lang.Object,
     *      org.geoserver.platform.Operation)
     */
    @Override
    public String getMimeType(final Object value, final Operation operation)
            throws ServiceException {

        if (value instanceof GetCapabilitiesTransformer
                && "GetCapabilities".equalsIgnoreCase(operation.getId())
                && operation.getParameters().length > 0
                && operation.getParameters()[0] instanceof GetCapabilitiesRequest) {

            return GetCapabilitiesTransformer.WMS_CAPS_MIME;
        }

        throw new IllegalArgumentException(value == null ? "null" : value.getClass().getName()
                + "/" + operation.getId());
    }

    /**
     * @param value
     *            {@link WMSCapsTransformer}
     * @param output
     *            destination
     * @param operation
     *            The operation identifier which resulted in <code>value</code>
     * @see org.geoserver.ows.Response#write(java.lang.Object, java.io.OutputStream,
     *      org.geoserver.platform.Operation)
     */
    @Override
    public void write(final Object value, final OutputStream output, final Operation operation)
            throws IOException, ServiceException {

        GetCapabilitiesTransformer transformer = (GetCapabilitiesTransformer) value;

        try {
            GetCapabilitiesRequest request = (GetCapabilitiesRequest) operation.getParameters()[0];
            transformer.transform(request, output);
        } catch (TransformerException e) {
            throw new ServiceException(e);
        }
    }

}
