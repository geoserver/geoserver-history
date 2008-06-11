package org.geoserver.catalog.impl;

import java.util.AbstractList;
import java.util.List;

/**
 * An unmodifiable list proxy in which each element in the list is wrapped in a 
 * proxy of its own.
 * <p>
 * Subclasses should implement {@link #createProxy(Object, Class)}. 
 * </p>
 * 
 * @author Justin Deoliveira, The Open Planning Project
 *
 */
public abstract class ProxyList extends AbstractList {

    protected List proxyList;
    protected Class proxyInterface;
    
    public ProxyList(List proxyList, Class proxyInterface) {
        this.proxyList = proxyList;
        this.proxyInterface = proxyInterface;
    }
    
    public Object get(int index) {
        Object proxyObject = proxyList.get( index );
        return createProxy(proxyObject, proxyInterface);
    }
    
    public int size() {
        return proxyList.size();
    }
    
    abstract protected <T> T createProxy( T proxyObject, Class<T> proxyInterface );
}
