/* Copyright (c) 2002 Vision for New York - www.vfny.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root application directory.
 */
package org.vfny.geoserver.db.jdbc;

import java.io.*;
import java.util.*;
import java.sql.*;

import org.apache.log4j.Category;

import org.vfny.geoserver.db.*;
import org.vfny.geoserver.responses.WfsException;

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
public class PostgisGML extends GetFeatureGML {

		// geometric types
		private static final int LINESTRING = 1;
		private static final int POINT = 2;
 
		// log class
		private Category _log = Category.getInstance(PostgisGML.class.getName());

		// remember internal, initialized geometric type
		private int geometryType = 0;

		// OGC primitive data types
		private PostgisLineString lineStringGeometry = new PostgisLineString();
		private PostgisPoint pointGeometry = new PostgisPoint();


	 /**
		* Pass through constructor.
		*/ 
		PostgisGML(String featureType, String srs) {
				super( featureType, srs );
		}

	 /**
		* Recieves an OGC type and creates an appropriate XML extended OGC type for processing.
		*
		* @param geometry The OGC simple type.
		*/ 
		public void initializeGeometry (String geometry) {

				//_log.info("geometry starts with: " + geometry);

				// set geometry, and write it
				if( geometry.startsWith("LINESTRING") ) {

						this.geometryType = LINESTRING;

				}
				else if( geometry.startsWith("POINT") ) {

						this.geometryType = POINT;

				}
		}

	 /**
		* Adds the goemetry GML using an extended primitive type.
		*
		* @param geometry The raw Postgis-generated geometry string. minus SRID.
		* @param tagName The geometry name.
		* @param gid The geometry identification number.
		*/ 
		public void addGeometry (String geometry, String tagName, String gid) 
				throws WfsException {

				try {
						if( geometryType == LINESTRING ) {
								lineStringGeometry.setPostgisLineString( geometry );
								finalResult.append( lineStringGeometry.toGml(tagName, this.featureType, gid, srs) );
						}
						else if( geometryType == POINT ) {
								pointGeometry.setPostgisPoint( geometry );
								finalResult.append( pointGeometry.toGml(tagName, this.featureType, gid, srs) );
						}
				}
				catch( SQLException e ) {
						_log.info("Could not read geometry from Postgis.");
				}
		}

}
