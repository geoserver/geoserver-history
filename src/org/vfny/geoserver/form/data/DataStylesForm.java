/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
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

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.vfny.geoserver.config.DataConfig;
import org.vfny.geoserver.config.StyleConfig;
import java.util.TreeSet;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;


/**
 * DOCUMENT ME!
 *
 * @author rgould To change the template for this generated type comment go to
 *         Window>Preferences>Java>Code Generation>Code and Comments
 */
public class DataStylesForm extends ActionForm {
    private String styleID;
    private boolean _default;
    private String filename;
    private String selectedStyle;
    private String action;
    private TreeSet styles;

    /*
     * Because of the way that STRUTS works, if the user does not check the default box,
     * or unchecks it, set_default() is never called, thus we must monitor set_default()
     * to see if it doesn't get called. This must be accessible, as ActionForms need to
     * know about it -- there is no way we can tell whether we are about to be passed to
     * an ActionForm or not.
     *
     * Probably a better way to do this, but I can't think of one.
     * -rgould
     */
    private boolean defaultChecked = false;
	private HttpServletRequest request;

    public void reset(ActionMapping arg0, HttpServletRequest request) {
        super.reset(arg0, request);
        this.request = request;

        defaultChecked = false;
        action = "";

        ServletContext context = getServlet().getServletContext();
        DataConfig config = (DataConfig) context.getAttribute(DataConfig.CONFIG_KEY);

        styles = new TreeSet(config.getStyles().keySet());

        StyleConfig sConfig;

        selectedStyle = (String) request.getSession().getAttribute("selectedStyle");

        sConfig = config.getStyle(selectedStyle);

        if (sConfig == null) {
            sConfig = config.getStyle((String) styles.first());
        }

        styleID = sConfig.getId();
        _default = sConfig.isDefault();
        filename = sConfig.getFilename().getPath();
    }

    public ActionErrors validate(ActionMapping mapping,
        HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();

        return errors;
    }

    /**
     * DOCUMENT ME!
     *
     * @return
     */
    public boolean is_default() {
        return _default;
    }

    /**
     * DOCUMENT ME!
     *
     * @return
     */
    public String getFilename() {
        return filename;
    }

    /**
     * DOCUMENT ME!
     *
     * @return
     */
    public String getStyleID() {
        return styleID;
    }

    /**
     * DOCUMENT ME!
     *
     * @param b
     */
    public void set_default(boolean b) {
        defaultChecked = true;
        _default = b;
    }

    /**
     * DOCUMENT ME!
     *
     * @param string
     */
    public void setFilename(String string) {
        filename = string;
    }

    /**
     * DOCUMENT ME!
     *
     * @param string
     */
    public void setStyleID(String string) {
        styleID = string;
    }

    /**
     * DOCUMENT ME!
     *
     * @return
     */
    public String getAction() {
        return action;
    }

    /**
     * DOCUMENT ME!
     *
     * @return
     */
    public boolean isDefaultChecked() {
        return defaultChecked;
    }

    /**
     * DOCUMENT ME!
     *
     * @return
     */
    public String getSelectedStyle() {
        return selectedStyle;
    }

    /**
     * DOCUMENT ME!
     *
     * @param string
     */
    public void setAction(String string) {
        action = string;
    }

    /**
     * DOCUMENT ME!
     *
     * @param b
     */
    public void setDefaultChecked(boolean b) {
        defaultChecked = b;
    }

    /**
     * DOCUMENT ME!
     *
     * @param string
     */
    public void setSelectedStyle(String string) {
        request.getSession().setAttribute("selectedStyle", string);
        selectedStyle = string;
    }

    /**
     * DOCUMENT ME!
     *
     * @return
     */
    public TreeSet getStyles() {
        return styles;
    }
}
