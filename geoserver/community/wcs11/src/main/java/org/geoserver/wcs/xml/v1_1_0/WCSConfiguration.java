package org.geoserver.wcs.xml.v1_1_0;

import org.eclipse.xsd.util.XSDSchemaLocationResolver;	
import org.geotools.xml.BindingConfiguration;
import org.geotools.xml.Configuration;

/**
 * Parser configuration for the http://www.opengis.net/wcs/1.1 schema.
 *
 * @generated
 */
public class WCSConfiguration extends Configuration {

    /**
     * Creates a new configuration.
     * 
     * @generated
     */     
    public WCSConfiguration() {
       super();
       
       //TODO: add dependencies here
    }
    
    /**
     * @return the schema namespace uri: http://www.opengis.net/wcs/1.1.
     * @generated
     */
    public String getNamespaceURI() {
    	return WCS.NAMESPACE;
    }
    
    /**
     * @return the uri to the the wcsAll.xsd .
     * @generated
     */
    public String getSchemaFileURL() {
        return getSchemaLocationResolver().resolveSchemaLocation( 
           null, getNamespaceURI(), "wcsAll.xsd"
        );
    }
    
    /**
     * @return new instanceof {@link WCSBindingConfiguration}.
     */    
    public BindingConfiguration getBindingConfiguration() {
     	return new WCSBindingConfiguration();
    }
} 