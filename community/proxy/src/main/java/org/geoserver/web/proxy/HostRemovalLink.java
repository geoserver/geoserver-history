/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.web.proxy;

import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.geoserver.catalog.CascadeDeleteVisitor;
import org.geoserver.catalog.Catalog;
import org.geoserver.catalog.CatalogInfo;
import org.geoserver.proxy.ProxyConfig;
import org.geoserver.web.GeoServerApplication;
import org.geoserver.web.wicket.GeoServerDialog;
import org.geoserver.web.wicket.GeoServerTablePanel;

/**
 * A reusable cascading, multiple removal link. Assumes the presence of a table
 * panel filled with catalog objects and a {@link GeoServerDialog} to be used
 * for reporting the objects that will be affected by the removal
 */
@SuppressWarnings("serial")
public class HostRemovalLink extends AjaxLink {
    
    GeoServerTablePanel<String> tableObjects;
    ProxyConfig config;

    public HostRemovalLink(String id, GeoServerTablePanel<String> tableObjects, ProxyConfig config) {
        super(id);
        this.tableObjects = tableObjects;
        this.config = config;
    }

    @Override
    public void onClick(AjaxRequestTarget target) {
        // see if the user selected anything
        final List<String> selection = tableObjects.getSelection();
        if(selection.size() == 0)
            return;
        
        //remove selected hostnames from list
        for (String hostname : selection) {
            config.hostnameWhitelist.remove(hostname);
        }
        //write changes to disk
        ProxyConfig.writeConfigToDisk(config);
        
        //disable the removal link, since nothing is selected any more
        setEnabled(false);
        target.addComponent(HostRemovalLink.this);
    }
}   