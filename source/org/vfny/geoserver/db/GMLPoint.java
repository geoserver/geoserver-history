/* Copyright (c) 2002 Vision for New York - www.vfny.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root application directory.
 */
package org.vfny.geoserver.db;

import java.sql.*;
import java.util.*;

import org.vfny.geoserver.types.*;

/**
 * Implements an OGC simple type.
 *
 * @author Vision for New York
 * @author Rob Hranac 
 * @version 0.9 alpha, 11/01/01
 *
 */
public class GMLPoint extends Point {

		// THESE SHOULD BE MOVED TO BBOX OR SOMETHING
		/** Conversion factor numerator */
		private static final double CONVERT_N = 1200; 

		/** Conversion factor denomenator */
		private static final double CONVERT_D = 3937;

		// FIX
		protected static final String header1 = "\n   <";
		protected static final String header2 = ">\n    <gml:Point gid=\"";
		protected static final String header3 = "\" srsName=\"EPSG:";
		protected static final String header4 = "\">\n      <gml:coordinates decimal=\".\" cs=\",\" ts=\" \">";
		protected static final String header5 = "</gml:coordinates>\n    </gml:Point>\n   </";
		protected static final String header6 = ">";
	
		public GMLPoint() {
				super();
		}
		
		public GMLPoint(double x, double y) {
				super( x, y);
		}

		public GMLPoint(double x, double y, double z) {
				super( x, y, z);
		}

		// FIX
		public String toGml(String tagName, String featureName, String gid, String srs) {
				return header1 + featureName + "." + tagName + header2 + gid + header3 + srs + header4 + getValue(COMMA_DELIMETER, CONVERT_N/CONVERT_D) + header5 + featureName + "." + tagName + header6;
		}


}
