/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */

package org.vfny.geoserver.action.data;

import java.io.File;
import java.io.IOException;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.Globals;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;
import org.vfny.geoserver.action.ConfigAction;
import org.vfny.geoserver.action.HTMLEncoder;
import org.vfny.geoserver.config.DataConfig;
import org.vfny.geoserver.config.StyleConfig;
import org.vfny.geoserver.form.data.StylesEditorForm;
import org.vfny.geoserver.form.data.StylesSelectForm;
import org.vfny.geoserver.global.UserContainer;
import org.vfny.geoserver.requests.Requests;


/**
 * Edit selected style
 * 
 * @author jgarnett, Refractions Research, Inc.
 * @author $Author: jive $ (last modification)
 * @version $Id: StylesSelect.java,v 1.1 2004/02/28 07:45:13 jive Exp $
 */
public class StylesSelect extends ConfigAction {
    public ActionForward execute(ActionMapping mapping, ActionForm form,
        UserContainer user, HttpServletRequest request, HttpServletResponse response)    
        throws IOException, ServletException {
        
        final StylesSelectForm selectForm = (StylesSelectForm) form;
        final String action = selectForm.getAction();
        final String styleId = selectForm.getSelectedStyle();
        Locale locale = (Locale) request.getLocale();
        
        DataConfig config = getDataConfig();
        MessageResources messages = servlet.getResources();
        
        // Need locale wording for edit and delete
        final String EDIT = HTMLEncoder.decode(messages.getMessage(locale, "label.edit"));
        final String DELETE = HTMLEncoder.decode(messages.getMessage(locale, "label.delete"));
        
        StyleConfig style = config.getStyle( styleId );
        if( style == null ){
            ActionErrors errors = new ActionErrors();
            errors.add("selectedStyle",
                new ActionError("error.style.invalid", styleId ));            
            request.setAttribute(Globals.ERROR_KEY, errors);            
            return mapping.findForward("config.data.style");
        }
        // Something is selected lets do the requested action
        //
        if (action.equals(DELETE)) {
            config.removeStyle( styleId ); 
            getApplicationState().notifyConfigChanged();
            return mapping.findForward("config.data.style");
        }
        if( action.equals(EDIT)){
            user.setStyle( new StyleConfig( style ) );
            return mapping.findForward("config.data.style.edit");            
        }
        return mapping.findForward("config.data.style");
    }
}
