/* Copyright (c) 2001, 2003 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.ows;

import org.geoserver.platform.GeoServerResourceLoader;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Controller which publishes schema files through a web interface.
 * <p>
 * To use this controller, it should be mapped to a particular url in the url
 * mapping of the spring dispatcher servlet. Example:
 * <pre>
 * <code>
 *   &lt;bean id="dispatcherMappings"
 *      class="org.springframework.web.servlet.handler.SimpleUrlHandlerMapping"&gt;
 *      &lt;property name="mappings"&gt;
 *        &lt;prop key="/schemas/** /*.xsd"&gt;
 *        &lt;prop key="/schemas/** /*.dtd"&gt;
 *      &lt;/property&gt;
 *   &lt;/bean&gt;
 * </code>
 * </pre>
 * </p>
 *
 * @author Justin Deoliveira, The Open Planning Project
 *
 */
public class SchemaPublisher extends AbstractController {
    GeoServerResourceLoader loader;

    public SchemaPublisher(GeoServerResourceLoader loader) {
        this.loader = loader;
    }

    protected ModelAndView handleRequestInternal(HttpServletRequest request,
        HttpServletResponse response) throws Exception {
        String ctxPath = request.getContextPath();
        String reqPath = request.getRequestURI();
        reqPath = reqPath.substring(ctxPath.length());

        if ((reqPath.length() > 1) && reqPath.startsWith("/")) {
            reqPath = reqPath.substring(1);
        }

        File schema = loader.find(reqPath);

        if (schema == null) {
            //return a 404
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);

            return null;
        }

        //copy teh schema to the poutput
        response.setContentType("text/xml");
        response.setCharacterEncoding("UTF-8");

        BufferedReader reader = new BufferedReader(new InputStreamReader(
                    new FileInputStream(schema)));

        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
                    response.getOutputStream()));

        try {
            String line = null;

            while ((line = reader.readLine()) != null) {
                writer.write(line);
            }
        } finally {
            writer.flush();
            reader.close();
        }

        return null;
    }
}
