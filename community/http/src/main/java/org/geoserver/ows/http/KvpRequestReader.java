package org.geoserver.ows.http;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Map;

/**
 * Creates a request bean from a kvp set.
 * <p>
 * This class is intended to be subclassed in cases when the creation
 * and initilization of a request bean cannot be created reflectivley. 
 * </p>
 * 
 * @author Justin Deoliveira, The Open Planning Project
 *
 */
public class KvpRequestReader {

	/**
	 * The class of the request bean
	 */
	private Class requestBean;
	
	/**
	 * Creats the new kvp request reader.
	 * 
	 * @param requestBean The type of the request read, not <code>null</code>
	 */
	public KvpRequestReader( Class requestBean ) {
		if ( requestBean == null ) 
			throw new NullPointerException();
		
		this.requestBean = requestBean;
	}
	
	/**
	 * @return The class of the request bean.
	 */
	public final Class getRequestBean() {
		return requestBean;
	}
	
	/**
	 * Creats a new instance of the request object.
	 * <p>
	 * Subclasses may with to override this method. The default implementation
	 * attempts to reflectivley create an instance of the request bean.
	 * </p>
	 * @return A new instance of the request.
	 */
	 public Object createRequest() throws Exception {
		 return getRequestBean().newInstance();
	 }
	
	/**
	 * Reads the request from the set of kvp parameters.
	 * <p>
	 * Subclasses may wish to override this method. The default implementation 
	 * uses java bean reflection to populate the request bean with parameters 
	 * taken from the kvp map.
	 * </p>
	 * <p>
	 * This method may return a new instance of the request object, or the original 
	 * passed in.
	 * </p>
	 * @param request The request instance.
	 * @param kvp The kvp set, map of String,Object.
	 * 
	 * @return A new request object, or the ori
	 */
	public Object read( Object request, Map kvp ) throws Exception {
		for ( Iterator e = kvp.entrySet().iterator(); e.hasNext(); ) {
			Map.Entry entry = (Map.Entry) e.next();
			String property = (String) entry.getKey();
			Object value = entry.getValue();
			if ( value == null ) 
				continue;
			
			Method setter = OWSUtils.setter( request.getClass(), property, value.getClass() );
			if ( setter != null ) {
				setter.invoke( request, new Object[] { value } );
			}
		}
		
		return request;
	}
	
	/**
	 * Equals override, equality is based on {@link #getRequestBean()}
	 */
	public final boolean equals(Object obj) {
		if ( obj instanceof KvpRequestReader ) {
			KvpRequestReader other = (KvpRequestReader) obj;
			return requestBean == other.requestBean;
		}
	
		return false;
	}
	
	
	public final int hashCode() {
		return requestBean.hashCode();
	}
	
}
