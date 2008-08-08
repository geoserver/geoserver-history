/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.web;


/**
 * Base class for secured web pages. By default it only allows
 * 
 * @author Andrea Aime - TOPP
 * 
 */
public class GeoServerSecuredPage extends GeoServerBasePage {

    public static final PageAuthorizer DEFAULT_AUTHORIZER = new DefaultPageAuthorizer();

    public GeoServerSecuredPage() {
        super();
        if (!getPageAuthorizer().isAccessAllowed(this.getClass(), getSession().getAuthentication()))
            setResponsePage(new UnauthorizedPage());
    }

    /**
     * Override to use a page authorizer other than the default one. When you do
     * so, remember to perform the same change in the associated
     * {@link MenuPageInfo} instance
     * 
     * @return
     */
    protected PageAuthorizer getPageAuthorizer() {
        return DEFAULT_AUTHORIZER;
    }
}
