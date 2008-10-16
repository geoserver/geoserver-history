package org.geoserver.config.hibernate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.geoserver.catalog.Catalog;
import org.geoserver.config.ConfigurationListener;
import org.geoserver.config.GeoServer;
import org.geoserver.config.GeoServerFactory;
import org.geoserver.config.GeoServerInfo;
import org.geoserver.config.ServiceInfo;
import org.geoserver.config.impl.GeoServerFactoryImpl;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.dialect.FirebirdDialect;

public class HibernateGeoServer implements GeoServer {

    /**
     * factory to create config objects.
     */
    GeoServerFactory factory = new GeoServerFactoryImpl();

    Catalog catalog;
    
    List<ConfigurationListener> listeners = new ArrayList<ConfigurationListener>();

    /**
     * hibernate session factory
     */
    SessionFactory sessionFactory;

    private Session session;

    public HibernateGeoServer() {
    }

    public Catalog getCatalog() {
        return catalog;
    }

    public void setCatalog(Catalog catalog) {
        this.catalog = catalog;
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

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

    public GeoServerFactory getFactory() {
        return factory;
    }

    public void setFactory(GeoServerFactory factory) {
        this.factory = factory;
    }

    public GeoServerInfo getGlobal() {
        Iterator i = getSession().createQuery("from " + GeoServerInfo.class.getName()).iterate();
        if (i.hasNext()) {
            GeoServerInfo geoserver = (GeoServerInfo) i.next();
            return geoserver;
        }
        
        return null;
    }

    public void setGlobal(GeoServerInfo configuration) {
        getSession().save(configuration);
//        getSession().flush();
        getSession().getTransaction().commit();
    }

    public void save(GeoServerInfo geoServer) {
        getSession().update(geoServer);
//        getSession().flush();
        getSession().getTransaction().commit();
    }

    public void add(ServiceInfo service) {
        service.setGeoServer(getGlobal());
        if(getService(service.getId()) == null)
            getSession().save(service);
        else
            getSession().update(service);
//        getSession().flush();
        getSession().getTransaction().commit();
    }

    public void remove(ServiceInfo service) {
        getSession().delete(service);
//        getSession().flush();
        getSession().getTransaction().commit();
    }

    public void save(ServiceInfo service) {
        getSession().update(service);
//        getSession().flush();
        getSession().getTransaction().commit();
    }

    public Collection<? extends ServiceInfo> getServices() {
        return getServices(ServiceInfo.class);
    }

    public Collection<? extends ServiceInfo> getServices(Class<?> clazz) {
        List list = getSession().createQuery("from " + clazz.getName()).list();
        return list;
    }

    public <T extends ServiceInfo> T getService(Class<T> clazz) {
        for (ServiceInfo si : getServices(clazz)) {
            if (clazz.isAssignableFrom(si.getClass())) {
                return (T) si;
            }
        }

        return null;
    }

    public ServiceInfo getService(String id) {
        return getService(id, ServiceInfo.class);
    }

    public <T extends ServiceInfo> T getService(String id, Class<T> clazz) {
        Iterator i = getSession().createQuery("from " + clazz.getName() + " where id = '" + id + "'").iterate();
        if (i.hasNext()) {
            T service = (T) i.next();
            return service;
        }

        return null;
    }

    public ServiceInfo getServiceByName(String name) {
        return getServiceByName(name, ServiceInfo.class);
    }

    public <T extends ServiceInfo> T getServiceByName(String name, Class<T> clazz) {
        Iterator i = getSession().createQuery("from " + clazz.getName() + " where name = '" + name + "'").iterate();
        if (i.hasNext()) {
            T service = (T) i.next();
            return service;
        }

        return null;
    }

    public void addListener(ConfigurationListener listener) {
        listeners.add( listener );
    }
    
    public void removeListener(ConfigurationListener listener) {
        listeners.remove( listener );
    }

    public void dispose() {
        catalog.dispose();
        listeners.clear();
        
        getSession().close();
    }

}