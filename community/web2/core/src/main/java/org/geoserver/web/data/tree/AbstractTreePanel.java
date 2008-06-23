/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.web.data.tree;

import javax.swing.tree.TreeNode;

import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.RequestCycle;
import org.apache.wicket.ResourceReference;
import org.apache.wicket.extensions.markup.html.tree.DefaultAbstractTree;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;

public class AbstractTreePanel extends Panel {

    TreeNode node;

    public AbstractTreePanel(String id, DataTreeTable tree,
            MarkupContainer parent, TreeNode node, int level) {
        super(id);
        this.node = node;
        add(tree.newIndentation(this, "indent", node, level));
        add(tree.newJunctionLink(this, "link", "image", node));
        Component icon = newNodeIcon(tree, this, "icon", node);
        add(icon);
        if(getNodeIcon(tree, node) == null)
            icon.setVisible(false);
        
    }
    
    /**
     * Creates the icon for current node. By default uses image reference specified by
     * {@link DefaultAbstractTree#getNodeIcon(TreeNode)}.
     * 
     * @param parent
     *            The parent component
     * @param id
     *            The component id
     * @param node
     *            The tree node
     * @return The web component that represents the icon of the current node
     */
    protected Component newNodeIcon(final DataTreeTable tree, MarkupContainer parent, String id, final TreeNode node)
    {
            return new WebMarkupContainer(id)
            {
                    private static final long serialVersionUID = 1L;

                    protected void onComponentTag(ComponentTag tag)
                    {
                            super.onComponentTag(tag);
                            tag.put("style", "background-image: url('" +
                                    RequestCycle.get().urlFor(getNodeIcon(tree, node)) + "')");
                    }
            };
    }
    
    protected ResourceReference getNodeIcon(DataTreeTable tree, TreeNode node) {
        return tree.getNodeIcon(node);
    }

}
