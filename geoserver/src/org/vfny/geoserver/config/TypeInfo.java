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
	LOG.finest("reading typeinfo for " + typeName);
        readTypeInfo(typeName);
	
    }
    
    /** Fetches the feature type name (also the table name)  */
    public String getName() { 
	return (internalType == null) ? null : internalType.getName(); 
    }
    
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
            FileReader featureTypeDocument = 
                new FileReader(typeName);
            internalType = 
                (FeatureType) Unmarshaller.unmarshal(FeatureType.class, 
                                                     featureTypeDocument);
	    LOG.finer("finished reading type " + typeName + "internal is " + internalType);
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
    private String getCapabilitiesXmlv14(String version) {
	// MAKE TERSE VERSION CAPABILITY
        StringBuffer tempResponse = new StringBuffer("    <FeatureType>\n");
        String name = internalType.getName();
	if (!version.startsWith("0.0.1")) {
	    //REVISIT: get this elsewhere?  Make sure that myns is
	    //declared in the capabilities document returned.
	    //name = "myns:" + name;
	}
	tempResponse.append("      <Name>" + name + "</Name>\n");
        tempResponse.append("      <Title>" + 
            internalType.getTitle() + "</Title>\n");
        tempResponse.append("      <Abstract>" + 
            internalType.getAbstract() + "</Abstract>\n");
        tempResponse.append("      <Keywords>" + 
            internalType.getKeywords() + "</Keywords>\n");
        tempResponse.append(
            "      <SRS>http://www.opengis.net/gml/srs/epsg#" + 
            internalType.getSRS() + "</SRS>\n");
	//TODO: Should we allow the admin to customize these?  He may
	//not want to publicize to the world that it is transactional.
        //but if we just use the internalType marshalling way then the
	//admin could easily mess up the xml, putting the wrong terms in.
        tempResponse.append("      <Operations>\n");
        tempResponse.append("        <Query/>\n");
	tempResponse.append("        <Insert/>\n");
	tempResponse.append("        <Update/>\n");
	tempResponse.append("        <Delete/>\n");
        tempResponse.append("      </Operations>\n");
        tempResponse.append(
            "      <LatLonBoundingBox minx=\"" + 
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
