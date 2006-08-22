package org.geoserver.ows.http;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.mockobjects.servlet.MockHttpServletRequest;
import com.mockobjects.servlet.MockHttpServletResponse;
import com.mockobjects.servlet.MockServletInputStream;
import com.mockobjects.servlet.MockServletOutputStream;

public class DispatcherTest extends TestCase {

	public void testReadOpContext() throws Exception {
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setupGetContextPath( "/geoserver" );
		request.setupGetRequestURI( "/geoserver/hello" );
		request.setupGetMethod( "get" );
		
		Dispatcher dispatcher = new Dispatcher();
		Map map = dispatcher.readOpContext( request );
		
		assertEquals( "hello", map.get( "service" ) );
		assertNull( map.get("request") );
		
		request = new MockHttpServletRequest();
		request.setupGetContextPath( "/geoserver" );
		request.setupGetRequestURI( "/geoserver/hello/Hello" );
		request.setupGetMethod( "get" );
		map = dispatcher.readOpContext( request );
		
		request = new MockHttpServletRequest();
		request.setupGetContextPath( "/geoserver" );
		request.setupGetRequestURI( "/geoserver/hello/Hello/" );
		
		request.setupGetMethod( "get" );
		map = dispatcher.readOpContext( request );
		
		assertEquals( "hello", map.get( "service" ) );
		assertEquals( "Hello", map.get( "request" ) );
	}
	
	public void testReadOpPost() throws Exception {
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setupGetContextPath( "/geoserver" );
		request.setupGetRequestURI( "/geoserver/hello" );
		request.setupGetMethod( "post" );
		
		String body = "<Hello service=\"hello\"/>";
				
		MockServletInputStream input = new MockServletInputStream();
		input.setupRead( body.getBytes() );
		
		request.setupGetInputStream( input );
		
		Dispatcher dispatcher = new Dispatcher();
		Map map = dispatcher.readOpPost( input );
		
		assertNotNull( map );
		assertEquals( "Hello", map.get( "request" ) );
		assertEquals( "hello", map.get( "service" ) );
	}
	
	public void testParseKVP() throws Exception {
		URL url = getClass().getResource( "applicationContext.xml" );
			
		FileSystemXmlApplicationContext context = 
			new FileSystemXmlApplicationContext ( url.toString() );
		
		Dispatcher dispatcher = 
			(Dispatcher) context.getBean( "dispatcher" );
		
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setupGetContextPath("/geoserver");
		
		Map params = new HashMap();
		params.put( "service", "hello" );
		params.put( "request", "Hello" );
		params.put( "message", "Hello world!");
		
		request.setupGetParameterMap( params );
		
		Map kvp = dispatcher.parseKVP( request );
		assertEquals( new Message( "Hello world!" ), kvp.get( "message" ) );
	}
	
	public void testParseXML() throws Exception {
		URL url = getClass().getResource( "applicationContext.xml" );
		
		FileSystemXmlApplicationContext context = 
			new FileSystemXmlApplicationContext ( url.toString() );
		
		Dispatcher dispatcher = 
			(Dispatcher) context.getBean( "dispatcher" );
		
		String body = "<Hello service=\"hello\" message=\"Hello world!\"/>";
		File file = File.createTempFile("geoserver","req");
		FileOutputStream output = new FileOutputStream( file );
		output.write( body.getBytes() );
		output.flush();
		output.close();
		
		MockServletInputStream input = new MockServletInputStream();
		input.setupRead( body.getBytes() );
		
		Object object = dispatcher.parseXML( file );
		assertEquals( new Message( "Hello world!" ), object );
	}
	
	public void testHelloOperationGet() throws Exception {
		URL url = getClass().getResource( "applicationContext.xml" );
		
		FileSystemXmlApplicationContext context = 
			new FileSystemXmlApplicationContext ( url.toString() );
		
		Dispatcher dispatcher = 
			(Dispatcher) context.getBean( "dispatcher" );
		
		MockHttpServletRequest request = new MockHttpServletRequest();
		
		request.setupGetContextPath("/geoserver");
		request.setupGetMethod("GET");
		
		MockHttpServletResponse response = new MockHttpServletResponse();
		response.setupOutputStream( new MockServletOutputStream() );
		
		Map params = new HashMap();
		params.put( "service", "hello" );
		params.put( "request", "Hello" );
		params.put( "message", "Hello world!");
		
		request.setupGetParameterMap( params );
		request.setupGetInputStream(null);
		request.setupGetRequestURI( 
			"http://localhost/geoserver/ows?service=hello&request=hello&message=HelloWorld" 
		);
		dispatcher.handleRequest( request, response );
		assertEquals( params.get( "message" ), response.getOutputStreamContents() );
	}
	
	public void testHelloOperationPost() throws Exception {
		URL url = getClass().getResource( "applicationContext.xml" );
		
		FileSystemXmlApplicationContext context = 
			new FileSystemXmlApplicationContext ( url.toString() );
		
		Dispatcher dispatcher = 
			(Dispatcher) context.getBean( "dispatcher" );
		
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setupGetContextPath("/geoserver");
		request.setupGetMethod("POST");
		request.setupGetRequestURI( "http://localhost/geoserver/ows" );
		MockHttpServletResponse response = new MockHttpServletResponse();
		response.setupOutputStream( new MockServletOutputStream() );
		
		Map params = new HashMap();
		request.setupGetParameterMap( params );
		
		String body = "<Hello service=\"hello\" message=\"Hello world!\"/>";
		MockServletInputStream input = new MockServletInputStream();
		input.setupRead( body.getBytes() );
		
		request.setupGetInputStream( input );
		
		dispatcher.handleRequest( request, response );
		assertEquals( "Hello world!", response.getOutputStreamContents() );
	}
}
