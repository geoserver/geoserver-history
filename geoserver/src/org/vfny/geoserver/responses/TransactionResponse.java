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
import org.geotools.data.postgis.PostgisConnectionFactory;
import org.geotools.data.postgis.PostgisDataSource;
import org.geotools.feature.AttributeType;
import org.geotools.feature.FeatureType;
import org.geotools.feature.FeatureTypeFactory;
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
import java.sql.SQLException;

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
	SubTransactionRequest subRequest = request.getSubRequest(0);
	TypeInfo meta = repository.getType(subRequest.getTypeName());
	//HACK: fails if types are in different databases

        if (meta == null) {
	    throw new WfsTransactionException("Couldnt find Feature Type: -" +
				      subRequest.getTypeName() + "-, feature information"
				      + " is not in the data folder.", 
				      subRequest.getHandle(), transHandle);
	}
	PostgisConnectionFactory db = 
	    new PostgisConnectionFactory (meta.getHost(), meta.getPort(),
					  meta.getDatabaseName()); 
        db.setLogin(meta.getUser(), meta.getPassword());

	try {
	    	java.sql.Connection con = db.getConnection();
		con.setAutoCommit(false);
	    for(int i = 0, n = request.getSubRequestSize(); i < n; i++) {  
		subRequest = request.getSubRequest(i);
		List addedFids = getSub(request.getSubRequest(i), con);
		if (addedFids != null){ LOG.finest("first fid is " + 
						   addedFids.get(0));
		response.addInsertResult(subRequest.getHandle(), addedFids);
		}
		
	    }   
	    con.commit();
	    con.close();
	} catch (SQLException e) {
	      String message = "Problem with transactions " 
			+ e.getMessage();
	    LOG.warning(message);
	    throw new WfsTransactionException(message, subRequest.getHandle());
	}
	return response.getXmlResponse();
    }


    /**
     * Parses the GetFeature reqeust and returns a contentHandler.
     * @return XML response to send to client
     */ 
    private static List getSub(SubTransactionRequest sub, 
			       java.sql.Connection con)
	throws WfsTransactionException {

        String result = new String();
	LOG.finer("sub request is " + sub);
        DataSource data = null;
	try {  //TODO: add when geotools gets updated.
	    FeatureType schema = FeatureTypeFactory.create(new AttributeType[0]);
	data = new PostgisDataSource(con, sub.getTypeName());
	} catch (DataSourceException e) {
	    String message = "Problem creating datasource: " 
			+ e.getMessage() +", " + e.getCause();
	    LOG.warning(message);
	    throw new WfsTransactionException(message, sub.getHandle());
	} catch (org.geotools.feature.SchemaException e) {
	     String message = "Problem creating schema for datasource: " 
			+ e.getMessage();
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
	    //ArrayList committed = new ArrayList();
	    Feature[] oldFeatures = data.getFeatures(null).getFeatures();
	    ArrayList oldFids = new ArrayList();
	    for (int i = 0; i < oldFeatures.length; i++) {
		oldFids.add(oldFeatures[i].getId());
	    }
	    FeatureCollection newFeatures = new FeatureCollectionDefault();
	    Feature[] featureArr = insert.getFeatures();
	    newFeatures.addFeatures(featureArr);
	    data.addFeatures(newFeatures);
	    
	    //the fids should really be returned from the datasource.
	    Feature[] allFeatures = data.getFeatures(null).getFeatures();
	    //This could also be cleaner if a equals method actually existed
	    //for Feature.  Then we could make Lists and do a removeAll.
	    ArrayList insertedIds = new ArrayList();
	    for (int j = 0; j < allFeatures.length; j++) {
		String curFid = allFeatures[j].getId();
		if (!oldFids.contains(curFid)){
		    LOG.finer("adding feature " + curFid);
		    insertedIds.add(curFid);
		}
	    }
	    return insertedIds;
 //List allFeatures = Arrays.asList(data.getFeatures(null).getFeatures());
	    
	    /*	    return allFeatures.removeAll(oldFeatures);
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
	    return committed;*/
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
