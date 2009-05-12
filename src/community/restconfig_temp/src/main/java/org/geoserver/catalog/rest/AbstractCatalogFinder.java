package org.geoserver.catalog.rest;

import org.geoserver.catalog.Catalog;
import org.restlet.Finder;

/**
 * Abstract base class for finders of catalog resources.
 * 
 * @author Justin Deoliveira, OpenGEO
 *
 */
public abstract class AbstractCatalogFinder extends Finder {

    /**
     * reference to the catalog
     */
    protected Catalog catalog;
    
    protected AbstractCatalogFinder( Catalog catalog ) {
        this.catalog = catalog;
    }
    
    //@Override
    //public abstract AbstractCatalogResource findTarget(Request request, Response response);
}
