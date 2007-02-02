/*
 * Created on Feb 16, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package org.vfny.geoserver.action.data;

import org.apache.struts.Globals;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.vfny.geoserver.action.ConfigAction;
import org.vfny.geoserver.config.DataConfig;
import org.vfny.geoserver.config.StyleConfig;
import org.vfny.geoserver.form.data.StylesNewForm;
import org.vfny.geoserver.global.UserContainer;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Create a new Style for editing (based on StyleNewForm information).
 * <p>
 * The new style will be placed in the UserContainer for editing,
 * the session will be redirected to the editor.
 * </p>
 * <p>
 * The new Style will not actually be added to the Configuration until
 * the editor form is submitted.
 * <p>
 */
public class StylesNewAction extends ConfigAction {
    public ActionForward execute(ActionMapping mapping, ActionForm form, UserContainer user,
        HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException {
        StylesNewForm newForm = (StylesNewForm) form;
        final String styleID = newForm.getStyleID();

        DataConfig config = getDataConfig();

        if (config.getStyles().containsKey(styleID)) {
            ActionErrors errors = new ActionErrors();
            errors.add("selectedStyle", new ActionError("error.style.exists", styleID));
            request.setAttribute(Globals.ERROR_KEY, errors);

            return mapping.findForward("config.data.style.new");
        }

        // Set up new StyleConfig
        StyleConfig style = new StyleConfig();
        style.setId(styleID);
        style.setDefault(config.getStyles().isEmpty());

        // Pass style over to the Editor
        user.setStyle(style);

        return mapping.findForward("config.data.style.editor");
    }
}
