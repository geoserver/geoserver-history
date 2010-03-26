/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.gss;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Statement;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.xml.namespace.QName;

import org.geoserver.catalog.Catalog;
import org.geoserver.catalog.FeatureTypeInfo;
import org.geoserver.catalog.NamespaceInfo;
import org.geoserver.config.GeoServer;
import org.geoserver.gss.CentralRevisionsType.LayerRevision;
import org.geoserver.gss.GSSInfo.GSSMode;
import org.geotools.data.DataAccess;
import org.geotools.data.Transaction;
import org.geotools.data.jdbc.JDBCUtils;
import org.geotools.data.postgis.VersionedPostgisDataStore;

public class DefaultGeoServerSynchronizationService implements GeoServerSynchronizationService {

    static final String SYNC_TABLES = "synch_tables";
    
    static final String UNIT_SYNC_TABLES_CREATION = "CREATE TABLE synch_tables(\n"
            + "table_id SERIAL PRIMARY KEY, \n" + "name VARCHAR(256) NOT NULL, \n"
            + "type CHAR(1) CHECK (type in ('p', 'b', '2') NOT NULL)";
    
    static final String SYNC_HISTORY = "synch_history";
    
    static final String SYNCH_HISTORY_CREATION = "CREATE TABLE synch_history(\n"
        + "table_name VARCHAR(256) not null,\n"
        + "local_revision LONG not null,\n"
        + "central_revision LONG,\n"
        + "primary key(table_name, local_revision))";

    private Catalog catalog;

    private GSSInfo info;

    public DefaultGeoServerSynchronizationService(GeoServer geoServer) {
        this.catalog = geoServer.getCatalog();
        this.info = geoServer.getService(GSSInfo.class);
    }

    /**
     * Checks the module is ready to be used. TODO: move this to a listener so that we don't do all
     * the ckecks for every request
     */
    void ensureEnabled() {
        if (info == null) {
            throw new GSSServiceException(
                    "The service is not properly configured, gssInfo not found");
        }
        
        if(info.getMode() == null) {
            throw new GSSServiceException("The gss mode has not been configured");
        }

        if (info.getVersioningDataStore() == null || !info.getVersioningDataStore().isEnabled()) {
            throw new GSSServiceException("The service is disabled as the "
                    + "versioning datastore is not available/disabled");
        }
        
        VersionedPostgisDataStore dataStore;
        try {
            DataAccess ds = info.getVersioningDataStore().getDataStore(null);
            if(!(ds instanceof VersionedPostgisDataStore)) {
                throw new GSSServiceException("The store attached to the gss module is not a PostGIS versioning one");
            }
            dataStore = (VersionedPostgisDataStore) ds;
            
            if(info.getMode() == GSSMode.Unit) {
                // check the required metadata tables are there, if not, create them
                Set<String> typeNames = new HashSet<String>(Arrays.asList(dataStore.getTypeNames()));
                if(!typeNames.contains(SYNC_TABLES)) {
                    runStatement(dataStore, UNIT_SYNC_TABLES_CREATION);
                }
                
                if(!typeNames.contains(SYNC_HISTORY)) {
                    runStatement(dataStore, SYNCH_HISTORY_CREATION);
                }
                
                
            }
        } catch(IOException e) {
            throw new GSSServiceException("A problem occurred while checking the versioning store", e);
        }

        
    }

    void runStatement(VersionedPostgisDataStore dataStore, String sqlStatement) {
        Connection conn = null;
        Statement st = null;
        try {
            conn = dataStore.getConnection(Transaction.AUTO_COMMIT);
            st = conn.createStatement();
            st.execute(sqlStatement);
        } catch(Exception e) {
            // hmm... we should really try to use createSchema() instead, but atm
            // we don't have the necessary control over it
            JDBCUtils.close(st);
            JDBCUtils.close(conn, Transaction.AUTO_COMMIT, null);
        }
    }

    void ensureClientEnabled() {
        ensureEnabled();

        if (info.getMode() == GSSMode.Central) {
            throw new GSSServiceException(
                    "gss configured in Central mode, won't to Unit service calls");
        }
    }

    public CentralRevisionsType getCentralRevision(GetCentralRevisionType request) {
        ensureClientEnabled();

        CentralRevisionsType cr = new CentralRevisionsType();
        for (QName typeName : request.getTypeNames()) {
            NamespaceInfo ns = catalog.getNamespaceByURI(typeName.getNamespaceURI());
            if (ns == null) {
                ns = catalog.getNamespaceByPrefix(typeName.getPrefix());
            }
            if (ns == null) {
                throw new GSSServiceException("Could not locate typeName: " + typeName);
            }

            FeatureTypeInfo fti = catalog.getFeatureTypeByName(ns, typeName.getLocalPart());
            if (fti == null) {
                throw new GSSServiceException("Could not locate typeName: " + typeName);
            }

            // TODO: actually get the central revision number from the local table
            cr.getLayerRevisions().add(new LayerRevision(typeName, 0));
        }

        return cr;
    }

}
