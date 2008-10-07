/* Copyright (c) 2001 - 2008 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.catalog.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.geoserver.catalog.Catalog;
import org.geoserver.catalog.MetadataLinkInfo;
import org.geoserver.catalog.NamespaceInfo;
import org.geoserver.catalog.ProjectionPolicy;
import org.geoserver.catalog.ResourceInfo;
import org.geoserver.catalog.StoreInfo;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * Default implementation of {@link ResourceInfo}.
 * 
 */
public abstract class ResourceInfoImpl implements ResourceInfo {

    String id;

    String name; // FIXME: make this protected or private ... hurts people
                    // extending in other pacakges

    String nativeName;
    
    List<String> alias = new ArrayList<String>();
    
    NamespaceInfo namespace;

    String title;

    String description;

    String _abstract;

    List<String> keywords = new ArrayList<String>();

    List<MetadataLinkInfo> metadataLinks = new ArrayList<MetadataLinkInfo>();

    CoordinateReferenceSystem nativeCRS;
    
    String srs;

    ReferencedEnvelope nativeBoundingBox;

    ReferencedEnvelope latLonBoundingBox;
    
    ProjectionPolicy projectionPolicy;

    boolean enabled;

    Map metadata = new HashMap();

    StoreInfo store;
    
    Catalog catalog;

    protected ResourceInfoImpl(Catalog catalog) {
        this.catalog = catalog;
    }

    protected ResourceInfoImpl(Catalog catalog, String id) {
        this(catalog);
        setId(id);
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
    
    public Catalog getCatalog() {
        return catalog;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNativeName() {
        return nativeName;
    }
    
    public void setNativeName(String nativeName) {
        this.nativeName = nativeName;
    }
    
    public NamespaceInfo getNamespace() {
        return namespace;
    }

    public void setNamespace(NamespaceInfo namespace) {
        this.namespace = namespace;
    }

    public String getPrefixedName() {
        return getNamespace().getPrefix() + ":" + getName();
    }
    
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAbstract() {
        return _abstract;
    }

    public void setAbstract(String _abstract) {
        this._abstract = _abstract;
    }

    public List<String> getKeywords() {
        return keywords;
    }

    public List<MetadataLinkInfo> getMetadataLinks() {
        return metadataLinks;
    }

    public String getSRS() {
        return srs;
    }

    public void setSRS(String srs) {
        this.srs = srs;
    }

    public ReferencedEnvelope getBoundingBox() throws Exception {
      CoordinateReferenceSystem declaredCRS = getCRS();
      CoordinateReferenceSystem nativeCRS = getNativeCRS();
      ProjectionPolicy php = getProjectionPolicy();
      
      if ( !CRS.equalsIgnoreMetadata(declaredCRS, nativeCRS) && 
          php == ProjectionPolicy.REPROJECT_TO_DECLARED ) {
          return nativeBoundingBox.transform(declaredCRS,true); 
      }
      
      return nativeBoundingBox;
    }

    public ReferencedEnvelope getLatLonBoundingBox() {
        return latLonBoundingBox;
    }

    public void setLatLonBoundingBox(ReferencedEnvelope box) {
        this.latLonBoundingBox = box;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Map getMetadata() {
        return metadata;
    }

    public void setMetadata(Map metaData) {
        this.metadata = metaData;
    }

    public StoreInfo getStore() {
        return store;
    }

    public void setStore(StoreInfo store) {
        this.store = store;
    }

    public Object getAdapter(Class adapterClass, Map hints) {
        // subclasses should override
        return null;
    }

    public String toString() {
        return name;
    }

    public List<String> getAlias() {
        return alias;
    }

    public CoordinateReferenceSystem getCRS() throws Exception {
        if ( getSRS() == null ) {
            return null;    
        }
        
        //TODO: cache this
        return CRS.decode( getSRS() );
    }

    public ReferencedEnvelope getNativeBoundingBox() {
        return nativeBoundingBox;
    }
    
    public void setNativeBoundingBox(ReferencedEnvelope box) {
        this.nativeBoundingBox = box;
    }
    
    public CoordinateReferenceSystem getNativeCRS() {
        return nativeCRS;
    }
    
    public void setNativeCRS(CoordinateReferenceSystem nativeCRS) {
        this.nativeCRS = nativeCRS;
    }

    

    public ProjectionPolicy getProjectionPolicy() {
        return projectionPolicy;
    }
    
    public void setProjectionPolicy(ProjectionPolicy projectionPolicy) {
        this.projectionPolicy = projectionPolicy;
    }

    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((_abstract == null) ? 0 : _abstract.hashCode());
        result = prime * result + ((alias == null) ? 0 : alias.hashCode());
        result = prime * result
                + ((description == null) ? 0 : description.hashCode());
        result = prime * result + (enabled ? 1231 : 1237);
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result
                + ((keywords == null) ? 0 : keywords.hashCode());
        result = prime
                * result
                + ((latLonBoundingBox == null) ? 0 : latLonBoundingBox
                        .hashCode());
        result = prime * result
                + ((metadataLinks == null) ? 0 : metadataLinks.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result
                + ((namespace == null) ? 0 : namespace.hashCode());
        result = prime
                * result
                + ((nativeBoundingBox == null) ? 0 : nativeBoundingBox
                        .hashCode());
        result = prime * result
                + ((nativeCRS == null) ? 0 : nativeCRS.hashCode());
        result = prime * result
                + ((nativeName == null) ? 0 : nativeName.hashCode());
        result = prime
                * result
                + ((projectionPolicy == null) ? 0 : projectionPolicy.hashCode());
        result = prime * result + ((srs == null) ? 0 : srs.hashCode());
        result = prime * result + ((store == null) ? 0 : store.hashCode());
        result = prime * result + ((title == null) ? 0 : title.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!( obj instanceof ResourceInfo) ) 
            return false;
        
        final ResourceInfo other = (ResourceInfo) obj;
        if (_abstract == null) {
            if (other.getAbstract() != null)
                return false;
        } else if (!_abstract.equals(other.getAbstract()))
            return false;
        if (alias == null) {
            if (other.getAlias() != null)
                return false;
        } else if (!alias.equals(other.getAlias()))
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
        if (keywords == null) {
            if (other.getKeywords() != null)
                return false;
        } else if (!keywords.equals(other.getKeywords()))
            return false;
        if (latLonBoundingBox == null) {
            if (other.getLatLonBoundingBox() != null)
                return false;
        } else if (!latLonBoundingBox.equals(other.getLatLonBoundingBox()))
            return false;
        if (metadataLinks == null) {
            if (other.getMetadataLinks() != null)
                return false;
        } else if (!metadataLinks.equals(other.getMetadataLinks()))
            return false;
        if (name == null) {
            if (other.getName() != null)
                return false;
        } else if (!name.equals(other.getName()))
            return false;
        if (namespace == null) {
            if (other.getNamespace() != null)
                return false;
        } else if (!namespace.equals(other.getNamespace()))
            return false;
        if (nativeBoundingBox == null) {
            if (other.getNativeBoundingBox() != null)
                return false;
        } else if (!nativeBoundingBox.equals(other.getNativeBoundingBox()))
            return false;
        if (nativeCRS == null) {
            if (other.getNativeCRS() != null)
                return false;
        } else if (CRS.equalsIgnoreMetadata(nativeCRS, other.getNativeCRS()))
            return false;
        if (nativeName == null) {
            if (other.getNativeName() != null)
                return false;
        } else if (!nativeName.equals(other.getNativeName()))
            return false;
        if (projectionPolicy == null) {
            if (other.getProjectionPolicy() != null)
                return false;
        } else if (!projectionPolicy.equals(other.getProjectionPolicy()))
            return false;
        if (srs == null) {
            if (other.getSRS() != null)
                return false;
        } else if (!srs.equals(other.getSRS() ))
            return false;
        if (store == null) {
            if (other.getStore() != null)
                return false;
        } else if (!store.equals(other.getStore()))
            return false;
        if (title == null) {
            if (other.getTitle() != null)
                return false;
        } else if (!title.equals(other.getTitle()))
            return false;
        return true;
    }

    /**
     * @param keywords the keywords to set
     */
    public void setKeywords(List<String> keywords) {
        this.keywords = keywords;
    }

    /**
     * @param metadataLinks the metadataLinks to set
     */
    public void setMetadataLinks(List<MetadataLinkInfo> metadataLinks) {
        this.metadataLinks = metadataLinks;
    }
}
