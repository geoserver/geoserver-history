/* Copyright (c) 2001 Vision for New York - www.vfny.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root application directory.
 */
package org.vfny.geoserver.responses;

import org.apache.log4j.Category;
import java.io.*;
import java.util.*;
import java.sql.*;
import javax.xml.bind.*;
import javax.xml.marshal.*;
import org.vfny.geoserver.config.*;
import org.vfny.geoserver.requests.*;
import org.vfny.geoserver.responses.*;

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
public class DescribeFeatureTypeResponse {

		// create standard logging instance for class
		private static Category _log = Category.getInstance( DescribeFeatureTypeResponse.class.getName() );

		// Main XML class for interpretation and response.
		private String xmlResponse = new String();

		// Instantiate configuration utility class.
		private static ConfigurationBean configuration = new ConfigurationBean();

		// Initialize some generic GML information
		// ABSTRACT OUTSIDE CLASS, IF POSSIBLE
		private static final String HEADER = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<xs:schema targetNamespace=\"http://www.vfny.org/vfny\" xmlns:xs=\"http://www.w3.org/2001/XMLSchema\" xmlns:vfny=\"http://www.vfny.org/vfny\" xmlns:gml=\"http://www.opengis.net/gml\" elementFormDefault=\"qualified\" attributeFormDefault=\"unqualified\" version=\"1.0\">\n  <xs:import namespace=\"http://www.opengis.net/gml\" schemaLocation=\"http://freefs.vfny.org:81/geoserver/data/capabilities/feature.xsd\"/>\n\n";
		private static final String FOOTER = "</xs:schema>";

	 /**
		* Passes the Post method to the Get method, with no modifications.
		*
		* @param request The DescribeFeatureType reqeuset object.
		*/ 		
		public DescribeFeatureTypeResponse(DescribeFeatureTypeRequest wfsRequest) {

				// generates response, using general function 
				xmlResponse  = generateTypes( wfsRequest );
		}

	 /**
		* Passes the Post method to the Get method, with no modifications.
		*
		*/ 		
		public String getXmlResponse() {

				//_log.info("output: " + xmlResponse);
				return xmlResponse;
		}

	 /**
		* Internal method to generate the XML response object, using feature types.
		*
		* @param wfsRequest The servlet request object.
		*/ 
		private String generateTypes(DescribeFeatureTypeRequest wfsRequest) {

				Vector requestedTables = wfsRequest.getFeatureTypes();
				//_log.info("these are the reqeusted feature types: " + requestedTables.toString() ); 
				//_log.info("is this null: " + (requestedTables.isEmpty()) ); 

				// Initialize database connection information
				String getDescribeFeatureResponse = new String();

				// Initialize return information and intermediate return objects
				String tempResponse = new String();
				ComplexType table = new ComplexType();

				// Initialize generic header information
				tempResponse = HEADER;
				
				// call appropriate subfunction
				if( requestedTables.isEmpty() )
						tempResponse = tempResponse + generateAllTypes( configuration.getFeatureTypeDirectory() );
				else
						tempResponse = tempResponse + generateSpecifiedTypes( requestedTables );
												
				// Initialize generic footer information
				tempResponse = tempResponse + FOOTER;

				//_log.info("output: " + tempResponse);
				return tempResponse;
		}

	 /**
		* Internal method to print just the requested types.
		*
		* @param table The table name.
		*/ 
		private String generateSpecifiedTypes(Vector requestedTables) {

				String tempResponse = new String();				
				String currentFile = new String();

				// Loop through requested tables to add element types
				for (int i = 0; i < requestedTables.size(); i++ ) {

						// set the current file
						// print type data for the table object
						currentFile = configuration.getFeatureTypeDirectory() + requestedTables.get(i).toString() + "/" + configuration.getFeatureTypeSchemaName() + ".xml";
						tempResponse = tempResponse + writeFile( currentFile );
						//_log.info("current file: " + currentFile);						
				}

				// Loop through requested tables again to add elements
				// NOT VERY EFFICIENT - PERHAPS THE MYSQL ABSTRACTION CAN FIX THIS; STORE IN HASH?
				for (int i = 0; i < requestedTables.size(); i++ ) {

						// Print element representation of table
						tempResponse = tempResponse + printElement( requestedTables.get(i).toString() );
				}

				tempResponse = tempResponse + "\n\n";
				return tempResponse;

		}


	 /**
		* Add feature type info. 
		* 
		* @param targetDirectoryName The directory in which to search for files.
		*/
		private String generateAllTypes(String targetDirectoryName) {

				// holds final response variable
				String tempResponse = new String();

				// iterated convenience variables
				File currentDirectory = new File( targetDirectoryName );
				String currentFeatureType = new String();
				String currentFileName = new String();

				// keeps master list of files within the directory
				String[] files = currentDirectory.list();
				File[] file = currentDirectory.listFiles();
						

				// Loop through all files in the directory
				for (int i = 0; i < files.length; i++) {
						
						// assign temp variables; convenience/confusion lesseners only
						currentFeatureType = file[i].getName();
						currentFileName = targetDirectoryName + "/" + currentFeatureType + "/" + configuration.getFeatureTypeSchemaName() + ".xml";

						// actual work of writing out file is delegated to private function
						tempResponse = tempResponse + writeFile(currentFileName);			
						tempResponse = tempResponse + "\n";
				}
				

				// Loop through requested files again to add elements
				// NOT VERY EFFICIENT - PERHAPS THE MYSQL ABSTRACTION CAN FIX THIS; STORE IN HASH?
				for (int i = 0; i < files.length; i++) {

						// assign convenience variable
						currentFeatureType = file[i].getName();
								
						// Print element representation of table
						tempResponse = tempResponse + printElement( currentFeatureType );
						
				}
				
				tempResponse = tempResponse + "\n\n";
				return tempResponse;
		}
				

	 /**
		* Internal method to print XML element information for table.
		*
		* @param table The table name.
		*/ 
		private static String printElement(String table) {
				return "\n  <xs:element name='" + table + "' type='vfny:" + table + "_Type' substitutionGroup='gml:_Feature'/>";
		}


	 /**
		* Adds a feature type object to the final output buffer
		*
		* @param featureTypeName The name of the feature type.
		*/
		public String writeFile(String inputFileName) {

				String finalOutput = new String();

				try {
						File inputFile = new File(inputFileName);
						FileInputStream inputStream = new FileInputStream(inputFile);
						byte[] fileBuffer = new byte[ inputStream.available() ];
						int bytesRead;

						while( (bytesRead = inputStream.read(fileBuffer)) != -1 ) {
								String tempOutput = new String(fileBuffer);
								finalOutput = finalOutput + tempOutput;
						}
				}
				catch (Exception e) {}

				return finalOutput;

		}


}
