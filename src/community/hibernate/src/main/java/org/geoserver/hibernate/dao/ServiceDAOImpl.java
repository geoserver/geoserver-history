/**
 * 
 */
package org.geoserver.hibernate.dao;

import java.util.Collection;

import java.util.List;
import javax.persistence.FlushModeType;
import javax.persistence.Query;
import org.geoserver.config.GeoServerInfo;
import org.geoserver.config.LoggingInfo;
import org.geoserver.config.ServiceInfo;
import org.geoserver.config.hibernate.beans.GeoServerInfoImplHb;
import org.geoserver.config.hibernate.beans.LoggingInfoImplHb;
import org.geoserver.hibernate.HibMapper;
import org.geotools.renderer.i18n.Logging;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 */
@Repository
@Transactional
public class ServiceDAOImpl extends AbstractDAOImpl implements ServiceDAO {

    /**
     * Constructor for HibernateDAO.
     */
    public ServiceDAOImpl() {
        super();
    }

    /**
     * 
     */
    public GeoServerInfo getGeoServer() {
        Query query = buildQuery("from ", GeoServerInfoImplHb.class);
        return (GeoServerInfo) first(query, true);
    }

    /**
     * 
     */
    public Collection<? extends ServiceInfo> getServices(Class<?> clazz) {
        Class clazz2 = HibMapper.mapHibernableClass(clazz);
        Query query = buildQuery("from ", clazz2);
        return (Collection<? extends ServiceInfo>) query.getResultList();

    }

    /**
     * 
     */
    public <T extends ServiceInfo> T getService(String id, Class<T> clazz) {
        Class clazz2 = HibMapper.mapHibernableClass(clazz);
        Query query = buildQuery("from ", clazz2, " where id = ", param(id));
        return (T) first(query);

    }

    /**
     * 
     */
    public <T extends ServiceInfo> T getServiceByName(String name, Class<T> clazz) {
        Query query = buildQuery("from ", clazz, " where name = ", param(name));
        List result = query.getResultList();
        if (result.isEmpty()) {
            return null;
        } else {
            T service = (T) result.get(0);
            return service; // todo: maybe we should deproxy it
        }

    }

    public void save(ServiceInfo entity) {
        super.save(entity);
        entityManager.flush();// TODO useless??
    }

    public void delete(ServiceInfo entity) {
        super.delete(entity);
    }

    public void update(ServiceInfo entity) {
        super.merge(entity);
    }

    public GeoServerInfo save(GeoServerInfo entity) {

        super.save(entity);
        entityManager.flush();// TODO useless??
        return entityManager.find(GeoServerInfoImplHb.class, entity.getId());// TODO useless??
    }

    public GeoServerInfo update(GeoServerInfo entity) {
        GeoServerInfo ret = entityManager.merge(entity);
        entityManager.flush();
        entityManager.refresh(ret);
        return ret;
    }

    public void delete(GeoServerInfo entity) {
        super.delete(entity);
    }

    public void setLogging(LoggingInfo entity) {
        LoggingInfoImplHb old = (LoggingInfoImplHb) getLogging();
        if (old == null) {
            entityManager.persist(entity);
        } else {
            old.copyFrom(entity);
            entityManager.merge(old);
        }
    }

    public LoggingInfo getLogging() {
        Query query = buildQuery("from ", LoggingInfoImplHb.class);
        return (LoggingInfo) first(query);
    }
}
