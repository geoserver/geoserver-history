/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */

package org.geoserver.data.gen;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;

import org.geotools.data.Repository;
import org.vfny.geoserver.global.GeoserverDataDirectory;


/**
 * Implementation of {@link Repository} 
 * This class interprets the data source name as a file name or an URL for
 * a property file containing the ds creation parameters
 * 
 * For shape files ending with .shp or SHP, the shape file could be passed as
 * name
 * 
 * 
 * @author Christian Mueller
 *
 */
public class DSFinderRepository extends org.geotools.data.gen.DSFinderRepository {
	
			
	

	
	protected URL getURLForLocation(String location) throws IOException{

		File f = GeoserverDataDirectory.findDataFile(location);
		URL url = null;		
		if (f.exists()) {
			url=f.toURI().toURL();
		} else {
			url = new URL(location);
		}		
		url = new URL(URLDecoder.decode(url.toExternalForm(),"UTF8"));			
		return url;
	}
	
}
