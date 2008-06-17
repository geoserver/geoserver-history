package org.geoserver.web;

import java.util.List;

import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;

public class GeoServerHomePage extends GeoServerBasePage {

    public GeoServerHomePage() {
        
        //TODO: ensure order by adding the well-known pages manually and then 
        // processing any remaining pages
        final List<MainPageInfo> pages = ((GeoServerApplication) getApplication()).getBeansOfType(MainPageInfo.class);
        ListView view = new ListView( "pages" ) {

            protected void populateItem(ListItem item) {
                int i = item.getIndex();
                MainPageInfo page = pages.get( i );
                item.add( new BookmarkablePageLink( "page" + i, page.getComponentClass() ) );
            }
        };
        
        add( view );
    }
}
