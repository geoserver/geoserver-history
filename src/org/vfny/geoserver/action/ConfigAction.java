/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.vfny.geoserver.config.DataConfig;
import org.vfny.geoserver.config.GlobalConfig;
import org.vfny.geoserver.config.WFSConfig;
import org.vfny.geoserver.config.WMSConfig;
import org.vfny.geoserver.global.UserContainer;


/**
 * GeoConfigAction is a common super class used by STRUTS Actions.
 * 
 * <p>
 * ConfigAction is used to store shared services, such as looking up the
 * Configuration Model.
 * </p>
 * Capabilities:
 * 
 * <ul>
 * <li>
 * Config (Model) Access: Convience routines have been writen to allow access
 * to the Config Model from the Web Container.
 * </li>
 * <li>Since configuraiton should only be attempted when logged in, an effort has been made to smooth the
 * required login check required by most geoserver config actions.
 * </li>
 * </ul>
 * <p>
 * Most config actions require the follow check to be made:
 * <pre><code>
 * <b>class</b> MyConfigAction <b>extends</b> ConfigAction {
 *   Redirect execute( HttpServletRequest request, ){
 *     <b>if</b>( !isLoggedIn( request )){
 *       return new Redirect(�Login Page�);
 *     }
 *     UserContainer user = getUserContainer( request );
 *     �
 *     <b>return new</b> Redirect(�my.jsp�);
 *   }
 * }
 * </code></pre>
 * </p>
 * <p>
 * To prevent the duplication of the above code in each and every config
 * action pleaes make use of the alternate execute method:
 * <pre><code>
 * <b>class</b> MyConfigAction <b>extends</b> ConfigAction {
 *   Redirect execute( UserContainer user, HttpServletRequest request ){
 *     �
 *     <b>return new</b> Redirect(�my.jsp�);
 *   }
 * }
 * </code></pre>
 * </p>
 * Please remember that Actions (like servlets) should never make use of
 * instance variables in order to remain thread-safe.
 * </p>
 *
 * @author Jody Garnett, Refractions Research, Inc.
 * @author $Author: jive $ (last modification)
 * @version $Id: ConfigAction.java,v 1.4 2004/02/02 08:56:44 jive Exp $
 */
public class ConfigAction extends GeoServerAction {
	/**
	 * Execute method that redirects user if not loggin in.
	 * <p>
	 * The UserContainer is gathered from the session context using the
	 * GeoServerAction.getUserContainer( request method ).
	 * </p>
	 * @see org.apache.struts.action.Action#execute(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public ActionForward execute(ActionMapping mapping,
			                     ActionForm form,
			                     HttpServletRequest request,
								 HttpServletResponse response) throws Exception {
		if( !isLoggedIn( request )){
			return mapping.findForward("login");
		}
		return execute( mapping, form, getUserContainer( request ), request, response );
	}
	/**
	 * A "safe" execute method, only called after the user has logged in.
     * <p>
     * You may still override the normal execute method if you do not require
     * this service. 
     * </p>
	 */
	ActionForward execute( ActionMapping mapping,
			               ActionForm form,
						   UserContainer user,
						   HttpServletRequest request,
						   HttpServletResponse response ) throws Exception {
		return null;
	}
    /**
     * Access Web Map Server Configuration Model from the WebContainer.
     * 
     * <p>
     * Note that this represents the Configuration and not the state of the Web
     * Feature Server.
     * </p>
     *
     * @return Configuration information for the Web Map Server
     */
    protected WMSConfig getWMSConfig() {
        return (WMSConfig) getServlet().getServletContext().getAttribute(WMSConfig.CONFIG_KEY);
    }

    /**
     * Access Web Feature Server Configuration Model from the WebContainer.
     * 
     * <p>
     * Note that this represents the Configuration and not the state of the Web
     * Feature Server.
     * </p>
     *
     * @return Configuration information for Web Feature Server
     */
    protected WFSConfig getWFSConfig() {
        return (WFSConfig) getServlet().getServletContext().getAttribute(WFSConfig.CONFIG_KEY);
    }
    /**
     * Access Web Map Server Configuration Model from the WebContainer.
     *
     * @return Configuration model for Global information.
     */
    protected GlobalConfig getGlobalConfig() {
        return (GlobalConfig) getServlet().getServletContext().getAttribute(GlobalConfig.CONFIG_KEY);
    }

    /**
     * Access Catalog Configuration Model from the WebContainer.
     *
     * @return Configuration model for Catalog information.
     */
    protected DataConfig getDataConfig() {
        return (DataConfig) getServlet().getServletContext().getAttribute(DataConfig.CONFIG_KEY);
    }
}
