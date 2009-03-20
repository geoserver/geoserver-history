/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.wcs.web;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;
import org.geoserver.wcs.WCSInfo;
import org.geoserver.web.services.BaseServiceAdminPage;

public class WCSAdminPage extends BaseServiceAdminPage<WCSInfo> {
    protected Class<WCSInfo> getServiceClass() {
        return WCSInfo.class;
    }
    
    protected void build(IModel info, Form form) {
        
    }

    protected String getServiceName(){
        return "WCS";
    }
}
