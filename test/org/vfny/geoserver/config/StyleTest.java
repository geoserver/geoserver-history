/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.config;

import junit.framework.TestCase;
import java.io.File;


/**
 * StyleTest purpose.
 * 
 * <p>
 * Description of StyleTest ...
 * </p>
 * 
 * <p></p>
 *
 * @author dzwiers, Refractions Research, Inc.
 * @version $Id: StyleTest.java,v 1.4 2004/01/31 00:17:53 jive Exp $
 */
public class StyleTest extends TestCase {
    private StyleConfig a;
    private StyleConfig b;
    File f;

    /**
     * Constructor for StyleTest.
     *
     * @param arg0
     */
    public StyleTest(String arg0) {
        super(arg0);
        a = new StyleConfig();
        a.setId("test 1");
        f = null;

        try {
            f = new File(".");
        } catch (Exception e) {
        }

        a.setFilename(f);
    }

    /*
     * Test for void NameSpaceConfig(NameSpaceConfig)
     */
    /*public void testNameSpaceNameSpace() {
       //test requires equals.
       b = new StyleConfig(a);
       assertTrue("Testing ContactConfig(ContactConfig)\nRelies on ContactConfig.equals.",a.equals(b));
       }*/
    /*
     * Test for Object clone()
     */
    /*public void testClone() {
       //test requires equals.
       b =(StyleConfig)a.clone();
       assertTrue("Testing ContactConfig(ContactConfig)\nRelies on ContactConfig.equals.",a.equals(b));
       }*/
    /*
     * Test for boolean equals(Object)
     */
    public void testEqualsObject() {
        b = new StyleConfig();
        b.setId("test 1");
        b.setFilename(f);
        assertTrue(a.equals(b));

        b.setFilename(null);
        assertTrue(!a.equals(b));

        b.setFilename(f);
        assertTrue(a.equals(b));

        b.setDefault(true);
        assertTrue(!a.equals(b));
    }
}
