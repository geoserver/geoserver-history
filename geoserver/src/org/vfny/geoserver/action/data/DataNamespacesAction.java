/*
 * Created on Jan 8, 2004
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
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
import org.vfny.geoserver.config.DataConfig;
import org.vfny.geoserver.form.data.DataNamespacesForm;

/**
 * @author rgould
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class DataNamespacesAction extends ConfigAction {
	public ActionForward execute(ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws IOException, ServletException {
			
		DataNamespacesForm namespacesForm = (DataNamespacesForm) form;
	
		String namespaceID = namespacesForm.getNamespaceID();
		String URI = namespacesForm.getURI();
		String prefix = namespacesForm.getPrefix();

		boolean _default = namespacesForm.is_default();
		if (namespacesForm.isDefaultChecked()) {
			_default = false;
		}
	
		DataConfig config = (DataConfig) getDataConfig();			
		
			
		return mapping.findForward("data.namespaces");
	}

}
