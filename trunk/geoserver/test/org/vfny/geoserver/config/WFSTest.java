/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.config;

import junit.framework.TestCase;


/**
 * WFSTest purpose.
 * 
 * <p>
 * Description of WFSTest ...
 * </p>
 * 
 * <p></p>
 *
 * @author dzwiers, Refractions Research, Inc.
 * @version $Id: WFSTest.java,v 1.4 2004/01/31 00:17:53 jive Exp $
 */
public class WFSTest extends TestCase {
    private WFSConfig a;
    private WFSConfig b;

    /**
     * Constructor for WFSTest.
     *
     * @param arg0
     */
    public WFSTest(String arg0) {
        super(arg0);
        a = new WFSConfig();
    }

    /*
     * Test for void NameSpaceConfig(NameSpaceConfig)
     */
    /*public void testNameSpaceNameSpace() {
       //test requires equals.
       b = new WFSConfig(a);
       assertTrue("Testing ContactConfig(ContactConfig)\nRelies on ContactConfig.equals.",a.equals(b));
       }*/
    /*
     * Test for Object clone()
     */
    /*public void testClone() {
       //test requires equals.
       b =(WFSConfig)a.clone();
       assertTrue("Testing ContactConfig(ContactConfig)\nRelies on ContactConfig.equals.",a.equals(b));
       }*/
}
