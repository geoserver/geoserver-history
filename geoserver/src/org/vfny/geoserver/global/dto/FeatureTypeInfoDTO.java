/* Copyright (c) 2001 - 2004 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.global.dto;

import java.util.LinkedList;
import java.util.List;

import org.geotools.filter.Filter;

import com.vividsolutions.jts.geom.Envelope;


/**
 * Data Transfer Object used for GeoServer FeatureTypeInfo information.
 * 
 * <p>
 * FeatureTypeInfo is used because FeatureType is already used to represent
 * schema information in GeoTools2.
 * </p>
 * 
 * <p>
 * Data Transfer object are used to communicate between the GeoServer
 * application and its configuration and persistent layers. As such the class
 * is final - to allow for its future use as an on-the-wire message.
 * </p>
 * 
 * <p>
 * FeatureTypeInfoDTO ftiDto = new FeatureTypeInfoDTO();
 * ftiDto.setName("My Feature Type");
 * ftiDto.setTitle("The Best Feature Type");
 * ftiDto.setSRS(23769);
 * ftiDto.setDataStoreId("myDataStore");
 * </p>
 *
 * @author dzwiers, Refractions Research, Inc.
 * @version $Id: FeatureTypeInfoDTO.java,v 1.1.2.3 2004/01/06 23:54:39 dmzwiers Exp $
 */
public final class FeatureTypeInfoDTO implements DataStructure {
    /** The Id of the datastore which should be used to get this featuretype. */
    private String dataStoreId;

    /** A bounding box for this featuretype */
    private Envelope latLongBBox;

    /** native wich EPGS code for the FeatureTypeInfo */
    private int SRS;

    /**
     * Copy of the featuretype schema as a string.
     *
     * @task TODO parse this into a memory representation.
     */
    private String schema;

    /** The featuretype name. */
    private String name;

    /**
     * The featuretype directory name. This is used to write to, and is  stored
     * because it may be longer than the name, as this often includes
     * information about the source of the featuretype.
     */
    private String dirName;

    /** The featuretype title */
    private String title;

    /** The feature type abstract, short explanation of this featuretype. */
    private String _abstract;

    /** A list of keywords to associate with this featuretype. */
    private List keywords;

    /**
     * configuration information.
     *
     * @task TODO figure out what this is used for exactly
     */
    private int numDecimals;

    /**
     * the list of exposed attributes. If the list is empty or not present at
     * all, all the FeatureTypeInfo's attributes are exposed, if is present,
     * only those oattributes in this list will be exposed by the services
     */
    private Filter definitionQuery = null;

    /** The default style name. */
    private String defaultStyle;

    /**
     * FeatureTypeInfo constructor.
     * 
     * <p>
     * Creates a FeatureTypeInfo to represent an instance with default data.
     * </p>
     *
     * @see defaultSettings()
     */
    public FeatureTypeInfoDTO() {
        defaultSettings();
    }

    /**
     * FeatureTypeInfo constructor.
     * 
     * <p>
     * Creates a copy of the FeatureTypeInfo provided. If the
     * FeatureTypeInfo provided  is null then default values are used. All
     * the data structures are cloned.
     * </p>
     *
     * @param f The featuretype to copy.
     */
    public FeatureTypeInfoDTO(FeatureTypeInfoDTO f) {
        if (f == null) {
            defaultSettings();

            return;
        }

        dataStoreId = f.getDataStoreId();
        latLongBBox = CloneLibrary.clone(f.getLatLongBBox());
        SRS = f.getSRS();
        schema = f.getSchema();
        name = f.getName();
        title = f.getTitle();
        _abstract = f.getAbstract();
        numDecimals = f.getNumDecimals();
        definitionQuery = f.getDefinitionQuery();

        try {
            keywords = CloneLibrary.clone(f.getKeywords()); //clone?
        } catch (Exception e) {
            keywords = new LinkedList();
        }

        defaultStyle = f.getDefaultStyle();
        dirName = f.getDirName();
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
        dataStoreId = "";
        latLongBBox = new Envelope();
        SRS = 0;
        schema = "";
        defaultStyle = "";
        name = "";
        title = "";
        _abstract = "";
        keywords = new LinkedList();
        numDecimals = 8;
        definitionQuery = null;
        dirName = "";
    }

    /**
     * Implement clone.
     * 
     * <p>
     * creates a clone of this object
     * </p>
     *
     * @return A copy of this FeatureTypeInfo
     *
     * @see java.lang.Object#clone()
     */
    public Object clone() {
        return new FeatureTypeInfoDTO(this);
    }

    /**
     * Implement equals.
     * 
     * <p>
     * recursively tests to determine if the object passed in is a copy of this
     * object.
     * </p>
     *
     * @param obj The FeatureTypeInfo object to test.
     *
     * @return true when the object passed is the same as this object.
     *
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof FeatureTypeInfoDTO)) {
            return false;
        }

        FeatureTypeInfoDTO f = (FeatureTypeInfoDTO) obj;
        boolean r = true;
        r = r && (dataStoreId == f.getDataStoreId());

        if (latLongBBox != null) {
            r = r && latLongBBox.equals(f.getLatLongBBox());
        } else if (f.getLatLongBBox() != null) {
            return false;
        }

        r = r && (SRS == f.getSRS());

        if (schema != null) {
            r = r && schema.equals(f.getSchema());
        } else if (f.getSchema() != null) {
            return false;
        }

        r = r && (defaultStyle == f.getDefaultStyle());
        r = r && (name == f.getName());
        r = r && (title == f.getTitle());
        r = r && (_abstract == f.getAbstract());
        r = r && (numDecimals == f.getNumDecimals());

        if (definitionQuery != null) {
            r = r && definitionQuery.equals(f.getDefinitionQuery());
        } else if (f.getDefinitionQuery() != null) {
            return false;
        }

        if (keywords != null) {
            r = r && EqualsLibrary.equals(keywords, f.getKeywords());
        } else if (f.getKeywords() != null) {
            return false;
        }

        r = r && (dirName == f.getDirName());

        return r;
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
		
		if (name != null) {
			r *= name.hashCode();
		}

		if (dataStoreId != null) {
			r *= dataStoreId.hashCode();
		}

		if (title != null) {
			r *= title.hashCode();
		}
		
		if(SRS!=0)
			r = SRS%r;
		
		return r;
	}

    /**
     * getAbstract purpose.
     * 
     * <p>
     * Description ...
     * </p>
     *
     * @return
     */
    public String getAbstract() {
        return _abstract;
    }

    /**
     * getDataStore purpose.
     * 
     * <p>
     * Description ...
     * </p>
     *
     * @return
     */
    public String getDataStoreId() {
        return dataStoreId;
    }

    /**
     * getKeywords purpose.
     * 
     * <p>
     * Description ...
     * </p>
     *
     * @return
     */
    public List getKeywords() {
        return keywords;
    }

    /**
     * getLatLongBBox purpose.
     * 
     * <p>
     * Description ...
     * </p>
     *
     * @return
     */
    public Envelope getLatLongBBox() {
        return latLongBBox;
    }

    /**
     * getName purpose.
     * 
     * <p>
     * Description ...
     * </p>
     *
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * getSRS purpose.
     * 
     * <p>
     * Description ...
     * </p>
     *
     * @return
     */
    public int getSRS() {
        return SRS;
    }

    /**
     * getTitle purpose.
     * 
     * <p>
     * Description ...
     * </p>
     *
     * @return
     */
    public String getTitle() {
        return title;
    }

    /**
     * setAbstract purpose.
     * 
     * <p>
     * Description ...
     * </p>
     *
     * @param string
     */
    public void setAbstract(String string) {
        _abstract = string;
    }

    /**
     * setDataStore purpose.
     * 
     * <p>
     * Description ...
     * </p>
     *
     * @param store
     */
    public void setDataStoreId(String store) {
        dataStoreId = store;
    }

    /**
     * setKeywords purpose.
     * 
     * <p>
     * Description ...
     * </p>
     *
     * @param list
     */
    public void setKeywords(List list) {
        keywords = list;
    }

    /**
     * setKeywords purpose.
     * 
     * <p>
     * Description ...
     * </p>
     *
     * @param key
     *
     * @return DOCUMENT ME!
     */
    public boolean addKeyword(String key) {
        if (keywords == null) {
            keywords = new LinkedList();
        }

        return keywords.add(key);
    }

    /**
     * setKeywords purpose.
     * 
     * <p>
     * Description ...
     * </p>
     *
     * @param key
     *
     * @return DOCUMENT ME!
     */
    public boolean removeKeyword(String key) {
        return keywords.remove(key);
    }

    /**
     * setLatLongBBox purpose.
     * 
     * <p>
     * Description ...
     * </p>
     *
     * @param envelope
     */
    public void setLatLongBBox(Envelope envelope) {
        latLongBBox = envelope;
    }

    /**
     * setName purpose.
     * 
     * <p>
     * Description ...
     * </p>
     *
     * @param string
     */
    public void setName(String string) {
        name = string;
    }

    /**
     * setSRS purpose.
     * 
     * <p>
     * Description ...
     * </p>
     *
     * @param i
     */
    public void setSRS(int i) {
        SRS = i;
    }

    /**
     * setTitle purpose.
     * 
     * <p>
     * Description ...
     * </p>
     *
     * @param string
     */
    public void setTitle(String string) {
        title = string;
    }

    /**
     * getNumDecimals purpose.
     * 
     * <p>
     * Description ...
     * </p>
     *
     * @return
     */
    public int getNumDecimals() {
        return numDecimals;
    }

    /**
     * setNumDecimals purpose.
     * 
     * <p>
     * Description ...
     * </p>
     *
     * @param i
     */
    public void setNumDecimals(int i) {
        numDecimals = i;
    }

    /**
     * getDefinitionQuery purpose.
     * 
     * <p>
     * Description ...
     * </p>
     *
     * @return
     */
    public Filter getDefinitionQuery() {
        return definitionQuery;
    }

    /**
     * setDefinitionQuery purpose.
     * 
     * <p>
     * Description ...
     * </p>
     *
     * @param filter
     */
    public void setDefinitionQuery(Filter filter) {
        definitionQuery = filter;
    }

    /**
     * getDefaultStyle purpose.
     * 
     * <p>
     * Description ...
     * </p>
     *
     * @return
     */
    public String getDefaultStyle() {
        return defaultStyle;
    }

    /**
     * setDefaultStyle purpose.
     * 
     * <p>
     * Description ...
     * </p>
     *
     * @param string
     */
    public void setDefaultStyle(String string) {
        defaultStyle = string;
    }

    /**
     * getSchema purpose.
     * 
     * <p>
     * Description ...
     * </p>
     *
     * @return
     */
    public String getSchema() {
        return schema;
    }

    /**
     * setSchema purpose.
     * 
     * <p>
     * Description ...
     * </p>
     *
     * @param schema
     */
    public void setSchema(String schema) {
        this.schema = schema;
    }

    /**
     * getDirName purpose.
     * 
     * <p>
     * Description ...
     * </p>
     *
     * @return
     */
    public String getDirName() {
        return dirName;
    }

    /**
     * setDirName purpose.
     * 
     * <p>
     * Description ...
     * </p>
     *
     * @param string
     */
    public void setDirName(String string) {
        dirName = string;
    }
}
