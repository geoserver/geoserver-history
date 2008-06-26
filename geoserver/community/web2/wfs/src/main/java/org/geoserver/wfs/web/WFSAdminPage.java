/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.wfs.web;

import java.util.Arrays;
import java.util.List;

import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.Radio;
import org.apache.wicket.markup.html.form.RadioGroup;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.geoserver.web.services.BaseServiceAdminPage;
import org.geoserver.wfs.web.publish.NamespaceManagerPage;
import org.geoserver.wfs.GMLInfo;
import org.geoserver.wfs.WFSInfo;
import org.geoserver.wfs.GMLInfo.SrsNameStyle;

public class WFSAdminPage extends BaseServiceAdminPage<WFSInfo> {

    protected Class<WFSInfo> getServiceClass() {
        return WFSInfo.class;
    }
    
    protected void build(WFSInfo info, Form form) {
        form.add(new BookmarkablePageLink("namespaces", NamespaceManagerPage.class));
        //max features
        form.add( new TextField( "maxFeatures" ) );
        
        //service level
        RadioGroup sl = new RadioGroup( "serviceLevel" );
        form.add( sl );
        sl.add( new Radio( "basic", new Model( WFSInfo.ServiceLevel.BASIC ) ) );
        sl.add( new Radio( "transactional", new Model( WFSInfo.ServiceLevel.TRANSACTIONAL  ) ) );
        sl.add( new Radio( "complete", new Model( WFSInfo.ServiceLevel.COMPLETE ) ) );
        
        //gml 2 
        form.add( new GMLPanel( "gml2", info.getGML().get( WFSInfo.Version.V_10 ) ) );
        form.add( new GMLPanel( "gml3", info.getGML().get( WFSInfo.Version.V_11 ) ) );
    }
    
    static class GMLPanel extends Panel {

        public GMLPanel(String id, GMLInfo info) { 
            super(id, new CompoundPropertyModel( info ) );
            
            //feature bounding
            CheckBox bounding = new CheckBox( "featureBounding" );
            add( bounding );
            
            //srsNameStyle
            List<GMLInfo.SrsNameStyle> choices = 
                Arrays.asList( SrsNameStyle.values() );
            DropDownChoice srsNameStyle = new DropDownChoice( "srsNameStyle", choices );
            add( srsNameStyle );
        }
        
    }
}
