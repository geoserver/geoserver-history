/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.config;

import junit.framework.TestCase;


/**
 * NameSpaceTest purpose.
 * 
 * <p>
 * Description of NameSpaceTest ...
 * </p>
 * 
 * <p></p>
 *
 * @author dzwiers, Refractions Research, Inc.
 * @version $Id: NameSpaceTest.java,v 1.3 2004/01/21 18:42:26 jive Exp $
 */
public class NameSpaceTest extends TestCase {
    private NameSpaceConfig a;
    private NameSpaceConfig b;

    /**
     * Constructor for NameSpaceTest.
     *
     * @param arg0
     */
    public NameSpaceTest(String arg0) {
        super(arg0);
        a = new NameSpaceConfig();
        a.setPrefix(":");
        a.setUri("http://www.google.ca");
    }

    /*
     * Test for void NameSpaceConfig(NameSpaceConfig)
     */
    /*public void testNameSpaceNameSpace() {
       //test requires equals.
       b = new NameSpaceConfig(a);
       assertTrue("Testing ContactConfig(ContactConfig)\nRelies on ContactConfig.equals.",a.equals(b));
       }*/
    /*
     * Test for Object clone()
     */
    /*public void testClone() {
       //test requires equals.
       b =(NameSpaceConfig)a.clone();
       assertTrue("Testing ContactConfig(ContactConfig)\nRelies on ContactConfig.equals.",a.equals(b));
       }*/
    /*
     * Test for boolean equals(Object)
     */
    public void testEqualsObject() {
        b = new NameSpaceConfig();
        b.setPrefix(":");
        b.setUri("http://www.google.ca");
        assertTrue(a.equals(b));

        b.setDefault(true);
        assertTrue(!a.equals(b));

        b.setDefault(false);
        b.setPrefix(".");
        assertTrue(!a.equals(b));
    }
}
