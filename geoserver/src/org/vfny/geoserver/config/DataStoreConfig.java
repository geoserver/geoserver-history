/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.config;

import org.geotools.data.*;
import org.w3c.dom.*;
import java.io.*;
import java.util.*;
import java.util.logging.*;


/**
 * DOCUMENT ME!
 *
 * @author Gabriel Roldán
 * @version 0.1
 */
public class DataStoreConfig extends AbstractConfig {
    /** DOCUMENT ME! */
    private static final Logger LOGGER = Logger.getLogger(
            "org.vfny.geoserver.config");

    /** unique namespace to refer to this datastore */
    private NameSpace nameSpace;

    /** wether this data store is enabled */
    private boolean enabled;

    /** a short description about this data store */
    private String title;

    /** a short description about this data store */
    private String _abstract;

    /** connection parameters to create the DataStore */
    private Map connectionParams;

    /**
     * Creates a new DataStoreConfig object.
     *
     * @param dsElem DOCUMENT ME!
     * @param catalog DOCUMENT ME!
     *
     * @throws ConfigurationException DOCUMENT ME!
     */
    public DataStoreConfig(Element dsElem, CatalogConfig catalog)
        throws ConfigurationException {
        LOGGER.finer("creating a new DataStore configuration");

        String namespacePrefix = getAttribute(dsElem, "namespace", true);

        this.nameSpace = catalog.getNameSpace(namespacePrefix);

        if (this.nameSpace == null) {
            String msg = "there is no namespace defined for datatasore '"
                + namespacePrefix + "'";
            throw new ConfigurationException(msg);
        }

        this.enabled = true || getBooleanAttribute(dsElem, "enabled", false);
        this.title = getChildText(dsElem, "title", false);
        this._abstract = getChildText(dsElem, "abstract", false);
        loadConnectionParams(getChildElement(dsElem, "connectionParams", true));
        LOGGER.finer("created DataStore " + toString());
    }

    /**
     * DOCUMENT ME!
     *
     * @param connElem DOCUMENT ME!
     *
     * @throws ConfigurationException DOCUMENT ME!
     */
    private void loadConnectionParams(Element connElem)
        throws ConfigurationException {
        LOGGER.fine("loading connection parameters for DataStore "
            + nameSpace.getPrefix());
        this.connectionParams = new HashMap();

        NodeList paramElems = connElem.getElementsByTagName("parameter");
        int pCount = paramElems.getLength();
        Element param;
        String paramKey;
        String paramValue;

        for (int i = 0; i < pCount; i++) {
            param = (Element) paramElems.item(i);
            paramKey = getAttribute(param, "name", true);
            paramValue = getAttribute(param, "value", true);
            connectionParams.put(paramKey, paramValue);
            LOGGER.finer("added parameter " + paramKey + ": '" + paramValue
                + "'");
        }
    }

    /**
     * By now just uses DataStoreFinder to find a new instance of a DataStore
     * capable of process <code>connectionParams</code>. In the future we can
     * see if it is better to cache or pool DataStores for performance, but
     * definitely we shouldn't maintain a single DataStore as instance
     * variable for synchronizing reassons
     *
     * @return DOCUMENT ME!
     *
     * @throws IOException if a datastore is found but can not be created for
     *         the passed parameters
     * @throws NullPointerException if no DataStore is found
     * @throws IllegalStateException if this DataStoreConfig is disabled by
     *         configuration
     */
    public DataStore getDataStore()
        throws IOException, NullPointerException, IllegalStateException {
        if (!isEnabled()) {
            throw new IllegalStateException(
                "this datastore is not enabled, check your configuration files");
        }

        DataStore dataStore = DataStoreFinder.getDataStore(connectionParams);

        return dataStore;
    }

    public String getTitle() {
        return title;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getAbstract() {
        return _abstract;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public NameSpace getNameSpace() {
        return nameSpace;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        return new StringBuffer("DataStoreConfig[namespace=").append(nameSpace
            .getPrefix()).append(", enabled=").append(enabled)
                                                             .append(", abstract=")
                                                             .append(_abstract)
                                                             .append(", connection parameters=")
                                                             .append(connectionParams)
                                                             .append("]")
                                                             .toString();
    }
}
