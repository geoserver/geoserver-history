package org.geoserver.wcs.web;

import org.apache.wicket.markup.html.form.Form;
import org.geoserver.web.services.BaseServiceAdminPage;
import org.geoserver.wcs.WCSInfo;

public class WCSAdminPage extends BaseServiceAdminPage<WCSInfo> {

    protected Class<WCSInfo> getServiceClass() {
        return WCSInfo.class;
    }
    
    protected void build(WCSInfo info, Form form) {
        
        //not much here

    }
    
}
