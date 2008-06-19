package org.geoserver.web;

/**
 * Extension point for the main pages of the GeoServer web application.
 * <p>
 * A main application page is one that is reachable directly from the
 * {@linkplain GeoServerHomePage home page}, and from the navigation bar of each
 * regular application page.  
 * </p>
 * @author Andrea Aime, The Open Planning Project
 * @author Justin Deoliveira, The Open Planning Project
 *
 */
public class MainPageInfo extends ComponentInfo<GeoServerBasePage> {
	private static final long serialVersionUID = 1L;
}
