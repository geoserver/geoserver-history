/*
 * Created on Feb 16, 2004
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
import org.vfny.geoserver.form.data.DataNamespacesEditorForm;
import org.vfny.geoserver.form.data.DataNamespacesSelectForm;
import org.vfny.geoserver.global.UserContainer;

/**
 * DataNamespacesSelectAction purpose.

 * <pre><code>
 * DataNamespacesSelectAction x = new DataNamespacesSelectAction(...);
 * </code></pre>
 * 
 * @author rgould, Refractions Research, Inc.
 * @author $Author: dmzwiers $ (last modification)
 * @version $Id: DataNamespacesSelectAction.java,v 1.4 2004/02/25 23:32:34 dmzwiers Exp $
 */
public class DataNamespacesSelectAction extends ConfigAction {
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            UserContainer user, HttpServletRequest request, HttpServletResponse response)
    throws IOException, ServletException {
        DataNamespacesSelectForm namespacesForm = (DataNamespacesSelectForm) form;

        String action = namespacesForm.getAction();

        DataConfig dataConfig = (DataConfig) getDataConfig();
        NameSpaceConfig config = null;
        
        Locale locale = (Locale) request.getLocale();
        MessageResources messages = servlet.getResources();
        String edit = HTMLEncoder.decode(messages.getMessage(locale, "label.edit"));
        String delete = HTMLEncoder.decode(messages.getMessage(locale, "label.delete"));

        config = (NameSpaceConfig) dataConfig.getNameSpace(namespacesForm.getSelectedNamespace());
        if(config==null){
        	throw new NullPointerException();
        }
        getUserContainer(request).setNamespaceConfig(config);

        if (action.equals(delete)) {

            dataConfig.removeNameSpace(namespacesForm.getSelectedNamespace());
            getApplicationState().notifyConfigChanged();
            
            getUserContainer(request).setNamespaceConfig(null);
            namespacesForm.reset(mapping, request);
            return mapping.findForward("config.data.namespace");
        }
        getUserContainer(request).setNamespaceConfig(config);
        return mapping.findForward("config.data.namespace.editor");
    }
}
