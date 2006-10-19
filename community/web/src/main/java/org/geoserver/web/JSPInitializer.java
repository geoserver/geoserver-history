package org.geoserver.web;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

import javax.servlet.ServletContext;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.context.ServletContextAware;

public class JSPInitializer 
	implements ServletContextAware, InitializingBean, ApplicationContextAware {

	ServletContext servletContext;
	
	ApplicationContext context;
	
	public void setServletContext( ServletContext servletContext ) {
		this.servletContext = servletContext;
	}
	
	public void setApplicationContext(ApplicationContext context) throws BeansException {
		this.context = context;
	}

	public void afterPropertiesSet() throws Exception {
		
		String path = servletContext.getRealPath( "/WEB-INF/lib" );
		File file = new File( path );
		if ( !file.exists() )
			return;
		
		File[] jars = file.listFiles( 
			new FilenameFilter () {
				public boolean accept(File dir, String name) {
					return name.endsWith( ".jar" );
				}
			}
		);
		
		File jspDir = new File( servletContext.getRealPath( "/WEB-INF/jsp" ) );
		for ( int i = 0; i < jars.length; i++ ) {
			JarFile jar = new JarFile( jars[i] );
			if ( jar.getEntry( "WEB-INF/jsp" ) == null ) 
				continue;
				
			Enumeration entries = jar.entries();
			while( entries.hasMoreElements() ) {
				ZipEntry entry = (ZipEntry) entries.nextElement();
				if ( !entry.getName().startsWith( "WEB-INF/jsp/") )
					continue;
				if ( entry.getName().equals( "WEB-INF/jsp/") )
					continue;
				
				File jsp = new File( jspDir, entry.getName().substring( "WEB-INF/jsp/".length() ) );
				if ( entry.isDirectory() ) {
					if ( !jsp.exists())
						jsp.mkdirs();
				}
				else {
					if ( jsp.exists() )
						jsp.delete();
					
					jsp.createNewFile();
					
					InputStream in = jar.getInputStream( entry );
					OutputStream out = new BufferedOutputStream( new FileOutputStream( jsp ) );
					
					int b = -1;
					while ( ( b = in.read() ) != -1 ) out.write( b );
					
					in.close();
					out.flush();
					out.close();
				}
			}
				
		}
	}

}
