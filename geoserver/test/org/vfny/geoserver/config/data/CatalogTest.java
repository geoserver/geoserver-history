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
package org.vfny.geoserver.config.data;

import junit.framework.TestCase;

import org.vfny.geoserver.config.EqualsLibrary;

/**
 * CatalogTest purpose.
 * <p>
 * Description of CatalogTest ...
 * <p>
 * 
 * @author dzwiers, Refractions Research, Inc.
 * @version $Id: CatalogTest.java,v 1.1.2.4 2004/01/06 22:05:11 dmzwiers Exp $
 */
public class CatalogTest extends TestCase {

	private CatalogConfig a,b;

	/**
	 * Constructor for CatalogTest.
	 * @param arg0
	 */
	public CatalogTest(String arg0) {
		super(arg0);
		
		a = new CatalogConfig();
		a.addDataStore("a",new DataStoreConfig());
		a.addDataStore("b",new DataStoreConfig());
		a.addDataStore("c",new DataStoreConfig());
		
		a.addFeature("a",new FeatureTypeConfig());
		a.addFeature("b",new FeatureTypeConfig());
		a.addFeature("c",new FeatureTypeConfig());
		
		a.addNameSpace("a",new NameSpaceConfig());
		a.addNameSpace("b",new NameSpaceConfig());
		a.addNameSpace("c",new NameSpaceConfig());
		
		a.getNameSpace("a").setDefault(true);
		a.setDefaultNameSpace(a.getNameSpace("a"));
		
		a.addStyle("a",new StyleConfig());
		a.addStyle("b",new StyleConfig());
		a.addStyle("c",new StyleConfig());
	}

	/*
	 * Test for void Data(Data)
	 */
	public void testCatalogCatalog() {
		b = new CatalogConfig(a);
		assertTrue("Testing GlobalCatalog(GlobalCatalog)\nRelies on GlobalCatalog.equals.",a.equals(b));
	}

	/*
	 * Test for Object clone()
	 */
	public void testClone() {
		b = (CatalogConfig)a.clone();
		assertTrue("Testing clone()\nRelies on GlobalCatalog.equals.",a.equals(b));
	}

	/*
	 * Test for boolean equals(Object)
	 */
	public void testEqualsObject() {
		b = new CatalogConfig();
		b.addDataStore("a",new DataStoreConfig());
		b.addDataStore("b",new DataStoreConfig());
		b.addDataStore("c",new DataStoreConfig());
		
		b.addFeature("a",new FeatureTypeConfig());
		b.addFeature("b",new FeatureTypeConfig());
		b.addFeature("c",new FeatureTypeConfig());
		
		b.addNameSpace("a",new NameSpaceConfig());
		b.addNameSpace("b",new NameSpaceConfig());
		b.addNameSpace("c",new NameSpaceConfig());
		
		b.getNameSpace("a").setDefault(true);
		b.setDefaultNameSpace(a.getNameSpace("a"));

		b.addStyle("a",new StyleConfig());
		b.addStyle("b",new StyleConfig());
		b.addStyle("c",new StyleConfig());

		assertTrue(EqualsLibrary.equals(a.getDataStores(),b.getDataStores()));
		assertTrue(a.getDefaultNameSpace().equals(b.getDefaultNameSpace()));
		assertTrue(EqualsLibrary.equals(a.getNameSpaces(),b.getNameSpaces()));
		assertTrue(EqualsLibrary.equals(a.getStyles(),b.getStyles()));
		assertTrue(EqualsLibrary.equals(a.getFeaturesTypes(),b.getFeaturesTypes()));
		assertTrue(a.equals(b));

		b.getNameSpace("a").setDefault(false);
		b.getNameSpace("b").setDefault(true);
		b.setDefaultNameSpace(a.getNameSpace("b"));
		assertTrue(!a.equals(b));
	}

}
