/* Copyright (c) 2004 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.action.data;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;
import org.vfny.geoserver.action.ConfigAction;
import org.vfny.geoserver.action.HTMLEncoder;
import org.vfny.geoserver.config.DataConfig;
import org.vfny.geoserver.config.DataFormatConfig;
import org.vfny.geoserver.form.data.DataFormatsSelectForm;
import org.vfny.geoserver.global.UserContainer;

/**
 * Select a Format for editing.
 *
 * @author User, Refractions Research, Inc.
 * @author dmzwiers
 * @author $Author: Alessio Fabiani (alessio.fabiani@gmail.com) $ (last modification)
 * @author $Author: Simone Giannecchini (simboss_ml@tiscali.it) $ (last modification)
 * @version $Id: DataFormatsSelectAction.java,v 1.12 2004/02/25 21:51:11 dmzwiers Exp $
 */
public class DataFormatsSelectAction extends ConfigAction {
    public ActionForward execute(ActionMapping mapping,
        ActionForm incomingForm, UserContainer user, HttpServletRequest request,
        HttpServletResponse response) throws IOException, ServletException {

        DataFormatsSelectForm form = (DataFormatsSelectForm) incomingForm;

        String buttonAction = form.getButtonAction();

        DataConfig dataConfig = (DataConfig) getDataConfig();
        DataFormatConfig dfConfig = null;
        
        Locale locale = (Locale) request.getLocale();
        MessageResources messages = servlet.getResources();
        
        String editLabel = HTMLEncoder.decode(messages.getMessage(locale, "label.edit"));
        String deleteLabel = HTMLEncoder.decode(messages.getMessage(locale, "label.delete"));
        
        if (editLabel.equals(buttonAction)) {
            dfConfig = (DataFormatConfig) dataConfig.getDataFormat(form
                    .getSelectedDataFormatId());
           
            getUserContainer(request).setDataFormatConfig(dfConfig);

            return mapping.findForward("config.data.format.editor");
        } else if (deleteLabel.equals(buttonAction)) {
            dataConfig.removeDataFormat(form.getSelectedDataFormatId());
            getUserContainer(request).setDataFormatConfig(null);

            form.reset(mapping, request);

            return mapping.findForward("config.data.format");
        }
        
        throw new ServletException(
            "Action '"+buttonAction+"'must be '"+editLabel+"' or '"+deleteLabel+"'");
    }
}