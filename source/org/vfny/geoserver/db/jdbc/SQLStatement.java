/* Copyright (c) 2002 Vision for New York - www.vfny.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root application directory.
 */
package org.vfny.geoserver.db.jdbc;

import java.util.*;

import org.apache.log4j.Category;

import org.vfny.geoserver.requests.Query;


/**
 * Contains an abstraction of an ANSII SQL operation.
 *
 * @author Rob Hranac, Vision for New York
 * @version $0.9 beta, 11/01/01$
 */
public class SQLStatement extends Query {


		/** Standard logging instance for class */
		private static Category _log = Category.getInstance(SQLStatement.class.getName());

		/** Creates a standard SQL statement for this query */
		private static final String SQL_OPERATION = "SELECT ";


	 /**
		* Constructor with query argument.
		*
		* @param genericQuery Stores a generic Web Feature Server query.
		*/ 
		public SQLStatement(Query genericQuery) {
				super();

				// HORRIBLE HACK ALERT
				// CASTING PROBLEMS CAUSED THIS, BUT THERE MUST BE A BETTER WAY
				// CAN'T JUST USE A FACTORY BECAUSE YOU DON"T KNOW WHAT TYPE TO
				// GENERATE UNTIL AFTER YOU HAVE READ IN SOME INFO

				this.handle = genericQuery.getHandle();
				this.featureTypeName = genericQuery.getFeatureTypeName();
				this.version = genericQuery.getVersion();
				this.propertyNames = genericQuery.getPropertyNames();
				this.filter = genericQuery.getFilter();
				this.featureType = genericQuery.getDatastoreConfiguration();
				this.boundingBox = genericQuery.getBoundingBox();
		}


	 /**
		* Retrieves a SQL representation of the entire object.
		*
		*/ 
		public String getSQL() {

				String SQL = SQL_OPERATION;

				SQL = "SELECT ";

				if( propertyNames.isEmpty() )
						SQL = SQL + "* ";
				else {
						for( int i=0; i < this.propertyNames.size() - 1; i++ )
								SQL = SQL + this.propertyNames.get(i) + ", ";
						SQL = SQL + this.propertyNames.get(this.propertyNames.size() - 1) + " ";
				}

				SQL = SQL + "FROM " + this.featureTypeName;

				if( !this.getFilter().getSQL().equals("") ) { 
						SQL = SQL + " WHERE " + this.getFilter().getSQL() + featureType.getSrs() + ")";
				}

				SQL = SQL + ";";

				//_log.info("sql is: " + SQL);

				return SQL;
		}
		
}
