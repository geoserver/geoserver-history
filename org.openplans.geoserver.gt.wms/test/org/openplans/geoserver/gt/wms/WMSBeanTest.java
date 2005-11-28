package org.openplans.geoserver.gt.wms;

import java.net.URI;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.geotools.catalog.Service;
import org.geotools.catalog.ServiceFinder;
import org.geotools.catalog.wms.WMSService;
import org.openplans.geoserver.gt.catalog.GSServiceFinder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping;

import com.mockrunner.mock.web.MockHttpServletRequest;

import junit.framework.TestCase;

public class WMSBeanTest extends TestCase {

	ApplicationContext context;
	
	protected void setUp() throws Exception {
		super.setUp();
		
		context = new ClassPathXmlApplicationContext(
			new String[]{
				"org.openplans.geoserver-servlet.xml",
				"org.openplans.geoserver.gt-applicationContext.xml",
				"org.openplans.geoserver.gt.wms-applicationContext.xml"
			}	
		);
	}
	
	public void testServiceFactory() throws Exception {
		ServiceFinder finder = 
			(ServiceFinder) context.getBean(GSServiceFinder.ID);
		
		Map params = new HashMap();
		params.put(WMSService.WMS_URL_KEY, new URL("http://localhost/geoserver/wms"));
		
		List services = finder.aquire(params);
		assertFalse(services.isEmpty());
		
		boolean found = false;
		for (Iterator itr = services.iterator(); itr.hasNext();) {
			Service service = (Service) itr.next();
			if (service instanceof WMSService)
				found = true;
		}
		
		assertTrue(found);
	}
	
	public void testURLMapping() throws Exception {
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setRequestURI("http://localhost/geoserver/wms");
		request.setRequestURL("http://localhost/geoserver/wms");
		request.setServletPath("/wms");
		request.setContextPath("/wms");
		
		SimpleUrlHandlerMapping mapping = 
			(SimpleUrlHandlerMapping) context.getBean("wmsServiceMapping");
		
		assertNotNull(mapping);
		assertNotNull(mapping.getHandler(request));
	}
	
	public void testService() throws Exception {
		assertTrue(context.containsBean(WMS.ID));
	}
	
	
}
