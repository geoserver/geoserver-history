/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.xacml.geoxacml;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Class holding some needed XACML Constants
 * 
 * @author Christian Mueller
 *
 */
public class XACMLConstants {
    
    public final static String GeoServerPrefix = "org:geoserver:" ;
    
    public final static String ActionAttributeId= "urn:oasis:names:tc:xacml:1.0:action:action-id";
    public static URI ActionAttributeURI;
    public final static String ResourceAttributeId= "urn:oasis:names:tc:xacml:1.0:resource:resource-id";
    public static URI ResourceAttributeURI;
    public final static String ResourceTypeId= GeoServerPrefix+"resource:type";
    public static URI ResourceTypeURI; 
    
//    Only needed if we would use roles without role attributes
//    public final static String RoleAttributeId= "urn:oasis:names:tc:xacml:2.0:subject:role";
//    public static URI RoleAttributeURI;

    
    public final static String RoleIdPrefix=GeoServerPrefix+"role:";
    public final static String RoleParamIdInfix=":param:";
    public final static String ObligationPrefix=GeoServerPrefix+"obligation:";
    
    /*
     * Predefined Role definitions
     */
    // role for geoserver itself
    public final static String GeoServerRole="ROLE_GEOSERVER";
    public final static String AdminRole="ROLE_ADMINISTRATOR";
    public final static String AnonymousRole="ROLE_ANONYMOUS";

    /*
     * Some common resouce names 
     */    
    public static String CatalogResouceName = "Catalog";
    
    /*
     * Some common resouce type names 
     */    
    public static String CatalogResourceType="CatalogType";
    public static String URLResourceType="URL";
    public static String WorkSpaceResourceType="Workspace";
    public static String LayerResourceType="Layer";
    public static String ResourceResourceType="Resource";
    
    
    /*
     * Some common obligation Ids 
     */
    public final static String CatalogModeObligationId=ObligationPrefix+"CatalogMode";
    
    /*
     * 
     * Creating URI Objects from string constants as needed
     */
    
    static {
        try {
            ActionAttributeURI = new URI(ActionAttributeId);
            ResourceAttributeURI = new URI(ResourceAttributeId);
            ResourceTypeURI = new URI(ResourceTypeId);
            // RoleAttributeURI = new URI(RoleAttributeId);
            
        } catch (URISyntaxException e) {
            // should not happen
        }
    }
    
    
}
