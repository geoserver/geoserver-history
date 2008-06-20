package org.geoserver.web.data.tree;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ResourceReference;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.markup.html.tree.table.AbstractColumn;
import org.apache.wicket.extensions.markup.html.tree.table.AbstractTreeColumn;
import org.apache.wicket.extensions.markup.html.tree.table.ColumnLocation;
import org.apache.wicket.extensions.markup.html.tree.table.IColumn;
import org.apache.wicket.extensions.markup.html.tree.table.IRenderable;
import org.apache.wicket.extensions.markup.html.tree.table.TreeTable;
import org.apache.wicket.extensions.markup.html.tree.table.ColumnLocation.Alignment;
import org.apache.wicket.extensions.markup.html.tree.table.ColumnLocation.Unit;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.markup.html.tree.ITreeStateListener;
import org.apache.wicket.model.Model;
import org.geoserver.catalog.WorkspaceInfo;
import org.geoserver.web.data.NewDataPage;
import org.geoserver.web.data.datastore.DataStoreConfiguration;
import org.vfny.geoserver.util.DataStoreUtils;

public class DataTreeTable extends TreeTable {

    MarkupContainer container;

    public DataTreeTable(String id, MarkupContainer container) {
        super(id, new DefaultTreeModel(new CatalogRootNode()), new IColumn[] {
                new CatalogNameColumn(), new ItemActionColumn() });

        setRootLess(true);
        getTreeState().setAllowSelectMultiple(false);
        getTreeState().addTreeStateListener(new TreeListener(this));

        this.container = container;
    }

    @Override
    protected Component newTreePanel(MarkupContainer parent, String id, TreeNode node, int level,
            IRenderNodeCallback renderNodeCallback) {
        if (node instanceof UnconfiguredFeatureTypesNode) {
            return new UnconfiguredFeatureTypesPanel(id, this, parent, (AbstractCatalogNode) node,
                    level);
        }
        if (node instanceof UnconfiguredFeatureTypeNode) {
            return new UnconfiguredFeatureTypePanel(id, this, parent, (AbstractCatalogNode) node,
                    level);
        }
        if (node instanceof ResourceNode) {
            return new LabelPanel(id, this, parent, (AbstractCatalogNode) node, level);
        }
        if (node instanceof NewDatastoreNode) {
            return new NewDataStorePanel(id, this, parent, (AbstractCatalogNode) node, level);
        } else {
            return super.newTreePanel(parent, id, node, level, renderNodeCallback);
        }
    }

    protected TreeNode getWorkspaceNode(TreeNode selected) {
        TreeNode node = selected;
        while (node != null && !(node instanceof WorkspaceNode)) {
            node = node.getParent();
        }
        return node;
    }

    @Override
    public Component newIndentation(MarkupContainer parent, String id, TreeNode node, int level) {
        return super.newIndentation(parent, id, node, level);
    }

    @Override
    public Component newJunctionLink(MarkupContainer parent, String id, String imageId,
            TreeNode node) {
        return super.newJunctionLink(parent, id, imageId, node);
    }

    @Override
    public Component newNodeIcon(MarkupContainer parent, String id, TreeNode node) {
        return super.newNodeIcon(parent, id, node);
    }

    @Override
    public ResourceReference getNodeIcon(TreeNode node) {
        return super.getNodeIcon(node);
    }

    class UnconfiguredFeatureTypesPanel extends LinkPanel {

        public UnconfiguredFeatureTypesPanel(String id, DataTreeTable tree, MarkupContainer parent,
                AbstractCatalogNode node, int level) {
            super(id, tree, parent, node, level);
            label.add(new AttributeModifier("class", true, new Model("command")));
        }

        @Override
        protected void onClick(AjaxRequestTarget target) {
            ((DataStoreNode) node.getParent()).setUnconfiguredChildrenVisible(true);
            getTreeState().expandNode(node.getParent());
            target.addComponent(DataTreeTable.this.getParent());
        }

        @Override
        protected ResourceReference getNodeIcon(DataTreeTable tree, TreeNode node) {
            return null;
        }

    }

    class NewDataStorePanel extends LinkPanel {

        public NewDataStorePanel(String id, DataTreeTable tree, MarkupContainer parent,
                AbstractCatalogNode node, int level) {
            super(id, tree, parent, node, level);
            label.add(new AttributeModifier("class", true, new Model("command")));
        }

        @Override
        protected void onClick(AjaxRequestTarget target) {
            final WorkspaceInfo workspace = ((NewDatastoreNode) node).getModel();
            final String workspaceId = workspace.getId();
            setResponsePage(new NewDataPage(workspaceId));
        }

        @Override
        protected ResourceReference getNodeIcon(DataTreeTable tree, TreeNode node) {
            return null;
        }
    }

    class UnconfiguredFeatureTypePanel extends LabelPanel {

        public UnconfiguredFeatureTypePanel(String id, DataTreeTable tree, MarkupContainer parent,
                AbstractCatalogNode node, int level) {
            super(id, tree, parent, node, level);
            label.add(new AttributeModifier("class", true, new Model("unconfiguredLayer")));
        }
    }

    // static class CatalogTreeModel extends LoadableDetachableModel {
    //
    // @Override
    // protected Object load() {
    // return new DefaultTreeModel(new CatalogRootNode());
    // }
    //
    // }

    static class CatalogNameColumn extends AbstractTreeColumn implements IColumn {

        public CatalogNameColumn() {
            super(new ColumnLocation(Alignment.MIDDLE, 20, Unit.PROPORTIONAL), "Catalog");
        }

        @Override
        public String renderNode(TreeNode node) {
            return ((AbstractCatalogNode) node).getNodeLabel();
        }
    }

    static class ItemActionColumn extends AbstractColumn {

        public ItemActionColumn() {
            super(new ColumnLocation(Alignment.LEFT, 50, Unit.PX), "");
        }

        public Component newCell(MarkupContainer parent, String id, TreeNode node, int level) {
            if (!(node instanceof AbstractPlaceholderNode))
                return new EditRemovePanel(id, (AbstractCatalogNode) node);
            else
                return new EmptyPanel(id);
        }

        public IRenderable newCell(TreeNode node, int level) {
            return null;
        }

    }

    final class TreeListener implements ITreeStateListener, Serializable {
        private final TreeTable tree;

        private TreeListener(TreeTable tree) {
            this.tree = tree;
        }

        public void nodeUnselected(TreeNode node) {
            // TODO Auto-generated method stub

        }

        public void nodeSelected(TreeNode selected) {
            if (selected instanceof UnconfiguredFeatureTypesNode) {

            }
            // TreeNode node = getWorkspaceNode(selected);
            // if (node != null) {
            // if (!tree.getTreeState().isNodeSelected(node))
            // tree.getTreeState().selectNode(node, true);
            // }
            if (!tree.getTreeState().isNodeExpanded(selected))
                tree.getTreeState().expandNode(selected);
        }

        public void nodeExpanded(TreeNode node) {
            if (node instanceof WorkspaceNode) {
                Enumeration children = node.getParent().children();
                while (children.hasMoreElements()) {
                    WorkspaceNode ws = (WorkspaceNode) children.nextElement();
                    if (!ws.equals(node))
                        tree.getTreeState().collapseNode(ws);
                }
                tree.getTreeState().selectNode(node, true);
            }

        }

        public void nodeCollapsed(TreeNode node) {
            if (node instanceof DataStoreNode) {
                ((DataStoreNode) node).setUnconfiguredChildrenVisible(false);
            }

        }

        public void allNodesExpanded() {
            // TODO Auto-generated method stub

        }

        public void allNodesCollapsed() {
            // TODO Auto-generated method stub

        }

    }
}
