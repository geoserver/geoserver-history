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
import org.vfny.geoserver.action.HTMLEncoder;
import org.vfny.geoserver.config.CoverageConfig;
import org.vfny.geoserver.config.CoverageStoreConfig;
import org.vfny.geoserver.config.DataConfig;
import org.vfny.geoserver.form.data.CoverageStoresSelectForm;
import org.vfny.geoserver.global.UserContainer;
import java.io.IOException;
import java.util.Iterator;
import java.util.Locale;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Select a Format for editing.
 *
 * @author User, Refractions Research, Inc.
 * @author dmzwiers
 * @author $Author: Alessio Fabiani (alessio.fabiani@gmail.com) $ (last
 *         modification)
 * @author $Author: Simone Giannecchini (simboss1@gmail.com) $ (last
 *         modification)
 * @version $Id: CoverageStoresSelectAction.java,v 1.12 2004/02/25 21:51:11
 *          dmzwiers Exp $
 */
public final class CoverageStoresSelectAction extends ConfigAction {
    public ActionForward execute(ActionMapping mapping, ActionForm incomingForm,
        UserContainer user, HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException {
        CoverageStoresSelectForm form = (CoverageStoresSelectForm) incomingForm;
        String buttonAction = form.getButtonAction();

        DataConfig dataConfig = (DataConfig) getDataConfig();
        CoverageStoreConfig dfConfig = null;
        Locale locale = (Locale) request.getLocale();
        MessageResources messages = getResources(request);

        String editLabel = HTMLEncoder.decode(messages.getMessage(locale, "label.edit"));
        String deleteLabel = HTMLEncoder.decode(messages.getMessage(locale, "label.delete"));

        String selectedDataFormat = form.getSelectedDataFormatId();
        if (editLabel.equals(buttonAction)) {
            dfConfig = (CoverageStoreConfig) dataConfig.getDataFormat(selectedDataFormat);

            getUserContainer(request).setDataFormatConfig(dfConfig);

            return mapping.findForward("config.data.format.editor");
        } else if (deleteLabel.equals(buttonAction)) {
            int count = countCoveragesUsingStore(dataConfig, selectedDataFormat);
            if(count > 0) {
                ActionErrors errors = new ActionErrors();
                errors.add(ActionErrors.GLOBAL_ERROR,
                    new ActionError("error.delete.coverage.dataset", selectedDataFormat));
                saveErrors(request, errors);
            } else {
                dataConfig.removeDataFormat(selectedDataFormat);
                getUserContainer(request).setDataFormatConfig(null);
            }

            form.reset(mapping, request);
            return mapping.findForward("config.data.format");
        }

        throw new ServletException(new StringBuffer("Action '").append(buttonAction)
                                                               .append("'must be '")
                                                               .append(editLabel).append("' or '")
                                                               .append(deleteLabel).append("'")
                                                               .toString());
    }

    private int countCoveragesUsingStore(DataConfig dataConfig, String selectedDataFormat) {
        int count = 0;
        for (Iterator it = dataConfig.getCoverages().values().iterator(); it.hasNext();) {
            CoverageConfig coverage = (CoverageConfig) it.next();
            if(selectedDataFormat.equals(coverage.getFormatId()))
                count++;
        }
        return count;
    }
}
