/* Copyright (c) 2002 Vision for New York - www.vfny.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root application directory.
 */
package org.vfny.geoserver.db;

import org.vfny.geoserver.requests.Query;

/**
 * Defines a general interface for querying and returning features from any datastore..
 * 
 * @author Vision for New York
 * @author Rob Hranac 
 * @version 0.9 beta, 11/01/01
 *
 */
public interface GetFeatureTransaction {

		public String getFeature (Query query, int maxFeatures);
}
