package org.geoserver.config.hibernate;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.geoserver.catalog.Catalog;
import org.geoserver.catalog.hibernate.HibCatalogImpl;
import org.geoserver.config.ConfigurationListener;
import org.geoserver.config.GeoServer;
import org.geoserver.config.GeoServerFactory;
import org.geoserver.config.GeoServerInfo;
import org.geoserver.config.JAIInfo;
import org.geoserver.config.LoggingInfo;
import org.geoserver.config.ServiceInfo;
import org.geoserver.config.hibernate.beans.GeoServerInfoImplHb;
import org.geoserver.config.impl.ServiceInfoImpl;
import org.geoserver.hibernate.HibMapper;
import org.geoserver.hibernate.Hibernable;
import org.geoserver.hibernate.dao.ServiceDAO;
import org.geoserver.jai.JAIInitializer;
import org.geotools.util.logging.Logging;

public class HibGeoServerImpl implements Serializable
// , GeoServer
{

    private static final Logger LOGGER = Logging.getLogger("org.geoserver.config.hibernate");

    /**
     * factory to create config objects.
     */
    private HibGeoServerFactoryImpl factory;

    transient private HibCatalogImpl catalog;

    transient private List<ConfigurationListener> listeners = new ArrayList<ConfigurationListener>();

    /**
     * 
     */
    transient private ServiceDAO serviceDAO;

    /**
     * TODO: please doublecheck the updates to this field<BR>
     * We need to cache JAI info because it has many transient values, and we prefer to cache twe
     * whole class instead of single values.
     */
    transient JAIInfo cachedJAI = null;

    private final GeoServer proxy;

    /**
     * 
     */
    private HibGeoServerImpl() {
        super();
        proxy = GSProxy.newInstance(this);
    }

    public GeoServer getProxy() {
        return proxy;
    }

    /**
     * 
     */
    public Catalog getCatalog() {
        return catalog;
    }

    public void setCatalog(Catalog catalog) {
        this.catalog = (HibCatalogImpl) catalog;
    }

    public GeoServerFactory getFactory() {
        return factory;
    }

    public void setFactory(GeoServerFactory factory) {
        this.factory = (HibGeoServerFactoryImpl) factory;
    }

    public GeoServerInfo getGlobal() {
        if (LOGGER.isLoggable(Level.FINEST))
            LOGGER.finest("Querying geoserver global configuration");
        GeoServerInfo geoserver = this.serviceDAO.getGeoServer();
        if (geoserver == null) {
            LOGGER.warning("Database is empty");
            geoserver = null;

        } else {

            if (cachedJAI == null) {
                LOGGER.info("getGlobal: JAI was null, inizitialized.");
                cachedJAI = geoserver.getJAI();
            } else {
                // The cached jai may have been updated.
                // Updated values should already be in db, but we need
                // the transient runtime values that are stored in cachedJAI.
                geoserver.setJAI(cachedJAI);
            }
        }

        return geoserver;
    }

    public void setGlobal(GeoServerInfo configuration) {
        // ensure contactinfo is not null
        if (configuration.getContact() == null) {
            LOGGER.warning("GeoServerInfo contact is not set. Creating empty one...");
            configuration.setContact(getFactory().createContact());
        }

        // retrieve existing global
        GeoServerInfo currentGlobal = getGlobal();
        GeoServerInfoImplHb merged = null;

        if (currentGlobal == null) {
            if (LOGGER.isLoggable(Level.INFO))
                LOGGER.info("Storing first instance of GeoServerInfo");

            GeoServerInfoImplHb inserted = (GeoServerInfoImplHb) this.serviceDAO
                    .save(configuration);
            inserted.copyTo((GeoServerInfoImplHb) configuration);
            ((GeoServerInfoImplHb) configuration).setId(inserted.getId());
        } else {

            GeoServerInfoImplHb oldconf = (GeoServerInfoImplHb) currentGlobal;
            GeoServerInfoImplHb newconf = (GeoServerInfoImplHb) configuration;

            newconf.setId(oldconf.getId());
            merged = (GeoServerInfoImplHb) this.serviceDAO.update(newconf);
            merged.copyTo(newconf);
        }

        JAIInitializer initializer = new JAIInitializer();
        try {
            initializer.initialize(proxy);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Error initializing JAI: " + ex.getMessage(), ex);
        }
        cachedJAI = configuration.getJAI();
        if (merged != null)
            merged.setJAI(cachedJAI);

        // fire the modification event
        firePostGlobalModified(merged);
    }

    public void save(GeoServerInfo geoServer) {
        setGlobal(geoServer);

        for (ConfigurationListener l : listeners) {
            try {
                l.handleGlobalChange(geoServer, new ArrayList<String>(), // FIXME: if really needed,
                                                                         // reload from DB and
                                                                         // compare
                        new ArrayList<Object>(), // FIXME: if really needed, reload from DB and
                                                 // compare
                        new ArrayList<Object>());// FIXME: if really needed, reload from DB and
                                                 // compare
            } catch (Exception e) {
                LOGGER.log(Level.WARNING, "Exception calling handleGlobalChange", e);
            }
        }

        // fire post modification event
        firePostGlobalModified(geoServer);
    }

    /**
     * @see GeoServer#add(ServiceInfo)
     */
    public void add(ServiceInfo service) {
        final String serviceId = service.getId();
        if (serviceId == null) {
            throw new NullPointerException("service id must not be null");
        }

        ServiceInfo existing = getService(serviceId);

        if (existing != null) {
            throw new IllegalArgumentException("service with id '" + serviceId + "' already exists");
        }

        // GeoServerInfo global = getGlobal();
        service.setGeoServer(proxy);

        if (LOGGER.isLoggable(Level.FINE))
            LOGGER.fine("CREATING SERVICE id:" + serviceId + " name:" + service.getName()
                    + " title:" + service.getTitle());

        if (service instanceof Hibernable) {
            this.serviceDAO.save(service);
        } else {
            LOGGER.info("Translating unhibernable instance of " + service.getClass().getName());
            HibServiceTranslator translator = new HibServiceTranslator(factory);
            ServiceInfoImpl hservice = translator.translate(service);
            this.serviceDAO.save(hservice);
        }

        // fire post modification event
        firePostServiceModified(service);
    }

    /**
     * 
     */
    public void remove(ServiceInfo service) {
        this.serviceDAO.delete(service);
    }

    /**
     * 
     */
    public void save(ServiceInfo service) {
        this.serviceDAO.update(service);

        for (ConfigurationListener l : listeners) {
            try {
                l.handleServiceChange(service, new ArrayList<String>(), // FIXME: if really needed,
                                                                        // reload from DB and
                                                                        // compare
                        new ArrayList<Object>(), // FIXME: if really needed, reload from DB and
                                                 // compare
                        new ArrayList<Object>());// FIXME: if really needed, reload from DB and
                                                 // compare
            } catch (Exception e) {
                LOGGER.log(Level.WARNING, "Exception calling handleServiceChange", e);
            }
        }

        // fire post modification event
        firePostServiceModified(service);
    }

    /**
     * 
     */
    public Collection<? extends ServiceInfo> getServices() {
        return getServices(ServiceInfo.class);
    }

    /**
     * 
     * @param clazz
     * @return
     */
    protected Collection<? extends ServiceInfo> getServices(Class<?> clazz) {
        Collection<ServiceInfoImpl> sis = (Collection<ServiceInfoImpl>) this.serviceDAO
                .getServices(clazz);
        for (ServiceInfoImpl serviceInfoImpl : sis) {
            serviceInfoImpl.setGeoServer(proxy);
        }
        return sis;
    }

    /**
     */
    public <T extends ServiceInfo> T getService(Class<T> clazz) {
        if (LOGGER.isLoggable(Level.FINE))
            LOGGER.fine("getService(" + clazz.getName() + ")");
        Class clazz2 = HibMapper.mapHibernableClass(clazz);

        // TODO: create dao.findService
        for (ServiceInfo si : getServices(clazz2)) {
            if (clazz2.isAssignableFrom(si.getClass())) {
                si.setGeoServer(proxy);
                return (T) si;
            }
        }

        return null;
    }

    /**
     * 
     */
    protected ServiceInfo getService(String id) {
        return getService(id, ServiceInfo.class);
    }

    /**
     *
     */
    public <T extends ServiceInfo> T getService(String id, Class<T> clazz) {
        T ret = this.serviceDAO.getService(id, clazz);
        if (ret != null)
            ret.setGeoServer(proxy);
        return ret;
    }

    /**
     * 
     */
    public <T extends ServiceInfo> T getServiceByName(String name, Class<T> clazz) {
        T ret = this.serviceDAO.getServiceByName(name, clazz);
        if (ret != null)
            ret.setGeoServer(proxy);
        return ret;
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
    }

    public ServiceDAO getServiceDAO() {
        return serviceDAO;
    }

    public void setServiceDAO(ServiceDAO serviceDAO) {
        this.serviceDAO = serviceDAO;
    }

    public void setCatalog(HibCatalogImpl catalog) {
        this.catalog = catalog;
    }

    public Collection<ConfigurationListener> getListeners() {
        return listeners;
    }

    public LoggingInfo getLogging() {
        return serviceDAO.getLogging();
    }

    public void setLogging(LoggingInfo logging) {
        serviceDAO.setLogging(logging);

        firePostLoggingModified(logging);
    }

    public void save(LoggingInfo logging) {
        serviceDAO.setLogging(logging);

        for (ConfigurationListener l : listeners) {
            try {
                l.handleLoggingChange(logging, new ArrayList<String>(), // FIXME: if really needed,
                                                                        // reload from DB and
                                                                        // compare
                        new ArrayList<Object>(), // FIXME: if really needed, reload from DB and
                                                 // compare
                        new ArrayList<Object>());// FIXME: if really needed, reload from DB and
                                                 // compare
            } catch (Exception e) {
                LOGGER.log(Level.WARNING, "Exception calling handleLoggingChange", e);
            }
        }

        // fire post modification event
        firePostLoggingModified(logging);
    }

    private void firePostGlobalModified(GeoServerInfo global) {
        for (ConfigurationListener l : listeners) {
            try {
                l.handlePostGlobalChange(global);
            } catch (Exception e) {
                LOGGER.log(Level.WARNING, "Exception in fireGlobalPostModified", e);
            }
        }
    }

    private void firePostLoggingModified(LoggingInfo logging) {
        for (ConfigurationListener l : listeners) {
            try {
                l.handlePostLoggingChange(logging);
            } catch (Exception e) {
                LOGGER.log(Level.WARNING, "Exception in fireLoggingPostModified", e);
            }
        }
    }

    void firePostServiceModified(ServiceInfo service) {
        for (ConfigurationListener l : listeners) {
            try {
                l.handlePostServiceChange(service);
            } catch (Exception e) {
                LOGGER.log(Level.WARNING, "Exception in firePostServiceModified", e);
            }
        }
    }

}

class GSProxy implements InvocationHandler, Serializable {

    private HibGeoServerImpl geoserverimpl;

    public static GeoServer newInstance(HibGeoServerImpl gsi) {
        return (GeoServer) Proxy.newProxyInstance(gsi.getClass().getClassLoader(),
                new Class[] { GeoServer.class }, new GSProxy(gsi));
    }

    private GSProxy(HibGeoServerImpl obj) {
        this.geoserverimpl = obj;
    }

    public Object invoke(Object proxy, Method m, Object[] args) throws Throwable {
        Object result;
        try {
            // System.out.println("before method " + m.getName());
            Method pmethod = geoserverimpl.getClass().getMethod(m.getName(), m.getParameterTypes());
            result = pmethod.invoke(geoserverimpl, args);
        } catch (InvocationTargetException e) {
            throw e.getTargetException();
        } catch (Exception e) {
            throw new RuntimeException("unexpected invocation exception: " + e.getMessage());
        }
        return result;
    }
}
