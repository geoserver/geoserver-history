package org.geoserver.monitor;

import java.util.Arrays;

import org.geoserver.config.GeoServer;
import org.geoserver.config.GeoServerInitializer;
import org.geoserver.monitor.Query.Comparison;
import org.geoserver.monitor.RequestData.Status;

public class MonitorInitializer implements GeoServerInitializer {

    Monitor monitor;
    
    public MonitorInitializer(Monitor monitor) {
        this.monitor = monitor;
    }
    
    public void initialize(GeoServer geoServer) throws Exception {
        //clear out any requests that were left in an inconsistent state
        Query query = new Query().filter("status",
            Arrays.asList(Status.RUNNING, Status.WAITING, Status.CANCELLING), Comparison.IN);
        for (RequestData data : monitor.getDAO().getRequests(query)) {
            if (InternalHostname.get().equals(data.getInternalHost())) {
                //mark start as INTERRUPTED
                data.setStatus(Status.INTERRUPTED);
                monitor.getDAO().save(data);
            }
        }
    }

}
