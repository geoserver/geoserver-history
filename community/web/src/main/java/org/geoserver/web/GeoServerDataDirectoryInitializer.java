package org.geoserver.web;

import java.io.File;

import javax.servlet.ServletContext;

import org.geoserver.GeoServerResourceLoader;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.web.context.ServletContextAware;

/**
 * Initializes an instanceof {@link org.geoserver.GeoServerResourceLoader} with a 
 * 'GeoServer Data Directory' from inside a servlet container.
 * <p>
 * An instance of this class must be declared in a spring context as follows:
 * <code>
 * 	<pre>
 * 	<bean id="dataDirInitializer" class="org.geoserver.data.GeoServerDataDirectoryInitializer">
 * 		<constructor-arg ref="loader"/>
 * 	</bean>
 * 	</pre>
 * </code>
 * Where <code>loader</code> is a predefinied resource loader.
 * </p>
 * <p>
 * The rules for determining the data directory are:
 * <ol>
 * 	<li>If the system property "GEOSERVER_DATA_DIR" is defined, it is used.
 * 	<li>If the servlet context property "GEOSERVER_DATA_DIR" is defined, it is used.
 * 	<li>If none of the above, the old style data directory is assumed and the root of context 
 * is used.
 * </ol>
 * </p>
 * @author Justin Deoliveira, The Open Planning Project, jdeolive@openplans.org
 *
 */
public class GeoServerDataDirectoryInitializer implements BeanPostProcessor, ServletContextAware {

	/**
	 * Teh servlet context
	 */
	ServletContext context;
	
	public Object postProcessBeforeInitialization(Object bean, String name) throws BeansException {
		return bean;
	}
	
	public Object postProcessAfterInitialization(Object bean, String name) throws BeansException {
		if ( bean instanceof GeoServerResourceLoader ) {
			GeoServerResourceLoader loader = (GeoServerResourceLoader) bean;
			File dataDir = null;
			
			//see if there's a system property
			String prop = System.getProperty( "GEOSERVER_DATA_DIR" );
			if ( prop != null && !prop.equals("") ) {
				dataDir = new File( prop );
			}
			else {
				//try the webxml
				String loc = context.getInitParameter( "GEOSERVER_DATA_DIR" );
				if ( loc != null ) {
					dataDir = new File(loc);
				}
			}
			
			if ( dataDir != null ) {
				loader.setBaseDirectory( dataDir );
			}
			else {
				//old style data directory
				loader.setBaseDirectory( new File( context.getRealPath( "/" ) ) );
				
				//add some additional directories to the search path as well
				loader.addSearchLocation(
					new File( context.getRealPath( "WEB-INF" ) )
				);
				loader.addSearchLocation(
					new File( context.getRealPath( "data" ) )	
				);
			}

		}
		
		return bean;
	}
	
	public void setServletContext( ServletContext context ) {
		this.context = context;
	}

}
