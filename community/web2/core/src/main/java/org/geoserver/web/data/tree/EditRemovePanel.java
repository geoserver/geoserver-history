package org.geoserver.web.data.tree;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ResourceReference;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.behavior.IBehavior;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.geoserver.catalog.Catalog;
import org.geoserver.catalog.DataStoreInfo;
import org.geoserver.catalog.ResourceInfo;
import org.geoserver.web.GeoServerApplication;
import org.geoserver.web.data.ResourceConfigurationPage;
import org.geoserver.web.data.datastore.DataStoreConfiguration;

/**
 * A simple component that can be used to edit/remove items from the tree
 * 
 * @author aaime
 * 
 */
public class EditRemovePanel extends Panel {

    private final AbstractCatalogNode node;

    public EditRemovePanel(String id, AbstractCatalogNode node) {
        super(id);
        this.node = node;

        AjaxLink link = new AjaxLink("edit") {

            @Override
            public void onClick(AjaxRequestTarget target) {
                onEditClick(target);
            }
        };
        Image icon = new Image("editIcon", new ResourceReference(GeoServerApplication.class,
                "img/icons/silk/pencil.png"));
        icon.add(new AttributeModifier("title", true, new Model("Edit " + node.getNodeLabel()))); 
        link.add(icon);
        add(link);

        link = new AjaxLink("remove") {

            @Override
            public void onClick(AjaxRequestTarget target) {
                onRemoveClick(target);
            }
        };
        icon = new Image("removeIcon", new ResourceReference(GeoServerApplication.class,
                "img/icons/silk/delete.png"));
        icon.add(new AttributeModifier("title", true, new Model("Remove " + node.getNodeLabel())));
        link.add(icon);
        add(link);
    }

    protected void onRemoveClick(AjaxRequestTarget target) {
        System.out.println("Removed!");

    }

    protected void onEditClick(AjaxRequestTarget target) {
        if (node instanceof WorkspaceNode) {
            System.out.println("Edit workspace node " + node.getNodeLabel());
        } else if (node instanceof DataStoreNode) {
            final String datastoreUniqueName = node.getNodeLabel();

            final Catalog catalog = node.getCatalog();
            final DataStoreInfo dataStore = catalog.getDataStoreByName(datastoreUniqueName);
            final String dataStoreInfoId = dataStore.getId();

            setResponsePage(new DataStoreConfiguration(dataStoreInfoId));

        } else if (node instanceof CoverageStoreNode) {
            System.out.println("Edit coverage node " + node.getNodeLabel());
        } else if (node instanceof ResourceNode) {
            System.out.println("Edit resource " + node.getNodeLabel());
            ResourceInfo resourceInfo = (ResourceInfo)node.getModel();
            setResponsePage(new ResourceConfigurationPage(resourceInfo));
        } else {
            throw new IllegalStateException("Don't know how to edit a "
                    + node.getClass().getSimpleName());
        }
    }
}
