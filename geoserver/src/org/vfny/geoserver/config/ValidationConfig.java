/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.config;

import java.beans.*;
import java.io.File;
import java.lang.reflect.*;
import java.util.*;
import java.util.Map.Entry;
import java.util.logging.Level;

import org.geotools.data.Catalog;
import org.geotools.data.DataStore;
import org.geotools.validation.Validation;

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
 * @author $Author: jive $ (last modification)
 * @version $Id: ValidationConfig.java,v 1.1.2.1 2003/11/26 06:21:40 jive Exp $
 *
 * @see http://vwfs.refractions.net/docs/Validating_Web_Feature_Server.pdf
 */
public class ValidationConfig extends AbstractConfig {
    /** Lookup of PlugInInfo */
    Map plugIns;
    
    /**
     * Configure based on gt2 Catalog.
     * <p>
     * Configuration based on the following:
     * <ul>
     * <li>tests: List of tests</li>
     * <li>tests.test: plugIn (String required)</li>
     * <li>tests.test.description: (String optional)</li>
     * <li>tests.test.defaults: defaults (Map optional) </li>
     * <li>validation.plugIn: Bean (Class required )</li>
     * <li>validation.plugIn.description: (String optional )</li>
     * <li>validation.plugIn.definition: (Map optional )</li> 
     * </ul>
     * </p>
     * @param config DOCUMENT ME!
     * @param catalog
     */
    public ValidationConfig(Map config ) {
        LOGGER.info("loading validation configuration");

        List tests = get( config, "validation", Collections.EMPTY_LIST );
        List plugInNames = new ArrayList();
        
        for( Iterator i=tests.iterator(); i.hasNext(); ){
            
        }
    }
    /**
     * ValidationConfig constructor.
     * <p>
     * Description
     * </p>
     * @param dir Validation Directory
     */
    public ValidationConfig(File dir) {
        // need to implement xml file format as outlined
        // in design documentation
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