package org.geoserver.wfs.web;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.Radio;
import org.apache.wicket.markup.html.form.RadioGroup;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.Model;
import org.geoserver.web.services.BaseServiceAdminPage;
import org.geoserver.wfs.WFSInfo;

public class WFSAdminPage extends BaseServiceAdminPage<WFSInfo> {

    protected Class<WFSInfo> getServiceClass() {
        return WFSInfo.class;
    }
    
    protected void build(WFSInfo info, Form form) {
        
        //service level
        RadioGroup sl = new RadioGroup( "serviceLevel" );
        form.add( sl );
        
        sl.add( new Radio( "basic", new Model( WFSInfo.ServiceLevel.BASIC ) ) );
        sl.add( new Radio( "transactional", new Model( WFSInfo.ServiceLevel.TRANSACTIONAL  ) ) );
        sl.add( new Radio( "complete", new Model( WFSInfo.ServiceLevel.COMPLETE ) ) );
        
        //max features
        form.add( new TextField( "maxFeatures" ) );
    }
    
}
