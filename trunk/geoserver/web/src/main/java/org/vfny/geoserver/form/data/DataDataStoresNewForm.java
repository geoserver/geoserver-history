/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.form.data;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.vfny.geoserver.util.DataStoreUtils;
import java.util.Collections;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;


/**
 * Used to accept information from user for a New DataStore Action.
 *
 * <p>
 * This form contains a convience property getDataStoreDescrptions() which is
 * simply to make writing the JSP easier.
 * </p>
 *
 * @author User, Refractions Research, Inc.
 * @author $Author: jive $ (last modification)
 * @version $Id$
 */
public class DataDataStoresNewForm extends ActionForm {
    private static final Pattern idPattern = Pattern.compile("^\\a$");

    /** Description provided by selected Datastore Factory */
    private String selectedDescription;

    /** User provided dataStoreID */
    private String dataStoreID;

    /**
     * Default state of New form
     *
     * @param mapping DOCUMENT ME!
     * @param request DOCUMENT ME!
     */
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        super.reset(mapping, request);
        selectedDescription = "";
        dataStoreID = "";
    }

    /**
     * List of available DataStoreDescriptions.
     *
     * <p>
     * Convience method for DataStureUtils.listDataStoresDescriptions().
     * </p>
     *
     * @return Sorted set of DataStore Descriptions.
     */
    public List getDescriptions() {
        List descriptions = DataStoreUtils.listDataStoresDescriptions();

        if ((descriptions == null) || descriptions.isEmpty()) {
            return Collections.EMPTY_LIST;
        }

        String enableVersioning = (String) getServlet().getServletContext().getInitParameter("enableVersioning");
        if(enableVersioning == null ||  !"TRUE".equals(enableVersioning.toUpperCase())) {
            descriptions.remove("Versioning Postgis");
        }
        return descriptions;
    }

    /**
     * Check NewForm for correct use
     *
     * @param mapping DOCUMENT ME!
     * @param request DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();

        if (!getDescriptions().contains(getSelectedDescription())) {
            errors.add("selectedDescription",
                new ActionError("error.dataStoreFactory.invalid", getSelectedDescription()));
        }

        if ((getDataStoreID() == null) || getDataStoreID().equals("")) {
            errors.add("dataStoreID",
                new ActionError("error.dataStoreId.required", getDataStoreID()));
        } else if (!Pattern.matches("^\\w(\\w|\\.)*$", getDataStoreID())) {
            errors.add("dataStoreID", new ActionError("error.dataStoreId.invalid", getDataStoreID()));
        }

        return errors;
    }

    public String getDataStoreID() {
        return dataStoreID;
    }

    public String getSelectedDescription() {
        return selectedDescription;
    }

    public void setDataStoreID(String string) {
        dataStoreID = string;
    }

    public void setSelectedDescription(String string) {
        selectedDescription = string;
    }

    /*
     * Allows the JSP page to easily access the list of dataStore Descriptions
     */
    public SortedSet getDataStoreDescriptions() {
        return new TreeSet(getDescriptions());
    }
}
