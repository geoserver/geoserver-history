/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.config;

/**
 * DOCUMENT ME!
 *
 * @author Gabriel Roldán
 * @version $Id: NameSpace.java,v 1.1.2.2 2003/11/14 20:39:14 groldan Exp $
 */
public class NameSpace {
    /** DOCUMENT ME!  */
    public static final String PREFIX_DELIMITER = ":";

    /** DOCUMENT ME! */
    private String prefix;

    /** DOCUMENT ME! */
    private String uri;

    /** DOCUMENT ME! */
    private boolean _default;

    /**
     * Creates a new NameSpace object.
     *
     * @param prefix DOCUMENT ME!
     * @param uri DOCUMENT ME!
     */
    public NameSpace(String prefix, String uri) {
        this(prefix, uri, false);
    }

    /**
     * Creates a new NameSpace object.
     *
     * @param prefix DOCUMENT ME!
     * @param uri DOCUMENT ME!
     * @param isDefault DOCUMENT ME!
     */
    public NameSpace(String prefix, String uri, boolean isDefault) {
        this.prefix = prefix;
        this.uri = uri;
        this._default = isDefault;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isDefault() {
        return _default;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getPrefix() {
        return prefix;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getUri() {
        return uri;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        return "xmlns:" + getPrefix() + "=\"" + getUri() + "\"";
    }

    /**
     * DOCUMENT ME!
     *
     * @param o DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean equals(Object o) {
        if (!(o instanceof NameSpace)) {
            return false;
        }

        NameSpace ns = (NameSpace) o;

        return getPrefix().equals(ns.getPrefix())
        && getUri().equals(ns.getUri());
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int hashCode() {
        return (17 * getPrefix().hashCode()) + (21 * getUri().hashCode());
    }
}
