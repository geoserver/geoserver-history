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
public class XmlLineString extends LineString {

		private static final String header1 = "\n   <";
		private static final String header2 = ">\n    <LineString gid=\"";
		private static final String header3 = "\" srsName=\"EPSG:";
		private static final String header4 = "\">\n      <gml:coordinates decimal=\".\" cs=\",\" ts=\"\">";
		private static final String header5 = "</gml:coordinates>\n    </LineString>\n   </";
		private static final String header6 = ">";
	
		public XmlLineString() 
		{
				super();
		}
		
		public XmlLineString(String value) throws SQLException
		{
				super(value);
		}

		public void setXmlLineString(String value) throws SQLException {
				value = value.trim();
				if ( value.indexOf("LINESTRING") == 0 ) {
						value = value.substring(10).trim();
				}
				PGtokenizer t = new PGtokenizer(PGtokenizer.removePara(value),',');
				int npoints = t.getSize();
				points = new Point[npoints];
				for( int p = 0; p < npoints; p++) {
						points[p] = new Point(t.getToken(p));
				}
				dimension = points[0].dimension;
		}
		
		
		public String toXml(String tagName, String gid, String srs) {
				return header1 + tagName + header2 + gid + header3 + srs + header4 + getValue() + header5 + tagName + header6;
		}

		public String getValue() {
				StringBuffer b = new StringBuffer("");
				for( int p = 0; p < points.length; p++ ) {
						if( p > 0 ) b.append(",");
						b.append(points[p].getValue());
				}
				return b.toString();
		}


}
