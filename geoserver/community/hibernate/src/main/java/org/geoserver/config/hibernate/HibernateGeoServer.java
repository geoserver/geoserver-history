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
import org.hibernate.Transaction;

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
        return getSessionFactory().getCurrentSession();
    }

    public GeoServerFactory getFactory() {
        return factory;
    }

    public void setFactory(GeoServerFactory factory) {
        this.factory = factory;
    }

    public GeoServerInfo getGlobal() {
        getSession().beginTransaction();
        Iterator i = getSession().createQuery("from " + GeoServerInfo.class.getName()).iterate();
        if (i.hasNext()) {
            GeoServerInfo geoserver = (GeoServerInfo) i.next();
            return geoserver;
        }
        
        return null;
    }

    public void setGlobal(GeoServerInfo configuration) {
        Transaction transaction = getSession().getTransaction();
        transaction.begin();
        getSession().save(configuration);
        transaction.commit();
    }

    public void save(GeoServerInfo geoServer) {
        Transaction transaction = getSession().getTransaction();
        transaction.begin();
        getSession().save(geoServer);
        transaction.commit();
    }

    public void add(ServiceInfo service) {
        Transaction transaction = getSession().getTransaction();
        transaction.begin();
        service.setGeoServer(getGlobal());
        getSession().save(service);
        transaction.commit();
    }

    public void remove(ServiceInfo service) {
        Transaction transaction = getSession().getTransaction();
        transaction.begin();
        getSession().delete(service);
        transaction.commit();
    }

    public void save(ServiceInfo service) {
        Transaction transaction = getSession().getTransaction();
        transaction.begin();
        getSession().save(service);
        transaction.commit();
    }

    public Collection<? extends ServiceInfo> getServices() {
        return getServices(ServiceInfo.class);
    }

    public Collection<? extends ServiceInfo> getServices(Class<?> clazz) {
        Transaction transaction = getSession().getTransaction();
        transaction.begin();
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
        Transaction transaction = getSession().getTransaction();
        transaction.begin();
        Iterator i = getSession().createQuery("from " + clazz.getName() + " where id = " + id).iterate();
        if (i.hasNext()) {
            T service = (T) i.next();
            return service;
        }

        transaction.rollback();
        return null;
    }

    public ServiceInfo getServiceByName(String name) {
        return getServiceByName(name, ServiceInfo.class);
    }

    public <T extends ServiceInfo> T getServiceByName(String name, Class<T> clazz) {
        Transaction transaction = getSession().getTransaction();
        transaction.begin();
        Iterator i = getSession().createQuery("from " + clazz.getName() + " where name = '" + name + "'").iterate();
        if (i.hasNext()) {
            T service = (T) i.next();
            return service;
        }

        transaction.rollback();
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
    }

}