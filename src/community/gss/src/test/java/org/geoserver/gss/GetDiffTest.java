package org.geoserver.gss;

import org.w3c.dom.Document;

import com.mockrunner.mock.web.MockHttpServletResponse;

public class GetDiffTest extends GSSTestSupport {

    /**
     * Tests that we asked for a out of order GetDiff: there has not been any PostDiff before it
     */
    public void testGetDiffOutOfOrder() throws Exception {
        MockHttpServletResponse response = postAsServletResponse(root(true), loadTextResource("GetDiffInitial.xml"));
        validate(response);
        Document dom = dom(response);
        // print(dom);
        checkOws10Exception(dom, "InvalidParameterValue", "fromVersion");
    }
    
    /**
     * Checks we properly report back the layer is not known to the service
     * @throws Exception
     */
    public void testUnknownLayer() throws Exception {
        MockHttpServletResponse response = postAsServletResponse(root(true),
                loadTextResource("GetDiffUnknown.xml"));
        validate(response);
        Document dom = dom(response);
        // print(dom);
        checkOws10Exception(dom, "InvalidParameterValue", "typeName");
    }
    
    public void testFirstDiff() {
        
    }
}
