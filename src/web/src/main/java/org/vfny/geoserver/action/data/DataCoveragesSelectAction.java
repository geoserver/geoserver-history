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
import org.vfny.geoserver.config.DataStoreConfig;
import org.vfny.geoserver.config.FeatureTypeConfig;
import org.vfny.geoserver.config.NameSpaceConfig;
import org.vfny.geoserver.config.WMSConfig;
import org.vfny.geoserver.form.data.DataCoveragesSelectForm;
import org.vfny.geoserver.global.UserContainer;
import java.io.IOException;
import java.util.Iterator;
import java.util.Locale;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * DOCUMENT ME!
 *
 * @author $Author: Alessio Fabiani (alessio.fabiani@gmail.com) $ (last
 *         modification)
 * @author $Author: Simone Giannecchini (simboss1@gmail.com) $ (last
 *         modification)
 */
public class DataCoveragesSelectAction extends ConfigAction {
    public ActionForward execute(ActionMapping mapping, ActionForm incomingForm,
        UserContainer user, HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException {
        DataCoveragesSelectForm form = (DataCoveragesSelectForm) incomingForm;

        String selectedCoverage = form.getSelectedCoverageName();
        String buttonAction = form.getButtonAction();

        DataConfig dataConfig = (DataConfig) getServlet().getServletContext()
                                                 .getAttribute(DataConfig.CONFIG_KEY);

        CoverageConfig cvConfig = dataConfig.getCoverageConfig(selectedCoverage);

        Locale locale = (Locale) request.getLocale();
        MessageResources messages = getResources(request);
        String edit = HTMLEncoder.decode(messages.getMessage(locale, "label.edit"));
        String delete = HTMLEncoder.decode(messages.getMessage(locale, "label.delete"));

        if (edit.equals(buttonAction)) {
            request.getSession().setAttribute(DataConfig.SELECTED_COVERAGE, cvConfig);
            user.setCoverageConfig(cvConfig);

            return mapping.findForward("config.data.coverage.editor");
        } else if (delete.equals(buttonAction)) {
            String group = coverageInGroup(dataConfig, selectedCoverage); 
            if(group != null) {
                ActionErrors errors = new ActionErrors();
                errors.add(ActionErrors.GLOBAL_ERROR,
                    new ActionError("error.delete.layer.in.group", selectedCoverage, group));
                saveErrors(request, errors);
            } else {
                dataConfig.removeCoverage(selectedCoverage);
                request.getSession().removeAttribute(DataConfig.SELECTED_COVERAGE);
                getApplicationState().notifyConfigChanged();
            }

            return mapping.findForward("config.data.coverage");
        }

        throw new ServletException(
            "Action must be a MessageResource key value of either 'label.edit' or 'label.delete'");
    }
    
    private String coverageInGroup(DataConfig dataConfig, String selectedCoverage) {
        CoverageConfig cv = dataConfig.getCoverageConfig(selectedCoverage);
        String name = cv.getName();
        CoverageStoreConfig cs = dataConfig.getDataFormat(cv.getFormatId());
        NameSpaceConfig ns = dataConfig.getNameSpace(cs.getNameSpaceId());
        String qualifiedName = ns.getPrefix() + ":" + name;
        WMSConfig config = getWMSConfig();
        for (Iterator it = config.getBaseMapLayers().keySet().iterator(); it.hasNext();) {
            String group = (String) it.next();
            String layers = (String) config.getBaseMapLayers().get(group);
            String[] layerNames = layers.split(",");
            for (int i = 0; i < layerNames.length; i++) {
                if(layerNames[i].equals(name) || layerNames[i].equals(qualifiedName))
                    return group;
            }
        } 
        return null;
    }
}
