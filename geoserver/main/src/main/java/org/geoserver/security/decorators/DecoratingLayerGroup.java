package org.geoserver.security.decorators;

import java.util.List;

import org.geoserver.catalog.LayerGroupInfo;
import org.geoserver.catalog.LayerInfo;
import org.geoserver.catalog.StyleInfo;
import org.geotools.geometry.jts.ReferencedEnvelope;

/**
 * Delegates every method to the wrapped {@link LayerGroupInfo}. Subclasses will
 * override selected methods to perform their "decoration" job
 * 
 * @author Andrea Aime
 */
public class DecoratingLayerGroup implements LayerGroupInfo {
    LayerGroupInfo delegate;

    public DecoratingLayerGroup(LayerGroupInfo delegate) {
        this.delegate = delegate;
    }

    public ReferencedEnvelope getBounds() {
        return delegate.getBounds();
    }

    public String getId() {
        return delegate.getId();
    }

    public List<LayerInfo> getLayers() {
        return delegate.getLayers();
    }

    public String getName() {
        return delegate.getName();
    }

    public List<StyleInfo> getStyles() {
        return delegate.getStyles();
    }

    public void setBounds(ReferencedEnvelope bounds) {
        delegate.setBounds(bounds);
    }

    public void setName(String name) {
        delegate.setName(name);
    }

}
