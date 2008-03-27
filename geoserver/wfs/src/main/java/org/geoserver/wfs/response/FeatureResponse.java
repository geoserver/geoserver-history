package org.geoserver.wfs.response;

import java.io.IOException;
import java.io.OutputStream;

import javax.xml.namespace.QName;

import org.geoserver.ows.Response;
import org.geoserver.platform.Operation;
import org.geoserver.platform.ServiceException;
import org.geoserver.wfs.WFS;
import org.geoserver.wfs.xml.v1_1_0.WFSConfiguration;
import org.geotools.xml.Encoder;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.vfny.geoserver.global.Data;
import org.vfny.geoserver.global.FeatureTypeInfo;

/**
 * Resonse which handles an individual {@link SimpleFeature} and encodes it as 
 * gml.
 *  
 * @author Justin Deoliveira, The Open Planning Project, jdeolive@openplans.org
 *
 */
public class FeatureResponse extends Response {

    Data catalog;
    WFSConfiguration configuration;
    private WFS wfs;
    
    public FeatureResponse(Data catalog, WFSConfiguration configuration, WFS wfs) {
        super( SimpleFeature.class );
        
        this.catalog = catalog;
        this.configuration = configuration;
        this.wfs = wfs;
    }
    
    public String getMimeType(Object value, Operation operation)
            throws ServiceException {
        
        return "text/xml; subtype=gml/3.1.1";
    }

    public void write(Object value, OutputStream output, Operation operation)
            throws IOException, ServiceException {

        //get the feature
        SimpleFeature feature = (SimpleFeature) value;
        SimpleFeatureType featureType = feature.getType();
        
        //grab the metadata
        FeatureTypeInfo meta = catalog.getFeatureTypeInfo(featureType.getName());
        
        //create teh encoder
        Encoder encoder = new Encoder( configuration );
        encoder.setEncoding(wfs.getCharSet());
        encoder.encode( feature, 
            new QName( meta.getNameSpace().getURI(), meta.getTypeName()), output );
    }

}
