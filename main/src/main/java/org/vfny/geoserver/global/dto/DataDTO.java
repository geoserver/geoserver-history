/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */

/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
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
 *
 * <p>
 * Example:
 * </p>
 * <pre><code>
 * DataDTO dDto = new DataDTO();
 * Map m = new HashMap();
 * NameSpaceInfoDTO ns = new NameSpaceInfoDTO();
 * ns.setUri("dzwiers.refractions.net");
 * m.put("nsDave",ns);
 * dDto.setNameSpaces(m);
 * ns = new NameSpaceInfoDTO();
 * ns.setUri("jgarnett.refractions.net");
 * ns.setDefault(true);
 * dDto.addNameSpace("nsJody"ns);
 * dDto.setDefaultNameSpace(ns);
 * ...
 * </code></pre>
 *
 * @author dzwiers, Refractions Research, Inc.
 * @author $Author: Alessio Fabiani (alessio.fabiani@gmail.com) $ (last modification)
 * @author $Author: Simone Giannecchini (simboss1@gmail.com) $ (last modification)
 * @version $Id$
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
     *
     * @uml.property name="dataStores"
     * @uml.associationEnd elementType="java.lang.String" qualifier="key:java.lang.String
     * org.vfny.geoserver.global.dto.DataStoreInfoDTO" multiplicity="(0 -1)" ordering=
     * "ordered"
     */
    private Map dataStores;

    /**
     * CoverageStoreInfoDTO referenced by key "<code>formatID</code>".
     *
     * @see org.vfny.geoserver.global.dto.CoverageStoreInfoDTO
     *
     * @uml.property name="formats"
     * @uml.associationEnd elementType="java.lang.String" qualifier="getFormatId:java.lang.String
     * org.vfny.geoserver.global.dto.CoverageStoreInfoDTO" multiplicity="(0 -1)" ordering="ordered"
     */
    private Map formats;

    /**
     * NamespaceDTO referenced by key "<code>prefix</code>".
     *
     * @see org.vfny.geoserver.global.dto.NameSpaceInfoDTO
     *
     * @uml.property name="nameSpaces"
     * @uml.associationEnd elementType="java.lang.String" qualifier="key:java.lang.String
     * org.vfny.geoserver.global.dto.NameSpaceInfoDTO" multiplicity="(0 -1)" ordering=
     * "ordered"
     */
    private Map nameSpaces;

    /**
     * FeatureTypesInfoDTO referenced by key
     * "<code>dataStoreID.typeName</code>"
     *
     * @see org.vfny.geoserver.global.dto.FeatureTypeInfoDTO
     *
     * @uml.property name="featuresTypes"
     * @uml.associationEnd elementType="org.vfny.geoserver.global.dto.AttributeTypeInfoDTO"
     * qualifier="key:java.lang.String org.vfny.geoserver.global.dto.FeatureTypeInfoDTO"
     * multiplicity="(0 -1)" ordering="ordered"
     */
    private Map featuresTypes;

    /**
     *
     * @uml.property name="coverages"
     * @uml.associationEnd qualifier="key:java.lang.String org.vfny.geoserver.global.dto.DataTransferObject"
     * multiplicity="(0 1)"
     */
    private Map coverages;

    /**
     * StyleDTO referenced by key "<code>id</code>"
     *
     * @see org.vfny.geoserver.global.dto.StyleDTO
     *
     * @uml.property name="styles"
     * @uml.associationEnd qualifier="key:java.lang.String org.vfny.geoserver.global.dto.DataTransferObject"
     * multiplicity="(0 1)"
     */
    private Map styles;

    /**
     * The default namespace for the server instance.
     *
     * <p>
     * This may be <code>null</code> if a default has not been defined. the
     * config files is supposed to use the "first" Namespace when a default is
     * not defined - but we have lost all sense of order by placing this in a
     * Map. For 99% of the time when no default has been provided it is
     * because there is only one Namespace for the application.
     * </p>
     *
     * @see org.vfny.geoserver.global.dto.NameSpaceInfo
     *
     * @uml.property name="defaultNameSpacePrefix" multiplicity="(0 1)"
     */
    private String defaultNameSpacePrefix;

    /**
     * Data constructor.
     *
     * <p>
     * does nothing
     * </p>
     */
    public DataDTO() {
    }

    /**
     * Creates a duplicate of the provided DataDTO using deep copy.
     *
     * <p>
     * Creates a copy of the Data provided. If the Data provided  is null then
     * default values are used. All the datastructures are cloned.
     * </p>
     *
     * @param dto The catalog to copy.
     *
     * @throws NullPointerException DOCUMENT ME!
     */
    public DataDTO(DataDTO dto) {
        if (dto == null) {
            throw new NullPointerException("Non null DataDTO required");
        }

        try {
            dataStores = CloneLibrary.clone(dto.getDataStores());
        } catch (Exception e) {
            dataStores = new HashMap();
        }

        try {
            formats = CloneLibrary.clone(dto.getFormats());
        } catch (Exception e) {
            formats = new HashMap();
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
            coverages = CloneLibrary.clone(dto.getCoverages());
        } catch (Exception e) {
            coverages = new HashMap();
        }

        try {
            styles = CloneLibrary.clone(dto.getStyles());
        } catch (Exception e) {
            styles = new HashMap();
        }

        defaultNameSpacePrefix = dto.getDefaultNameSpacePrefix();
    }

    /**
     * Implement clone as a Deep copy.
     *
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

        if (formats != null) {
            r = r && EqualsLibrary.equals(formats, c.getFormats());
        } else if (c.getFormats() != null) {
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

        if (coverages != null) {
            r = r && EqualsLibrary.equals(coverages, c.getCoverages());
        } else if (c.getCoverages() != null) {
            return false;
        }

        if (styles != null) {
            r = r && EqualsLibrary.equals(styles, c.getStyles());
        } else if (c.getStyles() != null) {
            return false;
        }

        if (defaultNameSpacePrefix != null) {
            r = r && defaultNameSpacePrefix.equals(c.getDefaultNameSpacePrefix());
        } else if (c.getDefaultNameSpacePrefix() != null) {
            return false;
        }

        return r;
    }

    /**
     * Implement hashCode as part of the Object contract.
     *
     * @return Service hashcode or 0
     *
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        int r = 1;

        if (dataStores != null) {
            r *= dataStores.hashCode();
        }

        if (formats != null) {
            r *= formats.hashCode();
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

        if (coverages != null) {
            r *= coverages.hashCode();
        }

        return r;
    }

    /**
     * Retrive a Map of DataStoreInfoDTO by "dataStoreID".
     *
     * @return Map of DataStoreInfoDTO by "dataStoreID"
     *
     * @uml.property name="dataStores"
     */
    public Map getDataStores() {
        return dataStores;
    }

    /**
     * Retrive a Map of CoverageStoreInfoDTO by "formatID".
     *
     * @return Map of CoverageStoreInfoDTO by "formatID"
     *
     * @uml.property name="formats"
     */
    public Map getFormats() {
        return formats;
    }

    /**
     * Return the getDefaultNameSpace.
     *
     * <p>
     * May consider just returning the "prefix" of the default Namespace here.
     * It is unclear what happens when we are starting out with a Empty
     * DataDTO class.
     * </p>
     *
     * @return Default namespace or <code>null</code>
     *
     * @uml.property name="defaultNameSpacePrefix"
     */
    public String getDefaultNameSpacePrefix() {
        return defaultNameSpacePrefix;
    }

    /**
     * Retrive Map of FeatureTypeInfoDTO by "dataStoreID.typeName".
     *
     * @return Map of FeatureTypeInfoDTO by "dataStoreID.typeName"
     *
     * @uml.property name="featuresTypes"
     */
    public Map getFeaturesTypes() {
        return featuresTypes;
    }

    /**
     * Map of NamespaceDTO by "prefix".
     *
     * @return Map of NamespaceDTO by "prefix".
     *
     * @uml.property name="nameSpaces"
     */
    public Map getNameSpaces() {
        return nameSpaces;
    }

    /**
     * Retrive Map of StyleDTO by "something?".  Key is Style.id
     *
     * @return Map of StyleDTO by "something"?
     *
     * @uml.property name="styles"
     */
    public Map getStyles() {
        return styles;
    }

    /**
     * Replace DataStoreInfoDTO map.
     *
     * @param map Map of DataStoreInfoDTO by "dataStoreID"
     *
     * @throws NullPointerException DOCUMENT ME!
     *
     * @uml.property name="dataStores"
     */
    public void setDataStores(Map map) {
        if (map == null) {
            throw new NullPointerException(
                "DataStores map must not be null. Use Collections.EMPTY_MAP if you must");
        }

        dataStores = new HashMap(map);

        if (map != null) {
            dataStores = map;
        }
    }

    /**
     * Replace CoverageStoreInfoDTO map.
     *
     * @param map Map of CoverageStoreInfoDTO by "formatID"
     *
     * @throws NullPointerException DOCUMENT ME!
     *
     * @uml.property name="formats"
     */
    public void setFormats(Map map) {
        if (map == null) {
            throw new NullPointerException(
                "Formats map must not be null. Use Collections.EMPTY_MAP if you must");
        }

        formats = new HashMap(map);

        if (map != null) {
            formats = map;
        }
    }

    /**
     * Sets the default namespace.
     *
     * <p>
     * Note the provided namespace must be present in the namespace map.
     * </p>
     *
     * @param dnsp the default namespace prefix.
     *
     * @throws NoSuchElementException DOCUMENT ME!
     *
     * @uml.property name="defaultNameSpacePrefix"
     */
    public void setDefaultNameSpacePrefix(String dnsp) {
        defaultNameSpacePrefix = dnsp;

        if (!nameSpaces.containsKey(dnsp)) {
            throw new NoSuchElementException("Invalid NameSpace Prefix for Default");
        }
    }

    /**
     * Set the FeatureTypeInfoDTO map.
     *
     * <p>
     * The dataStoreID used for the map must be in datastores.
     * </p>
     *
     * @param map of FeatureTypeInfoDTO by "dataStoreID.typeName"
     *
     * @throws NullPointerException DOCUMENT ME!
     *
     * @uml.property name="featuresTypes"
     */
    public void setFeaturesTypes(Map map) {
        if (map == null) {
            throw new NullPointerException(
                "FeatureTypeInfoDTO map must not be null. Use Collections.EMPTY_MAP if you must");
        }

        featuresTypes = map;
    }

    /**
     * Sets the NameSpaceInfoDTO map.
     *
     * <p>
     * The default prefix is not changed by this operation.
     * </p>
     *
     * @param map of NameSpaceInfoDTO by "prefix"
     *
     * @throws NullPointerException DOCUMENT ME!
     *
     * @uml.property name="nameSpaces"
     */
    public void setNameSpaces(Map map) {
        if (map == null) {
            throw new NullPointerException(
                "NameSpaceDTO map must not be null. Use Collections.EMPTY_MAP if you must");
        }

        nameSpaces = map;
    }

    /**
     * Set map of StyleDTO by "something?".
     *
     * @param map Map of StyleDTO by "someKey"?
     *
     * @throws NullPointerException DOCUMENT ME!
     *
     * @uml.property name="styles"
     */
    public void setStyles(Map map) {
        if (map == null) {
            throw new NullPointerException(
                "StyleInfoDTO map must not be null. Use Collections.EMPTY_MAP if you must");
        }

        styles = map;
    }

    /**
     * @return Returns the coverages.
     *
     * @uml.property name="coverages"
     */
    public Map getCoverages() {
        return coverages;
    }

    /**
     * @param coverages The coverages to set.
     *
     * @uml.property name="coverages"
     */
    public void setCoverages(Map coverages) {
        if (coverages == null) {
            throw new NullPointerException(
                "CoverageInfoDTO map must not be null. Use Collections.EMPTY_MAP if you must");
        }

        this.coverages = coverages;
    }
}
