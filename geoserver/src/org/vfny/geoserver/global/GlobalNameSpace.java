/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.global;

import org.vfny.geoserver.config.data.*;
/**
 * DOCUMENT ME!
 *
 * @author Gabriel Roldán
 * @version $Id: GlobalNameSpace.java,v 1.1.2.1 2004/01/03 00:20:15 dmzwiers Exp $
 */
public class GlobalNameSpace {
    /** DOCUMENT ME!  */
    public static final String PREFIX_DELIMITER = ":";

	private NameSpaceConfig nsc;
   
	public GlobalNameSpace(NameSpaceConfig config){
		nsc = config;
	}

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getPrefix() {
        return nsc.getPrefix();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getUri() {
        return nsc.getUri();
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
    	if(o == null)
    		return false;
        if (!(o instanceof GlobalNameSpace)) {
            return false;
        }

        GlobalNameSpace ns = (GlobalNameSpace) o;

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
