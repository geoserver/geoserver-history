/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.security;

import java.util.HashSet;
import java.util.Set;

/**
 * Represents a data access rule: identifies a workspace, a layer, an access mode, and the set of roles
 * that are allowed to access it
 */
public class DataAccessRule {

    /**
     * Any layer, or any workspace, or any role
     */
    public static final String ANY = "*";

    String workspace;

    String layer;

    AccessMode accessMode;

    Set<String> roles;

    public DataAccessRule(String workspace, String layer, AccessMode accessMode, Set<String> roles) {
        super();
        this.workspace = workspace;
        this.layer = layer;
        this.accessMode = accessMode;
        if (roles == null)
            this.roles = new HashSet<String>();
        else
            this.roles = new HashSet<String>(roles);
    }

    public String getWorkspace() {
        return workspace;
    }

    public void setWorkspace(String workspace) {
        this.workspace = workspace;
    }

    public String getLayer() {
        return layer;
    }

    public void setLayer(String layer) {
        this.layer = layer;
    }

    public AccessMode getAccessMode() {
        return accessMode;
    }

    public void setAccessMode(AccessMode accessMode) {
        this.accessMode = accessMode;
    }

    public Set<String> getRoles() {
        return roles;
    }

}
