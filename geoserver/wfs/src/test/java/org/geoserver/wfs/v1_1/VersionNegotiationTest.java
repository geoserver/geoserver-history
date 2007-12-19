/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.wfs.v1_1;

import net.opengis.ows.OwsFactory;
import net.opengis.wfs.GetCapabilitiesType;
import net.opengis.wfs.WfsFactory;
import org.geoserver.wfs.CapabilitiesTransformer;
import org.geoserver.wfs.GetCapabilities;
import org.geoserver.wfs.WFSTestSupport;
import org.geotools.xml.transform.TransformerBase;


public class VersionNegotiationTest extends WFSTestSupport {
    GetCapabilities getCaps;
    WfsFactory factory;
    OwsFactory owsFactory;

    protected void setUp() throws Exception {
        super.setUp();

        getCaps = new GetCapabilities(getWFS(), getCatalog());

        factory = WfsFactory.eINSTANCE;
        owsFactory = OwsFactory.eINSTANCE;
    }

    public void test0() throws Exception {
        // test when provided and accepted match up
        GetCapabilitiesType request = factory.createGetCapabilitiesType();
        request.setService("WFS");
        request.setAcceptVersions(owsFactory.createAcceptVersionsType());
        request.getAcceptVersions().getVersion().add("1.0.0");
        request.getAcceptVersions().getVersion().add("1.1.0");

        TransformerBase tx = getCaps.run(request);
        assertTrue(tx instanceof CapabilitiesTransformer.WFS1_1);
    }

    public void test1() throws Exception {
        // test accepted only 1.0
        GetCapabilitiesType request = factory.createGetCapabilitiesType();
        request.setService("WFS");
        request.setAcceptVersions(owsFactory.createAcceptVersionsType());
        request.getAcceptVersions().getVersion().add("1.0.0");

        TransformerBase tx = getCaps.run(request);
        assertTrue(tx instanceof CapabilitiesTransformer.WFS1_0);
    }

    public void test2() throws Exception {
        // test accepted only 1.1
        GetCapabilitiesType request = factory.createGetCapabilitiesType();
        request.setService("WFS");
        request.setAcceptVersions(owsFactory.createAcceptVersionsType());
        request.getAcceptVersions().getVersion().add("1.1.0");

        TransformerBase tx = getCaps.run(request);
        assertTrue(tx instanceof CapabilitiesTransformer.WFS1_1);
    }

    public void test5() throws Exception {
        // test accepted = 0.0.0
        GetCapabilitiesType request = factory.createGetCapabilitiesType();
        request.setService("WFS");
        request.setAcceptVersions(owsFactory.createAcceptVersionsType());
        request.getAcceptVersions().getVersion().add("0.0.0");

        TransformerBase tx = getCaps.run(request);
        assertTrue(tx instanceof CapabilitiesTransformer.WFS1_0);
    }

    public void test6() throws Exception {
        // test accepted = 1.1.1
        GetCapabilitiesType request = factory.createGetCapabilitiesType();
        request.setService("WFS");
        request.setAcceptVersions(owsFactory.createAcceptVersionsType());
        request.getAcceptVersions().getVersion().add("1.1.1");

        TransformerBase tx = getCaps.run(request);
        assertTrue(tx instanceof CapabilitiesTransformer.WFS1_1);
    }

    public void test7() throws Exception {
        // test accepted = 1.0.5
        GetCapabilitiesType request = factory.createGetCapabilitiesType();
        request.setService("WFS");
        request.setAcceptVersions(owsFactory.createAcceptVersionsType());
        request.getAcceptVersions().getVersion().add("1.0.5");

        TransformerBase tx = getCaps.run(request);
        assertTrue(tx instanceof CapabilitiesTransformer.WFS1_0);
    }
}
