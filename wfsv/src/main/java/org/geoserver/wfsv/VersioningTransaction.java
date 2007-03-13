package org.geoserver.wfsv;

import java.io.IOException;

import net.opengis.wfs.TransactionType;

import org.geoserver.wfs.Transaction;
import org.geoserver.wfs.WFS;
import org.geotools.data.DefaultTransaction;
import org.geotools.data.postgis.VersionedPostgisDataStore;
import org.vfny.geoserver.global.Data;

/**
 * Extends the base transaction to handle extended versioning elements
 * @author Andrea Aime, TOPP
 */
public class VersioningTransaction extends Transaction {

    public VersioningTransaction(WFS wfs, Data catalog) {
        super(wfs, catalog);
    }

    protected DefaultTransaction getDatastoreTransaction(TransactionType request) throws IOException {
        DefaultTransaction transaction = new DefaultTransaction();
        // use handle as the log messages
        transaction.putProperty(VersionedPostgisDataStore.AUTHOR, "unknown");
        transaction.putProperty(VersionedPostgisDataStore.MESSAGE, request.getHandle());
        return transaction;
    }
}
