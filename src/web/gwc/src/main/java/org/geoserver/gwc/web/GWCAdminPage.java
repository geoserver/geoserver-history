package org.geoserver.gwc.web;

import org.apache.wicket.markup.html.pages.RedirectPage;
import org.geoserver.web.GeoServerSecuredPage;

public class GWCAdminPage extends GeoServerSecuredPage {
    
    public GWCAdminPage() {
        setResponsePage(new RedirectPage("../gwc"));
    }

}
