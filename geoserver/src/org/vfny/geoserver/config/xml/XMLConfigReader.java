/*
 *    Geotools2 - OpenSource mapping toolkit
 *    http://geotools.org
 *    (C) 2003, Geotools Project Managment Committee (PMC)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 *
 */
package org.vfny.geoserver.config.xml;

import org.vfny.geoserver.config.*;
import org.vfny.geoserver.config.data.*;
import org.vfny.geoserver.config.wfs.*;
import org.vfny.geoserver.config.wms.*;

import java.io.*;
import java.util.*;
import java.util.logging.*;
import org.w3c.dom.*;
import org.xml.sax.*;
import javax.xml.parsers.*;
import java.nio.charset.*;
import org.geotools.filter.*;
import com.vividsolutions.jts.geom.*;
import org.vfny.geoserver.global.Log4JFormatter;
/**
 * XMLConfigReader purpose.
 * <p>
 * Description of XMLConfigReader 
 * Static class to load a configuration org.vfny.geoserver.config.
 * <p>
 * Example Use:
 * <pre><code>
 * ModelConfig m = XMLConfigReader.load(new File("/conf/"));
 * </code></pre>
 * 
 * @author dzwiers, Refractions Research, Inc.
 * @version $Id: XMLConfigReader.java,v 1.1.2.3 2003/12/31 23:35:18 dmzwiers Exp $
 */
public class XMLConfigReader {
	/**
	 * Used internally to create log information to detect errors.
	 */
	private static final Logger LOGGER = Logger.getLogger(
			"org.vfny.geoserver.config");
			
	/**
	 * The main data structure to contain the results. 
	 */
	private ModelConfig model = null;
	
	/**
	 * The root directory from which the configuration is loaded.
	 */
	private File root;
	
	/**
	 * Is set to true after the model is loaded into memory. 
	 */
	private boolean initialized = false;
	
	/**
	 * XMLConfigReader constructor.
	 * <p>
	 * Should never be called.
	 * </p>
	 *
	 */
	protected XMLConfigReader(){
		model = new ModelConfig();
		root = new File(".");
	}
	
	/**
	 * 
	 * <p>This method loads the config files from the 
	 * specified directory into a ModelConfig. If the path is incorrect, 
	 * or the directory is formed correctly, a ConfigException 
	 * will be thrown and/or null returned. <br><br>
	 * The config directory is as follows:<br>
	 * <ul><li>./WEB-INF/catalog.org.vfny.geoserver.config.org.vfny.geoserver.config.xml</li>
	 * <li>./WEB-INF/services.org.vfny.geoserver.config.org.vfny.geoserver.config.xml</li>
	 * <li>./data/featuretypes/ * /info.org.vfny.geoserver.config.org.vfny.geoserver.config.xml</li>
	 * <li>./data/featuretypes/ * /schema.org.vfny.geoserver.config.org.vfny.geoserver.config.xml</li>
	 * </ul>
	 * </p>
	 * @param root A directory which contains the config files.  
	 * @throws ConfigException When an error occurs. 
	 * @return The resulting ModelConfig
	 */
	public XMLConfigReader(File root) throws ConfigException{
		this.root = root;
		model = new ModelConfig();
		load();
		initialized = true;
	}
	
	public boolean isInitialized(){
		return initialized;
	}
	
	/**
	 * getModel purpose.
	 * <p>
	 * User'd responsibility to ensure a valid org.vfny.geoserver.config has been parsed. 
	 * Used to get the results of a configuration parse.
	 * </p>
	 * @return The results of a configuration parse residing in memory.
	 */
	public ModelConfig getModel(){
		return model;
	}
	
	/**
	 * load purpose.
	 * <p>
	 * Main load routine, sets up file handles for various other portions of the load procedure.
	 * </p>
	 * @throws ConfigException
	 */
	protected void load() throws ConfigException{	
		root = ReaderUtils.initFile(root,true);
		File configDir = ReaderUtils.initFile(new File(root,"WEB-INF/"),true);
		File configFile = ReaderUtils.initFile(new File(configDir,"services.xml"),false);
		
		loadServices(configFile);

		File catalogFile = ReaderUtils.initFile(new File(configDir,"catalog.xml"),false);
		File dataDir = ReaderUtils.initFile(new File(root,"data/"),true);
		File featureTypeDir = ReaderUtils.initFile(new File(dataDir,"featureTypes/"),true);
		loadCatalog(catalogFile,featureTypeDir);
		
		
		// Future additions
		// validationDir = ReaderUtils.initFile(new File(dataDir,"validation/"),true);
		// loadValidation(validationDir);	
	}
	
	/**
	 * loadServices purpose.
	 * <p>
	 * loads services.xml into memory with the assistance of other class methods.
	 * </p>
	 * @param configFile services.xml
	 * @throws ConfigException When an error occurs.
	 */
	protected void loadServices(File configFile) throws ConfigException{
		LOGGER.fine("loading config file: " + configFile);
		Element configElem = ReaderUtils.loadConfig(configFile);

		LOGGER.fine("parsing configuration documents");
		Element elem = (Element) configElem.getElementsByTagName("global").item(0);
		GlobalConfig g = loadGlobal(elem);
		model.setGlobal(g);

		NodeList configuredServices = configElem.getElementsByTagName("service");
		int nServices = configuredServices.getLength();

		for (int i = 0; i < nServices; i++) {
			elem = (Element) configuredServices.item(i);

			String serviceType = elem.getAttribute("type");

			if ("WFSConfig".equalsIgnoreCase(serviceType)) {
				model.setWfs(loadWFS(elem,model.getGlobal()));
			} else if ("WMSConfig".equalsIgnoreCase(serviceType)) {
				model.setWms(loadWMS(elem,model.getGlobal()));
			} else if ("Z39.50".equalsIgnoreCase(serviceType)) {
				//...
			} else {
				throw new ConfigException("Unknown service type: "+ serviceType);
			}
		}
	}
	
	/**
	 * loadCatalog purpose.
	 * <p>
	 * loads catalog.xml into memory with the assistance of other class methods.
	 * </p>
	 * @param catalogFile catalog.xml
	 * @param featureTypeDir the directory containing the info.xml files for the featuretypes.
	 * @throws ConfigException When an error occurs.
	 */
	protected void loadCatalog(File catalogFile, File featureTypeDir) throws ConfigException {
		LOGGER.fine("loading catalog file: " + catalogFile);
		Element catalogElem = ReaderUtils.loadConfig(catalogFile);
		
		LOGGER.info("loading catalog configuration");
		CatalogConfig c = new CatalogConfig();
		c.setNameSpaces(loadNameSpaces(ReaderUtils.getChildElement(catalogElem, "namespaces", true)));
		setDefaultNS(c);
		c.setDataStores(loadDataStores(ReaderUtils.getChildElement(catalogElem, "datastores", true),c.getNameSpaces()));
		c.setStyles(loadStyles(ReaderUtils.getChildElement(catalogElem, "styles", false)));
		c.setFeaturesTypes(loadFeatureTypes(featureTypeDir));

		model.setCatalog(c);
	}
	
	/**
	 * setDefaultNS purpose.
	 * <p>
	 * Finds and sets the default namespace. The namespaces in catalog must already be loaded.
	 * </p>
	 * @param c The catalog into which we will store the default namespace.
	 */
	protected void setDefaultNS(CatalogConfig c){
		Iterator i = c.getNameSpaces().values().iterator();
		while(i.hasNext()){
			org.vfny.geoserver.config.data.NameSpaceConfig ns = (org.vfny.geoserver.config.data.NameSpaceConfig)i.next();
			if(ns.isDefault()){
				c.setDefaultNameSpace(ns);
				return;
			}
		}
	}
	
	/**
	 * getLoggingLevel purpose.
	 * <p>
	 * Parses the LoggingLevel from a DOM tree and converts the level into a Level Object.
	 * </p>
	 * @param globalConfigElem
	 * @return The logging Level
	 * @throws ConfigException When an error occurs.
	 */
	protected Level getLoggingLevel(Element globalConfigElem) throws ConfigException{
		Level level = Logger.getLogger("org.vfny.geoserver").getLevel();
		Element levelElem = ReaderUtils.getChildElement(globalConfigElem, "loggingLevel");

		if (levelElem != null) {
			String levelName = levelElem.getFirstChild().getNodeValue();

			try {
				level = Level.parse(levelName);
			} catch (IllegalArgumentException ex) {
				LOGGER.warning("illegal loggingLevel name: " + levelName);
			}
		} else {
			LOGGER.config("No loggingLevel found, using default "
				+ "logging.properties setting");
		}

		return level;
	}
	
	/**
	 * loadGlobal purpose.
	 * <p>
	 * Converts a DOM tree into a GlobalConfig configuration.
	 * </p>
	 * @param globalElem A DOM tree representing a complete global configuration.
	 * @return A complete GlobalConfig object loaded from the DOM tree provided.
	 * @throws ConfigException When an error occurs.
	 */
	protected GlobalConfig loadGlobal(Element globalElem) throws ConfigException{
		GlobalConfig g = new GlobalConfig();
		LOGGER.fine("parsing global configuration parameters");
		
		Level loggingLevel = getLoggingLevel(globalElem);

		//init this now so the rest of the config has correct log levels.
		Log4JFormatter.init("org.geotools", loggingLevel);
		Log4JFormatter.init("org.vfny.geoserver", loggingLevel);
		LOGGER.config("logging level is " + loggingLevel);
		g.setLoggingLevel(loggingLevel);

		Element elem = null;
		elem = ReaderUtils.getChildElement(globalElem, "ContactInformation");
		g.setContact(loadContact(elem));

		elem = ReaderUtils.getChildElement(globalElem, "verbose", false);

		if (elem != null) {
			g.setVerbose(ReaderUtils.getBooleanAttribute(elem, "value", false));
		}

		elem = ReaderUtils.getChildElement(globalElem, "maxFeatures");

		if (elem != null) {
			//if the element is pressent, it's "value" attribute is mandatory
			g.setMaxFeatures(ReaderUtils.getIntAttribute(elem, "value", true, g.getMaxFeatures()));
		}

		LOGGER.config("maxFeatures is " + g.getMaxFeatures());
		elem = ReaderUtils.getChildElement(globalElem, "numDecimals");

		if (elem != null) {
			g.setNumDecimals(ReaderUtils.getIntAttribute(elem, "value", true, g.getNumDecimals()));
		}

		LOGGER.config("numDecimals returning is " + g.getNumDecimals());
		elem = ReaderUtils.getChildElement(globalElem, "charSet");

		if (elem != null) {
			String chSet = ReaderUtils.getAttribute(elem, "value", true);

			try {
				Charset cs = Charset.forName(chSet);
				g.setCharSet(cs);
				LOGGER.finer("charSet: " + cs.displayName());
			} catch (Exception ex) {
				LOGGER.info(ex.getMessage());
			}
		}

		LOGGER.config("charSet is " + g.getCharSet());
		g.setBaseUrl(ReaderUtils.getChildText(globalElem, "URL", true));

		String schemaBaseUrl = ReaderUtils.getChildText(globalElem, "SchemaBaseUrl");
		
		if (schemaBaseUrl != null) {
			g.setSchemaBaseUrl(schemaBaseUrl);
		} else {
			g.setSchemaBaseUrl(root.toString() + "/data/capabilities/");
		}
		return g;
	}
	
	/**
	 * loadContact purpose.
	 * <p>
	 * Converts a DOM tree into a ContactConfig
	 * </p>
	 * @param contactInfoElement a DOM tree to convert into a ContactConfig.
	 * @return The resulting ContactConfig object from the DOM tree.
	 * @throws ConfigException When an error occurs.
	 */
	protected ContactConfig loadContact(Element contactInfoElement) throws ConfigException{
		ContactConfig c = new ContactConfig();
		if (contactInfoElement == null) {
			return c;
		}

		Element elem;
		NodeList nodeList;
		elem = ReaderUtils.getChildElement(contactInfoElement, "ContactPersonPrimary");

		if (elem != null) {
			c.setContactPerson(ReaderUtils.getChildText(elem, "ContactPerson"));
			c.setContactOrganization(ReaderUtils.getChildText(elem, "ContactOrganization"));
		}

		c.setContactPosition(ReaderUtils.getChildText(contactInfoElement,"ContactPosition"));
		elem = ReaderUtils.getChildElement(contactInfoElement, "ContactAddress");
		
		if (elem != null) {
			c.setAddressType(ReaderUtils.getChildText(elem, "AddressType"));
			c.setAddress(ReaderUtils.getChildText(elem, "Address"));
			c.setAddressCity(ReaderUtils.getChildText(elem, "City"));
			c.setAddressState(ReaderUtils.getChildText(elem, "StateOrProvince"));
			c.setAddressPostalCode(ReaderUtils.getChildText(elem, "PostCode"));
			c.setAddressCountry(ReaderUtils.getChildText(elem, "Country"));
		}

		c.setContactVoice(ReaderUtils.getChildText(contactInfoElement,"ContactVoiceTelephone"));
		c.setContactFacsimile(ReaderUtils.getChildText(contactInfoElement,"ContactFacsimileTelephone"));
		c.setContactEmail(ReaderUtils.getChildText(contactInfoElement,"ContactElectronicMailAddress"));
		
		return c;
	}
	
	/**
	 * loadWFS purpose.
	 * <p>
	 * Converts a DOM tree into a WFSConfig object.
	 * </p>
	 * @param wfsElement a DOM tree to convert into a WFSConfig object.
	 * @param g A reference to the already loaded GlobalConfig object. Used to get the baseUrl
	 * @return A complete WFSConfig object loaded from the DOM tree provided.
	 * @see GlobalConfig#getBaseUrl()
	 * @throws ConfigException When an error occurs.
	 */
	protected WFSConfig loadWFS(Element wfsElement, GlobalConfig g) throws ConfigException{
		WFSConfig w = new WFSConfig();
		w.setService(loadService(wfsElement));
		w.setDescribeUrl(g.getBaseUrl().toString() + "wfs/");
		return w;
	}

	/**
	 * loadWMS purpose.
	 * <p>
	 * Converts a DOM tree into a WMSConfig object.
	 * </p>
	 * @param wmsElement a DOM tree to convert into a WMSConfig object.
	 * @param g A reference to the already loaded GlobalConfig object. Used to get the baseUrl
	 * @return A complete WMSConfig object loaded from the DOM tree provided.
	 * @see GlobalConfig#getBaseUrl()
	 * @throws ConfigException When an error occurs.
	 */
	protected WMSConfig loadWMS(Element wmsElement, GlobalConfig g) throws ConfigException{
		WMSConfig w = new WMSConfig();
		w.setService(loadService(wmsElement));
		w.setDescribeUrl(g.getBaseUrl().toString() + "wms/");
		return w;
	}

	/**
	 * loadService purpose.
	 * <p>
	 * Converts a DOM tree into a ServiceConfig object.
	 * </p>
	 * @param serviceRoot a DOM tree to convert into a ServiceConfig object.
	 * @return A complete ServiceConfig object loaded from the DOM tree provided.
	 * @throws ConfigException When an error occurs.
	 */
	protected ServiceConfig loadService(Element serviceRoot) throws ConfigException{
		ServiceConfig s = new ServiceConfig();

		s.setName(ReaderUtils.getChildText(serviceRoot, "name", true));
		s.setTitle(ReaderUtils.getChildText(serviceRoot, "title", true));
		s.setAbstract(ReaderUtils.getChildText(serviceRoot, "abstract"));
		s.setKeywords(ReaderUtils.getKeyWords(ReaderUtils.getChildElement(serviceRoot, "keywords")));
		s.setFees(ReaderUtils.getChildText(serviceRoot, "fees"));
		s.setAccessConstraints(ReaderUtils.getChildText(serviceRoot, "accessConstraints"));
		s.setMaintainer(ReaderUtils.getChildText(serviceRoot, "maintainer"));
		s.setEnabled(ReaderUtils.getBooleanAttribute(serviceRoot, "enabled", true));
		s.setOnlineResource(ReaderUtils.getChildText(serviceRoot, "onlineResource", true));
		
		return s;
	}

	/**
	 * loadNameSpaces purpose.
	 * <p>
	 * Converts a DOM tree into a Map of NameSpaces.
	 * </p>
	 * @param nsRoot a DOM tree to convert into a Map of NameSpaces.
	 * @return A complete Map of NameSpaces loaded from the DOM tree provided.
	 * @throws ConfigException When an error occurs.
	 */
	protected Map loadNameSpaces(Element nsRoot) throws ConfigException{

		NodeList nsList = nsRoot.getElementsByTagName("namespace");
		Element elem;
		int nsCount = nsList.getLength();
		Map nameSpaces = new HashMap(nsCount);

		for (int i = 0; i < nsCount; i++) {
			elem = (Element) nsList.item(i);
			org.vfny.geoserver.config.data.NameSpaceConfig ns = new org.vfny.geoserver.config.data.NameSpaceConfig();
			ns.setUri(ReaderUtils.getAttribute(elem, "uri", true));
			ns.setPrefix(ReaderUtils.getAttribute(elem, "prefix", true));
			ns.setDefault(ReaderUtils.getBooleanAttribute(elem, "default", false) || (nsCount == 1));
			LOGGER.config("added namespace " + ns);
			nameSpaces.put(ns.getPrefix(), ns);
		}
		return nameSpaces;
	}

	/**
	 * loadStyles purpose.
	 * <p>
	 * Converts a DOM tree into a Map of Styles.
	 * </p>
	 * @param stylesElem a DOM tree to convert into a Map of Styles.
	 * @return A complete Map of Styles loaded from the DOM tree provided.
	 * @throws ConfigException When an error occurs.
	 */
	protected Map loadStyles(Element stylesElem) throws ConfigException{
		Map styles = new HashMap();

		NodeList stylesList = null;

		if (stylesElem != null) {
			stylesList = stylesElem.getElementsByTagName("style");
		}

		if ((stylesList == null) || (stylesList.getLength() == 0)) {
			//no styles where defined, just add a default one
			styles.put("normal", "styles/normal.sld");
		}

		int styleCount = stylesList.getLength();
		Element styleElem;
		for (int i = 0; i < styleCount; i++) {
			styleElem = (Element) stylesList.item(i);
			StyleConfig s = new StyleConfig();
			s.setId(ReaderUtils.getAttribute(styleElem, "id", true));
			s.setFilename(new File(ReaderUtils.getAttribute(styleElem, "filename", true)));
			s.setDefault(ReaderUtils.getBooleanAttribute(styleElem, "default", false));
			styles.put(s.getId(), s);
		}
		return styles;
	}

	/**
	 * loadDataStores purpose.
	 * <p>
	 * Converts a DOM tree into a Map of DataStores.
	 * </p>
	 * @param stylesElem a DOM tree to convert into a Map of DataStores.
	 * @return A complete Map of DataStores loaded from the DOM tree provided.
	 * @throws ConfigException When an error occurs.
	 */
	protected Map loadDataStores(Element dsRoot, Map nameSpaces) throws ConfigException {
		Map dataStores = new HashMap();

		NodeList dsElements = dsRoot.getElementsByTagName("datastore");
		int dsCnt = dsElements.getLength();
		DataStoreConfig dsConfig;
		Element dsElem;

		for (int i = 0; i < dsCnt; i++) {
			dsElem = (Element) dsElements.item(i);
			dsConfig = loadDataStore(dsElem,nameSpaces);

			if (dataStores.containsKey(dsConfig.getId())) {
				throw new ConfigException("duplicated datastore id: "
					+ nameSpaces.get(dsConfig.getNameSpaceId()));
			}

			dataStores.put(dsConfig.getId(), dsConfig);
		}
		return dataStores;
	}

	/**
	 * loadDataStore purpose.
	 * <p>
	 * Converts a DOM tree into a DataStoreConfig object.
	 * </p>
	 * @param dsElem a DOM tree to convert into a DataStoreConfig object.
	 * @param nameSpaces the map of pre-loaded namespaces to check for inconsistencies.
	 * @return A complete DataStoreConfig object loaded from the DOM tree provided.
	 * @throws ConfigException When an error occurs.
	 */
	protected DataStoreConfig loadDataStore(Element dsElem, Map nameSpaces) throws ConfigException {
		DataStoreConfig ds = new DataStoreConfig();
		
		LOGGER.finer("creating a new DataStoreConfig configuration");
		ds.setId(ReaderUtils.getAttribute(dsElem, "id", true));

		String namespacePrefix = ReaderUtils.getAttribute(dsElem, "namespace", true);
		if(nameSpaces.containsKey(namespacePrefix)){
			ds.setNameSpaceId(namespacePrefix);
		}else{
			String msg = "there is no namespace defined for datatasore '"
				+ namespacePrefix + "'";
			throw new ConfigException(msg);
		}

		ds.setEnabled(ReaderUtils.getBooleanAttribute(dsElem, "enabled", false));
		ds.setTitle(ReaderUtils.getChildText(dsElem, "title", false));
		ds.setAbstract(ReaderUtils.getChildText(dsElem, "abstract", false));
		LOGGER.fine("loading connection parameters for DataStoreConfig " + ds.getNameSpaceId());
		ds.setConnectionParams(loadConnectionParams(ReaderUtils.getChildElement(dsElem, "connectionParams", true)));
		LOGGER.info("created " + toString());
		return ds;
	}

	/**
	 * loadConnectionParams purpose.
	 * <p>
	 * Converts a DOM tree into a Map of Strings which represent connection parameters.
	 * </p>
	 * @param stylesElem a DOM tree to convert into a Map of Strings which represent connection parameters.
	 * @return A complete Map of Strings which represent connection parameters loaded from the DOM tree provided.
	 * @throws ConfigException When an error occurs.
	 */
	protected Map loadConnectionParams(Element connElem) throws ConfigException {
		Map connectionParams = new HashMap();

		NodeList paramElems = connElem.getElementsByTagName("parameter");
		int pCount = paramElems.getLength();
		Element param;
		String paramKey;
		String paramValue;

		for (int i = 0; i < pCount; i++) {
			param = (Element) paramElems.item(i);
			paramKey = ReaderUtils.getAttribute(param, "name", true);
			paramValue = ReaderUtils.getAttribute(param, "value", true);
			connectionParams.put(paramKey, paramValue);
			LOGGER.finer("added parameter " + paramKey + ": '" + paramValue	+ "'");
		}
		return connectionParams;
	}

	/**
	 * loadFeatureTypes purpose.
	 * <p>
	 * Converts a DOM tree into a Map of FeatureTypes.
	 * </p>
	 * @param featureTypeDir a DOM tree to convert into a Map of FeatureTypes.
	 * @return A complete Map of FeatureTypes loaded from the DOM tree provided.
	 * @throws ConfigException When an error occurs.
	 */
	protected Map loadFeatureTypes(File featureTypeDir) throws ConfigException {
		LOGGER.finest("examining: " + featureTypeDir.getAbsolutePath());
		LOGGER.finest("is dir: " + featureTypeDir.isDirectory());
		Map featureTypes = new HashMap();

		if (featureTypeDir.isDirectory()) {
			File[] file = featureTypeDir.listFiles();
			for (int i = 0, n = file.length; i < n; i++) {
				LOGGER.fine("Info dir:"+file[i].toString());
				FeatureTypeConfig ft = loadFeature(new File(file[i],"info.org.vfny.geoserver.config.org.vfny.geoserver.config.xml"));
				featureTypes.put(ft.getName(), ft);
			}
		}
		return featureTypes;
	}

	/**
	 * loadDataStore purpose.
	 * <p>
	 * Converts a intoFile tree into a FeatureTypeConfig object. Uses loadFeaturePt2(Element) to interpret the XML.
	 * </p>
	 * @param infoFile a File to convert into a FeatureTypeConfig object. (info.xml)
	 * @return A complete FeatureTypeConfig object loaded from the File handle provided.
	 * @throws ConfigException When an error occurs.
	 * @see loadFeaturePt2(Element)
	 */
	protected FeatureTypeConfig loadFeature(File infoFile) throws ConfigException{
		if (isInfoFile(infoFile)) {
			Element featureElem = ReaderUtils.loadConfig(infoFile);
			FeatureTypeConfig ft = null;

			File parentDir = infoFile.getParentFile();
			ft = loadFeaturePt2(featureElem);
			ft.setDirName(parentDir.getName());

			File pathToSchemaFile = new File(parentDir, "schema.org.vfny.geoserver.config.org.vfny.geoserver.config.xml");
			LOGGER.finest("pathToSchema is " + pathToSchemaFile);
			ft.setSchema(loadSchema(pathToSchemaFile));
			LOGGER.finer("added featureType " + ft.getName());
			
			return ft;
		}
		throw new ConfigException("Invalid Info file.");
	}

	/**
	 * loadFeaturePt2 purpose.
	 * <p>
	 * Converts a DOM tree into a FeatureTypeConfig object.
	 * </p>
	 * @param fTypeRoot a DOM tree to convert into a FeatureTypeConfig object.
	 * @return A complete FeatureTypeConfig object loaded from the DOM tree provided.
	 * @throws ConfigException When an error occurs.
	 */
	protected FeatureTypeConfig loadFeaturePt2(Element fTypeRoot) throws ConfigException{
		FeatureTypeConfig ft = new FeatureTypeConfig();

		ft.setName(ReaderUtils.getChildText(fTypeRoot, "name", true));
		ft.setTitle(ReaderUtils.getChildText(fTypeRoot, "title", true));
		ft.setAbstract(ReaderUtils.getChildText(fTypeRoot, "abstract"));
		ft.setKeywords(getKeyWords(ReaderUtils.getChildElement(fTypeRoot, "keywords")));
		ft.setDataStoreId(ReaderUtils.getAttribute(fTypeRoot, "datastore", true));
		ft.setSRS(Integer.parseInt(ReaderUtils.getChildText(fTypeRoot, "SRS", true)));
		ft.setDefaultStyle(ReaderUtils.getAttribute(ReaderUtils.getChildElement(fTypeRoot,"styles"),"default",false));
		ft.setLatLongBBox(loadLatLongBBox(ReaderUtils.getChildElement(fTypeRoot, "latLonBoundingBox")));

		Element numDecimalsElem = ReaderUtils.getChildElement(fTypeRoot, "numDecimals",	false);

		if (numDecimalsElem != null) {
			ft.setNumDecimals(ReaderUtils.getIntAttribute(numDecimalsElem, "value", false, 8));
		}

		ft.setDefinitionQuery(loadDefinitionQuery(fTypeRoot));
		return ft;
	}

	/**
	 * getKeyWords purpose.
	 * <p>
	 * Converts a DOM tree into a List of Strings representing keywords.
	 * </p>
	 * @param keywordsElem a DOM tree to convert into a List of Strings representing keywords.
	 * @return A complete List of Strings representing keywords loaded from the DOM tree provided.
	 * @throws ConfigException When an error occurs.
	 */
	protected List getKeyWords(Element keywordsElem) {
		NodeList klist = keywordsElem.getElementsByTagName("keyword");
		int kCount = klist.getLength();
		List keywords = new ArrayList(kCount);
		String kword;
		Element kelem;

		for (int i = 0; i < kCount; i++) {
			kelem = (Element) klist.item(i);
			kword = ReaderUtils.getElementText(kelem);
			if (kword != null) {
				keywords.add(kword);
			}
		}

		return keywords;
	}

	/**
	 * loadLatLongBBox purpose.
	 * <p>
	 * Converts a DOM tree into a Envelope object.
	 * </p>
	 * @param bboxElem a DOM tree to convert into a Envelope object.
	 * @return A complete Envelope object loaded from the DOM tree provided.
	 * @throws ConfigException When an error occurs.
	 */
	protected Envelope loadLatLongBBox(Element bboxElem) throws ConfigException {
		boolean dynamic = ReaderUtils.getBooleanAttribute(bboxElem, "dynamic", false);

		if (!dynamic) {
			double minx = ReaderUtils.getDoubleAttribute(bboxElem, "minx", true);
			double miny = ReaderUtils.getDoubleAttribute(bboxElem, "minx", true);
			double maxx = ReaderUtils.getDoubleAttribute(bboxElem, "minx", true);
			double maxy = ReaderUtils.getDoubleAttribute(bboxElem, "minx", true);
			return new Envelope(minx, miny, maxx, maxy);
		}
		return new Envelope();
	}

	/**
	 * loadDefinitionQuery purpose.
	 * <p>
	 * Converts a DOM tree into a Filter object.
	 * </p>
	 * @param typeRoot a DOM tree to convert into a Filter object.
	 * @return A complete Filter object loaded from the DOM tree provided.
	 * @throws ConfigException When an error occurs.
	 */
	protected org.geotools.filter.Filter loadDefinitionQuery(Element typeRoot) throws ConfigException {
		Element defQNode = ReaderUtils.getChildElement(typeRoot, "definitionQuery", false);
		org.geotools.filter.Filter filter = null;

		if (defQNode != null) {
			LOGGER.fine("definitionQuery element found, looking for Filter");

			Element filterNode = ReaderUtils.getChildElement(defQNode, "Filter", false);

			if ((filterNode != null)
					&& ((filterNode = ReaderUtils.getFirstChildElement(filterNode)) != null)) {
						filter = FilterDOMParser.parseFilter(filterNode);

				return filter;
			}

			LOGGER.fine("No Filter definition query found");
		}
		return filter;
	}
	
	/**
	 * isInfoFile purpose.
	 * <p>
	 * Used to perform safety checks on info.xml file handles.
	 * </p>
	 * @param testFile The file to test.
	 * @return true if the file is an info.xml file.
	 */
	protected static boolean isInfoFile(File testFile) {
		String testName = testFile.getAbsolutePath();
		int start = testName.length() - "info.org.vfny.geoserver.config.org.vfny.geoserver.config.xml".length();
		int end = testName.length();

		return testName.substring(start, end).equals("info.org.vfny.geoserver.config.org.vfny.geoserver.config.xml");
	}

	/**
	 * loadSchema purpose.
	 * <p>
	 * Parses a schema file into a singular string.
	 * @TODO parse the schema file into a data structure.
	 * </p>
	 * @param path a schema.xml file to be read into a String
	 * @return A string representation of the file contents.
	 * @throws ConfigException When an error occurs.
	 */
	protected String loadSchema(File path) throws ConfigException{
		path = ReaderUtils.initFile(path, false);
		StringBuffer sb = new StringBuffer();
		try{
			FileReader fr = new FileReader(path);
			BufferedReader br = new BufferedReader(fr);
			while(br.ready()){
				sb.append(br.readLine()+"\n");
			}
		}catch(IOException e){
			throw new ConfigException(e);
		}
		return sb.toString();
	}
	
}

/**
 * ReaderUtils purpose.
 * <p>
 * This class is intended to be used as a library of XML relevant operation for the XMLConfigReader class.
 * <p>
 * @see XMLConfigReader
 * @author dzwiers, Refractions Research, Inc.
 * @version $Id: XMLConfigReader.java,v 1.1.2.3 2003/12/31 23:35:18 dmzwiers Exp $
 */
class ReaderUtils{
	/**
	 * Used internally to create log information to detect errors.
	 */
	private static final Logger LOGGER = Logger.getLogger(
			"org.vfny.geoserver.config");
	
	/**
	 * ReaderUtils constructor.
	 * <p>
	 * Static class, this should never be called.
	 * </p>
	 *
	 */
	private ReaderUtils(){}
	
	/**
	 * loadConfig purpose.
	 * <p>
	 * Parses the specified file into a DOM tree.
	 * </p>
	 * @param configFile The file to parse int a DOM tree.
	 * @return the resulting DOM tree
	 * @throws ConfigException
	 */
	public static Element loadConfig(File configFile)
		throws ConfigException {
		try {
			LOGGER.fine("loading configuration file " + configFile);

			FileReader fis = new FileReader(configFile);
			InputSource in = new InputSource(fis);
			DocumentBuilderFactory dfactory = DocumentBuilderFactory
				.newInstance();
			//dfactory.setNamespaceAware(true);
			/*set as optimizations and hacks for geoserver schema config files
			 * @HACK should make documents ALL namespace friendly, and validated. Some documents are XML fragments.
			 * @TODO change the following config for the parser and modify config files to avoid XML fragmentation.
			 */
			dfactory.setNamespaceAware(false);
			dfactory.setValidating(false);
			dfactory.setIgnoringComments(true);
			dfactory.setCoalescing(true);
			dfactory.setIgnoringElementContentWhitespace(true);

			Document serviceDoc = dfactory.newDocumentBuilder().parse(in);
			Element configElem = serviceDoc.getDocumentElement();

			return configElem;
		} catch (IOException ioe) {
			String message = "problem reading file " + configFile + "due to: "
				+ ioe.getMessage();
			LOGGER.warning(message);
			throw new ConfigException(message, ioe);
		} catch (ParserConfigurationException pce) {
			String message = "trouble with parser to read org.vfny.geoserver.config.org.vfny.geoserver.config.xml, make sure class"
				+ "path is correct, reading file " + configFile;
			LOGGER.warning(message);
			throw new ConfigException(message, pce);
		} catch (SAXException saxe) {
			String message = "trouble parsing XML in " + configFile + ": "
				+ saxe.getMessage();
			LOGGER.warning(message);
			throw new ConfigException(message, saxe);
		}
	}
	
	/**
	 * initFile purpose.
	 * <p>
	 * Checks to ensure the file is valid. Returns the file passed in to allow this to wrap file creations. 
	 * </p>
	 * @param f A file Handle to test.
	 * @param isDir true when the File passed in is expected to be a directory, false when the handle is expected to be a file.
	 * @return the File handle passed in
	 * @throws ConfigException When the file does not exist or is not the type specified.
	 */
	public static File initFile(File f, boolean isDir) throws ConfigException{
		if(!f.exists()){
			throw new ConfigException("Path specified does not have a valid file.\n"+f+"\n\n");
		}
		if(isDir && !f.isDirectory()){
			throw new ConfigException("Path specified does not have a valid file.\n"+f+"\n\n");
		}
		if(!isDir && !f.isFile()){
			throw new ConfigException("Path specified does not have a valid file.\n"+f+"\n\n");
		}
		LOGGER.fine("File is valid: " + f);
		return f;
	}
	
	/**
	 * getChildElement purpose.
	 * <p>
	 * Used to help with XML manipulations. Returns the first child element of the specified name. 
	 * An exception occurs when the node is required and not found.
	 * </p>
	 * @param root The root element to look for children in.
	 * @param name The name of the child element to look for.
	 * @param mandatory true when an exception should be thrown if the child element does not exist.
	 * @return The child element found, null if not found.
	 * @throws ConfigException When a child element is required and not found.
	 */
	public static Element getChildElement(Element root, String name, boolean mandatory) throws ConfigException {
		Node child = root.getFirstChild();

		while (child != null) {
			if (child.getNodeType() == Node.ELEMENT_NODE) {
				if (name.equals(child.getNodeName())) {
					return (Element) child;
				}
			}

			child = child.getNextSibling();
		}

		if (mandatory && (child == null)) {
			throw new ConfigException(root.getNodeName()
				+ " does not contains a child element named " + name);
		}

		return null;
	}

	/**
	 * getChildElement purpose.
	 * <p>
	 * Used to help with XML manipulations. Returns the first child element of the specified name. 
	 * </p>
	 * @param root The root element to look for children in.
	 * @param name The name of the child element to look for.
	 * @return The child element found, null if not found.
	 * @see getChildElement(Element,String,boolean)
	 */
	public static Element getChildElement(Element root, String name){
		try{
			return getChildElement(root, name, false);
		}catch(ConfigException e){
			//will never be here.
			return null;
		}
	}

	/**
	 * getIntAttribute purpose.
	 * <p>
	 * Used to help with XML manipulations. Returns the first child integer attribute of the specified name. 
	 * An exception occurs when the node is required and not found.
	 * </p>
	 * @param root The root element to look for children in.
	 * @param attName The name of the attribute to look for.
	 * @param mandatory true when an exception should be thrown if the attribute element does not exist.
	 * @param defaultValue a default value to return incase the attribute was not found. mutually exclusive with the ConfigException thrown.
	 * @return The int value if the attribute was found, the default otherwise.
	 * @throws ConfigException When a attribute element is required and not found.
	 */
	public static int getIntAttribute(Element elem, String attName, boolean mandatory, int defaultValue) throws ConfigException {
		String attValue = getAttribute(elem, attName, mandatory);

		if (!mandatory && (attValue == null)) {
			return defaultValue;
		}

		try {
			return Integer.parseInt(attValue);
		} catch (Exception ex) {
			if (mandatory) {
				throw new ConfigException(attName
					+ " attribute of element " + elem.getNodeName()
					+ " must be an integer, but it's '" + attValue + "'");
			} else {
				return defaultValue;
			}
		}
	}

	/**
	 * getIntAttribute purpose.
	 * <p>
	 * Used to help with XML manipulations. Returns the first child integer attribute of the specified name. 
	 * An exception occurs when the node is required and not found.
	 * </p>
	 * @param root The root element to look for children in.
	 * @param attName The name of the attribute to look for.
	 * @param mandatory true when an exception should be thrown if the attribute element does not exist.
	 * @return The value if the attribute was found, the null otherwise.
	 * @throws ConfigException When a child attribute is required and not found.
	 */
	public static String getAttribute(Element elem, String attName, boolean mandatory) throws ConfigException {
		Attr att = elem.getAttributeNode(attName);

		String value = null;

		if (att != null) {
			value = att.getValue();
		}

		if (mandatory) {
			if (att == null) {
				throw new ConfigException("element "
					+ elem.getNodeName()
					+ " does not contains an attribute named " + attName);
			} else if ("".equals(value)) {
				throw new ConfigException("attribute " + attName
					+ "in element " + elem.getNodeName() + " is empty");
			}
		}

		return value;
	}

	/**
	 * getBooleanAttribute purpose.
	 * <p>
	 * Used to help with XML manipulations. Returns the first child integer attribute of the specified name. 
	 * An exception occurs when the node is required and not found.
	 * </p>
	 * @param root The root element to look for children in.
	 * @param attName The name of the attribute to look for.
	 * @param mandatory true when an exception should be thrown if the attribute element does not exist.
	 * @return The value if the attribute was found, the false otherwise.
	 * @throws ConfigException When a child attribute is required and not found.
	 */
	public static boolean getBooleanAttribute(Element elem, String attName, boolean mandatory) throws ConfigException {
		String value = getAttribute(elem, attName, mandatory);

		return Boolean.valueOf(value).booleanValue();
	}

	/**
	 * getChildText purpose.
	 * <p>
	 * Used to help with XML manipulations. Returns the first child text value of the specified element name. 
	 * </p>
	 * @param root The root element to look for children in.
	 * @param childName The name of the attribute to look for.
	 * @return The value if the child was found, the null otherwise.
	 */
	public static String getChildText(Element root, String childName) {
		try {
			return getChildText(root, childName, false);
		} catch (ConfigException ex) {
			return null;
		}
	}

	/**
	 * getChildText purpose.
	 * <p>
	 * Used to help with XML manipulations. Returns the first child text value of the specified element name. 
	 * An exception occurs when the node is required and not found.
	 * </p>
	 * @param root The root element to look for children in.
	 * @param childName The name of the attribute to look for.
	 * @param mandatory true when an exception should be thrown if the text does not exist.
	 * @return The value if the child was found, the null otherwise.
	 * @throws ConfigException When a child attribute is required and not found.
	 */
	public static String getChildText(Element root, String childName, boolean mandatory) throws ConfigException {
		Element elem = getChildElement(root, childName, mandatory);

		if (elem != null) {
			return getElementText(elem, mandatory);
		} else {
			if (mandatory) {
				String msg = "Mandatory child " + childName + "not found in "
					+ " element: " + root;

				throw new ConfigException(msg);
			}

			return null;
		}
	}

	/**
	 * getChildText purpose.
	 * <p>
	 * Used to help with XML manipulations. Returns the text value of the specified element name. 
	 * </p>
	 * @param elem The root element to look for children in.
	 * @return The value if the text was found, the null otherwise.
	 */
	public static String getElementText(Element elem) {
		try {
			return getElementText(elem, false);
		} catch (ConfigException ex) {
			return null;
		}
	}

	/**
	 * getChildText purpose.
	 * <p>
	 * Used to help with XML manipulations. Returns the text value of the specified element name. 
	 * An exception occurs when the node is required and not found.
	 * </p>
	 * @param elem The root element to look for children in.
	 * @param mandatory true when an exception should be thrown if the text does not exist.
	 * @return The value if the text was found, the null otherwise.
	 * @throws ConfigException When text is required and not found.
	 */
	public static String getElementText(Element elem, boolean mandatory) throws ConfigException {
		String value = null;

		LOGGER.finer("getting element text for " + elem);

		if (elem != null) {
			Node child;

			NodeList childs = elem.getChildNodes();

			int nChilds = childs.getLength();

			for (int i = 0; i < nChilds; i++) {
				child = childs.item(i);

				if (child.getNodeType() == Node.TEXT_NODE) {
					value = child.getNodeValue();

					if (mandatory && "".equals(value.trim())) {
						throw new ConfigException(elem.getNodeName()
							+ " text is empty");
					}

					break;
				}
			}

			if (mandatory && (value == null)) {
				throw new ConfigException(elem.getNodeName()
					+ " element does not contains text");
			}
		} else {
			throw new ConfigException("Argument element can't be null");
		}

		return value;
	}

	/**
	 * getKeyWords purpose.
	 * <p>
	 * Used to help with XML manipulations. Returns a list of keywords that were found.
	 * </p>
	 * @param keywordsElem The root element to look for children in.
	 * @return The list of keywords that were found.
	 */
	public static List getKeyWords(Element keywordsElem) {
		NodeList klist = keywordsElem.getElementsByTagName("keyword");
		int kCount = klist.getLength();
		List keywords = new ArrayList(kCount);
		String kword;
		Element kelem;
		for (int i = 0; i < kCount; i++) {
			kelem = (Element) klist.item(i);
			kword = getElementText(kelem);
			if (kword != null) {
				keywords.add(kword);
			}
		}
		return keywords;
	}

	/**
	 * getFirstChildElement purpose.
	 * <p>
	 * Used to help with XML manipulations. Returns the element which represents the first child. 
	 * </p>
	 * @param elem The root element to look for children in.
	 * @return The element if a child was found, the null otherwise.
	 */
	public static Element getFirstChildElement(Element root){
	  Node child = root.getFirstChild();

	  while (child != null) {
		  if (child.getNodeType() == Node.ELEMENT_NODE)
		  {
			return (Element) child;
		  }
		  child = child.getNextSibling();
	  }
	  return null;
	}

	/**
	 * getDoubleAttribute purpose.
	 * <p>
	 * Used to help with XML manipulations. Returns the first child integer attribute of the specified name. 
	 * An exception occurs when the node is required and not found.
	 * </p>
	 * @param root The root element to look for children in.
	 * @param attName The name of the attribute to look for.
	 * @param mandatory true when an exception should be thrown if the attribute element does not exist.
	 * @return The double value if the attribute was found, the NaN otherwise.
	 * @throws ConfigException When a attribute element is required and not found.
	 */
	public static double getDoubleAttribute(Element elem, String attName, boolean mandatory) throws ConfigException {
		String value = getAttribute(elem, attName, mandatory);

		double d = Double.NaN;

		if (value != null) {
			try {
				d = Double.parseDouble(value);
			} catch (NumberFormatException ex) {
				throw new ConfigException("Illegal attribute value for "
					+ attName + " in element " + elem.getNodeName()
					+ ". Expected double, but was " + value);
			}
		}

		return d;
	}
}
