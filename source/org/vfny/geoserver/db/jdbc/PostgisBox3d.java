/* Copyright (c) 2001 Vision for New York - www.vfny.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root application directory.
 */

package org.vfny.geoserver.db.jdbc;

import java.sql.*;

import org.postgresql.util.*;
import org.apache.log4j.Category;

import org.vfny.geoserver.types.Point;

/**
 * Implements an OGC simple type.
 *
 * @author Vision for New York
 * @author Rob Hranac 
 * @version 0.9 alpha, 11/01/01
 *
 */
public class PostgisBox3d extends PGobject {

		/** Comma delimeter: default coordinate delimeter for PostGIS */
		protected static final char SPACE_DELIMETER = ' ';

		/** Comma delimeter: default coordinate delimeter for GML */
		protected static final char COMMA_DELIMETER = ',';

		// THESE SHOULD BE MOVED TO BBOX OR SOMETHING
		/** Conversion factor numerator */
		private static final double CONVERT_N = 1200; 

		/** Conversion factor denomenator */
		private static final double CONVERT_D = 3937;

		/** End conversion factor denomenator */
		private static final double CONVERSION = CONVERT_N / CONVERT_D;

		// initializes log file
		private Category _log = Category.getInstance(PostgisBox3d.class.getName());

		/**
		 * The lower left bottom corner of the box.
		 */
		Point llb;
		
		/**
		 * The upper right top corner of the box.
		 */
		Point urt;
		
		/**
		 * The upper right top corner of the box.
		 */
		String srid;
		
		
		public PostgisBox3d() {}
		
		public PostgisBox3d(Point llb, Point urt) {
				this.llb = llb;
				this.urt = urt;
		}
		
		public PostgisBox3d(String value) throws SQLException {
				setValue(value);
		}
		
		public void setValue(String value)
				throws SQLException, PSQLException {

				value = value.trim();
				if( value.startsWith("BOX3D") ) {
						value = value.substring(5);
				}
				PGtokenizer t = new PGtokenizer(PGtokenizer.removePara(value.trim()),' ');
				llb = (Point) new PostgisPoint(t.getToken(0), ',');
				urt = (Point) new PostgisPoint(t.getToken(1), ',');

		}

		public void setBoundingBox(String value)
				throws SQLException, PSQLException {

				value = value.trim();
				PGtokenizer t = new PGtokenizer(PGtokenizer.removePara(value.trim()),' ');
				llb = (Point) new PostgisPoint(t.getToken(0), ',');
				urt = (Point) new PostgisPoint(t.getToken(1), ',');

		}
		
		public String getValue() {
				return "BOX3D (" + llb.getValue(COMMA_DELIMETER, CONVERT_N/CONVERT_D) + " " + urt.getValue(COMMA_DELIMETER, CONVERT_N/CONVERT_D) + ")";
		}

		public String getEvilValue() {
				return "BOX3D (" + llb.getValue(SPACE_DELIMETER, 1/CONVERSION) + "," + urt.getValue(SPACE_DELIMETER, 1/CONVERSION) + ")";
		}

		public String getBoundingBox() {
				return " && GeometryFromText('" + this.getEvilValue() + "'::box3d," + this.getSrid() +")";				
		}

		public void setSrid(String srid) {
				this.srid = srid;				
		}

		public String getSrid() {
				return this.srid;				
		}

		public String toString() {
				return getValue();
		}
		
		public Object clone() {
				PostgisBox3d obj = new PostgisBox3d(llb,urt);
				obj.setType(type);
				return obj;
		}
		
}
