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


/**
 * CatalogTest purpose.
 * <p>
 * Description of CatalogTest ...
 * <p>
 * 
 * @author dzwiers, Refractions Research, Inc.
 * @version $Id: CatalogTest.java,v 1.1.2.3 2004/01/08 18:44:29 dmzwiers Exp $
 */
public class CatalogTest extends TestCase {

	private DataConfig a,b;

	/**
	 * Constructor for CatalogTest.
	 * @param arg0
	 */
	public CatalogTest(String arg0) {
		super(arg0);
		
		a = new DataConfig();
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
	/*public void testCatalogCatalog() {
		b = new DataConfig(a);
		assertTrue("Testing GlobalCatalog(GlobalCatalog)\nRelies on GlobalCatalog.equals.",a.equals(b));
	}*/

	/*
	 * Test for Object clone()
	 */
	/*public void testClone() {
		b = (DataConfig)a.clone();
		assertTrue("Testing clone()\nRelies on GlobalCatalog.equals.",a.equals(b));
	}*/

}
