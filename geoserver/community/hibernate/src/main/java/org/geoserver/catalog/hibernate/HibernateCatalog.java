package org.geoserver.catalog.hibernate;

import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.beanutils.PropertyUtils;
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
import org.geoserver.catalog.impl.LayerGroupInfoImpl;
import org.geoserver.catalog.impl.LayerInfoImpl;
import org.geoserver.catalog.impl.ResourceInfoImpl;
import org.geoserver.catalog.impl.StoreInfoImpl;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

/**
 * A {@link Catalog} implementation based on Hibernate
 * 
 * @author Justin Deoliveira (OpenGeo)
 * @author Alessio Fabianni (GeoSolutions)
 * @author Gabriel Roldan (OpenGeo)
 */
public class HibernateCatalog implements Catalog {

    /**
     * hibernate access
     */
    // HibernateTemplate hibernate;
    SessionFactory sessionFactory;

    /**
     * Flag indicating wether events are thrown on commit or as they happen
     */
    boolean fireEventsOnCommit = false;

    /**
     * listeners
     */
    protected List listeners = new ArrayList();

    /**
     * events
     */
    Map events = Collections.synchronizedMap(new MultiHashMap());

    /**
     * resources
     */
    protected ResourcePool resourcePool = new ResourcePool();

    private WorkspaceInfo defaultWorkspace;

    private NamespaceInfo defaultNamespace;

    private final HibernateCatalogFactory hibernateCatalogFactory;

    private Session session;
    
    public HibernateCatalog(){
        hibernateCatalogFactory = new HibernateCatalogFactory(this);
    }
    
    /**
     * @see Catalog#getFactory()
     * @see HibernateCatalogFactory
     */
    public CatalogFactory getFactory() {
        return hibernateCatalogFactory;
    }

    /**
     * Session factory getter
     * 
     * @see #setSessionFactory(SessionFactory)
     */
    public SessionFactory getSessionFactory() {
        return sessionFactory;
        // return hibernate.getSessionFactory();
    }

    /**
     * Session factory setter aimed for setter injection in a IOC container
     * 
     * @param sessionFactory
     */
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    /**
     * @todo make private?
     */
    public Session getSession() {
        if (this.session == null) {
            if (sessionFactory.getCurrentSession().isOpen()) {
                this.session = sessionFactory.getCurrentSession();
            } else {
                this.session = sessionFactory.openSession();
            }
        } else if(!this.session.isOpen()) {
            this.session = sessionFactory.openSession();
        }

        if (!this.session.getTransaction().isActive())
            this.session.beginTransaction();

        return this.session;
    }

    /**
     * Sets whether to fire events on commits, intended to be used by unit tests only?
     */
    void setFireEventsOnCommit(boolean fireEventsOnCommit) {
        this.fireEventsOnCommit = fireEventsOnCommit;
    }

    boolean isFireEventsOnCommit() {
        return fireEventsOnCommit;
    }

    /**
     * @see Catalog#getStore(String, Class)
     */
    public StoreInfo getStore(String id, Class clazz) {
        StoreInfo store = (StoreInfo) first("from " + clazz.getName() + " where id = '" + id + "'");
        if (store != null) {
            store.setCatalog(this);
        }
        return store;
    }

    /**
     * @see Catalog#getStoreByName(String, Class)
     */
    public StoreInfo getStoreByName(String name, Class clazz) {
        StoreInfo store = (StoreInfo) first("from " + clazz.getName() + " where name = '" + name + "'");
        if (store != null) {
            store.setCatalog(this);
        }
        return store;
    }

    /**
     * @see Catalog#add(StoreInfo)
     */
    public void add(StoreInfo store) {
        if (store.getWorkspace() == null) {
            store.setWorkspace(getDefaultWorkspace());
        }

        if (store.getWorkspace() == null) {
            throw new IllegalArgumentException("no workspace set and no default available");
        }

        ((StoreInfoImpl) store).setId(store.getName());
        store.setCatalog(this);
        if (getStoreByName(store.getName(), store.getClass()) == null)
            internalAdd(store);
    }

    /**
     * @see Catalog#remove(StoreInfo)
     */
    public void remove(StoreInfo store) {
        internalRemove(store);
    }

    /**
     * @see Catalog#save(StoreInfo)
     */
    public void save(StoreInfo store) {
        internalSave(store);
    }

    /**
     * @see Catalog#getStores(Class)
     */
    public List getStores(Class clazz) {
        List<StoreInfo> stores = list("from " + clazz.getName());
        for (StoreInfo store : stores) {
            store.setCatalog(this);
        }
        
        return stores;
        // return hibernate.find( );
    }

    /**
     * @see Catalog#getDataStore(String)
     */
    public DataStoreInfo getDataStore(String id) {
        return (DataStoreInfo) getStore(id, DataStoreInfo.class);
    }

    /**
     * @see Catalog#getDataStoreByName(String)
     */
    public DataStoreInfo getDataStoreByName(String name) {
        return (DataStoreInfo) getStoreByName(name, DataStoreInfo.class);
    }

    /**
     * @see Catalog#getDataStores()
     */
    public List getDataStores() {
        return getStores(DataStoreInfo.class);
    }

    /**
     * @see Catalog#getCoverageStore(String)
     */
    public CoverageStoreInfo getCoverageStore(String id) {
        return (CoverageStoreInfo) getStore(id, CoverageStoreInfo.class);
    }

    /**
     * @see Catalog#getCoverageStoreByName(String)
     */
    public CoverageStoreInfo getCoverageStoreByName(String name) {
        return (CoverageStoreInfo) getStoreByName(name, CoverageStoreInfo.class);
    }

    /**
     * @see Catalog#getCoverageStores()
     */
    public List getCoverageStores() {
        return getStores(CoverageStoreInfo.class);
    }

    /**
     * @see Catalog#getResourcePool()
     */
    public ResourcePool getResourcePool() {
        return resourcePool;
    }

    /**
     * @see Catalog#setResourcePool(ResourcePool)
     */
    public void setResourcePool(ResourcePool resourcePool) {
        this.resourcePool = resourcePool;
    }

    /**
     * @see Catalog#getResource(String, Class)
     */
    public <T extends ResourceInfo> T getResource(String id, Class<T> clazz) {
        ResourceInfo resource = (ResourceInfo) first("from " + clazz.getName() + " where id = '" + id + "'");
        if (resource != null) {
            resource.setCatalog(this);
            return (T) resource;
        }
        return null;
    }

    /**
     * @see Catalog#getResourceByName(String, Class)
     */
    public ResourceInfo getResourceByName(String name, Class clazz) {
        if (getDefaultNamespace() != null) {
            ResourceInfo resource = getResourceByName(getDefaultNamespace().getPrefix(), name, clazz);
            if (resource != null) {
                resource.setCatalog(this);
                return resource;
            }
        }

        List matches = new ArrayList();
        for (Iterator i = getResources(clazz).iterator(); i.hasNext();) {
            ResourceInfo resource = (ResourceInfo) i.next();
            if (name.equals(resource.getName())) {
                matches.add(resource);
            }
        }

        if (matches.size() == 1) {
            return (ResourceInfo) matches.get(0);
        }

        return null;
    }

    /**
     * @see Catalog#getResourceByName(String, String, Class)
     */
    public <T extends ResourceInfo> T getResourceByName(String ns, String name, Class<T> clazz) {
        NamespaceInfo namespace = null;
        if (ns == null) {
            namespace = getDefaultNamespace();
        } else {
            // first try prefix
            namespace = getNamespaceByPrefix(ns);
            if (namespace == null) {
                // then try uri
                namespace = getNamespaceByURI(ns);
            }
        }

        if (namespace != null) {
            ResourceInfo resource = (ResourceInfo) first("from " + clazz.getName() + " where name = '" + name + "' and namespace = " + namespace.getId());
            if (resource != null) {
                resource.setCatalog(this);
                return (T) resource;
            }
        } else {
            // TODO: throw exception
        }

        return null;
    }

    /**
     * @see Catalog#add(ResourceInfo)
     */
    public void add(ResourceInfo resource) {
        validate(resource);

        if (resource.getNamespace() == null) {
            // default to default namespace
            resource.setNamespace(getDefaultNamespace());
        }

        ((ResourceInfoImpl) resource).setId(resource.getName());
        internalAdd(resource);
    }

    /**
     * Checks whether a resource to be added is in a valid state (ie, it's attached to a
     * {@link StoreInfo})
     */
    void validate(ResourceInfo resource) {
        if (resource.getStore() == null) {
            throw new IllegalArgumentException("source must be part of a store");
        }
    }

    /**
     * @see Catalog#remove(ResourceInfo)
     */
    public void remove(ResourceInfo resource) {
        internalRemove(resource);
    }

    /**
     * @see Catalog#save(ResourceInfo)
     */
    public void save(ResourceInfo resource) {
        internalSave(resource);
    }

    /**
     * @see Catalog#getResources(Class)
     */
    public List getResources(Class clazz) {
        List<ResourceInfo> resources = list("from " + clazz.getName());
        for (ResourceInfo resource : resources) {
            resource.setCatalog(this);
        }
        return resources;
        // return hibernate.find( "from " + clazz.getName() );
    }

    /**
     * @see Catalog#getResourcesByNamespace(NamespaceInfo, Class)
     */
    public List getResourcesByNamespace(NamespaceInfo namespace, Class clazz) {
        return list("select r from " + clazz.getName() + " r, " + NamespaceInfo.class.getName() + " n where r.namespace = n" + " and n.prefix = '" + namespace.getPrefix() + "'");
    }

    /**
     * @see Catalog#getFeatureType(String)
     */
    public FeatureTypeInfo getFeatureType(String id) {
        return (FeatureTypeInfo) getResource(id, FeatureTypeInfo.class);
    }

    /**
     * @see Catalog#getFeatureTypeByName(String)
     */
    public FeatureTypeInfo getFeatureTypeByName(String name) {
        return (FeatureTypeInfo) getResourceByName(null, name, FeatureTypeInfo.class);
    }

    /**
     * @see Catalog#getFeatureTypeByName(String, String)
     */
    public FeatureTypeInfo getFeatureTypeByName(String ns, String name) {
        return (FeatureTypeInfo) getResourceByName(ns, name, FeatureTypeInfo.class);
    }

    /**
     * @see Catalog#getFeatureTypes()
     */
    public List getFeatureTypes() {
        return getResources(FeatureTypeInfo.class);
    }

    /**
     * @see Catalog#getFeatureTypesByNamespace(NamespaceInfo)
     */
    public List getFeatureTypesByNamespace(NamespaceInfo namespace) {
        return getResourcesByNamespace(namespace, FeatureTypeInfo.class);
    }

    /**
     * @see Catalog#getCoverage(String)
     */
    public CoverageInfo getCoverage(String id) {
        return (CoverageInfo) getResource(id, CoverageInfo.class);
    }

    /**
     * @see Catalog#getCoverageByName(String)
     */
    public CoverageInfo getCoverageByName(String name) {
        return (CoverageInfo) getResource(name, CoverageInfo.class);
    }

    /**
     * @see Catalog#getCoverageByName(String, String)
     */
    public CoverageInfo getCoverageByName(String ns, String name) {
        return (CoverageInfo) getResourceByName(ns, name, CoverageInfo.class);
    }

    /**
     * @see Catalog#getCoverages()
     */
    public List getCoverages() {
        return getResources(CoverageInfo.class);
    }

    /**
     * @see Catalog#getCoveragesByNamespace(NamespaceInfo)
     */
    public List getCoveragesByNamespace(NamespaceInfo namespace) {
        return getResourcesByNamespace(namespace, CoverageInfo.class);
    }

    /**
     * @see Catalog#getLayer(String)
     */
    public LayerInfo getLayer(String id) {
        return (LayerInfo) first("from " + LayerInfo.class.getName() + " where id = " + id);
    }

    /**
     * @see Catalog#getLayerByName(String)
     */
    public LayerInfo getLayerByName(String name) {
        return (LayerInfo) first("from " + LayerInfo.class.getName() + " where name = '" + name + "'");
    }

    /**
     * @see Catalog#add(LayerInfo)
     */
    public void add(LayerInfo layer) {
        validate(layer);

        ((LayerInfoImpl) layer).setId(layer.getName());
        if (layer.getType() == null) {
            if (layer.getResource() instanceof FeatureTypeInfo) {
                layer.setType(LayerInfo.Type.VECTOR);
            } else if (layer.getResource() instanceof CoverageInfo) {
                layer.setType(LayerInfo.Type.RASTER);
            } else {
                String msg = "Layer type not set and can't be derived from resource";
                throw new IllegalArgumentException(msg);
            }
        }
        internalAdd(layer);
    }

    void validate(LayerInfo layer) {
        if (layer.getName() == null) {
            throw new NullPointerException("Layer name must not be null");
        }
        if (layer.getResource() == null) {
            throw new NullPointerException("Layer resource must not be null");
        }
        // (JD): not sure if default style should be mandatory
        // if ( layer.getDefaultStyle() == null ){
        // throw new NullPointerException( "Layer default style must not be null" );
        // }
    }

    /**
     * @see Catalog#remove(LayerInfo)
     */
    public void remove(LayerInfo layer) {
        internalRemove(layer);
    }

    /**
     * @see Catalog#getMaps()
     */
    public List<MapInfo> getMaps() {
        return list("from " + MapInfo.class.getName());
    }

    /**
     * @see Catalog#getMap(String)
     */
    public MapInfo getMap(String id) {
        return (MapInfo) first("from " + MapInfo.class.getName() + " where id = " + id);
    }

    /**
     * @see Catalog#getMapByName(String)
     */
    public MapInfo getMapByName(String name) {
        return (MapInfo) first("from " + MapInfo.class.getName() + " where name = '" + name + "'");
    }

    /**
     * @see Catalog#add(MapInfo)
     */
    public void add(MapInfo map) {
        internalAdd(map);
    }

    /**
     * @see Catalog#remove(MapInfo)
     */
    public void remove(MapInfo map) {
        internalRemove(map);
    }

    /**
     * @see Catalog#save(MapInfo)
     */
    public void save(MapInfo map) {
        internalSave(map);
    }

    /**
     * @see Catalog#save(LayerInfo)
     */
    public void save(LayerInfo layer) {
        validate(layer);
        internalSave(layer);
    }

    /**
     * @see Catalog#getLayers()
     */
    public List getLayers() {
        return list("from " + LayerInfo.class.getName());
        // return hibernate.find( "from " + LayerInfo.class.getName() );
    }

    /**
     * @see Catalog#getLayers(ResourceInfo)
     */
    public List<LayerInfo> getLayers(ResourceInfo resource) {
        List<LayerInfo> matches = new ArrayList<LayerInfo>();
        for (Iterator l = getLayers().iterator(); l.hasNext();) {
            LayerInfo layer = (LayerInfo) l.next();
            if (resource.equals(layer.getResource())) {
                matches.add(layer);
            }
        }

        return matches;
    }

    /**
     * @see Catalog#getDefaultNamespace()
     * @todo implement getDefaultNamespace
     */
    public NamespaceInfo getDefaultNamespace() {
        return this.defaultNamespace; // FIXME: I don't have a namespace (even a default one, and
        // neither does coverage ...)
    }

    /**
     * @see Catalog#setDefaultNamespace(NamespaceInfo)
     * @todo implement setDefaultNamespace
     */
    public void setDefaultNamespace(NamespaceInfo defaultNamespace) {
        // NamespaceInfo ns = namespaces.get( defaultNamespace.getPrefix() );
        // if ( ns == null ) {
        // throw new IllegalArgumentException( "No such namespace: '" + defaultNamespace.getPrefix()
        // + "'" );
        // }

        this.defaultNamespace = defaultNamespace; // FIXME: I don't have a namespace, neither does
        // coverage - move to DataStoreInfo
    }

    /**
     * @see Catalog#getStyle(String)
     */
    public StyleInfo getStyle(String id) {
        StyleInfo style = (StyleInfo) first("from " + StyleInfo.class.getName() + " where id = " + id);
        style.setCatalog(this);
        return style;
    }

    /**
     * @see Catalog#getStyleByName(String)
     */
    public StyleInfo getStyleByName(String name) {
        StyleInfo style = (StyleInfo) first("from " + StyleInfo.class.getName() + " where name = '" + name + "'");
        style.setCatalog(this);
        return style;
    }

    /**
     * @see Catalog#add(StyleInfo)
     */
    public void add(StyleInfo style) {
        internalAdd(style);
    }

    /**
     * @see Catalog#remove(StyleInfo)
     */
    public void remove(StyleInfo style) {
        internalRemove(style);
    }

    /**
     * @see Catalog#save(StyleInfo)
     */
    public void save(StyleInfo style) {
        internalSave(style);
    }

    /**
     * @see Catalog#getStyles()
     */
    public List getStyles() {
        List<StyleInfo> styles = list("from " + StyleInfo.class.getName());
        for (StyleInfo style : styles) {
            style.setCatalog(this);
        }
        return styles;
        // return hibernate.find( "from " + StyleInfo.class.getName() );
    }

    /**
     * @see Catalog#getNamespace(String)
     */
    public NamespaceInfo getNamespace(String id) {
        return (NamespaceInfo) first("from " + NamespaceInfo.class.getName() + " where ns_id = '" + id + "'");
    }

    /**
     * @see Catalog#getNamespaceByPrefix(String)
     */
    public NamespaceInfo getNamespaceByPrefix(String prefix) {
        return (NamespaceInfo) first("from " + NamespaceInfo.class.getName() + " where prefix = '" + prefix + "'");
    }

    /**
     * @see Catalog#getNamespaceByURI(String)
     * @todo: revisit: what prevents us from having the same URI in more than one namespace?
     */
    public NamespaceInfo getNamespaceByURI(String uri) {
        return (NamespaceInfo) first("from " + NamespaceInfo.class.getName() + " where uri = '" + uri + "'");
    }

    /**
     * @see Catalog#add(NamespaceInfo)
     */
    public void add(NamespaceInfo namespace) {
        if (getNamespaceByPrefix(namespace.getPrefix()) == null)
            internalAdd(namespace);
    }

    /**
     * @see Catalog#remove(NamespaceInfo)
     */
    public void remove(NamespaceInfo namespace) {
        internalRemove(namespace);
    }

    /**
     * @see Catalog#save(NamespaceInfo)
     */
    public void save(NamespaceInfo namespace) {
        internalSave(namespace);
    }

    /**
     * @see Catalog#getNamespaces()
     */
    public List getNamespaces() {
        return list("from " + NamespaceInfo.class.getName());
        // return hibernate.find( "from " + NamespaceInfo.class.getName() );
    }

    /**
     * @see Catalog#addListener(CatalogListener)
     */
    public void addListener(CatalogListener listener) {
        listeners.add(listener);
    }

    /**
     * @see Catalog#removeListener(CatalogListener)
     */
    public void removeListener(CatalogListener listener) {
        listeners.remove(listener);
    }

    /**
     * @see Catalog#getListeners()
     * @todo revisit: we have add and remove listener, it seems like we should return a safe copy
     *       here!
     */
    public Collection getListeners() {
        return listeners;
    }

    /**
     * @todo revisit: where and what for this method comes from?
     * @param cql
     * @return
     */
    public Iterator search(String cql) {
        // TODO Auto-generated method stub
        return null;
    }

    private void internalAdd(Object object) {
        getSession().save(object);
//        getSession().flush();
        getSession().getTransaction().commit();
        fireAdded(object);
    }

    private void internalRemove(Object object) {
        getSession().delete(object);
//        getSession().flush();
        getSession().getTransaction().commit();
        fireRemoved(object);
    }

    private void internalSave(Object object) {
        getSession().update(object);
//        getSession().flush();
        getSession().getTransaction().commit();
        fireModified(object, null, null, null);
    }

    /**
     * Helper method to return the list of a query
     */
    private List list(String hql) {
        List list = getSession().createQuery(hql).list();
        return list;
    }

    /**
     * Helper method to return the first object of a query.
     */
    protected Object first(String hql) {
        Iterator i = getSession().createQuery(hql).iterate();

        if (i.hasNext()) {
            Object first = i.next();
            return first;
        }

        return null;
    }

    /**
     * Fires an added event with the {@code addedObject} as source
     * 
     * @param addedObject
     */
    void fireAdded(Object addedObject) {
        CatalogAddEventImpl event = new CatalogAddEventImpl();
        event.setSource(addedObject);

        fireEvent(event);
    }

    /**
     * Fires a modified event over the {@code modifiedObject}
     * 
     * @param modifiedObject
     *            the modified object to be set as the event's source
     * @param propertyNames
     *            the list of changed property names
     * @param oldValues
     *            the modified properties old values
     * @param newValues
     *            the modified properties new values
     */
    void fireModified(Object modifiedObject, List propertyNames, List oldValues, List newValues) {
        CatalogModifyEventImpl event = new CatalogModifyEventImpl();

        event.setSource(modifiedObject);
        event.setPropertyNames(propertyNames);
        event.setOldValues(oldValues);
        event.setNewValues(newValues);

        fireEvent(event);
    }

    /**
     * Fires a removed event
     * 
     * @param removedObject
     *            the object removed from the catalog, to be set as the event's source
     */
    void fireRemoved(Object removedObject) {
        CatalogRemoveEventImpl event = new CatalogRemoveEventImpl();
        event.setSource(removedObject);

        fireEvent(event);
    }

    private void fireEvent(CatalogEvent event) {
        if (fireEventsOnCommit) {
            // store for later
            events.put(getSession().getTransaction(), event);
        } else {
            // fire now
            doFireEvent(event);
        }

    }

    void fireEvents(Transaction tx) {
        if (!fireEventsOnCommit)
            return;

        Collection toFire = (Collection) events.remove(tx);
        if (toFire == null) {
            return;
        }
        for (Iterator e = toFire.iterator(); e.hasNext();) {
            CatalogEvent event = (CatalogEvent) e.next();
            doFireEvent(event);
        }
    }

    /**
     * Fires a catalog event for good
     * 
     * @see #fireEvent(CatalogEvent)
     */
    private void doFireEvent(CatalogEvent event) {
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

    void rollbackEvents(Transaction tx) {
        events.remove(tx);
    }

    /**
     * Diffs two objects determing which properties have changed.
     * <p>
     * <tt>o1</tt> and <tt>o2</tt> must be of the same type.
     * </p>
     * 
     * @param o1
     *            The first object.
     * @param o2
     *            The second object.
     * 
     * @return List of properties of the object that differ.
     */
    private List diff(Object o1, Object o2) {

        List changed = new ArrayList();
        PropertyDescriptor[] properties = PropertyUtils.getPropertyDescriptors(o1);

        BeanComparator comparator = new BeanComparator();
        for (int i = 0; i < properties.length; i++) {
            comparator.setProperty(properties[i].getName());
            if (comparator.compare(o1, o2) != 0) {
                changed.add(properties[i].getName());
            }
        }

        return changed;

    }

    /** ***************************************************************************************************** */
    /** * FIX THIS ** */

    /**
     * @see Catalog#add(LayerGroupInfo)
     */
    public void add(LayerGroupInfo layerGroup) {
        ((LayerGroupInfoImpl) layerGroup).setId(layerGroup.getName());
        internalAdd(layerGroup);
    }

    /**
     * @see Catalog#add(WorkspaceInfo)
     */
    public void add(WorkspaceInfo workspace) {
        if (getWorkspaceByName(workspace.getName()) == null)
            internalAdd(workspace);
    }

    /**
     * @see Catalog#dispose()
     */
    public void dispose() {
        sessionFactory.close();
        listeners.clear();
        events.clear();

        resourcePool.dispose();

        getSession().close();
    }

    /**
     * @see Catalog#getLayerGroup(String)
     */
    public LayerGroupInfo getLayerGroup(String id) {
        return null;
    }

    /**
     * @see Catalog#getLayerGroupByName(String)
     */
    public LayerGroupInfo getLayerGroupByName(String name) {
        return null;
    }

    /**
     * @see Catalog#getLayerGroups()
     */
    public List<LayerGroupInfo> getLayerGroups() {
        return list("from " + LayerGroupInfo.class.getName());
    }

    /**
     * @see Catalog#getStoresByWorkspace(WorkspaceInfo, Class)
     */
    public <T extends StoreInfo> List<T> getStoresByWorkspace(WorkspaceInfo workspace,
            Class<T> clazz) {
        return null;
    }

    /**
     * @see Catalog#getWorkspace(String)
     */
    public WorkspaceInfo getWorkspace(String id) {
        return (WorkspaceInfo) first("from " + WorkspaceInfo.class.getName() + " where id = '" + id + "'");
    }

    /**
     * @see Catalog#getWorkspaceByName(String)
     */
    public WorkspaceInfo getWorkspaceByName(String name) {
        return (WorkspaceInfo) first("from " + WorkspaceInfo.class.getName() + " where name = '" + name + "'");
    }

    /**
     * @see Catalog#getWorkspaces()
     */
    public List<WorkspaceInfo> getWorkspaces() {
        return list("from " + WorkspaceInfo.class.getName());
    }

    /**
     * @see Catalog#remove(LayerGroupInfo)
     */
    public void remove(LayerGroupInfo layerGroup) {
        internalRemove(layerGroup);
    }

    /**
     * @see Catalog#remove(WorkspaceInfo)
     */
    public void remove(WorkspaceInfo workspace) {
        internalRemove(workspace);
    }

    /**
     * @see Catalog#save(LayerGroupInfo)
     */
    public void save(LayerGroupInfo layerGroup) {
        internalSave(layerGroup);
    }

    /**
     * @see Catalog#save(WorkspaceInfo)
     */
    public void save(WorkspaceInfo workspace) {
        internalSave(workspace);
    }

    /**
     * @see Catalog#getDefaultWorkspace()
     * @todo implement getDefaultWorkspace
     */
    public WorkspaceInfo getDefaultWorkspace() {
        return /* this.defaultWorkspace */null;
    }

    /**
     * @see Catalog#setDefaultWorkspace(WorkspaceInfo)
     * @todo implement setDefaultWorkspace
     */
    public void setDefaultWorkspace(WorkspaceInfo workspace) {
        // TODO: FIX THIS... search the defWorkSpace on the DB
        // this.defaultWorkspace = workspace;
    }

}
