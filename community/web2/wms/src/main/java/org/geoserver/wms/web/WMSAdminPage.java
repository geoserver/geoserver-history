package org.geoserver.wms.web;

import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.CheckBox;
import org.geoserver.web.services.BaseServiceAdminPage;
import org.geoserver.wms.WMSInfo;
import org.geoserver.wms.WatermarkInfo;

import java.util.Arrays;

public class WMSAdminPage extends BaseServiceAdminPage<WMSInfo> {

	
    protected Class<WMSInfo> getServiceClass() {
        return WMSInfo.class;
    }
    
    protected void build(WMSInfo info, Form form) {
        
    	form.add( new TextField("interpolation"));
    	form.add( new CheckBox("watermark.enabled"));
    	form.add( new TextField("watermark.uRL"));
    	form.add( new TextField("watermark.transparency"));

    	form.add(new DropDownChoice("watermark.position", Arrays.asList(WatermarkInfo.Position.values())));
        

    }
    
}
