/* Copyright (c) 2004 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.action.data;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.vfny.geoserver.action.ConfigAction;

/**
 * DataDataStoresNewAction purpose.
 * <p>
 * Description of DataDataStoresNewAction ...
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
 * DataDataStoresNewAction x = new DataDataStoresNewAction(...);
 * </code></pre>
 * 
 * @author User, Refractions Research, Inc.
 * @author $Author: emperorkefka $ (last modification)
 * @version $Id: DataDataStoresNewAction.java,v 1.1.2.2 2004/01/12 05:18:37 emperorkefka Exp $
 */
public class DataDataStoresNewAction extends ConfigAction {
    public ActionForward execute(ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request,
        HttpServletResponse response)
        throws IOException, ServletException {
            
        //CREATE NEW BLANK DATASTORE AND FORWARD TO EDITOR WITH SAID DATASTORE SELECETED    
        
   //     System.out.println("### NEW ### requested, reset, forward, dsType: " + selectedDataStoreType);
        //Return them back to the form page so they can create a new dataStore.
    /*
        context.removeAttribute("selectedDataStore");
        dataStoresForm.setNewDataStore(true);
        dataStoresForm.setSelectedDataStoreType(selectedDataStoreType);
        dataStoresForm.reset(mapping, request);
        dataStoresForm.setAction("new"); */
        return mapping.findForward("dataConfigDataStores");

    }
}
