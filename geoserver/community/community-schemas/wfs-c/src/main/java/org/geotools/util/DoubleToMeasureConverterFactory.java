/* Copyright (c) 2001, 2003 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geotools.util;

import org.geotools.factory.Hints;
import org.geotools.measure.Measure;

/**
 * Factory for converter from Double to Measure. Unit-of-measure is set to null.
 * 
 * <p>
 * 
 * This converter allows data with unknown units to be converted to a measure,
 * with units later supplied in a ComplexDataStore client property mapping.
 * 
 */
public class DoubleToMeasureConverterFactory implements ConverterFactory {

    public Converter createConverter(Class source, Class target, Hints hints) {
        if (Double.class.equals(source) && Measure.class.equals(target)) {
            return new Converter() {
                public Object convert(Object source, Class target)
                        throws Exception {
                    if (source == null) {
                        return null;
                    } else {
                        return new Measure(((Double) source).doubleValue(), null);
                    }
                }
            };
        }
        return null;
    }
}
/* Copyright (c) 2001, 2003 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geotools.util;

import org.geotools.factory.Hints;
import org.geotools.measure.Measure;

/**
 * Factory for converter from Double to Measure. Unit-of-measure is set to null.
 * 
 * <p>
 * 
 * This converter allows data with unknown units to be converted to a measure,
 * with units later supplied in a ComplexDataStore client property mapping.
 * 
 */
public class DoubleToMeasureConverterFactory implements ConverterFactory {

    public Converter createConverter(Class source, Class target, Hints hints) {
        if (Double.class.equals(source) && Measure.class.equals(target)) {
            return new Converter() {
                public Object convert(Object source, Class target)
                        throws Exception {
                    if (source == null) {
                        return null;
                    } else {
                        return new Measure(((Double) source).doubleValue(), null);
                    }
                }
            };
        }
        return null;
    }
}
