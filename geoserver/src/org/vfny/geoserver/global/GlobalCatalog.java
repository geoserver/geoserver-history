/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.global;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.geotools.data.DataStore;
import org.geotools.data.DefaultTransaction;
import org.geotools.data.LockingManager;
import org.geotools.data.Transaction;
import org.geotools.styling.SLDStyle;
import org.geotools.styling.Style;
import org.geotools.styling.StyleFactory;
import org.vfny.geoserver.config.data.*;

/**
 * Holds the featureTypes.  Replaced TypeRepository.
 *
 * @author Gabriel Roldán
 * @author Chris Holmes
 * @version $Id: GlobalCatalog.java,v 1.1.2.2 2004/01/03 00:20:14 dmzwiers Exp $
 */
public class GlobalCatalog extends GlobalAbstract
/**
 * implements GlobalCatalog
 */
 {
    /** DOCUMENT ME! */
    private static final Logger LOGGER = Logger.getLogger(
            "org.vfny.geoserver.config");

    /** Default name of feature type information */
	private static final String INFO_FILE = "info.xml";
	
	private static StyleFactory styleFactory = StyleFactory.createStyleFactory();

    /** The holds the mappings between prefixes and uri's */
    private Map nameSpaces;

    /** DOCUMENT ME! */
    private GlobalNameSpace defaultNameSpace;

    /** DOCUMENT ME! */
    private Map dataStores;

    /** DOCUMENT ME! */
    private Map styles;

    /**
     * Map of <code>GlobalFeatureType</code>'s stored by full qualified name
     * (namespace prefix + PREFIX_DELIMITER + typeName)
     */
    private Map featureTypes;

    
    private CatalogConfig catalog;
    
    // we create instances of everything at the start to support the datastore connections
    public GlobalCatalog(CatalogConfig config) throws ConfigurationException {
    	catalog = config;

    	Iterator i = config.getDataStores().keySet().iterator();
    	while(i.hasNext()){
    		Object key = i.next();
    		dataStores.put(key,new GlobalDataStore((org.vfny.geoserver.config.data.DataStoreConfig)config.getDataStores().get(key)));
    	}

		i = config.getFeaturesTypes().keySet().iterator();
		while(i.hasNext()){
			Object key = i.next();
			featureTypes.put(key,new GlobalFeatureType((org.vfny.geoserver.config.data.FeatureTypeConfig)config.getFeaturesTypes().get(key)));
		}
    	defaultNameSpace = new GlobalNameSpace(config.getDefaultNameSpace());

		i = config.getNameSpaces().keySet().iterator();
		while(i.hasNext()){
			Object key = i.next();
			nameSpaces.put(key,new GlobalNameSpace((org.vfny.geoserver.config.data.NameSpaceConfig)config.getNameSpaces().get(key)));
		}

		i = config.getStyles().keySet().iterator();
		while(i.hasNext()){
			Object key = i.next();
			// should be re-worked
			try{
				styles.put(key,loadStyle(((org.vfny.geoserver.config.data.StyleConfig)config.getStyles().get(key)).getFilename()));
			}catch(IOException e){
				LOGGER.fine("Error loading style:"+key.toString());
			}
		}
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public GlobalDataStore[] getDataStores() {
        List dslist = new ArrayList(dataStores.values());
        GlobalDataStore[] dStores = new GlobalDataStore[dslist.size()];
        dStores = (GlobalDataStore[]) dslist.toArray(dStores);

        return dStores;
    }

    /**
     * searches a GlobalDataStore by its id attribute
     *
     * @param id the GlobalDataStore id looked for
     *
     * @return the GlobalDataStore with id attribute equals to <code>id</code>
     *         or null if there no exists
     */
    public GlobalDataStore getDataStore(String id) {
        return (GlobalDataStore) dataStores.get(id);
    }

    /**
     * returns the list of GlobalDataStore's of the given namespace
     *
     * @param ns DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public List getDataStores(GlobalNameSpace ns) {
        List dataStoresNs = new ArrayList();
        GlobalDataStore dsc;

        for (Iterator it = dataStores.values().iterator(); it.hasNext();) {
            dsc = (GlobalDataStore) it.next();

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
    public GlobalNameSpace[] getNameSpaces() {
        GlobalNameSpace[] ns = new GlobalNameSpace[nameSpaces.values().size()];

        return (GlobalNameSpace[]) new ArrayList(nameSpaces.values()).toArray(ns);
    }

    /**
     * DOCUMENT ME!
     *
     * @param prefix DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public GlobalNameSpace getNameSpace(String prefix) {
        GlobalNameSpace retNS = (GlobalNameSpace) nameSpaces.get(prefix);

        return retNS;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public GlobalNameSpace getDefaultNameSpace() {
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

    public Style getStyle(String id) {
        return (Style) styles.get(id);
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
    public GlobalFeatureType getFeatureType(String typeName)
        throws NoSuchElementException {
        int prefixDelimPos = typeName.lastIndexOf(GlobalNameSpace.PREFIX_DELIMITER);

        if (prefixDelimPos < 0) {
            //for backwards compatibility.  Only works if all
            //featureTypes have the same prefix.
            typeName = getDefaultNameSpace().getPrefix()
                + GlobalNameSpace.PREFIX_DELIMITER + typeName;
        }

        LOGGER.finest("getting type " + typeName);

        GlobalFeatureType ftype = (GlobalFeatureType) featureTypes.get(typeName);

        if (ftype == null) {
            throw new NoSuchElementException("there is no FeatureTypeConfig named "
                + typeName + " configured in this server");
        }

        return ftype;
    }

    /**
     * Gets a GlobalFeatureType from a local type name (ie unprefixed), and a
     * uri.  This method is slow, use getFeatureType(String typeName), where
     * possible.  For not he only user should be TransactionFeatureHandler.
     *
     * @param localName DOCUMENT ME!
     * @param uri DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public GlobalFeatureType getFeatureType(String localName, String uri) {
        for (Iterator it = featureTypes.values().iterator(); it.hasNext();) {
            GlobalFeatureType fType = (GlobalFeatureType) it.next();

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
   /* private void loadNameSpaces(Element nsRoot) throws ConfigurationException {
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

            GlobalNameSpace ns = new GlobalNameSpace(prefix, uri, defaultNS);
            LOGGER.config("added namespace " + ns);
            nameSpaces.put(prefix, ns);

            if (defaultNS) {
                defaultNameSpace = ns;
            }
        }
    }*/

    /**
     * DOCUMENT ME!
     *
     * @param dsRoot DOCUMENT ME!
     *
     * @throws ConfigurationException DOCUMENT ME!
     */
   /* private void loadDataStores(Element dsRoot) throws ConfigurationException {
        dataStores = new HashMap();

        NodeList dsElements = dsRoot.getElementsByTagName("datastore");
        int dsCnt = dsElements.getLength();
        GlobalDataStore dsConfig;
        Element dsElem;

        for (int i = 0; i < dsCnt; i++) {
            dsElem = (Element) dsElements.item(i);
            dsConfig = new GlobalDataStore(dsElem, this);

            if (dataStores.containsKey(dsConfig.getId())) {
                throw new ConfigurationException("duplicated datastore id: "
                    + dsConfig.getNameSpace());
            }

            dataStores.put(dsConfig.getId(), dsConfig);
        }
    }*/

    /**
     * DOCUMENT ME!
     *
     * @param stylesElem DOCUMENT ME!
     * @param styleDir DOCUMENT ME!
     *
     * @throws ConfigurationException DOCUMENT ME!
     *
     * @task TODO: load styles as SLD StyleConfig objects.
     * @task REVISIT: Do we want to load all up front?  Or do them dynamicall?
     *       What would be real nice for all these that we do up front now,
     *       like datastores, that we we used to do dynamically, is to give
     *       users the option of when they want to load them...
     */
    /*private void loadStyles(Element stylesElem, String styleDir)
        throws ConfigurationException {
        styles = new HashMap();

        NodeList stylesList = null;

        if (stylesElem != null) {
            stylesList = stylesElem.getElementsByTagName("style");
        }

        if ((stylesList == null) || (stylesList.getLength() == 0)) {
            //no styles where defined, just add a default one
            styles.put("normal", "styles/normal.sld");

            //return;
        }

        int styleCount = stylesList.getLength();
        Element styleElem;

        for (int i = 0; i < styleCount; i++) {
            styleElem = (Element) stylesList.item(i);

            String stId = getAttribute(styleElem, "id", true);
            String stFile = getAttribute(styleElem, "filename", true);

            //File file = new File(stFile);
            try {
                Style style = loadStyle(stFile, styleDir);
                LOGGER.fine("loaded style, id: " + stId + ", style: " + style);
                styles.put(stId, style);
            } catch (java.io.IOException fnfe) {
                LOGGER.warning("could not load style at " + stFile + ": "
                    + fnfe.getMessage());

                //throw new ConfigurationException(fnfe);
            }

            //styles.put(stId, stFile);
        }
    }*/

    //TODO: detect if a user put a full url, instead of just one to be resolved, and
    //use that instead.
    public Style loadStyle(String fileName, String base)
        throws IOException {
        URL url;

        //HACK: but I'm not sure if we can get the GlobalServer instance.  This is one thing
        //that will benefit from splitting up of config loading from representation.
        url = new File(base + fileName).toURL();

        SLDStyle stylereader = new SLDStyle(styleFactory, url);
        Style[] layerstyle = stylereader.readXML();

        return layerstyle[0];
    }
	public Style loadStyle(File fileName)
		throws IOException {
		URL url;

		//HACK: but I'm not sure if we can get the GlobalServer instance.  This is one thing
		//that will benefit from splitting up of config loading from representation.
		url = fileName.toURL();

		SLDStyle stylereader = new SLDStyle(styleFactory, url);
		Style[] layerstyle = stylereader.readXML();

		return layerstyle[0];
	}

    //} else {
    //url = file.toURL();
    //}
    //LOGGER.fine("pulling default style from "+url.toString());
    //LOGGER.fine("loading sld from " + url);

    /**
     * DOCUMENT ME!
     *
     * @param currentFile DOCUMENT ME!
     *
     * @throws ConfigurationException DOCUMENT ME!
     */
   /* private void loadFeatureTypes(File currentFile)
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
            Element featureElem = GlobalServer.loadConfig(currentFile.toString());
            GlobalFeatureType ft = null;

            try {
                File parentDir = currentFile.getParentFile();
                ft = new GlobalFeatureType(this, featureElem);

                String pathToSchemaFile = new File(parentDir, "schema.xml")
                    .toString();
                LOGGER.finest("pathToSchema is " + pathToSchemaFile);
                ft.setSchemaFile(pathToSchemaFile);
                featureTypes.put(ft.getName(), ft);
                LOGGER.finer("added featureType " + ft.getName());
            } catch (ConfigurationException cfge) {
                //HACK: should use a logger.
                cfge.printStackTrace(System.out);
                LOGGER.warning("could not add GlobalFeatureType at " + currentFile
                    + " due to " + cfge);
            }
        }
    }*/

    /*  private void loadType(String filePath) throws ConfigurationException {
       try {
           Element featureElem = GlobalServer.loadConfig(configFile);
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
       GlobalFeatureType ft = null;
       for (int i = 0; i < ftCount; i++) {
           ft = new GlobalFeatureType(this, (Element) ftlist.item(i));
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
            GlobalDataStore meta = (GlobalDataStore) i.next();

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
            GlobalDataStore meta = (GlobalDataStore) i.next();

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
     * @see org.geotools.data.GlobalCatalog#lockRefresh(java.lang.String,
     *      org.geotools.data.Transaction)
     */
    public boolean lockRefresh(String lockID, Transaction t)
        throws IOException {
        boolean refresh = false;

        for (Iterator i = dataStores.values().iterator(); i.hasNext();) {
            GlobalDataStore meta = (GlobalDataStore) i.next();

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
     * @see org.geotools.data.GlobalCatalog#lockRelease(java.lang.String,
     *      org.geotools.data.Transaction)
     */
    public boolean lockRelease(String lockID, Transaction t)
        throws IOException {
        boolean release = false;

        for (Iterator i = dataStores.values().iterator(); i.hasNext();) {
            GlobalDataStore meta = (GlobalDataStore) i.next();

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
     * @see org.geotools.data.GlobalCatalog#lockExists(java.lang.String)
     */
    public boolean lockExists(String lockID) {
        for (Iterator i = dataStores.values().iterator(); i.hasNext();) {
            GlobalDataStore meta = (GlobalDataStore) i.next();

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
}
