/* Copyright (c) 2008 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geotools.util;

import java.math.BigDecimal;
import java.math.BigInteger;

import javax.units.Unit;

import junit.framework.TestCase;

import org.geotools.measure.Measure;

/**
 * Exercise {@link NumberToMeasureConverterFactory} by converting different
 * types.
 */
public class NumberToMeasureConverterFactoryTest extends TestCase {

    public static void testDoubleToMeasure() {
        double value = 7.3;
        Measure measure = (Measure) Converters.convert(new Double(value),
                Measure.class);
        assertEquals(value, measure.doubleValue(), 0.0);
        assertNull(measure.getUnit());
    }

    public static void testFloatToMeasure() {
        float value = 2.7f;
        Measure measure = (Measure) Converters.convert(new Float(value),
                Measure.class);
        assertEquals(value, measure.floatValue(), 0.0f);
        assertNull(measure.getUnit());
    }

    public static void testLongToMeasure() {
        /*
         * -9007199254740991L = -(2**53 - 1), the most negative integer exactly
         * representable in a double, the type used internally by Measure.
         */
        long value = -9007199254740991L;
        Measure measure = (Measure) Converters.convert(new Long(value),
                Measure.class);
        assertEquals(value, measure.longValue());
        assertNull(measure.getUnit());
    }

    public static void testIntegerToMeasure() {
        int value = 887457897;
        Measure measure = (Measure) Converters.convert(new Integer(value),
                Measure.class);
        assertEquals(value, measure.intValue());
        assertNull(measure.getUnit());
    }

    public static void testShortToMeasure() {
        short value = -30001;
        Measure measure = (Measure) Converters.convert(new Short(value),
                Measure.class);
        assertEquals(value, measure.shortValue());
        assertNull(measure.getUnit());
    }

    public static void testByteToMeasure() {
        byte value = -101;
        Measure measure = (Measure) Converters.convert(new Byte(value),
                Measure.class);
        assertEquals(value, measure.byteValue());
        assertNull(measure.getUnit());
    }

    public static void testBigIntegerToMeasure() {
        BigInteger value = new BigInteger("-1687841849891");
        Measure measure = (Measure) Converters.convert(value, Measure.class);
        assertEquals(value.doubleValue(), measure.doubleValue(), 0.0);
        assertNull(measure.getUnit());
    }

    public static void testBigDecimalToMeasure() {
        BigDecimal value = new BigDecimal("9815971.917834581");
        Measure measure = (Measure) Converters.convert(value, Measure.class);
        assertEquals(value.doubleValue(), measure.doubleValue(), 0.0);
        assertNull(measure.getUnit());
    }

    public static void testMeasureToMeasure() {
        Measure value = new Measure(4.6, Unit.searchSymbol("m"));
        Measure measure = (Measure) Converters.convert(value, Measure.class);
        assertEquals(value, measure);
        assertEquals(value.getUnit(), measure.getUnit());
        /* Expect Converters.convert() to do an early return with same instance. */
        assertSame(value, measure);
    }

}
