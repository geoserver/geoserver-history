/* Copyright (c) 2001 - 2008 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.catalog.impl;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.geoserver.catalog.CatalogVisitor;
import org.geoserver.catalog.MetadataMap;
import org.geoserver.catalog.NamespaceInfo;

public class NamespaceInfoImpl implements NamespaceInfo {

    String id;

    String prefix;

    String uri;

    MetadataMap metadata = new MetadataMap();
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getName() {
        return getPrefix();
    }
    
    public String getURI() {
        return uri;
    }

    public void setURI(String uri) {
        this.uri = uri;
    }

    public MetadataMap getMetadata() {
        return metadata;
    }
    
    public void setMetadata(MetadataMap metadata) {
        this.metadata = metadata;
    }

    public void accept(CatalogVisitor visitor) {
        visitor.visit( this );
    }
    
    @Override
    public String toString() {
        return new StringBuilder(getClass().getSimpleName()).append('[').append(prefix).append(':')
                .append(uri).append(']').toString();
    }

    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((prefix == null) ? 0 : prefix.hashCode());
        result = prime * result + ((uri == null) ? 0 : uri.hashCode());
        return result;
    }

    public boolean equals(Object obj) {
        if ( obj == null ) {
            return false;
        }
        if ( !( obj instanceof NamespaceInfo ) ) {
            return false;
        }
        
        final NamespaceInfo other = (NamespaceInfo) obj;
        if (prefix == null) {
            if (other.getPrefix() != null)
                return false;
        } else if (!prefix.equals(other.getPrefix()))
            return false;
        if (uri == null) {
            if (other.getURI() != null)
                return false;
        } else if (!uri.equals(other.getURI()))
            return false;
        
        return true;
    }

}
