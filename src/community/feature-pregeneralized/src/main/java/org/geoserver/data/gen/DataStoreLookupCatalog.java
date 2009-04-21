/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */


package org.geoserver.data.gen;


import java.util.logging.Logger;

import org.geoserver.catalog.Catalog;
import org.geoserver.platform.GeoServerExtensions;
import org.geotools.data.DataStore;
import org.geotools.data.gen.DataStoreLookup;

/**
 * @author Christian Mueller
 * 
 * DataStoreLookup implementation using the geoserver catalog
 *
 */
public class DataStoreLookupCatalog implements DataStoreLookup {
		private Catalog catalogObject;
		private Logger log; 
		
	public DataStoreLookupCatalog() {
		super();		
		log= Logger.getLogger(this.getClass().getName());
	}
			
	
	
	public DataStore getDataStoreFor(String name) {
			return (DataStore) catalogObject.getDataStoreByName(name);		
	}

	public DataStore getDataStoreFor(String workspace, String name) {
		
		return (DataStore) catalogObject.getDataStoreByName(workspace,name);		
	}


	public void initialize(Object source) {
		String beanName="geoServer2";
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

}
