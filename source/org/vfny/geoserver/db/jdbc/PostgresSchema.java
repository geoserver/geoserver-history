/* Copyright (c) 2001 Vision for New York - www.vfny.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root application directory.
 */

package org.vfny.geoserver.db.jdbc;

import java.io.*;
import java.util.*;
import java.sql.*;
import javax.xml.bind.*;
import javax.xml.marshal.*;
import org.vfny.geoserver.servlets.utilities.*;
import org.vfny.geoserver.metadata.*;
import org.vfny.geoserver.xml.utilities.*;
import org.vfny.geoserver.xml.internal.featureType.*;

/**
 * Implements the WFS DescribeFeatureTypes inteface, which tells clients the schema for each feature type.
 *
 * This servlet returns descriptions of all feature types served by the FreeFS.
 * It does this by inspecting the table for the feature type every time it is
 * called.  This is not particularly efficient, but it does serve the purpose
 * of keeping in close sych with the database itself.  Future versions of this servlet
 * will likely store some of this data in the feature type directory.
 *
 * Note that this assumes that the possible schemas are only single tables,
 * with no foreign key relationships with other tables.
 *
 * IMPORTANT NOTE: This version assumes that all passwords, usernames, and database
 * for every table are the same.
 * 
 * @author Vision for New York
 * @author Rob Hranac 
 * @version 0.9 alpha, 11/01/01
 *
 */
public class PostgresSchema {

		// Main XML class for interpretation and response.
		private String xmlResponse = new String();

		// Instantiate configuration utility class.
		private static ConfigurationBean configuration = new ConfigurationBean();

		// Initialize all required database connection information
		private static String POSTGRESQL_DRIVER = "jdbc:postgresql";
		private static String HOST = "localhost";
		private static String PORT = configuration.getDatabasePort();		
		private static String DATABASE = configuration.getDatabaseName();
		private static String USER = configuration.getDatabaseUser();
		private static String PASSWORD = configuration.getDatabasePassword();

		// Initialize some generic GML information
		// ABSTRACT OUTSIDE CLASS, IF POSSIBLE
		private static String HEADER = "<?xml version='1.0' encoding='UTF-8'?>\n<xs:schema\t\txmlns:xs='http://www.w3.org/2001/XMLSchema'\n\t\t\txmlns:gml='http://www.opengis.net/gml'\n\t\t\telementFormDefault='qualified'\n\t\t\tattributeFormDefault='unqualified'\n\t\t\tversion='0.1'>\n<xs:import namespace='http://www.opengis.net/namespaces/gml/core/feature.xsd' schemalocation='feature.xsd'/>\n";
		private static String FOOTER = "\n</xs:schema>";
		private static Hashtable htmlEncodingTranslator = new Hashtable();

	 /**
		* Passes the Post method to the Get method, with no modifications.
		*
		* @param request The DescribeFeatureType reqeuset object.
		*/ 		
		public PostgresSchema(DescribeFeatureTypeRequest wfsRequest) {

				// generates response, using Postgres-specific function 
				xmlResponse  = generateTypes( wfsRequest );
		}

	 /**
		* Passes the Post method to the Get method, with no modifications.
		*
		*/ 		
		public String getXmlResponse() {

				return xmlResponse;
		}

	 /**
		* Internal method to generate the XML response object, using feature types.
		*
		* @param wfsRequest The servlet request object.
		*/ 
		private String generateTypes(DescribeFeatureTypeRequest wfsRequest) {

				List requestedTables = wfsRequest.getFeatureTypes();

				// SHOULD WORK TO GENERALIZE
				try {
						Class.forName("org.postgresql.Driver");
				}
				catch (Exception ClassNotFoundException) {}

				// Initialize database connection information
				String getDescribeFeatureResponse = new String();
				String databaseConnectionPath = POSTGRESQL_DRIVER + "://" + HOST + ":" + PORT + "/" + DATABASE;			

				// Initialize return information and intermediate return objects
				String tempResponse = new String();
				ComplexType table = new ComplexType();

				// Open connection to database
				try {
						Connection db = DriverManager.getConnection( databaseConnectionPath , USER, PASSWORD);

						// Initialize generic header information
						tempResponse = HEADER;

						// Loop through requested tables
						for (int i = 0; i < requestedTables.size(); i++ ) {
								
								// Print element representation of table
								tempResponse = tempResponse + printElement( requestedTables.get(i).toString() ) + "\n\n";

								// Print type data for the table object
								tempResponse = tempResponse + table.xmlPrint(db, requestedTables.get(i).toString() );
						}

						// Initialize generic footer information
						tempResponse = tempResponse + FOOTER;

						// Close objects and return response
						db.close();
				} catch (Exception Exception) {}

				return tempResponse;
		}

	 /**
		* Internal method to print XML element information for table.
		*
		* @param table The table name.
		*/ 
		private static String printElement(String table) throws Exception {
				return "\n<xs:element name='" + table + "' type='wfs:" + table + "_Type' substitutionGroup='gml:_Feature'/>";
		}

	 /**
		* Add feature type info. 
		* 
		* @param targetDirectoryName The directory in which to search for files.
		*/
		private static void addFeatureTypeInfo(String targetDirectoryName) throws Exception {
				File currentDirectory = new File(targetDirectoryName);

				// Recursively search through the appropriate directory
				if(currentDirectory.isDirectory()) {
						String targetFileName = new String();
						String[] files = currentDirectory.list();
						File[] file = currentDirectory.listFiles();
						
						// Loop through all files in the directory
						for (int i = 0; i < files.length; i++) {
								targetFileName = targetDirectoryName.concat(file[i].getName());
								File currentFile = new File(targetFileName);

								// Recurse, if a given object is a directory
								if(currentFile.isDirectory()) {
										addFeatureTypeInfo(targetFileName.concat("/"));
								}
								else {
										// Otherwise, append information from file
										if( targetFileName.substring( targetFileName.length() - 10, targetFileName.length() - 4 ).equals( configuration.getFeatureTypeSchemaName() ) ) {
												addFeatureType(targetFileName);
										}
								}
						}
				}
		}

	 /**
		* Adds a feature type object to the final output buffer
		*
		* @param featureTypeName The name of the feature type.
		*/
		private static void addFeatureType(String featureTypeName) throws Exception {

				// Unmarshall the configuration file for translation
				FeatureType responseFeatureType = new FeatureType();
				File featureTypeDocument = new File(featureTypeName);
				FileInputStream featureTypeFS = new FileInputStream(featureTypeDocument);
				try {
						responseFeatureType = responseFeatureType.unmarshal(featureTypeFS);
				} finally {
						featureTypeFS.close();
				}
			  //writeToBuffer(responseFeatureType);

				
		}


}
