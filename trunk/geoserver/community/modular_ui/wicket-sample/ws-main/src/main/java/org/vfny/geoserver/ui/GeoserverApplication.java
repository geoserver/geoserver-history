package org.vfny.geoserver.ui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.vfny.geoserver.ui.main.GeoserverHomePage;

import wicket.spring.SpringWebApplication;

public class GeoserverApplication extends SpringWebApplication {

	protected void init() {
		super.init();
		// make wicket remove wicket:id and <wicket:extends> and so
		// on so that generated html is clean xhtml that passes the
		// w3c validators
		getMarkupSettings().setStripWicketTags(true);
	}

	public Class getHomePage() {
		return GeoserverHomePage.class;
	}

	public List getBeansOfType(Class clazz) {
		ApplicationContext ac = internalGetApplicationContext();
		Map map = ac.getBeansOfType(clazz);
		List beans = new ArrayList();
		for (Iterator it = map.values().iterator(); it.hasNext();) {
			beans.add(it.next());
		}
		return beans;
	}

}
