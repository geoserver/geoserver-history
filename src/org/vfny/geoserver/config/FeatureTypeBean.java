/* Copyright (c) 2001 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root 
 * application directory.
 */
package org.vfny.geoserver.config;

import java.io.*;
import java.util.*;
import java.util.logging.Logger;
import org.exolab.castor.xml.*;
import org.vfny.geoserver.config.featureType.*;

/**
 * Reads all necessary feature type information to abstract away from servlets.
 * 
 * @author Rob Hranac, TOPP
 * @version $VERSION$
 */
public class FeatureTypeBean {
        
    /** Class logger */
    private static Logger LOGGER = 
        Logger.getLogger("org.vfny.geoserver.config");

    /** Castor-specified type to hold all the  */
    private FeatureType responseFeatureType;
    
    /** Initializes the database and request handler. */ 
    public FeatureTypeBean() {}
    

    /**
     * Initializes the database and request handler.
     * @param featureTypeName The query from the request object.
     */ 
    public FeatureTypeBean(String featureTypeName) {
        readFeatureTypeInformation(featureTypeName);
    }
    
    /** Fetches the feature type name (also the table name)  */
    public String getName() { return responseFeatureType.getName(); }
    
    /** Fetches the feature type abstract */
    public String getAbstract() { return responseFeatureType.getAbstract(); }
    
    /** Fetches the feature type spatial reference system */
    public String getSrs() { return responseFeatureType.getSRS(); }
    
    /** Fetches the user-defined feature type keywords  */
    public String getKeywords() { return responseFeatureType.getKeywords(); }

    /** Fetches the user-defined bounding box  */
    public String getBoundingBox() { 
        return responseFeatureType.getLatLonBoundingBox().toString();
    }
    
    /** Fetches the user-defined metadata URL  */
    public String getMetadataUrl() { 
        return responseFeatureType.getMetadataURL().toString();
    }
    
    /** Fetches the user-defined database name  */
    public String getDatabaseName() { 
        return responseFeatureType.getDatabaseName().toString();
    }

    /** Fetches the user-defined feature type keywords  */
    public String getHost() { 
        return responseFeatureType.getHost().toString();
    }
    
    /** Fetches the user-defined port for the database  */
    public String getPort() { 
        return responseFeatureType.getPort().toString();
    }

    /** Fetches the user-defined user for the database */
    public String getUser() { 
        return responseFeatureType.getUser().toString();
    }

    /** Fetches the user-defined password for the database */
    public String getPassword() { 
        return responseFeatureType.getPassword().toString();
    }

    /** Reads feature type information */ 
    public void setReadFeature(String featureTypeName) {
        readFeatureTypeInformation(featureTypeName);
    }
    
    /**
     * Returns a capabilities XML fragment for a specific feature type.
     * @param version The version of the request (0.0.14 or 0.0.15)
     */ 
    public String getCapabilitiesXml(String version) {
        
        if(version.equals("0.0.14")) {
            return getCapabilitiesXmlv14();
        } else {
            return getCapabilitiesXmlv15();
        }        
    }
    
    /**
     * Reads feature type information
     * @param featureTypeName The query from the request object.
     */ 
    private void readFeatureTypeInformation(String featureTypeName) {
        
        
        ConfigurationBean configurationInfo = new ConfigurationBean();
        String featureTypeFilePath = 
            configurationInfo.getFeatureTypeDirectory() + 
            featureTypeName + "/" + configurationInfo.getFeatureTypeInfoName() 
            + ".xml";
        try {
            FileReader featureTypeDocument = 
                new FileReader(featureTypeFilePath);
            responseFeatureType = 
                (FeatureType) Unmarshaller.unmarshal(FeatureType.class, 
                                                     featureTypeDocument);
        }
        catch( FileNotFoundException e ) {
                        LOGGER.info("Feature type file does not exist: " + 
                                  featureTypeFilePath);
        }
        catch( MarshalException e ) {
            LOGGER.info("Castor could not unmarshal feature type file: " + 
                      featureTypeFilePath);
            LOGGER.info("Castor says: " + e.toString() );
        }
                catch( ValidationException e ) {
                    LOGGER.info("Castor says: feature type XML not valid : "
                              + featureTypeFilePath);
                    LOGGER.info("Castor says: " + e.toString() );
                }
        
    }


    /**
     * Generates v0.0.14 capabilities document fragment for a feature type.
     *
     */ 
    private String getCapabilitiesXmlv14() {
        // SHOULD CHANGE TO STRING BUFFER
        // ALSO MAKE TERSE VERSION CAPABILITY
        String tempResponse = "    <FeatureType>\n";
        tempResponse = tempResponse + "      <Name>" + 
            responseFeatureType.getName() + "</Name>\n";
        tempResponse = tempResponse + "      <Title>" + 
            responseFeatureType.getTitle() + "</Title>\n";
        tempResponse = tempResponse + "      <Abstract>" + 
            responseFeatureType.getAbstract() + "</Abstract>\n";
        tempResponse = tempResponse + "      <Keywords>" + 
            responseFeatureType.getKeywords() + "</Keywords>\n";
        tempResponse = tempResponse + 
            "      <SRS>http://www.opengis.net/gml/srs/epsg#" + 
            responseFeatureType.getSRS() + "</SRS>\n";
        tempResponse = tempResponse + "      <Operations>\n";
        tempResponse = tempResponse + "        <Query/>\n";
        tempResponse = tempResponse + "      </Operations>\n";
        tempResponse = tempResponse + 
            "      <LatLonBoundingBox minx=\"" + 
            responseFeatureType.getLatLonBoundingBox().getMinx() + 
            "\" ";
        tempResponse = tempResponse + "miny=\"" + 
            responseFeatureType.getLatLonBoundingBox().getMiny() + 
            "\" ";
        tempResponse = tempResponse + "maxx=\"" + 
                    responseFeatureType.getLatLonBoundingBox().getMaxx() + 
            "\" ";
        tempResponse = tempResponse + "maxy=\"" + 
            responseFeatureType.getLatLonBoundingBox().getMaxy() + 
            "\"/>\n";
                //tempResponse = tempResponse + "      <MetaDataURL";
                //tempResponse = tempResponse + " type=\"" + responseFeatureType.getMetadataURL().getType();
                //tempResponse = tempResponse + "\" format=\"" + responseFeatureType.getMetadataURL().getFormat();
                //tempResponse = tempResponse + "\">" + responseFeatureType.getMetadataURL().getUrl() + "</MetaDataURL>\n";
        tempResponse = tempResponse + "    </FeatureType>\n";
        return tempResponse;
    }
    
     /**
      * Generates v0.0.15 capabilities document fragment for a feature type.
      *
      */ 
    private String getCapabilitiesXmlv15() {
        // SHOULD CHANGE TO STRING BUFFER
        // ALSO MAKE TERSE VERSION CAPABILITY
        String tempResponse = "        <wfsfl:FeatureType>\n";
        tempResponse = tempResponse + "            <wfsfl:Name>" + 
            responseFeatureType.getName() + "</wfsfl:Name>\n";
        tempResponse = tempResponse + 
            "            <wfsfl:SRS srsName=\""+
            "http://www.opengis.net/gml/srs/epsg#" + 
            responseFeatureType.getSRS() + "\"/>\n";
        tempResponse = tempResponse + 
            "            <wfsfl:LatLonBoundingBox minx=\"" + 
            responseFeatureType.getLatLonBoundingBox().getMinx();
        tempResponse = tempResponse + "\" miny=\"" + 
            responseFeatureType.getLatLonBoundingBox().getMiny();
        tempResponse = tempResponse + "\" maxx=\"" + 
            responseFeatureType.getLatLonBoundingBox().getMaxx();
        tempResponse = tempResponse + "\" maxy=\"" + 
            responseFeatureType.getLatLonBoundingBox().getMaxy() + 
            "\"/>\n";
        tempResponse = tempResponse + 
            "            <wfsfl:Operations><wfsfl:Query/></wfsfl:"
            + "Operations>\n";
        tempResponse = tempResponse + 
            "        </wfsfl:FeatureType>\n";                
        return tempResponse;
    }
}
