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
public class Polygon extends Geometry {

		protected LinearRing[] rings;
		
		public Polygon() {
				type = POLYGON;
		}

		public Polygon(LinearRing[] rings) {
				this();
				this.rings = rings;
				dimension = rings[0].dimension;
		}
	
		public String toString() {
				return "POLYGON " + getValue();
		}

		public String getValue() {
				StringBuffer b = new StringBuffer("(");
				for( int r = 0; r < rings.length; r++ ) {
						if( r > 0 ) b.append(",");
						b.append(rings[r].toString());
				}
				b.append(")");
				return b.toString();
		}
		
		public int numRings() {
				return rings.length;
		}
	
		// NOTE: CHANGED INT NAME TO 'INDEX' - PROBLEMS?
		public LinearRing getRing(int index) {
				if( index >= 0 & index < rings.length ) {
						return rings[index];
				}
				else {
						return null;
		}
	}
}
