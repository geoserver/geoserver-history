/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.global.dto;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import org.geotools.filter.Filter;
import org.vfny.geoserver.config.DataConfig;

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
 * <pre>Example:<code>
 * FeatureTypeInfoDTO ftiDto = new FeatureTypeInfoDTO();
 * ftiDto.setName("My Feature Type");
 * ftiDto.setTitle("The Best Feature Type");
 * ftiDto.setSRS(23769);
 * ftiDto.setDataStoreId("myDataStore");
 * </code></pre>
 *
 * @author dzwiers, Refractions Research, Inc.
 * @version $Id: FeatureTypeInfoDTO.java,v 1.13 2004/04/16 06:28:57 jive Exp $
 */
public final class FeatureTypeInfoDTO implements DataTransferObject {

	/**
	 * The Id of the datastore which should be used to get this featuretype.
	 * 
	 * @uml.property name="dataStoreId" multiplicity="(0 1)"
	 */
	private String dataStoreId;

	/**
	 * A bounding box for this featuretype
	 * 
	 * @uml.property name="latLongBBox"
	 * @uml.associationEnd multiplicity="(0 1)"
	 */
	private Envelope latLongBBox;

	/**
	 * native wich EPGS code for the FeatureTypeInfo
	 * 
	 * @uml.property name="sRS" multiplicity="(0 1)"
	 */
	private int SRS;

	/**
	 * Copy of the featuretype schema as a string.
	 * 
	 * @uml.property name="schema"
	 * @uml.associationEnd elementType="org.vfny.geoserver.global.dto.AttributeTypeInfoDTO"
	 * multiplicity="(0 -1)"
	 */
	private List schema;

	/**
	 * The schema name.
	 * 
	 * @uml.property name="schemaName" multiplicity="(0 1)"
	 */
	private String schemaName;

	/**
	 * The schemaBase name.
	 * 
	 * <p>
	 * Example NullType, or PointPropertyType.
	 * </p>
	 * 
	 * @uml.property name="schemaBase" multiplicity="(0 1)"
	 */
	private String schemaBase;

	/**
	 * The featuretype name.
	 * 
	 * <p>
	 * Often related to the title - like bc_roads_Type
	 * </p>
	 * 
	 * @uml.property name="name" multiplicity="(0 1)"
	 */
	private String name;

	/**
	 * 
	 */
	private String wmsPath;

	/**
	 * The featuretype directory name. This is used to write to, and is  stored
	 * because it may be longer than the name, as this often includes
	 * information about the source of the featuretype.
	 * 
	 * @uml.property name="dirName" multiplicity="(0 1)"
	 */
	private String dirName;

	/**
	 * The featuretype title
	 * 
	 * @uml.property name="title" multiplicity="(0 1)"
	 */
	private String title;


    /** The feature type abstract, short explanation of this featuretype. */
    private String _abstract;

	/**
	 * A list of keywords to associate with this featuretype.
	 * 
	 * @uml.property name="keywords"
	 * @uml.associationEnd elementType="java.lang.String" multiplicity="(0 -1)"
	 */
	private List keywords;

	/**
	 * Used to limit the number of decimals used in GML representations.
	 * 
	 * @uml.property name="numDecimals" multiplicity="(0 1)"
	 */
	private int numDecimals;

	/**
	 * the list of exposed attributes. If the list is empty or not present at
	 * all, all the FeatureTypeInfo's attributes are exposed, if is present,
	 * only those oattributes in this list will be exposed by the services
	 * 
	 * @uml.property name="definitionQuery"
	 * @uml.associationEnd multiplicity="(0 1)"
	 */
	private Filter definitionQuery = null;

	/**
	 * The default style name.
	 * 
	 * @uml.property name="defaultStyle" multiplicity="(0 1)"
	 */
	private String defaultStyle;

	// Modif C. Kolbowicz - 06/10/2004 

	/**
	 * The legend icon description.
	 * 
	 * @uml.property name="legendURL"
	 * @uml.associationEnd multiplicity="(0 1)"
	 */
	private LegendURLDTO legendURL;

	//-- Modif C. Kolbowicz - 06/10/2004 

	/**
	 * Holds the location of the file that contains schema information.
	 * 
	 * @uml.property name="schemaFile" multiplicity="(0 1)"
	 */
	private File schemaFile;


    /**
     * FeatureTypeInfo constructor.
     * 
     * <p>
     * does nothing
     * </p>
     */
    public FeatureTypeInfoDTO() {
    }

    /**
     * FeatureTypeInfo constructor.
     * 
     * <p>
     * Creates a copy of the FeatureTypeInfo provided. If the FeatureTypeInfo
     * provided  is null then default values are used. All the data structures
     * are cloned.
     * </p>
     *
     * @param dto The featuretype to copy.
     *
     * @throws NullPointerException DOCUMENT ME!
     */
    public FeatureTypeInfoDTO(FeatureTypeInfoDTO dto) {
        if (dto == null) {
            throw new NullPointerException(
                "Non null FeatureTypeInfoDTO required");
        }

        dataStoreId = dto.getDataStoreId();
        latLongBBox = CloneLibrary.clone(dto.getLatLongBBox());
        SRS = dto.getSRS();
        schema = dto.getSchemaAttributes();
        name = dto.getName();
        wmsPath = dto.getWmsPath();
        title = dto.getTitle();
        _abstract = dto.getAbstract();
        numDecimals = dto.getNumDecimals();
        definitionQuery = dto.getDefinitionQuery();

        // Modif C. Kolbowicz - 06/10/2004
        legendURL = dto.getLegendURL();

        //-- Modif C. Kolbowicz - 06/10/2004 
        try {
            keywords = CloneLibrary.clone(dto.getKeywords()); //clone?
        } catch (Exception e) {
            keywords = new LinkedList();
        }

        defaultStyle = dto.getDefaultStyle();

        dirName = dto.getDirName();
        schemaName = dto.getSchemaName();
        schemaBase = dto.getSchemaBase();
    }

    /**
     * Implement clone as a deep copy.
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
        if ((obj == null) || !(obj instanceof FeatureTypeInfoDTO)) {
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
            r = r && schema.equals(f.getSchemaAttributes());
        } else if (f.getSchemaAttributes() != null) {
            return false;
        }

        // Modif C. Kolbowicz - 06/10/2004
        if (legendURL != null) {
            r = r && schema.equals(f.getLegendURL());
        } else if (f.getLegendURL() != null) {
            return false;
        }

        //-- Modif C. Kolbowicz - 06/10/2004 
        r = r && (defaultStyle == f.getDefaultStyle());
        r = r && (name == f.getName());
        r = r && (wmsPath == f.getWmsPath());
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
        r = r && (schemaName == f.getSchemaName());
        r = r && (schemaBase == f.getSchemaBase());

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

        if (name != null) {
            r *= name.hashCode();
        }

        if (dataStoreId != null) {
            r *= dataStoreId.hashCode();
        }

        // Modif C. Kolbowicz - 06/10/2004
        if (legendURL != null) {
            r *= legendURL.hashCode();
        }

        //-- Modif C. Kolbowicz - 06/10/2004 
        if (title != null) {
            r *= title.hashCode();
        }

        if (SRS != 0) {
            r = SRS % r;
        }

        return r;
    }

    /**
     * Short description of FeatureType.
     *
     * @return Description of FeatureType
     */
    public String getAbstract() {
        return _abstract;
    }

	/**
	 * Identifier of DataStore used to create FeatureType.
	 * 
	 * @return DataStore identifier
	 * 
	 * @uml.property name="dataStoreId"
	 */
	public String getDataStoreId() {
		return dataStoreId;
	}

	/**
	 * List of keywords (limitied to text).
	 * 
	 * @return List of Keywords about this FeatureType
	 * 
	 * @uml.property name="keywords"
	 */
	public List getKeywords() {
		return keywords;
	}


    /**
     * Convience method for dataStoreId.typeName.
     * 
     * <p>
     * This key may be used to store this FeatureType in a Map for later.
     * </p>
     *
     * @return dataStoreId.typeName
     */
    public String getKey() {
        return getDataStoreId() + DataConfig.SEPARATOR + getName();
    }

	/**
	 * The extent of this FeatureType.
	 * 
	 * <p>
	 * Extent is measured against the tranditional LatLong coordinate system.
	 * </p>
	 * 
	 * @return Envelope of FeatureType
	 * 
	 * @uml.property name="latLongBBox"
	 */
	public Envelope getLatLongBBox() {
		return latLongBBox;
	}

	/**
	 * Name of featureType, must match typeName provided by DataStore.
	 * 
	 * @return typeName of FeatureType
	 * 
	 * @uml.property name="name"
	 */
	public String getName() {
		return name;
	}

	/**
	 * Spatial Reference System for FeatureType.
	 * 
	 * <p>
	 * Makes use of the standard EPSG codes?
	 * </p>
	 * 
	 * @return WPSG Spatial Reference System for FeatureType
	 * 
	 * @uml.property name="sRS"
	 */
	public int getSRS() {
		return SRS;
	}

	/**
	 * Title used to identify FeatureType to user.
	 * 
	 * @return FeatureType title
	 * 
	 * @uml.property name="title"
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
	 * 
	 * @uml.property name="dataStoreId"
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
	 * 
	 * @uml.property name="keywords"
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
     * @return boolean true when added.
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
     * @return true whwn removed
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
	 * 
	 * @uml.property name="latLongBBox"
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
	 * 
	 * @uml.property name="name"
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
	 * 
	 * @uml.property name="sRS"
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
	 * 
	 * @uml.property name="title"
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
	 * 
	 * @uml.property name="numDecimals"
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
	 * 
	 * @uml.property name="numDecimals"
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
	 * 
	 * @uml.property name="definitionQuery"
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
	 * 
	 * @uml.property name="definitionQuery"
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
	 * 
	 * @uml.property name="defaultStyle"
	 */
	public String getDefaultStyle() {
		//HACK: So our UI doesn't seem to allow the setting of styles or 
		//default styles or anything, despite the fact that shit chokes when none
		//is present.  This is making it so the beta release can not have any data
		//stores added to it.  This is a hacky ass way to get around it, just 
		//write out a normal style if it is null.  This can obviously be done 
		//better, and I have no idea why this default style shit is required - wfs
		//does not care about a style.  Should be able to seamlessly at least do
		//something for wms.
		if ((defaultStyle == null) || defaultStyle.equals("")) {
			defaultStyle = "normal";
		}

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
	 * 
	 * @uml.property name="defaultStyle"
	 */
	public void setDefaultStyle(String string) {
		defaultStyle = string;
	}


    /**
     * getSchema purpose.
     * 
     * <p>
     * Returns An ordered list of AttributeTypeInfoDTOs
     * </p>
     *
     * @return An ordered list of AttributeTypeInfoDTOs
     */
    public List getSchemaAttributes() {
        return schema;
    }

    /**
     * setSchema purpose.
     * 
     * <p>
     * Stores a list of AttributeTypeInfoDTOs.
     * </p>
     *
     * @param schemaElements An ordered list of AttributeTypeInfoDTOs
     */
    public void setSchemaAttributes(List schemaElements) {
        this.schema = schemaElements;
    }

	/**
	 * getDirName purpose.
	 * 
	 * <p>
	 * Description ...
	 * </p>
	 * 
	 * @return
	 * 
	 * @uml.property name="dirName"
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
	 * 
	 * @uml.property name="dirName"
	 */
	public void setDirName(String string) {
		dirName = string;
	}

	/**
	 * getSchemaName purpose.
	 * 
	 * <p>
	 * Description ...
	 * </p>
	 * 
	 * @return
	 * 
	 * @uml.property name="schemaName"
	 */
	public String getSchemaName() {
		return schemaName;
	}

	/**
	 * setSchemaName purpose.
	 * 
	 * <p>
	 * Description ...
	 * </p>
	 * 
	 * @param string
	 * 
	 * @uml.property name="schemaName"
	 */
	public void setSchemaName(String string) {
		schemaName = string;
	}

	/**
	 * getSchemaBase purpose.
	 * 
	 * <p>
	 * Usually generated as: getName + "_Type"
	 * </p>
	 * 
	 * @return
	 * 
	 * @uml.property name="schemaBase"
	 */
	public String getSchemaBase() {
		return schemaBase;
	}

	/**
	 * setSchemaBase purpose.
	 * 
	 * <p>
	 * Description ...
	 * </p>
	 * 
	 * @param string
	 * 
	 * @uml.property name="schemaBase"
	 */
	public void setSchemaBase(String string) {
		schemaBase = string;
	}

	// Modif C. Kolbowicz - 06/10/2004

	/**
	 * Gets a reference to an optional legend icon.
	 * 
	 * @return Value of property legendURL.
	 * 
	 * @uml.property name="legendURL"
	 */
	public LegendURLDTO getLegendURL() {
		return this.legendURL;
	}

	//-- Modif C. Kolbowicz - 06/10/2004
	// Modif C. Kolbowicz - 06/10/2004

	/**
	 * Returns a reference to an optional legend icon.
	 * 
	 * @param legendURL New value of property legendURL.
	 * 
	 * @uml.property name="legendURL"
	 */
	public void setLegendURL(LegendURLDTO legendURL) {
		this.legendURL = legendURL;
	}

	/**
	 * Gets the schema.xml file associated with this FeatureType.  This is set
	 * during the reading of configuration, it is not persisted as an element
	 * of the FeatureTypeInfoDTO, since it is just whether the schema.xml file
	 * was persisted, and its location.  If there is no schema.xml file then
	 * this method will return a File object with the location where the schema
	 * file would be located, but the file will return false for exists().
	 * 
	 * @uml.property name="schemaFile"
	 */
	public File getSchemaFile() {
		return this.schemaFile;
	}

	/**
	 * Sets the schema file.  Note that a non-exisiting file can be set here,
	 * to indicate that no schema.xml file is present.
	 * 
	 * @uml.property name="schemaFile"
	 */
	public void setSchemaFile(File schemaFile) {
		this.schemaFile = schemaFile;
	}

    //-- Modif C. Kolbowicz - 06/10/2004
    public String toString() {
        return "[FeatureTypeInfoDTO: " + name + ", datastoreId: " + dataStoreId
        + ", latLongBBOX: " + latLongBBox + "\n  SRS: " + SRS + ", schema:"
        + schema + ", schemaName: " + schemaName + ", dirName: " + dirName
        + ", title: " + title + "\n  definitionQuery: " + definitionQuery
        + ", defaultStyle: " + defaultStyle + ", legend icon: " + legendURL;
    }
	public String getWmsPath() {
		return wmsPath;
	}
	public void setWmsPath(String wmsPath) {
		this.wmsPath = wmsPath;
	}
}
