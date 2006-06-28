package org.geoserver.wfs;

import java.io.StringBufferInputStream;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import junit.framework.TestCase;

import org.apache.xml.utils.DOMBuilder;
import org.geoserver.ows.http.Dispatcher;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import com.mockobjects.servlet.MockHttpServletRequest;
import com.mockobjects.servlet.MockHttpServletResponse;
import com.mockobjects.servlet.MockServletInputStream;
import com.mockobjects.servlet.MockServletOutputStream;

public class GetCapabilitiesTest extends TestCase {

	ClassPathXmlApplicationContext context;
	
	protected void setUp() throws Exception {
		context = 
			new ClassPathXmlApplicationContext( "classpath*:applicationContext.xml" );
	}
	
	public void testGetCapabilitiesGet() throws Exception {
		
		Dispatcher dispatcher = 
			(Dispatcher) context.getBean( "dispatcher" );
		
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setupGetContextPath("/geoserver");
		request.setupGetMethod("GET");
		
		MockHttpServletResponse response = new MockHttpServletResponse();
		response.setupOutputStream( new MockServletOutputStream() );
		
		Map params = new HashMap();
		params.put( "service", "wfs" );
		params.put( "request", "GetCapabilities" );
		
		request.setupGetParameterMap( params );
		request.setupGetInputStream(null);
		
		dispatcher.handleRequest( request, response );
		
		DocumentBuilder builder = 
			DocumentBuilderFactory.newInstance().newDocumentBuilder();
		
		Document doc = builder.parse( 
			new InputSource( new StringReader( response.getOutputStreamContents() ) ) 
		);
		assertEquals( "WFS_Capabilities", doc.getDocumentElement().getNodeName() );
	}
	
	public void testGetCapabilitiesPost() throws Exception {
		Dispatcher dispatcher = 
			(Dispatcher) context.getBean( "dispatcher" );
		
		String xml = "<GetCapabilities service=\"WFS\" " + 
			"xmlns=\"http://www.opengis.net/wfs\" " + 
			"xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" " + 
			"xsi:schemaLocation=\"http://www.opengis.net/wfs http://schemas.opengis.net/wfs/1.0.0/WFS-basic.xsd\"/>" ;

		MockServletInputStream input = new MockServletInputStream();
		input.setupRead( xml.getBytes() );
		
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setupGetContextPath("/geoserver");
		request.setupGetMethod("POST");
		request.setupGetInputStream( input );
		request.setupGetParameterMap(null);
		
		MockHttpServletResponse response = new MockHttpServletResponse();
		response.setupOutputStream( new MockServletOutputStream() );
		
		dispatcher.handleRequest( request, response );
		
		DocumentBuilder builder = 
			DocumentBuilderFactory.newInstance().newDocumentBuilder();
		
		Document doc = builder.parse( 
			new InputSource( new StringReader( response.getOutputStreamContents() ) ) 
		);
		assertEquals( "WFS_Capabilities", doc.getDocumentElement().getNodeName() );
	}
	
}
 