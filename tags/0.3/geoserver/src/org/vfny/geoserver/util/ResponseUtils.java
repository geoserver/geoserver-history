/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.util;

import java.io.IOException;
import java.io.Writer;
import java.util.logging.Logger;


public class ResponseUtils {
    /** Class logger */
    private static Logger LOGGER = Logger.getLogger(
            "org.vfny.geoserver.responses");



 //J-
  /**
   * Parses the passed string, and encodes the special characters (used in
   * xml for special purposes) with the appropriate codes. e.g. '<' is
   * changed to '&lt;'
   *
   * @param inData The string to encode into xml.
   *
   * @return the encoded string. Returns null, if null is passed as argument
   *
   * @task REVISIT: Once we write directly to out, as we should, this  method
   *       should be simpler, as we can just write strings with escapes
   *       directly to out, replacing as we iterate of chars to write them.
   */
  //J+
    public static String encodeXML(String inData) {
        //return null, if null is passed as argument
        if (inData == null) {
            return null;
        }

        //if no special characters, just return
        //(for optimization. Though may be an overhead, but for most of the
        //strings, this will save time)
        if ((inData.indexOf('&') == -1) && (inData.indexOf('<') == -1)
                && (inData.indexOf('>') == -1) && (inData.indexOf('\'') == -1)
                && (inData.indexOf('\"') == -1)) {
            return inData;
        }

        //get the length of input String
        int length = inData.length();

        //create a StringBuffer of double the size (size is just for guidance
        //so as to reduce increase-capacity operations. The actual size of
        //the resulting string may be even greater than we specified, but is
        //extremely rare)
        StringBuffer buffer = new StringBuffer(2 * length);

        char charToCompare;

        //iterate over the input String
        for (int i = 0; i < length; i++) {
            charToCompare = inData.charAt(i);

            //if the ith character is special character, replace by code
            if (charToCompare == '&') {
                buffer.append("&amp;");
            } else if (charToCompare == '<') {
                buffer.append("&lt;");
            } else if (charToCompare == '>') {
                buffer.append("&gt;");
            } else if (charToCompare == '\"') {
                buffer.append("&quot;");
            } else if (charToCompare == '\'') {
                buffer.append("&apos;");
            } else {
                buffer.append(charToCompare);
            }
        }

        //return the encoded string
        return buffer.toString();
    }

     /**
     * Writes <CODE>string</CODE> into writer, escaping &, ', ", <, and >
     * with the XML excape strings.
     */
    public static void writeEscapedString(Writer writer, String string)
        throws IOException {
        for(int i = 0; i < string.length(); i++) {
            char c = string.charAt(i);
            if(c == '<')
                writer.write("&lt;");
            else if(c == '>')
                writer.write("&gt;");
            else if(c == '&')
                writer.write("&amp;");
            else if(c == '\'')
                writer.write("&apos;");
            else if(c == '"')
                writer.write("&quot;");
            else
		writer.write(c);
        }
    }
}
