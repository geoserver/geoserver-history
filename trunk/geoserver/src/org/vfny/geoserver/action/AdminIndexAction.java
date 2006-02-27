/*
 * Created on Jan 28, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package org.vfny.geoserver.action;

import javax.media.jai.JAI;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.vfny.geoserver.global.UserContainer;

/**
 * Admin Index Action. 
 * <p>
 * DOCUMENT ME.
 * </p>
 * @author $Author: Alessio Fabiani $ (last modification)
 */
public class AdminIndexAction extends ConfigAction {
    /* (non-Javadoc)
     * @see org.vfny.geoserver.action.ConfigAction#execute(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, org.vfny.geoserver.global.UserContainer, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            UserContainer user, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

    	long memCapacity = JAI.getDefaultInstance().getTileCache().getMemoryCapacity()/ 1024;
    	float memThreshold = JAI.getDefaultInstance().getTileCache().getMemoryThreshold()* 100;
    	
    	request.setAttribute("JAI_MEM_CAPACITY", new Long(memCapacity));
    	request.setAttribute("JAI_MEM_THRESHOLD", new Float(memThreshold));
    	
    	// return back to the admin screen
    	//
        return mapping.findForward("admin.main");
    }
}