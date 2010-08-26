package org.geoserver.monitor.ows;

import java.util.ArrayList;
import java.util.List;

import org.geoserver.monitor.Monitor;
import org.geoserver.monitor.RequestData;
import org.geoserver.monitor.RequestData.Status;
import org.geoserver.monitor.ows.wcs10.DescribeCoverageHandler;
import org.geoserver.monitor.ows.wcs10.GetCoverageHandler;
import org.geoserver.monitor.ows.wfs.DescribeFeatureTypeHandler;
import org.geoserver.monitor.ows.wfs.GetFeatureHandler;
import org.geoserver.monitor.ows.wfs.LockFeatureHandler;
import org.geoserver.monitor.ows.wfs.TransactionHandler;
import org.geoserver.monitor.ows.wms.GetFeatureInfoHandler;
import org.geoserver.monitor.ows.wms.GetMapHandler;
import org.geoserver.ows.DispatcherCallback;
import org.geoserver.ows.Request;
import org.geoserver.ows.Response;
import org.geoserver.platform.Operation;
import org.geoserver.platform.Service;
import org.geoserver.platform.ServiceException;

public class MonitorCallback implements DispatcherCallback {

    static List<RequestObjectHandler> HANDLERS = new ArrayList();
    static {
        //wfs
        HANDLERS.add(new DescribeFeatureTypeHandler());
        HANDLERS.add(new GetFeatureHandler());
        HANDLERS.add(new LockFeatureHandler());
        HANDLERS.add(new TransactionHandler());
        
        //wms
        HANDLERS.add(new GetFeatureInfoHandler());
        HANDLERS.add(new GetMapHandler());
        
        //wcs
        HANDLERS.add(new DescribeCoverageHandler());
        HANDLERS.add(new GetCoverageHandler());
        
        HANDLERS.add(new org.geoserver.monitor.ows.wcs11.DescribeCoverageHandler());
        HANDLERS.add(new org.geoserver.monitor.ows.wcs11.GetCoverageHandler());
    }
    
    Monitor monitor;
    
    public MonitorCallback(Monitor monitor) {
        this.monitor = monitor;
    }
    
    public Request init(Request request) {
        return null;
    }

    public Response responseDispatched(Request request, Operation operation, Object result,
            Response response) {
        return null;
    }

    public Service serviceDispatched(Request request, Service service) throws ServiceException {
        return null;
    }
    
    public Operation operationDispatched(Request request, Operation operation) {
        RequestData data = monitor.current();
        
        data.setOwsService(operation.getService().getId());
        data.setOwsOperation(operation.getId());
        data.setOwsVersion(operation.getService().getVersion().toString());
        
        if (operation.getParameters().length > 0) {
            //TODO: a better check for the request object
            Object reqObj = operation.getParameters()[0];
            for (RequestObjectHandler h : HANDLERS) {
                if (h.canHandle(reqObj)) {
                    data.setLayers(h.getLayers(reqObj));
                    break;
                }
            }
        }
        
        monitor.update();
        
        return operation;
    }

    public Object operationExecuted(Request request, Operation operation, Object result) {
        return null;
    }
    
    public void finished(Request request) {
        if (request.getError() != null) {
            RequestData data = monitor.current();
            data.setStatus(Status.FAILED);
            data.setErrorMessage(request.getError().getLocalizedMessage());
            data.setError(request.getError());
            
            monitor.update();
        }
    }

}
