/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.security;

/**
 * Access limits to a workspace (the write flag controls also direct access to data stores, though
 * normally only configuration code should be playing directy with stores)
 * @author Andrea Aime - GeoSolutions
 *
 */
public class WorkspaceAccessLimits extends AccessLimits {

    boolean readable;

    boolean writable;

    public WorkspaceAccessLimits(CatalogMode mode, boolean readable, boolean writable) {
        super(mode);
        this.readable = readable;
        this.writable = writable;
    }

    public boolean isReadable() {
        return readable;
    }

    public boolean isWritable() {
        return writable;
    }

    @Override
    public String toString() {
        return "WorkspaceAccessLimits [readable=" + readable + ", writable=" + writable + ", mode="
                + mode + "]";
    }
    
}
