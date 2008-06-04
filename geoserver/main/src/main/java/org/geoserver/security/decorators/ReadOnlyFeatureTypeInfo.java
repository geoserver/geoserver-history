/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.security.decorators;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.geoserver.catalog.AttributeTypeInfo;
import org.geoserver.catalog.Catalog;
import org.geoserver.catalog.DataStoreInfo;
import org.geoserver.catalog.FeatureTypeInfo;
import org.geoserver.catalog.MetadataLinkInfo;
import org.geoserver.catalog.NamespaceInfo;
import org.geoserver.catalog.ProjectionPolicy;
import org.geoserver.catalog.StoreInfo;
import org.geoserver.catalog.Wrapper;
import org.geotools.data.FeatureSource;
import org.geotools.factory.Hints;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.util.ProgressListener;

/**
 * Wraps a {@link FeatureTypeInfo} so that it will return a read only
 * FeatureSource
 * 
 * @author Andrea Aime - TOPP
 */
public class ReadOnlyFeatureTypeInfo implements FeatureTypeInfo, Wrapper<FeatureTypeInfo> {
    FeatureTypeInfo wrapped;

    public ReadOnlyFeatureTypeInfo(FeatureTypeInfo info) {
        this.wrapped = info;
    }

    public boolean isWrapperFor(Class<?> iface) {
        return FeatureTypeInfo.class.equals(iface);
    }

    public FeatureTypeInfo unwrap() {
        return wrapped;
    }

    // -----------------------------------------------------------------------------
    // WRAPPED METHODS TO ENFORCE READ ONLY CONTRACT
    // -----------------------------------------------------------------------------

    public FeatureSource getFeatureSource(ProgressListener listener, Hints hints)
            throws IOException {
        return new ReadOnlyFeatureSource(wrapped.getFeatureSource(listener, hints));
    }

    public DataStoreInfo getStore() {
        return wrapped.getStore();
    }

    // -----------------------------------------------------------------------------
    // PLAINLY DELEGATED METHODS
    // -----------------------------------------------------------------------------

    public String getAbstract() {
        return wrapped.getAbstract();
    }

    public <T> T getAdapter(Class<T> adapterClass, Map<?, ?> hints) {
        return wrapped.getAdapter(adapterClass, hints);
    }

    public List<String> getAlias() {
        return wrapped.getAlias();
    }

    public List<AttributeTypeInfo> getAttributes() {
        return wrapped.getAttributes();
    }

    public ReferencedEnvelope getBoundingBox() throws Exception {
        return wrapped.getBoundingBox();
    }

    public Catalog getCatalog() {
        return wrapped.getCatalog();
    }

    public CoordinateReferenceSystem getCRS() throws Exception {
        return wrapped.getCRS();
    }

    public String getDescription() {
        return wrapped.getDescription();
    }

    public SimpleFeatureType getFeatureType() throws IOException {
        return wrapped.getFeatureType();
    }

    public Filter getFilter() {
        return wrapped.getFilter();
    }

    public String getId() {
        return wrapped.getId();
    }

    public List<String> getKeywords() {
        return wrapped.getKeywords();
    }

    public ReferencedEnvelope getLatLonBoundingBox() {
        return wrapped.getLatLonBoundingBox();
    }

    public int getMaxFeatures() {
        return wrapped.getMaxFeatures();
    }

    public Map<String, Serializable> getMetadata() {
        return wrapped.getMetadata();
    }

    public List<MetadataLinkInfo> getMetadataLinks() {
        return wrapped.getMetadataLinks();
    }

    public String getName() {
        return wrapped.getName();
    }

    public NamespaceInfo getNamespace() {
        return wrapped.getNamespace();
    }

    public ReferencedEnvelope getNativeBoundingBox() {
        return wrapped.getNativeBoundingBox();
    }

    public CoordinateReferenceSystem getNativeCRS() {
        return wrapped.getNativeCRS();
    }

    public String getNativeName() {
        return wrapped.getNativeName();
    }

    public int getNumDecimals() {
        return wrapped.getNumDecimals();
    }

    public String getPrefixedName() {
        return wrapped.getPrefixedName();
    }

    public ProjectionPolicy getProjectionPolicy() {
        return wrapped.getProjectionPolicy();
    }

    public String getSRS() {
        return wrapped.getSRS();
    }

    public String getTitle() {
        return wrapped.getTitle();
    }

    public boolean isEnabled() {
        return wrapped.isEnabled();
    }

    public void setAbstract(String _abstract) {
        wrapped.setAbstract(_abstract);
    }

    public void setDescription(String description) {
        wrapped.setDescription(description);
    }

    public void setEnabled(boolean enabled) {
        wrapped.setEnabled(enabled);
    }

    public void setFilter(Filter filter) {
        wrapped.setFilter(filter);
    }

    public void setLatLonBoundingBox(ReferencedEnvelope box) {
        wrapped.setLatLonBoundingBox(box);
    }

    public void setMaxFeatures(int maxFeatures) {
        wrapped.setMaxFeatures(maxFeatures);
    }

    public void setName(String name) {
        wrapped.setName(name);
    }

    public void setNamespace(NamespaceInfo namespace) {
        wrapped.setNamespace(namespace);
    }

    public void setNativeBoundingBox(ReferencedEnvelope box) {
        wrapped.setNativeBoundingBox(box);
    }

    public void setNativeCRS(CoordinateReferenceSystem nativeCRS) {
        wrapped.setNativeCRS(nativeCRS);
    }

    public void setNativeName(String nativeName) {
        wrapped.setNativeName(nativeName);
    }

    public void setNumDecimals(int numDecimals) {
        wrapped.setNumDecimals(numDecimals);
    }

    public void setProjectionPolicy(ProjectionPolicy policy) {
        wrapped.setProjectionPolicy(policy);
    }

    public void setSRS(String srs) {
        wrapped.setSRS(srs);
    }

    public void setStore(StoreInfo store) {
        wrapped.setStore(store);
    }

    public void setTitle(String title) {
        wrapped.setTitle(title);
    }

}
