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
import org.vfny.geoserver.action.data.DataStoreUtils;

/**
 * Used to accept information from user for a New DataStore Action.
 * <p>
 * This form contains a convience property getDataStoreDescrptions() which
 * is simply to make writing the JSP easier.
 * </p>
 * @author User, Refractions Research, Inc.
 * @author $Author: emperorkefka $ (last modification)
 * @version $Id: DataDataStoresNewForm.java,v 1.1.2.5 2004/01/12 05:00:00 emperorkefka Exp $
 */
public class DataDataStoresNewForm extends ActionForm {
    /** Description provided by selected Datastore Factory */
    private String selectedDescription;
    
    /** User provided dataStoreID */
    private String dataStoreID;

    /** Default state of New form */
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        super.reset( mapping, request);
        selectedDescription = "";
        dataStoreID = null;
    }
    
    /**
     * List of available DataStoreDescriptions.
     * <p>
     * Convience method for DataStureUtils.listDataStoresDescriptions().
     * </p>
     * @return Sorted set of DataStore Descriptions.
     */
    public List getDescriptions(){
        List descriptions = DataStoreUtils.listDataStoresDescriptions();
        if( descriptions == null || descriptions.isEmpty() ){
            return Collections.EMPTY_LIST;
        }
        return descriptions;
    }    
    private static final Pattern idPattern = Pattern.compile("^\\a$");
     
    /** Check NewForm for correct use */ 
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();
                    
        if( !getDescriptions().contains( getSelectedDescription() ) ){
            errors.add( "selectedDescription",            
                new ActionError("errors.requiredFactory", getSelectedDescription() )
            );
        }
        if( Pattern.matches("^\\w*$", getDataStoreID() )){
            errors.add( "dataStoreID",            
                new ActionError("errors.requireDataStoreID", getDataStoreID() )
            );            
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
    public SortedSet getDataStoreDescriptions () {
        return new TreeSet(DataStoreUtils.listDataStoresDescriptions());
    }    

}
