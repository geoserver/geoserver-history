/*
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */

package org.geoserver.hibernate;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.collections.CollectionConverter;
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
import org.geoserver.catalog.hibernate.HibCatalogImpl;
import org.geoserver.catalog.hibernate.beans.AttributeTypeInfoImplHb;
import org.geoserver.catalog.hibernate.beans.AttributionInfoImplHb;
import org.geoserver.catalog.hibernate.beans.CoverageInfoImplHb;
import org.geoserver.catalog.hibernate.beans.CoverageStoreInfoImplHb;
import org.geoserver.catalog.hibernate.beans.DataStoreInfoImplHb;
import org.geoserver.catalog.hibernate.beans.FeatureTypeInfoImplHb;
import org.geoserver.catalog.hibernate.beans.LayerGroupInfoImplHb;
import org.geoserver.catalog.hibernate.beans.LayerInfoImplHb;
import org.geoserver.catalog.hibernate.beans.NamespaceInfoImplHb;
import org.geoserver.catalog.hibernate.beans.StyleInfoImplHb;
import org.geoserver.catalog.hibernate.beans.WorkspaceInfoImplHb;
import org.geoserver.catalog.impl.CoverageDimensionImpl;
import org.geoserver.catalog.impl.LayerGroupInfoImpl;
import org.geoserver.catalog.impl.ResourceInfoImpl;
import org.geoserver.catalog.impl.StoreInfoImpl;
import org.geoserver.config.ContactInfo;
import org.geoserver.config.GeoServerInfo;
import org.geoserver.config.JAIInfo;
import org.geoserver.config.LoggingInfo;
import org.geoserver.config.ServiceInfo;
import org.geoserver.config.hibernate.beans.ContactInfoImplHb;
import org.geoserver.config.hibernate.beans.GeoServerInfoImplHb;
import org.geoserver.config.hibernate.beans.LoggingInfoImplHb;
import org.geoserver.config.hibernate.beans.MetadataLinkInfoImplHb;
import org.geoserver.config.impl.JAIInfoImpl;
import org.geoserver.config.impl.ServiceInfoImpl;
import org.geoserver.config.util.ImplementationMapper;


/**
 * Provides implementation mapping for GeoServer interfaces.
 * Mainly used in XStream outputting.
 *
 * @author ETj <etj at geo-solutions.it>
 */
public class HibImplementationMapper implements ImplementationMapper {

    private static Map<Class, Class> impl = new HashMap<Class, Class>();
//    private static List<Converter> converters = new ArrayList<Converter>();
    
    static {
        populateImpl();
//        populateConverters();
    }

    public Class getImpl(Class clazz) {
        return impl.get(clazz);
    }

    public static Map<String, Converter> getLocalConverters() {
        return new HashMap<String, Converter>();
    }

    public List<Converter> getConverters(XStream xs) {
        List<Converter> converters = new ArrayList<Converter>();
        converters.add(new HibListConverter(xs));
        return converters;
    }
    
    private static void populateImpl() {
        impl.put(GeoServerInfo.class, GeoServerInfoImplHb.class);
        impl.put(LoggingInfo.class, LoggingInfoImplHb.class);
        impl.put(JAIInfo.class, JAIInfoImpl.class);
        impl.put(Catalog.class, HibCatalogImpl.class);
        impl.put(NamespaceInfo.class, NamespaceInfoImplHb.class);
        impl.put(WorkspaceInfo.class, WorkspaceInfoImplHb.class);
        impl.put(DataStoreInfo.class, DataStoreInfoImplHb.class);
        impl.put(CoverageStoreInfo.class, CoverageStoreInfoImplHb.class);
        impl.put(StyleInfo.class, StyleInfoImplHb.class);
        impl.put(FeatureTypeInfo.class, FeatureTypeInfoImplHb.class);
        impl.put(CoverageInfo.class, CoverageInfoImplHb.class);
        impl.put(CoverageDimensionInfo.class, CoverageDimensionImpl.class);
        impl.put(MetadataLinkInfo.class, MetadataLinkInfoImplHb.class);
        impl.put(AttributeTypeInfo.class, AttributeTypeInfoImplHb.class);
        impl.put(LayerInfo.class, LayerInfoImplHb.class);
        impl.put(LayerGroupInfo.class, LayerGroupInfoImplHb.class);
        impl.put(AttributionInfo.class, AttributionInfoImplHb.class);
        impl.put(ContactInfo.class, ContactInfoImplHb.class);
        impl.put(ResourceInfo.class, ResourceInfoImpl.class);

        impl.put(ServiceInfo.class, ServiceInfoImpl.class);
        impl.put(StoreInfo.class, StoreInfoImpl.class);
    }

//    private static void populateConverters() {
//        converters.add(new HibListConverter("hibllistconverter"));
//    }

}

/**
 * Loop through the items of an hibernate {@link org.hibernate.collection.PersistentList}.
 * If not handled by a Converter, XStream will try to extract its internal data.
 *
 * @author ETj <etj at geo-solutions.it>
 */
class HibListConverter 
        extends CollectionConverter
        implements Converter {

    public HibListConverter( XStream xs ) {
        super(xs.getMapper());
    }

    @Override
    public boolean canConvert(Class type) {
        return type.equals(org.hibernate.collection.PersistentList.class);
    }

}
