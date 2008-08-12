/* Copyright (c) 2001 - 2008 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.config.impl;

import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.geoserver.catalog.Catalog;
import org.geoserver.catalog.impl.ModificationProxy;
import org.geoserver.catalog.impl.ProxyList;
import org.geoserver.config.ConfigurationListener;
import org.geoserver.config.GeoServer;
import org.geoserver.config.GeoServerFactory;
import org.geoserver.config.GeoServerInfo;
import org.geoserver.config.ServiceInfo;

public class GeoServerImpl implements GeoServer {

    GeoServerFactory factory = new GeoServerFactoryImpl();
    GeoServerInfo global;
    Catalog catalog;
    
    List<ServiceInfo> services = new ArrayList<ServiceInfo>();
    List<ConfigurationListener> listeners = new ArrayList<ConfigurationListener>();

    public GeoServerFactory getFactory() {
        return factory;
    }
    
    public void setFactory(GeoServerFactory factory) {
        this.factory = factory;
    }

    public Catalog getCatalog() {
        return catalog;
    }
    
    public void setCatalog(Catalog catalog) {
        this.catalog = catalog;
    }
    
    public GeoServerInfo getGlobal() {
        if ( global == null ) {
            return null;
        }
        
        return ModificationProxy.create( global, GeoServerInfo.class );
    }
    
    public void setGlobal(GeoServerInfo global) {
        this.global = global;
    }
    
    public void add(ServiceInfo service) {
        if ( service.getId() == null ) {
            throw new NullPointerException( "service id must not be null" );
        }
        for ( ServiceInfo s : services ) {
            if ( s.getId().equals( service.getId() ) ) {
                throw new IllegalArgumentException( "service with id '" + s.getId() + "' already exists" );
            }
        }
        
        //may be adding a proxy, need to unwrap
        service = ModificationProxy.unwrap(service);
        services.add( service );
    }

    public <T extends ServiceInfo> T getService(Class<T> clazz) {
        for ( ServiceInfo si : services ) {
           if( clazz.isAssignableFrom( si.getClass() ) ) {
               return ModificationProxy.create( (T) si, clazz );
           }
        }
        
        return null;
    }

    public <T extends ServiceInfo> T getService(String id, Class<T> clazz) {
        for ( ServiceInfo si : services ) {
            if( id.equals( si.getId() ) ) {
                return ModificationProxy.create( (T) si, clazz );
            }
         }
         
         return null;
    }

    public <T extends ServiceInfo> T getServiceByName(String name, Class<T> clazz) {
        for ( ServiceInfo si : services ) {
            if( name.equals( si.getName() ) ) {
                return ModificationProxy.create( (T) si, clazz );
            }
         }
         
         return null;
    }

    public Collection<? extends ServiceInfo> getServices() {
        return new ProxyList( services, ServiceInfo.class ) {
            protected <T> T createProxy(T proxyObject, Class<T> proxyInterface) {
                return ModificationProxy.create( proxyObject, proxyInterface );
            }
        };
    }
    
    public void remove(ServiceInfo service) {
        services.remove( service );
    }

    public void save(GeoServerInfo geoServer) {
        ModificationProxy proxy = 
            (ModificationProxy) Proxy.getInvocationHandler( geoServer );
        
        List propertyNames = proxy.getPropertyNames();
        List oldValues = proxy.getOldValues();
        List newValues = proxy.getNewValues();
        
        for ( ConfigurationListener l : listeners ) {
            try {
                l.handleGlobalChange( geoServer, propertyNames, oldValues, newValues);
            }
            catch( Exception e ) {
                //log this
            }
        }
        
        proxy.commit();
    }

    public void save(ServiceInfo service) {
        ModificationProxy proxy = 
            (ModificationProxy) Proxy.getInvocationHandler( service );
        
        List propertyNames = proxy.getPropertyNames();
        List oldValues = proxy.getOldValues();
        List newValues = proxy.getNewValues();
        
        for ( ConfigurationListener l : listeners ) {
            try {
                l.handleServiceChange( service, propertyNames, oldValues, newValues);
            }
            catch( Exception e ) {
                //log this
            }
        }
        
        proxy.commit();
    }
    
    public void addListener(ConfigurationListener listener) {
        listeners.add( listener );
    }
    
    public void removeListener(ConfigurationListener listener) {
        listeners.remove( listener );
    }
    
    public void dispose() {
        global.dispose();
        catalog.dispose();
        services.clear();
        listeners.clear();
    }
}
