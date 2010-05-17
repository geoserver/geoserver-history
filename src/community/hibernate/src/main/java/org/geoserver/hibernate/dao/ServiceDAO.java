package org.geoserver.hibernate.dao;

import java.util.Collection;

import org.geoserver.config.GeoServerInfo;
import org.geoserver.config.LoggingInfo;
import org.geoserver.config.ServiceInfo;

public interface ServiceDAO {

    public abstract void save(ServiceInfo entity);

    public abstract void delete(ServiceInfo entity);

    public abstract void update(ServiceInfo entity);

    public abstract GeoServerInfo save(GeoServerInfo entity);

    public abstract void delete(GeoServerInfo entity);

    public abstract GeoServerInfo update(GeoServerInfo entity);

    /**
     * 
     * @return
     */
    public abstract GeoServerInfo getGeoServer();

    /**
     * 
     * @param <T>
     * @param id
     * @param clazz
     * @return
     */
    public abstract <T extends ServiceInfo> T getService(String id, Class<T> clazz);

    /**
     * 
     * @param <T>
     * @param name
     * @param clazz
     * @return
     */
    public abstract <T extends ServiceInfo> T getServiceByName(String name, Class<T> clazz);

    /**
     * 
     * @param clazz
     * @return
     */
    public abstract Collection<? extends ServiceInfo> getServices(Class<?> clazz);

    public void setLogging(LoggingInfo entity);

    public LoggingInfo getLogging();
}