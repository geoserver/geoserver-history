/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.form.wms;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.vfny.geoserver.config.WMSConfig;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;


public class WMSRenderingForm extends ActionForm {
    List svgRenderers;
    String svgRenderer;
    boolean svgAntiAlias;
    List intTypes;
    String allowInterpolation;

    /*
     * Because of the way that STRUTS works, if the user does not check the enabled box,
     * or unchecks it, setEnabled() is never called, thus we must monitor setEnabled()
     * to see if it doesn't get called. This must be accessible, as ActionForms need to
     * know about it -- there is no way we can tell whether we are about to be passed to
     * an ActionForm or not.
     *
     * Probably a better way to do this, but I can't think of one.
     * -rgould
     *
     * TODO: Hey richard Jody here - Struts knows that boolean properties are
     * not set if the user does nothing. Apparently that is why the reset
     * method exists.
     * Reset is called *every* time on ActionForm. Before the populate
     * process has a go at things.
     *
     * The problem is that reset() retrieves the WFS's config enabled value
     * and uses that to pre-populate the form. Thus, if they deselect it, setEnabled is
     * never called, and enabled still remains true. The way I have done it isn't simple,
     * but it works just fine.
     */
    private boolean svgAntiAliasChecked = false;

    public WMSRenderingForm() {
        svgRenderers = new ArrayList();
        svgRenderers.add(WMSConfig.SVG_SIMPLE);
        svgRenderers.add(WMSConfig.SVG_BATIK);
        svgAntiAlias = true;
        intTypes = new ArrayList();
        intTypes.add(WMSConfig.INT_NEAREST);
        intTypes.add(WMSConfig.INT_BIlINEAR);
        intTypes.add(WMSConfig.INT_BICUBIC);
    }

    public void reset(ActionMapping mapping, HttpServletRequest request) {
        super.reset(mapping, request);

        ServletContext context = getServlet().getServletContext();
        WMSConfig config = (WMSConfig) context.getAttribute(WMSConfig.CONFIG_KEY);

        svgRenderer = config.getSvgRenderer();

        if (svgRenderer == null) {
            svgRenderer = WMSConfig.SVG_SIMPLE;
        }

        svgAntiAlias = config.getSvgAntiAlias();

        allowInterpolation = config.getAllowInterpolation();

        if (allowInterpolation == null) {
            allowInterpolation = WMSConfig.INT_BIlINEAR;
        }
    }

    public ActionErrors validate(ActionMapping mapping,
        HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();

        return errors;
    }

    public void setSvgRenderer(String svgRenderer) {
        this.svgRenderer = svgRenderer;
    }

    public String getSvgRenderer() {
        return svgRenderer;
    }

    public List getSvgRenderers() {
        return svgRenderers;
    }

    /**
     * @param svgAntiAlias anti alias hint.
     */
    public void setSvgAntiAlias(boolean svgAntiAlias) {
        svgAntiAliasChecked = true;
        this.svgAntiAlias = svgAntiAlias;
    }

    /**
     * @return The value of the anti aliasing rendering hint.
     */
    public boolean getSvgAntiAlias() {
        return svgAntiAlias;
    }

    /**
     * DOCUMENT ME!
     *
     * @return
     */
    public boolean isSvgAntiAliasChecked() {
        return svgAntiAliasChecked;
    }

    /**
     * @param allowInterpolation interpolation rendering hint.
     */
    public void setAllowInterpolation(String allowInterpolation) {
        this.allowInterpolation = allowInterpolation;
    }

    /**
     * @return The value of the interpolation rendering hint.
     */
    public String getAllowInterpolation() {
        return allowInterpolation;
    }

    public List getIntTypes() {
        return intTypes;
    }

    /**
     * DOCUMENT ME!
     *
     * @param b
     */
    public void setEnabledChecked(boolean svgAntiAliasChecked) {
        this.svgAntiAliasChecked = svgAntiAliasChecked;
    }
}
