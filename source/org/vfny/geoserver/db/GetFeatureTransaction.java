/* Copyright (c) 2002 Vision for New York - www.vfny.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root application directory.
 */
package org.vfny.geoserver.db;

import org.vfny.geoserver.requests.Query;
import org.vfny.geoserver.requests.BoundingBox;


/**
 * Defines a general interface for querying and returning features from any datastore.
 * This interface _must_ be implemented by every datastore.
 * 
 * @author Rob Hranac, Vision for New York
 * @version $0.9 beta, 03/22/02$
 */
public interface GetFeatureTransaction {


	 /**
		* Adds all datastore query data to the returned 
		*
		* @param query Generic getFeature query
		*/ 
		public void getFeature (Query query);


	 /**
		* Returns final GML as a string.
		*
		* @return GML output
		*/ 
		public String getFinalResponse ();
}
