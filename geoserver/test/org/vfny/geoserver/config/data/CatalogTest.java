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
 * @version $Id: CatalogTest.java,v 1.1.2.1 2003/12/31 20:05:38 dmzwiers Exp $
 */
public class CatalogTest extends TestCase {

	private Catalog a,b;

	/**
	 * Constructor for CatalogTest.
	 * @param arg0
	 */
	public CatalogTest(String arg0) {
		super(arg0);
		
		a = new Catalog();
		a.addDataStore("a",new DataStore());
		a.addDataStore("b",new DataStore());
		a.addDataStore("c",new DataStore());
		
		a.addFeature("a",new FeatureType());
		a.addFeature("b",new FeatureType());
		a.addFeature("c",new FeatureType());
		
		a.addNameSpace("a",new NameSpace());
		a.addNameSpace("b",new NameSpace());
		a.addNameSpace("c",new NameSpace());
		
		a.getNameSpace("a").setDefault(true);
		a.setDefaultNameSpace(a.getNameSpace("a"));
		
		a.addStyle("a",new Style());
		a.addStyle("b",new Style());
		a.addStyle("c",new Style());
	}

	/*
	 * Test for void Catalog(Catalog)
	 */
	public void testCatalogCatalog() {
		b = new Catalog(a);
		assertTrue("Testing Catalog(Catalog)\nRelies on Catalog.equals.",a.equals(b));
	}

	/*
	 * Test for Object clone()
	 */
	public void testClone() {
		b = (Catalog)a.clone();
		assertTrue("Testing clone()\nRelies on Catalog.equals.",a.equals(b));
	}

	/*
	 * Test for boolean equals(Object)
	 */
	public void testEqualsObject() {
		b = new Catalog();
		b.addDataStore("a",new DataStore());
		b.addDataStore("b",new DataStore());
		b.addDataStore("c",new DataStore());
		
		b.addFeature("a",new FeatureType());
		b.addFeature("b",new FeatureType());
		b.addFeature("c",new FeatureType());
		
		b.addNameSpace("a",new NameSpace());
		b.addNameSpace("b",new NameSpace());
		b.addNameSpace("c",new NameSpace());
		
		b.getNameSpace("a").setDefault(true);
		b.setDefaultNameSpace(a.getNameSpace("a"));

		b.addStyle("a",new Style());
		b.addStyle("b",new Style());
		b.addStyle("c",new Style());

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
