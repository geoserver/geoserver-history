/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.global;

import org.geotools.data.Catalog;
import org.geotools.data.DataStore;
import org.geotools.data.DataStoreMetaData;
import org.geotools.data.DefaultTransaction;
import org.geotools.data.FeatureSource;
import org.geotools.data.FeatureTypeMetaData;
import org.geotools.data.LockingManager;
import org.geotools.data.NamespaceMetaData;
import org.geotools.data.Transaction;
import org.geotools.styling.SLDStyle;
import org.geotools.styling.Style;
import org.geotools.styling.StyleFactory;
import org.vfny.geoserver.global.dto.DataDTO;
import org.vfny.geoserver.global.dto.DataStoreInfoDTO;
import org.vfny.geoserver.global.dto.FeatureTypeInfoDTO;
import org.vfny.geoserver.global.dto.NameSpaceInfoDTO;
import org.vfny.geoserver.global.dto.StyleDTO;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
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


/**
 * This class stores all the information that a catalog would (and
 * CatalogConfig used to).
 *
 * @author Gabriel Roldán
 * @author Chris Holmes
 * @author dzwiers
 * @version $Id: Data.java,v 1.1.2.18 2004/01/12 04:55:14 jive Exp $
 */
public class Data extends GlobalLayerSupertype implements Catalog {
    /** for debugging */
    private static final Logger LOGGER = Logger.getLogger(
            "org.vfny.geoserver.global");

    /** Default name of feature type information */
    private static final String INFO_FILE = "info.xml";

    /** used to create styles */
    private static StyleFactory styleFactory = StyleFactory.createStyleFactory();

    /** holds the mappings between prefixes and NameSpaceInfo objects */
    private Map nameSpaces;

    /** NameSpaceInfo */
    private NameSpaceInfo defaultNameSpace;

    /** Mapping of DataStoreInfo by id */
    private Map dataStores;

    /** holds the mapping of Styles and style names */
    private Map styles;

    /**
     * Map of <code>FeatureTypeInfo</code>'s stored by full qualified name
     * (NameSpaceInfo prefix + PREFIX_DELIMITER + typeName)
     */
    private Map featureTypes;

    /** The DTO for this object */
    private DataDTO catalog;

    /**
     * Data constructor.
     * 
     * <p>
     * Creates a Data object from the data provided.
     * </p>
     *
     * @param config DataDTO initial data.
     *
     * @throws ConfigurationException
     */
    public Data(DataDTO config) throws ConfigurationException {
        load(config);
    }

    /**
     * Data constructor.
     * 
     * <p>
     * package only constructor for GeoServer to call.
     * </p>
     */
    Data() {
        nameSpaces = new HashMap();
        styles = new HashMap();
        featureTypes = new HashMap();
        dataStores = new HashMap();
    }

    /**
     * load purpose. Places the data in this container and innitializes it.
     * Complex tests are performed to detect existing datasources,  while the
     * remainder only include simplistic id checks.
     *
     * @param config
     *
     * @throws ConfigurationException
     * @throws NullPointerException 
     */
    void load(DataDTO config) throws ConfigurationException {
        catalog = config;

        if (config == null) {
            throw new NullPointerException("");
        }

        if (dataStores == null) {
            dataStores = new HashMap();
        }

        if (config.getDataStores() == null) {
            throw new NullPointerException("");
        }

        Set s = dataStores.keySet();
        Iterator i = config.getDataStores().keySet().iterator();

        while (i.hasNext()) {
            Object key = i.next();
            s.remove(key);

            //find missing ones
            if (!dataStores.containsKey(key)) {
                dataStores.put(key,
                    new DataStoreInfo((DataStoreInfoDTO) config.getDataStores()
                                                               .get(key), this));
            } else { // check for small changes

                DataStoreInfoDTO dsiDto = (DataStoreInfoDTO) ((DataStoreInfo) dataStores
                    .get(key)).toDTO();

                if ((dsiDto != null)
                        && !(dsiDto.equals(config.getDataStores().get(key)))) {
                    dataStores.put(key,
                        new DataStoreInfo((DataStoreInfoDTO) config.getDataStores()
                                                                   .get(key),
                            this));
                }
            }
        }

        // s contains all the unchecked values.
        List tmp = new LinkedList();
        i = s.iterator();

        while (i.hasNext()) {
            tmp.add(i.next());
        }

        for (int j = 0; j < tmp.size(); j++)
            dataStores.remove(tmp.get(j));

        List tmp2 = new LinkedList();

        if (featureTypes == null) {
            featureTypes = new HashMap();
        }

        s = featureTypes.keySet();

        if (config.getFeaturesTypes() == null) {
            throw new NullPointerException("");
        }

        i = config.getFeaturesTypes().keySet().iterator();

        while (i.hasNext()) {
            Object key = i.next();
            String nm = ((FeatureTypeInfoDTO) config.getFeaturesTypes().get(key))
                .getName();

            if (tmp2.contains(nm)) {
                throw new ConfigurationException(
                    "FeatureTypeInfo.getName() must be unique! ( " + nm + " )");
            } else {
                tmp2.add(nm);
            }

            s.remove(key);

            if (!featureTypes.containsKey(key)) {
                featureTypes.put(nm,
                    new FeatureTypeInfo((FeatureTypeInfoDTO) config.getFeaturesTypes()
                                                                   .get(key),
                        this));
            }
        }

        // s contains all the unchecked values.
        tmp = new LinkedList();
        i = s.iterator();

        while (i.hasNext()) {
            tmp.add(i.next());
        }

        for (int j = 0; j < tmp.size(); j++)
            featureTypes.remove(tmp.get(j));

        if (nameSpaces == null) {
            nameSpaces = new HashMap();
        }

        s = nameSpaces.keySet();

        if (config.getNameSpaces() == null) {
            throw new NullPointerException("");
        }

        i = config.getNameSpaces().keySet().iterator();

        while (i.hasNext()) {
            Object key = i.next();
            s.remove(key);

            if (!nameSpaces.containsKey(key)) {
                nameSpaces.put(key,
                    new NameSpaceInfo((NameSpaceInfoDTO) config.getNameSpaces().get(key)));

                if (((NameSpaceInfoDTO) config.getNameSpaces().get(key)).isDefault()) {
                    defaultNameSpace = (NameSpaceInfo) nameSpaces.get(key);
                }
            }
        }

        // s contains all the unchecked values.
        tmp = new LinkedList();
        i = s.iterator();

        while (i.hasNext()) {
            tmp.add(i.next());
        }

        for (int j = 0; j < tmp.size(); j++)
            nameSpaces.remove(tmp.get(j));

        if (styles == null) {
            styles = new HashMap();
        }

        s = styles.keySet();

        if (config.getStyles() == null) {
            throw new NullPointerException("");
        }

        i = config.getStyles().keySet().iterator();

        while (i.hasNext()) {
            Object key = i.next();
            s.remove(key);

            if (!styles.containsKey(key)) {
                try {
                    styles.put(key,
                        loadStyle(
                            ((StyleDTO) config.getStyles().get(key))
                            .getFilename()));
                } catch (IOException e) {
                    LOGGER.fine("Error loading style:" + key.toString());
                }
            }
        }

        // s contains all the unchecked values.
        tmp = new LinkedList();
        i = s.iterator();

        while (i.hasNext()) {
            tmp.add(i.next());
        }

        for (int j = 0; j < tmp.size(); j++)
            styles.remove(tmp.get(j));
    }

    /**
     * getDataStoreInfos purpose.
     * 
     * <p>
     * A list of the posible DataStoreInfo
     * </p>
     *
     * @return DataStoreInfo[] list of the posible DataStoreInfo
     */
    public DataStoreInfo[] getDataStoreInfos() {
        List dslist = new ArrayList(dataStores.values());
        DataStoreInfo[] dStores = new DataStoreInfo[dslist.size()];
        dStores = (DataStoreInfo[]) dslist.toArray(dStores);

        return dStores;
    }

    /**
     * toDTO purpose.
     * 
     * <p>
     * This method is package visible only, and returns a reference to the
     * GeoServerDTO. This method is unsafe, and should only be used with
     * extreme caution.
     * </p>
     *
     * @return DataDTO the generated object
     */
    Object toDTO() {
        return catalog;
    }

    /**
     * Locate a DataStoreInfo by its id attribute.
     *
     * @param id the DataStoreInfo id looked for
     *
     * @return the DataStoreInfo with id attribute equals to <code>id</code> or
     *         null if there no exists
     */
    public DataStoreInfo getDataStoreInfo(String id) {
        return (DataStoreInfo) dataStores.get(id);
    }

    /**
     * getNameSpaces purpose.
     * 
     * <p>
     * List of all relevant namespaces
     * </p>
     *
     * @return NameSpaceInfo[]
     */
    public NameSpaceInfo[] getNameSpaces() {
        NameSpaceInfo[] ns = new NameSpaceInfo[nameSpaces.values().size()];

        return (NameSpaceInfo[]) new ArrayList(nameSpaces.values()).toArray(ns);
    }

    /**
     * getNameSpace purpose.
     * 
     * <p>
     * The NameSpaceInfo from the specified prefix
     * </p>
     *
     * @param prefix
     *
     * @return NameSpaceInfo resulting from the specified prefix
     */
    public NameSpaceInfo getNameSpace(String prefix) {
        NameSpaceInfo retNS = (NameSpaceInfo) nameSpaces.get(prefix);

        return retNS;
    }

    /**
     * getDefaultNameSpace purpose.
     * 
     * <p>
     * Returns the default NameSpaceInfo for this Data object.
     * </p>
     *
     * @return NameSpaceInfo the default name space
     */
    public NameSpaceInfo getDefaultNameSpace() {
        return defaultNameSpace;
    }

    /**
     * getStyles purpose.
     * 
     * <p>
     * A reference to the map of styles
     * </p>
     *
     * @return Map A map containing the Styles.
     */
    public Map getStyles() {
        return this.styles;
    }

    public Style getStyle(String id) {
        return (Style) styles.get(id);
    }

    /**
     * getFeatureTypeInfo purpose.
     * 
     * <p>
     * returns the FeatureTypeInfo for the specified unique name
     * </p>
     *
     * @param typeName String The FeatureTypeInfo Name
     *
     * @return FeatureTypeInfo
     *
     * @throws NoSuchElementException
     */
    public FeatureTypeInfo getFeatureTypeInfo(String typeName)
        throws NoSuchElementException {
        int prefixDelimPos = typeName.lastIndexOf(":");

        if (prefixDelimPos < 0) {
            //for backwards compatibility.  Only works if all
            //featureTypes have the same prefix.
            typeName = getDefaultNameSpace().getPrefix() + ":" + typeName;
        }

        LOGGER.finest("getting type " + typeName);

        FeatureTypeInfo ftype = (FeatureTypeInfo) featureTypes.get(typeName);

        if (ftype == null) {
            throw new NoSuchElementException(
                "there is no FeatureTypeConfig named " + typeName
                + " configured in this server");
        }

        return ftype;
    }

    /**
     * Gets a FeatureTypeInfo from a local type name (ie unprefixed), and a
     * uri.  This method is slow, use getFeatureType(String typeName), where
     * possible.  For not he only user should be TransactionFeatureHandler.
     *
     * @param localName NameSpaceInfo name
     * @param uri NameSpaceInfo uri
     *
     * @return FeatureTypeInfo
     */
    public FeatureTypeInfo getFeatureTypeInfo(String localName, String uri) {
        for (Iterator it = featureTypes.values().iterator(); it.hasNext();) {
            FeatureTypeInfo fType = (FeatureTypeInfo) it.next();

            if (fType.isEnabled()) {
                if (fType.getShortName().equals(localName)
                        && fType.getNameSpace().getUri().equals(uri)) {
                    return fType;
                }
            }
        }

        return null;
    }

    /**
     * getFeatureTypeInfos purpose.
     * 
     * <p>
     * Returns all the featuretype information objects
     * </p>
     *
     * @return Map the Feature Type's inofrmation
     */
    public Map getFeatureTypeInfos() {
        return this.featureTypes;
    }

    //TODO: detect if a user put a full url, instead of just one to be resolved, and
    //use that instead.
    public Style loadStyle(String fileName, String base)
        throws IOException {
        URL url;

        //HACK: but I'm not sure if we can get the GeoServer instance.  This is one thing
        //that will benefit from splitting up of config loading from representation.
        url = new File(base + fileName).toURL();

        SLDStyle stylereader = new SLDStyle(styleFactory, url);
        Style[] layerstyle = stylereader.readXML();

        return layerstyle[0];
    }

    public Style loadStyle(File fileName) throws IOException {
        URL url;

        //HACK: but I'm not sure if we can get the GeoServer instance.  This is one thing
        //that will benefit from splitting up of config loading from representation.
        url = fileName.toURL();

        SLDStyle stylereader = new SLDStyle(styleFactory, url);
        Style[] layerstyle = stylereader.readXML();

        return layerstyle[0];
    }

    /**
     * tests whether a given file is a file containing type information.
     *
     * @param testFile the file to test.
     *
     * @return <tt>true</tt> if the file has type info.
     */
    private static boolean isInfoFile(File testFile) {
        String testName = testFile.getAbsolutePath();
        int start = testName.length() - INFO_FILE.length();
        int end = testName.length();

        return testName.substring(start, end).equals(INFO_FILE);
    }

    /**
     * Release lock by authorization
     *
     * @param lockID
     */
    public void lockRelease(String lockID) {
        boolean refresh = false;

        for (Iterator i = dataStores.values().iterator(); i.hasNext();) {
            DataStoreInfo meta = (DataStoreInfo) i.next();

            if (!meta.isEnabled()) {
                continue; // disabled
            }

            DataStore dataStore;

            try {
                dataStore = meta.getDataStore();
            } catch (IOException notAvailable) {
                continue; // not available
            }

            LockingManager lockingManager = dataStore.getLockingManager();

            if (lockingManager == null) {
                continue; // locks not supported
            }

            Transaction t = new DefaultTransaction("Refresh "
                    + meta.getNameSpace());

            try {
                t.addAuthorization(lockID);

                if (lockingManager.release(lockID, t)) {
                    refresh = true;
                }
            } catch (IOException e) {
                LOGGER.log(Level.WARNING, e.getMessage(), e);
            } finally {
                try {
                    t.close();
                } catch (IOException closeException) {
                    LOGGER.log(Level.FINEST, closeException.getMessage(),
                        closeException);
                }
            }
        }

        if (!refresh) {
            // throw exception? or ignore...
        }
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
    public void lockRefresh(String lockID) {
        boolean refresh = false;

        for (Iterator i = dataStores.values().iterator(); i.hasNext();) {
            DataStoreInfo meta = (DataStoreInfo) i.next();

            if (!meta.isEnabled()) {
                continue; // disabled
            }

            DataStore dataStore;

            try {
                dataStore = meta.getDataStore();
            } catch (IOException notAvailable) {
                continue; // not available
            }

            LockingManager lockingManager = dataStore.getLockingManager();

            if (lockingManager == null) {
                continue; // locks not supported
            }

            Transaction t = new DefaultTransaction("Refresh "
                    + meta.getNameSpace());

            try {
                t.addAuthorization(lockID);

                if (lockingManager.refresh(lockID, t)) {
                    refresh = true;
                }
            } catch (IOException e) {
                LOGGER.log(Level.WARNING, e.getMessage(), e);
            } finally {
                try {
                    t.close();
                } catch (IOException closeException) {
                    LOGGER.log(Level.FINEST, closeException.getMessage(),
                        closeException);
                }
            }
        }

        if (!refresh) {
            // throw exception? or ignore...
        }
    }

    /**
     * Implement lockRefresh.
     *
     * @param lockID
     * @param t
     *
     * @return true if lock was found and refreshed
     *
     * @throws IOException
     *
     * @see org.geotools.data.Data#lockRefresh(java.lang.String,
     *      org.geotools.data.Transaction)
     */
    public boolean lockRefresh(String lockID, Transaction t)
        throws IOException {
        boolean refresh = false;

        for (Iterator i = dataStores.values().iterator(); i.hasNext();) {
            DataStoreInfo meta = (DataStoreInfo) i.next();

            if (!meta.isEnabled()) {
                continue; // disabled
            }

            DataStore dataStore;

            try {
                dataStore = meta.getDataStore();
            } catch (IOException notAvailable) {
                continue; // not available
            }

            LockingManager lockingManager = dataStore.getLockingManager();

            if (lockingManager == null) {
                continue; // locks not supported
            }

            if (lockingManager.refresh(lockID, t)) {
                refresh = true;
            }
        }

        return refresh;
    }

    /**
     * Implement lockRelease.
     *
     * @param lockID
     * @param t
     *
     * @return true if the lock was found and released
     *
     * @throws IOException
     *
     * @see org.geotools.data.Data#lockRelease(java.lang.String,
     *      org.geotools.data.Transaction)
     */
    public boolean lockRelease(String lockID, Transaction t)
        throws IOException {
        boolean release = false;

        for (Iterator i = dataStores.values().iterator(); i.hasNext();) {
            DataStoreInfo meta = (DataStoreInfo) i.next();

            if (!meta.isEnabled()) {
                continue; // disabled
            }

            DataStore dataStore;

            try {
                dataStore = meta.getDataStore();
            } catch (IOException notAvailable) {
                continue; // not available
            }

            LockingManager lockingManager = dataStore.getLockingManager();

            if (lockingManager == null) {
                continue; // locks not supported
            }

            if (lockingManager.release(lockID, t)) {
                release = true;
            }
        }

        return release;
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
    public boolean lockExists(String lockID) {
        for (Iterator i = dataStores.values().iterator(); i.hasNext();) {
            DataStoreInfo meta = (DataStoreInfo) i.next();

            if (!meta.isEnabled()) {
                continue; // disabled
            }

            DataStore dataStore;

            try {
                dataStore = meta.getDataStore();
            } catch (IOException notAvailable) {
                continue; // not available
            }

            LockingManager lockingManager = dataStore.getLockingManager();

            if (lockingManager == null) {
                continue; // locks not supported
            }

            if (lockingManager.exists(lockID)) {
                return true;
            }
        }

        return false;
    }

    //
    // GeoTools2 Catalog API
    // 
    /**
     * Set of available Namespace prefixes.
     * @see org.geotools.data.Catalog#getPrefixes()
     * 
     * @return Set of namespace Prefixes
     */
    public Set getPrefixes() {
        return Collections.unmodifiableSet( nameSpaces.keySet() );
    }

    /**
     * Prefix of the defaultNamespace.
     * @see org.geotools.data.Catalog#getDefaultPrefix()
     * @return prefix of the default namespace
     */
    public String getDefaultPrefix() {
        return defaultNameSpace.getPrefix();
    }

    /**
     * Implement getNamespace.
     * <p>
     * Description ...
     * </p>
     * @see org.geotools.data.Catalog#getNamespace(java.lang.String)
     * 
     * @param prefix
     * @return
     */
    public NamespaceMetaData getNamespaceMetaData(String prefix) {
        return (NamespaceMetaData) getNameSpace( prefix );
    }

    /**
     * Register a DataStore with this Catalog.
     * <p>
     * This is part of the public CatalogAPI, the fact that we don't want
     * to support it here may be gounds for it's removal. 
     * </p>
     * <p>
     * GeoSever and the global package would really like to have <b>complete</b>
     * control over the DataStores in use by the application. It recognize that
     * this may not always be possible. As GeoServer is extend with additional
     * Modules (such as config) that wish to locate and talk to DataStores
     * independently of GeoServer the best we can do is ask them to register
     * with the this Catalog in global.
     * </p>
     * <p>
     * This reveals what may be a deisgn flaw in GeoTools2 DataStore. We have
     * know way of knowing if the dataStore has already been placed into our
     * care as DataStores are not good at identifying themselves. To complicate
     * matters most keep a static connectionPool around in their Factory - it
     * could be that the Factories are supposed to be smart enough to prevent
     * duplication.
     * </p>
     * 
     * @see org.geotools.data.Catalog#registerDataStore(org.geotools.data.DataStore)
     * 
     * @param dataStore
     * @throws IOException
     */
    public void registerDataStore(DataStore dataStore) throws IOException {
            
    }

    /**
     * Access to the set of DataStores in use by GeoServer.
     * <p>
     * The provided Set may not be modified :-)
     * </p>
     * @see org.geotools.data.Catalog#getDataStores(java.lang.String)
     * 
     * @param namespace
     * @return
     */
    public Set getDataStores() {
        return Collections.unmodifiableSet( new HashSet( dataStores.values() ) );
    }

    /**
     * Convience method for Accessing FeatureSource by prefix:typeName.
     * <p>
     * This method is part of the public Catalog API. It allows the Validation
     * framework to be writen using only public Geotools2 interfaces.
     * </p>
     * @see org.geotools.data.Catalog#getFeatureSource(java.lang.String, java.lang.String)
     * 
     * @param prefix Namespace prefix in which the FeatureType available
     * @param typeName typeNamed used to identify FeatureType
     * @return
     */
    public FeatureSource getFeatureSource(String prefix, String typeName) throws IOException {
        NamespaceMetaData namespace = getNamespaceMetaData( prefix );
        FeatureTypeMetaData featureType = namespace.getFeatureTypeMetaData( typeName );
        DataStoreMetaData dataStore = featureType.getDataStoreMetaData();
        
        return dataStore.getDataStore().getFeatureSource( typeName );       
    }
}
