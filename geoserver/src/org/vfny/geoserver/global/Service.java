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
 * Default configuration for services. This class represents all the 
 * commonalities to the WFS and WMS services.
 * 
 * <p>
 * WFS wfs = new WFS(dto);
 * Service serv = (Service)WFS;
 * System.out.println(serv.getName());
 * </p>
 * 
 * @see WMS
 * @see WFS
 *
 * @author Gabriel Roldán
 * @author Chris Holmes
 * @version $Id: Service.java,v 1.1.2.6 2004/01/08 23:44:48 dmzwiers Exp $
 */
public abstract class Service extends GlobalLayerSupertype{
	
	/**
	 * The DTO for this instance. When Sub-classes are using config they should use Caution.
	 */
	protected ServiceDTO config;

    /**
     * Service constructor.
     * <p>
     * Stores the new ServiceDTO data for this service.
     * </p>
     * @param config
     */
    public Service(ServiceDTO config){
    	if(config == null)
    		throw new NullPointerException();
    	this.config = config;
    }
    
    /**
     * isEnabled purpose.
     * <p>
     * Returns whether is service is enabled.
     * </p>
     * @return true when enabled.
     */
    public boolean isEnabled() {
        return config.isEnabled();
    }

    /**
     * getOnlineResource purpose.
     * <p>
     * Returns the Online Resource for this Service. 
     * </p>
     * @return URL The Online resource.
     */
    public URL getOnlineResource() {
        return config.getOnlineResource();
    }

    /**
     * getAbstract purpose.
     * <p>
     * A description of this service.
     * </p>
     * @return String This Service's abstract.
     */
	public String getAbstract() {
		return config.getAbstract();
	}

	/**
	 * getAccessConstraints purpose.
	 * <p>
	 * A description of this service's access constraints.
	 * </p>
	 * @return String This service's access constraints.
	 */
	public String getAccessConstraints() {
		return config.getAccessConstraints();
	}

	/**
	 * getFees purpose.
	 * <p>
	 * A description of the fees for this service.
	 * </p>
	 * @return String the fees for this service.
	 */
	public String getFees() {
		return config.getFees();
	}

	/**
	 * getKeywords purpose.
	 * <p>
	 * A list of the keywords for this service.
	 * </p>
	 * @return List keywords for this service
	 */
	public List getKeywords() {
		LinkedList ll = new LinkedList();
		String [] s = config.getKeywords();
		for(int i=0;i<s.length;i++)
			ll.add(s[i]);
		return ll;
	}

	/**
	 * getMaintainer purpose.
	 * <p>
	 * The name of the maintainer for this service.
	 * </p>
	 * @return String maintainer for this service.
	 */
	public String getMaintainer() {
		return config.getMaintainer();
	}

	/**
	 * getName purpose.
	 * <p>
	 * The name for this service.
	 * </p>
	 * @return String the service's name.
	 */
	public String getName() {
		return config.getName();
	}

	/**
	 * getTitle purpose.
	 * <p>
	 * The title for this service.
	 * </p>
	 * @return String the service's title.
	 */
	public String getTitle() {
		return config.getTitle();
	}
}
