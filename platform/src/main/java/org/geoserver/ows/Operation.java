package org.geoserver.ows;

import java.lang.reflect.Method;

/**
 * Bean wrapper for an operation.
 * <p>
 * An operation is nothing more then a Plain Old Java Object (POJO). It does 
 * not implement any GeoServer specific interface.
 * </p>
 * 
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
	
	public Operation( String id, Service service, Object operation ) {
		this.id = id;
		this.service = service;
		this.operation = operation;
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
	
	public Object run ( Object input ) 
		throws Exception {
		
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
}
