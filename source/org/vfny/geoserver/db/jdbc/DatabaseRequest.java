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
//import org.vfny.geoserver.db.jdbc.*;
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
public class DatabaseRequest {

		// Sets up global drivers/paths for PostgreSQL connection
		private static String DRIVER_CLASS = new String();
		private static String HOST = new String();
		private static String PORT = new String();		
		private static String DATABASE = new String();
		private static String USER = new String();
		private static String PASSWORD = new String();
		private static String DATABASE_CONNECTION_PATH = new String();			

		public DatabaseRequest() {
		}

		public void initialize(String driverPath,String driverClass) {
				// Establishes a configuration bean that finds and exposes freefs server properties
				ConfigurationBean configuration = new ConfigurationBean();

				// Sets up global drivers/paths for PostgreSQL connection
				HOST = "localhost";
				PORT = configuration.getDatabasePort();		
				DATABASE = configuration.getDatabaseName();
				USER = configuration.getDatabaseUser();
				PASSWORD = configuration.getDatabasePassword();
				DATABASE_CONNECTION_PATH = driverPath + "://" + HOST + ":" + PORT + "/" + DATABASE;			
				DRIVER_CLASS = driverClass;
		}


	 /**
		* Handles all Get requests.
		*
		* This method implements the main matching logic for the class.
		*
		* @param SQL The servlet request object.
		*/ 
		public ResultSet getResultSet(String SQL) throws Exception {

				String getFeatureResponse = new String();

				// Instantiate the driver classes
 				try {
						Class.forName(DRIVER_CLASS);
				}
				catch (Exception ClassNotFoundException) {
				}

				// This section of the code opens up the postgresql database connection path
				//try {
						Connection db = DriverManager.getConnection( DATABASE_CONNECTION_PATH , USER, PASSWORD);
						Statement statement = db.createStatement();
						ResultSet result = statement.executeQuery(SQL);
						return result;
						//}
						//catch (Exception Exception) {
						//response.getWriter().write( "something bad happened" );
						//}
				//catch (IOException e) {
						//System.out.println("Error reading uri: " + uri );
				//}
				//catch (SAXException e) {
				//System.out.println("Error in parsing: " + e.getMessage() );
				//}
		}

		public void closeResultSet(ResultSet result) {
				try {
						result.close();			
						result.getStatement().close();			
						result.getStatement().getConnection().close();			
				}
				catch (SQLException e) {
				} 
		}

}
