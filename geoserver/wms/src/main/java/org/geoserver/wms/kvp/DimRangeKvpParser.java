/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.wms.kvp;

import java.text.ParseException;
import org.geoserver.ows.KvpParser;
import org.geotools.util.NumberRange;
import org.vfny.geoserver.wms.WmsException;

/**
 * Parses the "dim_range" parameter as a list of number values separated by a comma.
 *
 * @author Cédric Briançon
 */
public class DimRangeKvpParser extends KvpParser {
    /**
     * 
     */
    public DimRangeKvpParser(String key) {
        super(key, NumberRange.class);
    }

    /**
     * Parses the value as two doubles separated by a comma.
     *
     * @return A {@linkplain org.geotools.util.NumberRange number range} value.
     * @throws ParseException if the value is not well-formed.
     */
    public Object parse(final String value) throws ParseException {
        if (!value.contains(",")) {
            throw new ParseException("The dim_range parameter does not contain any comma.", 0);
        }
        String[] range = value.split(",");
        if (range == null || range.length != 2) {
            throw new ParseException("The dim_range parameter is not composed of two number values " +
                    "separated by a comma.", 0);
        }
        final double min = Double.parseDouble(range[0]);
        final double max = Double.parseDouble(range[1]);
        return new NumberRange(min, max);
    }
}
