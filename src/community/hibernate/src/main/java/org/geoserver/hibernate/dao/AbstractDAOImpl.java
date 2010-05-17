/**
 * 
 */
package org.geoserver.hibernate.dao;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.geoserver.catalog.CatalogInfo;
import org.geoserver.catalog.Info;
import org.geoserver.hibernate.HibMapper;
import org.geotools.util.logging.Logging;
import org.hibernate.proxy.HibernateProxy;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author ETj <etj at geo-solutions.it>
 */
@Repository
@Transactional
public abstract class AbstractDAOImpl {

    protected final Logger LOGGER = Logging.getLogger(AbstractDAOImpl.class);

    @PersistenceContext
    protected EntityManager entityManager;

    /**
     * Constructor for HibernateDAO.
     */
    public AbstractDAOImpl() {       

    }

    protected Query buildQuery(Object... elems) {
        final StringBuilder builder = new StringBuilder();
        int cnt = 0;
        for (Object elem : elems) {
            if (elem instanceof String)
                builder.append(elem);
            else if (elem instanceof Class) {
                Class mappedclass = HibMapper.mapHibernableClass((Class) elem);
                builder.append(mappedclass.getSimpleName());
            } else if (elem instanceof QueryParam) {
                builder.append(":param").append(cnt++);
            }
        }

        Query query = entityManager.createQuery(builder.toString());
        query.setHint("org.hibernate.cacheable", true);
        cnt = 0;
        for (Object elem : elems) {
            if (elem instanceof QueryParam) {
                query.setParameter("param" + (cnt++), ((QueryParam) elem).param);
            }
        }

        return query;
    }

    protected static QueryParam param(Object param) {
        return new QueryParam(param);
    }

    /** Simple wrapper to tell which objects are bindable query parameters */
    protected static class QueryParam {
        Object param;

        public QueryParam(Object param) {
            this.param = param;
        }
    }

    /*
     */
    protected void save(Info entity) {
        if (LOGGER.isLoggable(Level.FINE))
            LOGGER.fine("Saving " + entity.getClass().getSimpleName());
//        if (!(entity instanceof Hibernable))
//            LOGGER.severe("Trying to handle a " + entity.getClass().getName());

        entityManager.persist(entity);
    }

    /*
     */
    protected <T> T merge(T entity) {
//        if (!(entity instanceof Hibernable))
//            LOGGER.severe("Trying to handle a " + entity.getClass().getName());

        return entityManager.merge(entity);
    }

    /*
     */
    protected void delete(CatalogInfo entity) {
//        if (!(entity instanceof Hibernable))
//            LOGGER.severe("Trying to handle a " + entity.getClass().getName());

        CatalogInfo attached = entityManager.merge(entity);
        entityManager.remove(attached);
        entityManager.flush();// TODO useless?
    }

    protected void delete(Info entity) {
//        if (!(entity instanceof Hibernable))
//            LOGGER.severe("Trying to handle a " + entity.getClass().getName());
        
        entityManager.remove(entity);
        entityManager.flush();// TODO useless?
    }

    /**
     * Helper method to return the first object of a query.
     * 
     * @param hql
     *            the hql query, may contain {@code ?} argument placeholders
     * @param arguments
     *            the hql query arguments, or {@code null}. Recognized argument types are
     *            {@link String}, {@link Integer}, {@link Boolean}, {@link Float} and {@link Double}
     *            . An object of any other type will result in an unchecked exception
     * @return the first object matching the query or {@code null} if the query returns no results
     */

    protected Object first(final Query query) {
        return first(query, true);
    }

    protected Object first(final Query query, boolean doWarn) {
        query.setMaxResults(doWarn ? 2 : 1);
        query.setHint("org.hibernate.cacheable", true);
        List<?> result = query.getResultList();
        if (result.isEmpty()) {
            return null;
        } else {
            if (doWarn && result.size() > 1) {
                LOGGER.log(Level.WARNING, "Found too many items in result", new RuntimeException(
                        "Trace: Found too many items in query"));
            }

            Object ret = result.get(0);
            if (ret instanceof HibernateProxy) {
                HibernateProxy proxy = (HibernateProxy) ret;
                ret = proxy.getHibernateLazyInitializer().getImplementation();
            }



            if (LOGGER.isLoggable(Level.FINE)){
                StringBuilder callerChain = new StringBuilder();
                for (StackTraceElement stackTraceElement : new Throwable().getStackTrace()) {
                    if ("first".equals(stackTraceElement.getMethodName()))
                        continue;
                    String cname = stackTraceElement.getClassName();
                    if (cname.startsWith("org.spring"))
                        continue;
                    cname = cname.substring(cname.lastIndexOf(".") + 1);
                    callerChain.append(cname).append('.').append(stackTraceElement.getMethodName())
                            .append(':').append(stackTraceElement.getLineNumber()).append(' ');
                    // if(++num==10) break;
                }            	
                LOGGER.fine("FIRST -->" + ret.getClass().getSimpleName() + " --- " + ret + " { "
                        + callerChain + "}");
            }
            return ret;
        }
    }

    protected List<?> list(Class clazz) {
        Query query = buildQuery("from ", clazz);
        query.setHint("org.hibernate.cacheable", true);
        List<?> result = query.getResultList();
        return result;
   }
        

    // ==========================================================================
        
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

}
