/**
 * 
 */
package org.geoserver.hibernate.dao;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.geoserver.catalog.Catalog;
import org.geoserver.catalog.CoverageInfo;
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
 * @author Alessio
 * 
 */
@Repository
@Transactional
public class GeoServerDAO implements IGeoServerDAO {

    @Autowired
    private SessionFactory sessionFactory;

    /**
     * @param sessionFactory the sessionFactory to set
     */
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    /**
     * Constructor for NeapolisHibernateDAO.
     */
    public GeoServerDAO() {
        super();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.geoserver.hibernate.dao.IGeoServerDAO#save(java.lang.Object)
     */
    public void save(Object entity) {
        sessionFactory.getCurrentSession().save(entity);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.geoserver.hibernate.dao.IGeoServerDAO#merge(java.lang.Object)
     */
    public synchronized void merge(Object entity) {
        sessionFactory.getCurrentSession().merge(entity);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.geoserver.hibernate.dao.IGeoServerDAO#delete(java.lang.Object)
     */
    public synchronized void delete(Object entity) {
        sessionFactory.getCurrentSession().delete(entity);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.geoserver.hibernate.dao.IGeoServerDAO#update(java.lang.Object)
     */
    public synchronized void update(Object entity) {
        sessionFactory.getCurrentSession().update(entity);
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
        Query query = sessionFactory.getCurrentSession().createQuery(hql);
        if (arguments != null) {
            for (int argN = 0; argN < arguments.length; argN++) {
                final Object arg = arguments[argN];
                final Class c = arg.getClass();
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
        return list("from " + clazz.getName());
    }

    /**
     * Helper method to return the list of a query
     */
    private List<?> list(String hql) {
        return sessionFactory.getCurrentSession().createQuery(hql).list();
    }

    /**
     * @see Catalog#getDefaultNamespace()
     */
    public synchronized HbNamespaceInfo getDefaultNamespace() {
        String hql = "from " + NamespaceInfo.class.getName() + " where default=?";
        HbNamespaceInfo info = (HbNamespaceInfo) first(hql, new Object[] { Boolean.TRUE });
        return info != null ? (HbNamespaceInfo) sessionFactory.getCurrentSession().get(HbNamespaceInfo.class, info.getId()) : null;
    }

    /**
     * 
     */
    public synchronized <T extends StoreInfo> T getStore(String id, Class<T> clazz) {
        T store = (T) first("from " + clazz.getName() + " where id = ?", new Object[] { id }); 
        return store != null ? (T) sessionFactory.getCurrentSession().get(StoreInfoImpl.class, store.getId()) : null;
    }

    /**
     * 
     */
    public synchronized <T extends StoreInfo> T getStoreByName(String name, Class<T> clazz) {
        T store = (T) first("from " + clazz.getName() + " where name = ?", new Object[] { name }); 
        return store != null ? (T) sessionFactory.getCurrentSession().get(StoreInfoImpl.class, store.getId()) : null;
    }

    /**
     * 
     */
    public synchronized <T extends ResourceInfo> T getResource(String id, Class<T> clazz) {
        T resource = (T) first("from " + clazz.getName() + " where id = ?", new Object[] { id });
        return resource != null ? (T) sessionFactory.getCurrentSession().get(ResourceInfoImpl.class, resource.getId()) : null;
    }

    /**
     * 
     */
    public synchronized <T extends ResourceInfo> T getResourceByName(String ns, String name, Class<T> clazz) {
        T resource = (T) first("from " + clazz.getName() + " where name = ? and namespace = ?", new Object[] { name, ns });
        return resource != null ? (T) sessionFactory.getCurrentSession().get(ResourceInfoImpl.class, resource.getId()) : null;
    }

    /**
     * 
     */
    public synchronized LayerInfo getLayer(String id) {
        LayerInfo layer = (LayerInfo) first("from " + LayerInfo.class.getName() + " where id = ?", new Object[] { id });
        return layer != null ? (LayerInfo) sessionFactory.getCurrentSession().get(LayerInfoImpl.class, layer.getId()) : null;
    }

    /**
     * 
     */
    public synchronized LayerInfo getLayerByName(String name) {
        LayerInfo layer = (LayerInfo) first("from " + LayerInfo.class.getName() + " where name = ?", new Object[] { name });
        return layer != null ? (LayerInfo) sessionFactory.getCurrentSession().get(LayerInfoImpl.class, layer.getId()) : null;
    }

    /**
     * 
     */
    public synchronized <T extends StoreInfo> List<T> getStores(Class<T> clazz) {
        return (List<T>) list(clazz);
    }

    /**
     * 
     */
    public synchronized <T extends ResourceInfo> List<T> getResources(Class<T> clazz) {
        return (List<T>) list(clazz);
    }

    /**
     * 
     */
    public synchronized <T extends ResourceInfo> List<T> getResourcesByNamespace(NamespaceInfo namespace, Class<T> clazz) {
        String hql = "select r from " + clazz.getName() + " r, " + NamespaceInfo.class.getName() + " n where r.namespace = n" + " and n.prefix = '" + namespace.getPrefix() + "'";
        return (List<T>) list(hql);
    }

    /**
     * 
     */
    public synchronized MapInfo getMap(String id) {
        MapInfo map = (MapInfo) first("from " + MapInfo.class.getName() + " where id = ? ", new Object[] { id });
        return map != null ? (MapInfo) sessionFactory.getCurrentSession().get(MapInfoImpl.class, map.getId()) : null;
    }

    /**
     * 
     */
    public synchronized MapInfo getMapByName(String name) {
        MapInfo map = (MapInfo) first("from " + MapInfo.class.getName() + " where name = ?", new Object[] { name });
        return map != null ? (MapInfo) sessionFactory.getCurrentSession().get(MapInfoImpl.class, map.getId()) : null;
    }

    /**
     * 
     */
    public synchronized List<MapInfo> getMaps() {
        return (List<MapInfo>) list(MapInfoImpl.class);
    }

    /**
     * 
     */
    public synchronized List<LayerInfo> getLayers() {
        return (List<LayerInfo>) list(LayerInfoImpl.class);
    }

    /**
     * 
     */
    public synchronized StyleInfo getStyle(String id) {
        StyleInfo style = (StyleInfo) first("from " + StyleInfo.class.getName() + " where id = ?", new Object[] { id }); 
        return style != null ? (StyleInfo) sessionFactory.getCurrentSession().get(StyleInfoImpl.class, style.getId()) : null;
    }

    /**
     * 
     */
    public synchronized StyleInfo getStyleByName(String name) {
        StyleInfo style = (StyleInfo) first("from " + StyleInfo.class.getName() + " where name = ?", new Object[] { name }); 
        return style != null ? (StyleInfo) sessionFactory.getCurrentSession().get(StyleInfoImpl.class, style.getId()) : null;
    }

    /**
     * 
     */
    public synchronized List<StyleInfo> getStyles() {
        return (List<StyleInfo>) list(StyleInfo.class);
    }

    /**
     * 
     */
    public synchronized NamespaceInfo getNamespace(String id) {
        HbNamespaceInfo nameSpace = (HbNamespaceInfo) first("from " + NamespaceInfo.class.getName() + " where id = ?", new Object[] { id });
        return nameSpace != null ? (NamespaceInfo) sessionFactory.getCurrentSession().get(HbNamespaceInfo.class, nameSpace.getId()) : null;
    }

    /**
     * 
     */
    public synchronized HbNamespaceInfo getNamespaceByPrefix(String prefix) {
        HbNamespaceInfo nameSpace = (HbNamespaceInfo) first("from " + NamespaceInfo.class.getName() + " where prefix = ?", new Object[] { prefix });
        return nameSpace != null ? (HbNamespaceInfo) sessionFactory.getCurrentSession().get(HbNamespaceInfo.class, nameSpace.getId()) : null;
    }

    /**
     * 
     */
    public synchronized NamespaceInfo getNamespaceByURI(String uri) {
        NamespaceInfo nameSpace = (NamespaceInfo) first("from " + NamespaceInfo.class.getName() + " where URI = ?", new Object[] { uri });
        return nameSpace != null ? (NamespaceInfo) sessionFactory.getCurrentSession().get(HbNamespaceInfo.class, nameSpace.getId()) : null;
    }

    /**
     * 
     */
    public synchronized List<NamespaceInfo> getNamespaces() {
        return (List<NamespaceInfo>) list(NamespaceInfoImpl.class);
    }

    /**
     * 
     */
    public synchronized HbWorkspaceInfo getDefaultWorkspace() {
        String hql = "from " + HbWorkspaceInfo.class.getName() + " where default=?";
        HbWorkspaceInfo info = (HbWorkspaceInfo) first(hql, new Object[] { Boolean.TRUE });
        return info != null ? (HbWorkspaceInfo) sessionFactory.getCurrentSession().get(HbWorkspaceInfo.class, info.getId()) : null;
    }

    /**
     * 
     */
    public synchronized List<LayerGroupInfo> getLayerGroups() {
        return (List<LayerGroupInfo>) list(LayerGroupInfoImpl.class);
    }

    /**
     * 
     */
    public synchronized ModelInfo getModel(String id) {
        ModelInfo model = (ModelInfo) first("from " + ModelInfo.class.getName() + " where id = ?", new Object[] { id }); 
        return model != null ? (ModelInfo) sessionFactory.getCurrentSession().get(ModelInfoImpl.class, model.getId()) : null;
    }

    /**
     * 
     */
    public synchronized ModelInfo getModelByName(String name) {
        ModelInfo model = (ModelInfo) first("from " + ModelInfo.class.getName() + " where name = ?", new Object[] { name }); 
        return model != null ? (ModelInfo) sessionFactory.getCurrentSession().get(ModelInfoImpl.class, model.getId()) : null;
    }

    /**
     * 
     */
    public synchronized ModelRunInfo getModelRun(String id) {
        ModelRunInfo modelRun = (ModelRunInfo) first("from " + ModelRunInfo.class.getName() + " where id = ?", new Object[] { id }); 
        return modelRun != null ? (ModelRunInfo) sessionFactory.getCurrentSession().get(ModelRunInfoImpl.class, modelRun.getId()) : null;
    }

    /**
     * 
     */
    public synchronized ModelRunInfo getModelRunByName(String name) {
        ModelRunInfo modelRun = (ModelRunInfo) first("from " + ModelRunInfo.class.getName() + " where name = ?", new Object[] { name }); 
        return modelRun != null ? (ModelRunInfo) sessionFactory.getCurrentSession().get(ModelRunInfoImpl.class, modelRun.getId()) : null;
    }

    /**
     * 
     */
    public synchronized List<ModelRunInfo> getModelRuns() {
        return (List<ModelRunInfo>) list(ModelRunInfo.class);
    }

    /**
     * 
     */
    public synchronized List<ModelRunInfo> getModelRuns(ModelInfo model) {
        Query query = sessionFactory.getCurrentSession().createQuery("from " + ModelRunInfo.class.getName() + " where model = ?");
        query.setEntity(0, model);
        return query.list();
    }

    /**
     * 
     */
    public List<CoverageInfo> getGridCoverages(ModelRunInfo modelRun) {
        Query query = sessionFactory.getCurrentSession().createQuery("select mr.gridCoverages from " + ModelRunInfo.class.getName() + " as mr");
        return query.list();
    }

    /**
     * 
     */
    public synchronized List<ModelInfo> getModels() {
        return (List<ModelInfo>) list(ModelInfo.class);
    }

    /**
     * 
     */
    public synchronized <T extends StoreInfo> List<T> getStoresByWorkspace(WorkspaceInfo workspace, Class<T> clazz) {
        return null;
    }

    /**
     * 
     */
    public synchronized WorkspaceInfo getWorkspace(String id) {
        WorkspaceInfo ws = (WorkspaceInfo) first("from " + WorkspaceInfo.class.getName() + " where id = ", new Object[] { id });
        return ws != null ? (WorkspaceInfo) sessionFactory.getCurrentSession().get(HbWorkspaceInfo.class, ws.getId()) : null;
    }

    /**
     * 
     */
    public synchronized WorkspaceInfo getWorkspaceByName(String name) {
        WorkspaceInfo ws = (WorkspaceInfo) first("from " + WorkspaceInfo.class.getName() + " where name = ?", new Object[] { name });
        return ws != null ? (WorkspaceInfo) sessionFactory.getCurrentSession().get(HbWorkspaceInfo.class, ws.getId()) : null;
    }

    /**
     * 
     */
    public synchronized List<WorkspaceInfo> getWorkspaces() {
        return (List<WorkspaceInfo>) list(WorkspaceInfo.class);
    }

    /**
     * 
     */
    public synchronized GeoServerInfo getGeoServer() {
        Iterator i = sessionFactory.getCurrentSession().createQuery("from " + GeoServerInfoImpl.class.getName()).iterate();
        if (i.hasNext())
            return (GeoServerInfo) sessionFactory.getCurrentSession().get(GeoServerInfoImpl.class, ((GeoServerInfo) i.next()).getId());
        
        return null;
    }

    /**
     * 
     */
    public synchronized Collection<? extends ServiceInfo> getServices(Class<?> clazz) {
        List<?> list = sessionFactory.getCurrentSession().createQuery("from " + clazz.getName()).list();
        return (Collection<? extends ServiceInfo>) list;
    }

    /**
     * 
     */
    public synchronized <T extends ServiceInfo> T getService(String id, Class<T> clazz) {
        Iterator i = sessionFactory.getCurrentSession().createQuery("from " + clazz.getName() + " where id = '" + id + "'").iterate();
        if (i.hasNext()) {
            T service = (T) i.next();
            return service != null ? (T) sessionFactory.getCurrentSession().get(ServiceInfoImpl.class, service.getId()) : null;
        }

        return null;
    }

    /**
     * 
     */
    public synchronized <T extends ServiceInfo> T getServiceByName(String name, Class<T> clazz) {
        Iterator i = sessionFactory.getCurrentSession().createQuery("from " + clazz.getName() + " where name = '" + name + "'").iterate();
        if (i.hasNext()) {
            T service = (T) i.next();
            return service != null ? (T) sessionFactory.getCurrentSession().get(ServiceInfoImpl.class, service.getId()) : null;
        }

        return null;
    }

}
