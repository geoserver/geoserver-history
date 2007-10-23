/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.wfs.v1_1;

import net.opengis.wfs.GetCapabilitiesType;
import net.opengis.wfs.GetFeatureType;
import net.opengis.wfs.WfsFactory;
import org.geoserver.platform.Operation;
import org.geoserver.platform.Service;
import org.geoserver.util.ErrorHandler;
import org.geoserver.util.ReaderUtils;
import org.geoserver.wfs.CapabilitiesTransformer;
import org.geoserver.wfs.WFSTestSupport;
import org.geoserver.wfs.xml.v1_1_0.WFS;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;


public class CapabilitiesTransformerTest extends WFSTestSupport {
    static Logger logger = Logger.getLogger("org.geoserver.wfs.test");

    GetCapabilitiesType request() {
        GetCapabilitiesType type = WfsFactory.eINSTANCE.createGetCapabilitiesType();
        type.setBaseUrl("http://localhost:8080/geoserver");

        return type;
    }

    public void test() throws Exception {
        CapabilitiesTransformer tx = new CapabilitiesTransformer.WFS1_1(getWFS(), getCatalog());
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        tx.transform(request(), output);

        InputStreamReader reader = new InputStreamReader(new ByteArrayInputStream(
                    output.toByteArray()));

        ErrorHandler handler = new ErrorHandler(logger, Level.WARNING);
        ReaderUtils.validate(reader, handler, WFS.NAMESPACE,
            "http://schemas.opengis.net/wfs/1.1.0/wfs.xsd");

        assertTrue(handler.errors.isEmpty());
    }
}
