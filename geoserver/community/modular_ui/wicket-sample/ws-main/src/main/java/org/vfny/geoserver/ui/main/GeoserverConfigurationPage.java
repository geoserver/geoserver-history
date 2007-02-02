package org.vfny.geoserver.ui.main;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.vfny.geoserver.ui.GeoserverBasePage;
import org.vfny.geoserver.ui.plugin.DataConfigurationPageInfo;

public class GeoserverConfigurationPage extends GeoserverBasePage {

	protected String getHeaderTitle() {
		return "Data Configuration";
	}

	protected String getPageAbstract() {
		return "Configure GeoServer Application config.data access and representation";
	}

	protected String getPageTitle() {
		return "GeoServer Data Configuration";
	}

	protected List getSubPageLinks() {
		List result = new ArrayList();
		List infos = getGeoserverApplication().getBeansOfType(
				DataConfigurationPageInfo.class);
		for (Iterator it = infos.iterator(); it.hasNext();) {
			DataConfigurationPageInfo info = (DataConfigurationPageInfo) it
					.next();
			result.add(buildPageLink(info));

		}
		return result;
	}
}
