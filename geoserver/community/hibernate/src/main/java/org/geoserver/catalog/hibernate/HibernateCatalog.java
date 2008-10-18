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
import org.geoserver.catalog.CoverageInfo;
import org.geoserver.catalog.CoverageStoreInfo;
import org.geoserver.catalog.DataStoreInfo;
import org.geoserver.catalog.FeatureTypeInfo;
import org.geoserver.catalog.LayerGroupInfo;
import org.geoserver.catalog.LayerInfo;
import org.geoserver.catalog.MapInfo;
import org.geoserver.catalog.ModelInfo;
import org.geoserver.catalog.ModelRunInfo;
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
import org.geoserver.catalog.impl.MapInfoImpl;
import org.geoserver.catalog.impl.NamespaceInfoImpl;
import org.geoserver.catalog.impl.StoreInfoImpl;
import org.hibernate.Query;
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
    private SessionFactory sessionFactory;

    /**
     * Flag indicating wether events are thrown on commit or as they happen
     */
    private boolean fireEventsOnCommit = false;

    /**
     * listeners
     */
    private List<CatalogListener> listeners = new ArrayList<CatalogListener>();

    /**
     * events
     */
    private Map events = Collections.synchronizedMap(new MultiHashMap());

    /**
     * resources
     */
    private ResourcePool resourcePool = new ResourcePool();

    private NamespaceInfo defaultNamespace;

    private final HibernateCatalogFactory hibernateCatalogFactory;

    private Session session;

    public HibernateCatalog() {
        hibernateCatalogFactory = new HibernateCatalogFactory(this);
    }

    /**
     * @see Catalog#getFactory()
     * @see HibernateCatalogFactory
     */
    public HibernateCatalogFactory getFactory() {
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
        } else if (!this.session.isOpen()) {
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
    public <T extends StoreInfo> T getStore(String id, Class<T> clazz) {
        T store = (T) first("from " + clazz.getName() + " where id = ?", new Object[] { id });
        if (store != null) {
            store.setCatalog(this);
        }
        return store;
    }

    /**
     * @see Catalog#getStoreByName(String, Class)
     */
    public <T extends StoreInfo> T getStoreByName(String name, Class<T> clazz) {
        T store = (T) first("from " + clazz.getName() + " where name = ?", new Object[] { name });
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
    public <T extends StoreInfo> List<T> getStores(Class<T> clazz) {
        List<T> stores = list(clazz);
        for (StoreInfo store : stores) {
            store.setCatalog(this);
        }

        return stores;
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
    public List<DataStoreInfo> getDataStores() {
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
    public List<CoverageStoreInfo> getCoverageStores() {
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
        ResourceInfo resource = (ResourceInfo) first("from " + clazz.getName() + " where id = ?",
                new Object[] { id });
        if (resource != null) {
            resource.setCatalog(this);
            return (T) resource;
        }
        return null;
    }

    /**
     * @see Catalog#getResourceByName(String, Class)
     */
    public <T extends ResourceInfo> T getResourceByName(String name, Class<T> clazz) {
        if (getDefaultNamespace() != null) {
            T resource = getResourceByName(getDefaultNamespace().getPrefix(), name, clazz);
            if (resource != null) {
                resource.setCatalog(this);
                return resource;
            }
        }

        // TODO: make a query to retrieve the matching list directly
        List<T> matches = new ArrayList<T>();
        for (Iterator i = getResources(clazz).iterator(); i.hasNext();) {
            T resource = (T) i.next();
            if (name.equals(resource.getName())) {
                matches.add(resource);
            }
        }

        if (matches.size() == 1) {
            return (T) matches.get(0);
        }// he, this method contract is odd... imho the method shouldn't even exist. Rather, let
        // client code care about asking for the resource in the default namespace explicitly

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
            ResourceInfo resource = (ResourceInfo) first("from " + clazz.getName()
                    + " where name = ? and namespace = ?", new Object[] { name, namespace.getId() });
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

        resource.setCatalog(this);
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
    public <T extends ResourceInfo> List<T> getResources(Class<T> clazz) {
        List<T> resources = list(clazz);
        for (T resource : resources) {
            resource.setCatalog(this);
        }
        return resources;
    }

    /**
     * @see Catalog#getResourcesByNamespace(NamespaceInfo, Class)
     */
    public <T extends ResourceInfo> List<T> getResourcesByNamespace(NamespaceInfo namespace,
            Class<T> clazz) {
        String hql = "select r from " + clazz.getName() + " r, " + NamespaceInfo.class.getName()
                + " n where r.namespace = n" + " and n.prefix = '" + namespace.getPrefix() + "'";
        return list(hql);
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
    public List<FeatureTypeInfo> getFeatureTypes() {
        return getResources(FeatureTypeInfo.class);
    }

    /**
     * @see Catalog#getFeatureTypesByNamespace(NamespaceInfo)
     */
    public List<FeatureTypeInfo> getFeatureTypesByNamespace(NamespaceInfo namespace) {
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
    public List<CoverageInfo> getCoverages() {
        return getResources(CoverageInfo.class);
    }

    /**
     * @see Catalog#getCoveragesByNamespace(NamespaceInfo)
     */
    public List<CoverageInfo> getCoveragesByNamespace(NamespaceInfo namespace) {
        return getResourcesByNamespace(namespace, CoverageInfo.class);
    }

    /**
     * @see Catalog#getLayer(String)
     */
    public LayerInfo getLayer(String id) {
        return (LayerInfo) first("from " + LayerInfo.class.getName() + " where id = ?",
                new Object[] { id });
    }

    /**
     * @see Catalog#getLayerByName(String)
     */
    public LayerInfo getLayerByName(String name) {
        return (LayerInfo) first("from " + LayerInfo.class.getName() + " where name = ?",
                new Object[] { name });
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
        return list(MapInfoImpl.class);
    }

    /**
     * @see Catalog#getMap(String)
     */
    public MapInfo getMap(String id) {
        return (MapInfo) first("from " + MapInfo.class.getName() + " where id = ? ",
                new Object[] { id });
    }

    /**
     * @see Catalog#getMapByName(String)
     */
    public MapInfo getMapByName(String name) {
        return (MapInfo) first("from " + MapInfo.class.getName() + " where name = ?",
                new Object[] { name });
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
        return list(LayerInfoImpl.class);
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
        StyleInfo style = (StyleInfo) first("from " + StyleInfo.class.getName() + " where id = ?",
                new Object[] { id });
        style.setCatalog(this);
        return style;
    }

    /**
     * @see Catalog#getStyleByName(String)
     */
    public StyleInfo getStyleByName(String name) {
        StyleInfo style = (StyleInfo) first(
                "from " + StyleInfo.class.getName() + " where name = ?", new Object[] { name });
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
    public List<StyleInfo> getStyles() {
        List<StyleInfo> styles = list(StyleInfo.class);
        for (StyleInfo style : styles) {
            style.setCatalog(this);
        }
        return styles;
    }

    /**
     * @see Catalog#getNamespace(String)
     */
    public NamespaceInfo getNamespace(String id) {
        return (NamespaceInfo) first("from " + NamespaceInfo.class.getName() + " where id = ?", new Object[] { id });
    }

    /**
     * @see Catalog#getNamespaceByPrefix(String)
     */
    public NamespaceInfo getNamespaceByPrefix(String prefix) {
        return (NamespaceInfo) first("from " + NamespaceInfo.class.getName() + " where prefix = ?", new Object[] { prefix });
    }

    /**
     * @see Catalog#getNamespaceByURI(String)
     * @todo: revisit: what prevents us from having the same URI in more than one namespace?
     */
    public NamespaceInfo getNamespaceByURI(String uri) {
        return (NamespaceInfo) first("from " + NamespaceInfo.class.getName() + " where uri = ?", new Object[] { uri });
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
    public List<NamespaceInfo> getNamespaces() {
        return list(NamespaceInfoImpl.class);
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
    public Collection<CatalogListener> getListeners() {
        return listeners;
    }

    private void internalAdd(Object object) {
        getSession().save(object);
        // getSession().flush();
        getSession().getTransaction().commit();
        fireAdded(object);
    }

    private void internalRemove(Object object) {
        getSession().delete(object);
        // getSession().flush();
        getSession().getTransaction().commit();
        fireRemoved(object);
    }

    private void internalSave(Object object) {
        getSession().update(object);
        // getSession().flush();
        getSession().getTransaction().commit();
        fireModified(object, null, null, null);
    }

    private List list(Class clazz) {
        return list("from " + clazz.getName());
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
        return first(hql, null);
    }

    /**
     * Helper method to return the first object of a query.
     * 
     * @param hql
     *            the hql query, may contain {@code ?} argument placeholders
     * @param arguments
     *            the hql query arguments, or {@code null}. Recognized argument types are
     *            {@link String}, {@link Integer}, {@link Boolean}, {@link Float} and
     *            {@link Double}. An object of any other type will result in an unchecked exception
     * @return the first object matching the query or {@code null} if the query returns no results
     */
    protected Object first(final String hql, final Object[] arguments) {
        Query query = getSession().createQuery(hql);
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
                    throw new IllegalArgumentException("Unrecognized type for argument " + argN
                            + " in query '" + hql + "': " + c);
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
     * @todo missing mapping for LayerGroupInfo
     */
    public List<LayerGroupInfo> getLayerGroups() {
        return list(LayerGroupInfoImpl.class);
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
        return (WorkspaceInfo) first("from " + WorkspaceInfo.class.getName() + " where id = ",
                new Object[] { id });
    }

    /**
     * @see Catalog#getWorkspaceByName(String)
     */
    public WorkspaceInfo getWorkspaceByName(String name) {
        return (WorkspaceInfo) first("from " + WorkspaceInfo.class.getName() + " where name = ?",
                new Object[] { name });
    }

    /**
     * @see Catalog#getWorkspaces()
     */
    public List<WorkspaceInfo> getWorkspaces() {
        return list(WorkspaceInfo.class);
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
    public HbWorkspaceInfo getDefaultWorkspace() {
        String hql = "from " + HbWorkspaceInfo.class.getName() + " where default=?";
        Query query = getSession().createQuery(hql);
        query.setBoolean(0, true);
        List list = query.list();
        HbWorkspaceInfo info = null;
        if (list.size() > 0) {
            info = (HbWorkspaceInfo) list.get(0);
        }
        return info;
    }

    /**
     * @see Catalog#setDefaultWorkspace(WorkspaceInfo)
     * @todo implement setDefaultWorkspace
     */
    public void setDefaultWorkspace(WorkspaceInfo workspace) {
        HbWorkspaceInfo currentDefault = getDefaultWorkspace();
        if (currentDefault != null && currentDefault != workspace) {
            currentDefault.setDefault(false);
            getSession().update(currentDefault);
        }
        ((HbWorkspaceInfo) workspace).setDefault(true);
        
        if (first("from " + WorkspaceInfo.class.getName() + " where name = ?", new Object[] { workspace.getName() }) == null) {
            getSession().saveOrUpdate(workspace);
            getSession().getTransaction().commit();
        }
    }

    /**
     * Creates the mimimum set of configuration objects, intended to be used to set up a new
     * database contents
     */
    public void bootStrap() {
        HbWorkspaceInfo defaultWs = getFactory().createWorkspace();
        defaultWs.setName("Default Workspace");
        setDefaultWorkspace(defaultWs);

        NamespaceInfo nsinfo = getFactory().createNamespace();
        nsinfo.setPrefix("topp");
        nsinfo.setURI("http://www.opengeo.org");
        setDefaultNamespace(defaultNamespace);
    }

    // Model / ModelRun methods
    public void add(ModelInfo model) {
        internalAdd(model);
    }

    public void add(ModelRunInfo modelRun) {
        internalAdd(modelRun);
    }

    public ModelInfo getModel(String id) {
        return (ModelInfo) first("from " + ModelInfo.class.getName() + " where id = ?", new Object[] { id });
    }

    public ModelInfo getModelByName(String name) {
        return (ModelInfo) first("from " + ModelInfo.class.getName() + " where name = ?", new Object[] { name });
    }

    public ModelRunInfo getModelRun(String id) {
        return (ModelRunInfo) first("from " + ModelRunInfo.class.getName() + " where id = ?", new Object[] { id });
    }

    public ModelRunInfo getModelRunByName(String name) {
        return (ModelRunInfo) first("from " + ModelRunInfo.class.getName() + " where name = ?", new Object[] { name });
    }

    public List<ModelRunInfo> getModelRuns() {
        return list(ModelRunInfo.class);
    }

    public List<ModelInfo> getModels() {
        return list(ModelInfo.class);
    }

    public void remove(ModelInfo model) {
        internalRemove(model);
    }

    public void remove(ModelRunInfo modelRun) {
        internalRemove(modelRun);
    }

    public void save(ModelInfo model) {
        internalSave(model);
    }

    public void save(ModelRunInfo modelRun) {
        internalSave(modelRun);
    }
}
