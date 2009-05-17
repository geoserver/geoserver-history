/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */


package org.geoserver.data.gen;


import java.io.IOException;
import java.util.logging.Logger;

import org.geoserver.catalog.Catalog;
import org.geoserver.catalog.DataStoreInfo;
import org.geoserver.platform.GeoServerExtensions;
import org.geotools.data.DataAccess;
import org.geotools.data.DataStore;
import org.geotools.data.Repository;
import org.opengis.feature.type.Name;

/**
 * @author Christian Mueller
 * 
 * Repository implementation using the geoserver catalog
 *
 */
public class CatalogRepository implements Repository {
		private Catalog catalogObject;
		private Logger log; 
		
	public CatalogRepository() {
		super();		
		log= Logger.getLogger(this.getClass().getName());
	}
			
	
	

	public DataStore dataStore(Name name) {
                
                String workspace = name .getNamespaceURI();
                String localName=name.getLocalPart();
		
		DataStoreInfo info = catalogObject.getDataStoreByName(workspace,localName);
		if (info==null) {
			throw new RuntimeException("Cannot find datastore "+ localName + "in workspace "+ workspace);
		}
		try {
			return info.getDataStore(null);
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
	}


	public void initialize(Object source) {
		String beanName="catalog2";
		catalogObject = (Catalog) GeoServerExtensions.bean(beanName);
		if (catalogObject==null) {
			log.warning("Cannot find bean "+beanName);
			catalogObject=(Catalog) GeoServerExtensions.bean(Catalog.class);			
		}	
		if (catalogObject==null) {
			log.severe("Cannot find bean implementing "+Catalog.class.getName());
			throw new RuntimeException("Cannot find geoserver catalog");			
		}
	}




    public DataAccess<?, ?> access(Name name) {
        return dataStore(name);
    }

}
