/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.wcs;

import org.geotools.xml.transform.TransformerBase;
import org.vfny.geoserver.wcs.responses.CoverageResponse;

import net.opengis.wcs.v1_1_1.CapabilitiesType;
import net.opengis.wcs.v1_1_1.CoverageDescriptionsType;
import net.opengis.wcs.v1_1_1.DescribeCoverageType;
import net.opengis.wcs.v1_1_1.GetCapabilitiesType;
import net.opengis.wcs.v1_1_1.GetCoverageType;



/**
 * Web Coverage Services interface.
 * <p>
 * Each of the methods on this class corresponds to an operation as defined
 * by the Web Coverage Specification. See {@link http://www.opengeospatial.org/standards/wcs}
 * for more details.
 * </p>
 * @author Andrea Aime, TOPP
 *
 */
public interface WebCoverageService111 {
    /**
    * GetCapabilities operation.
    */
    TransformerBase getCapabilities(GetCapabilitiesType request);

    /**
     * DescribeCoverage oeration.
     */
    CoverageDescriptionsType describeCoverage(DescribeCoverageType request);

    /**
     * GetCoverage operation.
     */
    CoverageResponse getCoverage(GetCoverageType request);
}
