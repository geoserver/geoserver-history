package org.geoserver;

import java.util.Collection;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * Utility class uses to process GeoServer extension points.
 * <p>
 * An instance of this class needs to be registered in spring context as follows.
 * <code>
 * 	<pre>
 * 	&lt;bean id="geoserverExtensions" class="org.geoserver.GeoServerExtensions"/&gt;
 * 	</pre>
 * </code>
 * 
 * It must be a singleton, and must not be loaded lazily.
 * </p>
 * @author Justin Deoliveira, The Open Planning Project
 *
 */
public class GeoServerExtensions implements ApplicationContextAware {

	/**
	 * A static application context
	 */
	static ApplicationContext context;
	
	public void setApplicationContext(ApplicationContext context) throws BeansException {
		GeoServerExtensions.context = context;
	}

	/**
	 * Loads all extensions implementing or extending <code>extensionPoint</code>
	 * 
	 * @param extensionPoint The type of the extensions.
	 * 
	 * @return A collection of the extensions, or an empty collection.
	 */
	public static final Collection extensions( Class extensionPoint ) {
		return context.getBeansOfType( extensionPoint ).values();
	}
}
