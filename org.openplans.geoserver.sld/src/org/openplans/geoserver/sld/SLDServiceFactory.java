package org.openplans.geoserver.sld;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.geotools.catalog.Catalog;
import org.geotools.catalog.Service;
import org.geotools.catalog.ServiceFactory;
import org.geotools.data.DataStoreFactorySpi.Param;

public class SLDServiceFactory implements ServiceFactory {

	public static final Param URI = new Param("uri", URI.class);

	public Service createService(Catalog catalog, URI uri, Map params) {

		// the sld file uri
		URI sld = null;

		// first check params
		if (params != null && params.containsKey(URI.key)) {
			sld = (URI) params.get(URI.key);
		}

		// try the uri passed in
		if (sld == null && uri != null) {
			sld = uri;
		}

		// make sure we can process the uri
		if (sld != null && canProcess(sld)) {
			return new SLDService(catalog, params, sld);
		}

		// no luck
		return null;
	}

	public Map createParams(URI uri) {
		HashMap params = new HashMap();
		params.put(URI.key,uri);
		
		return params;
	}
	
	public boolean canProcess(URI uri) {
		//check for a .sld or .xml file.
		File file = null;
		try {
			file = new File(uri.toURL().getFile());
		} 
		catch (MalformedURLException e) {
			return false;
		}
		
		if (file.getName() == null)
			return false;
		
		String name = file.getName();
		if (name.length() > 4) {
			String prefix = name.substring(name.length()-4);
			return prefix.equalsIgnoreCase(".SLD") || prefix.equalsIgnoreCase(".XML");
		}
		
		return false;
	}

	

}
