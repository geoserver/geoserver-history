/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.global;

import java.io.File;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * DOCUMENT ME!
 *
 * @author Gabriel Roldán
 * @version $Id: Abstract.java,v 1.1.2.2 2004/01/06 23:03:12 dmzwiers Exp $
 */
public abstract class Abstract {
    /** DOCUMENT ME! */
    protected static final Logger LOGGER = Logger.getLogger(
            "org.vfny.geoserver.global");

	/**
	 * 
	 * getDTO purpose.
	 * <p>
	 * A hook to get the DTO object of all subclasses. 
	 * This method is dangerous as it return the original. 
	 * It is not intended to be public, and should only be accessed 
	 * without a clone being created inside the package.
	 * </p>
	 * @return A DTO object to be casted as appropriate. 
	 */
	abstract Object toDTO();
	
    /**
     * checks <code>s</code> for nullity and if so, returns an empty String,
     * else just returns <code>s</code>
     *
     * @param s DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected String notNull(String s) {
        return (s == null) ? "" : s;
    }

    public static String get( Map map, String key ){
        if( map.containsKey( key )){
            return (String) map.get( key );
        }
        return null;
    }
    public static File get( Map map, String key, File defaultFile ){
        if( map.containsKey( key )){
            return (File) map.get( key );
        }
        return defaultFile;
    }
    public static String get( Map map, String key, String defaultValue ){
        if( map.containsKey( key )){
            return (String) map.get( key );
        }
        return defaultValue;
    }
    public static List get( Map map, String key, List defaultList ){
        if( map.containsKey( key )){
            return (List) map.get( key );
        }
        return defaultList;
    }
    public static Map get( Map map, String key, Map defaultMap ){
        if( map.containsKey( key )){
            return (Map) map.get( key );
        }
        return defaultMap;
    }    
    public static int get( Map map, String key, int defaultValue ){
        if( map.containsKey( key )){
            return Integer.parseInt( (String) map.get( key ));
        }
        else {
            return defaultValue;
        }
    }
    public static boolean get( Map map, String key, boolean defaultValue ){
        if( map.containsKey( key )){
            return Boolean.getBoolean( (String) map.get( key ));
        }
        else {
            return defaultValue;
        }
    }
    public static Charset get( Map map, String key, Charset defaultCharSet ){
        if( map.containsKey( key )){
            return (Charset) map.get( key );
        }
        return defaultCharSet;
    }
    public static Level get( Map map, String key, Level defaultLevel ){
        if( map.containsKey( key )){
            return (Level) map.get( key );
        }
        return defaultLevel;
    }
    public static URL get( Map map, String key, URL defaultUrl ){
        if( map.containsKey( key )){
            return (URL) map.get( key );
        }
        return defaultUrl;
    }
    public static Class get( Map map, String key, Class defaultType ){
        if( !map.containsKey( key )){
            return defaultType;
        }
        Object value = map.get( key );
        if( value instanceof Class ){
            return (Class) value;
        }
        if( value instanceof String ){
            try {
                return Class.forName( (String) value );
            } catch (ClassNotFoundException e) {
                LOGGER.log(Level.FINEST, e.getMessage(), e);                
            }
        }
        return defaultType;            
    }
}
