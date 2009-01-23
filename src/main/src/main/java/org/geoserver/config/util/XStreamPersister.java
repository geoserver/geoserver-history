/* Copyright (c) 2001 - 2008 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.config.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.apache.commons.collections.MultiHashMap;
import org.geoserver.catalog.AttributeTypeInfo;
import org.geoserver.catalog.Catalog;
import org.geoserver.catalog.CoverageInfo;
import org.geoserver.catalog.CoverageStoreInfo;
import org.geoserver.catalog.DataStoreInfo;
import org.geoserver.catalog.FeatureTypeInfo;
import org.geoserver.catalog.MetadataLinkInfo;
import org.geoserver.catalog.NamespaceInfo;
import org.geoserver.catalog.ResourceInfo;
import org.geoserver.catalog.StoreInfo;
import org.geoserver.catalog.StyleInfo;
import org.geoserver.catalog.WorkspaceInfo;
import org.geoserver.catalog.impl.AttributeTypeInfoImpl;
import org.geoserver.catalog.impl.CatalogImpl;
import org.geoserver.catalog.impl.CoverageStoreInfoImpl;
import org.geoserver.catalog.impl.DataStoreInfoImpl;
import org.geoserver.catalog.impl.FeatureTypeInfoImpl;
import org.geoserver.catalog.impl.LayerInfoImpl;
import org.geoserver.catalog.impl.MetadataLinkInfoImpl;
import org.geoserver.catalog.impl.NamespaceInfoImpl;
import org.geoserver.catalog.impl.ResolvingProxy;
import org.geoserver.catalog.impl.ResourceInfoImpl;
import org.geoserver.catalog.impl.StoreInfoImpl;
import org.geoserver.catalog.impl.StyleInfoImpl;
import org.geoserver.catalog.impl.WorkspaceInfoImpl;
import org.geoserver.config.ContactInfo;
import org.geoserver.config.impl.ContactInfoImpl;
import org.geoserver.config.impl.GeoServerImpl;
import org.geoserver.config.impl.GeoServerInfoImpl;
import org.geoserver.config.impl.ServiceInfoImpl;
import org.geoserver.ows.util.OwsUtils;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.util.logging.Logging;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.converters.collections.CollectionConverter;
import com.thoughtworks.xstream.converters.collections.MapConverter;
import com.thoughtworks.xstream.converters.reflection.FieldDictionary;
import com.thoughtworks.xstream.converters.reflection.ReflectionConverter;
import com.thoughtworks.xstream.converters.reflection.ReflectionProvider;
import com.thoughtworks.xstream.converters.reflection.SortableFieldKeySorter;
import com.thoughtworks.xstream.converters.reflection.Sun14ReflectionProvider;
import com.thoughtworks.xstream.io.ExtendedHierarchicalStreamWriterHelper;
import com.thoughtworks.xstream.io.HierarchicalStreamDriver;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.json.JettisonMappedXmlDriver;
import com.thoughtworks.xstream.mapper.Mapper;

/**
 * Utility class which loads and saves catalog and configuration objects to and
 * from an xstream.
 * 
 * @author Justin Deoliveira, The Open Planning Project
 * 
 */
public class XStreamPersister {

    public static class XML extends XStreamPersister {

        public XML() {
            super(null);
        }
        
    }

    public static class JSON extends XStreamPersister {

        public JSON() {
            //super( new JsonHierarchicalStreamDriver() );
            super( new JettisonMappedXmlDriver() );
        }
        
    }

    /**
     * logging instance
     */
    static Logger LOGGER = Logging.getLogger( "org.geoserver" );
    
    /**
     * internal xstream instance
     */
    XStream xs;

    /**
     * Catalog reference, used to resolve references to stores, workspaces 
     * + namespaces
     */
    Catalog catalog;
    
    /**
     * Constructs the persister and underlyign xstream.
     */
    protected XStreamPersister(HierarchicalStreamDriver streamDriver) {
        //control the order in which fields are sorted
        SortableFieldKeySorter sorter = new SortableFieldKeySorter();
        sorter.registerFieldOrder( CatalogImpl.class, new String[]{ "workspaces", "namespaces", "stores", "styles", 
            /* these we actually omit, but the sorter needs them specified */
            "layerGroups", "resources", "maps", "listeners", "layers",  "resourcePool", "resourceLoader" } ); 
        
        ReflectionProvider reflectionProvider = new Sun14ReflectionProvider( new FieldDictionary( sorter  ) ); 
        if ( streamDriver != null ) {
            xs = new XStream( reflectionProvider, streamDriver );
        }
        else {
            xs = new XStream( reflectionProvider );    
        }
        xs.setMode(XStream.NO_REFERENCES);
        
        // Aliases
        xs.alias("global", GeoServerInfoImpl.class);
        xs.alias("catalog", CatalogImpl.class);
        xs.alias("namespace", NamespaceInfo.class);
        xs.alias("workspace", WorkspaceInfo.class);
        xs.alias("dataStore", DataStoreInfoImpl.class);
        xs.alias("coverageStore", CoverageStoreInfoImpl.class);
        xs.alias("style",StyleInfo.class);
        xs.alias( "featureType", FeatureTypeInfoImpl.class );
        xs.alias( "attribute", AttributeTypeInfoImpl.class );
        xs.alias( "layer", LayerInfoImpl.class ); 
        xs.alias( "geographic", DefaultGeographicCRS.class);
        xs.aliasField("abstract", ResourceInfoImpl.class, "_abstract" );
        
        
        // GeoServerInfo
        xs.addDefaultImplementation(MetadataLinkInfoImpl.class,
                MetadataLinkInfo.class);
        xs.addDefaultImplementation(ContactInfoImpl.class, ContactInfo.class);
        xs.addDefaultImplementation(ArrayList.class, List.class);
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
        xs.omitField(CatalogImpl.class, "resourcePool");
        xs.omitField(CatalogImpl.class, "resourceLoader");
        xs.omitField(CatalogImpl.class, "resources");
        xs.omitField(CatalogImpl.class, "listeners");
        xs.omitField(CatalogImpl.class, "layers");
        xs.omitField(CatalogImpl.class, "maps");
        xs.omitField(CatalogImpl.class, "layerGroups");
        
        // StoreInfo
        xs.omitField(StoreInfoImpl.class, "catalog");
        xs.omitField(StoreInfoImpl.class, "workspace");
        
        // StyleInfo
        xs.omitField(StyleInfoImpl.class, "catalog");
        
        // ResourceInfo
        xs.omitField( ResourceInfoImpl.class, "catalog");
        xs.omitField( ResourceInfoImpl.class, "crs" );
        xs.omitField( ResourceInfoImpl.class, "nativeCRS");
        xs.omitField( ResourceInfoImpl.class, "namespace");
        xs.omitField( ResourceInfoImpl.class, "store");
        
        // FeatureTypeInfo
        
        // AttributeTypeInfo
        xs.omitField( AttributeTypeInfoImpl.class, "featureType");
        xs.omitField( AttributeTypeInfoImpl.class, "attribute");
        
        // LayerInfo
        xs.omitField( LayerInfoImpl.class, "resource");
        xs.omitField( LayerInfoImpl.class, "defaultStyle");
        
        //ReferencedEnvelope
        xs.omitField( ReferencedEnvelope.class, "crs" );
        
        // Converters
        xs.registerConverter(new StoreInfoConverter(xs.getMapper(),xs.getReflectionProvider()));
        xs.registerConverter(new ResourceInfoConverter(xs.getMapper(),xs.getReflectionProvider()));
        xs.registerConverter(new FeatureTypeInfoConverter(xs.getMapper(),xs.getReflectionProvider()));
        xs.registerConverter(new LayerInfoConverter(xs.getMapper(),xs.getReflectionProvider()));

        //local converters
        xs.registerLocalConverter(CatalogImpl.class, "stores",
                new MultiHashMapConverter());
        xs.registerLocalConverter(CatalogImpl.class, "namespaces",
                new SpaceMapConverter("namespace"));
        xs.registerLocalConverter(CatalogImpl.class, "workspaces",
                new SpaceMapConverter("workspace"));
        
        MetadataConverter metadataConverter = new MetadataConverter(xs.getMapper());
        xs.registerLocalConverter(ResourceInfoImpl.class, "metadata", metadataConverter );
        
        //xs.registerConverter(new CRSConverter());
        xs.registerConverter(new ProxyCollectionConverter( xs.getMapper() ) );
        xs.registerConverter(new ReferencedEnvelopeConverter(xs.getMapper(), xs.getReflectionProvider()));
    }

    public XStream getXStream() {
        return xs;
    }

    public void setCatalog(Catalog catalog) {
        this.catalog = catalog;
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
        //unwrap dynamic proxies
        obj = unwrapProxies( obj );
        
        xs.toXML(obj, new OutputStreamWriter( out, "UTF-8" ));
    }
    
    /**
     * Unwraps any proxies around the object.
     * <p>
     * If the object is not being proxied it is passed back.
     * </p>
     */
    protected Object unwrapProxies( Object obj ) {
        obj = GeoServerImpl.unwrap( obj );
        obj = CatalogImpl.unwrap( obj );
        return obj;
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
        T obj = clazz.cast( xs.fromXML( in ) );
        
        //call resolve() to ensure that any references created during loading
        // get resolved to actual objects, for instance for links from datastores
        // to workspaces
        if ( obj instanceof CatalogImpl ) {
            ((CatalogImpl)obj).resolve();
        }
        
        return obj;
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
                if (v instanceof CoverageStoreInfo ) {
                    writer.startNode( "coverageStore" );
                    context.convertAnother(v);
                    writer.endNode();
                }
            }
        }

        public Object unmarshal(HierarchicalStreamReader reader,
                UnmarshallingContext context) {
            MultiHashMap map = new MultiHashMap();
            
            while( reader.hasMoreChildren() ) {
                reader.moveDown();
                
                Object o = 0;
                if ( "dataStore".equals( reader.getNodeName() ) ) {
                    o = context.convertAnother( map, DataStoreInfoImpl.class );
                }
                else {
                    o = context.convertAnother( map, CoverageStoreInfoImpl.class );
                }
                map.put( o.getClass(), o );
                
                reader.moveUp();
            }
            
            return map;
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
            
            LOGGER.info( "Persisting " + name + " '" + source + "'" );
            
            
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
            Map map = new HashMap();
            
            while( reader.hasMoreChildren() ) {
               reader.moveDown();

               boolean def = "true".equals( reader.getAttribute( "default") );
               
               if ( "namespace".equals( name ) ) {
                   NamespaceInfoImpl ns = (NamespaceInfoImpl) context.convertAnother( map, NamespaceInfoImpl.class );
                   map.put( ns.getPrefix() , ns );
                   if ( def ) {
                       map.put( null, ns );
                   }
                   LOGGER.info( "Loading namespace '" + ns.getPrefix() + "'" );
               }
               else {
                   WorkspaceInfoImpl ws = (WorkspaceInfoImpl) context.convertAnother( map, WorkspaceInfoImpl.class );
                   map.put( ws.getName() , ws );
                   if ( def ) {
                       map.put( null, ws );
                   }
                   LOGGER.info( "Loading workspace '" + ws.getName() + "'" );
               }
               
               reader.moveUp();
            }
            
            return map;
        }
    }

    static class CRSConverter implements Converter {

        public boolean canConvert(Class type) {
            return CoordinateReferenceSystem.class.isAssignableFrom(type);
        }
        
        public void marshal(Object source, HierarchicalStreamWriter writer,
                MarshallingContext context) {
            
            CoordinateReferenceSystem crs = (CoordinateReferenceSystem)  source;
            try {
                writer.setValue("EPSG:" + CRS.lookupEpsgCode(crs, true) );
            } 
            catch (FactoryException e) {
                //TODO:log this
                e.printStackTrace();
            }
            
        }

        public Object unmarshal(HierarchicalStreamReader reader,
                UnmarshallingContext context) {
            
            String epsg = reader.getValue();
            try {
                return CRS.decode( epsg );
            } 
            catch (Exception e) {
                //TODO: log this
                e.printStackTrace();
            }
            
            return null;
        }
    }
    
    static void marshalReference( Object source, HierarchicalStreamWriter w, String name ) {
        
        Object obj = OwsUtils.get( source, name );
        if ( obj == null ) {
            return;
        }
        
        //could be a proxy, unwrap it
        obj = CatalogImpl.unwrap( obj );
        
        //gets its id
        String id = (String) OwsUtils.get( obj, "id" );
        
        //use name if no id set
        if ( id == null ) {
            id = (String) OwsUtils.get( obj, "name" );
        }
        
        w.startNode(name);
        w.setValue(id);
        w.endNode();
    }
    
    /**
     * Converter for handling stores.
     *
     */
    class StoreInfoConverter extends ReflectionConverter {

        public StoreInfoConverter(Mapper mapper,
                ReflectionProvider reflectionProvider) {
            super(mapper, reflectionProvider);
        }

        public boolean canConvert(Class type) {
            return StoreInfoImpl.class.isAssignableFrom(type);
        }

        protected void doMarshal(Object source,
                HierarchicalStreamWriter writer, MarshallingContext context) {
            super.doMarshal(source, writer, context);
            
            StoreInfo store = (StoreInfo) source;
            LOGGER.info( "Persisting store '" +  store.getName() +  "'");
            
            marshalReference( source, writer, "workspace" );
        }
        
        public Object doUnmarshal(Object result,
                HierarchicalStreamReader reader, UnmarshallingContext context) {
            StoreInfo store = (StoreInfo) super.doUnmarshal(result, reader, context);
            
            LOGGER.info( "Loaded store '" +  store.getName() +  "', " + (store.isEnabled() ? "enabled" : "disabled") );
            return store;
        }
        
        protected Object unmarshallField(UnmarshallingContext context,
                Object result, Class type, Field field) {
            
            if (WorkspaceInfo.class.isAssignableFrom(type)) {
                // next should be  id
                String ref = (String) context.convertAnother(result, String.class);

                // this object could be a reference to something referenced
                // later, create a proxy to it
                if ( catalog != null ) {
                    //try to resolve directly
                    WorkspaceInfo ws = catalog.getWorkspace( ref );
                    if ( ws != null ) {
                        return CatalogImpl.unwrap(ws);
                    }
                }
                
                //fall back on creating a resolving proxy
                return ResolvingProxy.create(ref, WorkspaceInfo.class);
            } else {
                return super.unmarshallField(context, result, type, field); 
            }
        }
    }

    /**
     * Converter for handling resources.
     *
     */
    class ResourceInfoConverter extends ReflectionConverter {
        
        public ResourceInfoConverter( Mapper mapper, ReflectionProvider reflectionProvider) {
            super(mapper, reflectionProvider);
        }
        
        public boolean canConvert(Class type) {
            return ResourceInfoImpl.class.isAssignableFrom(type);
        }
        
        protected void doMarshal(Object source,
                HierarchicalStreamWriter writer, MarshallingContext context) {
            
            LOGGER.info( "Persisting resource '" + source + "'");
            super.doMarshal(source, writer, context);
            
            //encode crs manually
            ResourceInfo resource = (ResourceInfo) source;
            encodeCRS(resource.getNativeCRS(), writer, "nativeCRS");
            
            marshalReference( source, writer, "store" );
            marshalReference( source, writer, "namespace" );
        }
        
        protected Object unmarshallField(UnmarshallingContext context,
                Object result, Class type, Field field) {
            
            if ( StoreInfo.class.isAssignableFrom(type)) {
                String ref = (String) context.convertAnother(result, String.class);
                
                if ( catalog != null ) {
                    //try to resolve directly
                    StoreInfo s = catalog.getStoreByName( ref, type );
                    if ( s != null ) {
                        return CatalogImpl.unwrap(s);
                    }
                }
                //fall back on proxy
                return ResolvingProxy.create(ref, StoreInfo.class);
                
            }
            if ( NamespaceInfo.class.isAssignableFrom(type)) {
                String ref = (String) context.convertAnother(result, String.class);
                
                if ( catalog != null ) {
                    //try to resolve directly
                    NamespaceInfo ns = catalog.getNamespace( ref );
                    if ( ns != null ) {
                        return CatalogImpl.unwrap(ns);
                    }
                    
                    //fall back on proxy
                    return ResolvingProxy.create(ref, NamespaceInfo.class);
                }
            }
            
            if ( CoordinateReferenceSystem.class.isAssignableFrom( type ) ) {
                return decodeCRS(context,result);
            }

            return super.unmarshallField(context, result, type, field);
        }
        
        public Object doUnmarshal(Object result,
                HierarchicalStreamReader reader, UnmarshallingContext context) {
            ResourceInfo obj = (ResourceInfo) super.doUnmarshal(result, reader, context);
            
            String enabled = obj.isEnabled() ? "enabled" : "disabled";
            String type = obj instanceof CoverageInfo ? "coverage" : 
                obj instanceof FeatureTypeInfo ? "feature type" : "resource";
            
            LOGGER.info( "Loaded " + type + " '" + obj.getName() + "', " + enabled );
            
            return obj;
        }
    }
    
    class ReferencedEnvelopeConverter extends ReflectionConverter {

        public ReferencedEnvelopeConverter(Mapper mapper,
                ReflectionProvider reflectionProvider) {
            super(mapper, reflectionProvider);
        }

        @Override
        public boolean canConvert(Class type) {
            return ReferencedEnvelope.class.isAssignableFrom( type );
        }

        @Override
        protected void doMarshal(Object source,
                HierarchicalStreamWriter writer, MarshallingContext context) {
            super.doMarshal(source, writer, context);
            
            ReferencedEnvelope re = (ReferencedEnvelope) source;
            //encodeCRS( re.getCoordinateReferenceSystem(), writer, "crs" );
            encodeSRS( re.getCoordinateReferenceSystem(), writer, "crs" );
        }

        @Override
        protected Object unmarshallField(UnmarshallingContext context,
                Object result, Class type, Field field) {

            if ( CoordinateReferenceSystem.class.isAssignableFrom( type) ) {
                return decodeCRS(context, result);
            }

            return super.unmarshallField(context, result, type, field);
        }
    }

    void encodeCRS( CoordinateReferenceSystem crs, HierarchicalStreamWriter writer, String nodeName ) {
        if ( crs != null ) {
            writer.startNode(nodeName);
            writer.setValue( crs.toWKT() );
            writer.endNode();
        }
    }

    void encodeSRS( CoordinateReferenceSystem crs, HierarchicalStreamWriter writer, String nodeName ) {
        if ( crs != null ) {
            Integer epsg;
            try {
                epsg = CRS.lookupEpsgCode(crs,true);
            } 
            catch (FactoryException e) {
                throw new RuntimeException( e );
            }

            if ( epsg != null ) {
                writer.startNode(nodeName);
                writer.setValue( "EPSG:" + epsg );
                writer.endNode();
            }
        }
    }

    CoordinateReferenceSystem decodeCRS( UnmarshallingContext context, Object result) {
        //get the string value
        String val = (String) context.convertAnother(result, String.class);
        if ( val == null ) {
            return null;
        }
        
        //first try parsing as epsg code, then as wkt
        try {
            return CRS.decode( val );
        } 
        catch (FactoryException e) {
            //try as wkt
            try {
                return CRS.parseWKT( val );
            } 
            catch (FactoryException e1) {
                throw new RuntimeException( "Unable to parse: " + val );
            }
        }
    }

    class FeatureTypeInfoConverter extends ResourceInfoConverter {

        public FeatureTypeInfoConverter(Mapper mapper,
                ReflectionProvider reflectionProvider) {
            super(mapper, reflectionProvider);
        }
        
        public boolean canConvert(Class type) {
            return FeatureTypeInfo.class.isAssignableFrom(type);
        }
    
        public Object doUnmarshal(Object result,
                HierarchicalStreamReader reader, UnmarshallingContext context) {
            FeatureTypeInfoImpl featureType = (FeatureTypeInfoImpl) super.doUnmarshal(result, reader, context);
            if ( featureType.getAttributes() == null ){
                featureType.setAttributes(new ArrayList());
            }
            for ( AttributeTypeInfo at : featureType.getAttributes() ) {
                at.setFeatureType( featureType );
            }
            
            return featureType;
        }
    }
    
    class LayerInfoConverter extends ReflectionConverter {

        public LayerInfoConverter(Mapper mapper,
                ReflectionProvider reflectionProvider) {
            super(mapper, reflectionProvider);
        }
        
        public boolean canConvert(Class type) {
            return LayerInfoImpl.class.isAssignableFrom( type );
        }
        
        protected void doMarshal(Object source,
                HierarchicalStreamWriter writer, MarshallingContext context) {
            super.doMarshal(source, writer, context);
            
            marshalReference( source, writer, "resource" );
            marshalReference( source, writer, "defaultStyle" );
        }
        
        protected Object unmarshallField(UnmarshallingContext context,
                Object result, Class type, Field field) {
            
            if ( ResourceInfo.class.isAssignableFrom( type ) ) {

                String ref = (String) context.convertAnother( result, String.class );
                
                if ( catalog != null ) {
                    ResourceInfo r = catalog.getResource( ref, ResourceInfo.class );
                    if ( r != null ) {
                        return CatalogImpl.unwrap(r);
                    }
                }
                
                return ResolvingProxy.create( ref, ResourceInfo.class );
            }
            
            if ( StyleInfo.class.isAssignableFrom( type ) ) {
                String ref = (String) context.convertAnother( result, String.class );
                
                if ( catalog != null ) {
                    StyleInfo s = catalog.getStyle( ref );
                    if ( s != null ) {
                        return CatalogImpl.unwrap(s);
                    }
                     
                    return ResolvingProxy.create( ref, StyleInfo.class );
                }
            }
            
            return super.unmarshallField(context, result, type, field);
        }
    }
    
    class ProxyCollectionConverter extends CollectionConverter {

        public ProxyCollectionConverter(Mapper mapper) {
            super(mapper);
        }
        
        @Override
        protected void writeItem(Object item, MarshallingContext context,
                HierarchicalStreamWriter writer) {
                        
            super.writeItem(unwrapProxies(item), context, writer);
        }
        
    }
    
    /**
     * Extension of MapConverter which will omit entries with null values.
     */
    class MetadataConverter extends MapConverter {

        public MetadataConverter(Mapper mapper) {
            super(mapper);
        }
        
        
        @Override
        public void marshal(Object source, HierarchicalStreamWriter writer,
                MarshallingContext context) {
            Map map = (Map) source;
            for (Iterator iterator = map.entrySet().iterator(); iterator.hasNext();) {
                Map.Entry entry = (Map.Entry) iterator.next();
                
                if ( entry.getValue() == null ) {
                    continue;
                }
                
                ExtendedHierarchicalStreamWriterHelper.startNode(writer, mapper().serializedClass(Map.Entry.class), Map.Entry.class);
        
                writeItem(entry.getKey(), context, writer);
                writeItem(entry.getValue(), context, writer);
        
                writer.endNode();
            }
        }
    }
}
