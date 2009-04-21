package org.geoserver.hibernate.dao;

import java.util.Collection;
import java.util.List;

import org.geoserver.catalog.CoverageInfo;
import org.geoserver.catalog.GeophysicParamInfo;
import org.geoserver.catalog.LayerGroupInfo;
import org.geoserver.catalog.LayerInfo;
import org.geoserver.catalog.MapInfo;
import org.geoserver.catalog.ModelInfo;
import org.geoserver.catalog.ModelRunInfo;
import org.geoserver.catalog.NamespaceInfo;
import org.geoserver.catalog.ResourceInfo;
import org.geoserver.catalog.StoreInfo;
import org.geoserver.catalog.StyleInfo;
import org.geoserver.catalog.WorkspaceInfo;
import org.geoserver.catalog.hibernate.HbNamespaceInfo;
import org.geoserver.catalog.hibernate.HbWorkspaceInfo;
import org.geoserver.config.GeoServerInfo;
import org.geoserver.config.ServiceInfo;

public interface GeoServerDAO {

    /**
     * 
     * @param entity
     */
    public abstract void save(Object entity);

    /**
     * 
     * @param entity
     */
    public abstract void merge(Object entity);

    /**
     * 
     * @param entity
     */
    public abstract void delete(Object entity);

    /**
     * 
     * @param entity
     */
    public abstract void update(Object entity);

    /**
     * 
     * @return
     */
    public abstract HbNamespaceInfo getDefaultNamespace();
    
    /**
     * 
     * @param <T>
     * @param id
     * @param clazz
     * @return
     */
    public abstract <T extends StoreInfo> T getStore(String id, Class<T> clazz);
    
    /**
     * 
     * @param <T>
     * @param name
     * @param clazz
     * @return
     */
    public abstract <T extends StoreInfo> T getStoreByName(String name, Class<T> clazz);
    
    /**
     * 
     * @param <T>
     * @param id
     * @param clazz
     * @return
     */
    public abstract <T extends ResourceInfo> T getResource(String id, Class<T> clazz);
    
    /**
     * 
     * @param <T>
     * @param ns
     * @param name
     * @param clazz
     * @return
     */
    public abstract <T extends ResourceInfo> T getResourceByName(String ns, String name, Class<T> clazz);
    
    /**
     * 
     * @param id
     * @return
     */
    public abstract LayerInfo getLayer(String id);
    
    /**
     * 
     * @param name
     * @return
     */
    public abstract LayerInfo getLayerByName(String name);
    
    /**
     * 
     * @param <T>
     * @param clazz
     * @return
     */
    public abstract <T extends ResourceInfo> List<T> getResources(Class<T> clazz);
    
    /**
     * 
     * @param <T>
     * @param namespace
     * @param clazz
     * @return
     */
    public abstract <T extends ResourceInfo> List<T> getResourcesByNamespace(NamespaceInfo namespace, Class<T> clazz);
    
    /**
     * 
     * @param <T>
     * @param clazz
     * @return
     */
    public abstract <T extends StoreInfo> List<T> getStores(Class<T> clazz);
    
    /**
     * 
     * @return
     */
    public abstract List<MapInfo> getMaps();
    
    /**
     * 
     * @param id
     * @return
     */
    public abstract MapInfo getMap(String id);
    
    /**
     * 
     * @param name
     * @return
     */
    public abstract MapInfo getMapByName(String name);
    
    /**
     * 
     * @return
     */
    public abstract List<LayerInfo> getLayers();
    
    /**
     * 
     * @param id
     * @return
     */
    public abstract StyleInfo getStyle(String id);
    
    /**
     * 
     * @param name
     * @return
     */
    public abstract StyleInfo getStyleByName(String name);
    
    /**
     * 
     * @return
     */
    public abstract List<StyleInfo> getStyles();
    
    /**
     * 
     * @param id
     * @return
     */
    public abstract NamespaceInfo getNamespace(String id);
    
    /**
     * 
     * @param prefix
     * @return
     */
    public abstract HbNamespaceInfo getNamespaceByPrefix(String prefix);
    
    /**
     * 
     * @param uri
     * @return
     */
    public abstract NamespaceInfo getNamespaceByURI(String uri);
    
    /**
     * 
     * @return
     */
    public abstract List<NamespaceInfo> getNamespaces();
    
    /**
     * 
     * @return
     */
    public abstract List<LayerGroupInfo> getLayerGroups();
    
    /**
     * 
     * @param <T>
     * @param workspace
     * @param clazz
     * @return
     */
    public abstract <T extends StoreInfo> List<T> getStoresByWorkspace(WorkspaceInfo workspace, Class<T> clazz);
    
    /**
     * 
     * @param id
     * @return
     */
    public abstract WorkspaceInfo getWorkspace(String id);
    
    /**
     * 
     * @param name
     * @return
     */
    public abstract WorkspaceInfo getWorkspaceByName(String name);
    
    /**
     * 
     * @return
     */
    public abstract List<WorkspaceInfo> getWorkspaces();
    
    /**
     * 
     * @return
     */
    public abstract HbWorkspaceInfo getDefaultWorkspace();
    
    /**
     * 
     * @param id
     * @return
     */
    public abstract ModelInfo getModel(String id);
    
    /**
     * 
     * @param name
     * @return
     */
    public abstract ModelInfo getModelByName(String name);

    /**
     * 
     * @param param
     * @return
     */
    public abstract List<ModelInfo> getModels(GeophysicParamInfo param);

    /**
     * 
     * @param id
     * @return
     */
    public abstract ModelRunInfo getModelRun(String id);
    
    /**
     * 
     * @param name
     * @return
     */
    public abstract ModelRunInfo getModelRunByName(String name);
    
    /**
     * 
     * @return
     */
    public abstract List<ModelRunInfo> getModelRuns();
    
    /**
     * 
     * @param model
     * @return
     */
    public abstract List<ModelRunInfo> getModelRuns(ModelInfo model);

    /**
     * 
     * @param param
     * @return
     */
    public abstract List<ModelRunInfo> getModelRuns(GeophysicParamInfo param);

    /**
     * 
     * @param modelRun
     * @return
     */
    public abstract List<CoverageInfo> getGridCoverages(ModelRunInfo modelRun);

    /**
     * 
     * @param param
     * @return
     */
    public abstract List<CoverageInfo> getGridCoverages(GeophysicParamInfo param);

    /**
     * 
     * @return
     */
    public abstract List<ModelInfo> getModels();

    /**
     * 
     * @return
     */
    public abstract List<GeophysicParamInfo> getGeophysicalParameters();

    /**
     * 
     * @return
     */
    public abstract GeoServerInfo getGeoServer();

    /**
     * 
     * @param <T>
     * @param id
     * @param clazz
     * @return
     */
    public abstract <T extends ServiceInfo> T getService(String id, Class<T> clazz);
    
    /**
     * 
     * @param <T>
     * @param name
     * @param clazz
     * @return
     */
    public abstract <T extends ServiceInfo> T getServiceByName(String name, Class<T> clazz);
    
    /**
     * 
     * @param clazz
     * @return
     */
    public abstract Collection<? extends ServiceInfo> getServices(Class<?> clazz);

    /**
     * 
     * @param model
     * @return
     */
    public abstract List<GeophysicParamInfo> getGeophysicalParameters(ModelInfo model);

    /**
     * 
     * @param modelRun
     * @return
     */
    public abstract List<GeophysicParamInfo> getGeophysicalParameters(ModelRunInfo modelRun);

    /**
     * 
     * @param coverage
     * @return
     */
    public abstract List<GeophysicParamInfo> getGeophysicalParameters(CoverageInfo coverage);

    /**
     * 
     * @param variableName
     * @return
     */
    public abstract GeophysicParamInfo getGeophysicParamByName(String variableName);

}