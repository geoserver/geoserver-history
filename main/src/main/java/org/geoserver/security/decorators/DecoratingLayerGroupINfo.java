package org.geoserver.security.decorators;

import java.util.List;

import org.geoserver.catalog.LayerGroupInfo;
import org.geoserver.catalog.LayerInfo;
import org.geoserver.catalog.StyleInfo;
import org.geoserver.catalog.Wrapper;
import org.geotools.geometry.jts.ReferencedEnvelope;

/**
 * Delegates every method to the wrapped {@link LayerGroupInfo}. Subclasses will
 * override selected methods to perform their "decoration" job
 * 
 * @author Andrea Aime
 */
public class DecoratingLayerGroupINfo implements LayerGroupInfo, Wrapper<LayerGroupInfo> {
    LayerGroupInfo delegate;

    public DecoratingLayerGroupINfo(LayerGroupInfo delegate) {
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

    public boolean isWrapperFor(Class<?> iface) {
        return LayerGroupInfo.class.isAssignableFrom(iface);
    }

    public LayerGroupInfo unwrap() {
        return delegate;
    }

}
