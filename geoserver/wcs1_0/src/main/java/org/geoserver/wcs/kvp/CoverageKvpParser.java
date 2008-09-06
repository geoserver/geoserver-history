/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.wcs.kvp;

import static org.vfny.geoserver.wcs.WcsException.WcsExceptionCode.InvalidParameterValue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.geoserver.ows.KvpParser;
import org.geoserver.ows.util.KvpUtils;
import org.vfny.geoserver.global.Data;
import org.vfny.geoserver.wcs.WcsException;
import org.vfny.geoserver.wcs.WcsException.WcsExceptionCode;

/**
 * <p>
 * The parser validates the coverage requested on a WCS 1.0.0 GetCoverage request.
 * </p>
 * 
 * @author Alessio Fabiani, GeoSolutions
 */
public class CoverageKvpParser extends KvpParser {

    private Data catalog;

    public CoverageKvpParser(Data catalog) {
        super("coverage", Collection.class);
        setService("wcs");
        this.catalog = catalog;
    }

    @Override
    public Object parse(String value) throws Exception {
        Collection coverages =  new ArrayList();
        final List<String> identifiers = KvpUtils.readFlat(value);
        if(identifiers == null || identifiers.size() == 0) {
            throw new WcsException("Required paramer, coverage, missing", WcsExceptionCode.MissingParameterValue, "coverage");
        }
        
        for (String coverage : identifiers) {
            String coverageName = coverage.indexOf("@") > 0 ? 
                    coverage.substring(0, coverage.indexOf("@")) : 
                        coverage;
            String fieldName = coverage.indexOf("@") > 0 ?
                    coverage.substring(coverage.indexOf("@")+1) : 
                    null;
            Integer type = catalog.getLayerType(coverageName);
            if (!Data.TYPE_RASTER.equals(type))
                throw new WcsException("Could not find coverage '" + coverage + "'", InvalidParameterValue, "coverage");
            coverages.add(coverage);
        }

        return coverages;
    }

}