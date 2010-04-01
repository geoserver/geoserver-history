/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.gss;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.namespace.QName;

import net.opengis.wfs.DeleteElementType;
import net.opengis.wfs.InsertElementType;
import net.opengis.wfs.PropertyType;
import net.opengis.wfs.UpdateElementType;

import org.geoserver.catalog.Catalog;
import org.geoserver.catalog.FeatureTypeInfo;
import org.geoserver.catalog.NamespaceInfo;
import org.geoserver.config.GeoServer;
import org.geoserver.gss.CentralRevisionsType.LayerRevision;
import org.geoserver.gss.GSSInfo.GSSMode;
import org.geotools.data.DataAccess;
import org.geotools.data.DataUtilities;
import org.geotools.data.DefaultQuery;
import org.geotools.data.DefaultTransaction;
import org.geotools.data.FeatureStore;
import org.geotools.data.Transaction;
import org.geotools.data.VersioningDataStore;
import org.geotools.data.VersioningFeatureStore;
import org.geotools.data.jdbc.JDBCUtils;
import org.geotools.data.postgis.VersionedPostgisDataStore;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.FeatureIterator;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.util.logging.Logging;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.FeatureType;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.PropertyIsEqualTo;
import org.opengis.filter.sort.SortBy;
import org.opengis.filter.sort.SortOrder;

/**
 * The GSS implementation
 */
public class DefaultGeoServerSynchronizationService implements GeoServerSynchronizationService {

    static final Logger LOGGER = Logging.getLogger(DefaultGeoServerSynchronizationService.class);

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
            + "table_name VARCHAR(256) NOT NULL,\n" + "feature_id BIGINT NOT NULL,\n"
            + "local_revision BIGINT NOT NULL,\n" + "resolved BOOLEAN NOT NULL,\n"
            + "difference TEXT,\n" + "primary key(table_name, feature_id, local_revision))";

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
        // basic sanity checks on the config
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

        FeatureIterator<SimpleFeature> fi = null;
        try {
            // basic sanity checks on the datastore
            DataAccess ds = info.getVersioningDataStore().getDataStore(null);
            if (!(ds instanceof VersionedPostgisDataStore)) {
                throw new GSSServiceException(
                        "The store attached to the gss module is not a PostGIS versioning one");
            }
            VersionedPostgisDataStore dataStore = (VersionedPostgisDataStore) ds;

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
                    runStatement(dataStore, SYNCH_CONFLICTS_CREATION);
                }
                dataStore.setVersioned(SYNCH_CONFLICTS, true, null, null);

                // version enable all tables that are supposed to be shared
                fi = dataStore.getFeatureSource(SYNCH_TABLES).getFeatures().features();
                while (fi.hasNext()) {
                    String tableName = (String) fi.next().getAttribute("table_name");
                    dataStore.setVersioned(tableName, true, null, null);
                }
                fi.close();
            }
        } catch (Exception e) {
            throw new GSSServiceException("A problem occurred while checking the versioning store",
                    e);
        } finally {
            if (fi != null) {
                fi.close();
            }
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

    void ensureUnitEnabled() {
        ensureEnabled();

        if (info.getMode() == GSSMode.Central) {
            throw new GSSServiceException(
                    "gss configured in Central mode, won't to Unit service calls");
        }
    }

    /**
     * Responds a GetCentralRevision reuest
     */
    public CentralRevisionsType getCentralRevision(GetCentralRevisionType request) {
        ensureUnitEnabled();

        CentralRevisionsType cr = new CentralRevisionsType();
        for (QName typeName : request.getTypeNames()) {
            long revision = getLastCentralRevision(typeName);
            cr.getLayerRevisions().add(new LayerRevision(typeName, revision));
        }

        return cr;
    }

    /**
     * Checks the feature type exists, it's actually synchronised, and returns the last known
     * central revision
     * 
     * @param typeName
     * @return
     */
    long getLastCentralRevision(QName typeName) {
        NamespaceInfo ns = catalog.getNamespaceByURI(typeName.getNamespaceURI());
        if (ns == null) {
            ns = catalog.getNamespaceByPrefix(typeName.getPrefix());
        }
        if (ns == null) {
            throw new GSSServiceException("Could not locate typeName: " + typeName,
                    "InvalidParameterValue", "typeName");
        }

        FeatureTypeInfo fti = catalog.getFeatureTypeByName(ns, typeName.getLocalPart());
        if (fti == null) {
            throw new GSSServiceException("Could not locate typeName: " + typeName,
                    "InvalidParameterValue", "typeName");
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
                throw new GSSServiceException(fti.getName() + " is not a synchronised layer",
                        "InvalidParameterValue", "typeName");
            }

            // generate conflicts

            // apply diffs

            // gather the record from the synch history table
            q = new DefaultQuery();
            q.setFilter(ff.equal(ff.property("table_name"), ff.literal(fti.getName()), true));
            q.setSortBy(new SortBy[] { ff.sort("central_revision", SortOrder.DESCENDING) });
            q.setMaxFeatures(1);
            fi = ds.getFeatureSource(SYNCH_HISTORY).getFeatures(q).features();

            if (fi.hasNext()) {
                return ((Number) fi.next().getAttribute("central_revision")).longValue();
            }
        } catch (IOException e) {
            throw new GSSServiceException("Could not compute the response", e);
        } finally {
            if (fi != null) {
                fi.close();
            }
        }
        return -1;
    }

    public PostDiffResponseType postDiff(PostDiffType request) {
        ensureUnitEnabled();

        // check the layer is actually shared and get the latest known central revision
        long centralRevision = getLastCentralRevision(request.getTypeName());

        if (request.getFromVersion() != centralRevision) {
            throw new GSSServiceException("Invalid fromVersion, it should be " + centralRevision,
                    "InvalidParameterValue", "fromVersion");
        }

        if (request.getFromVersion() > request.getToVersion()) {
            throw new GSSServiceException("Invalid toVersion, it should be higher than "
                    + request.getToVersion(), "InvalidParameterValue", "toVersion");
        }

        // make sure all of the changes are applied in one hit, or none is
        // very important, make sure all versioning writes use the same transaction or they
        // will deadlock
        Transaction transaction = new DefaultTransaction();
        try {
            String tableName = request.getTypeName().getLocalPart();
            VersioningDataStore ds = (VersioningDataStore) info.getVersioningDataStore()
                    .getDataStore(null);
            
            // grab the feature stores and bind them all to the same transaction
            VersioningFeatureStore conflicts = (VersioningFeatureStore) ds.getFeatureSource(SYNCH_CONFLICTS);
            conflicts.setTransaction(transaction);
            FeatureStore history = (FeatureStore) ds.getFeatureSource(SYNCH_HISTORY);
            history.setTransaction(transaction);
            VersioningFeatureStore fs = (VersioningFeatureStore) ds.getFeatureSource(tableName);
            fs.setTransaction(transaction);
            SimpleFeatureType ft = fs.getSchema();

            // get a hold on a revision number early so that we don't get concurrent changes
            // from the user (the datastore will make it so that no new revision numbers will
            // be generated until we commit or rollback this transaction
            long newLocalRevision = Long.parseLong(conflicts.getVersion());

            // TODO: handle conflicts!

            // apply changes
            if (request.getTransaction() != null) {
                List<DeleteElementType> deletes = request.getTransaction().getDelete();
                List<UpdateElementType> updates = request.getTransaction().getUpdate();
                List<InsertElementType> inserts = request.getTransaction().getInsert();

                for (DeleteElementType delete : deletes) {
                    fs.removeFeatures(delete.getFilter());
                }
                for (UpdateElementType update : updates) {
                    List<PropertyType> props = update.getProperty();
                    List<AttributeDescriptor> atts = new ArrayList<AttributeDescriptor>(props
                            .size());
                    List<Object> values = new ArrayList<Object>(props.size());
                    for (PropertyType prop : props) {
                        atts.add(ft.getDescriptor(prop.getName().getLocalPart()));
                        values.add(prop.getValue());
                    }
                    AttributeDescriptor[] attArray = (AttributeDescriptor[]) atts
                            .toArray(new AttributeDescriptor[atts.size()]);
                    Object[] valArray = (Object[]) values.toArray(new Object[values.size()]);
                    fs.modifyFeatures(attArray, valArray, update.getFilter());
                }
                for (InsertElementType insert : inserts) {
                    List<SimpleFeature> features = insert.getFeature();
                    fs.addFeatures(DataUtilities.collection(features));
                }
            }

            // save/update the synchronisation metadata
            long newCentralRevision = request.getToVersion();
            PropertyIsEqualTo ftSyncRecord = ff.equals(ff.property("table_name"), ff
                    .literal(tableName));
            SimpleFeatureType hft = (SimpleFeatureType) history.getSchema();
            SimpleFeature f = SimpleFeatureBuilder.build(hft, new Object[] { tableName,
                    newLocalRevision, newCentralRevision }, null);
            history.addFeatures(DataUtilities.collection(f));

            // commit all the changes
            transaction.commit();
        } catch (Throwable t) {
            // make sure we rollback the transaction in case of _any_ exception
            try {
                transaction.rollback();
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, "Rollback failed. This is unexpected", e);
            }

            throw new GSSServiceException("Error occurred while applyling the diff", t);
        } finally {
            // very important to close transaction, as it holds a connection to the db
            try {
                transaction.close();
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, "Tranaction close failed. This is unexpected", e);
            }

        }

        return new PostDiffResponseType();
    }
}
