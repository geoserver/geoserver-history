/* Copyright (c) 2002 Vision for New York - www.vfny.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root application directory.
 */
package org.vfny.geoserver.db.jdbc;

import java.sql.*;
import java.util.*;

import org.postgresql.util.*;

import org.vfny.geoserver.db.GMLPoint;
import org.vfny.geoserver.responses.WfsException;

/**
 * Extends the GML LineString to read Postgis data.
 *
 * @author Vision for New York
 * @author Rob Hranac 
 * @version 0.9 alpha, 11/01/01
 *
 */
public class PostgisPoint extends GMLPoint {


		public PostgisPoint() {
				super();
		}
		

		public PostgisPoint(double x, double y) {
				super( x, y);
		}


		public PostgisPoint(double x, double y, double z) {
				super( x, y, z);
		}


		public PostgisPoint(String value) 
				throws SQLException, PSQLException {
				
				this();
				setInternalValue(value, SPACE_DELIMETER);

		}


		public PostgisPoint(String value, char delimeter) 
				throws SQLException, PSQLException {
				
				this();
				setInternalValue(value, delimeter);

		}


		public void setPostgisPoint(String value) 
				throws SQLException {

				setInternalValue(value, SPACE_DELIMETER);
		}


		public void setPostgisPoint(String value, char delimeter) 
				throws SQLException {

				setInternalValue(value, delimeter);
		}


		private void setInternalValue(String value, char coordinateDelimeter)
				throws SQLException {

				value = value.trim();

				if ( value.indexOf("POINT") == 0 ) {
						value = value.substring(5).trim();
				}

				PGtokenizer t = new PGtokenizer(PGtokenizer.removePara(value), coordinateDelimeter);
				try {
						if ( t.getSize() == 3 ) {
								x = Double.valueOf(t.getToken(0)).doubleValue();
								y = Double.valueOf(t.getToken(1)).doubleValue();
								z = Double.valueOf(t.getToken(2)).doubleValue();
								dimension = 3;
						} else {
								x = Double.valueOf(t.getToken(0)).doubleValue();
								y = Double.valueOf(t.getToken(1)).doubleValue();
								z = 0.0;
								dimension = 2;
						}
				}
				catch(NumberFormatException e) {
						throw new PSQLException("postgis.Point",e.toString());
				}
		}


}
