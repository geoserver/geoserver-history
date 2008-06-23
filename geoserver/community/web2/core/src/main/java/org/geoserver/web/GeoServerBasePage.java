/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.web;

import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.ResourceReference;
import org.apache.wicket.extensions.breadcrumb.BreadCrumbBar;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.StringResourceModel;
import org.geoserver.catalog.Catalog;
import org.geoserver.config.GeoServer;
import org.geoserver.web.admin.ServerAdminPage;
import org.geoserver.web.services.ServicePageInfo;

/**
 * Base class for web pages in GeoServer web application.
 * <ul>
 * <li>The basic layout</li>
 * <li>An OO infrastructure for common elements location</li>
 * <li>An infrastructure for locating subpages in the Spring context and
 * creating links</li>
 * </ul>
 * 
 * TODO: breadcrumb automated cration. This can be done by using a list of
 * {@link BookmarkablePageInfo} instances that needs to be passed to each page,
 * a custom PageLink subclass that provides that information, and some code
 * coming from {@link BreadCrumbBar}. <br>
 * See also this discussion on the wicket users mailing list:
 * http://www.nabble.com/Bread-crumbs-based-on-pages%2C-not-panels--tf2244730.html#a6225855
 * 
 * @author Andrea Aaime, The Open Planning Project
 * @author Justin Deoliveira, The Open Planning Project
 */
public class GeoServerBasePage extends WebPage {

    public GeoServerBasePage() {

        // welcome page link
        add( new BookmarkablePageLink( "welcome", GeoServerHomePage.class )
            .add( new Label( "label", new StringResourceModel( "welcome", (Component)null, null ) )  ) );
        
        // server admin link
        add( new BookmarkablePageLink( "admin.server", ServerAdminPage.class ) 
            .add( new Label( "label", new StringResourceModel( "server", (Component)null, null ) ) ) );
        
        // list of services to administer
        List<ServicePageInfo> pages = getGeoServerApplication().getBeansOfType(
                ServicePageInfo.class);
        ListView services = new ListView("admin.services", pages) {
            protected void populateItem(ListItem item) {
                ServicePageInfo page = (ServicePageInfo) item.getModelObject();

                //add a link
                BookmarkablePageLink link = new BookmarkablePageLink("admin.service",
                        page.getComponentClass());
                link.add(new Image( "serviceIcon", new ResourceReference( page.getComponentClass(), page.getIcon() ) ) );
                link.add(new Label("serviceLabel", new StringResourceModel( page.getTitleKey(), (Component) null, null ) ));
                link.add(new AttributeModifier( "title", new StringResourceModel( page.getDescriptionKey(), (Component) null, null ) ) );
                
                item.add(link);
            }
        };
        add( services );
        
        //data link
        add( new BookmarkablePageLink( "data", org.geoserver.web.data.tree.DataPage.class ) 
            .add( new Label( "label", new StringResourceModel( "data", (Component) null, null ) ) ) );
    }

    /**
     * Returns the application instance.
     */
    protected GeoServerApplication getGeoServerApplication() {
        return (GeoServerApplication) getApplication();
    }

    /**
     * Convenience method for pages to get access to the geoserver
     * configuration.
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
