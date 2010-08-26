package org.geoserver.monitor;

import java.util.List;

/**
 * The GeoServer request monitor and primary entry point into the monitor api.
 * <p>
 * For each request submitted to a GeoServer instance the monitor maintains state about
 * the request and makes operations available that control the life cycle of the request.
 * The life cycle of a monitored request advances through the following states:
 * <ul>
 *  <li>the request is STARTED
 *  <li>the request is UPDATED any number of times.
 * </ul>
 * </p>
 * 
 * @author Andrea Aime, OpenGeo
 * @author Justin Deoliveira, OpenGeo
 *
 */
public class Monitor {

    /**  
     * thread local request object.
     */
    static ThreadLocal<RequestData> REQUEST = new ThreadLocal<RequestData>();
    
    /**
     * default page size when executing queries
     */
    static long PAGE_SIZE = 1000;
    
    MonitorDAO dao;
    
    public Monitor(MonitorConfig config) {
        this(config.createDAO());
    }
    
    public Monitor(MonitorDAO dao) {
        this.dao = dao;
    }
    
    public RequestData start() {
        RequestData req = new RequestData();
        req = dao.add(req);
        REQUEST.set(req);
        return req;
    }

    public RequestData current() {
        return REQUEST.get();
    }

    public void update() {
        dao.update(REQUEST.get());
    }

    public void complete() {
        dao.save(REQUEST.get());
        REQUEST.remove();
    }

    public void dispose() {
        dao.dispose();
        dao = null;
    }
    
    public MonitorDAO getDAO() {
        return dao;
    }
    
    public void query(MonitorQuery q, RequestDataVisitor visitor) {
        Long pageSize = q.getCount();
        if (pageSize == null || pageSize < 1) {
            pageSize = PAGE_SIZE;
        }
        
        Long offset = q.getOffset() != null ? q.getOffset() : 0;
        while(true) {
            q.page(offset, pageSize);
            List<RequestData> requests = dao.getRequests(q);
            if (requests.isEmpty()) {
                break;
            }
            
            for (RequestData r : requests) {
                visitor.visit(r);
            }
            
            if (requests.size() < pageSize) {
                break;
            }
            offset += pageSize;
        }
    }

}
