/* Copyright (c) 2004 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.action.data;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.vfny.geoserver.action.ConfigAction;
import org.vfny.geoserver.config.DataFormatConfig;
import org.vfny.geoserver.form.data.DataFormatsNewForm;
import org.vfny.geoserver.global.UserContainer;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Create a new DataFormatConfig based on user's input.
 * 
 * <p>
 * Will need to update the current DataFormatId as stored in session context.
 * </p>
 *
 * @author User, Refractions Research, Inc.
 * @author dmzwiers
 * @author $Author: Alessio Fabiani (alessio.fabiani@gmail.com) $ (last modification)
 * @author $Author: Simone Giannecchini (simboss_ml@tiscali.it) $ (last modification)
 * @version $Id: DataFormatsNewAction.java,v 1.12 2004/02/25 21:51:11 dmzwiers Exp $
 */
public class DataFormatsNewAction extends ConfigAction {
    public ActionForward execute(ActionMapping mapping, ActionForm form,
        UserContainer user, HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException {

        DataFormatsNewForm newForm = (DataFormatsNewForm) form;
        DataFormatConfig newFormatConfig;

        newFormatConfig = new DataFormatConfig(newForm.getDataFormatID(),
                newForm.getSelectedDescription());

        getUserContainer(request).setDataFormatConfig(newFormatConfig);

        return mapping.findForward("config.data.format.editor");
    }
}
