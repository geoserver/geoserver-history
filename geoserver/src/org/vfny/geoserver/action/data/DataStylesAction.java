/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */

package org.vfny.geoserver.action.data;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;
import org.vfny.geoserver.action.ConfigAction;
import org.vfny.geoserver.action.HTMLEncoder;
import org.vfny.geoserver.config.DataConfig;
import org.vfny.geoserver.config.StyleConfig;
import org.vfny.geoserver.form.data.DataStylesForm;
import org.vfny.geoserver.global.UserContainer;

import java.io.File;
import java.io.IOException;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * DOCUMENT ME!
 *
 * @author rgould To change the template for this generated type comment go to
 *         Window>Preferences>Java>Code Generation>Code and Comments
 */
public class DataStylesAction extends ConfigAction {
    public ActionForward execute(ActionMapping mapping, ActionForm form,
        UserContainer user, HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException {
        DataStylesForm stylesForm = (DataStylesForm) form;

        String action = stylesForm.getAction();

        String filename = stylesForm.getFilename();
        String styleID = stylesForm.getStyleID();

        boolean _default = stylesForm.is_default();

        if (stylesForm.isDefaultChecked()) {
            _default = false;
        }

        DataConfig dataConfig = (DataConfig) getDataConfig();
        StyleConfig config = null;
        
        
        Locale locale = (Locale) request.getLocale();
        MessageResources messages = servlet.getResources();
        String edit = HTMLEncoder.decode(messages.getMessage(locale, "label.edit"));
        String delete = HTMLEncoder.decode(messages.getMessage(locale, "label.delete"));
        String _new = HTMLEncoder.decode(messages.getMessage(locale, "label.new"));
        String submit = HTMLEncoder.decode(messages.getMessage(locale, "label.submit"));        

        if (action.equals(edit) || action.equals(submit)) {
            config = (StyleConfig) dataConfig.getStyle(stylesForm
                    .getSelectedStyle());
        } else if (action.equals(_new)) {
            config = new StyleConfig();
        }

        //If they push edit, simply forward them back so the information is repopulated.
        if (action.equals(edit)) {
            stylesForm.reset(mapping, request);

            return mapping.findForward("dataConfigStyles");
        }

        if (action.equals(delete)) {
            dataConfig.removeStyle(stylesForm.getSelectedStyle());
        } else {
            config.setFilename(new File(filename));
            config.setDefault(_default);
            config.setId(styleID);

            //Do configuration parameters here.
            dataConfig.addStyle(styleID, config);
        }

        stylesForm.reset(mapping, request);

        return mapping.findForward("dataConfigStyles");
    }
}
