/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */


package org.geoserver.xacml.geoxacml;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

public class GeoXACMLPDPControllerTest extends XACMLTestSupport {

    
    @Override
    protected void setUpInternal() throws Exception {
        super.setUpInternal();
        geoserverDataDirFromTest();
                                                        
    }
    
    public void testDirExists() throws Exception {
        File dir = new File( testData.getDataDirectoryRoot(), DataDirPolicyFinderModlule.BASE_DIR );
        assertTrue( dir.exists() );        
        
    }
    
    public void testAlice() throws Exception {
        File request = new File ("src/test/resources/requests/misc/requestAlice.xml");
        String xml = getXMLRequest(request);
        //InputStream resp = post("geoxacml?validate=true",xml);
        InputStream resp = post("security/geoxacml",xml);
        checkXACMLRepsonse(resp, "Permit");        
    }
    
    public void testBob() throws Exception {
        File request = new File ("src/test/resources/requests/misc/requestBob.xml");
        String xml = getXMLRequest(request);
        //InputStream resp = post("geoxacml?validate=true",xml);
        InputStream resp = post("security/geoxacml",xml);
        checkXACMLRepsonse(resp, "Deny");
        //dumpResponse(resp);
    }

    public void testEmployee() throws Exception{
        File request = new File ("src/test/resources/requests/rbac/EmployeeRequest.xml");
        String xml = getXMLRequest(request);
        InputStream resp = post("security/geoxacml",xml);
        checkXACMLRepsonse(resp, "Permit");                
    }

    public void testIsEmployee1() throws Exception {
        File request = new File ("src/test/resources/requests/rbac/EmployeeRequest1.xml");
        String xml = getXMLRequest(request);
        InputStream resp = post("security/geoxacml",xml);
        checkXACMLRepsonse(resp, "NotApplicable");                
    }

    public void testIsEmployee2() throws Exception {
        File request = new File ("src/test/resources/requests/rbac/EmployeeRequest2.xml");
        String xml = getXMLRequest(request);
        InputStream resp = post("security/geoxacml",xml);
        checkXACMLRepsonse(resp, "NotApplicable");                
    }
    
    
    public void testIsEmployee3() throws Exception {
        File request = new File ("src/test/resources/requests/rbac/EmployeeRequest3.xml");
        String xml = getXMLRequest(request);
        InputStream resp = post("security/geoxacml",xml);
        checkXACMLRepsonse(resp, "NotApplicable");                
    }
    
    public void testManagerJunior() throws Exception{
        
        File request = new File ("src/test/resources/requests/rbac/ManagerRequestJunior.xml");
        String xml = getXMLRequest(request);
        InputStream resp = post("security/geoxacml",xml);
        checkXACMLRepsonse(resp, "Permit");                
    }

    public void testManagerSenior() throws Exception{
        
        File request = new File ("src/test/resources/requests/rbac/ManagerRequestSenior.xml");
        String xml = getXMLRequest(request);
        InputStream resp = post("security/geoxacml",xml);
        checkXACMLRepsonse(resp, "Permit");                
    }
    
    
    public void testHPORAliceManager() throws Exception{
        
        File request = new File ("src/test/resources/requests/rbac/HPORAliceManagerRequest.xml");
        String xml = getXMLRequest(request);
        InputStream resp = post("security/geoxacml",xml);
        checkXACMLRepsonse(resp, "NotApplicable");                       
    }

    
    // TODO, is not working
//    public void testHPORAliceEmployee() throws Exception {
//        
//        File request = new File ("src/test/resources/requests/rbac/HPORAliceEmployeeRequest.xml");
//        String xml = getXMLRequest(request);
//        InputStream resp = post("security/geoxacml",xml);
//        checkXACMLRepsonse(resp, "Permit");                       
//        
//    }

    
    
    protected void checkXACMLRepsonse(InputStream resp,String decision) throws Exception{
        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document doc = builder.parse(resp);
        Node decisionNode = doc.getElementsByTagName("Decision").item(0);
        assertEquals(decision,decisionNode.getTextContent());
        Node statusNode = doc.getElementsByTagName("StatusCode").item(0);
        String statusCode = statusNode.getAttributes().getNamedItem("Value").getTextContent();
        assertEquals("urn:oasis:names:tc:xacml:1.0:status:ok",statusCode);
        
        
    }
    
    protected void dumpResponse(InputStream resp) throws IOException{      
      System.out.println("RESPONSE");       
      byte[] bytes = new byte[512];
      while (resp.read(bytes)!=-1) {
          System.out.println(new String(bytes));
      }        
    }
    
    private String getXMLRequest(File f) {
        try {
            StringBuffer buff = new StringBuffer();
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
            String line;
            while ((line=reader.readLine())!=null)
                buff.append(line);        
            reader.close();
            return buff.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    
}
