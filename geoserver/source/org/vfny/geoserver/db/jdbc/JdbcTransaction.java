/* Copyright (c) 2001 Vision for New York - www.vfny.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root application directory.
 */
package org.vfny.geoserver.db.jdbc;

import java.io.*;
import java.util.*;
import java.sql.*;

import org.apache.log4j.Category;

import org.vfny.geoserver.responses.*;
import org.vfny.geoserver.config.*;
import org.vfny.geoserver.requests.*;


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
public class JdbcTransaction {


		// initializes log file
		private Category _log = Category.getInstance(JdbcTransaction.class.getName());

		// defines all database types
		// TYPES ARE PROTECTED BECAUSE I WILL PROBABLY SPLIT OUT THE POSTGIS PACKAGE
		protected static final int POSTGIS_DATABASE_TYPE = 1;
		protected static final int ORACLE9i_DATABASE_TYPE = 2;
		
		// all useful database connection information is stored here
		private int databaseType = -1;

		private String driverClass = null;

		private String driverPath = null;

		private Connection dbConnection = null;



	 /**
		* Handles all Get requests.
		*
		* This method implements the main matching logic for the class.
		*
		* @param driverClass The driver class; should be passed from the database-specific subclass.
		* @param driverPath The driver path; should be passed from the database-specific subclass..
		* @param databaseType The database type; should be passed from the database-specific subclass.
		*/ 
		public JdbcTransaction( String driverClass, String driverPath, int databaseType ) {

				this.driverClass = driverClass;
				this.driverPath = driverPath;
				this.databaseType = databaseType;
		}


	 /**
		* Handles all Get requests.
		*
		* This method implements the main matching logic for the class.
		*
		* @param featureType A complete description of the feature type metadata.
		*
		*/ 
		protected void setDatabaseConnection( FeatureTypeBean featureType )
				throws WfsException {

				if( ( driverClass != null ) & ( driverClass != null ) & ( driverClass != null ) ) { 

						// makes a new feature type bean to deal with incoming
						String databaseConnectionPath = this.driverPath + "://" + featureType.getHost() + ":" + featureType.getPort() + "/" + featureType.getDatabaseName();

						// makes a new feature type bean to deal with incoming
						String user = featureType.getUser();

						// makes a new feature type bean to deal with incoming
						String password = featureType.getPassword();
						
						// Instantiate the driver classes
						try {
								Class.forName(this.driverClass);
								dbConnection = DriverManager.getConnection( databaseConnectionPath , user, password);
						}
						catch (SQLException e) {
								throw new WfsException();
						}
						catch (ClassNotFoundException e) {
								throw new WfsException();
						}

				}
				else {

				}

		}


	 /**
		* Handles all Get requests.
		*
		* This method implements the main matching logic for the class.
		*
		* @param SQL The servlet request object.
		*/ 
		protected ResultSet getResultSet(String SQL) throws Exception {

				ResultSet result = null;

				if( dbConnection != null ) {
						String getFeatureResponse = new String();
						
						Statement statement = dbConnection.createStatement();
						result = statement.executeQuery(SQL);
						
				}
				else {

				}

				return result;

		}


		protected void closeResultSet(ResultSet result) {

				try {
						result.close();			
						result.getStatement().close();			
						result.getStatement().getConnection().close();			
				}
				catch (SQLException e) {
						_log.info("Error closing result set.");
				} 
		}


		protected int getDatabaseType() {
				return this.databaseType;
		}



}
