/* Copyright (c) 2002 Vision for New York - www.vfny.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root application directory.
 */
package org.vfny.geoserver.db.jdbc;

import java.sql.*;
import java.util.*;

import org.postgresql.util.*;

import org.vfny.geoserver.db.GMLPolygon;
import org.vfny.geoserver.types.LinearRing;

/**
 * Extends the GML LineString to read Postgis data.
 *
 * @author Vision for New York
 * @author Rob Hranac 
 * @version 0.9 alpha, 11/01/01
 *
 */
public class PostgisPolygon extends GMLPolygon {

		public PostgisPolygon() {
				super();
		}
		
		public PostgisPolygon(LinearRing[] rings) {
				super(rings);
		}

		public PostgisPolygon(String value) throws SQLException {
				this();
				value = value.trim();
				if ( value.indexOf("POLYGON") == 0 ) {
						value = value.substring(7).trim();
				}
				PGtokenizer t = new PGtokenizer(PGtokenizer.removePara(value),',');
				int nrings = t.getSize();
				rings = new LinearRing[nrings];
				for( int r = 0; r < nrings; r++) {
						rings[r] = (LinearRing) new PostgisLinearRing(t.getToken(r));
				}
				dimension = rings[0].dimension;
		}
	
		public void setPostgisPolygon(String value) throws SQLException {
				value = value.trim();
				if ( value.indexOf("POLYGON") == 0 ) {
						value = value.substring(7).trim();
				}
				PGtokenizer t = new PGtokenizer(PGtokenizer.removePara(value),',');
				int nrings = t.getSize();
				rings = new LinearRing[nrings];
				for( int r = 0; r < nrings; r++) {
						rings[r] = (LinearRing) new PostgisLinearRing(t.getToken(r));
				}
				dimension = rings[0].dimension;
		}
	

}
