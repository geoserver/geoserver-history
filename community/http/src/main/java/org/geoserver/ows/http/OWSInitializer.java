package org.geoserver.ows.http;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.geoserver.http.util.RequestUtils;
import org.geoserver.ows.OWS;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * Initializes an OWS service on an incoming request.
 * <p>
 * 
 * </p>
 * 
 * @author Justin Deoliveira, The Open Planning Project, jdeolive@openplans.org
 *
 */
public class OWSInitializer implements HandlerInterceptor, ApplicationContextAware {

	ApplicationContext applicationContext;
	
	public void setApplicationContext(ApplicationContext applicationContext)
		throws BeansException {
		
		this.applicationContext = applicationContext;
	}
	
	public boolean preHandle(
		HttpServletRequest request, HttpServletResponse response, Object handler
	) throws Exception {
		
		//only for the OWS dispatched
		if ( handler instanceof OWSDispatcher ) {
			Collection services = 
				applicationContext.getBeansOfType( OWS.class ).values();
			for ( Iterator s = services.iterator(); s.hasNext(); ) {
					OWS ows = (OWS) s.next();
					
					//set the online resource
					try {
						URL url = new URL(
							RequestUtils.baseURL( request ) + ows.getId()
						);
						ows.setOnlineResource( url );
					} 
					catch (MalformedURLException e) {
						throw new RuntimeException( e );
					}
							
					//set the schema base
					ows.setSchemaBaseURL( RequestUtils.baseURL( request ) + "schemas" );
					
					//set the charset
				
					Charset charSet = null;
					try {
						charSet = Charset.forName( request.getCharacterEncoding() );
					}
					catch( Exception e ) {
						charSet = Charset.forName( "UTF-8" );
					}
					
					ows.setCharSet( charSet );
				}
			
		}
		
		return true;
	}

	public void postHandle(
		HttpServletRequest request, HttpServletResponse response, Object handler,
		ModelAndView modelAndView
	) throws Exception {
		
		// do nothing

	}

	public void afterCompletion(
		HttpServletRequest request, HttpServletResponse response, Object handler, 
		Exception ex
	) throws Exception {
		
		//do nothing

	}
}
