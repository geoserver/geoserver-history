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
 * GlobalTest purpose.
 * <p>
 * Description of GlobalTest ...
 * <p>
 * 
 * @author dzwiers, Refractions Research, Inc.
 * @version $Id: GlobalTest.java,v 1.1.2.2 2004/01/02 17:13:26 dmzwiers Exp $
 */
public class GlobalTest extends TestCase {

	/**
	 * Constructor for GlobalTest.
	 * @param arg0
	 */
	public GlobalTest(String arg0) {
		super(arg0);
		a = new GlobalConfig(); b = null;
			a.setSchemaBaseUrl("http://www.google.ca");
		a.setMaxFeatures(0);
	}



	private GlobalConfig a,b;

	/*
	 * Test for void ContactConfig(ContactConfig)
	 */
	public void testDataStoreDataStore() {
		//test requires equals.
		b = new GlobalConfig(a);
		assertTrue("Testing ContactConfig(ContactConfig)\nRelies on ContactConfig.equals.",a.equals(b));
	}

	/*
	 * Test for Object clone()
	 */
	public void testClone() {
		//test requires equals.
		b =(GlobalConfig)a.clone();
		assertTrue("Testing ContactConfig(ContactConfig)\nRelies on ContactConfig.equals.",a.equals(b));
	}

	/*
	 * Test for boolean equals(Object)
	 */
	public void testEqualsObject() {
		b = new GlobalConfig();
			b.setSchemaBaseUrl("http://www.google.ca");
		b.setMaxFeatures(0);
		
		assertTrue(a.equals(b));
		
		b.setMaxFeatures(300);
		assertTrue(!a.equals(b));
		
		a.setMaxFeatures(300);
		assertTrue(a.equals(b));
		
		ContactConfig c = new ContactConfig();
		c.setContactPerson("Me");
		b.setContact(c);
		assertTrue(!a.equals(b));
	}
}
