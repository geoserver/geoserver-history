package org.geoserver.config;

import java.util.Collection;

import org.geoserver.catalog.Catalog;

/**
 * Facade providing access to the GeoServer configuration.
 * 
 * @author Justin Deoliveira, The Open Planning Project
 * 
 * TODO: events
 */
public interface GeoServer {

    /**
     * The global geoserver configuration.
     * 
     * @uml.property name="configuration"
     * @uml.associationEnd inverse="geoServer:org.geoserver.config.GeoServerInfo"
     */
    GeoServerInfo getGlobal();

    /**
     * Sets the global configuration.
     */
    void setGlobal( GeoServerInfo global );
    
    /**
     * The catalog.
     */
    Catalog getCatalog();
    
    /**
     * Sets the catalog.
     */
    void setCatalog( Catalog catalog );
    
    /**
     * Saves the global geoserver configuration after modification.
     */
    void save(GeoServerInfo geoServer);

    /**
     * Adds a service to the configuration.
     */
    void add(ServiceInfo service);

    /**
     * Removes a service from the configuration.
     */
    void remove(ServiceInfo service);

    /**
     * Saves a service that has been modified.
     */
    void save(ServiceInfo service);

    /**
     * GeoServer services.
     * 
     * @uml.property name="services"
     * @uml.associationEnd multiplicity="(0 -1)"
     *                     inverse="geoServer1:org.geoserver.config.ServiceInfo"
     */
    Collection<? extends ServiceInfo> getServices();

    /**
     * GeoServer services filtered by class.
     * 
     * @param clazz
     *                The class of the services to return.
     */
    <T extends ServiceInfo> T getService(Class<T> clazz);

    /**
     * Looks up a service by id.
     * 
     * @param id
     *                The id of the service.
     * @param clazz The type of the service.
     * 
     * @return The service with the specified id, or <code>null</code> if no
     *         such service coud be found.
     */
    <T extends ServiceInfo> T getService(String id, Class<T> clazz);

    /**
     * Looks up a service by name.
     * 
     * @param name The name of the service.
     * @param clazz The type of the service.
     * 
     * @return The service with the specified name or <code>null</code> if no
     *         such service could be found.
     */
    <T extends ServiceInfo> T getServiceByName(String name, Class<T> clazz );

    /**
     * The factory used to create configuration object.
     * 
     * @uml.property name="factory"
     * @uml.associationEnd inverse="geoServer:org.geoserver.config.GeoServerFactory"
     */
    GeoServerFactory getFactory();

    /**
     * Sets the factory used to create configuration object.
     * 
     * @uml.property name="factory"
     * @uml.associationEnd inverse="geoServer:org.geoserver.config.GeoServerFactory"
     */
    void setFactory(GeoServerFactory factory);
    
    /**
     * Adds a listener to the configuration.
     */
    void addListener( ConfigurationListener listener );

    /**
     * Removes a listener from the configuration.
     */
    void removeListener( ConfigurationListener listener );
    
    /**
     * Disposes the configuration.
     */
    void dispose();
}
