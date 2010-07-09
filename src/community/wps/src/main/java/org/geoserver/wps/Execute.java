/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */

package org.geoserver.wps;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.datatype.XMLGregorianCalendar;

import net.opengis.ows11.BoundingBoxType;
import net.opengis.ows11.CodeType;
import net.opengis.wfs.FeatureCollectionType;
import net.opengis.wfs.GetFeatureType;
import net.opengis.wfs.WfsFactory;
import net.opengis.wps10.ComplexDataType;
import net.opengis.wps10.DataType;
import net.opengis.wps10.DocumentOutputDefinitionType;
import net.opengis.wps10.ExecuteResponseType;
import net.opengis.wps10.ExecuteType;
import net.opengis.wps10.HeaderType;
import net.opengis.wps10.InputReferenceType;
import net.opengis.wps10.InputType;
import net.opengis.wps10.LiteralDataType;
import net.opengis.wps10.MethodType;
import net.opengis.wps10.OutputDataType;
import net.opengis.wps10.OutputDefinitionType;
import net.opengis.wps10.OutputDefinitionsType;
import net.opengis.wps10.OutputReferenceType;
import net.opengis.wps10.ProcessBriefType;
import net.opengis.wps10.ProcessOutputsType1;
import net.opengis.wps10.Wps10Factory;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpConnectionManager;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.SimpleHttpConnectionManager;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.InputStreamRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.geoserver.config.GeoServerInfo;
import org.geoserver.ows.Ows11Util;
import org.geoserver.ows.URLMangler.URLType;
import org.geoserver.ows.util.KvpMap;
import org.geoserver.ows.util.KvpUtils;
import org.geoserver.ows.util.ResponseUtils;
import org.geoserver.wcs.WebCoverageService111;
import org.geoserver.wfs.WebFeatureService;
import org.geoserver.wfs.kvp.GetFeatureKvpRequestReader;
import org.geoserver.wps.ppio.BinaryPPIO;
import org.geoserver.wps.ppio.BoundingBoxPPIO;
import org.geoserver.wps.ppio.CDataPPIO;
import org.geoserver.wps.ppio.ComplexPPIO;
import org.geoserver.wps.ppio.LiteralPPIO;
import org.geoserver.wps.ppio.ProcessParameterIO;
import org.geoserver.wps.ppio.ReferencePPIO;
import org.geoserver.wps.ppio.XMLPPIO;
import org.geotools.data.Parameter;
import org.geotools.process.Process;
import org.geotools.process.ProcessFactory;
import org.geotools.process.Processors;
import org.geotools.util.Converters;
import org.geotools.xml.EMFUtils;
import org.opengis.feature.type.Name;
import org.opengis.util.InternationalString;
import org.opengis.util.ProgressListener;
import org.springframework.context.ApplicationContext;

/**
 * Main class used to handle Execute requests
 * 
 * @author Lucas Reed, Refractions Research Inc
 */
public class Execute {
    private static final int CONNECTION_TIMEOUT = 30 * 1000;

    WPSInfo wps;

    GeoServerInfo gs;

    ApplicationContext context;

    public Execute(WPSInfo wps, GeoServerInfo gs, ApplicationContext context) {
        this.wps = wps;
        this.gs = gs;
        this.context = context;
    }

    /**
     * Main method for performing decoding, execution, and response
     * 
     * @param object
     * @param output
     * @throws IllegalArgumentException
     */
    public ExecuteResponseType run(ExecuteType request) {
        // note the current time
        Date started = Calendar.getInstance().getTime();

        // load the process factory
        CodeType ct = request.getIdentifier();
        Name processName = Ows11Util.name(ct);
        ProcessFactory pf = Processors.createProcessFactory(processName);
        if (pf == null) {
            throw new WPSException("No such process: " + processName);
        }

        // parse the inputs into in memory representations the process can handle
        Map<String, Object> inputs = parseProcessInputs(request, processName, pf);

        // execute the process
        Map<String, Object> result = null;
        ProcessListener listener = new ProcessListener();
        try {
            Process p = pf.create(processName);
            result = p.execute(inputs, listener);
        } catch (WPSException e) {
            throw e;
        } catch (Throwable t) {
            throw new WPSException("InternalError: " + t.getMessage(), t);
        }
        
        // check if failure occurred from the listener
        Exception exception = listener.exception;
        if(exception != null) {
            throw new WPSException("InternalError:" + exception.getMessage(), exception);
        }

        // filter out the results we have not been asked about
        // and create a direct map between required outputs and
        // the gt process outputs
        Map<String, OutputDefinitionType> outputMap = new HashMap<String, OutputDefinitionType>();
        if (request.getResponseForm().getRawDataOutput() != null) {
            // only one output in raw form
            OutputDefinitionType od = request.getResponseForm().getRawDataOutput();
            String outputName = od.getIdentifier().getValue();
            outputMap.put(outputName, od);
            Map<String, Object> newResults = new HashMap<String, Object>();
            newResults.put(outputName, result.get(outputName));
            result = newResults;
        } else {
            for (Iterator it = request.getResponseForm().getResponseDocument().getOutput()
                    .iterator(); it.hasNext();) {
                OutputDefinitionType od = (OutputDefinitionType) it.next();
                String outputName = od.getIdentifier().getValue();
                outputMap.put(outputName, od);
            }
            for (String key : new HashSet<String>(result.keySet())) {
                if (!outputMap.containsKey(key)) {
                    result.remove(key);
                }
            }
        }

        // build the response
        Wps10Factory f = Wps10Factory.eINSTANCE;
        ExecuteResponseType response = f.createExecuteResponseType();
        response.setLang("en");
        response.setServiceInstance(ResponseUtils.appendQueryString(ResponseUtils.buildURL(request
                .getBaseUrl(), "ows", null, URLType.SERVICE), ""));

        // process
        final ProcessBriefType process = f.createProcessBriefType();
        response.setProcess(process);
        process.setIdentifier(ct);
        process.setProcessVersion(pf.getVersion(processName));
        process.setTitle(Ows11Util.languageString(pf.getTitle(processName).toString()));
        process.setAbstract(Ows11Util.languageString(pf.getDescription(processName).toString()));

        // status
        response.setStatus(f.createStatusType());
        response.getStatus().setCreationTime(
                Converters.convert(started, XMLGregorianCalendar.class));
        response.getStatus().setProcessSucceeded("Process succeeded.");

        // inputs
        response.setDataInputs(f.createDataInputsType1());
        for (Iterator i = request.getDataInputs().getInput().iterator(); i.hasNext();) {
            InputType input = (InputType) i.next();
            response.getDataInputs().getInput().add(EMFUtils.clone(input, f, true));
        }

        // output definitions
        OutputDefinitionsType outputs = f.createOutputDefinitionsType();
        response.setOutputDefinitions(outputs);

        Map<String, Parameter<?>> outs = pf.getResultInfo(processName, null);
        Map<String, ProcessParameterIO> ppios = new HashMap();

        for (String key : outputMap.keySet()) {
            Parameter p = pf.getResultInfo(processName, null).get(key);
            if (p == null) {
                throw new WPSException("No such output: " + key);
            }

            // find the ppio
            String mime = outputMap.get(key).getMimeType();
            ProcessParameterIO ppio = ProcessParameterIO.find(p, context, mime);
            if (ppio == null) {
                throw new WPSException("Unable to encode output: " + p.key);
            }
            ppios.put(p.key, ppio);

            DocumentOutputDefinitionType output = f.createDocumentOutputDefinitionType();
            outputs.getOutput().add(output);

            output.setIdentifier(Ows11Util.code(p.key));
            if (ppio instanceof ComplexPPIO) {
                output.setMimeType(((ComplexPPIO) ppio).getMimeType());
                if (ppio instanceof BinaryPPIO) {
                    output.setEncoding("base64");
                } else if (ppio instanceof XMLPPIO) {
                    output.setEncoding("utf-8");
                }
            }

            // TODO: better encoding handling + schema
        }

        // process outputs
        ProcessOutputsType1 processOutputs = f.createProcessOutputsType1();
        response.setProcessOutputs(processOutputs);

        for (String key : result.keySet()) {
            OutputDataType output = f.createOutputDataType();
            output.setIdentifier(Ows11Util.code(key));
            output.setTitle(Ows11Util
                    .languageString(pf.getResultInfo(processName, null).get(key).description));
            processOutputs.getOutput().add(output);

            final Object o = result.get(key);
            ProcessParameterIO ppio = ppios.get(key);

            if (ppio instanceof ReferencePPIO) {
                // encode as a reference
                OutputReferenceType ref = f.createOutputReferenceType();
                output.setReference(ref);

                ref.setMimeType(outputMap.get(key).getMimeType());
                ref.setHref(((ReferencePPIO) ppio).encode(o).toString());
            } else {
                // encode as data
                DataType data = f.createDataType();
                output.setData(data);

                if (ppio instanceof LiteralPPIO) {
                    LiteralDataType literal = f.createLiteralDataType();
                    data.setLiteralData(literal);

                    literal.setValue(((LiteralPPIO) ppio).encode(o));
                } else if (ppio instanceof BoundingBoxPPIO) {
                    BoundingBoxType bbox = ((BoundingBoxPPIO) ppio).encode(o);
                    data.setBoundingBoxData(bbox);
                } else if (ppio instanceof ComplexPPIO) {
                    ComplexDataType complex = f.createComplexDataType();
                    data.setComplexData(complex);

                    ComplexPPIO cppio = (ComplexPPIO) ppio;
                    complex.setMimeType(cppio.getMimeType());

                    if (cppio instanceof XMLPPIO) {
                        // encode directly
                        complex.getData().add(new XMLEncoderDelegate((XMLPPIO) cppio, o));
                    } else if (cppio instanceof CDataPPIO) {
                        complex.getData().add(new CDataEncoderDelegate((CDataPPIO) cppio, o));
                    } else if (cppio instanceof BinaryPPIO) {
                        complex.getData().add(new BinaryEncoderDelegate((BinaryPPIO) cppio, o));
                    } else {
                        throw new WPSException("Don't know how to encode an output whose PPIO is "
                                + cppio);
                    }
                }
            }
        }

        return response;
    }

    /**
     * Parses the process inputs into a {@link Map} using the various {@link ProcessParameterIO}
     * implementations
     * 
     * @param request
     * @param processName
     * @param pf
     */
    Map<String, Object> parseProcessInputs(ExecuteType request, Name processName, ProcessFactory pf) {
        Map<String, Object> inputs = new HashMap<String, Object>();
        for (Iterator i = request.getDataInputs().getInput().iterator(); i.hasNext();) {
            InputType input = (InputType) i.next();
            String inputId = input.getIdentifier().getValue();

            // locate the parameter for this request
            Parameter p = pf.getParameterInfo(processName).get(inputId);
            if (p == null) {
                throw new WPSException("No such parameter: " + inputId);
            }

            // find the ppio
            String mime = null;
            if (input.getData() != null && input.getData().getComplexData() != null) {
                mime = input.getData().getComplexData().getMimeType();
            } else if (input.getReference() != null) {
                mime = input.getReference().getMimeType();
            }
            ProcessParameterIO ppio = ProcessParameterIO.find(p, context, mime);
            if (ppio == null) {
                throw new WPSException("Unable to decode input: " + inputId);
            }

            // read the data
            Object decoded = null;
            try {
                if (input.getReference() != null) {
                    // this is a reference
                    InputReferenceType ref = input.getReference();

                    // grab the location and method
                    String href = ref.getHref();
                    MethodType method = ref.getMethod() != null ? ref.getMethod()
                            : MethodType.GET_LITERAL;

                    // TODO: handle in process GET requests by doing kvp parsing
                    if (href.startsWith("http://geoserver/wfs")) {
                        decoded = handleAsInternalWFS(ppio, ref);
                    } else if (href.startsWith("http://geoserver/wcs")
                            && method == MethodType.POST_LITERAL) {
                        WebCoverageService111 wcs = (WebCoverageService111) context
                                .getBean("wcs111ServiceTarget");
                        decoded = wcs
                                .getCoverage((net.opengis.wcs11.GetCoverageType) ref.getBody())[0];
                    } else {
                        decoded = executeRemoteRequest(ref, (ComplexPPIO) ppio, inputId);
                    }

                } else {
                    // actual data, figure out which type
                    DataType data = input.getData();

                    if (data.getLiteralData() != null) {
                        LiteralDataType literal = data.getLiteralData();
                        decoded = ((LiteralPPIO) ppio).decode(literal.getValue());
                    } else if (data.getComplexData() != null) {
                        ComplexDataType complex = data.getComplexData();
                        decoded = ((ComplexPPIO) ppio).decode(complex.getData().get(0));
                    } else if (data.getBoundingBoxData() != null) {
                        decoded = ((BoundingBoxPPIO) ppio).decode(data.getBoundingBoxData());
                    }

                }
            } catch (Exception e) {
                throw new WPSException("Unable to decode input: " + inputId, e);
            }

            // decode the input
            inputs.put(p.key, decoded);
        }
        
        return inputs;
    }

    /**
     * Process the request as an internal one, without going through GML encoding/decoding
     * 
     * @param ppio
     * @param ref
     * @param method
     * @return
     * @throws Exception
     */
    Object handleAsInternalWFS(ProcessParameterIO ppio, InputReferenceType ref) throws Exception {
        WebFeatureService wfs = (WebFeatureService) context.getBean("wfsServiceTarget");
        GetFeatureType gft = null;
        if (ref.getMethod() == MethodType.POST_LITERAL) {
            gft = (GetFeatureType) ref.getBody();
        } else {
            // simulate what the dispatcher is doing with the incoming kvp requests
            Map original = KvpUtils.parseQueryString(ref.getHref());
            KvpUtils.normalize(original);
            Map parsed = new KvpMap(original);
            List<Throwable> errors = KvpUtils.parse(parsed);
            if (errors.size() > 0) {
                throw new WPSException("Failed to parse KVP request", errors.get(0));
            }

            GetFeatureKvpRequestReader reader = (GetFeatureKvpRequestReader) context
                    .getBean("getFeatureKvpReader");
            gft = (GetFeatureType) reader.read(WfsFactory.eINSTANCE.createGetFeatureType(), parsed,
                    original);
        }

        FeatureCollectionType featureCollectionType = wfs.getFeature(gft);
        // this will also deal with axis order issues
        return ((ComplexPPIO) ppio).decode(featureCollectionType);
    }

    /**
     * Executes
     * 
     * @param ref
     * @return
     */
    Object executeRemoteRequest(InputReferenceType ref, ComplexPPIO ppio, String inputId)
            throws Exception {
        URL destination = new URL(ref.getHref());
        
        HttpMethod method = null;
        GetMethod refMethod = null;
        InputStream input = null;
        InputStream refInput = null;
        
        // execute the request
        try {
            if("http".equalsIgnoreCase(destination.getProtocol())) {
                // setup the client
                HttpClient client = new HttpClient();
                // setting timeouts (30 seconds, TODO: make this configurable)
                HttpConnectionManagerParams params = new HttpConnectionManagerParams();
                params.setSoTimeout(CONNECTION_TIMEOUT);
                params.setConnectionTimeout(CONNECTION_TIMEOUT);
                // TODO: make the http client a well behaved http client, no more than x connections
                // per server (x admin configurable maybe), persistent connections and so on
                HttpConnectionManager manager = new SimpleHttpConnectionManager();
                manager.setParams(params);
                client.setHttpConnectionManager(manager);

                // prepare either a GET or a POST request
                if (ref.getMethod() == null || ref.getMethod() == MethodType.GET_LITERAL) {
                    GetMethod get = new GetMethod(ref.getHref());
                    get.setFollowRedirects(true);
                    method = get;
                } else {
                    String encoding = ref.getEncoding();
                    if (encoding == null) {
                        encoding = "UTF-8";
                    }
                    
                    PostMethod post = new PostMethod(ref.getHref());
                    Object body = ref.getBody();
                    if (body == null) {
                        if(ref.getBodyReference() != null) {
                            URL refDestination = new URL(ref.getBodyReference().getHref());
                            if("http".equalsIgnoreCase(refDestination.getProtocol())) {
                                // open with commons http client
                                refMethod = new GetMethod(ref.getBodyReference().getHref());
                                refMethod.setFollowRedirects(true);
                                client.executeMethod(refMethod);
                                refInput = refMethod.getResponseBodyAsStream();
                            } else {
                                // open with the built-in url management
                                URLConnection conn = refDestination.openConnection();
                                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                                conn.setReadTimeout(CONNECTION_TIMEOUT);
                                refInput = conn.getInputStream();
                            }
                            post.setRequestEntity(new InputStreamRequestEntity(refInput, ppio.getMimeType()));
                        } else {
                            throw new WPSException("A POST request should contain a non empty body");
                        }
                    } else if (body instanceof String) {
                        post.setRequestEntity(new StringRequestEntity((String) body,
                                ppio.getMimeType(), encoding));
                    } else {
                        throw new WPSException(
                                "The request body should be contained in a CDATA section, "
                                        + "otherwise it will get parsed as XML instead of being preserved as is");
    
                    }
                    method = post;
                }
                // add eventual extra headers
                if(ref.getHeader() != null) {
                    for (Iterator it = ref.getHeader().iterator(); it.hasNext();) {
                        HeaderType header = (HeaderType) it.next();
                        method.setRequestHeader(header.getKey(), header.getValue());
                    }
                }
                int code = client.executeMethod(method);
    
                if (code == 200) {
                    input = method.getResponseBodyAsStream();
                } else {
                    throw new WPSException("Error getting remote resources from " + ref.getHref()
                            + ", http error " + code + ": " + method.getStatusText());
                }
            } else {
                // use the normal url connection methods then...
                URLConnection conn = destination.openConnection();
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setReadTimeout(CONNECTION_TIMEOUT);
                input = conn.getInputStream();
            }
            
            // actually parse teh data
            if(input != null) {
                return ppio.decode(input);
            } else {
                throw new WPSException("Could not find a mean to read input " + inputId);
            }
        } finally {
            // make sure to close the connection and streams no matter what
            if(input != null) {
                input.close();
            }
            if (method != null) {
                method.releaseConnection();
            }
            if(refMethod != null) {
                refMethod.releaseConnection();
            }
        }
    }
    
    /**
     * A process listener. For the moment just used to check if the process execution failed
     * @author Andrea Aime - OpenGeo
     *
     */
    static class ProcessListener implements ProgressListener {
        Exception exception; 

        public void complete() {
            // TODO Auto-generated method stub
            
        }

        public void dispose() {
            // TODO Auto-generated method stub
            
        }

        public void exceptionOccurred(Throwable exception) {
            this.exception = this.exception;
            
        }

        public String getDescription() {
            // TODO Auto-generated method stub
            return null;
        }

        public float getProgress() {
            // TODO Auto-generated method stub
            return 0;
        }

        public InternationalString getTask() {
            // TODO Auto-generated method stub
            return null;
        }

        public boolean isCanceled() {
            // TODO Auto-generated method stub
            return false;
        }

        public void progress(float percent) {
            // TODO Auto-generated method stub
            
        }

        public void setCanceled(boolean cancel) {
            // TODO Auto-generated method stub
            
        }

        public void setDescription(String description) {
            // TODO Auto-generated method stub
            
        }

        public void setTask(InternationalString task) {
            // TODO Auto-generated method stub
            
        }

        public void started() {
            // TODO Auto-generated method stub
            
        }

        public void warningOccurred(String source, String location, String warning) {
            // TODO Auto-generated method stub
            
        }
        
    }
    
}