/* Copyright (c) 2002 Vision for New York - www.vfny.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root application directory.
 */

package org.vfny.geoserver.responses;

import java.io.*;
import java.util.*;

import org.apache.log4j.Category;

import org.vfny.geoserver.db.GetFeatureTransaction;
import org.vfny.geoserver.db.jdbc.PostgisGetFeature;

/**
 * Handles a Get Feature request and creates a Get Feature response GML string.
 *
 *@author Rob Hranac, Vision for New York
 *@version 0.9 alpha, 11/01/01
 *
 */
public class GetFeatureFactory {


		/** standard logging class */
		static Category _log = Category.getInstance(GetFeatureFactory.class.getName());

	 /**
		* Constructor, which is required to take a request object.
		*
		*/ 
		public GetFeatureFactory() {
		}


	 /**
		* Parses the GetFeature reqeust and returns a contentHandler..
		*
		*/ 
		public GetFeatureTransaction createDatastore (int datastoreType) 
				throws WfsException {

				return new PostgisGetFeature();

		}

}
