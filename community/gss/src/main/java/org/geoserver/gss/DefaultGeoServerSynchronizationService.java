/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.gss;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
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
import org.geotools.data.DefaultQuery;
import org.geotools.data.Transaction;
import org.geotools.data.VersioningDataStore;
import org.geotools.data.jdbc.JDBCUtils;
import org.geotools.data.postgis.VersionedPostgisDataStore;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.FeatureIterator;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.sort.SortBy;
import org.opengis.filter.sort.SortOrder;

public class DefaultGeoServerSynchronizationService implements GeoServerSynchronizationService {

    static final String SYNCH_TABLES = "synch_tables";

    static final String UNIT_SYNC_TABLES_CREATION = "CREATE TABLE synch_tables(\n"
            + "table_id SERIAL PRIMARY KEY, \n" // 
            + "table_name VARCHAR(256) NOT NULL, \n" //
            + "type CHAR(1) NOT NULL CHECK (type in ('p', 'b', '2')))";

    static final String SYNCH_HISTORY = "synch_history";

    static final String SYNCH_HISTORY_CREATION = "CREATE TABLE synch_history(\n"
            + "table_name VARCHAR(256) NOT NULL,\n" //
            + "local_revision BIGINT NOT NULL,\n" //
            + "central_revision BIGINT,\n" //
            + "primary key(table_name, local_revision))";

    static final String SYNCH_CONFLICTS = "synch_conflicts";

    static final String SYNCH_CONFLICTS_CREATION = "CREATE TABLE synch_conflicts(\n"
            + "table_name VARCHAR(256) NOT NULL,\n" // 
            + "feature_id BIGINT NOT NULL,\n" //
            + "local_revision BIGINT NOT NULL,\n" // 
            + "resolved BOOLEAN NOT NULL,\n" //
            + "primary key(table_name, feature_id, local_revision))";

    private FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);

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

        if (info.getMode() == null) {
            throw new GSSServiceException("The gss mode has not been configured");
        }

        if (info.getVersioningDataStore() == null || !info.getVersioningDataStore().isEnabled()) {
            throw new GSSServiceException("The service is disabled as the "
                    + "versioning datastore is not available/disabled");
        }

        VersionedPostgisDataStore dataStore;
        try {
            DataAccess ds = info.getVersioningDataStore().getDataStore(null);
            if (!(ds instanceof VersionedPostgisDataStore)) {
                throw new GSSServiceException(
                        "The store attached to the gss module is not a PostGIS versioning one");
            }
            dataStore = (VersionedPostgisDataStore) ds;

            if (info.getMode() == GSSMode.Unit) {
                // check the required metadata tables are there, if not, create them
                Set<String> typeNames = new HashSet<String>(Arrays.asList(dataStore.getTypeNames()));
                if (!typeNames.contains(SYNCH_TABLES)) {
                    runStatement(dataStore, UNIT_SYNC_TABLES_CREATION);
                }
                dataStore.setVersioned(SYNCH_TABLES, false, null, null);

                if (!typeNames.contains(SYNCH_HISTORY)) {
                    runStatement(dataStore, SYNCH_HISTORY_CREATION);
                }
                dataStore.setVersioned(SYNCH_HISTORY, false, null, null);
                
                if (!typeNames.contains(SYNCH_CONFLICTS)) {
                    runStatement(dataStore, SYNCH_CONFLICTS);
                }
                dataStore.setVersioned(SYNCH_HISTORY, false, null, null);
            }
        } catch (Exception e) {
            throw new GSSServiceException("A problem occurred while checking the versioning store",
                    e);
        }

    }

    void runStatement(VersionedPostgisDataStore dataStore, String sqlStatement) throws IOException,
            SQLException {
        Connection conn = null;
        Statement st = null;
        try {
            conn = dataStore.getConnection(Transaction.AUTO_COMMIT);
            st = conn.createStatement();
            st.execute(sqlStatement);
        } finally {
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

            FeatureIterator<SimpleFeature> fi = null;
            try {
                // get the versioning data store
                VersioningDataStore ds = (VersioningDataStore) info.getVersioningDataStore()
                        .getDataStore(null);

                // check the table is actually synch-ed
                DefaultQuery q = new DefaultQuery();
                q.setFilter(ff.equal(ff.property("table_name"), ff.literal(fti.getName()), true));
                int count = ds.getFeatureSource(SYNCH_TABLES).getCount(q);
                if (count == 0) {
                    throw new GSSServiceException(fti.getName() + " is not a synchronised layer");
                }

                // gather the record from the synch history table
                q = new DefaultQuery();
                q.setFilter(ff.equal(ff.property("table_name"), ff.literal(fti.getName()), true));
                q.setSortBy(new SortBy[] { ff.sort("central_revision", SortOrder.DESCENDING) });
                q.setMaxFeatures(1);
                fi = ds.getFeatureSource(SYNCH_HISTORY).getFeatures(q).features();
                long revision = -1;
                if (fi.hasNext()) {
                    revision = ((Number) fi.next().getAttribute("central_revision")).longValue();
                }
                cr.getLayerRevisions().add(new LayerRevision(typeName, revision));
            } catch (IOException e) {
                throw new GSSServiceException("Could not compute the response", e);
            } finally {
                if (fi != null) {
                    fi.close();
                }
            }
        }

        return cr;
    }

}
