/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.web;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.geoserver.platform.Service;

/**
 * Hope page, shows just the introduction and the capabilities link
 * 
 * @author Andrea Aime - TOPP
 * 
 */
public class GeoServerHomePage extends GeoServerBasePage {

    public GeoServerHomePage() {
        // when hacking this service listing code please refer to 
        // http://jira.codehaus.org/browse/GEOS-2114
        ListView view = new ListView("services", getServices()) {

            @Override
            protected void populateItem(ListItem item) {
                Service s = (Service) item.getModelObject();
                ExternalLink link = new ExternalLink("link", "../ows?service=" + s.getId()
                        + "&version=" + s.getVersion() + "&request=GetCapabilities");
                link.add(new Label("serviceId", s.getId().toUpperCase() + " " + s.getVersion()));
                item.add(link);
            }

        };
        add(view);
    }

    private IModel getServices() {
        return new LoadableDetachableModel() {
            @Override
            protected Object load() {
                return getGeoServerApplication().getBeansOfType(Service.class);
            }
        };
    }
}
