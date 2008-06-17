package org.geoserver.web;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.IConverterLocator;
import org.apache.wicket.IRequestTarget;
import org.apache.wicket.RequestCycle;
import org.apache.wicket.protocol.http.WebRequestCycleProcessor;
import org.apache.wicket.request.IRequestCycleProcessor;
import org.apache.wicket.request.RequestParameters;
import org.apache.wicket.spring.SpringWebApplication;
import org.geoserver.catalog.Catalog;
import org.geoserver.config.GeoServer;
import org.geoserver.platform.GeoServerExtensions;
import org.geoserver.platform.GeoServerResourceLoader;
import org.geoserver.web.util.CompositeConverterLocator;
import org.geoserver.web.util.DataDirectoryConverterLocator;
import org.geoserver.web.util.GeoToolsConverterLocator;
import org.springframework.context.ApplicationContext;

/**
 * 
 * @author Andrea Aaime, The Open Planning Project
 * @author Justin Deoliveira, The Open Planning Project
 */
public class GeoServerApplication extends SpringWebApplication {

    public Class getHomePage() {
        return GeoServerHomePage.class;
    }

    /**
     * Returns the spring application context.
     */
    public ApplicationContext getApplicationContext() {
        return internalGetApplicationContext();
    }
    
    /**
     * Returns the geoserver configuration instance.
     */
    public GeoServer getGeoServer() {
        return getBeanOfType( GeoServer.class );
    }
    
    /**
     * Returns the catalog.
     */
    public Catalog getCatalog() {
        return getGeoServer().getCatalog();
    }
    
    /**
     * Returns the geoserver resource loader.
     */
    public GeoServerResourceLoader getResourceLoader() {
        return getBeanOfType( GeoServerResourceLoader.class );
    }
    
    /**
     * Loads a bean from the spring application context of a specific type.
     * <p>
     * If there are multiple beans of the specfied type in the context an 
     * exception is thrown.
     * </p>
     * @param type The class of the bean to return.
     */
    public <T> T getBeanOfType( Class<T> type ) {
        return GeoServerExtensions.bean( type, getApplicationContext() );
    }
    
    /**
     * Loads beans from the spring application context of a specific type.
     *
     * @param type The type of beans to return.
     *
     * @return A list of objects of the specified type, possibly empty.
     * @see {@link GeoServerExtensions#extensions(Class, ApplicationContext)}
     */
    public <T> List<T> getBeansOfType( Class<T> type ) {
        return GeoServerExtensions.extensions( type, getApplicationContext() );
    }
    
    /*
     * Overrides to return a custom request cycle processor. This is done in 
     * order to support "dynamic dispatching" from web.xml.
     */
    protected IRequestCycleProcessor newRequestCycleProcessor() {
        return new RequestCycleProcessor();
    }
    
    /*
     * Overrides to return a custom converter locator which loads converters 
     * from teh GeoToools converter subsystem. 
     */
    protected IConverterLocator newConverterLocator() {
        //TODO: load converters from application context
        
        List<IConverterLocator> converters = new ArrayList<IConverterLocator>();
        
        converters.add( new DataDirectoryConverterLocator( getResourceLoader()) );
        converters.add( super.newConverterLocator() );
        converters.add( new GeoToolsConverterLocator() );
        
        return new CompositeConverterLocator( converters );
    }
    
    static class RequestCycleProcessor extends WebRequestCycleProcessor {
        public IRequestTarget resolve(RequestCycle requestCycle,
                RequestParameters requestParameters) {
            IRequestTarget target = super.resolve(requestCycle, requestParameters);
            if ( target != null ) {
                return target;
            }
            
            return resolveHomePageTarget(requestCycle, requestParameters);
        }
    }
}
