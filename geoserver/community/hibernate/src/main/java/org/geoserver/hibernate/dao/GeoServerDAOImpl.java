/**
 * 
 */
package org.geoserver.hibernate.dao;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.geoserver.catalog.Catalog;
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
import org.geoserver.catalog.impl.CoverageInfoImpl;
import org.geoserver.catalog.impl.GeophysicParamInfoImpl;
import org.geoserver.catalog.impl.LayerGroupInfoImpl;
import org.geoserver.catalog.impl.LayerInfoImpl;
import org.geoserver.catalog.impl.MapInfoImpl;
import org.geoserver.catalog.impl.ModelInfoImpl;
import org.geoserver.catalog.impl.ModelRunInfoImpl;
import org.geoserver.catalog.impl.NamespaceInfoImpl;
import org.geoserver.catalog.impl.ResourceInfoImpl;
import org.geoserver.catalog.impl.StoreInfoImpl;
import org.geoserver.catalog.impl.StyleInfoImpl;
import org.geoserver.config.GeoServerInfo;
import org.geoserver.config.ServiceInfo;
import org.geoserver.config.impl.GeoServerInfoImpl;
import org.geoserver.config.impl.ServiceInfoImpl;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Alessio Fabiani, GeoSolutions SAS
 * @author Simone Giannecchini, GeoSolutions SAS
 * 
 */
@Repository
@Transactional
public class GeoServerDAOImpl implements GeoServerDAO {
	
	private static String sqlQueryBuilder(String... elements){
		final StringBuilder builder= new StringBuilder();
		for(String element:elements){
			builder.append(element);
		}
		return builder.toString();
		
	}

    @Autowired
    private SessionFactory sessionFactory;

    /**
     * @param sessionFactory the sessionFactory to set
     */
    public  void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    /**
     * @return 
     */
    public  SessionFactory getSessionFactory() {
        return this.sessionFactory;
    }
    
    /**
     * Constructor for NeapolisHibernateDAO.
     */
    public GeoServerDAOImpl() {
        super();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.geoserver.hibernate.dao.IGeoServerDAO#save(java.lang.Object)
     */
    public  void save(Object entity) {
        getSessionFactory().getCurrentSession().save(entity);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.geoserver.hibernate.dao.IGeoServerDAO#merge(java.lang.Object)
     */
    public  void merge(Object entity) {
        getSessionFactory().getCurrentSession().merge(entity);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.geoserver.hibernate.dao.IGeoServerDAO#delete(java.lang.Object)
     */
    public  void delete(Object entity) {
        getSessionFactory().getCurrentSession().delete(entity);
        getSessionFactory().getCurrentSession().flush();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.geoserver.hibernate.dao.IGeoServerDAO#update(java.lang.Object)
     */
    public  void update(Object entity) {
        getSessionFactory().getCurrentSession().update(entity);
    }

    /**
     * Helper method to return the first object of a query.
     * 
     * @param hql
     *            the hql query, may contain {@code ?} argument placeholders
     * @param arguments
     *            the hql query arguments, or {@code null}. Recognized argument types are
     *            {@link String}, {@link Integer}, {@link Boolean}, {@link Float} and {@link Double}
     *            . An object of any other type will result in an unchecked exception
     * @return the first object matching the query or {@code null} if the query returns no results
     */
    private Object first(final String hql, final Object[] arguments) {
        Query query = getSessionFactory().getCurrentSession().createQuery(hql);
        if (arguments != null) {
            for (int argN = 0; argN < arguments.length; argN++) {
                final Object arg = arguments[argN];
                final Class<? extends Object> c = arg.getClass();
                if (String.class == c) {
                    query.setString(argN, (String) arg);
                } else if (Boolean.class == c) {
                    query.setBoolean(argN, ((Boolean) arg).booleanValue());
                } else if (Integer.class == c) {
                    query.setInteger(argN, ((Integer) arg).intValue());
                } else if (Float.class == c) {
                    query.setFloat(argN, ((Float) arg).floatValue());
                } else if (Double.class == c) {
                    query.setDouble(argN, ((Double) arg).doubleValue());
                } else {
                    throw new IllegalArgumentException("Unrecognized type for argument " + argN + " in query '" + hql + "': " + c);
                }
            }
        }
        Iterator i = query.iterate();
        if (i.hasNext()) {
            Object first = i.next();
            return first;
        }

        return null;
    }

    /**
     * 
     * @param clazz
     * @return
     */
    private List<?> list(Class clazz) {
        return list(sqlQueryBuilder("from " , clazz.getName()));
    }

    /**
     * Helper method to return the list of a query
     */
    private List<?> list(String hql) {
        return getSessionFactory().getCurrentSession().createQuery(hql).list();
    }

    /**
     * @see Catalog#getDefaultNamespace()
     */
    public  HbNamespaceInfo getDefaultNamespace() {
        String hql = sqlQueryBuilder("from " , NamespaceInfo.class.getName(), " where default=?");
        HbNamespaceInfo info = (HbNamespaceInfo) first(hql, new Object[] { Boolean.TRUE });
        return info != null ? (HbNamespaceInfo) getSessionFactory().getCurrentSession().get(HbNamespaceInfo.class, info.getId()) : null;
    }

    /**
     * 
     */
    public  <T extends StoreInfo> T getStore(String id, Class<T> clazz) {
        T store = (T) first(sqlQueryBuilder("from " , clazz.getName() , " where id = ?"), new Object[] { id }); 
        return store != null ? (T) getSessionFactory().getCurrentSession().get(StoreInfoImpl.class, store.getId()) : null;
    }

    /**
     * 
     */
    public  <T extends StoreInfo> T getStoreByName(String name, Class<T> clazz) {
        T store = (T) first(sqlQueryBuilder("from " , clazz.getName() , " where name = ?"), new Object[] { name }); 
        return store != null ? (T) getSessionFactory().getCurrentSession().get(StoreInfoImpl.class, store.getId()) : null;
    }

    /**
     * 
     */
    public  <T extends ResourceInfo> T getResource(String id, Class<T> clazz) {
        T resource = (T) first(sqlQueryBuilder("from " , clazz.getName() , " where id = ?"), new Object[] { id });
        return resource != null ? (T) getSessionFactory().getCurrentSession().get(ResourceInfoImpl.class, resource.getId()) : null;
    }

    /**
     * 
     */
    public  <T extends ResourceInfo> T getResourceByName(String ns, String name, Class<T> clazz) {
        T resource = (T) first(sqlQueryBuilder("from " , clazz.getName() , " where name = ? and namespace = ?"), new Object[] { name, ns });
        return resource != null ? (T) getSessionFactory().getCurrentSession().get(ResourceInfoImpl.class, resource.getId()) : null;
    }

    /**
     * 
     */
    public  LayerInfo getLayer(String id) {
        LayerInfo layer = (LayerInfo) first(sqlQueryBuilder("from " , LayerInfo.class.getName() , " where id = ?"), new Object[] { id });
        return layer != null ? (LayerInfo) getSessionFactory().getCurrentSession().get(LayerInfoImpl.class, layer.getId()) : null;
    }

    /**
     * 
     */
    public  LayerInfo getLayerByName(String name) {
        LayerInfo layer = (LayerInfo) first(sqlQueryBuilder("from ", LayerInfo.class.getName() , " where name = ?"), new Object[] { name });
        return layer != null ? (LayerInfo) getSessionFactory().getCurrentSession().get(LayerInfoImpl.class, layer.getId()) : null;
    }

    /**
     * 
     */
    public  <T extends StoreInfo> List<T> getStores(Class<T> clazz) {
        return (List<T>) list(clazz);
    }

    /**
     * 
     */
    public  <T extends ResourceInfo> List<T> getResources(Class<T> clazz) {
        return (List<T>) list(clazz);
    }

    /**
     * 
     */
    public  <T extends ResourceInfo> List<T> getResourcesByNamespace(NamespaceInfo namespace, Class<T> clazz) {
        String hql = sqlQueryBuilder("select r from " , clazz.getName() , " r, " + NamespaceInfo.class.getName() , " n where r.namespace = n and n.prefix = '" , namespace.getPrefix() , "'");
        return (List<T>) list(hql);
    }

    /**
     * 
     */
    public  MapInfo getMap(String id) {
        MapInfo map = (MapInfo) first(sqlQueryBuilder("from " , MapInfo.class.getName() , " where id = ? "), new Object[] { id });
        return map != null ? (MapInfo) getSessionFactory().getCurrentSession().get(MapInfoImpl.class, map.getId()) : null;
    }

    /**
     * 
     */
    public  MapInfo getMapByName(String name) {
        MapInfo map = (MapInfo) first(sqlQueryBuilder("from " , MapInfo.class.getName() , " where name = ?"), new Object[] { name });
        return map != null ? (MapInfo) getSessionFactory().getCurrentSession().get(MapInfoImpl.class, map.getId()) : null;
    }

    /**
     * 
     */
    public  List<MapInfo> getMaps() {
        return (List<MapInfo>) list(MapInfoImpl.class);
    }

    /**
     * 
     */
    public  List<LayerInfo> getLayers() {
        return (List<LayerInfo>) list(LayerInfoImpl.class);
    }

    /**
     * 
     */
    public  StyleInfo getStyle(String id) {
        StyleInfo style = (StyleInfo) first(sqlQueryBuilder("from " , StyleInfo.class.getName() , " where id = ?"), new Object[] { id }); 
        return style != null ? (StyleInfo) getSessionFactory().getCurrentSession().get(StyleInfoImpl.class, style.getId()) : null;
    }

    /**
     * 
     */
    public  StyleInfo getStyleByName(String name) {
        StyleInfo style = (StyleInfo) first(sqlQueryBuilder("from " , StyleInfo.class.getName() , " where name = ?"), new Object[] { name }); 
        return style != null ? (StyleInfo) getSessionFactory().getCurrentSession().get(StyleInfoImpl.class, style.getId()) : null;
    }

    /**
     * 
     */
    public  List<StyleInfo> getStyles() {
        return (List<StyleInfo>) list(StyleInfo.class);
    }

    /**
     * 
     */
    public  NamespaceInfo getNamespace(String id) {
        HbNamespaceInfo nameSpace = (HbNamespaceInfo) first(sqlQueryBuilder("from " , NamespaceInfo.class.getName() , " where id = ?"), new Object[] { id });
        return nameSpace != null ? (NamespaceInfo) getSessionFactory().getCurrentSession().get(HbNamespaceInfo.class, nameSpace.getId()) : null;
    }

    /**
     * 
     */
    public  HbNamespaceInfo getNamespaceByPrefix(String prefix) {
        HbNamespaceInfo nameSpace = (HbNamespaceInfo) first(sqlQueryBuilder("from ", NamespaceInfo.class.getName() , " where prefix = ?"), new Object[] { prefix });
        return nameSpace != null ? (HbNamespaceInfo) getSessionFactory().getCurrentSession().get(HbNamespaceInfo.class, nameSpace.getId()) : null;
    }

    /**
     * 
     */
    public  NamespaceInfo getNamespaceByURI(String uri) {
        NamespaceInfo nameSpace = (NamespaceInfo) first(sqlQueryBuilder("from " , NamespaceInfo.class.getName() , " where URI = ?"), new Object[] { uri });
        return nameSpace != null ? (NamespaceInfo) getSessionFactory().getCurrentSession().get(HbNamespaceInfo.class, nameSpace.getId()) : null;
    }

    /**
     * 
     */
    public  List<NamespaceInfo> getNamespaces() {
        return (List<NamespaceInfo>) list(NamespaceInfoImpl.class);
    }

    /**
     * 
     */
    public  HbWorkspaceInfo getDefaultWorkspace() {
        String hql = sqlQueryBuilder("from " , HbWorkspaceInfo.class.getName(), " where default=?");
        HbWorkspaceInfo info = (HbWorkspaceInfo) first(hql, new Object[] { Boolean.TRUE });
        return info != null ? (HbWorkspaceInfo) getSessionFactory().getCurrentSession().get(HbWorkspaceInfo.class, info.getId()) : null;
    }

    /**
     * 
     */
    public  List<LayerGroupInfo> getLayerGroups() {
        return (List<LayerGroupInfo>) list(LayerGroupInfoImpl.class);
    }

    /**
     * 
     */
    public  ModelInfo getModel(String id) {
        ModelInfo model = (ModelInfo) first(sqlQueryBuilder("from " , ModelInfo.class.getName() , " where id = ?"), new Object[] { id }); 
        return model != null ? (ModelInfo) getSessionFactory().getCurrentSession().get(ModelInfoImpl.class, model.getId()) : null;
    }

    /**
     * 
     */
    public  ModelInfo getModelByName(String name) {
        ModelInfo model = (ModelInfo) first(sqlQueryBuilder("from ", ModelInfo.class.getName() , " where name = ?"), new Object[] { name }); 
        return model != null ? (ModelInfo) getSessionFactory().getCurrentSession().get(ModelInfoImpl.class, model.getId()) : null;
    }

    /**
     * 
     */
    public  ModelRunInfo getModelRun(String id) {
        ModelRunInfo modelRun = (ModelRunInfo) first(sqlQueryBuilder("from " , ModelRunInfo.class.getName() , " where id = ?"), new Object[] { id }); 
        return modelRun != null ? (ModelRunInfo) getSessionFactory().getCurrentSession().get(ModelRunInfoImpl.class, modelRun.getId()) : null;
    }

    /**
     * 
     */
    public  List<ModelInfo> getModels(GeophysicParamInfo param) {
        Query query = getSessionFactory().getCurrentSession().createQuery(sqlQueryBuilder("select gp.models from " , GeophysicParamInfoImpl.class.getName() , " as gp where gp = ?"));
        query.setEntity(0, param);
        
        return query.list();
    }

    /**
     * 
     */
    public  ModelRunInfo getModelRunByName(String name) {
        ModelRunInfo modelRun = (ModelRunInfo) first(sqlQueryBuilder("from " , ModelRunInfoImpl.class.getName() , " where name = ?"), new Object[] { name }); 
        return modelRun != null ? (ModelRunInfo) getSessionFactory().getCurrentSession().get(ModelRunInfoImpl.class, modelRun.getId()) : null;
    }

    /**
     * 
     */
    public  List<ModelRunInfo> getModelRuns() {
        return (List<ModelRunInfo>) list(ModelRunInfo.class);
    }

    /**
     * 
     */
    public  List<ModelRunInfo> getModelRuns(ModelInfo model) {
        Query query = getSessionFactory().getCurrentSession().createQuery(sqlQueryBuilder("from " , ModelRunInfoImpl.class.getName() , " where model = ?"));
        query.setEntity(0, model);
        return query.list();
    }

    /**
     * 
     */
    public  List<ModelRunInfo> getModelRuns(GeophysicParamInfo param) {
        Query query = getSessionFactory().getCurrentSession().createQuery(sqlQueryBuilder("select gp.modelRuns from " , GeophysicParamInfoImpl.class.getName() , " as gp where gp = ?"));
        query.setEntity(0, param);
        return query.list();
    }

    /**
     * 
     */
    public  List<CoverageInfo> getGridCoverages(ModelRunInfo modelRun) {
        Query query = getSessionFactory().getCurrentSession().createQuery(sqlQueryBuilder("select mr.gridCoverages from ", ModelRunInfoImpl.class.getName(), " as mr where mr = ?"));
        query.setEntity(0, modelRun);
        return query.list();
    }

    /**
     * 
     */
    public  List<CoverageInfo> getGridCoverages(GeophysicParamInfo param) {
        Query query = getSessionFactory().getCurrentSession().createQuery(sqlQueryBuilder("select gp.gridCoverages from " , GeophysicParamInfoImpl.class.getName() , " as gp where gp = ?"));
        query.setEntity(0, param);
        return query.list();
    }

    /**
     * 
     */
    public  List<GeophysicParamInfo> getGeophysicalParameters(ModelInfo model) {
        Query query = getSessionFactory().getCurrentSession().createQuery(sqlQueryBuilder("select m.geophysicalParameters from " , ModelInfoImpl.class.getName() , " as m where m = ?"));
        query.setEntity(0, model);
        return query.list();
    }

    /**
     * 
     */
    public  List<GeophysicParamInfo> getGeophysicalParameters(ModelRunInfo modelRun) {
        Query query = getSessionFactory().getCurrentSession().createQuery(sqlQueryBuilder("select mr.geophysicalParameters from ", ModelRunInfoImpl.class.getName(), " as mr where mr = ?"));
        query.setEntity(0, modelRun);
        return query.list();
    }

    /**
     * 
     */
    public  List<GeophysicParamInfo> getGeophysicalParameters(CoverageInfo coverage) {
        Query query = getSessionFactory().getCurrentSession().createQuery(sqlQueryBuilder("select cv.geophysicalParameters from " , CoverageInfoImpl.class.getName() , " as cv where cv = ?"));
        query.setEntity(0, coverage);
        return query.list();
    }

    /**
     * 
     */
    public  GeophysicParamInfo getGeophysicParamByName(String name) {
        GeophysicParamInfo geophysicParam = (GeophysicParamInfo) first(sqlQueryBuilder("from " , GeophysicParamInfoImpl.class.getName() , " where name = ?"), new Object[] { name }); 
        return geophysicParam != null ? (GeophysicParamInfo) getSessionFactory().getCurrentSession().get(GeophysicParamInfoImpl.class, geophysicParam.getId()) : null;
    }

    /**
     * 
     */
    public  List<ModelInfo> getModels() {
        return (List<ModelInfo>) list(ModelInfo.class);
    }

    /**
     * 
     */
    public  List<GeophysicParamInfo> getGeophysicalParameters() {
        return (List<GeophysicParamInfo>) list(GeophysicParamInfo.class);
    }

    /**
     * 
     */
    public  <T extends StoreInfo> List<T> getStoresByWorkspace(WorkspaceInfo workspace, Class<T> clazz) {
        return null;
    }

    /**
     * 
     */
    public  WorkspaceInfo getWorkspace(String id) {
        WorkspaceInfo ws = (WorkspaceInfo) first(sqlQueryBuilder("from " , WorkspaceInfo.class.getName() , " where id = "), new Object[] { id });
        return ws != null ? (WorkspaceInfo) getSessionFactory().getCurrentSession().get(HbWorkspaceInfo.class, ws.getId()) : null;
    }

    /**
     * 
     */
    public  WorkspaceInfo getWorkspaceByName(String name) {
        WorkspaceInfo ws = (WorkspaceInfo) first(sqlQueryBuilder("from ", WorkspaceInfo.class.getName() , " where name = ?"), new Object[] { name });
        return ws != null ? (WorkspaceInfo) getSessionFactory().getCurrentSession().get(HbWorkspaceInfo.class, ws.getId()) : null;
    }

    /**
     * 
     */
    public  List<WorkspaceInfo> getWorkspaces() {
        return (List<WorkspaceInfo>) list(WorkspaceInfo.class);
    }

    /**
     * 
     */
    public  GeoServerInfo getGeoServer() {
        Iterator i = getSessionFactory().getCurrentSession().createQuery(sqlQueryBuilder("from ", GeoServerInfoImpl.class.getName())).iterate();
        if (i.hasNext())
            return (GeoServerInfo) getSessionFactory().getCurrentSession().get(GeoServerInfoImpl.class, ((GeoServerInfo) i.next()).getId());
        
        return null;
    }

    /**
     * 
     */
    public  Collection<? extends ServiceInfo> getServices(Class<?> clazz) {
        List<?> list = getSessionFactory().getCurrentSession().createQuery(sqlQueryBuilder("from " , clazz.getName())).list();
        return (Collection<? extends ServiceInfo>) list;
    }

    /**
     * 
     */
    public  <T extends ServiceInfo> T getService(String id, Class<T> clazz) {
        Iterator i = getSessionFactory().getCurrentSession().createQuery(sqlQueryBuilder("from " , clazz.getName() , " where id = '" , id , "'")).iterate();
        if (i.hasNext()) {
//        	return  (T) i.next();
            T service = (T) i.next();
            return service != null ? (T) getSessionFactory().getCurrentSession().get(ServiceInfoImpl.class, service.getId()) : null;
        }

        return null;
    }

    /**
     * 
     */
    public  <T extends ServiceInfo> T getServiceByName(String name, Class<T> clazz) {
        Iterator i = getSessionFactory().getCurrentSession().createQuery(sqlQueryBuilder("from " , clazz.getName() , " where name = '" , name , "'")).iterate();
        if (i.hasNext()) {
//        	return  (T) i.next();
            T service = (T) i.next();
            return service != null ? (T) getSessionFactory().getCurrentSession().get(ServiceInfoImpl.class, service.getId()) : null;
        }

        return null;
    }

}
