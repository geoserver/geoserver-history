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
package org.vnfy.geoserver.config.data;

import java.util.Map;
import java.util.HashMap;

import org.vnfy.geoserver.config.CloneLibrary;
/**
 * DataStore purpose.
 * <p>
 * Description of DataStore ...
 * <p>
 * 
 * @author dzwiers, Refractions Research, Inc.
 * @version $Id: DataStore.java,v 1.1.2.1 2003/12/31 00:35:04 dmzwiers Exp $
 */
public class DataStore implements Cloneable{

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

	  /** connection parameters to create the DataStore */
	  private Map connectionParams;
	  
	  public DataStore(){
	  	defaultSettings();
	  }
	  
	  private void defaultSettings(){
		id = "";
		nameSpaceId = "";
		enabled = false;
		title = "";
		_abstract = "";
		connectionParams = new HashMap();
	  }
	  
	  public DataStore(DataStore ds){
	  	if(ds == null){
	  		defaultSettings();
	  		return;
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
	  
	  public Object clone(){
	  	return new DataStore(this);
	  }
	  
	  public boolean equals(Object obj){
	  	DataStore ds = (DataStore)obj;
	  	boolean r = true;
		r = r && id == ds.getId();
		r = r && nameSpaceId.equals(ds.getNameSpaceId());
		r = r && enabled == ds.isEnabled();
		r = r && _abstract == ds.getAbstract();
		if(connectionParams != null)
			r = r && connectionParams.equals(ds.getConnectionParams());
		else if(ds.getConnectionParams()!=null) return false;
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
	 * set_abstract purpose.
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
