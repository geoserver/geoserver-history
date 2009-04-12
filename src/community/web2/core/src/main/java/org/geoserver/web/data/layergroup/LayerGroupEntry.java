/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.web.data.layergroup;

import java.io.Serializable;

import org.geoserver.catalog.LayerInfo;
import org.geoserver.catalog.StyleInfo;
import org.geoserver.web.GeoServerApplication;

/**
 * Represents one layer in the layer group
 */
@SuppressWarnings("serial")
public class LayerGroupEntry implements Serializable {

    String sid;
    String lid;
    
    public LayerGroupEntry( LayerInfo layer, StyleInfo style ) {
        this.sid = style.getId();
        this.lid = layer.getId();
    }
    
    public StyleInfo getStyle() {
        return GeoServerApplication.get().getCatalog().getStyle( sid );
    }
    public void setStyle( StyleInfo style ) {
        sid = style.getId();
    }
    
    public LayerInfo getLayer() {
        return GeoServerApplication.get().getCatalog().getLayer( lid );
    }
    
    public void setLayer( LayerInfo layer ) {
        lid = layer.getId();
    }
    
    /*
    public LoadableDetachableModel toDetachableModel() {
        return new LayerGroupEntryModel( this );
    }
    
    
    public static class LayerGroupEntryModel extends LoadableDetachableModel {

        String lid;
        String sid;
        int index;
        
        public LayerGroupEntryModel( LayerGroupEntry entry ) {
            lid = entry.layer.getId();
            sid = entry.style.getId();
            index = entry.index;
        }
        
        @Override
        protected Object load() {
            Catalog catalog = GeoServerApplication.get().getCatalog();
            LayerInfo l = catalog.getLayer( lid );
            StyleInfo s = catalog.getStyle( sid );
            return new LayerGroupEntry( l, s, index );
        }
        
    }
    */
}
