/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.web.data.workspace;

import static org.geoserver.web.data.workspace.WorkspaceProvider.NAME;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.model.IModel;
import org.geoserver.catalog.WorkspaceInfo;
import org.geoserver.web.GeoServerSecuredPage;
import org.geoserver.web.data.SelectionRemovalLink;
import org.geoserver.web.wicket.GeoServerDialog;
import org.geoserver.web.wicket.GeoServerTablePanel;
import org.geoserver.web.wicket.SimpleAjaxLink;
import org.geoserver.web.wicket.GeoServerDataProvider.Property;

/**
 * Lists available workspaces, links to them, allows for addition and removal
 */
@SuppressWarnings("serial")
public class WorkspacePage extends GeoServerSecuredPage {
    WorkspaceProvider provider = new WorkspaceProvider();
    GeoServerTablePanel<WorkspaceInfo> workspaces;
    GeoServerDialog dialog;
    SelectionRemovalLink remove;
    
    public WorkspacePage() {
        // add new workspace link        
        BookmarkablePageLink newLink = new BookmarkablePageLink("new", WorkspaceNewPage.class);
        add( newLink );

        // the middle table
        add(workspaces = new GeoServerTablePanel<WorkspaceInfo>("table", provider, true) {
            @Override
            protected Component getComponentForProperty(String id, IModel itemModel,
                    Property<WorkspaceInfo> property) {
                if ( property == NAME ) {
                    return workspaceLink(id, itemModel);
                }
                
                throw new IllegalArgumentException("No such property "+ property.getName());
            }
            
            @Override
            protected void onSelectionUpdate(AjaxRequestTarget target) {
                remove.setEnabled(workspaces.getSelection().size() > 0);
                target.addComponent(remove);    
            }
        });
        workspaces.setOutputMarkupId(true);
        
        // the confirm dialog
        add(dialog = new GeoServerDialog("dialog"));
        
        // the cascade removal link
        add(remove = new SelectionRemovalLink("removeSelected", workspaces, dialog));
        remove.setOutputMarkupId(true);
        remove.setEnabled(false);
    }
    
    Component workspaceLink(String id, final IModel itemModel) {
        return new SimpleAjaxLink(id, WorkspaceProvider.NAME.getModel(itemModel)) {
            @Override
            public void onClick(AjaxRequestTarget target) {
                WorkspaceInfo ws = (WorkspaceInfo) itemModel.getObject();
                setResponsePage( new WorkspaceEditPage( ws ) );
            }
        };
    }
    
}