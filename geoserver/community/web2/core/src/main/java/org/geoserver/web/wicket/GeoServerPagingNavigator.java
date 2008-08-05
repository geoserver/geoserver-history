/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.web.wicket;

import org.apache.wicket.ajax.markup.html.navigation.paging.AjaxPagingNavigator;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.navigation.paging.IPageable;

/**
 * A custom navigator that sets classes for the elements
 * @author Andrea Aime - TOPP
 *
 */
public class GeoServerPagingNavigator extends AjaxPagingNavigator {

    public GeoServerPagingNavigator(String id, IPageable pageable) {
        super(id, pageable);
    }
    
    @Override
    protected Link newPagingNavigationLink(String id, IPageable pageable, int pageNumber) {
        Link link = super.newPagingNavigationLink(id, pageable, pageNumber);
        // we turn the id into the css class
        link.add(new SimpleAttributeModifier("class", id));
        return link;
    }
    
    @Override
    protected Link newPagingNavigationIncrementLink(String id, IPageable pageable, int increment) {
        Link link = super.newPagingNavigationIncrementLink(id, pageable, increment);
        // we turn the id into the css class
        link.add(new SimpleAttributeModifier("class", id));
        return link;
    }
    
}