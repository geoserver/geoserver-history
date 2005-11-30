package org.openplans.geoserver;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.mockrunner.mock.web.MockHttpServletRequest;

import junit.framework.TestCase;

public class RequestControllerTest extends TestCase {

	ApplicationContext context;
	
	protected void setUp() throws Exception {
		context = new ClassPathXmlApplicationContext(
			new String[]{
				"org.openplans.geoserver-servlet.xml",
				"test-applicationContext.xml"
			}
		);
	}
	
	public void testOperation() throws Exception {
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setRequestURL("http://localhost/dummy");
		request.setQueryString("service=dummy&&request=dummy&dummy=dummy");
		
		RequestController rc = 
			(RequestController) context.getBean("requestController");
		rc.handleRequest(request,null);
		
		DummyService dummyService = (DummyService) context.getBean("dummyService");
		assertTrue(dummyService.executed);
		assertTrue(dummyService.setter);
		
	}
	
}
