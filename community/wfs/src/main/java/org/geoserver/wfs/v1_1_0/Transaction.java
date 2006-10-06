package org.geoserver.wfs.v1_1_0;

import org.geoserver.data.GeoServerCatalog;
import org.geoserver.wfs.WFS;

/**
 * WFS 1.1 Transaction operation.
 * 
 * @author Justin Deoliveira, The Open Planning Project
 *
 */
public class Transaction extends org.geoserver.wfs.Transaction {

	public Transaction( WFS wfs, GeoServerCatalog catalog ) {
		super(wfs, catalog);
	}

}
