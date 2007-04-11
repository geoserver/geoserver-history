/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.wfs;

import com.vividsolutions.jts.geom.Envelope;
import net.opengis.wfs.ActionType;
import net.opengis.wfs.AllSomeType;
import net.opengis.wfs.DeleteElementType;
import net.opengis.wfs.InsertElementType;
import net.opengis.wfs.InsertedFeatureType;
import net.opengis.wfs.NativeType;
import net.opengis.wfs.PropertyType;
import net.opengis.wfs.TransactionResponseType;
import net.opengis.wfs.TransactionType;
import net.opengis.wfs.UpdateElementType;
import net.opengis.wfs.WfsFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.FeatureMap;
import org.geoserver.feature.ReprojectingFeatureCollection;
import org.geoserver.platform.ServiceException;
import org.geotools.data.DataStore;
import org.geotools.data.DefaultQuery;
import org.geotools.data.DefaultTransaction;
import org.geotools.data.FeatureLocking;
import org.geotools.data.FeatureSource;
import org.geotools.data.FeatureStore;
import org.geotools.data.FeatureWriter;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.AttributeType;
import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.Feature;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureType;
import org.geotools.xml.EMFUtils;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.Id;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.vfny.geoserver.global.Data;
import org.vfny.geoserver.global.FeatureTypeInfo;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.namespace.QName;


/**
 * Web Feature Service Transaction operation.
 *
 * @author Justin Deoliveira, The Open Planning Project
 *
 */
public class Transaction {
    /**
     * logger
     */
    static Logger LOGGER = Logger.getLogger("org.geoserver.wfs");

    /**
     * Empty array of QNames
     */
    protected static final QName[] EMPTY_QNAMES = new QName[0];

    /**
     * WFS configuration
     */
    protected WFS wfs;

    /**
     * The catalog
     */
    protected Data catalog;

    /**
     * Filter factory
     */
    protected FilterFactory filterFactory;

    /** Geotools2 transaction used for this opperations */
    protected org.geotools.data.Transaction transaction;
    protected List transactionElementHandlers = new ArrayList();

    public Transaction(WFS wfs, Data catalog) {
        this.wfs = wfs;
        this.catalog = catalog;
        // register local element handlers
        // TODO: shall we use the Spring context instead? We would
        // loose the inner class abilities
        transactionElementHandlers.add(new InsertElementHandler());
        transactionElementHandlers.add(new UpdateElementHandler());
        transactionElementHandlers.add(new DeleteElementHandler());
        transactionElementHandlers.add(new NativeElementHandler());
    }

    public void setFilterFactory(FilterFactory filterFactory) {
        this.filterFactory = filterFactory;
    }

    public TransactionResponseType transaction(TransactionType request)
        throws WFSException {
        // make sure server is supporting transactions
        if ((wfs.getServiceLevel() & WFS.TRANSACTIONAL) == 0) {
            throw new WFSException("Transaction support is not enabled");
        }

        try {
            return execute(request);
        } catch (WFSException e) {
            abort(request); // release any locks
            throw e;
        } catch (Exception e) {
            abort(request); // release any locks
            throw new WFSException(e);
        }
    }

    /**
     * Execute Transaction request.
     *
     * <p>
     * The results of this opperation are stored for use by writeTo:
     *
     * <ul>
     * <li> transaction: used by abort & writeTo to commit/rollback </li>
     * <li> request: used for users getHandle information to report errors </li>
     * <li> stores: FeatureStores required for Transaction </li>
     * <li> failures: List of failures produced </li>
     * </ul>
     * </p>
     *
     * <p>
     * Because we are using geotools2 locking facilities our modification will
     * simply fail with IOException if we have not provided proper
     * authorization.
     * </p>
     *
     * <p>
     * The specification allows a WFS to implement PARTIAL sucess if it is
     * unable to rollback all the requested changes. This implementation is able
     * to offer full Rollback support and will not require the use of PARTIAL
     * success.
     * </p>
     *
     * @param transactionRequest
     *
     * @throws ServiceException
     *             DOCUMENT ME!
     * @throws WfsException
     * @throws WfsTransactionException
     *             DOCUMENT ME!
     */
    protected TransactionResponseType execute(TransactionType request)
        throws Exception {
        // some defaults
        if (request.getReleaseAction() == null) {
            request.setReleaseAction(AllSomeType.ALL_LITERAL);
        }

        // the geotools transaction
        transaction = getDatastoreTransaction(request);

        //
        // We are going to preprocess our elements,
        // gathering all the FeatureSources we need
        //
        // Map of required FeatureStores by typeName
        Map stores = new HashMap();

        // Map of required FeatureStores by typeRef (dataStoreId:typeName)
        // (This will be added to the contents are harmed)
        Map stores2 = new HashMap();

        // List of type names, maintain this list because of the insert hack
        // described below
        // List typeNames = new ArrayList();
        Map elementHandlers = gatherElementHandlers(request.getGroup());

        // Gather feature types required by transaction elements and validate
        // the elements
        // finally gather FeatureStores required by Transaction Elements
        // and configure them with our transaction
        //
        // (I am using element rather than transaction sub request
        // to agree with the spec docs)
        for (Iterator it = elementHandlers.entrySet().iterator(); it.hasNext();) {
            Map.Entry entry = (Map.Entry) it.next();
            EObject element = (EObject) entry.getKey();
            TransactionElementHandler handler = (TransactionElementHandler) entry.getValue();
            Map featureTypeInfos = new HashMap();

            QName[] typeNames = handler.getTypeNames(element);

            for (int i = 0; i < typeNames.length; i++) {
                final QName typeName = typeNames[i];
                final String name = typeName.getLocalPart();
                final String namespaceURI;

                if (typeName.getNamespaceURI() != null) {
                    namespaceURI = typeName.getNamespaceURI();
                } else {
                    namespaceURI = catalog.getDefaultNameSpace().getURI();
                }

                LOGGER.fine("Locating FeatureSource uri:'" + namespaceURI + "' name:'" + name + "'");

                final FeatureTypeInfo meta = catalog.getFeatureTypeInfo(name, namespaceURI);

                if (meta == null) {
                    String msg = name + " is not available: ";
                    String handle = (String) EMFUtils.get(element, "handle");
                    throw new WFSTransactionException(msg, (String) null, handle);
                }

                featureTypeInfos.put(typeName, meta);
            }

            // check element validity
            handler.checkValidity(element, featureTypeInfos);

            // go through all feature type infos data objects, and load feature
            // stores
            for (Iterator m = featureTypeInfos.values().iterator(); m.hasNext();) {
                FeatureTypeInfo meta = (FeatureTypeInfo) m.next();
                String typeRef = meta.getDataStoreInfo().getId() + ":" + meta.getTypeName();

                String URI = meta.getNameSpace().getURI();
                QName elementName = new QName(URI, meta.getTypeName(),
                        meta.getNameSpace().getPrefix());
                QName elementNameDefault = null;

                if (catalog.getDefaultNameSpace().getURI().equals(URI)) {
                    elementNameDefault = new QName(meta.getTypeName());
                }

                LOGGER.fine("located FeatureType w/ typeRef '" + typeRef + "' and elementName '"
                    + elementName + "'");

                if (stores.containsKey(elementName)) {
                    // typeName already loaded
                    continue;
                }

                try {
                    FeatureSource source = meta.getFeatureSource();

                    if (source instanceof FeatureStore) {
                        FeatureStore store = (FeatureStore) source;
                        store.setTransaction(transaction);
                        stores.put(elementName, source);

                        if (elementNameDefault != null) {
                            stores.put(elementNameDefault, source);
                        }

                        stores2.put(typeRef, source);
                    } else {
                        String msg = elementName + " is read-only";
                        String handle = (String) EMFUtils.get(element, "handle");

                        throw new WFSTransactionException(msg, (String) null, handle);
                    }
                } catch (IOException ioException) {
                    String msg = elementName + " is not available: "
                        + ioException.getLocalizedMessage();
                    String handle = (String) EMFUtils.get(element, "handle");
                    throw new WFSTransactionException(msg, ioException, handle);
                }
            }
        }

        // provide authorization for transaction
        // 
        String authorizationID = request.getLockId();

        if (authorizationID != null) {
            if ((wfs.getServiceLevel() & WFS.SERVICE_LOCKING) == 0) {
                throw new WFSException("Lock support is not enabled");
            }

            LOGGER.finer("got lockId: " + authorizationID);

            if (!lockExists(authorizationID)) {
                String mesg = "Attempting to use a lockID that does not exist"
                    + ", it has either expired or was entered wrong.";
                throw new WFSException(mesg, "InvalidParameterValue");
            }

            try {
                transaction.addAuthorization(authorizationID);
            } catch (IOException ioException) {
                // This is a real failure - not associated with a element
                //
                throw new WFSException("Authorization ID '" + authorizationID + "' not useable",
                    ioException);
            }
        }

        // result
        TransactionResponseType result = WfsFactory.eINSTANCE.createTransactionResponseType();
        result.setTransactionResults(WfsFactory.eINSTANCE.createTransactionResultsType());
        result.getTransactionResults().setHandle(request.getHandle());
        result.setTransactionSummary(WfsFactory.eINSTANCE.createTransactionSummaryType());
        result.getTransactionSummary().setTotalInserted(BigInteger.valueOf(0));
        result.getTransactionSummary().setTotalUpdated(BigInteger.valueOf(0));
        result.getTransactionSummary().setTotalDeleted(BigInteger.valueOf(0));

        result.setInsertResults(WfsFactory.eINSTANCE.createInsertResultsType());

        // execute elements in order, recording results as we go
        // I will need to record the damaged area for pre commit validation
        // checks
        // Envelope envelope = new Envelope();
        try {
            for (Iterator it = elementHandlers.entrySet().iterator(); it.hasNext();) {
                Map.Entry entry = (Map.Entry) it.next();
                EObject element = (EObject) entry.getKey();
                TransactionElementHandler handler = (TransactionElementHandler) entry.getValue();

                handler.execute(element, request, stores, result);
            }
        } catch (WFSTransactionException e) {
            LOGGER.log(Level.SEVERE, "Transaction failed", e);

            // transaction failed, rollback
            ActionType action = WfsFactory.eINSTANCE.createActionType();

            if (e.getCode() != null) {
                action.setCode(e.getCode());
            } else {
                action.setCode("InvalidParameterValue");
            }

            action.setLocator(e.getLocator());
            action.setMessage(e.getMessage());
            result.getTransactionResults().getAction().add(action);

            // roll back the transaction
            transaction.rollback();
            transaction = null;
        }

        if (transaction != null) {
            // transaction succeeded

            // commit
            try {
                transaction.commit();
            } finally {
                transaction.close();
                transaction = null;
            }

            // 
            // Lets deal with the locks
            //
            // Q: Why talk to Data you ask
            // A: Only class that knows all the DataStores
            //
            // We really need to ask all DataStores to release/refresh
            // because we may have locked Features with this Authorizations
            // on them, even though we did not refer to them in this
            // transaction.
            //
            // Q: Why here, why now?
            // A: The opperation was a success, and we have completed the
            // opperation
            //
            // We also need to do this if the opperation is not a success,
            // you can find this same code in the abort method
            // 
            if (request.getLockId() != null) {
                if (request.getReleaseAction() == AllSomeType.ALL_LITERAL) {
                    lockRelease(request.getLockId());
                } else if (request.getReleaseAction() == AllSomeType.SOME_LITERAL) {
                    lockRefresh(request.getLockId());
                }
            }
        }

        //        
        // if ( result.getTransactionResult().getStatus().getPARTIAL() != null )
        // {
        // throw new WFSException("Canceling PARTIAL response");
        // }
        //        
        // try {
        // if ( result.getTransactionResult().getStatus().getFAILED() != null )
        // {
        // //transaction failed, roll it back
        // transaction.rollback();
        // }
        // else {
        // transaction.commit();
        // result.getTransactionResult().getStatus().setSUCCESS(
        // WfsFactory.eINSTANCE.createEmptyType() );
        // }
        //        	
        // }
        // finally {
        // transaction.close();
        // transaction = null;
        // }

        // JD: this is an issue with the spec, InsertResults must be present,
        // even if no insert
        // occured, howwever insert results needs to have at least one
        // "FeatureId" eliement, sp
        // we create an FeatureId with an empty fid
        if (result.getInsertResults().getFeature().isEmpty()) {
            InsertedFeatureType insertedFeature = WfsFactory.eINSTANCE.createInsertedFeatureType();
            insertedFeature.getFeatureId().add(filterFactory.featureId("none"));

            result.getInsertResults().getFeature().add(insertedFeature);
        }

        return result;

        // we will commit in the writeTo method
        // after user has got the response
        // response = build;
    }

    /**
     * Looks up the element handlers to be used for each element
     *
     * @param group
     * @return
     */
    private Map gatherElementHandlers(FeatureMap group)
        throws WFSTransactionException {
        Map map = new HashMap();

        for (Iterator it = group.iterator(); it.hasNext();) {
            FeatureMap.Entry entry = (FeatureMap.Entry) it.next();
            EObject element = (EObject) entry.getValue();
            map.put(element, findElementHandler(element.getClass()));
        }

        return map;
    }

    /**
     * Finds the best transaction element handler for the specified element type
     * (the one matching the most specialized superclass of type)
     *
     * @param type
     * @return
     */
    protected final TransactionElementHandler findElementHandler(Class type)
        throws WFSTransactionException {
        List matches = new ArrayList();

        for (Iterator it = transactionElementHandlers.iterator(); it.hasNext();) {
            TransactionElementHandler handler = (TransactionElementHandler) it.next();

            if (handler.getElementClass().isAssignableFrom(type)) {
                matches.add(handler);
            }
        }

        if (matches.isEmpty()) {
            // try to instantiate one
            String msg = "No transaction element handler for : ( " + type + " )";
            throw new WFSTransactionException(msg);
        }

        if (matches.size() > 1) {
            // sort by class hierarchy
            Comparator comparator = new Comparator() {
                    public int compare(Object o1, Object o2) {
                        TransactionElementHandler h1 = (TransactionElementHandler) o1;
                        TransactionElementHandler h2 = (TransactionElementHandler) o2;

                        if (h2.getElementClass().isAssignableFrom(h1.getElementClass())) {
                            return -1;
                        }

                        return 1;
                    }
                };

            Collections.sort(matches, comparator);
        }

        return (TransactionElementHandler) matches.get(0);
    }

    /**
     * Creates a gt2 transaction used to execute the transaction call
     *
     * @return
     */
    protected DefaultTransaction getDatastoreTransaction(TransactionType request)
        throws IOException {
        return new DefaultTransaction();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.vfny.geoserver.responses.Response#abort()
     */
    public void abort(TransactionType request) {
        if (transaction == null) {
            return; // no transaction to rollback
        }

        try {
            transaction.rollback();
            transaction.close();
        } catch (IOException ioException) {
            // nothing we can do here
            LOGGER.log(Level.SEVERE, "Failed trying to rollback a transaction:" + ioException);
        }

        if (request.getLockId() != null) {
            if (request.getReleaseAction() == AllSomeType.SOME_LITERAL) {
                try {
                    lockRefresh(request.getLockId());
                } catch (Exception e) {
                    LOGGER.log(Level.WARNING, "Error occured refreshing lock", e);
                }
            } else if (request.getReleaseAction() == AllSomeType.ALL_LITERAL) {
                try {
                    lockRelease(request.getLockId());
                } catch (Exception e) {
                    LOGGER.log(Level.WARNING, "Error occured releasing lock", e);
                }
            }
        }
    }

    void lockRelease(String lockId) throws WFSException {
        LockFeature lockFeature = new LockFeature(wfs, catalog);
        lockFeature.release(lockId);
    }

    /**
     * Implement lockExists.
     *
     * @param lockID
     *
     * @return true if lockID exists
     *
     * @see org.geotools.data.Data#lockExists(java.lang.String)
     */
    private boolean lockExists(String lockId) throws Exception {
        LockFeature lockFeature = new LockFeature(wfs, catalog);

        return lockFeature.exists(lockId);
    }

    /**
     * Refresh lock by authorization
     *
     * <p>
     * Should use your own transaction?
     * </p>
     *
     * @param lockID
     */
    private void lockRefresh(String lockId) throws Exception {
        LockFeature lockFeature = new LockFeature(wfs, catalog);
        lockFeature.refresh(lockId);
    }

    /**
     * Handler for the insert element
     *
     * @author Andrea Aime - TOPP
     *
     */
    protected class InsertElementHandler implements TransactionElementHandler {
        public void checkValidity(EObject element, Map featureTypeInfos)
            throws WFSTransactionException {
            if ((wfs.getServiceLevel() & WFS.SERVICE_INSERT) == 0) {
                throw new WFSException("Transaction INSERT support is not enabled");
            }
        }

        public void execute(EObject element, TransactionType request, Map featureStores,
            TransactionResponseType response) throws WFSTransactionException {
            LOGGER.finer("Transasction Insert:" + element);

            InsertElementType insert = (InsertElementType) element;
            long inserted = response.getTransactionSummary().getTotalInserted().longValue();

            try {
                // group features by their schema
                HashMap /* <FeatureType,FeatureCollection> */ schema2features = new HashMap();

                for (Iterator f = insert.getFeature().iterator(); f.hasNext();) {
                    Feature feature = (Feature) f.next();
                    FeatureType schema = feature.getFeatureType();
                    FeatureCollection collection = (FeatureCollection) schema2features.get(schema);

                    if (collection == null) {
                        collection = new DefaultFeatureCollection(null, schema) {
                                };
                        schema2features.put(schema, collection);
                    }

                    collection.add(feature);
                }

                // JD: change from set fo list because if inserting
                // features into different feature stores, they could very well
                // get given the same id
                // JD: change from list to map so that the map can later be
                // processed and we can report the fids back in the same order
                // as they were supplied
                HashMap schema2fids = new HashMap();

                for (Iterator c = schema2features.values().iterator(); c.hasNext();) {
                    FeatureCollection collection = (FeatureCollection) c.next();
                    FeatureType schema = collection.getSchema();

                    QName elementName = new QName(schema.getNamespace().toString(),
                            schema.getTypeName());
                    FeatureStore store = (FeatureStore) featureStores.get(elementName);

                    if (store == null) {
                        throw new WFSException("Could not locate FeatureStore for '" + elementName
                            + "'");
                    }

                    if (collection != null) {
                        // reprojection
                        CoordinateReferenceSystem target = schema.getDefaultGeometry()
                                                                 .getCoordinateSystem();

                        if (target != null) {
                            collection = new ReprojectingFeatureCollection(collection, target);
                        }

                        // Need to use the namespace here for the
                        // lookup, due to our weird
                        // prefixed internal typenames. see
                        // http://jira.codehaus.org/secure/ViewIssue.jspa?key=GEOS-143

                        // Once we get our datastores making features
                        // with the correct namespaces
                        // we can do something like this:
                        // FeatureTypeInfo typeInfo =
                        // catalog.getFeatureTypeInfo(schema.getTypeName(),
                        // schema.getNamespace());
                        // until then (when geos-144 is resolved) we're
                        // stuck with:
                        // QName qName = (QName) typeNames.get( i );
                        // FeatureTypeInfo typeInfo =
                        // catalog.featureType( qName.getPrefix(),
                        // qName.getLocalPart() );

                        // this is possible with the insert hack above.
                        LOGGER.finer("Use featureValidation to check contents of insert");

                        // featureValidation(
                        // typeInfo.getDataStore().getId(), schema,
                        // collection );
                        List fids = (List) schema2fids.get(schema.getTypeName());

                        if (fids == null) {
                            fids = new LinkedList();
                            schema2fids.put(schema.getTypeName(), fids);
                        }

                        fids.addAll(store.addFeatures(collection));
                    }

                    //
                    // Add to validation check envelope
                    // envelope.expandToInclude(collection.getBounds());
                }

                // report back fids, we need to keep the same order the
                // fids were reported
                // in the original feature collection
                InsertedFeatureType insertedFeature = null;

                for (Iterator f = insert.getFeature().iterator(); f.hasNext();) {
                    Feature feature = (Feature) f.next();
                    FeatureType schema = feature.getFeatureType();

                    // get the next fid
                    LinkedList fids = (LinkedList) schema2fids.get(schema.getTypeName());
                    String fid = (String) fids.removeFirst();

                    insertedFeature = WfsFactory.eINSTANCE.createInsertedFeatureType();
                    insertedFeature.setHandle(insert.getHandle());
                    insertedFeature.getFeatureId().add(filterFactory.featureId(fid));

                    response.getInsertResults().getFeature().add(insertedFeature);
                }

                // update the insert counter
                inserted += insert.getFeature().size();
            } catch (Exception e) {
                String msg = "Error perfomring insert";
                throw new WFSTransactionException(msg, e, insert.getHandle());
            }

            // update transaction summary
            response.getTransactionSummary().setTotalInserted(BigInteger.valueOf(inserted));
        }

        public Class getElementClass() {
            return InsertElementType.class;
        }

        public QName[] getTypeNames(EObject element) throws WFSTransactionException {
            InsertElementType insert = (InsertElementType) element;
            List typeNames = new ArrayList();

            if (!insert.getFeature().isEmpty()) {
                for (Iterator f = insert.getFeature().iterator(); f.hasNext();) {
                    Feature feature = (Feature) f.next();

                    String name = feature.getFeatureType().getTypeName();
                    String namespaceURI = null;

                    if (feature.getFeatureType().getNamespace() != null) {
                        namespaceURI = feature.getFeatureType().getNamespace().toString();
                    }

                    typeNames.add(new QName(namespaceURI, name));
                }
            } else {
                LOGGER.finer("Insert was empty - does not need a FeatuerSoruce");
            }

            return (QName[]) typeNames.toArray(new QName[typeNames.size()]);
        }
    }

    /**
     * Processes standard update elements
     *
     * @author Andrea Aime - TOPP
     *
     */
    protected class UpdateElementHandler implements TransactionElementHandler {
        public void checkValidity(EObject element, Map typeInfos)
            throws WFSTransactionException {
            // check inserts are enabled
            if ((wfs.getServiceLevel() & WFS.SERVICE_UPDATE) == 0) {
                throw new WFSException("Transaction Update support is not enabled");
            }

            // check that all required properties have a specified value
            UpdateElementType update = (UpdateElementType) element;

            try {
                FeatureTypeInfo meta = (FeatureTypeInfo) typeInfos.values().iterator().next();
                FeatureType featureType = meta.getFeatureType();

                for (Iterator prop = update.getProperty().iterator(); prop.hasNext();) {
                    PropertyType property = (PropertyType) prop.next();

                    if (property.getValue() == null) {
                        String propertyName = property.getName().getLocalPart();
                        AttributeType attributeType = featureType.getAttributeType(propertyName);

                        if ((attributeType != null) && (attributeType.getMinOccurs() > 0)) {
                            String msg = "Property '" + attributeType.getName()
                                + "' is mandatory but no value specified.";
                            throw new WFSException(msg, "MissingParameterValue");
                        }
                    }
                }
            } catch (IOException e) {
                throw new WFSTransactionException("Could not locate feature type information for "
                    + update.getTypeName(), e, update.getHandle());
            }
        }

        public void execute(EObject element, TransactionType request, Map featureStores,
            TransactionResponseType response) throws WFSTransactionException {
            UpdateElementType update = (UpdateElementType) element;
            QName elementName = update.getTypeName();
            String handle = update.getHandle();
            long updated = response.getTransactionSummary().getTotalUpdated().longValue();

            FeatureStore store = (FeatureStore) featureStores.get(elementName);

            if (store == null) {
                throw new WFSException("Could not locate FeatureStore for '" + elementName + "'");
            }

            LOGGER.finer("Transaction Update:" + element);

            try {
                Filter filter = (Filter) update.getFilter();

                AttributeType[] types = new AttributeType[update.getProperty().size()];
                Object[] values = new Object[update.getProperty().size()];

                for (int j = 0; j < update.getProperty().size(); j++) {
                    PropertyType property = (PropertyType) update.getProperty().get(j);
                    types[j] = store.getSchema().getAttributeType(property.getName().getLocalPart());
                    values[j] = property.getValue();
                }

                // Pass through data to collect fids and damaged
                // region
                // for validation
                //
                Set fids = new HashSet();
                LOGGER.finer("Preprocess to remember modification as a set of fids");

                FeatureCollection features = store.getFeatures(filter);
                Iterator preprocess = features.iterator();

                try {
                    while (preprocess.hasNext()) {
                        Feature feature = (Feature) preprocess.next();
                        fids.add(feature.getID());

                        // envelope.expandToInclude(feature.getBounds());
                    }
                } catch (NoSuchElementException e) {
                    throw new WFSException("Could not aquire FeatureIDs", e);
                } finally {
                    features.close(preprocess);
                }

                try {
                    if (types.length == 1) {
                        store.modifyFeatures(types[0], values[0], filter);
                    } else {
                        store.modifyFeatures(types, values, filter);
                    }
                } finally {
                    // make sure we unlock
                    if ((request.getLockId() != null) && store instanceof FeatureLocking
                            && (request.getReleaseAction() == AllSomeType.SOME_LITERAL)) {
                        FeatureLocking locking = (FeatureLocking) store;
                        locking.unLockFeatures(filter);
                    }
                }

                // Post process - check features for changed boundary and
                // pass them off to the ValidationProcessor
                if (!fids.isEmpty()) {
                    LOGGER.finer("Post process update for boundary update and featureValidation");

                    FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);
                    Set featureIds = new HashSet();

                    for (Iterator f = fids.iterator(); f.hasNext();) {
                        featureIds.add(ff.featureId((String) f.next()));
                    }

                    Id modified = ff.id(featureIds);

                    FeatureCollection changed = store.getFeatures(modified);

                    // envelope.expandToInclude(changed.getBounds());

                    // featureValidation(typeInfo.getDataStore().getId(),store.getSchema(),
                    // changed);
                }

                // update the update counter
                updated += fids.size();
            } catch (IOException ioException) {
                // JD: changing from throwing service exception to
                // adding action that failed
                throw new WFSTransactionException(ioException, null, handle);
            }

            // update transaction summary
            response.getTransactionSummary().setTotalUpdated(BigInteger.valueOf(updated));
        }

        public Class getElementClass() {
            return UpdateElementType.class;
        }

        public QName[] getTypeNames(EObject element) throws WFSTransactionException {
            return new QName[] { ((UpdateElementType) element).getTypeName() };
        }
    }

    /**
     * Processes standard Delete elements
     *
     * @author Andrea Aime - TOPP
     *
     */
    protected class DeleteElementHandler implements TransactionElementHandler {
        public Class getElementClass() {
            return DeleteElementType.class;
        }

        public QName[] getTypeNames(EObject element) throws WFSTransactionException {
            return new QName[] { ((DeleteElementType) element).getTypeName() };
        }

        public void checkValidity(EObject element, Map featureTypeInfos)
            throws WFSTransactionException {
            if ((wfs.getServiceLevel() & WFS.SERVICE_DELETE) == 0) {
                throw new WFSException("Transaction Delete support is not enabled");
            }

            // check that a filter was specified
            DeleteElementType delete = (DeleteElementType) element;

            if ((delete.getFilter() == null) || Filter.INCLUDE.equals(delete.getFilter())) {
                throw new WFSTransactionException("Must specify filter for delete",
                    "MissingParameterValue");
            }
        }

        public void execute(EObject element, TransactionType request, Map featureStores,
            TransactionResponseType response) throws WFSTransactionException {
            DeleteElementType delete = (DeleteElementType) element;
            QName elementName = delete.getTypeName();
            String handle = delete.getHandle();
            long deleted = response.getTransactionSummary().getTotalDeleted().longValue();

            FeatureStore store = (FeatureStore) featureStores.get(elementName);

            if (store == null) {
                throw new WFSException("Could not locate FeatureStore for '" + elementName + "'");
            }

            String typeName = store.getSchema().getTypeName();
            LOGGER.finer("Transaction Delete:" + element);

            try {
                Filter filter = (Filter) delete.getFilter();

                Envelope damaged = store.getBounds(new DefaultQuery(
                            delete.getTypeName().getLocalPart(), filter));

                if (damaged == null) {
                    damaged = store.getFeatures(filter).getBounds();
                }

                if ((request.getLockId() != null) && store instanceof FeatureLocking
                        && (request.getReleaseAction() == AllSomeType.SOME_LITERAL)) {
                    FeatureLocking locking = (FeatureLocking) store;

                    // TODO: Revisit Lock/Delete interaction in gt2
                    if (false) {
                        // REVISIT: This is bad - by releasing locks before
                        // we remove features we open ourselves up to the
                        // danger of someone else locking the features we
                        // are about to remove.
                        //
                        // We cannot do it the other way round, as the
                        // Features will not exist
                        //
                        // We cannot grab the fids offline using AUTO_COMMIT
                        // because we may have removed some of them earlier
                        // in the transaction
                        //
                        locking.unLockFeatures(filter);
                        store.removeFeatures(filter);
                    } else {
                        // This a bit better and what should be done, we
                        // will need to rework the gt2 locking api to work
                        // with fids or something
                        //
                        // The only other thing that would work
                        // would be to specify that FeatureLocking is
                        // required to remove locks when removing Features.
                        // 
                        // While that sounds like a good idea, it
                        // would be extra work when doing release mode ALL.
                        // 
                        DataStore data = store.getDataStore();
                        FilterFactory factory = CommonFactoryFinder.getFilterFactory(null);
                        FeatureWriter writer;
                        writer = data.getFeatureWriter(typeName, filter, transaction);

                        try {
                            while (writer.hasNext()) {
                                String fid = writer.next().getID();
                                Set featureIds = new HashSet();
                                featureIds.add(factory.featureId(fid));
                                locking.unLockFeatures(factory.id(featureIds));
                                writer.remove();
                                deleted++;
                            }
                        } finally {
                            writer.close();
                        }

                        store.removeFeatures(filter);
                    }
                } else {
                    // We don't have to worry about locking right now
                    FeatureWriter writer = store.getDataStore()
                                                .getFeatureWriter(typeName, filter, transaction);

                    try {
                        while (writer.hasNext()) {
                            writer.next();
                            writer.remove();
                            deleted++;
                        }
                    } finally {
                        writer.close();
                    }
                }
            } catch (IOException e) {
                String msg = e.getMessage();
                String eHandle = (String) EMFUtils.get(element, "handle");
                throw new WFSTransactionException(msg, (String) null, eHandle, handle);
            }

            // update deletion count
            response.getTransactionSummary().setTotalDeleted(BigInteger.valueOf(deleted));
        }
    }

    /**
     * Processes native elements as unrecognized ones, and checks wheter they
     * can be safely ignored on not.
     *
     * @author Andrea Aime - TOPP
     */
    public class NativeElementHandler implements TransactionElementHandler {
        public void checkValidity(EObject element, Map featureTypeInfos)
            throws WFSTransactionException {
            NativeType nativ = (NativeType) element;

            if (!nativ.isSafeToIgnore()) {
                throw new WFSTransactionException("Native element:" + nativ.getVendorId()
                    + " unsupported but marked as" + " unsafe to ignore", "InvalidParameterValue");
            }
        }

        public void execute(EObject element, TransactionType request, Map featureSources,
            TransactionResponseType response) throws WFSTransactionException {
            // nothing to do, we just ignore if possible
        }

        public Class getElementClass() {
            return NativeType.class;
        }

        public QName[] getTypeNames(EObject element) throws WFSTransactionException {
            // we don't handle this
            return EMPTY_QNAMES;
        }
    }
}
