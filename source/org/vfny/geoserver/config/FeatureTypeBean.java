/* Copyright (c) 2001 Vision for New York - www.vfny.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root application directory.
 */
package org.vfny.geoserver.config;

import java.io.*;
import java.util.*;

import org.apache.log4j.Category;
import org.exolab.castor.xml.*;

import org.vfny.geoserver.config.featureType.*;

/**
 * Reads all necessary feature type information to abstract away from servlets.
 * 
 * @author Vision for New York
 * @author Rob Hranac 
 * @version 0.9 alpha, 11/01/01
 *
 */
public class FeatureTypeBean {
 		
		private FeatureType responseFeatureType;
		//private String featureTypeName;

		// initializes log file
		private Category _log = Category.getInstance(FeatureTypeBean.class.getName());

		public FeatureTypeBean() {
		}

		public FeatureTypeBean(String featureTypeName) {
				readFeatureTypeInformation(featureTypeName);
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

		public String getDatabaseName() {
				return responseFeatureType.getDatabaseName().toString();
		}

		public String getHost() {
				return responseFeatureType.getHost().toString();
		}

		public String getPort() {
				return responseFeatureType.getPort().toString();
		}

		public String getUser() {
				return responseFeatureType.getUser().toString();
		}

		public String getPassword() {
				return responseFeatureType.getPassword().toString();
		}

		public void setReadFeature(String featureTypeName) {
				readFeatureTypeInformation(featureTypeName);
		}

		public String getCapabilitiesXml(String version) {

				if( version.equals("0.0.14") )
						return getCapabilitiesXmlv14();
				else
						return getCapabilitiesXmlv15();

		}

		private void readFeatureTypeInformation(String featureTypeName) {


				ConfigurationBean configurationInfo = new ConfigurationBean();
				String featureTypeFilePath = configurationInfo.getFeatureTypeDirectory() + featureTypeName + "/" + configurationInfo.getFeatureTypeInfoName() + ".xml";

				try {
						FileReader featureTypeDocument = new FileReader( featureTypeFilePath );
						responseFeatureType = (FeatureType) Unmarshaller.unmarshal(FeatureType.class, featureTypeDocument);
				}
				catch( FileNotFoundException e ) {
						_log.info("Feature type file does not exist: " + featureTypeFilePath);
				}
				catch( MarshalException e ) {
						_log.info("Castor could not unmarshal feature type file: " + featureTypeFilePath);
						_log.info("Castor says: " + e.toString() );
				}
				catch( ValidationException e ) {
						_log.info("Castor says the feature type file is not valid XML: " + featureTypeFilePath);
						_log.info("Castor says: " + e.toString() );
				}

		}

		private String getCapabilitiesXmlv14() {

				String tempResponse = "    <FeatureType>\n";
				tempResponse = tempResponse + "      <Name>" + responseFeatureType.getName() + "</Name>\n";
				tempResponse = tempResponse + "      <Title>" + responseFeatureType.getTitle() + "</Title>\n";
				tempResponse = tempResponse + "      <Abstract>" + responseFeatureType.getAbstract() + "</Abstract>\n";
				tempResponse = tempResponse + "      <Keywords>" + responseFeatureType.getKeywords() + "</Keywords>\n";
				tempResponse = tempResponse + "      <SRS>" + responseFeatureType.getSRS() + "</SRS>\n";
				tempResponse = tempResponse + "      <Operations>\n";
				tempResponse = tempResponse + "        <Query/>\n";
				tempResponse = tempResponse + "      </Operations>\n";
				tempResponse = tempResponse + "      <LatLonBoundingBox minx=\"" + responseFeatureType.getLatLonBoundingBox().getMinx() + "\" ";
				tempResponse = tempResponse + "miny=\"" + responseFeatureType.getLatLonBoundingBox().getMiny() + "\" ";
				tempResponse = tempResponse + "maxx=\"" + responseFeatureType.getLatLonBoundingBox().getMaxx() + "\" ";
				tempResponse = tempResponse + "maxy=\"" + responseFeatureType.getLatLonBoundingBox().getMaxy() + "\"/>\n";
				//tempResponse = tempResponse + "      <MetaDataURL";
				//tempResponse = tempResponse + " type=\"" + responseFeatureType.getMetadataURL().getType();
				//tempResponse = tempResponse + "\" format=\"" + responseFeatureType.getMetadataURL().getFormat();
				//tempResponse = tempResponse + "\">" + responseFeatureType.getMetadataURL().getUrl() + "</MetaDataURL>\n";
				tempResponse = tempResponse + "    </FeatureType>\n";
				return tempResponse;
		}


		private String getCapabilitiesXmlv15() {

				String tempResponse = "        <wfsfl:FeatureType>\n";
				tempResponse = tempResponse + "            <wfsfl:Name>" + responseFeatureType.getName() + "</wfsfl:Name>\n";
				tempResponse = tempResponse + "            <wfsfl:SRS srsName=\"" + responseFeatureType.getSRS() + "\"/>\n";
				tempResponse = tempResponse + "            <wfsfl:LatLonBoundingBox minx=\"" + responseFeatureType.getLatLonBoundingBox().getMinx();
				tempResponse = tempResponse + "\" miny=\"" + responseFeatureType.getLatLonBoundingBox().getMiny();
				tempResponse = tempResponse + "\" maxx=\"" + responseFeatureType.getLatLonBoundingBox().getMaxx();
				tempResponse = tempResponse + "\" maxy=\"" + responseFeatureType.getLatLonBoundingBox().getMaxy() + "\"/>\n";
				tempResponse = tempResponse + "        </wfsfl:FeatureType>\n";
				
				return tempResponse;
		}
}
