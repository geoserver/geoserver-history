package org.openplans.geoserver.sld;

import java.io.IOException;
import java.net.URI;

import org.geotools.catalog.GeoResourceInfo;
import org.geotools.catalog.Service;
import org.geotools.catalog.defaults.AbstractFileGeoResource;
import org.geotools.catalog.defaults.DefaultGeoResourceInfo;
import org.geotools.styling.Style;
import org.geotools.util.ProgressListener;

public class SLDGeoResource extends AbstractFileGeoResource {

	GeoResourceInfo info;
	
	public SLDGeoResource(Service parent, URI uri) {
		super(parent, uri);
	}

	public GeoResourceInfo getInfo(ProgressListener monitor) throws IOException {
		if (info != null) {
			String title = getURI().toString();
			String desc = "Styled Layer Descriptor";
			String[] keywords = new String[]{
				title, desc, "SLD"
			};
			
			info = new DefaultGeoResourceInfo(
				title,title,desc,null,null,null,keywords,null
			);
		}
		
		return info;
	}

	protected boolean canResolveInternal(Class adaptee) {
		return adaptee.isAssignableFrom(Style.class);
	}
	
	protected Object resolveInternal(Class clazz, ProgressListener listener) throws IOException {
		if (clazz.isAssignableFrom(Style.class)) {
			//parse sld 
		}
		
		return null;
	}
}
