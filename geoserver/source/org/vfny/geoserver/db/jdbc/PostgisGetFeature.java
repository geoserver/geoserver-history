/* Copyright (c) 2002 Vision for New York - www.vfny.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root application directory.
 */
package org.vfny.geoserver.db.jdbc;

import java.io.*;
import java.util.*;
import java.sql.*;

import org.apache.log4j.Category;

import org.vfny.geoserver.db.*;
import org.vfny.geoserver.config.*;
import org.vfny.geoserver.requests.Query;

/**
 * Connects to a Postgis database and returns properly formatted GML.
 *
 *
 *@author Vision for New York
 *@author Rob Hranac
 *@version 0.9 alpha, 11/01/01
 *
 */
public class PostgisGetFeature extends PostgisTransaction implements GetFeatureTransaction {

		// initialize to clean Postgis database requests of SRID part
		private StringTokenizer geometryCleaner;

		// initialize default geometry id and feature ids as well as temp default bounding box
		// ALL NEED TO BE GENERALIZED, JUST TEMPORARY
		private static final String GID_NAME = "gid";
		private static final String FID_NAME = "objectid";
		//private static final String DEFAULT_BBOX = " WHERE the_geom && GeometryFromText('BOX3D(979757,197685 981595,199542)'::box3d,-1);";

		// initializes log file
		private Category _log = Category.getInstance(PostgisGetFeature.class.getName());


	 /**
		* Initializes the database and request handler.
		*
		*/ 
		public PostgisGetFeature () {

				super();
		}
		

	 /**
		* Returns the full GML GetFeature response for each query.
		*
		* @param query The query from the request object.
		*/ 
		public String getFeature (Query genericQuery, int maxFeatures) {

				// create a JDBC version of the query
				SQLStatement query = new SQLStatement( genericQuery );

				// create the return response type
				PostgisGML response = new PostgisGML( query.getFeatureTypeName(), "32118");

				// initialize a few local tracking variables
				String geometryName = "none";
				String gid;
				String fid;

				// initialize some local convenience variables
				int columnCount;
				String currentColumnTypeName = new String();
				String currentColumnName = new String();
				String currentResult = new String();
				boolean firstPass = true;

				try {

						// return the SQL results, get their metadata, and remember the number of columns
						int resultCounter = 0;

						this.setDatabaseConnection( query.getDatastoreConfiguration() );

						ResultSet result = this.getResultSet( query.getSQL() );
						ResultSetMetaData resultMetaData = result.getMetaData();
						columnCount = resultMetaData.getColumnCount();


						// loop through entire result set
						while( result.next() && ( resultCounter < maxFeatures ) ) {

								//_log.info("got result: " + resultCounter + 1 );
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


								resultCounter++;
						}								
						
						// kill the database connection
						this.closeResultSet(result);
				}
				catch (SQLException e) {
						_log.info("Some sort of database connection error: " + e.getMessage());
				}
				catch (Exception e) {
						_log.info("Some unkown error: " + e.getMessage());
				}
				
				// return the final GML
				return response.getGML();
		}


	 /**
		* Throws away the SRID component of the Postgis datatype from the database.
		*
		* @param geometry Postgis representation of an OGC simple type as string.
		*/ 
		private String cleanGeometry (String geometry) {

				// toss the SRID part
				geometryCleaner = new StringTokenizer( geometry, ";" );
				geometryCleaner.nextToken();

				return geometryCleaner.nextToken();
		}

}
