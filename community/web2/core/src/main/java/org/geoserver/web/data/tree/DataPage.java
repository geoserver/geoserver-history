package org.geoserver.web.data.tree;

import java.io.Serializable;
import java.util.Enumeration;

import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ResourceReference;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.markup.html.tree.table.AbstractColumn;
import org.apache.wicket.extensions.markup.html.tree.table.AbstractTreeColumn;
import org.apache.wicket.extensions.markup.html.tree.table.ColumnLocation;
import org.apache.wicket.extensions.markup.html.tree.table.IColumn;
import org.apache.wicket.extensions.markup.html.tree.table.IRenderable;
import org.apache.wicket.extensions.markup.html.tree.table.TreeTable;
import org.apache.wicket.extensions.markup.html.tree.table.ColumnLocation.Alignment;
import org.apache.wicket.extensions.markup.html.tree.table.ColumnLocation.Unit;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.markup.html.tree.ITreeStateListener;
import org.apache.wicket.model.Model;
import org.geoserver.catalog.WorkspaceInfo;
import org.geoserver.web.GeoServerBasePage;
import org.geoserver.web.data.NewDataPage;

public class DataPage extends GeoServerBasePage {

    DataTreeTable tree;

    public DataPage() {
        WebMarkupContainer treeContainer = new WebMarkupContainer("treeParent");
        treeContainer.setOutputMarkupId(true);
        SelectionColumn selectionColumn = new SelectionColumn();
        CatalogNameColumn catalogNameColumn = new CatalogNameColumn();
        tree = new DataTreeTable("dataTree", new DefaultTreeModel(
                new CatalogRootNode()), new IColumn[] { selectionColumn,
                catalogNameColumn });

        tree.setRootLess(true);
        tree.getTreeState().setAllowSelectMultiple(false);
        tree.getTreeState().addTreeStateListener(new DataTreeListener());

        treeContainer.add(tree);
        add(treeContainer);

        Form form = new Form("controlForm");
        form.add(new AjaxButton("collapseAll") {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form form) {
                tree.getTreeState().collapseAll();
                target.addComponent(tree);
            }
        });
        add(form);
    }

    class UnconfiguredFeatureTypesPanel extends LinkPanel {

        public UnconfiguredFeatureTypesPanel(String id, DataTreeTable tree,
                MarkupContainer parent, AbstractCatalogNode node, int level) {
            super(id, tree, parent, node, level);
            label
                    .add(new AttributeModifier("class", true, new Model(
                            "command")));
        }

        @Override
        protected void onClick(AjaxRequestTarget target) {
            ((DataStoreNode) node.getParent())
                    .setUnconfiguredChildrenVisible(true);
            tree.getTreeState().expandNode(node.getParent());
            target.addComponent(tree.getParent());
        }

        @Override
        protected ResourceReference getNodeIcon(DataTreeTable tree,
                TreeNode node) {
            return null;
        }

    }

    class NewDataStorePanel extends LinkPanel {

        public NewDataStorePanel(String id, DataTreeTable tree,
                MarkupContainer parent, AbstractCatalogNode node, int level) {
            super(id, tree, parent, node, level);
            label
                    .add(new AttributeModifier("class", true, new Model(
                            "command")));
        }

        @Override
        protected void onClick(AjaxRequestTarget target) {
            final WorkspaceInfo workspace = ((NewDatastoreNode)node).getModel();
            final String workspaceId = workspace.getId();
            setResponsePage(new NewDataPage(workspaceId));
        }

        @Override
        protected ResourceReference getNodeIcon(DataTreeTable tree,
                TreeNode node) {
            return null;
        }
    }

    class UnconfiguredFeatureTypePanel extends LabelPanel {

        public UnconfiguredFeatureTypePanel(String id, DataTreeTable tree,
                MarkupContainer parent, AbstractCatalogNode node, int level) {
            super(id, tree, parent, node, level);
            label.add(new AttributeModifier("class", true, new Model(
                    "unconfiguredLayer")));
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

    class CatalogNameColumn extends AbstractTreeColumn implements IColumn {

        public CatalogNameColumn() {
            super(new ColumnLocation(Alignment.MIDDLE, 20, Unit.PROPORTIONAL),
                    "Catalog");
        }

        @Override
        public String renderNode(TreeNode node) {
            return ((AbstractCatalogNode) node).getNodeLabel();
        }

        @Override
        public Component newCell(MarkupContainer parent, String id,
                TreeNode node, int level) {
            if (node instanceof UnconfiguredFeatureTypesNode) {
                return new UnconfiguredFeatureTypesPanel(id, tree, parent,
                        (AbstractCatalogNode) node, level);
            }
            if (node instanceof UnconfiguredFeatureTypeNode) {
                return new UnconfiguredFeatureTypePanel(id, tree, parent,
                        (AbstractCatalogNode) node, level);
            }
            if (node instanceof ResourceNode) {
                return new LabelPanel(id, tree, parent,
                        (AbstractCatalogNode) node, level);
            }
            if (node instanceof NewDatastoreNode) {
                return new NewDataStorePanel(id, tree, parent,
                        (AbstractCatalogNode) node, level);
            } else {
                return super.newCell(parent, id, node, level);
            }

        }
    }

    class SelectionColumn extends AbstractColumn {

        public SelectionColumn() {
            super(new ColumnLocation(Alignment.LEFT, 50, Unit.PX), "");
        }

        public Component newCell(MarkupContainer parent, String id,
                TreeNode node, int level) {
            return new EmptyPanel(id);
        }

        public IRenderable newCell(TreeNode node, int level) {
            return null;
        }

    }

    final class DataTreeListener extends TreeAdapter implements Serializable {

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

    }

}
