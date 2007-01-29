package org.geoserver.wfs.kvp;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Map;

import net.opengis.wfs.WFSFactory;

import org.eclipse.emf.ecore.EObject;
import org.geoserver.ows.KvpRequestReader;
import org.geoserver.ows.util.OwsUtils;
import org.geotools.xml.EMFUtils;

/**
 * Web Feature Service Key Value Pair Request reader.
 * <p>
 * This request reader makes use of the Eclipse Modelling Framework 
 * reflection api.
 * </p>
 * @author Justin Deoliveira, The Open Planning Project
 *
 */
public class WFSKvpRequestReader extends KvpRequestReader {

	/**
	 * WFs factory used to create model objects / requests.
	 */
	WFSFactory wfsFactory;
	
	/**
	 * Creates the Wfs Kvp Request reader.
	 * 
	 * @param requestBean The request class, which must be an emf class.
	 */
	public WFSKvpRequestReader( Class requestBean ) {
		super(requestBean);
		
		//make sure an eobject is passed in
		if ( !EObject.class.isAssignableFrom( requestBean ) ) {
			String msg = "Request bean must be an EObject";
			throw new IllegalArgumentException( msg );
		}
		
		wfsFactory = WFSFactory.eINSTANCE;
	}

	/**
	 * Reflectivley creates the request bean instance.
	 */
	public Object createRequest() {
		String className = getRequestBean().getName();
		//strip off package
		int index = className.lastIndexOf( '.' );
		if ( index != -1 ) {
			className = className.substring( index + 1 );
		}
		
		Method create = OwsUtils.method( WFSFactory.class, "create" + className );
		try {
			return create.invoke( wfsFactory, null );
		} 
		catch ( Exception e ) {
			throw new RuntimeException( e );
		} 
	}
	
	public Object read(Object request, Map kvp) throws Exception {
		//use emf reflection
		EObject eObject = (EObject) request;
		for ( Iterator e = kvp.entrySet().iterator(); e.hasNext(); ) {
			Map.Entry entry = (Map.Entry) e.next();
			String property = (String) entry.getKey();
			Object value = entry.getValue();
			
			if ( EMFUtils.has( eObject, property ) ) {
				EMFUtils.set( eObject, property, value );	
			}
		}
		
		return request;
	}
	
}
