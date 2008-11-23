package org.geoserver.restconfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.geoserver.rest.AutoXMLFormat;
import org.geoserver.rest.DataFormat;
import org.geoserver.rest.JSONFormat;
import org.geoserver.rest.MapResource;
import org.geoserver.rest.RestletException;

/**
 * List the Services in a GeoServer instance in multiple formats.
 * 
 * @todo: This should find the services dynamically.  One day...
 * @author David Winslow <dwinslow@openplans.org>
 */
public class ServiceListResource extends MapResource {

	@Override
	public Object getMap() throws RestletException {
		Map<String, Object> m = new HashMap<String, Object>();
		List<String> serviceNames = new ArrayList<String>();
		serviceNames.add("WMS");
		serviceNames.add("WCS");
		serviceNames.add("WFS");
		m.put("services", serviceNames);
		return m;
	}

	@Override
	public Map getSupportedFormats() {
        Map<String, DataFormat> formats = new HashMap<String, DataFormat>();
        formats.put("xml", new AutoXMLFormat());
        formats.put("json", new JSONFormat());
        formats.put(null, formats.get("xml"));
		return formats;
	}

}
