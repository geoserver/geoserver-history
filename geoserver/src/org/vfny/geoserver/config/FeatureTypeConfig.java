/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.config;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.geotools.feature.AttributeType;
import org.geotools.feature.FeatureType;
import org.geotools.filter.Filter;
import org.vfny.geoserver.global.dto.AttributeTypeInfoDTO;
import org.vfny.geoserver.global.dto.FeatureTypeInfoDTO;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.GeometryFactory;

/**
 * User interface FeatureType staging area.
 * 
 * @author dzwiers, Refractions Research, Inc.
 * @version $Id: FeatureTypeConfig.java,v 1.20 2004/03/09 10:59:56 jive Exp $
 */
public class FeatureTypeConfig {
    /** The Id of the datastore which should be used to get this featuretype. */
    private String dataStoreId;

    /** A bounding box for this featuretype */
    private Envelope latLongBBox;

    /** native wich EPGS code for the FeatureTypeInfo */
    private int SRS;

    /**
     * This is an ordered list of AttributeTypeInfoConfig.
     * <p>
     * These attribtue have been defined by the user (or schema.xml file).
     * Additional attribute may be assumed based on the schemaBase
     * </p>
     * <p>
     * If this is <code>null</code>, all Attribtue information
     * will be generated. An empty list is used to indicate that only
     * attribtues indicated by the schemaBase will be returned.
     * </p>
     */
    private List schemaAttributes;

    /** Name (must match DataStore typeName). */
    private String name;

    /**
     * The schema name.
     * <p>
     * Usually  name + "_Type"                
     * </p>
     */
    private String schemaName;

    /**
     * The schema base.
     * <p>
     * The schema base is used to indicate additional attribtues, not defined
     * by the user. These attribute are fixed -not be edited by the user.
     * </p>
     * <p>
     * This easiest is "AbstractFeatureType"
     * </p>
     */
    private String schemaBase;

    /**
     * The featuretype directory name.
     * <p>
     * This is used to write to, and is  stored because it may be longer than
     * the name, as this often includes information about the source of the
     * featuretype.
     * </p>
     * <p>
     * A common naming convention is: <code>dataStoreId + "_" + name</code>
     * </p>
     */
    private String dirName;

    /**
     * The featuretype title.
     * <p>
     * Not sure what this is used for - usually name+"_Type"
     */
    private String title;

    /** The feature type abstract, short explanation of this featuretype. */
    private String _abstract;

    /**
     * A list of keywords to associate with this featuretype.
     * <p>
     * Keywords are destinct strings, often rendered surrounded by brackets
     * to aid search engines.
     * </p>
     */
    private Set keywords;

    /** Configuration information used to specify numeric percision */
    private int numDecimals;

    /**
     * Filter used to limit query.
     * <p>
     * TODO: Check the following comment - I don't belive it.
     * The list of exposed attributes. If the list is empty or not present at
     * all, all the FeatureTypeInfo's attributes are exposed, if is present,
     * only those oattributes in this list will be exposed by the services
     * </p>
     */
    private Filter definitionQuery = null;

    /**
     * The default style name.
     */
    private String defaultStyle;

    /**
     * Package visible constructor for test cases
     */
    FeatureTypeConfig() {
    }
    
    /**
     * Creates a FeatureTypeInfo to represent an instance with default data.
     * 
     * @param dataStoreId ID for data store in catalog
     * @param schema Geotools2 FeatureType
     * @param generate True to generate entries for all attribtues
     */
    public FeatureTypeConfig(String dataStoreId, FeatureType schema, boolean generate) {
        if ((dataStoreId == null) || (dataStoreId.length() == 0)) {
            throw new IllegalArgumentException(
                "dataStoreId is required for FeatureTypeConfig");
        }

        if (schema == null) {
            throw new IllegalArgumentException(
                "FeatureType is required for FeatureTypeConfig");
        }
        this.dataStoreId = dataStoreId;
        latLongBBox = new Envelope();

        if (schema.getDefaultGeometry() == null) {
            // pardon? Does this not make you a table?
            SRS = -1;
        } else {
            GeometryFactory geometryFactory = schema.getDefaultGeometry()
                                                    .getGeometryFactory();

            if (geometryFactory == null) {
                // Assume Cartisian Coordiantes
                SRS = 0; 
            } else {
                // Assume SRID number is good enough                
                SRS = geometryFactory.getSRID();
            }
        }
        if( generate ){         
            this.schemaAttributes = new ArrayList();
            for (int i = 0; i < schema.getAttributeCount(); i++) {
                AttributeType attrib = schema.getAttributeType(i);
                this.schemaAttributes.add(new AttributeTypeInfoConfig(attrib));
            }
        }
        else {
            this.schemaAttributes = null;        
        }
        defaultStyle = "";
        name = schema.getTypeName();
        title = schema.getTypeName() + "_Type";
        _abstract = "Generated from " + dataStoreId;
        keywords = new HashSet();
        keywords.add(dataStoreId);
        keywords.add(name);
        numDecimals = 8;
        definitionQuery = null;
        dirName = dataStoreId + "_" + name;
        schemaName = name + "_Type";        
        schemaBase = "AbstractFeatureType";
    }

    /**
     * FeatureTypeInfo constructor.
     * 
     * <p>
     * Creates a copy of the FeatureTypeInfoDTO provided. All the data
     * structures are cloned.
     * </p>
     *
     * @param dto The FeatureTypeInfoDTO to copy.
     *
     * @throws NullPointerException DOCUMENT ME!
     */
    public FeatureTypeConfig(FeatureTypeInfoDTO dto) {
        if (dto == null) {
            throw new NullPointerException(
                "Non null FeatureTypeInfoDTO required");
        }

        dataStoreId = dto.getDataStoreId();
        latLongBBox = new Envelope(dto.getLatLongBBox());
        SRS = dto.getSRS();
        
        if(dto.getSchemaAttributes() == null){
            schemaAttributes = null;
        }
        else {
            schemaAttributes = new LinkedList();
            
        	Iterator i = dto.getSchemaAttributes().iterator();
        	while (i.hasNext()){
        		schemaAttributes.add(new AttributeTypeInfoConfig(
        			(AttributeTypeInfoDTO) i.next()));
            }
        }
        name = dto.getName();
        title = dto.getTitle();
        _abstract = dto.getAbstract();
        numDecimals = dto.getNumDecimals();
        definitionQuery = dto.getDefinitionQuery();

        try {
            keywords = new HashSet(dto.getKeywords());
        } catch (Exception e) {
            keywords = new HashSet();
        }

        defaultStyle = dto.getDefaultStyle();
        dirName = dto.getDirName();
        schemaName = dto.getSchemaName();
        schemaBase = dto.getSchemaBase();        
    }

    /**
     * Implement toDTO.
     * 
     * <p>
     * Creates a represetation of this object as a FeatureTypeInfoDTO
     * </p>
     *
     * @return a representation of this object as a FeatureTypeInfoDTO
     *
     * @see org.vfny.geoserver.config.DataStructure#toDTO()
     */
    public FeatureTypeInfoDTO toDTO() {
        FeatureTypeInfoDTO f = new FeatureTypeInfoDTO();
        f.setDataStoreId(dataStoreId);
        f.setLatLongBBox(new Envelope(latLongBBox));
        f.setSRS(SRS);

        if( schemaAttributes == null ){
            // Use generated default attributes
            f.setSchemaAttributes( null );            
        }
        else {
            // Use user provided attribtue + schemaBase attribtues
            List s = new ArrayList();
            for (int i = 0; i < schemaAttributes.size(); i++){
                s.add(((AttributeTypeInfoConfig)schemaAttributes.get(i)).toDTO());
            }
            f.setSchemaAttributes(s);            
        }        
        f.setName(name);
        f.setTitle(title);
        f.setAbstract(_abstract);
        f.setNumDecimals(numDecimals);
        f.setDefinitionQuery(definitionQuery);

        try {
            f.setKeywords(new ArrayList(keywords));
        } catch (Exception e) {
            // do nothing, defaults already exist.
        }

        f.setDefaultStyle(defaultStyle);
        f.setDirName(dirName);
        f.setSchemaBase(schemaBase);
        f.setSchemaName(schemaName);
        return f;
    }


    /**
     * Searches through the schema looking for an AttributeTypeInfoConfig that
     * matches the name passed in attributeTypeName
     *
     * @param attributeTypeName the name of the AttributeTypeInfo to search
     *        for.
     *
     * @return AttributeTypeInfoConfig from the schema, if found
     */
    public AttributeTypeInfoConfig getAttributeFromSchema(
        String attributeTypeName) {
        Iterator iter = schemaAttributes.iterator();

        while (iter.hasNext()) {
            AttributeTypeInfoConfig atiConfig = (AttributeTypeInfoConfig) iter
                .next();

            if (atiConfig.getName().equals(attributeTypeName)) {
                return atiConfig;
            }
        }

        return null;
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
     * Access _abstract property.
     * 
     * @return Returns the _abstract.
     */
    public String getAbstract() {
        return _abstract;
    }
    /**
     * Set _abstract to _abstract.
     *
     * @param _abstract The _abstract to set.
     */
    public void setAbstract(String _abstract) {
        this._abstract = _abstract;
    }
    /**
     * Access dataStoreId property.
     * 
     * @return Returns the dataStoreId.
     */
    public String getDataStoreId() {
        return dataStoreId;
    }
    /**
     * Set dataStoreId to dataStoreId.
     *
     * @param dataStoreId The dataStoreId to set.
     */
    public void setDataStoreId(String dataStoreId) {
        this.dataStoreId = dataStoreId;
    }
    /**
     * Access defaultStyle property.
     * 
     * @return Returns the defaultStyle.
     */
    public String getDefaultStyle() {
        return defaultStyle;
    }
    /**
     * Set defaultStyle to defaultStyle.
     *
     * @param defaultStyle The defaultStyle to set.
     */
    public void setDefaultStyle(String defaultStyle) {
        this.defaultStyle = defaultStyle;
    }
    /**
     * Access definitionQuery property.
     * 
     * @return Returns the definitionQuery.
     */
    public Filter getDefinitionQuery() {
        return definitionQuery;
    }
    /**
     * Set definitionQuery to definitionQuery.
     *
     * @param definitionQuery The definitionQuery to set.
     */
    public void setDefinitionQuery(Filter definitionQuery) {
        this.definitionQuery = definitionQuery;
    }
    /**
     * Access dirName property.
     * 
     * @return Returns the dirName.
     */
    public String getDirName() {
        return dirName;
    }
    /**
     * Set dirName to dirName.
     *
     * @param dirName The dirName to set.
     */
    public void setDirName(String dirName) {
        this.dirName = dirName;
    }
    /**
     * Access keywords property.
     * 
     * @return Returns the keywords.
     */
    public Set getKeywords() {
        return keywords;
    }
    /**
     * Set keywords to keywords.
     *
     * @param keywords The keywords to set.
     */
    public void setKeywords(Set keywords) {
        this.keywords = keywords;
    }
    /**
     * Access latLongBBox property.
     * 
     * @return Returns the latLongBBox.
     */
    public Envelope getLatLongBBox() {
        return latLongBBox;
    }
    /**
     * Set latLongBBox to latLongBBox.
     *
     * @param latLongBBox The latLongBBox to set.
     */
    public void setLatLongBBox(Envelope latLongBBox) {
        this.latLongBBox = latLongBBox;
    }
    /**
     * Access name property.
     * 
     * @return Returns the name.
     */
    public String getName() {
        return name;
    }
    /**
     * Set name to name.
     *
     * @param name The name to set.
     */
    public void setName(String name) {
        this.name = name;
    }
    /**
     * Access numDecimals property.
     * 
     * @return Returns the numDecimals.
     */
    public int getNumDecimals() {
        return numDecimals;
    }
    /**
     * Set numDecimals to numDecimals.
     *
     * @param numDecimals The numDecimals to set.
     */
    public void setNumDecimals(int numDecimals) {
        this.numDecimals = numDecimals;
    }
    
    /**
     * Access schemaAttributes property.
     * 
     * @return Returns the schemaAttributes.
     */
    public List getSchemaAttributes() {
        return schemaAttributes;
    }
    /**
     * Set schemaAttributes to schemaAttributes.
     *
     * @param schemaAttributes The schemaAttributes to set.
     */
    public void setSchemaAttributes(List schemaAttributes) {
        this.schemaAttributes = schemaAttributes;    	
    }
    /**
     * Access schemaBase property.
     * 
     * @return Returns the schemaBase.
     */
    public String getSchemaBase() {
        return schemaBase;
    }
    /**
     * Set schemaBase to schemaBase.
     *
     * @param schemaBase The schemaBase to set.
     */
    public void setSchemaBase(String schemaBase) {
        this.schemaBase = schemaBase;
    }
    /**
     * Access schemaName property.
     * 
     * @return Returns the schemaName.
     */
    public String getSchemaName() {
        return schemaName;
    }
    /**
     * Set schemaName to schemaName.
     *
     * @param schemaName The schemaName to set.
     */
    public void setSchemaName(String schemaName) {
        this.schemaName = schemaName;
    }
    /**
     * Access sRS property.
     * 
     * @return Returns the sRS.
     */
    public int getSRS() {
        return SRS;
    }
    /**
     * Set sRS to srs.
     *
     * @param srs The sRS to set.
     */
    public void setSRS(int srs) {
        SRS = srs;
    }
    /**
     * Access title property.
     * 
     * @return Returns the title.
     */
    public String getTitle() {
        return title;
    }
    /**
     * Set title to title.
     *
     * @param title The title to set.
     */
    public void setTitle(String title) {
        this.title = title;
    }
}
