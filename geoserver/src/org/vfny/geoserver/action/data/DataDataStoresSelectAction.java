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
 * DataDataStoreSelectAction purpose.
 * <p>
 * Description of DataDataStoreSelectAction ...
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
 * DataDataStoreSelectAction x = new DataDataStoreSelectAction(...);
 * </code></pre>
 * 
 * @author User, Refractions Research, Inc.
 * @author $Author: emperorkefka $ (last modification)
 * @version $Id: DataDataStoresSelectAction.java,v 1.1.2.2 2004/01/12 05:18:37 emperorkefka Exp $
 */
public class DataDataStoresSelectAction extends ConfigAction {

    public ActionForward execute(ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request,
        HttpServletResponse response)
        throws IOException, ServletException {
            
    //DETERMINE IF THEY REQUESTED AN EDIT OR A DELETE
    //IF DELETE, REMOVE DATA STORE AND FORWARD BACK TO SELECTOR
    //IF EDIT, FORWARD TO EDITOR WITH CURRENT DATASTORE
                
    //If they push edit, simply forward them back so the information is repopulated.
 /*   if (action.equals("edit")) {
        System.out.println("edit selected for dataStore: " + dataStoresForm.getSelectedDataStore());
        dataStoresForm.reset(mapping, request);
        return mapping.findForward("dataConfigDataStores");
    }
        
    if (action.equals("delete")) {
        dataConfig.removeDataStore(dataStoresForm.getSelectedDataStore());
        System.out.println("Delete requested on " + dataStoresForm.getSelectedDataStore());
    } else {
            
        config.setId(dataStoreID);
        config.setEnabled(enabled);
        config.setNameSpaceId(namespace);
        config.setAbstract(description);
        
        //Do configuration parameters here.
        
        dataConfig.addDataStore(dataStoreID, config);
    } */
    return null;
    }
}
