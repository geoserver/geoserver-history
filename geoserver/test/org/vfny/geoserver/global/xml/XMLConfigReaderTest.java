/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.global.xml;

import junit.framework.TestCase;
import org.vfny.geoserver.global.ConfigurationException;
import org.vfny.geoserver.global.dto.DataDTO;
import org.vfny.geoserver.global.dto.FeatureTypeInfoDTO;
import org.vfny.geoserver.global.dto.WFSDTO;
import org.vfny.geoserver.global.dto.WMSDTO;
import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


/**
 * XMLConfigReaderTest purpose.
 * 
 * <p>
 * Description of XMLConfigReaderTest ...
 * </p>
 * 
 * <p></p>
 *
 * @author dzwiers, Refractions Research, Inc.
 * @version $Id: XMLConfigReaderTest.java,v 1.4 2004/01/21 18:42:26 jive Exp $
 */
public class XMLConfigReaderTest extends TestCase {
    private static final String testPath1 = "C:\\Java\\workspace\\Geoserver-ModelConfig\\tests\\test1\\";
    private static final String testPath2 = "C:\\Java\\workspace\\Geoserver-ModelConfig\\tests\\test2\\";
    private File root1 = null;
    private File root2 = null;

    /**
     * Constructor for XMLConfigReaderTest.
     *
     * @param arg0
     */
    public XMLConfigReaderTest(String arg0) {
        super(arg0);
        root1 = new File(testPath1);
        root2 = new File(testPath2);
    }

    public void testLoadServices() {
        File configFile = new File(root1, "services.xml");
        XMLConfigReaderExpose cfe = new XMLConfigReaderExpose();

        try {
            cfe.loadServicesWrapper(configFile);
        } catch (ConfigurationException e) {
            fail(e.toString());
        }

        boolean r = true;

        if (r) {
            r = r && (cfe.getData() != null);
            r = r && !cfe.getData().equals(new DataDTO());
            r = r && (cfe.getWfs() != null);
            r = r && !cfe.getWfs().equals(new WFSDTO());
            r = r && (cfe.getWms() != null);
            r = r && !cfe.getWms().equals(new WMSDTO());
        }

        assertTrue(r);
    }

    public void testLoadCatalog() {
        XMLConfigReaderExpose cfe = new XMLConfigReaderExpose();

        try {
            // pass incorrect feature dir to avoid running this portion
            cfe.loadCatalogWrapper(new File(root1, "catalog.xml"),
                new File(root1, "catalog.xml"));
        } catch (ConfigurationException e) {
            fail(e.toString());
        }

        DataDTO c = cfe.getData();
        boolean r = true;
        r = r && (c != null);

        if (r) {
            r = r && (c.getDataStores() != null);
            r = r && (c.getNameSpaces() != null);
            r = r && (c.getStyles() != null);
            r = r && !c.getDataStores().equals(new HashMap());
            r = r && !c.getNameSpaces().equals(new HashMap());
            r = r && !c.getStyles().equals(new HashMap());
        }

        assertTrue(r);
    }

    public void testLoadFeatures() {
        XMLConfigReaderExpose cfe = new XMLConfigReaderExpose();

        try {
            // pass incorrect feature dir to avoid running this portion
            cfe.loadFeatureTypesWrapper(new File(root1, "featureTypes/"));
        } catch (ConfigurationException e) {
            System.out.println("***************");
            System.out.println(e.getMessage());
            System.out.println("***************");
            fail(e.toString());
        }

        Map m = cfe.getData().getFeaturesTypes();
        boolean r = true;
        r = r && (m != null);

        if (r) {
            Iterator i = m.keySet().iterator();

            while (i.hasNext() && r) {
                String key = (String) i.next();
                FeatureTypeInfoDTO f = (FeatureTypeInfoDTO) m.get(key);

                if (f == null) {
                    r = false;
                } else {
                    r = r && (key == f.getName());
                    r = r && (f.getSchemaAttributes() != null);
                }
            }
        }

        assertTrue(r);
    }

    public void testLoad() {
        XMLConfigReader m = null;

        try {
            m = new XMLConfigReader(root2);
        } catch (ConfigurationException e) {
            fail(e.toString());
        }

        boolean r = true;

        if (r) {
            r = r && (m.getData() != null);
            r = r && !m.getData().equals(new DataDTO());
            r = r && (m.getWfs() != null);
            r = r && !m.getWfs().equals(new WFSDTO());
            r = r && (m.getWms() != null);
            r = r && !m.getWms().equals(new WMSDTO());

            DataDTO c = m.getData();
            r = r && (c != null);

            if (r) {
                r = r && (c.getDataStores() != null);
                r = r && (c.getFeaturesTypes() != null);
                r = r && (c.getNameSpaces() != null);
                r = r && (c.getStyles() != null);
                r = r && !c.getDataStores().equals(new HashMap());
                r = r && !c.getFeaturesTypes().equals(new HashMap());
                r = r && !c.getNameSpaces().equals(new HashMap());
                r = r && !c.getStyles().equals(new HashMap());

                Map mp = c.getFeaturesTypes();
                r = r && (m != null);

                if (r) {
                    Iterator i = mp.keySet().iterator();

                    while (i.hasNext() && r) {
                        String key = (String) i.next();
                        FeatureTypeInfoDTO f = (FeatureTypeInfoDTO) mp.get(key);

                        if (f == null) {
                            r = false;
                        } else {
                            r = r && (key == f.getName());
                            r = r && (f.getSchemaAttributes() != null);
                        }
                    }
                }
            }
        }

        assertTrue(r);
    }
}


class XMLConfigReaderExpose extends XMLConfigReader {
    public XMLConfigReaderExpose() {
        super();
    }

    public void loadServicesWrapper(File f) throws ConfigurationException {
        loadServices(f);
    }

    public void loadCatalogWrapper(File f1, File f2)
        throws ConfigurationException {
        loadCatalog(f1, f2);
    }

    public void loadFeatureTypesWrapper(File f) throws ConfigurationException {
        loadFeatureTypes(f);
    }
}
