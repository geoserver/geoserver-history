/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.config;

import junit.framework.TestCase;


/**
 * ServiceTest purpose.
 * 
 * <p>
 * Description of ServiceTest ...
 * </p>
 * 
 * <p></p>
 *
 * @author dzwiers, Refractions Research, Inc.
 * @version $Id: ServiceTest.java,v 1.4 2004/01/31 00:17:53 jive Exp $
 */
public class ServiceTest extends TestCase {
    private ServiceConfig a;
    private ServiceConfig b;

    /**
     * Constructor for ServiceTest.
     *
     * @param arg0
     */
    public ServiceTest(String arg0) {
        super(arg0);
        a = new ServiceConfig();
        a.setName("test 1");
        a.setMaintainer("tester 1");
    }

    /*
     * Test for void NameSpaceConfig(NameSpaceConfig)
     */
    /*public void testNameSpaceNameSpace() {
       //test requires equals.
       b = new ServiceConfig(a);
       assertTrue("Testing ContactConfig(ContactConfig)\nRelies on ContactConfig.equals.",a.equals(b));
       }*/
    /*
     * Test for Object clone()
     */
    /*public void testClone() {
       //test requires equals.
       b =(ServiceConfig)a.clone();
       assertTrue("Testing ContactConfig(ContactConfig)\nRelies on ContactConfig.equals.",a.equals(b));
       }*/
    /*
     * Test for boolean equals(Object)
     */
    public void testEqualsObject() {
        b = new ServiceConfig();
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
