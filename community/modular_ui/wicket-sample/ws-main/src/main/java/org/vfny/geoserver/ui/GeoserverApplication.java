package org.vfny.geoserver.ui;

import wicket.markup.html.PackageResource;
import wicket.protocol.http.WebApplication;

import com.sun.org.apache.xalan.internal.xsltc.compiler.Pattern;

public class GeoserverApplication extends WebApplication {

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

}
