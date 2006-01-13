/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.config;

import java.net.URL;

import org.vfny.geoserver.global.MetaDataLink;
import org.vfny.geoserver.global.dto.ServiceDTO;


/**
 * ServiceConfig purpose.
 * 
 * <p>
 * ServiceConfig is intended to be extended to provide some basic data storage
 * facilities.  This class represents the basic properties of a web service.
 * </p>
 * 
 * <p></p>
 *
 * @author dzwiers, Refractions Research, Inc.
 * @author $Author: Alessio Fabiani (alessio.fabiani@gmail.com) $ (last modification)
 * @author $Author: Simone Giannecchini (simboss1@gmail.com) $ (last modification)
 * @version $Id: ServiceConfig.java,v 1.16 2004/02/09 23:30:05 dmzwiers Exp $
 */
public class ServiceConfig {

	/**
	 * Represents when the web service is enabled/disabled. True when enabled.
	 * 
	 * @uml.property name="enabled" multiplicity="(0 1)"
	 */
	private boolean enabled = true;

	/**
	 * Online Reference URL for the web service. A location to look for when
	 * additional assistance is required.
	 * 
	 * @uml.property name="onlineResource" multiplicity="(0 1)"
	 */
	private URL onlineResource;

	/**
	 * The name of the service.
	 * 
	 * @uml.property name="name" multiplicity="(0 1)"
	 */
	private String name;

	/**
	 * The title of the service.
	 * 
	 * @uml.property name="title" multiplicity="(0 1)"
	 */
	private String title;


    /** A short abstract about the service. */
    private String _abstract;

	/**
	 * A list of keywords associated with the service.
	 * 
	 * @uml.property name="keywords" multiplicity="(0 1)"
	 */
	private String[] keywords;

	/**
	 * The fees associated with the service. When there are not any fees, the
	 * value  "NONE" is used.
	 * 
	 * @uml.property name="fees" multiplicity="(0 1)"
	 */
	private String fees;

	/**
	 * The access constraints associated with the service. When there are not
	 * any,  the value "NONE" is used.
	 * 
	 * @uml.property name="accessConstraints" multiplicity="(0 1)"
	 */
	private String accessConstraints = "NONE";

	/**
	 * Name of the person who maintains the web service. Should ideally be
	 * contact  information such as webmaster&amp;geoserver.org .
	 * 
	 * @uml.property name="maintainer" multiplicity="(0 1)"
	 */
	private String maintainer;

	/**
	 * 
	 * @uml.property name="metadataLink"
	 * @uml.associationEnd multiplicity="(0 1)"
	 */
	private MetaDataLink metadataLink;

    /**
     * ServiceConfig constructor.
     * 
     * <p>
     * Creates an empty ServiceConfig representation with default values.
     * </p>
     *
     * @see defaultSettings()
     */
    public ServiceConfig() {
        enabled = true;
        name = "";
        title = "";
        _abstract = "";
        keywords = new String[0];
        fees = "";
        accessConstraints = "NONE";
        maintainer = "";
        metadataLink = null;
    }

    /**
     * ServiceConfig constructor.
     * 
     * <p>
     * This is equivalent to calling the load method. When a null value is
     * passed in,  the default values are used. All non-primary datatypes are
     * cloned with the  exception of Strings (which have a singleton hash
     * table in memory representation).
     * </p>
     *
     * @param dto The ServiceDTO object to copy into the new ServiceConfig
     *        object.
     *
     * @throws NullPointerException if dto was null;
     */
    public ServiceConfig(ServiceDTO dto) {
        if (dto == null) {
            throw new NullPointerException("Non null ServiceDTO required");
        }

        update(dto);
    }

    /**
     * Implement loadDTO.
     * 
     * <p>
     * Takes a ServiceDTO and loads it into this ServiceConfig Object
     * </p>
     *
     * @param dto an instance of ServiceDTO
     *
     * @throws NullPointerException if dto is null
     *
     * @see org.vfny.geoserver.config.DataStructure#loadDTO(java.lang.Object)
     */
    public void update(ServiceDTO dto) {
        if (dto == null) {
            throw new NullPointerException(
                "Service Data Transfer Object required");
        }

        ServiceDTO s = (ServiceDTO) dto;
        enabled = s.isEnabled();
        name = s.getName();
        title = s.getTitle();
        _abstract = s.getAbstract();

        try {
            keywords = new String[s.getKeywords().length];

            for (int i = 0; i < keywords.length; i++)
                keywords[i] = s.getKeywords()[i];
        } catch (Exception e) {
            // should only happen for null
            keywords = new String[0];
        }

        fees = s.getFees();
        accessConstraints = s.getAccessConstraints();
        maintainer = s.getMaintainer();
        onlineResource = s.getOnlineResource();
        metadataLink = s.getMetadataLink();
    }

    /**
     * Implement toDTO.
     * 
     * <p>
     * Returns a copy of the data in a ServiceDTO object
     * </p>
     *
     * @return a copy of the data in a ServiceDTO object
     *
     * @see org.vfny.geoserver.config.DataStructure#toDTO()
     */

    // name needed to not match as the DTOs do not follow the same inheritance struture.
    public ServiceDTO toServDTO() {
        ServiceDTO sDto = new ServiceDTO();
        sDto.setAbstract(_abstract);
        sDto.setAccessConstraints(accessConstraints);
        sDto.setEnabled(enabled);
        sDto.setFees(fees);

        String[] s = new String[keywords.length];

        for (int i = 0; i < keywords.length; i++)
            s[i] = keywords[i];

        sDto.setKeywords(s);
        sDto.setMaintainer(maintainer);
        sDto.setName(name);
        sDto.setOnlineResource(onlineResource);
        sDto.setTitle(title);
        sDto.setMetadataLink(metadataLink);

        return sDto;
    }

	/**
	 * getName purpose.
	 * 
	 * @return
	 * 
	 * @uml.property name="name"
	 */
	public String getName() {
		return name;
	}

	/**
	 * getOnlineResource purpose.
	 * 
	 * @return
	 * 
	 * @uml.property name="onlineResource"
	 */
	public URL getOnlineResource() {
		return onlineResource;
	}

	/**
	 * getTitle purpose.
	 * 
	 * @return
	 * 
	 * @uml.property name="title"
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * setName purpose.
	 * 
	 * @param string
	 * 
	 * @uml.property name="name"
	 */
	public void setName(String string) {
		name = string;
	}

	/**
	 * setOnlineResource purpose.
	 * 
	 * @param url
	 * 
	 * @uml.property name="onlineResource"
	 */
	public void setOnlineResource(URL url) {
		onlineResource = url;
	}

	/**
	 * setTitle purpose.
	 * 
	 * @param string
	 * 
	 * @uml.property name="title"
	 */
	public void setTitle(String string) {
		title = string;
	}


    /**
     * getAbstract purpose.
     *
     * @return
     */
    public String getAbstract() {
        return _abstract;
    }

	/**
	 * getAccessConstraints purpose.
	 * 
	 * @return
	 * 
	 * @uml.property name="accessConstraints"
	 */
	public String getAccessConstraints() {
		return accessConstraints;
	}


    /**
     * isEnabled purpose.
     *
     * @return
     */
    public boolean isEnabled() {
        return enabled;
    }

	/**
	 * getFees purpose.
	 * 
	 * @return
	 * 
	 * @uml.property name="fees"
	 */
	public String getFees() {
		return fees;
	}

	/**
	 * getKeywords purpose.
	 * 
	 * @return
	 * 
	 * @uml.property name="keywords"
	 */
	public String[] getKeywords() {
		return keywords;
	}

	/**
	 * getMaintainer purpose.
	 * 
	 * @return
	 * 
	 * @uml.property name="maintainer"
	 */
	public String getMaintainer() {
		return maintainer;
	}


    /**
     * setAbstract purpose.
     *
     * @param string
     */
    public void setAbstract(String string) {
        _abstract = string;
    }

	/**
	 * setAccessConstraints purpose.
	 * 
	 * @param string
	 * 
	 * @uml.property name="accessConstraints"
	 */
	public void setAccessConstraints(String string) {
		accessConstraints = string;
	}

	/**
	 * setEnabled purpose.
	 * 
	 * @param b
	 * 
	 * @uml.property name="enabled"
	 */
	public void setEnabled(boolean b) {
		enabled = b;
	}

	/**
	 * setFees purpose.
	 * 
	 * @param string
	 * 
	 * @uml.property name="fees"
	 */
	public void setFees(String string) {
		fees = string;
	}

	/**
	 * setKeywords purpose.
	 * 
	 * @param list
	 * 
	 * @uml.property name="keywords"
	 */
	public void setKeywords(String[] list) {
		keywords = list;
	}

	/**
	 * setMaintainer purpose.
	 * 
	 * @param string
	 * 
	 * @uml.property name="maintainer"
	 */
	public void setMaintainer(String string) {
		maintainer = string;
	}

	/**
	 * @return Returns the metadataLink.
	 * 
	 * @uml.property name="metadataLink"
	 */
	public MetaDataLink getMetadataLink() {
		return metadataLink;
	}

	/**
	 * @param metadataLink The metadataLink to set.
	 * 
	 * @uml.property name="metadataLink"
	 */
	public void setMetadataLink(MetaDataLink metadataLink) {
		this.metadataLink = metadataLink;
	}

}
