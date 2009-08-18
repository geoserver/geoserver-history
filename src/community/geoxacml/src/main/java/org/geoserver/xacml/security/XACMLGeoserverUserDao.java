/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */

package org.geoserver.xacml.security;

import org.acegisecurity.GrantedAuthority;
import org.acegisecurity.userdetails.User;
import org.geoserver.security.GeoserverUserDao;
import org.geoserver.xacml.geoxacml.GeoXACMLConfig;
import org.geoserver.xacml.role.XACMLRole;

public class XACMLGeoserverUserDao extends GeoserverUserDao {

    @Override
    protected User createUserObject(String username, String password, boolean isEnabled,
            GrantedAuthority[] authorities) {
        User temporary = super.createUserObject(username, password, isEnabled, authorities);
        XACMLRole[] xacmlAuthorities = GeoXACMLConfig.getXACMLRoleAuthority().getXACMLRolesFor(temporary, temporary.getAuthorities());
        return new User(username,password,isEnabled,true,true,true,xacmlAuthorities);        
    }

}
