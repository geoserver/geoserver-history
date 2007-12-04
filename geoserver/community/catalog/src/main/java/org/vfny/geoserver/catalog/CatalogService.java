/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.catalog;

import org.vfny.geoserver.catalog.requests.AddCoverageRequest;
import org.vfny.geoserver.catalog.requests.AddFeatureTypeRequest;
import org.vfny.geoserver.catalog.requests.DeleteFeatureTypeRequest;
import org.vfny.geoserver.catalog.requests.UpdateRequest;
import org.vfny.geoserver.catalog.responses.AddCoverageResponse;
import org.vfny.geoserver.catalog.responses.AddFeatureTypeResponse;
import org.vfny.geoserver.catalog.responses.DeleteFeatureTypeResponse;
import org.vfny.geoserver.catalog.responses.UpdateResponse;

/**
 * Web Map Service implementation.
 * <p>
 * Each of the methods on this class corresponds to an operation as defined by
 * the Web Coverage Specification. See
 * {@link http://www.opengeospatial.org/standards/wcs} for more details.
 * </p>
 * 
 * @author Justin Deoliveira, The Open Planning Project, jdeolive@openplans.org
 * 
 */
public interface CatalogService {
	/**
	 * AddCoverage operation.
	 */
	AddCoverageResponse addCoverage(AddCoverageRequest request);

	/**
	 * AddFeatureType operation.
	 */
	AddFeatureTypeResponse addFeatureType(AddFeatureTypeRequest request);

	/**
	 * DeleteFeatureType operation.
	 */
	DeleteFeatureTypeResponse deleteFeatureType(DeleteFeatureTypeRequest request);

	/**
	 * UpdateCatalog operation.
	 */
	UpdateResponse updateCatalog(UpdateRequest request);

}
