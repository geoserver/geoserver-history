/* Copyright (c) 2001 Vision for New York - www.vfny.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root application directory.
 */

package org.vfny.geoserver.types;

import org.postgresql.util.*;
import java.sql.*;
import java.util.*;

/**
 * Implements an OGC simple type.
 *
 * @author Vision for New York
 * @author Rob Hranac 
 * @version 0.9 alpha, 11/01/01
 *
 */
public class MultiPolygon extends Geometry {

		protected Polygon[] polygons;
		
		public MultiPolygon() {
				type = MULTIPOLYGON;
		}
		
		public MultiPolygon(Polygon[] polygons) {
				this();
				this.polygons = polygons;
				dimension = polygons[0].dimension;
		}
		
		public String toString() {
				return "MULTIPOLYGON " + getValue();
		}
		
		public String getValue() {
				StringBuffer b = new StringBuffer("(");
				for( int p = 0; p < polygons.length; p++ ) {
						if( p > 0 ) b.append(",");
						b.append(polygons[p].getValue());
				}
				b.append(")");
				return b.toString();
		}

		public int numPolygons() {
				return polygons.length;
		}

		public Polygon getPolygon(int index) {
				if ( index >= 0 & index < polygons.length ) {
						return polygons[index];
				}
				else {
						return null;
				}
		}	
		
}
