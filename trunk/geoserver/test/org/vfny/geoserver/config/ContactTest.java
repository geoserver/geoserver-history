/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.config;

import junit.framework.TestCase;


/**
 * ContactTest purpose.
 * 
 * <p>
 * Description of ContactTest ...
 * </p>
 * 
 * <p></p>
 *
 * @author dzwiers, Refractions Research, Inc.
 * @version $Id: ContactTest.java,v 1.4 2004/01/31 00:17:53 jive Exp $
 */
public class ContactTest extends TestCase {
    private ContactConfig a;
    private ContactConfig b;

    /**
     * Constructor for ContactTest.
     *
     * @param arg0
     */
    public ContactTest(String arg0) {
        super(arg0);
        a = new ContactConfig();
        b = null;
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
     * Test for void ContactConfig(ContactConfig)
     */
    /*public void testContactContact() {
       //test requires equals.
       b = new ContactConfig(a);
       assertTrue("Testing ContactConfig(ContactConfig)\nRelies on ContactConfig.equals.",a.equals(b));
       }*/
    /*
     * Test for Object clone()
     */
    /*public void testClone() {
       //test requires equals.
       b =(ContactConfig)a.clone();
       assertTrue("Testing ContactConfig(ContactConfig)\nRelies on ContactConfig.equals.",a.equals(b));
       }*/
    /*
     * Test for boolean equals(Object)
     */
    public void testEqualsObject() {
        b = new ContactConfig();
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
