/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.form.data;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.vfny.geoserver.config.DataConfig;
import org.vfny.geoserver.config.NameSpaceConfig;
import org.vfny.geoserver.util.Requests;
import org.vfny.geoserver.util.RequestsLegacy;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.regex.Pattern;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;


/**
 * DOCUMENT ME!
 *
 * @author rgould To change the template for this generated type comment go to
 *         Window>Preferences>Java>Code Generation>Code and Comments
 */
public class DataNamespacesEditorForm extends ActionForm {
    private String URI;
    private boolean _default;
    private String prefix;
    private HttpServletRequest request;

    /*
     * Because of the way that STRUTS works, if the user does not check the default box,
     * or unchecks it, set_default() is never called, thus we must monitor set_default()
     * to see if it doesn't get called. This must be accessible, as ActionForms need to
     * know about it -- there is no way we can tell whether we are about to be passed to
     * an ActionForm or not.
     *
     * Probably a better way to do this, but I can't think of one.
     * -rgould
     */
    private boolean defaultChecked = false;

    public void reset(ActionMapping arg0, HttpServletRequest request) {
        super.reset(arg0, request);
        this.request = request;

        defaultChecked = false;

        ServletContext context = getServlet().getServletContext();
        DataConfig config = (DataConfig) context.getAttribute(DataConfig.CONFIG_KEY);

        NameSpaceConfig nsConfig;

        nsConfig = RequestsLegacy.getUserContainer(request).getNamespaceConfig();

        _default = nsConfig.isDefault();
        prefix = nsConfig.getPrefix();
        URI = nsConfig.getUri();
    }

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();

        if ((prefix == null) || prefix.equals("")) {
            errors.add("prefix", new ActionError("error.prefix.required", getPrefix()));
        } else if (!Pattern.matches("^\\w*$", prefix)) {
            errors.add("dataStoreID", new ActionError("error.prefix.invalid", prefix));
        }

        if ((URI == null) || URI.equals("")) {
            errors.add("URI", new ActionError("error.uri.required", prefix));
        } else {
            try {
                new java.net.URI(URI);
            } catch (URISyntaxException badURI) {
                errors.add("dataStoreID", new ActionError("error.uri.malformed", badURI));
            }
        }

        return errors;
    }

    /**
     * DOCUMENT ME!
     *
     * @return
     */
    public boolean is_default() {
        return _default;
    }

    /**
     * DOCUMENT ME!
     *
     * @return
     */
    public String getPrefix() {
        return prefix;
    }

    /**
     * DOCUMENT ME!
     *
     * @return
     */
    public String getURI() {
        return URI;
    }

    /**
     * DOCUMENT ME!
     *
     * @param b
     */
    public void set_default(boolean b) {
        defaultChecked = true;
        _default = b;
    }

    /**
     * DOCUMENT ME!
     *
     * @param string
     */
    public void setPrefix(String string) {
        prefix = string;
    }

    /**
     * DOCUMENT ME!
     *
     * @param string
     */
    public void setURI(String string) {
        URI = string;
    }

    /**
     * DOCUMENT ME!
     *
     * @return
     */
    public boolean isDefaultChecked() {
        return defaultChecked;
    }

    /**
     * DOCUMENT ME!
     *
     * @param b
     */
    public void setDefaultChecked(boolean b) {
        defaultChecked = b;
    }
}
