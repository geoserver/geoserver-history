/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.wfs;

import java.io.IOException;
import java.util.Map;
import java.util.NoSuchElementException;

import org.geotools.catalog.Service;
import org.geotools.data.DataStore;


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

    private String id;
    private String namespacePrefix;
    private boolean enabled;
    private String title;
    private String _abstract;
    
    /** Storage for metadata */
    private Map meta;
    /**
     * catalog service handle.
     */
    Service handle;
    
    public DataStoreInfo( Service handle ) {
    		this.handle = handle;
    }
    
    /**
     * @return The service handle from the local catalog.
     */
    public Service getCatalogHandle() {
		return handle;
	}
    
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
     * Get Connect params.
     * 
     * <p>
     * This is used to smooth any relative path kind of issues for any file
     * URLS. This code should be expanded to deal with any other context
     * sensitve isses dataStores tend to have.
     * </p>
     *
     * @return DOCUMENT ME!
     *
     * @task REVISIT: cache these?
     */
    //JD: move this code somewhere else
//    public static Map getParams(Map m, String baseDir) {
//        Map params = Collections.synchronizedMap(new HashMap(m));
//
//        for (Iterator i = params.entrySet().iterator(); i.hasNext();) {
//            Map.Entry entry = (Map.Entry) i.next();
//            String key = (String) entry.getKey();
//            Object value = entry.getValue();
//
//            try {
//	            	//TODO: this code is a pretty big hack, using the name to 
//	            	// determine if the key is a url, could be named something else
//	            	// and still be a url
//                if (key != null && key.matches(".* *url") && value instanceof String) {
//                    String path = (String) value;
//                    LOGGER.finer("in string url");
//                    if (path.startsWith("file:data/")) {
//                        path = path.substring(5); // remove 'file:' prefix
//
//                        File file = new File(baseDir, path);
//                        entry.setValue(file.toURL().toExternalForm());
//                    }
//                    //Not sure about this
//                } else if (value instanceof URL
//                        && ((URL) value).getProtocol().equals("file")) {
//                		LOGGER.finer("in URL url");
//                    URL url = (URL) value;
//                    String path = url.getPath();
//                    LOGGER.finer("path is " + path);
//                    if (path.startsWith("data/")){
//						File file = new File(baseDir, path);
//						entry.setValue(file.toURL());
//				    }
//                } 
//            } catch (MalformedURLException ignore) {
//                // ignore attempt to fix relative paths
//            }
//        }
//
//        return params;
//    }

    /**
     * Resolves the underlying catalog handle into a datatsore.
     * 
     * @throws IOException Any I/O errors occor
     * @throws NoSuchElementException DataStore could not be resolved
     */
    public DataStore dataStore() throws NoSuchElementException, IOException {
    		DataStore dataStore = 
    			(DataStore) handle.resolve( DataStore.class, null );
    		
    		if ( dataStore == null ) {
    			String msg = "Could not resolve datastore";
    			throw new NoSuchElementException( msg );
    		}
    		
    		return dataStore;
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
     * @return The namespace prefix for the datastore.
     */
    public String getNamespacePrefix() {
    		return namespacePrefix;
    }
    
    public void setNamespacePrefix( String namespacePrefix ) {
    		this.namespacePrefix = namespacePrefix;
    }

    /**
     * Implement toString.
     *
     * @return String
     *
     * @see java.lang.Object#toString()
     */
    public String toString() {
    		StringBuffer buffer = new StringBuffer();
    		buffer.append( "DataStoreConfig[namespace=").append( getNamespacePrefix());
    		buffer.append(", enabled=").append(isEnabled());
    		buffer.append(", abstract=").append(getAbstract());
    		buffer.append(", params=").append( handle.getConnectionParams() );
    	    buffer.append("]");
                                                          
    	    return buffer.toString();
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
}
