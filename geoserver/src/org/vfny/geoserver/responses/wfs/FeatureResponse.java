/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.responses.wfs;

import org.geotools.data.*;
import org.geotools.feature.*;
import org.geotools.filter.FidFilter;
import org.geotools.filter.FilterFactory;
import org.geotools.gml.producer.*;
import org.geotools.gml.producer.FeatureTransformer.FeatureTypeNamespaces;
import org.vfny.geoserver.*;
import org.vfny.geoserver.config.*;
import org.vfny.geoserver.requests.*;
import org.vfny.geoserver.requests.Query;
import org.vfny.geoserver.requests.wfs.*;
import org.vfny.geoserver.responses.*;

import java.io.*;
import java.util.*;
import java.util.logging.*;

import javax.xml.transform.TransformerException;


/**
 * Handles a Get Feature request and creates a Get Feature response GML string.
 *
 * @author Chris Holmes, TOPP
 * @author Jody Garnett, Refractions Research
 * @version $Id: FeatureResponse.java,v 1.1.2.14 2003/11/14 23:54:16 cholmesny Exp $
 */
public class FeatureResponse implements Response {
    /** Standard logging instance for class */
    private static final Logger LOGGER = Logger.getLogger(
            "org.vfny.geoserver.responses");
    
    // set by execute, used by write
    /**
     * This is a "magic" class provided by Geotools that writes out
     * GML for an array of FeatureResults.
     * <p>
     * This class seems to do all the work, if you have a problem with GML
     * you will need to hunt it down. We supply all of the header information
     * in the execute method, and work through the featureList in the writeTo
     * method. 
     * </p>
     * <p>
     * This value will be <code>null</code> until execute is called.
     * </p>
     */
    private FeatureTransformer transformer;
    /**
     * This is the request provided to the execute( Request ) method.
     * <p>
     * We save it so we can access the handle provided by the user for
     * error reporting during the writeTo( OutputStream ) opperation.
     * </p>
     * <p>
     * This value will be <code>null</code> until execute is called.
     * </p>
     */    
    private FeatureRequest request;
    /**
     * This is the resultList provided to the execute( Request ) method.
     * <p>
     * We save it so we can access the handle provided by the user for
     * error reporting during the writeTo( OutputStream ) opperation.
     * </p>
     * <p>
     * This value will be <code>null</code> until execute is called.
     * </p>
     */    
    private List resultList;
    /**
     * This is the FeatureLock provided by execute( Request ) method.
     * <p>
     * This will only be non null if RequestFeatureWithLock.
     * </p>
     */
    FeatureLock featureLock;    
    /**
     * Empty constructor
     */
    public FeatureResponse() {
        request = null;
        resultList = null;
        transformer = null;
        featureLock = null;        
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getContentType() {
        return ServerConfig.getInstance().getGlobalConfig().getMimeType();
    }

    /**
     * Jody here with one pass replacement for writeTo.
     * <p>
     * This code is a discussion point, when everyone has had there input
     * we will try and set things up properly.
     * </p>
     * <p>
     * I am providing a mirror of the existing desing:
     * - execute gathers the resultList
     * - sets up the header
     */
    public void writeTo(OutputStream out) throws ServiceException {
        if( request == null || resultList == null){
            throw new IllegalStateException(
                "execute has not been called prior to writeTo" );            
        }
        // execute should of set all the header information
        // including the lockID
        //
        // execute should also fail if all of the locks could not be aquired
        FeatureResults featureResults[] = (FeatureResults[])
            resultList.toArray(new FeatureResults[resultList.size()]);
        
        try {
            transformer.transform( featureResults, out );        
        }
        catch( TransformerException gmlException){
            ServiceException serviceException =
                new ServiceException( request.getHandle()+" error:"+gmlException.getMessage() );
            serviceException.initCause( gmlException );
            
            throw serviceException;                
        }
    }
    
    /**
     * Executes FeatureRequest.
     * <p>
     * Willing to execute a FetureRequest, or FeatureRequestWith Lock.
     * </p>
     */
    public void execute(Request req) throws ServiceException {
        execute( (FeatureRequest) req );
    }    

    
    /**
     * Performs a getFeatures, or getFeaturesWithLock (using gt2 locking ).
     * <p>
     * The idea is to grab the FeatureResulsts during execute, and use them
     * during writeTo.
     * </p>
     * @task TODO: split this up a bit more?  Also get the proper namespace
     *       declrations and schema locations.  Right now we're back up to
     *       where we were with 1.0., as we can return two FeatureTypes in the
     *       same namespace.  CITE didn't check for two in different
     *       namespaces, and gml builder just couldn't deal.  Now we should be
     *       able to, we just need to get the reporting right, use the
     *       AllSameType function as  Describe does.
     * @param req
     * @throws ServiceException
     */
    public void execute( FeatureRequest request ) throws ServiceException {
        LOGGER.finest("execute FeatureRequest response. Called request is: " + request);
	this.request = request;
        if( request instanceof FeatureWithLockRequest){
            featureLock = ((FeatureWithLockRequest)request).toFeatureLock();
            String authorization = featureLock.getAuthorization();        
            LOGGER.finest("FeatureWithLock using Lock:"+authorization );
        }
        
        //
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
        CatalogConfig catalog = ServerConfig.getInstance().getCatalog();        
        FeatureTypeConfig meta = null;
        NameSpace namespace;       
        Query query;
        int maxFeatures = request.getMaxFeatures();

        StringBuffer typeNames = new StringBuffer();
        Set lockedFids = new HashSet();
        Set lockFailedFids = new HashSet();
        
        FeatureLocking source;        
        Feature feature;
        String fid;
        FilterFactory filterFactory = FilterFactory.createFilterFactory();
        FidFilter fidFilter;
        int numberLocked;   
        
        transformer = new FeatureTransformer();
        FeatureTypeNamespaces ftNames = transformer.getFeatureTypeNamespaces();
        List results = new ArrayList();
        try {                   
            for (Iterator it = request.getQueries().iterator();
                       it.hasNext() && (maxFeatures > 0);) {
                      
                query = (Query) it.next();                                                                   
                meta = catalog.getFeatureType( query.getTypeName() );
                namespace = meta.getDataStore().getNameSpace();                
                source = (FeatureLocking) meta.getFeatureSource();
        
                typeNames.append( query.getTypeName() );
                if (it.hasNext() && (maxFeatures > 0)) {
                    typeNames.append(",");
                }
                                
                // This doesn't seem to be working?
                ftNames.declareNamespace( source.getSchema(), namespace.getPrefix(), namespace.getUri() );
                
                // Run through features and record FeatureIDs
                // Lock FeatureIDs as required
                FeatureResults features = source.getFeatures( query.toDataQuery( maxFeatures ) );
                maxFeatures -= features.getCount();
                results.add( features );
                
                if( featureLock != null){
                    // geotools2 locking code
                    source.setFeatureLock( featureLock );
                    FeatureReader reader = null;
                    try {                    
                        for( reader= features.reader(); reader.hasNext(); ){
                            feature = reader.next();
                            fid = feature.getID();
                                            
                            fidFilter = filterFactory.createFidFilter( fid );
                            numberLocked = source.lockFeatures( fidFilter );
                        
                            if( numberLocked == 1){
                                LOGGER.finest("Lock "+fid+" (authID:"+featureLock.getAuthorization()+")" );
                                lockedFids.add( fid );
                            }
                            else if ( numberLocked == 0 ){
                                LOGGER.finest("Lock "+fid+" conflict (authID:"+featureLock.getAuthorization()+")" );                            
                                lockFailedFids.add( fid );
                            }
                            else {
                                LOGGER.warning("Lock "+numberLocked+" "+fid+" (authID:"+featureLock.getAuthorization()+") duplicated FeatureID!" );
                                lockedFids.add( fid );
                            }                    
                        }
                    }
                    finally {
                        if( reader != null ) reader.close();
                    }
                    if( !lockedFids.isEmpty() ){
                        Transaction t = new DefaultTransaction();
                        source.setTransaction( t );
                        t.addAuthorization( featureLock.getAuthorization() );
                        source.releaseLock( featureLock.getAuthorization() );
                        t.commit();
                        source.setTransaction( Transaction.AUTO_COMMIT );
                    }
                }
            }
            
            if( featureLock != null && !lockFailedFids.isEmpty() ){
                // I think we need to release and fail when lockAll fails
                //
                // abort will take care of releasing the locks
                throw new WfsException(
                    "Could not aquire locks for:" + lockFailedFids
                );
            }

            // Can someone tell me what this does?
	    //Um, I'm not sure if it's needed, but it makes sure that xalan is
	    //used to perform the transformation.  If it's not set than it 
	    //could juse use whatever the user has on their classpath.  We 
	    //should check with IanS, he might have some particular reason
	    //for wanting xalan.
            System.setProperty("javax.xml.transform.TransformerFactory",
                "org.apache.xalan.processor.TransformerFactoryImpl");

            FeatureType schema = meta.getSchema();
            transformer.setIndentation(2);

            ServerConfig config = ServerConfig.getInstance();
            WFSConfig wfsConfig = config.getWFSConfig();
            String wfsSchemaLoc = wfsConfig.getWfsBasicLocation();
            String fSchemaLoc = wfsConfig.getDescribeUrl(typeNames.toString());
            namespace = meta.getDataStore().getNameSpace();
            transformer.addSchemaLocation("http://www.opengis.net/wfs", wfsSchemaLoc);
            transformer.addSchemaLocation(namespace.getUri(), fSchemaLoc);
	    transformer.setGmlPrefixing(true); //TODO: make this a user config
            if( featureLock != null){
                // TODO: chris needs to add the lock authorization to
                //       the transformer header info
		transformer.setLockId( featureLock.getAuthorization() );
            }
	    transformer.setSrsName("http://www.opengis.net/gml/srs/epsg.xml#" +
				   meta.getSRS());
	    resultList = results;
	    
        }
        catch (IOException e){
            throw new ServiceException(e, "problem with FeatureResults",
                            request.getHandle());            
        } catch (NoSuchElementException e) {
            throw new ServiceException(e, "problem with FeatureResults",
                            request.getHandle());
        } catch (IllegalAttributeException e) {
            throw new ServiceException(e, "problem with FeatureResults",
                            request.getHandle());
        }                     
    }
    
    /**
     * Convenience method to get the handle information from a query, if it
     * exists.
     *
     * @param query the query to get the handle from.
     *
     * @return A string to report more information where things went wrong.
     */
    private static String getLocator(Query query) {
        String locator = query.getHandle();

        if ((locator == null) || locator.equals("")) {
            locator = "Class FeatureResponse, in method getQuery";
        }

        return locator;
    }

    /**
     * Parses the GetFeature request and returns a contentHandler.
     *
     * @param query The geoserver representation of the query.
     * @param meta The info on the featureType.
     * @param maxFeatures The max number of features to get with this query.
     *
     * @return XML response to send to client
     *
     * @throws WfsException For any problems with the DataSource.
     */
    private static FeatureResults getFeatures(Query query,
        FeatureTypeConfig meta, int maxFeatures) throws WfsException {
        LOGGER.finest("about to get query: " + query);

        List propertyNames = null;

        if (!query.allRequested()) {
            propertyNames = query.getPropertyNames();
        }

        FeatureResults features = null;

        try {
            FeatureSource data = meta.getFeatureSource();
            LOGGER.finest("filter is " + query.getFilter());

            if (!query.allRequested()) {
                AttributeType[] mandatoryProps = meta.getSchema()
                                                     .getAttributeTypes();

                for (int i = 0; i < mandatoryProps.length; i++) {
                    query.addPropertyName(mandatoryProps[i].getName());
                }
            }

            org.geotools.data.Query dsQuery = query.getDataSourceQuery(maxFeatures);

            features = data.getFeatures(dsQuery);
        } catch (IOException e) {
            throw new WfsException(e, "While getting features from datasource",
                getLocator(query));
        }

        LOGGER.finest("successfully retrieved collection");

        return features;
    }
    
    /**
     * Release locks if we are into that sort of thing.
     * 
     * @see org.vfny.geoserver.responses.Response#abort()
     */
    public void abort() {
        if( request == null ){
            return; // request was not attempted
        }
        if( featureLock == null ){
            return; // we have no locks
        }
        
        CatalogConfig catalog = ServerConfig.getInstance().getCatalog();            
        // I think we need to release and fail when lockAll fails
        //
        try {
            for (Iterator it = request.getQueries().iterator(); it.hasNext();) {
                  
                Query query = (Query) it.next();                                                                   
                FeatureTypeConfig meta = catalog.getFeatureType( query.getTypeName() );
                FeatureLocking source = (FeatureLocking) meta.getFeatureSource();
                
                Transaction t = new DefaultTransaction();
                source.setTransaction( t );
                t.addAuthorization( featureLock.getAuthorization() );
                source.releaseLock( featureLock.getAuthorization() );
                t.commit();
                source.setTransaction( Transaction.AUTO_COMMIT );
            }            
        }
        catch( IOException ioException ){
            LOGGER.warning("Abort not complete:"+ioException);            
        }
    }

}
