package org.geoserver.wfs;

import java.io.IOException;
import java.math.BigInteger;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.namespace.QName;

import net.opengis.wfs.AllSomeType;
import net.opengis.wfs.LockFeatureResponseType;
import net.opengis.wfs.LockFeatureType;
import net.opengis.wfs.LockType;
import net.opengis.wfs.WFSFactory;

import org.geoserver.data.GeoServerCatalog;
import org.geoserver.data.feature.DataStoreInfo;
import org.geoserver.data.feature.FeatureTypeInfo;
import org.geotools.data.DataStore;
import org.geotools.data.DefaultQuery;
import org.geotools.data.DefaultTransaction;
import org.geotools.data.FeatureLock;
import org.geotools.data.FeatureLockFactory;
import org.geotools.data.FeatureLocking;
import org.geotools.data.FeatureReader;
import org.geotools.data.FeatureResults;
import org.geotools.data.FeatureSource;
import org.geotools.data.LockingManager;
import org.geotools.data.Query;
import org.geotools.data.Transaction;
import org.geotools.feature.Feature;
import org.geotools.feature.IllegalAttributeException;
import org.geotools.filter.Filter;
import org.geotools.filter.FilterFactory;
import org.geotools.filter.FilterFactoryFinder;
import org.opengis.filter.FeatureId;

/**
 * Web Feature Service 1.0 LockFeature Operation.
 * 
 * @author Justin Deoliveira, The Open Planning Project
 *
 */
public class LockFeature {

	/**
	 * The logger
	 */
	static Logger LOGGER = Logger.getLogger( "org.geoserver.wfs" );
	
	/**
	 * Web Feature Service configuration
	 */
	WFS wfs;
	
	/**
	 * The catalog
	 */
	GeoServerCatalog catalog;
	
	/**
     * Filter factory
     */
	FilterFactory filterFactory;
	
    public LockFeature( WFS wfs, GeoServerCatalog catalog ) {
    	this( wfs, catalog, null );
    }
    
    public LockFeature( WFS wfs, GeoServerCatalog catalog, FilterFactory filterFactory ) {
    	this.wfs = wfs;
    	this.catalog = catalog;
    	this.filterFactory = filterFactory;
    }
    
    public void setFilterFactory(FilterFactory filterFactory) {
		this.filterFactory = filterFactory;
	}
  
	public LockFeatureResponseType lockFeature( LockFeatureType request ) throws WFSException {
	
		FeatureLock fLock = null;
		try {
			//get the locks
			List locks = request.getLock();
			if ( locks == null || locks.isEmpty() ) {
				String msg = "A LockFeature request must contain at least one LOCK element";
			    throw new WFSException( msg );
			}

			//should we releas all? if not set default to true
			boolean lockAll = !( request.getLockAction() == AllSomeType.SOME_LITERAL ); 
			
			//create a new lock
			fLock = newFeatureLock( request );
			
			LOGGER.info("locks size is " + locks.size());

			if ( locks.size() == 0 ) {
				throw new WFSException( "Request contains no locks." );
			}

			LockFeatureResponseType response = WFSFactory.eINSTANCE.createLockFeatureResponseType();
			response.setLockId( fLock.getAuthorization() );
			response.setFeaturesLocked( WFSFactory.eINSTANCE.createFeaturesLockedType() );
			response.setFeaturesNotLocked( WFSFactory.eINSTANCE.createFeaturesNotLockedType() );
			
			for (int i = 0, n = locks.size(); i < n; i++) {
				LockType lock = (LockType) locks.get( i );
				LOGGER.info("curLock is " + lock);

				QName typeName = lock.getTypeName();
			    Filter filter = (Filter) lock.getFilter();
			    
			    FeatureTypeInfo meta;
				FeatureSource source;
				FeatureResults features;
				try {
					meta = catalog.featureType( typeName.getNamespaceURI(), typeName.getLocalPart() );
					source = meta.featureSource();
					features = source.getFeatures( filter );

					if( source instanceof FeatureLocking){
					    ((FeatureLocking)source).setFeatureLock( fLock );
					}
				} 
				catch ( IOException e ) {
					throw new WFSException( e );
				}
			  
			    FeatureReader reader = null;
			    int numberLocked = -1;
			    try {
			        for (reader = features.reader(); reader.hasNext();) {
			            Feature feature = reader.next();
			            FeatureId fid = fid( feature.getID() );
			            
			            if( !(source instanceof FeatureLocking) ){
			                LOGGER.fine("Lock " + fid +
			                        " not supported by data store (authID:"
			                        + fLock.getAuthorization() + ")");
			                response.getFeaturesNotLocked().getFeatureId().add( fid );
			                //lockFailedFids.add(fid);
			            }
			            else {
			                
			                //DEFQuery is just some indirection, should be in the locking interface.
			                //int numberLocked = ((DEFQueryFeatureLocking)source).lockFeature(feature);
			                //HACK: Query.NO_NAMES isn't working in postgis right now,
			                //so we'll just use all.
			                Query query = new DefaultQuery( 
			            		meta.getTypeName(), (Filter) fid, Query.DEFAULT_MAX, Query.ALL_NAMES, 
			            		lock.getHandle()
			        		);
			                
			                numberLocked = ((FeatureLocking)source).lockFeatures( query );

			                if (numberLocked == 1) {
			                    LOGGER.fine("Lock " + fid + " (authID:" + fLock.getAuthorization() + ")");
			                    response.getFeaturesLocked().getFeatureId().add( fid );
			                    //lockedFids.add(fid);
			                } 
			                else if (numberLocked == 0) {
			                    LOGGER.fine("Lock " + fid + " conflict (authID:" + fLock.getAuthorization() + ")");
			                    response.getFeaturesNotLocked().getFeatureId().add( fid );
			                    //lockFailedFids.add(fid);
			                } 
			                else {
			                    LOGGER.warning(
			                		"Lock " + numberLocked + " " + fid + " (authID:" + 
			                		fLock.getAuthorization() + ") duplicated FeatureID!"
			            		);
			                    response.getFeaturesLocked().getFeatureId().add( fid );
			                    //lockedFids.add(fid);
			                }
			            }
			        }
			    }
			    catch ( IOException ioe ) {
			    	throw new WFSException( ioe );
			    } 
			    catch (IllegalAttributeException e) {
			        // TODO: JG - I really dont like this
			        // reader says it will throw this if the attribtues do not match
			        // the FeatureTypeInfo
			        // I figure if this is thrown we are poorly configured or
			        // the DataStoreInfo needs some quality control
			        //
			        // should rollback the lock as well :-(
			    	String msg = "Lock request " + filter + " did not match " + typeName;
			        throw new WFSException( msg );
			    } finally {
			        if (reader != null) {
			            try {
							reader.close();
						} 
			            catch (IOException e) {
			            	LOGGER.log( Level.WARNING, e.getLocalizedMessage(), e );
			            }
			        }
			    }
			    
			    if ( numberLocked > 0 ) {
			    	//JD: Why is this necessary, arent the features already locked?
			        Transaction t = new DefaultTransaction();

			        try {
						try {
						    t.addAuthorization( response.getLockId() );
						    source.getDataStore().getLockingManager().refresh( response.getLockId(), t);
						} finally {
						    t.commit();
						}
					} 
			        catch (IOException e) {
			        	throw new WFSException( e );
					}
			    }
			}

			if ( lockAll && !response.getFeaturesNotLocked().getFeatureId().isEmpty() ) {
			    // I think we need to release and fail when lockAll fails
			    //
			    // abort will release the locks
			    throw new WFSException("Could not aquire locks for:" + response.getFeaturesNotLocked());
			}

			return response;
		} 
		catch( WFSException e ) {
		
			//release locks when something fails
			if ( fLock != null ) {
				try {
					release( fLock.getAuthorization() );
				} 
				catch ( WFSException e1 ) {
					//log it
					LOGGER.log( Level.SEVERE, "Error occured releasing locks", e1 );
				}
	        }

		
	       throw e;
		}
    }
	
	/**
     * Release lock by authorization
     *
     * @param lockID
     */
	public void release( String lockId ) throws WFSException {
		try {
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
	                t.addAuthorization(lockId);

	                if (lockingManager.release(lockId, t)) {
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
		catch (Exception e) {
			throw new WFSException( e );
		}
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
	public void releaseAll() throws WFSException {
		try {
			
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
		} 
		catch (Exception e) {
			throw new WFSException( e );
		}
	}
	
	
	public boolean exists( String lockId ) throws WFSException {
    	try {
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
                
                if (lockingManager.exists(lockId)) {
                    return true;
                }
            }

            return false;
		} 
    	catch (Exception e) {
    		throw new WFSException( e );
		}
    
    }
    
    public void refresh( String lockId ) throws WFSException {
    	try {
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
                    t.addAuthorization( lockId );

                    if (lockingManager.refresh( lockId , t)) {
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
    	catch (Exception e) {
    		throw new WFSException( e );
		}
    }
    
   private FeatureId fid( String fid ) {
		Set fids = new HashSet();
		fids.add( fid );
		
		return filterFactory.featureId( fids );
	}
	
	protected FeatureLock newFeatureLock( LockFeatureType request ) {
	      
		if ( request.getHandle() == null || request.getHandle().equals( "" ) ) {
			request.setHandle( "GeoServer" );
		}
        if ( request.getExpiry() == null ) {
        	request.setExpiry( BigInteger.valueOf( 0 ) );
        }

        int lockExpiry = request.getExpiry().intValue();
        if (lockExpiry < 0) {
            // negative time used to query if lock is available!
            return FeatureLockFactory.generate( request.getHandle(), lockExpiry );
        }

        if (lockExpiry == 0) {
            // perma lock with no expiry!
            return FeatureLockFactory.generate( request.getHandle(), 0 );
        }

        // FeatureLock is specified in seconds
        return FeatureLockFactory.generate(request.getHandle(), lockExpiry * 60 * 1000);
    }
}
