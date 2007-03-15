/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.wfsv;

import net.opengis.wfs.FeatureCollectionType;
import net.opengis.wfsv.GetDiffType;
import net.opengis.wfsv.GetLogType;

import org.geoserver.wfs.WebFeatureService;
import org.geotools.data.postgis.FeatureDiffReader;


/**
 * Versioned Web Feature Service implementation, extensions to both WFS 1.0 and 1.1.
 * <p>
 * Each of the methods on this class corresponds to an operation as defined
 * by the Web Feature Specification plus a few custom methods for handling feature versioning.
 * See {@link http://www.opengeospatial.org/standards/wfs} and
 * {@link http://docs.codehaus.org/display/GEOS/Versioning+WFS+-+Protocol+considerations} for more details.
 * </p>
 *
 * @author Andrea Aime, The Open Planning Project
 *
 */
public interface VersionedWebFeatureService extends WebFeatureService {
    
    /**
     * Executes WFSV GetLog request
     * @param request
     * @return
     */
    public FeatureCollectionType getLog(GetLogType request);
    
    /**
     * Executes the WFSV GetDiff request
     * @param request
     * @return
     */
    public FeatureDiffReader[] getDiff(GetDiffType request);
}
