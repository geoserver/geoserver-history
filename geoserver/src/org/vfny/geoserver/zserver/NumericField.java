/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.zserver;

import java.math.BigDecimal;
import java.util.logging.Logger;


/**
 * Implements a way to turn numbers into comparable strings and vice versa, so
 * that they can be indexed and compared by lucene.
 *
 * @author James Ricci
 * @author Chris Holmes, TOPP This code was mostly taken from the lucene users
 *         email list: From: James Ricci Subject: RE: Numeric Support Date:
 *         Fri, 26 Jul 2002 18:53:49 -0700
 * @version $Id: NumericField.java,v 1.4 2004/01/31 00:27:24 jive Exp $
 */
public class NumericField {
    /** The char to normalize against. */
    static protected final char NORM = 'j';

    /** The number of significant digits to the left of the decimal. */
    static protected final int defaultLeft = 10;

    /** The number of digits to the right of the decimal. */
    static protected final int defaultRight = 10;

    /** Standard logging instance */
    private static final Logger LOGGER = Logger.getLogger(
            "org.vfny.geoserver.zserver");

    /**
     * Normalizes the digit into a character.  The signMultiplier tells which
     * way to normalize, up for positive, down for negative.
     *
     * @param digit The character of the digit to be fixed up.
     * @param signMultiplier should be 1 for positives, -1 for negatives.
     *
     * @return the char that is 'digit' chars away from the NORM, in the
     *         direction of the signMultiplier.
     */
    protected static char fixupChar(char digit, int signMultiplier) {
        int bias = (digit - '0') * signMultiplier;

        return (char) (NORM + bias);
    }

    /**
     * Convenience method for the numberToString that takes a double. Simply
     * does the parsing on the string, passes it to numberToString.
     *
     * @param numString the string representation of a number.
     *
     * @return the lucene comparable string.
     *
     * @throws NumberFormatException if the passed in string can't be parsed.
     */
    public static String numberToString(String numString)
        throws NumberFormatException {
        Double num;

        num = new Double(numString);

        return numberToString(num);
    }

    /**
     * Calls numberToString with default values for the left and right
     * significant digits.
     *
     * @param num the number to convert.
     *
     * @return the lucene comparable string.
     */
    public static String numberToString(Double num) {
        return numberToString(num, defaultLeft, defaultRight);
    }

    /**
     * Converts a number to a comparable string. Uses a given number of
     * significant digits to the left and right of the decimal.
     *
     * @param num the number to convert.
     * @param leftPlaces the number of digits to the left of the decimal.
     * @param rightPlaces the number of digits to the right of the decimal.
     *
     * @return the lucene comparable string.
     */
    private static String numberToString(Double num, int leftPlaces,
        int rightPlaces) {
        //this corrects for the fact that Double.toString() sometimes
        //uses 1.0E3 notation, which breaks this function.  
        BigDecimal big = new BigDecimal(num.doubleValue());
        big = big.setScale(rightPlaces, BigDecimal.ROUND_HALF_UP);

        String s = big.toString();
        int signMultiplier;

        if (s.charAt(0) == '-') {
            s = s.substring(1);
            signMultiplier = -1;
        } else {
            signMultiplier = 1;
        }

        int decimalPos = s.indexOf('.');

        if (decimalPos < 0) {
            decimalPos = s.indexOf(',');
        }

        String wholePart = (decimalPos >= 0) ? s.substring(0, decimalPos) : s;
        String fracPart = (decimalPos >= 0) ? s.substring(decimalPos + 1) : "";

        /*Start of
           int expPos = s.indexOf("E");
           if (expPos >= 1) {
               String expPart = s.substring(expPos + 1);
               int decOffset = Integer.parseInt(expPart);
               if (decOffset < 0 ) {
               for (int i = 0; i < -decOffset; i++) {
                   appen
               decimalPos += decOffset;
               fracPart = fracPart.substring(0, expPos);
               }*/
        StringBuffer normalizedBuffer = new StringBuffer();

        if (signMultiplier == 0) {
            signMultiplier = 1;
        }

        // Build whole portion
        int length = wholePart.length();

        for (int i = 0; i < leftPlaces; i++) {
            int offset = (i + length) - leftPlaces;
            char c = (offset < 0) ? '0' : wholePart.charAt(offset);
            c = fixupChar(c, signMultiplier);
            normalizedBuffer.append(c);
        }

        length = fracPart.length();

        for (int i = 0; i < rightPlaces; i++) {
            int offset = i;
            char c = (offset >= length) ? '0' : fracPart.charAt(offset);
            c = fixupChar(c, signMultiplier);
            normalizedBuffer.append(c);
        }

        return normalizedBuffer.toString();
    }

    /**
     * Calls stringToNumber with default values for the left and right
     * significant digits.
     *
     * @param s the lucene searchable string to be converted to a double.
     *
     * @return the Double represented by s.
     */
    public static Double stringToNumber(String s) {
        return stringToNumber(s, defaultLeft, defaultRight);
    }

    /**
     * Converts a comparable string to a Double. Uses a given number of
     * significant digits to the left and right of the decimal.
     *
     * @param s the string to convert.
     * @param leftPlaces the number of digits to the left of the decimal.
     * @param rightPlaces the number of digits to the right of the decimal.
     *
     * @return the Double represented by s.
     */
    private static Double stringToNumber(String s, int leftPlaces,
        int rightPlaces) {
        boolean isNegative = false;
        int totalLength = leftPlaces + rightPlaces;

        if (s.length() != totalLength) {
            LOGGER.severe("string passed in to stringToNumber is "
                + "not a properly formatted NumericField string"
                + " Be sure to call numberToString first");

            //TODO: Create new exception to throw, have callers of this
            //function catch it.
        }

        for (int i = 0; i < totalLength; i++) {
            char c = s.charAt(i);

            if (c != NORM) {
                isNegative = (c < NORM);

                break;
            }
        }

        StringBuffer buffer = new StringBuffer();

        for (int i = 0; i < totalLength; i++) {
            int value = s.charAt(i) - NORM;

            if (isNegative) {
                value = -value;
            }

            buffer.append((char) ('0' + value));
        }

        if (rightPlaces > 0) {
            if (leftPlaces == 0) {
                buffer.insert(0, "0.");
            } else {
                buffer.insert(leftPlaces, '.');
            }
        }

        if (isNegative) {
            buffer.insert(0, '-');
        }

        return new Double(buffer.toString());
    }
}
