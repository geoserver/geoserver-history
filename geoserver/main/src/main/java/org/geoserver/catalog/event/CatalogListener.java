/* Copyright (c) 2001 - 2008 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.catalog.event;


/**
 * Listener for catalog events.
 * 
 * @author   Justin Deoliveira, The Open Planning Project
 *
 */
public interface CatalogListener {

    /**
     * Handles the event of an addition to the catalog.
     */
    void handleAddEvent(CatalogAddEvent event);

    /**
     * Handles the event of a removal from the catalog.
     * 
     */
    void handleRemoveEvent(CatalogRemoveEvent event);

    /**
     * Handles the event of a modification to an object in the catalog.
     */
    void handleModifyEvent(CatalogModifyEvent event);
	
}
