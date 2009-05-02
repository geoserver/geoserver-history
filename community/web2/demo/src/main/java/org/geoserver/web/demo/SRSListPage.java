/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.web.demo;

import org.apache.wicket.Component;
import org.apache.wicket.PageParameters;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.model.IModel;
import org.geoserver.web.GeoServerBasePage;
import org.geoserver.web.wicket.SRSListPanel;
import org.geoserver.web.wicket.SRSProvider;
import org.geoserver.web.wicket.SimpleAjaxLink;

/**
 * Lists all the SRS available in GeoServer
 */
@SuppressWarnings("serial")
public class SRSListPage extends GeoServerBasePage {

    public SRSListPage() {
        add(srsListPanel());
    }

    SRSListPanel srsListPanel() {
        return new SRSListPanel("srsListPanel") {
            @Override
            protected Component createLinkForCode(String linkId, IModel itemModel) {
                return new SimpleAjaxLink(linkId, SRSProvider.CODE.getModel(itemModel)) {
                    @Override
                    public void onClick(AjaxRequestTarget target) {
                        String epsgCode = getModelObjectAsString();
                        setResponsePage(SRSDescriptionPage.class, new PageParameters("code=EPSG:"
                                + epsgCode));
                    }
                };
            }
        };
    }
}
