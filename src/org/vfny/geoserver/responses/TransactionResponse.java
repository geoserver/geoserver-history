/* Copyright (c) 2001 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root 
 * application directory.
 */
package org.vfny.geoserver.responses;

//import java.util.List;
//import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.logging.Logger;
import java.sql.Connection;
import java.sql.SQLException;
import org.geotools.data.DataSource;
import org.geotools.data.DataSourceException;
import org.geotools.data.postgis.PostgisConnectionFactory;
import org.geotools.data.postgis.PostgisDataSource;
import org.geotools.feature.AttributeType;
import org.geotools.feature.FeatureType;
import org.geotools.feature.FeatureTypeFlat;
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


/**
 * Handles a Transaction request and creates a TransactionResponse string.
 *
 *@author Chris Holmes, TOPP
 *@version $VERSION$
 */
public class TransactionResponse {

    /** Standard logging instance for class */
    private static final Logger LOGGER = 
        Logger.getLogger("org.vfny.geoserver.responses");
    
    /** The handle of the transaction */
    //HACK: this is not safe, this class should probably not be static.
    private static String transHandle;

    private static TypeRepository repository = TypeRepository.getInstance();

    /** Constructor, which is required to take a request object. */ 
    private TransactionResponse () {}

    

    /**
     * Parses the GetFeature reqeust and returns a contentHandler.
     * @return XML response to send to client
     */ 
    public static String getXmlResponse(TransactionRequest request) 
	throws WfsTransactionException {
	
	LOGGER.finer("doing transaction response");
	//        TypeRepository repository = TypeRepository.getInstance();
	transHandle = request.getHandle();            
        // main handler and return string
        //  generate GML for heander for each table requested
	WfsTransResponse response = new WfsTransResponse
	    (WfsTransResponse.SUCCESS, request.getHandle());
	SubTransactionRequest subRequest = request.getSubRequest(0);
	Connection con = getConnection(subRequest);
	//HACK: fails if types are in different databases
	
	String lockId = request.getLockId();
	LOGGER.finer("got lockId: " + lockId);
	if (!request.getReleaseAll()){
	    String message = "release action SOME not supported yet by " +
		"geoserver, as only whole types can currently be locked"; 
	    throw new WfsTransactionException(message, request.getHandle(), 
					      request.getHandle());
	}
	for(int i = 0, n = request.getSubRequestSize(); i < n; i++) {  
	    subRequest = request.getSubRequest(i);
	    String typeName = subRequest.getTypeName(); 
	    
	    if (repository.isLocked(typeName)) {
		if (!repository.isCorrectLock(typeName, lockId)) { 
		    throw new WfsTransactionException("Feature Type: " + 
						      typeName + " is locked", 
						      subRequest.getHandle(), 
						      transHandle);
		}
	    }
	    Collection addedFids = getSub(subRequest, con);
	      if (addedFids != null) {
		  response.addInsertResult(subRequest.getHandle(), addedFids);
	      }
	}
	
	//HACK: fails to unlock locks with different typeNames.  Right now it
	//only unlocks the last typename.  Need to think this through, as
	//we don't want to unlock right after each transaction either, in case
	//the transaction does not go all the way through.
	//Or is better default action for now to have it release even if 
	//transaction does not complete?
	repository.unlock(subRequest.getTypeName(), lockId);
	try {
	    con.commit();
	    con.close();
	} catch (SQLException e) {
	    String message = "Problem with transactions " 
		+ e.getMessage();
	    LOGGER.warning(message);
	    throw new WfsTransactionException(message, subRequest.getHandle());
	}  //TODO: catch WfsTransactionExceptions and close the connection.
	return response.getXmlResponse();
    }


    private static Connection getConnection(SubTransactionRequest subRequest) 
	throws WfsTransactionException{
	String typeName = subRequest.getTypeName();
	
	TypeInfo meta = repository.getType(typeName);

	//HACK: fails if types are in different databases
        if (meta == null) {
	    String errorMsg = "Could not find Feature Type: -" + typeName +
		"-, feature information is not in the data folder.";
	    LOGGER.warning(errorMsg);
	    throw new WfsTransactionException(errorMsg, subRequest.getHandle(),
					      transHandle);
	}
	PostgisConnectionFactory db = 
	    new PostgisConnectionFactory (meta.getHost(), meta.getPort(),
					  meta.getDatabaseName()); 
        db.setLogin(meta.getUser(), meta.getPassword());
	Connection connection;
	try {
	        connection = db.getConnection();
		connection.setAutoCommit(false);
	} catch (SQLException e) {
	      String message = "Problem with transactions " 
			+ e.getMessage();
	    LOGGER.warning(message);
	    throw new WfsTransactionException(message, subRequest.getHandle());
	}
	return connection;
    }

    /**
     * Parses the GetFeature reqeust and returns a contentHandler.
     * @return XML response to send to client
     */ 
    private static Collection getSub(SubTransactionRequest sub, 
			       java.sql.Connection con)
	throws WfsTransactionException {

        String result = new String();
	LOGGER.finer("sub request is " + sub);
        DataSource data = null;
	try {  
	

	    //this hasn't been tested with more than 15,000 features...not sure
       //if things will mess up with more, and this means 100,000 is the limit 
	    String typeName = sub.getTypeName();
	    if (sub.getOpType() == SubTransactionRequest.INSERT) {
	    //this is for our insert fid hack...we only want the fid from data
		FeatureType schema = 
		    FeatureTypeFactory.create(new AttributeType[0]);
		 int srid = ((PostgisDataSource)data).querySRID(con, typeName);
		 //HACK: this should be fixed in the datasource...
		 ((FeatureTypeFlat)schema).setSRID(srid);
		data = new PostgisDataSource(con, typeName, schema, 100000);
	    } else {
		data = new PostgisDataSource(con, typeName);
	    }
	} catch (DataSourceException e) {
	    String message = "Problem creating datasource: " 
			+ e.getMessage() +", " + e.getCause();
	    LOGGER.warning(message);
	    throw new WfsTransactionException(message, sub.getHandle());
	} catch (org.geotools.feature.SchemaException e) {
	     String message = "Problem creating schema for datasource: " 
			+ e.getMessage();
	    LOGGER.warning(message);
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
	    LOGGER.finer("about to perform delete: " + sub);
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
    private static Collection doInsert(InsertRequest insert, DataSource data) 
	throws WfsTransactionException {
	String handle = insert.getHandle();
	try {
	    //ArrayList committed = new ArrayList();
	    Feature[] oldFeatures = data.getFeatures(null).getFeatures();
	    HashSet oldFids = new HashSet(oldFeatures.length);
	    int i;
	    for (i = 0; i < oldFeatures.length; i++) {
		oldFids.add(oldFeatures[i].getId());
	    }
	    LOGGER.finer("added " + i + "features to oldFid set");
	    FeatureCollection newFeatures = new FeatureCollectionDefault();
	    Feature[] featureArr = insert.getFeatures();
	    newFeatures.addFeatures(featureArr);
	    data.addFeatures(newFeatures);
	    LOGGER.finer("inserted features");
	    //the fids should really be returned from the datasource.
	    //String[] fids addFeatures(Feature features[]);
	    Feature[] allFeatures = data.getFeatures(null).getFeatures();
	    LOGGER.finer("all features # = " + allFeatures.length + ", before: "
		      + oldFeatures.length + ", inserted " + (allFeatures.length 
							      - oldFeatures.length));
	    //REVISIT: maybe just store features in the HashSet, and then
	    //call remove all.  Not sure about hashCodes in Feature.
	    //return allFeatures.removeAll(oldFids);
	    HashSet insertedIds = new HashSet();
	
	    for (int j = 0; j < allFeatures.length; j++) {
	    	String curFid = allFeatures[j].getId();
	    	if (!oldFids.contains(curFid)){
	    	    LOGGER.finer("adding feature " + curFid);
	    	    insertedIds.add(curFid);
	    	}
	    }
	    return insertedIds;
	} catch (DataSourceException e) {
	    LOGGER.warning("Problem with datasource " + e + " cause: " 
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
	    LOGGER.fine("attribut type is " + types[0] + ", values is " + 
		     update.getValues()[0]);
	    data.modifyFeatures(types, update.getValues(), update.getFilter());
	} catch (DataSourceException e) {
	    LOGGER.warning("Problem with datasource " + e + " cause: " 
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
	    LOGGER.warning("Problem with datasource " + e + " cause: " 
			+ e.getCause());
	    throw new WfsTransactionException("Problem removing features: " 
					      + e.getCause(),
					      delete.getHandle());
	}

    }
    
}
