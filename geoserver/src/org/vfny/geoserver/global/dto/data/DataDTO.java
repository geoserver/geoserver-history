/* Copyright (c) 2001 - 2004 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.global.dto.data;

import org.vfny.geoserver.global.dto.CloneLibrary;
import org.vfny.geoserver.global.dto.DataStructure;
import org.vfny.geoserver.global.dto.EqualsLibrary;
import java.util.HashMap;
import java.util.Map;


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
 * @author dzwiers, Refractions Research, Inc.
 * @version $Id: DataDTO.java,v 1.1.2.1 2004/01/05 22:14:40 dmzwiers Exp $
 *
 * @see DataSource
 * @see FeatureTypeInfo
 * @see StyleConfig
 */
public final class DataDTO implements DataStructure {
    /**
     * A set of datastores and their names.
     *
     * @see org.vfny.geoserver.config.data.DataStoreInfo
     */
    private Map dataStores;

    /**
     * A set of namespaces and their names.
     *
     * @see org.vfny.geoserver.config.data.NameSpaceConfig
     */
    private Map nameSpaces;

    /**
     * A set of featuretypes and their names.
     *
     * @see org.vfny.geoserver.config.data.FeatureTypeInfo
     */
    private Map featuresTypes;

    /**
     * A set of styles and their names.
     *
     * @see org.vfny.geoserver.config.data.StyleConfig
     */
    private Map styles;

    /**
     * the default namespace for the server instance.
     *
     * @see org.vfny.geoserver.config.data.NameSpaceConfig
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
        defaultSettings();
    }

    /**
     * Data constructor.
     * 
     * <p>
     * Creates a copy of the Data provided. If the Data
     * provided  is null then default values are used. All the datastructures
     * are cloned.
     * </p>
     *
     * @param c The catalog to copy.
     */
    public DataDTO(DataDTO c) {
        try {
            dataStores = CloneLibrary.clone(c.getDataStores());
        } catch (Exception e) {
            dataStores = new HashMap();
        }

        try {
            nameSpaces = CloneLibrary.clone(c.getNameSpaces());
        } catch (Exception e) {
            nameSpaces = new HashMap();
        }

        try {
            featuresTypes = CloneLibrary.clone(c.getFeaturesTypes());
        } catch (Exception e) {
            featuresTypes = new HashMap();
        }

        try {
            styles = CloneLibrary.clone(c.getStyles());
        } catch (Exception e) {
            styles = new HashMap();
        }

        defaultNameSpace = (NameSpaceDTO) c.getDefaultNameSpace().clone();
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
        dataStores = new HashMap();
        nameSpaces = new HashMap();
        styles = new HashMap();
        featuresTypes = new HashMap();
        defaultNameSpace = new NameSpaceDTO();
    }

    /**
     * Implement clone.
     * 
     * <p>
     * creates a clone of this object
     * </p>
     *
     * @return A copy of this Data
     *
     * @see java.lang.Object#clone()
     */
    public Object clone() {
        return new DataDTO(this);
    }

    /**
     * Implement equals.
     * 
     * <p>
     * recursively tests to determine if the object passed in is a copy of this
     * object.
     * </p>
     *
     * @param obj The Data object to test.
     *
     * @return true when the object passed is the same as this object.
     *
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object obj) {
        if ((obj == null) || !(obj instanceof DataDTO)) {
            return false;
        }

        DataDTO c = (DataDTO) obj;
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

        if (defaultNameSpace != null) {
            r = r && defaultNameSpace.equals(c.getDefaultNameSpace());
        } else if (c.getDefaultNameSpace() != null) {
            return false;
        }

        return r;
    }

    /**
     * getDataStores purpose.
     * 
     * <p>
     * Description ...
     * </p>
     *
     * @return
     */
    public Map getDataStores() {
        return dataStores;
    }

    /**
     * getDataStores purpose.
     * 
     * <p>
     * Description ...
     * </p>
     *
     * @param key DOCUMENT ME!
     *
     * @return
     */
    public DataStoreInfoDTO getDataStore(String key) {
        return (DataStoreInfoDTO) dataStores.get(key);
    }

    /**
     * getDefaultNameSpace purpose.
     * 
     * <p>
     * Description ...
     * </p>
     *
     * @return
     */
    public NameSpaceDTO getDefaultNameSpace() {
        return defaultNameSpace;
    }

    /**
     * getFeatures purpose.
     * 
     * <p>
     * Description ...
     * </p>
     *
     * @return
     */
    public Map getFeaturesTypes() {
        return featuresTypes;
    }

    /**
     * getFeatures purpose.
     * 
     * <p>
     * Description ...
     * </p>
     *
     * @param key DOCUMENT ME!
     *
     * @return
     */
    public FeatureTypeInfoDTO getFeature(String key) {
        return (FeatureTypeInfoDTO) featuresTypes.get(key);
    }

    /**
     * getNameSpaces purpose.
     * 
     * <p>
     * Description ...
     * </p>
     *
     * @return
     */
    public Map getNameSpaces() {
        return nameSpaces;
    }

    /**
     * getNameSpaces purpose.
     * 
     * <p>
     * Description ...
     * </p>
     *
     * @param key DOCUMENT ME!
     *
     * @return
     */
    public NameSpaceDTO getNameSpace(String key) {
        return (NameSpaceDTO) nameSpaces.get(key);
    }

    /**
     * getStyles purpose.
     * 
     * <p>
     * Description ...
     * </p>
     *
     * @return
     */
    public Map getStyles() {
        return styles;
    }

    /**
     * getStyles purpose.
     * 
     * <p>
     * Description ...
     * </p>
     *
     * @param key DOCUMENT ME!
     *
     * @return
     */
    public StyleDTO getStyle(String key) {
        return (StyleDTO) styles.get(key);
    }

    /**
     * setDataStores purpose.
     * 
     * <p>
     * Description ...
     * </p>
     *
     * @param map
     */
    public void setDataStores(Map map) {
        if (map != null) {
            dataStores = map;
        }
    }

    /**
     * setDataStores purpose.
     * 
     * <p>
     * Description ...
     * </p>
     *
     * @param key
     * @param ds DOCUMENT ME!
     */
    public void addDataStore(String key, DataStoreInfoDTO ds) {
        if (dataStores == null) {
            dataStores = new HashMap();
        }

        if ((key != null) && (ds != null)) {
            dataStores.put(key, ds);
        }
    }

    /**
     * setDataStores purpose.
     * 
     * <p>
     * Description ...
     * </p>
     *
     * @param key
     *
     * @return DOCUMENT ME!
     */
    public DataStoreInfoDTO removeDataStore(String key) {
        if (dataStores == null) {
            dataStores = new HashMap();
        }

        return (DataStoreInfoDTO) dataStores.remove(key);
    }

    /**
     * setDefaultNameSpace purpose.
     * 
     * <p>
     * Description ...
     * </p>
     *
     * @param support
     */
    public void setDefaultNameSpace(NameSpaceDTO support) {
        if (support != null) {
            defaultNameSpace = support;
        }
    }

    /**
     * setFeatures purpose.
     * 
     * <p>
     * Description ...
     * </p>
     *
     * @param map
     */
    public void setFeaturesTypes(Map map) {
        if (map != null) {
            featuresTypes = map;
        }
    }

    /**
     * setFeatures purpose.
     * 
     * <p>
     * Description ...
     * </p>
     *
     * @param key
     * @param ft DOCUMENT ME!
     */
    public void addFeature(String key, FeatureTypeInfoDTO ft) {
        if (featuresTypes == null) {
            featuresTypes = new HashMap();
        }

        if ((key != null) && (ft != null)) {
            featuresTypes.put(key, ft);
        }
    }

    /**
     * setFeatures purpose.
     * 
     * <p>
     * Description ...
     * </p>
     *
     * @param key
     *
     * @return DOCUMENT ME!
     */
    public FeatureTypeInfoDTO removeFeature(String key) {
        if (featuresTypes == null) {
            featuresTypes = new HashMap();
        }

        return (FeatureTypeInfoDTO) featuresTypes.remove(key);
    }

    /**
     * setNameSpaces purpose.
     * 
     * <p>
     * Description ...
     * </p>
     *
     * @param map
     */
    public void setNameSpaces(Map map) {
        if (map != null) {
            nameSpaces = map;
        }
    }

    /**
     * setNameSpaces purpose.
     * 
     * <p>
     * Description ...
     * </p>
     *
     * @param key
     * @param ns DOCUMENT ME!
     */
    public void addNameSpace(String key, NameSpaceDTO ns) {
        if (nameSpaces == null) {
            nameSpaces = new HashMap();
        }

        if ((key != null) && (ns != null)) {
            nameSpaces.put(key, ns);
        }
    }

    /**
     * setNameSpaces purpose.
     * 
     * <p>
     * Description ...
     * </p>
     *
     * @param key
     *
     * @return DOCUMENT ME!
     */
    public NameSpaceDTO removeNameSpace(String key) {
        if (nameSpaces == null) {
            nameSpaces = new HashMap();
        }

        return (NameSpaceDTO) nameSpaces.remove(key);
    }

    /**
     * setStyles purpose.
     * 
     * <p>
     * Description ...
     * </p>
     *
     * @param map
     */
    public void setStyles(Map map) {
        if (map != null) {
            styles = map;
        }
    }

    /**
     * setStyles purpose.
     * 
     * <p>
     * Description ...
     * </p>
     *
     * @param key
     * @param s DOCUMENT ME!
     */
    public void addStyle(String key, StyleDTO s) {
        if (styles == null) {
            styles = new HashMap();
        }

        if ((key != null) && (s != null)) {
            styles.put(key, s);
        }
    }

    /**
     * setStyles purpose.
     * 
     * <p>
     * Description ...
     * </p>
     *
     * @param key
     *
     * @return DOCUMENT ME!
     */
    public StyleDTO removeStyle(String key) {
        if (styles == null) {
            styles = new HashMap();
        }

        return (StyleDTO) styles.remove(key);
    }
}
