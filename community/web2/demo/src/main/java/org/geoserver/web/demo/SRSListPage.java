/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.web.demo;

import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.geoserver.web.GeoServerBasePage;
import org.geoserver.web.wicket.SRSListPanel;

/**
 * Lists all the SRS available in GeoServer
 */
@SuppressWarnings("serial")
public class SRSListPage extends GeoServerBasePage {

    public SRSListPage() {
        add( srsListPanel() );
    }

    SRSListPanel srsListPanel() {
        return new SRSListPanel( "srsListPanel") {
            @Override
            protected AbstractLink createLinkForCode(String linkId, String epsgCode) {
                return new BookmarkablePageLink("codeLink",  SRSDescriptionPage.class, 
                    new PageParameters("code=EPSG:" + epsgCode));
            }
        };
    }
}
