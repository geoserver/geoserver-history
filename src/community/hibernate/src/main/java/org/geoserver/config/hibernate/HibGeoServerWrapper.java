/*
 */

package org.geoserver.config.hibernate;

import java.util.Collection;
import org.geoserver.catalog.Catalog;
import org.geoserver.catalog.hibernate.HibCatalogImpl;
import org.geoserver.config.ConfigurationListener;
import org.geoserver.config.GeoServer;
import org.geoserver.config.GeoServerFactory;
import org.geoserver.config.GeoServerInfo;
import org.geoserver.config.LoggingInfo;
import org.geoserver.config.ServiceInfo;
import org.geoserver.hibernate.dao.ServiceDAO;

/**
 * 
 * @author ETj <etj at geo-solutions.it>
 */
public class HibGeoServerWrapper implements GeoServer {

    private HibGeoServerImpl delegate;

    public HibGeoServerWrapper(HibGeoServerImpl delegate) {
        this.delegate = delegate;
    }

    // public void setTransactionManager(JpaTransactionManager dummy) {
    // Logging.getLogger(HibGeoServerWrapper.class).warning("************* TransactionManager set ");
    // }
    //
    // public void setGeoserverLoader(HibGeoServerLoader dummy) {
    // Logging.getLogger(HibGeoServerWrapper.class).warning("************* Loader set ");
    // }
    //
    // public void setCatalogInterceptor(HibCatalogInterceptor dummy) {
    // Logging.getLogger(HibGeoServerWrapper.class).warning("************* Interceptor set ");
    // }

    public void setServiceDAO(ServiceDAO serviceDAO) {
        delegate.setServiceDAO(serviceDAO);
    }

    public void setGlobal(GeoServerInfo configuration) {
        delegate.setGlobal(configuration);
    }

    public void setFactory(GeoServerFactory factory) {
        delegate.setFactory(factory);
    }

    public void setCatalog(HibCatalogImpl catalog) {
        delegate.setCatalog(catalog);
    }

    public void setCatalog(Catalog catalog) {
        delegate.setCatalog(catalog);
    }

    public void save(ServiceInfo service) {
        delegate.save(service);
    }

    public void save(GeoServerInfo geoServer) {
        delegate.save(geoServer);
    }

    public void removeListener(ConfigurationListener listener) {
        delegate.removeListener(listener);
    }

    public void remove(ServiceInfo service) {
        delegate.remove(service);
    }

    public Collection<? extends ServiceInfo> getServices(Class<?> clazz) {
        return delegate.getServices(clazz);
    }

    public Collection<? extends ServiceInfo> getServices() {
        return delegate.getServices();
    }

    public ServiceDAO getServiceDAO() {
        return delegate.getServiceDAO();
    }

    public <T extends ServiceInfo> T getServiceByName(String name, Class<T> clazz) {
        return delegate.getServiceByName(name, clazz);
    }

    public <T extends ServiceInfo> T getService(String id, Class<T> clazz) {
        return delegate.getService(id, clazz);
    }

    public ServiceInfo getService(String id) {
        return delegate.getService(id);
    }

    public <T extends ServiceInfo> T getService(Class<T> clazz) {
        return delegate.getService(clazz);
    }

    public Collection<ConfigurationListener> getListeners() {
        return delegate.getListeners();
    }

    public GeoServerInfo getGlobal() {
        return delegate.getGlobal();
    }

    public GeoServerFactory getFactory() {
        return delegate.getFactory();
    }

    public Catalog getCatalog() {
        return delegate.getCatalog();
    }

    public void dispose() {
        delegate.dispose();
    }

    public void addListener(ConfigurationListener listener) {
        delegate.addListener(listener);
    }

    public void add(ServiceInfo service) {
        delegate.add(service);
    }

    public LoggingInfo getLogging() {
        return delegate.getLogging();
    }

    public void setLogging(LoggingInfo logging) {
        delegate.setLogging(logging);
    }

    public void save(LoggingInfo logging) {
        delegate.save(logging);
    }

}
