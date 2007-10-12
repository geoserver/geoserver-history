package org.geoserver.security;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Logger;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.acegisecurity.Authentication;
import org.acegisecurity.AuthenticationException;
import org.acegisecurity.BadCredentialsException;
import org.acegisecurity.GrantedAuthority;
import org.acegisecurity.GrantedAuthorityImpl;
import org.acegisecurity.providers.AuthenticationProvider;
import org.acegisecurity.providers.UsernamePasswordAuthenticationToken;


/**
 * An authentication provider that can validate OpenPlans.org username and authentication token pairs.
 * The authentication token consists of a hash of the username and a secret key; validation is done by 
 * hashing the plaintext username with the secret key and comparing with the token. 
 * 
 * @author David Winslow - TOPP
 */
public class OpenPlansAuthenticationProvider implements AuthenticationProvider {

	/**
	 * The secret key.
	 */
	final String secret;
	
	static Logger LOGGER = Logger.getLogger("org.geoserver.community");
	
	/**
	 * Override the default constructor to read in the secret key from a file on disk.
	 */
	public OpenPlansAuthenticationProvider (){
		String tempSecret = "";
		try{
			File secretFile = new File("/usr/lib/secret.txt");
			BufferedReader br =
				new BufferedReader(new InputStreamReader(new FileInputStream(secretFile)));
			tempSecret = br.readLine();
		} catch (IOException ioe){
			tempSecret = "blah";
			LOGGER.severe("couldn't read file for secret");
		}
		secret = tempSecret;
	}
	
	public Authentication authenticate(Authentication arg0)
			throws AuthenticationException {
		UsernamePasswordAuthenticationToken auth = (UsernamePasswordAuthenticationToken)arg0;
		
		if (auth.getCredentials() != null && getAuth(auth.getName()).equals(auth.getCredentials().toString())) {
			return createNewAuthentication(auth);
		}
		
		throw new BadCredentialsException("something went wrong");
	}

	/**
	 * Create an authenticated AuthenticationToken with the same credentials as an existing one.
	 * @param auth the original authentication token
	 * @return the authenticated one
	 */
	private Authentication createNewAuthentication(UsernamePasswordAuthenticationToken auth) {
		GrantedAuthority[] ga = null;
		
		if (auth.getName().equals("cdwinslow")){
			// grant david admin rights so there's a difference between being logged out and not :D
			ga = new GrantedAuthority[(auth.getAuthorities() == null? 1 : auth.getAuthorities().length + 1)];
			for (int i = 0;
				auth.getAuthorities() != null 
				&& i < auth.getAuthorities().length;
				i++)
			{				
				ga[i] = auth.getAuthorities()[i];
			}
			ga[(auth.getAuthorities() == null ? 0 : auth.getAuthorities().length)] = new GrantedAuthorityImpl("ROLE_ADMINISTRATOR");
		}
		
		UsernamePasswordAuthenticationToken upat = new UsernamePasswordAuthenticationToken(auth.getName(), auth.getCredentials(), ga);
		return upat;
	}

	public boolean supports(Class arg0) {
		return UsernamePasswordAuthenticationToken.class.equals(arg0);
	}

	/**
	 * Find the authentication token for a particular username
	 * @param username the username to authenticate
	 * @return the token for that username
	 */
	private String getAuth(String username) {
		String auth = "";
		SecretKeySpec key = new SecretKeySpec(secret.getBytes(), "SHA");
		try {
			Mac mac = Mac.getInstance("HmacSHA1");
			mac.init(key);
			mac.update(username.getBytes());
			byte[] result = (mac.doFinal());
			
			String charmap = "0123456789abcdef";
			for (int i = 0; i < result.length; i++) {
				int first = (result[i] >> 4) & 0x0f;
				int second = result[i] & 0x0f;
				auth += (charmap.charAt(first));
				auth += (charmap.charAt(second));
			}
		} catch (Exception nsae) {
			nsae.printStackTrace();
		}
		LOGGER.fine( "auth: " + auth);
		return auth;
	}
}
