/* Copyright (c) 2002 Vision for New York - www.vfny.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root application directory.
 */

package org.vfny.geoserver.responses;

import java.io.*;
import java.util.*;
import org.apache.log4j.Category;
import com.vividsolutions.jts.geom.Geometry;
import org.geotools.feature.*;
import org.geotools.data.postgis.*;
import org.geotools.data.DataSource;
import org.geotools.data.DataSourceException;
import org.vfny.geoserver.db.*;
import org.vfny.geoserver.requests.*;
import org.vfny.geoserver.config.*;


/**
 * Handles a Get Feature request and creates a Get Feature response GML string.
 *
 *@author Rob Hranac, TOPP
 *@version $0.9 beta, 22/03/02$
 */
public class GetFeatureResponse {


    /** Standard logging class */
    private static Category _log = Category.getInstance(GetFeatureResponse.class.getName());
    
    /** Encapsulates all request information */
    private GetFeatureRequest request = new GetFeatureRequest();
    
    
    /**
     * Constructor, which is required to take a request object.
     *
     * @param request The corresponding request object, which may contain multiple queries.
     */ 
    public GetFeatureResponse(GetFeatureRequest request) {
        this.request = request;
    }

    
    /**
     * Parses the GetFeature reqeust and returns a contentHandler.
     *
     * @return XML response to send to client
     */ 
    public String getXmlResponse () 
        throws WfsException {

        StringBuffer result = new StringBuffer();
                    
        // main handler and return string
        //  generate GML for heander for each table requested
        for( int i = 0, n = request.getQueryCount(); i < n; i++ ) {            
            result.append( getQuery( request.getQuery(i)));
            _log.debug("ended feature");
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

        //_log.debug("successfully retrieved collection");
        Feature[] features = collection.getFeatures();
        FeatureType schema = features[0].getSchema();
        AttributeType[] attributeTypes = schema.getAttributeTypes();
        Object[] attributes;

        GMLBuilder gml = new GMLBuilder(true);

        //_log.debug("about to create gml");
        //_log.debug("initializing..." + attributeTypes[schema.attributeTotal() - 1].getClass().toString());
        gml.initializeGeometry(attributeTypes[schema.attributeTotal() - 1].getType(), 
                               info.getName(), 
                               info.getSrs(), 
                               attributeTypes[schema.attributeTotal() - 1].getName());
        gml.startFeatureType( info.getName(), info.getSrs(), new BoundingBox());
        //_log.debug("started feature type");
        for(int i = 0, m = features.length; i < m; i++) {

            //_log.debug("fid: " + features[i].getId());
            gml.startFeature( features[i].getId());
            //_log.debug("got id: " + features[i].getId());
            //_log.debug("about to get attributes");
            attributes = features[i].getAttributes();
            //_log.debug("got attributes");
            //_log.debug("feature: " + features[i].toString());
            //_log.debug("att total: " + schema.attributeTotal());
            for(int j = 0, n = schema.attributeTotal() - 1; j < n; j++) {
                //_log.debug("sent attribute: " + attributes[j].toString());
                gml.addAttribute(attributeTypes[j].getName(), 
                                 attributes[j].toString());
            }
            //_log.debug("geometry att: " + attributes[schema.attributeTotal() - 1].getClass());
            //_log.debug("att name: " + attributeTypes[schema.attributeTotal() - 1].getName());
            gml.addGeometry( (Geometry) attributes[schema.attributeTotal() - 1],
                             attributeTypes[schema.attributeTotal() - 1].getName());
            //_log.debug("added geometry: " + ((Geometry) attributes[schema.attributeTotal() - 1]).toString());
            gml.endFeature();
            //_log.debug("ended feature");
        }
        gml.endFeatureType();        
        //_log.debug("ended feature type");
        //_log.debug("GML is: " + gml.getGML());

        return gml.getGML();
    }
    
}
