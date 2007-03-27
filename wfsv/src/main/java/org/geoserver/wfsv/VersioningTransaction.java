/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.wfsv;

import net.opengis.wfs.InsertedFeatureType;
import net.opengis.wfs.TransactionResponseType;
import net.opengis.wfs.TransactionType;
import net.opengis.wfs.WfsFactory;
import net.opengis.wfsv.DifferenceQueryType;
import net.opengis.wfsv.RollbackType;
import org.eclipse.emf.ecore.EObject;
import org.geoserver.wfs.Transaction;
import org.geoserver.wfs.TransactionElementHandler;
import org.geoserver.wfs.WFS;
import org.geoserver.wfs.WFSException;
import org.geoserver.wfs.WFSTransactionException;
import org.geotools.data.DefaultTransaction;
import org.geotools.data.FeatureStore;
import org.geotools.data.VersioningFeatureSource;
import org.geotools.data.VersioningFeatureStore;
import org.geotools.data.postgis.FeatureDiff;
import org.geotools.data.postgis.FeatureDiffReader;
import org.geotools.data.postgis.VersionedPostgisDataStore;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.Feature;
import org.geotools.feature.FeatureCollection;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;
import org.opengis.go.CommonFactory;
import org.vfny.geoserver.global.Data;
import org.vfny.geoserver.global.FeatureTypeInfo;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.xml.namespace.QName;


/**
 * Extends the base transaction to handle extended versioning elements
 *
 * @author Andrea Aime, TOPP
 */
public class VersioningTransaction extends Transaction {
    public VersioningTransaction(WFS wfs, Data catalog) {
        super(wfs, catalog);
        transactionElementHandlers.add(new RollbackElementHandler());
    }

    protected DefaultTransaction getDatastoreTransaction(TransactionType request)
        throws IOException {
        DefaultTransaction transaction = new DefaultTransaction();
        // use handle as the log messages
        transaction.putProperty(VersionedPostgisDataStore.AUTHOR, "unknown");
        transaction.putProperty(VersionedPostgisDataStore.MESSAGE, request.getHandle());

        return transaction;
    }

    public class RollbackElementHandler implements TransactionElementHandler {
        public void checkValidity(EObject element, Map featureTypeInfos)
            throws WFSTransactionException {
            // let's check we can perfom inserts, updates and deletes
            if ((wfs.getServiceLevel() & WFS.SERVICE_INSERT) == 0) {
                throw new WFSException("Transaction INSERT support is not enabled "
                    + "(required for rollback)");
            }

            if ((wfs.getServiceLevel() & WFS.SERVICE_UPDATE) == 0) {
                throw new WFSException("Transaction UPDATE support is not enabled "
                    + "(required for rollback)");
            }

            if ((wfs.getServiceLevel() & WFS.SERVICE_DELETE) == 0) {
                throw new WFSException("Transaction DELETE support is not enabled "
                    + "(required for rollback)");
            }

            // then, make sure we're hitting a versioning datastore
            RollbackType rollback = (RollbackType) element;
            FeatureTypeInfo info = (FeatureTypeInfo) featureTypeInfos.get(rollback.getDifferenceQuery()
                                                                                  .getTypeName());

            try {
                if (!(info.getFeatureSource() instanceof VersioningFeatureSource)) {
                    throw new WFSTransactionException("Cannot perform a rollback on "
                        + info.getTypeName() + " since the backing data store is not versioning",
                        "", rollback.getHandle());
                }
            } catch (IOException e) {
                throw new WFSTransactionException("Cannot get the feature source for feature type "
                    + info.getTypeName(), e, rollback.getHandle());
            }

            String fromVersion = rollback.getDifferenceQuery().getFromFeatureVersion();

            // finally, we check we support this rollback
            if ((fromVersion != null) && !fromVersion.equals("LAST")) {
                throw new WFSTransactionException("We don't support rolling back "
                    + "from a version other than the last one at the moment");
            }

            // TODO: we should check the user attribute, which is missing,
            // because we don't
            // support that one neither
        }

        public void execute(EObject element, TransactionType request, Map featureStores,
            TransactionResponseType response) throws WFSTransactionException {
            FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);
            RollbackType rollback = (RollbackType) element;
            DifferenceQueryType dq = rollback.getDifferenceQuery();
            VersioningFeatureStore vstore = (VersioningFeatureStore) featureStores.get(dq
                    .getTypeName());
            long inserted = response.getTransactionSummary().getTotalInserted().longValue();
            long updated = response.getTransactionSummary().getTotalUpdated().longValue();
            long deleted = response.getTransactionSummary().getTotalDeleted().longValue();

            FeatureDiffReader reader = null;

            try {
                // we use the difference to compute the number of inserted,
                // updated and deleted
                // features, but we can't use these to actually perform the
                // rollback, since
                // we would be unable to preserve the fids of the ones that were
                // deleted and
                // need to be re-inserted
                Filter filter = (dq.getFilter() != null) ? (Filter) dq.getFilter() : Filter.INCLUDE;
                String fromVersion = dq.getFromFeatureVersion();
                String toVersion = dq.getToFeatureVersion();
                reader = vstore.getDifferences(fromVersion, toVersion, filter);

                while (reader.hasNext()) {
                    FeatureDiff fd = reader.next();

                    if (fd.getState() == FeatureDiff.INSERTED) {
                        inserted++;

                        InsertedFeatureType insertedFeature = WfsFactory.eINSTANCE
                            .createInsertedFeatureType();
                        insertedFeature.setHandle(rollback.getHandle());
                        insertedFeature.getFeatureId().add(filterFactory.featureId(fd.getID()));
                        response.getInsertResults().getFeature().add(insertedFeature);
                    } else if (fd.getState() == FeatureDiff.UPDATED) {
                        updated++;
                        System.out.println("Updated : " + fd.getID());
                        System.out.println("Updated : " + fd.getState());
                    } else if (fd.getState() == FeatureDiff.DELETED) {
                        deleted++;
                    }
                }

                try {
                    vstore.rollback(dq.getToFeatureVersion(), (Filter) dq.getFilter());
                } catch (IOException e) {
                    throw new WFSTransactionException("Could not perform the rollback", e,
                        rollback.getHandle());
                }

                // update summary information
                response.getTransactionSummary().setTotalInserted(BigInteger.valueOf(inserted));
                response.getTransactionSummary().setTotalUpdated(BigInteger.valueOf(updated));
                response.getTransactionSummary().setTotalDeleted(BigInteger.valueOf(deleted));
            } catch (IOException e) {
                throw new WFSTransactionException("Could not perform the rollback", e,
                    rollback.getHandle());
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        //
                    }
                }
            }
        }

        public Class getElementClass() {
            return RollbackType.class;
        }

        public QName[] getTypeNames(EObject element) throws WFSTransactionException {
            RollbackType rollback = (RollbackType) element;

            return new QName[] { (QName) rollback.getDifferenceQuery().getTypeName() };
        }
    }
}
