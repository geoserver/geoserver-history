package org.geoserver.config.hibernate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;
import org.geoserver.catalog.Catalog;
import org.geoserver.catalog.hibernate.HibernateCatalog;
import org.geoserver.config.ConfigurationListener;
import org.geoserver.config.GeoServer;
import org.geoserver.config.GeoServerFactory;
import org.geoserver.config.GeoServerInfo;
import org.geoserver.config.ServiceInfo;
import org.geoserver.config.impl.GeoServerFactoryImpl;
import org.geoserver.config.impl.GeoServerInfoImpl;
import org.geoserver.wcs.WCSInfoImpl;
import org.geoserver.wfs.GMLInfo;
import org.geoserver.wfs.GMLInfoImpl;
import org.geoserver.wfs.WFSInfo;
import org.geoserver.wfs.WFSInfoImpl;
import org.geoserver.wfs.GMLInfo.SrsNameStyle;
import org.geoserver.wms.WMSInfoImpl;
import org.geoserver.wms.WatermarkInfo;
import org.geoserver.wms.WatermarkInfoImpl;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class HibernateGeoServer implements GeoServer {

    /**
     * factory to create config objects.
     */
    GeoServerFactory factory = new GeoServerFactoryImpl();

    HibernateCatalog catalog;

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
        this.catalog = (HibernateCatalog) catalog;
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
        } else if (!this.session.isOpen()) {
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
        GeoServerInfo geoserver;
        if (i.hasNext()) {
            geoserver = (GeoServerInfo) i.next();
        } else {
            // this is an empty configuration! create the minimal set of required object
            geoserver = serviceBootStrap();
            catalog.bootStrap();
        }

        return geoserver;
    }

    private GeoServerInfo serviceBootStrap() {
        GeoServerInfo geoserver;
        geoserver = getFactory().createGlobal();
        geoserver.setContactInfo(getFactory().createContact());
        //do not call setGlobal or we'll get an infinite loop
        getSession().save(geoserver);
        
        WFSInfoImpl wfs = new WFSInfoImpl();
        wfs.setId("wfs");
        wfs.setName("wfs");
        wfs.setEnabled(true);

        wfs.setServiceLevel(WFSInfo.ServiceLevel.COMPLETE);

        // gml2
        GMLInfo gml = new GMLInfoImpl();
        gml.setFeatureBounding(Boolean.TRUE);
        gml.setSrsNameStyle(SrsNameStyle.NORMAL);
        wfs.getGML().put(WFSInfo.Version.V_10, gml);

        // gml3
        gml = new GMLInfoImpl();
        gml.setFeatureBounding(true);
        gml.setSrsNameStyle(SrsNameStyle.URN);
        wfs.getGML().put(WFSInfo.Version.V_11, gml);

        add(wfs);

        WMSInfoImpl wms = new WMSInfoImpl();
        wms.setName("wms");
        wms.setEnabled(true);
        WatermarkInfo wm = new WatermarkInfoImpl();
        wm.setEnabled(false);
        wm.setPosition(WatermarkInfo.Position.BOT_RIGHT);
        wms.setWatermark(wm);

        add(wms);

        WCSInfoImpl wcs = new WCSInfoImpl();
        wcs.setId("wcs");
        wcs.setName("wcs");
        wcs.setEnabled(true);

        add(wcs);

        return geoserver;
    }

    public void setGlobal(GeoServerInfo configuration) {
        GeoServerInfo current = getGlobal();
        if (current == null || current.equals(configuration)) {
            current = configuration;
        } else {
            try {
                String id = current.getId();
                ((GeoServerInfoImpl) configuration).setId(id);
                PropertyUtils.copyProperties(current, configuration);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        getSession().saveOrUpdate(current);
        // getSession().flush();
        getSession().getTransaction().commit();
    }

    public void save(GeoServerInfo geoServer) {
        setGlobal(geoServer);
        // getSession().update(geoServer);
        // // getSession().flush();
        // getSession().getTransaction().commit();
    }

    public void add(ServiceInfo service) {
        service.setGeoServer(getGlobal());
        if (getService(service.getId()) == null)
            getSession().save(service);
        else
            getSession().update(service);
        // getSession().flush();
        getSession().getTransaction().commit();
    }

    public void remove(ServiceInfo service) {
        getSession().delete(service);
        // getSession().flush();
        getSession().getTransaction().commit();
    }

    public void save(ServiceInfo service) {
        Query query = getSession().createQuery(
                "from " + service.getClass().getName() + " where id = ?");
        String id = service.getId();
        query.setString(0, id);
        List list = query.list();
        if (list.size() > 0) {
            ServiceInfo dbResource = (ServiceInfo) list.get(0);
            try {
                PropertyUtils.copyProperties(dbResource, service);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            getSession().update(dbResource);
        } else {
            getSession().update(service);
        }
        // getSession().flush();
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
        Iterator i = getSession().createQuery(
                "from " + clazz.getName() + " where id = '" + id + "'").iterate();
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
        Iterator i = getSession().createQuery(
                "from " + clazz.getName() + " where name = '" + name + "'").iterate();
        if (i.hasNext()) {
            T service = (T) i.next();
            return service;
        }

        return null;
    }

    public void addListener(ConfigurationListener listener) {
        listeners.add(listener);
    }

    public void removeListener(ConfigurationListener listener) {
        listeners.remove(listener);
    }

    public void dispose() {
        catalog.dispose();
        listeners.clear();

        getSession().close();
    }

}