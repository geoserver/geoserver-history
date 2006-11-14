package org.geoserver.web;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.geoserver.GeoServerResourceLoader;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

/**
 * Controller which publishes schema files through a web interface.
 * 
 * @author Justin Deoliveira, The Open Planning Project
 *
 */
public class SchemaPublisher extends AbstractController {

	GeoServerResourceLoader loader;
	
	public SchemaPublisher( GeoServerResourceLoader loader ) {
		this.loader = loader;
	}
	
	protected ModelAndView handleRequestInternal(
		HttpServletRequest request, HttpServletResponse response
	) throws Exception {
		
		String ctxPath = request.getContextPath();
		String reqPath = request.getRequestURI();
		reqPath = reqPath.substring( ctxPath.length() );
		if ( reqPath.length() > 1 && reqPath.startsWith( "/" ) ) {
			reqPath = reqPath.substring( 1 );
		}
		
		File schema = loader.find( reqPath ); 
		if ( schema == null ) {
			//return a 404
			response.setStatus( HttpServletResponse.SC_NOT_FOUND );
			return null;
		}
		
		//copy teh schema to the poutput
		response.setContentType( "text/xml" );
		response.setCharacterEncoding( "UTF-8" );
		
		BufferedReader reader = 
			new BufferedReader( new InputStreamReader( new FileInputStream( schema ) ) );
		
		BufferedWriter writer = 
			new BufferedWriter( new OutputStreamWriter( response.getOutputStream() ) );
		
		try {
			String line = null;
			while( ( line = reader.readLine() ) != null ) {
				writer.write( line );
			}
		}
		finally {
			writer.flush();
			reader.close();
		}
	
		return null;
	}

}
