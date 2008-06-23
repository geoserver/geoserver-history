/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.web.util;

import java.util.List;
import java.util.Locale;

import org.apache.wicket.IConverterLocator;
import org.apache.wicket.util.convert.IConverter;

/**
 * Composite converter locator which allows the wrapping of multiple implementations
 * of IConverterLocator.
 * 
 * @author Justin Deoliveira, The Open Planning Project
 *
 */
public class CompositeConverterLocator implements IConverterLocator {

    List<IConverterLocator> locators;
    
    public CompositeConverterLocator( List<IConverterLocator> locators ) {
        this.locators = locators;
    }
    
    public IConverter getConverter(Class type) {
        return new CompositeConverter(type);
    }
    
    class CompositeConverter implements IConverter {

        Class target;
        
        public CompositeConverter( Class target ) {
            this.target = target;
        }
        
        public Object convertToObject(String value, Locale locale) {
            for (IConverterLocator locator : locators ) {
                IConverter converter = null;
                try {
                    converter = locator.getConverter( target );    
                }
                catch( Throwable t ) {
                    //TODO: log this at finest level
                }
                
                if ( converter != null ) {
                    Object converted = converter.convertToObject( value, locale );
                    if( converted != null ) {
                        return converted;
                    }
                }
            }
            
            return null;
        }

        public String convertToString(Object value, Locale locale) {
            for (IConverterLocator locator : locators ) {
                IConverter converter = null;
                try {
                    converter = locator.getConverter( value.getClass() );    
                }
                catch( Throwable t ) {
                    //TODO: log this at finest level
                }
                
                if ( converter != null ) {
                    String converted = converter.convertToString( value, locale );
                    if( converted != null ) {
                        return converted;
                    }
                }
            }
            
            return null;
        }
        
    }

}
