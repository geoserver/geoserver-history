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
        Iterator i = getSession().createQuery("from " + GeoServerInfo.class.getName()).iterate();
        if (i.hasNext()) {
            return (GeoServerInfo) i.next();
        }
        return null;
    }

    public void setGlobal(GeoServerInfo configuration) {
        getSession().save(configuration);
    }

    public void save(GeoServerInfo geoServer) {
        getSession().save(geoServer);
    }

    public void add(ServiceInfo service) {
        service.setGeoServer(getGlobal());
        getSession().save(service);
    }

    public void remove(ServiceInfo service) {
        getSession().delete(service);
    }

    public void save(ServiceInfo service) {
        getSession().save(service);
    }

    public Collection<? extends ServiceInfo> getServices() {
        return getServices(ServiceInfo.class);
    }

    public Collection<? extends ServiceInfo> getServices(Class<?> clazz) {
        return getSession().createQuery("from " + clazz.getName()).list();
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
        Iterator i = getSession().createQuery("from " + clazz.getName() + " where id = " + id)
                .iterate();
        if (i.hasNext()) {
            return (T) i.next();
        }

        return null;
    }

    public ServiceInfo getServiceByName(String name) {
        return getServiceByName(name, ServiceInfo.class);
    }

    public <T extends ServiceInfo> T getServiceByName(String name, Class<T> clazz) {

        Iterator i = getSession().createQuery(
                "from " + clazz.getName() + " where name = '" + name + "'").iterate();
        if (i.hasNext()) {
            return (T) i.next();
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
    }

}