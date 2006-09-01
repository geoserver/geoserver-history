package org.vfny.geoserver.ui;

import java.util.Collections;
import java.util.List;

public class GeoserverDemoPage extends GeoserverBasePage {

	protected String getHeaderTitle() {
		return "Demo";
	}

	protected String getPageAbstract() {
		return "Here is the demo page for Geoserver. Look here for examples on how to use Geoserver on its own and with other tools.";
	}

	protected String getPageTitle() {
		return "Demo";
	}

	protected List getSubPageLinks() {
		return Collections.emptyList();
	}

}
