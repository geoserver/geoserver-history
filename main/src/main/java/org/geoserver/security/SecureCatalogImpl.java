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
import org.geoserver.platform.GeoServerExtensions;

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
public class SecureCatalogImpl implements SecureCatalog {
    protected Catalog catalog;

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
        this.catalog = catalog;
        this.accessManager = manager;
    }

    /**
     * Returns the unsecured catalog
     */
    public Catalog unwrap() {
        return catalog;
    }

    public boolean isWrapperFor(Class<?> iface) {
        return Catalog.class.isAssignableFrom(iface);
    }

    // -------------------------------------------------------------------
    // SECURED METHODS
    // -------------------------------------------------------------------

    public CoverageInfo getCoverage(String id) {
        return (CoverageInfo) checkAccess(user(), catalog.getCoverage(id));
    }

    public CoverageInfo getCoverageByName(String ns, String name) {
        return (CoverageInfo) checkAccess(user(), catalog.getCoverageByName(ns, name));
    }

    public CoverageInfo getCoverageByName(String name) {
        return (CoverageInfo) checkAccess(user(), catalog.getCoverageByName(name));
    }

    public List<CoverageInfo> getCoverages() {
        return filterResources(user(), catalog.getCoverages());
    }

    public List<CoverageInfo> getCoveragesByNamespace(NamespaceInfo namespace) {
        return filterResources(user(), catalog.getCoveragesByNamespace(namespace));
    }

    public CoverageStoreInfo getCoverageStore(String id) {
        return checkAccess(user(), catalog.getCoverageStore(id));
    }

    public CoverageStoreInfo getCoverageStoreByName(String name) {
        return checkAccess(user(), catalog.getCoverageStoreByName(name));
    }

    public List<CoverageStoreInfo> getCoverageStores() {
        return filterStores(user(), catalog.getCoverageStores());
    }

    public DataStoreInfo getDataStore(String id) {
        return checkAccess(user(), catalog.getDataStore(id));
    }

    public DataStoreInfo getDataStoreByName(String name) {
        return checkAccess(user(), catalog.getDataStoreByName(name));
    }

    public List<DataStoreInfo> getDataStores() {
        return filterStores(user(), catalog.getDataStores());
    }

    public NamespaceInfo getDefaultNamespace() {
        return catalog.getDefaultNamespace();
    }

    public WorkspaceInfo getDefaultWorkspace() {
        return catalog.getDefaultWorkspace();
    }

    public FeatureTypeInfo getFeatureType(String id) {
        return checkAccess(user(), catalog.getFeatureType(id));
    }

    public FeatureTypeInfo getFeatureTypeByName(String ns, String name) {
        return checkAccess(user(), catalog.getFeatureTypeByName(ns, name));
    }

    public FeatureTypeInfo getFeatureTypeByName(String name) {
        return checkAccess(user(), catalog.getFeatureTypeByName(name));
    }

    public List<FeatureTypeInfo> getFeatureTypes() {
        return filterResources(user(), catalog.getFeatureTypes());
    }

    public List<FeatureTypeInfo> getFeatureTypesByNamespace(NamespaceInfo namespace) {
        return filterResources(user(), catalog.getFeatureTypesByNamespace(namespace));
    }

    public LayerInfo getLayer(String id) {
        return checkAccess(user(), catalog.getLayer(id));
    }

    public LayerInfo getLayerByName(String name) {
        return checkAccess(user(), catalog.getLayerByName(name));
    }

    public LayerGroupInfo getLayerGroup(String id) {
        return checkAccess(user(), catalog.getLayerGroup(id));
    }

    public LayerGroupInfo getLayerGroupByName(String name) {
        return checkAccess(user(), catalog.getLayerGroupByName(name));
    }

    public List<LayerGroupInfo> getLayerGroups() {
        return filterGroups(user(), catalog.getLayerGroups());
    }

    public List<LayerInfo> getLayers() {
        return filterLayers(user(), catalog.getLayers());
    }

    public List<LayerInfo> getLayers(ResourceInfo resource) {
        return filterLayers(user(), catalog.getLayers(resource));
    }

    public NamespaceInfo getNamespace(String id) {
        return checkAccess(user(), catalog.getNamespace(id));
    }

    public NamespaceInfo getNamespaceByPrefix(String prefix) {
        return checkAccess(user(), catalog.getNamespaceByPrefix(prefix));
    }

    public NamespaceInfo getNamespaceByURI(String uri) {
        return checkAccess(user(), catalog.getNamespaceByURI(uri));
    }

    public List<NamespaceInfo> getNamespaces() {
        return filterNamespaces(user(), catalog.getNamespaces());
    }

    public <T extends ResourceInfo> T getResource(String id, Class<T> clazz) {
        return checkAccess(user(), catalog.getResource(id, clazz));
    }

    public <T extends ResourceInfo> T getResourceByName(String name, Class<T> clazz) {
        return checkAccess(user(), catalog.getResourceByName(name, clazz));
    }

    public <T extends ResourceInfo> T getResourceByName(String ns, String name, Class<T> clazz) {
        return checkAccess(user(), catalog.getResourceByName(ns, name, clazz));
    }

    public <T extends ResourceInfo> List<T> getResources(Class<T> clazz) {
        return filterResources(user(), catalog.getResources(clazz));
    }

    public <T extends ResourceInfo> List<T> getResourcesByNamespace(NamespaceInfo namespace,
            Class<T> clazz) {
        return filterResources(user(), catalog.getResourcesByNamespace(namespace, clazz));
    }

    public <T extends StoreInfo> T getStore(String id, Class<T> clazz) {
        return checkAccess(user(), catalog.getStore(id, clazz));
    }

    public <T extends StoreInfo> T getStoreByName(String name, Class<T> clazz) {
        return checkAccess(user(), catalog.getStoreByName(name, clazz));
    }

    public <T extends StoreInfo> List<T> getStores(Class<T> clazz) {
        return filterStores(user(), catalog.getStores(clazz));
    }

    public <T extends StoreInfo> List<T> getStoresByWorkspace(WorkspaceInfo workspace,
            Class<T> clazz) {
        return filterStores(user(), catalog.getStoresByWorkspace(workspace, clazz));
    }

    public WorkspaceInfo getWorkspace(String id) {
        return checkAccess(user(), catalog.getWorkspace(id));
    }

    public WorkspaceInfo getWorkspaceByName(String name) {
        return checkAccess(user(), catalog.getWorkspaceByName(name));
    }

    public List<WorkspaceInfo> getWorkspaces() {
        return filterWorkspaces(user(), catalog.getWorkspaces());
    }

    // -------------------------------------------------------------------
    // Security support method
    // -------------------------------------------------------------------

    protected Authentication user() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    /**
     * Given a resource and a user, returns it back if the user can access it in
     * the specified mode, null otherwise
     * 
     * @return
     */
    protected <T extends ResourceInfo> T checkAccess(Authentication user, T info) {
        if (info == null)
            return null;

        // return the resource if the user can read it, it's not really there
        // otherwise
        if (accessManager.canAccess(user, info, AccessMode.READ))
            return info;
        else
            return null;
    }

    /**
     * Given a store and a user, returns it back if the user can access its
     * workspace in the specified mode, null otherwise
     * 
     * @return
     */
    protected <T extends StoreInfo> T checkAccess(Authentication user, T store) {
        if (store == null)
            return null;

        // return the store if the user can read in its workspace, it's not
        // really there otherwise
        if (accessManager.canAccess(user, store.getWorkspace(), AccessMode.READ))
            return store;
        else
            return null;
    }

    /**
     * Given a layer and a user, returns it back if the user can access it, null
     * otherwise
     * 
     * @return
     */
    protected <T extends LayerInfo> T checkAccess(Authentication user, T layer) {
        if (layer == null)
            return null;

        // return the layer if the user can read in its workspace, it's not
        // really there otherwise
        if (accessManager.canAccess(user, layer, AccessMode.READ))
            return layer;
        else
            return null;
    }

    /**
     * Given a layer group and a user, returns it back if the user can access
     * it, null otherwise
     * 
     * @return
     */
    protected <T extends LayerGroupInfo> T checkAccess(Authentication user, T group) {
        if (group == null)
            return null;

        // return the layer if the user can read in its workspace, it's not
        // really there otherwise
        if (accessManager.canAccess(user, group, AccessMode.READ))
            return group;
        else
            return null;
    }

    /**
     * Given a namespace and user, returns it back if the user can access it,
     * null otherwise
     * 
     * @return
     */
    protected <T extends NamespaceInfo> T checkAccess(Authentication user, T group) {
        // route the security check thru the associated workspace info
        WorkspaceInfo info = checkAccess(user, catalog.getWorkspace(group.getPrefix()));
        if (info == null)
            return null;
        else
            return group;
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
    protected <T extends LayerGroupInfo> List<T> filterGroups(Authentication user, List<T> groups) {
        List<T> result = new ArrayList<T>();
        for (T original : groups) {
            T secured = checkAccess(user, original);
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
    protected <T extends LayerInfo> List<T> filterLayers(Authentication user, List<T> layers) {
        List<T> result = new ArrayList<T>();
        for (T original : layers) {
            T secured = checkAccess(user, original);
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
    // PURE DELEGATING METHODS
    // (MapInfo being here since its role in the grand scheme of things
    // is still undefined)
    // -------------------------------------------------------------------

    public MapInfo getMap(String id) {
        return catalog.getMap(id);
    }

    public MapInfo getMapByName(String name) {
        return catalog.getMapByName(name);
    }

    public List<MapInfo> getMaps() {
        return catalog.getMaps();
    }

    public void add(LayerGroupInfo layerGroup) {
        catalog.add(layerGroup);
    }

    public void add(LayerInfo layer) {
        catalog.add(layer);
    }

    public void add(MapInfo map) {
        catalog.add(map);
    }

    public void add(NamespaceInfo namespace) {
        catalog.add(namespace);
    }

    public void add(ResourceInfo resource) {
        catalog.add(resource);
    }

    public void add(StoreInfo store) {
        catalog.add(store);
    }

    public void add(StyleInfo style) {
        catalog.add(style);
    }

    public void add(WorkspaceInfo workspace) {
        catalog.add(workspace);
    }

    public void addListener(CatalogListener listener) {
        catalog.addListener(listener);
    }

    public void dispose() {
        catalog.dispose();
    }

    public CatalogFactory getFactory() {
        return catalog.getFactory();
    }

    public Collection<CatalogListener> getListeners() {
        return catalog.getListeners();
    }

    // TODO: why is resource pool being exposed???
    public ResourcePool getResourcePool() {
        return catalog.getResourcePool();
    }

    public StyleInfo getStyle(String id) {
        return catalog.getStyle(id);
    }

    public StyleInfo getStyleByName(String name) {
        return catalog.getStyleByName(name);
    }

    public List<StyleInfo> getStyles() {
        return catalog.getStyles();
    }

    public void remove(LayerGroupInfo layerGroup) {
        catalog.remove(layerGroup);
    }

    public void remove(LayerInfo layer) {
        catalog.remove(layer);
    }

    public void remove(MapInfo map) {
        catalog.remove(map);
    }

    public void remove(NamespaceInfo namespace) {
        catalog.remove(namespace);
    }

    public void remove(ResourceInfo resource) {
        catalog.remove(resource);
    }

    public void remove(StoreInfo store) {
        catalog.remove(store);
    }

    public void remove(StyleInfo style) {
        catalog.remove(style);
    }

    public void remove(WorkspaceInfo workspace) {
        catalog.remove(workspace);
    }

    public void removeListener(CatalogListener listener) {
        catalog.removeListener(listener);
    }

    public void save(LayerGroupInfo layerGroup) {
        catalog.save(layerGroup);
    }

    public void save(LayerInfo layer) {
        catalog.save(layer);
    }

    public void save(MapInfo map) {
        catalog.save(map);
    }

    public void save(NamespaceInfo namespace) {
        catalog.save(namespace);
    }

    public void save(ResourceInfo resource) {
        catalog.save(resource);
    }

    public void save(StoreInfo store) {
        catalog.save(store);
    }

    public void save(StyleInfo style) {
        catalog.save(style);
    }

    public void save(WorkspaceInfo workspace) {
        catalog.save(workspace);
    }

    public void setDefaultNamespace(NamespaceInfo defaultNamespace) {
        catalog.setDefaultNamespace(defaultNamespace);
    }

    public void setDefaultWorkspace(WorkspaceInfo workspace) {
        catalog.setDefaultWorkspace(workspace);
    }

    public void setResourcePool(ResourcePool resourcePool) {
        catalog.setResourcePool(resourcePool);
    }

}
