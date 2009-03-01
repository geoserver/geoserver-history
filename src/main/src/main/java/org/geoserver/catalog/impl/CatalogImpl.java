/* Copyright (c) 2001 - 2008 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.catalog.impl;

import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MultiHashMap;
import org.geoserver.catalog.Catalog;
import org.geoserver.catalog.CatalogFactory;
import org.geoserver.catalog.CoverageInfo;
import org.geoserver.catalog.CoverageStoreInfo;
import org.geoserver.catalog.DataStoreInfo;
import org.geoserver.catalog.FeatureTypeInfo;
import org.geoserver.catalog.LayerGroupInfo;
import org.geoserver.catalog.LayerInfo;
import org.geoserver.catalog.MapInfo;
import org.geoserver.catalog.NamespaceInfo;
import org.geoserver.catalog.ResourceInfo;
import org.geoserver.catalog.ResourcePool;
import org.geoserver.catalog.StoreInfo;
import org.geoserver.catalog.StyleInfo;
import org.geoserver.catalog.WorkspaceInfo;
import org.geoserver.catalog.event.CatalogAddEvent;
import org.geoserver.catalog.event.CatalogEvent;
import org.geoserver.catalog.event.CatalogListener;
import org.geoserver.catalog.event.CatalogModifyEvent;
import org.geoserver.catalog.event.CatalogRemoveEvent;
import org.geoserver.catalog.event.impl.CatalogAddEventImpl;
import org.geoserver.catalog.event.impl.CatalogModifyEventImpl;
import org.geoserver.catalog.event.impl.CatalogRemoveEventImpl;
import org.geoserver.ows.util.OwsUtils;
import org.geoserver.platform.GeoServerResourceLoader;
import org.opengis.feature.type.Name;

/**
 * A default catalog implementation that is memory based.
 * 
 * @author Justin Deoliveira, The Open Planning Project
 * 
 */
public class CatalogImpl implements Catalog {

    /**
     * stores
     */
    protected MultiHashMap/* <Class> */stores = new MultiHashMap();

    /**
     * resources
     */
    protected MultiHashMap/* <Class> */resources = new MultiHashMap();

    /**
     * namespaces
     */
    protected HashMap<String, NamespaceInfo> namespaces = new HashMap<String, NamespaceInfo>();

    /**
     * workspaces
     */
    protected HashMap<String, WorkspaceInfo> workspaces = new HashMap<String, WorkspaceInfo>();
    
    /**
     * layers
     */
    protected List<LayerInfo> layers = new ArrayList();

    /**
     * maps
     */
    protected List<MapInfo> maps = new ArrayList<MapInfo>();

    /**
     * layer groups
     */
    protected List<LayerGroupInfo> layerGroups = new ArrayList<LayerGroupInfo>();
    
    /**
     * styles
     */
    protected List<StyleInfo> styles = new ArrayList();

    /**
     * listeners
     */
    protected List listeners = new ArrayList();

    /** 
     * resources
     */
    protected ResourcePool resourcePool = new ResourcePool();
    protected GeoServerResourceLoader resourceLoader;

    public CatalogFactory getFactory() {
        return new CatalogFactoryImpl( this );
    }

    // Store methods
    public void add(StoreInfo store) {
        
        if ( store.getWorkspace() == null ) {
            store.setWorkspace( getDefaultWorkspace() );
        }

        validate(store);
        resolve(store);
        stores.put(store.getClass(), store);
        added(store);
    }

    void validate(StoreInfo store) {
        if ( store.getName() == null ) {
            throw new IllegalArgumentException( "Store name must not be null");
        }
        if ( store.getWorkspace() == null ) {
            throw new IllegalArgumentException( "Store must be part of a workspace");
        }
    }
    
    public void remove(StoreInfo store) {
        store = unwrap(store);
        stores.remove(store.getClass(),store);
        removed(store);
    }

    public void save(StoreInfo store) {
        validate(store);
        
        if ( store.getId() == null ) {
            //add it instead of saving
            add( store );
            return;
        }
        
        saved(store);
    }

    public <T extends StoreInfo> T getStore(String id, Class<T> clazz) {
        List l = lookup(clazz, stores);
        for (Iterator i = l.iterator(); i.hasNext();) {
            StoreInfo store = (StoreInfo) i.next();
            if (id.equals(store.getId())) {
                return ModificationProxy.create( (T) store, clazz );
                //return store;
            }
        }

        return null;
    }

    public <T extends StoreInfo> T getStoreByName(String name, Class<T> clazz) {
       T store = getStoreByName( (WorkspaceInfo) null, name, clazz );
       if ( store != null ) {
           return store;
       }
       
       //look for secondary match
       List l = lookup(clazz, stores);
       ArrayList matches = new ArrayList();
       for (Iterator i = l.iterator(); i.hasNext();) {
           store = (T) i.next();
           if ( name.equals( store.getName() ) ) {
               matches.add( store );
           }
       }
       
       if ( matches.size() == 1 ) {
           return ModificationProxy.create( (T) matches.get( 0 ), clazz);
       }
       
       return null;
    }

    public <T extends StoreInfo> T getStoreByName(WorkspaceInfo workspace,
            String name, Class<T> clazz) {
        
        if ( workspace == null ) {
            workspace = getDefaultWorkspace();
        }
        
        List l = lookup(clazz, stores);
        for (Iterator i = l.iterator(); i.hasNext();) {
            StoreInfo store = (StoreInfo) i.next();
            if (name.equals(store.getName()) && store.getWorkspace().equals( workspace )) {
                return ModificationProxy.create( (T) store, clazz );
            }
        }
        
        return null;
    }

    public <T extends StoreInfo> T getStoreByName(String workspaceName,
           String name, Class<T> clazz) {
        return getStoreByName(
            workspaceName != null ? getWorkspace(workspaceName) : null, name, clazz);
    }
    
    public <T extends StoreInfo> List<T> getStoresByWorkspace(
            String workspaceName, Class<T> clazz) {
        
        WorkspaceInfo workspace = null;
        if ( workspaceName != null ) {
            workspace = getWorkspaceByName(workspaceName);
            if ( workspace == null ) {
                return Collections.EMPTY_LIST;
            }
        }
        
        return getStoresByWorkspace(workspace, clazz);
        
    }

    public <T extends StoreInfo> List<T> getStoresByWorkspace(
            WorkspaceInfo workspace, Class<T> clazz) {

        if ( workspace == null ) {
            workspace = getDefaultWorkspace();
        }

        List all = lookup(clazz, stores);
        List matches = new ArrayList();

        for (Iterator s = all.iterator(); s.hasNext();) {
            StoreInfo store = (StoreInfo) s.next();
            if (workspace.equals(store.getWorkspace())) {
                matches.add(store);
            }
        }

        return ModificationProxy.createList(matches,clazz);
    }

    public List getStores(Class clazz) {
        return ModificationProxy.createList(lookup(clazz, stores) , clazz);
    }

    public DataStoreInfo getDataStore(String id) {
        return (DataStoreInfo) getStore(id, DataStoreInfo.class);
    }

    public DataStoreInfo getDataStoreByName(String name) {
        return (DataStoreInfo) getStoreByName(name,DataStoreInfo.class);
    }

    public DataStoreInfo getDataStoreByName(String workspaceName, String name) {
        return (DataStoreInfo) getStoreByName(workspaceName, name, DataStoreInfo.class);
    }

    public DataStoreInfo getDataStoreByName(WorkspaceInfo workspace, String name) {
        return (DataStoreInfo) getStoreByName(workspace, name, DataStoreInfo.class);
    }

    public List<DataStoreInfo> getDataStoresByWorkspace(String workspaceName) {
        return getStoresByWorkspace( workspaceName, DataStoreInfo.class );
    }
    
    public List<DataStoreInfo> getDataStoresByWorkspace(WorkspaceInfo workspace) {
        return getStoresByWorkspace( workspace, DataStoreInfo.class );
    }

    public List getDataStores() {
        return getStores(DataStoreInfo.class);
    }

    public CoverageStoreInfo getCoverageStore(String id) {
        return (CoverageStoreInfo) getStore(id, CoverageStoreInfo.class);
    }

    public CoverageStoreInfo getCoverageStoreByName(String name) {
        return (CoverageStoreInfo) getStoreByName(name, CoverageStoreInfo.class);
    }
    
    public CoverageStoreInfo getCoverageStoreByName(String workspaceName,
            String name) {
        return getStoreByName(workspaceName,name,CoverageStoreInfo.class);
    }
    
    public CoverageStoreInfo getCoverageStoreByName(WorkspaceInfo workspace,
            String name) {
        return getStoreByName(workspace, name,CoverageStoreInfo.class);
    }
    
    public List<CoverageStoreInfo> getCoverageStoresByWorkspace(
            String workspaceName) {
        return getStoresByWorkspace( workspaceName, CoverageStoreInfo.class );
    }
    
    public List<CoverageStoreInfo> getCoverageStoresByWorkspace(
            WorkspaceInfo workspace) {
        return getStoresByWorkspace( workspace, CoverageStoreInfo.class );
    }

    public List getCoverageStores() {
        return getStores(CoverageStoreInfo.class);
    }

    // Resource methods
    public void add(ResourceInfo resource) {
        if ( resource.getNamespace() == null ) {
            //default to default namespace
            resource.setNamespace( getDefaultNamespace() );
        }
        
        validate(resource);
        resolve(resource);
        resources.put(resource.getClass(), resource);
        added(resource);
    }

    void validate(ResourceInfo resource) {
        if ( resource.getStore() == null ) {
            throw new IllegalArgumentException( "Resource must be part of a store");
        }
        if ( resource.getNamespace() == null ) {
            throw new IllegalArgumentException( "Resource must be part of a namespace");
        }
    }
    
    public void remove(ResourceInfo resource) {
        resource = unwrap(resource);
        resources.remove(resource.getClass(), resource);
        removed(resource);
    }

    public void save(ResourceInfo resource) {
        validate(resource);
        saved(resource);
    }

    public <T extends ResourceInfo> T getResource(String id, Class<T> clazz) {
        List l = lookup(clazz, resources);
        for (Iterator i = l.iterator(); i.hasNext();) {
            ResourceInfo resource = (ResourceInfo) i.next();
            if (id.equals(resource.getId())) {
                return ModificationProxy.create((T) resource, clazz );
            }
        }

        return null;
    }

    public <T extends ResourceInfo> T getResourceByName(String ns, String name, Class<T> clazz) {

        NamespaceInfo namespace = null;
        if ("".equals( ns ) ) {
            ns = null;
        }
        if ( ns == null ) {
            //if namespace was null, try the default namespace
            if ( getDefaultNamespace() != null ) {
                namespace = getDefaultNamespace();
            }
        }
        else {
            namespace = getNamespaceByPrefix( ns );
            if ( namespace == null ) {
                namespace = getNamespaceByURI( ns );
            }
        }

        List l = lookup(clazz, resources);
        if ( namespace != null ) {
            for (Iterator i = l.iterator(); i.hasNext();) {
                ResourceInfo resource = (ResourceInfo) i.next();
                if (name.equals(resource.getName())) {
                    NamespaceInfo namespace1 = resource.getNamespace();
                    if (namespace1 != null && namespace1.equals( namespace )) {
                            return ModificationProxy.create( (T) resource, clazz );
                    }
                }
            }
        }

        if ( ns == null ) {
            // no namespace was specified, so do an exhaustive lookup
            List matches = new ArrayList();
            for (Iterator i = l.iterator(); i.hasNext();) {
                ResourceInfo resource = (ResourceInfo) i.next();
                if (name.equals(resource.getName())) {
                    matches.add( resource );
                }
            }
            
            if ( matches.size() == 1 ) {
                return ModificationProxy.create( (T) matches.get( 0 ), clazz );
            }
        }
        return null;
    }
    
    public <T extends ResourceInfo> T getResourceByName(NamespaceInfo ns,
            String name, Class<T> clazz) {
        return getResourceByName( ns != null ? ns.getPrefix() : null , name, clazz);
    }

    public <T extends ResourceInfo> T getResourceByName(Name name, Class<T> clazz) {
        return getResourceByName( name.getNamespaceURI(), name.getLocalPart(), clazz );
    }
    
    public <T extends ResourceInfo> T getResourceByName( String name, Class<T> clazz ) {
        ResourceInfo resource;
        
        // check is the name is a fully qualified one
        int colon = name.indexOf( ':' );
        if ( colon != -1 ) {
            String ns = name.substring(0, colon);
            String localName = name.substring(colon + 1);
            return getResourceByName(ns, localName, clazz);
        }
        else {
            return getResourceByName((String)null,name,clazz);
        }
    }
    
    public List getResources(Class clazz) {
        return ModificationProxy.createList( lookup(clazz,resources), clazz );
    }

    public List getResourcesByNamespace(NamespaceInfo namespace, Class clazz) {
        List all = lookup(clazz, resources);
        List matches = new ArrayList();

        if ( namespace == null ) {
            namespace = getDefaultNamespace();
        }

        for (Iterator r = all.iterator(); r.hasNext();) {
            ResourceInfo resource = (ResourceInfo) r.next();
            if (namespace != null ) {
                if (namespace.equals(resource.getNamespace())) {
                    matches.add( resource );
                }
            }
            else if ( resource.getNamespace() == null ) {
                matches.add(resource);
            }
        }

        return ModificationProxy.createList( matches, clazz );
    }
    
    public <T extends ResourceInfo> List<T> getResourcesByNamespace(
            String namespace, Class<T> clazz) {
        if ( namespace == null ) {
            return getResourcesByNamespace((NamespaceInfo)null,clazz); 
        }
        
        NamespaceInfo ns = getNamespace(namespace);
        if ( ns == null ) {
            return Collections.EMPTY_LIST;
        }
        
        return getResourcesByNamespace(ns, clazz);
    }

    public <T extends ResourceInfo> T getResourceByStore(StoreInfo store,
            String name, Class<T> clazz) {
        List all = lookup(clazz,resources);
        for (Iterator r = all.iterator(); r.hasNext(); ) {
            ResourceInfo resource = (ResourceInfo) r.next();
            if ( name.equals( resource.getName() ) && store.equals( resource.getStore() ) ) {
                return ModificationProxy.create((T)resource, clazz);
            }
                
        }
        
        return null;
    }

    public <T extends ResourceInfo> List<T> getResourcesByStore(
            StoreInfo store, Class<T> clazz) {
        List all = lookup(clazz,resources);
        List matches = new ArrayList();
        
        for (Iterator r = all.iterator(); r.hasNext();) {
            ResourceInfo resource = (ResourceInfo) r.next();
            if (store.equals(resource.getStore())) {
                matches.add(resource);
            }
        }

        return  ModificationProxy.createList( matches, clazz );
    }

    public FeatureTypeInfo getFeatureType(String id) {
        return (FeatureTypeInfo) getResource(id, FeatureTypeInfo.class);
    }

    public FeatureTypeInfo getFeatureTypeByName(String ns, String name) {
        return (FeatureTypeInfo) getResourceByName(ns, name,
                FeatureTypeInfo.class);
    }

    public FeatureTypeInfo getFeatureTypeByName(NamespaceInfo ns, String name) {
        return getResourceByName(ns, name, FeatureTypeInfo.class );
    }

    public FeatureTypeInfo getFeatureTypeByName(Name name) {
        return getResourceByName(name, FeatureTypeInfo.class);
    }
    
    public FeatureTypeInfo getFeatureTypeByName(String name) {
        return (FeatureTypeInfo) getResourceByName(name, FeatureTypeInfo.class);
    }
    
    public List getFeatureTypes() {
        return getResources(FeatureTypeInfo.class);
    }

    public List getFeatureTypesByNamespace(NamespaceInfo namespace) {
        return getResourcesByNamespace(namespace, FeatureTypeInfo.class);
    }
    
    public FeatureTypeInfo getFeatureTypeByStore(DataStoreInfo dataStore,
            String name) {
        return getFeatureTypeByDataStore(dataStore, name);
    }
    
    public FeatureTypeInfo getFeatureTypeByDataStore(DataStoreInfo dataStore,
            String name) {
        return getResourceByStore( dataStore, name, FeatureTypeInfo.class );
    }
    
    public List<FeatureTypeInfo> getFeatureTypesByStore(DataStoreInfo store) {
        return getFeatureTypesByDataStore(store);
    }

    public List<FeatureTypeInfo> getFeatureTypesByDataStore(DataStoreInfo store) {
        return getResourcesByStore(store, FeatureTypeInfo.class);
    }

    public CoverageInfo getCoverage(String id) {
        return (CoverageInfo) getResource(id, CoverageInfo.class);
    }

    public CoverageInfo getCoverageByName(String ns, String name) {
        return (CoverageInfo) getResourceByName(ns, name, CoverageInfo.class);
    }
    
    public CoverageInfo getCoverageByName(NamespaceInfo ns, String name) {
        return (CoverageInfo) getResourceByName(ns, name, CoverageInfo.class);
    }
    
    public CoverageInfo getCoverageByName(Name name) {
        return getResourceByName(name, CoverageInfo.class);
    }
    
    public CoverageInfo getCoverageByName(String name) {
        return (CoverageInfo) getResourceByName( name, CoverageInfo.class );
    }

    public List getCoverages() {
        return getResources(CoverageInfo.class);
    }

    public List getCoveragesByNamespace(NamespaceInfo namespace) {
        return getResourcesByNamespace(namespace, CoverageInfo.class);
    }
    
    public List<CoverageInfo> getCoveragesByStore(CoverageStoreInfo store) {
        return getResourcesByStore(store,CoverageInfo.class);
    }
    
    public CoverageInfo getCoverageByCoverageStore(
            CoverageStoreInfo coverageStore, String name) {
        return getResourceByStore( coverageStore, name, CoverageInfo.class );
    }
    public List<CoverageInfo> getCoveragesByCoverageStore(
            CoverageStoreInfo store) {
        return getResourcesByStore( store, CoverageInfo.class );
    }

    // Layer methods
    public void add(LayerInfo layer) {
        validate(layer);
        resolve(layer);
        
        if ( layer.getType() == null ) {
            if ( layer.getResource() instanceof FeatureTypeInfo ) {
                layer.setType( LayerInfo.Type.VECTOR );
            }
            else if ( layer.getResource() instanceof CoverageInfo ) {
                layer.setType( LayerInfo.Type.RASTER );
            }
            else {
                String msg = "Layer type not set and can't be derived from resource";
                throw new IllegalArgumentException( msg );
            }
        }
        
        layers.add(layer);
        added(layer);
    }

    void validate( LayerInfo layer ) {
        if ( layer.getName() == null ) {
            throw new NullPointerException( "Layer name must not be null" );
        }
        if ( layer.getResource() == null ) {
            throw new NullPointerException( "Layer resource must not be null" );
        }
        //(JD): not sure if default style should be mandatory
        //if ( layer.getDefaultStyle() == null ){
        //    throw new NullPointerException( "Layer default style must not be null" );
        //}
    }
    
    public void remove(LayerInfo layer) {
        layers.remove(unwrap(layer));
        removed(layer);
    }

    public void save(LayerInfo layer) {
        validate( layer );
        saved(layer);
    }

    public LayerInfo getLayer(String id) {
        for (Iterator l = layers.iterator(); l.hasNext();) {
            LayerInfo layer = (LayerInfo) l.next();
            if (id.equals(layer.getId())) {
                return ModificationProxy.create( layer, LayerInfo.class );
            }
        }

        return null;
    }
    
    public LayerInfo getLayerByName(String name) {
        String prefix = null;
        String resource = null;
        
        int colon = name.indexOf( ':' );
        if ( colon != -1 ) {
            //search by resource name
            prefix = name.substring( 0, colon );
            resource = name.substring( colon + 1 );
            
            for (Iterator l = layers.iterator(); l.hasNext();) {
                LayerInfo layer = (LayerInfo) l.next();
                ResourceInfo r = layer.getResource();
                
                if ( prefix.equals( r.getNamespace().getPrefix() ) && resource.equals( r.getName() ) ) {
                    return ModificationProxy.create( layer, LayerInfo.class );
                }
            }
        }
        else {
            //search by layer name
            for (Iterator l = layers.iterator(); l.hasNext();) {
                LayerInfo layer = (LayerInfo) l.next();
                if ( name.equals( layer.getName() ) ) {
                    return ModificationProxy.create( layer, LayerInfo.class );
                }
            }
        }

        return null;
    }

    public List<LayerInfo> getLayers(ResourceInfo resource) {
        List<LayerInfo> matches = new ArrayList<LayerInfo>();
        for (Iterator l = layers.iterator(); l.hasNext();) {
            LayerInfo layer = (LayerInfo) l.next();
            if ( resource.equals( layer.getResource() ) ) {
                matches.add( layer );
            }
        }

        return ModificationProxy.createList(matches,LayerInfo.class);
    }
    
    public List<LayerInfo> getLayers(StyleInfo style) {
        List<LayerInfo> matches = new ArrayList<LayerInfo>();
        for (Iterator l = layers.iterator(); l.hasNext();) {
            LayerInfo layer = (LayerInfo) l.next();
            if ( style.equals( layer.getDefaultStyle() ) || layer.getStyles().contains( style ) ) {
                matches.add( layer );
            }
        }

        return ModificationProxy.createList(matches,LayerInfo.class);
    }
    
    public List getLayers() {
        return ModificationProxy.createList( new ArrayList(layers), LayerInfo.class );
    }

    // Map methods
    public MapInfo getMap(String id) {
        for (MapInfo map : maps) {
            if (id.equals(map.getId())) {
                return ModificationProxy.create(map,MapInfo.class);
            }
        }

        return null;
    }

    public MapInfo getMapByName(String name) {
        for (MapInfo map : maps) {
            if (name.equals(map.getName())) {
                return ModificationProxy.create(map,MapInfo.class);
            }
        }

        return null;
    }
    
    public List<MapInfo> getMaps() {
        return ModificationProxy.createList( new ArrayList(maps), MapInfo.class );
    }

    public void add(LayerGroupInfo layerGroup) {
        validate(layerGroup);
        resolve(layerGroup);
        
        if ( layerGroup.getStyles().isEmpty() ) {
            for ( LayerInfo l : layerGroup.getLayers() ) {
                layerGroup.getStyles().add( l.getDefaultStyle() );
            }    
        }
        
        layerGroups.add( layerGroup );
        added( layerGroup );
    }
    
    void validate( LayerGroupInfo layerGroup ) {
        if( layerGroup.getName() == null ) {
            throw new NullPointerException( "Layer group name must not be null");
        }
        if ( layerGroup.getLayers() == null || layerGroup.getLayers().isEmpty() ) {
            throw new NullPointerException( "Layer group must not be empty");
        }
        if ( layerGroup.getStyles() != null && !layerGroup.getStyles().isEmpty() && 
            !(layerGroup.getStyles().size() == layerGroup.getLayers().size()) ) {
            throw new IllegalArgumentException( "Layer group has different number of styles than layers");
        }
    }
    
    public void remove(LayerGroupInfo layerGroup) {
        layerGroups.remove( unwrap(layerGroup) );
        removed( layerGroup );
    }
    
    public void save(LayerGroupInfo layerGroup) {
        saved(layerGroup);
    }
    
    public List<LayerGroupInfo> getLayerGroups() {
        return ModificationProxy.createList( new ArrayList(layerGroups), LayerGroupInfo.class );
    }
    
    public LayerGroupInfo getLayerGroup(String id) {
        for (LayerGroupInfo layerGroup : layerGroups ) {
            if ( id.equals( layerGroup.getId() ) ) {
                return ModificationProxy.create(layerGroup,LayerGroupInfo.class);
            }
        }
        
        return null;
    }
    
    public LayerGroupInfo getLayerGroupByName(String name) {
        for (LayerGroupInfo layerGroup : layerGroups ) {
            if ( name.equals( layerGroup.getName() ) ) {
                return ModificationProxy.create(layerGroup,LayerGroupInfo.class);
            }
        }
        
        return null;
    }
    
    public void add(MapInfo map) {
        resolve(map);
        maps.add(map);
        added(map);
    }

    public void remove(MapInfo map) {
        maps.remove(unwrap(map));
        removed(map);
    }

    public void save(MapInfo map) {
        saved( map );
    }
    
    // Namespace methods
    public NamespaceInfo getNamespace(String id) {
        for (NamespaceInfo namespace : namespaces.values() ) {
            if (id.equals(namespace.getId())) {
                return ModificationProxy.create( namespace, NamespaceInfo.class ); 
            }
        }

        return null;
    }

    public NamespaceInfo getNamespaceByPrefix(String prefix) {
        NamespaceInfo ns = namespaces.get( prefix ); 
        return ns != null ? ModificationProxy.create(ns, NamespaceInfo.class ) : null;
    }

    public NamespaceInfo getNamespaceByURI(String uri) {
        for (NamespaceInfo namespace : namespaces.values() ) {
            if (uri.equals(namespace.getURI())) {
                return ModificationProxy.create( namespace, NamespaceInfo.class );
            }
        }

        return null;
    }

    public List getNamespaces() {
        ArrayList<NamespaceInfo> ns = new ArrayList<NamespaceInfo>();
        for ( Map.Entry<String,NamespaceInfo> e : namespaces.entrySet() ) {
            if ( e.getKey() == null ) 
                continue;
            ns.add( e.getValue() );
        }
        
        return ModificationProxy.createList( ns, NamespaceInfo.class );
    }

    public void add(NamespaceInfo namespace) {
        validate(namespace);
        resolve(namespace);
        
        synchronized (namespaces) {
            namespaces.put(namespace.getPrefix(),namespace);
            if ( namespaces.get( null ) == null ) {
                namespaces.put( null, namespace );
            }
        }
        
        added(namespace);
    }

    void validate(NamespaceInfo namespace) {
        if ( namespace.getPrefix() == null ) {
            throw new NullPointerException( "Namespace prefix must not be null");
        }
        if ( namespace.getURI() == null ) {
            throw new NullPointerException( "Namespace uri must not be null");
        }
    }
    
    public void remove(NamespaceInfo namespace) {
        namespaces.remove(namespace.getPrefix());
        removed(namespace);
    }

    public void save(NamespaceInfo namespace) {
        validate(namespace);
        
        ModificationProxy h = 
            (ModificationProxy) Proxy.getInvocationHandler(namespace);
        
        NamespaceInfo ns = (NamespaceInfo) h.getProxyObject();
        if ( !namespace.getPrefix().equals( ns.getPrefix() ) ) {
            synchronized (namespaces) {
                namespaces.remove( ns.getPrefix() );
                namespaces.put( namespace.getPrefix(), ns );
            }
        }
        
        saved(namespace);
    }

    public NamespaceInfo getDefaultNamespace() {
        return namespaces.containsKey(null) ? 
                ModificationProxy.create(namespaces.get( null ),NamespaceInfo.class) : null;
    }

    public void setDefaultNamespace(NamespaceInfo defaultNamespace) {
        NamespaceInfo ns = namespaces.get( defaultNamespace.getPrefix() );
        if ( ns == null ) {
            throw new IllegalArgumentException( "No such namespace: '" + defaultNamespace.getPrefix() + "'" );
        }
        
        namespaces.put( null, ns );
    }

    // Workspace methods
    public void add(WorkspaceInfo workspace) {
        validate(workspace);
        
        if ( workspaces.containsKey( workspace.getName() ) ) {
            throw new IllegalArgumentException( "Workspace with name '" + workspace.getName() + "' already exists.");
        }
        
        resolve(workspace);
        synchronized (workspaces) {
            workspaces.put( workspace.getName(), workspace );
            if ( workspaces.get( null ) == null ) {
                workspaces.put( null, workspace );
            }
        }
        
        added( workspace );
    }
    
    void validate(WorkspaceInfo workspace) {
        if ( workspace.getName() == null ) {
            throw new NullPointerException( "workspace name must not be null");
        }
    }
    
    public void remove(WorkspaceInfo workspace) {
        //JD: maintain the link between namespace and workspace, remove this when this is no 
        // longer necessary
        if ( getNamespaceByPrefix( workspace.getName() ) != null ) {
            throw new IllegalStateException ( "Cannot delete workspace with linked namespace");
        }
        workspaces.remove( workspace.getName() );
        removed( workspace );
    }
    
    public void save(WorkspaceInfo workspace) {
        validate(workspace);
        
        ModificationProxy h = 
            (ModificationProxy) Proxy.getInvocationHandler(workspace);
        
        WorkspaceInfo ws = (WorkspaceInfo) h.getProxyObject();
        if ( !workspace.getName().equals( ws.getName() ) ) {
            synchronized (workspaces) {
                workspaces.remove( ws.getName() );
                workspaces.put( workspace.getName(), ws );
            }
        }
        
        saved(workspace);
    }
    
    public WorkspaceInfo getDefaultWorkspace() {
        return workspaces.containsKey( null ) ? 
                ModificationProxy.create( workspaces.get( null ), WorkspaceInfo.class ) : null;
    }
    
    public void setDefaultWorkspace(WorkspaceInfo workspace) {
        workspaces.put( null, workspace );
    }
    
    public List<WorkspaceInfo> getWorkspaces() {
        ArrayList<WorkspaceInfo> ws = new ArrayList<WorkspaceInfo>();
        
        //strip out default namespace
        for ( Map.Entry<String, WorkspaceInfo> e : workspaces.entrySet() ) {
            if ( e.getKey() == null ) {
                continue;
            }
            
            ws.add( e.getValue() );
        }
        
        return ModificationProxy.createList( ws, WorkspaceInfo.class );
    }
    
    public WorkspaceInfo getWorkspace(String id) {
        for ( WorkspaceInfo ws : workspaces.values() ) {
            if ( id.equals( ws.getId() ) ) {
                return ModificationProxy.create(ws,WorkspaceInfo.class);
            }
        }
        
        return null;
    }
    
    public WorkspaceInfo getWorkspaceByName(String name) {
        return workspaces.containsKey(name) ? 
                ModificationProxy.create( workspaces.get( name ), WorkspaceInfo.class ) : null;
    }
    
    // Style methods
    public StyleInfo getStyle(String id) {
        for (Iterator s = styles.iterator(); s.hasNext();) {
            StyleInfo style = (StyleInfo) s.next();
            if (id.equals(style.getId())) {
                return ModificationProxy.create(style,StyleInfo.class);
            }
        }

        return null;
    }

    public StyleInfo getStyleByName(String name) {
        for (Iterator s = styles.iterator(); s.hasNext();) {
            StyleInfo style = (StyleInfo) s.next();
            if (name.equals(style.getName())) {
                return ModificationProxy.create(style,StyleInfo.class);
            }
        }

        return null;
    }

    public List getStyles() {
        return ModificationProxy.createList(styles,StyleInfo.class);
    }

    public void add(StyleInfo style) {
        validate(style);
        resolve(style);
        styles.add(style);
        added(style);
    }

    void validate( StyleInfo style ) {
        if ( style.getName() == null ) {
            throw new NullPointerException( "Style name must not be null");
        }
        if ( style.getFilename() == null ) {
            throw new NullPointerException( "Style fileName must not be null");
        }
    }
    
    public void remove(StyleInfo style) {
        styles.remove(unwrap(style));
        removed(style);
    }

    public void save(StyleInfo style) {
        validate(style);
        saved( style );
    }

    // Event methods
    public Collection getListeners() {
        return Collections.unmodifiableCollection(listeners);
    }

    public void addListener(CatalogListener listener) {
        listeners.add(listener);

    }

    public void removeListener(CatalogListener listener) {
        listeners.remove(listener);
    }

    public Iterator search(String cql) {
        // TODO Auto-generated method stub
        return null;
    }

    public ResourcePool getResourcePool() {
        return resourcePool;
    }
    
    public void setResourcePool(ResourcePool resourcePool) {
        this.resourcePool = resourcePool;
    }
    
    public GeoServerResourceLoader getResourceLoader() {
        return resourceLoader;
    }
    public void setResourceLoader(GeoServerResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }
    public void dispose() {
        if ( stores != null ) stores.clear();
        if ( resources != null ) resources.clear();
        if ( namespaces != null ) namespaces.clear();
        if ( workspaces != null ) workspaces.clear();
        if ( layers != null ) layers.clear();
        if ( layerGroups != null ) layerGroups.clear();
        if ( maps != null ) maps.clear();
        if ( styles != null ) styles.clear();
        if ( listeners != null ) listeners.clear();
        
        if ( resourcePool != null ) resourcePool.dispose();
    }
    
    List lookup(Class clazz, MultiHashMap map) {
        ArrayList result = new ArrayList();
        for (Iterator k = map.keySet().iterator(); k.hasNext();) {
            Class key = (Class) k.next();
            if (clazz.isAssignableFrom(key)) {
                result.addAll(map.getCollection(key));
            }
        }

        return result;
    }

    protected void added(Object object) {
        fireAdded( object );
    }
    
    protected void fireAdded(Object object) {
        CatalogAddEventImpl event = new CatalogAddEventImpl();
        event.setSource(object);

        event(event);
    }

    protected void saved(Object object) {
        //this object is a proxy
        ModificationProxy h = 
            (ModificationProxy) Proxy.getInvocationHandler(object);
        
        //get the real object
        Object real = h.getProxyObject();
        
        //fire out what changed
        List propertyNames = h.getPropertyNames();
        List newValues = h.getNewValues();
        List oldValues = h.getOldValues();
        
        //TODO: protect this original object, perhaps with another proxy 
        fireModified( real, propertyNames, oldValues, newValues );
        
        //commit to the original object
        h.commit();    
        
        //resolve to do a sync on the object
        syncIdWithName(real);
    }
    
    protected void fireModified(Object object, List propertyNames, List oldValues,
            List newValues) {
        CatalogModifyEventImpl event = new CatalogModifyEventImpl();

        event.setSource(object);
        event.setPropertyNames(propertyNames);
        event.setOldValues(oldValues);
        event.setNewValues(newValues);

        event(event);
    }

    protected void removed(Object object) {
        CatalogRemoveEventImpl event = new CatalogRemoveEventImpl();
        event.setSource(object);

        event(event);
    }

    protected void event(CatalogEvent event) {
        for (Iterator l = listeners.iterator(); l.hasNext();) {
            CatalogListener listener = (CatalogListener) l.next();
            if (event instanceof CatalogAddEvent) {
                listener.handleAddEvent((CatalogAddEvent) event);
            } else if (event instanceof CatalogRemoveEvent) {
                listener.handleRemoveEvent((CatalogRemoveEvent) event);
            } else if (event instanceof CatalogModifyEvent) {
                listener.handleModifyEvent((CatalogModifyEvent) event);
            }
        }
    }
    
    /**
     * Implementation method for resolving all {@link ResolvingProxy} instances.
     */
    public void resolve() {
        //JD creation checks are done here b/c when xstream depersists 
        // some members may be left null
        
        //workspaces
        if ( workspaces == null ) {
            workspaces = new HashMap<String, WorkspaceInfo>();
        }
        for ( WorkspaceInfo ws : workspaces.values() ) {
            resolve(ws);
        }
        
        //namespaces
        if ( namespaces == null ) {
            namespaces = new HashMap<String, NamespaceInfo>();
        }
        for ( NamespaceInfo ns : namespaces.values() ) {
            resolve(ns);
        }
        
        //stores
        if ( stores == null ) {
            stores = new MultiHashMap();
        }
        for ( Object o : stores.values() ) {
            resolve((StoreInfoImpl)o);
        }
        
        //styles
        if ( styles == null ) {
            styles = new ArrayList<StyleInfo>();
        }
        for ( StyleInfo s : styles ) {
            resolve(s);
        }
        
        //resources
        if ( resources == null ) {
            resources = new MultiHashMap();    
        }
        for( Object o : resources.values() ) {
            resolve((ResourceInfo)o);
        }
        
        //layers
        if ( layers == null ) {
            layers = new ArrayList<LayerInfo>();    
        }
        for ( LayerInfo l : layers ) { 
            resolve(l);
        }
        
        //layer groups
        if ( layerGroups == null ) {
            layerGroups = new ArrayList<LayerGroupInfo>();    
        }
        for ( LayerGroupInfo lg : layerGroups ) {
            resolve(lg);
        }
        
        //maps
        if ( maps == null ) {
            maps = new ArrayList<MapInfo>();
        }
        for ( MapInfo m : maps ) {
            resolve(m);
        }
        
        if ( resourcePool == null ) {
            resourcePool = new ResourcePool();    
        }
        
        if ( listeners == null ) {
            listeners = new ArrayList<CatalogListener>();    
        }
    }
    
    protected void resolve(WorkspaceInfo workspace) {
        syncIdWithName(workspace);
    }
    
    protected void resolve(NamespaceInfo namespace) {
        Object prefix = OwsUtils.get( namespace, "prefix");
        OwsUtils.set( namespace, "id", prefix );
    }
    
    protected void resolve(StoreInfo store) {
        syncIdWithName(store);
        StoreInfoImpl s = (StoreInfoImpl) store;
        
        //resolve the workspace
        WorkspaceInfo resolved = ResolvingProxy.resolve( this, s.getWorkspace());
        if ( resolved != null ) {
            s.setWorkspace(  resolved );    
        }
        else {
            //this means the workspace has not yet been added to the catalog, keep the proxy around
        }
        if ( s.getMetadata() == null ) {
            s.setMetadata(new HashMap());
        }
        s.setCatalog( this );
    }

    protected void resolve(ResourceInfo resource) {
        syncIdWithName(resource);
        ResourceInfoImpl r = (ResourceInfoImpl) resource;
        
        //resolve the store
        StoreInfo resolved = ResolvingProxy.resolve( this, r.getStore() );
        if ( resolved != null ) {
            r.setStore( resolved );
        }
        if ( r.getMetadata() == null ) {
            r.setMetadata(new HashMap());
        }
        r.setCatalog(this);
        
        if ( resource instanceof FeatureTypeInfo ) {
            resolve( (FeatureTypeInfo) resource );
        }
    }
    
    protected void resolve(FeatureTypeInfo featureType) {
        FeatureTypeInfoImpl ft = (FeatureTypeInfoImpl) featureType;
        
        if ( ft.getAttributes() == null ) {
            ft.setAttributes( new ArrayList() );
        }
    }

    protected void resolve(LayerInfo layer) {
        syncIdWithName(layer);
    }
    
    protected void resolve(LayerGroupInfo layerGroup) {
        syncIdWithName(layerGroup);
        LayerGroupInfoImpl lg = (LayerGroupInfoImpl) layerGroup;
        
        for ( int i = 0; i < lg.getLayers().size(); i++ ) {
            LayerInfo l = lg.getLayers().get( i );
            LayerInfo resolved = ResolvingProxy.resolve( this, l );
            lg.getLayers().set( i, resolved );
        }
        if ( lg.getStyles() == null ) {
            lg.setStyles(new ArrayList<StyleInfo>());
        }
        for ( int i = 0; i < lg.getStyles().size(); i++ ) {
            StyleInfo s = lg.getStyles().get( i );
            StyleInfo resolved = ResolvingProxy.resolve( this, s );
            lg.getStyles().set( i, resolved );
        }
        
        if ( lg.getMetadata() == null ) {
            lg.setMetadata(new HashMap());
        }
    }
    
    protected void resolve(StyleInfo style) {
        syncIdWithName(style);
        ((StyleInfoImpl)style).setCatalog( this );
    }
    
    protected void resolve(MapInfo map) {
        syncIdWithName(map);
    }
    
    protected void syncIdWithName( Object o ) {
        if ( OwsUtils.getter(o.getClass(), "name", String.class) != null  && 
             OwsUtils.getter(o.getClass(), "id", String.class ) != null ) {
            
            Object name = OwsUtils.get( o, "name");
            OwsUtils.set( o, "id", name);    
        }
    }
    
    public void sync( CatalogImpl other ) {
        stores = other.stores;
        resources = other.resources;
        namespaces = other.namespaces;
        workspaces = other.workspaces;
        layers = other.layers;
        maps = other.maps;
        layerGroups = other.layerGroups;
        styles = other.styles;
        listeners = other.listeners;
        
        if ( resourcePool != other.resourcePool ) {
            resourcePool.dispose();
            resourcePool = other.resourcePool;
        }
    }
    
    public static <T> T unwrap(T obj) {
        return ModificationProxy.unwrap(obj);
    }
    
}
