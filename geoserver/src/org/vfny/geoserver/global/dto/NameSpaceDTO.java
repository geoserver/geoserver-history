/* Copyright (c) 2001 - 2004 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.global.dto;



/**
 * Data Transfer Object for GeoServer NameSpace information.
 * 
 * <p>
 * Represents the portion of a namespace required for the configuration of
 * geoserver. Defines namespaces to be used by the datastores.
 * </p>
 * 
 * <p>
 * Data Transfer object are used to communicate between the GeoServer
 * application and its configuration and persistent layers. As such the class
 * is final - to allow for its future use as an on-the-wire message.
 * </p>
 * 
 * <p>
 * Jody here - does this actual defin the namespace used by the GML?
 * </p>
 * 
 * <p>
 * NameSpaceDTO nsDto = new NameSpaceDTO();
 * nsDto.setDefault(false);
 * nsDto.setPrefix("me");
 * nsDto.setUri("dzwiers.refraction.net");
 * </p>
 * 
 * @author dzwiers, Refractions Research, Inc.
 * @version $Id: NameSpaceDTO.java,v 1.1.2.2 2004/01/06 23:54:39 dmzwiers Exp $
 */
public final class NameSpaceDTO implements DataStructure {
    //public static final String PREFIX_DELIMITER = ":";

    /** The namespace prefix. */
    private String prefix;

    /** The URI for this namespace. */
    private String uri;

    /** Whether this is the default namespace. */
    private boolean _default;

    /**
     * NameSpaceConfig constructor.
     * 
     * <p>
     * Creates a NameSpaceConfig to represent an instance with default data.
     * </p>
     *
     * @see defaultSettings()
     */
    public NameSpaceDTO() {
        defaultSettings();
    }

    /**
     * NameSpaceConfig constructor.
     * 
     * <p>
     * Creates a copy of the NameSpaceConfig provided. If the NameSpaceConfig
     * provided  is null then default values are used. All the data structures
     * are cloned.
     * </p>
     *
     * @param ns The namespace to copy.
     */
    public NameSpaceDTO(NameSpaceDTO ns) {
        if (ns == null) {
            defaultSettings();

            return;
        }

        prefix = ns.getPrefix();
        uri = ns.getUri();
        _default = ns.isDefault();
    }

    /**
     * defaultSettings purpose.
     * 
     * <p>
     * This method creates default values for the class. This method  should
     * noly be called by class constructors.
     * </p>
     */
    private void defaultSettings() {
        prefix = "";
        uri = "";
        _default = false;
    }

    /**
     * Implement clone.
     * 
     * <p>
     * creates a clone of this object
     * </p>
     *
     * @return A copy of this NameSpaceConfig
     *
     * @see java.lang.Object#clone()
     */
    public Object clone() {
        return new NameSpaceDTO(this);
    }

    /**
     * Implement equals.
     * 
     * <p>
     * recursively tests to determine if the object passed in is a copy of this
     * object.
     * </p>
     *
     * @param obj The NameSpaceConfig object to test.
     *
     * @return true when the object passed is the same as this object.
     *
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object obj) {
    	if(obj == null ||!(obj instanceof NameSpaceDTO))
    		return false;
    		
        NameSpaceDTO ns = (NameSpaceDTO) obj;

        return ((prefix == ns.getPrefix())
        && ((uri == ns.getUri()) && (_default == ns.isDefault())));
    }

	/**
	 * Implement hashCode.
	 * 
	 * @see java.lang.Object#hashCode()
	 * 
	 * @return Service hashcode or 0
	 */
	public int hashCode() {
		int r = 1;
		
		if (prefix != null) {
			r *= prefix.hashCode();
		}

		if (uri != null) {
			r *= uri.hashCode();
		}
		
		return r;
	}
	
    /**
     * isDefault purpose.
     * 
     * <p>
     * Description ...
     * </p>
     *
     * @return
     */
    public boolean isDefault() {
        return _default;
    }

    /**
     * getPrefix purpose.
     * 
     * <p>
     * Description ...
     * </p>
     *
     * @return
     */
    public String getPrefix() {
        return prefix;
    }

    /**
     * getUri purpose.
     * 
     * <p>
     * Description ...
     * </p>
     *
     * @return
     */
    public String getUri() {
        return uri;
    }

    /**
     * setDdefault purpose.
     * 
     * <p>
     * Description ...
     * </p>
     *
     * @param b
     */
    public void setDefault(boolean b) {
        _default = b;
    }

    /**
     * setPrefix purpose.
     * 
     * <p>
     * Description ...
     * </p>
     *
     * @param string
     */
    public void setPrefix(String string) {
        prefix = string;
    }

    /**
     * setUri purpose.
     * 
     * <p>
     * Description ...
     * </p>
     *
     * @param string
     */
    public void setUri(String string) {
        uri = string;
    }
}
