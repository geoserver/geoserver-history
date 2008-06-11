/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.acegisecurity.Authentication;
import org.acegisecurity.context.SecurityContextHolder;
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
import org.geoserver.catalog.event.CatalogListener;
import org.geoserver.catalog.impl.AbstractDecorator;
import org.geoserver.platform.GeoServerExtensions;
import org.geoserver.security.decorators.ReadOnlyDataStoreInfo;
import org.geoserver.security.decorators.ReadOnlyFeatureTypeInfo;
import org.geoserver.security.decorators.ReadOnlyLayerGroupInfo;
import org.geoserver.security.decorators.ReadOnlyLayerInfo;

/**
 * 
 * @author Andrea Aime - TOPP TODO: - docs - uniform argument order in helper
 *         methods - create wrappers around the returned values so that they can
 *         be used only within the limits of the current user power - move the
 *         layer group checks into this class out of DataAccessManager? - does
 *         administration goes thru this catalog, or directly accesses the
 *         insecured one? Option one, admin directly accesses. Option two, admin
 *         users get a special role that make them uber-powerful (root like) and
 *         everything goes thru the secured catalog
 */
public class SecureCatalogImpl extends AbstractDecorator<Catalog> implements Catalog {
    protected DataAccessManager accessManager;

    public SecureCatalogImpl(Catalog catalog) throws Exception {
        this(catalog, lookupDataAccessManager(catalog));
    }

    static DataAccessManager lookupDataAccessManager(Catalog catalog) throws Exception {
        DataAccessManager manager = GeoServerExtensions.bean(DataAccessManager.class);
        if (manager == null) {
            manager = new DefaultDataAccessManager(catalog);
        }
        return manager;
    }

    public SecureCatalogImpl(Catalog catalog, DataAccessManager manager) {
        super(catalog);
        this.accessManager = manager;
    }

    // -------------------------------------------------------------------
    // SECURED METHODS
    // -------------------------------------------------------------------

    public CoverageInfo getCoverage(String id) {
        return (CoverageInfo) checkAccess(user(), delegate.getCoverage(id));
    }

    public CoverageInfo getCoverageByName(String ns, String name) {
        return (CoverageInfo) checkAccess(user(), delegate.getCoverageByName(ns, name));
    }

    public CoverageInfo getCoverageByName(String name) {
        return (CoverageInfo) checkAccess(user(), delegate.getCoverageByName(name));
    }

    public List<CoverageInfo> getCoverages() {
        return filterResources(user(), delegate.getCoverages());
    }

    public List<CoverageInfo> getCoveragesByNamespace(NamespaceInfo namespace) {
        return filterResources(user(), delegate.getCoveragesByNamespace(namespace));
    }

    public CoverageStoreInfo getCoverageStore(String id) {
        return checkAccess(user(), delegate.getCoverageStore(id));
    }

    public CoverageStoreInfo getCoverageStoreByName(String name) {
        return checkAccess(user(), delegate.getCoverageStoreByName(name));
    }

    public List<CoverageStoreInfo> getCoverageStores() {
        return filterStores(user(), delegate.getCoverageStores());
    }

    public DataStoreInfo getDataStore(String id) {
        return checkAccess(user(), delegate.getDataStore(id));
    }

    public DataStoreInfo getDataStoreByName(String name) {
        return checkAccess(user(), delegate.getDataStoreByName(name));
    }

    public List<DataStoreInfo> getDataStores() {
        return filterStores(user(), delegate.getDataStores());
    }

    public NamespaceInfo getDefaultNamespace() {
        return delegate.getDefaultNamespace();
    }

    public WorkspaceInfo getDefaultWorkspace() {
        return delegate.getDefaultWorkspace();
    }

    public FeatureTypeInfo getFeatureType(String id) {
        return checkAccess(user(), delegate.getFeatureType(id));
    }

    public FeatureTypeInfo getFeatureTypeByName(String ns, String name) {
        return checkAccess(user(), delegate.getFeatureTypeByName(ns, name));
    }

    public FeatureTypeInfo getFeatureTypeByName(String name) {
        return checkAccess(user(), delegate.getFeatureTypeByName(name));
    }

    public List<FeatureTypeInfo> getFeatureTypes() {
        return filterResources(user(), delegate.getFeatureTypes());
    }

    public List<FeatureTypeInfo> getFeatureTypesByNamespace(NamespaceInfo namespace) {
        return filterResources(user(), delegate.getFeatureTypesByNamespace(namespace));
    }

    public LayerInfo getLayer(String id) {
        return checkAccess(user(), delegate.getLayer(id));
    }

    public LayerInfo getLayerByName(String name) {
        return checkAccess(user(), delegate.getLayerByName(name));
    }

    public LayerGroupInfo getLayerGroup(String id) {
        return checkAccess(user(), delegate.getLayerGroup(id));
    }

    public LayerGroupInfo getLayerGroupByName(String name) {
        return checkAccess(user(), delegate.getLayerGroupByName(name));
    }

    public List<LayerGroupInfo> getLayerGroups() {
        return filterGroups(user(), delegate.getLayerGroups());
    }

    public List<LayerInfo> getLayers() {
        return filterLayers(user(), delegate.getLayers());
    }

    public List<LayerInfo> getLayers(ResourceInfo resource) {
        return filterLayers(user(), delegate.getLayers(unwrap(resource)));
    }

    public NamespaceInfo getNamespace(String id) {
        return checkAccess(user(), delegate.getNamespace(id));
    }

    public NamespaceInfo getNamespaceByPrefix(String prefix) {
        return checkAccess(user(), delegate.getNamespaceByPrefix(prefix));
    }

    public NamespaceInfo getNamespaceByURI(String uri) {
        return checkAccess(user(), delegate.getNamespaceByURI(uri));
    }

    public List<NamespaceInfo> getNamespaces() {
        return filterNamespaces(user(), delegate.getNamespaces());
    }

    public <T extends ResourceInfo> T getResource(String id, Class<T> clazz) {
        return checkAccess(user(), delegate.getResource(id, clazz));
    }

    public <T extends ResourceInfo> T getResourceByName(String name, Class<T> clazz) {
        return checkAccess(user(), delegate.getResourceByName(name, clazz));
    }

    public <T extends ResourceInfo> T getResourceByName(String ns, String name, Class<T> clazz) {
        return checkAccess(user(), delegate.getResourceByName(ns, name, clazz));
    }

    public <T extends ResourceInfo> List<T> getResources(Class<T> clazz) {
        return filterResources(user(), delegate.getResources(clazz));
    }

    public <T extends ResourceInfo> List<T> getResourcesByNamespace(NamespaceInfo namespace,
            Class<T> clazz) {
        return filterResources(user(), delegate.getResourcesByNamespace(namespace, clazz));
    }

    public <T extends StoreInfo> T getStore(String id, Class<T> clazz) {
        return checkAccess(user(), delegate.getStore(id, clazz));
    }

    public <T extends StoreInfo> T getStoreByName(String name, Class<T> clazz) {
        return checkAccess(user(), delegate.getStoreByName(name, clazz));
    }

    public <T extends StoreInfo> List<T> getStores(Class<T> clazz) {
        return filterStores(user(), delegate.getStores(clazz));
    }

    public <T extends StoreInfo> List<T> getStoresByWorkspace(WorkspaceInfo workspace,
            Class<T> clazz) {
        return filterStores(user(), delegate.getStoresByWorkspace(workspace, clazz));
    }

    public WorkspaceInfo getWorkspace(String id) {
        return checkAccess(user(), delegate.getWorkspace(id));
    }

    public WorkspaceInfo getWorkspaceByName(String name) {
        return checkAccess(user(), delegate.getWorkspaceByName(name));
    }

    public List<WorkspaceInfo> getWorkspaces() {
        return filterWorkspaces(user(), delegate.getWorkspaces());
    }

    // -------------------------------------------------------------------
    // Security support method
    // -------------------------------------------------------------------

    protected Authentication user() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    /**
     * Given a {@link FeatureTypeInfo} and a user, returns it back if the user
     * can access it in write mode, makes it read only if the user can access it
     * in read only mode, returns null otherwise
     * 
     * @return
     */
    protected <T extends ResourceInfo> T checkAccess(Authentication user, T info) {
        if (info == null)
            return null;

        // return the resource if the user can read and write it, 
        if (accessManager.canAccess(user, info, AccessMode.READ))
            if (accessManager.canAccess(user, info, AccessMode.WRITE)) {
                return info;
            } else if(info instanceof FeatureTypeInfo) { 
                return (T) new ReadOnlyFeatureTypeInfo((FeatureTypeInfo) info);
            } else if(info instanceof CoverageInfo) {
                return info; // coverages are read only so far
            } else {
                throw new RuntimeException("Unknown resource type " + info.getClass());
            }
        else
            return null;
    }

    /**
     * Given a store and a user, returns it back if the user can access its
     * workspace in read mode, null otherwise
     * 
     * @return
     */
    protected <T extends StoreInfo> T checkAccess(Authentication user, T store) {
        if (store == null)
            return null;

        // return the store if the user can read in its workspace, it's not
        // really there otherwise
        if (accessManager.canAccess(user, store.getWorkspace(), AccessMode.READ))
            if (accessManager.canAccess(user, store.getWorkspace(), AccessMode.WRITE)) {
                return store;
            } else if(store instanceof DataStoreInfo) { 
                return (T) new ReadOnlyDataStoreInfo((DataStoreInfo) store);
            } else if(store instanceof CoverageStoreInfo) {
                return store; // coverages stores are read only so far
            } else {
                throw new RuntimeException("Unknown store type " + store.getClass());
            }
        else
            return null;
    }

    /**
     * Given a layer and a user, returns it back if the user can access it, null
     * otherwise
     * 
     * @return
     */
    protected LayerInfo checkAccess(Authentication user, LayerInfo layer) {
        if (layer == null)
            return null;

        // return the layer if the user can read in its workspace, it's not
        // really there otherwise
        if (accessManager.canAccess(user, layer, AccessMode.READ))
            if (accessManager.canAccess(user, layer, AccessMode.WRITE)) {
                return layer;
            } else {
                return new ReadOnlyLayerInfo(layer);
            }
        else
            return null;
    }

    /**
     * Given a layer group and a user, returns it back if the user can access
     * it, null otherwise
     * 
     * @return
     */
    protected LayerGroupInfo checkAccess(Authentication user, LayerGroupInfo group) {
        if (group == null)
            return null;

        // scan thru the layers, if any cannot be accessed, we hide the group, otherwise
        // we return the group back, eventually wrapping the read only layers
        final List<LayerInfo> layers = group.getLayers();
        ArrayList<LayerInfo> wrapped = new ArrayList<LayerInfo>(layers.size());
        boolean needsWrapping = false;
        for (LayerInfo layer : layers) {
            LayerInfo checked = checkAccess(user, layer);
            if(checked == null)
                return null;
            else if(checked != null) 
                needsWrapping = true;
            wrapped.add(checked);
        }
        
        if(needsWrapping)
            return new ReadOnlyLayerGroupInfo(group, wrapped);
        else
            return group;
    }

    /**
     * Given a namespace and user, returns it back if the user can access it,
     * null otherwise
     * 
     * @return
     */
    protected <T extends NamespaceInfo> T checkAccess(Authentication user, T ns) {
        // route the security check thru the associated workspace info
        WorkspaceInfo info = checkAccess(user, delegate.getWorkspace(ns.getPrefix()));
        if (info == null)
            return null;
        else
            return ns;
    }

    /**
     * Given a workspace and user, returns it back if the user can access it,
     * null otherwise
     * 
     * @return
     */
    protected <T extends WorkspaceInfo> T checkAccess(Authentication user, T ws) {
        if (ws == null)
            return null;

        // return the ws if the user can access it, otherwise null
        if (accessManager.canAccess(user, ws, AccessMode.READ))
            return ws;
        else
            return null;
    }

    /**
     * Given a list of resources, returns a copy of it containing only the
     * resources the user can access
     * 
     * @param user
     * @param resources
     * 
     * @return
     */
    protected <T extends ResourceInfo> List<T> filterResources(Authentication user,
            List<T> resources) {
        List<T> result = new ArrayList<T>();
        for (T original : resources) {
            T secured = checkAccess(user, original);
            if (secured != null)
                result.add(secured);
        }
        return result;
    }

    /**
     * Given a list of stores, returns a copy of it containing only the
     * resources the user can access
     * 
     * @param user
     * @param resources
     * 
     * @return
     */
    protected <T extends StoreInfo> List<T> filterStores(Authentication user, List<T> resources) {
        List<T> result = new ArrayList<T>();
        for (T original : resources) {
            T secured = checkAccess(user, original);
            if (secured != null)
                result.add(secured);
        }
        return result;
    }

    /**
     * Given a list of layer groups, returns a copy of it containing only the
     * groups the user can access
     * 
     * @param user
     * @param groups
     * 
     * @return
     */
    protected List<LayerGroupInfo> filterGroups(Authentication user, List<LayerGroupInfo> groups) {
        List<LayerGroupInfo> result = new ArrayList<LayerGroupInfo>();
        for (LayerGroupInfo original : groups) {
            LayerGroupInfo secured = checkAccess(user, original);
            if (secured != null)
                result.add(secured);
        }
        return result;
    }

    /**
     * Given a list of layers, returns a copy of it containing only the layers
     * the user can access
     * 
     * @param user
     * @param layers
     * 
     * @return
     */
    protected List<LayerInfo> filterLayers(Authentication user, List<LayerInfo> layers) {
        List<LayerInfo> result = new ArrayList<LayerInfo>();
        for (LayerInfo original : layers) {
            LayerInfo secured = checkAccess(user, original);
            if (secured != null)
                result.add(secured);
        }
        return result;
    }

    /**
     * Given a list of namespaces, returns a copy of it containing only the
     * namespaces the user can access
     * 
     * @param user
     * @param namespaces
     * 
     * @return
     */
    protected <T extends NamespaceInfo> List<T> filterNamespaces(Authentication user,
            List<T> namespaces) {
        List<T> result = new ArrayList<T>();
        for (T original : namespaces) {
            T secured = checkAccess(user, original);
            if (secured != null)
                result.add(secured);
        }
        return result;
    }

    /**
     * Given a list of workspaces, returns a copy of it containing only the
     * workspaces the user can access
     * 
     * @param user
     * @param namespaces
     * 
     * @return
     */
    protected <T extends WorkspaceInfo> List<T> filterWorkspaces(Authentication user,
            List<T> workspaces) {
        List<T> result = new ArrayList<T>();
        for (T original : workspaces) {
            T secured = checkAccess(user, original);
            if (secured != null)
                result.add(secured);
        }
        return result;
    }
    
    // -------------------------------------------------------------------
    // Unwrappers, used to make sure the lower level does not get hit by
    // read only wrappers
    // -------------------------------------------------------------------
    
    LayerGroupInfo unwrap(LayerGroupInfo layerGroup) {
        if(layerGroup instanceof ReadOnlyLayerGroupInfo)
            return ((ReadOnlyLayerGroupInfo) layerGroup).unwrap(LayerGroupInfo.class);
        return layerGroup;
    }
    
    LayerInfo unwrap(LayerInfo layer) {
        if(layer instanceof ReadOnlyLayerInfo)
            return ((ReadOnlyLayerInfo) layer).unwrap(LayerInfo.class);
        return layer;
    }
    
    ResourceInfo unwrap(ResourceInfo info) {
        if(info instanceof ReadOnlyFeatureTypeInfo)
            return ((ReadOnlyFeatureTypeInfo) info).unwrap(ResourceInfo.class);
        return info;
    }
    
    StoreInfo unwrap(StoreInfo info) {
        if(info instanceof ReadOnlyDataStoreInfo)
            return ((ReadOnlyDataStoreInfo) info).unwrap(StoreInfo.class);
        return info;
    }

    // -------------------------------------------------------------------
    // PURE DELEGATING METHODS
    // (MapInfo being here since its role in the grand scheme of things
    // is still undefined)
    // -------------------------------------------------------------------

    public MapInfo getMap(String id) {
        return delegate.getMap(id);
    }

    public MapInfo getMapByName(String name) {
        return delegate.getMapByName(name);
    }

    public List<MapInfo> getMaps() {
        return delegate.getMaps();
    }

    public void add(LayerGroupInfo layerGroup) {
        delegate.add(unwrap(layerGroup));
    }

    

    public void add(LayerInfo layer) {
        delegate.add(unwrap(layer));
    }

    public void add(MapInfo map) {
        delegate.add(map);
    }

    public void add(NamespaceInfo namespace) {
        delegate.add(namespace);
    }

    public void add(ResourceInfo resource) {
        delegate.add(unwrap(resource));
    }

    public void add(StoreInfo store) {
        delegate.add(unwrap(store));
    }

    public void add(StyleInfo style) {
        delegate.add(style);
    }

    public void add(WorkspaceInfo workspace) {
        delegate.add(workspace);
    }

    public void addListener(CatalogListener listener) {
        delegate.addListener(listener);
    }

    public void dispose() {
        delegate.dispose();
    }

    public CatalogFactory getFactory() {
        return delegate.getFactory();
    }

    public Collection<CatalogListener> getListeners() {
        return delegate.getListeners();
    }

    // TODO: why is resource pool being exposed???
    public ResourcePool getResourcePool() {
        return delegate.getResourcePool();
    }

    public StyleInfo getStyle(String id) {
        return delegate.getStyle(id);
    }

    public StyleInfo getStyleByName(String name) {
        return delegate.getStyleByName(name);
    }

    public List<StyleInfo> getStyles() {
        return delegate.getStyles();
    }

    public void remove(LayerGroupInfo layerGroup) {
        delegate.remove(unwrap(layerGroup));
    }

    public void remove(LayerInfo layer) {
        delegate.remove(unwrap(layer));
    }

    public void remove(MapInfo map) {
        delegate.remove(map);
    }

    public void remove(NamespaceInfo namespace) {
        delegate.remove(namespace);
    }

    public void remove(ResourceInfo resource) {
        delegate.remove(unwrap(resource));
    }

    public void remove(StoreInfo store) {
        delegate.remove(unwrap(store));
    }

    public void remove(StyleInfo style) {
        delegate.remove(style);
    }

    public void remove(WorkspaceInfo workspace) {
        delegate.remove(workspace);
    }

    public void removeListener(CatalogListener listener) {
        delegate.removeListener(listener);
    }

    public void save(LayerGroupInfo layerGroup) {
        delegate.save(unwrap(layerGroup));
    }

    public void save(LayerInfo layer) {
        delegate.save(unwrap(layer));
    }

    public void save(MapInfo map) {
        delegate.save(map);
    }

    public void save(NamespaceInfo namespace) {
        delegate.save(namespace);
    }

    public void save(ResourceInfo resource) {
        delegate.save(unwrap(resource));
    }

    public void save(StoreInfo store) {
        delegate.save(unwrap(store));
    }

    public void save(StyleInfo style) {
        delegate.save(style);
    }

    public void save(WorkspaceInfo workspace) {
        delegate.save(workspace);
    }

    public void setDefaultNamespace(NamespaceInfo defaultNamespace) {
        delegate.setDefaultNamespace(defaultNamespace);
    }

    public void setDefaultWorkspace(WorkspaceInfo workspace) {
        delegate.setDefaultWorkspace(workspace);
    }

    public void setResourcePool(ResourcePool resourcePool) {
        delegate.setResourcePool(resourcePool);
    }

}
