package org.geoserver.restconfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.geoserver.rest.XStreamFormat;
import org.geoserver.rest.DataFormat;
import org.geoserver.rest.JSONFormat;
import org.geoserver.rest.MapResource;
import org.geoserver.rest.RestletException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * Configure a GeoServer service.
 * 
 * @todo: This should find the services dynamically.  One day...
 * @author David Winslow <dwinslow@openplans.org>
 */
public class ServiceConfigurationResource extends MapResource implements ApplicationContextAware{
    ApplicationContext myApplicationContext;

    public void setApplicationContext(ApplicationContext context){
        myApplicationContext = context;
    }

	@Override
	public Object getMap() throws RestletException {
        String service = (String)getRequest().getAttributes().get("service");
        String bean = service.toLowerCase() + "Config";
        return myApplicationContext.getBean(bean);
	}

	@Override
	public Map getSupportedFormats() {
        Map<String, DataFormat> formats = new HashMap<String, DataFormat>();
        formats.put("xml", new XStreamFormat());
        formats.put(null, formats.get("xml"));
		return formats;
	}

}
