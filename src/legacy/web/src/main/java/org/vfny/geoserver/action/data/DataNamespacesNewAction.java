/*
 * Created on Feb 18, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
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
import org.vfny.geoserver.form.data.DataNamespacesNewForm;
import org.vfny.geoserver.global.UserContainer;
import java.io.IOException;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * DataNamespacesNewAction purpose.
 * <p>
 * Description of DataNamespacesNewAction ...
 *
 * @author rgould, Refractions Research, Inc.
 * @author $Author: dmzwiers $ (last modification)
 * @version $Id$
 */
public class DataNamespacesNewAction extends ConfigAction {
    public ActionForward execute(ActionMapping mapping, ActionForm form, UserContainer user,
        HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException {
        DataNamespacesNewForm namespacesForm = (DataNamespacesNewForm) form;

        String prefix = namespacesForm.getPrefix();
        
        DataConfig dataConfig = (DataConfig) getDataConfig();
        
        for (Iterator it = dataConfig.getNameSpaces().values().iterator(); it.hasNext();) {
            NameSpaceConfig ns = (NameSpaceConfig) it.next();
            if(ns.getPrefix().equals(prefix)) {
                ActionErrors errors = new ActionErrors();
                errors.add(ActionErrors.GLOBAL_ERROR,
                        new ActionError("error.prefix.duplicate", new Object[] {ns.getPrefix(), ns.getUri()}));
                saveErrors(request, errors);
                return mapping.findForward("config.data.namespace.new");
            }
        }

        NameSpaceConfig config = new NameSpaceConfig();
        config.setPrefix(prefix);

        getUserContainer(request).setNamespaceConfig(config);

        return mapping.findForward("config.data.namespace.editor");
    }
}
