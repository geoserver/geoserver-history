/* Copyright (c) 2004 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.form.data;

import java.util.Collections;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;
import org.vfny.geoserver.action.data.DataStoreUtils;

/**
 * DataDataStoresNewForm purpose.
 * <p>
 * Description of DataDataStoresNewForm ...
 * </p>
 * 
 * <p>
 * Capabilities:
 * </p>
 * <ul>
 * <li>
 * Feature: description
 * </li>
 * </ul>
 * <p>
 * Example Use:
 * </p>
 * <pre><code>
 * DataDataStoresNewForm x = new DataDataStoresNewForm(...);
 * </code></pre>
 * 
 * @author User, Refractions Research, Inc.
 * @author $Author: jive $ (last modification)
 * @version $Id: DataDataStoresNewForm.java,v 1.1.2.3 2004/01/12 04:41:38 jive Exp $
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
    public List getDataStoreDescriptions(){
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
        MessageResources resources = (MessageResources) request.getAttribute( Action.MESSAGES_KEY );
                    
        if( !getDataStoreDescriptions().contains( getSelectedDescription() ) ){
            String message = resources.getMessage("label.selectedDescription", getSelectedDescription() );
            errors.add( "selectedDataStoreDescription",            
                new ActionError("errors.required", message)
            );
        }
        if( Pattern.matches("^\\w*$", getDataStoreID() )){
                   
            String message = resources.getMessage("label.dataStoreID",getDataStoreID() );
            errors.add( "dataStoreID",            
                new ActionError("errors.required", message)
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

}
