/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.config;

import org.geotools.data.Catalog;
import org.geotools.data.DataStore;
import org.geotools.data.DefaultTransaction;
import org.geotools.data.FeatureLock;
import org.geotools.data.FeatureLockFactory;
import org.geotools.data.FeatureLocking;
import org.geotools.data.FeatureSource;
import org.geotools.data.LockingManager;
import org.geotools.data.Transaction;
import org.geotools.data.DataStoreFinder;
import org.geotools.feature.FeatureType;
import org.geotools.filter.*;
import org.vfny.geoserver.WfsException;
import org.vfny.geoserver.requests.wfs.*;
import org.vfny.geoserver.responses.wfs.*;
import org.w3c.dom.*;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Holds the featureTypes.  Replaced TypeRepository.
 *
 * @author Gabriel Roldán
 * @author Chris Holmes
 * @version $Id: CatalogConfig.java,v 1.1.2.10 2003/12/03 20:49:59 cholmesny Exp $
 */
public class CatalogConfig extends AbstractConfig /**implements Catalog**/ {
    /** DOCUMENT ME! */
    private static final Logger LOGGER = Logger.getLogger(
            "org.vfny.geoserver.config");

    /** Default name of feature type information */
    public static final String INFO_FILE = "info.xml";

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
     * Configure based on gt2 Catalog.
     * 
     * <p>
     * Quick hack for JUnit test cases, really the gt2 Catalog interface should
     * be implemented by CatalogConfig. And whatever methods  GeoServer needs
     * to get its job done is what gt2 Catalog needs to provide.
     * </p>
     * Right now a Map is provided to set anything that gt2 Catalog cannot.
     * 
     * <p>
     * Given a namespace foo in the catalog the config map defines:
     * 
     * <ul>
     * <li>
     * foo.uri: String (default foo)
     * </li>
     * <li>
     * foo.default: boolean (default false, true if only one namespace)
     * </li>
     * </ul>
     * </p>
     *
     * @param config DOCUMENT ME!
     * @param catalog
     */
    public CatalogConfig(Map config, Catalog catalog) {
        LOGGER.info("loading catalog configuration");

        String[] spaceNames = catalog.getNameSpaces();
        nameSpaces = new HashMap(spaceNames.length);

        for (int i = 0; i < spaceNames.length; i++) {
            String name = spaceNames[i];

            String uri = get(config, name + ".uri", name);
            boolean defaultNS = get(config, name + ".default",
                    spaceNames.length == 1);

            NameSpace ns = new NameSpace(name, uri, defaultNS);

            if (defaultNS) {
                defaultNameSpace = ns;
            }

            LOGGER.config("Added NameSpace: " + ns);
            nameSpaces.put(name, ns);
        }

        LOGGER.info("loading DataStore configuration");
	Iterator iter = DataStoreFinder.getAvailableDataSources();

        while (iter.hasNext()) {
            LOGGER.fine(iter.next() + " is an available DataSource");
        }
        // gt2 currently assumes one datastore per namespace
        //
        // I know this is wrong, please feed our requirements into the gt2
        // Catalog        
        dataStores = new HashMap(spaceNames.length);

        DataStoreConfig dsConfig;

        for (int i = 0; i < spaceNames.length; i++) {
            DataStore store = catalog.getDataStore(spaceNames[i]);
            NameSpace nameSpace = (NameSpace) nameSpaces.get(spaceNames[i]);
            DataStoreConfig dataStoreConfig = new DataStoreConfig(config,
                    store, nameSpace);

            dataStores.put(dataStoreConfig.getId(), dataStoreConfig);
        }

        LOGGER.info("loading style configuration");
        styles = new HashMap();

        // just use the default style
        styles.put("normal", "styles/normal.sld");

        LOGGER.info("loading FeatureType configuration");
        featureTypes = new HashMap();

        for (Iterator i = dataStores.values().iterator(); i.hasNext();) {
            DataStoreConfig dataStoreConfig = (DataStoreConfig) i.next();

            try {
                DataStore store = dataStoreConfig.getDataStore();

                String[] typeNames = store.getTypeNames();

                for (int t = 0; t < typeNames.length; t++) {
                    FeatureType type;

                    try {
                        type = store.getSchema(typeNames[t]);

                        FeatureTypeConfig typeConfig = new FeatureTypeConfig(config,
                                type, dataStoreConfig);
                        featureTypes.put(typeNames[t], typeConfig);
                    } catch (IOException e) {
			
			LOGGER.log(Level.CONFIG, "problem loading type " + typeNames[t], e); 
                        // type was not available?
                    }
                }
            } catch (IOException e) {
		LOGGER.log(Level.CONFIG, "problem loading datastore " + dataStoreConfig, e); 
                // datastore not available                
            }
        }
    }

    /**
     * Creates a new CatalogConfig object.
     *
     * @param root DOCUMENT ME!
     * @param featureTypeDir DOCUMENT ME!
     *
     * @throws ConfigurationException DOCUMENT ME!
     */
    public CatalogConfig(Element root, String featureTypeDir)
        throws ConfigurationException {
        LOGGER.info("loading catalog configuration");
        loadNameSpaces(getChildElement(root, "namespaces", true));
        loadDataStores(getChildElement(root, "datastores", true));
        loadStyles(getChildElement(root, "styles", false));

        File startDir = new File(featureTypeDir);
        this.featureTypes = new HashMap();
        loadFeatureTypes(startDir);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public DataStoreConfig[] getDataStores() {
        List dslist = new ArrayList(dataStores.values());
        DataStoreConfig[] dStores = new DataStoreConfig[dslist.size()];
        dStores = (DataStoreConfig[]) dslist.toArray(dStores);

        return dStores;
    }

    /**
     * searches a DataStoreConfig by its id attribute
     *
     * @param id the DataStore id looked for
     *
     * @return the DataStoreConfig with id attribute equals to <code>id</code>
     *         or null if there no exists
     */
    public DataStoreConfig getDataStore(String id) {
        return (DataStoreConfig) dataStores.get(id);
    }

    /**
     * returns the list of DataStoreConfig's of the given namespace
     *
     * @param ns DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public List getDataStores(NameSpace ns) {
        List dataStoresNs = new ArrayList();
        DataStoreConfig dsc;

        for (Iterator it = dataStores.values().iterator(); it.hasNext();) {
            dsc = (DataStoreConfig) it.next();

            if (dsc.getNameSpace().equals(ns)) {
                dataStoresNs.add(dsc);
            }
        }

        return dataStoresNs;
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
        NameSpace retNS = (NameSpace) nameSpaces.get(prefix);

        return retNS;
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
     * Gets a FeatureTypeConfig from a local type name (ie unprefixed), and a
     * uri.  This method is slow, use getFeatureType(String typeName), where
     * possible.  For not he only user should be TransactionFeatureHandler.
     *
     * @param localName DOCUMENT ME!
     * @param uri DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public FeatureTypeConfig getFeatureType(String localName, String uri) {
        for (Iterator it = featureTypes.values().iterator(); it.hasNext();) {
            FeatureTypeConfig fType = (FeatureTypeConfig) it.next();

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
            LOGGER.config("added namespace " + ns);
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

            if (dataStores.containsKey(dsConfig.getId())) {
                throw new ConfigurationException("duplicated datastore id: "
                    + dsConfig.getNameSpace());
            }

            dataStores.put(dsConfig.getId(), dsConfig);
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
     * @param currentFile DOCUMENT ME!
     *
     * @throws ConfigurationException DOCUMENT ME!
     */
    private void loadFeatureTypes(File currentFile)
        throws ConfigurationException {
        LOGGER.finest("examining: " + currentFile.getAbsolutePath());
        LOGGER.finest("is dir: " + currentFile.isDirectory());

        if (currentFile.isDirectory()) {
            File[] file = currentFile.listFiles();

            for (int i = 0, n = file.length; i < n; i++) {
                loadFeatureTypes(file[i]);
            }
        } else if (isInfoFile(currentFile)) {
            String curPath = currentFile.getAbsolutePath();
            Element featureElem = ServerConfig.loadConfig(currentFile.toString());
            FeatureTypeConfig ft = null;

            try {
                File parentDir = currentFile.getParentFile();
                ft = new FeatureTypeConfig(this, featureElem);

                String pathToSchemaFile = new File(parentDir, "schema.xml")
                    .toString();
                LOGGER.finest("pathToSchema is " + pathToSchemaFile);
                ft.setSchemaFile(pathToSchemaFile);
                featureTypes.put(ft.getName(), ft);
                LOGGER.finer("added featureType " + ft.getName());
            } catch (ConfigurationException cfge) {
                LOGGER.warning("could not add FeatureType at " + currentFile
                    + " due to " + cfge);
            }
        }
    }

    /*  private void loadType(String filePath) throws ConfigurationException {
       try {
           Element featureElem = ServerConfig.loadConfig(configFile);
           String featureTag = featureElem.getTagName();
           if (!featureTag.equals(rootTag) && !featureTag.equals(OLD_ROOT_TAG)) {
               featureElem = (Element) featureElem.getElementsByTagName(rootTag)
                                                  .item(0);
               if (featureElem == null) {
                   String message = "could not find root tag: " + rootTag
                       + " in file: " + filePath;
                   LOGGER.warning(message);
                   throw new ConfigurationException(message);
               }
           }
       NodeList ftlist = fTypesElem.getElementsByTagName("featureType");
       Element ftypeElem;
       int ftCount = ftlist.getLength();
       FeatureTypeConfig ft = null;
       for (int i = 0; i < ftCount; i++) {
           ft = new FeatureTypeConfig(this, (Element) ftlist.item(i));
           featureTypes.put(ft.getName(), ft);
       }
       }*/

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
            DataStoreConfig meta = (DataStoreConfig) i.next();
            if (!meta.isEnabled()) continue; // disabled
    
            DataStore dataStore;                
            try {
                dataStore = meta.getDataStore();
            }
            catch(IOException notAvailable){
                continue; // not available
            }
            LockingManager lockingManager = dataStore.getLockingManager();
            if( lockingManager == null ) continue; // locks not supported
    
            Transaction t = new DefaultTransaction("Refresh "+meta.getNameSpace() );
            try {
                t.addAuthorization( lockID );
                if( lockingManager.release( lockID, t ) ){
                    refresh = true;
                }
            } catch (IOException e) {
                LOGGER.log( Level.WARNING, e.getMessage(), e );
            }
            finally {
                try {
                    t.close();
                } catch (IOException closeException) {
                    LOGGER.log(Level.FINEST, closeException.getMessage(), closeException);
                }
            }
        }
        if( !refresh ){
            // throw exception? or ignore...
        }
    }

    /**
     * Refresh lock by authorization
     * <p>
     * Should use your own transaction?
     * </p>
     * @param lockID
     */
    public void lockRefresh(String lockID){
        boolean refresh = false;
        for (Iterator i = dataStores.values().iterator(); i.hasNext();) {
            DataStoreConfig meta = (DataStoreConfig) i.next();
            if (!meta.isEnabled()) continue; // disabled
            
            DataStore dataStore;                
            try {
                dataStore = meta.getDataStore();
            }
            catch(IOException notAvailable){
                continue; // not available
            }
            LockingManager lockingManager = dataStore.getLockingManager();
            if( lockingManager == null ) continue; // locks not supported
            
            Transaction t = new DefaultTransaction("Refresh "+meta.getNameSpace() );
            try {
                t.addAuthorization( lockID );
                if( lockingManager.refresh( lockID, t ) ){
                    refresh = true;
                }
            } catch (IOException e) {
                LOGGER.log( Level.WARNING, e.getMessage(), e );
            }
            finally {
                try {
                    t.close();
                } catch (IOException closeException) {
                    LOGGER.log(Level.FINEST, closeException.getMessage(), closeException);
                }
            }
        }
        if( !refresh ){
            // throw exception? or ignore...
        }
    }

    /**
     * Implement lockRefresh.
     * 
     * @see org.geotools.data.Catalog#lockRefresh(java.lang.String, org.geotools.data.Transaction)
     * 
     * @param arg0
     * @param arg1
     * @return true if lock was found and refreshed
     * @throws IOException
     */
    public boolean lockRefresh(String lockID, Transaction t) throws IOException {
        boolean refresh = false;
        for (Iterator i = dataStores.values().iterator(); i.hasNext();) {
            DataStoreConfig meta = (DataStoreConfig) i.next();
            if (!meta.isEnabled()) continue; // disabled
            
            DataStore dataStore;                
            try {
                dataStore = meta.getDataStore();
            }
            catch(IOException notAvailable){
                continue; // not available
            }
            LockingManager lockingManager = dataStore.getLockingManager();
            if( lockingManager == null ) continue; // locks not supported
            
            if( lockingManager.refresh( lockID, t ) ){
                refresh = true;
            }
        }
        return refresh;
    }

    /**
     * Implement lockRelease.
     * 
     * @see org.geotools.data.Catalog#lockRelease(java.lang.String, org.geotools.data.Transaction)
     * 
     * @param lockID
     * @param t
     * @return true if the lock was found and released
     * @throws IOException
     */
    public boolean lockRelease(String lockID, Transaction t) throws IOException {
        boolean release = false;
        for (Iterator i = dataStores.values().iterator(); i.hasNext();) {
            DataStoreConfig meta = (DataStoreConfig) i.next();
            if (!meta.isEnabled()) continue; // disabled
            
            DataStore dataStore;                
            try {
                dataStore = meta.getDataStore();
            }
            catch(IOException notAvailable){
                continue; // not available
            }
            LockingManager lockingManager = dataStore.getLockingManager();
            if( lockingManager == null ) continue; // locks not supported
            
            if( lockingManager.release( lockID, t ) ){
                release = true;
            }
        }
        return release;
    }

    /**
     * Implement lockExists.
     * 
     * @see org.geotools.data.Catalog#lockExists(java.lang.String)
     * 
     * @param lockID
     * @return true if lockID exists
     */
    public boolean lockExists(String lockID) {
        for (Iterator i = dataStores.values().iterator(); i.hasNext();) {
            DataStoreConfig meta = (DataStoreConfig) i.next();
            if (!meta.isEnabled()) continue; // disabled
            
            DataStore dataStore;                
            try {
                dataStore = meta.getDataStore();
            }
            catch(IOException notAvailable){
                continue; // not available
            }
            LockingManager lockingManager = dataStore.getLockingManager();
            if( lockingManager == null ) continue; // locks not supported
            
            if( lockingManager.exists( lockID ) ){
                return true;
            }
        }
        return false;
    }
}
