/* Copyright (c) 2004 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.form.data;

import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 * Select current DataStore for edit or delete Action.
 * @author User, Refractions Research, Inc.
 * @author $Author: jive $ (last modification)
 * @version $Id: DataDataStoresSelectForm.java,v 1.1.2.2 2004/01/12 02:16:19 jive Exp $
 */
public class DataDataStoresSelectForm extends ActionForm {
    /** Action that spawned us? Edit or Delete? */
    private String action;
       
    /** This seams wrong */
    private SortedSet dataStores;
    
    /** Selection from list - will be a dataStoreId */
    private String selectedDataStore;
    
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        super.reset( mapping, request );
        //POPULATE dataStores WITH AVAILABLE DATA STORES
    }
    /** Validate as required */
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();
        
        return errors;
    }    
}
