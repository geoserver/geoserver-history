/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wms.responses.featureInfo;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

import org.geoserver.platform.GeoServerExtensions;
import org.geoserver.template.FeatureWrapper;
import org.geoserver.template.GeoServerTemplateLoader;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureType;
import org.vfny.geoserver.global.CoverageInfo;
import org.vfny.geoserver.global.Data;
import org.vfny.geoserver.global.FeatureTypeInfo;
import org.vfny.geoserver.global.GeoserverDataDirectory;
import org.vfny.geoserver.global.WMS;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;


/**
 * Produces a FeatureInfo response in HTML.  Relies on {@link AbstractFeatureInfoResponse} and
 * the feature delegate to do most of the work, just implements an HTML based
 * writeTo method.
 *
 * <p>
 * In the future James suggested that we allow some sort of template system, so
 * that one can control the formatting of the html output, since now we just
 * hard code some minimal header stuff. See
 * http://jira.codehaus.org/browse/GEOS-196
 * </p>
 *
 * @author James Macgill, PSU
 * @author Andrea Aime, TOPP
 * @version $Id$
 */
public class HTMLTableFeatureInfoResponse extends AbstractFeatureInfoResponse {
    private static Configuration templateConfig;

    static {
        // initialize the template engine, this is static to maintain a cache
        // over instantiations of kml writer
        templateConfig = new Configuration();
        templateConfig.setObjectWrapper(new FeatureWrapper());
    }
    
    GeoServerTemplateLoader templateLoader;
    
    
    /**
     *
     */
    public HTMLTableFeatureInfoResponse() {
        format = "text/html";
        supportedFormats = Collections.singletonList(format);
    }

    /**
     * Returns any extra headers that this service might want to set in the HTTP response object.
     * @see org.vfny.geoserver.Response#getResponseHeaders()
     */
    public HashMap getResponseHeaders() {
        return new HashMap();
    }

    /**
     * Writes the image to the client.
     *
     * @param out The output stream to write to.
     *
     * @throws org.vfny.geoserver.ServiceException For problems with geoserver
     * @throws java.io.IOException For problems writing the output.
     */
    public void writeTo(OutputStream out)
        throws org.vfny.geoserver.ServiceException, java.io.IOException {
        // setup the writer
        final Charset charSet = getRequest().getGeoServer().getCharSet();
        final OutputStreamWriter osw = new OutputStreamWriter(out, charSet);
        
        // if there is only one feature type loaded, we allow for header/footer customization,
        // otherwise we stick with the generic ones
        Template footer = null;
        if(results.size() == 1) {
            FeatureType templateFeatureType = ((FeatureCollection) results.get(0)).getSchema();
            Template header = getTemplate(templateFeatureType, "header.ftl", charSet);
            footer = getTemplate(templateFeatureType, "footer.ftl", charSet);
            try {
                header.process(null, osw);
            } catch (TemplateException e) {
                String msg = "Error occured processing header template.";
                throw (IOException) new IOException(msg).initCause(e);
            }
        }
        
        //process content template for all feature collections found
        for (Iterator it = results.iterator(); it.hasNext();) {
            FeatureCollection fc = (FeatureCollection) it.next();
            if(fc.size() > 0) {
                FeatureType ft = fc.getSchema();
                Template content = getTemplate(ft, "content.ftl", charSet);
                try {
                    content.process(fc, osw);
                } catch(TemplateException e) {
                    String msg = "Error occured processing content template " + 
                    content.getName() + " for " + ft.getTypeName();
                    throw (IOException) new IOException(msg).initCause(e);
                }
            }
        }
        
        // if a template footer was loaded (ie, there were only one feature 
        // collection), process it
        if(footer != null){
            try {
                footer.process(null, osw);
            } catch (TemplateException e) {
                String msg = "Error occured processing footer template.";
                throw (IOException) new IOException(msg).initCause(e);
            }
        }
        osw.flush();
    }

    public String getContentDisposition() {
        return null;
    }
    
    /**
     * Uses a {@link GeoServerTemplateLoader TemplateLoader} too look up for the template file
     * named <code>templateFilename</code> for the given <code>featureType</code>.
     * 
     * @param featureType the featureType to look the template for, may well correspond to an 
     * actually registered feature type or to a wrapper feature type used to adapt the result of
     * the feature info request over a raster coverage.
     * @param templateFileName the name of the template to look for
     * @param charset the encoding to apply to the resulting {@link Template}
     * @return the template named <code>templateFileName</code>
     * @throws IOException if the template can't be loaded
     */
    Template getTemplate(FeatureType featureType, String templateFileName, Charset charset) throws IOException {
        // setup template subsystem
        if(templateLoader == null) {
            templateLoader = new GeoServerTemplateLoader(getClass());
        }
        
        final Data catalog =  (Data) GeoServerExtensions.bean("data");
        final String localName = featureType.getTypeName();
        final String namespaceURI = featureType.getNamespace().toString();
        final FeatureTypeInfo featureTypeInfo = catalog.getFeatureTypeInfo(localName, namespaceURI);
        
        if(featureTypeInfo == null){
            //It may be a wrapped coverage
            CoverageInfo cInfo = catalog.getCoverageInfo(localName);
            if(cInfo != null){
                templateLoader.setCoverageName(localName);
            }else{
                throw new IllegalArgumentException("Can't find neither a FeatureType nor " +
                        "a CoverageInfo named " + localName);
            }
        }else{
            templateLoader.setFeatureType(featureType);
        }

        synchronized (templateConfig) {
            templateConfig.setTemplateLoader(templateLoader);
            Template t = templateConfig.getTemplate(templateFileName);
            t.setEncoding(charset.name());
            return t;
        }
    }
}
