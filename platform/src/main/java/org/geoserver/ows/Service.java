package org.geoserver.ows;

/**
 * Bean wrapper for an Open Web Service (OWS).
 * <p>
 * Service descriptors are identified by an id, version pair. Two service descriptors are 
 * considered equal if they have the same id, and version.
 * </p>
 * 
 * @author Justin Deoliveira, The Open Planning Project, jdeolive@openplans.org
 *
 */
public final class Service {

	/**
	 * Identifier for the service.
	 */
	String id;
	
	/**
	 * The service itself.
	 */
	Object service;
	
	/**
	 * The service version
	 */
	String version;
	
	/**
	 * Creates a new service descriptor.
	 * 
	 * @param id A string identifing the service, must not be <code>null</code>
	 * @param service The object implementing the service.
	 */
	public Service( String id, Object service ) {
		this( id, service, null );
	}
	
	/**
	 * Creates a new service descriptor.
	 * 
	 * @param id A string identifing the service.
	 * @param service The object implementing the service.
	 * @param version The version of the service, specified in [major].[minor].[patch] format.
	 */
	public Service( String id, Object service, String version ) {
		this.id = id;
		this.service = service;	
		this.version = version;
		
		if ( id == null ) {
			throw new NullPointerException( "id" );
		}
	}
	
	public String getId() {
		return id;
	}
	
	public Object getService() {
		return service;
	}
	
	public String getVersion() {
		return version;
	}
	
	public boolean equals(Object obj) {
		if ( !( obj instanceof Service ) ) {
			return false;
		}
		
		Service other = (Service) obj;
		if ( !id.equals( other.id) ) 
			return false;
	
		if ( version == null ) {
			if ( other.version != null ) 
				return false;
		}
		else {
			if ( ! version.equals( other.version ) )
				return false;
			
		}
		
		return true;
	}
	
	public int hashCode() {
		int result = 0;
		
		result = id.hashCode();
		if ( version != null ) {
			result = result*17 + version.hashCode();
		}
		
		return result;
	}
	
	public String toString() {
		return "Service( " + id + ", " + version + " )";
	}
}
