/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.config;

import junit.framework.TestCase;


/**
 * FeatureTest purpose.
 * 
 * <p>
 * Description of FeatureTest ...
 * </p>
 * 
 * <p></p>
 *
 * @author dzwiers, Refractions Research, Inc.
 * @version $Id: FeatureTest.java,v 1.4 2004/01/31 00:17:53 jive Exp $
 */
public class FeatureTest extends TestCase {
    private FeatureTypeConfig a;
    private FeatureTypeConfig b;

    /**
     * Constructor for FeatureTest.
     *
     * @param arg0
     */
    public FeatureTest(String arg0) {
        super(arg0);
        a = new FeatureTypeConfig();
        b = null;
        a.setAbstract("abstract");
        a.setDataStoreId("dsId");
        a.setSRS(0);
    }

    /*
     * Test for void ContactConfig(ContactConfig)
     */
    /*public void testDataStoreDataStore() {
       //test requires equals.
       b = new FeatureTypeConfig(a);
       assertTrue("Testing ContactConfig(ContactConfig)\nRelies on ContactConfig.equals.",a.equals(b));
       }*/
    /*
     * Test for Object clone()
     */
    /*public void testClone() {
       //test requires equals.
       b =(FeatureTypeConfig)a.clone();
       assertTrue("Testing ContactConfig(ContactConfig)\nRelies on ContactConfig.equals.",a.equals(b));
       }*/
    /*
     * Test for boolean equals(Object)
     */
    public void testEqualsObject() {
        b = new FeatureTypeConfig();
        b.setAbstract("abstract");
        b.setDataStoreId("dsId");
        b.setSRS(0);

        assertTrue(a.equals(b));

        b.setSRS(5);
        assertTrue(!a.equals(b));

        a.setSRS(5);
        assertTrue(a.equals(b));
    }
}
