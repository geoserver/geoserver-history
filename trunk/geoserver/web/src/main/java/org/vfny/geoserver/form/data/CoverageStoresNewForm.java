/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.form.data;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.geoserver.data.util.CoverageStoreUtils;

import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;


/**
 * Used to accept information from user for a New DataFormat Action.
 *
 * @author User, Refractions Research, Inc.
 * @author jive
 * @author $Author: Alessio Fabiani (alessio.fabiani@gmail.com) $ (last
 *         modification)
 * @author $Author: Simone Giannecchini (simboss1@gmail.com) $ (last
 *         modification)
 */
public final class CoverageStoresNewForm extends ActionForm {
    /**
     *
     */
    private static final long serialVersionUID = -7723738069176272163L;

    /**
     * Description provided by selected Dataformat GDSFactory
     */
    private String selectedDescription;

    /**
     * User provided dataFormatID
     */
    private String dataFormatID;
    private List formatDescriptions;

    /**
     * Default state of New form
     *
     * @param mapping
     *            DOCUMENT ME!
     * @param request
     *            DOCUMENT ME!
     */
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        super.reset(mapping, request);
        selectedDescription = "";
        dataFormatID = "";
        formatDescriptions = CoverageStoreUtils.listDataFormatsDescriptions();
    }

    /**
     * Check NewForm for correct use
     *
     * @param mapping
     *            DOCUMENT ME!
     * @param request
     *            DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();

        if (!getDataFormatDescriptions().contains(getSelectedDescription())) {
            errors.add("selectedDescription",
                new ActionError("error.dataFormatFactory.invalid", getSelectedDescription()));
        }

        if ((getDataFormatID() == null) || getDataFormatID().equals("")) {
            errors.add("dataFormatID",
                new ActionError("error.dataFormatId.required", getDataFormatID()));
        } else if (!Pattern.matches("^\\w(\\w|\\.)*$", getDataFormatID())) {
            errors.add("dataFormatID",
                new ActionError("error.dataFormatId.invalid", getDataFormatID()));
        }

        return errors;
    }

    /**
     *
     */
    public String getDataFormatID() {
        return dataFormatID;
    }

    /**
     *
     */
    public String getSelectedDescription() {
        return selectedDescription;
    }

    /**
     *
     */
    public void setDataFormatID(String string) {
        dataFormatID = string;
    }

    /**
     *
     */
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
