/* Copyright (c) 2002 Vision for New York - www.vfny.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root application directory.
 */
package org.vfny.geoserver.db.jdbc;

import java.sql.*;
import java.util.*;

import org.postgresql.util.*;

import org.vfny.geoserver.db.GMLMultiPolygon;
import org.vfny.geoserver.types.Polygon;

/**
 * Extends the GML LineString to read Postgis data.
 *
 * @author Vision for New York
 * @author Rob Hranac 
 * @version 0.9 alpha, 11/01/01
 *
 */
public class PostgisMultiPolygon extends GMLMultiPolygon {

		public PostgisMultiPolygon() {
				super();
		}
		
		public PostgisMultiPolygon(Polygon[] polygons) {
				super(polygons);
		}

		/**
		 * This is called to construct a LinearRing from the 
		 * PostGIS string representation of a ring.
		 *
		 * @param value Definition of this ring in the PostGIS 
		 * string format.
		 */
		public PostgisMultiPolygon(String value) throws SQLException {
				this();
				value = value.trim();
				if ( value.indexOf("MULTIPOLYGON") == 0 ) {
						PGtokenizer t = new PGtokenizer(PGtokenizer.removePara(value.substring(12).trim()),',');
						int npolygons = t.getSize();
						polygons = new Polygon[npolygons];
						for( int p = 0; p < npolygons; p++) {
								polygons[p] = (Polygon) new PostgisPolygon(t.getToken(p));
						}
						dimension = polygons[0].dimension;
				} else {
						throw new SQLException("postgis.multipolygongeometry");
				}
		}
				
		/**
		 * This is called to construct a LinearRing from the 
		 * PostGIS string representation of a ring.
		 *
		 * @param value Definition of this ring in the PostGIS 
		 * string format.
		 */
		public void setPostgisMultiPolygon(String value) throws SQLException {
				value = value.trim();
				if ( value.indexOf("MULTIPOLYGON") == 0 ) {
						PGtokenizer t = new PGtokenizer(PGtokenizer.removePara(value.substring(12).trim()),',');
						int npolygons = t.getSize();
						polygons = new Polygon[npolygons];
						for( int p = 0; p < npolygons; p++) {
								polygons[p] = (Polygon) new PostgisPolygon(t.getToken(p));
						}
						dimension = polygons[0].dimension;
				} else {
						throw new SQLException("postgis.multipolygongeometry");
				}
		}
	

}
