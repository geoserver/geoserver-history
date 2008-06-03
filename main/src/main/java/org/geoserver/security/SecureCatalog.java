/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.security;

import org.geoserver.catalog.Catalog;
import org.geoserver.catalog.Wrapper;

/**
 * A secure catalog is a wrapper around a catalog that filters and changes the
 * nature of the objects returned by the wrapped catalog, in particular:
 * <ul>
 * <li>it filters out any object that the current user is not allowed to see</li>
 * <li>it wraps the returned objects into read only wrappers if the current
 * user is not allowed to write on them</li>
 * </ul>
 * 
 * @author Andrea Aime - TOPP
 * 
 */
public interface SecureCatalog extends Catalog, Wrapper<Catalog> {

}
