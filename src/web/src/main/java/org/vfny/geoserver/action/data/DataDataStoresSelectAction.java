/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.action.data;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;
import org.vfny.geoserver.action.ConfigAction;
import org.vfny.geoserver.config.CoverageConfig;
import org.vfny.geoserver.config.DataConfig;
import org.vfny.geoserver.config.DataStoreConfig;
import org.vfny.geoserver.config.FeatureTypeConfig;
import org.vfny.geoserver.form.data.DataDataStoresSelectForm;
import org.vfny.geoserver.global.UserContainer;
import java.io.IOException;
import java.util.Iterator;
import java.util.Locale;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Select a DataStore for editing.
 *
 * @author User, Refractions Research, Inc.
 * @author $Author: dmzwiers $ (last modification)
 * @version $Id$
 */
public class DataDataStoresSelectAction extends ConfigAction {
    public ActionForward execute(ActionMapping mapping, ActionForm incomingForm,
        UserContainer user, HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException {
        DataDataStoresSelectForm form = (DataDataStoresSelectForm) incomingForm;

        String buttonAction = form.getButtonAction();

        DataConfig dataConfig = (DataConfig) getDataConfig();
        DataStoreConfig dsConfig = null;

        Locale locale = (Locale) request.getLocale();

        //MessageResources messages = servlet.getResources();
        MessageResources messages = getResources(request);

        String editLabel = messages.getMessage(locale, "label.edit");
        String deleteLabel = messages.getMessage(locale, "label.delete");

        String selectedDataStore = form.getSelectedDataStoreId();
        if (editLabel.equals(buttonAction)) {
            dsConfig = (DataStoreConfig) dataConfig.getDataStore(selectedDataStore);

            getUserContainer(request).setDataStoreConfig(dsConfig);

            return mapping.findForward("config.data.store.editor");
        } else if (deleteLabel.equals(buttonAction)) {
            int count = countFeatureTypesUsingStore(dataConfig, selectedDataStore);
            if(count > 0) {
                ActionErrors errors = new ActionErrors();
                errors.add(ActionErrors.GLOBAL_ERROR,
                    new ActionError("error.delete.datastore", selectedDataStore, count));
                saveErrors(request, errors);
            } else {
                dataConfig.removeDataStore(selectedDataStore);
                getUserContainer(request).setDataStoreConfig(null);
            }

            form.reset(mapping, request);

            return mapping.findForward("config.data.store");
        }

        throw new ServletException("Action '" + buttonAction + "'must be '" + editLabel + "' or '"
            + deleteLabel + "'");
    }
    
    private int countFeatureTypesUsingStore(DataConfig dataConfig, String selectedDataStore) {
        int count = 0;
        for (Iterator it = dataConfig.getFeaturesTypes().values().iterator(); it.hasNext();) {
            FeatureTypeConfig ft = (FeatureTypeConfig) it.next();
            if(selectedDataStore.equals(ft.getDataStoreId()))
                count++;
        }
        return count;
    }
}
