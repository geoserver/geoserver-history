/* Copyright (c) 2001 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root 
 * application directory.
 */
package org.vfny.geoserver.responses;

import java.io.*;
import java.util.*;
import java.util.logging.Logger;
import javax.servlet.*;
import javax.servlet.http.*;
import org.vfny.geoserver.requests.*;
import org.vfny.geoserver.config.*;


/**
 * Handles a GetCapabilities request and creates a GetCapabilities response 
 * GML string.
 *
 * Therefore, the get response is assembled not as a monolithic document,
 * which would be much neater, but as a series of subdocuments.  Also, I 
 * have implemented some horrible hacks in the auto-generated code to
 * get it to work in places.  My advice: don't regenerate this code.
 *
 *@author Rob Hranac, TOPP
 *@author Chris Holmes, TOPP
 *@version $VERSION$
 */
public class CapabilitiesResponse {

    /** Standard logging instance for class */
    private static final Logger LOGGER = 
        Logger.getLogger("org.vfny.geoserver.requests");
    
    /** Version of the response */
    private String version;
    
    /** Service requested */
    private String service;
    
    /** Version information for the server. */
    private VersionBean versionInfo = new VersionBean();
    
    /** Configuration information for the server. */
    private static ConfigInfo config = ConfigInfo.getInstance();
    
    /** XML Tag Type: start */
    private static final int TAG_START = 1;
    
    /** XML Tag Type: end */
    private static final int TAG_END = 2;
    
    /** XML Tag Type: only */
    private static final int TAG_ONLY = 3;
    
    
    /******************************************
      Convenience variables for XML subfunctions
     *******************************************/

    /** Operations capabilities file */
    private final String OPERATIONS_FILE = config.getCapabilitiesDir() + "operations.xml";
    
    /** Filter capabilities file */
    private final String FILTER_FILE = config.getCapabilitiesDir() + "filter.xml";
    
    /** Service metadata file */
    private final String SERVICE_METADATA_FILE = config.getCapabilitiesDir() + "serviceMetadata.xml";
    
    /** Operations signatures file */
    private final String OPERATIONS_SIGNATURES_FILE = config.getCapabilitiesDir() + "operationsSignatures.xml";

    /** Additional capabilities file */
    private final String ADDITIONAL_CAPABILITIES_FILE = config.getCapabilitiesDir() + "additionalCapabilities.xml";
    
    /** Final XML output stream elements and configuration object */
    private static XmlOutputStream xmlOutFinal = new XmlOutputStream(60000);
    
    /** Temporary XML output stream elements and configuration object */
    private static XmlOutputStream xmlOutTemp = new XmlOutputStream(60000);
    
    private static final String WFS_XMLNS_URL = "http://www.opengis.net/wfs";
 
    private static final String OGC_XMLNS_URL =	"http://www.opengis.net/ogc";
    
    
    /**
     * Sets version and service.
     * @param request Request from the capabilities response server.
     */ 
    public CapabilitiesResponse(CapabilitiesRequest request) {
        version = request.getVersion();
        service = request.getService();
	if (version == null) {
	    version = ""; //so we don\"t get a null pointer exception
	}
    }
    

    /**
     * Creates the XML response.
     *
     */
    public String getXmlResponse()
        throws WfsException {
        
        // Add xml objects to return stream
        // A TOTAL MESS
        // NEEDS TO BE FIXED
        
        xmlOutTemp.reset();
        xmlOutFinal.reset();
        
	if(version.equals("0.0.15")) {
	    addHeaderInfo(version);
            xmlOutFinal.writeFile( SERVICE_METADATA_FILE );
            xmlOutFinal.writeFile( OPERATIONS_SIGNATURES_FILE );

            addTag("ContentMetadata", TAG_START, 3 );
            addTag("wfsfl:wfsFeatureTypeList", TAG_START, 6 );
            addFeatureTypeInfo( config.getTypeDir(), version );
            addTag("wfsfl:wfsFeatureTypeList", TAG_END, 6 );
            addTag("ContentMetadata", TAG_END, 3 );
            
            xmlOutFinal.writeFile( ADDITIONAL_CAPABILITIES_FILE );
            addTag("WFS_Capabilities", TAG_END, 0 );

        } else { //0.0.14 or 1.0.0
	    if (!version.equals("0.0.14")) {
		//default is to return 1.0.0
		version = "1.0.0";
	    }
	    addHeaderInfo(version);
            //addTag("WFS_Capabilities", TAG_START, 0 );
            service();
            capability();
            //xmlOutFinal.writeFile( OPERATIONS_FILE );
            
            addTag("FeatureTypeList", TAG_START, 2 );
	    addTag("Operations", TAG_START, 3);
	    addTag("Query", TAG_ONLY, 4);
	    addTag("Lock", TAG_ONLY, 4);
	    addTag("Operations", TAG_END, 3);
            addFeatureTypeInfo( config.getTypeDir(), version );
            addTag("FeatureTypeList", TAG_END, 2 );
            xmlOutFinal.writeFile( FILTER_FILE );
            addTag("WFS_Capabilities", TAG_END, 0 );
        }
	
	String retString = xmlOutFinal.toString();
	if(!config.formatOutput()){
	    //REVISIT: this is not as fast as doing all the formatting 
	    //ourselves, but I'm not sure if it's worth the effort and
	    //code complication, as these return strings will never be
	    //all that large.  Should do some performance testing.
	    retString = retString.replaceAll(">\n[ \\t\\n]*", ">");
	    retString = retString.replaceAll("\n[ \\t\\n]*", " ");
	}
        return retString;//xmlOutFinal.toString();

    }
    
    
    /**
     * Internal utility that writes some header information.
     *
     */
    private void addHeaderInfo(String version) {
	String spaces = "   ";
        String encoding = config.getXmlHeader();
        String firstTag = "<WFS_Capabilities version=\"" + version + "\"";
        if (version.equals("0.0.15")) {
	    //I don\"t have 0.0.15 spec right now, but this element is not in .14 or 1.0
	    firstTag += " sequence=\"" + versionInfo.getWfsUpdateSequence() + "\"";
	} else if (version.equals("1.0.0")) {
	    firstTag += addNameSpace("", WFS_XMLNS_URL);
	    String[] namespaces = config.getAllXmlns();
	    for(int i = 0; i < namespaces.length; i++) {
		firstTag += "\n" + spaces + namespaces[i];
	    }
	    //REVISIT: put this ns in config?
	    firstTag += addNameSpace(":ogc", OGC_XMLNS_URL);
	} else if (version.equals("0.0.14")){
	    //because our filter file now has the ogc prefix.  
	    //REVISIT: could also strip out ogc: from that file.
	     firstTag += addNameSpace(":ogc", OGC_XMLNS_URL);
	}
	firstTag += ">";	   
        xmlOutFinal.write( encoding.getBytes(), 0, encoding.length() );
        xmlOutFinal.write( firstTag.getBytes(), 0, firstTag.length() );
    }


    /**
     * Internal utility that writes a namespace tag.
     *
     * @param qName the qualified name, use the empty string
     * if it is to be the default namespace, if not be sure
     * to include the colon (:wfs for example)
     * @param url The uri of the schema.
     */
    private String addNameSpace(String qName, String url){
	String spaces = "   ";
	return "\n" + spaces + "xmlns" + qName + "=\"" + url + "\"";
    }
    
    
    /**
     * Internal utility that writes xml tags.
     *
     * @param tag.The XML tag name.
     * @param tagType.The XML tag type, defined in the class.
     * @param spaces.Spaces to be added to the XML tag.
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
     * Adds service information to the XML output stream.
     *
     */
    private void service()
        throws WfsException {
        
        try {
            xmlOutFinal.write( config.getServiceXml( versionInfo.getWfsName() ).getBytes() );
        } catch (IOException e) {
            throw new WfsException( e, "Error appending to XML file", CapabilitiesResponse.class.getName() );
        }
        
    }
    
    
    /**
     * Adds capability information to the XML output stream.
     * 
     */
    private void capability()
        throws WfsException {
        
        StringBuffer tempCapabilityInfo = new StringBuffer();
        
        tempCapabilityInfo.append("\n  <Capability>\n    <Request>");
        tempCapabilityInfo.append(tempReturnCapability("GetCapabilities"));
        tempCapabilityInfo.append(tempReturnCapability("DescribeFeatureType"));
        tempCapabilityInfo.append(tempReturnCapability("GetFeature"));
	tempCapabilityInfo.append(tempReturnCapability("Transaction"));
	tempCapabilityInfo.append(tempReturnCapability("LockFeature"));
        tempCapabilityInfo.append("\n    </Request>\n  </Capability>\n");
        
        try {
            xmlOutFinal.write(tempCapabilityInfo.toString().getBytes());
        } catch (IOException e) {
            throw new WfsException( e, "Error appending to XML file", CapabilitiesResponse.class.getName() );
        }
    }

    
    /**
     * Adds capability information to the XML output stream.
     *
     */
    private String tempReturnCapability(String request) {
        
        String url = config.getUrl();
        String tempCapability = new String();
        
        tempCapability = "\n      <" + request + ">";
        if (request.equals("DescribeFeatureType") )
            tempCapability = tempCapability + "\n        <SchemaDescriptionLanguage><XMLSCHEMA/></SchemaDescriptionLanguage>";
        if (request.equals("GetFeature") )
            tempCapability = tempCapability + "\n        <ResultFormat><GML2/></ResultFormat>";
        tempCapability = tempCapability + "\n        <DCPType><HTTP><Get onlineResource=\"" + url + "/" + request + "?\"/></HTTP></DCPType>";
        tempCapability = tempCapability + "\n        <DCPType><HTTP><Post onlineResource=\"" + url + "/" + request + "\"/></HTTP></DCPType>\n      </" + request + ">";
        
        return tempCapability;
    }
    
    
    /**
     * Adds feature type metadata to the XML output stream.
     * 
     * @param targetDirectoryName The directory in which to search for files.
     * @param responseVersion The expected version of the WFS response.
     */
    private void addFeatureTypeInfo(String targetDirectoryName, String responseVersion)
        throws WfsException {
        
        // holds final response variable
     
        
        // iterated convenience variables
	TypeRepository repository = TypeRepository.getInstance();
	List typeNames = repository.getAllTypeNames();
        // Loop through all files in the repository.
        for (Iterator i = typeNames.iterator(); i.hasNext();) {
	    String featureTypeName = i.next().toString();
	 TypeInfo responseFeatureType = repository.getType( featureTypeName );
	 StringBuffer tempResponse = new StringBuffer();
	 if (responseFeatureType != null) {
	    tempResponse.append(
		responseFeatureType.getCapabilitiesXml( responseVersion ));
        }
        try {
            xmlOutFinal.write(tempResponse.toString().getBytes());
        } catch (Exception e) {
            throw new WfsException( e, "Could not write XML output file", 
				    CapabilitiesResponse.class.getName() );
        }

        }
        
    }

    

}


