/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.global;

import java.util.Locale;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;


/**
 * Represents a User for GeoServer.
 * 
 * <p>
 * Used as a typesafe Session container. This is an alternative to using calls
 * to request.getAttributes( key ) and casting.
 * </p>
 * 
 * <p>
 * The User object is saved in session scope by ConfigAction:
 * </p>
 * <pre><code>
 * HttpSession session = request.getSession();
 * User user = request.getAttributes( UserContainer.WEB_CONTAINER_KEY );
 * if( user == null ){
 *     user = new UserContainer( request.getLocal() );
 *     session.setAttributes( UserContainer.WEB_CONTAINER_KEY, user );
 * }
 * </code></pre>
 * 
 * <p>
 * This class is based on the UserContainer class outlined in the book
 * "Programming Jakarta Struts" by Chuck Cavaness.
 * </p>
 *
 * @author jgarnett, Refractions Research, Inc.
 * @author $Author: dmzwiers $ (last modification)
 * @version $Id: UserContainer.java,v 1.1.2.3 2004/01/09 17:15:30 dmzwiers Exp $
 */
public class UserContainer implements HttpSessionBindingListener {
    public final static String SESSION_KEY = "GEOSERVER.USER";

    /** User's locale */
    private Locale locale;

    /** User name for this user */
    public String username;

    public UserContainer() {
        this(Locale.getDefault());
    }

    public UserContainer(Locale local) {
    }

    /**
     * User's Locale.
     * 
     * <p>
     * Used to format messages. Should be used in conjunction with
     * internatalization support.
     * </p>
     *
     * @return Locale for the User.
     */
    public Locale getLocale() {
        return locale;
    }

    /**
     * Set the user's Locale.
     *
     * @param locale User's locale.
     */
    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    /**
     * Session callback.
     *
     * @param arg0
     *
     * @see javax.servlet.http.HttpSessionBindingListener#valueBound(javax.servlet.http.HttpSessionBindingEvent)
     */
    public void valueBound(HttpSessionBindingEvent arg0) {
        // not needed
    }

    /**
     * Clean up user resources when unbound from session.
     *
     * @param arg0
     *
     * @see javax.servlet.http.HttpSessionBindingListener#valueUnbound(javax.servlet.http.HttpSessionBindingEvent)
     */
    public void valueUnbound(HttpSessionBindingEvent arg0) {
        cleanUp();
    }

    /**
     * Clean up user resources.
     */
    private void cleanUp() {
        locale = null;
    }
}
