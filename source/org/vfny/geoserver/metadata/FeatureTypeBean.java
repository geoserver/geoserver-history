/*
 * Copyright (c) 2001 Vision for New York - www.vfny.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root application directory.
 */
package org.vfny.geoserver.metadata;

import java.io.*;
import java.util.*;
import javax.xml.bind.*;
import javax.xml.marshal.*;
import org.vfny.geoserver.xml.internal.featureType.*;
import org.vfny.geoserver.metadata.*;

/**
 * Reads all necessary feature type information to abstract away from servlets.
 * 
 * @author Vision for New York
 * @author Rob Hranac 
 * @version 0.9 alpha, 11/01/01
 *
 */
public class FeatureTypeBean {
 		
		private static ConfigurationBean configurationInfo = new ConfigurationBean();
		private static FeatureType responseFeatureType = new FeatureType();
		private String featureTypeHost = new String();
		private String featureTypeName = new String();

		public FeatureTypeBean() throws Exception {
				ConfigurationBean configuration = new ConfigurationBean();
		}

		public String getName() {
				return responseFeatureType.getName();
		}

		public String getAbstract() {
				return responseFeatureType.getAbstract();
		}

		public String getSrs() {
				return responseFeatureType.getSRS();
		}

		public String getKeywords() {
				return responseFeatureType.getKeywords();
		}

		public String getBoundingBox() {
				return responseFeatureType.getLatLonBoundingBox().toString();
		}

		public String getMetadataUrl() {
				return responseFeatureType.getMetadataURL().toString();
		}

		public void setHost(String featureTypeHost) {
				this.featureTypeHost = featureTypeHost;				
		}

		public void setReadFeature(String featureTypeName) throws Exception {
				this.featureTypeName = featureTypeName;

				// Unmarshall the configuration file for translation
				File featureTypeDocument = new File( configurationInfo.getFeatureTypeDirectory() + "/" + featureTypeHost + "/" + featureTypeName + configurationInfo.getFeatureTypeInfoName() + ".xml");
				FileInputStream featureTypeFS = new FileInputStream(featureTypeDocument);
				try {
						responseFeatureType = responseFeatureType.unmarshal(featureTypeFS);
				} finally {
						featureTypeFS.close();
				}
		}

}
