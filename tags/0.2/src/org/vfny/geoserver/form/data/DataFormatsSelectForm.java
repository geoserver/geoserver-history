/* Copyright (c) 2004 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.form.data;

import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;
import org.vfny.geoserver.action.HTMLEncoder;
import org.vfny.geoserver.config.ConfigRequests;


/**
 * Select current DataStore for edit or delete Action.
 *
 * @author rgould, Refractions Research, Inc.
 * @author emperorkefka
 * @author $Author: Alessio Fabiani (alessio.fabiani@gmail.com) $ (last modification)
 * @author $Author: Simone Giannecchini (simboss_ml@tiscali.it) $ (last modification)
 * @version $Id: DataDataStoresSelectForm.java,v 1.9 2004/03/16 19:57:40 emperorkefka Exp $
 */
public class DataFormatsSelectForm extends ActionForm {

	/**
	 * Action that spawned us must be "edit" or "delete"
	 * 
	 * @uml.property name="buttonAction" multiplicity="(0 1)"
	 */
	private String buttonAction;

	/**
	 * Selection from list - will be a dataStoreId
	 * 
	 * @uml.property name="selectedDataFormatId" multiplicity="(0 1)"
	 */
	private String selectedDataFormatId;

	/**
	 * 
	 * @uml.property name="dataFormatIds"
	 * @uml.associationEnd elementType="java.lang.String" multiplicity="(0 -1)"
	 */
	private List dataFormatIds;


    /**
     * Reset form
     *
     * @param mapping DOCUMENT ME!
     * @param request DOCUMENT ME!
     */
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        super.reset(mapping, request);

        // Pass data from congif layer to screen
        // REVIST: Bad Design JSP should lookup data itself!
        dataFormatIds = ConfigRequests.getDataConfig(request).listDataFormatIds();

        // Usual reset stuff
        selectedDataFormatId = null; // nothing selected yet        
        buttonAction = null; // updated when user submits form        
    }

    /**
     * Validate as required
     *
     * @param mapping DOCUMENT ME!
     * @param request DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public ActionErrors validate(ActionMapping mapping,
        HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();

        Locale locale = (Locale) request.getLocale();
        MessageResources messages = servlet.getResources();
        String EDIT = HTMLEncoder.decode(messages.getMessage(locale, "label.edit"));
        String DELETE = HTMLEncoder.decode(messages.getMessage(locale, "label.delete"));
        
        if (!getDataFormatIds().contains(getSelectedDataFormatId())) {
            errors.add("selectedDataFormatId",
                new ActionError("errors.factory.invalid",
                    getSelectedDataFormatId()));
        }

        if (!DELETE.equals(getButtonAction())
                && !EDIT.equals(getButtonAction())) {
            errors.add("buttonAction",
                new ActionError("errors.buttonAction.invalid", getButtonAction()));
        }
        return errors;
    }

	/**
	 * List of current DataFormatIds
	 * 
	 * @return DOCUMENT ME!
	 * 
	 * @uml.property name="dataFormatIds"
	 */
	public List getDataFormatIds() {
		return dataFormatIds;
	}

	/**
	 * DataFormatID selected by User.
	 * 
	 * <p>
	 * If the user has not selected anything (is this possible?) we will return
	 * <code>null</code>.
	 * </p>
	 * 
	 * @return Selected DataFormatID or <code>null</code> if nothing is selected
	 * 
	 * @uml.property name="selectedDataFormatId"
	 */
	public String getSelectedDataFormatId() {
		return selectedDataFormatId;
	}

	/**
	 * The button the user hit to submit this form.
	 * 
	 * <p>
	 * We are doubling up and having the Same action process both Edit and
	 * Delete.
	 * </p>
	 * 
	 * @return Either <code>edit</code> or <code>delete</code>
	 * 
	 * @uml.property name="buttonAction"
	 */
	public String getButtonAction() {
		return buttonAction;
	}

	/**
	 * 
	 * @uml.property name="buttonAction"
	 */
	public void setButtonAction(String string) {
		buttonAction = string;
	}

	/**
	 * 
	 * @uml.property name="selectedDataFormatId"
	 */
	public void setSelectedDataFormatId(String string) {
		selectedDataFormatId = string;
	}

}