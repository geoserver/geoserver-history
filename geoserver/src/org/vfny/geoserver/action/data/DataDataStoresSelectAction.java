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
import org.vfny.geoserver.config.ConfigRequests;
import org.vfny.geoserver.config.DataConfig;
import org.vfny.geoserver.config.DataStoreConfig;
import org.vfny.geoserver.form.data.DataDataStoresSelectForm;

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
 * @version $Id: DataDataStoresSelectAction.java,v 1.1.2.4 2004/01/12 08:51:37 emperorkefka Exp $
 */
public class DataDataStoresSelectAction extends ConfigAction {

    public ActionForward execute(ActionMapping mapping,
        ActionForm incomingForm,
        HttpServletRequest request,
        HttpServletResponse response)
        throws IOException, ServletException {

    //DETERMINE IF THEY REQUESTED AN EDIT OR A DELETE
    //IF DELETE, REMOVE DATA STORE AND FORWARD BACK TO SELECTOR
    //IF EDIT, FORWARD TO EDITOR WITH CURRENT DATASTORE
    
        DataDataStoresSelectForm form = (DataDataStoresSelectForm) incomingForm;
        
        String buttonAction = form.getButtonAction();
        
        DataConfig dataConfig = (DataConfig) getDataConfig();
        DataStoreConfig dsConfig = null;
        
        if ("edit".equals(buttonAction)) {
            dsConfig = (DataStoreConfig) dataConfig.getDataStore(form.getSelectedDataStoreId());
            
            //This would be ideal. Perhaps later. Session for now.
            //getUserContainer(request).setDataStoreID(form.getSelectedDataStoreId());
            request.getSession().setAttribute("selectedDataStoreId", form.getSelectedDataStoreId());
            return mapping.findForward("dataConfigDataStores");
            
        } else if ("delete".equals(buttonAction)) {
            System.out.println("@@@@@ BEFORE @@@@ " +  ConfigRequests.getDataConfig(request).listDataStoreIds());
            dataConfig.removeDataStore(form.getSelectedDataStoreId());
            request.getSession().removeAttribute("selectedDataStoreId");
            form.setSelectedDataStoreId("fred");
            request.removeAttribute("selectedDataStoreId");
            System.out.println("@@@@@ AFTER @@@@ " +  ConfigRequests.getDataConfig(request).listDataStoreIds());
            form.reset(mapping, request);
            return mapping.findForward("dataConfigDataStores");
        }
        
        throw new ServletException("Action must equal either 'edit' or 'delete'");        
    }
}
