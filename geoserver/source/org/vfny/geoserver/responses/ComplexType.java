/*
 * Copyright (c) 2001 Vision for New York - www.vfny.org.  All rights reserved.
 * This servlet implements the OGC WFS getCapabilitiesResponse inteface.
 *
 */
package org.vfny.geoserver.responses;

import java.io.*;
import java.util.*;
import java.sql.*;


/**
 * Encapsulates and abstracts a 'table' object for GML writing.
 * 
 * Passed a database connection and table name within the database, this method
 * returns a GML description of all data within the table.
 * 
 * THIS CLASS MUST GET ENHANCED ERROR CHECKING (DOES TABLE EXIST?) AND 
 * ALSO BE MODIFIED TO ADHERE TO FILTER SPECS.
 * SHOULD ABSTRCT SOME GML GENERATION OUT OF CLASS.
 * SHOULD ABSTRACT GML FORMATING OUT OF CLASS.
 *
 *@author Vision for New York
 *@author Rob Hranac
 *@version 0.9 alpha, 11/01/01
 *
 */
public class ComplexType {

		// MUST BE MODIFIED TO GENERALIZE; SHOULD PASS TO CONSTRUCTOR
		private static String SQL = "SELECT * FROM ";

	 /**
		* Empty constructor.
		*/ 
		public ComplexType () {}

	 /**
		* Performs main parsing logic to extract data from table.
		*
		* @param db The database object, passed from requester.
		* @param tableName The table name to parse information from, .
		*/ 
		public String xmlPrint (Connection db, String tableName) throws Exception {

				PostgreSqlSchemaElement element = new PostgreSqlSchemaElement();
				String getDescribeFeatureResponse = new String();
				Statement statement = db.createStatement();
				ResultSet result = statement.executeQuery( SQL + tableName );
				ResultSetMetaData resultMetaData = result.getMetaData();

				// Print table header information
				getDescribeFeatureResponse = getDescribeFeatureResponse + tableHeaderPrint(tableName);

				// Loop through result set
				if ( result.next() ) {

						// Loop through column set
						for( int i = 1 ; i <= resultMetaData.getColumnCount() ; i++ ) {

								// Set XML tag name
								element.setName( tableName, resultMetaData.getColumnName(i) );

								// Set XML tag content
								element.setType( resultMetaData.getColumnTypeName(i), result.getString(i).trim(), resultMetaData.isNullable(i) );

								// Print XML tag and content
								getDescribeFeatureResponse = getDescribeFeatureResponse + element.xmlPrint();
						}
				}

				// Print table footer information
				getDescribeFeatureResponse = getDescribeFeatureResponse + tableFooterPrint();

				// Close appropriate objects and return result
				statement.close();
				result.close();
				return getDescribeFeatureResponse;
		}

	 /**
		* Simple internal function to correctly parse datastore content to XML.
		*
		* @param tagName The XML tag name.
		* @param tagContent The XML tag content.
		*/ 
		private static String xmlBracket ( String tagName, String tagContent ) throws Exception {
				String result = new String();

				result = ("<").concat( tagName.concat(">") );
				result = result + tagContent;
				result = result + ("</").concat( tagName.concat(">") );
				return result;
		}

	 /**
		* Simple internal function to to print table header information.
		*
		* MUST FIND SOME WAY TO ABSTRACT THIS INFORMATION.
		* 
		* @param tableName The table name.
		*/ 
		private static String tableHeaderPrint ( String tableName ) throws Exception {
				return "<xs:complexType name='" + tableName + "_Type'>\n\t<xs:complexContent>\n\t\t<xs:extension base='gml:AbstractFeatureType'>\n\t\t\t<xs:sequence minOccurs='0' maxOccurs='unbounded'>";
		}

	 /**
		* Simple internal function to to print table footer information.
		*
		* MUST FIND SOME WAY TO ABSTRACT THIS INFORMATION.
		* .
		*/ 
		private static String tableFooterPrint () throws Exception {
				return "\n\t\t\t</xs:sequence>\n\t\t</xs:extension>\n\t</xs:complexContent>\n</xs:complexType>";
		}

}
