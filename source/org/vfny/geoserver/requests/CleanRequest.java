/*
 * Copyright (c) 2001 Vision for New York - www.vfny.org.  All rights reserved.
 */

package org.vfny.geoserver.requests;

import java.io.*;
import java.util.*;

/**
 * Cleans an request string of HTTP-imposed special character encodings .
 *
 * @author Vision for New York
 * @author Rob Hranac 
 * @version 0.9 alpha, 11/01/01
 *
 */
public class CleanRequest {

		private static Hashtable htmlEncodingTranslator = new Hashtable();

		public CleanRequest() {
				htmlEncodingTranslator.put("%3C","<");
				htmlEncodingTranslator.put("%3E",">");
				htmlEncodingTranslator.put("%22","'");
				htmlEncodingTranslator.put("%20"," ");
				htmlEncodingTranslator.put("%27","'");
		}

		public static String clean(String dirtyRequest) {
				String cleanedRequest = new String();
				int i = 0;

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
				if ( htmlEncodingTranslator.containsKey( dirtyRequest.substring( dirtyRequest.length() - 3, dirtyRequest.length() ) ) ) {
						cleanedRequest = cleanedRequest + ((String) htmlEncodingTranslator.get( dirtyRequest.substring( dirtyRequest.length() - 3, dirtyRequest.length() ) ) );
				}
				else {
						cleanedRequest = cleanedRequest + dirtyRequest.substring( dirtyRequest.length() - 3, dirtyRequest.length() );
				}
				
				return cleanedRequest;
		}

}
