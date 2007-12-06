/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wms.requests;

import com.mockrunner.mock.web.MockHttpServletRequest;
import junit.framework.TestCase;
import org.geotools.styling.Style;
import org.vfny.geoserver.Response;
import org.vfny.geoserver.ServiceException;
import org.vfny.geoserver.global.Config;
import org.vfny.geoserver.global.Data;
import org.vfny.geoserver.global.GeoServer;
import org.vfny.geoserver.global.WMS;
import org.vfny.geoserver.servlets.AbstractService;
import org.vfny.geoserver.testdata.MockUtils;
import org.vfny.geoserver.util.requests.readers.KvpRequestReader;
import org.vfny.geoserver.util.requests.readers.XmlRequestReader;
import org.vfny.geoserver.wms.servlets.GetLegendGraphic;
import org.vfny.geoserver.wms.servlets.WMService;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;


public class GetLegendGraphicKvpReaderTest extends TestCase {
    /**
     * request reader to test against, initialized by default with all
     * parameters from <code>requiredParameters</code> and
     * <code>optionalParameters</code>
     */
    GetLegendGraphicKvpReader requestReader;

    /** test values for required parameters */
    Map requiredParameters;

    /** test values for optional parameters */
    Map optionalParameters;

    /** both required and optional parameters joint up */
    Map allParameters;

    /** mock request */
    MockHttpServletRequest httpRequest;

    /** mock servlet */
    WMService dummy;

    /**
     * Remainder:
     * <ul>
     * <li>VERSION/Required
     * <li>REQUEST/Required
     * <li>LAYER/Required
     * <li>FORMAT/Required
     * <li>STYLE/Optional
     * <li>FEATURETYPE/Optional
     * <li>RULE/Optional
     * <li>SCALE/Optional
     * <li>SLD/Optional
     * <li>SLD_BODY/Optional
     * <li>WIDTH/Optional
     * <li>HEIGHT/Optional
     * <li>EXCEPTIONS/Optional
     * </ul>
     */
    protected void setUp() throws Exception {
        super.setUp();
        requiredParameters = new HashMap();
        requiredParameters.put("VERSION", "1.0.0");
        requiredParameters.put("REQUEST", "GetLegendGraphic");
        requiredParameters.put("LAYER", "cite:Ponds");
        requiredParameters.put("FORMAT", "image/png");

        optionalParameters = new HashMap();
        optionalParameters.put("STYLE", "Ponds");
        optionalParameters.put("FEATURETYPE", "fake_not_used");
        // optionalParameters.put("RULE", "testRule");
        optionalParameters.put("SCALE", "1000");
        optionalParameters.put("WIDTH", "120");
        optionalParameters.put("HEIGHT", "90");
        // ??optionalParameters.put("EXCEPTIONS", "");
        allParameters = new HashMap(requiredParameters);
        allParameters.putAll(optionalParameters);

        Data data = MockUtils.createTestCiteData(new GeoServer());
        WMS wms = new WMS(MockUtils.newWmsDto());
        wms.setData(data);

        GetLegendGraphic service = new GetLegendGraphic(wms);
        this.requestReader = new GetLegendGraphicKvpReader(allParameters,
                service);
        this.httpRequest = MockUtils.newHttpRequest(allParameters, true);

        dummy = new WMService("dummy", wms) {
                    protected Response getResponseHandler() {
                        return null;
                    }

                    protected KvpRequestReader getKvpReader(Map params) {
                        return null;
                    }

                    protected XmlRequestReader getXmlRequestReader() {
                        return null;
                    }
                };
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    // disabled this test for the moment as a 
    //	fix for http://jira.codehaus.org/browse/GEOS-710
    //	Since at the moment none of the other request do check the version numbers, we 
    //	disable this check for the moment, and wait for a proper fix once the 
    //	we support more than one version of WMS/WFS specs

    //	public void testVersion() throws Exception {
    //		requiredParameters.put("VERSION", "WRONG");
    //		
    //		this.requestReader = 
    //			new GetLegendGraphicKvpReader(requiredParameters, dummy);
    //		try {
    //			requestReader.getRequest(httpRequest);
    //			fail("Expected ServiceException due to wrong VERSION parameter");
    //		} catch (ServiceException e) {
    //			// OK
    //		}
    //		requiredParameters.put("VERSION", "1.0.0");
    //		GetLegendGraphicRequest parsedRequest;
    //		parsedRequest = (GetLegendGraphicRequest) requestReader
    //				.getRequest(httpRequest);
    //	}

    /**
     * This test ensures that when a SLD parameter has been passed that refers
     * to a SLD document with multiple styles, the required one is choosed based
     * on the LAYER parameter.
     * <p>
     * This is the case where a remote SLD document is used in "library" mode.
     * </p>
     */
    public void testRemoteSLDMultipleStyles() throws ServiceException {
        final URL remoteSldUrl = getClass()
                                     .getResource("test-data/MultipleStyles.sld");
        this.allParameters.put("SLD", remoteSldUrl.toExternalForm());

        this.allParameters.put("LAYER", "cite:Ponds");
        this.allParameters.put("STYLE", "Ponds");
        requestReader = new GetLegendGraphicKvpReader(this.allParameters, dummy);

        GetLegendGraphicRequest request = (GetLegendGraphicRequest) requestReader
            .getRequest(httpRequest);

        //the style names Ponds is declared in third position on the sld doc
        Style selectedStyle = request.getStyle();
        assertNotNull(selectedStyle);
        assertEquals("Ponds", selectedStyle.getName());

        this.allParameters.put("LAYER", "cite:Lakes");
        this.allParameters.put("STYLE", "Lakes");
        requestReader = new GetLegendGraphicKvpReader(this.allParameters, dummy);
        request = (GetLegendGraphicRequest) requestReader.getRequest(httpRequest);

        //the style names Ponds is declared in third position on the sld doc
        selectedStyle = request.getStyle();
        assertNotNull(selectedStyle);
        assertEquals("Lakes", selectedStyle.getName());
    }
}
