package org.geoserver.wfs.response.v1_1_0;

import java.io.IOException;
import java.io.OutputStream;

import net.opengis.wfs.ActionType;
import net.opengis.wfs.TransactionResponseType;
import net.opengis.wfs.TransactionType;

import org.geoserver.ows.util.RequestUtils;
import org.geoserver.ows.util.ResponseUtils;
import org.geoserver.platform.Operation;
import org.geoserver.platform.ServiceException;
import org.geoserver.wfs.WFS;
import org.geoserver.wfs.WFSException;
import org.geoserver.wfs.xml.v1_1_0.WFSConfiguration;
import org.geotools.xml.Encoder;
import org.vfny.geoserver.global.Data;

/**
 * Response for wfs 1.1 transaction request.
 * 
 * @author Justin Deoliveira, The Open Planning Project
 *
 */
public class TransactionResponse extends WFS11Response {

    WFS wfs;
    Data catalog;
    WFSConfiguration configuration;
    
    public TransactionResponse(WFS wfs, Data catalog, WFSConfiguration configuration) {
        super(TransactionResponseType.class);
        this.wfs = wfs;
        this.catalog = catalog;
        this.configuration = configuration;
    }
    
    @Override
    public void write(Object value, OutputStream output, Operation operation)
            throws IOException, ServiceException {
        
        TransactionResponseType response = (TransactionResponseType) value;
        if (!response.getTransactionResults().getAction().isEmpty()) {
            //since we do atomic transactions, an action failure means all we rolled back
            // spec says to throw exception
            ActionType action = (ActionType) response.getTransactionResults().getAction().iterator()
                                                     .next();
            throw new WFSException(action.getMessage(), action.getCode(), action.getLocator());
        }

        Encoder encoder = new Encoder(configuration, configuration.schema());
        encoder.setEncoding(wfs.getCharSet());

        TransactionType req = (TransactionType)operation.getParameters()[0];
        String proxifiedBaseUrl = RequestUtils.proxifiedBaseURL(req.getBaseUrl(), wfs.getGeoServer().getProxyBaseUrl());
        
        encoder.setSchemaLocation(org.geoserver.wfs.xml.v1_1_0.WFS.NAMESPACE,
            ResponseUtils.appendPath(proxifiedBaseUrl, "schemas/wfs/1.1.0/wfs.xsd"));
        encoder.encode(response, org.geoserver.wfs.xml.v1_1_0.WFS.TRANSACTIONRESPONSE, output);
    }

}
