/* Copyright (c) 2001 Vision for New York - www.vfny.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root application directory.
 */
package org.vfny.geoserver.responses;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.xml.bind.*;
import javax.xml.marshal.*;

import org.apache.log4j.Category;

import org.vfny.geoserver.requests.*;
import org.vfny.geoserver.config.*;
import org.vfny.geoserver.config.configuration.*;

/**
 * Implements the WFS GetCapabilities interface, which tells clients what the server can do.
 *
 * Note that this behavior is implemented using
 * the early access release of JAX-B from Sun.  I found this release to be
 * brilliant in concept, but imperfect in implementation.  In particular,
 * the validator appears to throw invalid exceptions on nested, repeated
 * subelements of valid, well-formed internal XML document representations.
 *
 * Therefore, the get response is assembled not as a monolithic document,
 * which would be much neater, but as a series of subdocuments.  Also, I 
 * have implemented some horrible hacks in the auto-generated code to
 * get it to work in places.  My advice: don't regenerate this code.
 *
 * @author Vision for New York
 * @author Rob Hranac 
 * @version 0.9 alpha, 11/01/01
 *
 */

public class GetCapabilitiesResponse {

		// create standard logging instance for class
		private Category _log = Category.getInstance( GetCapabilitiesResponse.class.getName() );

		// Get some global variables
		private String version;
		private String service;

		// Get some global variables
		private VersionBean versionInfo = new VersionBean();
		private ConfigurationBean configurationInfo = new ConfigurationBean();

		// Convenience variables for XML subfunctions
		private static final int TAG_START = 1;
		private static final int TAG_END = 2;
		private static final int TAG_ONLY = 3;

		private final String OPERATIONS_FILE = configurationInfo.getCapabilitiesDirectory() + "operations.xml";
		private final String FILTER_FILE = configurationInfo.getCapabilitiesDirectory() + "filter.xml";
		private final String SERVICE_METADATA_FILE = configurationInfo.getCapabilitiesDirectory() + "serviceMetadata.xml";
		private final String OPERATIONS_SIGNATURES_FILE = configurationInfo.getCapabilitiesDirectory() + "operationsSignatures.xml";
		private final String ADDITIONAL_CAPABILITIES_FILE = configurationInfo.getCapabilitiesDirectory() + "additionalCapabilities.xml";

		// Create xml output stream elements and configuration object
		private static XmlOutputStream xmlOutFinal = new XmlOutputStream(60000);
		private static XmlOutputStream xmlOutTemp = new XmlOutputStream(60000);

	 /**
		* Passes the Post method to the Get method, with no modifications.
		*
		*/ 
		public GetCapabilitiesResponse(GetCapabilitiesRequest request) {

				version = request.getVersion();
				service = request.getService();

				//_log.info("this is the response version: " + version);
		}

	 /**
		* Handles all Get.responses.
		*
		* This method implements the main return XML logic for the class.
		*
		*/
		public String getXmlResponse()
				throws WfsException {

				// Add xml objects to return stream
				// A TOTAL MESS
				// NEEDS TO BE FIXED

				xmlOutTemp.reset();
				xmlOutFinal.reset();

				if( version.equals("0.0.14") ) {
								addTag("WFS_Capabilities", TAG_START, 0 );
								service();
								capability();
								//xmlOutFinal.writeFile( OPERATIONS_FILE );
								
								addTag("FeatureTypeList", TAG_START, 2 );
								addFeatureTypeInfo( configurationInfo.getFeatureTypeDirectory(), version );
								addTag("FeatureTypeList", TAG_END, 2 );
								
								xmlOutFinal.writeFile( FILTER_FILE );
								addTag("WFS_Capabilities", TAG_END, 0 );
				}
				else {
								xmlOutFinal.writeFile( SERVICE_METADATA_FILE );
								xmlOutFinal.writeFile( OPERATIONS_SIGNATURES_FILE );

								addTag("ContentMetadata", TAG_START, 3 );
								addTag("wfsfl:FeatureTypeList", TAG_START, 6 );
								addFeatureTypeInfo( configurationInfo.getFeatureTypeDirectory(), version );
								addTag("wfsfl:FeatureTypeList", TAG_END, 6 );
								addTag("ContentMetadata", TAG_END, 3 );

								xmlOutFinal.writeFile( ADDITIONAL_CAPABILITIES_FILE );

				}
				return xmlOutFinal.toString();

		}


	/**
		* Internal utility that writes some header information.
		*
		*/
		private void addHeaderInfo() {

				String encoding = "<?xml version='1.0' encoding='UTF-8'?>\n";
				String firstTag = "<WFS_Capabilities version='" + versionInfo.getWfsVersion() + "' sequence='" + versionInfo.getWfsUpdateSequence() + "'>\n";

				xmlOutFinal.write( encoding.getBytes(), 0, encoding.length() );
				xmlOutFinal.write( firstTag.getBytes(), 0, firstTag.length() );
		}


	/**
		* Internal utility that writes xml tags.
		* Immediately writes the tag to the final buffer.
		*
		* @param tag.The XML tag name.
		* @param tagType.The XML tag type, defined in the class.
		*/
		private void addTag( String tag, int tagType, int spaces ) {

				String tempSpaces = new String();
				for( int i=0; i < spaces ; i++ ) {
						tempSpaces = tempSpaces + " ";
				}
				if ( tagType == TAG_END )
						tag = ("/").concat(tag);
				if ( tagType == TAG_ONLY )
						tag = tag.concat("/");
				tag = tempSpaces + "<" + tag;
				tag = tag.concat(">\n");

				xmlOutFinal.write(tag.getBytes(), 0, tag.length() );
		}


		/**
		 * Set all configurable server content 
		 * return document.
		 */
		private void service()
				throws WfsException {

				try {
						xmlOutFinal.write( configurationInfo.getServiceXml( versionInfo.getWfsName() ).getBytes() );
				} catch (IOException e) {
						throw new WfsException( e, "Error appending to XML file", GetCapabilitiesResponse.class.getName() );
				}

		}


	 /**
		* Set all capability information.
		* 
		* This gets messy.  I finally got sick of the JAXB idiocy and wrote it by hand.
		*/
		private void capability()
				throws WfsException {

				String tempCapabilityInfo = new String();

				tempCapabilityInfo = "\n  <Capability>\n    <Request>";
				tempCapabilityInfo = tempCapabilityInfo + tempReturnCapability("GetCapabilities");
				tempCapabilityInfo = tempCapabilityInfo + tempReturnCapability("DescribeFeatureType");
				tempCapabilityInfo = tempCapabilityInfo + tempReturnCapability("GetFeature");
				tempCapabilityInfo = tempCapabilityInfo + "\n    </Request>\n  </Capability>";

				try {
						xmlOutFinal.write(tempCapabilityInfo.getBytes());
				} catch (IOException e) {
						throw new WfsException( e, "Error appending to XML file", GetCapabilitiesResponse.class.getName() );
				}
		}

	 /**
		* Temporary function for adding capability response data. 
		*/
		private String tempReturnCapability(String request) {

				String url = configurationInfo.getUrl();
				String tempCapability = new String();

				tempCapability = "\n      <" + request + ">";
				if (request.equals("DescribeFeatureType") )
						tempCapability = tempCapability + "\n        <SchemaDescriptionLanguage><XMLSCHEMA/></SchemaDescriptionLanguage>";
				if (request.equals("GetFeature") )
						tempCapability = tempCapability + "\n        <ResultFormat><GML2/></ResultFormat>";
				tempCapability = tempCapability + "\n        <DCPType><HTTP><Get onlineResource='" + url + "/" + request + "?'/></HTTP></DCPType>";
				tempCapability = tempCapability + "\n        <DCPType><HTTP><Post onlineResource='" + url + "/" + request + "'/></HTTP></DCPType>\n      </" + request + ">";

				return tempCapability;
		}

	 /**
		* Add feature type info. 
		* 
		* @param targetDirectoryName The directory in which to search for files.
		*/
		private void addFeatureTypeInfo(String targetDirectoryName, String responseVersion)
				throws WfsException {

				// holds final response variable
				String tempResponse = new String();

				// iterated convenience variables
				File currentDirectory = new File( targetDirectoryName );
				String currentFeatureType = new String();
				String currentFileName = new String();

				// keeps master list of files within the directory
				String[] files = currentDirectory.list();
				File[] file = currentDirectory.listFiles();
						
				// Loop through all files in the directory
				for (int i = 0; i < files.length; i++) {
						// assign temp variables; convenience/confusion lesseners only
						currentFileName = file[i].getName();
						addFeatureType( currentFileName, responseVersion );
				}
								
		}



	 /**
		* Adds a feature type object to the final output buffer
		*
		* @param featureTypeName The name of the feature type.
		*/
		private void addFeatureType(String featureTypeName, String responseVersion) 
				throws WfsException {

				FeatureTypeBean responseFeatureType = new FeatureTypeBean( featureTypeName );
				String tempResponse = responseFeatureType.getCapabilitiesXml( responseVersion );

				try {
						xmlOutFinal.write(tempResponse.getBytes());
				} catch (Exception e) {
						throw new WfsException( e, "Could not write XML output file", GetCapabilitiesResponse.class.getName() );
				}
				
		}

	/**
		* Internal utility to write a root element to the temporary buffer, then final buffer.
		* Validates, marshals, strips encoding content, and writes to final buffer.
		*
		* @param xmlBranch.The XML branch root element (JAXB class).
		*/
		private void writeToBuffer(MarshallableRootElement xmlBranch)
				throws WfsException {

				xmlOutTemp.reset();

				try {
						xmlBranch.validate();
						xmlBranch.marshal(xmlOutTemp);
						xmlOutTemp.writeToClean(xmlOutFinal);
				}
				catch (StructureValidationException e) {
						throw new WfsException( e, "Internal XML file is not valid", GetCapabilitiesResponse.class.getName() );
				}
				catch (IOException e) {
						throw new WfsException( e, "Had problems reading internal XML file", GetCapabilitiesResponse.class.getName() );
				}
		}



}


