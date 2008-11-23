/* Copyright (c) 2001 - 2008 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.catalog.impl;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.geoserver.catalog.Catalog;
import org.geoserver.catalog.NamespaceInfo;
import org.geoserver.catalog.WorkspaceInfo;


/**
 * A proxy which holds onto an identifier which will later be 
 * resolved into a real object.
 * 
 * @author Justin Deoliveira, The Open Planning Project
 *
 */
public class ResolvingProxy extends ProxyBase {

    /**
     * Wraps an object in the proxy.
     * 
     * @throws RuntimeException If creating the proxy fails.
     */
    public static <T> T create( String ref, Class<T> clazz ) {
        InvocationHandler h = new ResolvingProxy( ref );
        
        Class proxyClass = 
            Proxy.getProxyClass( clazz.getClassLoader(), clazz );
        
        T proxy;
        try {
            proxy = (T) proxyClass.getConstructor(
                new Class[] { InvocationHandler.class }).newInstance(new Object[] { h } );
        }
        catch( Exception e ) {
            throw new RuntimeException( e );
        }
        
        return proxy;
    }
    
    public static <T> T resolve( Catalog catalog, T object ) {
        if ( object instanceof Proxy ) {
            InvocationHandler h = Proxy.getInvocationHandler( object );
            if ( h instanceof ResolvingProxy ) {
                String ref = ((ResolvingProxy)h).getRef();
                if ( object instanceof WorkspaceInfo ) {
                    return (T) catalog.getWorkspace( ref );
                }
                if ( object instanceof NamespaceInfo ) {
                    return (T) catalog.getNamespace( ref );
                }
            }
        }
        
        return object;
    }
    
    /**
     * the reference
     */
    String ref;
    
    public ResolvingProxy(String ref) {
        this.ref = ref;
    }
    
    public String getRef() {
        return ref;
    }
    
    @Override
    protected Object handleGetUnSet(Object proxy, Method method, String property) throws Throwable {
        if ( "id".equalsIgnoreCase( property ) ) {
            return ref;
        }
            
        return null;
    }
    
    @Override
    protected Object handleOther(Object proxy, Method method, Object[] args) throws Throwable {
        return null;
    }
}
