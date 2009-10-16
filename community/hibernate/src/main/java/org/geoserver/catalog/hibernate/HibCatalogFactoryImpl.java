package org.geoserver.catalog.hibernate;

import java.util.logging.Logger;

import org.geoserver.catalog.AttributeTypeInfo;
import org.geoserver.catalog.AttributionInfo;
import org.geoserver.catalog.CatalogFactory;
import org.geoserver.catalog.CoverageDimensionInfo;
import org.geoserver.catalog.CoverageInfo;
import org.geoserver.catalog.CoverageStoreInfo;
import org.geoserver.catalog.DataStoreInfo;
import org.geoserver.catalog.FeatureTypeInfo;
import org.geoserver.catalog.LayerGroupInfo;
import org.geoserver.catalog.LayerInfo;
import org.geoserver.catalog.LegendInfo;
import org.geoserver.catalog.MapInfo;
import org.geoserver.catalog.MetadataLinkInfo;
import org.geoserver.catalog.StyleInfo;
import org.geoserver.catalog.hibernate.beans.AttributeTypeInfoImplHb;
import org.geoserver.catalog.hibernate.beans.AttributionInfoImplHb;
import org.geoserver.catalog.hibernate.beans.CoverageDimensionInfoImplHb;
import org.geoserver.catalog.hibernate.beans.CoverageInfoImplHb;
import org.geoserver.catalog.hibernate.beans.CoverageStoreInfoImplHb;
import org.geoserver.catalog.hibernate.beans.DataStoreInfoImplHb;
import org.geoserver.catalog.hibernate.beans.FeatureTypeInfoImplHb;
import org.geoserver.catalog.hibernate.beans.LayerGroupInfoImplHb;
import org.geoserver.catalog.hibernate.beans.LayerInfoImplHb;
import org.geoserver.catalog.hibernate.beans.NamespaceInfoImplHb;
import org.geoserver.catalog.hibernate.beans.StyleInfoImplHb;
import org.geoserver.catalog.hibernate.beans.WorkspaceInfoImplHb;
import org.geoserver.catalog.impl.LegendInfoImpl;
import org.geoserver.catalog.impl.MapInfoImpl;
import org.geoserver.catalog.impl.MetadataLinkInfoImpl;

public class HibCatalogFactoryImpl implements CatalogFactory {

    private final static Logger LOGGER = Logger.getLogger(HibCatalogFactoryImpl.class.getName());

    private final HibCatalogImpl catalog;

    /**
     * @param catalog
     */
    public HibCatalogFactoryImpl(HibCatalogImpl catalog) {
        this.catalog = catalog;
    }

    public DataStoreInfo createDataStore() {
        return new DataStoreInfoImplHb(catalog);
    }

    public MetadataLinkInfo createMetadataLink() {
        return new MetadataLinkInfoImpl();
    }

    public CoverageStoreInfo createCoverageStore() {
        return new CoverageStoreInfoImplHb(catalog);
    }

    public FeatureTypeInfo createFeatureType() {
        return new FeatureTypeInfoImplHb(catalog);
    }

    public CoverageInfo createCoverage() {
        return new CoverageInfoImplHb(catalog);
    }

    public LayerInfo createLayer() {
        return new LayerInfoImplHb();
    }

    public MapInfo createMap() {
        return new MapInfoImpl();
    }

    public StyleInfo createStyle() {
        return new StyleInfoImplHb(catalog);
    }

    public NamespaceInfoImplHb createNamespace() {
        return new NamespaceInfoImplHb();
    }

    public <T> T create(Class<T> clazz) {
        LOGGER.severe("TODO");
        return null;
    }

    public AttributeTypeInfo createAttribute() {
        return new AttributeTypeInfoImplHb();
    }

    public LayerGroupInfo createLayerGroup() {
        return new LayerGroupInfoImplHb();
    }

    public LegendInfo createLegend() {
        return new LegendInfoImpl();
    }

    public WorkspaceInfoImplHb createWorkspace() {
        return new WorkspaceInfoImplHb();
    }

    public CoverageDimensionInfo createCoverageDimension() {
        return new CoverageDimensionInfoImplHb();
    }

    public AttributionInfo createAttribution() {
        return new AttributionInfoImplHb();
    }

}
