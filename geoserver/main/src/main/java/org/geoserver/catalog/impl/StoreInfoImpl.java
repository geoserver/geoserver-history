/* Copyright (c) 2001 - 2008 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.catalog.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.geoserver.catalog.Catalog;
import org.geoserver.catalog.StoreInfo;
import org.geoserver.catalog.WorkspaceInfo;
import org.opengis.util.ProgressListener;

/**
 * Default implementation of {@link StoreInfo}.
 * 
 */
public abstract class StoreInfoImpl implements StoreInfo {

    String id;

    String name;

    String description;

    boolean enabled;

    WorkspaceInfo workspace;
    
    Catalog catalog;

    Map connectionParameters = new HashMap();

    Map metadata = new HashMap();
    
    Throwable error;

    protected StoreInfoImpl(Catalog catalog) {
        this.catalog = catalog;
    }

    protected StoreInfoImpl(Catalog catalog, String id) {
        this(catalog);
        setId(id);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    
    public Catalog getCatalog() {
        return catalog;
    }

    public void setCatalog( Catalog catalog ) {
        this.catalog = catalog;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public WorkspaceInfo getWorkspace() {
        return workspace;
    }
    
    public void setWorkspace(WorkspaceInfo workspace) {
        this.workspace = workspace;
    }
    
    public Map getConnectionParameters() {
        return connectionParameters;
    }

    public void setConnectionParameters(Map connectionParameters) {
        this.connectionParameters = connectionParameters;
    }

    public Map getMetadata() {
        return metadata;
    }

    public void setMetadata(Map metadata) {
        this.metadata = metadata;
    }

    public Object getAdapter(Class adapterClass, Map hints) {
        // subclasses should override
        return null;
    }

    public Iterator getResources(ProgressListener monitor) throws IOException {
        // subclasses should override
        return null;
    }

    public String toString() {
        return name;
    }

    public Throwable getError() {
        return error;
    }
    
    public void setError(Throwable error) {
        this.error = error;
    }
    
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime
                * result
                + ((connectionParameters == null) ? 0 : connectionParameters
                        .hashCode());
        result = prime * result
                + ((description == null) ? 0 : description.hashCode());
        result = prime * result + (enabled ? 1231 : 1237);
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result
                + ((workspace == null) ? 0 : workspace.hashCode());
        return result;
    }

    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!( obj instanceof StoreInfo ) ) {
            return false;
        }
        
        final StoreInfo other = (StoreInfo) obj;
        if (connectionParameters == null) {
            if (other.getConnectionParameters() != null)
                return false;
        } else if (!connectionParameters.equals(other.getConnectionParameters()))
            return false;
        if (description == null) {
            if (other.getDescription() != null)
                return false;
        } else if (!description.equals(other.getDescription()))
            return false;
        if (enabled != other.isEnabled())
            return false;
        if (id == null) {
            if (other.getId() != null)
                return false;
        } else if (!id.equals(other.getId()))
            return false;
        if (name == null) {
            if (other.getName() != null)
                return false;
        } else if (!name.equals(other.getName()))
            return false;
        if (workspace == null) {
            if (other.getWorkspace() != null)
                return false;
        } else if (!workspace.equals(other.getWorkspace()))
            return false;
        return true;
    }
}
