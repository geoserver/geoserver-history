/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.global.dto;

import java.util.HashMap;
import java.util.Map;


/**
 * Data Transfer Object for GeoServer DataStore information.
 * 
 * <p>
 * Used to describe a datastore, typically one specified in the catalog.xml
 * config file.
 * </p>
 * 
 * <p>
 * Data Transfer object are used to communicate between the GeoServer
 * application and its configuration and persistent layers. As such the class
 * is final - to allow for its future use as an on-the-wire message.
 * </p>
 * Example:<code> DataStoreInfoDTO dsiDto = new DataStoreInfoDTO();
 * dsiDto.setIde("myDataStore"); dsiDto.setEnabled(true); dsiDto.setTile("My
 * Data Store"); Map m = new HashMap(); m.put("key","param");
 * dsiDto.setConnectionParams(m); </code>
 *
 * @author dzwiers, Refractions Research, Inc.
 * @author $Author: Alessio Fabiani (alessio.fabiani@gmail.com) $ (last modification)
 * @author $Author: Simone Giannecchini (simboss_ml@tiscali.it) $ (last modification)
 * @version $Id: FormatInfoDTO.java,v 1.5 2004/02/02 08:56:45 jive Exp $
 */
public final class FormatInfoDTO implements DataTransferObject {
    /** unique Format identifier */
    private String id;

    private String type;
    private String url;

    /** true if this Format is enabled */
    private boolean enabled;

    /** The title of this Format */
    private String title;

    /** a short description about this Format */
    private String _abstract;

    /**
     * Parameters to create the FormatInfo
     * 
     * <p>
     * Limitied to Strings for both Keys and Values.
     * </p>
     */
    private Map parameters;
    
    /**
     * FormatInfo constructor.
     * 
     * <p>
     * does nothing
     * </p>
     */
    public FormatInfoDTO() {
    }

    /**
     * FormatInfo constructor.
     * 
     * <p>
     * Creates a copy of the FormatInfo provided. If the FormatInfo
     * provided  is null then default values are used. All the datastructures
     * are cloned.
     * </p>
     *
     * @param dto The format to copy.
     *
     * @throws NullPointerException DOCUMENT ME!
     */
    public FormatInfoDTO(FormatInfoDTO dto) {
        if (dto == null) {
            throw new NullPointerException("Non-Null FormatDTO is requried");
        }

        id = dto.getId();
        //nameSpaceId = dto.getNameSpaceId();
        type = dto.getType();
        url = dto.getUrl();
        enabled = dto.isEnabled();
        _abstract = dto.getAbstract();

        parameters = new HashMap(dto.getParameters());
    }

    /**
     * Implement clone.
     * 
     * <p>
     * creates a clone of this object
     * </p>
     *
     * @return A copy of this FormatInfo
     *
     * @see java.lang.Object#clone()
     */
    public Object clone() {
        return new FormatInfoDTO(this);
    }

    /**
     * Implement equals.
     * 
     * <p>
     * recursively tests to determine if the object passed in is a copy of this
     * object.
     * </p>
     *
     * @param obj The FormatInfo object to test.
     *
     * @return true when the object passed is the same as this object.
     *
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object obj) {
        if ((obj == null) || !(obj instanceof FormatInfoDTO)) {
            return false;
        }

        FormatInfoDTO ds = (FormatInfoDTO) obj;
        boolean r = true;
        r = r && (id == ds.getId());
        //r = r && (nameSpaceId == ds.getNameSpaceId());
        r = r && (type == ds.getType());
        r = r && (url == ds.getUrl());
        r = r && (enabled == ds.isEnabled());
        r = r && (_abstract == ds.getAbstract());

        if (parameters != null) {
            r = r && parameters.equals(ds.getParameters());
        } else if (ds.getParameters() != null) {
            return false;
        }

        return r;
    }

    /**
     * Implement hashCode.
     *
     * @return Service hashcode or 0
     *
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        int r = 1;

        if (id != null) {
            r *= id.hashCode();
        }

//        if (nameSpaceId != null) {
//            r *= nameSpaceId.hashCode();
//        }
        if (type != null) {
            r *= type.hashCode();
        }
        
        if (url != null) {
            r *= url.hashCode();
        }
        
        if (_abstract != null) {
            r *= _abstract.hashCode();
        }

        return r;
    }

    /**
     * Short description of Format
     *
     * @return Short description
     */
    public String getAbstract() {
        return _abstract;
    }

    /**
     * Map of param:value both of which are represented as text.
     * 
     * <p>
     * The map is based on String Keys, and String values.
     * </p>
     *
     * @return Map of Params for FormatFactoryAPI use
     */
    public Map getParameters() {
        return parameters;
    }

    /**
     * Value is <code>true</code> if the Format should be enabled.
     *
     * @return ture if Format shoudl be enabled
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Unique identifier representing this Format.
     * 
     * <p>
     * This value is used to refer to this Format by FeatureTypeInfoDTO.
     * </p>
     *
     * @return an identifier, non null
     */
    public String getId() {
        return id;
    }

    /**
     * Title for Format, used in error messages & configuration.
     *
     * @return Title dor the Format
     */
    public String getTitle() {
        return title;
    }

    /**
     * Updates the Format abstract.
     *
     * @param description
     */
    public void setAbstract(String description) {
        _abstract = description;
    }

    /**
     * Provide Format parameters.
     * 
     * <p>
     * Map is limited to text based keys and values
     * </p>
     *
     * @param map
     */
    public void setParameters(Map map) {
        if (map != null) {
        	parameters = map;
        }
    }

    /**
     * setEnabled purpose.
     * 
     * <p>
     * Description ...
     * </p>
     *
     * @param b
     */
    public void setEnabled(boolean b) {
        enabled = b;
    }

    /**
     * Sets the unique identifier for this FormatInfoDTO.
     *
     * @param identifier non<code>null</code> identifier for Format
     */
    public void setId(String identifier) {
        id = identifier;
    }

    /**
     * Set title used to identify this Format to the user.
     *
     * @param formatTitle Title used to identify Format to user
     */
    public void setTitle(String formatTitle) {
        title = formatTitle;
    }
	/**
	 * @return Returns the type.
	 */
	public String getType() {
		return type;
	}
	/**
	 * @param type The type to set.
	 */
	public void setType(String type) {
		this.type = type;
	}
	/**
	 * @return Returns the url.
	 */
	public String getUrl() {
		return url;
	}
	/**
	 * @param url The url to set.
	 */
	public void setUrl(String url) {
		this.url = url;
	}
}
