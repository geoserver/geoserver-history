/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.wfs.response;

import net.opengis.wfs.FeatureCollectionType;
import net.opengis.wfs.GetFeatureType;
import net.opengis.wfs.ResultTypeType;
import net.opengis.wfs.WfsFactory;

import org.geoserver.config.GeoServer;
import org.geoserver.ows.Response;
import org.geoserver.ows.util.OwsUtils;
import org.geoserver.ows.util.ResponseUtils;
import org.geoserver.platform.Operation;
import org.geoserver.platform.ServiceException;
import org.geoserver.wfs.WFSInfo;
import org.geoserver.wfs.xml.v1_1_0.WFSConfiguration;
import org.geotools.xml.Encoder;
import org.xml.sax.SAXException;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;


/**
 * WFS output format for a GetFeature operation in which the resultType is "hits".
 *
 * @author Justin Deoliveira, The Open Planning Project, jdeolive@openplans.org
 *
 */
public class HitsOutputFormat extends Response {
    /**
     * WFS configuration
     */
    WFSInfo wfs;

    /**
     * Xml configuration
     */
    WFSConfiguration configuration;

    public HitsOutputFormat(GeoServer gs, WFSConfiguration configuration) {
        super(FeatureCollectionType.class);

        this.wfs = gs.getService( WFSInfo.class );
        this.configuration = configuration;
    }

    /**
     * @return "text/xml";
     */
    public String getMimeType(Object value, Operation operation)
        throws ServiceException {
        return "text/xml";
    }

    /**
     * Checks that the resultType is of type "hits".
     */
    public boolean canHandle(Operation operation) {
        GetFeatureType request = (GetFeatureType) OwsUtils.parameter(operation.getParameters(),
                GetFeatureType.class);

        return (request != null) && (request.getResultType() == ResultTypeType.HITS_LITERAL);
    }

    public void write(Object value, OutputStream output, Operation operation)
        throws IOException, ServiceException {
        FeatureCollectionType featureCollection = (FeatureCollectionType) value;

        //create a new feautre collcetion type with just the numbers
        FeatureCollectionType hits = WfsFactory.eINSTANCE.createFeatureCollectionType();
        hits.setNumberOfFeatures(featureCollection.getNumberOfFeatures());
        hits.setTimeStamp(featureCollection.getTimeStamp());

        Encoder encoder = new Encoder(configuration, configuration.schema());
        encoder.setEncoding(Charset.forName( wfs.getGeoServer().getGlobal().getCharset()) );
        encoder.setSchemaLocation(org.geoserver.wfs.xml.v1_1_0.WFS.NAMESPACE,
            ResponseUtils.appendPath(wfs.getSchemaBaseURL(), "wfs/1.1.0/wfs.xsd"));

        encoder.encode(hits, org.geoserver.wfs.xml.v1_1_0.WFS.FEATURECOLLECTION, output);
    }
}
