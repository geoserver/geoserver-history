/*
 * Created on Jan 28, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package org.vfny.geoserver.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * Remove UserContainter from session (and reset session) and return to welcome.
 * <p>
 * This is a propert ConfigAction - you need to be logged in for this to work.
 * </p>
 * @author rgould, Refractions Research, Inc.
 * @author $Author: jive $ (last modification)
 * @version $Id: LogoutAction.java,v 1.2 2004/02/02 08:56:44 jive Exp $
 */
public class LogoutAction extends GeoServerAction {
    public ActionForward execute(ActionMapping mapping,
    		                     ActionForm form,
    		                     //UserContainer user,								 
								 HttpServletRequest request,
								 HttpServletResponse response) {
    	// remove UserContainer from Session
    	logOut( request );
    	
    	// totally remove session
    	request.getSession().invalidate();
    	
    	// return back to the welcome screen
    	// (this is actually (for once) the correct place to go
    	//
        return mapping.findForward("welcome");
        
    }
}
