/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.wps.ppio;

import jaitools.JAITools;
import jaitools.numeric.Range;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Parses a {@link JAITools} range from a string defining it
 * @author Andrea Aime - GeoSolutions
 * @author Emanuele Tajarol - GeoSolutions
 */
public class JAIToolsRangePPIO extends LiteralPPIO {
    
    private final static String RE_OPEN  = "(\\(|\\[)"; // char for opening a range
    private final static String RE_CLOSE = "(\\)|\\])"; // char for closing range
    private final static String RE_NUM   = "(\\-?\\d+(?:\\.\\d*)?)?"; // a nullable general number

    private final static String RANGE_REGEX = RE_OPEN + RE_NUM + ";" + RE_NUM + RE_CLOSE ; // + "\\z";
    private final static Pattern RANGE_PATTERN = Pattern.compile(RANGE_REGEX);

    private final static String RANGELIST_REGEX = "(" + RE_OPEN + RE_NUM + ";" + RE_NUM + RE_CLOSE + ")+" ; // "\\z";
    private final static Pattern RANGELIST_PATTERN = Pattern.compile(RANGELIST_REGEX);

    /**
     * Parses a single range from a string
     *
     * @param sRange
     * @return
     */
    public static Range<Double> parseRange(String sRange) {
        Matcher m = RANGE_PATTERN.matcher(sRange);
        
        if( ! m.matches())
            throw new IllegalArgumentException("Bad range definition '"+sRange+"'");

        return parseRangeInternal(m, sRange);
    }

    /**
     * Return the parsed Range.
     *
     * @param sRange
     * @return
     */
    static Range<Double> parseRangeInternal(Matcher m, String sRange) {
        Double min = null;
        Double max = null;

        if(m.groupCount() != 4) {
            throw new IllegalStateException("Range returned wrong group count ("+sRange+") : " + m.groupCount());
        }

        if(m.group(2) != null) {
            min = new Double(m.group(2));
        }
        if(m.group(3) != null) {
            max = new Double(m.group(3));
        }

        boolean inclmin;
        if(m.group(1).equals("("))
            inclmin = false;
        else if(m.group(1).equals("["))
            inclmin = true;
        else
            throw new IllegalArgumentException("Bad min delimiter ("+sRange+")");

        boolean inclmax;
        if(m.group(4).equals(")"))
            inclmax = false;
        else if(m.group(4).equals("]"))
            inclmax = true;
        else
            throw new IllegalArgumentException("Bad max delimiter ("+sRange+")");

        if(min != null && max != null && min>max)
            throw new IllegalArgumentException("Bad min/max relation ("+sRange+")");

        return new Range<Double>(min, inclmin, max, inclmax);
    }

    /**
     * Parses a list of ranges from a string
     * @param sRangeList
     * @return
     */
    public static List<Range<Double>> parseRanges(String sRangeList) {
        // check that the whole input string is a list of ranges
        Matcher m = RANGELIST_PATTERN.matcher(sRangeList);
        if(! m.matches()) 
            throw new IllegalArgumentException("Bad range definition '"+sRangeList+"'");

        // fetch every single range
        m = RANGE_PATTERN.matcher(sRangeList);
        
        List<Range<Double>> ret = new ArrayList<Range<Double>>();
        while(m.find()) {
            Range<Double> range = parseRangeInternal(m, sRangeList);
            ret.add(range);
        }

        return ret;
    }

    public JAIToolsRangePPIO() {
        super(Range.class);
    }

    /**
     * Decodes the parameter (as a string) to its internal object implementation.
     */
    public Object decode(String value) throws Exception {
        return parseRange(value);
    }

    /**
     * Encodes the internal object representation of a parameter as a string.
     */
    public String encode( Object value ) throws Exception {
        throw new UnsupportedOperationException("JaiTools range not supported out of the box");
    }
}
