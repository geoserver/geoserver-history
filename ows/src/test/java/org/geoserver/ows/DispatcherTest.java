package org.geoserver.ows;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
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
		
		BufferedInputStream buffered = new BufferedInputStream( input );
		buffered.mark( 2048 );
		Map map = dispatcher.readOpPost( buffered );
		
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
		request.setupQueryString( "service=hello&request=hello&message=Hello World!" );
		
		Map kvp = dispatcher.parseKVP( request );
		
		Dispatcher.Request req = new Dispatcher.Request();
		req.kvp = kvp;
		Message message = (Message) dispatcher.parseRequestKVP( Message.class, req );
		assertEquals( new Message( "Hello world!" ) , message );
	}
	
	public void testParseXML() throws Exception {
		URL url = getClass().getResource( "applicationContext.xml" );
		
		FileSystemXmlApplicationContext context = 
			new FileSystemXmlApplicationContext ( url.toString() );
		
		Dispatcher dispatcher = 
			(Dispatcher) context.getBean( "dispatcher" );
		
		String body = "<Hello service=\"hello\" message=\"Hello world!\"/>";
		File file = File.createTempFile("geoserver","req");
		file.deleteOnExit();
		
		FileOutputStream output = new FileOutputStream( file );
		output.write( body.getBytes() );
		output.flush();
		output.close();
		
		BufferedInputStream input = new BufferedInputStream( new FileInputStream( file ) );
		input.mark( 8192 );
		Object object = dispatcher.parseRequestXML( input );
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
		params.put( "version", "1.0.0" );
		params.put( "message", "Hello world!");
		
		request.setupGetParameterMap( params );
		request.setupGetInputStream(null);
		request.setupGetRequestURI( 
			"http://localhost/geoserver/ows?service=hello&request=hello&message=HelloWorld" 
		);
		request.setupQueryString( "service=hello&request=hello&message=HelloWorld" );
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
		request.setupGetContentType( "application/xml" );
		MockHttpServletResponse response = new MockHttpServletResponse();
		response.setupOutputStream( new MockServletOutputStream() );
		
		Map params = new HashMap();
		request.setupGetParameterMap( params );
		
		String body = "<Hello service=\"hello\" message=\"Hello world!\" version=\"1.0.0\" />";
		MockServletInputStream input = new MockServletInputStream();
		input.setupRead( body.getBytes() );
		
		request.setupGetInputStream( input );
		
		dispatcher.handleRequest( request, response );
		assertEquals( "Hello world!", response.getOutputStreamContents() );
	}
}
