/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.wfs;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.logging.Logger;

import javax.xml.namespace.QName;

import net.opengis.wfs.AllSomeType;
import net.opengis.wfs.GetFeatureType;
import net.opengis.wfs.LockType;
import net.opengis.wfs.WFSFactory;
import net.opengis.wfs.WFSLockFeatureResponseType;
import net.opengis.wfs.WFSPackage;

import org.eclipse.emf.ecore.EFactory;
import org.eclipse.emf.ecore.EObject;
import org.geoserver.data.GeoServerCatalog;
import org.geoserver.data.feature.AttributeTypeInfo;
import org.geoserver.data.feature.DataStoreInfo;
import org.geoserver.data.feature.FeatureTypeInfo;
import org.geoserver.ows.ServiceException;
import org.geoserver.wfs.http.TypeNameKvpReader;
import org.geotools.data.DefaultTransaction;
import org.geotools.data.FeatureLock;
import org.geotools.data.FeatureLockFactory;
import org.geotools.data.FeatureLocking;
import org.geotools.data.FeatureReader;
import org.geotools.data.FeatureResults;
import org.geotools.data.FeatureSource;
import org.geotools.data.LockingManager;
import org.geotools.data.Transaction;
import org.geotools.data.crs.ForceCoordinateSystemFeatureResults;
import org.geotools.data.crs.ReprojectFeatureResults;
import org.geotools.feature.Feature;
import org.geotools.feature.FeatureType;
import org.geotools.feature.IllegalAttributeException;
import org.geotools.filter.FidFilter;
import org.geotools.filter.Filter;
import org.geotools.filter.FilterFactory;
import org.geotools.filter.FilterFactoryFinder;
import org.opengis.filter.FeatureId;
import org.opengis.filter.expression.PropertyName;
import org.opengis.filter.spatial.BBOX;
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

    /** The model factory */
    protected EFactory factory;
    
    /** The time to hold the lock for */
    protected BigInteger expiry;

    /** The max features */
    protected BigInteger maxFeatures;

    /** The well known name given to the query */
    protected String handle;
    
    /** The output format of the response */
    protected String outputFormat = "GML2";
    
    /** feature id filters */
    protected List featureId;
    
    /** bounding box */
    protected Envelope bbox;
   
    /** queries */
    protected List query;
    
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
        this.outputFormat = outputFormat;
    }

    /**
     * @return the output format for this GetFeature request.
     */
    public String getOutputFormat() {
    	return outputFormat;
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
    	return handle;
    }
    
    /**
     * Sets the maximum number of features this request should return.
     *
     * @param maxFeatures The maximum number of features to return.
     */
    public void setMaxFeatures( BigInteger maxFeatures ) {
        this.maxFeatures = maxFeatures;
    }

    /**
     * Returns the maximum number of features for this request.
     *
     * @return Maximum number of features this request will return.
     */
    public BigInteger getMaxFeatures() {
        return maxFeatures;
    }

   /**
     * Sets the expiration of the locks (in minutes).
     *
     * @param expiry How many minutes till the lock should expire.
     */
    public void setExpiry( BigInteger expiry ) {
		this.expiry = expiry;
    }
    
    /**
     * Gets the expiration of the locks (in minutes).
     *
     * @return How many minutes till the lock should expire.
     */
    public BigInteger getExpiry() {
		return expiry;
    }
  
    public void setTypeName(List typeName) throws WFSException {
		querySet( "typeName", typeName );
	}
    
    public void setFilter( List filter ) throws WFSException {
		querySet( "filter", filter );
	}
    
    public void setPropertyName(List propertyName) throws WFSException {
		querySet( "propertyName", propertyName );
	}
    
    public void setBBOX(Envelope bbox) {
		this.bbox = bbox;
	}
    
    public Envelope getBBOX() {
		return bbox;
	}
    
    public void setQuery( List query ) {
    	this.query = query;
    }
    
    protected void querySet( String property, List values ) throws WFSException {
		
    	//no values specified, do nothing
    	if ( values == null ) 
    		return;
    	
    	if ( query == null ) 
			query = new ArrayList();
		
		int m = values.size();
		int n = query.size();
		
		if ( m == 1 && n > 1 ) {
			//apply single value to all queries
			EMFUtils.set( query, property, values.get( 0 ) );
			return;
		}
		
		//match up sizes
		if ( m > n ) {
			
			if ( n == 0 ) {
				//make same size, with empty objects
				for ( int i = 0; i < m; i++ ) {
					query.add( query() );
				}
			}
			else if ( n == 1 ) {
				//clone single object up to 
				EObject q = (EObject) query.get( 0 );
				for ( int i = 1; i < m; i++ ) {
					query.add( EMFUtils.clone( q, factory ) );
				}
				return;
			}
			else {
				//illegal
    			String msg = "Specified " + m + " " + property + " for " + n + " queries.";
    			throw new WFSException( msg );	
			}
		}
		
		EMFUtils.set( query, property, values );
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
    	
    	if ( query == null || query.isEmpty() ) {
    		String msg = "No query specified";
			throw new WFSException( msg );
		}
			
		if ( EMFUtils.isUnset( query, "typeName" ) ) {
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
				
				EMFUtils.set( query, "typeName", typeNames );
			}
		}
		
		if ( EMFUtils.isUnset( query, "typeName") ) {
			String msg = "No feature types specified";
			throw new WFSException( msg );
		}
		
		//make sure not both featureid and filter specified
		if ( featureId != null && EMFUtils.isSet( query, "filter" ) ) {
			String msg = "featureid and filter both specified but are mutually exclusive";
			throw new WFSException( msg );
		}
		//make sure not both featureid and bbox specified
		if ( featureId != null && bbox != null ) {
			String msg = "featureid and bbox both specified but are mutually exclusive";
			throw new WFSException( msg );
		}
		//make sure not both filter and bbox specified
		if ( bbox != null && EMFUtils.isSet( query, "filter") ) {
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
			
			EMFUtils.set( query, "filter", filters );
		}
		
		if ( bbox != null ) {
			List filters = new ArrayList();
			for ( Iterator q = query.iterator(); q.hasNext(); ) {
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
			
			EMFUtils.set( query, "filter", filters );
		}
		
		GetFeatureResults results = new GetFeatureResults();
	    for ( Iterator q = query.iterator(); q.hasNext(); ) {
	    	EObject query = (EObject) q.next();
    		results.addQuery( query( query ) );
	    }
	    
		//look up an output format, default to GML
	    String outputFormat = getOutputFormat();
		if ( outputFormat == null ) {
			//default to GML
			outputFormat = "GML2";
		}
		
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
    	GetFeatureResults results = init();
        
		run( results );
		
		return results;
    }
    
    public GetFeatureResults getFeatureWithLock() throws ServiceException {
		GetFeatureResults results = init();
		results.setLocking( true );
		
		try {
			run( results );	
		}
		catch( Exception e ) {
			//free locks
			LockFeature lockFeature = new LockFeature( wfs, catalog );
			if ( results.getLockId() != null ) {
				lockFeature.setLockId( results.getLockId() );
				lockFeature.release();
			}
		
			throw new WFSException( e );
		}
		
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
        if ( this.maxFeatures != null ) {
    		maxFeatures = this.maxFeatures.intValue();
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
                FeatureResults features = source.getFeatures( gtQuery );
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
                if ( results.isLocking() ) {
                	LockFeature lockFeature = new LockFeature( wfs, catalog );
                	lockFeature.setFilterFactory( filterFactory );
                    lockFeature.setExpiry( expiry );
                    lockFeature.setHandle( handle );
                    lockFeature.setLockAction( AllSomeType.ALL_LITERAL );
                    
                	LockType lockType = WFSFactory.eINSTANCE.createLockType();
                	lockType.setFilter( query.getFilter() );
                	lockType.setHandle( query.getHandle() );
                	lockType.setTypeName( query.getTypeName() );
                	
                	ArrayList lock = new ArrayList();
                	lock.add( lockType );
                	lockFeature.setLock( lock );
                	
                	WFSLockFeatureResponseType response = lockFeature.lockFeature();
                	results.setLockId( response.getLockId() );

                }
            }
            
        } 
        catch (IOException e) {
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
