package org.geoserver.catalog.impl;

import java.io.Serializable;
import java.util.Map;

import org.geoserver.catalog.Catalog;
import org.geoserver.catalog.WorkspaceInfo;

public class WorkspaceInfoImpl implements WorkspaceInfo {

    Catalog catalog;
    
    String id;
    String name;
    
    Map<String,Serializable> metadata;
    
    public WorkspaceInfoImpl( Catalog catalog ) {
        this.catalog = catalog;
    }
    
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public Map<String, Serializable> getMetadata() {
        return metadata;
    }

    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof WorkspaceInfo))
            return false;
        
        final WorkspaceInfo other = (WorkspaceInfo) obj;
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
        return true;
    }
}
