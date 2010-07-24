/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.wps.gs;

import org.geoserver.catalog.Catalog;
import org.geoserver.catalog.ProjectionPolicy;
import org.geoserver.wps.jts.DescribeParameter;
import org.geoserver.wps.jts.DescribeProcess;
import org.geoserver.wps.jts.DescribeResult;
import org.geotools.feature.FeatureCollection;
import org.geotools.process.ProcessException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

@DescribeProcess(title = "Catalog import", description = "Imports the provided feature collection into the catalog")
public class ImportProcess implements GeoServerProcess {

	private Catalog catalog;

	public ImportProcess(Catalog catalog) {
		this.catalog = catalog;
	}

	@DescribeResult(name = "layerName", description = "The qualified name of the created layer (workspace:name)")
	public String execute(
			@DescribeParameter(name = "features", description = "The features that will make up the new GeoServer layer") FeatureCollection features,
			@DescribeParameter(name = "workspace", min = 0, description = "The target workspace (the default one will be used if omitted)") String workspace,
			@DescribeParameter(name = "store", min = 0, description = "The target store (the workspace default one will be used if omitted)") String store,
			@DescribeParameter(name = "name", min = 0, description = "The name of the layer to be created (if missing the name of the features contained in the collection will be used") String name,
			@DescribeParameter(name = "srs", min = 0, description = "The target coordinate reference system (the feature collection one will be analyzed and used if possible)") CoordinateReferenceSystem declaredSRS,
			@DescribeParameter(name = "srsHandling", min = 0, description = "The desired SRS handling, FORCE_DECLARED will be used if not specified") ProjectionPolicy srsHandling,
			@DescribeParameter(name = "styleName", min = 0, description = "The name of the style to be used for the layer. If missing a default style will be chosen according to the type of geometries contained in the collection") String styleName)
			throws ProcessException {
		return null;
	}

}
