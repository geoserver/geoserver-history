/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.responses.wfs;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import org.vfny.geoserver.WfsException;
import org.vfny.geoserver.global.GeoServer;
import org.vfny.geoserver.global.NameSpaceInfo;
import org.vfny.geoserver.oldconfig.TypeInfo;
import org.vfny.geoserver.oldconfig.TypeRepository;
import org.vfny.geoserver.oldconfig.VersionBean;
import org.vfny.geoserver.requests.CapabilitiesRequest;
import org.vfny.geoserver.responses.XmlOutputStream;


//import javax.servlet.*;
//import javax.servlet.http.*;

/**
 * Handles a GetCapabilities request and creates a GetCapabilities response GML
 * string. Therefore, the get response is assembled not as a monolithic
 * document, which would be much neater, but as a series of subdocuments.
 * Also, I  have implemented some horrible hacks in the auto-generated code to
 * get it to work in places.  My advice: don't regenerate this code.
 *
 * @author Rob Hranac, TOPP
 * @author Chris Holmes, TOPP
 * @version $Id: OldCapabilitiesResponse.java,v 1.3 2004/01/12 21:01:26 dmzwiers Exp $
 */
public class OldCapabilitiesResponse {
    /** Standard logging instance for class */
    private static final Logger LOGGER = Logger.getLogger(
            "org.vfny.geoserver.requests");

    /** Configuration information for the server. */
    private static GeoServer config = null;

    /** XML Tag Type: start */
    private static final int TAG_START = 1;

    /** XML Tag Type: end */
    private static final int TAG_END = 2;

    /** XML Tag Type: only */
    private static final int TAG_ONLY = 3;

    /** Filter capabilities file */
    private static String FILTER_FILE = "";

    /** ServiceConfig metadata file */
    private static String SERVICE_METADATA_FILE = "";

    /** Operations signatures file */
    private static String OPERATIONS_SIGNATURES_FILE = "";

    /** Additional capabilities file */
    private static String ADDITIONAL_CAPABILITIES_FILE = "";
    private static final String WFS_XMLNS_URL = "http://www.opengis.net/wfs";
    private static final String OGC_XMLNS_URL = "http://www.opengis.net/ogc";
    private static  String CAP_LOC = "";
    private static final String SCHEMA_URI = "http://www.w3.org/2001/XMLSchema-instance";

    /** Version of the response */
    private String version;

    /** ServiceConfig requested */
    private String service;

    /** Version information for the server. */
    private VersionBean versionInfo = new VersionBean();

    /** Final XML output stream elements and configuration object */
    private XmlOutputStream xmlOutFinal = new XmlOutputStream(60000);

    /** Temporary XML output stream elements and configuration object */
    private XmlOutputStream xmlOutTemp = new XmlOutputStream(60000);

    /**
     * Sets version and service.
     *
     * @param request Request from the capabilities response server.
     */
    public OldCapabilitiesResponse(CapabilitiesRequest request) {
        version = request.getVersion();
        service = request.getService();
		config = request.getGeoServer();
        if (version == null) {
            version = ""; //so we don\"t get a null pointer exception
        }
        CAP_LOC = config.getSchemaBaseUrl()+ "wfs/1.0.0/GlobalWFS-capabilities.xsd";
		FILTER_FILE = request.getRootDir() + "capabilities/"	+ "filter.xml";
		SERVICE_METADATA_FILE = request.getRootDir() + "capabilities/" + "serviceMetadata.xml";
		OPERATIONS_SIGNATURES_FILE = request.getRootDir() + "capabilities/" + "operationsSignatures.xml";
		ADDITIONAL_CAPABILITIES_FILE = request.getRootDir() + "capabilities/" + "additionalCapabilities.xml";
    }

    /**
     * Creates the XML response.
     *
     * @return The capabilities document for this server instance.
     *
     * @throws WfsException DOCUMENT ME!
     */
    public String getXmlResponse() throws WfsException {
        // Add xml objects to return stream
        // A TOTAL MESS
        // NEEDS TO BE FIXED
        xmlOutTemp.reset();
        xmlOutFinal.reset();

        if (version.equals("0.0.15")) {
            addHeaderInfo(version);
            xmlOutFinal.writeFile(SERVICE_METADATA_FILE);
            xmlOutFinal.writeFile(OPERATIONS_SIGNATURES_FILE);

            addTag("ContentMetadata", TAG_START, 3);
            addTag("wfsfl:wfsFeatureTypeList", TAG_START, 6);
            addFeatureTypeInfo(version);
            addTag("wfsfl:wfsFeatureTypeList", TAG_END, 6);
            addTag("ContentMetadata", TAG_END, 3);

            xmlOutFinal.writeFile(ADDITIONAL_CAPABILITIES_FILE);
            addTag("WFS_Capabilities", TAG_END, 0);
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
            addTag("FeatureTypeList", TAG_START, 2);
            addTag("Operations", TAG_START, 3);
            addTag("Query", TAG_ONLY, 4);
            addTag("Lock", TAG_ONLY, 4);
            addTag("Operations", TAG_END, 3);
            addFeatureTypeInfo(version);
            addTag("FeatureTypeList", TAG_END, 2);
            xmlOutFinal.writeFile(FILTER_FILE);
            addTag("WFS_Capabilities", TAG_END, 0);
        }

        String retString = xmlOutFinal.toString();

        if (!config.isVerbose()) {
            //REVISIT: this is not as fast as doing all the formatting
            //ourselves, but I'm not sure if it's worth the effort and
            //code complication, as these return strings will never be
            //all that large.  Should do some performance testing.
            retString = retString.replaceAll(">\n[ \\t\\n]*", ">");
            retString = retString.replaceAll("\n[ \\t\\n]*", " ");
        }

        return retString; //xmlOutFinal.toString();
    }

    /**
     * Internal utility that writes some header information.
     *
     * @param version The string of the capabilities version requested.
     */
    private void addHeaderInfo(String version) {
        String spaces = "   ";
        String encoding = "<?xml version=\"1.0\" encoding=\"" + config.getCharSet().displayName()+ "\"?>";
        String firstTag = "<WFS_Capabilities version=\"" + version + "\"";

        if (version.equals("0.0.15")) {
            //I don\"t have 0.0.15 spec right now, but this element is not in .14 or 1.0
            firstTag += (" sequence=\"" + versionInfo.getWfsUpdateSequence()
            + "\"");
        } else if (version.equals("1.0.0")) {
            firstTag += addNameSpace("", WFS_XMLNS_URL);

            NameSpaceInfo[] namespaces = config.getData().getNameSpaces();

            for (int i = 0; i < namespaces.length; i++) {
                firstTag += ("\n" + spaces + namespaces[i].toString());
            }

            //REVISIT: put this ns in config?
            firstTag += addNameSpace(":ogc", OGC_XMLNS_URL);
            firstTag += addNameSpace(":xsi", SCHEMA_URI);
        } else if (version.equals("0.0.14")) {
            //because our filter file now has the ogc prefix.
            //REVISIT: could also strip out ogc: from that file.
            firstTag += addNameSpace(":ogc", OGC_XMLNS_URL);
        }

        firstTag += ("\n   xsi:schemaLocation=\"" + WFS_XMLNS_URL + " "
        + CAP_LOC);
        firstTag += "\">";
        xmlOutFinal.write(encoding.getBytes(), 0, encoding.length());
        xmlOutFinal.write(firstTag.getBytes(), 0, firstTag.length());
    }

    /**
     * Internal utility that writes a namespace tag.
     *
     * @param qName the qualified name, use the empty string if it is to be the
     *        default namespace, if not be sure to include the colon (:wfs for
     *        example)
     * @param url The uri of the schema.
     *
     * @return The formatted xml namespace declration with this qname and url.
     */
    private String addNameSpace(String qName, String url) {
        String spaces = "   ";

        return "\n" + spaces + "xmlns" + qName + "=\"" + url + "\"";
    }

    /**
     * Internal utility that writes xml tags.
     *
     * @param tag XML tag name.
     * @param tagType XML tag type, defined in the class.
     * @param spaces to be added to the XML tag.
     */
    private void addTag(String tag, int tagType, int spaces) {
        String tempSpaces = new String();

        for (int i = 0; i < spaces; i++) {
            tempSpaces = tempSpaces + " ";
        }

        if (tagType == TAG_END) {
            tag = ("/").concat(tag);
        }

        if (tagType == TAG_ONLY) {
            tag = tag.concat("/");
        }

        tag = tempSpaces + "<" + tag;
        tag = tag.concat(">\n");

        xmlOutFinal.write(tag.getBytes(), 0, tag.length());
    }

    /**
     * Adds service information to the XML output stream.
     *
     * @throws WfsException For any io problems.
     */
    private void service() throws WfsException {
        /*
           try {
             xmlOutFinal.write(config.getWFSConfig().getServiceXml()
                               .getBytes());
           }
           catch (IOException e) {
             throw new WfsException(e, "Error appending to XML file",
                                    OldCapabilitiesResponse.class.getName());
           }
         */
    }

    /**
     * Adds capability information to the XML output stream.
     *
     * @throws WfsException For io problems.
     */
    private void capability() throws WfsException {
        StringBuffer tempCapabilityInfo = new StringBuffer();

        tempCapabilityInfo.append("\n  <Capability>\n    <Request>");
        tempCapabilityInfo.append(tempReturnCapability("GetCapabilities"));
        tempCapabilityInfo.append(tempReturnCapability("DescribeFeatureType"));
        tempCapabilityInfo.append(tempReturnCapability("GetFeature"));
        tempCapabilityInfo.append(tempReturnCapability("Transaction"));
        tempCapabilityInfo.append(tempReturnCapability("LockFeature"));
        tempCapabilityInfo.append(tempReturnCapability("GetFeatureWithLock"));
        tempCapabilityInfo.append("\n    </Request>\n  </Capability>\n");

        try {
            xmlOutFinal.write(tempCapabilityInfo.toString().getBytes());
        } catch (IOException e) {
            throw new WfsException(e, "Error appending to XML file",
                OldCapabilitiesResponse.class.getName());
        }
    }

    /**
     * Adds capability information to the XML output stream.
     *
     * @param request The operation to turn into a Capability element.
     *
     * @return The requested capability in the capability document format.
     */
    private String tempReturnCapability(String request) {
        String url = config.getBaseUrl() + "wfs/";
        String tempCapability = new String();

        tempCapability = "\n      <" + request + ">";

        if (request.equals("DescribeFeatureType")) {
            tempCapability = tempCapability
                + "\n        <SchemaDescriptionLanguage><XMLSCHEMA/></SchemaDescriptionLanguage>";
        }

        if (request.startsWith("GetFeature")) {
            tempCapability = tempCapability
                + "\n        <ResultFormat><GML2/></ResultFormat>";
        }

        tempCapability = tempCapability
            + "\n        <DCPType><HTTP><Get onlineResource=\"" + url + "/"
            + request + "?\"/></HTTP></DCPType>";
        tempCapability = tempCapability
            + "\n        <DCPType><HTTP><Post onlineResource=\"" + url + "/"
            + request + "\"/></HTTP></DCPType>\n      </" + request + ">";

        return tempCapability;
    }

    /**
     * Adds feature type metadata to the XML output stream.
     *
     * @param responseVersion The expected version of the WFS response.
     *
     * @throws WfsException For IO problems.
     */
    private void addFeatureTypeInfo(String responseVersion)
        throws WfsException {
        // iterated convenience variables
        TypeRepository repository = TypeRepository.getInstance();
        List typeNames = repository.getAllTypeNames();

        // Loop through all files in the repository.
        for (Iterator i = typeNames.iterator(); i.hasNext();) {
            String featureTypeName = i.next().toString();
            LOGGER.finest("getting capabilities info for " + featureTypeName);

            TypeInfo responseFeatureType = repository.getType(featureTypeName);
            StringBuffer tempResponse = new StringBuffer();

            if (responseFeatureType != null) {
                tempResponse.append(responseFeatureType.getCapabilitiesXml(
                        responseVersion));
            }

            try {
                xmlOutFinal.write(tempResponse.toString().getBytes());
            } catch (IOException e) {
                throw new WfsException(e, "Could not write XML output file",
                    OldCapabilitiesResponse.class.getName());
            }
        }
    }
}
