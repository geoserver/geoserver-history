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
package org.vfny.geoserver.config;


import junit.framework.TestCase;

import org.vfny.geoserver.config.data.CatalogConfig;
import org.vfny.geoserver.config.data.NameSpaceConfig;

/**
 * ConfigTest purpose.
 * <p>
 * Description of ConfigTest ...
 * <p>
 * 
 * @author dzwiers, Refractions Research, Inc.
 * @version $Id: ConfigTest.java,v 1.1.2.2 2004/01/02 17:13:26 dmzwiers Exp $
 */
public class ConfigTest extends TestCase {

	private ModelConfig a,b;
	/**
	 * Constructor for ConfigTest.
	 * @param arg0
	 */
	public ConfigTest(String arg0) {
		super(arg0);
		a = new ModelConfig(); b = null;
		GlobalConfig g = new GlobalConfig();
			g.setSchemaBaseUrl("http://www.google.ca");
		g.setMaxFeatures(0);
		a.setGlobal(g);
	}

	/*
	 * Test for void ContactConfig(ContactConfig)
	 */
	public void testDataStoreDataStore() {
		//test requires equals.
		b = new ModelConfig(a);
		assertTrue("Testing ContactConfig(ContactConfig)\nRelies on ContactConfig.equals.",a.equals(b));
	}

	/*
	 * Test for Object clone()
	 */
	public void testClone() {
		//test requires equals.
		b =(ModelConfig)a.clone();
		assertTrue("Testing ContactConfig(ContactConfig)\nRelies on ContactConfig.equals.",a.equals(b));
	}

	/*
	 * Test for boolean equals(Object)
	 */
	public void testEqualsObject() {
		b = new ModelConfig();
		GlobalConfig g = new GlobalConfig();
			g.setSchemaBaseUrl("http://www.google.ca");
		g.setMaxFeatures(0);
		b.setGlobal(g);
		
		assertTrue(a.getGlobal().equals(b.getGlobal()));
		assertTrue(a.equals(b));
		
		CatalogConfig c = new CatalogConfig();
		c.addNameSpace("c",new NameSpaceConfig());
		b.setCatalog(c);
		assertTrue(!a.equals(b));
	}

}
