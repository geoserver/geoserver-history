/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.responses.wfs;

import org.geotools.data.*;
import org.geotools.feature.*;
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


/**
 * Handles a Get Feature request and creates a Get Feature response GML string.
 *
 * @author Chris Holmes, TOPP
 * @version $Id: FeatureResponse.java,v 1.1.2.4 2003/11/12 02:15:55 cholmesny Exp $
 */
public class FeatureResponse implements Response {
    /** Standard logging instance for class */
    private static final Logger LOGGER = Logger.getLogger(
            "org.vfny.geoserver.responses");

    /** DOCUMENT ME! */
    private static final ServerConfig config = ServerConfig.getInstance();
    private FeatureResults[] features;
    private FeatureTransformer transformer;

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

    /**
     * DOCUMENT ME!
     *
     * @param req DOCUMENT ME!
     *
     * @throws WfsException DOCUMENT ME!
     * @throws WfsException DOCUMENT ME!
     * @task TODO: split this up a bit more?  Also get the proper namespace
     * declrations and schema locations.  Right now we're back up to where
     * we were with 1.0.*, as we can return two FeatureTypes in the same
     * namespace.  CITE didn't check for two in different namespaces, and
     * gml builder just couldn't deal.  Now we should be able to, we just
     * need to get the reporting right, use the AllSameType function as 
     * Describe does.
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

        //transformer.setMaxFeatures(maxFeatures);
        ServerConfig config = ServerConfig.getInstance();
        WFSConfig wfsConfig = config.getWFSConfig();
        String wfsSchemaLoc = config.getGlobalConfig().getSchemaBaseUrl()
            + "wfs/1.0.0/WFS-basic.xsd";
        String fSchemaLoc = wfsConfig.getURL()
            + "?request=DescribeFeatureType&" //HACK: bad hard code here.
            + "typeName=" + typeNames;
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
