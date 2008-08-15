package com.orci.geotools.data.LineMergerPostgis;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.geotools.data.DataSourceException;
import org.geotools.data.DataStore;
import org.geotools.data.jdbc.ConnectionPool;
import org.geotools.data.postgis.PostgisConnectionFactory;
import org.geotools.data.postgis.PostgisDataStoreFactory;

/**
 * This class is used to create OpenTMSDataStore objects
 * Copyright (c) 2005, Open Roads Consulting, Inc.
 */
public class LineMergerPostgisDataStoreFactory extends PostgisDataStoreFactory {
    private static final Logger LOG = org.geotools.util.logging.Logging.getLogger(LineMergerPostgisDataStoreFactory.class.getName());

    /** Param, package visibiity for JUnit tests. */
    static final Param HOST = new Param("host", String.class,
            "postgis host machine", true, "localhost");

    /** Param, package visibiity for JUnit tests. */
    static final Param PORT = new Param("port", Integer.class,
            "postgis connection port (default is 5432)", true, new Integer(5432));

    /** Param, package visibiity for JUnit tests. */
    static final Param DATABASE = new Param("database", String.class,
            "postgis database");

    /** Param, package visibiity for JUnit tests. */
    static final Param SCHEMA = new Param("schema", String.class,
            "postgis schema", false, "public");
    
    /** Param, package visibiity for JUnit tests. */
    static final Param USER = new Param("user", String.class,
            "user name to login as");

    /** Param, package visibiity for JUnit tests. */
    static final Param PASSWD = new Param("passwd", String.class,
            "password used to login", false);


    /** Param, package visibiity for JUnit tests. */
    static final Param NAMESPACE = new Param("namespace", String.class,
            "namespace prefix used", false);

    static final Param WKBENABLED = new Param("wkb enabled", Boolean.class,
            "set to true if Well Known Binary should be used to read PostGIS "
            + "data (experimental)", false, new Boolean(true));

    static final Param LOOSEBBOX = new Param("loose bbox", Boolean.class,
            "set to true if the Bounding Box should be 'loose', faster but "
            + "not as deadly accurate", false, new Boolean(true));
    
    /** unique feature key. */
    public static final Param FEATURE_UNIQUE_KEY = new Param("LineMergerPostgis:FeatureUniqueKey",
            String.class,
            "Feature Unique Key",
            true,
            "rtesysshrt"
            );
   
    private Map<Map, DataStore> cache = new HashMap<Map, DataStore>();
    
    /**
     * {@inheritDoc}
     */
    public DataStore createDataStore(Map params) throws IOException {
        LOG.info("createDataStore");
        if (cache.containsKey(params)) {
            return (DataStore) cache.get(params);
        }
        
        // parse Postgis parameters
        String dbHost = (String) HOST.lookUp(params);
        String dbUser = (String) USER.lookUp(params);
        String dbPasswd = (String) PASSWD.lookUp(params);
        Integer dbPort = (Integer) PORT.lookUp(params);
        String dbSchema = (String) SCHEMA.lookUp(params);
        String database = (String) DATABASE.lookUp(params);
        Boolean wkbEnabled = (Boolean) WKBENABLED.lookUp(params);
        Boolean isLooseBbox = (Boolean) LOOSEBBOX.lookUp(params);
        String namespace = (String) NAMESPACE.lookUp(params);

        // get connection pool
        PostgisConnectionFactory connFact = new PostgisConnectionFactory(dbHost, dbPort.toString(), database);
        connFact.setLogin(dbUser, dbPasswd);
        ConnectionPool pool = null;
        try {
            pool = connFact.getConnectionPool();
        } catch (SQLException e) {
            throw new DataSourceException("Could not create connection", e);
        }

        LineMergerPostgisDataStore dataStore = null;
        if (dbSchema == null && namespace == null) {
            dataStore = new LineMergerPostgisDataStore(pool, params); 
        } else if (dbSchema == null && namespace != null) {
            dataStore = new LineMergerPostgisDataStore(pool, namespace, params);
        } else {
            dataStore = new LineMergerPostgisDataStore(pool, namespace, dbSchema, params);
        }

        if (wkbEnabled != null) dataStore.setWKBEnabled(wkbEnabled.booleanValue());
        if (isLooseBbox != null) dataStore.setLooseBbox(isLooseBbox.booleanValue());
        
        return dataStore;
    }
    
    /**
     * {@inheritDoc}
     */
    public DataStore createNewDataStore(Map params) throws IOException {
        throw new UnsupportedOperationException("Cannot create a new Database");
    }
    
    /**
     * {@inheritDoc}
     */
    public String getDescription() {
        return "Inrix Real Time TMC Feature.";
    }
    
    /**
     * {@inheritDoc}
     */
    public Param[] getParametersInfo() {
        return new Param[] { 
                HOST, PORT, DATABASE, USER, PASSWD, WKBENABLED, LOOSEBBOX, NAMESPACE, 
                FEATURE_UNIQUE_KEY, 
                };
    }
    
    /**
     * {@inheritDoc}
     */
    public boolean canProcess(Map params) {
        if (params == null) return false;
        if (!params.containsKey(FEATURE_UNIQUE_KEY.key)) return false;
        return true;
    }
    
    /**
     * {@inheritDoc}
     */
    public String getDisplayName() {
        return "LineMergerPostgis";
    }
}
