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
 * ServiceTest purpose.
 * <p>
 * Description of ServiceTest ...
 * <p>
 * 
 * @author dzwiers, Refractions Research, Inc.
 * @version $Id: ServiceTest.java,v 1.1.2.1 2003/12/30 23:39:32 dmzwiers Exp $
 */
public class ServiceTest extends TestCase {

	private Service a,b;
	
	/**
	 * Constructor for ServiceTest.
	 * @param arg0
	 */
	public ServiceTest(String arg0) {
		super(arg0);
		a = new Service();
		a.setName("test 1");
		a.setMaintainer("tester 1");
	}

	/*
	 * Test for void NameSpace(NameSpace)
	 */
	public void testNameSpaceNameSpace() {
		//test requires equals.
		b = new Service(a);
		assertTrue("Testing Contact(Contact)\nRelies on Contact.equals.",a.equals(b));
	}

	/*
	 * Test for Object clone()
	 */
	public void testClone() {
		//test requires equals.
		b =(Service)a.clone();
		assertTrue("Testing Contact(Contact)\nRelies on Contact.equals.",a.equals(b));
	}

	/*
	 * Test for boolean equals(Object)
	 */
	public void testEqualsObject() {
		b = new Service();
		b.setName("test 1");
		b.setMaintainer("tester 1");
		assertTrue(a.equals(b));
		
		b.setName("test 2");
		assertTrue(!a.equals(b));
		
		b.setName("test 1");
		b.setMaintainer("tester 2");
		assertTrue(!a.equals(b));
	}
}
