/*
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */

package org.geoserver.config.util;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.Converter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.geoserver.catalog.AttributeTypeInfo;
import org.geoserver.catalog.AttributionInfo;
import org.geoserver.catalog.Catalog;
import org.geoserver.catalog.CoverageDimensionInfo;
import org.geoserver.catalog.CoverageInfo;
import org.geoserver.catalog.CoverageStoreInfo;
import org.geoserver.catalog.DataStoreInfo;
import org.geoserver.catalog.FeatureTypeInfo;
import org.geoserver.catalog.LayerGroupInfo;
import org.geoserver.catalog.LayerInfo;
import org.geoserver.catalog.MetadataLinkInfo;
import org.geoserver.catalog.NamespaceInfo;
import org.geoserver.catalog.ResourceInfo;
import org.geoserver.catalog.StoreInfo;
import org.geoserver.catalog.StyleInfo;
import org.geoserver.catalog.WorkspaceInfo;
import org.geoserver.catalog.impl.AttributeTypeInfoImpl;
import org.geoserver.catalog.impl.AttributionInfoImpl;
import org.geoserver.catalog.impl.CatalogImpl;
import org.geoserver.catalog.impl.CoverageDimensionImpl;
import org.geoserver.catalog.impl.CoverageInfoImpl;
import org.geoserver.catalog.impl.CoverageStoreInfoImpl;
import org.geoserver.catalog.impl.DataStoreInfoImpl;
import org.geoserver.catalog.impl.FeatureTypeInfoImpl;
import org.geoserver.catalog.impl.LayerGroupInfoImpl;
import org.geoserver.catalog.impl.LayerInfoImpl;
import org.geoserver.catalog.impl.MetadataLinkInfoImpl;
import org.geoserver.catalog.impl.NamespaceInfoImpl;
import org.geoserver.catalog.impl.ResourceInfoImpl;
import org.geoserver.catalog.impl.StoreInfoImpl;
import org.geoserver.catalog.impl.StyleInfoImpl;
import org.geoserver.catalog.impl.WorkspaceInfoImpl;
import org.geoserver.config.ContactInfo;
import org.geoserver.config.GeoServerInfo;
import org.geoserver.config.JAIInfo;
import org.geoserver.config.LoggingInfo;
import org.geoserver.config.ServiceInfo;
import org.geoserver.config.impl.ContactInfoImpl;
import org.geoserver.config.impl.GeoServerInfoImpl;
import org.geoserver.config.impl.JAIInfoImpl;
import org.geoserver.config.impl.LoggingInfoImpl;
import org.geoserver.config.impl.ServiceInfoImpl;

/**
 * Maps the default geoserver implementations to their related interfaces.
 * <BR>
 * Use mainly for encoding/decoding classes in XStream.
 *
 * @see org.geoserver.config.util.XStreamPersister
 *
 * @author ETj <etj at geo-solutions.it>
 */
public class DefaultImplementationMapper implements ImplementationMapper{

    private static Map<Class, Class> impl = new HashMap<Class, Class>();
    
    static {
        impl.put(GeoServerInfo.class, GeoServerInfoImpl.class);
        impl.put(LoggingInfo.class, LoggingInfoImpl.class);
        impl.put(JAIInfo.class, JAIInfoImpl.class);
        impl.put(Catalog.class, CatalogImpl.class);
        impl.put(NamespaceInfo.class, NamespaceInfoImpl.class);
        impl.put(WorkspaceInfo.class, WorkspaceInfoImpl.class);
        impl.put(DataStoreInfo.class, DataStoreInfoImpl.class);
        impl.put(CoverageStoreInfo.class, CoverageStoreInfoImpl.class);
        impl.put(StyleInfo.class, StyleInfoImpl.class);
        impl.put(FeatureTypeInfo.class, FeatureTypeInfoImpl.class);
        impl.put(CoverageInfo.class, CoverageInfoImpl.class);
        impl.put(CoverageDimensionInfo.class, CoverageDimensionImpl.class);
        impl.put(MetadataLinkInfo.class, MetadataLinkInfoImpl.class);
        impl.put(AttributeTypeInfo.class, AttributeTypeInfoImpl.class);
        impl.put(LayerInfo.class, LayerInfoImpl.class);
        impl.put(LayerGroupInfo.class, LayerGroupInfoImpl.class);
        impl.put(AttributionInfo.class, AttributionInfoImpl.class);
        impl.put(ContactInfo.class, ContactInfoImpl.class);
        impl.put(ResourceInfo.class, ResourceInfoImpl.class);

        impl.put(ServiceInfo.class, ServiceInfoImpl.class);
        impl.put(StoreInfo.class, StoreInfoImpl.class);
    }

    public Class getImpl(Class clazz) {
        return impl.get(clazz);
    }

    public List<Converter> getConverters(XStream xs) {
        return new ArrayList<Converter>();
    }

}
