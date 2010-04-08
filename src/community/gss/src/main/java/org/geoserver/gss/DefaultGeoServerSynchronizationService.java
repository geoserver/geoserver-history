/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.gss;

import static org.geotools.data.DataUtilities.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.namespace.QName;
import javax.xml.parsers.ParserConfigurationException;

import net.opengis.wfs.DeleteElementType;
import net.opengis.wfs.InsertElementType;
import net.opengis.wfs.PropertyType;
import net.opengis.wfs.TransactionType;
import net.opengis.wfs.UpdateElementType;

import org.eclipse.emf.ecore.EObject;
import org.geoserver.catalog.Catalog;
import org.geoserver.catalog.FeatureTypeInfo;
import org.geoserver.catalog.NamespaceInfo;
import org.geoserver.config.GeoServer;
import org.geoserver.gss.CentralRevisionsType.LayerRevision;
import org.geoserver.gss.GSSException.GSSExceptionCode;
import org.geoserver.gss.GSSInfo.GSSMode;
import org.geoserver.gss.xml.GSSConfiguration;
import org.geoserver.wfsv.VersioningTransactionConverter;
import org.geotools.data.DataAccess;
import org.geotools.data.DataUtilities;
import org.geotools.data.DefaultQuery;
import org.geotools.data.DefaultTransaction;
import org.geotools.data.FeatureDiff;
import org.geotools.data.FeatureDiffReader;
import org.geotools.data.FeatureSource;
import org.geotools.data.FeatureStore;
import org.geotools.data.Transaction;
import org.geotools.data.VersioningDataStore;
import org.geotools.data.VersioningFeatureSource;
import org.geotools.data.VersioningFeatureStore;
import org.geotools.data.jdbc.JDBCUtils;
import org.geotools.data.postgis.VersionedPostgisDataStore;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.gml3.GML;
import org.geotools.util.Converters;
import org.geotools.util.logging.Logging;
import org.geotools.xml.EMFUtils;
import org.geotools.xml.Encoder;
import org.geotools.xml.Parser;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.Id;
import org.opengis.filter.Not;
import org.opengis.filter.PropertyIsEqualTo;
import org.opengis.filter.identity.FeatureId;
import org.opengis.filter.identity.Identifier;
import org.opengis.filter.sort.SortBy;
import org.opengis.filter.sort.SortOrder;
import org.xml.sax.SAXException;

/**
 * The GSS implementation
 */
public class DefaultGeoServerSynchronizationService implements GeoServerSynchronizationService {

    // exception locator constant
    static final String TO_VERSION = "toVersion";

    static final String TYPE_NAME = "typeName";

    static final String FROM_VERSION = "fromVersion";

    static final Logger LOGGER = Logging.getLogger(DefaultGeoServerSynchronizationService.class);

    // metadata tables and sql to build them
    static final String SYNCH_TABLES = "synch_tables";

    static final String UNIT_SYNC_TABLES_CREATION = "CREATE TABLE synch_tables(\n"
            + "table_id SERIAL PRIMARY KEY, \n" // 
            + "table_name VARCHAR(256) NOT NULL, \n" //
            + "type CHAR(1) NOT NULL CHECK (type in ('p', 'b', '2')))";

    static final String SYNCH_HISTORY = "synch_history";

    static final String SYNCH_HISTORY_CREATION = "CREATE TABLE synch_history(\n"
            + "id SERIAL PRIMARY KEY,\n" //
            + "table_name VARCHAR(256) NOT NULL,\n" //
            + "local_revision BIGINT NOT NULL,\n" //
            + "central_revision BIGINT,\n" //
            + "unique(table_name, local_revision))";

    static final String SYNCH_CONFLICTS = "synch_conflicts";

    // conflict can be in 'c', conflict, 'r', resolved or 'm', clean merge state
    // clean merge is a marker stating that the same change occurred both locally
    // and in central, and as such it should not be reported in GetDiff
    static final String SYNCH_CONFLICTS_CREATION = "CREATE TABLE synch_conflicts(\n"
            + "id SERIAL PRIMARY KEY,\n" + "table_name VARCHAR(256) NOT NULL,\n" // 
            + "feature_id UUID NOT NULL,\n" //
            + "local_revision BIGINT NOT NULL,\n" //  
            + "date_created TIMESTAMP NOT NULL,\n" // 
            + "state CHAR(1) NOT NULL CHECK (state in ('c', 'r', 'm')),\n" //  
            + "date_resolved TIMESTAMP,\n" //
            + "local_feature TEXT,\n" // 
            + "unique(table_name, feature_id, local_revision))";

    FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);

    Catalog catalog;

    GSSInfo info;

    GSSConfiguration configuration;

    public DefaultGeoServerSynchronizationService(GeoServer geoServer,
            GSSConfiguration configuration) {
        this.catalog = geoServer.getCatalog();
        this.info = geoServer.getService(GSSInfo.class);
        this.configuration = configuration;
    }

    /**
     * Checks the module is ready to be used. TODO: move this to a listener so that we don't do all
     * the ckecks for every request
     */
    void ensureEnabled() {
        // basic sanity checks on the config
        if (info == null) {
            throw new GSSException("The service is not properly configured, gssInfo not found");
        }

        if (info.getMode() == null) {
            throw new GSSException("The gss mode has not been configured");
        }

        if (info.getVersioningDataStore() == null || !info.getVersioningDataStore().isEnabled()) {
            throw new GSSException("The service is disabled as the "
                    + "versioning datastore is not available/disabled");
        }

        FeatureIterator<SimpleFeature> fi = null;
        try {
            // basic sanity checks on the datastore
            DataAccess ds = info.getVersioningDataStore().getDataStore(null);
            if (!(ds instanceof VersionedPostgisDataStore)) {
                throw new GSSException(
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
            throw new GSSException("A problem occurred while checking the versioning store", e);
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
            throw new GSSException("gss configured in Central mode, won't to Unit service calls");
        }
    }

    /**
     * Responds a GetCentralRevision reuest
     */
    public CentralRevisionsType getCentralRevision(GetCentralRevisionType request) {
        ensureUnitEnabled();

        try {
            CentralRevisionsType cr = new CentralRevisionsType();
            for (QName typeName : request.getTypeNames()) {
                checkSyncronized(typeName);
                SimpleFeature record = getLastSynchronizationRecord(typeName);
                long revision = -1;
                if (record != null) {
                    revision = ((Number) record.getAttribute("central_revision")).longValue();
                }
                cr.getLayerRevisions().add(new LayerRevision(typeName, revision));
            }

            return cr;
        } catch (IOException e) {
            throw new GSSException("Could not compute the response", e);
        }
    }

    /**
     * Checks the feature type exists, it's actually synchronized, and returns the last known
     * central revision
     * 
     * @param typeName
     * @return
     */
    SimpleFeature getLastSynchronizationRecord(QName typeName) throws IOException {
        FeatureIterator<SimpleFeature> fi = null;
        try {
            // get the versioning data store
            VersioningDataStore ds = (VersioningDataStore) info.getVersioningDataStore()
                    .getDataStore(null);

            // gather the record from the synch history table
            DefaultQuery q = new DefaultQuery();
            q.setFilter(ff.equal(ff.property("table_name"), ff.literal(typeName.getLocalPart()),
                    true));
            q.setSortBy(new SortBy[] { ff.sort("central_revision", SortOrder.DESCENDING) });
            q.setMaxFeatures(1);
            fi = ds.getFeatureSource(SYNCH_HISTORY).getFeatures(q).features();

            if (fi.hasNext()) {
                return fi.next();
            } else {
                return null;
            }
        } finally {
            if (fi != null) {
                fi.close();
            }
        }
    }

    /**
     * Check the typeName exists and is one of the synchronised tables
     * 
     * @param typeName
     * @throws IOException
     */
    void checkSyncronized(QName typeName) throws IOException {
        NamespaceInfo ns = catalog.getNamespaceByURI(typeName.getNamespaceURI());
        if (ns == null) {
            ns = catalog.getNamespaceByPrefix(typeName.getPrefix());
        }
        if (ns == null) {
            throw new GSSException("Could not locate typeName: " + typeName,
                    GSSExceptionCode.InvalidParameterValue, TYPE_NAME);
        }

        FeatureTypeInfo fti = catalog.getFeatureTypeByName(ns, typeName.getLocalPart());
        if (fti == null) {
            throw new GSSException("Could not locate typeName: " + typeName,
                    GSSExceptionCode.InvalidParameterValue, TYPE_NAME);
        }

        // get the versioning data store
        VersioningDataStore ds = (VersioningDataStore) info.getVersioningDataStore().getDataStore(
                null);

        // check the table is actually synch-ed
        DefaultQuery q = new DefaultQuery();
        q.setFilter(ff.equal(ff.property("table_name"), ff.literal(fti.getName()), true));
        int count = ds.getFeatureSource(SYNCH_TABLES).getCount(q);
        if (count == 0) {
            throw new GSSException(fti.getName() + " is not a synchronized layer",
                    GSSExceptionCode.InvalidParameterValue, TYPE_NAME);
        }
    }

    public PostDiffResponseType postDiff(PostDiffType request) {
        ensureUnitEnabled();

        Transaction transaction = new DefaultTransaction();
        try {
            // check the layer is actually shared and get the latest known central revision
            checkSyncronized(request.getTypeName());
            SimpleFeature record = getLastSynchronizationRecord(request.getTypeName());
            long lastCentralRevision = -1;
            long lastLocalRevision = -1;
            if (record != null) {
                lastCentralRevision = ((Number) record.getAttribute("central_revision"))
                        .longValue();
                lastLocalRevision = ((Number) record.getAttribute("local_revision")).longValue();
            }

            if (request.getFromVersion() != lastCentralRevision) {
                throw new GSSException("Invalid fromVersion, it should be " + lastCentralRevision,
                        GSSExceptionCode.InvalidParameterValue, FROM_VERSION);
            }

            if (request.getFromVersion() > request.getToVersion()) {
                throw new GSSException("Invalid toVersion, it should be higher than "
                        + request.getToVersion(), GSSExceptionCode.InvalidParameterValue,
                        TO_VERSION);
            }

            // make sure all of the changes are applied in one hit, or none
            // very important, make sure all versioning writes use the same transaction or they
            // will deadlock each other

            String tableName = request.getTypeName().getLocalPart();
            VersioningDataStore ds = (VersioningDataStore) info.getVersioningDataStore()
                    .getDataStore(null);

            // grab the feature stores and bind them all to the same transaction
            VersioningFeatureStore conflicts = (VersioningFeatureStore) ds
                    .getFeatureSource(SYNCH_CONFLICTS);
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

            // apply changes
            TransactionType changes = request.getTransaction();
            if (changes != null) {
                List<DeleteElementType> deletes = changes.getDelete();
                List<UpdateElementType> updates = changes.getUpdate();
                List<InsertElementType> inserts = changes.getInsert();

                // We need to find conflicts: local changes occurred since last synchronisation
                // that hit the same features contained in this changeset. For those we need
                // to create a conflict record and revert the local changes so that we
                // can apply the central ones
                Set<FeatureId> deletedFids = getEObjectFids(deletes);
                Set<FeatureId> updatedFids = getEObjectFids(updates);
                Set<FeatureId> changedFids = new HashSet<FeatureId>();
                changedFids.addAll(deletedFids);
                changedFids.addAll(updatedFids);
                // limit the changeset to the window between the last and the current
                // synchronization
                String lastLocalRevisionId = lastLocalRevision != -1 ? String
                        .valueOf(lastLocalRevision) : "FIRST";
                String newLocalRevisionId = String.valueOf(newLocalRevision);
                FeatureDiffReader localChanges = fs.getDifferences(lastLocalRevisionId,
                        newLocalRevisionId, ff.id(changedFids), null);
                while (localChanges.hasNext()) {
                    FeatureDiff fd = localChanges.next();
                    FeatureId diffFeatureId = ff.featureId(fd.getID());
                    if (fd.getState() == FeatureDiff.INSERTED) {
                        throw new GSSException(
                                "A new locally inserted feature has the same "
                                        + "id as a modified feature coming from Central, this is impossible, "
                                        + "there is either a bug in ID generation or someone manually tampered with it!");
                    } else if (fd.getState() == FeatureDiff.DELETED) {
                        if (deletedFids.contains(diffFeatureId)) {
                            saveCleanMergeMarker(fs, conflicts, lastLocalRevisionId,
                                    newLocalRevision, fd.getID());
                        } else {
                            handleDeletionConflict(fs, conflicts, lastLocalRevisionId,
                                    newLocalRevision, fd.getID());
                        }
                    } else {
                        if (updatedFids.contains(diffFeatureId)) {
                            if (isSameUpdate(fd, findUpdate(fd.getID(), updates))) {
                                saveCleanMergeMarker(fs, conflicts, lastLocalRevisionId,
                                        newLocalRevision, fd.getID());
                            } else {
                                handleUpdateConflict(fs, conflicts, lastLocalRevisionId,
                                        newLocalRevision, fd.getID());
                            }
                        } else {
                            handleUpdateConflict(fs, conflicts, lastLocalRevisionId,
                                    newLocalRevision, fd.getID());
                        }
                    }
                }

                // not that conflicting local changes have been moved out of the way, apply the
                // central ones
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
            
            if(t instanceof GSSException) {
                throw (GSSException) t;
            } else {
                new GSSException("Error occurred while applyling the diff", t);
            }
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

    public GetDiffResponseType getDiff(GetDiffType request) {
        FeatureIterator<SimpleFeature> fi = null;
        try {
            checkSyncronized(request.getTypeName());
            SimpleFeature record = getLastSynchronizationRecord(request.getTypeName());
            if (record == null) {
                throw new GSSException(
                        "Out of order invocation, no PostDiff was called before GetDiff",
                        GSSExceptionCode.InvalidParameterValue, FROM_VERSION);
            }

            // the local revision at the last server update
            long lastPostRevision = (Long) record.getAttribute("local_revision");
            if (request.getFromVersion() > lastPostRevision) {
                throw new GSSException(
                        "Invalid fromVersion, it's more recent than the latest PostDiff synchronisation",
                        GSSExceptionCode.InvalidParameterValue, FROM_VERSION);
            }

            // ok, we need to find what revisions we have to jump over (the synch ones)
            VersioningDataStore ds = (VersioningDataStore) info.getVersioningDataStore()
                    .getDataStore(null);

            // gather all records in the synch history that happened after the requested revision
            DefaultQuery q = new DefaultQuery();
            String tableName = request.getTypeName().getLocalPart();
            Filter tableFilter = ff.equal(ff.property("table_name"), ff.literal(tableName), true);
            Filter revisionFilter = ff.greater(ff.property("local_revision"), ff.literal(request
                    .getFromVersion()));
            q.setFilter(ff.and(tableFilter, revisionFilter));
            q.setSortBy(new SortBy[] { ff.sort("local_revision", SortOrder.ASCENDING) });
            fi = ds.getFeatureSource(SYNCH_HISTORY).getFeatures(q).features();

            // build a list so that taking elements pair-wise we get the intervals we need to query
            // (we won't sent local changes happened after the last PostDiff as there is no way
            // to know if they would conflict with Central or not)
            List<Long> intervals = new ArrayList<Long>();
            intervals.add(request.getFromVersion());
            while (fi.hasNext()) {
                intervals.add((Long) fi.next().getAttribute("local_revision"));
            }
            fi.close();
            
            // gather the ids of the features still under conflict, we don't want to load their diffs
            Filter nonConflictingFilter = getFidConflictFilter(tableName, getActiveConflicts(tableName));

            // gather all of the diff readers for the non conflicting features
            VersioningFeatureSource fs = (VersioningFeatureSource) ds.getFeatureSource(tableName);
            FeatureDiffReader[] readers = new FeatureDiffReader[intervals.size() - 1];
            for (int i = 1; i < intervals.size(); i++) {
                // mind we need to skip the actual synch points, so we subtract 1
                // from the revision number
                String fromVersion = String.valueOf(intervals.get(i - 1));
                String toVersion = String.valueOf(intervals.get(i) - 1);

                Filter filter = nonConflictingFilter;
                // skip over all the clean merges
                Filter cleanMerges = getFidConflictFilter(tableName, getCleanMerges(tableName, intervals.get(i)));
                if(cleanMerges != Filter.INCLUDE) {
                    if(filter != Filter.INCLUDE) {
                        filter = ff.and(cleanMerges, filter);
                    } else {
                        filter = cleanMerges;
                    }
                } 
                
                readers[i - 1] = fs.getDifferences(fromVersion, toVersion, filter, null);
            }

            // now we need to merge the readers into a global set of changes, skip
            // the changes on the conflicting features, and turn everything into a
            // transaction (easy no?)
            FeatureDiffReader differences = new MergingFeatureDiffReader(readers);
            TransactionType transaction = new VersioningTransactionConverter().convert(differences,
                    TransactionType.class);
            transaction.setReleaseAction(null);
            transaction.setVersion(null);
            transaction.setService(null);

            GetDiffResponseType response = new GetDiffResponseType();
            response.setFromVersion(request.getFromVersion());
            response.setToVersion(lastPostRevision);
            response.setTypeName(request.getTypeName());
            response.setTransaction(transaction);
            return response;
        } catch (IOException e) {
            throw new GSSException("Failed to compute the GetDiff response", e);
        } finally {
            if (fi != null) {
                fi.close();
            }
        }
    }
    
    /**
     * Gathers all of the conflicts ids and builds a fid filter excluding all of them
     * @return
     */
    Filter getFidConflictFilter(String tableName, FeatureCollection conflicts) {
        FeatureIterator<SimpleFeature> fi = null; 
        try {
            fi = conflicts.features();
            Set<FeatureId> conflictIds = new HashSet<FeatureId>();
            while (fi.hasNext()) {
                conflictIds.add(ff.featureId(tableName + "." + fi.next().getAttribute("feature_id")));
            }
            fi.close();
            return conflictIds.size() > 0 ? ff.not(ff.id(conflictIds)) : Filter.INCLUDE;
        } finally {
            if(fi != null) {
                fi.close();
            }
        }
    }

    /**
     * Returns true if the feature diff and the update element would apply the same change
     */
    boolean isSameUpdate(FeatureDiff fd, UpdateElementType update) {
        List<PropertyType> updateProperties = update.getProperty();
        Set<String> fdAttributes = new HashSet<String>(fd.getChangedAttributes());
        if (updateProperties.size() != fdAttributes.size()) {
            return false;
        }

        for (PropertyType pt : updateProperties) {
            String attName = pt.getName().getLocalPart();
            if (!fdAttributes.contains(attName)) {
                return false;
            }

            // compare the values (mind, the upValue comes from a parser, might
            // not be the right type, use converters)
            Object fdValue = fd.getFeature().getAttribute(attName);
            Object upValue = pt.getValue();

            if (fdValue == null && upValue == null) {
                continue;
            } else if (fdValue != null && upValue != null) {
                Class target = fd.getFeature().getType().getDescriptor(attName).getType()
                        .getBinding();
                upValue = Converters.convert(upValue, target);
                if (upValue == null) {
                    // could not perform a conversion to the target type, evidently not equal
                    return false;
                }
                // ok, same type, they should be comparable now
                if (!fdValue.equals(upValue)) {
                    return false;
                }
            } else {
                // one is null, the other is not
                return false;
            }
        }

        // did we really manage to go thru all those checks? Wow, it's the same change all right
        return true;
    }

    /**
     * Finds the update element that's modifying a certain id (assuming the updates are using ID
     * filters)
     */
    UpdateElementType findUpdate(String featureId, List<UpdateElementType> updates) {
        for (UpdateElementType update : updates) {
            Set<Identifier> ids = ((Id) update.getFilter()).getIdentifiers();
            for (Identifier id : ids) {
                if (id.toString().equals(featureId)) {
                    return update;
                }
            }
        }
        return null;
    }

    /**
     * Rolls back the locally deleted feature and store a conflict record
     * 
     * @param lastLocalRevisionId
     * @param id
     */
    void handleDeletionConflict(VersioningFeatureStore layer, VersioningFeatureStore conflicts,
            String lastLocalRevisionId, long newLocalRevision, String id) throws IOException {
        // create a conflict feature. For local deletions we just don't store the feature
        SimpleFeature conflict = SimpleFeatureBuilder.build(conflicts.getSchema(), new Object[] {
                layer.getSchema().getTypeName(), // table_name
                id.substring(id.lastIndexOf(".") + 1), // feature uuid
                newLocalRevision, // local revision
                new Date(), // date created
                'c', // not resolved, conflict
                null, // date fixed
                null // local feature, none since it was locally removed
                }, null);
        conflicts.addFeatures(collection(conflict));

        // roll back the local changes
        Id filter = ff.id(Collections.singleton(ff.featureId(id)));
        layer.rollback(lastLocalRevisionId, filter, null);
    }

    /**
     * Stores a clean merge marker
     * 
     * @param lastLocalRevisionId
     * @param id
     */
    void saveCleanMergeMarker(VersioningFeatureStore layer, VersioningFeatureStore conflicts,
            String lastLocalRevisionId, long newLocalRevision, String id) throws IOException {
        // create a conflict feature. For local deletions we just don't store the feature
        SimpleFeature conflict = SimpleFeatureBuilder.build(conflicts.getSchema(), new Object[] {
                layer.getSchema().getTypeName(), // table_name
                id.substring(id.lastIndexOf(".") + 1), // feature uuid
                newLocalRevision, // local revision
                new Date(), // date created
                'm', // clean merge marker
                null, // date fixed
                null // local feature, none since it was locally removed
                }, null);
        conflicts.addFeatures(collection(conflict));
    }

    /**
     * Rolls back the locally modified feature and store a conflict record
     * 
     * @param lastLocalRevisionId
     * @param id
     */
    void handleUpdateConflict(VersioningFeatureStore layer, VersioningFeatureStore conflicts,
            String lastLocalRevisionId, long newLocalRevision, String id) throws IOException {
        // create a conflict feature. For local deletions we just don't store the feature
        SimpleFeature conflict = SimpleFeatureBuilder.build(conflicts.getSchema(), new Object[] {
                layer.getSchema().getTypeName(), // table_name
                id.substring(id.lastIndexOf(".") + 1), // feature uuid
                newLocalRevision, // local revision
                new Date(), // date created
                'c', // resolved?
                null, // date fixed
                toGML3(getFeatureById(layer, id)) // local feature value
                }, null);
        conflicts.addFeatures(collection(conflict));

        // roll back the local changes
        Id filter = ff.id(Collections.singleton(ff.featureId(id)));
        layer.rollback(lastLocalRevisionId, filter, null);
    }

    /**
     * Converts a simple feature to a GML3 representation
     * 
     * @param featureById
     * @return
     * @throws IOException
     */
    String toGML3(SimpleFeature feature) throws IOException {
        Encoder encoder = new Encoder(configuration, configuration.getXSD().getSchema());
        NamespaceInfo nsi = catalog
                .getNamespaceByURI(feature.getType().getName().getNamespaceURI());
        if (nsi != null) {
            encoder.getNamespaces().declarePrefix(nsi.getPrefix(), nsi.getURI());
        }
        encoder.setEncoding(Charset.forName("UTF-8"));
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        encoder.encode(feature, GML._Feature, bos);
        return bos.toString("UTF-8");
    }

    /**
     * Parses the representation of a GML3 feature back into a {@link SimpleFeature}
     * 
     * @param gml3
     * @return
     * @throws IOException
     */
    SimpleFeature fromGML3(String gml3) throws IOException {
        try {
            Parser parser = new Parser(configuration);
            parser.setStrict(false);
            return (SimpleFeature) parser.parse(new StringReader(gml3));
        } catch (ParserConfigurationException e) {
            throw (IOException) new IOException("Failure parsing the feature").initCause(e);
        } catch (SAXException e) {
            throw (IOException) new IOException("Failure parsing the feature").initCause(e);
        }
    }

    /**
     * Returns the fids of the features being modified by this {@link UpdateElementType} or by this
     * {@link DeleteElementType} The code assumes changes only contain fid filters (we know that
     * since the versioning datastore only generates that kind of filter in diff transactions)
     */
    Set<FeatureId> getEObjectFids(List<? extends EObject> objects) {
        Set<FeatureId> ids = new HashSet<FeatureId>();
        for (EObject object : objects) {
            Filter f = (Filter) EMFUtils.get(object, "filter");
            if (!(f instanceof Id)) {
                throw new GSSException("Unexpected filter type, GSS can only handle FID filters");
            }

            for (Identifier id : ((Id) f).getIdentifiers()) {
                ids.add((FeatureId) id);
            }
        }

        return ids;
    }

    /**
     * Retrieves a single feature by id from the feature source. Returns null if no feature with
     * that id is found
     */
    SimpleFeature getFeatureById(FeatureSource<SimpleFeatureType, SimpleFeature> fs, String id)
            throws IOException {
        Filter filter = ff.id(Collections.singleton(ff.featureId(id)));
        FeatureIterator<SimpleFeature> fi = null;
        try {
            fi = fs.getFeatures(filter).features();
            if (fi.hasNext()) {
                return fi.next();
            } else {
                return null;
            }
        } finally {
            fi.close();
        }
    }

    /**
     * Returns all the active conflicts for the specified table
     */
    FeatureCollection<SimpleFeatureType, SimpleFeature> getActiveConflicts(String tableName)
            throws IOException {
        VersioningDataStore ds = (VersioningDataStore) info.getVersioningDataStore().getDataStore(
                null);
        VersioningFeatureSource conflicts = (VersioningFeatureSource) ds
                .getFeatureSource(SYNCH_CONFLICTS);

        Filter unresolved = ff.equals(ff.property("state"), ff.literal(String.valueOf('c')));
        Filter tableFilter = ff.equals(ff.property("table_name"), ff.literal(tableName));
        return conflicts.getFeatures(ff.and(unresolved, tableFilter));
    }
    
    /**
     * Returns the clean merges occurred at the specified revision
     */
    FeatureCollection<SimpleFeatureType, SimpleFeature> getCleanMerges(String tableName, long revision)
            throws IOException {
        VersioningDataStore ds = (VersioningDataStore) info.getVersioningDataStore().getDataStore(
                null);
        VersioningFeatureSource conflicts = (VersioningFeatureSource) ds
                .getFeatureSource(SYNCH_CONFLICTS);

        Filter unresolved = ff.equals(ff.property("state"), ff.literal(String.valueOf('m')));
        Filter tableFilter = ff.equals(ff.property("table_name"), ff.literal(tableName));
        Filter version = ff.equals(ff.property("local_revision"), ff.literal(revision));
        return conflicts.getFeatures(ff.and(ff.and(unresolved, tableFilter), version));
    }

}
