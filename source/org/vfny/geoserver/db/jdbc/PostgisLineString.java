/* Copyright (c) 2001 Vision for New York - www.vfny.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root application directory.
 */
package org.vfny.geoserver.db.jdbc;

import java.sql.*;
import java.util.*;

import org.postgresql.util.*;

import org.vfny.geoserver.types.Point;
import org.vfny.geoserver.db.GMLLineString;

/**
 * Extends the GML LineString to read Postgis data.
 *
 * @author Vision for New York
 * @author Rob Hranac 
 * @version 0.9 alpha, 11/01/01
 *
 */
public class PostgisLineString extends GMLLineString {

		public PostgisLineString() {
				super();
		}
				
		public PostgisLineString(Point[] points) {
				super(points);
		}

		public PostgisLineString(String value)
				throws SQLException, PSQLException {

				this();
				value = value.trim();
				if ( value.indexOf("LINESTRING") == 0 ) {
						value = value.substring(10).trim();
				}
				PGtokenizer t = new PGtokenizer(PGtokenizer.removePara(value),',');
				int npoints = t.getSize();
				points = new Point[npoints];
				for( int p = 0; p < npoints; p++) {
						points[p] = (Point) new PostgisPoint(t.getToken(p));
				}
				dimension = points[0].dimension;
		}

		public void setPostgisLineString(String value)
				throws SQLException, PSQLException {

				value = value.trim();
				if ( value.indexOf("LINESTRING") == 0 ) {
						value = value.substring(10).trim();
				}
				PGtokenizer t = new PGtokenizer(PGtokenizer.removePara(value),',');
				int npoints = t.getSize();
				points = new Point[npoints];
				for( int p = 0; p < npoints; p++) {
						points[p] = (Point) new PostgisPoint(t.getToken(p));
				}
				dimension = points[0].dimension;
		}


}
