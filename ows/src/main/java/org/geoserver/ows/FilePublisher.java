package org.geoserver.ows;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jmimemagic.Magic;
import net.sf.jmimemagic.MagicMatch;

import org.geoserver.platform.GeoServerResourceLoader;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

/**
 * Controller which publishes files through a web interface.
 * <p>
 * To use this controller, it should be mapped to a particular url in the url
 * mapping of the spring dispatcher servlet. Example:
 * <pre>
 * <code>
 *   &lt;bean id="filePublisher" class="org.geoserver.ows.FilePublisher"/&gt;
 *   &lt;bean id="dispatcherMappings"
 *      &lt;property name="alwaysUseFullPath" value="true"/&gt;
 *      class="org.springframework.web.servlet.handler.SimpleUrlHandlerMapping"&gt;
 *      &lt;property name="mappings"&gt;
 *        &lt;prop key="/schemas/** /*.xsd"&gt;filePublisher&lt;/prop&gt;
 *        &lt;prop key="/schemas/** /*.dtd"&gt;filePublisher&lt;/prop&gt;
 *        &lt;prop key="/styles/*"&gt;filePublisher&lt;/prop&gt;
 *      &lt;/property&gt;
 *   &lt;/bean&gt;
 * </code>
 * </pre>
 * </p>
 *
 * @author Justin Deoliveira, The Open Planning Project
 *
 */
public class FilePublisher extends AbstractController {

    /**
     * Resource loader
     */
    protected GeoServerResourceLoader loader;
    
    /**
     * Creates the new file publisher.
     * 
     * @param loader The loader used to locate files.
     */
    public FilePublisher( GeoServerResourceLoader loader ) {
        this.loader = loader;
    }
    
    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String ctxPath = request.getContextPath();
        String reqPath = request.getRequestURI();
        reqPath = reqPath.substring(ctxPath.length());

        if ((reqPath.length() > 1) && reqPath.startsWith("/")) {
            reqPath = reqPath.substring(1);
        }

        //load the file
        File file = loader.find(reqPath);
        if (file == null) {
            //return a 404
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return null;
        }

        //figure out the mime type
        MagicMatch match = Magic.getMagicMatch( file, true );
        if ( match == null ) {
            //return a 415: Unsupported Media Type
            response.setStatus( HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE );
            return null;
        }
        response.setContentType(match.getMimeType());

        //TODO: should hte encpoding be gotten from the file?
        response.setCharacterEncoding("UTF-8");

        //copy teh content to the output
        byte[] buffer = new byte[ 8192 ];
        InputStream input = new FileInputStream( file );
        OutputStream output = response.getOutputStream();
        
        try {
            int n = -1;
            while( ( n = input.read( buffer ) ) != -1 ) {
                output.write( buffer, 0, n );
            }
            
        } finally {
            output.flush();
            input.close();
        }

        return null;
    }

    
}
