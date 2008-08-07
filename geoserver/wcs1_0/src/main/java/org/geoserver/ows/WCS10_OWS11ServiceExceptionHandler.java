/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.ows;

import java.util.List;

import org.geoserver.platform.Service;
import org.vfny.geoserver.global.GeoServer;


/**
 * A default implementation of {@link ServiceExceptionHandler} which outputs
 * as service exception in a <code>ows:ExceptionReport</code> document.
 * <p>
 * This service exception handler will generate an OWS exception report,
 * see {@linkplain http://schemas.opengis.net/ows/1.1.0/owsExceptionReport.xsd}.
 * </p>
 *
 * @author Justin Deoliveira, The Open Planning Project
 *
 */
public class WCS10_OWS11ServiceExceptionHandler extends LegacyServiceExceptionHandler {
    protected boolean verboseExceptions = false;
    
    /**
     * Constructor to be called if the exception is not for a particular service.
     *
     */
    public WCS10_OWS11ServiceExceptionHandler(Service service, OWS ows, GeoServer geoServer) {
        super(service, ows, geoServer);
    }

    /**
     * Constructor to be called if the exception is for a particular service.
     *
     * @param services List of services this handler handles exceptions for.
     */
    public WCS10_OWS11ServiceExceptionHandler(List services, OWS ows, GeoServer geoServer) {
        super(services, ows, geoServer);
        contentType = "application/vnd.ogc.se_xml";
    }
    
//    /**
//     * Writes out an OWS ExceptionReport document.
//     */
//    public void handleServiceException(ServiceException exception, Request request) {
//        Ows11Factory factory = Ows11Factory.eINSTANCE;
//
//        ExceptionType e = factory.createExceptionType();
//
//        if (exception.getCode() != null) {
//            e.setExceptionCode(exception.getCode());
//        } else {
//            //set a default
//            e.setExceptionCode("NoApplicableCode");
//        }
//
//        e.setLocator(exception.getLocator());
//
//        //add the message
//        StringBuffer sb = new StringBuffer();
//        dumpExceptionMessages(exception, sb, true);
//        e.getExceptionText().add(sb.toString());
//        e.getExceptionText().addAll(exception.getExceptionText());
//
//        if(verboseExceptions) {
//            //add the entire stack trace
//            //exception.
//            e.getExceptionText().add("Details:");
//            ByteArrayOutputStream trace = new ByteArrayOutputStream();
//            exception.printStackTrace(new PrintStream(trace));
//            e.getExceptionText().add(new String(trace.toByteArray()));
//        }
//
//        ExceptionReportType report = factory.createExceptionReportType();
//        report.setVersion("1.1.0");
//        report.getException().add(e);
//
//        HttpServletResponse response = request.getHttpResponse();
////        response.setContentType("application/xml");
//        response.setContentType("application/vnd.ogc.se_xml");
//
//        //response.setCharacterEncoding( "UTF-8" );
//        OWSConfiguration configuration = new OWSConfiguration();
//
//        Encoder encoder = new Encoder(configuration, configuration.schema());
//        encoder.setIndenting(true);
//        encoder.setIndentSize(2);
//        encoder.setLineWidth(60);
//
//        encoder.setSchemaLocation(OWS.NAMESPACE, RequestUtils.schemaBaseURL(request.getHttpRequest()) + "ows/1.1.0/owsAll.xsd");
//
//        try {
//            encoder.encode(report, OWS.ExceptionReport, response.getOutputStream());
//        } catch (Exception ex) {
//            //throw new RuntimeException(ex);
//            // Hmm, not much we can do here.  I guess log the fact that we couldn't write out the exception and be done with it...
//            LOGGER.log(Level.INFO, "Problem writing exception information back to calling client:", ex);
//        } finally {
//            try {
//                response.getOutputStream().flush();
//            } catch (IOException ioe) {
//            }
//        }
//    }
}
