/* Copyright (c) 2001 Vision for New York - www.vfny.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root application directory.
 */
package org.vfny.geoserver.db;

import java.sql.*;
import java.util.*;

import org.vfny.geoserver.types.*;

/**
 * Extends an OGC Polygon simple type to return GML data.
 *
 * @author Vision for New York
 * @author Rob Hranac 
 * @version 0.9 alpha, 11/01/01
 *
 */
public class GMLMultiPolygon extends MultiPolygon {

		// FIX
		protected static final String header1 = "\n   <";
		protected static final String header2 = ">\n    <LineString gid=\"";
		protected static final String header3 = "\" srsName=\"EPSG:";
		protected static final String header4 = "\">\n      <gml:coordinates decimal=\".\" cs=\",\" ts=\"\">";
		protected static final String header5 = "</gml:coordinates>\n    </LineString>\n   </";
		protected static final String header6 = ">";
	
		public GMLMultiPolygon() {
				super();
		}

		public GMLMultiPolygon(Polygon[] polygons) {
				super(polygons);
		}
		
		// FIX
		public String toGml(String tagName, String gid, String srs) {
				return header1 + tagName + header2 + gid + header3 + srs + header4 + getValue() + header5 + tagName + header6;
		}

		public String getValue() {
				StringBuffer b = new StringBuffer("");
				for( int p = 0; p < polygons.length; p++ ) {
						if( p > 0 ) b.append(",");
						b.append(polygons[p].getValue());
				}
				b.append(")");
				return b.toString();
		}

}
