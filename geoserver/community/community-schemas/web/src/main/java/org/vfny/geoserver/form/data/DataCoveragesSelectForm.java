/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.form.data;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.vfny.geoserver.config.DataConfig;
import java.util.Set;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;


/**
 * DOCUMENT ME!
 *
 * @author $Author: Alessio Fabiani (alessio.fabiani@gmail.com) $ (last modification)
 * @author $Author: Simone Giannecchini (simboss1@gmail.com) $ (last modification)
 */
public class DataCoveragesSelectForm extends ActionForm {
    /**
     *
     */
    private static final long serialVersionUID = 1300704188707189533L;

    /**
     *
     */
    private String selectedCoverageName;

    /**
     *
     */
    private String buttonAction;

    public void reset(ActionMapping arg0, HttpServletRequest arg1) {
        super.reset(arg0, arg1);
        selectedCoverageName = "";
    }

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();

        return errors;
    }

    /**
     * DOCUMENT ME!
     *
     * @return Returns the selectedCoverageName.
     */
    public String getSelectedCoverageName() {
        return selectedCoverageName;
    }

    /**
     * DOCUMENT ME!
     *
     * @param selectedCoverageName The selectedCoverageName to set.
     */
    public void setSelectedCoverageName(String selectedCoverageName) {
        this.selectedCoverageName = selectedCoverageName;
    }

    public Set getCoverageNames() {
        ServletContext context = getServlet().getServletContext();
        DataConfig config = (DataConfig) context.getAttribute(DataConfig.CONFIG_KEY);

        return config.getCoverages().keySet();
    }

    /**
     * Access buttonAction property.
     *
     * @return Returns the buttonAction.
     */
    public String getButtonAction() {
        return buttonAction;
    }

    /**
     * Set buttonAction to buttonAction.
     *
     * @param buttonAction The buttonAction to set.
     */
    public void setButtonAction(String buttonAction) {
        this.buttonAction = buttonAction;
    }
}
