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
package org.vnfy.geoserver.config;


import junit.framework.TestCase;

/**
 * GlobalTest purpose.
 * <p>
 * Description of GlobalTest ...
 * <p>
 * 
 * @author dzwiers, Refractions Research, Inc.
 * @version $Id: GlobalTest.java,v 1.1.2.1 2003/12/30 23:39:32 dmzwiers Exp $
 */
public class GlobalTest extends TestCase {

	/**
	 * Constructor for GlobalTest.
	 * @param arg0
	 */
	public GlobalTest(String arg0) {
		super(arg0);
		a = new Global(); b = null;
			a.setSchemaBaseUrl("http://www.google.ca");
		a.setMaxFeatures(0);
	}



	private Global a,b;

	/*
	 * Test for void Contact(Contact)
	 */
	public void testDataStoreDataStore() {
		//test requires equals.
		b = new Global(a);
		assertTrue("Testing Contact(Contact)\nRelies on Contact.equals.",a.equals(b));
	}

	/*
	 * Test for Object clone()
	 */
	public void testClone() {
		//test requires equals.
		b =(Global)a.clone();
		assertTrue("Testing Contact(Contact)\nRelies on Contact.equals.",a.equals(b));
	}

	/*
	 * Test for boolean equals(Object)
	 */
	public void testEqualsObject() {
		b = new Global();
			b.setSchemaBaseUrl("http://www.google.ca");
		b.setMaxFeatures(0);
		
		assertTrue(a.equals(b));
		
		b.setMaxFeatures(300);
		assertTrue(!a.equals(b));
		
		a.setMaxFeatures(300);
		assertTrue(a.equals(b));
		
		Contact c = new Contact();
		c.setContactPerson("Me");
		b.setContact(c);
		assertTrue(!a.equals(b));
	}
}
