/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.global.dto;

import java.net.URL;
import java.util.Arrays;

import org.vfny.geoserver.global.MetaDataLink;


/**
 * Data Transfer Object representing GeoServer Service information.
 * 
 * <p>
 * ServiceConfig is intended to be extended to provide some basic data storage
 * facilities.  This class represents the basic properties of a web service.
 * </p>
 * 
 * <p>
 * Data Transfer object are used to communicate between the GeoServer
 * application and its configuration and persistent layers. As such the class
 * is final - to allow for its future use as an on-the-wire message.
 * </p>
 * 
 * <p>
 * It is very tempting to force Web Services to completely define their own DTO
 * elements (rather than trying for reuse using subclassing or aggregation) -
 * simply to force each service to document what <b>it</b> means by each of
 * these peices of information.
 * </p>
 *
 * @author dzwiers, Refractions Research, Inc.
 * @author $Author: Alessio Fabiani (alessio.fabiani@gmail.com) $ (last modification)
 * @author $Author: Simone Giannecchini (simboss_ml@tiscali.it) $ (last modification)
 * @version $Id: ServiceDTO.java,v 1.4 2004/01/31 00:27:26 jive Exp $
 */
public final class ServiceDTO implements DataTransferObject {

	/**
	 * Represents when the Web Service is enabled/disabled.
	 * 
	 * <p>
	 * Example: <code>true</code>
	 * </p>
	 * 
	 * @uml.property name="enabled" multiplicity="(0 1)"
	 */
	private boolean enabled;

	/**
	 * Online Reference URL for the web service.
	 * 
	 * <p>
	 * A location to look for when additional assistance is required.
	 * </p>
	 * 
	 * <p>
	 * Example: <code>new URL("http://www.openplans.org/")</code>
	 * </p>
	 * 
	 * @uml.property name="onlineResource" multiplicity="(0 1)"
	 */
	private URL onlineResource;

	/**
	 * The name of the service.
	 * 
	 * <p>
	 * Example: <code>FreeWFS</code>
	 * </p>
	 * 
	 * <p>
	 * It is not clear from the examples if this name allows whitespace?
	 * </p>
	 * 
	 * @uml.property name="name" multiplicity="(0 1)"
	 */
	private String name;

	/**
	 * The title of the service.
	 * 
	 * <p>
	 * Example: <code>The Open Planning Project Basemap Server</code>
	 * </p>
	 * 
	 * @uml.property name="title" multiplicity="(0 1)"
	 */
	private String title;


    /**
     * A short abstract about the service.
     * 
     * <p>
     * Example:
     * <pre><code>
     * This is a test server.  It contains some basemap data from New York City.
     * </code></pre>
     * </p>
     */
    private String serverAbstract;

	/**
	 * A list of keywords associated with the service.
	 * 
	 * <p>
	 * Example: <code>new String[]{"WFS","New York"}</code>
	 * </p>
	 * 
	 * @uml.property name="keywords" multiplicity="(0 1)"
	 */
	private String[] keywords = new String[0];

	/**
	 * The fees associated with the service.
	 * 
	 * <p>
	 * When there are not any fees, the value  "NONE" is used.
	 * </p>
	 * 
	 * <p>
	 * Example: <code>NONE</code>
	 * </p>
	 * 
	 * @uml.property name="fees" multiplicity="(0 1)"
	 */
	private String fees;

	/**
	 * The access constraints associated with the service. When there are not
	 * any, the value "NONE" is used.
	 * 
	 * <p>
	 * Example: <code>"NONE"</code>
	 * </p>
	 * 
	 * @uml.property name="accessConstraints" multiplicity="(0 1)"
	 */
	private String accessConstraints;

	/**
	 * Name of the person who maintains the web service. Should ideally be
	 * contact  information such as a email address.
	 * 
	 * <p>
	 * Example: <code>"The Open Planning Project"</code>
	 * </p>
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
     * does nothing
     * </p>
     *
     * @see defaultSettings()
     */
    public ServiceDTO() {
    }

    /**
     * ServiceConfig constructor.
     * 
     * <p>
     * This is equivalent to calling the clone method. When a null value is
     * passed in,  the default values are used. All non-primary datatypes are
     * cloned with the  exception of Strings (which have a singleton hash
     * table in memory representation).
     * </p>
     *
     * @param dto The ServiceConfig object to copy into the new ServiceConfig
     *        object.
     *
     * @throws NullPointerException If dto is null
     *
     * @see defaultSettings()
     * @see clone()
     */
    public ServiceDTO(ServiceDTO dto) {
        if (dto == null) {
            throw new NullPointerException("ServiceDTO object required");
        }

        enabled = dto.isEnabled();
        name = dto.getName();
        title = dto.getTitle();
        serverAbstract = dto.getAbstract();
        keywords = CloneLibrary.clone(dto.getKeywords());
        fees = dto.getFees();
        accessConstraints = dto.getAccessConstraints();
        maintainer = dto.getMaintainer();
        onlineResource = dto.getOnlineResource();
        metadataLink = dto.getMetadataLink();
    }

    /**
     * Implements clone.
     *
     * @return An instance of a ServiceConfig object which represents a copy of
     *         the existing ServiceConfig Object.
     *
     * @see java.lang.Object#clone()
     */
    public Object clone() {
        return new ServiceDTO(this);
    }

    /**
     * Implement equals.
     *
     * @param other The ServiceConfig object which will be tested.
     *
     * @return true when the classes are equal.
     *
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object other) {
        if ((other == null) || !(other instanceof ServiceDTO)) {
            return false;
        }

        ServiceDTO dto = (ServiceDTO) other;

        if (enabled != dto.isEnabled()) {
            return false;
        }

        if ((name != null) ? (!name.equals(dto.name)) : (dto.name != null)) {
            return false;
        }

        if ((title != null) ? (!title.equals(dto.title)) : (dto.title != null)) {
            return false;
        }

        if ((serverAbstract != null)
                ? (!serverAbstract.equals(dto.getAbstract()))
                    : (dto.serverAbstract != null)) {
            return false;
        }

        if (!Arrays.equals(keywords, dto.keywords)) {
            return false;
        }

        if ((fees != null) ? (!fees.equals(dto.fees)) : (dto.fees != null)) {
            return false;
        }

        if ((accessConstraints != null)
                ? (!accessConstraints.equals(dto.accessConstraints))
                    : (dto.accessConstraints != null)) {
            return false;
        }

        if ((maintainer != null) ? (!maintainer.equals(dto.maintainer))
                                     : (dto.maintainer != null)) {
            return false;
        }

        if ((metadataLink != null) ? (!metadataLink.equals(dto.metadataLink))
                : (dto.metadataLink != null)) {
        	return false;
        }
        
        return true;
    }

    /**
     * Implement hashCode for ServiceDTO.
     *
     * @return Hashcode in agreement with equals method
     *
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        return (enabled ? 1 : 0) | ((name != null) ? name.hashCode() : 0)
        | ((title != null) ? title.hashCode() : 0)
        | ((serverAbstract != null) ? serverAbstract.hashCode() : 0)
        | ((keywords != null) ? keywords.hashCode() : 0)
        | ((fees != null) ? fees.hashCode() : 0)
        | ((accessConstraints != null) ? accessConstraints.hashCode() : 0)
        | ((maintainer != null) ? maintainer.hashCode() : 0)
        | ((metadataLink != null) ? metadataLink.hashCode() : 0);
    }

	/**
	 * Name of Service.
	 * 
	 * @return
	 * 
	 * @uml.property name="name"
	 */
	public String getName() {
		return name;
	}

	/**
	 * Online Reference URL for the web service.
	 * 
	 * <p>
	 * A location to look for when additional assistance is required.
	 * </p>
	 * 
	 * <p>
	 * Example: <code>new URL("http://www.openplans.org/")</code>
	 * </p>
	 * 
	 * @return DOCUMENT ME!
	 * 
	 * @uml.property name="onlineResource"
	 */
	public URL getOnlineResource() {
		return onlineResource;
	}

	/**
	 * The title of the service.
	 * 
	 * <p>
	 * Example: <code>The Open Planning Project Basemap Server</code>
	 * </p>
	 * 
	 * @return DOCUMENT ME!
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
	 * Sets the title of the service.
	 * 
	 * <p>
	 * Example: <code>The Open Planning Project Basemap Server</code>
	 * </p>
	 * 
	 * @param string Title of the Service
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
        return serverAbstract;
    }

	/**
	 * The access constraints associated with the service.
	 * 
	 * <p>
	 * When there are not any, the value "NONE" is used.
	 * </p>
	 * 
	 * <p>
	 * Example: <code>"NONE"</code>
	 * </p>
	 * 
	 * @return DOCUMENT ME!
	 * 
	 * @uml.property name="accessConstraints"
	 */
	public String getAccessConstraints() {
		return accessConstraints;
	}


    /**
     * Represents when the Web Service is enabled/disabled.
     *
     * @return <code>true</code> is service is enabled
     */
    public boolean isEnabled() {
        return enabled;
    }

	/**
	 * The fees associated with the service.
	 * 
	 * <p>
	 * When there are not any fees, the value  "NONE" is used.
	 * </p>
	 * 
	 * <p>
	 * Example: <code>NONE</code>
	 * </p>
	 * 
	 * @return DOCUMENT ME!
	 * 
	 * @uml.property name="fees"
	 */
	public String getFees() {
		return fees;
	}

	/**
	 * Keywords associated with the service.
	 * 
	 * <p>
	 * Example: <code>new String[]{"WFS","New York"}</code>
	 * </p>
	 * 
	 * @return DOCUMENT ME!
	 * 
	 * @uml.property name="keywords"
	 */
	public String[] getKeywords() {
		return keywords;
	}

	/**
	 * Name of the party who maintains the web service.
	 * 
	 * <p>
	 * Should ideally be contact information such as a email address.
	 * </p>
	 * 
	 * <p>
	 * Example: <code>"The Open Planning Project"</code>
	 * </p>
	 * 
	 * @return The maintainer of this Service
	 * 
	 * @uml.property name="maintainer"
	 */
	public String getMaintainer() {
		return maintainer;
	}


    /**
     * Provides a short abstract about the service.
     * 
     * <p>
     * Example:
     * <pre><code>
     * This is a test server.  It contains some basemap data from New York City.
     * </code></pre>
     * </p>
     *
     * @param serviceAbstract Abstract describing service
     */
    public void setAbstract(String serviceAbstract) {
        serverAbstract = serviceAbstract;
    }

	/**
	 * Provide the access constraints associated with the service.
	 * 
	 * <p>
	 * When there are not any, use the value "NONE".
	 * </p>
	 * 
	 * <p>
	 * Example: <code>"NONE"</code>
	 * </p>
	 * 
	 * @param constraints DOCUMENT ME!
	 * 
	 * @uml.property name="accessConstraints"
	 */
	public void setAccessConstraints(String constraints) {
		accessConstraints = constraints;
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
	 * Provide the fees associated with the service.
	 * 
	 * <p>
	 * When there are not any fees, use the value "NONE".
	 * </p>
	 * 
	 * <p>
	 * Example: <code>NONE</code>
	 * </p>
	 * 
	 * @param string DOCUMENT ME!
	 * 
	 * @uml.property name="fees"
	 */
	public void setFees(String string) {
		fees = string;
	}

	/**
	 * Provide keywords associated with the service.
	 * 
	 * <p>
	 * Example: <code>new String[]{"WFS","New York"}</code>
	 * </p>
	 * 
	 * @param array DOCUMENT ME!
	 * 
	 * @uml.property name="keywords"
	 */
	public void setKeywords(String[] array) {
		keywords = array;
	}

	/**
	 * Provide the party that maintains the web service.
	 * 
	 * <p>
	 * Should ideally be contact information such as a email address.
	 * </p>
	 * 
	 * <p>
	 * Example: <code>"The Open Planning Project"</code>
	 * </p>
	 * 
	 * @param string DOCUMENT ME!
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
