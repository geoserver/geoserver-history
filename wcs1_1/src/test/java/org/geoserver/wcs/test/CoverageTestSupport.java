/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.wcs.test;

import static org.custommonkey.xmlunit.XMLAssert.assertXpathEvaluatesTo;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.xml.XMLConstants;
import javax.xml.namespace.QName;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.custommonkey.xmlunit.SimpleNamespaceContext;
import org.custommonkey.xmlunit.XMLUnit;
import org.custommonkey.xmlunit.XpathEngine;
import org.geoserver.data.test.MockData;
import org.geoserver.test.ows.KvpRequestReaderTestSupport;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 * Base support class for wcs tests.
 * 
 * @author Andrea Aime, TOPP
 * 
 */
public abstract class CoverageTestSupport extends KvpRequestReaderTestSupport {
    protected static final String BASEPATH = "wcs";
 
    /**
     * @return The global wfs instance from the application context.
     */
    protected WCS getWCS() {
        return (WCS) applicationContext.getBean("wcs");
    }

    @Override
    protected void populateDataDirectory(MockData dataDirectory) throws Exception {
        dataDirectory.addWellKnownCoverageTypes();
    }
}
