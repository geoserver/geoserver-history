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
    protected List layers = new ArrayList();

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
    protected List styles = new ArrayList();

    /**
     * listeners
     */
    protected List listeners = new ArrayList();

    /** 
     * resources
     */
    protected ResourcePool resourcePool = new ResourcePool();

    public CatalogFactory getFactory() {
        return new CatalogFactoryImpl( this );
    }

    // Store methods
    public void add(StoreInfo store) {
        
        if ( store.getWorkspace() == null ) {
            store.setWorkspace( getDefaultWorkspace() );
        }
        
        validate(store);
        
        ((StoreInfoImpl)store).setId( store.getName() );
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
        stores.remove(store.getClass(), store);
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
        List l = lookup(clazz, stores);
        for (Iterator i = l.iterator(); i.hasNext();) {
            StoreInfo store = (StoreInfo) i.next();
            if (name.equals(store.getName())) {
                return ModificationProxy.create( (T) store, clazz );
            }
        }

        return null;
    }
    
    public <T extends StoreInfo> List<T> getStoresByWorkspace(
            WorkspaceInfo workspace, Class<T> clazz) {

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
        return (DataStoreInfo) getStoreByName(name, DataStoreInfo.class);
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
        
       ((ResourceInfoImpl)resource).setId( resource.getName() );
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

        List l = lookup(clazz, resources);
        for (Iterator i = l.iterator(); i.hasNext();) {
            ResourceInfo resource = (ResourceInfo) i.next();
            if (name.equals(resource.getName())) {
                if (resource.getNamespace().getPrefix().equals(ns)
                        || resource.getNamespace().getURI().equals(ns)) {
                    return ModificationProxy.create( (T) resource, clazz );
                }
            }
        }

        return null;
    }

    public <T extends ResourceInfo> T getResourceByName( String name, Class<T> clazz ) {
        if ( getDefaultNamespace() != null ) {
            ResourceInfo resource = getResourceByName( getDefaultNamespace().getPrefix(), name, clazz );
            if ( resource != null ) {
                //return ModificationProxy.create( (T) resource, clazz );
                return (T) resource;
            }    
        }
        
        List matches = new ArrayList();
        List l = lookup(clazz, resources);
        for (Iterator i = l.iterator(); i.hasNext();) {
            ResourceInfo resource = (ResourceInfo) i.next();
            if (name.equals(resource.getName())) {
                matches.add( resource );
            }
        }
        
        if ( matches.size() == 1 ) {
            return ModificationProxy.create( (T) matches.get( 0 ), clazz );
        
        }
        return null;
    }
    
    public List getResources(Class clazz) {
        return ModificationProxy.createList( lookup(clazz,resources), clazz );
    }

    public List getResourcesByNamespace(NamespaceInfo namespace, Class clazz) {
        List all = lookup(clazz, resources);
        List matches = new ArrayList();

        for (Iterator r = all.iterator(); r.hasNext();) {
            ResourceInfo resource = (ResourceInfo) r.next();
            if (namespace.equals(resource.getNamespace())) {
                matches.add(resource);
            }
        }

        return ModificationProxy.createList( matches, clazz );
    }

    public <T extends ResourceInfo> List<T> getResourcesByStore(StoreInfo store) {
        List all = lookup(ResourceInfo.class, resources);
        List matches = new ArrayList();
        for ( Iterator r = all.iterator(); r.hasNext(); ) {
            ResourceInfo resource = (ResourceInfo) r.next();
            if ( store.equals( resource.getStore() ) ) {
                matches.add( resource );
            }
        }
        
        return ModificationProxy.createList( matches, ResourceInfo.class );
    }
    
    public FeatureTypeInfo getFeatureType(String id) {
        return (FeatureTypeInfo) getResource(id, FeatureTypeInfo.class);
    }

    public FeatureTypeInfo getFeatureTypeByName(String ns, String name) {
        return (FeatureTypeInfo) getResourceByName(ns, name,
                FeatureTypeInfo.class);
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
    
    public List<FeatureTypeInfo> getFeatureTypesByStore(DataStoreInfo store) {
        return getResourcesByStore(store);
    }

    public CoverageInfo getCoverage(String id) {
        return (CoverageInfo) getResource(id, CoverageInfo.class);
    }

    public CoverageInfo getCoverageByName(String ns, String name) {
        return (CoverageInfo) getResourceByName(ns, name, CoverageInfo.class);
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
        return getResourcesByStore(store);
    }

    // Layer methods
    public void add(LayerInfo layer) {
        validate(layer);
        
        ((LayerInfoImpl)layer).setId( layer.getName() );
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
        layers.remove(layer);
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
        for (Iterator l = layers.iterator(); l.hasNext();) {
            LayerInfo layer = (LayerInfo) l.next();
            if (name.equals(layer.getName())) {
                return ModificationProxy.create( layer, LayerInfo.class );
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
        ((LayerGroupInfoImpl)layerGroup).setId( layerGroup.getName() );
        layerGroups.add( layerGroup );
        added( layerGroup );
    }
    
    public void remove(LayerGroupInfo layerGroup) {
        layerGroups.remove( layerGroup );
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
        ((MapInfoImpl)map).setId(map.getName());
        maps.add(map);
        added(map);
    }

    public void remove(MapInfo map) {
        maps.remove(map);
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
        ((NamespaceInfoImpl)namespace).setId(namespace.getPrefix());
        
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
        
        ((WorkspaceInfoImpl)workspace).setId( workspace.getName());
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
        return Collections.unmodifiableList(styles);
    }

    public void add(StyleInfo style) {
        validate(style);
        
        ((StyleInfoImpl)style).setId( style.getName() );
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
        styles.remove(style);
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
    
    public void dispose() {
        if ( stores != null ) stores.clear();
        if ( resources != null ) resources.clear();
        if ( namespaces != null ) namespaces.clear();
        if ( workspaces != null ) workspaces.clear();
        if ( layers != null ) layers.clear();
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

    protected void sync(Object object) {
//        try {
//            String id = (String) OwsUtils.get( object, "id" );
//            String name = (String) OwsUtils.get( object, "name" );
//            
//            if ( !id.equals(name) ) {
//                //set the id to be the name
//                OwsUtils.set( object, "id", name );
//            }
//        } 
//        catch (Exception e) {
//            
//        }
    }
    
    protected void saved(Object object) {
        //synchronize the id and the name of the object
        sync(object);
        
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
        //TODO: handle for other types of objects when need arises
        
        //resolve all references from store to workspaces
        for ( Object o : stores.values() ) {
            StoreInfoImpl s = (StoreInfoImpl) o;
            s.setWorkspace( ResolvingProxy.resolve( this, s.getWorkspace() ) );
            s.setCatalog( this );
        }
        for ( Object o : styles ) {
            StyleInfoImpl s = (StyleInfoImpl) o;
            s.setCatalog( this );
        }
        
        resources = new MultiHashMap();
        layers = new ArrayList<LayerInfo>();
        layerGroups = new ArrayList<LayerGroupInfo>();
        resourcePool = new ResourcePool();
        listeners = new ArrayList<CatalogListener>();
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
