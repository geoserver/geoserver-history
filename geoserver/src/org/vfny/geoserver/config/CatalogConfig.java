/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.config;

import org.geotools.filter.*;
import org.vfny.geoserver.WfsException;
import org.vfny.geoserver.requests.wfs.*;
import org.vfny.geoserver.responses.wfs.*;
import org.w3c.dom.*;
import java.util.*;
import java.util.logging.Logger;


/**
 * DOCUMENT ME!
 *
 * @author Gabriel Roldán
 * @version 0.1
 */
public class CatalogConfig extends AbstractConfig {
    /** DOCUMENT ME! */
    private static final Logger LOGGER = Logger.getLogger(
            "org.vfny.geoserver.config");

    /** The holds the mappings between prefixes and uri's */
    private Map nameSpaces;

    /** DOCUMENT ME! */
    private NameSpace defaultNameSpace;

    /** DOCUMENT ME! */
    private Map dataStores;

    /** DOCUMENT ME! */
    private Map styles;

    /**
     * Map of <code>FeatureTypeConfig</code>'s stored by full qualified name
     * (namespace prefix + PREFIX_DELIMITER + typeName)
     */
    private Map featureTypes;

    /**
     * Creates a new CatalogConfig object.
     *
     * @param root DOCUMENT ME!
     *
     * @throws ConfigurationException DOCUMENT ME!
     */
    public CatalogConfig(Element root) throws ConfigurationException {
        LOGGER.info("loading catalog configuration");
        loadNameSpaces(getChildElement(root, "namespaces", true));
        loadDataStores(getChildElement(root, "datastores", true));
        loadStyles(getChildElement(root, "styles", false));
        loadFeatureTypes(getChildElement(root, "featureTypes", true));
    }

    /**
     * implement it!
     *
     * @param typeName DOCUMENT ME!
     * @param filter DOCUMENT ME!
     * @param lockId DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws WfsException DOCUMENT ME!
     * @throws UnsupportedOperationException DOCUMENT ME!
     */
    public boolean isLocked(String typeName, Filter filter, String lockId)
        throws WfsException {
        throw new UnsupportedOperationException("not implemented yet");
    }

    public synchronized String lock(String typeName, Filter filter,
        boolean lockAll, int expiry) throws WfsException {
        throw new UnsupportedOperationException("not implemented yet");
    }

    /**
     * A convenience method to lock all the features in a given typeName for
     * the default expiry length of time.
     *
     * @param typeName the name of the featureType to lock.
     *
     * @return the id string of the lock, if successful, null otherwise.
     *
     * @throws WfsException when locking isn't successful.
     */
    public synchronized String lock(String typeName) throws WfsException {
        return lock(typeName, null, true, -1);
    }

    public boolean lockIdExists(String lockId) {
        throw new UnsupportedOperationException("not implemented yet");
    }

    /**
     * Unlocks a completed transaction request.  This should only be called
     * after the transaction is committed.  It performs the proper release
     * action for each of the sub-requests.  This method should be called by
     * transactions, as they are the only way to release a lock, which is why
     * a WfsTransactionException is thrown here.
     *
     * @param completed a successfully completed transaction request.
     *
     * @return true if there were features released, false otherwise.
     *
     * @throws WfsTransactionException if there was trouble with the backend.
     * @throws UnsupportedOperationException DOCUMENT ME!
     */
    public synchronized boolean unlock(TransactionRequest completed)
        throws WfsTransactionException {
        throw new UnsupportedOperationException("not implemented yet");
    }

    public DataStoreConfig[] getDataStores() {
        List dslist = new ArrayList(dataStores.values());
        DataStoreConfig[] dStores = new DataStoreConfig[dslist.size()];
        dStores = (DataStoreConfig[]) dslist.toArray(dStores);

        return dStores;
    }

    /**
     * DOCUMENT ME!
     *
     * @param ns DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public DataStoreConfig getDataStore(NameSpace ns) {
        return (DataStoreConfig) dataStores.get(ns.getPrefix());
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public NameSpace[] getNameSpaces() {
        NameSpace[] ns = new NameSpace[nameSpaces.values().size()];

        return (NameSpace[]) new ArrayList(nameSpaces.values()).toArray(ns);
    }

    /**
     * DOCUMENT ME!
     *
     * @param prefix DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public NameSpace getNameSpace(String prefix) {
        return (NameSpace) nameSpaces.get(prefix);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public NameSpace getDefaultNameSpace() {
        return defaultNameSpace;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Map getStyles() {
        return this.styles;
    }

    /**
     * DOCUMENT ME!
     *
     * @param typeName DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws NoSuchElementException DOCUMENT ME!
     */
    public FeatureTypeConfig getFeatureType(String typeName)
        throws NoSuchElementException {
        int prefixDelimPos = typeName.lastIndexOf(NameSpace.PREFIX_DELIMITER);

        if (prefixDelimPos < 0) {
            //for backwards compatibility.  Only works if all
            //featureTypes have the same prefix.
            typeName = getDefaultNameSpace().getPrefix()
                + NameSpace.PREFIX_DELIMITER + typeName;
        }

        LOGGER.finest("getting type " + typeName);

        FeatureTypeConfig ftype = (FeatureTypeConfig) featureTypes.get(typeName);

        if (ftype == null) {
            throw new NoSuchElementException("there is no FeatureType named "
                + typeName + " configured in this server");
        }

        return ftype;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Map getFeatureTypes() {
        return this.featureTypes;
    }

    /**
     * DOCUMENT ME!
     *
     * @param nsRoot DOCUMENT ME!
     *
     * @throws ConfigurationException DOCUMENT ME!
     */
    private void loadNameSpaces(Element nsRoot) throws ConfigurationException {
        NodeList nsList = nsRoot.getElementsByTagName("namespace");
        Element elem;
        String uri;
        String prefix;
        boolean defaultNS;
        int nsCount = nsList.getLength();

        nameSpaces = new HashMap(nsCount);

        for (int i = 0; i < nsCount; i++) {
            elem = (Element) nsList.item(i);
            uri = getAttribute(elem, "uri", true);
            prefix = getAttribute(elem, "prefix", true);
            defaultNS = getBooleanAttribute(elem, "default", false);
            defaultNS = (defaultNS || (nsCount == 1));

            NameSpace ns = new NameSpace(prefix, uri, defaultNS);
            nameSpaces.put(prefix, ns);

            if (defaultNS) {
                defaultNameSpace = ns;
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param dsRoot DOCUMENT ME!
     *
     * @throws ConfigurationException DOCUMENT ME!
     */
    private void loadDataStores(Element dsRoot) throws ConfigurationException {
        dataStores = new HashMap();

        NodeList dsElements = dsRoot.getElementsByTagName("datastore");
        int dsCnt = dsElements.getLength();

        DataStoreConfig dsConfig;
        Element dsElem;

        for (int i = 0; i < dsCnt; i++) {
            dsElem = (Element) dsElements.item(i);
            dsConfig = new DataStoreConfig(dsElem, this);

            if (dataStores.containsKey(dsConfig.getNameSpace().getPrefix())) {
                throw new ConfigurationException("duplicated datastore id: "
                    + dsConfig.getNameSpace());
            }

            dataStores.put(dsConfig.getNameSpace().getPrefix(), dsConfig);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param stylesElem DOCUMENT ME!
     *
     * @throws ConfigurationException DOCUMENT ME!
     *
     * @task TODO: load styles as SLD Style objects.
     */
    private void loadStyles(Element stylesElem) throws ConfigurationException {
        styles = new HashMap();

        NodeList stylesList = null;

        if (stylesElem != null) {
            stylesList = stylesElem.getElementsByTagName("style");
        }

        if ((stylesList == null) || (stylesList.getLength() == 0)) {
            //no styles where defined, just add a default one
            styles.put("normal", "styles/normal.sld");

            return;
        }

        int styleCount = stylesList.getLength();
        Element styleElem;

        for (int i = 0; i < styleCount; i++) {
            styleElem = (Element) stylesList.item(i);

            String stId = getAttribute(styleElem, "id", true);
            String stFile = getAttribute(styleElem, "filename", true);
            styles.put(stId, stFile);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param fTypesElem DOCUMENT ME!
     *
     * @throws ConfigurationException DOCUMENT ME!
     */
    private void loadFeatureTypes(Element fTypesElem)
        throws ConfigurationException {
        NodeList ftlist = fTypesElem.getElementsByTagName("featureType");
        Element ftypeElem;
        int ftCount = ftlist.getLength();
        this.featureTypes = new HashMap(2 * ftCount);

        FeatureTypeConfig ft = null;

        for (int i = 0; i < ftCount; i++) {
            ft = new FeatureTypeConfig(this, (Element) ftlist.item(i));
            featureTypes.put(ft.getName(), ft);
        }
    }
}
