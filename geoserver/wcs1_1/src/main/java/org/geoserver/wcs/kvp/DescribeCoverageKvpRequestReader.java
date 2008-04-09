/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.wcs.kvp;

import java.util.List;
import java.util.Map;

import net.opengis.wcs11.DescribeCoverageType;
import net.opengis.wcs11.Wcs111Factory;

import org.geoserver.ows.kvp.EMFKvpRequestReader;
import org.geoserver.ows.util.KvpUtils;
import org.vfny.geoserver.global.Data;
import org.vfny.geoserver.wcs.WcsException;
import org.vfny.geoserver.wcs.WcsException.WcsExceptionCode;

/**
 * Describe coverage kvp reader TODO: check if this reader class is really
 * necessary
 * 
 * @author Andrea Aime
 * 
 */
public class DescribeCoverageKvpRequestReader extends EMFKvpRequestReader {
    private Data catalog;

    public DescribeCoverageKvpRequestReader(Data catalog) {
        super(DescribeCoverageType.class, Wcs111Factory.eINSTANCE);
        this.catalog = catalog;
    }

    public Object read(Object request, Map kvp, Map rawKvp) throws Exception {
        // let super do its thing
        request = super.read(request, kvp, rawKvp);

        DescribeCoverageType describeCoverage = (DescribeCoverageType) request;
        
        // we need at least one coverage
        final String identifiersValue = (String) rawKvp.get("identifiers");
        final List identifiers = KvpUtils.readFlat(identifiersValue);
        if(identifiers == null || identifiers.size() == 0) {
            throw new WcsException("Required paramer, identifiers, missing", WcsExceptionCode.MissingParameterValue, "identifiers");
        }
        
        // all right, set into the model (note there is a mismatch between the kvp name and the
        // xml name, that's why we have to parse the identifiers by hand)
        describeCoverage.getIdentifier().addAll(identifiers);
        
        
        // if not specified, throw a resounding exception (by spec)
        if(!describeCoverage.isSetVersion())
            throw new WcsException("Version has not been specified", WcsExceptionCode.MissingParameterValue, "version");

        return request;
    }
}
