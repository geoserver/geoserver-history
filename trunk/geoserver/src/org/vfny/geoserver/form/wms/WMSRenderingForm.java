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
	
	public WMSRenderingForm() {
		svgRenderers = new ArrayList();
		svgRenderers.add(WMSConfig.SVG_SIMPLE);
		svgRenderers.add(WMSConfig.SVG_BATIK);
		svgAntiAlias = true;
	}
	public void reset(ActionMapping mapping, HttpServletRequest request) {
        super.reset(mapping, request);

        ServletContext context = getServlet().getServletContext();
        WMSConfig config = (WMSConfig) context.getAttribute(WMSConfig.CONFIG_KEY);

        svgRenderer = config.getSvgRenderer();
        if (svgRenderer == null) 
        	svgRenderer = WMSConfig.SVG_SIMPLE;
        
        svgAntiAlias = config.getSvgAntiAlias();
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
    	this.svgAntiAlias = svgAntiAlias;
    }
    
    /**
     * @return The value of the anti aliasing rendering hint.
     */
    public boolean getSvgAntiAlias() {
    	return svgAntiAlias;
    }
}
