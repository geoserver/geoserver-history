package org.vfny.geoserver.wms.responses.featureInfo;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

import org.geoserver.template.FeatureWrapper;
import org.geoserver.template.GeoServerTemplateLoader;
import org.geotools.feature.Feature;
import org.geotools.feature.FeatureType;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * Executes a "description" template for a feature.
 * <p>
 * Usage:
 * <pre>
 * <code>
 * Feature feature = ...  //some feature
 * Writer writer = ...    //some writer
 * 
 * FeatureDescriptionTemplate template = new FeatureDescriptionTemplate();
 * template.execute( feature );
 * </code>
 * </pre>
 * </p> 
 * @author Justin Deoliveira, The Open Planning Project, jdeolive@openplans.org
 *
 */
public class FeatureDescriptionTemplate {


    /**
     * The template configuration used for placemark descriptions
     */
    static Configuration templateConfig;

    static {
        //initialize the template engine, this is static to maintain a cache 
        // over instantiations of kml writer
        templateConfig = new Configuration();
        templateConfig.setObjectWrapper(new FeatureWrapper());
    }

    /**
     * Executes the template for a feature writing the results to an output 
     * stream.
     * <p>
     * This method is convenience for:
     * <code>
     * execute( feature, new OutputStreamWriter( output ) );
     * </code>
     * </p>
     * 
     * @param feature The feature to execute the template against.
     * @param output The output to write the result of the template to.
     * 
     * @throws IOException Any errors that occur during execution of the template.
     */
    public void execute( Feature feature, OutputStream output ) throws IOException {
        execute( feature, new OutputStreamWriter( output ) );
    }
    
    /**
     * Executes the template for a feature writing the results to a writer.
     * 
     * @param feature The feature to execute the template against.
     * @param writer The writer to write the template output to.
     * 
     * @throws IOException Any errors that occur during execution of the template.
     */
    public void execute( Feature feature, Writer writer ) throws IOException {
        
       execute( feature, feature.getFeatureType(), writer );
    }
    
    /**
     * Executes the template for a feature returning the result as a string.
     *
     * @param feature The feature to execute the template against.
     * 
     * @throws IOException Any errors that occur during execution of the template.
     */
    public String execute( Feature feature ) throws IOException {
       
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        execute( feature, output );
        
        return new String( output.toByteArray() );
    }
    
    /*
     * Internal helper method to exceute the template against feature or 
     * feature collection.
     */
    private void execute( Object feature, FeatureType featureType, Writer writer ) 
        throws IOException {
        
        //descriptions are "templatable" by users, so see if there is a 
        // template available for use
        GeoServerTemplateLoader templateLoader = new GeoServerTemplateLoader(getClass());
        templateLoader.setFeatureType(featureType.getTypeName());

        Template template = null;

        //Configuration is not thread safe
        synchronized (templateConfig) {
            templateConfig.setTemplateLoader(templateLoader);
            template = templateConfig.getTemplate("description.ftl");
        }

        try {
            template.process(feature, writer);
        } catch (TemplateException e) {
            String msg = "Error occured processing template.";
            throw (IOException) new IOException(msg).initCause(e);
        }
    }
}
