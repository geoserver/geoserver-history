/*
 */

package org.geoserver.catalog.hibernate.beans;

import java.io.Serializable;

import org.geoserver.catalog.impl.StyleInfoImpl;

/**
 *
 * @author ETj <etj at geo-solutions.it>
 */
public class GroupedLayerHb implements Serializable {

    private Long id = null;
    private Integer version = null;
    private LayerInfoImplHb layer = null;
    private StyleInfoImpl style = null;

    public GroupedLayerHb(LayerInfoImplHb layer, StyleInfoImpl style) {
        this.layer = layer;
        this.style = style;
    }

    public GroupedLayerHb() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    public LayerInfoImplHb getLayer() {
        return layer;
    }

    public void setLayer(LayerInfoImplHb layer) {
        this.layer = layer;
    }

    public StyleInfoImpl getStyle() {
        return style;
    }

    public void setStyle(StyleInfoImpl style) {
        this.style = style;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(getClass().getSimpleName()).append('[')
                .append("id:").append(getId());
        if(layer != null)
            sb.append(" l[").append(layer.getId()).append(' ').append(layer.getName()).append(']');
        if(style != null)
            sb.append(" s[").append(style.getId()).append(' ').append(style.getName()).append(']');
        sb.append(']');

        return sb.toString();
    }

}
