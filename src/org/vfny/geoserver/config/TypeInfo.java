/* Copyright (c) 2001 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root 
 * application directory.
 */
package org.vfny.geoserver.config;

import java.io.File;
import java.util.List;
import java.util.Iterator;
import java.util.Properties;
import java.util.logging.Logger;
import java.sql.Connection;
import java.sql.SQLException;
import org.vfny.geoserver.config.FeatureType;
import org.geotools.feature.Feature;
//import org.geotools.feature.FeatureType;
import org.geotools.feature.FeatureTypeFactory;
import org.geotools.feature.AttributeType;
import org.geotools.feature.SchemaException;
import org.geotools.data.DataSource;
import org.geotools.data.DataSourceException;
import org.geotools.data.postgis.PostgisConnectionFactory;
import org.geotools.data.postgis.PostgisDataSource;
import org.vfny.geoserver.responses.WfsException;
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

    /** standard connection for getFeatures. */
    Connection dbConnection;

    //hold the default schema for this datasource?  Would save processing, and
    //when schemas can go to xsd and back it would be good to have here.

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
     * returns a connection to the database containing the table for this
     * type.  If this typeInfo has already made a connection to the database
     * then this just returns that connection.  This method should _not_ be
     * used if transactions are being performed, getNewConnection should be 
     * used instead, or else the getFeature calls risk returning modified 
     * features that have not yet been committed and could roll back.
     */
    public Connection getConnection() throws WfsException {
	LOG.finer("getting connection for type: " + getName());
	if (dbConnection == null) {
	    try {
		this.dbConnection = getNewConnection();
	    } catch (SQLException e) {
		String message = "Problem getting Connection to db: " 
		    + e.getMessage();
		LOG.warning(message);
		throw new WfsException(message, "typeInfo");
	    }
	}
	return dbConnection;
    }

    /**
     * returns a new connection.  This is a relatively expensive operation,
     * so getConnection should be used where possible.  But this method must
     * be used if transactions are being performed, or else you risk 
     * inconsistent features.
     * @return a new connection created using the information read from the
     * type represented by this typeInfo.
     * @throws SQLException if there were problems connecting with the SQL 
     * database.
     * @tasks TODO: put the charset in the configuration, for internationalization.
     * this method would then set the right charSet.
     */
    public Connection getNewConnection() throws SQLException {
	LOG.finer("getting new connection for type : " + getName());
	PostgisConnectionFactory db = 
	    new PostgisConnectionFactory (getHost(), getPort(), 
					  getDatabaseName()); 
	Properties dbProps = new Properties();
	dbProps.put("user", getUser());
	dbProps.put("password", getPassword());
	//dbProps.put("charSet", "iso-8859-1");
	Connection connection;
	connection = db.getConnection(dbProps);
	return connection;
    }


    /**
     * gets the datasource associated with this typeInfo.  This uses the current
     * datasource connection, so it should not be used for transactions, those
     * should generate their own datasources from their connections.  
     *
     * @param propertyNames the list of attributes that this datasource should
     * return.  If null then all attributes are returned.  If empty (size = 0)
     * then a datasource that only returns fids will be returned.
     * @return a newly created datasource using the connection held by this 
     * typeInfo.  
     * @throws WfsException if there were problems creating the datasource
     * or the schema.
     */
    public DataSource getDataSource(List propertyNames, int maxFeatures) 
	throws WfsException {
	LOG.finer("about to get datasource for " + getName());
	Connection connection = getConnection();
	DataSource data = null;
	try {
	    data = new PostgisDataSource(connection, getName(), maxFeatures);
	    if (propertyNames != null) {
		org.geotools.feature.FeatureType schema = data.getSchema();
		AttributeType[] properties = new AttributeType[propertyNames.size()];
		try {
		    for(int i = 0; i < propertyNames.size(); i++) {
			String curPropName = propertyNames.get(i).toString();
			//TODO: get rid of this code duplication.  
			int prefixDelimPos = 
			    curPropName.lastIndexOf(PREFIX_DELIMITER);
			//this gets out the namespace prefix.
			//REVISIT: check to make sure it's the right namespace
			if (prefixDelimPos > 0) {
			    curPropName = curPropName.substring
				(prefixDelimPos + 1, curPropName.length());
			   
			} 
			//this strips out the leading featureName of .14 style.
			//REVISIT: this is fine for postgis, as you can't 
			//declare attributes of the feature.property style,
			//but when we add support for oracle and others this
			//wont work.  In other words, put this in postgis
			//datasource.
			int propDelimPos = curPropName.lastIndexOf(".");
			if (propDelimPos > 0) {
			    //for backwards compatibility.  Only works if all 
			    //featureTypes have the same prefix.
			    curPropName = curPropName.substring
				(propDelimPos + 1, curPropName.length());
			   
			} 
			properties[i] = 
			    schema.getAttributeType(curPropName);
			if (properties[i] == null) {
			    //TODO: iterate through schema to get the valid
			    //names
			    throw new WfsException("property name: " + 
						   curPropName + " is "
						   +"not a part of featureType"
						   + ", try a DescribeFeatureType"
						   + " request for this typename");
			}
		    }
		    schema = FeatureTypeFactory.create(properties);	 
		}  catch (SchemaException e) {
		    throw new WfsException(e, "While trying to create schema for " +
					   "return feature", getName());
		}
		data.setSchema(schema);
	    }
	} catch (DataSourceException e) {
            throw new WfsException(e, "While getting features from datasource",
				   getName());
	}
	return data;
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
