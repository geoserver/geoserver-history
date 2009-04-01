/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.wms.web;

import java.util.Arrays;

import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.validation.validator.NumberValidator;
import org.geoserver.web.services.BaseServiceAdminPage;
import org.geoserver.wms.WMSInfo;
import org.geoserver.wms.WMSInfo.WMSInterpolation;
import org.geoserver.wms.WatermarkInfo.Position;

/**
 * Edits the WMS service details 
 */
@SuppressWarnings("serial")
public class WMSAdminPage extends BaseServiceAdminPage<WMSInfo> {
    
    protected Class<WMSInfo> getServiceClass() {
        return WMSInfo.class;
    }
    
    protected void build(IModel info, Form form) {
    	form.add(new DropDownChoice("interpolation", Arrays.asList(WMSInfo.WMSInterpolation.values()), new InterpolationRenderer()));
    	form.add(new CheckBox("watermark.enabled"));
    	form.add(new TextField("watermark.uRL"));
    	TextField transparency = new TextField("watermark.transparency");
    	transparency.add(NumberValidator.range(0, 100));
        form.add(transparency);
    	form.add(new DropDownChoice("watermark.position", Arrays.asList(Position.values()), new WatermarkPositionRenderer()));
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
}
