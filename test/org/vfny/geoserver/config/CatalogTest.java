/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.config;

import junit.framework.TestCase;


/**
 * CatalogTest purpose.
 * 
 * <p>
 * Description of CatalogTest ...
 * </p>
 * 
 * <p></p>
 *
 * @author dzwiers, Refractions Research, Inc.
 * @version $Id: CatalogTest.java,v 1.3 2004/01/21 18:42:26 jive Exp $
 */
public class CatalogTest extends TestCase {
    private DataConfig a;
    private DataConfig b;

    /**
     * Constructor for CatalogTest.
     *
     * @param arg0
     */
    public CatalogTest(String arg0) {
        super(arg0);

        a = new DataConfig();

        //		a.addDataStore("a",new DataStoreConfig());
        //		a.addDataStore("b",new DataStoreConfig());
        //		a.addDataStore("c",new DataStoreConfig());
        //		a.addFeatureType("a",new FeatureTypeConfig());
        //		a.addFeatureType("b",new FeatureTypeConfig());
        //		a.addFeatureType("c",new FeatureTypeConfig());
        //		a.addNameSpace("a",new NameSpaceConfig());
        //		a.addNameSpace("b",new NameSpaceConfig());
        //		a.addNameSpace("c",new NameSpaceConfig());
        //		a.getNameSpace("a").setDefault(true);
        //		a.setDefaultNameSpace(a.getNameSpace("a"));
        //		a.addStyle("a",new StyleConfig());
        //		a.addStyle("b",new StyleConfig());
        //		a.addStyle("c",new StyleConfig());
    }

    /*
     * Test for void Data(Data)
     */
    /*public void testCatalogCatalog() {
       b = new DataConfig(a);
       assertTrue("Testing GlobalCatalog(GlobalCatalog)\nRelies on GlobalCatalog.equals.",a.equals(b));
       }*/
    /*
     * Test for Object clone()
     */
    /*public void testClone() {
       b = (DataConfig)a.clone();
       assertTrue("Testing clone()\nRelies on GlobalCatalog.equals.",a.equals(b));
       }*/
}
