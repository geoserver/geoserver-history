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
import org.vfny.geoserver.servlets.wfs.FeatureWithLock;

import java.io.*;
import java.util.*;
import java.util.logging.*;

import javax.xml.transform.TransformerException;


/**
 * Handles a Get Feature request and creates a Get Feature response GML string.
 *
 * @author Chris Holmes, TOPP
 * @version $Id: FeatureResponse.java,v 1.1.2.6 2003/11/13 19:55:30 jive Exp $
 */
public class FeatureResponse implements Response {
    /** Standard logging instance for class */
    private static final Logger LOGGER = Logger.getLogger(
            "org.vfny.geoserver.responses");

    /** DOCUMENT ME! */
    private static final ServerConfig config = ServerConfig.getInstance();
    private FeatureResults[] features;
    private FeatureTransformer transformer;

    // set by execute2, used by write2
    private FeatureRequest request;
    private List resultList;
    private Set fids;
    private Set lockFailedFids;
    
    /**
     * Empty constructor
     */
    public FeatureResponse() {
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getContentType() {
        return config.getGlobalConfig().getMimeType();
    }

    /**
     * DOCUMENT ME!
     *
     * @param out DOCUMENT ME!
     *
     * @throws ServiceException DOCUMENT ME!
     * @throws WfsException DOCUMENT ME!
     */
    public void writeTo(OutputStream out) throws ServiceException {
        try {
            if ((transformer == null) || (features == null)) {
                throw new IllegalStateException(
                    "execute has not been called prior to writeTo");
            }

            //FeatureCollection coll = features[0].collection();
            //Feature firstF = coll.features().next();
            //LOGGER.info("first feature is " + firstF);
            //LOGGER.info("schema is " + firstF.getFeatureType());
            transformer.transform(features, out);
        } catch (Exception ex) {
            ex.printStackTrace();

            //REVISIT: This fails mightily if it fails here, since
            //get output stream is already called.
            //TODO: user config option to have better error reporting, at 
            //the expense of performance.  Then use a ByteBufferOutputStream
            // to buffer the response, write it to the passed in output
            //stream if no problems, if there are then exception can still
            //get thrown.
            throw new WfsException(ex);
        }
    }
    static int safe = 0;
    
    /**
     * Writes response in a safe manner.
     * <p>
     * Uses a temporary file to hold the response during construction.
     * If the response is successful the temporary file is copied to out.
     * If not an error message should be produced
     * (currently throw ServiceException).
     * </p>
     * <p>
     * This idea could really be moved "up" a level.
     * Pros: common safty code, less work for Response implementations
     * Cons: Some Responses may be being Safe and not require this
     * </p>
     * @param out
     * @throws ServiceException
     */
    public synchronized void writeToSafe( OutputStream out ) throws ServiceException {
        if( request == null ){
            throw new IllegalStateException(
                "execute has not been called prior to writeToSafe");
        }
        safe++;
        File temp = null;        
        try {
            temp = File.createTempFile( "FeatureResponse"+safe,"tmp" );
            FileOutputStream safe = new FileOutputStream( temp );
                        
            writeToImplementation( safe );
            safe.close();
            
            // request succeeded!
            
            // copy result to the real output stream
            InputStream copy = new BufferedInputStream( new FileInputStream( temp ) );
            // need to buffer this for performance
            // not sure if it has been done already
            if( out instanceof BufferedOutputStream ){
                out = new BufferedOutputStream( out );    
            }
            
            int b;
            
            b = copy.read();
            while( b != -1 ){
                out.write( b );
                b = copy.read();                    
            }
            copy.close();                  
        }
        catch( IOException ex ){
            // write out actual error message?
            throw new WfsException(ex);
        } catch (TransformerException te) {
            // write out actual error message?
            throw new WfsException(te);
        }
        finally {
            if( temp != null ){
                temp.delete();
            }                    
        }
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
    public void writeToImplementation( OutputStream out ) throws ServiceException, TransformerException {
        // to do a single pass we would 
        // have to perform the execution here
        //
        // This should probably be done, since FeatureResults are "live"
        // and may change between the locking and the writing
        if( request == null || resultList == null){
            throw new IllegalStateException(
                "execute has not been called prior to writeTo" );            
        }
        int maxFeatures = request.getMaxFeatures();
        for( Iterator i=resultList.iterator(); i.hasNext() && maxFeatures>0; ){
            FeatureResults featureResults = (FeatureResults) i.next();
            
            transformer.transform( featureResults, out );
        }        
    }
    /**
     * Jody here with a replacement for execute that make use
     * of geotools2 locking.
     * <p>
     * This code is a discussion point, when everyone has had there input
     * we will try and set things up properly.
     * </p>
     * <p>
     * The idea is to grab the FeatureSources during execute, and use them
     * during writeTo
     * </p>
     * @param req
     * @throws ServiceException
     */
    public void execute2( FeatureRequest request ) throws ServiceException {
        LOGGER.finest("write xml response. called request is: " + request);
        FeatureLock featureLock = null;
        if( request instanceof FeatureWithLockRequest){
            featureLock = ((FeatureWithLockRequest)request).toFeatureLock();                        
        }
        // I wonder if the FeatureLocking api should just take the
        // provided lock and try and lock features on during
        // the getFeatures operation?
        // Pros:
        // - we would not need two passes
        // - the FeatureSource implementors can have a shot at optimization
        // Cons:
        // - expected to report the fids we did not lock
        //   (the geotools2 locking api just reports number locked) 
        //        
        String authorization = featureLock.getAuthorization();
        
        LOGGER.finest("We will lock using:"+authorization );

        CatalogConfig catalog = config.getCatalog();        
        FeatureTypeConfig meta = null;
        NameSpace namespace;       
        Query query;
        int maxFeatures = request.getMaxFeatures();

        StringBuffer typeNames = new StringBuffer();
        fids = new HashSet();
        lockFailedFids = new HashSet();
        
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
                meta = catalog.getFeatureType(query.getTypeName());
                namespace = meta.getDataStore().getNameSpace();                
                source = (FeatureLocking) meta.getFeatureSource();
        
                typeNames.append( query.getTypeName() );
                if (it.hasNext() && (maxFeatures > 0)) {
                    typeNames.append(",");
                }                
                // This doesn't seem to be working?
                ftNames.declareNamespace( source.getSchema(), namespace.getPrefix(), namespace.getUri() );
                                                
                source.setFeatureLock( featureLock );
    
                // Run through features and record FeatureIDs
                // Lock FeatureIDs as required
                FeatureResults features = source.getFeatures( query.toDataQuery( maxFeatures ) );
                for( FeatureReader reader= features.reader(); reader.hasNext(); ){
                    feature = reader.next();
                    fid = feature.getID();
                    fids.add( fid );
                    maxFeatures--;
                                        
                    if( featureLock != null ){                    
                        fidFilter = filterFactory.createFidFilter( fid );
                        numberLocked = source.lockFeatures( fidFilter );
                    
                        if( numberLocked == 1){
                            LOGGER.finest("Lock "+fid+" (authID:"+featureLock.getAuthorization()+")" );
                        }
                        else if ( numberLocked == 0 ){
                            LOGGER.finest("Lock "+fid+" conflict (authID:"+featureLock.getAuthorization()+")" );                            
                            lockFailedFids.add( fid );
                        }
                        else {
                            LOGGER.warning("Lock "+numberLocked+" "+fid+" (authID:"+featureLock.getAuthorization()+") duplicated FeatureID!" );                            
                        }                                        
                    }                    
                }
                results.add( features );
            }
            features = (FeatureResults[])
                results.toArray(new FeatureResults[results.size()]);

            System.setProperty("javax.xml.transform.TransformerFactory",
                "org.apache.xalan.processor.TransformerFactoryImpl");

            FeatureType schema = meta.getSchema();
            transformer.setIndentation(2);

            ServerConfig config = ServerConfig.getInstance();
            WFSConfig wfsConfig = config.getWFSConfig();
            String wfsSchemaLoc = config.getGlobalConfig().getSchemaBaseUrl()
                + "wfs/1.0.0/WFS-basic.xsd";
            String fSchemaLoc = wfsConfig.getURL()
                + "?request=DescribeFeatureType&" //HACK: bad hard code here.
                + "typeName=" + typeNames;
                
            namespace = meta.getDataStore().getNameSpace();
            transformer.addSchemaLocation("http://www.opengis.net/wfs", wfsSchemaLoc);
            transformer.addSchemaLocation(namespace.getUri(), fSchemaLoc);
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
     * DOCUMENT ME!
     *
     * @param req DOCUMENT ME!
     *
     * @throws WfsException DOCUMENT ME!
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
    public void execute(Request req) throws ServiceException {
        FeatureRequest request = (FeatureRequest) req;
        LOGGER.finest("write xml response. called request is: " + request);

        String outputFormat = request.getOutputFormat();
        
        if (!"GML2".equalsIgnoreCase(outputFormat)) {
            throw new WfsException("output format: " + outputFormat + " not "
                + "supported by geoserver");
        }

        String lockId = null;

        //REVISIT: this could probably be done more efficiently, with maybe
        //just one run through, and may need to be with the next spec version
        //but this should work for now, do locking, and then do getting.
        
        
        if (request instanceof FeatureWithLockRequest) {
            LockRequest lock = ((FeatureWithLockRequest) request).asLockRequest();
            lockId = LockResponse.performLock(lock, false);
        }

        int maxFeatures = request.getMaxFeatures();
        LOGGER.info("request is " + request);

        //Kinda hacky, might be better to put maxFeatures in FeatureTransformer
        //for real.  Could consider having an easier one for just one Query.
        //Query curQuery = request.getQuery(0);
        
        ArrayList results = new ArrayList(request.getQueryCount());
        CatalogConfig catalog = config.getCatalog();
        StringBuffer typeNames = new StringBuffer();
        FeatureTypeConfig meta = null;

        transformer = new FeatureTransformer();

        FeatureTypeNamespaces ftNames = transformer.getFeatureTypeNamespaces();

        try {
            for (Iterator it = request.getQueries().iterator();
                    it.hasNext() && (maxFeatures > 0);) {
                Query curQuery = (Query) it.next();
                String typeName = curQuery.getTypeName();
                meta = catalog.getFeatureType(curQuery.getTypeName());

                FeatureResults curResult = getFeatures(curQuery, meta,
                        maxFeatures);
                results.add(curResult);
                typeNames.append(typeName);
                maxFeatures -= curResult.getCount();

                if (it.hasNext() && (maxFeatures > 0)) {
                    typeNames.append(",");
                }

                NameSpace namespace = meta.getDataStore().getNameSpace();

                //This doesn't seem to be working.
                ftNames.declareNamespace(curResult.reader().getFeatureType(),
                    namespace.getPrefix(), namespace.getUri());

                //ftNames.declareDefaultNamespace(namespace.getPrefix(),
                //			     namespace.getUri());
            }
        } catch (IOException ioe) {
            throw new ServiceException(ioe, "problem with FeatureResults",
                request.getHandle());
        }

        //results.trimToSize();
        features = (FeatureResults[]) results.toArray(new FeatureResults[results
                .size()]);

        System.setProperty("javax.xml.transform.TransformerFactory",
            "org.apache.xalan.processor.TransformerFactoryImpl");

        //HACK! Should not just use the last meta.
        FeatureType schema = meta.getSchema();

        transformer.setIndentation(2);
        transformer.setGmlPrefixing(true); //TODO: make this a user config

        ServerConfig config = ServerConfig.getInstance();
        WFSConfig wfsConfig = config.getWFSConfig();
        String wfsSchemaLoc = config.getGlobalConfig().getSchemaBaseUrl()
            + "wfs/1.0.0/WFS-basic.xsd";
        String fSchemaLoc = wfsConfig.getDescribeUrl(typeNames.toString());
        NameSpace namespace = meta.getDataStore().getNameSpace();
        transformer.addSchemaLocation("http://www.opengis.net/wfs", wfsSchemaLoc);
        transformer.addSchemaLocation(namespace.getUri(), fSchemaLoc);

        //ftNames.declareNamespace(schema, namespace.getPrefix(), namespace.getUri());
        //        transformer.getFeatureTypeNamespaces().declareDefaultNamespace(namespace
        //.getPrefix(), namespace.getUri());
        //transformer.setPrettyPrint(config.getGlobalConfig().isVerbose());
        //transformer.setDefaultNamespace(config.getCatalog().getDefaultNameSpace()
        //                                  .getUri());
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
}
