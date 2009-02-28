/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
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
import org.vfny.geoserver.global.UserContainer;
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
 * @version $Id$
 */
public class DataDataStoresNewAction extends ConfigAction {
    public ActionForward execute(ActionMapping mapping, ActionForm form, UserContainer user,
        HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException {
        DataDataStoresNewForm newForm = (DataDataStoresNewForm) form;
        DataStoreConfig newDataStoreConfig;

        newDataStoreConfig = new DataStoreConfig(newForm.getDataStoreID(),
                newForm.getSelectedDescription());

        getUserContainer(request).setDataStoreConfig(newDataStoreConfig);

        return mapping.findForward("config.data.store.editor");
    }
}
