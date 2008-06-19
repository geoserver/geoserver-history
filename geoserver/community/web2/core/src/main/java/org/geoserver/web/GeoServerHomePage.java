package org.geoserver.web;

import java.util.List;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.geoserver.catalog.ResourceInfo;
import org.geoserver.catalog.StoreInfo;
import org.geoserver.web.data.ResourceConfigurationPage;

public class GeoServerHomePage extends GeoServerBasePage {

    public GeoServerHomePage() {
        
        //TODO: ensure order by adding the well-known pages manually and then 
        // processing any remaining pages
        final List<MainPageInfo> pages = ((GeoServerApplication) getApplication()).getBeansOfType(MainPageInfo.class);
        ListView view = new ListView( "pages", pages ) {
			private static final long serialVersionUID = 8943715788283282175L;

			protected void populateItem(ListItem item) {
                MainPageInfo page = (MainPageInfo) item.getModelObject();
                
                BookmarkablePageLink link = new BookmarkablePageLink( "page", page.getComponentClass() ) ;
                link.add( new Label( "label", page.getTitleKey() ) );
                item.add(link);
            }
        };
        
        add( view );
        
        List<ResourceInfo> resources = getGeoServer().getCatalog().getResources(ResourceInfo.class);
        view = new ListView("resources", resources){
        	@Override
        	protected void populateItem(ListItem item) {
        		final ResourceInfo info = (ResourceInfo)item.getModelObject();
        		Link link = new Link("resourcelink"){
        			@Override
        			public void onClick() {
        				setResponsePage(new ResourceConfigurationPage(info));
        			}
        		};
        		link.add(new Label("resourcelabel", info.getId()));
        		item.add(link);
        	}
        };
        add(view);
    }
}
