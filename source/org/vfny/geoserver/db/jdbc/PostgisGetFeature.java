/* Copyright (c) 2002 Vision for New York - www.vfny.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root application directory.
 */
package org.vfny.geoserver.db.jdbc;

import java.io.*;
import java.util.*;
import java.sql.*;

import org.apache.log4j.Category;

import com.vividsolutions.jts.io.*;
import com.vividsolutions.jts.geom.*;

import org.vfny.geoserver.db.*;
import org.vfny.geoserver.config.*;
import org.vfny.geoserver.requests.Query;
import org.vfny.geoserver.requests.BoundingBox;
import org.vfny.geoserver.responses.WfsException;


/**
 * Connects to a Postgis database and returns properly formatted GML.
 *
 * <p>This standard class must exist for every supported datastore.</p>
 *
 *@author Rob Hranac, Vision for New York
 *@version $0.9 alpha, 11/01/01$
 */
public class PostgisGetFeature extends PostgisTransaction implements GetFeatureTransaction {


		/** Standard logging instance */
		private Category _log = Category.getInstance(PostgisGetFeature.class.getName());

		/** Initialize to clean Postgis database requests of SRID part */
		private StringTokenizer geometryCleaner;

		/** GID - since all layers may contain only one primary geometry (i.e. geometry or geom collection), this is the same as a row ID */
		// NEEDS TO BE GENERALIZED, JUST TEMPORARY
		private static final String GID_NAME = "objectid";

		/** FID - since all layers may contain only one primary geometry (i.e. geometry or geom collection), this is the same as a row ID */
		// NEEDS TO BE GENERALIZED, JUST TEMPORARY
		private static final String FID_NAME = "objectid";

		/** Factory for producing geometries (from JTS) */
		private GeometryFactory geometryFactory;

		/** Well Known Text reader (from JTS) */
		private WKTReader geometryReader;

		/** Creates a GML response with methods for adding features, attributes, and geometries */
		private GMLBuilder response;

		/** The maximum features allowed by the server for any given response */
		private int maxFeatures;


	 /**
		* Initializes the database and request handler.
		*
		* @param response The query from the request object.
		* @param maxFeatures The query from the request object.
		*/ 
		public PostgisGetFeature (GMLBuilder response, int maxFeatures) {

				super();

				// create the return response type
				this.response = response;
				this.maxFeatures = maxFeatures;

		}
		

	 /**
		* Returns the full GML GetFeature response for each query and bounding box
		*
		* <p>Note that bounding box parameters are included because get feature
		* requests may contain more than 1 generic query with a single unified 
		* bounding box for each request.</p>
		*
		* @param genericQuery The query from the request object.
		*/ 
		public void getFeature (Query genericQuery) {

				//_log.info("bbox: " + bbox.getCoordinates() );

				// create a JDBC version of the query
				// HACK ALERT; THIS SHOULD BE IMPLEMENTED WITH A FACTORY AND SUBCLASSING
				SQLStatement query = new SQLStatement( genericQuery );

				// initialize a few local tracking variables
				String geometryName = "none";
				String geometryResult = "";
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

						// Creates a well-known text reader and geometry factory for generation
						// CAN SPECIFY PRECISION AND SRID HERE, IF YOU WANT
						geometryFactory = new GeometryFactory();
						geometryReader = new WKTReader(geometryFactory);

						//_log.info("bbox: " + bbox.getCoordinates() );

						// initialize GML result set with appropriate feature type
						response.startFeatureType( query.getFeatureTypeName(), query.getMetadata().getSrs(), query.getBoundingBox() );

						// loop through entire result set or until maxFeatures are reached
						while( result.next() && ( resultCounter < maxFeatures ) ) {

								//_log.info("got result: " + resultCounter + 1 );

								// find gid and fid and start the feature type
								gid = result.getString( GID_NAME );
								fid = result.getString( FID_NAME );
								response.startFeature( fid );

								// loop through all columns
								// INEFFICIENT TO LOOP THROUGH THESE COLUMNS - MAKE ABSTRACT
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
														response.initializeGeometry( createGeometry(currentResult), query.getFeatureTypeName(), query.getMetadata().getSrs(), geometryName );

														// note that we have discovered the geometry type
														firstPass = false;

														// remember geometry result
														geometryResult = currentResult;
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
									 
										// remember geometry result
										else if( currentColumnName.equals(geometryName) ) {
												geometryResult = currentResult;
										}
								}

								// write out geometry to response object
								// and wrap up the given feature element
								response.addGeometry( createGeometry(geometryResult), gid );
								response.endFeature();

								// increment result count
								resultCounter++;
						}								
						
						// kill the database connection
						this.closeResultSet(result);
				}
				catch (SQLException e) {
						_log.info("Some sort of database connection error: " + e.getMessage());
				}
				catch (WfsException e) {
						_log.info("Some unkown error: " + e.getMessage());
				}
				catch (ParseException e) {
						_log.info("Failed to parse the geometry from PostGIS: " + e.getMessage());
				}
				catch (Exception e) {
						_log.info("Error from the result set: " + e.getMessage());
						_log.info( e.toString() );
						e.printStackTrace();
				}
				
				// end the GML feature type after end of loop is reached
				response.endFeatureType();

		}


	 /**
		* Returns the full GML GetFeature response for each query.
		*
		*/ 
		public String getFinalResponse() {

				// return the final GML
				return response.getGML();
		}


	 /**
		* Creates a new geometry object from the PostGIS database.
		*
		* @param geometryString The PostGIS WKT + SRID string representation of the geometry
		*/ 
		private Geometry createGeometry(String geometryString)
				throws ParseException{

				//_log.info("geom string: " + geometryString);

				// clean geometry of SRID
				String tempGeomStr;
				StringTokenizer cleanGeometry = new StringTokenizer( geometryString, ";");
				cleanGeometry.nextToken();
				tempGeomStr = cleanGeometry.nextToken();

				// create geometry
				Geometry tempGeom = geometryReader.read( tempGeomStr );

				// return the geometry
				return tempGeom;
		}


}
