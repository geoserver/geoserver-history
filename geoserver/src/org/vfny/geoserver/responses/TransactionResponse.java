/* Copyright (c) 2001 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root 
 * application directory.
 */
package org.vfny.geoserver.responses;

import java.util.List;
import java.util.ArrayList;
import java.util.logging.Logger;
import org.geotools.data.DataSource;
import org.geotools.data.DataSourceException;
import org.geotools.data.postgis.PostgisConnection;
import org.geotools.data.postgis.PostgisDataSource;
import org.geotools.feature.AttributeType;
import org.geotools.feature.FeatureType;
import org.geotools.feature.Feature;
import org.geotools.feature.SchemaException;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureCollectionDefault;
import org.vfny.geoserver.requests.TransactionRequest;
import org.vfny.geoserver.requests.SubTransactionRequest;
import org.vfny.geoserver.requests.DeleteRequest;
import org.vfny.geoserver.requests.UpdateRequest;
import org.vfny.geoserver.requests.InsertRequest;
import org.vfny.geoserver.config.TypeInfo;
import org.vfny.geoserver.config.TypeRepository;


/**
 * Handles a Transaction request and creates a TransactionResponse string.
 *
 *@author Chris Holmes, TOPP
 *@version $VERSION$
 */
public class TransactionResponse {

    /** Standard logging instance for class */
    private static final Logger LOG = 
        Logger.getLogger("org.vfny.geoserver.responses");
    
    /** The handle of the transaction */
    private static String transHandle;

    /** Constructor, which is required to take a request object. */ 
    private TransactionResponse () {}

    

    /**
     * Parses the GetFeature reqeust and returns a contentHandler.
     * @return XML response to send to client
     */ 
    public static String getXmlResponse(TransactionRequest request) 
	throws WfsTransactionException {
	
	LOG.info("getting xml for transaction");
        TypeRepository repository = TypeRepository.getInstance();
	transHandle = request.getHandle();            
        // main handler and return string
        //  generate GML for heander for each table requested
	WfsTransResponse response = new WfsTransResponse
	    (WfsTransResponse.SUCCESS, request.getHandle());
	    for(int i = 0, n = request.getSubRequestSize(); i < n; i++) {  
		SubTransactionRequest subRequest = request.getSubRequest(i);
		List addedFids = getSub(request.getSubRequest(i), repository);
		if (addedFids != null){ LOG.finest("first fid is " + 
						     addedFids.get(0));
		response.addInsertResult(subRequest.getHandle(), addedFids);
		}
		LOG.finest("ended feature");
	    }        
	    return response.getXmlResponse();
    }


    /**
     * Parses the GetFeature reqeust and returns a contentHandler.
     * @return XML response to send to client
     */ 
    private static List getSub(SubTransactionRequest sub, 
			       TypeRepository repository)
	throws WfsTransactionException {

        String result = new String();
	LOG.finer("sub request is " + sub);
	TypeInfo meta = repository.getType(sub.getTypeName());
        if (meta == null) {
	    throw new WfsTransactionException("Couldnt find Feature Type: -" +
				   sub.getTypeName() + "-, feature information"
				   + " is not in the data folder.", 
				   sub.getHandle(), transHandle);
	}
	PostgisConnection db = new PostgisConnection (meta.getHost(),
                                                      meta.getPort(),
                                                      meta.getDatabaseName()); 
        db.setLogin(meta.getUser(), meta.getPassword());
        DataSource data = null;
	try {  //TODO: add when geotools gets updated.
	data = new PostgisDataSource(db, meta.getName());
	} catch (DataSourceException e) {
	    String message = "Problem creating datasource " 
			+ e.getCause();
	    LOG.warning(message);
	    throw new WfsTransactionException(message, sub.getHandle());
	}
	
	switch (sub.getOpType()) {
	case SubTransactionRequest.UPDATE: 
	    UpdateRequest update = (UpdateRequest)sub;
	    doUpdate(update, data);
	    break;
	case SubTransactionRequest.INSERT:
	    InsertRequest insert = (InsertRequest)sub;
	    return doInsert(insert, data);
	case SubTransactionRequest.DELETE:
	    LOG.finer("about to perform delete: " + sub);
	    doDelete((DeleteRequest)sub, data);
	    break;
	default: break;
	}
	return null;
    }

    /**
     * Performs the insert operation.
     * @param insert the request to perform.
     * @param data the datasource to remove features from.
     */
    private static List doInsert(InsertRequest insert, DataSource data) 
	throws WfsTransactionException {
	String handle = insert.getHandle();
	try {
	    ArrayList committed = new ArrayList();
	    FeatureCollection features = new FeatureCollectionDefault();
	    Feature[] featureArr = insert.getFeatures();
	    features.addFeatures(featureArr);
	    data.addFeatures(features);
	    if (featureArr != null && featureArr.length > 0) {
		String featureName = featureArr[0].getSchema().getTypeName();
		LOG.finer("featureArr length is " + featureArr.length);
		for(int i = 0; i < featureArr.length; i++) {
 	    //TODO: error checking to make sure they got in successfully.
		    String featureId = //HACK! need to fix fid all the way
			//through, then we can just call .getId()
			featureArr[i].getAttributes()[0].toString();
		    LOG.finer("adding feature " + featureId);
		    committed.add(featureName + "." + featureId);
		}
	    }
	    return committed;
	} catch (DataSourceException e) {
	    LOG.warning("Problem with datasource " + e + " cause: " 
			+ e.getCause());
	    throw new WfsTransactionException("Problem updating features: " 
					      +e.toString() + " cause: " + 
					      e.getCause(), handle);
	} 
    }

     /**
     * Performs the update operation.
     * @param update the request to perform.
     * @param data the datasource to remove features from.
     */
    private static void doUpdate(UpdateRequest update, DataSource data) 
	throws WfsTransactionException {
	String handle = update.getHandle();
	try {
	    FeatureType schema = ((PostgisDataSource)data).getSchema();
	    //schema = schema.setTypeName("dude");
	    AttributeType[] types = update.getTypes(schema);
	    LOG.fine("attribut type is " + types[0] + ", values is " + 
		     update.getValues()[0]);
	    data.modifyFeatures(types, update.getValues(), update.getFilter());
	} catch (DataSourceException e) {
	    LOG.warning("Problem with datasource " + e + " cause: " 
			+ e.getCause());
	    throw new WfsTransactionException("Problem updating features: " 
					      +e.toString() + " cause: " + 
					      e.getCause(), handle);
	} catch (SchemaException e) {
	     throw new WfsTransactionException(e.toString(), handle);
	}
    }

    /**
     * Performs the delete operation.
     * @param delete the request to perform.
     * @param data the datasource to remove features from.
     */
    private static void doDelete(DeleteRequest delete, DataSource data) 
	throws WfsTransactionException {
	String retString = new String();
	String handle = delete.getHandle();
	try {
	    data.removeFeatures(delete.getFilter());
	} catch (DataSourceException e) {
	    LOG.warning("Problem with datasource " + e + " cause: " 
			+ e.getCause());
	    throw new WfsTransactionException("Problem removing features: " 
					      + e.getCause(),
					      delete.getHandle());
	}

    }
    
}
