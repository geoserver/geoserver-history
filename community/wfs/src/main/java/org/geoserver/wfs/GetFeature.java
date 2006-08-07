/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.wfs;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.logging.Logger;

import org.geoserver.data.GeoServerCatalog;
import org.geoserver.data.feature.AttributeTypeInfo;
import org.geoserver.data.feature.FeatureTypeInfo;
import org.geoserver.ows.ServiceException;
import org.geotools.catalog.GeoResource;
import org.geotools.data.DefaultTransaction;
import org.geotools.data.FeatureLock;
import org.geotools.data.FeatureLockFactory;
import org.geotools.data.FeatureLocking;
import org.geotools.data.FeatureReader;
import org.geotools.data.FeatureResults;
import org.geotools.data.FeatureSource;
import org.geotools.data.Transaction;
import org.geotools.feature.Feature;
import org.geotools.feature.IllegalAttributeException;
import org.geotools.filter.FidFilter;
import org.geotools.filter.FilterFactory;
import org.geotools.filter.FilterFactoryFinder;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * Implements the WFS GetFeature interface, which responds to requests for GML.
 * This servlet accepts a getFeatures request and returns GML2.0 structured
 * XML docs.  It is made up of the standard request params, plus one or  more
 * {@link Query} objects, plus a user-assigned handle.  There are also params
 * for feature versioning and alternate formats, but GeoServer does not yet
 * support those.
 *
 * @author Rob Hranac, TOPP
 * @version $Id$
 */
public class GetFeature implements ApplicationContextAware {
	
    /** Standard logging instance for class */
    private static final Logger LOGGER = Logger.getLogger(
            "org.vfny.geoserver.requests");

    /** Creates a max features constraint for the entire request */
    protected int maxFeatures = Integer.MAX_VALUE; //SOFT_MAX_FEATURES;

    /** The time to hold the lock for */
    protected int lockExpiry = 0;

    /** Specifies the output format */
    protected String outputFormat = "GML2";

    /** Specifices the user-defined name for the entire get feature request */
    protected String handle = null;

    /** Creates an object version type */
    protected String featureVersion = null;

    /** Creates a full list of queries */
    protected List queries = new ArrayList();

    /** The catalog */
    protected GeoServerCatalog catalog;
    
    /** The wfs configuration */
    protected WFS wfs;
    
    /** The output stream */
    protected OutputStream outputStream;
    
    /** Spring application context */
    protected ApplicationContext applicationContext;
    
    /**
     * Creates the GetFeature operation.
     * 
     * @param catalog The catalog.
     */
    public GetFeature( GeoServerCatalog catalog, WFS wfs ) {
    		this.catalog = catalog;
    		this.wfs = wfs;
    }
    
    /**
     * @return The reference to the GeoServer catalog.
     */
    public GeoServerCatalog getCatalog() {
		return catalog;
	}
    
    /**
     * @return The reference to the WFS configuration.
     */
    public WFS getWFS() {
    		return wfs;
    }
    
    /**
     * Sets the entire set of queries for this GetFeature request.
     *
     * @param queries The Querys of this request.
     */
    public void setQueries(List queries) {
        this.queries = queries;
    }

    /**
     * Returns a specific query from this GetFeature request.
     *
     * @param query a Query to add to this request.
     */
    public void addQuery(Query query) {
        this.queries.add(query);
    }

    /**
     * Returns the entire set of queries for this GetFeature request.
     *
     * @return The List of Query objects for this request.
     */
    public List getQueries() {
        return this.queries;
    }

    /**
     * Returns the number of queries for this GetFeature request.
     *
     * @return The number of queries held by this request.
     */
    public int getQueryCount() {
        return this.queries.size();
    }

    /**
     * Returns a specific query from this GetFeature request.
     *
     * @param i The index of the query to retrieve.
     *
     * @return The query at position i.
     */
    public Query getQuery(int i) {
        return (Query) this.queries.get(i);
    }

    /**
     * Sets the output format for this GetFeature request.
     *
     * @param outputFormat only gml2 is currently supported.
     */
    public void setOutputFormat(String outputFormat) {
        this.outputFormat = outputFormat;
    }

    /**
     * Gets the output format for this GetFeature request.
     *
     * @return "GML2"
     */
    public String getOutputFormat() {
        return this.outputFormat;
    }

    /**
     * Sets the user-defined name for this request.
     *
     * @param handle The string to be used in reporting errors.
     */
    public void setHandle(String handle) {
        this.handle = handle;
    }

    /**
     * Returns the user-defined name for the entire GetFeature request.
     *
     * @return The string to use in error reporting with this request.
     */
    public String getHandle() {
        return this.handle;
    }

    /**
     * Returns the version for the entire GetFeature request. Not currently
     * used in GeoServer.
     *
     * @param featureVersion The version of the feature to retrieve.
     */
    public void setFeatureVersion(String featureVersion) {
        this.featureVersion = featureVersion;
    }

    /**
     * Returns the version for the entire GetFeature request. Not currently
     * used in GeoServer.
     *
     * @return The version of the feature to retrieve.
     */
    public String getFeatureVersion() {
        return featureVersion;
    }

    /**
     * Sets the maximum number of features this request should return.
     *
     * @param maxFeatures The maximum number of features to return.
     */
    public void setMaxFeatures(int maxFeatures) {
        if (maxFeatures > 0) {
            this.maxFeatures = maxFeatures;
        }

        //if (maxFeatures > HARD_MAX_FEATURES) {
        //  this.maxFeatures = HARD_MAX_FEATURES;
        //}
    }

    /**
     * Parses the GetFeature reqeust and returns a contentHandler.
     *
     * @param maxFeatures The maximum number of features to return.
     */
    public void setMaxFeatures(String maxFeatures) {
        if (maxFeatures != null) {
            Integer tempInt = new Integer(maxFeatures);
            setMaxFeatures(tempInt.intValue());
        }
    }

    /**
     * Returns the maximum number of features for this request.
     *
     * @return Maximum number of features this request will return.
     */
    public int getMaxFeatures() {
        return this.maxFeatures;
    }

   /**
     * Sets the expiration of the locks (in minutes).
     *
     * @param expiry How many minutes till the lock should expire.
     */
    public void setLockExpiry( int lockExpiry ) {
    		this.lockExpiry = lockExpiry;
    }
    
    /**
     * Gets the expiration of the locks (in minutes).
     *
     * @return How many minutes till the lock should expire.
     */
    public int getLockExpiry() {
    		return lockExpiry;
    }
    
    /**
     * Sets the output stream used to write response to.
     * 
     * @param outputStream
     */
    public void setOutputStream(OutputStream outputStream) {
		this.outputStream = outputStream;
	}
    
    /**
     * Sets the application context.
     * <p>
     * This method is usually called by the spring container.
     * </p>
     */
    public void setApplicationContext(ApplicationContext applicationContext) 
    		throws BeansException {
    		this.applicationContext = applicationContext;
    }
    
    /**
     * Initializes a getFeature operation by reading the outputFormat and 
     * looking up the feature producer.
     * 
     * @return A GetFeatureResults object.
     */
    protected GetFeatureResults init() throws ServiceException {
	    	String outputFormat = getOutputFormat();
		if ( outputFormat == null ) {
			//default to GML
			outputFormat = "GML2";
		}
			
	    FeatureProducer delegate = lookupProducer( outputFormat );
	 	
	    GetFeatureResults results = new GetFeatureResults();
	    results.setFeatureProducer( delegate );
	    
	    return results;
    }
    
    protected FeatureProducer lookupProducer( String outputFormat ) 
    		throws WFSException {
    	
    		Collection producers = 
    			applicationContext.getBeansOfType( FeatureProducer.class ).values();
    		List matches = new ArrayList();
    		for ( Iterator p = producers.iterator(); p.hasNext(); ) {
    			FeatureProducer producer = (FeatureProducer) p.next();
    			if ( producer.canProduce( outputFormat ) ) 
    				matches.add( producer );
    		}
    		
    		if ( matches.isEmpty() ) {
    			String msg = 
	    			"output format: " + outputFormat + " not supported by geoserver";
	    		throw new WFSException( msg, null );
    		}
    		
    		if ( matches.size() > 1 ) {
    			String msg = 
    				"multiple produces found for output format: " + outputFormat;
    			throw new WFSException( msg, null );
    		}
    		
    		return (FeatureProducer) matches.get( 0 );
    }
    
    public void getFeatureWithLock() throws ServiceException {
		GetFeatureResults results = init();
		
		FeatureLock lock = featureLock();
		results.setFeatureLock( lock );
		
		LOGGER.finest("FeatureWithLock using Lock:" + lock.getAuthorization());
		
		run( results );
    }
    
    /**
     * Turn this request into a FeatureLock.
     * 
     * <p>
     * You will return FeatureLock.getAuthorization() to your user so they can
     * refer to this lock again.
     * </p>
     * 
     * <p>
     * The getAuthorization() value is based on getHandle(), with a default of
     * "GeoServer" if the user has not provided a handle.
     * </p>
     * The FeatureLock produced is based on expiry:
     * 
     * <ul>
     * <li>
     * negative expiry: reports if lock is available
     * </li>
     * <li>
     * zero expiry: perma lock that never expires!
     * </li>
     * <li>
     * postive expiry: lock expires in a number of minuets
     * </li>
     * </ul>
     * 
     *
     * @return
     */
    protected FeatureLock featureLock() {
      
        if ((handle == null) || (handle.length() == 0)) {
            handle = "GeoServer";
        }

        if (lockExpiry < 0) {
            // negative time used to query if lock is available!
            return FeatureLockFactory.generate(handle, lockExpiry);
        }

        if (lockExpiry == 0) {
            // perma lock with no expiry!
            return FeatureLockFactory.generate(handle, 0);
        }

        // FeatureLock is specified in seconds
        return FeatureLockFactory.generate(handle, lockExpiry * 60 * 1000);
    }

    /**
     * Performs a getFeatures, or getFeaturesWithLock (using gt2 locking ).
     * 
     * <p>
     * The idea is to grab the FeatureResulsts during execute, and use them
     * during writeTo.
     * </p>
     *
     * @param request
     *
     * @throws ServiceException
     * @throws WfsException DOCUMENT ME!
     *
     * @task TODO: split this up a bit more?  Also get the proper namespace
     *       declrations and schema locations.  Right now we're back up to
     *       where we were with 1.0., as we can return two FeatureTypes in the
     *       same namespace.  CITE didn't check for two in different
     *       namespaces, and gml builder just couldn't deal.  Now we should be
     *       able to, we just need to get the reporting right, use the
     *       AllSameType function as  Describe does.
     */
    public void getFeature() throws ServiceException {
       
    		GetFeatureResults results = init();
        
    		run( results );
    }
    
    protected void run( GetFeatureResults results) throws ServiceException {
    	
        // Optimization Idea
        //
        // We should be able to reduce this to a two pass opperations.
        //
        // Pass #1 execute
        // - Attempt to Locks Fids during the first pass
        // - Also collect Bounds information during the first pass
        //
        // Pass #2 writeTo
        // - Using the Bounds to describe our FeatureCollections
        // - Iterate through FeatureResults producing GML
        //
        // And allways remember to release locks if we are failing:
        // - if we fail to aquire all the locks we will need to fail and
        //   itterate through the the FeatureSources to release the locks
        //
        FeatureTypeInfo meta = null;
        
        Query query;
        int maxFeatures = getMaxFeatures();

        Set lockedFids = new HashSet();
        Set lockFailedFids = new HashSet();

        FeatureSource source;
        Feature feature;
        String fid;
        FilterFactory filterFactory = FilterFactoryFinder.createFilterFactory();
        FidFilter fidFilter;
        int numberLocked;

        try {
            for (Iterator it = getQueries().iterator(); it.hasNext() && (maxFeatures > 0);) {
                query = (Query) it.next();
                
                meta = featureTypeInfo( query.getTypeName() );
                source = meta.featureSource();

                List attrs = meta.getAttributes();
                List propNames = query.getPropertyNames(); // REAL LIST: be careful here :)
                List attributeNames = meta.attributeNames();

                for (Iterator iter = propNames.iterator(); iter.hasNext();) {
                    String propName = (String) iter.next();

                    if (!attributeNames.contains(propName)) {
                        String mesg = "Requested property: " + propName
                            + " is " + "not available for "
                            + query.getTypeName() + ".  "
                            + "The possible propertyName values are: "
                            + attributeNames;
                        throw new WFSException( mesg, null );
                    }
                }

                if (propNames.size() != 0) {
                    Iterator ii = attrs.iterator();
                    List tmp = new LinkedList();

                    while (ii.hasNext()) {
                        AttributeTypeInfo ati = (AttributeTypeInfo) ii.next();

                        //String attName = (String) ii.next();
                        LOGGER.finer("checking to see if " + propNames
                            + " contains" + ati);

                        if (((ati.getMinOccurs() > 0)
                                && (ati.getMaxOccurs() != 0))
                                || propNames.contains(ati.getName())) {
                            tmp.add(ati.getName());
                        }
                    }

                    query.setPropertyNames(tmp);
                }

                // This doesn't seem to be working?
                // Run through features and record FeatureIDs
                // Lock FeatureIDs as required
                //}
                LOGGER.fine("Query is " + query + "\n To gt2: "
                    + query.toDataQuery(maxFeatures));

                //DJB: note if maxFeatures gets to 0 the while loop above takes care of this! (this is a subtle situation)
                
                FeatureResults features = source.getFeatures(query.toDataQuery(
                            maxFeatures));
                if (it.hasNext()) //DJB: dont calculate feature count if you dont have to. The MaxFeatureReader will take care of the last iteration
                	maxFeatures -= features.getCount();

                //GR: I don't know if the featuresults should be added here for later
                //encoding if it was a lock request. may be after ensuring the lock
                //succeed?
                results.addFeatures(meta, features);

                if (results.getFeatureLock() != null) {
                		FeatureLock featureLock = results.getFeatureLock();
                    // geotools2 locking code
                    if (source instanceof FeatureLocking) {
                        ((FeatureLocking) source).setFeatureLock(featureLock);
                    }

                    FeatureReader reader = null;

                    try {
                        for (reader = features.reader(); reader.hasNext();) {
                            feature = reader.next();
                            fid = feature.getID();

                            if (!(source instanceof FeatureLocking)) {
                                LOGGER.finest("Lock " + fid
                                    + " not supported by data store (authID:"
                                    + featureLock.getAuthorization() + ")");
                                lockFailedFids.add(fid);

                                continue; // locking is not supported!
                            } else {
                                fidFilter = filterFactory.createFidFilter(fid);
                                numberLocked = ((FeatureLocking) source)
                                    .lockFeatures(fidFilter);

                                if (numberLocked == 1) {
                                    LOGGER.finest("Lock " + fid + " (authID:"
                                        + featureLock.getAuthorization() + ")");
                                    lockedFids.add(fid);
                                } else if (numberLocked == 0) {
                                    LOGGER.finest("Lock " + fid
                                        + " conflict (authID:"
                                        + featureLock.getAuthorization() + ")");
                                    lockFailedFids.add(fid);
                                } else {
                                    LOGGER.warning("Lock " + numberLocked + " "
                                        + fid + " (authID:"
                                        + featureLock.getAuthorization()
                                        + ") duplicated FeatureID!");
                                    lockedFids.add(fid);
                                }
                            }
                        }
                    } finally {
                        if (reader != null) {
                            reader.close();
                        }
                    }

                    if (!lockedFids.isEmpty()) {
                        Transaction t = new DefaultTransaction();

                        try {
                            t.addAuthorization(featureLock.getAuthorization());
                            source.getDataStore().getLockingManager().refresh(featureLock
                                .getAuthorization(), t);
                        } finally {
                            t.commit();
                        }
                    }
                }
            }

            if ((results.getFeatureLock() != null) && !lockFailedFids.isEmpty()) {
                // I think we need to release and fail when lockAll fails
                //
                // abort will take care of releasing the locks
                throw new WFSException(
                		"Could not aquire locks for:" + lockFailedFids, null 
            		);
            }
            
            FeatureProducer delegate = results.getFeatureProducer();
            try {
            		delegate.produce( outputFormat, results, outputStream );	
            }
            catch( Throwable t ) {
            		String msg = "Error occured producing features";
            		throw new WFSException( msg, t, null );
            }
            
        
        } 
        catch (IOException e) {
        		throw new ServiceException(
    				"problem with FeatureResults", e, getHandle()
        		);
        } 
        catch (NoSuchElementException e) {
            throw new ServiceException(
            		"problem with FeatureResults", e, getHandle()
        		);
        } catch (IllegalAttributeException e) {
            throw new ServiceException(
            		"problem with FeatureResults", e, getHandle()
        		);
        }
    }
    
    //JD: move this method to catalog
    FeatureTypeInfo featureTypeInfo( String name ) throws WFSException, IOException {
    	
    		List resources = catalog.resources( FeatureTypeInfo.class );
    		List metas = new ArrayList();
    		
    		for ( Iterator r = resources.iterator(); r.hasNext(); ) {
    			GeoResource resource = (GeoResource) r.next();
    			FeatureTypeInfo meta = 
    				(FeatureTypeInfo) resource.resolve( FeatureTypeInfo.class, null );
    			if ( name.equals( meta.getTypeName() ) )
    				metas.add( meta );
    			
    		}
    		
    		if ( metas.isEmpty() ) {
        		String msg = "Could not locate " + name + " in catalog.";
        		throw new WFSException( msg, null );
        }
        if ( metas.size() > 1 ) {
        		String msg = "Found multiple entries under " + name + " in catalog.";
        		throw new WFSException( msg, null );	
        }	
        
        return (FeatureTypeInfo) metas.get( 0 );
    }
    
    /**
     * Standard override of toString()
     *
     * @return a String representation of this request.
     */
    public String toString() {
        StringBuffer returnString = new StringBuffer("\nRequest");
        returnString.append(": " + handle);
        returnString.append("\n output format:" + outputFormat);
        returnString.append("\n max features:" + maxFeatures);
        returnString.append("\n version:" + featureVersion);
        returnString.append("\n queries: ");

        Iterator iterator = queries.listIterator();

        while (iterator.hasNext()) {
            returnString.append(iterator.next().toString() + " \n");
        }

        //returnString.append("\n inside: " + filter.toString());
        return returnString.toString();
    }

    /**
     * DOCUMENT ME!
     *
     * @param obj DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean equals(Object obj) {
        super.equals(obj);

        if (!(obj instanceof GetFeature)) {
            return false;
        }

        GetFeature frequest = (GetFeature) obj;
        boolean isEqual = true;

        if ((this.featureVersion == null) && (frequest.getFeatureVersion() == null)) {
            isEqual = isEqual && true;
        } else if ((this.featureVersion == null) || (frequest.getFeatureVersion() == null)) {
            isEqual = false;
        } else if (frequest.getFeatureVersion().equals(featureVersion)) {
            isEqual = isEqual && true;
        } else {
            isEqual = false;
        }

        LOGGER.finest("checking version equality: " + isEqual);

        if ((this.handle == null) && (frequest.getHandle() == null)) {
            isEqual = isEqual && true;
        } else if ((this.handle == null) || (frequest.getHandle() == null)) {
            isEqual = false;
        } else if (frequest.getHandle().equals(handle)) {
            isEqual = isEqual && true;
        } else {
            isEqual = false;
        }

        LOGGER.finest("checking handle equality: " + isEqual);

        if ((this.outputFormat == null) && (frequest.getOutputFormat() == null)) {
            isEqual = isEqual && true;
        } else if ((this.outputFormat == null)
                || (frequest.getOutputFormat() == null)) {
            isEqual = false;
        } else if (frequest.getOutputFormat().equals(outputFormat)) {
            isEqual = isEqual && true;
        } else {
            isEqual = false;
        }

        LOGGER.finest("checking output format equality: " + isEqual);

        if (this.maxFeatures == frequest.getMaxFeatures()) {
            isEqual = isEqual && true;
        }

        LOGGER.finest("checking max features equality: " + isEqual);

        ListIterator internalIterator = queries.listIterator();
        ListIterator externalIterator = frequest.getQueries().listIterator();

        while (internalIterator.hasNext()) {
            if (!externalIterator.hasNext()) {
                isEqual = false;
                LOGGER.finest("query lists not same size");

                break;
            } else {
                if (((Query) internalIterator.next()).equals(
                            (Query) externalIterator.next())) {
                    isEqual = true && isEqual;
                    LOGGER.finest("query properties match: " + isEqual);
                } else {
                    isEqual = false;
                    LOGGER.finest("queries not equal");

                    break;
                }
            }
        }

        return isEqual;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int hashCode() {
        int result = super.hashCode();
        result = (23 * result) + ((queries == null) ? 0 : queries.hashCode());
        result = (23 * result) + ((handle == null) ? 0 : handle.hashCode());

        return result;
    }
}
