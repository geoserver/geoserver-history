package org.geoserver.catalog.event;

/**
 * Event for the removal of an object to the catalog. 
 *
 * @author Justin Deoliveira, The Open Planning Project
 *
 */
public interface CatalogRemoveEvent extends CatalogEvent {

    /**
     * the object that was removed.
     */
    Object getSource();
}
