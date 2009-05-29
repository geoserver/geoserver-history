/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.wms.web;

import java.util.Arrays;
import java.util.List;

import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.validation.validator.NumberValidator;
import org.geoserver.web.services.BaseServiceAdminPage;
import org.geoserver.web.util.MapModel;
import org.geoserver.wms.WMS;
import org.geoserver.wms.WMSInfo;
import org.geoserver.wms.WMSInfo.WMSInterpolation;
import org.geoserver.wms.WatermarkInfo.Position;

/**
 * Edits the WMS service details 
 */
@SuppressWarnings("serial")
public class WMSAdminPage extends BaseServiceAdminPage<WMSInfo> {
    
    static final List<String> SVG_RENDERERS = Arrays.asList(new String[] {WMS.SVG_BATIK, WMS.SVG_SIMPLE});
    
    protected Class<WMSInfo> getServiceClass() {
        return WMSInfo.class;
    }
    
    protected void build(IModel info, Form form) {
        // general
    	form.add(new DropDownChoice("interpolation", Arrays.asList(WMSInfo.WMSInterpolation.values()), new InterpolationRenderer()));
    	// resource limits
    	TextField maxMemory = new TextField("maxRequestMemory");
    	maxMemory.add(NumberValidator.minimum(0.0));
    	form.add(maxMemory);
    	TextField maxTime = new TextField("maxRenderingTime");
    	maxTime.add(NumberValidator.minimum(0.0));
        form.add(maxTime);
        TextField maxErrors = new TextField("maxRenderingErrors");
        maxErrors.add(NumberValidator.minimum(0.0));
        form.add(maxErrors);
    	// watermark
    	form.add(new CheckBox("watermark.enabled"));
    	form.add(new TextField("watermark.uRL"));
    	TextField transparency = new TextField("watermark.transparency");
    	transparency.add(NumberValidator.range(0, 100));
        form.add(transparency);
    	form.add(new DropDownChoice("watermark.position", Arrays.asList(Position.values()), new WatermarkPositionRenderer()));
    	// svg
    	PropertyModel metadataModel = new PropertyModel(info, "metadata");
        form.add(new CheckBox("svg.antialias", new MapModel(metadataModel, "svgAntiAlias")));
    	form.add(new DropDownChoice("svg.producer", new MapModel(metadataModel, "svgRenderer"), SVG_RENDERERS, new SVGMethodRenderer()));
    }
    
    protected String getServiceName(){
        return "WMS";
    }

    private class WatermarkPositionRenderer implements  IChoiceRenderer {

        public Object getDisplayValue(Object object) {
            return new StringResourceModel(((Position) object).name(), WMSAdminPage.this, null).getString();
        }

        public String getIdValue(Object object, int index) {
            return ((Position) object).name();
        }
        
    }
    
    private class InterpolationRenderer implements  IChoiceRenderer {

        public Object getDisplayValue(Object object) {
            return new StringResourceModel(((WMSInterpolation) object).name(), WMSAdminPage.this, null).getString();
        }

        public String getIdValue(Object object, int index) {
            return ((WMSInterpolation) object).name();
        }
        
    }
    
    private class SVGMethodRenderer implements  IChoiceRenderer {

        public Object getDisplayValue(Object object) {
            return new StringResourceModel("svg." + object, WMSAdminPage.this, null).getString();
        }

        public String getIdValue(Object object, int index) {
            return (String) object;
        }
        
    }
}
