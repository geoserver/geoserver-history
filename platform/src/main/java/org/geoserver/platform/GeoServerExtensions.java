package org.geoserver.platform;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
 * It must be a singleton, and must not be loaded lazily. Futhermore, this 
 * bean must be loaded before any beans that use it.
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
	 * Loads all extensions implementing or extending <code>extensionPoint</code>.
	 * 
	 * @param extensionPoint The class or interface of the extensions.
	 * 
	 * @return A collection of the extensions, or an empty collection.
	 */
	public static final List extensions( Class extensionPoint ) {
		return new ArrayList( context.getBeansOfType( extensionPoint ).values() );
	}
}
