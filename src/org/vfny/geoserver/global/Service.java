/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.global;

import org.vfny.geoserver.global.dto.ServiceDTO;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;


/**
 * Default configuration for services. This class represents all the
 * commonalities to the WFS and WMS services.
 * 
 * <p>
 * WFS wfs = new WFS(dto); Service serv = (Service)WFS;
 * System.out.println(serv.getName());
 * </p>
 *
 * @author Gabriel Roldán
 * @author Chris Holmes
 * @version $Id: Service.java,v 1.5 2004/02/09 18:02:20 dmzwiers Exp $
 *
 * @see WMS
 * @see WFS
 */
public abstract class Service extends GlobalLayerSupertype {

	private boolean enabled;
	private URL onlineResource;
	private String name;
	private String title;
	private String serverAbstract;
	private String[] keywords = new String[0];
	private String fees;
	private String accessConstraints;
	private String maintainer;

    /**
     * Service constructor.
     * 
     * <p>
     * Stores the new ServiceDTO data for this service.
     * </p>
     *
     * @param config
     *
     * @throws NullPointerException when the param is null
     */
    public Service(ServiceDTO dto) {
        if (dto == null) {
            throw new NullPointerException();
        }

        enabled = dto.isEnabled();
        name = dto.getName();
        title = dto.getTitle();
        serverAbstract = dto.getAbstract();
        keywords = dto.getKeywords();
        fees = dto.getFees();
        accessConstraints = dto.getAccessConstraints();
        maintainer = dto.getMaintainer();
        onlineResource = dto.getOnlineResource();
    }

    /**
     * isEnabled purpose.
     * 
     * <p>
     * Returns whether is service is enabled.
     * </p>
     *
     * @return true when enabled.
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * getOnlineResource purpose.
     * 
     * <p>
     * Returns the Online Resource for this Service.
     * </p>
     *
     * @return URL The Online resource.
     */
    public URL getOnlineResource() {
        return onlineResource;
    }

    /**
     * getAbstract purpose.
     * 
     * <p>
     * A description of this service.
     * </p>
     *
     * @return String This Service's abstract.
     */
    public String getAbstract() {
        return serverAbstract;
    }

    /**
     * getAccessConstraints purpose.
     * 
     * <p>
     * A description of this service's access constraints.
     * </p>
     *
     * @return String This service's access constraints.
     */
    public String getAccessConstraints() {
        return accessConstraints;
    }

    /**
     * getFees purpose.
     * 
     * <p>
     * A description of the fees for this service.
     * </p>
     *
     * @return String the fees for this service.
     */
    public String getFees() {
        return fees;
    }

    /**
     * getKeywords purpose.
     * 
     * <p>
     * A list of the keywords for this service.
     * </p>
     *
     * @return List keywords for this service
     */
    public List getKeywords() {
        LinkedList ll = new LinkedList();
        String[] s = keywords;

        for (int i = 0; i < s.length; i++)
            ll.add(s[i]);

        return ll;
    }

    /**
     * getMaintainer purpose.
     * 
     * <p>
     * The name of the maintainer for this service.
     * </p>
     *
     * @return String maintainer for this service.
     */
    public String getMaintainer() {
        return maintainer;
    }

    /**
     * getName purpose.
     * 
     * <p>
     * The name for this service.
     * </p>
     *
     * @return String the service's name.
     */
    public String getName() {
        return name;
    }

    /**
     * getTitle purpose.
     * 
     * <p>
     * The title for this service.
     * </p>
     *
     * @return String the service's title.
     */
    public String getTitle() {
        return title;
    }
    
    Object toDTO(){
    	ServiceDTO dto = new ServiceDTO();
    	dto.setAccessConstraints(accessConstraints);
    	dto.setEnabled(enabled);
    	dto.setFees(fees);
    	dto.setKeywords(keywords);
    	dto.setMaintainer(maintainer);
    	dto.setName(name);
    	dto.setOnlineResource(onlineResource);
    	dto.setAbstract(serverAbstract);
    	dto.setTitle(title);
    	return dto;
    }
}
