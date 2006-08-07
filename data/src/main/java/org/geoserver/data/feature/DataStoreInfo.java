/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.data.feature;


import java.util.Map;

import org.geotools.data.DataStore;
import org.geotools.resources.Utilities;


/**
 * This is the configuration iformation for one DataStore. This class can also
 * generate real datastores.
 *
 * @author Gabriel Rold?n
 * @author dzwiers
 * @author Justin Deoliveira
 * @version $Id: DataStoreInfo.java,v 1.14 2004/06/26 19:51:24 jive Exp $
 */
public class DataStoreInfo  {

	/**
	 * Id of the datastore
	 */
    private String id;
    /**
     * The namespace prefix given to the datstore
     */
    private String namespacePrefix;
    /**
     * Flag indicating wether the datastore is enabled or nojt.
     */
    private boolean enabled;
    /**
     * Title of datastore.
     */
    private String title;
    /**
     * Abstract description of datastore.
     */
    private String _abstract;
    
    /** 
     * Storage for metadata 
     */
    private Map meta;
    /**
     * The underlying datastore.
     */
    DataStore dataStore;
    
   
    /**
     * getId purpose.
     * 
     * <p>
     * Returns the dataStore's id.
     * </p>
     *
     * @return String the id.
     */
    public String getId() {
        return id;
    }

    public void setId(String id) {
		this.id = id;
	}
    
    /**
     * @return The namespace prefix for the datastore.
     */
    public String getNamespacePrefix() {
    		return namespacePrefix;
    }
    
    public void setNamespacePrefix( String namespacePrefix ) {
    		this.namespacePrefix = namespacePrefix;
    }
   
    /**
     * isEnabled purpose.
     * 
     * <p>
     * Returns true when the data store is enabled.
     * </p>
     *
     * @return true when the data store is enabled.
     */
    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
    
    /**
     * getTitle purpose.
     * 
     * <p>
     * Returns the dataStore's title.
     * </p>
     *
     * @return String the title.
     */
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
		this.title = title;
	}
    
    /**
     * getAbstract purpose.
     * 
     * <p>
     * Returns the dataStore's abstract.
     * </p>
     *
     * @return String the abstract.
     */
    public String getAbstract() {
        return _abstract;
    }

    public void setAbstract( String _abstract ) {
    		this._abstract = _abstract;
    }
    
    /**
     * Implement containsMetaData.
     *
     * @param key
     *
     * @return
     *
     * @see org.geotools.data.MetaData#containsMetaData(java.lang.String)
     */
    public boolean containsMetaData(String key) {
        return meta.containsKey(key);
    }

    /**
     * Implement putMetaData.
     *
     * @param key
     * @param value
     *
     * @see org.geotools.data.MetaData#putMetaData(java.lang.String,
     *      java.lang.Object)
     */
    public void putMetaData(String key, Object value) {
        meta.put(key, value);
    }

    /**
     * Implement getMetaData.
     *
     * @param key
     *
     * @return
     *
     * @see org.geotools.data.MetaData#getMetaData(java.lang.String)
     */
    public Object getMetaData(String key) {
    		return meta.get(key);
    }

    /**
     * The underlying datastore.
     * @return
     */
    public DataStore getDataStore() {
		return dataStore;
	}
    
    public void setDataStore(DataStore dataStore) {
		this.dataStore = dataStore;
	}
    
    public boolean equals(Object obj) {
    		if ( !( obj instanceof DataStoreInfo ) )  {
    			return false;
    		}
    		
    		DataStoreInfo other = (DataStoreInfo) obj;
    		return Utilities.equals( id, other.id ) && 
    			Utilities.equals( namespacePrefix, other.namespacePrefix ) &&
    			Utilities.equals( title, other.title ) && 
    			Utilities.equals( _abstract, other._abstract ) && 
    			enabled == other.enabled;
    	}
    
    public int hashCode() {
    		int PRIME = 1000003;
        int result = 0;

        result = id != null ? PRIME * result + id.hashCode() : result;
        result = namespacePrefix != null ? PRIME * result + namespacePrefix.hashCode() : result;
        result = title != null ? PRIME * result + title.hashCode() : result;
        result = _abstract != null ? PRIME * result + title.hashCode() : result;
        
        return result;
    }
   
    public String toString() {
    		StringBuffer buffer = new StringBuffer();
    		buffer.append( "DataStoreConfig[namespace=").append( getNamespacePrefix());
    		buffer.append(", enabled=").append(isEnabled());
    		buffer.append(", abstract=").append(getAbstract());
   	    buffer.append("]");
                                                          
    	    return buffer.toString();
    }

}
