/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.config;

import junit.framework.TestCase;
import java.util.HashMap;
import java.util.Map;


/**
 * DataStoreTest purpose.
 * 
 * <p>
 * Description of DataStoreTest ...
 * </p>
 * 
 * <p></p>
 *
 * @author dzwiers, Refractions Research, Inc.
 * @version $Id: DataStoreTest.java,v 1.3 2004/01/21 18:42:26 jive Exp $
 */
public class DataStoreTest extends TestCase {
    private DataStoreConfig a;
    private DataStoreConfig b;

    /**
     * Constructor for DataStoreTest.
     *
     * @param arg0
     */
    public DataStoreTest(String arg0) {
        super(arg0);

        //		a = new DataStoreConfig(); b = null;
        a.setAbstract("abstract");
        a.setEnabled(true);

        //		a.setId("id");
        a.setNameSpaceId("prefix:");
        a.setTitle("title");

        Map params = new HashMap();
        params.put("1", new Integer(1));
        params.put("2", new Integer(2));
        params.put("3", new Integer(3));
        a.setConnectionParams(params);
    }

    /*
     * Test for void ContactConfig(ContactConfig)
     */
    /*public void testDataStoreDataStore() {
       //test requires equals.
       b = new DataStoreConfig(a);
       assertTrue("Testing ContactConfig(ContactConfig)\nRelies on ContactConfig.equals.",a.equals(b));
       }*/
    /*
     * Test for Object clone()
     */
    /*public void testClone() {
       //test requires equals.
       b =(DataStoreConfig)a.clone();
       assertTrue("Testing ContactConfig(ContactConfig)\nRelies on ContactConfig.equals.",a.equals(b));
       }*/
    /*
     * Test for boolean equals(Object)
     */
    public void testEqualsObject() {
        //		b = new DataStoreConfig();
        b.setAbstract("abstract");
        b.setEnabled(true);

        //		b.setId("id");
        b.setNameSpaceId("prefix:");
        b.setTitle("title");

        Map params = new HashMap();
        params.put("1", new Integer(1));
        params.put("2", new Integer(2));
        params.put("3", new Integer(3));
        b.setConnectionParams(params);

        assertTrue(a.equals(b));

        params.put("4", new Integer(4)); //should only store ref.
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
