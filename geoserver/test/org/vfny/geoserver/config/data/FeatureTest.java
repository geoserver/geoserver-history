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

/**
 * FeatureTest purpose.
 * <p>
 * Description of FeatureTest ...
 * <p>
 * 
 * @author dzwiers, Refractions Research, Inc.
 * @version $Id: FeatureTest.java,v 1.1.2.2 2004/01/02 17:13:26 dmzwiers Exp $
 */
public class FeatureTest extends TestCase {

	/**
	 * Constructor for FeatureTest.
	 * @param arg0
	 */
	public FeatureTest(String arg0) {
		super(arg0);
		a = new FeatureTypeConfig(); b = null;
		a.setAbstract("abstract");
		a.setDataStoreId("dsId");
		a.setSRS(0);
	}



	private FeatureTypeConfig a,b;

	/*
	 * Test for void ContactConfig(ContactConfig)
	 */
	public void testDataStoreDataStore() {
		//test requires equals.
		b = new FeatureTypeConfig(a);
		assertTrue("Testing ContactConfig(ContactConfig)\nRelies on ContactConfig.equals.",a.equals(b));
	}

	/*
	 * Test for Object clone()
	 */
	public void testClone() {
		//test requires equals.
		b =(FeatureTypeConfig)a.clone();
		assertTrue("Testing ContactConfig(ContactConfig)\nRelies on ContactConfig.equals.",a.equals(b));
	}

	/*
	 * Test for boolean equals(Object)
	 */
	public void testEqualsObject() {
		b = new FeatureTypeConfig();
		b.setAbstract("abstract");
		b.setDataStoreId("dsId");
		b.setSRS(0);
		
		assertTrue(a.equals(b));
		
		b.setSRS(5);
		assertTrue(!a.equals(b));
		
		a.setSRS(5);
		assertTrue(a.equals(b));
	}
}
