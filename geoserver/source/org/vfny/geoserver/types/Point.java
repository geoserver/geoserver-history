/* Copyright (c) 2001 Vision for New York - www.vfny.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root application directory.
 */

package org.vfny.geoserver.types;

import java.sql.*;
import java.util.*;

import org.postgresql.util.*;

/**
 * Implements an OGC simple type.
 *
 * @author Vision for New York
 * @author Rob Hranac 
 * @version 0.9 alpha, 11/01/01
 *
 */
public class Point extends Geometry {


		/** Comma delimeter: default coordinate delimeter for PostGIS */
		protected static final char SPACE_DELIMETER = ' ';

		/** Comma delimeter: default coordinate delimeter for GML */
		protected static final char COMMA_DELIMETER = ',';

		// THESE SHOULD BE MOVED TO BBOX OR SOMETHING
		/** Conversion factor numerator */
		private static final double CONVERT_N = 1200; 

		/** Conversion factor denomenator */
		private static final double CONVERT_D = 3937;

		/** The X coordinate of the point. */
		public double x;
		
		/** The Y coordinate of the point. */
		public double y;
		
		/** The Z coordinate of the point. */
		public double z;
		

		public Point() {
				type = POINT;
		}
		

		public Point(double x, double y, double z) {
				this();
				this.x = x;
				this.y = y;
				this.z = z;
				dimension = 3;
		}


		public Point(double x, double y) {
				this();
				this.x = x;
				this.y = y;
				this.z = 0.0;
				dimension = 2;
		}
	

		public String toString() {
				return "POINT (" + getValue() + ")";
		}
		

		public String getValue() {

				return getValueInternal( SPACE_DELIMETER, CONVERT_N / CONVERT_D );

		}


		public String getValue(char delimeter) {

				return getValueInternal( delimeter, CONVERT_N / CONVERT_D );

		}


		public String getValue(double conversionFactor) {

				return getValueInternal( SPACE_DELIMETER, conversionFactor );

		}


		public String getValue(char delimeter, double conversionFactor) {

				return getValueInternal( delimeter, conversionFactor );

		}
		

		private String getValueInternal(char coordinateDelimeter, double conversionFactor) {

				String tempDelimeter = new String();
				tempDelimeter = tempDelimeter.valueOf( coordinateDelimeter );

				x = x * conversionFactor;
				y = y * conversionFactor;

				if ( dimension == 3 ) {
						z = z * conversionFactor;
						return x + tempDelimeter + y + tempDelimeter + z;
				} else {
						return x + tempDelimeter + y;
				}

		}


		public double getX() {
				return x;
		}
		

		public double getY() {
				return y;
		}
	

		public double getZ() {
				return z;
		}
	

		public void setX(double x) {
				this.x = x;
		}


		public void setY(double y) {
				this.y = y;
		}


		public void setZ(double z) {
				this.z = z;
		}
	

		public void setX(int x) {
				this.x = (double)x;
		}


		public void setY(int y) {
				this.y = (double)y;
		}


		public void setZ(int z) {
				this.z = (double)z;
		}

}
