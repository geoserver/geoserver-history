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

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;

import java.io.*;
import java.util.*;
import java.util.logging.*;


/**
 * Handles a Transaction request and creates a TransactionResponse string.
 *
 * @author Chris Holmes, TOPP
 * @version $Id: TransactionResponse.java,v 1.1.2.5 2003/11/16 07:38:48 jive Exp $
 */
public class TransactionResponse implements Response {
    /** Standard logging instance for class */
    private static final Logger LOGGER = Logger.getLogger(
            "org.vfny.geoserver.responses");
    
    /** Response to be streamed during writeTo */
    private WfsTransResponse response;

    /** Request provided to Execute method */
    private TransactionRequest request;
    
    /** Geotools2 transaction used for this opperations */
    protected Transaction transaction;
    
    /**
     * Constructor
     */
    public TransactionResponse() {
        transaction = null;
    }

    public void execute(Request request) throws WfsException {
        if (!(request instanceof TransactionRequest)) {
            throw new WfsException(
                "bad request, expected TransactionRequest, but got " + request);
        }        
        execute( (TransactionRequest) request );
        
    }
    
    /**
     * Execute Transaction request.
     * <p>
     * The results of this opperation are stored for use by writeTo:
     * <ul>
     * <li>transaction: used by abort & writeTo to commit/rollback</li>
     * <li>request: used for users getHandle information to report errors</li>
     * <li>stores: FeatureStores required for Transaction</li>
     * <li>failures: List of failures produced</li>
     * </ul>
     * 
     * <p>
     * Because we are using geotools2 locking facilities our modification
     * will simply fail with IOException if we have not provided proper
     * authorization.
     * </p>
     * <p>
     * The specification allows a WFS to implement PARTIAL sucess if it is
     * unable to rollback all the requested changes.  This implementation is
     * able to offer full Rollback support and will not require the use of
     * PARTIAL success.
     * </p>
     * 
     * @param transactionRequest
     * @throws WfsException
     */
    protected void execute(TransactionRequest transactionRequest ) throws WfsException{
        request = transactionRequest; // preserved toWrite() handle access 
        transaction = new DefaultTransaction();
        
        CatalogConfig catalog = ServerConfig.getInstance().getCatalog();
                    
        WfsTransResponse build = new WfsTransResponse( WfsTransResponse.SUCCESS );
                                 
        // Map of required FeatureStores by typeName
        Map stores = new HashMap();
        
        // Gather FeatureStores required by Transaction Elements
        // and configure them with our transaction
        //
        // (I am using element rather than transaction sub request
        // to agree with the spec docs)
        for( int i=0; i < request.getSubRequestSize(); i++){
            SubTransactionRequest element = request.getSubRequest( i );
            String typeName = element.getTypeName();
            if( !stores.containsKey( typeName )){
                FeatureTypeConfig meta = catalog.getFeatureType( typeName );
                try {
                    FeatureSource source = meta.getFeatureSource();
                    if( source instanceof FeatureStore ){
                        FeatureStore store = (FeatureStore) source;
                        store.setTransaction( transaction );
                        stores.put( typeName, source );                                                                   
                    }
                    else {
                        throw new WfsTransactionException(
                            typeName+" is read-only",
                            element.getHandle(),
                            request.getHandle()
                        );
                    }                    
                }
                catch( IOException ioException ){
                    throw new WfsTransactionException(
                        typeName+" is not available:"+ioException,
                        element.getHandle(),
                        request.getHandle()
                    );                    
                }
            }
        }
        
        // provide authorization for transaction
        // 
        String authorizationID = request.getLockId();
        if( authorizationID != null ){
            LOGGER.finer("got lockId: " + authorizationID );
            try {
                transaction.addAuthorization( authorizationID );
            } catch (IOException ioException) {
                // This is a real failure - not associated with a element
                //
                throw new WfsException( "Authorization ID '"+authorizationID+"' not useable", ioException);
            }
        }
        // execute elements in order,
        // recording results as we go
        //
        // I will need to record the damaged area for
        // pre commit validation checks
        //
        Envelope envelope = new Envelope();
        
        for( int i=0; i < request.getSubRequestSize(); i++){
            SubTransactionRequest element = request.getSubRequest( i );
            String typeName = element.getTypeName();
            String handle = element.getHandle();
            FeatureStore store = (FeatureStore) stores.get( typeName );
                        
            if( element instanceof DeleteRequest ){
                try {
                    DeleteRequest delete = (DeleteRequest) element;
                    Filter filter = delete.getFilter();
                    
                
                    Envelope damaged = store.getBounds( new DefaultQuery( filter ) );
                    if( damaged == null ){
                        damaged = store.getFeatures( filter ).getBounds();
                    }
                    
                    store.removeFeatures( filter );
                    
                    envelope.expandToInclude( damaged );
                }
                catch (IOException ioException) {
                    throw new WfsTransactionException(
                        ioException.getMessage(),
                        element.getHandle(),
                        request.getHandle()
                    );
                }
            }
            if( element instanceof InsertRequest ){
                try {
                    InsertRequest insert = (InsertRequest) element;
                    FeatureCollection collection = insert.getFeatures();
                    
                    FeatureReader reader = DataUtilities.reader( collection );
                    //
                    featureValidation( store.getSchema(), collection );
                    
                    Set fids = store.addFeatures( reader );
                    build.addInsertResult( element.getHandle(), fids );
                    
                    //
                    // Add to validation check envelope                                
                    envelope.expandToInclude( collection.getBounds() );
                }
                catch( IOException ioException ){
                    throw new WfsTransactionException(
                        ioException.getMessage(),
                        element.getHandle(),
                        request.getHandle()
                    );                    
                }                  
            }
            if( element instanceof UpdateRequest ){
                try {
                    UpdateRequest update = (UpdateRequest) element;
                    Filter filter = update.getFilter();
                    
                    AttributeType types[] = update.getTypes( store.getSchema() );
                    Object values[] = update.getValues();
                    
                    envelope.expandToInclude( store.getBounds( new DefaultQuery(filter)));
                    
                    if( types.length == 1){
                        store.modifyFeatures( types[0], values[0], filter );                        
                    }
                    else {
                        store.modifyFeatures( types, values, filter );                        
                    }
                    // we only have to do this again if values contains a geometry type
                    //
                    envelope.expandToInclude( store.getBounds( new DefaultQuery(filter)));                       
                }   
                             
                catch( IOException ioException ){
                    throw new WfsTransactionException(
                        ioException.getMessage(),
                        element.getHandle(),
                        request.getHandle()
                    );                    
                } catch (SchemaException typeException) {
                    throw new WfsTransactionException(
                        typeName +" inconsistent with update:"+typeException.getMessage(),
                        element.getHandle(),
                        request.getHandle()
                    );
                }
            }
        }
        // All opperations have worked thus far
        // 
        // Time for some global Validation Checks against envelope
        //
        try {
            integrityValidation( stores, envelope );            
        }
        catch( IOException invalid ){
            throw new WfsTransactionException( invalid );            
        }
        // okay we are good to go, we will commit in the writeTo method
        // after user has got the response 
        response = build;                
    }

    protected void featureValidation( FeatureType type, FeatureCollection collection ) throws IOException {
        // need to hook into featureValidation check here
        // 
        // For now we will check type and geometry validity
        // (just for fun)
        for( FeatureIterator i=collection.features(); i.hasNext();){
            Feature feature = i.next();
            int compare = DataUtilities.compare( type, feature.getFeatureType() );
            if( compare != 0){
                throw new IOException(
                    feature.getID()+" "+type.getTypeName()+" validation failed:"+ 
                    feature.getFeatureType() );
            }            
            if( type.getDefaultGeometry() != null){
                Geometry geom = feature.getDefaultGeometry();
                if( !geom.isValid() ){
                    throw new IOException(
                        feature.getID()+" geometry validation failed:"+
                        geom );                    
                }                
            }           
        }        
    }
    protected void integrityValidation( Map stores, Envelope check ) throws IOException {
        // need to hook into integrity validation here
        //
        // For now we will just check that there are no duplicate
        // fids, really we are only supposed to check within the
        // check envelope
        //
        // Remember that these FeatureSources are backed by the transaction
        //
        // We can be smart and only request the Fids
        //
        // Chris tells me that by asking for a Query with no properties
        // I'll be okay
        DefaultQuery fidQuery = new DefaultQuery( Filter.NONE, new String[0] );
        fidQuery.setHandle( request.getHandle()+" integrity validation");
        for( Iterator i=stores.values().iterator(); i.hasNext();){
            FeatureSource source = (FeatureSource) i.next();
            String typeName = source.getSchema().getTypeName();
            Set sanityCheck = new HashSet();
            
            FeatureReader reader = source.getFeatures( fidQuery ).reader();
            try {
                while( reader.hasNext() ){
                    String fid = reader.next().getID();
                    if( sanityCheck.contains( fid )){
                        throw new IOException(
                            typeName+" validation error: "+
                            fid +" is a duplicate feature id"
                        );    
                    }
                    else {
                        sanityCheck.add( fid );
                    }
                }
            } catch (NoSuchElementException noElemenetException) {
                throw new IOException(
                    "Problem confirming "+typeName+" integrity:"+
                    noElemenetException
                );
            } catch (IllegalAttributeException attribException) {
                throw new IOException(
                    "Problem confirming "+typeName+" integrity:"+
                    attribException
                );
            }
            finally {
                reader.close();
                sanityCheck.clear();
            }
        }    
    }
    /** 
     * Responce MIME type as define by ServerConig.
     */
    public String getContentType() {       
        return ServerConfig.getInstance().getGlobalConfig().getMimeType();
    }

    /**
     * Writes generated xmlResponse.
     * <p>
     * I have delayed commiting the result until we have returned it
     * to the user, this gives us a chance to rollback if we are not able
     * to provide a response.
     * </p>     
     */
    public void writeTo(OutputStream out) throws ServiceException, IOException {
        if( transaction == null || response == null){
            throw new ServiceException("Transaction not executed");            
        }
        try {
            Writer writer;
            
            writer = new OutputStreamWriter( out );
            writer = new BufferedWriter( writer );
            
            response.writeXmlResponse( writer );
            writer.flush();
            
            if( response.status == WfsTransResponse.SUCCESS ){
                transaction.commit();                
            }
            else {
                transaction.rollback();
            }
        }
        catch( IOException ioException ){
            transaction.rollback();
            throw ioException;                       
        }
        finally {
            transaction.close();
            transaction = null;            
        }                            
    }    

    /* (non-Javadoc)
     * @see org.vfny.geoserver.responses.Response#abort()
     */
    public void abort() {
        if( transaction == null ){
            return; // no transaction to rollback
        }
        try {
            transaction.rollback();
            transaction.close();            
        }
        catch( IOException ioException ){
            // nothing we can do here
            LOGGER.log( Level.SEVERE, "Failed trying to rollback a transaction:"+ioException);   
        }        
    }

}
