package org.geoserver.monitor.hib;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.geoserver.monitor.MonitorDAO;
import org.geoserver.monitor.MonitorQuery;
import org.geoserver.monitor.PipeliningTaskQueue;
import org.geoserver.monitor.RequestData;
import org.geoserver.monitor.MonitorConfig.Mode;
import org.geoserver.monitor.MonitorConfig.Sync;
import org.geoserver.monitor.MonitorQuery.Comparison;
import org.geoserver.monitor.MonitorQuery.SortOrder;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;

public class HibernateMonitorDAO2 implements MonitorDAO {

    HibernateTemplate hib;
    PipeliningTaskQueue<Thread> tasks;
    Mode mode = Mode.HISTORY;
    Sync sync = Sync.ASYNC;
    
    public HibernateMonitorDAO2() {
        setMode(Mode.HISTORY);
        setSync(Sync.ASYNC);
    }
    
    public void setSync(Sync sync) {
        this.sync = sync;
        if (sync != Sync.SYNC) {
            if (tasks == null) {
                tasks = new PipeliningTaskQueue<Thread>();
                tasks.start();
            }
        }
        else {
            if (tasks != null) {
                dispose();
            }
        }
    }
    public void setMode(Mode mode) {
        this.mode = mode;
    }
    
    public void setSessionFactory(SessionFactory sessionFactory) {
        hib = new HibernateTemplate(sessionFactory);
    }
    
    public RequestData add(final RequestData data) {
        if (mode != Mode.HISTORY) {
            if (sync == Sync.ASYNC_UPDATE) {
                //async_update means don't run the initial insert asynchronously
                new Insert(data).run();
            }
            else {
                run(new Insert(data));
            }
        }
        else {
          //don't persist yet, we persist at the very end of request
        }
        
        return data;
    }
    
    public void update(RequestData data) {
        if (mode != Mode.HISTORY) {
            save(data);
        }
        else {
          //don't persist yet, we persist at the very end of request
        }
    }
    
    public void save(final RequestData data) {
        if(mode == Mode.HISTORY) {
            run(new Insert(data));
        }
        else {
            run(new Update(data));
        }
    }
    
    public void clear() {
    }

    public void dispose() {
        if (tasks != null) {
            tasks.shutdown();
            tasks = null;
        }
    }

    public List<RequestData> getOwsRequests() {
        throw new UnsupportedOperationException();
    }

    public List<RequestData> getOwsRequests(String service, String operation, String version) {
        throw new UnsupportedOperationException();
    }

    public RequestData getRequest(long id) {
        return (RequestData) hib.get(RequestData.class, id);
    }

    @SuppressWarnings("unchecked")
    public List<RequestData> getRequests() {
        return hib.executeFind(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                StringBuffer sb = new StringBuffer();
                sb.append("SELECT rd ");
                sb.append("FROM RequestData rd ");
                sb.append("ORDER BY rd.startTime DESC");
                
                Query q = session.createQuery(sb.toString());
                return q.list();
            }
        });
    }

    @SuppressWarnings("unchecked")
    public List<RequestData> getRequests(final MonitorQuery q) {
        return hib.executeFind(new HibernateCallback() {
            
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
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
                
                Query query = session.createQuery(sb.toString());
                if (q.getOffset() != null) {
                    query.setFirstResult(q.getOffset().intValue());
                }
                
                if (q.getCount() != null) {
                    query.setMaxResults(q.getCount().intValue());
                }
                
                for (int i = 0; i < objs.size(); i++) {
                    query.setParameter(i, objs.get(i));
                }
                
                return query.list();
            }
        });
        
        
    }
    
    protected void run(Task task) {
        if (tasks != null) {
            tasks.execute(Thread.currentThread(), new Async(task), task.desc);
        }
        else {
            task.run();
        }
    }

    class Async implements Runnable {

        Task task;
        
        Async(Task task) {
            this.task = task;
        }
        
        public void run() {
            synchronized(task.data) {
                task.run();
            }
        }
        
    }
    
    static abstract class Task implements Runnable {
        
        RequestData data;
        String desc;
        
        Task(RequestData data) {
            this.data = data;
        }
    }
    
    class Insert extends Task {
        
        Insert(RequestData data) {
            super(data);
            this.desc = "Insert " + data.internalid;
        }
        
        public void run() {
            hib.executeWithNewSession(new HibernateCallback() {
                public Object doInHibernate(Session session) throws HibernateException,
                        SQLException {
                    Transaction tx = session.beginTransaction();
                    session.save(data);
                    tx.commit();
                    return data;
                }
            });
        }
    }
    
    class Update extends Task {

        Update(RequestData data) {
            super(data);
            this.desc = "Update " + data.internalid;
        }
        
        public void run() {
            hib.executeWithNewSession(new HibernateCallback() {
                public Object doInHibernate(Session session) throws HibernateException,
                        SQLException {
                    try {
                    Transaction tx = session.beginTransaction();
                    session.update(data);
                    tx.commit();
                    }
                    catch(HibernateException e) {
                        if (tasks != null) tasks.print();
                        throw e;
                    }
                    return null;
                }
            });
        }
        
    }
}
