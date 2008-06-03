/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.catalog;

/**
 * Generic interface that can be implemented by object wrappers that allow
 * client code to unwrap their contentes
 * 
 * @author Andrea Aime - TOPP
 * 
 * @param <T>
 */
public interface Wrapper<T> {
    T unwrap();
}
