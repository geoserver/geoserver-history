package org.geoserver.gss;

import static org.custommonkey.xmlunit.XMLAssert.*;

import org.w3c.dom.Document;

import com.mockrunner.mock.web.MockHttpServletResponse;

public class GetCentralRevisionTest extends GSSTestSupport {

    public void testCentralRevisionOneLayer() throws Exception {
        MockHttpServletResponse response = getAsServletResponse(root() + "service=gss&request=GetCentralRevision&typeName=topp:restricted");
        validate(response);
        Document doc = dom(response);
        // print(doc);
        
        assertXpathEvaluatesTo("1", "count(//gss:CentralRevisions/gss:LayerRevision)", doc);
    }
    
    public void testCentralRevisionUnknownLayer() throws Exception {
        MockHttpServletResponse response = getAsServletResponse(root() + "service=gss&request=GetCentralRevision&typeName=topp:missing");
        validate(response);
        Document doc = dom(response);
        // print(doc);
        checkOwsException(doc);
    }
}
