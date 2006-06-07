/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.sample.servlets;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.vfny.geoserver.ExceptionHandler;
import org.vfny.geoserver.Response;
import org.vfny.geoserver.global.HelloWorld;
import org.vfny.geoserver.servlets.AbstractService;
import org.vfny.geoserver.util.requests.readers.KvpRequestReader;
import org.vfny.geoserver.util.requests.readers.XmlRequestReader;

public class HelloWorldService extends AbstractService {
		
    public HelloWorldService(HelloWorld helloWorld) {
    		super("HW","HW",helloWorld);
    }
    
    public HelloWorld getHelloWorld() {
    		return (HelloWorld) getServiceRef();
    }
    
    public void setHelloWorld(HelloWorld helloWorld) {
    		setServiceRef(helloWorld);
    }
    
    protected ExceptionHandler getExceptionHandler() {
        return null;
    }
    
    protected boolean isServiceEnabled(HttpServletRequest req){
    		return getHelloWorld().isEnabled();
    }
    
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String s = createHelloWorldResponse();
		OutputStream stream = response.getOutputStream();
		stream.write(s.getBytes());
	}
    
    private String createHelloWorldResponse() {
		String xmlString = null;
		try{
			javax.xml.parsers.DocumentBuilderFactory dbfac = javax.xml.parsers.DocumentBuilderFactory.newInstance();
			javax.xml.parsers.DocumentBuilder docBuilder = dbfac.newDocumentBuilder();
			org.w3c.dom.Document doc = docBuilder.newDocument();
			
			// Add the root
			org.w3c.dom.Element root = doc.createElement("myFirstSample");
			doc.appendChild(root);
			            
            //add a text element to the child
			org.w3c.dom.Text text = doc.createTextNode("Hello World!");
            root.appendChild(text);
            
            //set up a transformer
            javax.xml.transform.TransformerFactory transfac = javax.xml.transform.TransformerFactory.newInstance();
            javax.xml.transform.Transformer trans = transfac.newTransformer();
            trans.setOutputProperty(javax.xml.transform.OutputKeys.OMIT_XML_DECLARATION, "yes");
            trans.setOutputProperty(javax.xml.transform.OutputKeys.INDENT, "yes");

            //create string from xml tree
            java.io.StringWriter sw = new java.io.StringWriter();
            javax.xml.transform.stream.StreamResult result = new javax.xml.transform.stream.StreamResult(sw);
            javax.xml.transform.dom.DOMSource source = new javax.xml.transform.dom.DOMSource(doc);
            trans.transform(source, result);
            xmlString = sw.toString();
		}catch(Exception e){
			e.printStackTrace();
		}
		return xmlString;
	}
    
    protected KvpRequestReader getKvpReader(Map arg0) {
    	// TODO Auto-generated method stub
    	return null;
    }

	protected Response getResponseHandler() {
		// TODO Auto-generated method stub
		return null;
	}

	protected XmlRequestReader getXmlRequestReader() {
		// TODO Auto-generated method stub
		return null;
	}
}
