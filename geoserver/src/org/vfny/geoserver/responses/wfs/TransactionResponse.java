/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.responses.wfs;

import org.geotools.data.*;
import org.geotools.feature.*;
import org.geotools.feature.FeatureType;
import org.geotools.filter.Filter;
import org.vfny.geoserver.*;
import org.vfny.geoserver.config.*;
import org.vfny.geoserver.oldconfig.*;
import org.vfny.geoserver.requests.*;
import org.vfny.geoserver.requests.readers.*;
import org.vfny.geoserver.requests.wfs.*;
import org.vfny.geoserver.responses.Response;
import java.io.*;
import java.util.*;
import java.util.logging.*;


/**
 * Handles a Transaction request and creates a TransactionResponse string.
 *
 * @author Chris Holmes, TOPP
 * @version $Id: TransactionResponse.java,v 1.1.2.2 2003/11/12 03:42:30 jive Exp $
 */
public class TransactionResponse implements Response {
    
    /** Standard logging instance for class */
    private static final Logger LOGGER = Logger.getLogger(
            "org.vfny.geoserver.responses");

    /** The handle of the transaction */

    //HACK: this is not safe, this class should probably not be static.
    //TODO: just get rid of this, it should always be set in Transaction
    //anyways.
    //private static String transHandle;
    
    private static ServerConfig config = ServerConfig.getInstance();

    /** temporal, remove it when response streaming be implemented */
    private String xmlResponse;

    /**
     * Constructor
     */
    public TransactionResponse() {
    }

    public void execute(Request request) throws WfsException {
        if (!(request instanceof TransactionRequest)) {
            throw new WfsException(
                "bad request, expected TransactionRequest, but got " + request);
        }

        TransactionRequest transReques = (TransactionRequest) request;
        xmlResponse = getXmlResponse(transReques);
    }

    public String getContentType() {
        return ServerConfig.getInstance().getGlobalConfig().getMimeType();
    }

    public void writeTo(OutputStream out) throws ServiceException {
        try {
            byte[] content = xmlResponse.getBytes();
            out.write(content);
        } catch (IOException ex) {
            throw new WfsException(ex, "", getClass().getName());
        }
    }

    /**
     * Parses the GetFeature reqeust and returns a contentHandler.
     *
     * @param request The request to transact.
     *
     * @return XML response to send to client
     *
     * @throws WfsTransactionException For any problems with transaction.
     * @throws WfsException If the featureType is locked and the lockId is
     *         wrong.
     */
    private static String getXmlResponse(TransactionRequest request)
        throws WfsTransactionException, WfsException {
        LOGGER.finer("doing transaction response");

        //        TypeRepository repository = TypeRepository.getInstance();
        //transHandle = request.getHandle();

        // main handler and return string
        //  generate GML for heander for each table requested
        WfsTransResponse response = new WfsTransResponse(WfsTransResponse.SUCCESS,
                request.getHandle());
                
        SubTransactionRequest subRequest = request.getSubRequest(0);
        String lockId = request.getLockId();
        LOGGER.finer("got lockId: " + lockId);

        //try {
        for (int i = 0, n = request.getSubRequestSize(); i < n; i++) {
            subRequest = request.getSubRequest(i);

            String typeName = subRequest.getTypeName();
            Filter filter = null;

            if (subRequest.getOpType() == SubTransactionRequest.DELETE) {
                filter = ((DeleteRequest) subRequest).getFilter();
            } else if (subRequest.getOpType() == SubTransactionRequest.UPDATE) {
                filter = ((UpdateRequest) subRequest).getFilter();
            }

            //REVISIT: should inserts detect if the whole featureType is
            //locked?  Hard to check right now.
            if (config.getCatalog().isLocked(typeName, filter, lockId)) {
                if (lockId == null) {
                    throw new WfsTransactionException("attempting to modify "
                        + "locked features");
                }

                LOGGER.finer("it's locked, should throw exception");

                String message = ("Feature Type: " + typeName + " is locked,"
                    + " and the lockId of the request - " + lockId
                    + " - is not the proper lock");
                throw new WfsException(message, subRequest.getHandle());
            }

            Collection addedFids = getSub(subRequest);

            if (addedFids != null) {
                response.addInsertResult(subRequest.getHandle(), addedFids);
            }
        }

        commitAll(request);
        config.getCatalog().unlock(request);

        return response.getXmlResponse();
    }

    /**
     * Commits all the sub transactions.
     *
     * @param request holds the subTransactions that were just completed.
     *
     * @throws WfsTransactionException if there was problems with the
     *         datasource.
     * @throws UnsupportedOperationException DOCUMENT ME!
     *
     * @task REVISIT: This leaves a bunch of connections open with postgis.
     *       Should have a close() method in the datasource interface.
     * @task TODO: REDO
     */
    private static void commitAll(TransactionRequest request)
        throws WfsTransactionException {
        SubTransactionRequest subRequest = request.getSubRequest(0);
        throw new UnsupportedOperationException("this is being rethinked :)");

        /*
           try {
             for (int i = 0, n = request.getSubRequestSize(); i < n; i++) {
               subRequest = request.getSubRequest(i);
        
                   String typeName = subRequest.getTypeName();
                   TypeInfo typeInfo = repository.getType(typeName);
                   LOGGER.fine("committing " + request);
                   typeInfo.getTransactionDataSource().commit();
                   typeInfo.getTransactionDataSource().setAutoCommit(true);
                 }
               }
               catch (DataSourceException e) {
                 String message = "Problem accessing datasource: " + e.getMessage()
                     + ", " + e.getCause();
                 LOGGER.info(message);
                 throw new WfsTransactionException(message, subRequest.getHandle());
               }
               catch (WfsException e) {
                 throw new WfsTransactionException(e, subRequest.getHandle(),
                                                   transHandle);
               }
         */
    }

    /**
     * Handles one sub transaction request.
     *
     * @param sub The sub request to perform.
     *
     * @return XML response to send to client for this operation.
     *
     * @throws WfsTransactionException For any datasource problems.
     * @throws UnsupportedOperationException DOCUMENT ME!
     */
    private static Collection getSub(SubTransactionRequest sub)
        throws WfsTransactionException {
        LOGGER.finer("sub request is " + sub);
        throw new UnsupportedOperationException("this is being rethinked :)");

        /*
           String typeName = sub.getTypeName();
        
               try {
                 DataSource data = repository.getType(typeName)
                     .getTransactionDataSource();
                 DataSourceMetaData metad = data.getMetaData();
                 LOGGER.finer("metad is " + metad);
        
                 //LOGGER.finer(" supports trans: " + metad.supportsTransactions());
                 if (!metad.supportsAdd()) {
                   String message = typeName + " does not support transactions";
                   throw new WfsTransactionException(message, sub.getHandle());
                 }
        
                 data.setAutoCommit(false);
        
                 switch (sub.getOpType()) {
                   case SubTransactionRequest.UPDATE:
        
                     UpdateRequest update = (UpdateRequest) sub;
                     doUpdate(update, data);
        
                     break;
        
                   case SubTransactionRequest.INSERT:
        
                     InsertRequest insert = (InsertRequest) sub;
        
                     return doInsert(insert, data);
        
                   case SubTransactionRequest.DELETE:
                     LOGGER.finer("about to perform delete: " + sub);
                     doDelete( (DeleteRequest) sub, data);
        
                     break;
        
                   default:
                     break;
                 }
               }
               catch (DataSourceException e) {
                 String message = "Problem creating datasource: " + e.getMessage()
                     + ", " + e.getCause();
                 LOGGER.info(message);
                 throw new WfsTransactionException(message, sub.getHandle());
               }
               catch (WfsException e) {
                 throw new WfsTransactionException(e, sub.getHandle(), transHandle);
               }
        
               return null;
         */
    }

    /**
     * Performs the insert operation.
     *
     * @param insert the request to perform.
     * @param data the datasource to add features to.
     *
     * @return The Set of FIDs inserted.
     *
     * @throws WfsTransactionException For any backend problems.
     */
    private static Collection doInsert(InsertRequest insert, DataSource data)
        throws WfsTransactionException {
        String handle = insert.getHandle();

        try {
            //FeatureCollection newFeatures = FeatureCollections.newCollection();
            //TODO: allow different features types (need to change in request),
            //maybe then divide up by type, get the right datasources.
            //Feature[] featureArr = insert.getFeatures();
            //newFeatures.addFeatures(featureArr);
            return data.addFeatures(insert.getFeatures());
        } catch (DataSourceException e) {
            LOGGER.info("Problem with datasource " + e + " cause: "
                + e.getCause());

            try {
                data.rollback();
            } catch (DataSourceException de) {
                //should we null the datasource?  Close the connection?
                //What we really need is a connection pool, seperate for
                //transactions and requests.
                LOGGER.info("could not roll back datasource");
            }

            throw new WfsTransactionException("Problem updating features: "
                + e.toString() + " cause: " + e.getCause(), handle);
        }
    }

    /**
     * Performs the update operation.
     *
     * @param update the request to perform.
     * @param data the datasource to remove features from.
     *
     * @throws WfsTransactionException For any backend problems.
     */
    private static void doUpdate(UpdateRequest update, DataSource data)
        throws WfsTransactionException {
        String handle = update.getHandle();

        try {
            FeatureType schema = data.getSchema();

            //schema = schema.setTypeName("dude");
            AttributeType[] types = update.getTypes(schema);
            LOGGER.fine("attribut type is " + types[0] + ", values is "
                + update.getValues()[0]);
            data.modifyFeatures(types, update.getValues(), update.getFilter());
        } catch (DataSourceException e) {
            LOGGER.info("Problem with datasource " + e + " cause: "
                + e.getCause());

            try {
                data.rollback();
            } catch (DataSourceException de) {
                //should we null the datasource?  Close the connection?
                //What we really need is a connection pool, seperate for
                //transactions and requests.
                LOGGER.info("could not roll back datasource");
            }

            throw new WfsTransactionException("Problem updating features: "
                + e.toString() + " cause: " + e.getCause(), handle);
        } catch (SchemaException e) {
            throw new WfsTransactionException(e.toString(), handle);
        }
    }

    /**
     * Performs the delete operation.
     *
     * @param delete the request to perform.
     * @param data the datasource to remove features from.
     *
     * @throws WfsTransactionException For any backend problems.
     */
    private static void doDelete(DeleteRequest delete, DataSource data)
        throws WfsTransactionException {
        try {
            data.removeFeatures(delete.getFilter());
        } catch (DataSourceException e) {
            LOGGER.info("Problem with datasource " + e + " cause: "
                + e.getCause());

            try {
                data.rollback();
            } catch (DataSourceException de) {
                //should we null the datasource?  Close the connection?
                //What we really need is a connection pool, seperate for
                //transactions and requests.
                LOGGER.info("could not roll back datasource");
            }

            throw new WfsTransactionException("Problem removing features: "
                + e.getCause(), delete.getHandle());
        }
    }
}
