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
 * ContactTest purpose.
 * <p>
 * Description of ContactTest ...
 * <p>
 * 
 * @author dzwiers, Refractions Research, Inc.
 * @version $Id: ContactTest.java,v 1.1.2.1 2003/12/30 23:39:33 dmzwiers Exp $
 */
public class ContactTest extends TestCase {

	private Contact a,b;
	/**
	 * Constructor for ContactTest.
	 * @param arg0
	 */
	public ContactTest(String arg0) {
		super(arg0);
		a = new Contact(); b = null;
		a.setAddress("101 happy lane");
		a.setAddressCity("victoria");
		a.setAddressCountry("canada");
		a.setAddressPostalCode("v8v8v8");
		a.setAddressState("bc");
		a.setAddressType("postal");
		a.setContactEmail("info@refractions.net");
		a.setContactFacsimile("555-0001");
		a.setContactOrganization("Refractions");
		a.setContactPerson("Joe");
		a.setContactPosition("Manager");
		a.setContactVoice("555-0000");
	}

	/*
	 * Test for void Contact(Contact)
	 */
	public void testContactContact() {
		//test requires equals.
		b = new Contact(a);
		assertTrue("Testing Contact(Contact)\nRelies on Contact.equals.",a.equals(b));
	}

	/*
	 * Test for Object clone()
	 */
	public void testClone() {
		//test requires equals.
		b =(Contact)a.clone();
		assertTrue("Testing Contact(Contact)\nRelies on Contact.equals.",a.equals(b));
	}

	/*
	 * Test for boolean equals(Object)
	 */
	public void testEqualsObject() {
		b = new Contact();
		b.setAddress("101 happy lane");
		b.setAddressCity("victoria");
		b.setAddressCountry("canada");
		b.setAddressPostalCode("v8v8v8");
		b.setAddressState("bc");
		b.setAddressType("postal");
		b.setContactEmail("info@refractions.net");
		b.setContactFacsimile("555-0001");
		b.setContactOrganization("Refractions");
		b.setContactPerson("Joe");
		b.setContactPosition("Manager");
		b.setContactVoice("555-0000");
		
		assertTrue(a.equals(b));

		b.setContactVoice("555-0001");
		assertTrue(!a.equals(b));

		b.setContactVoice("555-0000");
		assertTrue(a.equals(b));
	}

}
