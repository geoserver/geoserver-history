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
import org.geotools.feature.FeatureTypeFactory;
import org.geotools.feature.FeatureTypeFlat;
import org.geotools.feature.AttributeType;
import org.geotools.feature.SchemaException;
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
	LOG.finest("get xml response called request is: " + request);

        TypeRepository repository = TypeRepository.getInstance();
        StringBuffer result = new StringBuffer();
	int maxFeatures = request.getMaxFeatures();
                    
        // main handler and return string
        //  generate GML for heander for each table requested
        for(int i = 0, n = request.getQueryCount(); i < n; i++) {            
            result.append(getQuery(request.getQuery(i), repository, maxFeatures));
            LOG.finest("ended feature");
        }        

        // return final string
        return result.toString();
    }


    private static String getLocator(Query query){
	String locator = query.getHandle();
	if (locator == null  || locator.equals("")) {
	    locator = "Class FeatureResponse, in method getQuery";
	}
	return locator;
    }

    /**
     * Parses the GetFeature reqeust and returns a contentHandler.
     * @return XML response to send to client
     */ 
    private static String getQuery(Query query, TypeRepository repository, 
				   int maxFeatures) throws WfsException {
        
	LOG.finest("about to get query: " + query);
        TypeInfo meta = repository.getType(query.getTypeName());
	if (meta == null) {
	    throw new WfsException("Could not find Feature Type named: " +
				   query.getTypeName() + ", feature information"
				   + " is not in the data folder.", 
				   getLocator(query));
	}
        PostgisConnection db = new PostgisConnection (meta.getHost(),
                                                      meta.getPort(),
                                                      meta.getDatabaseName()); 
	LOG.finest("connecting to db " + meta.getHost() + " is host, port "
		      + meta.getPort() + " name: " + meta.getDatabaseName());
        db.setLogin(meta.getUser(), meta.getPassword());
	LOG.finest("setting user and pass " + meta.getUser() + meta.getPassword());
	FeatureCollection collection = null;
	FeatureType schema = null;
	String tableName = meta.getName();
	try {
	schema = PostgisDataSource.makeSchema(tableName, db);
	if (!query.allRequested()) {
	    List propertyNames = query.getPropertyNames();
	    AttributeType[] properties = new AttributeType[propertyNames.size()];
	    try {
	    for(int i = 0; i < propertyNames.size(); i++) {
		properties[i] = 
		    schema.getAttributeType(propertyNames.get(i).toString());
		//TODO: make sure geometry is included, or change featuretypeflat
		//to not have required geometry.
            }
	    //HACK: we should not rely on schema being a featureTypeFlat.  And
	    //get and set srid methods will go away soon (which should make it
	    //so the datasource works without these hacks)
	    int srid = ((FeatureTypeFlat)schema).getSRID();
	    schema = FeatureTypeFactory.create(properties);	    
	    ((FeatureTypeFlat)schema).setSRID(srid);
	    } catch (SchemaException e) {
		throw new WfsException(e, "While trying to create schema for" +
				       "return feature", getLocator(query));
	    }
	}

	    DataSource data = 
		new PostgisDataSource(db, tableName, schema, maxFeatures);
	    LOG.finest("++++++++++++++++++filter is " + query.getFilter());
            collection = data.getFeatures(query.getFilter());
        } catch(DataSourceException e) {
            throw new WfsException(e, "While getting features from datasource",
				   getLocator(query));
        }

        LOG.finest("successfully retrieved collection");
	GMLBuilder gml = new GMLBuilder(true);
        Feature[] features = collection.getFeatures();
	LOG.finest("features lenght is: " + features.length);
	gml.startFeatureType(meta.getName(), meta.getSrs());
        LOG.finest("started feature type");
	if (features.length > 0) {
	    //FeatureType schema = features[0].getSchema();
        AttributeType[] attributeTypes = schema.getAttributeTypes();
        Object[] attributes;
	AttributeType geometryAttr = schema.getDefaultGeometry();
	int geometryPosition = -1;
	if (geometryAttr != null) {
	    geometryPosition = geometryAttr.getPosition();
	}
        LOG.finest("about to create gml");
        LOG.finest("initializing..." + attributeTypes[schema.attributeTotal() - 1].getClass().toString());
	if (geometryAttr != null) {
	    gml.initializeGeometry(attributeTypes[geometryPosition].
                               getType(), 
                               meta.getName(), 
                               meta.getSrs(), 
                               attributeTypes[geometryPosition].
                               getName());
	}
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
        }
	gml.endFeatureType();        
        LOG.finest("ended feature type");
	
        LOG.finest("GML is: " + gml.getGML());

        return gml.getGML();
    }
    
}
