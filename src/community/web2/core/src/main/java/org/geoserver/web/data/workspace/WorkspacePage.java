/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.web.data.workspace;

import static org.geoserver.web.data.workspace.WorkspaceProvider.NAME;

import java.util.Iterator;
import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.model.IModel;
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
        add(removeWorkspacesLink());

        add(workspaces = new GeoServerTablePanel<WorkspaceInfo>("table", provider, true) {
            @Override
            protected Component getComponentForProperty(String id, IModel itemModel,
                    Property<WorkspaceInfo> property) {
                if ( property == NAME ) {
                    return workspaceLink(id, itemModel);
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
    
    Component removeWorkspacesLink() {
        return new AjaxLink("removeSelected", null) {

            @Override
            public void onClick(AjaxRequestTarget target) {
                // see if the user selected anything
                final List<WorkspaceInfo> selection = workspaces.getSelection();
                if(selection.size() == 0)
                    return;
                
                // if there is something to cancel, let's warn the user about what
                // could go wrong, and if the user accepts, let's delete what's needed
                dialog.showOkCancel(target, new GeoServerDialog.DialogDelegate() {
                    protected Component getContents(String id) {
                        return new ConfirmRemovalPanel(id, selection);
                    }
                    
                    protected boolean onSubmit(AjaxRequestTarget target) {
                        CascadeDeleteVisitor visitor = new CascadeDeleteVisitor(getCatalog());
                        for (WorkspaceInfo wi : selection) {
                            wi.accept(visitor);
                        }
                        workspaces.clearSelection();
                        return true;
                    }
                    
                    @Override
                    public void onClose(AjaxRequestTarget target) {
                        target.addComponent(workspaces);
                    }
                    
                });
             }
            
        };
    }
}