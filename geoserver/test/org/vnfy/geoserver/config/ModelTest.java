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
import org.vnfy.geoserver.config.data.*;

/**
 * ModelTest purpose.
 * <p>
 * Description of ModelTest ...
 * <p>
 * 
 * @author dzwiers, Refractions Research, Inc.
 * @version $Id: ModelTest.java,v 1.1.2.2 2003/12/31 00:35:06 dmzwiers Exp $
 */
public class ModelTest extends TestCase {

	private Model a,b;
	/**
	 * Constructor for ModelTest.
	 * @param arg0
	 */
	public ModelTest(String arg0) {
		super(arg0);
		a = new Model(); b = null;
		Global g = new Global();
			g.setSchemaBaseUrl("http://www.google.ca");
		g.setMaxFeatures(0);
		a.setGlobal(g);
	}

	/*
	 * Test for void Contact(Contact)
	 */
	public void testDataStoreDataStore() {
		//test requires equals.
		b = new Model(a);
		assertTrue("Testing Contact(Contact)\nRelies on Contact.equals.",a.equals(b));
	}

	/*
	 * Test for Object clone()
	 */
	public void testClone() {
		//test requires equals.
		b =(Model)a.clone();
		assertTrue("Testing Contact(Contact)\nRelies on Contact.equals.",a.equals(b));
	}

	/*
	 * Test for boolean equals(Object)
	 */
	public void testEqualsObject() {
		b = new Model();
		Global g = new Global();
			g.setSchemaBaseUrl("http://www.google.ca");
		g.setMaxFeatures(0);
		b.setGlobal(g);
		
		assertTrue(a.getGlobal().equals(b.getGlobal()));
		assertTrue(a.equals(b));
		
		Catalog c = new Catalog();
		c.addNameSpace("c",new NameSpace());
		b.setCatalog(c);
		assertTrue(!a.equals(b));
	}

}
