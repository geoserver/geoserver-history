/*
 * Copyright (c) 2001 Vision for New York - www.vfny.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root application directory.
 */
package org.vfny.geoserver.metadata;

import java.io.*;
import java.util.*;
import javax.xml.bind.*;
import javax.xml.marshal.*;
import org.vfny.geoserver.xml.internal.configuration.*;

/**
 * Reads all feature type data to abstract away from servlets.
 * 
 * @author Vision for New York
 * @author Rob Hranac 
 * @version 0.9 alpha, 11/01/01
 *
 */
public class ReadFeatureTypes {

		private String returnHtml = new String();

	 /**
		* Empty constructor.
		*
		*/
		public ReadFeatureTypes () {}

	 /**
		* This function lists all files in HTML for the meta-data pages.
		* 
		* The logic here is recursive: pass the function the main featureType directory and it
		* will list all subdirectories and their files..
		*
		* @param targetDirectoryName The target directory from which to start listing files.
		*/
		public String listFilesHtml(String targetDirectoryName) throws Exception {
				File currentDirectory = new File(targetDirectoryName);

				if(currentDirectory.isDirectory()) {
						String targetFileName = new String();
						String[] files = currentDirectory.list();
						File[] file = currentDirectory.listFiles();
						
						for (int i = 0; i < files.length; i++) {
								targetFileName = targetDirectoryName.concat(file[i].getName());
								File currentFile = new File(targetFileName);
								if(currentFile.isDirectory())
										listFilesHtml(targetFileName.concat("/"));
								else
										if( targetFileName.substring( targetFileName.length() - 8, targetFileName.length() - 4 ).equals( "info" ) ) {
												FeatureTypeBean featureType = new FeatureTypeBean();
												featureType.setHost( hostName(targetFileName) );
												featureType.setReadFeature( featureTypeName(targetFileName) );
												returnHtml = returnHtml + "</tr><tr class='bodyText'><td><a href='featureType.jsp?hostName=" + hostName(targetFileName);
												returnHtml = returnHtml + "&featureTypeName=" + featureTypeName(targetFileName) + "'>" + featureType.getName() + "</a></td>";
												returnHtml = returnHtml + "<td>" + hostName(targetFileName) + "</td>";
												returnHtml = returnHtml + "<td>" + getDescription(featureType) + "</td></tr><tr>";
										}
						}
				}

				return returnHtml;
		}

	 /**
		* Internal private function to extract feature type name from a full path.
		* 
		* @param fullPath The full path from which to extract the feature type name.
		*/
		private String featureTypeName(String fullPath) {
				String returnFileName = new String();
				StringTokenizer newPath = new StringTokenizer(fullPath, "/");
				for ( int i = 0; i < newPath.countTokens() + 5; i++ )
						returnFileName = newPath.nextToken();
				return returnFileName;
		}

	 /**
		* Internal private function to extract host name from a full path.
		* 
		* @param fullPath The full path from which to extract the host name name.
		*/
		private String hostName(String fullPath) {
				String returnFileName = new String();
				StringTokenizer newPath = new StringTokenizer(fullPath, "/");
				for ( int i = 0; i < newPath.countTokens() + 3; i++ )
						returnFileName = newPath.nextToken();
				return returnFileName;
		}

	 /**
		* Internal private function to extract first 30 or fewer chars from the data description.
		* 
		* @param featureType A featureTypeBean to use for extraction.
		*/
		private String getDescription(FeatureTypeBean featureType) {
				String returnAbstract = featureType.getAbstract();

				if ( returnAbstract.length() < 30 )
						return returnAbstract.substring(0, returnAbstract.length() );
				else
						return returnAbstract.substring(0,30);

		}


}
