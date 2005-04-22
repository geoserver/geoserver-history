package org.vfny.geoserver.global;

/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */

import java.io.File;
import javax.servlet.ServletContext;

/**
 *   This class allows for abstracting the location of the Geoserver Data directory.  Some people call this "GEOSERVER_HOME".
 *   
 *   Inside this directory should be two more directories:
 *     a. "WEB-INF/"  Inside this is a catalog.xml
 *     b. "data/"     Inside this is a set of other directories.
 *    
 *   For the exact content of these directories, see any existing geoserver install's server/geoserver directory.
 * 
 *   In order to find the geoserver data directory the following steps take place:
 *  
 *    1. search for the "GEOSERVER_DATA_DIR" system property.
 *         this will most likely have come from "java -DGEOSERVER_DATA_DIR=..." or from you web container
 *    2. search for a "GEOSERVER_DATA_DIR" in the web.xml document
 *        <context-param>
 *             <param-name>GEOSERVER_DATA_DIR</param-name>
 *             <param-value>...</param-value>
 *         </context-param>
 *    3. It defaults to the old behavior - ie. the application root - usually "server/geoserver" in your .WAR.
 *  
 * 
 *    NOTE: a set method is currently undefined because you should either modify you web.xml or
 *          set the environment variable and re-start geoserver.
 * 
 * @author dblasby
 *
 */
public class GeoserverDataDirectory
{
	/**
	 *   See the class documentation for more details.
	 *   1. search for the "GEOSERVER_DATA_DIR" system property. 
	 *   2. search for a "GEOSERVER_DATA_DIR" in the web.xml document
	 *   3. It defaults to the old behavior - ie. the application root - usually "server/geoserver" in your .WAR.
	 * @return  location of the geoserver data dir
	 */
	static public File getGeoserverDataDirectory(ServletContext servContext)
	{
		//see if there's a system property
		String prop = System.getProperty("GEOSERVER_DATA_DIR");
		if (prop != null)
		{
			 //its defined!!
			return new File(prop);
		}
		
		//try the webxml
		String loc = servContext.getInitParameter("GEOSERVER_DATA_DIR");
		if (loc != null)
		{
			//its defined!!
			return new File(loc);
		}
		
		//return default
		String rootDir = servContext.getRealPath("/");
		return new File (rootDir);
	}
}


