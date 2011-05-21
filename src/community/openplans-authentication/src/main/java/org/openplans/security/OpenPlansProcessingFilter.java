/* Copyright 2004, 2005, 2006 Spring Technology Pty Limited
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.openplans.security;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.geoserver.filters.GeoServerFilter;
import org.springframework.security.Authentication;
import org.springframework.security.AuthenticationException;
import org.springframework.security.AuthenticationManager;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.security.providers.UsernamePasswordAuthenticationToken;
import org.springframework.security.ui.AuthenticationDetailsSource;
import org.springframework.security.ui.AuthenticationDetailsSourceImpl;
import org.springframework.security.ui.AuthenticationEntryPoint;
import org.springframework.security.ui.rememberme.RememberMeServices;

import org.springframework.beans.factory.InitializingBean;

import sun.misc.BASE64Decoder;


/**
 * A ProcessingFilter that extracts the username and password for a request from a cookie set by OpenPlans.org.  The 
 * OpenPlans site generates a special token and then base64-encodes the string username+'\0'+token in a cookie named 
 * __ac.
 * 
 * @author David Winslow - TOPP
 * @note This class is heavily based on the BasicProcessingFilter provided in the main Spring Security Security code.
 */
public class OpenPlansProcessingFilter implements GeoServerFilter, InitializingBean {
    //~ Static fields/initializers =====================================================================================

    static Logger LOGGER = org.geotools.util.logging.Logging.getLogger("org.geoserver.community");
    //~ Instance fields ================================================================================================

    private AuthenticationDetailsSource authenticationDetailsSource = new AuthenticationDetailsSourceImpl();
    private AuthenticationEntryPoint authenticationEntryPoint;
    private AuthenticationManager authenticationManager;
    private RememberMeServices rememberMeServices;
    private boolean ignoreFailure = true;

    //~ Methods ========================================================================================================

    public void afterPropertiesSet() throws Exception {
        // Assert.notNull(this.authenticationManager, "An AuthenticationManager is required");
        //Assert.notNull(this.authenticationEntryPoint, "An AuthenticationEntryPoint is required");
    }

    public void destroy() {}

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        if (!(request instanceof HttpServletRequest)) {
            throw new ServletException("Can only process HttpServletRequest");
        }

        if (!(response instanceof HttpServletResponse)) {
            throw new ServletException("Can only process HttpServletResponse");
        }

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

            String username = "";
            String password = "";
            
            String[] pair = getUserAndPassword(httpRequest);
        if (pair != null){
			LOGGER.info("Attempting to authenticate via OpenPlans cookie");
            username = pair[0];
            password = pair[1];

            if (authenticationIsRequired(username)) {
                UsernamePasswordAuthenticationToken authRequest =
                        new UsernamePasswordAuthenticationToken(username, password);
                authRequest.setDetails(authenticationDetailsSource.buildDetails((HttpServletRequest) request));

                Authentication authResult;

                try {
                    authResult = authenticationManager.authenticate(authRequest);
                } catch (AuthenticationException failed) {
                    // Authentication failed
                        LOGGER.info("Authentication request for user: " + username + " failed: " + failed.toString());

                    SecurityContextHolder.getContext().setAuthentication(null);

                    if (rememberMeServices != null) {
                        rememberMeServices.loginFail(httpRequest, httpResponse);
                    }

                    if (ignoreFailure) {
                        chain.doFilter(request, response);
                    } else {
                        authenticationEntryPoint.commence(request, response, failed);
                    }

                    return;
                }

                // Authentication success
                    LOGGER.info("Authentication success: " + authResult.toString());

                SecurityContextHolder.getContext().setAuthentication(authResult);
//                System.out.println(
//                		((UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal())
//                		.getUsername());
                if (rememberMeServices != null) {
//                    rememberMeServices.loginSuccess(httpRequest, httpResponse, authResult);
                }
            }
    	} else LOGGER.info("Not authenticating via OpenPlans cookie");

        chain.doFilter(request, response);
    }

    private boolean authenticationIsRequired(String username) {
        // Only reauthenticate if username doesn't match SecurityContextHolder and user isn't authenticated
        // (see SEC-53)
        Authentication existingAuth = SecurityContextHolder.getContext().getAuthentication();

        if(existingAuth == null || !existingAuth.isAuthenticated()) {
            return true;
        }

        // Limit username comparison to providers which use usernames (ie UsernamePasswordAuthenticationToken)
        // (see SEC-348)

        if (existingAuth instanceof UsernamePasswordAuthenticationToken && !existingAuth.getName().equals(username)) {
            return true;
        }

        return false;
    }

    public AuthenticationEntryPoint getAuthenticationEntryPoint() {
        return authenticationEntryPoint;
    }

    public AuthenticationManager getAuthenticationManager() {
        return authenticationManager;
    }

    public void init(FilterConfig arg0) throws ServletException {}

    public boolean isIgnoreFailure() {
        return ignoreFailure;
    }

    public void setAuthenticationDetailsSource(AuthenticationDetailsSource authenticationDetailsSource) {
        // Assert.notNull(authenticationDetailsSource, "AuthenticationDetailsSource required");
        this.authenticationDetailsSource = authenticationDetailsSource;
    }

    public void setAuthenticationEntryPoint(AuthenticationEntryPoint authenticationEntryPoint) {
        this.authenticationEntryPoint = authenticationEntryPoint;
    }

    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    public void setIgnoreFailure(boolean ignoreFailure) {
        this.ignoreFailure = ignoreFailure;
    }

    public void setRememberMeServices(RememberMeServices rememberMeServices) {
        this.rememberMeServices = rememberMeServices;
    }

    /**
     * Actually do the credential extraction from the cookie.
     * @param request the HttpServletRequest that contains the cookie
     * @return an array where the first element is the username and the second element is the authentication
     *  token if the cookie is found and of valid form, or null otherwise.
     */ 
	private String[] getUserAndPassword(HttpServletRequest request) {
		String cookie = null;
		String[] pair = null;
	
		try {
            for (int i = 0; i < request.getCookies().length; i++) {
//             System.out.println("Cookie: " + request.getCookies()[i].getName());
                if (request.getCookies()[i].getName().equals("__ac")) {
                    cookie = request.getCookies()[i].getValue();
//                  System.out.println("Found authentication cookie");
                }
            }

			byte[] decoded = (new BASE64Decoder()).decodeBuffer(cookie);
			
			int nullCharacterLocation = 0;
			
			while (nullCharacterLocation < decoded.length && decoded[nullCharacterLocation]!='\0'){
				nullCharacterLocation++;
			}
			
			pair = new String[]{
				new String(decoded, 0, nullCharacterLocation),
				new String(decoded, nullCharacterLocation+1, decoded.length - nullCharacterLocation-1)
			};
		} catch (Exception e) {
//             System.out.println(e);
			return null;
		}	
		
		return pair;
	}

}
