/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.global.xml;

import junit.framework.TestCase;
import org.vfny.geoserver.global.ConfigurationException;
import org.vfny.geoserver.global.dto.GeoServerDTO;
import org.vfny.geoserver.global.dto.WFSDTO;
import org.vfny.geoserver.global.dto.WMSDTO;
import java.io.File;


/**
 * XMLConfigWriterTest purpose.
 * 
 * <p>
 * Description of XMLConfigWriterTest ...
 * </p>
 * 
 * <p></p>
 *
 * @author dzwiers, Refractions Research, Inc.
 * @version $Id: XMLConfigWriterTest.java,v 1.3 2004/01/21 18:42:26 jive Exp $
 */
public class XMLConfigWriterTest extends TestCase {
    private static final String testPath1 = "C:/Java/workspace/Geoserver-ModelConfig/tests/test3/";
    private static final String testPath2 = "C:/Java/workspace/Geoserver-ModelConfig/tests/test2/";
    private static final String testPath3 = "C:/Java/workspace/Geoserver-ModelConfig/tests/test4/";
    private File root1 = null;
    private File root2 = null;
    private File root3 = null;

    /**
     * Constructor for XMLConfigWriterTest.
     *
     * @param arg0
     */
    public XMLConfigWriterTest(String arg0) {
        super(arg0);

        try {
            root1 = new File(testPath1);
            root2 = new File(testPath2);
            root3 = new File(testPath3);
        } catch (Exception e) {
        }
    }

    public void testStoreBlank() {
        try {
            XMLConfigWriter.store(new WMSDTO(), new WFSDTO(),
                new GeoServerDTO(), root1);
        } catch (ConfigurationException e) {
            fail(e.toString());
        }
    }

    public void testRoundTrip() {
        try {
            XMLConfigReader cr = new XMLConfigReader(root2);
            XMLConfigWriter.store(cr.getWms(), cr.getWfs(), cr.getGeoServer(),
                cr.getData(), root3);
        } catch (ConfigurationException e) {
            fail(e.toString());
        }
    }
}
