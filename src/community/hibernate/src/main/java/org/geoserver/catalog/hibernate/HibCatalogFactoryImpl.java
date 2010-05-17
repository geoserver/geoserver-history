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
import org.geoserver.catalog.hibernate.beans.LayerGroupInfoImplHb;
import org.geoserver.catalog.hibernate.beans.LayerInfoImplHb;
import org.geoserver.catalog.impl.AttributeTypeInfoImpl;
import org.geoserver.catalog.impl.AttributionInfoImpl;
import org.geoserver.catalog.impl.CoverageDimensionImpl;
import org.geoserver.catalog.impl.CoverageInfoImpl;
import org.geoserver.catalog.impl.CoverageStoreInfoImpl;
import org.geoserver.catalog.impl.DataStoreInfoImpl;
import org.geoserver.catalog.impl.FeatureTypeInfoImpl;
import org.geoserver.catalog.impl.LegendInfoImpl;
import org.geoserver.catalog.impl.MapInfoImpl;
import org.geoserver.catalog.impl.MetadataLinkInfoImpl;
import org.geoserver.catalog.impl.NamespaceInfoImpl;
import org.geoserver.catalog.impl.StyleInfoImpl;
import org.geoserver.catalog.impl.WorkspaceInfoImpl;

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
        return new LayerInfoImplHb();
    }

    public MapInfo createMap() {
        return new MapInfoImpl();
    }

    public StyleInfo createStyle() {
        return new StyleInfoImpl(catalog);
    }

    public NamespaceInfoImpl createNamespace() {
        return new NamespaceInfoImpl();
    }

    public <T> T create(Class<T> clazz) {
        LOGGER.severe("TODO");
        return null;
    }

    public AttributeTypeInfo createAttribute() {
        return new AttributeTypeInfoImpl();
    }

    public LayerGroupInfo createLayerGroup() {
        return new LayerGroupInfoImplHb();
    }

    public LegendInfo createLegend() {
        return new LegendInfoImpl();
    }

    public WorkspaceInfoImpl createWorkspace() {
        return new WorkspaceInfoImpl();
    }

    public AttributionInfo createAttribution() {
        return new AttributionInfoImpl();
    }

	public CoverageDimensionInfo createCoverageDimension() {
		return new CoverageDimensionImpl();
	}

}
