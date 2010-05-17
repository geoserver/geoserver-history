/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.wcs.xml;

import org.geoserver.config.GeoServer;
import org.geoserver.ows.adapters.XmlRequestReaderAdapter;
import org.geoserver.wcs.WCSInfo;

/**
 * Bridge towards the old the old xml readers that injects the proper service
 * info object in the superclass
 * 
 * @author Andrea Aime
 * 
 */
public class WCSXmlRequestReaderAdapter extends XmlRequestReaderAdapter {

	public WCSXmlRequestReaderAdapter(String namespace, String local,
			GeoServer gs, Class delegate) {
		super(namespace, local, gs.getService(WCSInfo.class), delegate);
	}

}
