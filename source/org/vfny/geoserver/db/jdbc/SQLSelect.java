/* Copyright (c) 2001 Vision for New York - www.vfny.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root application directory.
 */
package org.vfny.geoserver.db.jdbc;

import java.util.*;


/**
 * Contains an abstraction of an ANSII SQL SLECT operation.
 *
 * @author Rob Hranac, Vision for New York
 * @version $0.9 beta, 11/01/01$
 */
public class SQLSelect {


		/** Stores the SQL operation type (i.e. 'SELECT', 'DELETE', etc.) */
		private String sqlOperation = new String();

		/** Table name (corresponds to feature types) */
		private String featureTypeName = new String();

		/** Column names (corresponds to attributes) */
		private List properties = Collections.synchronizedList(new ArrayList());

		/** Stores the SQL predicate via a Filter */
		private String predicate = new String();


	 /**
		* Constructor with type argument.
		*
		* @param sqlOperation Stores the SQL operation type (i.e. 'SELECT', 'DELETE', etc.).
		*/ 
		public SQLSelect(String sqlOperation) {
				this.sqlOperation = sqlOperation;
		}


	 /**
		* Resets all internal variables.
		*
		*/ 
		public void empty() {
				this.featureTypeName = "";
				this.predicate = "";
				this.properties.clear();
		}


	 /**
		* Sets the SQL predicate (i.e. WHERE clause).
		*
		* @param predicate Stores the SQL selection information.
		*/ 
		public void setPredicate(String predicate) {
				this.predicate = predicate;
		}


		/** Gets the SQL predicate (i.e. WHERE clause). */ 
		public String getPredicate() { return this.predicate; }


	 /**
		* Sets the SQL table name.
		*
		* @param featureTypeName Table name.
		*/ 
		public void setFeatureTypeName(String featureTypeName) {
				this.featureTypeName = featureTypeName;
		}


		/** Gets the SQL table name. */ 
		public String getFeatureTypeName() { return this.featureTypeName; }


	 /**
		* Adds a SQL column name.
		*
		* @param property Column name.
		*/ 
		public void addProperty(String property) {
				this.properties.add(property);
		}


	 /**
		* Retrieves a SQL representation of the entire object.
		*
		*/ 
		public String getSQL() {
				String SQL = new String();

				SQL = "SELECT ";
				if( properties.isEmpty() )
						SQL = SQL + "* ";
				else {
						for( int i=0; i < this.properties.size() - 1; i++ )
								SQL = SQL + this.properties.get(i) + ", ";
						SQL = SQL + this.properties.get(this.properties.size() - 1) + " ";
				}

				SQL = SQL + "FROM " + this.featureTypeName;

				if( !this.predicate.equals("") )
						SQL = SQL + " WHERE " + this.predicate;

				SQL = SQL + ";";

				return SQL;
		}
		
}
