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
import com.vividsolutions.jts.geom.*;
/**
 * XMLConfigWriter purpose.
 * <p>
 * This class is intended to store a configuration to be written and complete the output to XML.
 * <p>
 * 
 * @author dzwiers, Refractions Research, Inc.
 * @version $Id: XMLConfigWriter.java,v 1.1.2.4 2004/01/02 17:53:28 dmzwiers Exp $
 */
public class XMLConfigWriter {
	/**
	 * Used internally to create log information to detect errors.
	 */
	private static final Logger LOGGER = Logger.getLogger(
			"org.vfny.geoserver.config");
		
	/**
	 * The main data structure to contain the results. 
	 */
	private ModelConfig model;
	
	/**
	 * XMLConfigWriter constructor.
	 * <p>
	 * Should never be called.
	 * </p>
	 *
	 */
	private XMLConfigWriter(){}
	
	/**
	 * XMLConfigWriter constructor.
	 * <p>
	 * Stores the specified model in the new object. Throws the exception if the parameter is null.
	 * </p>
	 * @param model The model to be written
	 * @throws ConfigException is thrown when the parameter is null.
	 */
	public XMLConfigWriter(ModelConfig model) throws ConfigException{
		if(model == null){
			throw new ConfigException("ModelConfig was null");
		}
		this.model = model;
	}
	
	/**
	 * 
	 * store purpose.
	 * <p>
	 * Will write the org.vfny.geoserver.config rom memory to XML files in the dir specified. 
	 * This method is intended to complete the round trip with 
	 * XMLConfigReader.load(File).
	 * </p>
	 * @param dir Directory in which to write the configuration in memory.
	 * @throws ConfigException
	 * @see XMLConfigReader
	 */	
	public void store (File root) throws ConfigException{
		LOGGER.fine("In method store");
		if(model == null)
			throw new ConfigException("ModelConfig is null: cannot write.");
		
		WriterUtils.initFile(root,true);
		File configDir = WriterUtils.initFile(new File(root,"WEB-INF/"),true);
		File configFile = WriterUtils.initWriteFile(new File(configDir,"services.org.vfny.geoserver.config.org.vfny.geoserver.config.xml"),false);
		try{
			storeServices(new WriterHelper(new FileWriter(configFile)));
		}catch(IOException e){
			throw new ConfigException(e);
		}

		File catalogFile = WriterUtils.initWriteFile(new File(configDir,"catalog.org.vfny.geoserver.config.org.vfny.geoserver.config.xml"),false);
		try{
			storeCatalog(new WriterHelper(new FileWriter(catalogFile)));
		}catch(IOException e){
			throw new ConfigException(e);
		}
		
		File dataDir = WriterUtils.initFile(new File(root,"data/"),true);
		File featureTypeDir = WriterUtils.initFile(new File(dataDir,"featureTypes/"),true);
		storeFeatures(featureTypeDir);
		
		//File styleDir = ReaderUtils.initFile(new File(dataDir,"styles/"),true);
		//storeStyles(styleDir);
	}
	
	/**
	 * WriterHelper purpose.
	 * <p>
	 * Used to provide assitance writing xml to a Writer.
	 * <p>
	 * 
	 * @author dzwiers, Refractions Research, Inc.
	 * @version $Id: XMLConfigWriter.java,v 1.1.2.4 2004/01/02 17:53:28 dmzwiers Exp $
	 */
	protected class WriterHelper{
		/**
		 * The output writer.
		 */
		protected Writer writer;
		
		/**
		 * WriterHelper constructor.
		 * <p>
		 * Should never be called.
		 * </p>
		 *
		 */
		protected WriterHelper(){}
		
		/**
		 * WriterHelper constructor.
		 * <p>
		 * Stores the specified writer to use for output.
		 * </p>
		 * @param writer the writer which will be used for outputing the xml.
		 */
		public WriterHelper(Writer writer){
			LOGGER.fine("In constructor WriterHelper");
			this.writer = writer;
		}
		
		/**
		 * write purpose.
		 * <p>
		 * Writes the String specified to the stored output writer.
		 * </p>
		 * @param s The String to write.
		 * @throws ConfigException When an IO exception occurs.
		 */
		public void write(String s)throws ConfigException{
			try{
				writer.write(s);
				writer.flush();
			}catch(IOException e){
				throw new ConfigException(e);
			}
		}

		/**
		 * writeln purpose.
		 * <p>
		 * Writes the String specified to the stored output writer.
		 * </p>
		 * @param s The String to write.
		 * @throws ConfigException When an IO exception occurs.
		 */
		public void writeln(String s)throws ConfigException{
			try{
				writer.write(s+"\n");
				writer.flush();
			}catch(IOException e){
				throw new ConfigException(e);
			}
		}

		/**
		 * openTag purpose.
		 * <p>
		 * Writes an open xml tag with the name specified to the stored output writer.
		 * </p>
		 * @param tagName The tag name to write.
		 * @throws ConfigException When an IO exception occurs.
		 */
		public void openTag(String tagName)throws ConfigException{
			writeln("<"+tagName+">");
		}

		/**
		 * openTag purpose.
		 * <p>
		 * Writes an open xml tag with the name and attributes specified to the stored output writer.
		 * </p>
		 * @param tagName The tag name to write.
		 * @param attributes The tag attributes to write.
		 * @throws ConfigException When an IO exception occurs.
		 */
		public void openTag(String tagName, Map attributes)throws ConfigException{
			write("<"+tagName+" ");
			Iterator i = attributes.keySet().iterator();
			while(i.hasNext()){
				String s = (String)i.next();
				write(s+" = "+"\""+(attributes.get(s)).toString()+"\" ");
			}
			writeln(">");
		}

		/**
		 * closeTag purpose.
		 * <p>
		 * Writes an close xml tag with the name specified to the stored output writer.
		 * </p>
		 * @param tagName The tag name to write.
		 * @throws ConfigException When an IO exception occurs.
		 */
		public void closeTag(String tagName)throws ConfigException{
			writeln("</"+tagName+">");
		}

		/**
		 * textTag purpose.
		 * <p>
		 * Writes a text xml tag with the name and text specified to the stored output writer.
		 * </p>
		 * @param tagName The tag name to write.
		 * @param data The text data to write.
		 * @throws ConfigException When an IO exception occurs.
		 */
		public void textTag(String tagName, String data)throws ConfigException{
			writeln("<"+tagName+">"+data+"</"+tagName+">");
		}

		/**
		 * valueTag purpose.
		 * <p>
		 * Writes an xml tag with the name and value specified to the stored output writer.
		 * </p>
		 * @param tagName The tag name to write.
		 * @param value The text data to write.
		 * @throws ConfigException When an IO exception occurs.
		 */
		public void valueTag(String tagName, String value)throws ConfigException{
			writeln("<"+tagName+" value = \""+value+"\" />");
		}

		/**
		 * attrTag purpose.
		 * <p>
		 * Writes an xml tag with the name and attributes specified to the stored output writer.
		 * </p>
		 * @param tagName The tag name to write.
		 * @param attributes The tag attributes to write.
		 * @throws ConfigException When an IO exception occurs.
		 */
		public void attrTag(String tagName, Map attributes)throws ConfigException{
			write("<"+tagName+" ");
			Iterator i = attributes.keySet().iterator();
			while(i.hasNext()){
				String s = (String)i.next();
				write(s+" = "+"\""+(attributes.get(s)).toString()+"\" ");
			}
			write(" />");
		}

		/**
		 * textTag purpose.
		 * <p>
		 * Writes an xml tag with the name, text and attributes specified to the stored output writer.
		 * </p>
		 * @param tagName The tag name to write.
		 * @param data The tag text to write.
		 * @param attributes The tag attributes to write.
		 * @throws ConfigException When an IO exception occurs.
		 */
		public void textTag(String tagName, Map attributes, String data)throws ConfigException{
			write("<"+tagName+" ");
			Iterator i = attributes.keySet().iterator();
			while(i.hasNext()){
				String s = (String)i.next();
				write(s+" = "+"\""+(attributes.get(s)).toString()+"\" ");
			}
			write(">"+data+"</"+tagName+">");
		}

		/**
		 * comment purpose.
		 * <p>
		 * Writes an xml comment with the text specified to the stored output writer.
		 * </p>
		 * @param comment The comment text to write.
		 * @throws ConfigException When an IO exception occurs.
		 */
		public void comment(String comment)throws ConfigException{
			writeln("<!--");
			writeln(comment);
			writeln("-->");
		}
	}
	
	/**
	 * 
	 * storeServices purpose.
	 * <p>
	 * Writes the services.xml file from the model in memory.
	 * </p>
	 * @param cw The Configuration Writer
	 * @throws ConfigException When an IO exception occurs.
	 */
	protected void storeServices(WriterHelper cw) throws ConfigException{
		LOGGER.fine("In method storeServices");
		cw.writeln("<?org.vfny.geoserver.config.org.vfny.geoserver.config.xml version=\"1.0\" encoding=\"UTF-8\"?>");
		cw.comment("ServiceConfig level configuration");
		cw.openTag("serverConfiguration");
		
		GlobalConfig g = model.getGlobal();
		if(g!=null){
			cw.openTag("global");
			
			if(g.getLoggingLevel()!=null){
				cw.comment("Defines the logging level.  Common options are SEVERE,\n"+
					"WARNING, INFO, CONFIG, FINER, FINEST, in order of\n"+
					"Increasing statements logged.");
				cw.textTag("loggingLevel",g.getLoggingLevel().toString());
			}
			if(g.getBaseUrl()!=null && g.getBaseUrl()!=""){
				cw.comment("The base URL where this servlet will run.  If running locally\n"+
	        		"then http://localhost:8080 (or whatever port you're running on)\n"+
        			"should work.  If you are serving to the world then this must be\n"+
        			"the location where the geoserver servlets appear");
        		cw.textTag("URL",g.getBaseUrl());
			}
        	cw.comment("Sets the max number of Features returned by GetFeature");
    	    cw.valueTag("maxFeatures",""+g.getMaxFeatures());
	        cw.comment("Whether newlines and indents should be returned in \n"+
					"XML responses.  Default is false");
			cw.valueTag("verbose",""+g.isVerbose());
			cw.comment("Sets the max number of decimal places past the zero returned in\n"+
        	 	 	"a GetFeature response.  Default is 4");
    	    cw.valueTag("numDecimals",""+g.getNumDecimals());
			if(g.getCharSet()!=null){
	        	cw.comment("Sets the global character set.  This could use some more testing\n"+
					"from international users, but what it does is sets the encoding\n"+
					"globally for all postgis database connections (the charset tag\n"+
					"in FeatureTypeConfig), as well as specifying the encoding in the return\n"+
					"org.vfny.geoserver.config.org.vfny.geoserver.config.xml header and mime type.  The default is UTF-8.  Also be warned\n"+
					"that GeoServer does not check if the CharSet is valid before\n"+
					"attempting to use it, so it will fail miserably if a bad charset\n"+
					"is used.");
				cw.valueTag("charSet",g.getCharSet().toString());
			}
			if(g.getSchemaBaseUrl()!=null && g.getSchemaBaseUrl()!=""){
				cw.comment("Define a base url for the location of the wfs schemas.\n"+
					"By default GeoServer loads and references its own at\n"+
					"<URL>/data/capabilities. Uncomment to enable.  The\n"+
					"standalone Tomcat server needs SchemaBaseUrl defined\n"+
					"for validation.");
				cw.textTag("SchemaBaseUrl",g.getSchemaBaseUrl());
			}
			if(g.getContact()!=null)
				storeContact(g.getContact(),cw);
			cw.closeTag("global");
		}
		if(!(model.getWfs()==null && model.getWms()==null)){
			cw.openTag("services");
			
			if(model.getWfs()!=null)
				storeService(model.getWfs(),cw);
			if(model.getWms()!=null)
				storeService(model.getWms(),cw);
			// Z39.50 is not used in the current system.

			cw.closeTag("services");
		}
		cw.closeTag("serverConfiguration");
	}

	/**
	 * 
	 * storeContact purpose.
	 * <p>
	 * Writes a contact into the WriterHelper provided from the ContactConfig provided.
	 * </p>
	 * @param cw The Configuration Writer
	 * @param c The ContactConfig to write.
	 * @throws ConfigException When an IO exception occurs.
	 */
	protected void storeContact(ContactConfig c, WriterHelper cw) throws ConfigException{
		LOGGER.fine("In method storeContact");
		if(c!=null && !c.equals(new ContactConfig())){
			cw.openTag("ContactInformation");
			cw.openTag("ContactPersonPrimary");
			cw.textTag("ContactPerson",c.getContactPerson());
			cw.textTag("ContactOrganization",c.getContactOrganization());
			cw.closeTag("ContactPersonPrimary");
			cw.textTag("ContactPosition",c.getContactPosition());
			cw.openTag("ContactAddress");
			cw.textTag("AddressType",c.getAddressType());
			cw.textTag("Address",c.getAddress());
			cw.textTag("City",c.getAddressCity());
			cw.textTag("StateOrProvince",c.getAddressState());
			cw.textTag("PostCode",c.getAddressPostalCode());
			cw.textTag("Country",c.getAddressCountry());
			cw.closeTag("ContactAddress");
			cw.textTag("ContactVoiceTelephone",c.getContactVoice());
			cw.textTag("ContactFacsimileTelephone",c.getContactFacsimile());
			cw.textTag("ContactElectronicMailAddress",c.getContactEmail());
			cw.closeTag("ContactInformation");
		}
	}
	
	/**
	 * 
	 * storeService purpose.
	 * <p>
	 * Writes a service into the WriterHelper provided from the GlobalWFS or GlobalWMS object provided.
	 * </p>
	 * @param obj either a GlobalWFS or GlobalWMS object.
	 * @param cw The Configuration Writer
	 * @throws ConfigException When an IO exception occurs or the object provided is not of the correct type.
	 */
	protected void storeService(Object obj, WriterHelper cw) throws ConfigException{
		LOGGER.fine("In method storeService");
		ServiceConfig s = null;
		String u = null;
		String t = "";
		if(obj instanceof WFSConfig){
			WFSConfig w = (WFSConfig)obj;
			s = w.getService();
			u = w.getDescribeUrl();
			t = "GlobalWFS";
		}else
		if(obj instanceof WMSConfig){
			WMSConfig w = (WMSConfig)obj;
			s = w.getService();
			u = w.getDescribeUrl();
			t = "GlobalWMS";
		}else
		throw new ConfigException("Invalid object: not GlobalWMS of GlobalWFS");
		Map atrs = new HashMap();
		atrs.put("type",t);
		atrs.put("enabled",s.isEnabled()+"");
		cw.openTag("service",atrs);
		cw.comment("ServiceConfig elements, needed for the capabilities document\n"+
				"Title and OnlineResource are the two required");
		if(s.getName()!=null && s.getName()!="")
			cw.textTag("name",s.getName());
		if(s.getTitle()!=null && s.getTitle()!="")
			cw.textTag("title",s.getTitle());
		if(s.getAbstract()!=null && s.getAbstract()!="")
			cw.textTag("abstract",s.getAbstract());
		if(s.getKeywords().size()!=0){
			cw.openTag("keywords");
			Iterator i = s.getKeywords().iterator();
			while(i.hasNext()){
				cw.textTag("keyword",i.next().toString());
			}
			cw.closeTag("keywords");
		}
		if(s.getOnlineResource()!=null && s.getOnlineResource()!="")
			cw.textTag("onlineResource",s.getOnlineResource());
		if(s.getFees()!=null && s.getFees()!="")
			cw.textTag("fees",s.getFees());
		if(s.getAccessConstraints()!=null && s.getAccessConstraints()!="")
			cw.textTag("accessConstraints",s.getAccessConstraints());
		if(s.getMaintainer()!=null && s.getMaintainer()!="")
			cw.textTag("maintainer",s.getMaintainer());
		cw.closeTag("service");
	}

	/**
	 * 
	 * storeCatalog purpose.
	 * <p>
	 * Writes a catalog into the WriterHelper provided from GlobalCatalog provided in memory.
	 * </p>
	 * @param cw The Configuration Writer
	 * @throws ConfigException When an IO exception occurs.
	 */
	protected void storeCatalog(WriterHelper cw) throws ConfigException{
		LOGGER.fine("In method storeCatalog");
		cw.writeln("<?org.vfny.geoserver.config.org.vfny.geoserver.config.xml version=\"1.0\" encoding=\"UTF-8\"?>");
		cw.openTag("catalog");
		if(model.getCatalog().getDataStores().size()!=0){
			cw.openTag("datastores");
			cw.comment("a datastore configuration element serves as a common data source connection\n"+
				"parameters repository for all featuretypes it holds.");
			Iterator i = model.getCatalog().getDataStores().keySet().iterator();
			while(i.hasNext()){
				String s = (String)i.next();
				DataStoreConfig ds = (DataStoreConfig)model.getCatalog().getDataStores().get(s);
				if(ds != null)
					storeDataStore(cw,ds);
			}
			cw.closeTag("datastores");
		}
		if(model.getCatalog().getNameSpaces().size()!=0){
			cw.comment("Defines namespaces to be used by the datastores.");
			cw.openTag("namespaces");
			Iterator i = model.getCatalog().getNameSpaces().keySet().iterator();
			while(i.hasNext()){
				String s = (String)i.next();
				NameSpaceConfig ns = (NameSpaceConfig)model.getCatalog().getNameSpaces().get(s);
				if(ns != null)
					storeNameSpace(cw,ns);
			}
			cw.closeTag("namespaces");
		}
		if(model.getCatalog().getStyles().size()!=0){
			cw.comment("Defines the style ids to be used by the wms.  The files must be\n"+
				"contained in geoserver/misc/wms/styles.  We're working on finding\n"+
				"a better place for them, but for now that's where you must put them\n"+
				"if you want them on the server.");
			cw.openTag("styles");
			Iterator i = model.getCatalog().getStyles().keySet().iterator();
			while(i.hasNext()){
				String s = (String)i.next();
				StyleConfig st = (StyleConfig)model.getCatalog().getStyles().get(s);
				if(st != null)
					storeStyle(cw,st);
			}
			cw.closeTag("styles");
		}
		cw.closeTag("catalog");
	}

	/**
	 * 
	 * storeDataStore purpose.
	 * <p>
	 * Writes a DataStoreConfig into the WriterHelper provided.
	 * </p>
	 * @param cw The Configuration Writer
	 * @param ds The Datastore. 
	 * @throws ConfigException When an IO exception occurs.
	 */
	protected void storeDataStore(WriterHelper cw, DataStoreConfig ds) throws ConfigException{
		LOGGER.fine("In method storeDataStore");
		Map temp = new HashMap();
		if(ds.getId()!=null)
			temp.put("id",ds.getId());
		temp.put("enabled",ds.isEnabled()+"");
		if(ds.getNameSpaceId()!=null)
			temp.put("namespace",ds.getNameSpaceId());
		cw.openTag("datastore",temp);
		if(ds.getAbstract()!=null && ds.getAbstract()!="")
			cw.textTag("abstract",ds.getAbstract());
		if(ds.getTitle()!=null && ds.getTitle()!="")
			cw.textTag("title",ds.getTitle());
		if(ds.getConnectionParams().size()!=0){
			cw.openTag("connectionParams");
			Iterator i = ds.getConnectionParams().keySet().iterator();
			temp = new HashMap();
			while(i.hasNext()){
				String key = (String)i.next();
				temp.put("name",key);
				temp.put("value",ds.getConnectionParams().get(key).toString());
				cw.attrTag("parameter",temp);
			}
			cw.closeTag("connectionParams");
		}
		cw.closeTag("datastore");
	}

	/**
	 * 
	 * storeNameSpace purpose.
	 * <p>
	 * Writes a NameSpaceConfig into the WriterHelper provided.
	 * </p>
	 * @param cw The Configuration Writer
	 * @param ns The Namespace. 
	 * @throws ConfigException When an IO exception occurs.
	 */
	protected void storeNameSpace(WriterHelper cw, NameSpaceConfig ns) throws ConfigException{
		LOGGER.fine("In method storeNameSpace");
		Map attr = new HashMap();
		if(ns.getUri()!=null && ns.getUri()!="")
			attr.put("uri", ns.getUri());
		if(ns.getPrefix()!=null && ns.getPrefix()!="")
			attr.put("prefix", ns.getPrefix());
		if(ns.isDefault())
			attr.put("default","true");
		if(attr.size()!=0)
			cw.attrTag("namespace",attr);
	}

	/**
	 * 
	 * storeStyle purpose.
	 * <p>
	 * Writes a StyleConfig into the WriterHelper provided.
	 * </p>
	 * @param cw The Configuration Writer
	 * @param ns The StyleConfig. 
	 * @throws ConfigException When an IO exception occurs.
	 */
	protected void storeStyle(WriterHelper cw, StyleConfig s) throws ConfigException{
		LOGGER.fine("In method storeStyle");
		Map attr = new HashMap();
		if(s.getId()!=null && s.getId()!="")
			attr.put("id",s.getId());
		if(s.getFilename()!=null)
			attr.put("filename",s.getFilename());
		if(s.isDefault())
			attr.put("default","true");
		if(attr.size()!=0)
			cw.attrTag("style",attr);
	}

	/**
	 * storeStyle purpose.
	 * <p>
	 * Sets up writing FeatureTypes into their Directories.
	 * </p>
	 * @param dir The FeatureTypes directory
	 * @throws ConfigException When an IO exception occurs.
	 * @see storeFeature(FeatureTypeConfig,File)
	 */
	protected void storeFeatures(File dir) throws ConfigException{
		LOGGER.fine("In method storeFeatures");
		Iterator i = model.getCatalog().getFeaturesTypes().keySet().iterator();
		while(i.hasNext()){
			String s = (String)i.next();
			FeatureTypeConfig ft = (FeatureTypeConfig)model.getCatalog().getFeaturesTypes().get(s);
			if(ft!=null){
				File dir2 = WriterUtils.initWriteFile(new File(dir,ft.getDirName()),true);
				storeFeature(ft,dir2);
				if(ft.getSchema()!=null)
					storeFeatureSchema(ft.getSchema(),dir2);
			}
		}
	}

	/**
	 * storeStyle purpose.
	 * <p>
	 * Writes a FeatureTypes into it's Directory.
	 * </p>
	 * @param dir The particular FeatureTypeConfig directory
	 * @throws ConfigException When an IO exception occurs.
	 * @see storeFeatures(File)
	 */
	protected void storeFeature(FeatureTypeConfig ft, File dir) throws ConfigException{
		LOGGER.fine("In method storeFeature");
		File f = WriterUtils.initWriteFile(new File(dir,"info.org.vfny.geoserver.config.org.vfny.geoserver.config.xml"),false);
		try{
			WriterHelper cw = new WriterHelper(new FileWriter(f));
			Map m = new HashMap();
			if(ft.getDataStoreId()!=null && ft.getDataStoreId()!="")
				m.put("datastore",ft.getDataStoreId());
			cw.openTag("featureType",m);
			if(ft.getName()!=null && ft.getName()!="")
				cw.textTag("name",ft.getName());
			cw.comment("native wich EPGS code for the FeatureTypeConfig");
			cw.textTag("SRS",ft.getSRS()+"");
			if(ft.getTitle()!=null && ft.getTitle()!="")
				cw.textTag("title",ft.getTitle());
			if(ft.getAbstract()!=null && ft.getAbstract()!="")
				cw.textTag("abstract",ft.getAbstract());
			cw.valueTag("numDecimals",ft.getNumDecimals()+"");
			if(ft.getKeywords().size()!=0){
				String s = "";
				Iterator i = ft.getKeywords().iterator();
				if(i.hasNext()){
					s = i.next().toString();
					while(i.hasNext()){
						s = s + "," + i.next().toString();
					}
				}
				cw.textTag("keywords",s);
			}
			if(ft.getLatLongBBox()!=null){
				m = new HashMap();
				Envelope e = ft.getLatLongBBox();
				// from creation, isn't stored otherwise
				if(!e.equals(new Envelope())){
					m.put("dynamic","true");
					m.put("minx",e.getMinX()+"");
					m.put("miny",e.getMinY()+"");
					m.put("maxx",e.getMaxX()+"");
					m.put("maxy",e.getMaxY()+"");
				}
				cw.attrTag("latLonBoundingBox",m);
			}
			if(ft.getDefaultStyle()!=null && ft.getDefaultStyle()!=""){
				cw.comment("the default style this FeatureTypeConfig can be represented by.\n"+
					"at least must contain the \"default\" attribute ");
				m = new HashMap();
				m.put("default",ft.getDefaultStyle());
				cw.attrTag("styles",m);
			}
			if(ft.getDefinitionQuery()!=null){
				cw.openTag("definitionQuery");
				StringWriter sw = new StringWriter();
				/*
				 * @TODO replace with current implementation.
				 */
				org.geotools.filter.XMLEncoder xe = new org.geotools.filter.XMLEncoder(sw);
				xe.encode(ft.getDefinitionQuery());
				cw.writeln(sw.toString());
				cw.closeTag("definitionQuery");
			}
			cw.closeTag("featureType");
		}catch(IOException e){
			throw new ConfigException(e);
		}
	}
	
	protected void storeFeatureSchema(String fs, File dir) throws ConfigException{
		File f = WriterUtils.initWriteFile(new File(dir,"schema.org.vfny.geoserver.config.org.vfny.geoserver.config.xml"),false);
		try{
			WriterHelper cw = new WriterHelper(new FileWriter(f));
			cw.write(fs);
		}catch(IOException e){
			throw new ConfigException(e);
		}
	}
}

/**
 * WriterUtils purpose.
 * <p>
 * This is a static class which is used by XMLConfigWriter for File IO validation tests. 
 * <p>
 * 
 * @author dzwiers, Refractions Research, Inc.
 * @version $Id: XMLConfigWriter.java,v 1.1.2.4 2004/01/02 17:53:28 dmzwiers Exp $
 */
class WriterUtils{
	/**
	 * Used internally to create log information to detect errors.
	 */
	private static final Logger LOGGER = Logger.getLogger(
			"org.vfny.geoserver.config");
			
	/**
	 * WriterUtils constructor.
	 * <p>
	 * Static class, should never be used.
	 * </p>
	 *
	 */
	private WriterUtils(){}

	/**
	 * initFile purpose.
	 * <p>
	 * Checks to ensure the handle exists. If the handle is a directory and not created, it is created
	 * </p>
	 * @param f the File handle
	 * @param isDir true when the handle is intended to be a directory.
	 * @return The file passed in.
	 * @throws ConfigException When an IO error occurs or the handle is invalid.
	 */
	public static File initFile(File f, boolean isDir) throws ConfigException{
		if(!f.exists()){
			LOGGER.fine("Creating File: "+f.toString());
			if(isDir){
				if(!f.mkdir())
					throw new ConfigException("Path specified does not have a valid file.\n"+f+"\n\n");
			}else{
				try{
					if(!f.createNewFile())
						throw new ConfigException("Path specified does not have a valid file.\n"+f+"\n\n");
				}catch(IOException e){
					throw new ConfigException(e);
				}
			}
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
	 * initFile purpose.
	 * <p>
	 * Checks to ensure the handle exists and can be writen to. If the handle is a directory and not created, it is created
	 * </p>
	 * @param f the File handle
	 * @param isDir true when the handle is intended to be a directory.
	 * @return The file passed in.
	 * @throws ConfigException When an IO error occurs or the handle is invalid.
	 */
	public static File initWriteFile(File f, boolean isDir) throws ConfigException{
		initFile(f,isDir);
		if(!f.canWrite())
			throw new ConfigException("Cannot Write to file: "+f.toString());
		return f;
	}
}