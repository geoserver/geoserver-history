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
 * NameSpaceTest purpose.
 * <p>
 * Description of NameSpaceTest ...
 * <p>
 * 
 * @author dzwiers, Refractions Research, Inc.
 * @version $Id: NameSpaceTest.java,v 1.1.2.1 2004/01/07 21:36:13 dmzwiers Exp $
 */
public class NameSpaceTest extends TestCase {

	private NameSpaceConfig a,b;
	/**
	 * Constructor for NameSpaceTest.
	 * @param arg0
	 */
	public NameSpaceTest(String arg0) {
		super(arg0);
		a = new NameSpaceConfig();
		a.setPrefix(":");
		a.setUri("http://www.google.ca");
	}

	/*
	 * Test for void NameSpaceConfig(NameSpaceConfig)
	 */
	public void testNameSpaceNameSpace() {
		//test requires equals.
		b = new NameSpaceConfig(a);
		assertTrue("Testing ContactConfig(ContactConfig)\nRelies on ContactConfig.equals.",a.equals(b));
	}

	/*
	 * Test for Object clone()
	 */
	public void testClone() {
		//test requires equals.
		b =(NameSpaceConfig)a.clone();
		assertTrue("Testing ContactConfig(ContactConfig)\nRelies on ContactConfig.equals.",a.equals(b));
	}

	/*
	 * Test for boolean equals(Object)
	 */
	public void testEqualsObject() {
		b = new NameSpaceConfig();
		b.setPrefix(":");
		b.setUri("http://www.google.ca");
		assertTrue(a.equals(b));
		
		b.setDefault(true);
		assertTrue(!a.equals(b));
		
		b.setDefault(false);
		b.setPrefix(".");
		assertTrue(!a.equals(b));
	}
}
