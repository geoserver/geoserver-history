/* Copyright (c) 2001 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root 
 * application directory.
 */
package org.vfny.geoserver.config;

import java.io.File;
import java.util.Iterator;
import java.util.logging.Logger;
import org.vfny.geoserver.config.FeatureType;

/**
 * Reads all necessary feature type information to abstract away from servlets.
 * 
 * @author Rob Hranac, TOPP
 * @author Chris Holmes, TOPP
 * @version $VERSION$
 */
public class TypeInfo {
        
    /** Class logger */
    private static Logger LOG = Logger.getLogger("org.vfny.geoserver.config");

    public static final String PREFIX_DELIMITER = ":";

    /** Castor-specified type to hold all the  */
    private FeatureType internalType;

    private ConfigInfo config = ConfigInfo.getInstance();
    
    /** The namespace prefix used internally for this featureType.*/
    private String prefix;

    /** the string of where the schema file should be located.*/
    private String pathToSchemaFile;

    /** Initializes the database and request handler. */ 
    public TypeInfo() {}
    

    /**
     * Initializes the database and request handler.
     * @param featureTypeName The query from the request object.
     */ 
    public TypeInfo(String typeName) {
	LOG.finest("reading typeinfo for " + typeName);
        readTypeInfo(typeName);
	
    }

    /**
     * gets the Namespace prefix used internally for this featureType.*/
    public String getPrefix(){
	return this.prefix;
    }
    
    /** Sets the prefix.  The prefix should generally be set during feature
     * type reading, based on the name of the folder in which it is stored,
     * such as ns01:rail, where rail is the name and ns01 is the prefix.
     */
    void setPrefix(String prefix){ 
	this.prefix = prefix;
    }

    /**
     * gets the string of the path to the schema file.  This is set during
     * feature reading, the schema file should be in the same folder as the
     * feature type info, with the name schema.xml.  This function does not
     * guarantee that the schema file actually exists, it just gives the
     * location where it _should_ be located.
     */
    public String getSchemaFile(){
	return pathToSchemaFile;
    }

    /**
     * gets the namespace uri of this type.  The mappings must be set right
     * in ConfigInfo for this to work right.
     */
    public String getXmlns(){
	return config.getNSUri(this.prefix);
    }
    
    /** Fetches the feature type name (also the table name)  */
    //REVISIT: name getTableName()?  It should only be used for that purpose.
    public String getName() { 
	LOG.finer("returning name " + internalType.getName());
	return internalType.getName(); 
    }
    
    /** Fetches the featureType name with its proper namespace prefix*/
    public String getFullName() {
	return prefix + ":" + internalType.getName();
    }
    
    /** Fetches the feature type abstract */
    public String getAbstract() { return internalType.getAbstract(); }
    
    /** Fetches the feature type spatial reference system */
    public String getSrs() { return internalType.getSRS(); }
    
    /** Fetches the user-defined feature type keywords  */
     public String getKeywords() { 
	StringBuffer keywords = new StringBuffer();
	Iterator keywordIter = internalType.getKeywords().iterator();
	while (keywordIter.hasNext()) {
	    keywords.append(keywordIter.next().toString());
	    if (keywordIter.hasNext()){
		keywords.append(", ");
	    }
	}
	return keywords.toString(); 
    }    

    /** Fetches the user-defined bounding box  */
    public String getBoundingBox() { 
        return internalType.getLatLonBoundingBox().toString();
    }
    
    /** Fetches the user-defined metadata URL  */
    public String getMetadataUrl() { 
        return null; //not implemented in new FeatureType internal type yet.
    }
    
    /** Fetches the user-defined database name  */
    public String getDatabaseName() { 
        return internalType.getDatabaseName().toString();
    }

    /** Fetches the user-defined feature type keywords  */
    public String getHost() { 
        return internalType.getHost().toString();
    }
    
    /** Fetches the user-defined port for the database  */
    public String getPort() { 
        return internalType.getPort().toString();
    }

    /** Fetches the user-defined user for the database */
    public String getUser() { 
        return internalType.getUser().toString();
    }

    /** Fetches the user-defined password for the database */
    public String getPassword() { 
        return internalType.getPassword().toString();
    }

    /**
     * Returns a capabilities XML fragment for a specific feature type.
     * @param version The version of the request (0.0.14 or 0.0.15)
     */ 
    public String getCapabilitiesXml(String version) {        
	LOG.finest("getting capabilities " + version);
        if(version.equals("0.0.14") || version.equals("1.0.0" )) {
	    //1.0.0 is almost exactly like 0.0.14
            return getCapabilitiesXmlv14(version);
        } else {
            return getCapabilitiesXmlv15();
        }        
    }
    
    /**
     * Reads feature type information
     * @param typeName The query from the request object.
     */ 
    private void readTypeInfo(String typeName) {
        
	try {
	    
	    File featureTypeFile = new File(typeName);
	    File parentDir = featureTypeFile.getParentFile();
	    String parentDirName =  parentDir.getName();
	    int prefixDelimPos = parentDirName.lastIndexOf(PREFIX_DELIMITER);
	    if (prefixDelimPos > 0) {
		prefix = parentDirName.substring(0, prefixDelimPos);
	    } else {
		prefix = config.getDefaultNSPrefix();
	    }
	    pathToSchemaFile = 
		new File(parentDir, config.SCHEMA_FILE).toString();
	    LOG.finer("pathToSchema is " + pathToSchemaFile);
	    //LOG.finer("prefix is " + prefix);
	    
	    internalType = FeatureType.getInstance(typeName);
	    
	} catch( ConfigurationException e ) {
            LOG.info("Trouble reading featureType info at " + typeName + 
		     ": " + e.getMessage());
	}
    }
    

    /**
     * Generates v0.0.14 capabilities document fragment for a feature type.
     * Also used for v1.0.0, as they are practically identical, except
     * LatLonBoundingBox has a Long instead of a Lon, and it uses the prefix.
     *
     */ 
    private String getCapabilitiesXmlv14(String version) {
	// MAKE TERSE VERSION CAPABILITY
        StringBuffer tempResponse = new StringBuffer("    <FeatureType>\n");
        String name = internalType.getName();
	String latLonName = "LatLonBoundingBox";
	if (!version.startsWith("0.0.1")) {
	    //REVISIT: get this elsewhere?  Make sure that myns is
	    //declared in the capabilities document returned.
	    name = prefix + ":" + name;
	    latLonName = "LatLongBoundingBox";
	}
	tempResponse.append("      <Name>" + name + "</Name>\n");
        tempResponse.append("      <Title>" + 
            internalType.getTitle() + "</Title>\n");
        tempResponse.append("      <Abstract>" + 
            internalType.getAbstract() + "</Abstract>\n");
        tempResponse.append("      <Keywords>" + 
            getKeywords() + "</Keywords>\n");
        tempResponse.append(
            "      <SRS>http://www.opengis.net/gml/srs/epsg#" + 
            internalType.getSRS() + "</SRS>\n");
	//TODO: Should we allow the admin to customize these?  He may
	//not want to publicize to the world that it is transactional.
        //but if we just use the internalType marshalling way then the
	//admin could easily mess up the xml, putting the wrong terms in.
	//--query datasource on its capabilities.
        tempResponse.append("      <Operations>\n");
        tempResponse.append("        <Query/>\n");
	tempResponse.append("        <Insert/>\n");
	tempResponse.append("        <Update/>\n");
	tempResponse.append("        <Delete/>\n");
        tempResponse.append("      </Operations>\n");
        tempResponse.append(
            "      <" + latLonName + " minx=\"" + 
            internalType.getLatLonBoundingBox().getMinx() + 
            "\" ");
        tempResponse.append("miny=\"" + 
            internalType.getLatLonBoundingBox().getMiny() + 
            "\" ");
        tempResponse.append("maxx=\"" + 
                    internalType.getLatLonBoundingBox().getMaxx() + 
            "\" ");
        tempResponse.append("maxy=\"" + 
            internalType.getLatLonBoundingBox().getMaxy() + 
            "\"/>\n");
                //tempResponse.append("      <MetaDataURL";
                //tempResponse.append(" type=\"" + internalType.getMetadataURL().getType();
                //tempResponse.append("\" format=\"" + internalType.getMetadataURL().getFormat();
                //tempResponse.append("\">" + internalType.getMetadataURL().getUrl() + "</MetaDataURL>\n";
        tempResponse.append("    </FeatureType>\n");
	return tempResponse.toString();
    }
    
     /**
      * Generates v0.0.15 capabilities document fragment for a feature type.
      *
      */ 
    private String getCapabilitiesXmlv15() {
        // SHOULD CHANGE TO STRING BUFFER
        // ALSO MAKE TERSE VERSION CAPABILITY
        StringBuffer tempResponse = 
	    new StringBuffer("        <wfsfl:FeatureType>\n");
        tempResponse.append("            <wfsfl:Name>" + 
            internalType.getName() + "</wfsfl:Name>\n");
        tempResponse.append(
            "            <wfsfl:SRS srsName=\""+
            "http://www.opengis.net/gml/srs/epsg#" + 
            internalType.getSRS() + "\"/>\n");
        tempResponse.append(
            "            <wfsfl:LatLonBoundingBox minx=\"" + 
            internalType.getLatLonBoundingBox().getMinx());
        tempResponse.append("\" miny=\"" + 
            internalType.getLatLonBoundingBox().getMiny());
        tempResponse.append("\" maxx=\"" + 
            internalType.getLatLonBoundingBox().getMaxx());
        tempResponse.append("\" maxy=\"" + 
            internalType.getLatLonBoundingBox().getMaxy() + 
            "\"/>\n");
        tempResponse.append(
            "            <wfsfl:Operations><wfsfl:Query/></wfsfl:"
            + "Operations>\n");
        tempResponse.append(
            "        </wfsfl:FeatureType>\n");                
        return tempResponse.toString();
    }
}
