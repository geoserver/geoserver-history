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

import junit.framework.TestCase;
import java.io.*;
import java.util.*;
import org.vnfy.geoserver.config.*;
import org.vnfy.geoserver.config.data.*;
import org.vnfy.geoserver.config.wfs.*;
import org.vnfy.geoserver.config.wms.*;

/**
 * XMLConfigReaderTest purpose.
 * <p>
 * Description of XMLConfigReaderTest ...
 * <p>
 * 
 * @author dzwiers, Refractions Research, Inc.
 * @version $Id: XMLConfigReaderTest.java,v 1.1.2.2 2003/12/31 00:35:05 dmzwiers Exp $
 */
public class XMLConfigReaderTest extends TestCase {

	private static final String testPath1 = "C:\\Java\\workspace\\Geoserver-Model\\tests\\test1";
	private static final String testPath2 = "C:\\Java\\workspace\\Geoserver-Model\\tests\\test2\\";
	private File root1 = null;
	private File root2 = null;

	/**
	 * Constructor for XMLConfigReaderTest.
	 * @param arg0
	 */
	public XMLConfigReaderTest(String arg0) {
		super(arg0);
		root1 = new File(testPath1);
		root2 = new File(testPath2);
	}
	
	public void testLoadServices(){
		File configFile = new File(root1,"services.org.vnfy.geoserver.config.org.vnfy.geoserver.config.xml");
		XMLConfigReaderExpose cfe = new XMLConfigReaderExpose();
		try{
			cfe.loadServicesWrapper(configFile);
		}catch(ConfigException e){
			fail(e.toString());
		}
		
		Model m = cfe.getModel();
		boolean r = true;
		r = r && m!=null;
		if(r){
			r = r && m.getGlobal() != null;
			r = r && !m.getGlobal().equals(new Global());
			r = r && m.getWfs() != null;
			r = r && !m.getWfs().equals(new WFS());
			r = r && m.getWms() != null;
			r = r && !m.getWms().equals(new WMS());
		}
		assertTrue(r);
	}
	
	public void testLoadCatalog(){
		XMLConfigReaderExpose cfe = new XMLConfigReaderExpose();
		try{
			// pass incorrect feature dir to avoid running this portion
			cfe.loadCatalogWrapper(new File(root1,"catalog.org.vnfy.geoserver.config.org.vnfy.geoserver.config.xml"),new File(root1,"catalog.org.vnfy.geoserver.config.org.vnfy.geoserver.config.xml"),new File(root1,"data/styles/"));
		}catch(ConfigException e){
			fail(e.toString());
		}
		Model m = cfe.getModel();
		Catalog c = m.getCatalog();
		boolean r = true;
		r = r && c!=null;
		if(r){
			r = r && c.getDataStores()!= null;
			r = r && c.getNameSpaces()!= null;
			r = r && c.getStyles()!= null;
			r = r && !c.getDataStores().equals(new HashMap());
			r = r && !c.getNameSpaces().equals(new HashMap());
			r = r && !c.getStyles().equals(new HashMap());
		}
		assertTrue(r);
	}
	
	public void testLoadFeatures(){
		XMLConfigReaderExpose cfe = new XMLConfigReaderExpose();
		try{
			// pass incorrect feature dir to avoid running this portion
			cfe.loadFeatureTypesWrapper(new File(root1,"featureTypes/"));
		}catch(ConfigException e){
System.out.println("***************");
System.out.println(e.getMessage());
System.out.println("***************");
			fail(e.toString());
		}
		Map m = cfe.getModel().getCatalog().getFeaturesTypes();
		boolean r = true;
		r = r && m!=null;
		if(r){
			Iterator i = m.keySet().iterator();
			while(i.hasNext() && r){
				String key = (String)i.next();
				FeatureType f = (FeatureType)m.get(key);
				if(f == null)
					r = false;
				else{
					r = r && key == f.getName();
					r = r && f.getSchema() != null;
				} 
			}
		}
		assertTrue(r);
	}
	
	public void testLoad(){
		XMLConfigReader rc = null;
		try{
			rc = new XMLConfigReader(root2);
		}catch(ConfigException e){
			fail(e.toString());
		}
		boolean r = true;
		

		Model m = rc.getModel();
		r = r && m!=null;
		if(r){
			r = r && m.getGlobal() != null;
			r = r && !m.getGlobal().equals(new Global());
			r = r && m.getWfs() != null;
			r = r && !m.getWfs().equals(new WFS());
			r = r && m.getWms() != null;
			r = r && !m.getWms().equals(new WMS());

			Catalog c = m.getCatalog();
			r = r && c!=null;
			if(r){
				r = r && c.getDataStores()!= null;
				r = r && c.getFeaturesTypes()!= null;
				r = r && c.getNameSpaces()!= null;
				r = r && c.getStyles()!= null;
				r = r && !c.getDataStores().equals(new HashMap());
				r = r && !c.getFeaturesTypes().equals(new HashMap());
				r = r && !c.getNameSpaces().equals(new HashMap());
				r = r && !c.getStyles().equals(new HashMap());
		
				Map mp = c.getFeaturesTypes();
				r = r && m!=null;
				if(r){
					Iterator i = mp.keySet().iterator();
					while(i.hasNext() && r){
						String key = (String)i.next();
						FeatureType f = (FeatureType)mp.get(key);
						if(f == null)
							r = false;
						else{
							r = r && key == f.getName();
							r = r && f.getSchema() != null;
						}
					} 
				}
			}
		}
		assertTrue(r);
	}
}

class XMLConfigReaderExpose extends XMLConfigReader{
	public XMLConfigReaderExpose(){
			super();
	}
	
	public void loadServicesWrapper(File f) throws ConfigException{
		loadServices(f);
	}
	
	public void loadCatalogWrapper(File f1, File f2, File f3) throws ConfigException{
		loadCatalog(f1,f2,f3);
	}
	
	public void loadFeatureTypesWrapper(File f) throws ConfigException{
		loadFeatureTypes(f);
	}
}