/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */

package org.geoserver.xacml.geoxacml;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.acegisecurity.Authentication;
import org.acegisecurity.GrantedAuthority;
import org.acegisecurity.GrantedAuthorityImpl;
import org.acegisecurity.context.SecurityContextHolder;
import org.acegisecurity.providers.AuthenticationProvider;
import org.acegisecurity.providers.ProviderManager;
import org.acegisecurity.providers.TestingAuthenticationProvider;
import org.acegisecurity.providers.TestingAuthenticationToken;
import org.geoserver.catalog.WorkspaceInfo;
import org.geoserver.platform.GeoServerExtensions;
import org.geoserver.security.AccessMode;
import org.geoserver.test.GeoServerTestSupport;
import org.geoserver.xacml.request.WorkspaceRequestCtxBuilder;
import org.geoserver.xacml.role.Role;
import org.geotools.xacml.transport.XACMLHttpTransport;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.sun.xacml.ctx.RequestCtx;
import com.sun.xacml.ctx.ResponseCtx;
import com.sun.xacml.ctx.Result;

public class GeoXACMLPDPControllerTest extends GeoServerTestSupport {

    //private String pdpURL = "http://localhost:8080/geoserver/security/geoxacml";
    private String pdpURL = null;
    
    @Override
    protected void setUpInternal() throws Exception {
        super.setUpInternal();
        
        ProviderManager providerManager = (ProviderManager) GeoServerExtensions.bean("authenticationManager");
        List<AuthenticationProvider> list = new ArrayList<AuthenticationProvider>();
        list.add(new TestingAuthenticationProvider());
        providerManager.setProviders(list);
        
        Authentication admin = new TestingAuthenticationToken("admin", "geoserver",
                new GrantedAuthority[] { new GrantedAuthorityImpl("ROLE_ADMINISTRATOR") });
        // Authentication anonymous = new TestingAuthenticationToken("anonymous", null, null);
        SecurityContextHolder.getContext().setAuthentication(admin);


    }

    public void testDirExists() throws Exception {
        File dir = new File(testData.getDataDirectoryRoot(), DataDirPolicyFinderModlule.BASE_DIR);
        assertTrue(dir.exists());

    }

    public void testRemote() throws Exception {
        
        List<RequestCtx> requestCtxs = createRequestCtxList(); 

        for (RequestCtx requestCtx : requestCtxs) {
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                requestCtx.encode(out);
                InputStream resp = post("security/geoxacml", out.toString());
                checkXACMLRepsonse(resp, "Permit");
        }
    }
    
    public void testURLTransport() {
        
        if (pdpURL==null) return;
        URL url=null;
        try {
            url = new URL(pdpURL);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        
        XACMLHttpTransport transport = new XACMLHttpTransport(url,false);
        
        List<RequestCtx> requestCtxs = createRequestCtxList();
        if (requestCtxs.isEmpty()==false) {
            ResponseCtx responseCtx=transport.evaluateRequestCtx(requestCtxs.get(0));
            assertTrue(responseCtx.getResults().iterator().next().getDecision()==Result.DECISION_PERMIT);
        }
        
        List<ResponseCtx> responseCtxs = transport.evaluateRequestCtxList(requestCtxs);
        for (ResponseCtx responseCtx : responseCtxs) {
            assertTrue(responseCtx.getResults().iterator().next().getDecision()==Result.DECISION_PERMIT);
        }                
    }
    
    public void testURLTransportMultiThreaded() {
        
        if (pdpURL==null) return;
        URL url=null;
        try {
            url = new URL(pdpURL);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        
        XACMLHttpTransport transport = new XACMLHttpTransport(url,true);
        
        List<RequestCtx> requestCtxs = createRequestCtxList();
        if (requestCtxs.isEmpty()==false) {
            ResponseCtx responseCtx=transport.evaluateRequestCtx(requestCtxs.get(0));
            assertTrue(responseCtx.getResults().iterator().next().getDecision()==Result.DECISION_PERMIT);
        }
        
        List<RequestCtx> requestCtxs2 =new ArrayList<RequestCtx>();
        for (int i=0;i<10;i++) {
            requestCtxs2.addAll(requestCtxs);
        }
        List<ResponseCtx> responseCtxs = transport.evaluateRequestCtxList(requestCtxs2);
        for (ResponseCtx responseCtx : responseCtxs) {
            assertTrue(responseCtx.getResults().iterator().next().getDecision()==Result.DECISION_PERMIT);
        }                
    }

    private List<RequestCtx> createRequestCtxList() {
        List<RequestCtx> result = new ArrayList<RequestCtx>();
        for (WorkspaceInfo wsInfo : getCatalog().getWorkspaces()) {
            Set<String> roleStrings = GeoXACMLConfig.getRoleAssignmentAuthority().getRoleIdsFor(
                    SecurityContextHolder.getContext().getAuthentication());
            for (String roleString : roleStrings) {
                Role role = new Role(roleString);
                WorkspaceRequestCtxBuilder b = new WorkspaceRequestCtxBuilder(role, wsInfo,
                        AccessMode.READ);
                RequestCtx rctx = b.createRequestCtx();
                result.add(rctx);
            }
        }
        return result;
        
    }
    
    protected void checkXACMLRepsonse(InputStream resp, String decision) throws Exception {
        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document doc = builder.parse(resp);
        Node decisionNode = doc.getElementsByTagName("Decision").item(0);
        assertEquals(decision, decisionNode.getTextContent());
        Node statusNode = doc.getElementsByTagName("StatusCode").item(0);
        String statusCode = statusNode.getAttributes().getNamedItem("Value").getTextContent();
        assertEquals("urn:oasis:names:tc:xacml:1.0:status:ok", statusCode);

    }

    protected void dumpResponse(InputStream resp) throws IOException {
        System.out.println("RESPONSE");
        byte[] bytes = new byte[512];
        while (resp.read(bytes) != -1) {
            System.out.println(new String(bytes));
        }
    }

}
