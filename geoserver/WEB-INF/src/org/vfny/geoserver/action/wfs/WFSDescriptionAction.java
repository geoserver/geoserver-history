/*
 * Created on Dec 29, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package org.vfny.geoserver.action.wfs;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author User
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public final class WFSDescriptionAction extends Action {

	public ActionForward execute(ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws IOException, ServletException {

			WFSDescriptionForm descriptionForm = (WFSDescriptionForm) form;

			String name = descriptionForm.getName();
			String title = descriptionForm.getTitle();
			String accessConstraints = descriptionForm.getAccessConstraints();
			String fees = descriptionForm.getFees();
			String maintainer = descriptionForm.getMaintainer();
			String keywords = descriptionForm.getKeywords();
			String _abstract = descriptionForm.get_abstract();

			HttpSession session = request.getSession();
			session.setAttribute("test", form);

			return mapping.findForward("welcome");
		}

}
