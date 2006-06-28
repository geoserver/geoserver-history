package org.geoserver.wfs;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.geoserver.ows.http.Dispatcher;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.mockobjects.servlet.MockHttpServletRequest;
import com.mockobjects.servlet.MockHttpServletResponse;
import com.mockobjects.servlet.MockServletOutputStream;

public class GetCapabilitiesTest extends TestCase {

	public void testGetCapabilities() throws Exception {
		
		ClassPathXmlApplicationContext context = 
			new ClassPathXmlApplicationContext( "classpath*:applicationContext.xml" );
		
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
	}
	
}
