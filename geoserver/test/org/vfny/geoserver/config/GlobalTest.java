/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.config;

import junit.framework.TestCase;


/**
 * GlobalTest purpose.
 * 
 * <p>
 * Description of GlobalTest ...
 * </p>
 * 
 * <p></p>
 *
 * @author dzwiers, Refractions Research, Inc.
 * @version $Id: GlobalTest.java,v 1.4 2004/01/31 00:17:53 jive Exp $
 */
public class GlobalTest extends TestCase {
    private GlobalConfig a;
    private GlobalConfig b;

    /**
     * Constructor for GlobalTest.
     *
     * @param arg0
     */
    public GlobalTest(String arg0) {
        super(arg0);
        a = new GlobalConfig();
        b = null;
        a.setSchemaBaseUrl("http://www.google.ca");
        a.setMaxFeatures(0);
    }

    /*
     * Test for void ContactConfig(ContactConfig)
     */
    /*public void testDataStoreDataStore() {
       //test requires equals.
       b = new GlobalConfig(a);
       assertTrue("Testing ContactConfig(ContactConfig)\nRelies on ContactConfig.equals.",a.equals(b));
       }*/
    /*
     * Test for Object clone()
     */
    /*public void testClone() {
       //test requires equals.
       b =(GlobalConfig)a.clone();
       assertTrue("Testing ContactConfig(ContactConfig)\nRelies on ContactConfig.equals.",a.equals(b));
       }*/
    /*
     * Test for boolean equals(Object)
     */
    public void testEqualsObject() {
        b = new GlobalConfig();
        b.setSchemaBaseUrl("http://www.google.ca");
        b.setMaxFeatures(0);

        assertTrue(a.equals(b));

        b.setMaxFeatures(300);
        assertTrue(!a.equals(b));

        a.setMaxFeatures(300);
        assertTrue(a.equals(b));

        ContactConfig c = new ContactConfig();
        c.setContactPerson("Me");
        b.setContact(c);
        assertTrue(!a.equals(b));
    }
}
