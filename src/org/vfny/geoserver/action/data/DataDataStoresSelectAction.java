
/* Copyright (c) 2004 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.action.data;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;
import org.vfny.geoserver.action.ConfigAction;
import org.vfny.geoserver.config.ConfigRequests;
import org.vfny.geoserver.config.DataConfig;
import org.vfny.geoserver.config.DataStoreConfig;
import org.vfny.geoserver.form.data.DataDataStoresSelectForm;
import java.io.IOException;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Select a DataStore for editing.
 *
 * @author User, Refractions Research, Inc.
 * @author $Author: jive $ (last modification)
 * @version $Id: DataDataStoresSelectAction.java,v 1.5 2004/02/02 15:06:18 jive Exp $
 */
public class DataDataStoresSelectAction extends ConfigAction {
    public ActionForward execute(ActionMapping mapping,
        ActionForm incomingForm, HttpServletRequest request,
        HttpServletResponse response) throws IOException, ServletException {

        DataDataStoresSelectForm form = (DataDataStoresSelectForm) incomingForm;

        String buttonAction = form.getButtonAction();

        DataConfig dataConfig = (DataConfig) getDataConfig();
        DataStoreConfig dsConfig = null;
        
        Locale locale = (Locale) request.getLocale();
        MessageResources messages = servlet.getResources();
        
        String editLabel = messages.getMessage(locale, "label.edit");
        String deleteLabel = messages.getMessage(locale, "label.delete");
        
        if (editLabel.equals(buttonAction)
        	|| editLabel.equalsIgnoreCase( buttonAction ) // HACK: temp hack!
        	) {
            dsConfig = (DataStoreConfig) dataConfig.getDataStore(form
                    .getSelectedDataStoreId());

            //This would be ideal. Perhaps later. Session for now.
            //getUserContainer(request).setDataStoreID(form.getSelectedDataStoreId());
            request.getSession().setAttribute("selectedDataStoreId",
                form.getSelectedDataStoreId());

            return mapping.findForward("dataConfigDataStores");
        } else if (deleteLabel.equals(buttonAction)
        		|| deleteLabel.equalsIgnoreCase( buttonAction ) // HACK: temp hack!
        	) {
            dataConfig.removeDataStore(form.getSelectedDataStoreId());
            request.getSession().removeAttribute("selectedDataStoreId");

            form.reset(mapping, request);

            return mapping.findForward("dataConfigDataStores");
        }
        
        throw new ServletException(
            "Action '"+buttonAction+"'must be '"+editLabel+"' or '"+deleteLabel+"'");
    }
}
