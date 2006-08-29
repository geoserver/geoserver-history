package org.geoserver.ows;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Bean wrapper for an operation.
 * <p>
 * An operation is nothing more then a Plain Old Java Object (POJO). It does 
 * not implement any GeoServer specific interface.
 * </p>
 * <p>
 * An operation descriptor is identified by an id,service pair. Two operation
 * descriptors are considred equal if they have the same id, service pair.
 * </p>
 * @author Justin Deoliveira, The Open Planning Project, jdeolive@openplans.org
 *
 */
public final class Operation {

	/**
	 * Unique identifier withing service of the operation.
	 */
	String id;
	
	/**
	 * Service this operation is a component of.
	 */
	Service  service;

	/**
	 * The operation itself.
	 */
	Object operation;
	
	/**
	 * Creates a new operation descriptor.
	 * 
	 * @param id Id of the operation, must not be <code>null</code>
	 * @param service The service containing the operation, must not be <code>null</code>
	 * @param operation
	 */
	public Operation( String id, Service service, Object operation ) {
		this.id = id;
		this.service = service;
		this.operation = operation;
		
		if ( id == null ) 
			throw new NullPointerException( "id" );
	
		if ( service == null ) {
			throw new NullPointerException( "service" );
		}
	}
	
	public String getId() {
		return id;
	}
	
	public Service getService() {
		return service;
	}
	
	public Object getOperation() {
		return operation;
	}
	
	public boolean set(String property, Object value) 
		throws Exception {
		
		Method method = method( "set" + property, null );
		if (method != null) {
			method.invoke( operation, new Object[]{value} );
			return true;
		}
		
		return false;
	}
	
	public Object get( String property ) throws Exception {
		
		Method method = method( "get" + property, null );
		if ( method != null ) {
			return method.invoke( operation, null ); 
		}
		
		return null;
	}
	
	public Object run ( Object input ) throws Exception {
		
		Method method = null;
		
		if ( input != null ) {
			method = method( getId(), input.getClass() );	
		}
		else {
			method = method( getId(), null );
		}
		
		if (method != null ) {
			Object[] parameters = input != null ? new Object[]{input} : null;
			
			return method.invoke( operation, parameters );	
		}
		
		return null;
	}

	protected Method method ( String name, Class parameter ) {
		Method[] methods = getOperation().getClass().getMethods();
		for ( int i = 0; i < methods.length; i++ ) {
			Method method = methods[i];
			if (method.getName().equalsIgnoreCase( name ) ) {
				if ( parameter != null ) {
					if ( method.getParameterTypes().length == 1) {
						Class type = method.getParameterTypes()[0];
						if ( type.isAssignableFrom( parameter ) ) {
							return method;
						}
					}
				}
				else return method;
			}
		}
		
		return null;
	}
	
	public boolean equals(Object obj) {
		if ( obj == null )
			return false;
		
		if ( !( obj instanceof Operation ) ) {
			return false;
		}
		
		Operation other = (Operation) obj;
		if ( !id.equals( other.id ) ) 
			return false;
		
		if ( !service.equals( other.service ) )
			return false;
		
		return true;
	}
	
	public int hashCode() {
		return id.hashCode()*17 + service.hashCode();
	}
	
	public String toString() {
		return "Operation( " + id + ", " + service.getId() + " )";
	}
	
}
