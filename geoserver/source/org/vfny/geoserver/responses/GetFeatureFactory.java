/* Copyright (c) 2002 Vision for New York - www.vfny.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root application directory.
 */

package org.vfny.geoserver.responses;

import java.io.*;
import java.util.*;

import org.apache.log4j.Category;

import org.vfny.geoserver.db.GetFeatureTransaction;
import org.vfny.geoserver.db.GMLBuilder;
import org.vfny.geoserver.db.jdbc.PostgisGetFeature;


/**
 * Creates a GMLBuilder, specific to a given data store.
 *
 *@author Rob Hranac, Vision for New York
 *@version 0.9 beta, 11/01/01
 *
 */
public class GetFeatureFactory {


		/** Standard logging class */
		static Category _log = Category.getInstance(GetFeatureFactory.class.getName());

		/** Default maximum features allowed */
		int maxFeatures = 500;


	 /**
		* Constructor, which is required to take a request object.
		*
		*/ 
		public GetFeatureFactory(int maxFeatures) {

				this.maxFeatures = maxFeatures;
		}


	 /**
		* Returns a GMLBuilder, based on datastore.
		*
		*/ 
		public GetFeatureTransaction createDatastore (int datastoreType) 
				throws WfsException {

				return new PostgisGetFeature(new GMLBuilder(true), this.maxFeatures);

		}

}
