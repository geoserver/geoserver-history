/* Copyright (c) 2001 Vision for New York - www.vfny.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root application directory.
 */

package org.vfny.geoserver.metadata;

import org.apache.log4j.Category;
import java.io.*;
import java.util.*;
import javax.xml.bind.*;
import javax.xml.marshal.*;
import org.vfny.geoserver.xml.internal.configuration.*;

/**
 * Reads all necessary configuration data and abstracts it away from the response servlets.
 * 
 * @author Vision for New York
 * @author Rob Hranac 
 * @version 0.9 alpha, 11/01/01
 *
 */
public class ConfigurationBean {

		// create standard logging instance for class
		private static Category _log = Category.getInstance( ConfigurationBean.class.getName() );

		// A JAXB class to read internal configuration information.
		private static FreeFSConfiguration configuration = new FreeFSConfiguration();

		// Set up directory structure
		private static final String ROOT_DIRECTORY = findRootResinDirectory();
		private static final String FEATURE_TYPE_DIRECTORY = ROOT_DIRECTORY + "/data/featureTypes/";
		private static final String CONFIGURATION_DIRECTORY = ROOT_DIRECTORY + "/data/configuration.xml";
		private static final String CAPABILITIES_DIRECTORY = ROOT_DIRECTORY + "/data/capabilities/";
		private static final String FEATURE_TYPE_INFO_NAME = "info";
		private static final String FEATURE_TYPE_SCHEMA_NAME = "schema";

	 /**
		* Constructor that reads in configuration information from FreeFS configuration file
		* This information is primarily used in the 'Service' section of the
		* return document.
		*/
		public ConfigurationBean() {
				try {
						File configurationDocument = new File(CONFIGURATION_DIRECTORY);
						FileInputStream configurationFS = new FileInputStream(configurationDocument);
						configuration = configuration.unmarshal(configurationFS);
						configurationFS.close();
				} 
				catch (Exception Exception) {}
		}

		private static String findRootResinDirectory() {	
				return System.getProperty("user.dir") + "/webapps/freefs";
		}

		public String getTitle() {
				return configuration.getService().getTitle();
		}

		public String getAbstract() {
				return configuration.getService().getAbstract();
		}

		public String getKeywords() {
				return configuration.getService().getKeywords();
		}

		public String getOnlineResource() {
				return configuration.getService().getOnlineResource();
		}

		public String getFees() {
				return configuration.getService().getFees();
		}

		public String getAccessConstraints() {
				return configuration.getService().getAccessConstraints();
		}

		public String getDatabaseName() {
				return configuration.getDatabase().getName();
		}

		public String getDatabaseUser() {
				return configuration.getDatabase().getUser();
		}

		public String getDatabasePassword() {
				return configuration.getDatabase().getPassword();
		}

		public String getDatabasePort() {
				return configuration.getDatabase().getPort();
		}

		public String getCurrentTime() {
				java.util.Date now = new java.util.Date();
				return now.toString();
		}

		public String getMaintainer() {
				return configuration.getService().getMaintainer();
		}

		public String getFreeFsVersion() {
				return "0.9b";
		}

		public String getUrl() {
				return configuration.getService().getURL();
		}

		public String getFeatureTypeDirectory() {
				return FEATURE_TYPE_DIRECTORY;
		}

		public String getFeatureTypeInfoName() {
				return FEATURE_TYPE_INFO_NAME;
		}

		public String getCapabilitiesDirectory() {
				return CAPABILITIES_DIRECTORY;
		}

		public String getFeatureTypeSchemaName() {
				return FEATURE_TYPE_SCHEMA_NAME;
		}

}
