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
package org.vnfy.geoserver.config.catalog;

import java.io.File;

import junit.framework.TestCase;
/**
 * StyleTest purpose.
 * <p>
 * Description of StyleTest ...
 * <p>
 * 
 * @author dzwiers, Refractions Research, Inc.
 * @version $Id: StyleTest.java,v 1.1.2.1 2003/12/30 23:39:23 dmzwiers Exp $
 */
public class StyleTest extends TestCase {

	private Style a,b;
	File f;
	/**
	 * Constructor for StyleTest.
	 * @param arg0
	 */
	public StyleTest(String arg0) {
		super(arg0);
		a = new Style();
		a.setId("test 1");
		f = null;
		try{
			f = new File(".");
		}catch(Exception e){}
		a.setFilename(f);
	}

	/*
	 * Test for void NameSpace(NameSpace)
	 */
	public void testNameSpaceNameSpace() {
		//test requires equals.
		b = new Style(a);
		assertTrue("Testing Contact(Contact)\nRelies on Contact.equals.",a.equals(b));
	}

	/*
	 * Test for Object clone()
	 */
	public void testClone() {
		//test requires equals.
		b =(Style)a.clone();
		assertTrue("Testing Contact(Contact)\nRelies on Contact.equals.",a.equals(b));
	}

	/*
	 * Test for boolean equals(Object)
	 */
	public void testEqualsObject() {
		b = new Style();
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
