/* Copyright (c) 2001 Vision for New York - www.vfny.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root application directory.
 *
 */
package org.vfny.geoserver.db.jdbc;

import org.apache.log4j.Category;
import java.io.*;
import java.util.*;
import java.sql.*;
import org.vfny.geoserver.xml.utilities.*;
import org.vfny.geoserver.metadata.*;
import org.vfny.geoserver.servlets.utilities.*;
import org.vfny.geoserver.types.*;

import org.vfny.geoserver.xml.requests.*;
import org.vfny.geoserver.db.jdbc.*;
import org.vfny.geoserver.db.jdbc.*;

import org.vfny.geoserver.types.*;


/**
 * Implements the WFS GetFeature interface, which responds to requests for GML.
 *
 * This servlet accepts a getFeatures request and returns GML2.0 structured
 * XML docs.
 *
 *@author Vision for New York
 *@author Rob Hranac
 *@version 0.9 alpha, 11/01/01
 *
 */
public class GetFeatureGML {

		private String featureType;
		private String srs = "911";

		private String finalResult = new String();
		private XmlLineString geometryType;

		private static final String GID_NAME = "gid";
		private static final String FID_NAME = "objectid";
		private static final String DEFAULT_BBOX = " WHERE the_geom && GeometryFromText('BOX3D(979757 197685,981595 199542)'::box3d,-1);";

		private static final String XML1 = "\n <featureMember fid=\"";
		private static final String XML2 = "\">";
		private static final String XML3 = "\n  <";
		private static final String XML4 = ">";
		private static final String XML5 = "\n </featureMember>";

		private Category _log = Category.getInstance(GetFeatureGML.class.getName());

	 /**
		* Empty constructor.
		*/ 
		public GetFeatureGML(String featureType, String srs) {
				this.srs = srs;
				this.featureType = featureType;
		}

	 /**
		* Parses the GetFeature reqeust and returns a contentHandler.
		*
		* @param request The XML WFS GetFeature request.
		*/ 
		public void initializeGeometry (String geometry) {

				// set geometry, and write it
				this.geometryType = new XmlLineString();

		}

	 /**
		* Parses the GetFeature reqeust and returns a contentHandler.
		*
		* @param request The XML WFS GetFeature request.
		*/ 
		public void addGeometry (String geometry, String tagName, String gid) throws SQLException {

				try {
						geometryType.setXmlLineString( geometry );
						finalResult = finalResult + geometryType.toXml(tagName, gid, srs);
				}
				catch( SQLException e ) {
						_log.info("some sort of XML writing problem");
				}
		}

		public void addAttribute (String name, String value) {

				finalResult = finalResult + "\n   <" + name + ">" + value + "</" + name + ">"; 
		}

		public void startFeature (String fid) {

				finalResult = finalResult + XML1 + fid + XML2 + XML3 + this.featureType + XML4;
		}

		public void endFeature () {

				finalResult = finalResult + XML3 + this.featureType + XML4 + XML5;
		}

		public String getGML () {

				return finalResult;
		}


}
