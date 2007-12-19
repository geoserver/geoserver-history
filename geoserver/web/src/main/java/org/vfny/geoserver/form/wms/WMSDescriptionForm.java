/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.form.wms;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.vfny.geoserver.config.WMSConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;


/**
 * DOCUMENT ME!
 *
 * @author rgould To change the template for this generated type comment go to
 *         Window>Preferences>Java>Code Generation>Code and Comments
 */
public final class WMSDescriptionForm extends ActionForm {
    private String name;
    private String title;
    private String accessConstraints;
    private String fees;
    private String maintainer;
    private String keywords;
    private String _abstract;

    public void reset(ActionMapping arg0, HttpServletRequest arg1) {
        super.reset(arg0, arg1);

        ServletContext context = getServlet().getServletContext();
        WMSConfig config = (WMSConfig) context.getAttribute(WMSConfig.CONFIG_KEY);

        this.name = config.getName();
        this._abstract = config.getAbstract();
        this.fees = config.getFees();
        this.maintainer = config.getMaintainer();
        this.title = config.getTitle();
        this.accessConstraints = config.getAccessConstraints();

        String out = "";

        for (int i = 0; i < config.getKeywords().size(); i++) {
            out = out + config.getKeywords().get(i) + System.getProperty("line.separator");
        }

        this.keywords = out;
    }

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();

        if ((name == null) || (name.length() == 0)) {
            errors.add("name", new ActionError("error.name.required"));
        }

        if ((title == null) || (title.length() == 0)) {
            errors.add("title", new ActionError("error.title.required"));
        }

        if ((fees == null) || (fees.length() == 0)) {
            errors.add("fees", new ActionError("error.fees.required"));
        }

        if ((accessConstraints == null) || (accessConstraints.length() == 0)) {
            errors.add("accessConstraints", new ActionError("error.accessConstraints.required"));
        }

        if ((maintainer == null) || (maintainer.length() == 0)) {
            errors.add("maintainer", new ActionError("error.maintainer.required"));
        }

        if ((_abstract == null) || (_abstract.length() == 0)) {
            errors.add("abstract", new ActionError("error.abstract.required"));
        }

        String[] array = (keywords != null) ? keywords.split("\n") : new String[0];

        if (array.length == 0) {
            errors.add("keywords", new ActionError("error.keywords.required"));
        }

        return errors;
    }

    /**
     * DOCUMENT ME!
     *
     * @return
     */
    public String get_abstract() {
        return _abstract;
    }

    /**
     * DOCUMENT ME!
     *
     * @return
     */
    public String getAccessConstraints() {
        return accessConstraints;
    }

    /**
     * DOCUMENT ME!
     *
     * @return
     */
    public String getFees() {
        return fees;
    }

    /**
     * DOCUMENT ME!
     *
     * @return
     */
    public String getKeywords() {
        return keywords;
    }

    /**
     * DOCUMENT ME!
     *
     * @return
     */
    public String getMaintainer() {
        return maintainer;
    }

    /**
     * DOCUMENT ME!
     *
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * DOCUMENT ME!
     *
     * @return
     */
    public String getTitle() {
        return title;
    }

    /**
     * DOCUMENT ME!
     *
     * @param string
     */
    public void set_abstract(String string) {
        _abstract = string;
    }

    /**
     * DOCUMENT ME!
     *
     * @param string
     */
    public void setAccessConstraints(String string) {
        accessConstraints = string;
    }

    /**
     * DOCUMENT ME!
     *
     * @param string
     */
    public void setFees(String string) {
        fees = string;
    }

    /**
     * DOCUMENT ME!
     *
     * @param string
     */
    public void setKeywords(String string) {
        keywords = string;
    }

    /**
     * DOCUMENT ME!
     *
     * @param string
     */
    public void setMaintainer(String string) {
        maintainer = string;
    }

    /**
     * DOCUMENT ME!
     *
     * @param string
     */
    public void setName(String string) {
        name = string;
    }

    /**
     * DOCUMENT ME!
     *
     * @param string
     */
    public void setTitle(String string) {
        title = string;
    }
}
