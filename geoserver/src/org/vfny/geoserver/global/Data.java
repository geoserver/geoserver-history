/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.global;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.LinkedList;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.geotools.data.DataStore;
import org.geotools.data.DefaultTransaction;
import org.geotools.data.LockingManager;
import org.geotools.data.Transaction;
import org.geotools.styling.SLDStyle;
import org.geotools.styling.Style;
import org.geotools.styling.StyleFactory;
import org.vfny.geoserver.global.dto.DataDTO;
import org.vfny.geoserver.global.dto.DataStoreInfoDTO;
import org.vfny.geoserver.global.dto.FeatureTypeInfoDTO;
import org.vfny.geoserver.global.dto.NameSpaceDTO;
import org.vfny.geoserver.global.dto.StyleDTO;

/**
 * Holds the featureTypes.  Replaced TypeRepository.
 *
 * @author Gabriel Roldán
 * @author Chris Holmes
 * @version $Id: Data.java,v 1.1.2.14 2004/01/08 18:25:11 dmzwiers Exp $
 */
public class Data extends Abstract
/**
 * implements Data
 */
 {
    /** DOCUMENT ME! */
    private static final Logger LOGGER = Logger.getLogger(
            "org.vfny.geoserver.global");

    /** Default name of feature type information */
	private static final String INFO_FILE = "info.xml";
	
	private static StyleFactory styleFactory = StyleFactory.createStyleFactory();

    /** The holds the mappings between prefixes and uri's */
    private Map nameSpaces;

    /** DOCUMENT ME! */
    private NameSpace defaultNameSpace;

    /** DOCUMENT ME! */
    private Map dataStores;

    /** DOCUMENT ME! */
    private Map styles;

    /**
     * Map of <code>FeatureTypeInfo</code>'s stored by full qualified name
     * (NameSpace prefix + PREFIX_DELIMITER + typeName)
     */
    private Map featureTypes;

    
    private DataDTO catalog;
    
    // we create instances of everything at the start to support the datastore connections
    public Data(DataDTO config) throws ConfigurationException {
		load(config);
    }
    
    Data(){
    	nameSpaces = new HashMap();
    	styles = new HashMap();
    	featureTypes = new HashMap();
    	dataStores = new HashMap();
    }
	
	/**
	 * load purpose.
	 * Places the data in this container and innitializes it. 
	 * Complex tests are performed to detect existing datasources, 
	 * while the remainder only include simplistic id checks.
	 * </p>
	 * @param config
	 * @throws ConfigurationException
	 */
	void load(DataDTO config) throws ConfigurationException {
		catalog = config;
		if(config == null)
			throw new NullPointerException("");

		if(dataStores == null)
			dataStores = new HashMap();
		if(config.getDataStores() == null)
			throw new NullPointerException("");
		Set s = dataStores.keySet();
		Iterator i = config.getDataStores().keySet().iterator();
		while(i.hasNext()){
			Object key = i.next();
			s.remove(key);
			//find missing ones
			if(!dataStores.containsKey(key)){
				dataStores.put(key,new DataStoreInfo((DataStoreInfoDTO)config.getDataStores().get(key),nameSpaces));
			}else{// check for small changes
				DataStoreInfoDTO dsiDto = (DataStoreInfoDTO)((DataStoreInfo)dataStores.get(key)).toDTO();
				if(dsiDto!=null && !(dsiDto.equals(config.getDataStores().get(key)))){
					dataStores.put(key,new DataStoreInfo((DataStoreInfoDTO)config.getDataStores().get(key),nameSpaces));
				}
			}
		}
		// s contains all the unchecked values.
		List tmp = new LinkedList();
		i = s.iterator();
		while(i.hasNext()){
			tmp.add(i.next());
		}
		for(int j=0;j<tmp.size();j++)
			dataStores.remove(tmp.get(j));
			
			
		List tmp2 = new LinkedList();
		if(featureTypes == null)
			featureTypes = new HashMap();
		s = featureTypes.keySet();
		if(config.getFeaturesTypes() == null)
			throw new NullPointerException("");
		i = config.getFeaturesTypes().keySet().iterator();
		while(i.hasNext()){
			Object key = i.next();
			String nm = ((FeatureTypeInfoDTO)config.getFeaturesTypes().get(key)).getName();
			if(tmp2.contains(nm)){
				throw new ConfigurationException("FeatureTypeInfo.getName() must be unique! ( "+nm+" )");
			}else{
				tmp2.add(nm);
			}
			s.remove(key);
			if(!featureTypes.containsKey(key))
				featureTypes.put(nm,new FeatureTypeInfo((FeatureTypeInfoDTO)config.getFeaturesTypes().get(key), this));
		}
		// s contains all the unchecked values.
		tmp = new LinkedList();
		i = s.iterator();
		while(i.hasNext()){
			tmp.add(i.next());
		}
		for(int j=0;j<tmp.size();j++)
			featureTypes.remove(tmp.get(j));



		if(nameSpaces == null)
			nameSpaces = new HashMap();
		s = nameSpaces.keySet();
		if(config.getNameSpaces() == null)
			throw new NullPointerException("");
		i = config.getNameSpaces().keySet().iterator();
		while(i.hasNext()){
			Object key = i.next();
			s.remove(key);
			if(!nameSpaces.containsKey(key)){
				nameSpaces.put(key,new NameSpace((NameSpaceDTO)config.getNameSpaces().get(key)));
				if(((NameSpaceDTO)config.getNameSpaces().get(key)).isDefault())
					defaultNameSpace = (NameSpace)nameSpaces.get(key);
			}
		}
		// s contains all the unchecked values.
		tmp = new LinkedList();
		i = s.iterator();
		while(i.hasNext()){
			tmp.add(i.next());
		}
		for(int j=0;j<tmp.size();j++)
			nameSpaces.remove(tmp.get(j));



		if(styles == null)
			styles = new HashMap();
		s = styles.keySet();
		if(config.getStyles() == null)
			throw new NullPointerException("");
		i = config.getStyles().keySet().iterator();
		while(i.hasNext()){
			Object key = i.next();
			s.remove(key);
			if(!styles.containsKey(key))
				try{
					styles.put(key,loadStyle(((StyleDTO)config.getStyles().get(key)).getFilename()));
				}catch(IOException e){
					LOGGER.fine("Error loading style:"+key.toString());
				}
		}
		// s contains all the unchecked values.
		tmp = new LinkedList();
		i = s.iterator();
		while(i.hasNext()){
			tmp.add(i.next());
		}
		for(int j=0;j<tmp.size();j++)
			styles.remove(tmp.get(j));
	}

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public DataStoreInfo[] getDataStoreInfos() {
        List dslist = new ArrayList(dataStores.values());
        DataStoreInfo[] dStores = new DataStoreInfo[dslist.size()];
        dStores = (DataStoreInfo[]) dslist.toArray(dStores);

        return dStores;
    }
    
    Object toDTO(){
    	return catalog;
    }

    /**
     * searches a DataStoreInfo by its id attribute
     *
     * @param id the DataStoreInfo id looked for
     *
     * @return the DataStoreInfo with id attribute equals to <code>id</code>
     *         or null if there no exists
     */
    public DataStoreInfo getDataStoreInfo(String id) {
        return (DataStoreInfo) dataStores.get(id);
    }

    /**
     * returns the list of DataStoreInfo's of the given NameSpace
     *
     * @param ns DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    /*public List getDataStores(NameSpace ns) {
        List dataStoresNs = new ArrayList();
        DataStoreInfo dsc;

        for (Iterator it = dataStores.values().iterator(); it.hasNext();) {
            dsc = (DataStoreInfo) it.next();

            if (dsc.getNameSpace().equals(ns)) {
                dataStoresNs.add(dsc);
            }
        }

        return dataStoresNs;
    }*/

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
    public FeatureTypeInfo getFeatureTypeInfo(String typeName)
        throws NoSuchElementException {
        int prefixDelimPos = typeName.lastIndexOf(NameSpace.PREFIX_DELIMITER);

        if (prefixDelimPos < 0) {
            //for backwards compatibility.  Only works if all
            //featureTypes have the same prefix.
            typeName = getDefaultNameSpace().getPrefix()
                + NameSpace.PREFIX_DELIMITER + typeName;
        }

        LOGGER.finest("getting type " + typeName);

        FeatureTypeInfo ftype = (FeatureTypeInfo) featureTypes.get(typeName);

        if (ftype == null) {
            throw new NoSuchElementException("there is no FeatureTypeConfig named "
                + typeName + " configured in this server");
        }

        return ftype;
    }

    /**
     * Gets a FeatureTypeInfo from a local type name (ie unprefixed), and a
     * uri.  This method is slow, use getFeatureType(String typeName), where
     * possible.  For not he only user should be TransactionFeatureHandler.
     *
     * @param localName DOCUMENT ME!
     * @param uri DOCUMENT ME!
     *
     * @return DOCUMENT ME!
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
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Map getFeatureTypeInfos() {
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
        NodeList nsList = nsRoot.getElementsByTagName("NameSpace");
        Element elem;
        String uri;
        String prefix;
        boolean defaultNS;
        int nsCount = nsList.getLength();
        NameSpaces = new HashMap(nsCount);

        for (int i = 0; i < nsCount; i++) {
            elem = (Element) nsList.item(i);
            uri = getAttribute(elem, "uri", true);
            prefix = getAttribute(elem, "prefix", true);
            defaultNS = getBooleanAttribute(elem, "default", false);
            defaultNS = (defaultNS || (nsCount == 1));

            NameSpace ns = new NameSpace(prefix, uri, defaultNS);
            LOGGER.config("added NameSpace " + ns);
            NameSpaces.put(prefix, ns);

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
        DataStoreInfo dsConfig;
        Element dsElem;

        for (int i = 0; i < dsCnt; i++) {
            dsElem = (Element) dsElements.item(i);
            dsConfig = new DataStoreInfo(dsElem, this);

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

        //HACK: but I'm not sure if we can get the GeoServer instance.  This is one thing
        //that will benefit from splitting up of config loading from representation.
        url = new File(base + fileName).toURL();

        SLDStyle stylereader = new SLDStyle(styleFactory, url);
        Style[] layerstyle = stylereader.readXML();

        return layerstyle[0];
    }
	public Style loadStyle(File fileName)
		throws IOException {
		URL url;

		//HACK: but I'm not sure if we can get the GeoServer instance.  This is one thing
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
            Element featureElem = GeoServer.loadConfig(currentFile.toString());
            FeatureTypeInfo ft = null;

            try {
                File parentDir = currentFile.getParentFile();
                ft = new FeatureTypeInfo(this, featureElem);

                String pathToSchemaFile = new File(parentDir, "schema.xml")
                    .toString();
                LOGGER.finest("pathToSchema is " + pathToSchemaFile);
                ft.setSchemaFile(pathToSchemaFile);
                featureTypes.put(ft.getName(), ft);
                LOGGER.finer("added featureType " + ft.getName());
            } catch (ConfigurationException cfge) {
                //HACK: should use a logger.
                cfge.printStackTrace(System.out);
                LOGGER.warning("could not add FeatureTypeInfo at " + currentFile
                    + " due to " + cfge);
            }
        }
    }*/

    /*  private void loadType(String filePath) throws ConfigurationException {
       try {
           Element featureElem = GeoServer.loadConfig(configFile);
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
       FeatureTypeInfo ft = null;
       for (int i = 0; i < ftCount; i++) {
           ft = new FeatureTypeInfo(this, (Element) ftlist.item(i));
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
}
