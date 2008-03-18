/* Copyright (c) 2008 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geotools.util;

import org.geotools.data.complex.ComplexDataStore;
import org.geotools.factory.Hints;
import org.geotools.measure.Measure;

/**
 * Factory for converter from {@link Number} to {@link Measure}.
 * Unit-of-measure is set to null.
 * 
 * <p>
 * 
 * This converter allows data with unknown units to be converted to a
 * {@link Measure}, with units later supplied in a {@link ComplexDataStore}
 * client property mapping.
 * 
 */
public class NumberToMeasureConverterFactory implements ConverterFactory {

    public Converter createConverter(Class source, Class target, Hints hints) {
        /*
         * Measure is final, and Converters.convert() checks for type equality,
         * so no need to worry about source being either a Measure (checked) or
         * a subclass (prohibited).
         */
        if (Number.class.isAssignableFrom(source)
                && Measure.class.equals(target)) {
            return new Converter() {
                public Object convert(Object source, Class target)
                        throws Exception {
                    if (source == null) {
                        return null;
                    } else {
                        return new Measure(((Number) source).doubleValue(),
                                null);
                    }
                }
            };
        }
        return null;
    }

}
