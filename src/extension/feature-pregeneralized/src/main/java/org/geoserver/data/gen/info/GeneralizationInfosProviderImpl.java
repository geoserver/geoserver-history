/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */




package org.geoserver.data.gen.info;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;

import org.vfny.geoserver.global.GeoserverDataDirectory;



/**
 * @author Christian Mueller
 * 
 * The default implementation for GeneralizationInfosProvider,
 * reading the info from an XML file.
 * 
 * The xml schema file is "/geninfos_1.0.xsd"
 * 
 *
 */
public class GeneralizationInfosProviderImpl extends org.geotools.data.gen.info.GeneralizationInfosProviderImpl {

	

	protected URL deriveURLFromSourceObject(Object source) throws IOException{
		if (source==null) 
			throw new IOException("Cannot read from null");
	
		
		if (source instanceof String) {		
			File f = GeoserverDataDirectory.findDataFile((String)source);
			URL url = null;		
			if (f.exists()) {
				url=f.toURI().toURL();
			} else {
				url = new URL((String) source);
			}		
			url = new URL(URLDecoder.decode(url.toExternalForm(),"UTF8"));			
			return url;
		}	
		
		throw new IOException("Cannot read from "+source);
	}
}
