/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.wfs;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;
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

import javax.xml.namespace.QName;

import net.opengis.wfs.WFSFactory;
import net.opengis.wfs.WFSPackage;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EFactory;
import org.eclipse.emf.ecore.EObject;
import org.geoserver.data.GeoServerCatalog;
import org.geoserver.data.feature.AttributeTypeInfo;
import org.geoserver.data.feature.FeatureTypeInfo;
import org.geoserver.ows.ServiceException;
import org.geoserver.wfs.http.TypeNameKvpReader;
import org.geotools.catalog.GeoResource;
import org.geotools.data.DefaultTransaction;
import org.geotools.data.FeatureLock;
import org.geotools.data.FeatureLockFactory;
import org.geotools.data.FeatureLocking;
import org.geotools.data.FeatureReader;
import org.geotools.data.FeatureResults;
import org.geotools.data.FeatureSource;
import org.geotools.data.Transaction;
import org.geotools.data.crs.ForceCoordinateSystemFeatureResults;
import org.geotools.data.crs.ReprojectFeatureResults;
import org.geotools.feature.Feature;
import org.geotools.feature.FeatureType;
import org.geotools.feature.IllegalAttributeException;
import org.geotools.feature.SchemaException;
import org.geotools.filter.FidFilter;
import org.geotools.filter.Filter;
import org.geotools.filter.FilterFactory;
import org.geotools.filter.FilterFactoryFinder;
import org.geotools.resources.Utilities;
import org.opengis.filter.FeatureId;
import org.opengis.filter.expression.PropertyName;
import org.opengis.filter.spatial.BBOX;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.operation.OperationNotFoundException;
import org.opengis.referencing.operation.TransformException;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.vividsolutions.jts.geom.Envelope;

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

    /** The request object */
    protected EObject request;
    
    /** The model factory */
    protected EFactory factory;
    
    /** The time to hold the lock for */
    protected int lockExpiry = 0;

      /** feature id filters */
    protected List featureId;
    
    /** filters */
    protected List filter;
    
    /** property names */
    protected List propertyName;
   
    /** bounding box */
    protected Envelope bbox;
    
    /** The catalog */
    protected GeoServerCatalog catalog;
    
    /** The wfs configuration */
    protected WFS wfs;
    
    /** filter factory */
    protected FilterFactory filterFactory;
    
    /** Spring application context */
    protected ApplicationContext applicationContext;
    
    /**
     * Creates the GetFeature operation.
     * 
     */
    public GetFeature( WFS wfs, GeoServerCatalog catalog) {
		this( wfs, catalog, WFSFactory.eINSTANCE );
    }
    
    /**
     * Creates the GetFeature operation supplied the efactory used to create
     * model objects.
     * 
     */
    public GetFeature( WFS wfs, GeoServerCatalog catalog, EFactory factory ) {
    	this.wfs = wfs;
    	this.catalog = catalog;
    	this.factory = factory;
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
     * Sets the filter factory to use to create filters.
     * 
     * @param filterFactory
     */
    public void setFilterFactory(FilterFactory filterFactory) {
		this.filterFactory = filterFactory;
	}
    
    /**
     * Sets the output format for this GetFeature request.
     */
    public void setOutputFormat(String outputFormat) {
        set( "outputFormat", outputFormat );
    }

    /**
     * @return the output format for this GetFeature request.
     */
    public String getOutputFormat() {
    	return (String) get( "outputFormat" );
	}

    /**
     * Sets the user-defined name for this request.
     *
     * @param handle The string to be used in reporting errors.
     */
    public void setHandle(String handle) {
    	set( "handle", handle );
    }
    
    /**
     * Returns the user-defined name for the entire GetFeature request.
     *
     * @return The string to use in error reporting with this request.
     */
    public String getHandle() {
    	return (String) get( "handle" );
    }
    
    /**
     * Sets the maximum number of features this request should return.
     *
     * @param maxFeatures The maximum number of features to return.
     */
    public void setMaxFeatures( BigInteger maxFeatures ) {
        set( "maxFeatures", maxFeatures );
    }

    /**
     * Returns the maximum number of features for this request.
     *
     * @return Maximum number of features this request will return.
     */
    public BigInteger getMaxFeatures() {
        return (BigInteger) get( "maxFeatures" );
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
  
    public void setTypeName(List typeName) throws WFSException {
		batchSet( "typeName", typeName );
	}
    
    public void setFilter( List filter ) throws WFSException {
		batchSet( "filter", filter );
	}
    
    public void setPropertyName(List propertyName) throws WFSException {
		batchSet( "propertyName", propertyName );
	}
    
    public void setBBOX(Envelope bbox) {
		this.bbox = bbox;
	}
    
    public Envelope getBBOX() {
		return bbox;
	}
    
    
    protected Object get( String property ) {
    	return EMFUtils.get( request(), property );
    }
    
    protected void set( String property, Object value ) {
    	EMFUtils.set( request(), property, value );
    }
    
    protected void batchSet( String property, List values ) throws WFSException {
		EObject request = request();
		EList queries = (EList) EMFUtils.get( request, "query" );
		
		int m = values.size();
		int n = queries.size();
		
		if ( m == 1 && n > 1 ) {
			//apply single value to all queries
			EMFUtils.set( queries, property, values.get( 0 ) );
			return;
		}
		
		//match up sizes
		if ( m > n ) {
			
			if ( n == 0 ) {
				//make same size, with empty objects
				for ( int i = 0; i < m; i++ ) {
					queries.add( query() );
				}
			}
			else if ( n == 1 ) {
				//clone single object up to 
				EObject query = (EObject) queries.get( 0 );
				for ( int i = 1; i < m; i++ ) {
					queries.add( EMFUtils.clone( query, factory ) );
				}
				return;
			}
			else {
				//illegal
    			String msg = "Specified " + m + " " + property + " for " + n + " queries.";
    			throw new WFSException( msg );	
			}
		}
		
		EMFUtils.set( queries, property, values );
	}
    
    protected EObject request() {
		if ( request == null ) {
			request = factory.create( WFSPackage.eINSTANCE.getGetFeatureType() );
		}
		
		return request;
    }
    
    protected EObject query() {
    	return factory.create( WFSPackage.eINSTANCE.getQueryType() );
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
    	//build up the queries
		EList queries = (EList) EMFUtils.get( request(), "query" );
		
		if ( queries.isEmpty() ) {
			String msg = "No query specified";
			throw new WFSException( msg );
		}
			
		if ( EMFUtils.isUnset( queries, "typeName" ) ) {
			//no type names specified, try to infer from feature ids
			if ( featureId != null ) {
				ArrayList typeNames = new ArrayList();
				TypeNameKvpReader reader = new TypeNameKvpReader( catalog );
				for ( Iterator i = featureId.iterator(); i.hasNext(); ) {
					String fid = (String) i.next();
					int pos = fid.indexOf(".");
	                if ( pos != -1 ) {
                		String typeName = fid.substring( 0, fid.lastIndexOf( "." ) );
                		typeNames.add( reader.qName( typeName ) );
	                }
				}
				
				EMFUtils.set( queries, "typeName", typeNames );
			}
		}
		
		if ( EMFUtils.isUnset( queries, "typeName") ) {
			String msg = "No feature types specified";
			throw new WFSException( msg );
		}
		
		//make sure not both featureid and filter specified
		if ( featureId != null && EMFUtils.isSet( queries, "filter" ) ) {
			String msg = "featureid and filter both specified but are mutually exclusive";
			throw new WFSException( msg );
		}
		//make sure not both featureid and bbox specified
		if ( featureId != null && bbox != null ) {
			String msg = "featureid and bbox both specified but are mutually exclusive";
			throw new WFSException( msg );
		}
		//make sure not both filter and bbox specified
		if ( bbox != null && EMFUtils.isSet( queries, "filter") ) {
			String msg = "bbox and filter both specified but are mutually exclusive";
			throw new WFSException( msg );
		}
		
		if ( featureId != null ) {
			//set filters to fids
			List filters = new ArrayList();
			
			for ( Iterator i = featureId.iterator(); i.hasNext(); ) {
				String fid = (String) i.next();
				Set fids = new HashSet();
				fids.add( fid );
				
				FeatureId filter = filterFactory.featureId( fids );
				filters.add( filter );
			}
			
			EMFUtils.set( queries, "filter", filters );
		}
		
		if ( bbox != null ) {
			List filters = new ArrayList();
			for ( Iterator q = queries.iterator(); q.hasNext(); ) {
				EObject query = (EObject) q.next();
				QName typeName = (QName) EMFUtils.get( query, "typeName" );
				
				FeatureTypeInfo featureTypeInfo = null;
				FeatureType featureType = null;
				try {
					featureTypeInfo = 
						catalog.featureType( typeName.getPrefix(), typeName.getLocalPart() );
					featureType = featureTypeInfo.featureType();
				}
				catch( IOException e ) {
					String msg = "Unable to load feature type: " + typeName;
					throw new WFSException( msg, e );
				}
				
				String name = featureType.getDefaultGeometry().getName();
				BBOX filter = filterFactory.bbox( 
					name, bbox.getMinX(), bbox.getMinY(), bbox.getMaxX(), bbox.getMaxY(), null	
				);
				filters.add( filter );
			}
			
			EMFUtils.set( queries, "filter", filters );
		}
		
		GetFeatureResults results = new GetFeatureResults();
	    for ( Iterator q = queries.iterator(); q.hasNext(); ) {
	    	EObject query = (EObject) q.next();
    		results.addQuery( query( query ) );
	    }
	    
		//look up an output format, default to GML
	    String outputFormat = getOutputFormat();
		if ( outputFormat == null ) {
			//default to GML
			outputFormat = "GML2";
		}
//		FeatureProducer delegate = lookupProducer( outputFormat );
//		results.setFeatureProducer( delegate );
	
//	
//		for ( int i = 0; i < types.size(); i++ ) {
//			QName featureType = (QName) types.get( i );
//			List properties = null;
//            Filter f = null;
//            
//            // permissive logic: lets one property list apply to all types
//            LOGGER.finest("setting properties: " + i);
//
//            if ( propertyName == null || propertyName.isEmpty() ) {
//                properties = null;
//            }
//            else if ( propertyName.size() == 1 ) {
//                properties = (List) propertyName.get( 0 );
//            } 
//            else {
//                properties = (List) propertyName.get( i );
//            }
//
//            // permissive logic: lets one filter apply to all types
//            LOGGER.finest("setting filters: " + i);
//
//            if ( filter == null || filter.isEmpty() ) {
//                f = null;
//            } 
//            else if ( filter.size() == 1 ) {
//                f = (Filter) filter.get( 0 );
//            } 
//            else {
//                f = (Filter) filter.get( i );
//            }
//            
//            results.addQuery( query( featureType, properties, f ) );
//		}
//		
		
	    return results;
    }
    
    protected Query query( EObject q ) {
		Query query = new Query();
    		
        query.setTypeName( (QName) EMFUtils.get( q, "typeName" ) );
        
        List propertyNames = (List) EMFUtils.get( q, "propertyName" );
        for (int i = 0; i < propertyNames.size(); i++) {
    		PropertyName propertyName = (PropertyName) propertyNames.get( i );
            query.addPropertyName( propertyName.getPropertyName() );
        }
        
        Filter filter = (Filter) EMFUtils.get( q, "filter" );
        if ( filter != null) {
            query.addFilter( filter );
        }

        return query;	
    }
    
   
    
//    public void getFeatureWithLock() throws ServiceException {
//		GetFeatureResults results = init();
//		
//		FeatureLock lock = featureLock();
//		results.setFeatureLock( lock );
//		
//		LOGGER.finest("FeatureWithLock using Lock:" + lock.getAuthorization());
//		
//		run( results );
//    }
    
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
//    protected FeatureLock featureLock() {
//      
//        if ((handle == null) || (handle.length() == 0))	 {
//            handle = "GeoServer";
//        }
//
//        if (lockExpiry < 0) {
//            // negative time used to query if lock is available!
//            return FeatureLockFactory.generate(handle, lockExpiry);
//        }
//
//        if (lockExpiry == 0) {
//            // perma lock with no expiry!
//            return FeatureLockFactory.generate(handle, 0);
//        }
//
//        // FeatureLock is specified in seconds
//        return FeatureLockFactory.generate(handle, lockExpiry * 60 * 1000);
//    }

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
    public GetFeatureResults getFeature() throws ServiceException {
       return getFeature( request() );
    }
    
    public GetFeatureResults getFeature( EObject request ) throws ServiceException {
		this.request = request;
    		
		GetFeatureResults results = init();
        
		run( results );
		
		return results;
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
        int maxFeatures = Integer.MAX_VALUE; 
        if ( EMFUtils.get( request, "maxFeatures") != null ) {
    		maxFeatures = ((BigInteger) EMFUtils.get( request, "maxFeatures") ).intValue();
        }

        Set lockedFids = new HashSet();
        Set lockFailedFids = new HashSet();

        FeatureSource source;
        Feature feature;
        String fid;
        FilterFactory filterFactory = FilterFactoryFinder.createFilterFactory();
        FidFilter fidFilter;
        int numberLocked;

        try {
            for (Iterator it = results.getQueries().iterator(); it.hasNext() && (maxFeatures > 0);) {
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
                        throw new WFSException( mesg );
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
                
                org.geotools.data.Query gtQuery = query.toDataQuery( maxFeatures );
                FeatureResults features = source.getFeatures();
                if (it.hasNext()) //DJB: dont calculate feature count if you dont have to. The MaxFeatureReader will take care of the last iteration
                	maxFeatures -= features.getCount();

                //JD: reproject if neccessary
                if ( gtQuery.getCoordinateSystemReproject() != null ) {
                	try {
						features = new ReprojectFeatureResults( features, gtQuery.getCoordinateSystemReproject() );
					} 
                	catch ( Exception e ) {
                		String msg = "Unable to reproject features";
                		throw new WFSException( msg, e );
					} 
                }
                
                //JD: override crs if neccessary
                if ( gtQuery.getCoordinateSystem() != null ) {
                	try {
						features = new ForceCoordinateSystemFeatureResults( features, gtQuery.getCoordinateSystem() );
					} 
            		catch (Exception e) {
						String msg = "Unable to set coordinate system";
						throw new WFSException( msg, e );
					}
                }
                
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
                throw new WFSException( "Could not aquire locks for:" + lockFailedFids );
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
    
    FeatureTypeInfo featureTypeInfo( QName name ) throws WFSException, IOException {
    	
	    	FeatureTypeInfo meta = 
	    		catalog.featureType( name.getPrefix(), name.getLocalPart() );
	    	
    		if ( meta == null ) {
        		String msg = "Could not locate " + name + " in catalog.";
        		throw new WFSException( msg );
        }
    		
        return meta;
    }
    
    /**
     * Standard override of toString()
     *
     * @return a String representation of this request.
     */
    public String toString() {
        StringBuffer returnString = new StringBuffer("\nRequest");
        returnString.append(": " + getHandle() );
        returnString.append("\n output format:" + getOutputFormat() );
        returnString.append("\n max features:" + getMaxFeatures() );
        
        returnString.append("\n queries: ");

        return returnString.toString();
    }

}
