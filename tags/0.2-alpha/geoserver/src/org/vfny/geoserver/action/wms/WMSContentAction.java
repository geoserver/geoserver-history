/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */

package org.vfny.geoserver.action.wms;

import java.io.IOException;
import java.net.URL;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.vfny.geoserver.action.ConfigAction;
import org.vfny.geoserver.config.WMSConfig;
import org.vfny.geoserver.form.wms.WMSContentForm;
import org.vfny.geoserver.global.UserContainer;


/**
 * DOCUMENT ME!
 *
 * @author rgould To change the template for this generated type comment go to
 *         Window>Preferences>Java>Code Generation>Code and Comments
 */
public final class WMSContentAction extends ConfigAction {
    public ActionForward execute(ActionMapping mapping, ActionForm form,
        UserContainer user, HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException {
        WMSContentForm contentForm = (WMSContentForm) form;

        boolean enabled = contentForm.isEnabled();

        if (contentForm.isEnabledChecked() == false) {
            enabled = false;
        }

        String onlineResource = contentForm.getOnlineResource();

        WMSConfig config = getWMSConfig();

        config.setEnabled(enabled);
        config.setOnlineResource(new URL(onlineResource));

        getApplicationState().notifyConfigChanged();

        return mapping.findForward("config");
    }
}
