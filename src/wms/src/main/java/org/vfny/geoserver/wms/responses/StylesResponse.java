package org.vfny.geoserver.wms.responses;

import java.io.IOException;
import java.io.OutputStream;

import javax.xml.transform.TransformerException;

import org.geoserver.ows.Response;
import org.geoserver.platform.Operation;
import org.geoserver.platform.ServiceException;
import org.geotools.styling.SLDTransformer;
import org.geotools.styling.StyledLayerDescriptor;
import org.vfny.geoserver.wms.WmsException;

public class StylesResponse extends Response {

    public static final String SLD_MIME_TYPE = "application/vnd.ogc.sld+xml";

    public StylesResponse() {
        super(StyledLayerDescriptor.class);
    }

    @Override
    public String getMimeType(Object value, Operation operation) throws ServiceException {
        return SLD_MIME_TYPE;
    }

    @Override
    public void write(Object value, OutputStream output, Operation operation) throws IOException,
            ServiceException {
        StyledLayerDescriptor sld = (StyledLayerDescriptor) value;

        SLDTransformer tx = new SLDTransformer();
        try {
            tx.setIndentation(4);
            tx.transform(sld, output);
        } catch (TransformerException e) {
            throw new WmsException(e);
        }
    }

}
