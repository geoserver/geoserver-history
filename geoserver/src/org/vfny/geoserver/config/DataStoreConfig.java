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

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.factory.FactoryFinder;
import org.vfny.geoserver.global.dto.DataStoreInfoDTO;

/**
 * DataStoreInfo purpose.
 * <p>
 * Used to describe a datastore, typically one specified in the catalog.xml config file. 
 * <p>
 * 
 * @author dzwiers, Refractions Research, Inc.
 * @version $Id: DataStoreConfig.java,v 1.2.2.6 2004/01/10 06:13:31 emperorkefka Exp $
 */
public class DataStoreConfig{

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
			connectionParams =
				new HashMap( ds.getConnectionParams() );
		}catch(Exception e){
			connectionParams = new HashMap();  	
		}
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
	  public void update(DataStoreInfoDTO ds){
		if(ds == null){
			throw new NullPointerException("DataStoreInfo Data Transfer Object required");
		}
		id = ds.getId();
		nameSpaceId = ds.getNameSpaceId();
		enabled = ds.isEnabled();
		_abstract = ds.getAbstract();
		try{
			connectionParams = new HashMap(ds.getConnectionParams()); //clone?
		}catch(Exception e){
			connectionParams = new HashMap();  	
		}
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
	  public DataStoreInfoDTO toDTO(){
		DataStoreInfoDTO ds = new DataStoreInfoDTO();
		ds.setId(id);
		ds.setNameSpaceId(nameSpaceId);
		ds.setEnabled(enabled);
		ds.setAbstract(_abstract);
		try{
			ds.setConnectionParams( new HashMap(connectionParams));
		} catch(Exception e){
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
	
	// Access to Dyanmic Content
	/** It would be nice if we did not throw this away - but life is too short */
	public DataStore findDataStore() throws IOException  {
		return DataStoreFinder.getDataStore( connectionParams );
	}
	
}
