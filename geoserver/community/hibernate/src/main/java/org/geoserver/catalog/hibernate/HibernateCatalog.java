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
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

public class HibernateCatalog implements Catalog {

	/**
	 * hibernate access
	 */
	//HibernateTemplate hibernate;
	SessionFactory sessionFactory;
	
	/**
	 * Flag indicating wether events are thrown on commit or as they happen
	 */
	boolean fireEventsOnCommit = false;
	
	/**
	 * listeners
	 */
	List listeners = new ArrayList();
	/**
	 * events
	 */
	Map events = Collections.synchronizedMap( new MultiHashMap() );
	
	
	public CatalogFactory getFactory() {
		return new HibernateCatalogFactory( this );
	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
		//return hibernate.getSessionFactory();
	}
	
	public Session getSession() {
		return sessionFactory.getCurrentSession();
	}
	
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	public void setFireEventsOnCommit(boolean fireEventsOnCommit) {
		this.fireEventsOnCommit = fireEventsOnCommit;
	}
	
	public boolean isFireEventsOnCommit() {
		return fireEventsOnCommit;
	}
	
	
	public StoreInfo getStore(String id, Class clazz) {
		return (StoreInfo) first( "from " + clazz.getName() + " where id = " + id );
	}
	
	public StoreInfo getStoreByName(String name, Class clazz) {
		return (StoreInfo) first( "from " + clazz.getName() + " where name = '" + name + "'" );
	}

	public void add(StoreInfo store) {
		internalAdd( store );
	}

	public void save(StoreInfo store) {
		internalSave( store );
	}

	public void remove(StoreInfo store) {
		internalRemove( store );
	}

	public List getStores(Class clazz) {
		return list( "from " + clazz.getName() );
		//return hibernate.find( );
	}

	public DataStoreInfo getDataStore(String id) {
		return (DataStoreInfo) getStore(id, DataStoreInfo.class);
	}

	public DataStoreInfo getDataStoreByName(String name) {
		return (DataStoreInfo) getStoreByName( name, DataStoreInfo.class );
	}
	
	public List getDataStores() {
		return getStores(DataStoreInfo.class);
	}
	
	public CoverageStoreInfo getCoverageStore(String id) {
		return (CoverageStoreInfo) getStore(id, CoverageStoreInfo.class);
	}

	public CoverageStoreInfo getCoverageStoreByName(String name) {
		return (CoverageStoreInfo) getStoreByName( name, CoverageStoreInfo.class );
	}
	
	public List getCoverageStores() {
		return getStores(CoverageStoreInfo.class);
	}

	/**
	 * TODO: FIX ME!!!!
	 */
	public ResourcePool getResourcePool() {
	    // TODO: ??
	    return null;
	}

	public <T extends ResourceInfo> T getResource(String id, Class<T> clazz) {
		return (T) first( "from " + clazz.getName() + " where id = " + id );
	}

	public <T extends ResourceInfo> T getResourceByName(String name, Class<T> clazz) {
	    return getResourceByName(null, name, clazz);
	}

	public <T extends ResourceInfo> T getResourceByName( String ns, String name, Class<T> clazz ) {
		NamespaceInfo namespace = null;
		if ( ns == null ) {
			namespace = getDefaultNamespace();
		}
		else {
			//first try prefix
			namespace = getNamespaceByPrefix( ns );
			if ( namespace == null ) {
				//then try uri
				namespace = getNamespaceByURI( ns );
			}
		}
		
		if ( namespace != null ) {
			return (T) first("from " + clazz.getName() + 
				" where name = '" + name + "' and namespace = " + namespace.getId() );
		}
		else {
			//TODO: throw exception
		}
		
		return null;
	}
	
	public void add(ResourceInfo resource) {
		internalAdd( resource );
	}

	public void remove(ResourceInfo resource) {
		internalRemove( resource );
	}

	public void save(ResourceInfo resource) {
		internalSave( resource );
	}

	public List getResources(Class clazz) {
		return list( "from " + clazz.getName() );
		//return hibernate.find( "from " + clazz.getName() );
	}

	public List getResourcesByNamespace(NamespaceInfo namespace, Class clazz) {
		return list( "select r from " + clazz.getName()  + " r, " 
			+ NamespaceInfo.class.getName() + " n where r.namespace = n" +
			" and n.prefix = '" + namespace.getPrefix() + "'" ); 
	}
	
	/**
	 * TODO: FIX ME!!!!
	 */
	public void setResourcePool(ResourcePool resourcePool) {
	    // TODO: ??
	}
	
	public FeatureTypeInfo getFeatureType(String id) {
		return (FeatureTypeInfo) getResource( id, FeatureTypeInfo.class );
	}
	
	public FeatureTypeInfo getFeatureTypeByName(String name) {
            return (FeatureTypeInfo) getResourceByName( null, name, FeatureTypeInfo.class );
	}

	public FeatureTypeInfo getFeatureTypeByName(String ns, String name) {
		return (FeatureTypeInfo) getResourceByName( ns, name, FeatureTypeInfo.class );
	}

	public List getFeatureTypes() {
		return getResources(FeatureTypeInfo.class);
	}

	public List getFeatureTypesByNamespace(NamespaceInfo namespace) {
		return getResourcesByNamespace( namespace, FeatureTypeInfo.class );
	}
	
	public CoverageInfo getCoverage(String id) {
		return (CoverageInfo) getResource( id, CoverageInfo.class );
	}

	public CoverageInfo getCoverageByName(String name) {
            return (CoverageInfo) getResource( name, CoverageInfo.class );
	}

	public CoverageInfo getCoverageByName(String ns, String name) {
		return (CoverageInfo) getResourceByName( ns, name, CoverageInfo.class );
	}
	
	public List getCoverages() {
		return getResources( CoverageInfo.class );
	}

	public List getCoveragesByNamespace(NamespaceInfo namespace) {
		return getResourcesByNamespace( namespace, CoverageInfo.class );
	}
	
	public LayerInfo getLayer(String id) {
		return (LayerInfo) first( "from " + LayerInfo.class.getName() + " where id = " + id );
	}

	public LayerInfo getLayerByName(String name) {
            return (LayerInfo) first( "from " + LayerInfo.class.getName() + " where name = " + name );
	}

	public void add(LayerInfo layer) {
		internalAdd( layer );
	}
	
	public void remove(LayerInfo layer) {
		internalRemove( layer );
	}
        
        public List<MapInfo> getMaps() {
            return list( "from " + MapInfo.class.getName() );
        }
        
        public MapInfo getMap(String id) {
            return (MapInfo) first( "from " + MapInfo.class.getName() + " where id = " + id);
        }

        public MapInfo getMapByName(String name) {
            return (MapInfo) first( "from " + MapInfo.class.getName() + " where name = " + name);
        }

        public void add( MapInfo map ) {
            internalAdd( map );
        }
	
        public void remove( MapInfo map ) {
            internalRemove( map );
        }
        
        public void save(MapInfo map) {
            internalSave(map);
        }
        
	public void save(LayerInfo layer) {
		internalSave( layer );
	}
	
	public List getLayers() {
		return list( "from " + LayerInfo.class.getName() );
		//return hibernate.find( "from " + LayerInfo.class.getName() );
	}
	
	public List<LayerInfo> getLayers(ResourceInfo resource) {
	    List<LayerInfo> matches = new ArrayList<LayerInfo>();
	    for (Iterator l = getLayers().iterator(); l.hasNext();) {
	        LayerInfo layer = (LayerInfo) l.next();
	        if ( resource.equals( layer.getResource() ) ) {
	            matches.add( layer );
	        }
	    }

	    return matches;
	}

	public NamespaceInfo getDefaultNamespace() {
		return null; // FIXME: I don't have a namespace (even a default one, and neither does coverage ...)
	}

	public void setDefaultNamespace(NamespaceInfo defaultNamespace) {
		// FIXME: I don't have a namespace, neither does coverage - move to DataStoreInfo
	}

	public StyleInfo getStyle(String id) {
		return (StyleInfo) first( "from " + StyleInfo.class.getName() + " where id = " + id );
	}

	public StyleInfo getStyleByName(String name) {
		return (StyleInfo) first( "from " + StyleInfo.class.getName() + " where name = '" + name + "'" );
	}
	
	public void add(StyleInfo style) {
		internalAdd( style );
	}

	public void remove(StyleInfo style) {
		internalRemove( style );
	}

	public void save(StyleInfo style) {
		internalSave( style );
	}

	public List getStyles() {
		return list( "from " + StyleInfo.class.getName() );
		//return hibernate.find( "from " + StyleInfo.class.getName() );
	}

	public NamespaceInfo getNamespace(String id) {
		return null;
	}

	public NamespaceInfo getNamespaceByPrefix(String prefix) {
		return (NamespaceInfo) first( "from " + NamespaceInfo.class.getName() 
			+ " where prefix = '" + prefix + "'" );
	}
	
	public NamespaceInfo getNamespaceByURI(String uri) {
		return (NamespaceInfo) first( "from " + NamespaceInfo.class.getName() 
			+ " where uri = '" + uri + "'" );
	}
	
	public void add(NamespaceInfo namespace) {
		internalAdd( namespace );
	}

	public void remove(NamespaceInfo namespace) {
		internalRemove( namespace );
	}

	public void save(NamespaceInfo namespace) {
		internalSave( namespace );
	}

	public List getNamespaces() {
		return list( "from " + NamespaceInfo.class.getName() );
		//return hibernate.find( "from " + NamespaceInfo.class.getName() );
	}

	public void addListener(CatalogListener listener) {
		listeners.add(listener);
	}

	public void removeListener(CatalogListener listener) {
		listeners.remove(listener);
	}
	
	public Collection getListeners() {
		return listeners;
	}
	
	public Iterator search(String cql) {
		// TODO Auto-generated method stub
		return null;
	}
	
	protected void internalAdd( Object object ) {
		getSession().save( object );
		added( object );
	}
	
	protected void internalRemove( Object object ) {
		getSession().delete( object );
		removed( object );
	}
	
	protected void internalSave( Object object ) {
		getSession().save( object );
		modified( object, null, null, null );
	}
	
	/*
	 * Helper method to return the list of a query 
	 */
	protected List list( String hql ) {
		return getSession().createQuery( hql ).list();
	}
	
	/*
	 * Helper method to return the first object of a query. 
	 */
	protected Object first( String hql ) {
		Iterator i = getSession().createQuery( hql ).iterate();
		
		if ( i.hasNext() ) {
			return i.next();
		}
		
		return null;
	}
	
	protected void added( Object object ) {
		CatalogAddEventImpl event = new CatalogAddEventImpl();
		event.setSource( object );
	
		event( event );
	}
	
	protected void modified( Object object, List propertyNames, List oldValues, List newValues ) {
		CatalogModifyEventImpl event = new CatalogModifyEventImpl();
		
		event.setSource( object );
		event.setPropertyNames( propertyNames );
		event.setOldValues( oldValues );
		event.setNewValues( newValues );
		
		event( event );
	}
	
	protected void removed( Object object ) {
		CatalogRemoveEventImpl event = new CatalogRemoveEventImpl();
		event.setSource( object );
		
		event( event );
	}
	
	protected void event( CatalogEvent event ) {
		if ( fireEventsOnCommit ) {
			//store for later
			events.put(getSession().getTransaction(), event);		
		}
		else {
			//fire now
			fireEvent( event );
		}
	
	}
	
	protected void fireEvents( Transaction tx ) {
		if ( !fireEventsOnCommit ) 
			return;
		
		Collection toFire = (Collection) events.remove( tx );
		if ( toFire == null ) {
			return;
		}
		for ( Iterator e = toFire.iterator(); e.hasNext(); ) {
			CatalogEvent event = (CatalogEvent) e.next();
			fireEvent( event );
		}
	}
	
	protected void fireEvent( CatalogEvent event ) {
		for ( Iterator l = listeners.iterator(); l.hasNext(); ) {
			CatalogListener listener = (CatalogListener) l.next();
			if ( event instanceof CatalogAddEvent ) {
				listener.handleAddEvent ( (CatalogAddEvent) event );	
			}
			else if ( event instanceof CatalogRemoveEvent ) {
				listener.handleRemoveEvent ( (CatalogRemoveEvent) event );	
			}
			else if ( event instanceof CatalogModifyEvent ) {
				listener.handleModifyEvent ( (CatalogModifyEvent) event );	
			}
		}
	}
	
	protected void rollbackEvents( Transaction tx ) {
		events.remove( tx );
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
	protected List diff(Object o1, Object o2) {

		List changed = new ArrayList();
		PropertyDescriptor[] properties = PropertyUtils
				.getPropertyDescriptors(o1);

		BeanComparator comparator = new BeanComparator();
		for (int i = 0; i < properties.length; i++) {
			comparator.setProperty(properties[i].getName());
			if (comparator.compare(o1, o2) != 0) {
				changed.add(properties[i].getName());
			}
		}

		return changed;

	}
	
	
	
	
	/********************************************************************************************************/
	
	
	
    /**
     * TODO: FIX ME!!!!
     */
    public void add(LayerGroupInfo layerGroup) {
        // TODO: internalAdd(layerGroup);
    }

    /**
     * TODO: FIX ME!!!!
     */
    public void add(WorkspaceInfo workspace) {
        // TODO: internalAdd(workspace);
    }

    public void dispose() {
        sessionFactory.close();
        listeners.clear();
        events.clear();
    }

    public WorkspaceInfo getDefaultWorkspace() {
        return null;
    }

    public LayerGroupInfo getLayerGroup(String id) {
        return null;
    }

    public LayerGroupInfo getLayerGroupByName(String name) {
        return null;
    }

    public List<LayerGroupInfo> getLayerGroups() {
        return null;
    }

    public <T extends StoreInfo> List<T> getStoresByWorkspace(WorkspaceInfo workspace,
            Class<T> clazz) {
        return null;
    }

    public WorkspaceInfo getWorkspace(String id) {
        return null;
    }

    public WorkspaceInfo getWorkspaceByName(String name) {
        return null;
    }

    public List<WorkspaceInfo> getWorkspaces() {
        return null;
    }

    public void remove(LayerGroupInfo layerGroup) {
    }

    public void remove(WorkspaceInfo workspace) {
    }

    public void save(LayerGroupInfo layerGroup) {
    }

    public void save(WorkspaceInfo workspace) {
    }

    public void setDefaultWorkspace(WorkspaceInfo workspace) {
    }

}
