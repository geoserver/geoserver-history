/* Copyright (c) 2001 - 2008 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.config.util;

import java.awt.geom.AffineTransform;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
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
import org.geoserver.catalog.LayerGroupInfo;
import org.geoserver.catalog.LayerInfo;
import org.geoserver.catalog.MetadataLinkInfo;
import org.geoserver.catalog.NamespaceInfo;
import org.geoserver.catalog.ResourceInfo;
import org.geoserver.catalog.StoreInfo;
import org.geoserver.catalog.StyleInfo;
import org.geoserver.catalog.WorkspaceInfo;
import org.geoserver.catalog.impl.AttributeTypeInfoImpl;
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
import org.geotools.coverage.grid.GeneralGridRange;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.referencing.operation.DefaultMathTransformFactory;
import org.geotools.referencing.operation.matrix.GeneralMatrix;
import org.geotools.util.logging.Logging;
import org.opengis.coverage.grid.GridGeometry;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.MathTransformFactory;
import org.opengis.referencing.operation.Matrix;

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
        xs.alias( "coverage", CoverageInfoImpl.class);
        xs.alias( "coverageDimension", CoverageDimensionImpl.class);
        xs.alias( "metadataLink", MetadataLinkInfo.class);
        xs.alias( "attribute", AttributeTypeInfoImpl.class );
        xs.alias( "layer", LayerInfoImpl.class ); 
        xs.alias( "layerGroup", LayerGroupInfoImpl.class );
        xs.alias( "geographic", DefaultGeographicCRS.class);
        xs.alias( "gridGeometry", GridGeometry2D.class);
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
        xs.omitField(StoreInfoImpl.class, "id");
        xs.omitField(StoreInfoImpl.class, "catalog");
        xs.omitField(StoreInfoImpl.class, "workspace");
        xs.omitField(StoreInfoImpl.class, "metadata");
        xs.omitField(StoreInfoImpl.class, "connectionParameters");
        
        // StyleInfo
        xs.omitField(StyleInfoImpl.class, "id");
        xs.omitField(StyleInfoImpl.class, "catalog");
        
        // ResourceInfo
        xs.omitField( ResourceInfoImpl.class, "id");
        xs.omitField( ResourceInfoImpl.class, "catalog");
        xs.omitField( ResourceInfoImpl.class, "crs" );
        xs.omitField( ResourceInfoImpl.class, "nativeCRS");
        xs.omitField( ResourceInfoImpl.class, "namespace");
        xs.omitField( ResourceInfoImpl.class, "store");
        xs.omitField( ResourceInfoImpl.class, "metadata");
        
        // FeatureTypeInfo
        
        // CoverageInfo
        xs.addDefaultImplementation( GridGeometry2D.class, GridGeometry.class );
        
        // AttributeTypeInfo
        xs.omitField( AttributeTypeInfoImpl.class, "featureType");
        xs.omitField( AttributeTypeInfoImpl.class, "attribute");
        
        // LayerInfo
        xs.omitField( LayerInfoImpl.class, "id");
        xs.omitField( LayerInfoImpl.class, "resource");
        xs.omitField( LayerInfoImpl.class, "defaultStyle");
        xs.omitField( LayerInfoImpl.class, "styles");
        xs.omitField( LayerInfoImpl.class, "metadata");
        
        // LayerGroupInfo
        xs.omitField(LayerGroupInfoImpl.class, "id" );
        xs.omitField(LayerGroupInfoImpl.class, "layers" );
        xs.omitField(LayerGroupInfoImpl.class, "styles" );
        xs.omitField(LayerGroupInfoImpl.class, "metadata");
        
        //ReferencedEnvelop
        xs.omitField( ReferencedEnvelope.class, "crs" );
        xs.omitField( GeneralEnvelope.class, "crs" );
        
        //NamespaceInfo
        xs.omitField( NamespaceInfoImpl.class, "id");
        xs.omitField( NamespaceInfoImpl.class, "catalog");
        
        //WorkspaceInfo
        xs.omitField( WorkspaceInfoImpl.class, "id");
        xs.omitField( WorkspaceInfoImpl.class, "metadata");
        
        // Converters
        xs.registerConverter(new StoreInfoConverter(xs.getMapper(),xs.getReflectionProvider()));
        xs.registerConverter(new ResourceInfoConverter(xs.getMapper(),xs.getReflectionProvider()));
        xs.registerConverter(new FeatureTypeInfoConverter(xs.getMapper(),xs.getReflectionProvider()));
        xs.registerConverter(new LayerInfoConverter(xs.getMapper(),xs.getReflectionProvider()));
        xs.registerConverter(new LayerGroupInfoConverter(xs.getMapper(),xs.getReflectionProvider()));
        xs.registerConverter(new WorkspaceInfoConverter(xs.getMapper(),xs.getReflectionProvider()));
        
        //local converters
        xs.registerLocalConverter(CatalogImpl.class, "stores",
                new MultiHashMapConverter());
        xs.registerLocalConverter(CatalogImpl.class, "namespaces",
                new SpaceMapConverter("namespace"));
        xs.registerLocalConverter(CatalogImpl.class, "workspaces",
                new SpaceMapConverter("workspace"));
        
//        MetadataConverter metadataConverter = new MetadataConverter(xs.getMapper());
//        xs.registerLocalConverter(ResourceInfoImpl.class, "metadata", metadataConverter );
//        xs.registerLocalConverter(StoreInfoImpl.class, "connectionParameters", new MetadataConverter( xs.getMapper() ));
        
        //xs.registerConverter(new CRSConverter());
        xs.registerConverter(new ProxyCollectionConverter( xs.getMapper() ) );
        xs.registerConverter(new ReferencedEnvelopeConverter(xs.getMapper(), xs.getReflectionProvider()));
        xs.registerConverter(new GeneralEnvelopeConverter(xs.getMapper(), xs.getReflectionProvider() ));
        xs.registerConverter(new GridGeometry2DConverter(xs.getMapper(), xs.getReflectionProvider()));
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
    
    static void marshalCollectionReference( Object source, HierarchicalStreamWriter w, String name, String itemName ) {
        Collection c = (Collection) OwsUtils.get( source, name );
        if ( c == null || c.isEmpty() ) {
            return;
        }
        
        w.startNode(name);
        for ( Object o : c ) {
            encodeReference(o, w, itemName);
        }
        w.endNode();
    }
    
    static void marshalReference( Object source, HierarchicalStreamWriter w, String name ) {
        
        Object obj = OwsUtils.get( source, name );
        if ( obj == null ) {
            return;
        }
        
        encodeReference( obj, w, name );
        
    }
    
    static void encodeReference( Object obj, HierarchicalStreamWriter w, String elementName ) {
        //could be a proxy, unwrap it
        obj = CatalogImpl.unwrap( obj );
        
        //gets its id
        String id = (String) OwsUtils.get( obj, "id" );
        
        //use name if no id set
        if ( id == null ) {
            id = (String) OwsUtils.get( obj, "name" );
        }
        
        w.startNode(elementName);
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
            
            encodeMap( "connectionParameters", source, writer, context, getXStream() );
            marshalReference( source, writer, "workspace" );
            encodeMetadata(source, writer, context, getXStream());
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
            } else if ( "metadata".equals( field.getName() ) ) {
                return decodeMetadata( result, context, getXStream() );
            } else if ( "connectionParameters".equals( field.getName() ) ) {
                return decodeMetadata( result, context, getXStream() );
            }
            else {
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
            encodeMetadata( source, writer, context, getXStream() );
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
            
            if ( "metadata".equals( field.getName() ) ) {
                return decodeMetadata( result, context, getXStream() );
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
    
    class GeneralEnvelopeConverter extends ReflectionConverter {
        
        public GeneralEnvelopeConverter(Mapper mapper,
                ReflectionProvider reflectionProvider) {
            super(mapper, reflectionProvider);
        }

        @Override
        public boolean canConvert(Class type) {
            return GeneralEnvelope.class.isAssignableFrom( type );
        }

        @Override
        protected void doMarshal(Object source,
                HierarchicalStreamWriter writer, MarshallingContext context) {
            super.doMarshal(source, writer, context);
            
            GeneralEnvelope ge = (GeneralEnvelope) source;
            //encodeCRS( re.getCoordinateReferenceSystem(), writer, "crs" );
            encodeSRS( ge.getCoordinateReferenceSystem(), writer, "crs" );
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
    
    class GridGeometry2DConverter extends ReflectionConverter {
        public GridGeometry2DConverter(Mapper mapper,
                ReflectionProvider reflectionProvider) {
            super(mapper, reflectionProvider);
        }

        @Override
        public boolean canConvert(Class type) {
            return GridGeometry2D.class.isAssignableFrom( type );
        }

        @Override
        protected void doMarshal(Object source,
                HierarchicalStreamWriter writer, MarshallingContext context) {
         
            GridGeometry2D g = (GridGeometry2D) source;
            MathTransform tx = g.getGridToCRS();

            writer.addAttribute("dimension", String.valueOf(g.getGridRange().getDimension()));
            
            //grid range
            StringBuffer low = new StringBuffer();
            StringBuffer high = new StringBuffer();
            for (int r = 0; r < g.getGridRange().getDimension(); r++) {
                low.append(g.getGridRange().getLower(r)).append(" ");
                high.append(g.getGridRange().getUpper(r)).append(" ");
            }
            low.setLength(low.length()-1);
            high.setLength(high.length()-1);
            
            writer.startNode("range");
            writer.startNode( "low" ); writer.setValue( low.toString() ); writer.endNode();
            writer.startNode( "high" ); writer.setValue( high.toString() ); writer.endNode();
            writer.endNode();
            
            //transform
            if (tx instanceof AffineTransform) {
                AffineTransform atx = (AffineTransform) tx;
                
                writer.startNode("transform");
                writer.startNode("scaleX"); writer.setValue(Double.toString( atx.getScaleX())); writer.endNode();
                writer.startNode("scaleY"); writer.setValue(Double.toString( atx.getScaleY())); writer.endNode();
                writer.startNode("shearX"); writer.setValue(Double.toString( atx.getShearX())); writer.endNode();
                writer.startNode("shearX"); writer.setValue(Double.toString( atx.getShearY())); writer.endNode();
                writer.startNode("translateX"); writer.setValue(Double.toString( atx.getTranslateX())); writer.endNode();
                writer.startNode("translateY"); writer.setValue(Double.toString( atx.getTranslateY())); writer.endNode();
                writer.endNode();
            }
            
            //crs
            encodeSRS(g.getCoordinateReferenceSystem(), writer, "crs");
        }
        
        @Override
        public Object unmarshal(HierarchicalStreamReader reader,
                UnmarshallingContext context) {
             int[] high,low;
            
            //reader.moveDown(); //grid
            
            reader.moveDown(); //range
            
            reader.moveDown(); //low
            low = toIntArray( reader.getValue() );
            reader.moveUp();
            reader.moveDown(); //high
            high = toIntArray( reader.getValue() );
            reader.moveUp();
            
            reader.moveUp(); //range
            
            if ( reader.hasMoreChildren() ) {
                reader.moveDown(); //transform or crs
            }
            
            AffineTransform transform = null;
            if ( "transform".equals( reader.getNodeName() ) ) {
                transform = new AffineTransform();
                double sx,sy,shx,shy,tx,ty;
                
                reader.moveDown(); //scaleX
                sx = Double.parseDouble( reader.getValue() );
                reader.moveUp();
                
                reader.moveDown(); //scaleY
                sy = Double.parseDouble( reader.getValue() );
                reader.moveUp();
                transform.setToScale( sx, sy );
                
                reader.moveDown(); //shearX
                shx = Double.parseDouble( reader.getValue() );
                reader.moveUp();
                
                reader.moveDown(); //shearY
                shy = Double.parseDouble( reader.getValue() );
                reader.moveUp();
                transform.setToScale( shx, shy );
                
                reader.moveDown(); //translateX
                tx = Double.parseDouble( reader.getValue() );
                reader.moveUp();
                
                reader.moveDown(); //translateY
                ty = Double.parseDouble( reader.getValue() );
                reader.moveUp();
                transform.setToTranslation(tx, ty);
                
                reader.moveUp();
                if ( reader.hasMoreChildren() ) {
                    reader.moveDown(); //crs
                }
            }
            
            CoordinateReferenceSystem crs = null;
            if ( "crs".equals( reader.getNodeName() ) ) {
                crs = decodeCRS(reader);
                reader.moveUp();
            }
            
            // new grid range
            GeneralGridRange gridRange = new GeneralGridRange(low, high);
            
            // grid to crs
            MathTransform gridToCRS = null;
            if ( transform != null ) {
                Matrix matrix = new GeneralMatrix(transform);
                final MathTransformFactory factory = new DefaultMathTransformFactory();
                try {
                    gridToCRS = factory.createAffineTransform(matrix);
                } 
                catch (FactoryException e) {
                    throw new RuntimeException( e );
                }

            }
            
            GridGeometry2D gg = new GridGeometry2D( gridRange, gridToCRS, crs );
            return serializationMethodInvoker.callReadResolve(gg);
        }
            
        int[] toIntArray( String s ) {
            String[] split = s.split( " " );
            int[] ints = new int[split.length];
            for ( int i = 0; i < split.length; i++ ) {
                ints[i] = Integer.parseInt( split[i] );
            }
            return ints;
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

    CoordinateReferenceSystem decodeCRS( HierarchicalStreamReader reader ) {
        return decodeCRS( reader.getValue() );
    }
    
    CoordinateReferenceSystem decodeCRS( UnmarshallingContext context, Object result) {
        //get the string value
        String val = (String) context.convertAnother(result, String.class);
        return decodeCRS( val );
       
    }
    
    CoordinateReferenceSystem decodeCRS( String val ) {
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

    void encodeMetadata( Object o, HierarchicalStreamWriter writer, MarshallingContext context, XStream xs ) {
       encodeMap( "metadata", o, writer, context, xs );
    }
    
    void encodeMap( String name, Object o, HierarchicalStreamWriter writer, MarshallingContext context, XStream xs ) {
        Map map = (Map) OwsUtils.get( o, name);
        if ( map != null && !map.isEmpty() ) {
            writer.startNode(name);
            context.convertAnother( map, new MetadataConverter( xs.getMapper() ));
            writer.endNode();
        }
    }
    
    Map decodeMetadata( Object obj, UnmarshallingContext context, XStream xs ) {
        return (Map) context.convertAnother( obj, Map.class, new MetadataConverter( xs.getMapper() ) );
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
            marshalCollectionReference( source, writer, "styles", "style");
            
            encodeMetadata(source, writer, context, getXStream());
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
                return handleStyleRef( ref );
            }
            
            if ( "styles".equals( field.getName() ) ) {
                Collection refs = (Collection) context.convertAnother( result, field.getType(), new StringCollectionConverter( getXStream().getMapper() ) );
                Collection styles;
                try {
                    styles = refs.getClass().newInstance();
                } 
                catch( Exception e ) {
                    styles = new HashSet();
                }
                
                for ( Iterator i = refs.iterator(); i.hasNext(); ) {
                    String ref = (String) i.next();
                    styles.add( handleStyleRef(ref) );
                }
                
                return styles;
            }
            if ( "metadata".equals( field.getName() ) ) {
                return decodeMetadata( result, context, getXStream() );
            }
            return super.unmarshallField(context, result, type, field);
        }
        
        StyleInfo handleStyleRef( String ref ) {
            if ( catalog != null ) {
                StyleInfo s = catalog.getStyle( ref );
                if ( s != null ) {
                    return CatalogImpl.unwrap(s);
                }
            }
            return ResolvingProxy.create( ref, StyleInfo.class );
        }
    }
    
    class StringCollectionConverter extends CollectionConverter {

        public StringCollectionConverter(Mapper mapper) {
            super(mapper);
        }
        
        @Override
        protected Object readItem(HierarchicalStreamReader reader,
                UnmarshallingContext context, Object current) {
            return reader.getValue();
        }
    }
    
    class LayerGroupInfoConverter extends ReflectionConverter {

        public LayerGroupInfoConverter(Mapper mapper,
                ReflectionProvider reflectionProvider) {
            super(mapper, reflectionProvider);
        }
        
        @Override
        public boolean canConvert(Class type) {
            return LayerGroupInfoImpl.class.isAssignableFrom( type );
        }
        
        protected void doMarshal(Object source,
                HierarchicalStreamWriter writer, MarshallingContext context) {
            super.doMarshal(source, writer, context);
            
            LayerGroupInfo lg = (LayerGroupInfo) source;
            
            writer.startNode("layers");
            for ( LayerInfo l : lg.getLayers() ){
                writer.startNode("layer");
                writer.setValue( l.getName() );
                writer.endNode();
            }
            writer.endNode();
            
            writer.startNode("styles");
            for ( StyleInfo s : lg.getStyles() ){
                writer.startNode("style");
                writer.setValue( s.getName() );
                writer.endNode();
            }
            writer.endNode();
            
            encodeMetadata(source, writer, context, getXStream());
        }
        
        @Override
        protected Object unmarshallField(UnmarshallingContext context,
                Object result, Class type, Field field) {
            
            if ( "layers".equals( field.getName()  ) ) {
                return context.convertAnother( result, List.class, new RefCollectionConverter( getXStream().getMapper() ) {
                    @Override
                    protected Object handleRef(String ref) {
                        if ( catalog != null ) {
                            LayerInfo l = catalog.getLayerByName( ref );
                            if ( l != null ) {
                                return l;
                            }
                        }
                        
                        return ResolvingProxy.create( ref, LayerInfo.class );
                    }
                });
            }
            else if ( "styles".equals( field.getName()  ) ) {
                return context.convertAnother( result, List.class, 
                    new RefCollectionConverter( getXStream().getMapper()) {
                        @Override
                        protected Object handleRef(String ref) {
                            if ( catalog != null ) {
                                StyleInfo s = catalog.getStyleByName( ref );
                                if ( s != null ) {
                                    return s;
                                }
                            }
                            
                            return ResolvingProxy.create( ref, StyleInfo.class );
                        }
                });
            }
            else if ( "metadata".equals( field.getName() ) ) {
                return decodeMetadata(result, context, getXStream() );
            }
            else {
                return super.unmarshallField(context, result, type, field);
            }
        }
    }
    
    class WorkspaceInfoConverter extends ReflectionConverter {

        public WorkspaceInfoConverter(Mapper mapper,
                ReflectionProvider reflectionProvider) {
            super(mapper, reflectionProvider);
        }
        
        @Override
        public boolean canConvert(Class type) {
            return WorkspaceInfo.class.isAssignableFrom(type);
        }
        
        @Override
        protected void doMarshal(Object source,
                HierarchicalStreamWriter writer, MarshallingContext context) {
            super.doMarshal(source, writer, context);
            encodeMetadata( source, writer, context, getXStream() );
        }
        
        @Override
        protected Object unmarshallField(UnmarshallingContext context,
                Object result, Class type, Field field) {
            
            if ( "metadata".equals( field.getName() ) ) {
                return decodeMetadata( result, context, getXStream() );
            }
            
            return super.unmarshallField(context, result, type, field);
        }
    }
    abstract class RefCollectionConverter extends CollectionConverter {

        public RefCollectionConverter(Mapper mapper) {
            super(mapper);
        }
        
        @Override
        protected void populateCollection(HierarchicalStreamReader reader,
                UnmarshallingContext context, Collection collection) {
            while( reader.hasMoreChildren() ) {
                reader.moveDown();
                collection.add( handleRef( reader.getValue() ) );
                reader.moveUp();
            }
        }
        
        protected abstract Object handleRef( String ref );    
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
                
                writer.startNode(entry.getKey().toString());
                writeItem( entry.getValue(), context, writer );
                writer.endNode();
            }
        }
        
        @Override
        protected void populateMap(HierarchicalStreamReader reader,
                UnmarshallingContext context, Map map) {
            while (reader.hasMoreChildren()) {
                reader.moveDown();

                Object key = reader.getNodeName();
                reader.moveDown();
                
                Object value = readItem(reader, context, map);
                reader.moveUp();

                map.put(key, value);

                reader.moveUp();
            }
        }
    }
}
