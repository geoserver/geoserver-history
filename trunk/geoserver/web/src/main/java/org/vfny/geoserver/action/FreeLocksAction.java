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
import org.vfny.geoserver.global.Data;
import org.vfny.geoserver.global.UserContainer;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Free Locks held by each and every DataStore.
 * <p>
 * This represents an action that interacts with the running GeoServer
 * application (it is not really a config action, it is just that I want the
 * user to be logged in).
 * </p>
 * <p>
 * This class is waiting on changes to the DataStore api before it will
 * actually be useful.
 * </p>
 * @author Jody Garnett, Refractions Research, Inc.
 * @author $Author: jive $ (last modification)
 * @version $Id$
 */
public class FreeLocksAction extends ConfigAction {
    /* (non-Javadoc)
     * @see org.vfny.geoserver.action.ConfigAction#execute(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, org.vfny.geoserver.global.UserContainer, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    public ActionForward execute(ActionMapping mapping, ActionForm form, UserContainer user,
        HttpServletRequest request, HttpServletResponse response)
        throws Exception {
        // WFS is only used to aquire the GeoServer
        Data data = getWFS(request).getData();

        int count = data.lockReleaseAll();

        // Provide status message
        //
        ActionErrors errors = new ActionErrors();
        errors.add(ActionErrors.GLOBAL_ERROR,
            new ActionError("message.freeLocks", new Integer(count)));
        request.setAttribute(Globals.ERROR_KEY, errors);

        // return back to the admin screen
        //
        return mapping.findForward("admin");
    }
}
