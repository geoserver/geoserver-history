/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */

/*
 * Created on Jan 8, 2004
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package org.vfny.geoserver.form.data;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.vfny.geoserver.config.ConfigRequests;
import org.vfny.geoserver.config.DataConfig;
import org.vfny.geoserver.config.StyleConfig;
import org.vfny.geoserver.global.UserContainer;
import org.vfny.geoserver.util.Requests;
import org.vfny.geoserver.util.RequestsLegacy;

import java.util.Iterator;
import java.util.TreeSet;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;


/**
 * Holds the current selection, and set of styles.
 * <p>
 * Current style selection is held in the UserContainer.
 * </p>
 * @author jgarnett, Refractions Research
 */
public class StylesSelectForm extends ActionForm {
    /** Selected style ID */
    private String selectedStyle;

    /** Action requested on selectedStyle */
    private String action;

    /** Sorted set of styles IDs */
    private TreeSet styles;

    public void reset(ActionMapping arg0, HttpServletRequest request) {
        super.reset(arg0, request);

        ServletContext context = getServlet().getServletContext();
        DataConfig config = ConfigRequests.getDataConfig(request);

        styles = new TreeSet();

        Iterator i = config.getStyles().values().iterator();
        boolean defaultSet = false;

        while (i.hasNext()) {
            StyleConfig sc = (StyleConfig) i.next();

            if (sc.isDefault()) {
                styles.add(sc.getId() + "*");
                defaultSet = true;
            } else {
                styles.add(sc.getId());
            }
        }

        StyleConfig sConfig;

        UserContainer user = RequestsLegacy.getUserContainer(request);
        selectedStyle = "";
    }

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();

        if ((selectedStyle == null) || selectedStyle.equals("")) {
            errors.add("selectedStyle", new ActionError("error.style.required", selectedStyle));
        }

        if (!styles.contains(selectedStyle)) {
            errors.add("selectedStyle", new ActionError("error.style.invalid", selectedStyle));
        }

        return errors;
    }

    /**
     * Access selectedStyle property.
     *
     * @return Returns the selectedStyle.
     */
    public String getSelectedStyle() {
        return selectedStyle;
    }

    /**
     * Set selectedStyle to selectedStyle.
     *
     * @param selectedStyle The selectedStyle to set.
     */
    public void setSelectedStyle(String selectedStyle) {
        this.selectedStyle = selectedStyle;
    }

    /**
     * Access action property.
     *
     * @return Returns the action.
     */
    public String getAction() {
        return action;
    }

    /**
     * Set action to action.
     *
     * @param action The action to set.
     */
    public void setAction(String action) {
        this.action = action;
    }

    /**
     * Access styles property.
     *
     * @return Returns the styles.
     */
    public TreeSet getStyles() {
        return styles;
    }
}
