package org.geoserver.web.data.tree;

import javax.swing.tree.TreeNode;

import org.apache.wicket.ResourceReference;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.panel.Panel;
import org.geoserver.web.GeoServerApplication;
import org.geoserver.web.data.tree.AbstractCatalogNode.SelectionState;

public class SelectionPanel extends Panel {

    public SelectionPanel(String id, final TreeNode node,
            final DataTreeTable tree) {
        super(id);
        final AbstractCatalogNode catalogNode = ((AbstractCatalogNode) node);
        final Image img = new Image("img", getImageResource(catalogNode));
        AjaxLink link = new AjaxLink("link") {

            @Override
            public void onClick(AjaxRequestTarget target) {
                catalogNode.nextSelectionState();
                img.setImageResourceReference(getImageResource(catalogNode));
                target.addComponent(tree.getParent());
            }
        };
        add(link);
    }

    ResourceReference getImageResource(AbstractCatalogNode node) {
        if (node.getSelectionState() == SelectionState.SELECTED)
            return new ResourceReference(GeoServerApplication.class,
                    "img/icons/checkbox_checked.png");
        else if (node.getSelectionState() == SelectionState.PARTIAL)
            return new ResourceReference(GeoServerApplication.class,
                    "img/icons/checkbox_indeterminate.png");
        else
            return new ResourceReference(GeoServerApplication.class,
                    "img/icons/checkbox_unchecked.png");
    }

}
