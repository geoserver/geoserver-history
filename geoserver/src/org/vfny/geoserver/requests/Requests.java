/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.requests;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.vfny.geoserver.global.GeoServer;
import org.vfny.geoserver.global.UserContainer;

/**
 * Utility methods helpful when processing GeoServer Requests.
 * <p>
 * Provides helper functions and classes useful when implementing your own
 * Response classes. Of significant importantance are the Request processing
 * functions that allow access to the WebContainer, GeoServer and the User's Session.
 * </p>
 * <p>
 * If you are working with the STRUTS API the Action method is the direct
 * paralle of the Response classes. You may whish to look at how ConfigAction
 * is implemented, it is a super class which delegates to these Request
 * processing methods.
 * </p>
 * 
 * @author Jody Garnett
 */
public final class Requests {

	/**
	 * Aquire GeoServer from Web Container.  
	 * <p>
	 * In GeoServer is create by a STRUTS plug-in and is available through
	 * the Web container.
	 * </p>
	 * <p>
	 * Test cases may seed the request object with a Mock WebContainer and a 
	 * Mock GeoServer.
	 * </p>
	 * @param request GeoServer Request used to aquire session reference
	 * @return GeoServer instance for the current Web Application
	 */
	public static GeoServer getGeoServer( Request request ){
		return getGeoServer( request.getHttpServletRequest() );
	}
	
	/**
	 * Aquire GeoServer from Web Container.  
	 * <p>
	 * In GeoServer is create by a STRUTS plug-in and is available through
	 * the Web container.
	 * </p>
	 * <p>
	 * Test cases may seed the request object with a Mock WebContainer and a 
	 * Mock GeoServer.
	 * </p>
	 * @param request HttpServletRequest used to aquire servlet context
	 * @return GeoServer instance for the current Web Application
	 */
	public static GeoServer getGeoServer(HttpServletRequest request) {
		HttpSession session = request.getSession();
		ServletContext context = session.getServletContext();
		return (GeoServer) context.getAttribute( "GeoServer" );		
	}

	/**
	 * Aquire type safe session information in a UserContainer.  
	 * <p>
	 * Please note that the UserContainer may be lazyly created.
	 * </p>
	 * @param request GeoServer Request used to aquire session reference
	 * @return UserContainer containing typesafe session information.
	 */
	public static UserContainer getUserContainer( Request request ){
		return getUserContainer( request.getHttpServletRequest() );
	}
	
	/**
	 * Aquire type safe session information in a UserContainer.  
	 * <p>
	 * Please note that the UserContainer may be lazyly created.
	 * </p>
	 * 
	 * @param request Http Request used to aquire session reference
	 * @return UserContainer containing typesafe session information.
	 */
	public static UserContainer getUserContainer( HttpServletRequest request ){
		HttpSession session = request.getSession();
		
		synchronized( session ){
			UserContainer user = (UserContainer) session.getAttribute( UserContainer.SESSION_KEY );
			if( user == null ){
				user = new UserContainer( request.getLocale() );
				session.setAttribute( UserContainer.SESSION_KEY, user );
			}
			return user;                
		}
	}

	/**
	 * Tests is user is loggin in.
	 * <p>
	 * True if UserContainer exists has been created.
	 * </p>
	 * @param request HttpServletRequest providing current Session
	 * @return
	 */
	public static boolean isLoggedIn(HttpServletRequest request) {
		HttpSession session = request.getSession();
		synchronized( session ){
			UserContainer user = (UserContainer) session.getAttribute( UserContainer.SESSION_KEY );
			return user != null;
		}
	}
	/**
	 * Ensures a user is logged out.
	 * <p>
	 * Removes the UserContainer, and thus GeoServers knowledge of the current
	 * user attached to this Session.
	 * </p>
	 * @param request HttpServletRequest providing current Session
	 */
	public static void logOut( HttpServletRequest request ){
		HttpSession session = request.getSession();
		session.removeValue( UserContainer.SESSION_KEY );
	}
}
