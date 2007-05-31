package org.geoserver.wfsv;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Map;

import javax.xml.namespace.QName;

import net.opengis.wfs.InsertedFeatureType;
import net.opengis.wfs.TransactionResponseType;
import net.opengis.wfs.TransactionType;
import net.opengis.wfs.WfsFactory;
import net.opengis.wfsv.RollbackType;

import org.eclipse.emf.ecore.EObject;
import org.geoserver.wfs.TransactionElementHandler;
import org.geoserver.wfs.WFS;
import org.geoserver.wfs.WFSException;
import org.geoserver.wfs.WFSTransactionException;
import org.geotools.data.VersioningFeatureSource;
import org.geotools.data.VersioningFeatureStore;
import org.geotools.data.postgis.FeatureDiff;
import org.geotools.data.postgis.FeatureDiffReader;
import org.geotools.factory.CommonFactoryFinder;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;
import org.vfny.geoserver.global.FeatureTypeInfo;

/**
 * Handles the extended rollback elements
 * 
 * @author Andrea Aime - TOPP
 */
public class RollbackElementHandler implements TransactionElementHandler {

    private WFS wfs;

    private FilterFactory filterFactory;

    public RollbackElementHandler(WFS wfs, FilterFactory filterFactory) {
        this.wfs = wfs;
        this.filterFactory = filterFactory;
    }

    public void checkValidity(EObject element, Map featureTypeInfos) throws WFSTransactionException {
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
        FeatureTypeInfo info = (FeatureTypeInfo) featureTypeInfos.get(rollback.getTypeName());

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

        // TODO: we should check the user attribute, but for the moment
        // we don't have an authentication subsystem
    }

    public void execute(EObject element, TransactionType request, Map featureStores,
            TransactionResponseType response) throws WFSTransactionException {
        FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);
        RollbackType rollback = (RollbackType) element;
        VersioningFeatureStore vstore = (VersioningFeatureStore) featureStores.get(rollback
                .getTypeName());
        long inserted = response.getTransactionSummary().getTotalInserted().longValue();
        long updated = response.getTransactionSummary().getTotalUpdated().longValue();
        long deleted = response.getTransactionSummary().getTotalDeleted().longValue();

        FeatureDiffReader reader = null;

        try {
            // we use the difference to compute the number of inserted,
            // updated and deleted features, but we can't use these to
            // actually perform the rollback, since we would be unable to
            // preserve the fids of the ones that were deleted and need to
            // be re-inserted
            Filter filter = (rollback.getFilter() != null) ? (Filter) rollback.getFilter()
                    : Filter.INCLUDE;
            String version = rollback.getToFeatureVersion();
            reader = vstore.getDifferences(null, version, filter);

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
                String user = rollback.getUser();
                String[] users = ((user != null) && !user.trim().equals("")) ? new String[] { user }
                        : null;
                vstore.rollback(version, (Filter) rollback.getFilter(), users);
            } catch (IOException e) {
                throw new WFSTransactionException("Could not perform the rollback", e, rollback
                        .getHandle());
            }

            // update summary information
            response.getTransactionSummary().setTotalInserted(BigInteger.valueOf(inserted));
            response.getTransactionSummary().setTotalUpdated(BigInteger.valueOf(updated));
            response.getTransactionSummary().setTotalDeleted(BigInteger.valueOf(deleted));
        } catch (IOException e) {
            throw new WFSTransactionException("Could not perform the rollback", e, rollback
                    .getHandle());
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

        return new QName[] { (QName) rollback.getTypeName() };
    }
}
