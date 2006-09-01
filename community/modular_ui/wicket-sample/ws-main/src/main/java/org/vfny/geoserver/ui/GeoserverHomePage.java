package org.vfny.geoserver.ui;

import java.util.ArrayList;
import java.util.List;

public class GeoserverHomePage extends GeoserverBasePage {

	protected String getPageTitle() {
		return "This is a main title";
	}

	protected String getPageAbstract() {
		return "This is an abstract";
	}

	protected List getSubPageLinks() {
		// here some kind of introspection should look up the
		// "main page" classes
		List links = new ArrayList();

		links.add(buildPageLink(GeoserverHomePage.class, "Home",
				"Geoserver administration"));
		links.add(buildPageLink(GeoserverDemoPage.class, "Demo",
				"Geoserver demos"));
		links.add(buildExternalLink("http://www.google.com", "Google",
				"Go to Google"));
		return links;
	}

	protected String getHeaderTitle() {
		return "Geoserver rocks";
	}

	protected String getKeywords() {
		return "keywords";
	}

	protected String getPageDescription() {
		return "This is the page description";
	}
}
