/* Copyright (c) 2004 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.form.data;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.vfny.geoserver.config.ConfigRequests;

/**
 * Select current DataStore for edit or delete Action.
 * 
 * @author User, Refractions Research, Inc.
 * @author $Author: emperorkefka $ (last modification)
 * @version $Id: DataDataStoresSelectForm.java,v 1.1.2.7 2004/01/12 06:57:55 emperorkefka Exp $
 */
public class DataDataStoresSelectForm extends ActionForm {
    
    /** Action that spawned us must be "edit" or "delete" */
    private String buttonAction;
    
    /** Selection from list - will be a dataStoreId */
    private String selectedDataStoreId;
    
    private List dataStoreIds;
    /** Reset form */
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        super.reset( mapping, request );
        
        // Pass data from congif layer to screen
        // REVIST: Bad Design JSP should lookup data itself!
        dataStoreIds = ConfigRequests.getDataConfig(request).listDataStoreIds();
        
        // Usual reset stuff
        selectedDataStoreId = null; // nothing selected yet        
        buttonAction = null; // updated when user submits form        
    }
    
    /** Validate as required */
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();
                   /* 
        if( !getDataStoreIds().contains( getSelectedDataStoreId() ) ){
            errors.add( "selectedDataStoreId",            
                new ActionError("errors.requiredFactory", getSelectedDataStoreId() )
            );
        }
        if( !"delete".equals( getButtonAction() ) &&
            !"edit".equals( getButtonAction() ) ){
            errors.add( "buttonAction",            
                new ActionError("errors.requireDataStoreID", getButtonAction() )
            );            
        }*/
        return errors;
    }    

    /** List of current DataStoreIds */
    public List getDataStoreIds(){
        return dataStoreIds;
    }
    
    /**
     * DataStoreID selected by User.
     * <p>
     * If the user has not selected anything (is this possible?) we will 
     * return <code>null</code>.
     * </p>
     * @return Selected DataStoreID or <code>null</code> if nothing is selected 
     */
    public String getSelectedDataStoreId() {
        return selectedDataStoreId;
    }
    
    /**
     * The button the user hit to submit this form.
     * <p>
     * We are doubling up and having the Same action process both
     * Edit and Delete.
     * </p>
     * @return Either <code>edit</code> or <code>delete</code>
     */
    public String getButtonAction() {
        return buttonAction;
    }

    public void setButtonAction(String string) {
        buttonAction = string;
    }    
    public void setSelectedDataStoreId(String string) {
        selectedDataStoreId = string;
    }

}
