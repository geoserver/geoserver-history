/**
 * 
 */
package org.geoserver.hibernate;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

/**
 * @author Alessio
 * 
 */
public class GeoServerDAO /* extends HibernateDaoSupport */ {

    /**
     * 
     */
    private SessionFactory sessionFactory = null;
    
    /**
     * 
     */
    private Session session = null;
    
    /**
     * 
     * @param entity
     */
    public void save(Object entity) {
        getTransaction();
        getSession().save(entity);
    }

    /**
     * 
     * @param entity
     */
    public void merge(Object entity) {
        getTransaction();
        getSession().merge(entity);
    }

    /**
     * 
     * @param entity
     */
    public void delete(Object entity) {
        getTransaction();
        getSession().delete(entity);
    }

    /**
     * 
     * @param entity
     */
    public void update(Object entity) {
        getTransaction();
        getSession().update(entity);
    }

    /**
     * 
     * @param queryString
     * @return
     */
    public synchronized Query createQuery(String queryString) {
        getTransaction();
        return getSession().createQuery(queryString);
    }
    
    /**
     * 
     */
    public synchronized void commit() {
        getTransaction().commit();
        getSession().close();
    }

    /**
     * @return the sessionFactory
     */
    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    /**
     * @param sessionFactory the sessionFactory to set
     */
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    /**
     * @param session the session to set
     */
    public void setSession(Session session) {
        this.session = session;
    }

    /**
     * @return the session
     */
    public synchronized Session getSession() {
        if (session == null) {
            if (sessionFactory.getCurrentSession() != null && sessionFactory.getCurrentSession().isOpen())
                session = sessionFactory.getCurrentSession();
            else
                session = sessionFactory.openSession();
        } else if (!session.isOpen()) {
            session = sessionFactory.openSession();
        }
        
        return session;
    }
    
    /**
     * 
     * @return
     */
    public synchronized Transaction getTransaction() {
        if (session == null || !session.isOpen())
            getSession();
        
        if (session.getTransaction().isActive())
            return session.getTransaction();
        else
            return session.beginTransaction();
    }
}
