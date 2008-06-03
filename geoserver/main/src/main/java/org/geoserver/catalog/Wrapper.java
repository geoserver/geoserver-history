/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.catalog;


/**
 * Generic interface that can be implemented by object wrappers that allow
 * client code to unwrap their contents
 * 
 * @author Andrea Aime - TOPP
 * 
 * @param <T>
 */
public interface Wrapper<T> {
    /**
     * Unwraps the object, returning the wrapped instance (this is not
     * recursive, if you need full unwrapping use
     * {@link WrapperUtils#deepUnwrap(Wrapper))
     * 
     * @return
     */
    T unwrap();

    /**
     * Returns true if this either implements the interface argument or is
     * directly a wrapper for an object that does. Returns false otherwise. 
     * 
     * @param iface
     *            a Class defining an interface.
     * @return true if this implements the interface or directly wraps an object that does.
     */
    boolean isWrapperFor(Class<?> iface);
}
