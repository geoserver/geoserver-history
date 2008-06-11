/*
 * Created on Jan 28, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package org.vfny.geoserver.action;

import com.sun.media.jai.util.SunTileCache;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.vfny.geoserver.global.GeoServer;
import org.vfny.geoserver.global.UserContainer;
import javax.media.jai.JAI;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


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
    public ActionForward execute(ActionMapping mapping, ActionForm form, UserContainer user,
        HttpServletRequest request, HttpServletResponse response)
        throws Exception {
        ServletContext sc = request.getSession().getServletContext();

        final JAI jaiDef = ((GeoServer) sc.getAttribute(GeoServer.WEB_CONTAINER_KEY)).getJAIDefault();
        final SunTileCache jaiCache = ((GeoServer) sc.getAttribute(GeoServer.WEB_CONTAINER_KEY))
            .getJaiCache();
        final long memCapacity = jaiCache.getMemoryCapacity() / 1024;
        final long memUsed = jaiCache.getCacheMemoryUsed() / 1024;
        final float memThreshold = jaiCache.getMemoryThreshold() * 100;
        final int numThreads = jaiDef.getTileScheduler().getParallelism();
        final int priiority = jaiDef.getTileScheduler().getPriority();

        request.setAttribute("JAI_MEM_CAPACITY", new Long(memCapacity));
        request.setAttribute("JAI_MEM_USED", new Long(memUsed));
        request.setAttribute("JAI_MEM_THRESHOLD", new Float(memThreshold));
        request.setAttribute("JAI_TILE_THREADS", new Integer(numThreads));
        request.setAttribute("JAI_TILE_PRIORITY", new Integer(priiority));

        // return back to the admin screen
        //
        return mapping.findForward("admin.main");
    }
}
