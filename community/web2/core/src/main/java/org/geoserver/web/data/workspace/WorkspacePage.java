/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.web.data.workspace;

import static org.geoserver.web.data.workspace.WorkspaceProvider.NAME;
import static org.geoserver.web.data.workspace.WorkspaceProvider.REMOVE;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.geoserver.catalog.CascadeDeleteVisitor;
import org.geoserver.catalog.WorkspaceInfo;
import org.geoserver.web.GeoServerSecuredPage;
import org.geoserver.web.data.ConfirmRemovalPanel;
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
    
    public WorkspacePage() {
        
        BookmarkablePageLink newLink = new BookmarkablePageLink("new", WorkspaceNewPage.class);
        add( newLink );

        add(workspaces = new GeoServerTablePanel<WorkspaceInfo>("table", provider) {
            @Override
            protected Component getComponentForProperty(String id, IModel itemModel,
                    Property<WorkspaceInfo> property) {
                if ( property == NAME ) {
                    return workspaceLink(id, itemModel);
                }
                if ( property == REMOVE ) {
                    return removeWorkspaceLink(id, itemModel);
                }
                
                throw new IllegalArgumentException("No such property "+ property.getName());
            }
        });
        workspaces.setOutputMarkupId(true);
        
        add(dialog = new GeoServerDialog("dialog"));        
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
    
    Component removeWorkspaceLink(String id, final IModel itemModel) {
        final WorkspaceInfo workspace = (WorkspaceInfo) itemModel.getObject();

        ResourceModel resRemove = new ResourceModel("removeWorkspace", "Remove");

        return new SimpleAjaxLink(id, null, resRemove) {

            @Override
            protected void onClick(AjaxRequestTarget target) {
                dialog.showOkCancel(target, new GeoServerDialog.DialogDelegate() {
                    protected Component getContents(String id) {
                        return new ConfirmRemovalPanel(id, workspace);
                    }
                    
                    protected boolean onSubmit(AjaxRequestTarget target) {
                        CascadeDeleteVisitor visitor = new CascadeDeleteVisitor(getCatalog());
                        workspace.accept(visitor);
                        target.addComponent(workspaces);
                        return true;
                    }
                    
                });
            }
            
        };
    }
}