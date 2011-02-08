package org.geoserver.monitor;

import java.util.Iterator;
import java.util.List;

public interface MonitorDAO {

    RequestData init(RequestData data);
    
    void add(RequestData data);
    
    void update(RequestData data);
    
    void save(RequestData data);
    
    RequestData getRequest(long id);
    
    List<RequestData> getRequests();
    
    List<RequestData> getRequests(Query query);
    
    void getRequests(Query query, RequestDataVisitor visitor);
    
    long getCount(Query query);
    
    Iterator<RequestData> getIterator(Query query);
    
//    ResourceData getLayer(String name);
//    
//    List<ResourceData> getLayers();
//    
//    List<ResourceData> getLayers(MonitorQuery query);
//    
//    void getLayers(MonitorQuery query, MonitorVisitor<ResourceData> visitor);
    
    List<RequestData> getOwsRequests();
    
    List<RequestData> getOwsRequests(String service, String operation, String version);
    
    void clear();
    
    void dispose();
}
