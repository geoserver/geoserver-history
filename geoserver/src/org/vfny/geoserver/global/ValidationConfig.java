/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.global;

import java.beans.BeanDescriptor;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.geotools.validation.FeatureValidation;
import org.geotools.validation.IntegrityValidation;
import org.geotools.validation.Validation;
import org.geotools.validation.ValidationProcessor;
import org.geotools.validation.attributes.UniqueFIDIntegrityValidation;
import org.geotools.validation.spatial.IsValidGeometryFeatureValidation;

import org.vfny.geoserver.config.*;

/**
 * ValidationConfig sets up the Tests used for the VWFS.
 * 
 * <p>
 * The ValidationProcess stores test in two data structures according to test
 * type. This class is responsible for loading test cases into the Validation
 * Processor for use.
 * </p>
 * 
 * <p>
 * Capabilities:
 * <ul>
 * <li>
 * Read Test Definition:
 * Define users tests in terms of PlugIns
 * </li>
 * <li>
 * Read Plug-In Definition Files:
 * Mapping from PlugIn to Java Bean implementing the test
 * </li>
 * </ul>
 * </p>
 * 
 * <p>
 * For more information please read the documentation available at:
 * </p>
 *
 * @author jgarnett, Refractions Research, Inc.
 * @author $Author: dmzwiers $ (last modification)
 * @version $Id: ValidationConfig.java,v 1.1.2.3 2004/01/02 17:34:57 dmzwiers Exp $
 *
 * @see http://vwfs.refractions.net/docs/Validating_Web_Feature_Server.pdf
 */
public class ValidationConfig extends AbstractConfig {
    private static final Logger LOGGER = Logger.getLogger(
            "org.vfny.geoserver.config");    
    /** This is the validation processor we are configuring.
     * <p>
     * I don't like having parts of the application, contained by the
     * configuration classes. I would like a separation of configuration
     * form results...
     * </p>
     * But not today.
     */
    ValidationProcessor processor = new ValidationProcessor();
    
    /** Lookup of PlugInInfo */
    Map plugIns;
    
    /**
     * Configure based on gt2 CatalogConfig.
     * <p>
     * Configuration based on the following:
     * <ul>
     * <li>tests: List of tests</li>
     * <li>tests.test: plugIn (String required)</li>
     * <li>tests.test.description: definition (Map optional) </li>
     * <li>validation.plugIn: Bean (Class required )</li>
     * <li>validation.plugIn.description: (String optional )</li>
     * <li>validation.plugIn.defaults: (Map optional )</li> 
     * </ul>
     * </p>
     * @param config DOCUMENT ME!
     * @param catalog
     */
  /*  public ValidationConfig(Map config ) throws ConfigurationException {
        LOGGER.info("loading validation configuration");

        List tests = get( config, "validation", Collections.EMPTY_LIST );
        List plugInNames = new ArrayList();
        
        for( Iterator i=tests.iterator(); i.hasNext(); ){
            String test = (String) i.next();
            String plugIn = get( config, "tests."+test );
            if( plugIn == null ) continue; // ignore test then
            plugInNames.add( plugIn );                                     
        }
        if( plugInNames.isEmpty() ) return; //no validation required
        
        // configure plugIns
        for( Iterator i=plugInNames.iterator(); i.hasNext(); ){
            String plugIn = (String) i.next();
            Class type = get( config, "validation."+plugIn, Validation.class );
            String description = get( config, "validation."+plugIn+".description" );
            Map definition = get( config, "validation."+plugIn+".defaults", Collections.EMPTY_MAP );
            
            plugIns.put( plugIn, new PlugIn(plugIn, type,description, definition ) );
        }
        // now we can set up the tests
        for( Iterator i=tests.iterator(); i.hasNext(); ){
            String test = (String) i.next();
            String plugInName = get( config, "tests."+test );
            Map definition = get( config, "validation."+test+".definition", Collections.EMPTY_MAP );
                        
            PlugIn plugIn = (PlugIn) plugIns.get( plugInName );
            Validation validation = plugIn.createValidation( definition );
            if( validation instanceof FeatureValidation ){
                processor.addValidation( (FeatureValidation) validation );
            }
            if( validation instanceof IntegrityValidation){
                processor.addValidation( (IntegrityValidation) validation );
            }                                     
        }                
    }*/
    /**
     * ValidationConfig constructor.
     * <p>
     * Description
     * </p>
     * @param dir Validation Directory
     */
   /* public ValidationConfig(File dir) {
        if( dir.exists() ){
            LOGGER.info("Default isValidALL and uniqueFID enabled");
            // XML not supported yet - lets use a couple Validations
            processor.addValidation(
                new IsValidGeometryFeatureValidation(
                    "isValidALL",
                    "Tests to see if a geometry is valid",
                     Validation.ALL
                )
            );
            processor.addValidation(            
                new UniqueFIDIntegrityValidation(
                    "uniqueFID",
                    "Checks if each feature has a unique ID",
                    Validation.ALL,
                    "FID"
                )
            );
        }        
    }*/
    
    public ValidationConfig(ModelConfig config){
    	// do nothing yet.
    }
    
    /**
     * getProcessor purpose.
     * <p>
     * Description ...
     * </p>
     * @return
     */
    public ValidationProcessor getProcessor() {
        return processor;
    }

}
/**
 * Contains the information required for Validation creation.
 * <p>
 * Currently just used for configuration, may need to be public for dynamic
 * configuration.
 * </p>
 * 
 * @see http://vwfs.refractions.net/docs/Validating_Web_Feature_Server.pdf 
 */
class PlugIn extends AbstractConfig {
    Map defaults;
    String plugInName;
    String plugInDescription;
    BeanInfo beanInfo;
    Map propertyMap;
    PlugIn( Map config ) throws ConfigurationException{
        this( get( config, "name"), get( config, "bean", Validation.class ), get( config,"description"), config );        
    }
    
    PlugIn( String name, Class type, String description, Map config  ) throws ConfigurationException{
        if( type == null || !Validation.class.isAssignableFrom( type ) &&
            type.isInterface() ){
            throw new ConfigurationException( "Not a validation test '"+name+"' plugIn:"+type );            
        }
        try {
            beanInfo = Introspector.getBeanInfo( type );
        } catch (IntrospectionException e) {
            LOGGER.log(Level.FINEST, e.getMessage(), e);
            throw new ConfigurationException( "Could not use the '"+name+"' plugIn:"+type );
        }
        defaults = config;
        plugInName = name;
        plugInDescription = description;
        
        propertyMap = propertyMap( beanInfo );                     
    }
    
    protected PropertyDescriptor propertyInfo( String name ){
        return (PropertyDescriptor) propertyMap.get( name );
    }
    
    protected static Map propertyMap( BeanInfo info ){
        PropertyDescriptor properties[] = info.getPropertyDescriptors();
        Map lookup = new HashMap( properties.length );
        for( int i = 0; i< properties.length; i++ ){
            lookup.put( properties[i].getName(), properties[i] );
        }        
        return lookup;
    }
    /**
     * Create a Validation based on provided <code>test</code> definition.
     * <p>
     * Creates the required Java Bean and configures according to the provided
     * test definition, using this plugIn's defaults.
     * </p>
     * @param test Map defining User's test.
     * @return Validation ready for use by the ValidationProcessor
     */
    public Validation createValidation( Map test ) throws ConfigurationException {
        BeanDescriptor beanDescriptor = beanInfo.getBeanDescriptor();
        Class type = beanDescriptor.getBeanClass();
        String name = get( test, "name" );
        String description = get( test, "description" );
                
           
        Constructor create;                  
        try {
            create = type.getConstructor( new Class[0] );
                        
        } catch (SecurityException e) {
            LOGGER.log(Level.FINEST, e.getMessage(), e);
            throw new ConfigurationException( "Could not create '"+plugInName+"' as "+type.getName(), e );            
        } catch (NoSuchMethodException e) {
            LOGGER.log(Level.FINEST, e.getMessage(), e);
            throw new ConfigurationException( "Could not create '"+plugInName+"' as "+type.getName(), e );
        } catch (IllegalArgumentException e) {
            LOGGER.log( Level.FINEST, e.getMessage(), e );
            throw new ConfigurationException( "Could not create '"+plugInName+"' as "+type.getName(), e );            
        }
        
        Validation validate;
        try {                
            validate = (Validation) create.newInstance( new Object[0] );
        } catch (InstantiationException e) {
            LOGGER.log( Level.FINEST, e.getMessage(), e );
            throw new ConfigurationException( "Could not create '"+name+"' as plugIn "+plugInName, e );            
        } catch (IllegalAccessException e) {
            LOGGER.log( Level.FINEST, e.getMessage(), e );
            throw new ConfigurationException( "Could not create '"+name+"' as plugIn "+plugInName, e );            
        } catch (InvocationTargetException e) {
            LOGGER.log( Level.FINEST, e.getMessage(), e );
            throw new ConfigurationException( "Could not create '"+name+"' as plugIn "+plugInName, e );         
        }
        configure( validate, defaults );
        configure( validate, test );
        return validate;
    }
    protected void configure( Object bean, Map config ) throws ConfigurationException{
        if( config == null ) return;
        
        String name = get( config, "name", "A" ); // A test
        
        PropertyDescriptor property;        
        for( Iterator i=config.entrySet().iterator(); i.hasNext(); ){
            Map.Entry entry = (Entry) i.next();
            property = propertyInfo( (String) entry.getKey() );
            if( property == null ) {
                LOGGER.warning( name+ " test ignored "+plugInName+" "+entry.getKey() );
                continue;                                                           
            }
            try {
                property.getWriteMethod().invoke( bean, new Object[]{ entry.getValue() } );
            } catch (IllegalArgumentException e) {
                LOGGER.log( Level.SEVERE, name+ " test failed to configure "+plugInName+" "+entry.getKey(), e );
                throw new ConfigurationException( name+ " test failed to configure "+plugInName+" "+entry.getKey(), e );                
            } catch (IllegalAccessException e) {
                LOGGER.log( Level.SEVERE, name+ " test failed to configure "+plugInName+" "+entry.getKey(), e );
                throw new ConfigurationException( name+ " test failed to configure "+plugInName+" "+entry.getKey(), e );                
            } catch (InvocationTargetException e) {
                LOGGER.log( Level.SEVERE, name+ " test failed to configure "+plugInName+" "+entry.getKey(), e );
                throw new ConfigurationException( name+ " test failed to configure "+plugInName+" "+entry.getKey(), e );                
            }
        }
    }
}