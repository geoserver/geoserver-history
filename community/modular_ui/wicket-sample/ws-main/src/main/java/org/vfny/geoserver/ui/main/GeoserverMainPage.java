package org.vfny.geoserver.ui.main;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.vfny.geoserver.ui.GeoserverBasePage;
import org.vfny.geoserver.ui.plugin.BookmarkablePageInfo;
import org.vfny.geoserver.ui.plugin.MainPageInfo;

public abstract class GeoserverMainPage extends GeoserverBasePage {

	protected List getSubPageLinks() {
		List result = buildLinksForPageType(MainPageInfo.class);
		result.add(buildPageLink(new BookmarkablePageInfo(
				GeoserverHomePage.class, "Home", "Home page")));
		return result;
	}

	
}
