/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.platform;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.geotools.factory.FactoryRegistry;
import org.geotools.util.SoftValueHashMap;
import org.geotools.util.logging.Logging;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;


/**
 * Utility class uses to process GeoServer extension points.
 * <p>
 * An instance of this class needs to be registered in spring context as follows.
 * <code>
 *         <pre>
 *         &lt;bean id="geoserverExtensions" class="org.geoserver.GeoServerExtensions"/&gt;
 *         </pre>
 * </code>
 * It must be a singleton, and must not be loaded lazily. Furthermore, this
 * bean must be loaded before any beans that use it.
 * </p>
 * @author Justin Deoliveira, The Open Planning Project
 * @author Andrea Aime, The Open Planning Project
 *
 */
public class GeoServerExtensions implements ApplicationContextAware, ApplicationListener {
    
    /**
     * logger 
     */
    public static Logger LOGGER = Logging.getLogger( "org.geoserver" );
    
    /**
     * Caches the names of the beans for a particular type, so that the lookup (expensive)
     * wont' be needed. We cache names instead of beans because doing the latter we would
     * break the "singleton=false" directive of some beans
     */
    static SoftValueHashMap<Class, String[]> extensionsCache = new SoftValueHashMap<Class, String[]>(40);
    
    /**
     * A static application context
     */
    static ApplicationContext context;

    /**
     * Sets the web application context to be used for looking up extensions.
     * <p>
     * This method is called by the spring container, and should never be called
     * by client code. If client needs to supply a particular context, methods
     * which take a context are available.
     * </p>
     * <p>
     * This is the context that is used for methods which dont supply their
     * own context.
     * </p>
     */
    public void setApplicationContext(ApplicationContext context)
        throws BeansException {
        GeoServerExtensions.context = context;
        extensionsCache.clear();
    }

    /**
     * Loads all extensions implementing or extending <code>extensionPoint</code>.
     *
     * @param extensionPoint The class or interface of the extensions.
     * @param context The context in which to perform the lookup.
     *
     * @return A collection of the extensions, or an empty collection.
     */
    public static final <T> List<T> extensions(Class<T> extensionPoint, ApplicationContext context) {
        String[] names;
        if(GeoServerExtensions.context == context){
            names = extensionsCache.get(extensionPoint);
        }else{
            names = null;
        }
        if(names == null) {
            checkContext(context);
            if ( context != null ) {
                try {
                    names = context.getBeanNamesForType(extensionPoint);
                    //update cache only if dealing with the same context
                    if(GeoServerExtensions.context == context){
                        extensionsCache.put(extensionPoint, names);
                    }
                }
                catch( Exception e ) {
                    //JD: this can happen during testing... if the application 
                    // context has been closed and a non-one time setup test is
                    // run that triggers an extension lookup
                    LOGGER.log( Level.SEVERE, "bean lookup error", e );
                    return Collections.EMPTY_LIST;
                }
            }
            else {
                return Collections.EMPTY_LIST;
            }
        }
        
        //look up all the beans
        List result = new ArrayList(names.length);
        for (int i = 0; i < names.length; i++) {
            result.add(context.getBean(names[i]));
        }
        
        //load from factory spi
        Iterator i = FactoryRegistry.lookupProviders(extensionPoint);
        while( i.hasNext() ) result.add( i.next() );
        
        //sort the results based on ExtensionPriority
        Collections.sort( result, new Comparator() {

            public int compare(Object o1, Object o2) {
                int p1 = ExtensionPriority.LOWEST;
                if ( o1 instanceof ExtensionPriority ) {
                    p1 = ((ExtensionPriority)o1).getPriority();
                }
                
                int p2 = ExtensionPriority.LOWEST;
                if ( o2 instanceof ExtensionPriority ) {
                    p2 = ((ExtensionPriority)o2).getPriority();
                }
                
                return p1 - p2;
            }
        });
        
        return result;
    }

    /**
     * Loads all extensions implementing or extending <code>extensionPoint</code>.
     * <p>
     * This method uses the "default" application context to perform the lookup.
     * See {@link #setApplicationContext(ApplicationContext)}.
     * </p>
     * @param extensionPoint The class or interface of the extensions.
     *
     * @return A collection of the extensions, or an empty collection.
     */
    public static final <T> List<T> extensions(Class<T> extensionPoint) {
        return extensions(extensionPoint, context);
    }
    
    /**
     * Returns a specific bean given its name
     * @param name
     * @return
     */
    public static final Object bean(String name) {
        checkContext(context);
        return context != null ? context.getBean(name) : null;
    }

    /**
     * Loads a single bean by its type.
     * <p>
     * This method returns null if there is no such bean. An exception is thrown
     * if multiple beans of the specified type exist.
     * </p>
     *
     * @param type THe type of the bean to lookup.
     * 
     * @throws IllegalArgumentException If there are multiple beans of the specified
     * type in the context. 
     */
    public static final <T> T bean(Class<T> type) throws IllegalArgumentException {
        checkContext(context);
        return context != null ? bean( type, context ) : null;
    }
    
    /**
     * Loads a single bean by its type from the specified application context.
     * <p>
     * This method returns null if there is no such bean. An exception is thrown
     * if multiple beans of the specified type exist.
     * </p>
     *
     * @param type THe type of the bean to lookup.
     * @param context The application context
     * 
     * @throws IllegalArgumentException If there are multiple beans of the specified
     * type in the context. 
     */
    public static final <T> T bean(Class<T> type, ApplicationContext context) throws IllegalArgumentException {
        List<T> beans = extensions(type,context);
        if ( beans.isEmpty() ) {
            return null;
        }
        
        if ( beans.size() > 1 ) {
            throw new IllegalArgumentException( "Multiple beans of type " + type.getName() );
        }
        
        return beans.get( 0 );
    }
    
    public void onApplicationEvent(ApplicationEvent event) {
        if(event instanceof ContextRefreshedEvent)
            extensionsCache.clear();
    }
    
    /**
     * Checks the context, if null will issue a warning.
     */
    static void checkContext(ApplicationContext context) {
        if ( context == null ) {
            LOGGER.severe( "Extension lookup occured, but ApplicationContext is unset.");
        }
    }
}
