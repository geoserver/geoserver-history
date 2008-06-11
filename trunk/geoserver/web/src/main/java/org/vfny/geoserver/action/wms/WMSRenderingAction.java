/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.action.wms;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.vfny.geoserver.action.ConfigAction;
import org.vfny.geoserver.config.WMSConfig;
import org.vfny.geoserver.form.wms.WMSRenderingForm;
import org.vfny.geoserver.global.UserContainer;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class WMSRenderingAction extends ConfigAction {
    public ActionForward execute(ActionMapping mapping, ActionForm form, UserContainer user,
        HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException {
        WMSConfig config = getWMSConfig();
        WMSRenderingForm renderingForm = (WMSRenderingForm) form;

        boolean svgAntiAlias = renderingForm.getSvgAntiAlias();
        boolean globalWatermarking = renderingForm.getGlobalWatermarking();

        if (renderingForm.isSvgAntiAliasChecked() == false) {
            svgAntiAlias = false;
        }

        if (renderingForm.isGlobalWatermarkingChecked() == false) {
            globalWatermarking = false;
        }

        config.setSvgRenderer(renderingForm.getSvgRenderer());
        config.setSvgAntiAlias(svgAntiAlias);
        config.setGlobalWatermarking(globalWatermarking);
        config.setGlobalWatermarkingURL(renderingForm.getGlobalWatermarkingURL());
        config.setWatermarkTransparency(renderingForm.getWatermarkTransparency());
        config.setWatermarkPosition(renderingForm.getWatermarkPosition());
        config.setAllowInterpolation(renderingForm.getAllowInterpolation());
        getApplicationState().notifyConfigChanged();

        return mapping.findForward("config");
    }
}
