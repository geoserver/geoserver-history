/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.gss;

import static org.geoserver.gss.GSSCore.*;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.opengis.wfs.TransactionType;

import org.geoserver.catalog.Catalog;
import org.geoserver.config.GeoServer;
import org.geoserver.gss.GSSInfo.GSSMode;
import org.geoserver.wfsv.VersioningTransactionConverter;
import org.geotools.data.DefaultQuery;
import org.geotools.data.DefaultTransaction;
import org.geotools.data.FeatureDiffReader;
import org.geotools.data.FeatureSource;
import org.geotools.data.Transaction;
import org.geotools.data.VersioningDataStore;
import org.geotools.data.VersioningFeatureStore;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.FeatureIterator;
import org.geotools.util.logging.Logging;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.sort.SortBy;
import org.opengis.filter.sort.SortOrder;

/**
 * This object gets periodically invoked to perform all outstanding layer synchronisations with the
 * units
 * 
 * @author Andrea Aime - OpenGeo
 */
public class SynchronizationManager extends TimerTask {

    static final Logger LOGGER = Logging.getLogger(DefaultGeoServerSynchronizationService.class);

    FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);

    Catalog catalog;

    GSSInfo info;

    GSSCore core;

    GSSClientFactory clientFactory;

    public SynchronizationManager(GeoServer geoServer, GSSClientFactory clientFactory) {
        this.catalog = geoServer.getCatalog();
        this.info = geoServer.getService(GSSInfo.class);
        this.core = new GSSCore(info);
        this.clientFactory = clientFactory;
    }

    /**
     * Runs the synchronisation. To be used by {@link Timer}, if you need to manually run the
     * synchronization please invoke {@link #synchronizeOustandlingLayers()} instead.
     */
    @Override
    public void run() {
        try {
            synchronizeOustandlingLayers();
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error occurred while running the scheduled synchronisation",
                    e);
        }
    }

    /**
     * Runs the synchronisation on all unit layers that require it (all the ones that haven't
     * synchronised according to the requested frequency and that are inside the call window)
     * 
     * @throws IOException
     */
    public void synchronizeOustandlingLayers() throws IOException {
        if (info.getMode() != GSSMode.Central) {
            return;
        }
        
        LOGGER.info("Performing scheduled synchronisation");

        // make sure we're running in "Central" mode
        core.ensureCentralEnabled();

        FeatureIterator<SimpleFeature> fi = null;
        FeatureIterator<SimpleFeature> li = null;
        Transaction transaction = null;
        try {
            // grab the layers to be synchronised
            VersioningDataStore ds = (VersioningDataStore) info.getVersioningDataStore()
                    .getDataStore(null);
            FeatureSource<SimpleFeatureType, SimpleFeature> outstanding = ds
                    .getFeatureSource(SYNCH_OUTSTANDING);
            DefaultQuery q = new DefaultQuery(SYNCH_OUTSTANDING);
            q.setSortBy(new SortBy[] { ff.sort("last_synchronization", SortOrder.ASCENDING) });
            fi = outstanding.getFeatures(q).features();

            while (fi.hasNext()) {
                // extract relevant attributes
                SimpleFeature layer = fi.next();
                String layerName = (String) layer.getAttribute("table_name");
                String address = (String) layer.getAttribute("unit_address");
                String user = (String) layer.getAttribute("synch_user");
                String password = (String) layer.getAttribute("synch_password");
                Long getDiffCentralRevision = (Long) layer.getAttribute("getdiff_central_revision");
                Long lastUnitRevision = (Long) layer.getAttribute("last_unit_revision");

                // get the last central revision the client knows about
                GSSClient client = getClient(address, user, password);
                long clientCentralRevision = client.getCentralRevision(layerName);

                // compute the diff that we have to send the client. Notice that we have
                // to skip over the local change occurred when we last performed a GetDiff
                // against the client
                VersioningFeatureStore fs = (VersioningFeatureStore) ds.getFeatureSource(layerName);
                transaction = new DefaultTransaction();
                fs.setTransaction(transaction);
                String fromRevision = clientCentralRevision == -1 ? "FIRST" : String
                        .valueOf(clientCentralRevision);
                TransactionType changes;
                if (getDiffCentralRevision == null) {
                    // first time
                    FeatureDiffReader fdr = fs.getDifferences(fromRevision, "LAST", null, null);
                    changes = new VersioningTransactionConverter().convert(fdr,
                            TransactionType.class);
                } else {
                    // we need to jump over the last local changes
                    String before = String.valueOf(getDiffCentralRevision - 1);
                    String after = String.valueOf(getDiffCentralRevision);
                    FeatureDiffReader fdr1 = fs.getDifferences(fromRevision, before, null, null);
                    FeatureDiffReader fdr2 = fs.getDifferences(after, "LAST", null, null);
                    FeatureDiffReader[] fdr = new FeatureDiffReader[] { fdr1, fdr2 };
                    changes = new VersioningTransactionConverter().convert(fdr,
                            TransactionType.class);
                }

                // what is the latest change on this layer? (worst case it's the last GetDiff from
                // this Unit)
                long lastRevision = -1;
                li = fs.getLog("LAST", fromRevision, null, null, 1).features();
                if (li.hasNext()) {
                    lastRevision = (Long) li.next().getAttribute("revision");
                }
                li.close();

                // finally run the PostDiff
                client.postDiff(layerName, clientCentralRevision, lastRevision, changes);

                // grab the changes from the client and apply them locally
                TransactionType tt = client.getDiff(layerName, lastUnitRevision == null ? -1
                        : lastRevision);
                core.applyChanges(changes, fs);

                // close up
                transaction.commit();
                transaction.close();
            }
        } finally {
            if (fi != null) {
                fi.close();
            }
            if (li != null) {
                li.close();
            }
            if (transaction != null) {
                transaction.close();
            }
        }

    }

    protected GSSClient getClient(String address, String username, String password)
            throws MalformedURLException {
        return clientFactory.createClient(new URL(address), username, password);
    }

}
