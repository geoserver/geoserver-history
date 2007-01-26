package org.geoserver.ows;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import net.opengis.ows.v1_0_0.ExceptionReportType;
import net.opengis.ows.v1_0_0.ExceptionType;
import net.opengis.ows.v1_0_0.OWSFactory;

import org.apache.xml.serialize.OutputFormat;

import org.geoserver.ows.xml.v1_0.OWSConfiguration;
import org.geoserver.platform.Service;
import org.geoserver.platform.ServiceException;
import org.geotools.xml.Encoder;

/**
 * A default service exception handler.
 * <p>
 * This exception handler will return an "ows:ExceptionReport" document defined
 * by the schema: {@linkplain http://schemas.opengis.net/ows/1.0.0/owsExceptionReport.xsd}.
 * </p>
 * 
 * @author Justin Deoliveira, The Open Planning Project
 *
 */
public class DefaultServiceExceptionHandler extends ServiceExceptionHandler {

	/**
	 * Constructor to be called if the exception is not for a particular service.
	 */
	public DefaultServiceExceptionHandler() {
		super( Collections.EMPTY_LIST );
	}

	/**
	 * Constructor to be called if the exception is for a particular service.
	 * 
	 * @param services List of services this handler handles exceptions for.
	 */
	public DefaultServiceExceptionHandler( List services ) {
		super( services );
	}
	
	public void handleServiceException(ServiceException exception,
			Service service, HttpServletResponse response) {
		
		OWSFactory factory = OWSFactory.eINSTANCE;
		
		ExceptionType e = factory.createExceptionType();
		if ( exception.getCode() != null ) {
			e.setExceptionCode( exception.getCode() );	
		}
		else {
			//set a default
			e.setExceptionCode( "NoApplicableCode" );
		}
		
		e.setLocator( exception.getLocator() );
		
		//add the message
		e.getExceptionText().add( exception.getMessage() );
		e.getExceptionText().addAll( exception.getExceptionText() );
		
		//add the entire stack trace
		//exception.
		ByteArrayOutputStream trace = new ByteArrayOutputStream();
		exception.printStackTrace( new PrintStream( trace ) );
		e.getExceptionText().add( new String( trace.toByteArray() ) );
		
		ExceptionReportType report = factory.createExceptionReportType();
		report.setVersion( "1.0.0" );
		report.getException().add( e );
		
		response.setContentType( "application/xml" );
		//response.setCharacterEncoding( "UTF-8" );
		
		OWSConfiguration configuration = new OWSConfiguration();
		
		OutputFormat format = new OutputFormat();
		format.setIndenting( true );
		format.setIndent( 2 );
		format.setLineWidth( 60 );
		
		Encoder encoder = new Encoder( configuration, configuration.schema() );
		encoder.setOutputFormat( format );
		
		//TODO: dont hardcode schema location
		encoder.setSchemaLocation(
			org.geoserver.ows.xml.v1_0.OWS.NAMESPACE, 
			"http://schemas.opengis.net/ows/1.0.0/owsExceptionReport.xsd" 
		);
		try {
			encoder.encode( report, org.geoserver.ows.xml.v1_0.OWS.EXCEPTIONREPORT, response.getOutputStream() );
		} 
		catch( Exception ex ) {
			throw new RuntimeException( ex );
		}
		finally {
			try {
				response.getOutputStream().flush();
			} 
			catch (IOException ioe) {}
		}

	}

}
