package org.geoserver.catalog.event;

/**
 * Event for the addition of an object to the catalog. 
 *
 * @author Justin Deoliveira, The Open Planning Project
 *
 */
public interface CatalogAddEvent extends CatalogEvent {

    /**
     * the object that was added.
     */
    Object getSource();
}
