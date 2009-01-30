/*
 * Copyright (c) 2001 - 2008 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */

package org.geoserver.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Utility methods for manipulating text.
 * 
 * @author Ben Caradoc-Davies, CSIRO Exploration and Mining
 */
public class TextUtil {

    /**
     * 
     * Reads an {@link InputStream} as a {@link String} converting line endings to "\n".
     * 
     * @param input
     *            the stream to read
     * @return a string where line endings have been converted to "\n"
     * @throws IOException
     */
    public static String readInputStreamAsString(InputStream input) throws IOException {
        BufferedReader reader = null;
        StringBuffer buffer = new StringBuffer();
        try {
            reader = new BufferedReader(new InputStreamReader(input));
            String line = null;
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
                buffer.append("\n");
            }
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
        return buffer.toString();
    }

}
