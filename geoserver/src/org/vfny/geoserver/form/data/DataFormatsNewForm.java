/* Copyright (c) 2004 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.form.data;

import java.util.Collections;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.vfny.geoserver.action.data.DataFormatUtils;


/**
 * Used to accept information from user for a New DataStore Action.
 * 
 * <p>
 * This form contains a convience property getDataStoreDescrptions() which is
 * simply to make writing the JSP easier.
 * </p>
 *
 * @author User, Refractions Research, Inc.
 * @author jive
 * @author $Author: Alessio Fabiani (alessio.fabiani@gmail.com) $ (last modification)
 * @author $Author: Simone Giannecchini (simboss_ml@tiscali.it) $ (last modification)
 * @version $Id: DataDataStoresNewForm.java,v 1.8 2004/03/15 08:16:11 jive Exp $
 */
public class DataFormatsNewForm extends ActionForm {
    private static final Pattern idPattern = Pattern.compile("^\\a$");

    /** Description provided by selected Datastore Factory */
    private String selectedDescription;

    /** User provided dataStoreID */
    private String dataFormatID;

    private List formatDescriptions;
    
    /**
     * Default state of New form
     *
     * @param mapping DOCUMENT ME!
     * @param request DOCUMENT ME!
     */
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        super.reset(mapping, request);
        selectedDescription = "";
        dataFormatID = "";
        formatDescriptions = DataFormatUtils.listDataFormatsDescriptions();
    }


    /**
     * Check NewForm for correct use
     *
     * @param mapping DOCUMENT ME!
     * @param request DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public ActionErrors validate(ActionMapping mapping,
        HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();

        if (!getDataFormatDescriptions().contains(getSelectedDescription())) {
            errors.add("selectedDescription",
                new ActionError("error.dataFormatFactory.invalid",
                    getSelectedDescription()));
        }

        if ((getDataFormatID() == null) || getDataFormatID().equals("")) {
            errors.add("dataFormatID",
                new ActionError("error.dataFormatId.required", getDataFormatID()));
        } else if (!Pattern.matches("^[a-zA-Z](\\w|\\.)*$", getDataFormatID())) {
            errors.add("dataFormatID",
                    new ActionError("error.dataFormatId.invalid", getDataFormatID()));
        }
        

        return errors;
    }

    public String getDataFormatID() {
        return dataFormatID;
    }

    public String getSelectedDescription() {
        return selectedDescription;
    }

    public void setDataFormatID(String string) {
        dataFormatID = string;
    }

    public void setSelectedDescription(String string) {
        selectedDescription = string;
    }

    /*
     * Allows the JSP page to easily access the list of dataFormat Descriptions
     */
    public SortedSet getDataFormatDescriptions() {
        return new TreeSet(formatDescriptions);
    }
}
