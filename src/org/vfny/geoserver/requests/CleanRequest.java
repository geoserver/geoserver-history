/* Copyright (c) 2002 Vision for New York - www.vfny.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root application directory.
 */
package org.vfny.geoserver.requests;

import java.io.*;
import java.util.*;
import org.apache.log4j.Category;


/**
 * Cleans an request string of HTTP-imposed special character encodings.
 *
 * @author Rob Hranac, Vision for New York
 * @version 0.9 beta, 11/01/01
 *
 */
public class CleanRequest {

    /** Holds mappings between HTTP and ASCII encodings */
    private static Category _log = Category.getInstance(CleanRequest.class.getName());

    /** Holds mappings between HTTP and ASCII encodings */
    private static Hashtable htmlEncodingTranslator = new Hashtable();
    

    /**
     * Constructor; automatically creates an internal representation of the bounding box.
     *
     */ 
    public CleanRequest() {
        htmlEncodingTranslator.put("%3C","<");
        htmlEncodingTranslator.put("%3E",">");
        htmlEncodingTranslator.put("%22","'");
        htmlEncodingTranslator.put("%20"," ");
        htmlEncodingTranslator.put("%27","'");
    }
    
    
    /**
     * Cleans an HTTP string and returns pure ASCII as a string.
     *
     * @param dirtyRequest The HTTP-encoded string.
     */ 
    public static String clean(String dirtyRequest) {
        
        String cleanedRequest = new String();
        int i = 0;
        
        _log.info("dirty request: " + dirtyRequest);

        if(dirtyRequest != null) {
            // loop through the string, three characters at a time, looking for special HTTP encodings
            // if you find an HTTP encoding, replace it with ASCII
            while ( i < dirtyRequest.length() - 3 ) {
                if ( htmlEncodingTranslator.containsKey( dirtyRequest.substring(i,i+3) ) ) {
                    cleanedRequest = cleanedRequest + ((String) htmlEncodingTranslator.get( dirtyRequest.substring(i,i+3) ));
                    i = i+3;
                }
                else {
                    cleanedRequest = cleanedRequest + dirtyRequest.substring(i,i+1);
                    i=i+1;
                }
            }
            
            // check the final trio of characters - replace if HTTP encoding found
            if ( htmlEncodingTranslator.containsKey( dirtyRequest.substring( dirtyRequest.length() - 3, dirtyRequest.length()))) {
                cleanedRequest = cleanedRequest + ((String) htmlEncodingTranslator.get( dirtyRequest.substring( dirtyRequest.length() - 3, dirtyRequest.length())));
            }
            
            
            // do not replace last three characters
            else {
                cleanedRequest = cleanedRequest + dirtyRequest.substring( dirtyRequest.length() - 3, dirtyRequest.length() );
            }
            
            return cleanedRequest;
        }
        else {
            return "";
        }
    }
}
