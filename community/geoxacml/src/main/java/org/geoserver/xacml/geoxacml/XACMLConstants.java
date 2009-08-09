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
    public final static String GeoServerRole=RoleIdPrefix+"ROLE_GEOSERVER";
    public static URI GeoServerRoleURI;
    public final static String AdminRole=RoleIdPrefix+"ROLE_ADMINISTRATOR";
    public static URI AdminRoleURI;
    public final static String AnonymousRole=RoleIdPrefix+"ROLE_ANONYMOUS";
    public static URI AnonymousRoleURI;

    /*
     * Some common resouce names 
     */    
    public static String CatalogResouceName = "Catalog";
    
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
            
            GeoServerRoleURI = new URI(GeoServerRole);
            AdminRoleURI = new URI(AdminRole);
            AnonymousRoleURI = new URI(AnonymousRole);
        } catch (URISyntaxException e) {
            // should not happen
        }
    }
    
    
}
