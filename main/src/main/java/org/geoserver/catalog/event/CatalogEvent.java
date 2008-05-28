package org.geoserver.catalog.event;

/**
 * Catalog event.
 * 
 * @author Justin Deoliveira, The Open Planning Project
 */
public interface CatalogEvent {

    /**
     * The source of the event.
     */
    Object getSource();

}
