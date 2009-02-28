/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.action.data;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.vfny.geoserver.action.ConfigAction;
import org.vfny.geoserver.config.DataConfig;
import org.vfny.geoserver.config.NameSpaceConfig;
import org.vfny.geoserver.form.data.DataNamespacesEditorForm;
import org.vfny.geoserver.global.UserContainer;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * DOCUMENT ME!
 *
 * @author rgould To change the template for this generated type comment go to
 *         Window>Preferences>Java>Code Generation>Code and Comments
 */
public class DataNamespacesEditorAction extends ConfigAction {
    public ActionForward execute(ActionMapping mapping, ActionForm form, UserContainer user,
        HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException {
        DataNamespacesEditorForm namespacesForm = (DataNamespacesEditorForm) form;

        String URI = namespacesForm.getURI();
        String prefix = namespacesForm.getPrefix();

        // let's check name and URI are unique
        DataConfig dataConfig = (DataConfig) getDataConfig();
        NameSpaceConfig config = getUserContainer(request).getNamespaceConfig();
        ActionErrors errors = new ActionErrors();
        for (Iterator it = dataConfig.getNameSpaces().values().iterator(); it.hasNext();) {
            NameSpaceConfig other = (NameSpaceConfig) it.next();
            if(other == config)
                continue;
            if(other.getPrefix().equals(prefix))
                errors.add(ActionErrors.GLOBAL_ERROR,
                        new ActionError("error.prefix.duplicate", new Object[] {other.getPrefix(), other.getUri()}));
            if(other.getUri().equals(URI))
                errors.add(ActionErrors.GLOBAL_ERROR,
                        new ActionError("error.uri.duplicate", other.getPrefix()));
        }
        if(errors.size() > 0) {
            saveErrors(request, errors);
            return mapping.findForward("config.data.namespace.editor");
        }

        
        config.setPrefix(prefix);
        config.setUri(URI);

        dataConfig.addNameSpace(prefix, config);
        getApplicationState().notifyConfigChanged();

        return mapping.findForward("config.data.namespace");
    }
}
