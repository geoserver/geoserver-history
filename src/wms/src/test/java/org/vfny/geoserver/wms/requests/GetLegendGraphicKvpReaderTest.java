/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wms.requests;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import junit.framework.Test;

import org.geoserver.platform.ServiceException;
import org.geoserver.wms.WMS;
import org.geoserver.wms.WMSTestSupport;
import org.geotools.styling.Style;
import org.vfny.geoserver.wms.servlets.GetLegendGraphic;

import com.mockrunner.mock.web.MockHttpServletRequest;


public class GetLegendGraphicKvpReaderTest extends WMSTestSupport {
    /**
     * request reader to test against, initialized by default with all
     * parameters from <code>requiredParameters</code> and
     * <code>optionalParameters</code>
     */
    GetLegendGraphicKvpReader requestReader;

    /** test values for required parameters */
    Map<String, String> requiredParameters;

    /** test values for optional parameters */
    Map<String, String> optionalParameters;

    /** both required and optional parameters joint up */
    Map<String, String> allParameters;

    /** mock request */
    MockHttpServletRequest httpRequest;

    /** mock config object */
    WMS wms;
    
    /**
     * This is a READ ONLY TEST so we can use one time setup
     */
    public static Test suite() {
        return new OneTimeTestSetup(new GetLegendGraphicKvpReaderTest());
    }
    

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
    protected void setUpInternal() throws Exception {
        super.setUpInternal();
        requiredParameters = new HashMap<String, String>();
        requiredParameters.put("VERSION", "1.0.0");
        requiredParameters.put("REQUEST", "GetLegendGraphic");
        requiredParameters.put("LAYER", "cite:Ponds");
        requiredParameters.put("FORMAT", "image/png");

        optionalParameters = new HashMap<String, String>();
        optionalParameters.put("STYLE", "Ponds");
        optionalParameters.put("FEATURETYPE", "fake_not_used");
        // optionalParameters.put("RULE", "testRule");
        optionalParameters.put("SCALE", "1000");
        optionalParameters.put("WIDTH", "120");
        optionalParameters.put("HEIGHT", "90");
        // ??optionalParameters.put("EXCEPTIONS", "");
        allParameters = new HashMap<String, String>(requiredParameters);
        allParameters.putAll(optionalParameters);

        wms = getWMS();

        GetLegendGraphic service = new GetLegendGraphic(wms);
        this.requestReader = new GetLegendGraphicKvpReader(allParameters, wms);
        this.httpRequest = createRequest("wms", allParameters);
    }

    /**
     * This test ensures that when a SLD parameter has been passed that refers
     * to a SLD document with multiple styles, the required one is choosed based
     * on the LAYER parameter.
     * <p>
     * This is the case where a remote SLD document is used in "library" mode.
     * </p>
     */
    public void testRemoteSLDMultipleStyles() throws ServiceException {
        final URL remoteSldUrl = getClass().getResource("MultipleStyles.sld");
        this.allParameters.put("SLD", remoteSldUrl.toExternalForm());

        this.allParameters.put("LAYER", "cite:Ponds");
        this.allParameters.put("STYLE", "Ponds");
        requestReader = new GetLegendGraphicKvpReader(this.allParameters, wms);

        GetLegendGraphicRequest request = (GetLegendGraphicRequest) requestReader.getRequest(httpRequest);

        //the style names Ponds is declared in third position on the sld doc
        Style selectedStyle = request.getStyle();
        assertNotNull(selectedStyle);
        assertEquals("Ponds", selectedStyle.getName());

        this.allParameters.put("LAYER", "cite:Lakes");
        this.allParameters.put("STYLE", "Lakes");
        requestReader = new GetLegendGraphicKvpReader(this.allParameters, wms);
        request = (GetLegendGraphicRequest) requestReader.getRequest(httpRequest);

        //the style names Ponds is declared in third position on the sld doc
        selectedStyle = request.getStyle();
        assertNotNull(selectedStyle);
        assertEquals("Lakes", selectedStyle.getName());
    }
    
    public void testMissingLayerParameter(){
        requiredParameters.remove("LAYER");
        requestReader = new GetLegendGraphicKvpReader(requiredParameters, wms);
        try{
            requestReader.getRequest(httpRequest);
            fail("Expected ServiceException");
        }catch(ServiceException e){
            assertEquals("LayerNotDefined", e.getCode());
        }
    }

    public void testMissingFormatParameter(){
        requiredParameters.remove("FORMAT");
        requestReader = new GetLegendGraphicKvpReader(requiredParameters, wms);
        try{
            requestReader.getRequest(httpRequest);
            fail("Expected ServiceException");
        }catch(ServiceException e){
            assertEquals("MissingFormat", e.getCode());
        }
    }


    public void testStrictParameter() {
        GetLegendGraphicRequest request;
        
        //default value
        requestReader = new GetLegendGraphicKvpReader(allParameters, wms);
        request = (GetLegendGraphicRequest) requestReader.getRequest(httpRequest);
        assertTrue(request.isStrict());

        allParameters.put("STRICT", "false");
        allParameters.remove("LAYER");
        requestReader = new GetLegendGraphicKvpReader(allParameters, wms);
        request = (GetLegendGraphicRequest) requestReader.getRequest(httpRequest);
        assertFalse(request.isStrict());
    }
}
