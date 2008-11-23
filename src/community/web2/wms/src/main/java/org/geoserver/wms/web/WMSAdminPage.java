/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.wms.web;

import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.model.IModel;
import org.geoserver.web.services.BaseServiceAdminPage;
import org.geoserver.wms.web.data.StylesPage;
import org.geoserver.wms.WMSInfo;
import org.geoserver.wms.WatermarkInfo;

import java.util.Arrays;

public class WMSAdminPage extends BaseServiceAdminPage<WMSInfo> {
    protected Class<WMSInfo> getServiceClass() {
        return WMSInfo.class;
    }
    
    protected void build(IModel info, Form form) {
    	form.add(new TextField("interpolation"));
    	form.add(new CheckBox("watermark.enabled"));
    	form.add(new TextField("watermark.uRL"));
    	form.add(new TextField("watermark.transparency"));

    	form.add(new DropDownChoice("watermark.position", Arrays.asList(WatermarkInfo.Position.values())));
    }
    
    protected String getServiceName(){
        return "WMS";
    }
    
}
