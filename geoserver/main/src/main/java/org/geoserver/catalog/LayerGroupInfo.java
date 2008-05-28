package org.geoserver.catalog;

import java.util.List;

import org.geotools.geometry.jts.ReferencedEnvelope;

/**
 * A map in which the layers grouped together can be referenced as 
 * a regular layer.
 * 
 * @author Justin Deoliveira, The Open Planning Project
 *
 */
public interface LayerGroupInfo {

    String getId();
    
    /**
     * The name of the layer group.
     */
    String getName();

    /**
     * Sets the name of the layer group.
     */
    void setName( String name );
    
    /**
     * The layers in the group.
     */
    List<LayerInfo> getLayers();
    
    /**
     * The styles for the layers in the group.
     * <p>
     * This list is a 1-1 correspondence to {@link #getLayers()}.
     * </p>
     */
    List<StyleInfo> getStyles();
    
    /**
     * The bounds for the base map.
     */
    ReferencedEnvelope getBounds();

    /**
     * Sets the bounds for the base map.
     */
    void setBounds( ReferencedEnvelope bounds );
}
