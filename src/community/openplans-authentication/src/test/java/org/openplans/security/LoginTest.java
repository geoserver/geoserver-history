package org.openplans.security;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.FilterChain;
import javax.servlet.http.Cookie;

import org.acegisecurity.context.SecurityContextHolder;
import org.acegisecurity.context.SecurityContextImpl;
import org.acegisecurity.providers.AuthenticationProvider;
import org.acegisecurity.providers.ProviderManager;
import org.geoserver.test.GeoServerTestSupport;

import sun.misc.BASE64Encoder;

import com.mockrunner.mock.web.MockFilterChain;
import com.mockrunner.mock.web.MockHttpServletRequest;
import com.mockrunner.mock.web.MockHttpServletResponse;

public class LoginTest extends GeoServerTestSupport {

	private static final String secret;

	static {
		String tempsecret;
		try {
			File f = new File("/var/lib/secret.txt");

			BufferedReader br = new BufferedReader(new InputStreamReader(
					new FileInputStream(f)));
			tempsecret = br.readLine();
		} catch (IOException ioe) {
			tempsecret = "testing123";
		}
		secret = tempsecret;
	}

	public void testLoginSuccessful() throws Exception{
		OpenPlansProcessingFilter testFilter = new OpenPlansProcessingFilter();
		ProviderManager authenticationManager = new ProviderManager();
		authenticationManager.setProviders(
				Arrays.asList(new AuthenticationProvider[]{new OpenPlansAuthenticationProvider()})
				);
		testFilter.setAuthenticationManager(authenticationManager);
				
		MockHttpServletRequest request   = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		SecurityContextHolder.setContext(new SecurityContextImpl());
		request.addCookie(new Cookie("__ac", generateCookie("cdwinslow")));
		testFilter.doFilter(request, response, new MockFilterChain());
		assertEquals(
				SecurityContextHolder.getContext()
				.getAuthentication()
				.getPrincipal()
				.toString(),
				"cdwinslow");
	}
	
	public void testLoginFailure() throws Exception{
	   OpenPlansProcessingFilter testFilter = new OpenPlansProcessingFilter();
	   ProviderManager authenticationManager = new ProviderManager();
	   authenticationManager.setProviders(
			   Arrays.asList(new AuthenticationProvider[]{new OpenPlansAuthenticationProvider()})
			   );
	   
	   MockHttpServletRequest request = new MockHttpServletRequest();
	   MockHttpServletResponse response = new MockHttpServletResponse();
	   request.addCookie(new Cookie("__ac", "this is an invalid cookie"));
	   
	   SecurityContextHolder.setContext(new SecurityContextImpl());
	   testFilter.doFilter(request, response, new MockFilterChain());
	   
	   assertNull(SecurityContextHolder.getContext().getAuthentication());
	   
	   
	}
	
	public String generateCookie(String username) throws Exception{
	    SecretKeySpec key = new SecretKeySpec(secret.getBytes(), "SHA"); 
	    Mac mac = Mac.getInstance("HmacSHA1");
	    mac.init(key);
	    mac.update(username.getBytes());
	    byte[] result = (mac.doFinal());
	    String blah = "0123456789abcdef";
	    
	    String resultString = "";
	    for (int i = 0; i < result.length; i++){
	    	int first = (result[i] >> 4) & 0x0f;
	    	int second = result[i] & 0x0f;
	    	resultString += Character.valueOf(blah.charAt(first)) + Character.valueOf(blah.charAt(second)).toString();
	    }
	    
	    BASE64Encoder be = new BASE64Encoder();
	    
//	    System.out.println(resultString);
	    return be.encode((username + "\0" + resultString).getBytes());
	}	
}
