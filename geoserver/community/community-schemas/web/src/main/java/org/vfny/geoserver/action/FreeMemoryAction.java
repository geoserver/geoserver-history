/*
 * Created on Jan 28, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package org.vfny.geoserver.action;

import org.apache.struts.Globals;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.vfny.geoserver.global.UserContainer;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Free Memory by running the garbage collector.
 * <p>
 * This represents an action that interacts with the running GeoServer
 * application (it is not really a config action, it is just that I want the
 * user to be logged in).
 * </p>
 * @author Jody Garnett, Refractions Research, Inc.
 * @author $Author: jive $ (last modification)
 * @version $Id$
 */
public class FreeMemoryAction extends ConfigAction {
    /* (non-Javadoc)
     * @see org.vfny.geoserver.action.ConfigAction#execute(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, org.vfny.geoserver.global.UserContainer, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    public ActionForward execute(ActionMapping mapping, ActionForm form, UserContainer user,
        HttpServletRequest request, HttpServletResponse response)
        throws Exception {
        Runtime rt = Runtime.getRuntime();

        long before = rt.freeMemory();

        System.gc();
        System.runFinalization();

        long after = rt.freeMemory();
        long difference = (before - after) / 1024;

        // Provide status message
        //
        ActionErrors errors = new ActionErrors();
        errors.add(ActionErrors.GLOBAL_ERROR,
            new ActionError("message.memory", new Long(difference)));
        request.setAttribute(Globals.ERROR_KEY, errors);

        // return back to the admin screen
        //
        return mapping.findForward("admin");
    }
}
