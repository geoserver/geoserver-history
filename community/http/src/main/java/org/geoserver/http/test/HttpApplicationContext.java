package org.geoserver.http.test;

import java.io.IOException;
import java.io.InputStream;

/**
 * Provides a test application context containing bean definitions of http support 
 * classes.
 * <p> 
 * <pre>
 * &lt;beans>
 *
 * &lt;!-- controller which dispatches to ows services -->
 * &lt;bean id="dispatcher" class="org.geoserver.ows.http.OWSDispatcher"/>
 * 	
 * &lt;!-- interceptor which initializes ows services -->
 * &lt;bean id="owsInitializer" class="org.geoserver.ows.http.OWSInitializer"/>
 * 
 * &lt;!-- dispatcher mappings -->
 * &lt;bean id="owsDispatcherMapping" 
 * 	class="org.springframework.web.servlet.handler.SimpleUrlHandlerMapping">
 * 	
 * 	&lt;property name="interceptors">
 * 		&lt;list>
 *			 &lt;ref bean="owsInitializer"/>
 * 		&lt;/list>
 * 	&lt;/property>
 * 	
 * 	&lt;!-- map ows by default -->
 * 	&lt;property name="mappings">
 * 	  &lt;props>
 * 	    &lt;prop key="/ows">dispatcher&lt;/prop>
 *     	&lt;prop key="/ows/*">dispatcher&lt;/prop>
 *     &lt;/props>
 *   &lt;/property>
 *   
 * &lt;/bean>
 *
 * &lt;/beans>
 * </pre>
 * </p>
 * @author Justin Deoliveira, The Open Planning Project
 *
 */
public class HttpApplicationContext {

	public static InputStream getBeanDefinitions() throws IOException {
		return HttpApplicationContext.class.getResourceAsStream( "test-applicationContext.xml" );
	}
	
	public static InputStream getDispatcherServletDefinitions() throws IOException {
		return HttpApplicationContext.class.getResourceAsStream( "test-dispatcher-servlet.xml" );
	}
}
