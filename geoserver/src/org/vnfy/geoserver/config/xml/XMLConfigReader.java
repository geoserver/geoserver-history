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
package org.vnfy.geoserver.config.xml;

import org.vnfy.geoserver.config.*;
import org.vnfy.geoserver.config.data.*;
import org.vnfy.geoserver.config.wfs.*;
import org.vnfy.geoserver.config.wms.*;

import java.io.*;
import java.util.*;
import java.util.logging.*;
import org.w3c.dom.*;
import org.xml.sax.*;
import javax.xml.parsers.*;
import java.nio.charset.*;
import org.geotools.filter.*;
import com.vividsolutions.jts.geom.*;
import org.vfny.geoserver.global.*;
/**
 * XMLConfigReader purpose.
 * <p>
 * Description of XMLConfigReader 
 * Static class to load a configuration org.vnfy.geoserver.config.
 * <p>
 * Example Use:
 * <pre><code>
 * Model m = XMLConfigReader.load(new File("/conf/"));
 * </code></pre>
 * 
 * @author dzwiers, Refractions Research, Inc.
 * @version $Id: XMLConfigReader.java,v 1.1.2.2 2003/12/31 00:35:04 dmzwiers Exp $
 */
public class XMLConfigReader {
	private static final Logger LOGGER = Logger.getLogger(
			"org.vfny.geoserver.config");
	private Model model = null;
	
	private File root;
	
	private boolean initialized = false;
	/**
	 * XMLConfigReader constructor.
	 * <p>
	 * Should never be called.
	 * </p>
	 *
	 */
	protected XMLConfigReader(){
		model = new Model();
		root = new File(".");
	}
	
	/**
	 * 
	 * <p>This method loads the config files from the 
	 * specified directory into a Model. If the path is incorrect, 
	 * or the directory is formed correctly, a ConfigException 
	 * will be thrown and/or null returned. <br><br>
	 * The config directory is as follows:<br>
	 * <ul><li>./WEB-INF/catalog.org.vnfy.geoserver.config.org.vnfy.geoserver.config.xml</li>
	 * <li>./WEB-INF/services.org.vnfy.geoserver.config.org.vnfy.geoserver.config.xml</li>
	 * <li>./data/featuretypes/ * /info.org.vnfy.geoserver.config.org.vnfy.geoserver.config.xml</li>
	 * <li>./data/featuretypes/ * /schema.org.vnfy.geoserver.config.org.vnfy.geoserver.config.xml</li>
	 * </ul>
	 * </p>
	 * @param root A directory which contains the config files.  
	 * @throws ConfigException When an error occurs. 
	 * @return The resulting Model
	 */
	public XMLConfigReader(File root) throws ConfigException{
		this.root = root;
		model = new Model();
		load();
		initialized = true;
	}
	
	public boolean isInitialized(){
		return initialized;
	}
	
	/**
	 * getModel purpose.
	 * <p>
	 * User'd responsibility to ensure a valid org.vnfy.geoserver.config has been parsed. 
	 * Used to get the results of a configuration parse.
	 * </p>
	 * @return The results of a configuration parse residing in memory.
	 */
	public Model getModel(){
		return model;
	}
	
	protected void load() throws ConfigException{
		
		root = ReaderUtils.initFile(root,true);
		File configDir = ReaderUtils.initFile(new File(root,"WEB-INF/"),true);
		File configFile = ReaderUtils.initFile(new File(configDir,"services.org.vnfy.geoserver.config.org.vnfy.geoserver.config.xml"),false);
		
		loadServices(configFile);

		File catalogFile = ReaderUtils.initFile(new File(configDir,"catalog.org.vnfy.geoserver.config.org.vnfy.geoserver.config.xml"),false);
		File dataDir = ReaderUtils.initFile(new File(root,"data/"),true);
		File featureTypeDir = ReaderUtils.initFile(new File(dataDir,"featureTypes/"),true);
		File styleDir = ReaderUtils.initFile(new File(dataDir,"styles/"),true);
		loadCatalog(catalogFile,featureTypeDir,styleDir);
		
		
		// Future additions
		// validationDir = ReaderUtils.initFile(new File(dataDir,"validation/"),true);
		// loadValidation(validationDir);	
	}
	
	protected void loadServices(File configFile) throws ConfigException{
		LOGGER.fine("loading config file: " + configFile);
		Element configElem = ReaderUtils.loadConfig(configFile);

		LOGGER.fine("parsing configuration documents");
		Element elem = (Element) configElem.getElementsByTagName("global").item(0);
		Global g = loadGlobal(elem);
		model.setGlobal(g);

		NodeList configuredServices = configElem.getElementsByTagName("service");
		int nServices = configuredServices.getLength();

		for (int i = 0; i < nServices; i++) {
			elem = (Element) configuredServices.item(i);

			String serviceType = elem.getAttribute("type");

			if ("WFS".equalsIgnoreCase(serviceType)) {
				model.setWfs(loadWFS(elem,model.getGlobal()));
			} else if ("WMS".equalsIgnoreCase(serviceType)) {
				model.setWms(loadWMS(elem,model.getGlobal()));
			} else if ("Z39.50".equalsIgnoreCase(serviceType)) {
				//...
			} else {
				throw new ConfigException("Unknown service type: "+ serviceType);
			}
		}
	}
	
	protected void loadCatalog(File catalogFile, File featureTypeDir, File styleDir) throws ConfigException {
		LOGGER.fine("loading catalog file: " + catalogFile);
		Element catalogElem = ReaderUtils.loadConfig(catalogFile);
		
		LOGGER.info("loading catalog configuration");
		Catalog c = new Catalog();
		c.setNameSpaces(loadNameSpaces(ReaderUtils.getChildElement(catalogElem, "namespaces", true)));
		setDefaultNS(c);
		c.setDataStores(loadDataStores(ReaderUtils.getChildElement(catalogElem, "datastores", true),c.getNameSpaces()));
		c.setStyles(loadStyles(ReaderUtils.getChildElement(catalogElem, "styles", false), styleDir));
		c.setFeaturesTypes(loadFeatureTypes(featureTypeDir));

		model.setCatalog(c);
	}
	
	protected void setDefaultNS(Catalog c){
		Iterator i = c.getNameSpaces().values().iterator();
		while(i.hasNext()){
			org.vnfy.geoserver.config.data.NameSpace ns = (org.vnfy.geoserver.config.data.NameSpace)i.next();
			if(ns.isDefault()){
				c.setDefaultNameSpace(ns);
				return;
			}
		}
	}
	
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
	
	protected Global loadGlobal(Element globalElem) throws ConfigException{
		Global g = new Global();
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
	
	protected Contact loadContact(Element contactInfoElement) throws ConfigException{
		Contact c = new Contact();
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
	
	protected WFS loadWFS(Element wfsElement, Global g) throws ConfigException{
		WFS w = new WFS();
		w.setService(loadService(wfsElement));
		w.setDescribeUrl(g.getBaseUrl().toString() + "wfs/");
		return w;
	}
	
	protected WMS loadWMS(Element wmsElement, Global g) throws ConfigException{
		WMS w = new WMS();
		w.setService(loadService(wmsElement));
		w.setDescribeUrl(g.getBaseUrl().toString() + "wms/");
		return w;
	}
	
	protected Service loadService(Element serviceRoot) throws ConfigException{
		Service s = new Service();

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
	
	protected Map loadNameSpaces(Element nsRoot) throws ConfigException{

		NodeList nsList = nsRoot.getElementsByTagName("namespace");
		Element elem;
		int nsCount = nsList.getLength();
		Map nameSpaces = new HashMap(nsCount);

		for (int i = 0; i < nsCount; i++) {
			elem = (Element) nsList.item(i);
			org.vnfy.geoserver.config.data.NameSpace ns = new org.vnfy.geoserver.config.data.NameSpace();
			ns.setUri(ReaderUtils.getAttribute(elem, "uri", true));
			ns.setPrefix(ReaderUtils.getAttribute(elem, "prefix", true));
			ns.setDefault(ReaderUtils.getBooleanAttribute(elem, "default", false) || (nsCount == 1));
			LOGGER.config("added namespace " + ns);
			nameSpaces.put(ns.getPrefix(), ns);
		}
		return nameSpaces;
	}
	
	protected Map loadStyles(Element stylesElem, File styleDir) throws ConfigException{
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
			Style s = new Style();
			s.setId(ReaderUtils.getAttribute(styleElem, "id", true));
			s.setFilename(new File(ReaderUtils.getAttribute(styleElem, "filename", true)));
			s.setDefault(ReaderUtils.getBooleanAttribute(styleElem, "default", false));
			styles.put(s.getId(), s);
		}
		return styles;
	}
	
	protected Map loadDataStores(Element dsRoot, Map nameSpaces) throws ConfigException {
		Map dataStores = new HashMap();

		NodeList dsElements = dsRoot.getElementsByTagName("datastore");
		int dsCnt = dsElements.getLength();
		DataStore dsConfig;
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
	
	protected DataStore loadDataStore(Element dsElem, Map nameSpaces) throws ConfigException {
		DataStore ds = new DataStore();
		
		LOGGER.finer("creating a new DataStore configuration");
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
		LOGGER.fine("loading connection parameters for DataStore " + ds.getNameSpaceId());
		ds.setConnectionParams(loadConnectionParams(ReaderUtils.getChildElement(dsElem, "connectionParams", true)));
		LOGGER.info("created " + toString());
		return ds;
	}
	
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
	
	protected Map loadFeatureTypes(File featureTypeDir) throws ConfigException {
		LOGGER.finest("examining: " + featureTypeDir.getAbsolutePath());
		LOGGER.finest("is dir: " + featureTypeDir.isDirectory());
		Map featureTypes = new HashMap();

		if (featureTypeDir.isDirectory()) {
			File[] file = featureTypeDir.listFiles();
			for (int i = 0, n = file.length; i < n; i++) {
				LOGGER.fine("Info dir:"+file[i].toString());
				FeatureType ft = loadFeature(new File(file[i],"info.org.vnfy.geoserver.config.org.vnfy.geoserver.config.xml"));
				featureTypes.put(ft.getName(), ft);
			}
		}
		return featureTypes;
	}
	
	protected FeatureType loadFeature(File infoFile) throws ConfigException{
		if (isInfoFile(infoFile)) {
			Element featureElem = ReaderUtils.loadConfig(infoFile);
			FeatureType ft = null;

			File parentDir = infoFile.getParentFile();
			ft = loadFeaturePt2(featureElem);
			ft.setDirName(parentDir.getName());

			File pathToSchemaFile = new File(parentDir, "schema.org.vnfy.geoserver.config.org.vnfy.geoserver.config.xml");
			LOGGER.finest("pathToSchema is " + pathToSchemaFile);
			ft.setSchema(loadSchema(pathToSchemaFile));
			LOGGER.finer("added featureType " + ft.getName());
			
			return ft;
		}
		throw new ConfigException("Invalid Info file.");
	}
	
	protected FeatureType loadFeaturePt2(Element fTypeRoot) throws ConfigException{
		FeatureType ft = new FeatureType();

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
	
	protected static boolean isInfoFile(File testFile) {
		String testName = testFile.getAbsolutePath();
		int start = testName.length() - "info.org.vnfy.geoserver.config.org.vnfy.geoserver.config.xml".length();
		int end = testName.length();

		return testName.substring(start, end).equals("info.org.vnfy.geoserver.config.org.vnfy.geoserver.config.xml");
	}
	
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


class ReaderUtils{
	private static final Logger LOGGER = Logger.getLogger(
			"org.vfny.geoserver.config");
	
	private ReaderUtils(){}
	
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
			String message = "trouble with parser to read org.vnfy.geoserver.config.org.vnfy.geoserver.config.xml, make sure class"
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
	
	public static Element getChildElement(Element root, String name) throws ConfigException{
		return getChildElement(root, name, false);
	}
	
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
	
	public static String getAttribute(Element elem, String attName,
		boolean mandatory) throws ConfigException {
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
	
	public static boolean getBooleanAttribute(Element elem, String attName, boolean mandatory) throws ConfigException {
		String value = getAttribute(elem, attName, mandatory);

		return Boolean.valueOf(value).booleanValue();
	}
	
	public static String getChildText(Element root, String childName) {
		try {
			return getChildText(root, childName, false);
		} catch (ConfigException ex) {
			return null;
		}
	}
	
	public static String getChildText(Element root, String childName,
		boolean mandatory) throws ConfigException {
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
	
	public static String getElementText(Element elem) {
		try {
			return getElementText(elem, false);
		} catch (ConfigException ex) {
			return null;
		}
	}

	public static String getElementText(Element elem, boolean mandatory)
		throws ConfigException {
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
	
	public static double getDoubleAttribute(Element elem, String attName,
		boolean mandatory) throws ConfigException {
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
