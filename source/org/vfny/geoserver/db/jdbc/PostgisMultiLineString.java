/* Copyright (c) 2002 Vision for New York - www.vfny.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root application directory.
 */
package org.vfny.geoserver.db.jdbc;

import java.sql.*;
import java.util.*;

import org.postgresql.util.*;

import org.vfny.geoserver.db.GMLMultiLineString;
import org.vfny.geoserver.types.LineString;

/**
 * Extends the GML LineString to read Postgis data.
 *
 * @author Vision for New York
 * @author Rob Hranac 
 * @version 0.9 alpha, 11/01/01
 *
 */
public class PostgisMultiLineString extends GMLMultiLineString {

		public PostgisMultiLineString() {
				super();
		}
		
		public PostgisMultiLineString(LineString[] lines) {
				super(lines);
		}

		/**
		 * This is called to construct a LinearRing from the 
		 * PostGIS string representation of a ring.
		 *
		 * @param value Definition of this ring in the PostGIS 
		 * string format.
		 */
		public PostgisMultiLineString(String value) throws SQLException {
				this();
				value = value.trim();
				if ( value.indexOf("MULTILINESTRING") == 0 ) {
						PGtokenizer t = new PGtokenizer(PGtokenizer.removePara(value.substring(15).trim()),',');
						int nlines = t.getSize();
						lines = new LineString[nlines];
						for( int p = 0; p < nlines; p++) {
								lines[p] = (LineString) new PostgisLineString(t.getToken(p));
						}
						dimension = lines[0].dimension;
				} else {
						throw new SQLException("postgis.multilinestringgeometry");
				}
		}
	
		
		/**
		 * This is called to construct a LinearRing from the 
		 * PostGIS string representation of a ring.
		 *
		 * @param value Definition of this ring in the PostGIS 
		 * string format.
		 */
		public void setPostgisMultiLineString(String value) throws SQLException {
				value = value.trim();
				if ( value.indexOf("MULTILINESTRING") == 0 ) {
						PGtokenizer t = new PGtokenizer(PGtokenizer.removePara(value.substring(15).trim()),',');
						int nlines = t.getSize();
						lines = new LineString[nlines];
						for( int p = 0; p < nlines; p++) {
								lines[p] = (LineString) new PostgisLineString(t.getToken(p));
						}
						dimension = lines[0].dimension;
				} else {
						throw new SQLException("postgis.multilinestringgeometry");
				}
		}
	

}
