/* Copyright (c) 2004 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.form.data;

import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.vfny.geoserver.config.DataConfig;

/**
 * Select current DataStore for edit or delete Action.
 * @author User, Refractions Research, Inc.
 * @author $Author: emperorkefka $ (last modification)
 * @version $Id: DataDataStoresSelectForm.java,v 1.1.2.3 2004/01/12 04:10:28 emperorkefka Exp $
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
    
    public SortedSet getDataStoreIDs () {
        ServletContext context = getServlet().getServletContext();
        DataConfig config =
            (DataConfig) context.getAttribute(DataConfig.CONFIG_KEY);

        return new TreeSet(config.getDataStores().keySet());
    }
}
