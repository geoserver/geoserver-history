package org.geoserver.wfs.response.v1_1_0;

import java.io.IOException;
import java.io.OutputStream;

import net.opengis.wfs.LockFeatureResponseType;
import net.opengis.wfs.LockFeatureType;

import org.geoserver.ows.Response;
import org.geoserver.ows.util.RequestUtils;
import org.geoserver.ows.util.ResponseUtils;
import org.geoserver.platform.Operation;
import org.geoserver.platform.ServiceException;
import org.geoserver.wfs.WFS;
import org.geoserver.wfs.WFSException;
import org.geoserver.wfs.xml.v1_0_0.WfsXmlReader;
import org.geoserver.wfs.xml.v1_1_0.WFSConfiguration;
import org.geotools.xml.Encoder;
import org.vfny.geoserver.global.Data;
import org.xml.sax.SAXException;

public class LockFeatureTypeResponse extends WFS11Response {

    WFS wfs;
    Data catalog;
    WFSConfiguration configuration;
    
    public LockFeatureTypeResponse(WFS wfs, Data catalog, WFSConfiguration configuration) {
        super( LockFeatureResponseType.class ); 
        this.wfs = wfs;
        this.catalog = catalog;
        this.configuration = configuration;
    }
    
    public void write(Object value, OutputStream output, Operation operation)
            throws IOException, ServiceException {
        
        LockFeatureResponseType lockResponse = (LockFeatureResponseType) value;
        
        Encoder encoder = new Encoder(configuration, configuration.schema());
        encoder.setEncoding(wfs.getCharSet());
        
        LockFeatureType req = (LockFeatureType)operation.getParameters()[0];
        String proxifiedBaseUrl = RequestUtils.proxifiedBaseURL(req.getBaseUrl(), wfs.getGeoServer().getProxyBaseUrl());
        
        encoder.setSchemaLocation(org.geoserver.wfs.xml.v1_1_0.WFS.NAMESPACE,
                ResponseUtils.appendPath(proxifiedBaseUrl, "schemas/wfs/1.1.0/wfs.xsd"));

        try {
            encoder.encode(lockResponse, org.geoserver.wfs.xml.v1_1_0.WFS.LOCKFEATURERESPONSE,
                    output);
            output.flush();
        } 
        catch (SAXException e) {
            throw new WFSException( e );
        }
        
    }

}
