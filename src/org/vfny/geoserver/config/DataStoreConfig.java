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
 * @version $Id: DataStoreConfig.java,v 1.3 2004/01/07 01:47:12 cholmesny Exp $
 */
public class DataStoreConfig extends AbstractConfig {
    /** DOCUMENT ME! */
    private static final Logger LOGGER = Logger.getLogger(
            "org.vfny.geoserver.config");

    /** unique datasore identifier */
    private String id;

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

    /** DataStore we are representing */
    private DataStore dataStore = null;

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
        this.id = getAttribute(dsElem, "id", true);

        String namespacePrefix = getAttribute(dsElem, "namespace", true);
        this.nameSpace = catalog.getNameSpace(namespacePrefix);

        if (this.nameSpace == null) {
            String msg = "there is no namespace defined for datatasore '"
                + namespacePrefix + "'";
            throw new ConfigurationException(msg);
        }

        //Huh?  Doesn't this just set this always to true?
        //this.enabled = true || getBooleanAttribute(dsElem, "enabled", false);
        this.enabled = getBooleanAttribute(dsElem, "enabled", false);
        LOGGER.info("datastore " + id + " is enabled " + enabled);
        this.title = getChildText(dsElem, "title", false);
        this._abstract = getChildText(dsElem, "abstract", false);
        loadConnectionParams(getChildElement(dsElem, "connectionParams", true),
            catalog.getFeatureTypeDir());
        LOGGER.info("created " + toString());
    }

    /**
     * Configuration based on gt2 Catalog information.
     * 
     * <p>
     * For the namespace food the config map defines:
     * </p>
     * 
     * <ul>
     * <li>
     * foo.id: String (default foo)
     * </li>
     * <li>
     * foo.enabled: boolean (default true)
     * </li>
     * <li>
     * foo.title: String
     * </li>
     * <li>
     * foo.abstract: String
     * </li>
     * </ul>
     * 
     *
     * @param config
     * @param store
     * @param config
     */
    public DataStoreConfig(Map config, DataStore store, NameSpace namespace) {
        LOGGER.finer("creating a new DataStore configuration");

        String name = namespace.getPrefix();
        id = get(config, name + ".id", name);
        nameSpace = namespace;
        enabled = get(config, name + ".enabled", true);
        title = get(config, name + ".title");
        _abstract = get(config, name + ".abstract");
        dataStore = store;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getId() {
        return this.id;
    }

    /**
     * DOCUMENT ME!
     *
     * @param connElem DOCUMENT ME!
     * @param featureTypeDir DOCUMENT ME!
     *
     * @throws ConfigurationException DOCUMENT ME!
     */
    private void loadConnectionParams(Element connElem, File featureTypeDir)
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

            //This is a horrible hack to get relative resolving of shapefiles.
            //The way to do it right might be to somehow allow 
            //FeatureTypeConfig to load a new datastore on the spot if it's
            //just a file...
            if (paramKey.equals("filename")) {
                try {
                    paramValue = new File(featureTypeDir, paramValue).toURL()
                                                                     .toString();
                } catch (java.net.MalformedURLException murle) {
                    LOGGER.info("problem reading datasource info for "
                        + paramValue + ": " + murle);
                }

                paramKey = "url";
            }

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
     * <p></p>
     * 
     * <p>
     * JG: Umm we actually require a single DataStore for for locking &
     * transaction support to work. DataStore is expected to be thread aware
     * (that is why it has Transaction Support).
     * </p>
     *
     * @return DOCUMENT ME!
     *
     * @throws IOException if a datastore is found but can not be created for
     *         the passed parameters
     * @throws IllegalStateException if this DataStoreConfig is disabled by
     *         configuration
     * @throws NoSuchElementException if no DataStore is found
     * @throws DataSourceException DOCUMENT ME!
     */
    public synchronized DataStore getDataStore()
        throws IOException, IllegalStateException, NoSuchElementException {
        LOGGER.info("about to get datastore for " + this.toString());

        if (!isEnabled()) {
            throw new IllegalStateException(
                "this datastore is not enabled, check your configuration files");
        }

        Iterator iter = DataStoreFinder.getAvailableDataStores();

        while (iter.hasNext()) {
            LOGGER.info(iter.next() + " is an available DataSource");
        }

        if (dataStore == null) {
            try {
                dataStore = DataStoreFinder.getDataStore(connectionParams);
                LOGGER.fine("connection established by " + toString());
            } catch (Throwable ex) {
                throw new DataSourceException("can't create the datastore "
                    + getId() + ": " + ex.getClass().getName() + ": "
                    + ex.getMessage(), ex);
            }

            if (dataStore == null) {
                LOGGER.fine("failed to establish connection with " + toString());
                throw new NoSuchElementException(
                    "No datastore found capable of managing " + toString());
            }
        }

        return dataStore;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
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
