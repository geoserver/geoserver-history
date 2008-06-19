package org.geoserver.web.data.tree;

import javax.swing.tree.TreeNode;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ResourceReference;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.Model;

public class LabelPanel extends AbstractTreePanel {

    Label label;

    public LabelPanel(String id, DataTreeTable tree, MarkupContainer parent,
            AbstractCatalogNode node, int level) {
        super(id, tree, parent, node, level);

        label = new org.apache.wicket.markup.html.basic.Label("label",
                new Model(node));
        add(label);
    }
    
    @Override
    protected ResourceReference getNodeIcon(DataTreeTable tree, TreeNode node) {
        return null;
    }

}
