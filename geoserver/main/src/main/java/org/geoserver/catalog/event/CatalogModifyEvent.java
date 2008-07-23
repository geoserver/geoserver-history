package org.geoserver.catalog.event;

import java.util.List;

/**
 * Event for the modification of an object in the catalog.
 * 
 * @author Justin Deoliveira, The Open Planning Project
 * 
 */
public interface CatalogModifyEvent extends CatalogEvent {

    /**
     * The names of the properties that were modified.
     */
    List<String> getPropertyNames();

    /**
     * The old values of the properties that were modified.
     */
    List<Object> getOldValues();

    /**
     * The new values of the properties that were modified.
     */
    List<Object> getNewValues();
}
