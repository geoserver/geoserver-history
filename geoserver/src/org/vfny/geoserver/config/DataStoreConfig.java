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

import org.vfny.geoserver.global.dto.*;
import java.util.HashMap;
import java.util.Map;

/**
 * DataStoreInfo purpose.
 * <p>
 * Used to describe a datastore, typically one specified in the catalog.xml config file. 
 * <p>
 * 
 * @author dzwiers, Refractions Research, Inc.
 * @version $Id: DataStoreConfig.java,v 1.2.2.2 2004/01/07 21:23:08 dmzwiers Exp $
 */
public class DataStoreConfig implements DataStructure{

	  /** unique datasore identifier */
	  private String id;

	  /** unique namespace to refer to this datastore */
	  private String nameSpaceId;

	  /** wether this data store is enabled */
	  private boolean enabled;

	  /** a short description about this data store */
	  private String title;

	  /** a short description about this data store */
	  private String _abstract;

	  /** connection parameters to create the DataStoreInfo */
	  private Map connectionParams;
	  
	/**
	 * DataStoreInfo constructor.
	 * <p>
	 * Creates a DataStoreInfo to represent an instance with default data.
	 * </p>
	 * @see defaultSettings()
	 */
	  public DataStoreConfig(){
		id = "";
		nameSpaceId = "";
		enabled = false;
		title = "";
		_abstract = "";
		connectionParams = new HashMap();
	  }
	
	/**
	 * DataStoreInfo constructor.
	 * <p>
	 * Creates a copy of the DataStoreInfo provided. All the datastructures are cloned. 
	 * </p>
	 * @param ds The datastore to copy.
	 */
	  public DataStoreConfig(DataStoreConfig ds){
	  	if(ds == null){
			throw new NullPointerException();
	  	}
	  	id = ds.getId();
		nameSpaceId = ds.getNameSpaceId();
	  	enabled = ds.isEnabled();
	  	_abstract = ds.getAbstract();
	  	try{
	  		connectionParams = CloneLibrary.clone(ds.getConnectionParams()); //clone?
	  	}catch(Exception e){
	  		connectionParams = new HashMap();  	
	  	}
	  }
	
	/**
	 * DataStoreInfo constructor.
	 * <p>
	 * Creates a copy of the DataStoreInfoDTO provided. All the datastructures are cloned. 
	 * </p>
	 * @param ds The datastore to copy.
	 */
	  public DataStoreConfig(DataStoreInfoDTO ds){
		if(ds == null){
			throw new NullPointerException();
		}
		id = ds.getId();
		nameSpaceId = ds.getNameSpaceId();
		enabled = ds.isEnabled();
		_abstract = ds.getAbstract();
		try{
			connectionParams = CloneLibrary.clone(ds.getConnectionParams()); //clone?
		}catch(Exception e){
			connectionParams = new HashMap();  	
		}
	  }

	/**
	 * Implement clone.
	 * <p>
	 * creates a clone of this object
	 * </p>
	 * @see java.lang.Object#clone()
	 * 
	 * @return A copy of this DataStoreInfo
	 */
	  public Object clone(){
	  	return new DataStoreConfig(this);
	  }
	
	/**
	 * Implement loadDTO.
	 * <p>
	 * Populates the data fields with the DataStoreInfoDTO provided.
	 * </p>
	 * @see org.vfny.geoserver.config.DataStructure#loadDTO(java.lang.Object)
	 * 
	 * @param obj the DataStoreInfoDTO to use.
	 * @return true when the param is valid and stored.
	 */
	  public boolean updateDTO(Object obj){
		if(obj == null || !(obj instanceof DataStoreInfoDTO)){
			return false;
		}
		DataStoreInfoDTO ds = (DataStoreInfoDTO)obj;
		id = ds.getId();
		nameSpaceId = ds.getNameSpaceId();
		enabled = ds.isEnabled();
		_abstract = ds.getAbstract();
		try{
			connectionParams = CloneLibrary.clone(ds.getConnectionParams()); //clone?
		}catch(Exception e){
			connectionParams = new HashMap();  	
		}
		return true;
	  }
	  
	  /**
	   * Implement toDTO.
	   * <p>
	   * Create a DataStoreInfoDTO from the current config object.
	   * </p>
	   * @see org.vfny.geoserver.config.DataStructure#toDTO()
	   * 
	   * @return The data represented as a DataStoreInfoDTO
	   */
	  public Object toDTO(){
		DataStoreInfoDTO ds = new DataStoreInfoDTO();
		ds.setId(id);
		ds.setNameSpaceId(nameSpaceId);
		ds.setEnabled(enabled);
		ds.setAbstract(_abstract);
		try{
			ds.setConnectionParams(CloneLibrary.clone(connectionParams));
		}catch(Exception e){
			// default already created  	
		}
		return ds;
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
	 * getConnectionParams purpose.
	 * <p>
	 * Description ...
	 * </p>
	 * @return
	 */
	public Map getConnectionParams() {
		return connectionParams;
	}

	/**
	 * isEnabled purpose.
	 * <p>
	 * Description ...
	 * </p>
	 * @return
	 */
	public boolean isEnabled() {
		return enabled;
	}

	/**
	 * getId purpose.
	 * <p>
	 * Description ...
	 * </p>
	 * @return
	 */
	public String getId() {
		return id;
	}

	/**
	 * getNameSpace purpose.
	 * <p>
	 * Description ...
	 * </p>
	 * @return
	 */
	public String getNameSpaceId() {
		return nameSpaceId;
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
		if(string != null)
		_abstract = string;
	}

	/**
	 * setConnectionParams purpose.
	 * <p>
	 * Description ...
	 * </p>
	 * @param map
	 */
	public void setConnectionParams(Map map) {
		if(map != null)
		connectionParams = map;
	}

	/**
	 * setEnabled purpose.
	 * <p>
	 * Description ...
	 * </p>
	 * @param b
	 */
	public void setEnabled(boolean b) {
		enabled = b;
	}

	/**
	 * setId purpose.
	 * <p>
	 * Description ...
	 * </p>
	 * @param string
	 */
	public void setId(String string) {
		if(string != null)
		id = string;
	}

	/**
	 * setNameSpace purpose.
	 * <p>
	 * Description ...
	 * </p>
	 * @param support
	 */
	public void setNameSpaceId(String support) {
		if(support != null)
		nameSpaceId = support;
	}

	/**
	 * setTitle purpose.
	 * <p>
	 * Description ...
	 * </p>
	 * @param string
	 */
	public void setTitle(String string) {
		if(string != null)
		title = string;
	}

}
