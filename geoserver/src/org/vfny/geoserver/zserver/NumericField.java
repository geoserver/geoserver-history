package org.vfny.geoserver.zserver;

import java.lang.*;


public class NumericField
{
    static protected final char NORM = 'j';

    //TODO: test these for speed and reliability.
    static protected final int defaultLeft = 8;
    static protected final int defaultRight = 10;

    protected static char fixupChar(char digit, int signMultiplier)
    {
        int bias = (digit - '0') * signMultiplier;

        return (char)(NORM + bias);
    }

    public static String numberToString(String dubString) 
    throws NumberFormatException {
	Double num;
	//try {
	    num = new Double(dubString);
	    //} catch (NumberFormatException e) {
	    //return null;
	    //}
	return numberToString(num);
    }

    public static String numberToString(Double num) {
	return numberToString(num, defaultLeft, defaultRight);
    }

   

    public static String numberToString(Double num, int leftPlaces, int
rightPlaces)
    {
        String s = num.toString();

        int signMultiplier;

        if (s.charAt(0) == '-')
        {
            s = s.substring(1);
	    signMultiplier = -1;
        } else {
	    signMultiplier = 1;
	}

        int decimalPos = s.indexOf('.');

        if (decimalPos < 0)
        {
            decimalPos = s.indexOf(',');
        }

        String wholePart = (decimalPos >= 0) ? s.substring(0, decimalPos) :
s;
        String fracPart  = (decimalPos >= 0) ? s.substring(decimalPos + 1) :
"";

        StringBuffer normalizedBuffer = new StringBuffer();



        if (signMultiplier == 0)
        {
            signMultiplier = 1;
        }

        // Build whole portion
        int length = wholePart.length();

        for (int i = 0; i < leftPlaces; i++)
        {
            int offset = i + length - leftPlaces;
            char c = (offset < 0) ? '0' : wholePart.charAt(offset);

            c = fixupChar(c, signMultiplier);
            normalizedBuffer.append(c);
        }

        length = fracPart.length();

        for (int i = 0; i < rightPlaces; i++)
        {
            int offset =  i;
            char c = (offset >= length) ? '0' : fracPart.charAt(offset);

            c = fixupChar(c, signMultiplier);
            normalizedBuffer.append(c);
        }

        return normalizedBuffer.toString();
    }

    public static Double stringToNumber(String s) {
	return stringToNumber(s, defaultLeft, defaultRight);
    }

    public static Double stringToNumber(String s, int leftPlaces, int
rightPlaces)
    {
        boolean isNegative = false;
        int totalLength = leftPlaces + rightPlaces;

        for (int i = 0; i < totalLength; i++)
        {
            char c = s.charAt(i);

            if (c != NORM)
            {
                isNegative = (c < NORM);
                break;
            }
        }

        StringBuffer buffer = new StringBuffer();

        for (int i = 0; i < totalLength; i++)
        {
            int value = s.charAt(i) - NORM;

            if (isNegative)
            {
                value = -value;
            }

            buffer.append((char) ('0' + value));
        }

        if (rightPlaces > 0)
        {
            if (leftPlaces == 0)
            {
                buffer.insert(0, "0.");
            }
            else
            {
                buffer.insert(leftPlaces, '.');
            }
        }

        if (isNegative)
        {
            buffer.insert(0, '-');
        }

        return new Double(buffer.toString());
    }
}
