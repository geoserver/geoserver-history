package com.orci.geotools.data.LineMergerPostgis;

import java.io.IOException;
import java.util.Map;
import java.util.logging.Logger;

import org.geotools.data.DefaultQuery;
import org.geotools.data.FeatureReader;
import org.geotools.data.Query;
import org.geotools.data.Transaction;
import org.geotools.data.jdbc.ConnectionPool;
import org.geotools.data.postgis.PostgisDataStore;
import org.geotools.feature.FeatureType;
import org.geotools.filter.Filter;


/**
 * This is the OpenTMS data srote class.
 * Copyright (c) 2005, Open Roads Consulting, Inc.
 */
public class LineMergerPostgisDataStore extends PostgisDataStore {
    private static final Logger LOG = org.geotools.util.logging.Logging.getLogger(LineMergerPostgisDataStore.class.getName());
    
    private String featureUniqueKey;

    /**
     * Construct <code>WFSDataStore</code>.
     * @param pool DB connection pool
     * @param params the connection parameters
     * @throws IOException error initializing.
     */
    public LineMergerPostgisDataStore(ConnectionPool pool, Map params) throws IOException {
        super(pool);
        
        try { featureUniqueKey = (String)LineMergerPostgisDataStoreFactory.FEATURE_UNIQUE_KEY.lookUp(params); } catch (Exception e) { }
    }

    /**
     * Construct <code>WFSDataStore</code>.
     * @param pool DB connection pool
     * @param namespace the namespace
     * @param params the connection parameters
     * @throws IOException error initializing.
     */
    public LineMergerPostgisDataStore(ConnectionPool pool, String namespace, Map params) throws IOException {
        super(pool, namespace);
        
        try { featureUniqueKey = (String)LineMergerPostgisDataStoreFactory.FEATURE_UNIQUE_KEY.lookUp(params); } catch (Exception e) { }
    }

    /**
     * Construct <code>WFSDataStore</code>.
     * @param pool DB connection pool
     * @param namespace the namespace
     * @param dbSchema the schema
     * @param params the connection parameters
     * @throws IOException error initializing.
     */
    public LineMergerPostgisDataStore(ConnectionPool pool, String namespace, String dbSchema, Map params) throws IOException {
        super(pool, namespace, dbSchema);
        
        try { featureUniqueKey = (String)LineMergerPostgisDataStoreFactory.FEATURE_UNIQUE_KEY.lookUp(params); } catch (Exception e) { }
    }

    /**
     * {@inheritDoc}
     */
    public FeatureReader getFeatureReader(final FeatureType requestType, final Filter filter, final Transaction transaction) throws IOException {
        LOG.info("getFeatureReader #1");
        return new LineMergerFeatureReader(super.getFeatureReader(requestType, filter, transaction), featureUniqueKey);
    }

    /**
     * {@inheritDoc}
     */
    public FeatureReader getFeatureReader(Query query, Transaction trans) throws IOException {
        LOG.info("getFeatureReader #2 " + query.getClass().getName());
        if (!query.retrieveAllProperties()) {
            String[] props = new String[query.getPropertyNames().length + 1];
            System.arraycopy(query.getPropertyNames(), 0, props, 0, query.getPropertyNames().length);
            props[props.length - 1] = featureUniqueKey;
            query = new DefaultQuery(query.getTypeName(), query.getNamespace(), query.getFilter(), query.getMaxFeatures(), props, query.getHandle());
        }
        return new LineMergerFeatureReader(super.getFeatureReader(query, trans), featureUniqueKey);
        //return super.getFeatureReader(query, trans);
    }
}
