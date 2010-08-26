package org.geoserver.monitor.hib;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.geoserver.monitor.MonitorDAO;
import org.geoserver.monitor.MonitorQuery;
import org.geoserver.monitor.PipeliningTaskQueue;
import org.geoserver.monitor.RequestData;
import org.geoserver.monitor.MonitorQuery.Comparison;
import org.geoserver.monitor.MonitorQuery.SortOrder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class HibernateMonitorDAO implements MonitorDAO {

    PipeliningTaskQueue<Long> tasks = new PipeliningTaskQueue<Long>();
    protected EntityManager entityManager;
    
    @PersistenceContext
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
        tasks.start();
    }
    
    public RequestData add(RequestData data) {
        entityManager.persist(data);
        return data;
    }
    
    public List<RequestData> getRequests() {
        StringBuffer sb = new StringBuffer();
        sb.append("SELECT rd ");
        sb.append("FROM RequestData rd ");
        sb.append("ORDER BY rd.startTime DESC");
        
        Query q = entityManager.createQuery(sb.toString());
        return q.getResultList();
    }
    
    public List<RequestData> getRequests(MonitorQuery q) {
        List<Object> objs = new ArrayList();
        
        StringBuffer sb = new StringBuffer("SELECT rd");
        sb.append(" FROM RequestData rd");
        
        if (q.getFilterProperty() != null) {
            sb.append(" WHERE ").append("rd.").append(q.getFilterProperty());
            if (q.getFilterValue() == null) {
                sb.append(" IS");
                if (q.getFilterCompare() != Comparison.EQ) {
                    sb.append( " NOT");
                }
                sb.append(" NULL");
            }
            else {
                sb.append(" ").append(q.getFilterCompare());
                if (q.getFilterCompare() == Comparison.IN) {
                    sb.append(" (");
                    for (Object o : (List)q.getFilterValue()) {
                        sb.append("?, ");
                        objs.add(o);
                    }
                    sb.setLength(sb.length()-2);
                    sb.append(")");
                }
                else {
                    sb.append(" ?");
                    objs.add(q.getFilterValue());
                }
            }
        }
        
        if (q.getFromDate() != null || q.getToDate() != null) {
            if (q.getFilterProperty() != null) {
                sb.append(" AND");
            }
            else {
                sb.append(" WHERE");
            }
            
            sb.append(" ").append("rd.startTime");
            if (q.getFromDate() != null && q.getToDate() != null) {
                sb.append(" BETWEEN ? AND ?");
                 objs.add(q.getFromDate());
                 objs.add(q.getToDate());
            }
            else if (q.getFromDate() != null) {
                sb.append(" >= ?");
                objs.add(q.getFromDate());
            }
            else {
                sb.append(" <= ?"); 
                objs.add(q.getToDate());
            }
        }
        
        if (q.getSortBy() != null) {
            sb.append(" ORDER BY ").append("rd.").append(q.getSortBy()).append(" ").append(q.getSortOrder());
        }
        else if (q.getFromDate() != null || q.getToDate() != null) {
            //by default sort dates descending
            sb.append(" ORDER BY ").append("rd.startTime").append(" ").append(SortOrder.DESC);
        }
        
        Query query = entityManager.createQuery(sb.toString());
        if (q.getOffset() != null) {
            query.setFirstResult(q.getOffset().intValue());
        }
        
        if (q.getCount() != null) {
            query.setMaxResults(q.getCount().intValue());
        }
        
        for (int i = 0; i < objs.size(); i++) {
            query.setParameter(i+1, objs.get(i));
        }
        
        return query.getResultList();
    }
    
    public RequestData getRequest(long id) {
        StringBuffer sb = new StringBuffer();
        sb.append("SELECT rd ");
        sb.append("FROM RequestData rd ");
        sb.append("WHERE rd.id = ?");
        
        Query q = entityManager.createQuery(sb.toString());
        q.setParameter(1, id);
        
        return (RequestData) q.getSingleResult();
    }
    
    public List<RequestData> getOwsRequests() {
        StringBuffer sb = new StringBuffer();
        sb.append("SELECT rd ");
        sb.append("FROM RequestData rd ");
        sb.append("WHERE rd.owsService != null");
        
        Query q = entityManager.createQuery(sb.toString());
        return q.getResultList();
    }
    
    public List<RequestData> getOwsRequests(String service, String operation, String version) {
        return null;
    }
    
    public void update(RequestData data) {
        //entityManager.merge(data);
        save(data);
    }
    
    public void save(final RequestData data) {
        tasks.execute(data.getId(), new Runnable() {
            public void run() {
                entityManager.merge(data);
            }
        });
        
    }
    
    public List<RequestData> getPagedRequests(Date from, Date to, int start, int pageSize) {
        Query selectQuery = getRequestsByInterval(from, to, false);
        selectQuery.setMaxResults(pageSize);
        selectQuery.setFirstResult(0);
        return selectQuery.getResultList();
    }

    public long getRequestsCount(Date from, Date to) {
        Query selectQuery = getRequestsByInterval(from, to, true);
        return (Long) selectQuery.getSingleResult();
    }

    /**
     * Builds a query for requests in a certain time interval. Boy, how much I miss the Hibernate
     * Criteria API...
     * 
     * @param from The from date, or null
     * @param to The to date, or null
     * @param count True if the query should just return a count of the results, false otherwise
     * @return
     */
    Query getRequestsByInterval(Date from, Date to, boolean count) {
        StringBuffer sb = new StringBuffer();
        if (count)
            sb.append("select count(*) ");
        sb.append("from RequestStats rs ");
        if (from != null || to != null) {
            sb.append("where ");
        }
        if (from != null) {
            sb.append("rs.startTime >= ? ");
            if (to != null)
                sb.append("and ");
        }
        if (to != null) {
            sb.append("rs.startTime <= ? ");
        }
        if(!count)
            sb.append("order by rs.startTime desc");

        Query query = entityManager.createQuery(sb.toString());
        if (from != null) {
            query.setParameter(1, from);
        }
        if (to != null) {
            query.setParameter(2, to);
        }

        return query;
    }
    
    public void clear() {
    }
    
    public void dispose() {
        entityManager.close();
    }
}
