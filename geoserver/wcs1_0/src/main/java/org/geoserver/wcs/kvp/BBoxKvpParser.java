/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.wcs.kvp;

import java.util.List;

import org.geoserver.ows.KvpParser;
import org.geoserver.ows.util.KvpUtils;
import org.geotools.geometry.GeneralEnvelope;
import org.vfny.geoserver.wcs.WcsException;
import org.vfny.geoserver.wcs.WcsException.WcsExceptionCode;

/**
 * WCS 1.0.0 BBoxKvpParser.
 * 
 * @author Alessio Fabiani, GeoSolutions
 * 
 */
public class BBoxKvpParser extends KvpParser {
    public BBoxKvpParser() {
        super("BBOX", GeneralEnvelope.class);
        setService("wcs");
    }

    public Object parse(String value) throws Exception {
        List unparsed = KvpUtils.readFlat(value, KvpUtils.INNER_DELIMETER);

        // check to make sure that the bounding box has 4 coordinates
        if (unparsed.size() < 4) {
            throw new WcsException("Requested bounding box contains wrong"
                    + "number of coordinates (should have at least 4): " + unparsed.size(),
                    WcsExceptionCode.InvalidParameterValue, "BBOX");
        }

        // if it does, store them in an array of doubles
        int size = unparsed.size();
        double[] lower = new double[(int) Math.floor(size / 2.0)];
        double[] upper = new double[lower.length];

        for (int i = 0; i < lower.length; i++) {
            try {
                lower[i] = Double.parseDouble((String) unparsed.get(i));
            } catch (NumberFormatException e) {
                throw new WcsException("Bounding box coordinate is not parsable:"
                        + unparsed.get(i * 2), WcsExceptionCode.InvalidParameterValue,
                        "BBOX");
            }

            try {
                upper[i] = Double.parseDouble((String) unparsed.get(lower.length + i));
            } catch (NumberFormatException e) {
                throw new WcsException("Bounding box coordinate is not parsable:"
                        + unparsed.get(i * 2 + 1), WcsExceptionCode.InvalidParameterValue,
                        "BBOX");
            }
        }

        // we do not check that lower <= higher because in the case of geographic
        // bbox we have to accept the case where the lower coordinate is higher
        // than the high one and handle it as antimeridian crossing, better do that once
        // in the code that handles GetCoverage (the same check must be performed for xml requests)

        GeneralEnvelope bbox = new GeneralEnvelope(lower, upper);
        return bbox;
    }
}
