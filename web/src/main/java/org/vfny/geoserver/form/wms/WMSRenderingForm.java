package org.vfny.geoserver.form.wms;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.vfny.geoserver.config.WMSConfig;

public class WMSRenderingForm extends ActionForm {
	
	List svgRenderers;
	String svgRenderer;
	boolean svgAntiAlias;
	boolean allowInterpolation;
	
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

    private boolean allowInterpolationChecked = false;

	public WMSRenderingForm() {
		svgRenderers = new ArrayList();
		svgRenderers.add(WMSConfig.SVG_SIMPLE);
		svgRenderers.add(WMSConfig.SVG_BATIK);
		svgAntiAlias = true;
		allowInterpolation = true;
	}
	public void reset(ActionMapping mapping, HttpServletRequest request) {
        super.reset(mapping, request);

        ServletContext context = getServlet().getServletContext();
        WMSConfig config = (WMSConfig) context.getAttribute(WMSConfig.CONFIG_KEY);

        svgRenderer = config.getSvgRenderer();
        if (svgRenderer == null) 
        	svgRenderer = WMSConfig.SVG_SIMPLE;
        
        svgAntiAlias = config.getSvgAntiAlias();
        allowInterpolation = config.getAllowInterpolation();
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
    public void setAllowInterpolation(boolean allowInterpolation) {
    	allowInterpolationChecked = true;
    	this.allowInterpolation = allowInterpolation;
    }
    
    /**
     * @return The value of the interpolation rendering hint.
     */
    public boolean getAllowInterpolation() {
    	return allowInterpolation;
    }
    
    /**
     * DOCUMENT ME!
     *
     * @return
     */
    public boolean isAllowInterpolationChecked() {
        return allowInterpolationChecked;
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
