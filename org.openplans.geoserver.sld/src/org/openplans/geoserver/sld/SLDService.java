package org.openplans.geoserver.sld;

import java.io.IOException;
import java.net.URI;
import java.util.Map;

import org.geotools.catalog.Catalog;
import org.geotools.catalog.GeoResource;
import org.geotools.catalog.ServiceInfo;
import org.geotools.catalog.defaults.AbstractFileService;
import org.geotools.util.ProgressListener;

public class SLDService extends AbstractFileService {

	public SLDService(Catalog parent, Map params, URI uri) {
		super(parent, params, uri);
	}

	protected GeoResource createMember(ProgressListener listener) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public ServiceInfo getInfo(ProgressListener monitor) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}
}
