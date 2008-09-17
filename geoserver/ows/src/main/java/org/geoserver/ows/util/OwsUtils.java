/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.ows.util;

import java.lang.reflect.Method;
import java.util.Map;

import org.geotools.util.SoftValueHashMap;


/**
 * Utility class for performing reflective operations.
 *
 * @author Justin Deoliveira, The Open Planning Project
 *
 */
public class OwsUtils {
    
    /**
     * Cache of reflection information about a class, keyed by class.
     */
    static Map<Class, ClassProperties> classPropertiesCache = new SoftValueHashMap<Class, ClassProperties>();
    
    /**
     * Accessor for the class to property info cache.
     */
    static ClassProperties classProperties(Class clazz) {
        // SoftValueHashMap is thread safe, no need to synch
        ClassProperties properties = classPropertiesCache.get(clazz);
        if(properties == null) {
            properties = new ClassProperties(clazz);
            classPropertiesCache.put(clazz, properties);
        }
        return properties;
    }
    
    
    /**
     * Returns a setter method for a property of java bean.
     * <p>
     * The <tt>type</tt> parameter may be <code>null</code> to indicate the 
     * the setter for the property should be returned regardless of the type. If
     * not null it will be used to filter the returned method.
     * </p>
     * @param clazz The type of the bean.
     * @param property The property name.
     * @param type The type of the property, may be <code>null</code>.
     *
     * @return The setter method, or <code>null</code> if not found.
     */
    public static Method setter(Class clazz, String property, Class type) {
        return classProperties(clazz).setter(property, type);
    }

    /**
     * Returns a getter method for a property of java bean.
     *
     * @param clazz The type of the bean.
     * @param property The property name.
     * @param type The type of the property, may be null.
     *
     * @return The setter method, or <code>null</code> if not found.
     */
    public static Method getter(Class clazz, String property, Class type) {
        return classProperties(clazz).getter(property, type);
    }

    /**
     * Reflectivley retreives a propety from a java bean.
     *
     * @param object The java bean.
     * @param property The property to retreive.
     * @param type Teh type of the property to retreive.
     *
     * @return The property, or null if it could not be found..
     */
    public static Object property(Object object, String property, Class type) {
        Method getter = getter(object.getClass(), property, type);

        if (getter != null) {
            try {
                return getter.invoke(object, null);
            } catch (Exception e) {
                //TODO: log this
            }
        }

        return null;
    }

    /**
     * Returns a method with a pariticular name of a class, ignoring method
     * paramters.
     *
     * @param clazz The class
     * @param name The name of the method.
     *
     * @return The method, or <code>null</code> if it could not be found.
     */
    public static Method method(Class clazz, String name) {
        return classProperties(clazz).method( name );
    }

    /**
     * Returns an object of a particular type in a list of objects of
     * various types.
     *
     * @param parameters A list of objects, of various types.
     * @param type The type of paramter to be returned.
     *
     * @return The object of the specified type, or <code>null</code>
     */
    public static Object parameter(Object[] parameters, Class type) {
        for (int i = 0; i < parameters.length; i++) {
            Object parameter = parameters[i];

            if ((parameter != null) && type.isAssignableFrom(parameter.getClass())) {
                return parameter;
            }
        }

        return null;
    }
}
