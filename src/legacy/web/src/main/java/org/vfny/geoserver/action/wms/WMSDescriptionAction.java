/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.action.wms;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.vfny.geoserver.action.ConfigAction;
import org.vfny.geoserver.config.WMSConfig;
import org.vfny.geoserver.form.wms.WMSDescriptionForm;
import org.vfny.geoserver.global.UserContainer;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * DOCUMENT ME!
 *
 * @author rgould To change the template for this generated type comment go to
 *         Window>Preferences>Java>Code Generation>Code and Comments
 */
public final class WMSDescriptionAction extends ConfigAction {
    public ActionForward execute(ActionMapping mapping, ActionForm form, UserContainer user,
        HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException {
        WMSDescriptionForm descriptionForm = (WMSDescriptionForm) form;

        String name = descriptionForm.getName();
        String title = descriptionForm.getTitle();
        String accessConstraints = descriptionForm.getAccessConstraints();
        String fees = descriptionForm.getFees();
        String maintainer = descriptionForm.getMaintainer();
        String keywords = descriptionForm.getKeywords();
        String _abstract = descriptionForm.get_abstract();

        WMSConfig config = getWMSConfig();
        config.setName(name);
        config.setTitle(title);
        config.setAccessConstraints(accessConstraints);
        config.setFees(fees);
        config.setMaintainer(maintainer);
        config.setAbstract(_abstract);

        List array = (keywords != null)
            ? Arrays.asList(keywords.split(System.getProperty("line.separator"))) : new ArrayList();

        config.setKeywords(array);
        getApplicationState().notifyConfigChanged();

        return mapping.findForward("config");
    }
}
