package org.geoserver.hibernate.dao;

import java.util.List;

import org.geoserver.catalog.LayerGroupInfo;
import org.geoserver.catalog.LayerInfo;
import org.geoserver.catalog.MapInfo;
import org.geoserver.catalog.NamespaceInfo;
import org.geoserver.catalog.ResourceInfo;
import org.geoserver.catalog.StoreInfo;
import org.geoserver.catalog.StyleInfo;
import org.geoserver.catalog.WorkspaceInfo;
import org.geoserver.catalog.impl.NamespaceInfoImpl;
import org.geoserver.catalog.impl.WorkspaceInfoImpl;

public interface CatalogDAO {

    public abstract void save(StoreInfo entity);

    public abstract void delete(StoreInfo entity);

    public abstract StoreInfo update(StoreInfo entity);

    public abstract void save(ResourceInfo entity);

    public abstract void delete(ResourceInfo entity);

    public abstract ResourceInfo update(ResourceInfo entity);

    public abstract void save(NamespaceInfo entity);

    public abstract void delete(NamespaceInfo entity);

    public abstract NamespaceInfo update(NamespaceInfo entity);

    public abstract void save(LayerInfo entity);

    public abstract void delete(LayerInfo entity);

    public abstract LayerInfo update(LayerInfo entity);

    public abstract void save(MapInfo entity);

    public abstract void delete(MapInfo entity);

    public abstract MapInfo update(MapInfo entity);

    public abstract void save(StyleInfo entity);

    public abstract void delete(StyleInfo entity);

    public abstract StyleInfo update(StyleInfo entity);

    public abstract void save(LayerGroupInfo entity);

    public abstract void delete(LayerGroupInfo entity);

    public abstract LayerGroupInfo update(LayerGroupInfo entity);

    public abstract void save(WorkspaceInfo entity);

    public abstract void delete(WorkspaceInfo entity);

    public abstract WorkspaceInfo update(WorkspaceInfo entity);

    NamespaceInfoImpl getDefaultNamespace();

    WorkspaceInfoImpl getDefaultWorkspace();

    LayerGroupInfo getLayerGroup(String id);

    LayerGroupInfo getLayerGroupByName(String name);

    List<LayerGroupInfo> getLayerGroups();

    LayerInfo getLayer(String id);

    LayerInfo getLayerByName(String name);

    LayerInfo getLayerByName(String nsprefix, String name);

    List<LayerInfo> getLayers();

    List<LayerInfo> getLayersByResourceId(String resid);

    MapInfo getMap(String id);

    MapInfo getMapByName(String name);

    List<MapInfo> getMaps();

    NamespaceInfoImpl getNamespace(String id);

    NamespaceInfoImpl getNamespaceByPrefix(String prefix);

    NamespaceInfo getNamespaceByURI(String uri);

    List<NamespaceInfo> getNamespaces();

    <T extends ResourceInfo> T getResource(String id, Class<T> clazz);

    <T extends ResourceInfo> T getResourceByName(String ns, String name, Class<T> clazz);

    <T extends ResourceInfo> T getResourceByStore(StoreInfo store, String name, Class<T> clazz);

    <T extends ResourceInfo> List<T> getResources(Class<T> clazz);

    <T extends ResourceInfo> List<T> getResourcesByName(String name, Class<T> clazz);

    <T extends ResourceInfo> List<T> getResourcesByNamespace(NamespaceInfo namespace, Class<T> clazz);

    <T extends ResourceInfo> List<T> getResourcesByStore(StoreInfo store, Class<T> clazz);

    <T extends StoreInfo> T getStore(String id, Class<T> clazz);

    @Deprecated
    <T extends StoreInfo> T getStoreByName(String name, Class<T> clazz);

    <T extends StoreInfo> T getStoreByName(WorkspaceInfo workspace, String name, Class<T> clazz);

    <T extends StoreInfo> List<T> getStores(Class<T> clazz);

    <T extends StoreInfo> List<T> getStoresByName(String name, Class<T> clazz);

    <T extends StoreInfo> List<T> getStoresByWorkspace(WorkspaceInfo workspace, Class<T> clazz);

    StyleInfo getStyle(String id);

    StyleInfo getStyleByName(String name);

    List<StyleInfo> getStyles();

    WorkspaceInfo getWorkspace(String id);

    WorkspaceInfo getWorkspaceByName(String name);

    List<WorkspaceInfo> getWorkspaces();

}