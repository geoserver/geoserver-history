/* Copyright (c) 2001 Vision for New York - www.vfny.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root application directory.
 */
package org.vfny.geoserver.db;

import java.sql.*;
import java.util.*;

import org.vfny.geoserver.types.*;

/**
 * Extends an OGC LineString simple type to return GML data.
 *
 * @author Vision for New York
 * @author Rob Hranac 
 * @version 0.9 alpha, 11/01/01
 *
 */
public class GMLLineString extends LineString {

		/** Comma delimeter: default coordinate delimeter for PostGIS */
		protected static final char SPACE_DELIMETER = ' ';

		/** Comma delimeter: default coordinate delimeter for GML */
		protected static final char COMMA_DELIMETER = ',';

		// THESE SHOULD BE MOVED TO BBOX OR SOMETHING
		/** Conversion factor numerator */
		private static final double CONVERT_N = 1200; 

		/** Conversion factor denomenator */
		private static final double CONVERT_D = 3937;

		protected static final String header1 = "\n   <";
		protected static final String header2 = ">\n    <gml:LineString gid=\"";
		protected static final String header3 = "\" srsName=\"EPSG:";
		protected static final String header4 = "\">\n      <gml:coordinates decimal=\".\" cs=\",\" ts=\" \">";
		protected static final String header5 = "</gml:coordinates>\n    </gml:LineString>\n   </";
		protected static final String header6 = ">";
	
		public GMLLineString() {
				super();
		}
		
		public GMLLineString(Point[] points) {
				super(points);
		}

		public String toGml(String tagName, String featureType, String gid, String srs) {
				return header1 + featureType + "." + tagName + header2 + gid + header3 + srs + header4 + getValue() + header5 + featureType + "." + tagName + header6;
		}

		public String getValue() {
				StringBuffer b = new StringBuffer("");
				for( int p = 0; p < points.length; p++ ) {
						if( p > 0 )
								b.append(" ");
						b.append( points[p].getValue(COMMA_DELIMETER, CONVERT_N/CONVERT_D) );
				}
				return b.toString();
		}


}
