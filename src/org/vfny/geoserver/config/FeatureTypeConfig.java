/*
 *    Geotools2 - OpenSource mapping toolkit
 *    http://geotools.org
 *    (C) 2003, Geotools Project Managment Committee (PMC)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 *
 */
package org.vfny.geoserver.config;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.geotools.filter.Filter;
import org.vfny.geoserver.global.dto.AttributeTypeInfoDTO;
import org.vfny.geoserver.global.dto.FeatureTypeInfoDTO;

import com.vividsolutions.jts.geom.Envelope;
/**
 * FeatureTypeInfo purpose.
 * <p>
 * Description of FeatureTypeInfo ...
 * <p>
 * 
 * @author dzwiers, Refractions Research, Inc.
 * @version $Id: FeatureTypeConfig.java,v 1.9 2004/01/14 02:57:49 emperorkefka Exp $
 */
public class FeatureTypeConfig{
	
	/**
	 * The Id of the datastore which should be used to get this featuretype.
	 */
	private String dataStoreId;
	
	/**
	 * A bounding box for this featuretype
	 */
	private Envelope latLongBBox;
	
	/**
	 * native wich EPGS code for the FeatureTypeInfo
	 */
	private int SRS;
	
	/**
	 * Copy of the featuretype schema as a string.
	 * <p>
	 * This is an ordered list of AttributeTypeInfoConfig.
	 * </p>
	 */
	private List schema;
	
	/**
	 * The featuretype name.
	 */
	private String name;
	
	/** the schema name */
	private String schemaName;
	
	/** the schema base */
	private String schemaBase;
	
	/**
	 * The featuretype directory name. This is used to write to, and is 
	 * stored because it may be longer than the name, as this often includes 
	 * information about the source of the featuretype.
	 */
	private String dirName;
	
	/**
	 * The featuretype title
	 */
	private String title;
	
	/**
	 * The feature type abstract, short explanation of this featuretype.
	 */
	private String _abstract;
	
	/**
	 * A list of keywords to associate with this featuretype. 
	 */
	private List keywords;
	
	/**
	 * configuration information.
	 * @TODO figure out what this is used for exactly
	 */
	private int numDecimals;
	
	/**
	 * the list of exposed attributes. If the list is empty or not present
	 * at all, all the FeatureTypeInfo's attributes are exposed, if is present,
	 * only those oattributes in this list will be exposed by the services
	 */
	private Filter definitionQuery = null;
	
	/**
	 * The default style name.
	 */
	private String defaultStyle;

	/**
	 * FeatureTypeInfo constructor.
	 * <p>
	 * Creates a FeatureTypeInfo to represent an instance with default data.
	 * </p>
	 * @see defaultSettings()
	 */
	public FeatureTypeConfig(){
		dataStoreId = "";
		latLongBBox = new Envelope();
		SRS = 0;
		schema = new ArrayList();
		defaultStyle = "";
		name = "";
		title = "";
		_abstract = "";
		keywords = new LinkedList();
		numDecimals = 8;
		definitionQuery = null;
		dirName = schemaName = schemaBase = "";
	}

	/**
	 * FeatureTypeInfo constructor.
	 * <p>
	 * Creates a copy of the FeatureTypeInfoDTO provided. All the data structures are cloned. 
	 * </p>
	 * @param f The FeatureTypeInfoDTO to copy.
	 */
	public FeatureTypeConfig(FeatureTypeInfoDTO f){
		if(f==null){
			throw new NullPointerException();
		}
		dataStoreId = f.getDataStoreId();
		latLongBBox = new Envelope( f.getLatLongBBox() );
		SRS = f.getSRS();
		schema = f.getSchema();
		name = f.getName();
		title = f.getTitle();
		_abstract = f.getAbstract();
		numDecimals = f.getNumDecimals();
		definitionQuery = f.getDefinitionQuery();
		try{
			keywords = new ArrayList( f.getKeywords() ); 
		}catch(Exception e){
			keywords = new LinkedList();
		}
		defaultStyle = f.getDefaultStyle();
		dirName = f.getDirName();
		schemaName = f.getSchemaName();
		schemaBase = f.getSchemaBase();
	}

	/**
	 * load purpose.
	 * <p>
	 * Loads the new data into this instance object from an FeatureTypeInfoDTO.
	 * </p>
	 * @param obj an instance of FeatureTypeInfoDTO to load.
	 * @return true when the parameter is valid and stored.
	 */
	public void update(FeatureTypeInfoDTO f){
		if(f==null){
			throw new NullPointerException("FeatureTypeInfo Data Transfer Object required");
		}
		dataStoreId = f.getDataStoreId();
		latLongBBox = new Envelope(f.getLatLongBBox());
		SRS = f.getSRS();
		schema = new ArrayList();
		for(int i=0;i<f.getSchema().size();i++)
			schema.add(new AttributeTypeInfoConfig((AttributeTypeInfoDTO)f.getSchema().get(i)));
		name = f.getName();
		title = f.getTitle();
		_abstract = f.getAbstract();
		numDecimals = f.getNumDecimals();
		definitionQuery = f.getDefinitionQuery();
		try{
			keywords = new ArrayList(f.getKeywords());
		}catch(Exception e){
			keywords = new LinkedList();
		}
		defaultStyle = f.getDefaultStyle();
		dirName = f.getDirName();
		schemaName = f.getSchemaName();
		schemaBase = f.getSchemaBase();
	}
	
	/**
	 * Implement toDTO.
	 * <p>
	 * Creates a represetation of this object as a FeatureTypeInfoDTO
	 * </p>
	 * @see org.vfny.geoserver.config.DataStructure#toDTO()
	 * 
	 * @return a representation of this object as a FeatureTypeInfoDTO
	 */
	public FeatureTypeInfoDTO toDTO(){
		FeatureTypeInfoDTO f = new FeatureTypeInfoDTO();
		f.setDataStoreId(dataStoreId);
		f.setLatLongBBox( new Envelope(latLongBBox));
		f.setSRS(SRS);
		List s = new ArrayList();
		for(int i=0;i<schema.size();i++)
			s.add(schema.get(i));
		f.setSchema(s);
		f.setName(name);
		f.setTitle(title);
		f.setAbstract(_abstract);
		f.setNumDecimals(numDecimals);
		f.setDefinitionQuery(definitionQuery);
		try{
			f.setKeywords( new ArrayList(keywords));
		}catch(Exception e){
			// do nothing, defaults already exist.
		}
		f.setDefaultStyle(defaultStyle);
		f.setDirName(dirName);
		f.setSchemaBase(schemaBase);
		f.setSchemaName(schemaName);
		return f;
	}
	
	/**
	 * getAbstract purpose.
	 * <p>
	 * Description ...
	 * </p>
	 * @return
	 */
	public String getAbstract() {
		return _abstract;
	}

	/**
	 * getDataStore purpose.
	 * <p>
	 * Description ...
	 * </p>
	 * @return
	 */
	public String getDataStoreId() {
		return dataStoreId;
	}

	/**
	 * getKeywords purpose.
	 * <p>
	 * Description ...
	 * </p>
	 * @return
	 */
	public List getKeywords() {
		return keywords;
	}

	/**
	 * getLatLongBBox purpose.
	 * <p>
	 * Description ...
	 * </p>
	 * @return
	 */
	public Envelope getLatLongBBox() {
		return latLongBBox;
	}

	/**
	 * getName purpose.
	 * <p>
	 * Description ...
	 * </p>
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * getSRS purpose.
	 * <p>
	 * Description ...
	 * </p>
	 * @return
	 */
	public int getSRS() {
		return SRS;
	}

	/**
	 * getTitle purpose.
	 * <p>
	 * Description ...
	 * </p>
	 * @return
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * setAbstract purpose.
	 * <p>
	 * Description ...
	 * </p>
	 * @param string
	 */
	public void setAbstract(String string) {
		_abstract = string;
	}

	/**
	 * setDataStore purpose.
	 * <p>
	 * Description ...
	 * </p>
	 * @param store
	 */
	public void setDataStoreId(String store) {
		dataStoreId = store;
	}

	/**
	 * setKeywords purpose.
	 * <p>
	 * Description ...
	 * </p>
	 * @param list
	 */
	public void setKeywords(List list) {
		keywords = list;
	}

	/**
	 * setKeywords purpose.
	 * <p>
	 * Description ...
	 * </p>
	 * @param list
	 */
	public boolean addKeyword(String key) {
		if(keywords == null)
			keywords = new LinkedList();
		return keywords.add(key);
	}

	/**
	 * setKeywords purpose.
	 * <p>
	 * Description ...
	 * </p>
	 * @param list
	 */
	public boolean removeKeyword(String key) {
		return keywords.remove(key);
	}

	/**
	 * setLatLongBBox purpose.
	 * <p>
	 * Description ...
	 * </p>
	 * @param envelope
	 */
	public void setLatLongBBox(Envelope envelope) {
		latLongBBox = envelope;
	}

	/**
	 * setName purpose.
	 * <p>
	 * Description ...
	 * </p>
	 * @param string
	 */
	public void setName(String string) {
		name = string;
	}

	/**
	 * setSRS purpose.
	 * <p>
	 * Description ...
	 * </p>
	 * @param i
	 */
	public void setSRS(int i) {
		SRS = i;
	}

	/**
	 * setTitle purpose.
	 * <p>
	 * Description ...
	 * </p>
	 * @param string
	 */
	public void setTitle(String string) {
		title = string;
	}

	/**
	 * getNumDecimals purpose.
	 * <p>
	 * Description ...
	 * </p>
	 * @return
	 */
	public int getNumDecimals() {
		return numDecimals;
	}

	/**
	 * setNumDecimals purpose.
	 * <p>
	 * Description ...
	 * </p>
	 * @param i
	 */
	public void setNumDecimals(int i) {
		numDecimals = i;
	}

	/**
	 * getDefinitionQuery purpose.
	 * <p>
	 * Description ...
	 * </p>
	 * @return
	 */
	public Filter getDefinitionQuery() {
		return definitionQuery;
	}

	/**
	 * setDefinitionQuery purpose.
	 * <p>
	 * Description ...
	 * </p>
	 * @param filter
	 */
	public void setDefinitionQuery(Filter filter) {
		definitionQuery = filter;
	}

	/**
	 * getDefaultStyle purpose.
	 * <p>
	 * Description ...
	 * </p>
	 * @return
	 */
	public String getDefaultStyle() {
		return defaultStyle;
	}

	/**
	 * setDefaultStyle purpose.
	 * <p>
	 * Description ...
	 * </p>
	 * @param string
	 */
	public void setDefaultStyle(String string) {
		defaultStyle = string;
	}

	/**
	 * getSchema purpose.
	 * <p>
	 * Description ...
	 * </p>
	 * @return
	 */
	public List getSchema() {
		return schema;
	}
	
	/**
	 * Searches through the schema looking for an AttributeTypeInfoConfig
	 * that matches the name passed in attributeTypeName
	 * 
	 * @param attributeTypeName, the name of the AttributeTypeInfo to search for.
	 * @return AttributeTypeInfoConfig from the schema, if found
	 */
	public AttributeTypeInfoConfig getAttributeFromSchema(String attributeTypeName ){
		Iterator iter = schema.iterator();
		while(iter.hasNext()) {
			AttributeTypeInfoConfig atiConfig = (AttributeTypeInfoConfig) iter.next();
			if (atiConfig.getName().equals(attributeTypeName)) {
				return atiConfig;
			}
		}
		return null;
		
	}

	/**
	 * setSchema purpose.
	 * <p>
	 * Description ...
	 * </p>
	 * @param schemaElements
	 */
	public void setSchema(List schemaElements) {
		this.schema = schemaElements;
	}

	/**
	 * getDirName purpose.
	 * <p>
	 * Description ...
	 * </p>
	 * @return
	 */
	public String getDirName() {
		return dirName;
	}

	/**
	 * setDirName purpose.
	 * <p>
	 * Description ...
	 * </p>
	 * @param string
	 */
	public void setDirName(String string) {
		dirName = string;
	}

    
	/**
	 * getSchemaName purpose.
	 * <p>
	 * Description ...
	 * </p>
	 * @return
	 */
	public String getSchemaName() {
		return schemaName;
	}

	/**
	 * setSchemaName purpose.
	 * <p>
	 * Description ...
	 * </p>
	 * @param string
	 */
	public void setSchemaName(String string) {
		schemaName = string;
	}

	/**
	 * getSchemaBase purpose.
	 * <p>
	 * Description ...
	 * </p>
	 * @return
	 */
	public String getSchemaBase() {
		return schemaBase;
	}

	/**
	 * setSchemaBase purpose.
	 * <p>
	 * Description ...
	 * </p>
	 * @param string
	 */
	public void setSchemaBase(String string) {
		schemaBase = string;
	}
}
