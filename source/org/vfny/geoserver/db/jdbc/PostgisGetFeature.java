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
import org.vfny.geoserver.xml.requests.*;

import org.vfny.geoserver.metadata.*;
import org.vfny.geoserver.servlets.utilities.*;
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
public class PostgisGetFeature {

		private DatabaseRequest requestHandler = new DatabaseRequest();
		private String databaseType = new String(); 

		private StringTokenizer geometryCleaner;

		private static final String GID_NAME = "gid";
		private static final String FID_NAME = "objectid";
		private static final String DEFAULT_BBOX = " WHERE the_geom && GeometryFromText('BOX3D(979757 197685,981595 199542)'::box3d,-1);";

		private Category _log = Category.getInstance(PostgisGetFeature.class.getName());

	 /**
		* Initializes the database and request handler.
		*
		* @param driverPath Path to the Postgis JDBC driver.
		* @param driverClass Unique classname of the Postgis JDBC driver.
		* @param databaseType Database type, according to passing class.
		*/ 
		public PostgisGetFeature (String driverPath, String driverClass, String databaseType) {
				requestHandler.initialize(driverPath, driverClass);
				this.databaseType = databaseType;
		}

	 /**
		* Returns the full GML for each query.
		*
		* @param query The query request from the .
		*/ 
		public String getFeature (SQLStatement query) {

				String SQL = setSQL( query );				
				String featureType = query.getFeatureTypeName();

				GetFeatureGML response = new GetFeatureGML(featureType, "911");

				String geometryName = "none";
				String gid;
				String fid;

				int columnCount;
				String currentColumnTypeName = new String();
				String currentColumnName = new String();
				String currentResult = new String();
				boolean firstPass = true;

				try {						
						
						// return the SQL results, get thier metadata, and remember the number of columns
						ResultSet result = requestHandler.getResultSet(SQL);
						ResultSetMetaData resultMetaData = result.getMetaData();
						columnCount = resultMetaData.getColumnCount();


						// loop through entire result set
						while( result.next() ) {

								// find gid and fid and start the feature type
								gid = result.getString( GID_NAME );
								fid = result.getString( FID_NAME );
								response.startFeature( fid );

								// loop through all columns
								// INEFFICIENT TO LOOP THROUGH THESE COLUMNS - ABSTRACT
								// ALSO, THIS REMAINS JUST WRONG BECUASE IT IGNORES USER SPECIFICATIONS
								// ALSO, QUESTION REMAINS WHETHER IT IS FASTER TO LOOP THROUGH COLUMNS HERE OR READ AND MATCH STORED SCHEMA
								for( int i = 1 ; i <= columnCount ; i++ ) {

										// set column name and result from database
										// IT IS STILL PRETTY INEFFICIENT TO READ THE METADATA EVERY TIME THROUGHT THIS - ABSTRACT IT
										currentColumnName = resultMetaData.getColumnName(i);
										currentResult = result.getString(i).trim();

										// if this is the first row in result set, set the geometric type
										// loop through columns to find geogaphic type and instantiate correct class to handle it
										// since we had to pull out a row to see what type is nested in the geometry field,
										//  also must write this row to XML output
										if( firstPass ) {

												// read in current column name
												// AGAIN, INEFFICIENT
												currentColumnTypeName = resultMetaData.getColumnTypeName(i);

												// if current column is a geometry, set the type
												if( currentColumnTypeName.equals("geometry") ) {

														// remember the geometry column name
														geometryName = currentColumnName;
														
														// set the geometric type
														response.initializeGeometry( cleanGeometry( currentResult ) );
														
														// note that we have discovered the geometry type
														firstPass = false;
												}

												// if current column is an attribute, write it to response object
												else if( !currentColumnName.equals(GID_NAME) && !currentColumnName.equals(FID_NAME) && !currentColumnName.equals(geometryName) ) {
														response.addAttribute( currentColumnName, currentResult );														
												}

										}
										
										// if we have passed the first row, check to see if it is an attribute and - if so - write it to response object
										else if( !currentColumnName.equals(GID_NAME) && !currentColumnName.equals(FID_NAME) && !currentColumnName.equals(geometryName) ) {
												response.addAttribute( currentColumnName, currentResult );
										}
								}

								// write out geometry to response object
								// and wrap up the given feature element
								response.addGeometry( cleanGeometry(currentResult), geometryName, gid );
								response.endFeature();
						}								
						
						// kill the database connection
						requestHandler.closeResultSet(result);
				}
				catch (Exception Exception) {
				}
				
				// return the final GML
				return response.getGML();
		}

	 /**
		* Parses the GetFeature reqeust and returns a contentHandler.
		*
		* @param request The XML WFS GetFeature request.
		*/ 
		private String setSQL (SQLStatement query) {

				// THIS IS A TEMPORARY FUNCTION UNTIL I THINK OF SOMETHING SMARTER
				// GETFEATURE IS SLOW ENOUGH THAT YOU DON'T WANT TO BE RETURNING
				// HUGE DATASETS AS DEFAULT
				if( !query.getPredicate().equals("") ) {
						return query.getSQL();						
				}
				else {
						return query.getSQL().substring(0, query.getSQL().length() - 1) + DEFAULT_BBOX;
				}
		}

	 /**
		* Parses the GetFeature reqeust and returns a contentHandler.
		*
		* @param request The XML WFS GetFeature request.
		*/ 
		private String cleanGeometry (String geometry) {
				geometryCleaner = new StringTokenizer( geometry, ";" );
				geometryCleaner.nextToken();
				return geometryCleaner.nextToken();
		}

}
