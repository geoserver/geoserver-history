/* Copyright (c) 2001 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root 
 * application directory.
 */
package org.vfny.geoserver.config;

import java.io.*;
import java.util.*;
import java.util.logging.Logger;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;
import org.vfny.geoserver.config.featureType.FeatureType;

/**
 * Reads all necessary feature type information to abstract away from servlets.
 * 
 * @author Rob Hranac, TOPP
 * @version $VERSION$
 */
public class TypeInfo {
        
    /** Class logger */
    private static Logger LOG = Logger.getLogger("org.vfny.geoserver.config");

    /** Castor-specified type to hold all the  */
    private FeatureType internalType;

    
    /** Initializes the database and request handler. */ 
    public TypeInfo() {}
    

    /**
     * Initializes the database and request handler.
     * @param featureTypeName The query from the request object.
     */ 
    public TypeInfo(String typeName) {
        readTypeInfo(typeName);
    }
    
    /** Fetches the feature type name (also the table name)  */
    public String getName() { return internalType.getName(); }
    
    /** Fetches the feature type abstract */
    public String getAbstract() { return internalType.getAbstract(); }
    
    /** Fetches the feature type spatial reference system */
    public String getSrs() { return internalType.getSRS(); }
    
    /** Fetches the user-defined feature type keywords  */
    public String getKeywords() { return internalType.getKeywords(); }

    /** Fetches the user-defined bounding box  */
    public String getBoundingBox() { 
        return internalType.getLatLonBoundingBox().toString();
    }
    
    /** Fetches the user-defined metadata URL  */
    public String getMetadataUrl() { 
        return internalType.getMetadataURL().toString();
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
        if(version.equals("0.0.14")) {
            return getCapabilitiesXmlv14();
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
            FileReader featureTypeDocument = 
                new FileReader(typeName);
            internalType = 
                (FeatureType) Unmarshaller.unmarshal(FeatureType.class, 
                                                     featureTypeDocument);
        }
        catch( FileNotFoundException e ) {
            LOG.info("Feature type file does not exist: "+typeName);
        }
        catch( MarshalException e ) {
            LOG.info("Castor could not unmarshal feature type file: " + 
                      typeName);
            LOG.info("Castor says: " + e.toString() );
        }
        catch( ValidationException e ) {
            LOG.info("Castor says: feature type XML not valid : "
                     + typeName);
            LOG.info("Castor says: " + e.toString() );
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
            internalType.getName() + "</Name>\n";
        tempResponse = tempResponse + "      <Title>" + 
            internalType.getTitle() + "</Title>\n";
        tempResponse = tempResponse + "      <Abstract>" + 
            internalType.getAbstract() + "</Abstract>\n";
        tempResponse = tempResponse + "      <Keywords>" + 
            internalType.getKeywords() + "</Keywords>\n";
        tempResponse = tempResponse + 
            "      <SRS>http://www.opengis.net/gml/srs/epsg#" + 
            internalType.getSRS() + "</SRS>\n";
        tempResponse = tempResponse + "      <Operations>\n";
        tempResponse = tempResponse + "        <Query/>\n";
        tempResponse = tempResponse + "      </Operations>\n";
        tempResponse = tempResponse + 
            "      <LatLonBoundingBox minx=\"" + 
            internalType.getLatLonBoundingBox().getMinx() + 
            "\" ";
        tempResponse = tempResponse + "miny=\"" + 
            internalType.getLatLonBoundingBox().getMiny() + 
            "\" ";
        tempResponse = tempResponse + "maxx=\"" + 
                    internalType.getLatLonBoundingBox().getMaxx() + 
            "\" ";
        tempResponse = tempResponse + "maxy=\"" + 
            internalType.getLatLonBoundingBox().getMaxy() + 
            "\"/>\n";
                //tempResponse = tempResponse + "      <MetaDataURL";
                //tempResponse = tempResponse + " type=\"" + internalType.getMetadataURL().getType();
                //tempResponse = tempResponse + "\" format=\"" + internalType.getMetadataURL().getFormat();
                //tempResponse = tempResponse + "\">" + internalType.getMetadataURL().getUrl() + "</MetaDataURL>\n";
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
            internalType.getName() + "</wfsfl:Name>\n";
        tempResponse = tempResponse + 
            "            <wfsfl:SRS srsName=\""+
            "http://www.opengis.net/gml/srs/epsg#" + 
            internalType.getSRS() + "\"/>\n";
        tempResponse = tempResponse + 
            "            <wfsfl:LatLonBoundingBox minx=\"" + 
            internalType.getLatLonBoundingBox().getMinx();
        tempResponse = tempResponse + "\" miny=\"" + 
            internalType.getLatLonBoundingBox().getMiny();
        tempResponse = tempResponse + "\" maxx=\"" + 
            internalType.getLatLonBoundingBox().getMaxx();
        tempResponse = tempResponse + "\" maxy=\"" + 
            internalType.getLatLonBoundingBox().getMaxy() + 
            "\"/>\n";
        tempResponse = tempResponse + 
            "            <wfsfl:Operations><wfsfl:Query/></wfsfl:"
            + "Operations>\n";
        tempResponse = tempResponse + 
            "        </wfsfl:FeatureType>\n";                
        return tempResponse;
    }
}
