package org.geoserver.web;

import org.apache.wicket.markup.html.WebPage;
import org.geoserver.catalog.Catalog;
import org.geoserver.config.GeoServer;

/**
 * Base class for web pages in GeoServer web application.
 * <ul>
 * <li>The basic layout</li>
 * <li>An OO infrastructure for common elements location</li>
 * <li>An infrastructure for locating subpages in the Spring context and
 * creating links</li>
 * </ul>
 * 
 * TODO: breadcrumb automated cration.
 * This can be done by using a list of {@link BookmarkablePageInfo} instances
 * that needs to be passed to each page, a custom PageLink subclass that provides
 * that information, and some code coming from {@link BreadCrumbBar}. <br>
 * See also this discussion on the wicket users mailing list:
 * http://www.nabble.com/Bread-crumbs-based-on-pages%2C-not-panels--tf2244730.html#a6225855
 * 
 * @author Andrea Aaime, The Open Planning Project
 * @author Justin Deoliveira, The Open Planning Project
 */
public class GeoServerBasePage extends WebPage {

    /**
     * Returns the application instance. 
     */
    protected GeoServerApplication getGeoServerApplication() {
        return (GeoServerApplication) getApplication();
    }
    
    /**
     * Convenience method for pages to get access to the geoserver configuration. 
     */
    protected GeoServer getGeoServer() {
        return getGeoServerApplication().getGeoServer();
    }
    
    /**
     * Convenience method for pages to get access to the catalog.
     */
    protected Catalog getCatalog() {
        return getGeoServerApplication().getCatalog();
    }
}
