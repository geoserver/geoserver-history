package org.geoserver.wms;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import javax.servlet.http.HttpServletResponse;

import org.geoserver.ows.ServiceExceptionHandler;
import org.geoserver.ows.util.ResponseUtils;
import org.geoserver.platform.Service;
import org.geoserver.platform.ServiceException;
import org.vfny.geoserver.global.WMS;

public class WMSExceptionHandler extends ServiceExceptionHandler {

	WMS wms;
	
	public WMSExceptionHandler(Service service, WMS wms ) {
		super(service);
		this.wms = wms;
	}

	public void handleServiceException(ServiceException exception,
			Service service, HttpServletResponse response)  {

		String tab = "   ";
		StringBuffer sb = new StringBuffer("<?xml version=\"1.0\"");
		sb.append(" encoding=\"UTF-8\" standalone=\"no\" ?>");

		String dtd = 
			ResponseUtils.appendPath( wms.getSchemaBaseURL(), "wms/1.1.1/WMS_exception_1_1_1.dtd" );
		
		sb.append("<!DOCTYPE ServiceExceptionReport SYSTEM \"" + dtd + "\"> ");
		sb.append("<ServiceExceptionReport version=\"1.1.1\">");

		// Write exception code
		String code = (exception.getCode() != null) ? (" code=\"" + exception.getCode() + "\"") : "";
		sb.append(tab + "<ServiceException" + code  + ">" );
		
		sb.append("\n" + tab + tab);
        sb.append(ResponseUtils.encodeXML(exception.getMessage()));

         ByteArrayOutputStream stackTrace = new ByteArrayOutputStream();
         exception.printStackTrace(new PrintStream(stackTrace));

         sb.append(ResponseUtils.encodeXML(new String(stackTrace.toByteArray())));
         
		 sb.append( "\n" + tab + "</ServiceException>" );

		 // Write footer
		 sb.append("  </ServiceExceptionReport>");

		response.setContentType( "application/vnd.ogc.se_xml" );
		response.setCharacterEncoding("UTF-8");
		
		try {
			response.getOutputStream().write(sb.toString().getBytes());
			response.getOutputStream().flush();
		} 
		catch (IOException e) {
			throw new RuntimeException( e );
		}
	}

}
