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

import java.io.File;

import junit.framework.TestCase;
/**
 * StyleTest purpose.
 * <p>
 * Description of StyleTest ...
 * <p>
 * 
 * @author dzwiers, Refractions Research, Inc.
 * @version $Id: StyleTest.java,v 1.1.2.2 2004/01/08 18:44:29 dmzwiers Exp $
 */
public class StyleTest extends TestCase {

	private StyleConfig a,b;
	File f;
	/**
	 * Constructor for StyleTest.
	 * @param arg0
	 */
	public StyleTest(String arg0) {
		super(arg0);
		a = new StyleConfig();
		a.setId("test 1");
		f = null;
		try{
			f = new File(".");
		}catch(Exception e){}
		a.setFilename(f);
	}

	/*
	 * Test for void NameSpaceConfig(NameSpaceConfig)
	 */
	/*public void testNameSpaceNameSpace() {
		//test requires equals.
		b = new StyleConfig(a);
		assertTrue("Testing ContactConfig(ContactConfig)\nRelies on ContactConfig.equals.",a.equals(b));
	}*/

	/*
	 * Test for Object clone()
	 */
	/*public void testClone() {
		//test requires equals.
		b =(StyleConfig)a.clone();
		assertTrue("Testing ContactConfig(ContactConfig)\nRelies on ContactConfig.equals.",a.equals(b));
	}*/

	/*
	 * Test for boolean equals(Object)
	 */
	public void testEqualsObject() {
		b = new StyleConfig();
		b.setId("test 1");
		b.setFilename(f);
		assertTrue(a.equals(b));
		
		b.setFilename(null);
		assertTrue(!a.equals(b));

		b.setFilename(f);
		assertTrue(a.equals(b));

		b.setDefault(true);
		assertTrue(!a.equals(b));
	}

}
