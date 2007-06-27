package org.vfny.geoserver.wms.responses.featureInfo;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.logging.Logger;

import org.geoserver.template.FeatureWrapper;
import org.geoserver.template.GeoServerTemplateLoader;
import org.geotools.feature.Feature;
import org.geotools.feature.FeatureType;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * Executes a template for a feature.
 * <p>
 * Usage:
 * <pre>
 * <code>
 * Feature feature = ...  //some feature
 * Writer writer = ...    //some writer
 * 
 * FeatureTemplate template = new FeatureTemplate();
 * 
 *  //title
 * template.title( feature );
 * 
 *  //description
 * template.description( feature );
 * </code>
 * </pre>
 * </p> 
 * @author Justin Deoliveira, The Open Planning Project, jdeolive@openplans.org
 *
 */
public class FeatureTemplate {

	/**
	 * logger
	 */
	static Logger LOGGER = Logger.getLogger( "org.geoserver.wms" );
	
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
     * Executes the title template for a feature writing the results to an 
     * output stream.
     * <p>
     * This method is convenience for:
     * <code>
     * description( feature, new OutputStreamWriter( output ) );
     * </code>
     * </p>
     * 
     * @param feature The feature to execute the template against.
     * @param output The output to write the result of the template to.
     * 
     * @throws IOException Any errors that occur during execution of the template.
     */
    public void title( Feature feature, OutputStream output ) throws IOException {
        title( feature, new OutputStreamWriter( output ) );
    }
    
    /**
     * Executes the description template for a feature writing the results to an 
     * output stream.
     * <p>
     * This method is convenience for:
     * <code>
     * description( feature, new OutputStreamWriter( output ) );
     * </code>
     * </p>
     * 
     * @param feature The feature to execute the template against.
     * @param output The output to write the result of the template to.
     * 
     * @throws IOException Any errors that occur during execution of the template.
     */
    public void description( Feature feature, OutputStream output ) throws IOException {
        description( feature, new OutputStreamWriter( output ) );
    }
    
    /**
     * Executes the title template for a feature writing the results to a 
     * writer.
     * 
     * @param feature The feature to execute the template against.
     * @param writer The writer to write the template output to.
     * 
     * @throws IOException Any errors that occur during execution of the template.
     */
    public void title( Feature feature, Writer writer ) throws IOException {
       execute( feature, feature.getFeatureType(), writer, "title.ftl" );
    }
    
    
    /**
     * Executes the description template for a feature writing the results to a 
     * writer.
     * 
     * @param feature The feature to execute the template against.
     * @param writer The writer to write the template output to.
     * 
     * @throws IOException Any errors that occur during execution of the template.
     */
    public void description( Feature feature, Writer writer ) throws IOException {
       execute( feature, feature.getFeatureType(), writer, "description.ftl" );
    }
    
    /**
     * Executes the title template for a feature returning the result as a 
     * string.
     *
     * @param feature The feature to execute the template against.
     * 
     * @throws IOException Any errors that occur during execution of the template.
     */
    public String title( Feature feature ) throws IOException {
       
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        title( feature, output );
        
        return new String( output.toByteArray() );
    }
    
    /**
     * Executes the description template for a feature returning the result as a 
     * string.
     *
     * @param feature The feature to execute the template against.
     * 
     * @throws IOException Any errors that occur during execution of the template.
     */
    public String description( Feature feature ) throws IOException {
       
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        description( feature, output );
        
        return new String( output.toByteArray() );
    }
    
    /*
     * Internal helper method to exceute the template against feature or 
     * feature collection.
     */
    private void execute( Object feature, FeatureType featureType, Writer writer, String template ) 
        throws IOException {
        
        //descriptions are "templatable" by users, so see if there is a 
        // template available for use
        GeoServerTemplateLoader templateLoader = new GeoServerTemplateLoader(getClass());
        templateLoader.setFeatureType(featureType);

        Template t = null;

        //Configuration is not thread safe
        synchronized (templateConfig) {
            templateConfig.setTemplateLoader(templateLoader);
            
            //HACK for backwards compatability with "kmlPlacemarkDescription"
            // which has been replaced with just "description"
            if ( "description.ftl".equals( template ) ) {
            	//first try kmlPlacemarkDescription
            	try {
            		t = templateConfig.getTemplate( "kmlPlacemarkDescription.ftl" );
            		if ( t != null ) {
            			LOGGER.warning( "'kmlPlacemarkDescription.ftl' has been " +
        					"deprecated, please use 'description.ftl'");
            		}
            	}
            	catch( Exception e ) {
            		//its ok, ignore
            	}
            }
            
            //proceed as normal
            if ( t == null ) {
            	t = templateConfig.getTemplate(template);	
            }
            
        }

        try {
            t.process(feature, writer);
        } catch (TemplateException e) {
            String msg = "Error occured processing template.";
            throw (IOException) new IOException(msg).initCause(e);
        }
    }
}
