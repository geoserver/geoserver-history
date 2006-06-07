

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.vfny.geoserver.ExceptionHandler;
import org.vfny.geoserver.servlets.AbstractService;

public class HelloWorld extends AbstractService {

	public HelloWorld() {
		super("HWS","SayHello", null);
	}
		
	protected ExceptionHandler getExceptionHandler() {
		return null;
	}
		
    public void doGet(HttpServletRequest request, HttpServletResponse response) 
    		throws ServletException, IOException {
    	
		OutputStream stream = response.getOutputStream();
		stream.write("Hello World".getBytes());
	}
    
}
