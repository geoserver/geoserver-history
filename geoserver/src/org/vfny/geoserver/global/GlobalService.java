/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.global;

import org.vfny.geoserver.config.*;
import java.util.*;
/**
 * default configuration for services
 *
 * @author Gabriel Roldán
 * @author Chris Holmes
 * @version $Id: GlobalService.java,v 1.1.2.1 2004/01/03 00:20:15 dmzwiers Exp $
 */
public abstract class GlobalService{
	
	private ServiceConfig config;

    /** DOCUMENT ME! */
    protected String URL;
    
    public GlobalService(ServiceConfig config){
    	this.config = config;
    	URL = "";
    }
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isEnabled() {
        return config.isEnabled();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getOnlineResource() {
        return config.getOnlineResource();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    //public abstract String getServiceType();

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getURL() {
        return URL;
    }

    /**
     * Gets the base schema url.
     *
     * @return The url to use as the base for schema locations.
     *
     * @deprecated Use GlobalData.getSchemaBaseUrl()
     */
    public String getSchemaBaseUrl() {
        return GlobalServer.getInstance().getGlobalData().getSchemaBaseUrl();
    }
    
	/**
		 * DOCUMENT ME!
		 *
		 * @return DOCUMENT ME!
		 */
		public String getAbstract() {
			return config.getAbstract();
		}

		/**
		 * DOCUMENT ME!
		 *
		 * @return DOCUMENT ME!
		 */
		public String getAccessConstraints() {
			return config.getAccessConstraints();
		}

		/**
		 * DOCUMENT ME!
		 *
		 * @return DOCUMENT ME!
		 */
		public String getFees() {
			return config.getFees();
		}

		/**
		 * DOCUMENT ME!
		 *
		 * @return DOCUMENT ME!
		 */
		public List getKeywords() {
			return config.getKeywords();
		}

		/**
		 * DOCUMENT ME!
		 *
		 * @return DOCUMENT ME!
		 */
		public String getMaintainer() {
			return config.getMaintainer();
		}

		/**
		 * DOCUMENT ME!
		 *
		 * @return DOCUMENT ME!
		 */
		public String getName() {
			return config.getName();
		}

		/**
		 * DOCUMENT ME!
		 *
		 * @return DOCUMENT ME!
		 */
		public String getTitle() {
			return config.getTitle();
		}
}
