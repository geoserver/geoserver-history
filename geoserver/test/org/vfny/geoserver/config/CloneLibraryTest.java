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

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import com.vividsolutions.jts.geom.Envelope;

/**
 * CloneLibraryTest purpose.
 * <p>
 * Description of CloneLibraryTest ...
 * <p>
 * 
 * @author dzwiers, Refractions Research, Inc.
 * @version $Id: CloneLibraryTest.java,v 1.1.2.1 2003/12/31 20:05:32 dmzwiers Exp $
 */
public class CloneLibraryTest extends TestCase {

	/**
	 * Constructor for CloneLibraryTest.
	 * @param arg0
	 */
	public CloneLibraryTest(String arg0) {
		super(arg0);
	}

	/*
	 * Test for List clone(List)
	 */
	public void testCloneList() {
		List a,b;
		a = new LinkedList(); b = null;
		a.add("a");
		a.add("b");
		a.add("c");
		try{
			b = CloneLibrary.clone(a);
		}catch(CloneNotSupportedException e){
			fail(e.toString());
		}
		// requires EqualsLibrary tests to be completed
		assertTrue(EqualsLibrary.equals(a,b));
		
		a.add("d");
		assertTrue(!EqualsLibrary.equals(a,b));
		
		
		try{
			b = CloneLibrary.clone(a);
		}catch(CloneNotSupportedException e){
			fail(e.toString());
		}
		assertTrue(EqualsLibrary.equals(a,b));
		
		a.remove("d");
		a.add("d");
		assertTrue(EqualsLibrary.equals(a,b));
	}

	/*
	 * Test for Map clone(Map)
	 */
	public void testCloneMap() {
		Map a,b;
		a = new HashMap(); b = null;
		a.put("a",new Integer(0));
		a.put("b",new Integer(1));
		a.put("c",new Integer(2));
		try{
			b = CloneLibrary.clone(a);
		}catch(CloneNotSupportedException e){
			fail(e.toString());
		}
		// requires EqualsLibrary tests to be completed
		assertTrue(EqualsLibrary.equals(a,b));
		
		a.put("d",new Integer(3));
		assertTrue(!EqualsLibrary.equals(a,b));
		
		
		try{
			b = CloneLibrary.clone(a);
		}catch(CloneNotSupportedException e){
			fail(e.toString());
		}
		assertTrue(EqualsLibrary.equals(a,b));
		
		a.remove("d");
		a.put("d",new Integer(3));
		assertTrue(EqualsLibrary.equals(a,b));
	}

	/*
	 * Test for Envelope clone(Envelope)
	 */
	public void testCloneEnvelope() {
		Envelope a,b;
		a = new Envelope(1,2,3,4);
		b = CloneLibrary.clone(a);
		assertTrue(a.equals(b));
		
		a.expandToInclude(5,6);
		assertTrue(!a.equals(b));

		b = CloneLibrary.clone(a);
		assertTrue(a.equals(b));
	}

}
