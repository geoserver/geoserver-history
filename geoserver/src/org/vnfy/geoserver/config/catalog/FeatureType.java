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
package org.vnfy.geoserver.config.catalog;

import java.util.List;
import java.util.LinkedList;
import com.vividsolutions.jts.geom.*;
import org.geotools.filter.*;
import org.vnfy.geoserver.config.CloneLibrary;
import org.vnfy.geoserver.config.EqualsLibrary;
/**
 * FeatureType purpose.
 * <p>
 * Description of FeatureType ...
 * <p>
 * 
 * @author dzwiers, Refractions Research, Inc.
 * @version $Id: FeatureType.java,v 1.1.2.1 2003/12/30 23:39:14 dmzwiers Exp $
 */
public class FeatureType implements Cloneable{
	private String dataStoreId;
	private Envelope latLongBBox;
	private int SRS;
	/**
	 * Constant after loaded.
	 */
	private String schema;
	private String name;
	private String dirName;
	private String title;
	private String _abstract;
	private List keywords;
	private int numDecimals;
	private Filter definitionQuery = null;
	private String defaultStyle;
	
	public FeatureType(){
		defaultSettings();
	}
	
	private void defaultSettings(){
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
	
	public FeatureType(FeatureType f){
		if(f==null){
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
		try{
			keywords = CloneLibrary.clone(f.getKeywords()); //clone?
		}catch(Exception e){
			keywords = new LinkedList();
		}
		defaultStyle = f.getDefaultStyle();
		dirName = f.getDirName();
	}
	
	public Object clone(){
		return new FeatureType(this);
	}
	
	public boolean equals(Object obj){
		if(obj == null)
			return false;
		FeatureType f = (FeatureType)obj;
		boolean r = true;
		r = r && dataStoreId == f.getDataStoreId();
		if(latLongBBox != null)
			r = r && latLongBBox.equals(f.getLatLongBBox());
		else if(f.getLatLongBBox()!=null) return false;
		r = r && SRS == f.getSRS();
		if(schema != null)
			r = r && schema.equals(f.getSchema());
		else if(f.getSchema() != null) return false;
		r = r && defaultStyle == f.getDefaultStyle();
		r = r && name == f.getName();
		r = r && title == f.getTitle();
		r = r && _abstract == f.getAbstract();
		r = r && numDecimals == f.getNumDecimals();
		if(definitionQuery != null)
			r = r && definitionQuery.equals(f.getDefinitionQuery());
		else if(f.getDefinitionQuery()!=null) return false;
		if(keywords != null)
			r = r && EqualsLibrary.equals(keywords,f.getKeywords());
		else if(f.getKeywords()!=null) return false;
		r = r && dirName==f.getDirName();
		return r;
	}
	/**
	 * get_abstract purpose.
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
	 * set_abstract purpose.
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
	public String getSchema() {
		return schema;
	}

	/**
	 * setSchema purpose.
	 * <p>
	 * Description ...
	 * </p>
	 * @param schema
	 */
	public void setSchema(String schema) {
		this.schema = schema;
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

}
