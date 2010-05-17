/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.action.wfs;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.vfny.geoserver.action.ConfigAction;
import org.vfny.geoserver.config.WFSConfig;
import org.vfny.geoserver.form.wfs.WFSContentForm;
import org.vfny.geoserver.global.UserContainer;
import java.io.IOException;
import java.net.URL;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * DOCUMENT ME!
 *
 * @author rgould To change the template for this generated type comment go to
 *         Window>Preferences>Java>Code Generation>Code and Comments
 */
public final class WFSContentAction extends ConfigAction {
    public ActionForward execute(ActionMapping mapping, ActionForm form, UserContainer user,
        HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException {
        WFSContentForm contentForm = (WFSContentForm) form;

        boolean enabled = contentForm.isEnabled();

        if (contentForm.isEnabledChecked() == false) {
            enabled = false;
        }

        boolean srsXmlStyle = contentForm.isSrsXmlStyle();

        if (contentForm.isSrsXmlStyleChecked() == false) {
            srsXmlStyle = false;
        }

        boolean citeConformanceHacks = contentForm.getCiteConformanceHacks();

        if (contentForm.getCiteConformanceHacksChecked() == false) {
            citeConformanceHacks = false; // deal with the way HTTP works.
        }

        boolean featureBounding = contentForm.isFeatureBounding();

        if (contentForm.isFeatureBoundingChecked() == false) {
            featureBounding = false;
        }

        String onlineResource = contentForm.getOnlineResource();
        String[] selectedFeatures = contentForm.getSelectedFeatures();
        String[] features = contentForm.getFeatures();

        WFSConfig config = getWFSConfig();
        config.setSrsXmlStyle(srsXmlStyle);
        config.setEnabled(enabled);
        config.setOnlineResource(new URL(onlineResource));
        config.setServiceLevel(contentForm.getServiceLevel());
        config.setCiteConformanceHacks(citeConformanceHacks);
        config.setFeatureBounding(featureBounding);
        getApplicationState().notifyConfigChanged();

        return mapping.findForward("config");
    }
}
