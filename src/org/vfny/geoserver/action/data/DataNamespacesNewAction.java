/*
 * Created on Feb 18, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
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
import org.vfny.geoserver.form.data.DataNamespacesNewForm;
import org.vfny.geoserver.global.UserContainer;
/**
 * DataNamespacesNewAction purpose.
 * <p>
 * Description of DataNamespacesNewAction ...
 * 
 * @author rgould, Refractions Research, Inc.
 * @author $Author: dmzwiers $ (last modification)
 * @version $Id: DataNamespacesNewAction.java,v 1.3 2004/02/26 00:17:29 dmzwiers Exp $
 */
public class DataNamespacesNewAction extends ConfigAction {
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            UserContainer user, HttpServletRequest request, HttpServletResponse response)
    throws IOException, ServletException {
        DataNamespacesNewForm namespacesForm = (DataNamespacesNewForm) form;
        
        String prefix = namespacesForm.getPrefix();

        NameSpaceConfig config = new NameSpaceConfig();
        config.setPrefix(prefix);
        
        getUserContainer(request).setNamespaceConfig(config);
        
        return mapping.findForward("config.data.namespace.editor");
    }
}
