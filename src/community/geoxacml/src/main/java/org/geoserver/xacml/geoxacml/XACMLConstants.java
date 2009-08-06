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
    public final static String ActionAttributeId= "urn:oasis:names:tc:xacml:1.0:action:action-id";
    public static URI ActionAttributeURI;
    public final static String ResourceAttributeId= "urn:oasis:names:tc:xacml:1.0:resource:resource-id";
    public static URI ResourceAttributeURI;
    public final static String ResourceTypeId= "org:geoserver:resource:type";
    public static URI ResourceTypeURI;
    
    public final static String RoleAttributeId= "urn:oasis:names:tc:xacml:2.0:subject:role";
    public static URI RoleAttributeURI;
    
    
    static {
        try {
            ActionAttributeURI = new URI(ActionAttributeId);
            ResourceAttributeURI = new URI(ResourceAttributeId);
            ResourceTypeURI = new URI(ResourceTypeId);
            RoleAttributeURI = new URI(RoleAttributeId);
        } catch (URISyntaxException e) {
            // should not happen
        }
    }
    
    public final static String ResourceTypeOperation="Operation";
    
}
