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
import org.geoserver.config.ServiceInfo;
import org.geotools.util.Version;

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
                ServiceInfo service = (ServiceInfo) item.getModelObject();
                final String serviceId = service.getId();
                item.add( new Label("service", service.getId().toUpperCase()) );
                item.add( new ListView( "versions", service.getVersions()) {
                    @Override
                    protected void populateItem(ListItem item) {
                        Version version = (Version) item.getModelObject();
                        ExternalLink link = new ExternalLink("link", "../ows?service=" + serviceId
                                + "&version=" + version.toString() + "&request=GetCapabilities");
                        item.add( link );
                        
                        link.add( new Label( "version", version.toString() ) );
                    }
                });
            }
        };
        add(view);
    }

    private IModel getServices() {
        return new LoadableDetachableModel() {
            @Override
            protected Object load() {
                return getGeoServerApplication().getGeoServer().getServices();
            }
        };
    }
}
