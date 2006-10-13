package org.geoserver.ows;


/**
 * Operation of a service
 * <p>
 * An operation is identified by an id,service pair. Two operation
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
	Service service;

	/**
	 * Parameters of the operation
	 */
	Object[] parameters;
	
	/**
	 * Creates a new operation descriptor.
	 * 
	 * @param id Id of the operation, must not be <code>null</code>
	 * @param service The service containing the operation, must not be <code>null</code>
	 * @param parameters The parameters of the operation, may be <code>null</code>
	 * 
	 */
	public Operation( String id, Service service, Object[] parameters ) {
		this.id = id;
		this.service = service;
		this.parameters = parameters;
		
		if ( id == null ) 
			throw new NullPointerException( "id" );
	
		if ( service == null ) {
			throw new NullPointerException( "service" );
		}
	}
	
	/**
	 * @return The id of the operation.
	 */
	public String getId() {
		return id;
	}
	
	/**
	 * @return The service implementing the operation.
	 */
	public Service getService() {
		return service;
	}
	
	/**
	 * @return The parameters supplied to the operation
	 */
	public Object[] getParameters() {
		return parameters;
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
