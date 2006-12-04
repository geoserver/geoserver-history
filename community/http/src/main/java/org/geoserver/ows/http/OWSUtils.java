package org.geoserver.ows.http;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Utility class for performing reflective operations.
 * 
 * @author Justin Deoliveira, The Open Planning Project
 *
 */
public class OWSUtils {

	/**
	 * Returns a setter method for a property of java bean.
	 * 
	 * @param clazz The type of the bean.
	 * @param property The property name.
	 * @param type The type of the property.
	 * 
	 * @return The setter method, or <code>null</code> if not found.
	 */
	public static Method setter( Class clazz, String property, Class type ) {
		//TODO: this lookup doesn't deal with classes that might have two setters
		// that are in the same class hierachy
		Method[] methods = clazz.getMethods();
		for ( int i = 0; i < methods.length; i++) {
			Method method = methods[ i ];
			if ( method.getName().equalsIgnoreCase( "set" + property ) ) {
				if ( 
					method.getParameterTypes().length == 1 &&
					method.getParameterTypes()[ 0 ].isAssignableFrom( type )
				) {
					return method;
				}
			}
		}
		
		return null;
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
	public static Method getter( Class clazz, String property, Class type ) {
		//TODO: this lookup doesn't deal with classes that might have two setters
		// that are in the same class hierachy
		Method[] methods = clazz.getMethods();
		for ( int i = 0; i < methods.length; i++) {
			Method method = methods[ i ];
			if ( method.getName().equalsIgnoreCase( "get" + property ) ) {
				if ( type != null ) {
					if ( type.equals( method.getReturnType() ) ) {
						return method;
					}	
				}
				else {
					return method;
				}
			}
		}
		
		return null;
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
	public static Object property( Object object, String property, Class type ) {
		Method getter = getter( object.getClass(), property, type );
		if ( getter != null ) {
			try {
				return getter.invoke( object, null );
			}
			catch( Exception e ) {
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
	public static Method method( Class clazz, String name ) {
		Method[] methods = clazz.getMethods();
		for ( int i = 0; i < methods.length; i++) {
			Method method = methods[ i ];
			if ( method.getName().equalsIgnoreCase( name ) ) {
				return method;
			}
		}
		
		return null;
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
	public static Object parameter( Object[] parameters, Class type ) {
		
		for ( int i = 0; i < parameters.length; i++ ) {
			Object parameter = parameters[ i ];
			if ( parameter != null && type.isAssignableFrom( parameter.getClass() ) ) {
				return parameter;
			}
		}
		
		return null;
	}
}
