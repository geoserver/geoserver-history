/* Copyright (c) 2002 Vision for New York - www.vfny.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root application directory.
 */

package org.vfny.geoserver.db.jdbc;

import java.util.*;

import org.apache.log4j.Category;

import org.vfny.geoserver.requests.Query;


public class SQLStatement extends Query {

		/** create standard logging instance for class */
		private static Category _log = Category.getInstance(SQLStatement.class.getName());

		private static final String SQL_OPERATION = "SELECT ";


		public SQLStatement(Query genericQuery) {
				super();

				// HORRIBLE HACK ALERT
				// CASTING PROBLEMS CAUSED THIS, BUT THERE MUST BE A BETTER WAY
				this.handle = genericQuery.getHandle();
				this.featureTypeName = genericQuery.getFeatureTypeName();
				this.version = genericQuery.getVersion();
				this.propertyNames = genericQuery.getPropertyNames();
				this.filter = genericQuery.getFilter();
				this.featureType = genericQuery.getDatastoreConfiguration();
		}


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

				if( !this.getFilter().getSQL().equals("") )
						SQL = SQL + " WHERE " + this.getFilter().getSQL();

				SQL = SQL + ";";

				_log.info("sql is: " + SQL);



				return SQL;
		}
		
}
