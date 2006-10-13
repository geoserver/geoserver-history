package org.geoserver.ows.http;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.geoserver.http.util.RequestUtils;
import org.geoserver.http.util.ResponseUtils;
import org.geoserver.ows.Operation;
import org.geoserver.ows.Service;
import org.geoserver.ows.ServiceException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

public class OWSDispatcher extends AbstractController {

	protected ModelAndView handleRequestInternal(
		HttpServletRequest httpRequest, HttpServletResponse httpResponse
	) throws Exception {
	
		try {
			dispatch( httpRequest, httpResponse );
		}
		catch( ServiceException e ) {
			exception( e, httpRequest, httpResponse );
		}
		catch( Throwable t ) {
			throw new RuntimeException( t );
		}
		return null;
	}
	
	void dispatch( HttpServletRequest httpRequest, HttpServletResponse httpResponse ) throws
		Throwable {
		
		//step 1: parse kvp set
		Map kvp = parseKVP( httpRequest );
		
		//step 2: determine which operation is being called
		String method = httpRequest.getMethod();
		String service = null;
		String request = null;
		String version = null;
		
		File cache = cacheInputStream( httpRequest );
		
		if ( "get".equalsIgnoreCase( method ) ) {
			//lookup in query string
			service = (String) kvp.get( "service" );
			request = (String) kvp.get( "request" );
			version = (String) kvp.get( "version" );
		}
		else if ( "post".equalsIgnoreCase( method ) ) {
			InputStream input = input( cache );
			if ( input != null ) {
				try {
					Map xml = readOpPost( input );
					service = (String) xml.get( "service" );
					request = (String) xml.get( "request" );
					version = (String) xml.get( "version" );
				}
				finally {
					input.close();
				}	
			}
		}
		
		if ( service == null || request == null || version == null ) {
			Map map = readOpContext( httpRequest );
			if ( service == null ) {
				service = (String) map.get( "service" );
			}
			if ( request == null ) {
				request = (String) map.get( "request" );
			}
			if ( version == null ) {
				version = (String) map.get( "version" );
			}
		}
		
		if ( request == null ) {
			String msg = "Could not determine request.";
			throw new RuntimeException( msg );
		}
		
		//step 2: look up the service
		Service serviceDescriptor = findService( service, version );
		Object serviceBean = serviceDescriptor.getService();
		
		//step 3: lookup the operation, initial lookup based on (service,request)
		Method operation = OWSUtils.method( serviceBean.getClass(), request );
		if ( operation == null ) {
			String msg = "Could not dispatch request: ( " + service + ", " + request + " )";
			throw new ServiceException( msg ); 
		}
		
		//step 4: setup the paramters
		Object[] parameters = new Object[ operation.getParameterTypes().length ];
		for ( int i = 0; i < parameters.length; i++ ) {
			Class parameterType = operation.getParameterTypes()[ i ];
			
			//first check for servlet request and response
			if ( parameterType.isAssignableFrom( HttpServletRequest.class ) ) {
				parameters[ i ] = httpRequest; 
			}
			else if ( parameterType.isAssignableFrom( HttpServletResponse.class ) ) {
				parameters[ i ] = httpResponse;
			}
			//next check for input and output
			else if ( parameterType.isAssignableFrom( InputStream.class ) ) {
				parameters[ i ] = httpRequest.getInputStream();
			}
			else if ( parameterType.isAssignableFrom( OutputStream.class ) ) {
				parameters[ i ] = httpResponse.getOutputStream();
			}
			else {
				//check for a request object
				if ( "get".equalsIgnoreCase( method ) ) {
					//use the kvp reader mechanism
					Object requestBean = parseRequestKVP( parameterType, kvp );
					if ( requestBean != null ) {
						parameters[ i ] = requestBean;
					}
					
				}
				else if ( "post".equalsIgnoreCase( method ) ){
					//use the xml reader mechanism
					Object requestBean = parseRequestXML( cache );
					if ( requestBean != null ) {
						parameters[ i ] = requestBean;
					}
				}
			}
		}
		
		//step 5: execute
		Object result = null;
		try {
			result = operation.invoke( serviceBean, parameters );
		}
		catch( InvocationTargetException e ) {
			if ( e.getTargetException() != null ) {
				throw e.getTargetException();
			}
		}
				
		//step 6: write response
		if ( result != null ) {
			//look up the response for the result
			//TODO: choose based on request
			Collection responses = 
				getApplicationContext().getBeansOfType( Response.class ).values();
			Operation opDescriptor = new Operation( request, serviceDescriptor, parameters );
			ArrayList matches = new ArrayList();
			for( Iterator itr = responses.iterator(); itr.hasNext(); ) {
				Response response = (Response) itr.next();
				
				if (!response.canHandle( opDescriptor ) )
					continue;
				
				if ( response.getBinding().isAssignableFrom( result.getClass() ) ) {
					matches.add( response );
				}
			}
			
			if ( matches.isEmpty() ) {
				String msg = "No response: (" + result.getClass() + ")"; 
				throw new RuntimeException( msg );
			}
			
			if ( matches.size() > 1 ) {
				//sort by class hierarchy and v
				Collections.sort( 
					matches, 
					new Comparator() {

						public int compare(Object o1, Object o2) {
							Class c1 = ((Response)o1).getBinding();
							Class c2 = ((Response)o2).getBinding();
							
							if ( c1.equals( c2 ) )
								return 0;
							
							if ( c1.isAssignableFrom( c2 ) )
								return 1;
							
							if ( c2.isAssignableFrom( c1 ) );
								return -1;
						}
					}
				);
				
				//check first two and make sure bindings are not equal
				Response r1 = (Response) matches.get( 0 );
				Response r2 = (Response) matches.get( 1 );
				
				if ( r1.getBinding().equals( r2.getBinding() ) ) {
					String msg = "Multiple responses: (" + result.getClass() + ")"; 
					throw new RuntimeException( msg );	
				}
			}
			
			Response response = (Response) matches.get( 0 );
			
			//set the mime type
			httpResponse.setContentType( response.getMimeType( opDescriptor ) );
			
			//TODO: initialize any header params (gzip,deflate,etc...)
			
			OutputStream output = httpResponse.getOutputStream();
			response.write( result, output, opDescriptor );
		
			try {
				output.flush();
			}
			catch( IOException e ) {
				//TODO: log
			}
			
		}
		
		if (cache != null) {
			cache.deleteOnExit();	
		}
		
	}

	Collection loadServices() {
		Collection services = getApplicationContext().getBeansOfType( Service.class ).values();
	
		if ( !( new HashSet( services ).size() == services.size() ) ) {
			String msg = "Two identical service descriptors found";
			throw new IllegalStateException( msg );
		}
	
		return services;
	}
	
	Service findService( String id, String version ) {
		Collection services = loadServices();
		
		//first just match on service,request
		List matches = new ArrayList();
		for ( Iterator itr = services.iterator(); itr.hasNext(); ) {
			Service sBean = (Service) itr.next();
			
			if (sBean.getId().equalsIgnoreCase( id ) ) {
				matches.add( sBean );
			}
		}
		
		if ( matches.isEmpty() ) {
			String msg = "No service: ( " + id + " )"; 
			throw new RuntimeException( msg );
		}
		
		Service sBean = null;
		//if multiple, use version to filter match
		if ( matches.size() > 1 ) {
			List vmatches = new ArrayList( matches );
			//match up the version
			if ( version != null ) {
				//version specified, look for a match
				for ( Iterator itr = vmatches.iterator(); itr.hasNext(); ) {
					Service s = (Service) itr.next();

					if ( version.equals( s.getVersion() ) ) 
						continue;
					
					itr.remove();
				}
				
				if ( vmatches.isEmpty() ) {
					//no matching version found, drop out and next step 
					// will sort to return highest version
					vmatches = new ArrayList( matches );
				}
			}
			
			//multiple services found, sort by version
			if ( vmatches.size() > 1 ) {
				//use highest version
				Collections.sort( 
					vmatches, 
					new Comparator() {

						public int compare( Object o1, Object o2 ) {
							String v1 = ((Service)o1).getVersion();
							String v2 = ((Service)o2).getVersion();
							
							if ( v1 != null ) {
								return -1*v1.compareTo( v2 );
							}
							else if ( v2 != null ) {
								return -1;
							}
							
							return 0;
						}
						
					}
				);
			}
			
			sBean = (Service) vmatches.get( 0 );
		}
		else {
			//only a single match, that was easy
			sBean = (Service) matches.get( 0 );
		}
		
		return sBean;
	}
	
//	Operation findOperation( String service, String id, String version ) {
//		Collection operations = loadOperations();
//		
//		//first just match on service,request
//		List matches = new ArrayList();
//		for ( Iterator itr = operations.iterator(); itr.hasNext(); ) {
//			Operation opBean = (Operation) itr.next();
//			Service sBean = opBean.getService();
//			
//			if (opBean.getId().equalsIgnoreCase( id ) ) {
//				if ( service == null || service.equalsIgnoreCase( sBean.getId() ) ) {
//					matches.add( opBean );
//				}
//			}
//		}
//		
//		if ( matches.isEmpty() ) {
//			String msg = "No operation: (" + service + "," + id + ",)"; 
//			throw new RuntimeException( msg );
//		}
//		
//		Operation opBean = null;
//		//if multiple, use version to filter match
//		if ( matches.size() > 1 ) {
//			List vmatches = new ArrayList( matches );
//			//match up the version
//			if ( version != null ) {
//				//version specified, look for a match
//				for ( Iterator itr = vmatches.iterator(); itr.hasNext(); ) {
//					Operation o = (Operation) itr.next();
//					Service s = (Service) o.getService();
//
//					if ( version.equals( s.getVersion() ) ) 
//						continue;
//					
//					itr.remove();
//				}
//				
//				if ( vmatches.isEmpty() ) {
//					//no matching version found, drop out and next step 
//					// will sort to return highest version
//					vmatches = new ArrayList( matches );
//				}
//			}
//			
//			//multiple operations found, sort by version
//			if ( vmatches.size() > 1 ) {
//				//use highest version
//				Collections.sort( 
//					vmatches, 
//					new Comparator() {
//
//						public int compare( Object o1, Object o2 ) {
//							String v1 = ((Operation)o1).getService().getVersion();
//							String v2 = ((Operation)o2).getService().getVersion();
//							
//							if ( v1 != null ) {
//								return -1*v1.compareTo( v2 );
//							}
//							else if ( v2 != null ) {
//								return -1;
//							}
//							
//							return 0;
//						}
//						
//					}
//				);
//			}
//			
//			opBean = (Operation) vmatches.get( 0 );
//		}
//		else {
//			//only a single match, that was easy
//			opBean = (Operation) matches.get( 0 );
//		}
//		
//		return opBean;
//	}
	
	KvpRequestReader findKvpRequestReader( Class type ) {
		Collection kvpReaders = 
			getApplicationContext().getBeansOfType( KvpRequestReader.class ).values();
		
		if ( !( new HashSet( kvpReaders ).size() == kvpReaders.size() ) ) {
			String msg = "Two identical kvp readers found";
			throw new IllegalStateException( msg );
		}
		
		List matches = new ArrayList();
		for ( Iterator itr = kvpReaders.iterator(); itr.hasNext(); ) {
			KvpRequestReader kvpReader = (KvpRequestReader) itr.next();
			if ( kvpReader.getRequestBean().isAssignableFrom( type ) ) {
				matches.add( kvpReader );
			}
		}
		
		if ( matches.isEmpty() ) {
			//try to instantiate one
			String msg = "No kvp reader: ( " + type +  " )"; 
			throw new RuntimeException( msg );
		}
		
		if ( matches.size() > 1 ) {
			//sort by class hierarchy
			Comparator comparator = new Comparator() {

				public int compare( Object o1, Object o2 ) {
					KvpRequestReader kvp1 = (KvpRequestReader) o1;
					KvpRequestReader kvp2 = (KvpRequestReader) o2;
					
					if ( kvp2.getRequestBean().isAssignableFrom( kvp1.getRequestBean() ) ) {
						return -1;
					}
					
					return 1;
				}
			};
			
			Collections.sort( matches, comparator );
		}
		
		return (KvpRequestReader) matches.get( 0 );
	}
	
	Collection loadXmlReaders() {
		Collection xmlReaders = getApplicationContext().getBeansOfType( XmlRequestReader.class ).values();
		if ( !( new HashSet( xmlReaders ).size() == xmlReaders.size() ) ) {
			String msg = "Two identical xml readers found";
			throw new IllegalStateException( msg );
		}
		
		return xmlReaders;
	}
	

	XmlRequestReader findXmlReader( String namespace, String element, String version ) {
		Collection xmlReaders = loadXmlReaders();
		
		//first just match on namespace, element
		List matches = new ArrayList();
		for ( Iterator itr = xmlReaders.iterator(); itr.hasNext(); ) {
			XmlRequestReader xmlReader = (XmlRequestReader) itr.next();
			
			if ( xmlReader.getElement().equalsIgnoreCase( element ) ) {
				String rnamespace = xmlReader.getNamespace();
				if ( rnamespace == null ) {
					rnamespace = "";
				}
				if ( rnamespace.equalsIgnoreCase( namespace ) ) {
					matches.add( xmlReader );
				}
			}
		}
		
		if ( matches.isEmpty() ) {
			String msg = "No xml reader: (" + namespace + "," + element + ")"; 
			throw new RuntimeException( msg );
		}
		
		XmlRequestReader xmlReader = null;
		//if multiple, use version to filter match
		if ( matches.size() > 1 ) {
			List vmatches = new ArrayList( matches );
			//match up the version
			if ( version != null ) {
				//version specified, look for a match
				for ( Iterator itr = vmatches.iterator(); itr.hasNext(); ) {
					XmlRequestReader r = (XmlRequestReader) itr.next();
				
					if ( version.equals( r.getVersion() ) ) 
						continue;
					
					itr.remove();
				}
				
				if ( vmatches.isEmpty() ) {
					//no matching version found, drop out and next step 
					// will sort to return highest version
					vmatches = new ArrayList( matches );
				}
			}
			
			//multiple readers found, sort by version
			if ( vmatches.size() > 1 ) {
				//use highest version
				Collections.sort( 
					vmatches, 
					new Comparator() {

						public int compare( Object o1, Object o2 ) {
							String v1 = ((XmlRequestReader)o1).getVersion();
							String v2 = ((XmlRequestReader)o2).getVersion();
							
							if ( v1 != null ) {
								return -1*v1.compareTo( v2 );
							}
							else if ( v2 != null ) {
								return -1;
							}
							
							return 0;
						}
						
					}
				);
			}
			
			xmlReader = (XmlRequestReader) vmatches.get( 0 );
		}
		else {
			//only a single match, that was easy
			xmlReader = (XmlRequestReader) matches.get( 0 );
		}
		
		return xmlReader;
	}
	
	File cacheInputStream( HttpServletRequest request ) throws IOException  {
		InputStream input = request.getInputStream();
		if ( input == null )
			return null;
		
		File cache = File.createTempFile("geoserver","req");
		BufferedOutputStream output = 
			new BufferedOutputStream( new FileOutputStream( cache ) );
		
		byte[] buffer = new byte[1024];
		int n = 0;
		int nread = 0;
		
		while (( n = input.read( buffer ) ) > 0) {
			output.write( buffer, 0, n );
			nread += n;
		}
		
		output.flush();
		output.close();
		
		if ( nread == 0 )
			return null;
		
		return cache;
	}
	
	BufferedInputStream input( File cache ) throws IOException {
		return cache == null ? null :
			new BufferedInputStream( new FileInputStream( cache ) );
	}
	
	Map parseKVP( HttpServletRequest request ) throws ServiceException {
		//unparsed kvp set
		Map kvp = request.getParameterMap();
		
		if ( kvp == null )
			return Collections.EMPTY_MAP;
		
		//look up parser objects
		Collection parsers = getApplicationContext().getBeansOfType(KvpParser.class).values();
		Map parsedKvp = new KvpMap();
		
		for ( Iterator itr = kvp.entrySet().iterator(); itr.hasNext(); ) {
			Map.Entry entry = (Map.Entry) itr.next();
			String key = (String) entry.getKey();
			String value = null;
			if ( entry.getValue() instanceof String ) {
				value = (String) entry.getValue();
			}
			else if ( entry.getValue() instanceof String[] ) {
				//TODO: perhaps handle multiple values for a key
				value = (String) ( (String[]) entry.getValue() )[0];
			}
			
			
			//find the parser for this key value pair
			Object parsed = null;
			for ( Iterator pitr = parsers.iterator(); pitr.hasNext(); ) {
				KvpParser parser = (KvpParser) pitr.next();
				if ( key.equalsIgnoreCase( parser.getKey() ) ) {
					try {
						parsed = parser.parse( value );
					}
					catch(Throwable t) {
						String msg = "kvp parsing failed for: " + key;
						throw new ServiceException( msg, t );
					}
				}
			}
			
			//if noone could parse, just set to string value
			if ( parsed == null ) {
				parsed = value;
			}
			
			//convert key to lowercase 
			parsedKvp.put( key.toLowerCase(), parsed );
		}
		
		return parsedKvp;
	}
	
	Object parseRequestKVP( Class type, Map kvp ) throws Exception {
		KvpRequestReader kvpReader = findKvpRequestReader( type );
		if ( kvpReader != null ) {
			Object requestBean = kvpReader.createRequest();
			if ( requestBean != null ) {
				requestBean = kvpReader.read( requestBean, kvp );
			}
			
			return requestBean;
		}
		
		return  null;
	}
	Object parseRequestXML( File cache ) throws Exception {
		InputStream input = input( cache );
		
		//check for an empty input stream
		if ( input.available() == 0 ) {
			input.close();
			return null;
		}
		
		//create stream parser
		XmlPullParserFactory factory = 
			XmlPullParserFactory.newInstance();
		factory.setNamespaceAware(true);
		factory.setValidating(false);
			
		//parse root element
		XmlPullParser parser = factory.newPullParser();
		parser.setInput( input, "UTF-8" );
		parser.nextTag();
		
		String namespace = parser.getNamespace() != null ? parser.getNamespace() : "";
		String element = parser.getName();
		String version = null;
		
		for ( int i = 0; i < parser.getAttributeCount(); i++ ) {
			if ( "version".equals( parser.getAttributeName( i ) ) ) {
				version = parser.getAttributeValue( i );
				break;
			}
		}
		
		parser.setInput( null );
		
		//reset input stream
		input.close();
		input = input( cache );
		
		XmlRequestReader xmlReader = findXmlReader( namespace, element, version );
		return xmlReader.read( input );
	}
	
	Map readOpContext( HttpServletRequest request ) {
		//try to get from request url
		String ctxPath = request.getContextPath();
		String reqPath = request.getRequestURI();
		reqPath = reqPath.substring( ctxPath.length() );
		
		if ( reqPath.startsWith("/") ) {
			reqPath = reqPath.substring( 1, reqPath.length() );
		}
		
		if ( reqPath.endsWith("/") ) {
			reqPath = reqPath.substring( 0, reqPath.length()-1 );
		}
		
		Map map = new HashMap();
		int index = reqPath.indexOf('/');
		if (index != -1) {
			
			map.put( "service", reqPath.substring( 0, index ) );
			map.put( "request", reqPath.substring( index + 1 ) );
		}
		else {
			
			map.put( "service", reqPath );
			
		}
		
		return map;
	}
	
	Map readOpPost( InputStream input ) throws Exception {
	
		//create stream parser
		XmlPullParserFactory factory = 
			XmlPullParserFactory.newInstance();
		factory.setNamespaceAware(true);
		factory.setValidating(false);
			
		//parse root element
		XmlPullParser parser = factory.newPullParser();
		parser.setInput( input, "UTF-8" );
		parser.nextTag();
		
		Map map = new HashMap();
		map.put( "request", parser.getName() );
		for ( int i = 0; i < parser.getAttributeCount(); i++ ) {
			if ( "service".equals( parser.getAttributeName( i ) ) ) {
				map.put( "service", parser.getAttributeValue(i) );
			}
			if ( "version".equals( parser.getAttributeName( i ) ) ) {
				map.put( "version", parser.getAttributeValue( i ) );
			}
		}
		
		//close parser + release resources
		parser.setInput(null);
		input.close();
		
		return map;
	}
	
	void exception( ServiceException e, HttpServletRequest request, HttpServletResponse response ) {
		
		try {
			String tab = "   ";
			
			StringBuffer s = new StringBuffer();
			s.append( "<?xml version=\"1.0\" ?>\n" );
			s.append( "<ServiceExceptionReport\n" );
			s.append( tab + "version=\"1.2.0\"\n" );
			s.append( tab + "xmlns=\"http://www.opengis.net/ogc\"\n" );
			s.append( tab + "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" );
			s.append( tab );
			s.append( "xsi:schemaLocation=\"http://www.opengis.net/ogc " );
			s.append( RequestUtils.baseURL(request)  + "schemas/wfs/1.0.0/OGC-exception.xsd\">\n" );

			s.append( tab + "<ServiceException" );
			if ( e.getCode() != null && !e.getCode().equals( "" ) ) {
				s.append( " code=\"" + e.getCode() + "\"" );
			}
			if ( e.getLocator() != null && !e.getLocator().equals( "" ) ) {
	            s.append(" locator=\"" + e.getLocator() + "\"" );
	        }

			if ( e.getMessage() != null && !e.getMessage().equals( "" ) ) {
				s.append( ">\n" + tab + tab );
				s.append( e.getMessage() );
				
				ByteArrayOutputStream stackTrace = new ByteArrayOutputStream();
				e.printStackTrace( new PrintStream( stackTrace ) );
				
				s.append( ResponseUtils.encodeXML( new String( stackTrace.toByteArray() ) ) );
			}
			

			s.append( "\n</ServiceException>" );
			s.append( "</ServiceExceptionReport>" );
			response.setContentType( "text/xml" );
			response.setCharacterEncoding( "UTF-8" );
			response.getOutputStream().write( s.toString().getBytes() );
			response.getOutputStream().flush();
			
		} 
		catch (IOException ioe) {
			throw new RuntimeException( ioe );
		}
	
	}

	/**
	 * Map which makes keys case insensitive.
	 * 
	 * @author Justin Deoliveira, The Open Planning Project
	 *
	 */
	private static class KvpMap extends HashMap {
		
		public boolean containsKey(Object key) {
			return super.containsKey( lower( key ) );
		}
		
		public Object get(Object key) {
			return super.get( lower( key ) ); 
		}
		
		public Object put(Object key, Object value) {
			return super.put( lower( key ), value );
		} 
		
		Object lower( Object key ) {
			if ( key != null && key instanceof String ) {
				return ( (String) key ).toLowerCase();
			}
			
			return key;
		}
		
	}
}
