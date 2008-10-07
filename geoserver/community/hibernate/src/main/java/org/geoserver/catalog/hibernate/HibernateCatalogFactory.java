package org.geoserver.catalog.hibernate;

import org.geoserver.catalog.AttributeTypeInfo;
import org.geoserver.catalog.Catalog;
import org.geoserver.catalog.CatalogFactory;
import org.geoserver.catalog.CoverageInfo;
import org.geoserver.catalog.CoverageStoreInfo;
import org.geoserver.catalog.DataStoreInfo;
import org.geoserver.catalog.FeatureTypeInfo;
import org.geoserver.catalog.LayerGroupInfo;
import org.geoserver.catalog.LayerInfo;
import org.geoserver.catalog.LegendInfo;
import org.geoserver.catalog.MapInfo;
import org.geoserver.catalog.MetadataLinkInfo;
import org.geoserver.catalog.NamespaceInfo;
import org.geoserver.catalog.StyleInfo;
import org.geoserver.catalog.WorkspaceInfo;
import org.geoserver.catalog.impl.CoverageInfoImpl;
import org.geoserver.catalog.impl.CoverageStoreInfoImpl;
import org.geoserver.catalog.impl.DataStoreInfoImpl;
import org.geoserver.catalog.impl.FeatureTypeInfoImpl;
import org.geoserver.catalog.impl.LayerGroupInfoImpl;
import org.geoserver.catalog.impl.LayerInfoImpl;
import org.geoserver.catalog.impl.LegendInfoImpl;
import org.geoserver.catalog.impl.MapInfoImpl;
import org.geoserver.catalog.impl.MetadataLinkInfoImpl;
import org.geoserver.catalog.impl.NamespaceInfoImpl;
import org.geoserver.catalog.impl.StyleInfoImpl;
import org.geoserver.catalog.impl.WorkspaceInfoImpl;

public class HibernateCatalogFactory implements CatalogFactory {

    Catalog catalog;

    /**
     * @param catalog
     */
    public HibernateCatalogFactory(Catalog catalog) {
        this.catalog = catalog;
    }

    public DataStoreInfo createDataStore() {
        return new DataStoreInfoImpl(catalog);
    }

    public MetadataLinkInfo createMetadataLink() {
        return new MetadataLinkInfoImpl();
    }

    public CoverageStoreInfo createCoverageStore() {
        return new CoverageStoreInfoImpl(catalog);
    }

    public FeatureTypeInfo createFeatureType() {
        return new FeatureTypeInfoImpl(catalog);
    }

    public CoverageInfo createCoverage() {
        return new CoverageInfoImpl(catalog);
    }

    public LayerInfo createLayer() {
        return new LayerInfoImpl();
    }

    public MapInfo createMap() {
        return new MapInfoImpl();
    }

    public StyleInfo createStyle() {
        return new StyleInfoImpl(catalog);
    }

    public NamespaceInfo createNamespace() {
        return new NamespaceInfoImpl(catalog);
    }

    public <T> T create(Class<T> clazz) {
        return null;
    }

    public AttributeTypeInfo createAttribute() {
        return null;
    }

    public LayerGroupInfo createLayerGroup() {
        return new LayerGroupInfoImpl();
    }

    public LegendInfo createLegend() {
        return new LegendInfoImpl();
    }

    public WorkspaceInfo createWorkspace() {
        return new WorkspaceInfoImpl();
    }

}
