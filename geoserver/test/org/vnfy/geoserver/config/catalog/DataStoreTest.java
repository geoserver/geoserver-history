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

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

/**
 * DataStoreTest purpose.
 * <p>
 * Description of DataStoreTest ...
 * <p>
 * 
 * @author dzwiers, Refractions Research, Inc.
 * @version $Id: DataStoreTest.java,v 1.1.2.1 2003/12/30 23:39:23 dmzwiers Exp $
 */
public class DataStoreTest extends TestCase {

	/**
	 * Constructor for DataStoreTest.
	 * @param arg0
	 */
	public DataStoreTest(String arg0) {
		super(arg0);
		a = new DataStore(); b = null;
		a.setAbstract("abstract");
		a.setEnabled(true);
		a.setId("id");
		a.setNameSpaceId("prefix:");
		a.setTitle("title");
		Map params = new HashMap();
		params.put("1",new Integer(1));
		params.put("2",new Integer(2));
		params.put("3",new Integer(3));
		a.setConnectionParams(params);
	}


	private DataStore a,b;

	/*
	 * Test for void Contact(Contact)
	 */
	public void testDataStoreDataStore() {
		//test requires equals.
		b = new DataStore(a);
		assertTrue("Testing Contact(Contact)\nRelies on Contact.equals.",a.equals(b));
	}

	/*
	 * Test for Object clone()
	 */
	public void testClone() {
		//test requires equals.
		b =(DataStore)a.clone();
		assertTrue("Testing Contact(Contact)\nRelies on Contact.equals.",a.equals(b));
	}

	/*
	 * Test for boolean equals(Object)
	 */
	public void testEqualsObject() {
		b = new DataStore();
		b.setAbstract("abstract");
		b.setEnabled(true);
		b.setId("id");
		b.setNameSpaceId("prefix:");
		b.setTitle("title");
		Map params = new HashMap();
		params.put("1",new Integer(1));
		params.put("2",new Integer(2));
		params.put("3",new Integer(3));
		b.setConnectionParams(params);
		
		assertTrue(a.equals(b));
		
		params.put("4",new Integer(4)); //should only store ref.
		assertTrue(!a.equals(b));
		
		params.remove("4");
		b.setConnectionParams(params);
		assertTrue(a.equals(b));
		
		b.setEnabled(false);
		assertTrue(!a.equals(b));
		b.setEnabled(true);
		assertTrue(a.equals(b));
	}
}
