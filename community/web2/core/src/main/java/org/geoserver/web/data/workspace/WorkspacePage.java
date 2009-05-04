/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.web.data.workspace;

import static org.geoserver.web.data.workspace.WorkspaceProvider.*;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.model.StringResourceModel;
import org.geoserver.catalog.CatalogBuilder;
import org.geoserver.catalog.WorkspaceInfo;
import org.geoserver.web.GeoServerSecuredPage;
import org.geoserver.web.wicket.ConfirmationAjaxLink;
import org.geoserver.web.wicket.GeoServerTablePanel;
import org.geoserver.web.wicket.ParamResourceModel;
import org.geoserver.web.wicket.SimpleAjaxLink;
import org.geoserver.web.wicket.GeoServerDataProvider.Property;

/**
 * Lists available workspaces, links to them, allows for addition and removal
 */
@SuppressWarnings("serial")
public class WorkspacePage extends GeoServerSecuredPage {
    WorkspaceProvider provider = new WorkspaceProvider();
    
    public WorkspacePage() {
        
        BookmarkablePageLink newLink = new BookmarkablePageLink("new", WorkspaceNewPage.class);
        add( newLink );

        add(new GeoServerTablePanel<WorkspaceInfo>("table", provider) {
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

        ParamResourceModel confirmRemove = new ParamResourceModel(
                "confirmRemoveWorkspaceX", this, workspace.getName());

        return new ConfirmationAjaxLink(id, null, resRemove, confirmRemove) {
            @Override
            protected void onClick(AjaxRequestTarget target) {
                CatalogBuilder cb = new CatalogBuilder(getCatalog());
                cb.removeWorkspace(workspace, true);
                setResponsePage(WorkspacePage.this);
            }
        };
    }
}
