/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.web.data.workspace;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.geoserver.catalog.Catalog;
import org.geoserver.catalog.NamespaceInfo;
import org.geoserver.catalog.WorkspaceInfo;
import org.geoserver.web.GeoServerSecuredPage;
import org.geoserver.web.data.namespace.NamespaceDetachableModel;
import org.geoserver.web.data.store.StorePanel;
import org.geoserver.web.data.store.StoreProvider;

/**
 * Allows editing a specific workspace
 */
@SuppressWarnings("serial")
public class WorkspaceEditPage extends GeoServerSecuredPage {

    IModel wsModel;
    IModel nsModel;
    
    public WorkspaceEditPage( WorkspaceInfo ws ) {
        
        wsModel = new WorkspaceDetachableModel( ws );
        add( new Label("name", new PropertyModel( wsModel , "name" ) ) );

        NamespaceInfo ns = getCatalog().getNamespaceByPrefix( ws.getName() );
        nsModel = new NamespaceDetachableModel(ns);
        
        Form form = new Form( "form", new CompoundPropertyModel( nsModel ) ) {
            protected void onSubmit() {
                Catalog catalog = getCatalog();
                catalog.save( (NamespaceInfo) nsModel.getObject() );
                catalog.save( (WorkspaceInfo) wsModel.getObject() );
            }
        };
        add(form);
        form.add(new TextField("uRI"));
        
        //stores
        StorePanel storePanel = new StorePanel( "storeTable", new StoreProvider( ws ) );
        form.add(storePanel);
        
        form.add(new SubmitLink("save"){
            @Override
            public void onSubmit() {
                super.onSubmit();
            }
        });
        form.add(new BookmarkablePageLink("cancel", WorkspacePage.class));
        
     
    }
    
}
