/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
/*
 * Created on Dec 23, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package org.vfny.geoserver.form.wfs;

import org.apache.struts.action.ActionForm;


/**
 * @author User
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
/**
 * DOCUMENT ME!
 *
 * @author User To change the template for this generated type comment go to
 *         Window>Preferences>Java>Code Generation>Code and Comments
 */
public final class WFSDescriptionForm extends ActionForm {
    private String name;
    private String title;
    private String accessConstraints;
    private String fees;
    private String maintainer;
    private String keywords;
    private String _abstract;


    public WFSDescriptionForm() {
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
	/**
	 * @return
	 */
	public String get_abstract() {
		return _abstract;
	}

	/**
	 * @return
	 */
	public String getAccessConstraints() {
		return accessConstraints;
	}

	/**
	 * @return
	 */
	public String getFees() {
		return fees;
	}

	/**
	 * @return
	 */
	public String getKeywords() {
		return keywords;
	}

	/**
	 * @return
	 */
	public String getMaintainer() {
		return maintainer;
	}

	/**
	 * @param string
	 */
	public void set_abstract(String string) {
		_abstract = string;
	}

	/**
	 * @param string
	 */
	public void setAccessConstraints(String string) {
		accessConstraints = string;
	}

	/**
	 * @param string
	 */
	public void setFees(String string) {
		fees = string;
	}

	/**
	 * @param list
	 */
	public void setKeywords(String string) {
		keywords = string;
	}

	/**
	 * @param string
	 */
	public void setMaintainer(String string) {
		maintainer = string;
	}

}
