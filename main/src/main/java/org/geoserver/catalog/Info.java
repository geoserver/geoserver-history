/* Copyright (c) 2001 - 2008 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.catalog;

/**
 * Interface implemented by all catalog and configuration objects.
 * 
 * @author Justin Deoliveira, OpenGeo
 *
 */
public interface Info {

    /**
     * Returns the identifier for the object.
     */
    String getId();
}
