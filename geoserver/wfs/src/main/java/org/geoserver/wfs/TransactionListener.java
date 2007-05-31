package org.geoserver.wfs;

/**
 * Implemented by classes needing to listen to datastore change events during a
 * WFS Transaction
 */
public interface TransactionListener {
    /**
     * Check/alter feature collections and filters before a change hits the
     * datastores
     */
    void dataStoreChange(TransactionEvent event) throws WFSException;
}