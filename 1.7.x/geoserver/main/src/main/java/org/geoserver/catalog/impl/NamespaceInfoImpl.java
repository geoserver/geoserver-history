/* Copyright (c) 2001 - 2008 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.catalog.impl;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.geoserver.catalog.Catalog;
import org.geoserver.catalog.NamespaceInfo;
import org.geoserver.catalog.ResourceInfo;

public class NamespaceInfoImpl implements NamespaceInfo {

    String id;

    String prefix;

    String uri;

    Set resources = new HashSet();

    Catalog catalog;
    
    HashMap<String,Serializable> metadata;
    
    public NamespaceInfoImpl(Catalog catalog) {
        this.catalog = catalog;
    }

    public NamespaceInfoImpl(Catalog catalog,String id) {
        this(catalog);
        this.id = id;
    }

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

    public String getURI() {
        return uri;
    }

    public void setURI(String uri) {
        this.uri = uri;
    }

    public Iterator resources() {
        return resources.iterator();
    }

    public ResourceInfo getResource(String name) {
        // TODO: replace this with a hibernate query?
        for (Iterator i = resources(); i.hasNext();) {
            ResourceInfo resource = (ResourceInfo) i.next();
            if (name.equals(resource.getName())) {
                return resource;
            }
        }

        return null;
    }

    public void add(ResourceInfo resource) {
        resource.setNamespace(this);
        resources.add(resource);
    }

    public void remove(ResourceInfo resource) {
        resource.setNamespace(null);
        resources.remove(resources);
    }

    protected Set getResources() {
        return resources;
    }

    protected void setResources(Set resources) {
        this.resources = resources;
    }

    public Map<String, Serializable> getMetadata() {
        return metadata;
    }
    
    public String toString() {
        return prefix;
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
