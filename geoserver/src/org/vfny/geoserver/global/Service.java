/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.global;

import java.net.URL;
import java.util.LinkedList;
import java.util.List;

import org.vfny.geoserver.global.dto.ServiceDTO;
/**
 * default configuration for services
 *
 * @author Gabriel Roldán
 * @author Chris Holmes
 * @version $Id: Service.java,v 1.1.2.5 2004/01/07 21:36:13 dmzwiers Exp $
 */
public abstract class Service extends Abstract{
	
	private ServiceDTO config;

    /** DOCUMENT ME! */
    //protected String URL;
    
    public Service(ServiceDTO config){
    	this.config = config;
    	//URL = "";
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
    public URL getOnlineResource() {
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
    /*public String getURL() {
        return URL;
    }*/

    /**
     * Gets the base schema url.
     *
     * @return The url to use as the base for schema locations.
     *
     * @deprecated Use GlobalData.getSchemaBaseUrl()
     */
    /*public String getSchemaBaseUrl() {
        return GeoServer.getInstance().getSchemaBaseUrl();
    }*/
    
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
			LinkedList ll = new LinkedList();
			String [] s = config.getKeywords();
			for(int i=0;i<s.length;i++)
				ll.add(s[i]);
			return ll;
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
