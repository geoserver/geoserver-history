/* Copyright (c) 2002 Vision for New York - www.vfny.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root application directory.
 */
package org.vfny.geoserver.db.jdbc;

import java.sql.*;
import java.util.*;

import org.postgresql.util.*;

import org.vfny.geoserver.db.GMLLinearRing;
import org.vfny.geoserver.types.Point;
import org.vfny.geoserver.responses.WfsException;

/**
 * Extends the GML LineString to read Postgis data.
 *
 * @author Vision for New York
 * @author Rob Hranac 
 * @version 0.9 alpha, 11/01/01
 *
 */
public class PostgisLinearRing extends GMLLinearRing {
		
		public PostgisLinearRing(Point[] points) {
				super(points);
		}

		/**
		 * This is called to construct a LinearRing from the 
		 * PostGIS string representation of a ring.
		 *
		 * @param value Definition of this ring in the PostGIS 
		 * string format.
		 */
		public PostgisLinearRing (String value)
				throws SQLException, PSQLException {

				PGtokenizer t = new PGtokenizer(PGtokenizer.removePara(value.trim()),',');
				int npoints = t.getSize();

				points = new Point[npoints];
				for( int p = 0; p < npoints; p++ ) {
						points[p] = (Point) new PostgisPoint(t.getToken(p));
				}
				dimension = points[0].dimension;

		}
		
 		public void setPostgisLinearRing(String value)
				throws SQLException, PSQLException {
				
				PGtokenizer t = new PGtokenizer(PGtokenizer.removePara(value.trim()),',');
				int npoints = t.getSize();

				points = new Point[npoints];
				for( int p = 0; p < npoints; p++ ) {
						points[p] = (Point) new PostgisPoint(t.getToken(p));
				}
				dimension = points[0].dimension;
		}
	

}
