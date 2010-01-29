/* Copyright (c) 2001 - 2010 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.platform;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * A servlet filter that allows for advanced dispatching. 
 * <p>
 * This fiter allows for a single mapping from web.xml for all requests to the spring dispatcher. 
 * It creates a wrapper around the servlet request object that "fakes" the serveltPath property to 
 * make it look like the mapping was created in web.xml when in actuality it was created in spring.
 * </p>
 * @author Justin Deoliveira, OpenGeo
 *
 */
public class AdvancedDispatchFilter implements Filter {

    public void destroy() {
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        if (request instanceof HttpServletRequest) {
            request = new AdvancedDispatchHttpRequest((HttpServletRequest) request);
        }
        chain.doFilter(request, response);
    }

    public void init(FilterConfig filterConfig) throws ServletException {
    }
    
    static class AdvancedDispatchHttpRequest implements HttpServletRequest {
        
        HttpServletRequest delegate;
        String servletPath = null;
        
        public AdvancedDispatchHttpRequest(HttpServletRequest delegate) {
            this.delegate = delegate;
            
            if (delegate.getClass().getSimpleName().endsWith("MockHttpServletRequest")) {
                return;
            }
            
            String path = delegate.getPathInfo();
            int slash = path.indexOf('/', 1);
            if (slash > -1 ) {
                this.servletPath = path.substring(0, slash);
            }
            else {
                this.servletPath = path;
            }
            
            int question = this.servletPath.indexOf('?');
            if (question > -1 ) {
                this.servletPath = this.servletPath.substring(0, question);
            }
            
        }
        public Object getAttribute(String name) {
            return delegate.getAttribute(name);
        }

        public Enumeration getAttributeNames() {
            return delegate.getAttributeNames();
        }

        public String getAuthType() {
            return delegate.getAuthType();
        }

        public String getCharacterEncoding() {
            return delegate.getCharacterEncoding();
        }

        public int getContentLength() {
            return delegate.getContentLength();
        }

        public String getContentType() {
            return delegate.getContentType();
        }

        public String getContextPath() {
            return delegate.getContextPath();
        }

        public Cookie[] getCookies() {
            return delegate.getCookies();
        }

        public long getDateHeader(String name) {
            return delegate.getDateHeader(name);
        }

        public String getHeader(String name) {
            return delegate.getHeader(name);
        }

        public Enumeration getHeaderNames() {
            return delegate.getHeaderNames();
        }

        public Enumeration getHeaders(String name) {
            return delegate.getHeaders(name);
        }

        public ServletInputStream getInputStream() throws IOException {
            return delegate.getInputStream();
        }

        public int getIntHeader(String name) {
            return delegate.getIntHeader(name);
        }

        public String getLocalAddr() {
            return delegate.getLocalAddr();
        }

        public Locale getLocale() {
            return delegate.getLocale();
        }

        public Enumeration getLocales() {
            return delegate.getLocales();
        }

        public String getLocalName() {
            return delegate.getLocalName();
        }

        public int getLocalPort() {
            return delegate.getLocalPort();
        }

        public String getMethod() {
            return delegate.getMethod();
        }

        public String getParameter(String name) {
            return delegate.getParameter(name);
        }

        public Map getParameterMap() {
            return delegate.getParameterMap();
        }

        public Enumeration getParameterNames() {
            return delegate.getParameterNames();
        }

        public String[] getParameterValues(String name) {
            return delegate.getParameterValues(name);
        }

        public String getPathInfo() {
            return delegate.getPathInfo();
        }

        public String getPathTranslated() {
            return delegate.getPathTranslated();
        }

        public String getProtocol() {
            return delegate.getProtocol();
        }

        public String getQueryString() {
            return delegate.getQueryString();
        }

        public BufferedReader getReader() throws IOException {
            return delegate.getReader();
        }

        public String getRealPath(String path) {
            return delegate.getRealPath(path);
        }

        public String getRemoteAddr() {
            return delegate.getRemoteAddr();
        }

        public String getRemoteHost() {
            return delegate.getRemoteHost();
        }

        public int getRemotePort() {
            return delegate.getRemotePort();
        }

        public String getRemoteUser() {
            return delegate.getRemoteUser();
        }

        public RequestDispatcher getRequestDispatcher(String path) {
            return delegate.getRequestDispatcher(path);
        }

        public String getRequestedSessionId() {
            return delegate.getRequestedSessionId();
        }

        public String getRequestURI() {
            return delegate.getRequestURI();
        }

        public StringBuffer getRequestURL() {
            return delegate.getRequestURL();
        }

        public String getScheme() {
            return delegate.getScheme();
        }

        public String getServerName() {
            return delegate.getServerName();
        }

        public int getServerPort() {
            return delegate.getServerPort();
        }

        public String getServletPath() {
            return servletPath != null ? servletPath : delegate.getServletPath();
        }

        public HttpSession getSession() {
            return delegate.getSession();
        }

        public HttpSession getSession(boolean create) {
            return delegate.getSession(create);
        }

        public Principal getUserPrincipal() {
            return delegate.getUserPrincipal();
        }

        public boolean isRequestedSessionIdFromCookie() {
            return delegate.isRequestedSessionIdFromCookie();
        }

        public boolean isRequestedSessionIdFromUrl() {
            return delegate.isRequestedSessionIdFromUrl();
        }

        public boolean isRequestedSessionIdFromURL() {
            return delegate.isRequestedSessionIdFromURL();
        }

        public boolean isRequestedSessionIdValid() {
            return delegate.isRequestedSessionIdValid();
        }

        public boolean isSecure() {
            return delegate.isSecure();
        }

        public boolean isUserInRole(String role) {
            return delegate.isUserInRole(role);
        }

        public void removeAttribute(String name) {
            delegate.removeAttribute(name);
        }

        public void setAttribute(String name, Object o) {
            delegate.setAttribute(name, o);
        }

        public void setCharacterEncoding(String env) throws UnsupportedEncodingException {
            delegate.setCharacterEncoding(env);
        }
    }

}
