/* Copyright (c) 2001 - 2004 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.global.dto;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;


/**
 * Data Transfer Object used to represent GeoServer Catalog information.
 * 
 * <p>
 * Represents an instance of the catalog.xml file in the configuration of the
 * server, along with associated configuration files for the feature types.
 * </p>
 * 
 * <p>
 * Data Transfer object are used to communicate between the GeoServer
 * application and its configuration and persistent layers. As such the class
 * is final - to allow for its future use as an on-the-wire message.
 * </p>
 * <p>
 * Example:
 * </p>
 * <pre><code>
 * DataDTO dDto = new DataDTO();
 * Map m = new HashMap();
 * NameSpaceDTO ns = new NameSpaceDTO();
 * ns.setUri("dzwiers.refractions.net");
 * m.put("nsDave",ns);
 * dDto.setNameSpaces(m);
 * ns = new NameSpaceDTO();
 * ns.setUri("jgarnett.refractions.net");
 * ns.setDefault(true);
 * dDto.addNameSpace("nsJody"ns);
 * dDto.setDefaultNameSpace(ns);
 * ...
 * </code></pre>
 * 
 * @author dzwiers, Refractions Research, Inc.
 * @version $Id: DataDTO.java,v 1.1.2.4 2004/01/09 09:52:44 jive Exp $
 *
 * @see DataSource
 * @see FeatureTypeInfo
 * @see StyleConfig
 */
public final class DataDTO implements DataTransferObject {
    /**
     * DataStoreInfoDTO referenced by key "<code>dataStoreID</code>".
     *
     * @see org.vfny.geoserver.global.dto.DataStoreInfoDTO
     */
    private Map dataStores;

    /**
     * NamespaceDTO referenced by key "<code>prefix</code>".
     * 
     * @see org.vfny.geoserver.global.dto.NameSpaceDTO
     */
    private Map nameSpaces;

    /**
     * FeatureTypesInfoDTO referenced by key "<code>dataStoreID.typeName</code>"
     * 
     * @see org.vfny.geoserver.global.dto.FeatureTypeInfoDTO
     */
    private Map featuresTypes;

    /**
     * StyleDTO referenced by key "<code>something?</code>"
     * 
     * @see org.vfny.geoserver.global.dto.StyleDTO
     */
    private Map styles;

    /**
     * The default namespace for the server instance.
     * <p>
     * This may be <code>null</code> if a default has not been defined.
     * the config files is supposed to use the "first" Namespace when a default
     * is not defined - but we have lost all sense of order by placing this
     * in a Map. For 99% of the time when no default has been provided it is
     * because there is only one Namespace for the application.
     * </p>
     * <p>
     * TODO: It is really tempting to use a "prefix" here - in fact that is probably
     * the correct thing to do.
     * </p>
     * @see org.vfny.geoserver.global.dto.NameSpace
     */
    private NameSpaceDTO defaultNameSpace;

    /**
     * Data constructor.
     * 
     * <p>
     * Creates a Data to represent an instance with default data.
     * </p>
     * 
     * @see defaultSettings()
     */
    public DataDTO() {
		dataStores = new HashMap();
		nameSpaces = new HashMap();
		styles = new HashMap();
		featuresTypes = new HashMap();
		
		// TODO: David I am not sure we can have a default until a namespace is defined		
		defaultNameSpace = new NameSpaceDTO();			
    }

    /**
     * Creates a duplicate of the provided DataDTO using deep copy.
     * 
     * <p>
     * Creates a copy of the Data provided. If the Data
     * provided  is null then default values are used. All the datastructures
     * are cloned.
     * </p>
     *
     * @param c The catalog to copy.
     */
    public DataDTO(DataDTO dto) {
    	if( dto == null ){
    		throw new NullPointerException("Non null DataDTO required");
    	}    	
        try {
            dataStores = CloneLibrary.clone(dto.getDataStores());
        } catch (Exception e) {
            dataStores = new HashMap();
        }

        try {
            nameSpaces = CloneLibrary.clone(dto.getNameSpaces());
        } catch (Exception e) {
            nameSpaces = new HashMap();
        }

        try {
            featuresTypes = CloneLibrary.clone(dto.getFeaturesTypes());
        } catch (Exception e) {
            featuresTypes = new HashMap();
        }

        try {
            styles = CloneLibrary.clone(dto.getStyles());
        } catch (Exception e) {
            styles = new HashMap();
        }

        defaultNameSpace = (NameSpaceDTO) dto.getDefaultNameSpace().clone();
    }

    /**
     * Implement clone as a Deep copy.
     * @return A copy of this Data
     *
     * @see java.lang.Object#clone()
     */
    public Object clone() {
        return new DataDTO(this);
    }

    /**
     * Implement equals as part of the Object contract.
     * 
     * <p>
     * Recursively tests to determine if the object passed in is a copy of this
     * object.
     * </p>
     *
     * @param other The Data object to test.
     *
     * @return true when the object passed is the same as this object.
     *
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object other) {
        if ((other == null) || !(other instanceof DataDTO)) {
            return false;
        }

        DataDTO c = (DataDTO) other;
        boolean r = true;

        if (dataStores != null) {
            r = r && EqualsLibrary.equals(dataStores, c.getDataStores());
        } else if (c.getDataStores() != null) {
            return false;
        }

        if (nameSpaces != null) {
            r = r && EqualsLibrary.equals(nameSpaces, c.getNameSpaces());
        } else if (c.getNameSpaces() != null) {
            return false;
        }

        if (featuresTypes != null) {
            r = r && EqualsLibrary.equals(featuresTypes, c.getFeaturesTypes());
        } else if (c.getFeaturesTypes() != null) {
            return false;
        }

		if (styles != null) {
			r = r && EqualsLibrary.equals(styles, c.getStyles());
		} else if (c.getStyles() != null) {
			return false;
		}

        if (defaultNameSpace != null) {
            r = r && defaultNameSpace.equals(c.getDefaultNameSpace());
        } else if (c.getDefaultNameSpace() != null) {
            return false;
        }

        return r;
    }

	/**
	 * Implement hashCode as part of the Object contract.
	 * 
	 * @see java.lang.Object#hashCode()
	 * 
	 * @return Service hashcode or 0
	 */
	public int hashCode() {
		int r = 1;
		
		if (dataStores != null) {
			r *= dataStores.hashCode();
		}

		if (nameSpaces != null) {
			r *= nameSpaces.hashCode();
		}

		if (featuresTypes != null) {
			r *= featuresTypes.hashCode();
		}

		if (styles != null) {
			r *= styles.hashCode();
		}

		if (defaultNameSpace != null) {
			r *= defaultNameSpace.hashCode();
		}
		return r;
	}
	
    /**
     * Retrive a Map of DataStoreInfoDTO by "dataStoreID".
     * 
     * @return Map of DataStoreInfoDTO by "dataStoreID"
     */
    public Map getDataStores() {
        return dataStores;
    }

    /**
     * Return the getDefaultNameSpace.
     * 
     * <p>
     * May consider just returning the "prefix" of the default Namespace here.
     * It is unclear what happens when we are starting out with a Empty DataDTO
     * class.
     * </p>
     *
     * @return Default namespace or <code>null</code>
     */
    public NameSpaceDTO getDefaultNameSpace() {
        return defaultNameSpace;
    }

    /**
     * Retrive Map of FeatureTypeInfoDTO by "dataStoreID.typeName".
     * 
     * @return Map of FeatureTypeInfoDTO by "dataStoreID.typeName"
     */
    public Map getFeaturesTypes() {
        return featuresTypes;
    }

    /**
     * Map of NamespaceDTO by "prefix".
     * 
     * @return Map of NamespaceDTO by "prefix".
     */
    public Map getNameSpaces() {
        return nameSpaces;
    }

    /**
     * Retrive Map of StyleDTO by "something?".
     * 
     * TODO: What is the key?
     * 
     * @return Map of StyleDTO by "something"?
     */
    public Map getStyles() {
        return styles;
    }

    /**
     * Replace DataStoreInfoDTO map.
     * 
     * @param map Map of DataStoreInfoDTO by "dataStoreID"
     */
    public void setDataStores(Map map) {
    	if( map == null ){
    		throw new NullPointerException("DataStores map must not be null. Use Collections.EMPTY_MAP if you must");
    	}
    	dataStores = new HashMap( map );
        if (map != null) {
            dataStores = map;
        }
    }

    /**
     * Sets the default namespace.
     * 
     * <p>
     * Note the provided namespace must be present in the namespace map.
     * </p>
     * <p>
     * TODO: Consider using just the prefix?
     * </p>
     * @param support
     */
    public void setDefaultNameSpace(NameSpaceDTO defaultNamespaceDTO) {
    	if( nameSpaces.containsValue( defaultNamespaceDTO )){
			defaultNameSpace = defaultNamespaceDTO;
    	}
    	else {
    		throw new NoSuchElementException("Provided namespace "+defaultNamespaceDTO.getPrefix()+" must be contained in Namespaces");
    	}
    }

    /**
     * Set the FeatureTypeInfoDTO map.
     * 
     * <p>
     * The dataStoreID used for the map must be in datastores.
     * </p>
     *
     * @param Map of FeatureTypeInfoDTO by "dataStoreID.typeName"
     */
    public void setFeaturesTypes(Map map) {
		if( map == null ){
			throw new NullPointerException("FeatureTypeInfoDTO map must not be null. Use Collections.EMPTY_MAP if you must");
		}
        featuresTypes = map;        
    }

    /**
     * Sets the NameSpaceDTO map.
     * <p>
     * TODO: Should we reset the default when given a new Map? Consider using just prefix for default
     * </p>
     * @param Map of NameSpaceDTO by "prefix"
     */
    public void setNameSpaces(Map map) {
		if( map == null ){
			throw new NullPointerException("NameSpaceDTO map must not be null. Use Collections.EMPTY_MAP if you must");
		}    	
        nameSpaces = map;        
    }

    /**
     * Set map of StyleDTO by "something?".
     * 
     * @param map Map of StyleDTO by "someKey"?
     */
    public void setStyles(Map map) {
		if( map == null ){
			throw new NullPointerException("StyleInfoDTO map must not be null. Use Collections.EMPTY_MAP if you must");
		}    	
        styles = map;        
    }    
}
