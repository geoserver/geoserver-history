/* Copyright (c) 2001 Vision for New York - www.vfny.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root application directory.
 */
package org.vfny.geoserver.db.jdbc;

import java.io.*;
import java.util.*;
import java.sql.*;

import org.apache.log4j.Category;

import org.vfny.geoserver.requests.*;
import org.vfny.geoserver.responses.*;
import org.vfny.geoserver.config.*;


/**
 * Shell for JDBC transactions of all types.
 *
 * This provides a base class to the database transactional classes.  
 *
 *@author Rob Hranac, Vision for New York
 *@version $0.9 alpha, 11/01/01$
 *
 */
public class JdbcTransaction {


		/** Initializes standard logging file */
		private Category _log = Category.getInstance(JdbcTransaction.class.getName());

		// TYPES ARE PROTECTED BECAUSE I WILL PROBABLY SPLIT OUT THE POSTGIS PACKAGE
		/** Sets the PostGIS database type */
		protected static final int POSTGIS_DATABASE_TYPE = 1;

		/** Sets the Oracle database type */
		protected static final int ORACLE9i_DATABASE_TYPE = 2;
		
		/** Stores the database type */
		private int databaseType = -1;

		/** Stores a string for the jdbc database driver class */
		private String driverClass = null;

		/** Stores the jdbc database driver path*/
		private String driverPath = null;

		/** Encapsulates the connection database for child classes */
		private Connection dbConnection = null;


	 /**
		* Constructor with all internal database driver classes, driver paths, and database types.
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
		* Creates a database connection method to initialize a given database for feature extraction.
		*
		* @param featureType A complete description of the feature type metadata.
		*/ 
		protected void setDatabaseConnection( FeatureTypeBean featureType )
				throws WfsException {
				
				// Creates a database connection
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
						throw new WfsException();
				}

		}


	 /**
		* Recieves SQL and returns a result set.
		*
		* @param SQL The raw SQL request object thing.
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


	 /**
		* Closes the result set.  Child class must remember to call
		*
		* @param result The servlet request object.
		*/ 
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


	 /**
		* Returns database type upon request.
		*
		*/ 
		protected int getDatabaseType() {
				return this.databaseType;
		}



}
