/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */

package org.vfny.geoserver.action.data;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;
import org.vfny.geoserver.action.ConfigAction;
import org.vfny.geoserver.action.HTMLEncoder;
import org.vfny.geoserver.config.DataConfig;
import org.vfny.geoserver.config.NameSpaceConfig;
import org.vfny.geoserver.form.data.DataNamespacesEditorForm;
import org.vfny.geoserver.global.UserContainer;


/**
 * DOCUMENT ME!
 *
 * @author rgould To change the template for this generated type comment go to
 *         Window>Preferences>Java>Code Generation>Code and Comments
 */
public class DataNamespacesEditorAction extends ConfigAction {
    public ActionForward execute(ActionMapping mapping, ActionForm form,
        UserContainer user, HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException {
        DataNamespacesEditorForm namespacesForm = (DataNamespacesEditorForm) form;

        String URI = namespacesForm.getURI();
        String prefix = namespacesForm.getPrefix();

        boolean _default = namespacesForm.is_default();

        if (namespacesForm.isDefaultChecked()) {
            _default = false;
        }

        DataConfig dataConfig = (DataConfig) getDataConfig();
        NameSpaceConfig config = null;
        
        config = getUserContainer(request).getNamespaceConfig();
        
        config.setDefault(_default);
        config.setPrefix(prefix);
        config.setUri(URI);

        dataConfig.addNameSpace(prefix, config);
        getApplicationState().notifyConfigChanged();

        return mapping.findForward("dataConfigNamespaces");
    }
}
