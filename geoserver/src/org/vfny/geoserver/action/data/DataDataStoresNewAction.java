/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
/* Copyright (c) 2004 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.action.data;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.vfny.geoserver.action.ConfigAction;
import org.vfny.geoserver.config.DataStoreConfig;
import org.vfny.geoserver.form.data.DataDataStoresNewForm;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Create a new DataStoreConfig based on user's input.
 * 
 * <p>
 * Will need to update the current DataStoreId as stored in session context.
 * </p>
 *
 * @author User, Refractions Research, Inc.
 * @author $Author: dmzwiers $ (last modification)
 * @version $Id: DataDataStoresNewAction.java,v 1.4 2004/01/21 00:26:07 dmzwiers Exp $
 */
public class DataDataStoresNewAction extends ConfigAction {
    public ActionForward execute(ActionMapping mapping, ActionForm form,
        HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException {
        /*
           if( isLoggedIn( request )){
                   return forward to login page
           }
           UserContainer user = getUserContainer( request );
         */
        DataDataStoresNewForm newForm = (DataDataStoresNewForm) form;
        DataStoreConfig newDataStoreConfig;

        newDataStoreConfig = new DataStoreConfig(newForm.getDataStoreID(),
                newForm.getSelectedDescription());

        getDataConfig().addDataStore(newDataStoreConfig);

        request.getSession().setAttribute("selectedDataStoreId",
            newForm.getDataStoreID());

        return mapping.findForward("dataConfigDataStores");
    }
}
