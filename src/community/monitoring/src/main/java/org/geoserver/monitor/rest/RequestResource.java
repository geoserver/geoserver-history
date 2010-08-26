package org.geoserver.monitor.rest;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.geoserver.monitor.Monitor;
import org.geoserver.monitor.MonitorQuery;
import org.geoserver.monitor.RequestData;
import org.geoserver.monitor.MonitorQuery.Comparison;
import org.geoserver.monitor.MonitorQuery.SortOrder;
import org.geoserver.ows.util.ClassProperties;
import org.geoserver.ows.util.OwsUtils;
import org.geoserver.rest.ReflectiveResource;
import org.geoserver.rest.RestletException;
import org.geoserver.rest.format.DataFormat;
import org.geoserver.rest.format.MediaTypes;
import org.geoserver.rest.format.ReflectiveHTMLFormat;
import org.geoserver.rest.format.StreamDataFormat;
import org.geotools.util.Converters;
import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.data.Status;
import org.restlet.resource.Resource;

import freemarker.template.Configuration;

public class RequestResource extends ReflectiveResource {
    
    static {
        MediaTypes.registerExtension("csv", new MediaType("application/csv"));
    }
    
    Monitor monitor;
    
    public RequestResource(Monitor monitor) {
        this.monitor = monitor;
    }
    
    @Override
    protected List<DataFormat> createSupportedFormats(Request request, Response response) {
        List<DataFormat> formats = super.createSupportedFormats(request, response);
        formats.add(createCSVFormat(request, response));
        return formats;
    }
    
       @Override
    protected DataFormat createHTMLFormat(Request request, Response response) {
        return new HTMLFormat(request, response, this);
    }

    DataFormat createCSVFormat(Request request, Response response) {
        String fields = getAttribute("fields");
        if (fields != null) {
            return new CSVFormat(fields.split(";"));
        }
        else {
            List<String> props = 
                OwsUtils.getClassProperties(RequestData.class).properties();
            
            props.remove("class");
            props.remove("body");
            props.remove("error");
            
            return new CSVFormat(props.toArray(new String[props.size()]));
        }
    }
    
    @Override
    public boolean allowGet() {
        return true;
    }
    
    @Override
    public boolean allowDelete() {
        return true;
    }
    
    @Override
    protected Object handleObjectGet() throws Exception {
        String req = getAttribute("request");
        
        if (req == null) {
            //return a collection
            Form form = null;
            if (getRequest().getResourceRef() != null) {
                form = getRequest().getResourceRef().getQueryAsForm();
            }
            else {
                form = new Form();
            }
            
            
            // date range
            String from = form.getFirstValue("from");
            String to = form.getFirstValue("to");

            MonitorQuery q = new MonitorQuery().between(
                from != null ? Converters.convert(from, Date.class) : null, 
                to != null ? Converters.convert(to, Date.class) : null);
            
            //sorting
            String sortBy;
            SortOrder sortOrder;
            
            String order = form.getFirstValue("order");
            if (order != null) {
                int semi = order.indexOf(';');
                if (semi != -1) {
                    String[] split = order.split(";");
                    sortBy = split[0];
                    sortOrder = SortOrder.valueOf(split[1]);
                }
                else {
                    sortBy = order;
                    sortOrder = SortOrder.ASC;
                }
                
                q.sort(sortBy, sortOrder);
            }
            
            //limit offset
            String offset = form.getFirstValue("offset");
            String count = form.getFirstValue("count");
            q.page(offset != null ? Long.parseLong(offset) : null,
                count != null ? Long.parseLong(count) : null);
            
            //live?
            String live = form.getFirstValue("live");
            if (live != null) {
                if ("yes".equalsIgnoreCase(live) || "true".equalsIgnoreCase(live)) {
                    q.filter("status", Arrays.asList(
                        org.geoserver.monitor.RequestData.Status.RUNNING,
                        org.geoserver.monitor.RequestData.Status.WAITING,
                        org.geoserver.monitor.RequestData.Status.CANCELLING), Comparison.IN);
                }
                else {
                    q.filter("status", Arrays.asList(
                        org.geoserver.monitor.RequestData.Status.FINISHED,
                        org.geoserver.monitor.RequestData.Status.FAILED), Comparison.IN);
                }
            }
            
            return monitor.getDAO().getRequests(q);
        }
        else {
            //return the individual
            RequestData data = monitor.getDAO().getRequest(Long.parseLong(req));
            if (data == null) {
                throw new RestletException("No such request" + req, Status.CLIENT_ERROR_NOT_FOUND);
            }
            return data;
        }
    }
    
    @Override
    protected void handleObjectDelete() throws Exception {
        String req = getAttribute("request");
        if (req == null) {
            monitor.getDAO().clear();
        }
    }

    static class HTMLFormat extends ReflectiveHTMLFormat {
        
        protected HTMLFormat(Request request, Response response, Resource resource) {
            super(RequestData.class, request, response, resource);
        }
        
        @Override
        protected Configuration createConfiguration(Object data, Class clazz) {
            Configuration cfg = super.createConfiguration(data, clazz);
            cfg.setClassForTemplateLoading(RequestResource.class, "");
            return cfg;
        }
        
        @Override
        protected String getTemplateName(Object data) {
            if (data instanceof RequestData) {
                return "request.html";
            }
            else {
                return "requests.html";
            }
        }
    }
    
    static class CSVFormat extends StreamDataFormat {

        String[] fields;
        protected CSVFormat(String[] fields) {
            super(new MediaType("application/csv"));
            
            this.fields = fields;
        }

        @Override
        protected void write(Object object, OutputStream out) throws IOException {
            BufferedWriter w = new BufferedWriter(new OutputStreamWriter(out));
            
            StringBuffer sb = new StringBuffer();
            for (String fld : fields) {
                sb.append(fld).append(",");
            }
            sb.setLength(sb.length()-1);
            w.write(sb.append("\n").toString());
            sb.setLength(0);
            
            List<RequestData> requests;
            if (object instanceof List) {
                requests = (List<RequestData>) object;
            }
            else {
                requests = Collections.singletonList((RequestData)object);
            }
            
            for (RequestData r : requests) {
                for (String fld : fields) {
                    Object val = OwsUtils.get(r, fld);
                    if (val != null) {
                        val = val.toString().replaceAll(",", " ");
                    }
                    sb.append(val).append(",");
                }
                sb.setLength(sb.length()-1);
                sb.append("\n");
                w.write(sb.toString());
                sb.setLength(0);
            }
            
            w.flush();
        }
        
        @Override
        protected Object read(InputStream in) throws IOException {
            return null;
        }
        
    }
}
