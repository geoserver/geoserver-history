/*
 */

package org.geoserver.catalog.hibernate.beans;

/**
 *
 * @author ETj <etj at geo-solutions.it>
 */
public class GroupedLayerHb {

    private Long id = null;
    private Integer version = null;
    private LayerInfoImplHb layer = null;
    private StyleInfoImplHb style = null;

    public GroupedLayerHb(LayerInfoImplHb layer, StyleInfoImplHb style) {
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

    public StyleInfoImplHb getStyle() {
        return style;
    }

    public void setStyle(StyleInfoImplHb style) {
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
