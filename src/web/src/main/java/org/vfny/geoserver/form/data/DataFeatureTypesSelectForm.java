/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.form.data;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.vfny.geoserver.config.DataConfig;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;


/**
 * DOCUMENT ME!
 *
 * @author User To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Generation - Code and Comments
 */
public class DataFeatureTypesSelectForm extends ActionForm {
    private String selectedFeatureTypeName;
    private String buttonAction;

    public void reset(ActionMapping arg0, HttpServletRequest arg1) {
        super.reset(arg0, arg1);
        selectedFeatureTypeName = "";
    }

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();

        return errors;
    }

    /**
     * DOCUMENT ME!
     *
     * @return Returns the selectedFeatureTypeName.
     */
    public String getSelectedFeatureTypeName() {
        return selectedFeatureTypeName;
    }

    /**
     * DOCUMENT ME!
     *
     * @param selectedFeatureTypeName The selectedFeatureTypeName to set.
     */
    public void setSelectedFeatureTypeName(String selectedFeatureTypeName) {
        this.selectedFeatureTypeName = selectedFeatureTypeName;
    }

    public List getTypeNames() {
        ServletContext context = getServlet().getServletContext();
        DataConfig config = (DataConfig) context.getAttribute(DataConfig.CONFIG_KEY);

        ArrayList ret = new ArrayList(config.getFeaturesTypes().keySet());
        Object[] sortable = ret.toArray();
        Arrays.sort(sortable);

        return Arrays.asList(sortable);
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
