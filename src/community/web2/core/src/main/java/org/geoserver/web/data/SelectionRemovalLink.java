/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.web.data;

import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.geoserver.catalog.CascadeDeleteVisitor;
import org.geoserver.catalog.Catalog;
import org.geoserver.catalog.CatalogInfo;
import org.geoserver.web.GeoServerApplication;
import org.geoserver.web.wicket.GeoServerDialog;
import org.geoserver.web.wicket.GeoServerTablePanel;

/**
 * A reusable cascading, multiple removal link. Assumes the presence of a table
 * panel filled with catalog objects and a {@link GeoServerDialog} to be used
 * for reporting the objects that will be affected by the removal
 */
@SuppressWarnings("serial")
public class SelectionRemovalLink extends AjaxLink {
    
    GeoServerTablePanel<? extends CatalogInfo> catalogObjects;
    GeoServerDialog dialog;

    public SelectionRemovalLink(String id, GeoServerTablePanel<? extends CatalogInfo> catalogObjects, GeoServerDialog dialog) {
        super(id);
        this.catalogObjects = catalogObjects;
        this.dialog = dialog;
    }

    @Override
    public void onClick(AjaxRequestTarget target) {
        // see if the user selected anything
        final List<? extends CatalogInfo> selection = catalogObjects.getSelection();
        if(selection.size() == 0)
            return;
        
        // if there is something to cancel, let's warn the user about what
        // could go wrong, and if the user accepts, let's delete what's needed
        dialog.showOkCancel(target, new GeoServerDialog.DialogDelegate() {
            protected Component getContents(String id) {
                // show a confirmation panel for all the objects we have to remove
                return new ConfirmRemovalPanel(id, selection);
            }
            
            protected boolean onSubmit(AjaxRequestTarget target) {
                // cascade delete the whole selection
                Catalog catalog = GeoServerApplication.get().getCatalog();
                CascadeDeleteVisitor visitor = new CascadeDeleteVisitor(catalog);
                for (CatalogInfo ci : selection) {
                    ci.accept(visitor);
                }
                
                // the deletion will have changed what we see in the page
                // so better clear out the selection
                catalogObjects.clearSelection();
                return true;
            }
            
            @Override
            public void onClose(AjaxRequestTarget target) {
                // if the selection has been cleared out it's sign a deletion
                // occurred, so refresh the table
                if(catalogObjects.getSelection().size() == 0) {
                    setEnabled(false);
                    target.addComponent(SelectionRemovalLink.this);
                    target.addComponent(catalogObjects);
                }
            }
            
        });

    }

}
