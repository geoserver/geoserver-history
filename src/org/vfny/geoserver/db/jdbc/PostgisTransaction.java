/* Copyright (c) 2002 Vision for New York - www.vfny.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root application directory.
 */
package org.vfny.geoserver.db.jdbc;

import java.util.*;
import java.sql.*;

import org.apache.log4j.Category;

import org.vfny.geoserver.config.*;

/**
 * Defines a shell class for onnecting to a Postgis database.
 *
 *@author Vision for New York
 *@author Rob Hranac
 *@version 0.9 alpha, 11/01/01
 *
 */
public class PostgisTransaction extends JdbcTransaction {


		/** Creates standard logging file */ 
		private Category _log = Category.getInstance(PostgisTransaction.class.getName());

		/** Creates PostGIS-specific JDBC driver class */ 
		private static final String POSTGIS_DRIVER_CLASS = "org.postgresql.Driver";

		/** Creates PostGIS-specific JDBC driver path */ 
		private static final String POSTGIS_DRIVER_PATH = "jdbc:postgresql";


	 /**
		* Initializes the database and request handler.
		*
		*/ 
		public PostgisTransaction () {

				// set superclass protected variables
				super( POSTGIS_DRIVER_CLASS, POSTGIS_DRIVER_PATH, POSTGIS_DATABASE_TYPE);

				
		}

}
