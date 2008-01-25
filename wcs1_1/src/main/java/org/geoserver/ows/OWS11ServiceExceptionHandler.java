/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.ows;

import net.opengis.ows.v1_1_0.ExceptionReportType;
import net.opengis.ows.v1_1_0.ExceptionType;
import net.opengis.ows.v1_1_0.Ows11Factory;

import org.apache.xml.serialize.OutputFormat;
import org.geoserver.ows.util.RequestUtils;
import org.geoserver.platform.Service;
import org.geoserver.platform.ServiceException;
import org.geotools.ows.v1_1.OWS;
import org.geotools.ows.v1_1.OWSConfiguration;
import org.geotools.xml.Encoder;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


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
public class OWS11ServiceExceptionHandler extends ServiceExceptionHandler {
    protected boolean verboseExceptions = false;
    
    /**
     * Constructor to be called if the exception is not for a particular service.
     *
     */
    public OWS11ServiceExceptionHandler() {
        super(Collections.EMPTY_LIST);
    }

    /**
     * Constructor to be called if the exception is for a particular service.
     *
     * @param services List of services this handler handles exceptions for.
     */
    public OWS11ServiceExceptionHandler(List services) {
        super(services);
    }
    
    /**
     * Constructor to be called if the exception is for a particular service.
     *
     * @param services List of services this handler handles exceptions for.
     */
    public OWS11ServiceExceptionHandler(Service service) {
        super(Arrays.asList(service));
    }

    /**
     * Writes out an OWS ExceptionReport document.
     */
    public void handleServiceException(ServiceException exception, Service service,
        HttpServletRequest request, HttpServletResponse response) {
        Ows11Factory factory = Ows11Factory.eINSTANCE;

        ExceptionType e = factory.createExceptionType();

        if (exception.getCode() != null) {
            e.setExceptionCode(exception.getCode());
        } else {
            //set a default
            e.setExceptionCode("NoApplicableCode");
        }

        e.setLocator(exception.getLocator());

        //add the message
        StringBuffer sb = new StringBuffer();
        dumpExceptionMessages(exception, sb);
        e.getExceptionText().add(sb.toString());
        e.getExceptionText().addAll(exception.getExceptionText());

        if(verboseExceptions) {
            //add the entire stack trace
            //exception.
            e.getExceptionText().add("Details:");
            ByteArrayOutputStream trace = new ByteArrayOutputStream();
            exception.printStackTrace(new PrintStream(trace));
            e.getExceptionText().add(new String(trace.toByteArray()));
        }

        ExceptionReportType report = factory.createExceptionReportType();
        report.setVersion("1.1.0");
        report.getException().add(e);

        response.setContentType("application/xml");

        //response.setCharacterEncoding( "UTF-8" );
        OWSConfiguration configuration = new OWSConfiguration();

        OutputFormat format = new OutputFormat();
        format.setIndenting(true);
        format.setIndent(2);
        format.setLineWidth(60);

        Encoder encoder = new Encoder(configuration, configuration.schema());
        encoder.setOutputFormat(format);

        encoder.setSchemaLocation(OWS.NAMESPACE,
            RequestUtils.schemaBaseURL(request) + "ows/1.1.0/owsAll.xsd");

        try {
            encoder.encode(report, OWS.ExceptionReport,
                response.getOutputStream());
        } catch (Exception ex) {
            //throw new RuntimeException(ex);
            // Hmm, not much we can do here.  I guess log the fact that we couldn't write out the exception and be done with it...
            LOGGER.log(Level.INFO, "Problem writing exception information back to calling client:", ex);
        } finally {
            try {
                response.getOutputStream().flush();
            } catch (IOException ioe) {
            }
        }
    }
}
