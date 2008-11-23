package org.geoserver.web.demo;

import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.basic.Label;
import org.geoserver.web.GeoServerBasePage;
import org.geotools.referencing.CRS;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

public class SRSDescriptionPage extends GeoServerBasePage {
    
    public SRSDescriptionPage(PageParameters params) {
        String code = params.getString("code");
        add(new Label("code", code));
        String description = "";
        try {
            description = CRS.getAuthorityFactory(true).
                getDescriptionText(code).toString(getLocale());
        } catch(Exception e) {
            //
        }
        add(new Label("description", description));
        CoordinateReferenceSystem crs = null;
        try {
            crs = CRS.decode(code);
        } catch(Exception e) {
            //
        }
        String wkt = "";
        if(crs != null) {
            wkt = crs.toString();
        }
        add(new Label("wkt", wkt));
    }
}
