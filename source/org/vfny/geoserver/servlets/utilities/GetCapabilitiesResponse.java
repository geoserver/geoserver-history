/* Copyright (c) 2001 Vision for New York - www.vfny.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root application directory.
 */

package org.vfny.geoserver.servlets;

import org.apache.log4j.Category;
import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.xml.bind.*;
import javax.xml.marshal.*;
import org.vfny.geoserver.xml.utilities.*;
import org.vfny.geoserver.xml.responses.*;
import org.vfny.geoserver.xml.internal.configuration.*;
import org.vfny.geoserver.xml.internal.featureType.*;
import org.vfny.geoserver.metadata.*;

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
		private static Category _log = Category.getInstance( GetCapabilitiesResponse.class.getName() );

		// Get some global variables
		private static VersionBean versionInfo = new VersionBean();
		private static ConfigurationBean configurationInfo = new ConfigurationBean();

		// Convenience variables for XML subfunctions
		private static final int TAG_START = 1;
		private static final int TAG_END = 2;
		private static final int TAG_ONLY = 3;

		private static final String OPERATIONS_FILE = configurationInfo.getCapabilitiesDirectory() + "operations.xml";
		private static final String FILTER_FILE = configurationInfo.getCapabilitiesDirectory() + "filter.xml";

		// Create xml output stream elements and configuration object
		private static XmlOutputStream xmlOutFinal = new XmlOutputStream(2000);
		private static XmlOutputStream xmlOutTemp = new XmlOutputStream(2000);

	 /**
		* Passes the Post method to the Get method, with no modifications.
		*
		*/ 
		public GetCapabilitiesResponse() {
		}

	 /**
		* Handles all Get requests.
		*
		* This method implements the main return XML logic for the class.
		*
		* @param getXmlResponse The servlet request object.
		* @param response The servlet response object.
		*/
		public String getXmlResponse() {

				// Add xml objects to return stream
				try {
						xmlOutTemp.reset();
						xmlOutFinal.reset();
						_log.info("just reset");

						addTag("WFS_Capabilities", TAG_START );
						service();
						capability();
						xmlOutFinal.writeFile( OPERATIONS_FILE );

						addTag("FeatureTypeList", TAG_START );
						addFeatureTypeInfo( configurationInfo.getFeatureTypeDirectory() );
						addTag("FeatureTypeList", TAG_END );

						xmlOutFinal.writeFile( FILTER_FILE );
						addTag("WFS_Capabilities", TAG_END );
				}
				catch (Exception Exception) {}

				return xmlOutFinal.toString();

		}



	/**
		* Internal utility that writes some header information.
		*
		*/
		private static void addHeaderInfo() {

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
		private static void addTag( String tag, int tagType ) {

				if ( tagType == TAG_END )
						tag = ("/").concat(tag);
				if ( tagType == TAG_ONLY )
						tag = tag.concat("/");
				tag = ("<").concat(tag);
				tag = tag.concat(">\n");

				xmlOutFinal.write(tag.getBytes(), 0, tag.length() );
		}


		/**
		 * Set all configurable server content 
		 * return document.
		 */
		private static void service() {
				
				// Instantiate service response object
				org.vfny.geoserver.xml.responses.Service responseService = new org.vfny.geoserver.xml.responses.Service();
			 
				// Set service section of Response, based on Configuration input
				responseService.setName( versionInfo.getWfsName() );
				responseService.setTitle( configurationInfo.getTitle() );
				responseService.setAbstract( configurationInfo.getAbstract() );
				responseService.setKeywords( configurationInfo.getKeywords() );
				responseService.setOnlineResource( configurationInfo.getOnlineResource() );
				responseService.setFees( configurationInfo.getFees() );
				responseService.setAccessConstraints( configurationInfo.getAccessConstraints() );

				// Concatenate into XML output stream
				writeToBuffer(responseService);
		}


	 /**
		* Set all capability information.
		* 
		* This gets messy.  I finally got sick of the JAXB idiocy and wrote it by hand.
		*/
		private static void capability() {

				String tempCapabilityInfo = new String();

				tempCapabilityInfo = "\n<Capability>\n\t<Request>";
				tempCapabilityInfo = tempCapabilityInfo + tempReturnCapability("GetCapabilities");
				tempCapabilityInfo = tempCapabilityInfo + tempReturnCapability("DescribeFeatureType");
				tempCapabilityInfo = tempCapabilityInfo + tempReturnCapability("GetFeature");
				tempCapabilityInfo = tempCapabilityInfo + "\n\t</Request>\n</Capability>";

				try {
				xmlOutFinal.write(tempCapabilityInfo.getBytes());
				} catch (Exception Exception) {}
		}

	 /**
		* Temporary function for adding capability response data. 
		*/
		private static String tempReturnCapability(String request) {

				String url = configurationInfo.getUrl();
				String tempCapability = new String();

				tempCapability = "\n\t<" + request + ">";
				if (request.equals("DescribeFeatureType") )
						tempCapability = tempCapability + "\n\t\t<SchemaDescriptionLanguage><XMLSCHEMA/></SchemaDescriptionLanguage>";
				if (request.equals("GetFeature") )
						tempCapability = tempCapability + "\n\t\t<ResultFormat><GML2/></ResultFormat>";
				tempCapability = tempCapability + "\n\t\t<DCPType><HTTP><Get onlineResource='" + url + "/" + request + "?'/></HTTP></DCPType>";
				tempCapability = tempCapability + "\n\t\t\t<DCPType><HTTP><Post onlineResource='" + url + "/" + request + "'/></HTTP></DCPType>\n\t\t</" + request + ">";

				return tempCapability;
		}

	 /**
		* Add feature type info. 
		* 
		* @param targetDirectoryName The directory in which to search for files.
		*/
		private static void addFeatureTypeInfo(String targetDirectoryName) throws Exception {
				File currentDirectory = new File(targetDirectoryName);

				// Recursively search through the appropriate directory
				if(currentDirectory.isDirectory()) {
						String targetFileName = new String();
						String[] files = currentDirectory.list();
						File[] file = currentDirectory.listFiles();
						
						// Loop through all files in the directory
						for (int i = 0; i < files.length; i++) {
								targetFileName = targetDirectoryName.concat(file[i].getName());
								File currentFile = new File(targetFileName);

								// Recurse, if a given object is a directory
								if(currentFile.isDirectory()) {
										addFeatureTypeInfo(targetFileName.concat("/"));
								}
								else {
										// Otherwise, append information from file
										if( targetFileName.substring( targetFileName.length() - 8, targetFileName.length() - 4 ).equals( configurationInfo.getFeatureTypeInfoName() ) ) {
												addFeatureType(targetFileName);
										}
								}
						}
				}
		}

	 /**
		* Adds a feature type object to the final output buffer
		*
		* @param featureTypeName The name of the feature type.
		*/
		private static void addFeatureType(String featureTypeName) throws Exception {

				// Unmarshall the configuration file for translation
				FeatureType responseFeatureType = new FeatureType();
				File featureTypeDocument = new File(featureTypeName);
				FileInputStream featureTypeFS = new FileInputStream(featureTypeDocument);
				try {
						responseFeatureType = responseFeatureType.unmarshal(featureTypeFS);
				} finally {
						featureTypeFS.close();
				}
			  writeToBuffer(responseFeatureType);

				
		}

	/**
		* Internal utility to write a root element to the temporary buffer, then final buffer.
		* Validates, marshals, strips encoding content, and writes to final buffer.
		*
		* @param xmlBranch.The XML branch root element (JAXB class).
		*/
		private static void writeToBuffer(MarshallableRootElement xmlBranch) {
				xmlOutTemp.reset();
				try { xmlBranch.validate(); } catch (StructureValidationException e) {}
				try { xmlBranch.marshal(xmlOutTemp); } catch (IOException e) {}
				try { xmlOutTemp.writeToClean(xmlOutFinal); } catch (IOException e) {}
		}



}


