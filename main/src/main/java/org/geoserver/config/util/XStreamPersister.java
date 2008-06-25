package org.geoserver.config.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.Map;

import org.apache.commons.collections.MultiHashMap;
import org.geoserver.catalog.DataStoreInfo;
import org.geoserver.catalog.MetadataLinkInfo;
import org.geoserver.catalog.NamespaceInfo;
import org.geoserver.catalog.StyleInfo;
import org.geoserver.catalog.WorkspaceInfo;
import org.geoserver.catalog.impl.CatalogImpl;
import org.geoserver.catalog.impl.CoverageStoreInfoImpl;
import org.geoserver.catalog.impl.DataStoreInfoImpl;
import org.geoserver.catalog.impl.MetadataLinkInfoImpl;
import org.geoserver.catalog.impl.NamespaceInfoImpl;
//import org.geoserver.catalog.impl.ResolvingProxy;
import org.geoserver.catalog.impl.StoreInfoImpl;
import org.geoserver.catalog.impl.StyleInfoImpl;
import org.geoserver.catalog.impl.WorkspaceInfoImpl;
import org.geoserver.config.ContactInfo;
import org.geoserver.config.impl.ContactInfoImpl;
import org.geoserver.config.impl.GeoServerInfoImpl;
import org.geoserver.config.impl.ServiceInfoImpl;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.converters.reflection.ReflectionConverter;
import com.thoughtworks.xstream.converters.reflection.ReflectionProvider;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.mapper.Mapper;

/**
 * Utility class which loads and saves catalog and configuration objects to and
 * from an xstream.
 * 
 * @author Justin Deoliveira, The Open Planning Project
 * 
 */
public class XStreamPersister {

    /**
     * internal xstream instance
     */
    XStream xs;

    /**
     * Constructs the persister and underlyign xstream.
     */
    public XStreamPersister() {
        xs = new XStream();

        // GeoServerInfo
        xs.addDefaultImplementation(MetadataLinkInfoImpl.class,
                MetadataLinkInfo.class);
        xs.addDefaultImplementation(ContactInfoImpl.class, ContactInfo.class);
        xs.alias("global", GeoServerInfoImpl.class);
        xs.omitField(GeoServerInfoImpl.class, "clientProperties");

        // ServiceInfo
        xs.omitField(ServiceInfoImpl.class, "clientProperties");
        xs.omitField(ServiceInfoImpl.class, "geoServer");

        // Catalog
        xs.addDefaultImplementation(WorkspaceInfoImpl.class,
                WorkspaceInfo.class);
        xs.addDefaultImplementation(NamespaceInfoImpl.class,
                NamespaceInfo.class);
        xs.addDefaultImplementation(StyleInfoImpl.class,
                StyleInfo.class);
        
        xs.alias("catalog", CatalogImpl.class);
        xs.alias("namespace", NamespaceInfo.class);
        xs.alias("workspace", WorkspaceInfo.class);
        xs.alias("dataStore", DataStoreInfoImpl.class);
        xs.alias("coverageStore", CoverageStoreInfoImpl.class);
        xs.alias("style",StyleInfo.class);
        xs.registerConverter(new StoreInfoConverter(xs.getMapper(), xs
                .getReflectionProvider()));

        xs.omitField(CatalogImpl.class, "resourcePool");
        xs.omitField(CatalogImpl.class, "resources");
        xs.omitField(CatalogImpl.class, "listeners");
        xs.omitField(CatalogImpl.class, "layers");
        xs.omitField(CatalogImpl.class, "maps");
        xs.omitField(StoreInfoImpl.class, "catalog");
        xs.omitField(StyleInfoImpl.class, "catalog");
        
        xs.registerLocalConverter(CatalogImpl.class, "stores",
                new MultiHashMapConverter());
        xs.registerLocalConverter(CatalogImpl.class, "namespaces",
                new SpaceMapConverter("namespace"));
        xs.registerLocalConverter(CatalogImpl.class, "workspaces",
                new SpaceMapConverter("workspace"));
    }

    public XStream getXStream() {
        return xs;
    }

    /**
     * Saves an object to persistence.
     * 
     * @param obj The object to save. 
     * @param out The stream to save the object to.
     * 
     * @throws IOException
     */
    public void save(Object obj, OutputStream out) throws IOException {
        xs.toXML(obj, out);
    }
    
    /**
     * Loads an object from peristence.
     * 
     * @param in The input stream to read the object from.
     * @param clazz The class of the expected object.
     * 
     * @throws IOException
     */
    public <T> T load(InputStream in, Class<T> clazz ) throws IOException {
        return clazz.cast( xs.fromXML( in ) );
    }

    /**
     * Converter which marshals / unmarshals the class to list multi hash map
     * maintained by the catalog.
     * 
     */
    static class MultiHashMapConverter implements Converter {
        public boolean canConvert(Class type) {
            return MultiHashMap.class.equals(type);
        }

        public void marshal(Object source, HierarchicalStreamWriter writer,
                MarshallingContext context) {
            MultiHashMap map = (MultiHashMap) source;
            for (Object v : map.values()) {
                if (v instanceof DataStoreInfo) {
                    writer.startNode("dataStore");
                    context.convertAnother(v);
                    writer.endNode();
                }
            }
        }

        public Object unmarshal(HierarchicalStreamReader reader,
                UnmarshallingContext context) {
            return null;
        }
    }

    /**
     * Converter for handling the workspace and namespace maps.
     *
     */
    static class SpaceMapConverter implements Converter {

        String name;
        
        SpaceMapConverter( String name ) {
            this.name = name;
        }
        
        public boolean canConvert(Class type) {
            return Map.class.isAssignableFrom(type);
        }

        public void marshal(Object source, HierarchicalStreamWriter writer,
                MarshallingContext context) {
            Map map = (Map) source;
            
            for (Object o : map.entrySet()) {
                Map.Entry e = (Map.Entry) o;
                if ( e.getKey() == null ) {
                    continue;
                }
                
                writer.startNode(name);
                if ( map.get( null ) == e.getValue() ) {
                    writer.addAttribute("default", "true");
                }
                context.convertAnother(e.getValue());
                writer.endNode();
            }
        }

        public Object unmarshal(HierarchicalStreamReader reader,
                UnmarshallingContext context) {
            return null;
        }
    }

    /**
     * Converter for handling stores.
     *
     */
    static class StoreInfoConverter extends ReflectionConverter {

        public StoreInfoConverter(Mapper mapper,
                ReflectionProvider reflectionProvider) {
            super(mapper, reflectionProvider);
        }

        public boolean canConvert(Class type) {
            return StoreInfoImpl.class.isAssignableFrom(type);
        }

        protected void marshallField(final MarshallingContext context,
                Object newObj, Field field) {
            if (newObj instanceof WorkspaceInfo) {
                WorkspaceInfo ws = (WorkspaceInfo) newObj;
                if (ws.getId() != null) {
                    context.convertAnother(ws.getId());
                } else {
                    context.convertAnother(ws.getName());
                }
            } else {
                super.marshallField(context, newObj, field);
            }
        }

        protected Object unmarshallField(UnmarshallingContext context,
                Object result, Class type, Field field) {
            if (WorkspaceInfo.class.isAssignableFrom(type)) {
                // next should be workspace id
                String ref = (String) context.convertAnother(result,
                        String.class);

                // this workspace could be a reference to something referenced
                // later, create a proxy to it
                //return ResolvingProxy.create(ref, WorkspaceInfo.class);
                return null;
            } else {
                return super.unmarshallField(context, result, type, field);
            }
        }
    }
}
