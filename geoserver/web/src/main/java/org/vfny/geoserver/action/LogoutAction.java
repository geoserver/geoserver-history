/*
 * Created on Jan 28, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package org.vfny.geoserver.action;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Remove UserContainter from session (and reset session) and return to welcome.
 * <p>
 * This is a propert ConfigAction - you need to be logged in for this to work.
 * </p>
 * @author rgould, Refractions Research, Inc.
 * @author $Author: dmzwiers $ (last modification)
 * @version $Id$
 */
public class LogoutAction extends GeoServerAction {
    public ActionForward execute(ActionMapping mapping, ActionForm form,
        HttpServletRequest request, HttpServletResponse response) {
        // remove UserContainer from Session
        //        logOut(request);

        //if we don't invalidate their session, we can save other variables, such as locale

        // return back to the welcome screen
        // (this is actually (for once) the correct place to go
        //
        return mapping.findForward("welcome");
    }
}
