/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */

package org.vfny.geoserver.action.wfs;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.vfny.geoserver.action.ConfigAction;
import org.vfny.geoserver.config.WFSConfig;
import org.vfny.geoserver.form.wfs.WFSDescriptionForm;
import org.vfny.geoserver.global.UserContainer;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * DOCUMENT ME!
 *
 * @author rgould To change the template for this generated type comment go to
 *         Window>Preferences>Java>Code Generation>Code and Comments
 */
public final class WFSDescriptionAction extends ConfigAction {
    public ActionForward execute(ActionMapping mapping, ActionForm form,
        UserContainer user, HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException {
        WFSDescriptionForm descriptionForm = (WFSDescriptionForm) form;

        String name = descriptionForm.getName();
        String title = descriptionForm.getTitle();
        String accessConstraints = descriptionForm.getAccessConstraints();
        String fees = descriptionForm.getFees();
        String maintainer = descriptionForm.getMaintainer();
        String keywords = descriptionForm.getKeywords();
        String _abstract = descriptionForm.get_abstract();

        WFSConfig config = getWFSConfig();
        config.setName(name);
        config.setTitle(title);
        config.setAccessConstraints(accessConstraints);
        config.setFees(fees);
        config.setMaintainer(maintainer);
        config.setAbstract(_abstract);

        String[] array = (keywords != null)
            ? keywords.split(System.getProperty("line.separator")) : new String[0];

        config.setKeywords(array);

        return mapping.findForward("wfsConfigDescription");
    }
}
