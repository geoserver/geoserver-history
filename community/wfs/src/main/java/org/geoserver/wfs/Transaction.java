package org.geoserver.wfs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.namespace.QName;

import net.opengis.wfs.AllSomeType;
import net.opengis.wfs.DeleteElementType;
import net.opengis.wfs.InsertElementType;
import net.opengis.wfs.NativeType;
import net.opengis.wfs.PropertyType;
import net.opengis.wfs.StatusType;
import net.opengis.wfs.TransactionOperation;
import net.opengis.wfs.TransactionResultType;
import net.opengis.wfs.UpdateElementType;
import net.opengis.wfs.WFSFactory;

import org.geoserver.data.GeoServerCatalog;
import org.geoserver.data.feature.DataStoreInfo;
import org.geoserver.data.feature.FeatureTypeInfo;
import org.geoserver.ows.ServiceException;
import org.geotools.data.DataStore;
import org.geotools.data.DefaultQuery;
import org.geotools.data.DefaultTransaction;
import org.geotools.data.FeatureLocking;
import org.geotools.data.FeatureReader;
import org.geotools.data.FeatureSource;
import org.geotools.data.FeatureStore;
import org.geotools.data.FeatureWriter;
import org.geotools.data.LockingManager;
import org.geotools.feature.AttributeType;
import org.geotools.feature.Feature;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureType;
import org.geotools.feature.IllegalAttributeException;
import org.geotools.filter.FidFilter;
import org.geotools.filter.Filter;
import org.geotools.filter.FilterFactory;
import org.geotools.filter.FilterFactoryFinder;

import com.vividsolutions.jts.geom.Envelope;

/**
 * Web Feature Service Transaction operation.
 * 
 * @author Justin Deoliveira, The Open Planning Project
 *
 */
public class Transaction {

	/**
	 * logger
	 */
	static Logger LOGGER = Logger.getLogger( "org.geoserver.wfs" );
	
	/**
	 * WFS configuration
	 */
	WFS wfs;
	/**
	 * The catalog 
	 */
	GeoServerCatalog catalog;
	/**
	 * Lock identifier
	 */
	String lockId;
	/**
	 * Request handle
	 */
	String handle;
	/**
	 * Operations
	 */
	List operation;
	/**
	 * Release action 
	 */
	AllSomeType releaseAction;
	
	/** Geotools2 transaction used for this opperations */
    protected org.geotools.data.Transaction transaction;
    
	public Transaction( WFS wfs, GeoServerCatalog catalog ) {
		this.wfs = wfs;
		this.catalog = catalog;
	}
	
	public void setOperation( List operation ) {
		this.operation = operation;
	}
	
	public void setLockId( String lockId ) {
		this.lockId = lockId;
	}
	
	public void setHandle(String handle) {
		this.handle = handle;
	}
	
	public String getHandle() {
		return handle;
	}
	
	public void setReleaseAction( AllSomeType releaseAction ) {
		this.releaseAction = releaseAction;
	}

	public TransactionResultType transaction() throws WFSException {
		//make sure server is supporting transactions
		if (( wfs.getServiceLevel() & WFS.TRANSACTIONAL) == 0) {
            throw new WFSException("Transaction support is not enabled");
        }
		
		try {
			return execute();	
		}
		catch( WFSException e ) {
			throw e;
		}
		catch( Exception e ) {
			throw new WFSException( e );
		}
		
	}
	
	/**
     * Execute Transaction request.
     * 
     * <p>
     * The results of this opperation are stored for use by writeTo:
     * 
     * <ul>
     * <li>
     * transaction: used by abort & writeTo to commit/rollback
    * </li>
     * <li>
     * request: used for users getHandle information to report errors
     * </li>
     * <li>
     * stores: FeatureStores required for Transaction
     * </li>
     * <li>
     * failures: List of failures produced
     * </li>
     * </ul>
     * </p>
     * 
     * <p>
     * Because we are using geotools2 locking facilities our modification will
     * simply fail with IOException if we have not provided proper
     * authorization.
     * </p>
     * 
     * <p>
     * The specification allows a WFS to implement PARTIAL sucess if it is
     * unable to rollback all the requested changes.  This implementation is
     * able to offer full Rollback support and will not require the use of
     * PARTIAL success.
     * </p>
     *
     * @param transactionRequest
     *
     * @throws ServiceException DOCUMENT ME!
     * @throws WfsException
     * @throws WfsTransactionException DOCUMENT ME!
     */
    protected TransactionResultType execute() throws Exception {
         
    	//some defaults
    	if ( releaseAction == null ) {
    		releaseAction = AllSomeType.ALL_LITERAL;
    	}
    	
    	//the geotools transaction
        transaction = new DefaultTransaction();
        
        //result
        TransactionResultType result = WFSFactory.eINSTANCE.createTransactionResultType();
        result.setHandle( handle );
        result.setStatus( WFSFactory.eINSTANCE.createStatusType() );
        
        //
        // We are going to preprocess our elements,
        // gathering all the FeatureSources we need
        //
        // Map of required FeatureStores by typeName
        Map stores = new HashMap();
        
        // Map of required FeatureStores by typeRef (dataStoreId:typeName)
        // (This will be added to the contents are harmed)
        Map stores2= new HashMap();

        // List of type names, maintain this list because of the insert hack described below
        List typeNames = new ArrayList();
        
        // Gather FeatureStores required by Transaction Elements
        // and configure them with our transaction
        //
        // (I am using element rather than transaction sub request
        // to agree with the spec docs)
        for ( int i = 0; i < operation.size(); i++ ) {
        	TransactionOperation element = (TransactionOperation) operation.get( i );

            String typeRef = null;
            QName elementName = null;
            FeatureTypeInfo meta = null;
            
            if ( element instanceof NativeType ) {
            	throw new WFSException( "Native transactions unsupported" );
            }
            
            if ( element instanceof InsertElementType ) {
            	InsertElementType insert = (InsertElementType) element;
            	
                // Option 1: Guess FeatureStore based on insert request
                //
            	if ( !insert.getFeature().isEmpty() ) {
                	Feature feature = (Feature) insert.getFeature().iterator().next();
                	
                    String name = feature.getFeatureType().getTypeName();
                    String namespaceURI = null;
                    if ( feature.getFeatureType().getNamespace() != null ) {
                    	namespaceURI = feature.getFeatureType().getNamespace().toString();
                    }
                    else {
                    	//default
                    	namespaceURI = catalog.getNamespaceSupport().getURI( "" );
                    }
                    
                    String prefix = 
                    	catalog.getNamespaceSupport().getPrefix( namespaceURI );
                    
                    LOGGER.fine("Locating FeatureSource uri:'"+namespaceURI+"' name:'"+name+"'");                                       
                    meta = catalog.featureType( prefix, name );
                
                    //HACK: The insert request does not get the correct typename,
                    //as we can no longer hack in the prefix since we are using the
                    //real featureType.  So this is the only good place to do the
                    //look-up for the internal typename to use.  We should probably
                    //rethink our use of prefixed internal typenames (cdf:bc_roads),
                    //and have our requests actually use a type uri and type name.
                    //Internally we can keep the prefixes, but if we do that then
                    //this will be less hacky and we'll also be able to read in xml
                    //for real, since the prefix should refer to the uri.
                    //
                    // JG:
                    // Transalation Insert does not have a clue about prefix - this provides the clue
                   
                    elementName = new QName( namespaceURI, name, prefix );
                    
                    //element.setTypeName( meta.getNameSpace().getPrefix()+":"+meta.getTypeName() );
                }
                else {
                    LOGGER.finer("Insert was empty - does not need a FeatuerSoruce");
                	continue; // insert is actually empty
                }
            }
            else {
                // Option 2: lookup based on elmentName (assume prefix:typeName)
                typeRef = null; // unknown at this time
                
                //one of DeleteElementType, or UpdateElementType, both contain typeName property
                elementName = (QName) EMFUtils.get( element, "typeName" );
                
                if( stores.containsKey( elementName )) {
                    LOGGER.finer("FeatureSource '"+elementName+"' already loaded." );
                    continue;
                }
                LOGGER.fine("Locating FeatureSource '"+elementName+"'...");
                
                meta = catalog.featureType( elementName.getPrefix(), elementName.getLocalPart() );
                
                String namespaceURI = 
                	catalog.getNamespaceSupport().getURI( meta.namespacePrefix() );
                
                EMFUtils.set( 
            		element, "typeName", new QName( namespaceURI, meta.getTypeName(), meta.namespacePrefix() )
				);
            
            }
            
            typeRef = meta.getDataStore().getId()+":"+meta.getTypeName();
            
            LOGGER.fine("located FeatureType w/ typeRef '"+typeRef+"' and elementName '"+elementName+"'" );                          
            if (stores.containsKey(elementName)) {
                // typeName already loaded
                continue;
            }
            
            typeNames.add( elementName );
            
            try {
                FeatureSource source = meta.featureSource();
                if (source instanceof FeatureStore) {
                    FeatureStore store = (FeatureStore) source;
                    store.setTransaction(transaction);
                    stores.put( elementName, source );
                    stores2.put( typeRef, source );
                } 
                else {
                	String msg = elementName + " is read-only";
                	String eHandle = (String) EMFUtils.get( element, "handle" );
                	throw new WFSTransactionException( msg, (String) null, eHandle, handle );
                }
            } 
            catch (IOException ioException) {
            	String msg = elementName + " is not available: " + ioException.getLocalizedMessage();
            	String eHandle = (String) EMFUtils.get( element, "handle" );
                throw new WFSTransactionException( msg, ioException, eHandle, handle );
            }
        }

        // provide authorization for transaction
        // 
        String authorizationID = lockId;

        if (authorizationID != null) {
        	if ( ( wfs.getServiceLevel() & WFS.SERVICE_LOCKING ) == 0 ) {
        		throw new WFSException("Lock support is not enabled");
        	}
            LOGGER.finer("got lockId: " + authorizationID);

            if ( !lockExists( authorizationID ) ) {
            	String mesg = "Attempting to use a lockID that does not exist"
                    + ", it has either expired or was entered wrong.";
                throw new WFSException( mesg );
            }
            
            try {
                transaction.addAuthorization(authorizationID);
            } catch (IOException ioException) {
                // This is a real failure - not associated with a element
                //
                throw new WFSException("Authorization ID '" + authorizationID
                    + "' not useable", ioException);
            }
        }

        // execute elements in order,
        // recording results as we go
        //
        // I will need to record the damaged area for
        // pre commit validation checks
        //
        Envelope envelope = new Envelope();

        for (int i = 0; i < operation.size(); i++) {
            TransactionOperation element = (TransactionOperation) operation.get( i );
            
            // We expect element name to be of the format prefix:typeName
            // We take care to force the insert element to have this format above.
            //
            // JD: changd to maintain teh list in a seperate list
            //String elementName = element.getTypeName();
            QName elementName = (QName) typeNames.get( i );
            String handle = (String) EMFUtils.get( element, "handle" );
            
            FeatureStore store = (FeatureStore) stores.get(elementName);
            if( store == null ){
            	throw new WFSException( "Could not locate FeatureStore for '"+elementName+"'" );                        
            }
            String typeName = store.getSchema().getTypeName();
            
            if (element instanceof DeleteElementType ) {
                if (( wfs.getServiceLevel() & WFS.SERVICE_DELETE) == 0) {
                    // could we catch this during the handler, rather than during execution?
                    throw new WFSException( "Transaction Delete support is not enabled");
                }
                
                DeleteElementType delete = (DeleteElementType) element;
                
                //do a check for Filter.NONE, the spec specifically does not
                // allow this
                if (delete.getFilter() == Filter.NONE) {
                	throw new WFSException( "Filter must be supplied for Transaction Delete" );
                }
                
                LOGGER.finer( "Transaction Delete:"+element );
                try {
                    Filter filter = (Filter) delete.getFilter();

                    Envelope damaged = store.getBounds(
                		new DefaultQuery( delete.getTypeName().getLocalPart(), filter));

                    if (damaged == null) {
                        damaged = store.getFeatures(filter).getBounds();
                    }

                    if ((lockId != null)
                            && store instanceof FeatureLocking
                            && (releaseAction == AllSomeType.SOME_LITERAL)) {
                        FeatureLocking locking = (FeatureLocking) store;

                        // TODO: Revisit Lock/Delete interaction in gt2 
                        if (false) {
                            // REVISIT: This is bad - by releasing locks before
                            // we remove features we open ourselves up to the danger
                            // of someone else locking the features we are about to
                            // remove.
                            //
                            // We cannot do it the other way round, as the Features will
                            // not exist
                            //
                            // We cannot grab the fids offline using AUTO_COMMIT
                            // because we may have removed some of them earlier in the
                            // transaction
                            //
                            locking.unLockFeatures(filter);
                            store.removeFeatures(filter);
                        } else {
                            // This a bit better and what should be done, we will
                            // need to rework the gt2 locking api to work with
                            // fids or something
                            //
                            // The only other thing that would work would be
                            // to specify that FeatureLocking is required to
                            // remove locks when removing Features.
                            // 
                            // While that sounds like a good idea, it would be
                            // extra work when doing release mode ALL.
                            // 
                            DataStore data = store.getDataStore();
                            FilterFactory factory = FilterFactoryFinder
                                .createFilterFactory();
                            FeatureWriter writer;                            
                            writer = data.getFeatureWriter(typeName, filter,
                                    transaction);

                            try {
                                while (writer.hasNext()) {
                                    String fid = writer.next().getID();
                                    locking.unLockFeatures(factory
                                        .createFidFilter(fid));
                                    writer.remove();
                                }
                            } finally {
                                writer.close();
                            }

                            store.removeFeatures(filter);
                        }
                    } else {
                        // We don't have to worry about locking right now
                        //
                        store.removeFeatures(filter);
                    }

                    envelope.expandToInclude(damaged);
                } catch (IOException ioException) {
                	String msg = ioException.getMessage();
                	String eHandle = (String) EMFUtils.get( element, "handle" );
                	
                    throw new WFSTransactionException(msg, (String) null, eHandle, handle );
                }
            }

            if (element instanceof InsertElementType) {
                if ((wfs.getServiceLevel() & WFS.SERVICE_INSERT) == 0) {
                    // could we catch this during the handler, rather than during execution?
                    throw new WFSException( "Transaction INSERT support is not enabled" );
                }
                LOGGER.finer( "Transasction Insert:"+element );
                
                InsertElementType insert = (InsertElementType) element;
                
                try {
                    
                    FeatureType schema = store.getSchema();

                    FeatureCollection collection = insert.getFeature();
                    FeatureReader reader = collection.reader();
                    	
                    // Need to use the namespace here for the lookup, due to our weird
                    // prefixed internal typenames.  see 
                    //   http://jira.codehaus.org/secure/ViewIssue.jspa?key=GEOS-143
                    
                    // Once we get our datastores making features with the correct namespaces
                    // we can do something like this:
                    // FeatureTypeInfo typeInfo = catalog.getFeatureTypeInfo(schema.getTypeName(), schema.getNamespace());
                    // until then (when geos-144 is resolved) we're stuck with:
                    QName qName = (QName) typeNames.get( i );
                    FeatureTypeInfo typeInfo = 
                    	catalog.featureType( qName.getPrefix(), qName.getLocalPart() );

                    // this is possible with the insert hack above.
                    LOGGER.finer("Use featureValidation to check contents of insert" );
                    //featureValidation( typeInfo.getDataStore().getId(), schema, collection );

                    Set fids = store.addFeatures(reader);
                    
                    //TODO: JD, report back fids?
                    //build.addInsertResult( insert.getHandle(), fids );
                    
                    //
                    // Add to validation check envelope                                
                    envelope.expandToInclude(collection.getBounds());
                } catch (IOException ioException) {
                	throw new WFSTransactionException( ioException, insert.getHandle(), handle );
                    
                }
            }

            if (element instanceof UpdateElementType) {
                if ((wfs.getServiceLevel() & WFS.SERVICE_UPDATE) == 0) {
                    // could we catch this during the handler, rather than during execution?
                    throw new WFSException( "Transaction Update support is not enabled");
                }
                
                LOGGER.finer( "Transaction Update:"+element);
                UpdateElementType update = (UpdateElementType) element;
                try {
                    Filter filter = (Filter) update.getFilter();

                    AttributeType[] types = new AttributeType[ update.getProperty().size() ];
                    Object[] values = new Object[ update.getProperty().size() ];
                    for ( int j = 0; j < update.getProperty().size(); j++ ) {
                    	PropertyType property = (PropertyType) update.getProperty().get( i );
                    	types[ j ] = store.getSchema().getAttributeType( property.getName() );
                    	values[ j ] = property.getValue();
                    }
                     
                    DefaultQuery query = 
                    	new DefaultQuery(update.getTypeName().getLocalPart(), filter);

                    // Pass through data to collect fids and damaged region
                    // for validation
                    //
                    Set fids = new HashSet();
                    LOGGER.finer("Preprocess to remember modification as a set of fids" );                    
                    FeatureReader preprocess = store.getFeatures( filter ).reader();
                    try {
                        while( preprocess.hasNext() ){
                            Feature feature = preprocess.next();
                            fids.add( feature.getID() );
                            envelope.expandToInclude( feature.getBounds() );
                        }
                    } catch (NoSuchElementException e) {
                        throw new WFSException( "Could not aquire FeatureIDs", e );
                    } catch (IllegalAttributeException e) {
                        throw new WFSException( "Could not aquire FeatureIDs", e );
                    }
                    finally {
                        preprocess.close();
                    }
                    
                    try{
	                    if (types.length == 1) {
	                        store.modifyFeatures(types[0], values[0], filter);
	                    } else {
	                        store.modifyFeatures(types, values, filter);
	                    }
	                }catch (IOException e) {
	                	//DJB: this is for cite tests.  We should probaby do this for all the 
	                	// exceptions here - throw a transaction FAIL instead of serice exception 
	                	
	                	//this failed - we want a FAILED not a service exception!
	                	result.getStatus().setFAILED( WFSFactory.eINSTANCE.createEmptyType() );
	                	result.setMessage( e.getLocalizedMessage() );
	                	
	                	// DJB: it looks like the transaction is rolled back in writeTo()
	                }                   
	                    
	                if (( lockId != null)
	                        && store instanceof FeatureLocking
	                        && ( releaseAction == AllSomeType.SOME_LITERAL )) {
	                    FeatureLocking locking = (FeatureLocking) store;
	                    locking.unLockFeatures(filter);
	                }
                    
	                // Post process - check features for changed boundary and
	                // pass them off to the ValidationProcessor
	                //
	                if( !fids.isEmpty() ) {
	                    LOGGER.finer("Post process update for boundary update and featureValidation");
	                    FidFilter modified = FilterFactoryFinder.createFilterFactory().createFidFilter();
	                    modified.addAllFids( fids );
	                
	                    FeatureCollection changed = store.getFeatures( modified ).collection();
	                    envelope.expandToInclude( changed.getBounds() );
	                
	                    FeatureTypeInfo typeInfo = //catalog.getFeatureTypeInfo(element.getTypeName());
	                    	catalog.featureType( update.getTypeName().getPrefix(), update.getTypeName().getLocalPart() );
	                    
	                    //featureValidation(typeInfo.getDataStore().getId(),store.getSchema(), changed);                    
	                }
                } catch (IOException ioException) {
                	throw new WFSTransactionException( ioException, update.getHandle(), handle );
                } 
            } //end update
        }

        
        if ( result.getStatus().getPARTIAL() != null ) {
            throw new WFSException("Canceling PARTIAL response");
        }
        
        try {
        	if ( result.getStatus().getFAILED() != null ) {
            	//transaction failed, roll it back
            	transaction.rollback();
            }
        	else {
        		transaction.commit();
        		result.getStatus().setSUCCESS( WFSFactory.eINSTANCE.createEmptyType() );
        	}
        	
        }
        finally {
        	transaction.close();
        	transaction = null;
        }
        
        // 
        // Lets deal with the locks
        //
        // Q: Why talk to Data you ask
        // A: Only class that knows all the DataStores
        //
        // We really need to ask all DataStores to release/refresh
        // because we may have locked Features with this Authorizations
        // on them, even though we did not refer to them in this transaction.
        //
        // Q: Why here, why now?
        // A: The opperation was a success, and we have completed the opperation
        //
        // We also need to do this if the opperation is not a success,
        // you can find this same code in the abort method
        // 
        if ( lockId != null ) {
            if ( releaseAction == AllSomeType.ALL_LITERAL ) {
                lockRelease( lockId );
            } else if ( releaseAction == AllSomeType.SOME_LITERAL) {
                lockRefresh( lockId );
            }
        }
        
        return result;
        
        // we will commit in the writeTo method
        // after user has got the response
        //response = build;
    }

    
    /**
     * Implement lockExists.
     *
     * @param lockID
     *
     * @return true if lockID exists
     *
     * @see org.geotools.data.Data#lockExists(java.lang.String)
     */
    private boolean lockExists( String lockID) throws Exception {
    	List dataStores = catalog.dataStores();
		
    	for (Iterator i = dataStores.iterator(); i.hasNext();) {
            DataStoreInfo meta = (DataStoreInfo) i.next();

            if (!meta.isEnabled()) {
                continue; // disabled
            }

            DataStore dataStore;

            try {
                dataStore = meta.getDataStore();
            } catch (IllegalStateException notAvailable) {
                continue; // not available
            }

            LockingManager lockingManager = dataStore.getLockingManager();

            if (lockingManager == null) {
                continue; // locks not supported
            }

            if (lockingManager.exists(lockID)) {
                return true;
            }
        }

        return false;
    }
    
    /**
     * Release all feature locks currently held.
     * 
     * <p>
     * This is the implementation for the Admin "free lock" action, transaction
     * locks are not released.
     * </p>
     *
     * @return Number of locks released
     */
    private int lockReleaseAll() throws Exception {
        int count = 0;

        List dataStores = catalog.dataStores();
        for (Iterator i = dataStores.iterator(); i.hasNext();) {
            DataStoreInfo meta = (DataStoreInfo) i.next();

            if (!meta.isEnabled()) {
                continue; // disabled
            }

            DataStore dataStore;

            try {
                dataStore = meta.getDataStore();
            } catch (IllegalStateException notAvailable) {
                continue; // not available
            } catch (Throwable huh) {
                continue; // not even working
            }

            LockingManager lockingManager = dataStore.getLockingManager();

            if (lockingManager == null) {
                continue; // locks not supported
            }

            // TODO: implement LockingManger.releaseAll()
            //count += lockingManager.releaseAll();            
        }

        return count;
    }

    /**
     * Release lock by authorization
     *
     * @param lockID
     */
    private void lockRelease(String lockID) throws Exception {
        boolean refresh = false;

        List dataStores = catalog.dataStores();
        for (Iterator i = dataStores.iterator(); i.hasNext();) {
            DataStoreInfo meta = (DataStoreInfo) i.next();

            if (!meta.isEnabled()) {
                continue; // disabled
            }

            DataStore dataStore;

            try {
                dataStore = meta.getDataStore();
            } catch (IllegalStateException notAvailable) {
                continue; // not available
            }

            LockingManager lockingManager = dataStore.getLockingManager();

            if (lockingManager == null) {
                continue; // locks not supported
            }

            org.geotools.data.Transaction t = 
            	new DefaultTransaction("Refresh " + meta.getNamespacePrefix() );

            try {
                t.addAuthorization(lockID);

                if (lockingManager.release(lockID, t)) {
                    refresh = true;
                }
            } catch (IOException e) {
                LOGGER.log(Level.WARNING, e.getMessage(), e);
            } finally {
                try {
                    t.close();
                } catch (IOException closeException) {
                    LOGGER.log(Level.FINEST, closeException.getMessage(),
                        closeException);
                }
            }
        }

        if (!refresh) {
            // throw exception? or ignore...
        }
    }
    
    /**
     * Refresh lock by authorization
     * 
     * <p>
     * Should use your own transaction?
     * </p>
     *
     * @param lockID
     */
    private void lockRefresh(String lockID) throws Exception {
        boolean refresh = false;

        List dataStores = catalog.dataStores();
        for (Iterator i = dataStores.iterator(); i.hasNext();) {
            DataStoreInfo meta = (DataStoreInfo) i.next();

            if (!meta.isEnabled()) {
                continue; // disabled
            }

            DataStore dataStore;

            try {
                dataStore = meta.getDataStore();
            } catch (IllegalStateException notAvailable) {
                continue; // not available
            }

            LockingManager lockingManager = dataStore.getLockingManager();

            if (lockingManager == null) {
                continue; // locks not supported
            }

            org.geotools.data.Transaction t = 
            	new DefaultTransaction("Refresh " + meta.getNamespacePrefix());

            try {
                t.addAuthorization(lockID);

                if (lockingManager.refresh(lockID, t)) {
                    refresh = true;
                }
            } catch (IOException e) {
                LOGGER.log(Level.WARNING, e.getMessage(), e);
            } finally {
                try {
                    t.close();
                } catch (IOException closeException) {
                    LOGGER.log(Level.FINEST, closeException.getMessage(),
                        closeException);
                }
            }
        }

        if (!refresh) {
            // throw exception? or ignore...
        }
    }
}
