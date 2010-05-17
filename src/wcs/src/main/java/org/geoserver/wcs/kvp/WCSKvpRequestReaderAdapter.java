/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */

package org.geoserver.wcs.kvp;

import org.geoserver.config.GeoServer;
import org.geoserver.ows.adapters.KvpRequestReaderAdapter;
import org.geoserver.wcs.WCSInfo;

/**
 * Bridge towards the old the old kvp readers that injects the proper service
 * info object in the superclass
 * 
 * @author Andrea Aime
 * 
 */
public class WCSKvpRequestReaderAdapter extends KvpRequestReaderAdapter {

	public WCSKvpRequestReaderAdapter(Class requestBean, Class delegateClass,
			GeoServer geoServer) {
		super(requestBean, delegateClass, geoServer.getService("wcs", WCSInfo.class));
	}

}
