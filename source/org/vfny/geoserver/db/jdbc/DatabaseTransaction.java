/*
 * Copyright (c) 2001 Vision for New York - www.vfny.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root application directory.
 *
 */
package org.vfny.geoserver.db.jdbc;

import java.io.*;
import java.util.*;
import java.sql.*;
import org.vfny.geoserver.xml.utilities.*;
import org.vfny.geoserver.metadata.*;
import org.vfny.geoserver.servlets.utilities.*;

import org.vfny.geoserver.xml.requests.*;
import org.vfny.geoserver.db.jdbc.*;


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
public class DatabaseTransaction {

		private static DatabaseRequest requestHandler = new DatabaseRequest();
		private static String DATABASE_TYPE = new String(); 

	 /**
		* Empty constructor.
		*/ 
		public DatabaseTransaction (String driverPath, String driverClass, String databaseType) {
				requestHandler.initialize(driverPath, driverClass);
				DATABASE_TYPE = databaseType;
		}

	 /**
		* Parses the GetFeature reqeust and returns a contentHandler.
		* @param request The XML WFS GetFeature request.
		*/ 
		public String getFeature (GetFeatureHandlerImpl contentHandler) {

				String response = new String();
				String SQL = new String();
				String featureType = new String();

				try {						
						for (int j = 0; j < contentHandler.getQueries().size(); j++ ) {
								
								SQL = ((SQLStatement) contentHandler.getQueries().get(j)).getSQL();						
								featureType = ((SQLStatement) contentHandler.getQueries().get(j)).getFeatureTypeName();
								ResultSet result = requestHandler.getResultSet(SQL);
								ResultSetMetaData resultMetaData = result.getMetaData();
								
								response = response + "<" + featureType + " xmlns:gml='http://www.opengis.net/gml' xmlns:xs='http://www.w3.org/2001/XMLSchema-instance'>\n";
								response = response + formatXml("gml:description","myDescription");
								response = response + formatXml("gml:name","myName");
								
								// Generate GML for each column requested
								while( result.next() ) {
										for( int i = 1 ; i <= resultMetaData.getColumnCount() ; i++ ) {
												response = response + checkXml( resultMetaData.getColumnName(i) , result.getString(i).trim(), resultMetaData.getColumnTypeName(i) );
										}
								}
								requestHandler.closeResultSet(result);

								// Close data collection, tag to table
								response = response + "</" + featureType + ">";
						}
				}
				catch (Exception Exception) {
				}

				return response;
		}

	 /**
		* Parses the GetFeature reqeust and returns a contentHandler.
		* @param request The XML WFS GetFeature request.
		*/ 
		public String insertTransaction (GetFeatureHandlerImpl contentHandler) {

				return "adsf";
		}


	 /**
		* Performs much of the geographic XML generation.
		*
		* @param tagName The tag name is the column name from the datastore.
		* @param tagContent The tag content is the tuple from the datastore.
		* @param contentType The content type is the geographic content type from the datastore.
		*
		*/ 
		private static String checkXml ( String tagName, String tagContent, String contentType ) throws Exception {
				StringTokenizer geographicTypeTokenizer = new StringTokenizer( tagContent, ";()" );
				String geographicType = new String();
				String geographicData = new String();
				String response = new String();
				

				// The algorithm below requires data from the database in order to function properly
				// Due to the way that PostgreSQL/PostGIS stores and reports geographic data types.
				// Geometry 
				// Right now spatial projection systems are faked.
				if ( contentType.equals( "geometry" ) ) {

						if( geographicTypeTokenizer.countTokens() > 1 ) {
								geographicType = geographicTypeTokenizer.nextToken();
								geographicType = geographicTypeTokenizer.nextToken();
								geographicData = geographicTypeTokenizer.nextToken();

								// Handles point type
								if( geographicType.equals( "POINT" )  ) {
										StringTokenizer geographicDataTokenizer = new StringTokenizer( geographicData , " " );
										response = "<" + tagName + ">\n\t<gml:coordinates decimal='.' cs=',' ts=' '>\n\t\t" + geographicDataTokenizer.nextToken();
										response = response + "," + geographicDataTokenizer.nextToken() + "\n\t</gml:coordinates>\n</" + tagName + ">\n";
										return response;
								}

								// Handles linestring type
								if( geographicType.equals( "LINESTRING" )  ) {
										StringTokenizer geographicDataTokenizer = new StringTokenizer( geographicData , ", " );
										response = "<" + tagName + ">\n\t<gml:coordinates decimal='.' cs=',' ts=' '>\n\t\t";
										while ( geographicDataTokenizer.hasMoreTokens() )
												response = response + geographicDataTokenizer.nextToken() + "," + geographicDataTokenizer.nextToken() + " "; 
										response = response + "\n\t</gml:coordinates>\n</" + tagName + ">\n";
										return response;
								}

								// Handles polygon type
								if( geographicType.equals( "POLYGON" )  ) {
										StringTokenizer geographicDataTokenizer = new StringTokenizer( geographicData , ", " );
										response = "<" + tagName + ">\n\t<gml:coordinates decimal='.' cs=',' ts=' '>\n\t\t\t";
										while ( geographicDataTokenizer.hasMoreTokens() )
												response = response + geographicDataTokenizer.nextToken() + "," + geographicDataTokenizer.nextToken() + " "; 
										response = response + "\n\t</gml:coordinates>\n</" + tagName + ">\n";
										return response;
								}

								// All 'MULTI' FEATURES ARE NOT CORRECTLY IMPLEMENTED
								// STUBS ARE LEFT HERE AS A REMINDER
								if( geographicType.equals( "MULTIPOINT" )  ) {
										StringTokenizer geographicDataTokenizer = new StringTokenizer( geographicData , ", " );
										response = "<" + tagName + ">\n\t<gml:Point gid='1' srsName='http://?/espg.xml#EPSG:4326'>\n\t\t<gml:coordinates decimal='.' cs=',' ts=' '>\n\t\t\t";
										while ( geographicDataTokenizer.hasMoreTokens() )
												response = response + geographicDataTokenizer.nextToken() + "," + geographicDataTokenizer.nextToken() + " "; 
										response = response + "\n\t\t</gml:coordinates>\n\t</gml:Point>\n</" + tagName + ">\n";
										return response;
								}
								if( geographicType.equals( "MULTILINESTRING" )  ) {
										StringTokenizer geographicDataTokenizer = new StringTokenizer( geographicData , ", " );
										response = "<" + tagName + ">\n\t<gml:Point gid='1' srsName='http://?/espg.xml#EPSG:4326'>\n\t\t<gml:coordinates decimal='.' cs=',' ts=' '>\n\t\t\t";
										while ( geographicDataTokenizer.hasMoreTokens() )
												response = response + geographicDataTokenizer.nextToken() + "," + geographicDataTokenizer.nextToken() + " "; 
										response = response + "\n\t\t</gml:coordinates>\n\t</gml:Point>\n</" + tagName + ">\n";
										return response;
								}
								if( geographicType.equals( "MULTIPLOYGON" )  ) {
										StringTokenizer geographicDataTokenizer = new StringTokenizer( geographicData , ", " );
										response = "<" + tagName + ">\n\t<gml:Point gid='1' srsName='http://?/espg.xml#EPSG:4326'>\n\t\t<gml:coordinates decimal='.' cs=',' ts=' '>\n\t\t\t";
										while ( geographicDataTokenizer.hasMoreTokens() )
												response = response + geographicDataTokenizer.nextToken() + "," + geographicDataTokenizer.nextToken() + " "; 
										response = response + "\n\t\t</gml:coordinates>\n\t</gml:Point>\n</" + tagName + ">\n";
										return response;
								}
								if( geographicType.equals( "GEOMETRYCOLLECTION" )  ) {
										StringTokenizer geographicDataTokenizer = new StringTokenizer( geographicData , ", " );
										response = "<" + tagName + ">\n\t<gml:Point gid='1' srsName='http://?/espg.xml#EPSG:4326'>\n\t\t<gml:coordinates decimal='.' cs=',' ts=' '>\n\t\t\t";
										while ( geographicDataTokenizer.hasMoreTokens() )
												response = response + geographicDataTokenizer.nextToken() + "," + geographicDataTokenizer.nextToken() + " "; 
										response = response + "\n\t\t</gml:coordinates>\n\t</gml:Point>\n</" + tagName + ">\n";
										return response;
								}
								return "fail";
						}
						return "fail";
				}
				else
						return formatXml( tagName, tagContent );
		}


	 /**
		* Simple local method for pulling XML format data.
		*
		* @param tagName The generic XML text-based data.
		* @param tagContent The generic XML tag content.
		*
		*/ 
		private static String formatXml ( String tagName, String tagContent ) throws Exception {
				String result = new String();

				result = "<" + tagName + ">" + tagContent + "</" + tagName + ">\n"; 

				return result;
		}
}
