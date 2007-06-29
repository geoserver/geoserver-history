package org.vfny.geoserver.wms.responses.featureInfo;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.logging.Logger;
import java.util.HashMap;
import java.util.Map;

import org.geoserver.template.FeatureWrapper;
import org.geoserver.template.GeoServerTemplateLoader;
import org.geotools.feature.Feature;
import org.geotools.feature.FeatureType;

import freemarker.ext.beans.BeansWrapper;
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
 * For performance reasons the template lookups will be cached, so it's advised to 
 * use the same FeatureTemplate object in a loop that encodes various features, but not
 * to cache it for a long time (static reference).
 * @author Justin Deoliveira, The Open Planning Project, jdeolive@openplans.org
 * @author Andrea Aime, TOPP
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
    
    Map templateCache = new HashMap();


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
//      ByteArrayOutputStream output = new ByteArrayOutputStream();
        StringWriter writer = new StringWriter();
        title(feature, writer);

        return writer.toString();  //new String(output.toByteArray());

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
       
        //ByteArrayOutputStream output = new ByteArrayOutputStream();
        StringWriter writer = new StringWriter();
        description( feature, writer );
        
        return  writer.toString();  // new String( output.toByteArray() );
    }
    
    /*
     * Internal helper method to exceute the template against feature or 
     * feature collection.
     */
    private void execute( Object feature, FeatureType featureType, Writer writer, String template ) 
        throws IOException {
        Template t = lookupTemplate(featureType, template);

        try {
            t.process(feature, writer);
        } catch (TemplateException e) {
            String msg = "Error occured processing template.";
            throw (IOException) new IOException(msg).initCause(e);
        }
    }

    /**
     * Returns the template for the specified feature type. Looking up templates is pretty
     * expensive, so we cache templates by feture type and template.
     * @param featureType
     * @param template
     * @return
     * @throws IOException
     */
    private Template lookupTemplate(FeatureType featureType, String template) throws IOException {
        Template t;
        
        // lookup the cache first
        TemplateKey key = new TemplateKey(featureType, template);
        t = (Template) templateCache.get(key);
        if(t != null)
            return t;
        
        // otherwise, build a loader and do the lookup
        GeoServerTemplateLoader templateLoader = new GeoServerTemplateLoader(getClass());
        templateLoader.setFeatureType(featureType);

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
        templateCache.put(key, t);
        return t;
    }
    
    private static class TemplateKey {
        FeatureType type;
        String template;
        public TemplateKey(FeatureType type, String template) {
            super();
            this.type = type;
            this.template = template;
        }
        
        public int hashCode() {
            final int PRIME = 31;
            int result = 1;
            result = PRIME * result + ((template == null) ? 0 : template.hashCode());
            result = PRIME * result + ((type == null) ? 0 : type.hashCode());
            return result;
        }
        
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            final TemplateKey other = (TemplateKey) obj;
            if (template == null) {
                if (other.template != null)
                    return false;
            } else if (!template.equals(other.template))
                return false;
            if (type == null) {
                if (other.type != null)
                    return false;
            } else if (!type.equals(other.type))
                return false;
            return true;
        }
    }
}
