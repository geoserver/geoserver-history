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
import org.apache.wicket.validation.validator.UrlValidator;
import org.geoserver.catalog.Catalog;
import org.geoserver.catalog.NamespaceInfo;
import org.geoserver.catalog.WorkspaceInfo;
import org.geoserver.web.GeoServerSecuredPage;
import org.geoserver.web.data.namespace.NamespaceDetachableModel;

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
                setResponsePage(WorkspacePage.class);
            }
        };
        add(form);
        TextField uri = new TextField("uri", new PropertyModel(nsModel, "uRI"));
        uri.add(new UrlValidator());
        form.add(uri);
        
        //stores
//        StorePanel storePanel = new StorePanel("storeTable", new StoreProvider(ws), false);
//        form.add(storePanel);
        
        form.add(new SubmitLink("save"));
        form.add(new BookmarkablePageLink("cancel", WorkspacePage.class));
        
     
    }
    
}
