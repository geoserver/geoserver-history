package org.geoserver.web.wicket;

import org.apache.wicket.markup.html.WebPage;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

public class CRSPanelTestPage extends WebPage {

    public CRSPanelTestPage(CoordinateReferenceSystem crs) {
        add( new CRSPanel( "content", crs ) );
    }
}
