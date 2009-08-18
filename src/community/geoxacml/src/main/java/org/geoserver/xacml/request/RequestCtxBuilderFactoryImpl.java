/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */

package org.geoserver.xacml.request;

import org.geoserver.catalog.ResourceInfo;
import org.geoserver.catalog.WorkspaceInfo;
import org.geoserver.security.AccessMode;
import org.geoserver.xacml.role.XACMLRole;

/**
 * Default implementation for {@link RequestCtxBuilderFactory}
 * 
 * @author Christian Mueller
 *
 */
public class RequestCtxBuilderFactoryImpl implements RequestCtxBuilderFactory {

    public RequestCtxBuilder getCatalogRequestCtxBuilder() {
        return new CatalogRequestCtxBuilder();
    }
    public RequestCtxBuilder getXACMLRoleRequestCtxBuilder(XACMLRole targetRole) {
        return new XACMLRoleRequestCtxBuilder(targetRole);
    }
    public RequestCtxBuilder getWorkspaceRequestCtxBuilder(XACMLRole role, WorkspaceInfo info, AccessMode mode) {
        return new WorkspaceRequestCtxBuilder(role,info,mode);
    }
    public RequestCtxBuilder getURLMatchRequestCtxBuilder(XACMLRole role, String urlString, String action) {
        return new URLMatchRequestCtxBuilder(role,urlString,action);
    }
    public RequestCtxBuilder getResourceInfoRequestCtxBuilder(XACMLRole role, ResourceInfo resourceInfo, AccessMode mode) {
        return new ResourceInfoRequestCtxBuilder(role, resourceInfo, mode); 
    }
    
}
