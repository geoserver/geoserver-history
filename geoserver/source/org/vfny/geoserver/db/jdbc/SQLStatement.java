/* Copyright (c) 2001 Vision for New York - www.vfny.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root application directory.
 */

package org.vfny.geoserver.db.jdbc;

import java.util.*;

public class SQLStatement {

		private String sqlOperation = new String();
		private String featureTypeName = new String();
		private List properties = Collections.synchronizedList(new ArrayList());
		private String predicate = new String();

		public SQLStatement(String sqlOperation) {
				this.sqlOperation = sqlOperation;
		}

		public void empty() {
				this.featureTypeName = "";
				this.predicate = "";
				this.properties.clear();
		}

		public void setPredicate(String predicate) {
				this.predicate = predicate;
		}

		public String getPredicate() {
				return this.predicate;
		}

		public void setFeatureTypeName(String featureTypeName) {
				this.featureTypeName = featureTypeName;
		}

		public String getFeatureTypeName() {
				return this.featureTypeName;
		}

		public void addProperty(String property) {
				this.properties.add(property);
		}

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
