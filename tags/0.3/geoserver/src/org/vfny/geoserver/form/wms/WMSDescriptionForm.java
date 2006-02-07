/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */

package org.vfny.geoserver.form.wms;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.vfny.geoserver.config.WMSConfig;


/**
 * DOCUMENT ME!
 *
 * @author rgould To change the template for this generated type comment go to
 *         Window>Preferences>Java>Code Generation>Code and Comments
 */
public final class WMSDescriptionForm extends ActionForm {

	/**
	 * 
	 * @uml.property name="name" multiplicity="(0 1)"
	 */
	private String name;

	/**
	 * 
	 * @uml.property name="title" multiplicity="(0 1)"
	 */
	private String title;

	/**
	 * 
	 * @uml.property name="accessConstraints" multiplicity="(0 1)"
	 */
	private String accessConstraints;

	/**
	 * 
	 * @uml.property name="fees" multiplicity="(0 1)"
	 */
	private String fees;

	/**
	 * 
	 * @uml.property name="maintainer" multiplicity="(0 1)"
	 */
	private String maintainer;

	/**
	 * 
	 * @uml.property name="keywords" multiplicity="(0 1)"
	 */
	private String keywords;

	/**
	 * 
	 * @uml.property name="_abstract" multiplicity="(0 1)"
	 */
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

        for (int i = 0; i < config.getKeywords().length; i++) {
            out = out + config.getKeywords()[i]
                + System.getProperty("line.separator");
        }

        this.keywords = out;
    }

    public ActionErrors validate(ActionMapping mapping,
        HttpServletRequest request) {
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
            errors.add("accessConstraints",
                new ActionError("error.accessConstraints.required"));
        }

        if ((maintainer == null) || (maintainer.length() == 0)) {
            errors.add("maintainer",
                new ActionError("error.maintainer.required"));
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
	 * 
	 * @uml.property name="_abstract"
	 */
	public String get_abstract() {
		return _abstract;
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @return
	 * 
	 * @uml.property name="accessConstraints"
	 */
	public String getAccessConstraints() {
		return accessConstraints;
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @return
	 * 
	 * @uml.property name="fees"
	 */
	public String getFees() {
		return fees;
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @return
	 * 
	 * @uml.property name="keywords"
	 */
	public String getKeywords() {
		return keywords;
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @return
	 * 
	 * @uml.property name="maintainer"
	 */
	public String getMaintainer() {
		return maintainer;
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @return
	 * 
	 * @uml.property name="name"
	 */
	public String getName() {
		return name;
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @return
	 * 
	 * @uml.property name="title"
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param string
	 * 
	 * @uml.property name="_abstract"
	 */
	public void set_abstract(String string) {
		_abstract = string;
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param string
	 * 
	 * @uml.property name="accessConstraints"
	 */
	public void setAccessConstraints(String string) {
		accessConstraints = string;
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param string
	 * 
	 * @uml.property name="fees"
	 */
	public void setFees(String string) {
		fees = string;
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param string
	 * 
	 * @uml.property name="keywords"
	 */
	public void setKeywords(String string) {
		keywords = string;
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param string
	 * 
	 * @uml.property name="maintainer"
	 */
	public void setMaintainer(String string) {
		maintainer = string;
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param string
	 * 
	 * @uml.property name="name"
	 */
	public void setName(String string) {
		name = string;
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param string
	 * 
	 * @uml.property name="title"
	 */
	public void setTitle(String string) {
		title = string;
	}

}
