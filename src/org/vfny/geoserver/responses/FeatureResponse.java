/* Copyright (c) 2001 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root 
 * application directory.
 */
package org.vfny.geoserver.responses;

import java.io.*;
import java.util.*;
import java.util.logging.Logger;
import com.vividsolutions.jts.geom.Geometry;
import org.geotools.feature.Feature;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureType;
import org.geotools.feature.AttributeType;
import org.geotools.data.DataSource;
import org.geotools.data.DataSourceException;
import org.geotools.data.postgis.PostgisConnection;
import org.geotools.data.postgis.PostgisDataSource;
import org.vfny.geoserver.requests.FeatureRequest;
import org.vfny.geoserver.requests.Query;
import org.vfny.geoserver.config.FeatureTypeBean;

/**
 * Handles a Get Feature request and creates a Get Feature response GML string.
 *
 *@author Rob Hranac, TOPP
 *@version $VERSION$
 */
public class FeatureResponse {

    /** Standard logging instance for class */
    private static final Logger LOGGER = 
        Logger.getLogger("org.vfny.geoserver.responses");
    
    /** Encapsulates all request information */
    private FeatureRequest request = new FeatureRequest();
    
    
    /** Constructor, which is required to take a request object. */ 
    public FeatureResponse(FeatureRequest request) { this.request = request; }

    /**
     * Parses the GetFeature reqeust and returns a contentHandler.
     * @return XML response to send to client
     */ 
    public String getXmlResponse () 
        throws WfsException {

        StringBuffer result = new StringBuffer();
                    
        // main handler and return string
        //  generate GML for heander for each table requested
        for( int i = 0, n = request.getQueryCount(); i < n; i++ ) {            
            result.append( getQuery( request.getQuery(i)));
            LOGGER.finest("ended feature");
        }        

        // return final string
        return result.toString();
    }


    /**
     * Parses the GetFeature reqeust and returns a contentHandler.
     *
     * @return XML response to send to client
     */ 
    static public String getQuery(Query query)
        throws WfsException {
        
        FeatureTypeBean info = new FeatureTypeBean( query.getFeatureTypeName());
        PostgisConnection db = new PostgisConnection ( info.getHost(),
                                                       info.getPort(),
                                                       info.getDatabaseName()); 
        db.setLogin( info.getUser(), info.getPassword());
        DataSource data = new PostgisDataSource( db, info.getName());

        FeatureCollection collection = null;
        try {
            collection = data.getFeatures( query.getFilter());
        } catch(DataSourceException e) {
            throw new WfsException(e.getMessage());
        }

        //LOGGER.finest("successfully retrieved collection");
        Feature[] features = collection.getFeatures();
        FeatureType schema = features[0].getSchema();
        AttributeType[] attributeTypes = schema.getAttributeTypes();
        Object[] attributes;

        GMLBuilder gml = new GMLBuilder(true);

        //LOGGER.finest("about to create gml");
        //LOGGER.finest("initializing..." + attributeTypes[schema.attributeTotal() - 1].getClass().toString());
        gml.initializeGeometry(attributeTypes[schema.attributeTotal() - 1].getType(), 
                               info.getName(), 
                               info.getSrs(), 
                               attributeTypes[schema.attributeTotal() - 1].getName());
        gml.startFeatureType( info.getName(), info.getSrs());
        //LOGGER.finest("started feature type");
        for(int i = 0, m = features.length; i < m; i++) {

            //LOGGER.finest("fid: " + features[i].getId());
            gml.startFeature( features[i].getId());
            //LOGGER.finest("got id: " + features[i].getId());
            //LOGGER.finest("about to get attributes");
            attributes = features[i].getAttributes();
            //LOGGER.finest("got attributes");
            //LOGGER.finest("feature: " + features[i].toString());
            //LOGGER.finest("att total: " + schema.attributeTotal());
            for(int j = 0, n = schema.attributeTotal() - 1; j < n; j++) {
                //LOGGER.finest("sent attribute: " + attributes[j].toString());
                gml.addAttribute(attributeTypes[j].getName(), 
                                 attributes[j].toString());
            }
            //LOGGER.finest("geometry att: " + attributes[schema.attributeTotal() - 1].getClass());
            //LOGGER.finest("att name: " + attributeTypes[schema.attributeTotal() - 1].getName());
            gml.addGeometry( (Geometry) attributes[schema.attributeTotal() - 1],
                             attributeTypes[schema.attributeTotal() - 1].getName());
            //LOGGER.finest("added geometry: " + ((Geometry) attributes[schema.attributeTotal() - 1]).toString());
            gml.endFeature();
            //LOGGER.finest("ended feature");
        }
        gml.endFeatureType();        
        //LOGGER.finest("ended feature type");
        //LOGGER.finest("GML is: " + gml.getGML());

        return gml.getGML();
    }
    
}
