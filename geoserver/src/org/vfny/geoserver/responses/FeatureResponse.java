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
import org.vfny.geoserver.config.TypeInfo;
import org.vfny.geoserver.config.TypeRepository;

/**
 * Handles a Get Feature request and creates a Get Feature response GML string.
 *
 *@author Rob Hranac, TOPP
 *@version $VERSION$
 */
public class FeatureResponse {

    /** Standard logging instance for class */
    private static final Logger LOG = 
        Logger.getLogger("org.vfny.geoserver.responses");
    
    /** Constructor, which is required to take a request object. */ 
    private FeatureResponse () {}

    /**
     * Parses the GetFeature reqeust and returns a contentHandler.
     * @return XML response to send to client
     */ 
    public static String getXmlResponse(FeatureRequest request) 
        throws WfsException {

        TypeRepository repository = TypeRepository.getInstance();
        StringBuffer result = new StringBuffer();
                    
        // main handler and return string
        //  generate GML for heander for each table requested
        for(int i = 0, n = request.getQueryCount(); i < n; i++) {            
            result.append(getQuery(request.getQuery(i), repository));
            LOG.finest("ended feature");
        }        

        // return final string
        return result.toString();
    }


    /**
     * Parses the GetFeature reqeust and returns a contentHandler.
     * @return XML response to send to client
     */ 
    private static String getQuery(Query query, TypeRepository repository)
        throws WfsException {
        
        TypeInfo meta = repository.getType(query.getTypeName());
	
        PostgisConnection db = new PostgisConnection (meta.getHost(),
                                                      meta.getPort(),
                                                      meta.getDatabaseName()); 
        db.setLogin(meta.getUser(), meta.getPassword());
	FeatureCollection collection = null;
	try {
	    DataSource data = new PostgisDataSource(db, meta.getName());
            collection = data.getFeatures(query.getFilter());
        } catch(DataSourceException e) {
            throw new WfsException(e.getMessage());
        }

        LOG.finest("successfully retrieved collection");
        Feature[] features = collection.getFeatures();
        FeatureType schema = features[0].getSchema();
        AttributeType[] attributeTypes = schema.getAttributeTypes();
        Object[] attributes;
	int geometryPosition = schema.getDefaultGeometry().getPosition();
        GMLBuilder gml = new GMLBuilder(true);

        LOG.finest("about to create gml");
        LOG.finest("initializing..." + attributeTypes[schema.attributeTotal() - 1].getClass().toString());
        gml.initializeGeometry(attributeTypes[geometryPosition].
                               getType(), 
                               meta.getName(), 
                               meta.getSrs(), 
                               attributeTypes[geometryPosition].
                               getName());
        gml.startFeatureType(meta.getName(), meta.getSrs());

        LOG.finest("started feature type");
        for(int i = 0, m = features.length; i < m; i++) {

            LOG.finest("fid: " + features[i].getId());
            gml.startFeature(features[i].getId());
            attributes = features[i].getAttributes();
            LOG.finest("feature: " + features[i].toString());
            LOG.finest("att total: " + schema.attributeTotal());
            for(int j = 0, n = schema.attributeTotal(); j < n; j++) {
                LOG.finest("sent attribute: " + attributes[j].toString());
		if (j == geometryPosition) {
		    LOG.finest("geometry att: " + attributes[j].getClass());
		    LOG.finest("att name: " + attributeTypes[j].getName());
            gml.addGeometry((Geometry) attributes[j], 
			    attributeTypes[j].getName());
		LOG.finest("added geometry: " + ((Geometry) attributes[j]).toString());
		} else {
                gml.addAttribute(attributeTypes[j].getName(), 
                                 attributes[j].toString());
		}
	    }
            
            
            gml.endFeature();
            LOG.finest("ended feature");
        }
        gml.endFeatureType();        
        LOG.finest("ended feature type");
        LOG.finest("GML is: " + gml.getGML());

        return gml.getGML();
    }
    
}
