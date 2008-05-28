package org.geoserver.catalog.impl;

import java.util.ArrayList;
import java.util.List;

import org.geoserver.catalog.LayerGroupInfo;
import org.geoserver.catalog.LayerInfo;
import org.geoserver.catalog.StyleInfo;
import org.geotools.geometry.jts.ReferencedEnvelope;

public class LayerGroupInfoImpl implements LayerGroupInfo {

    String id;
    String name;
    String path;
    List<LayerInfo> layers = new ArrayList<LayerInfo>();
    List<StyleInfo> styles = new ArrayList<StyleInfo>();
    ReferencedEnvelope bounds;
  
    public LayerGroupInfoImpl() {
    }
    
    public String getId() {
        return id;
    }
    
    public void setId( String id ) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    
    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
    
    public List<LayerInfo> getLayers() {
        return layers;
    }
    
    public List<StyleInfo> getStyles() {
        return styles;
    }
    
    public ReferencedEnvelope getBounds() {
        return bounds;
    }
    
    public void setBounds(ReferencedEnvelope bounds) {
        this.bounds = bounds;
    }
    
}
