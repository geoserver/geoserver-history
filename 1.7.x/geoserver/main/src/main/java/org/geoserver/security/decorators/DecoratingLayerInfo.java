package org.geoserver.security.decorators;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

import org.geoserver.catalog.LayerInfo;
import org.geoserver.catalog.LegendInfo;
import org.geoserver.catalog.ResourceInfo;
import org.geoserver.catalog.StyleInfo;
import org.geoserver.catalog.impl.AbstractDecorator;

/**
 * Delegates every method to the wrapped {@link LayerInfo}. Subclasses will
 * override selected methods to perform their "decoration" job
 * 
 * @author Andrea Aime
 */
public class DecoratingLayerInfo extends AbstractDecorator<LayerInfo> implements LayerInfo {

    public DecoratingLayerInfo(LayerInfo delegate) {
        super(delegate);
    }

    public StyleInfo getDefaultStyle() {
        return delegate.getDefaultStyle();
    }

    public String getId() {
        return delegate.getId();
    }

    public LegendInfo getLegend() {
        return delegate.getLegend();
    }

    public Map<String, Serializable> getMetadata() {
        return delegate.getMetadata();
    }

    public String getName() {
        return delegate.getName();
    }

    public String getPath() {
        return delegate.getPath();
    }

    public ResourceInfo getResource() {
        return delegate.getResource();
    }

    public Set<StyleInfo> getStyles() {
        return delegate.getStyles();
    }

    public Type getType() {
        return delegate.getType();
    }

    public boolean isEnabled() {
        return delegate.isEnabled();
    }

    public void setDefaultStyle(StyleInfo defaultStyle) {
        delegate.setDefaultStyle(defaultStyle);
    }

    public void setEnabled(boolean enabled) {
        delegate.setEnabled(enabled);
    }

    public void setLegend(LegendInfo legend) {
        delegate.setLegend(legend);
    }

    public void setName(String name) {
        delegate.setName(name);
    }

    public void setPath(String path) {
        delegate.setPath(path);
    }

    public void setResource(ResourceInfo resource) {
        delegate.setResource(resource);
    }

    public void setType(Type type) {
        delegate.setType(type);
    }
}
