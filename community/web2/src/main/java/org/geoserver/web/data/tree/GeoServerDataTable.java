package org.geoserver.web.data.tree;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;

import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;

import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.markup.html.tree.table.AbstractRenderableColumn;
import org.apache.wicket.extensions.markup.html.tree.table.AbstractTreeColumn;
import org.apache.wicket.extensions.markup.html.tree.table.ColumnLocation;
import org.apache.wicket.extensions.markup.html.tree.table.IColumn;
import org.apache.wicket.extensions.markup.html.tree.table.TreeTable;
import org.apache.wicket.extensions.markup.html.tree.table.ColumnLocation.Alignment;
import org.apache.wicket.extensions.markup.html.tree.table.ColumnLocation.Unit;
import org.apache.wicket.extensions.markup.html.tree.table.TreeTable.IRenderNodeCallback;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.markup.html.tree.ITreeStateListener;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.geoserver.web.GeoServerBasePage;
import org.geoserver.web.data.datastore.DataStoreConfiguration;
import org.vfny.geoserver.util.DataStoreUtils;

public class GeoServerDataTable extends GeoServerBasePage {

    public GeoServerDataTable() {
        final TreeTable tree = new TreeTable(
                "catalog",
                new CatalogTreeModel(),
                new IColumn[] { new CatalogNameColumn(), new ItemActionColumn() }) {
            
            @Override
            protected Component newTreePanel(MarkupContainer parent,
                    String id, TreeNode node, int level,
                    IRenderNodeCallback renderNodeCallback) {
                if(node instanceof NewDatastoreNode) {
                    return new NewDataStoreFragment(id, newIndentation(parent, "indent", node, level), newJunctionLink(parent, "link", "image", node));
                } else {
                    return super.newTreePanel(parent, id, node, level, renderNodeCallback);
                }
            }
        };
        add(tree);
        tree.setRootLess(true);
        tree.getTreeState().setAllowSelectMultiple(false);
        tree.getTreeState().addTreeStateListener(new TreeListener(tree));
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

    protected static TreeNode getWorkspaceNode(TreeNode selected) {
        TreeNode node = selected;
        while (node != null && !(node instanceof WorkspaceNode)) {
            node = node.getParent();
        }
        return node;
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
            TreeNode node = getWorkspaceNode(selected);
            if (node != null) {
                if (!tree.getTreeState().isNodeSelected(node))
                    tree.getTreeState().selectNode(node, true);
            }
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
            // TODO Auto-generated method stub

        }

        public void allNodesExpanded() {
            // TODO Auto-generated method stub

        }

        public void allNodesCollapsed() {
            // TODO Auto-generated method stub

        }
    }

    public class ItemActionColumn extends AbstractRenderableColumn implements
            IColumn {

        public ItemActionColumn() {
            super(new ColumnLocation(Alignment.MIDDLE, 20, Unit.PROPORTIONAL),
                    "Action");
        }

        @Override
        public String getNodeValue(TreeNode node) {
            return "Edit controls should be here for node "
                    + ((CatalogNode) node).getNodeLabel();
        }

    }

    public class CatalogNameColumn extends AbstractTreeColumn implements
            IColumn {

        public CatalogNameColumn() {
            super(new ColumnLocation(Alignment.MIDDLE, 20, Unit.PROPORTIONAL),
                    "Catalog");
        }
        

        @Override
        public String renderNode(TreeNode node) {
            return ((CatalogNode) node).getNodeLabel();
        }

    }

    class CatalogTreeModel extends LoadableDetachableModel {

        @Override
        protected Object load() {
            return new DefaultTreeModel(new CatalogRootNode());
        }

    }
    
    /**
     * This fragment holds the "new datastore" drop down
     * @author aaime
     */
    class NewDataStoreFragment extends Fragment {

        public NewDataStoreFragment(String id, Component indent, Component link) {
            super(id, "newDataStoreFragment", GeoServerDataTable.this);
            Form form = new Form("newDataStoreForm");
            add(form);
            List stores = new ArrayList(DataStoreUtils.listDataStoresDescriptions());
            DropDownChoice storeChoice = new DropDownChoice("stores", new Model(), stores) {
                @Override
                protected boolean wantOnSelectionChangedNotifications() {
                    return true;
                }
                
                @Override
                protected void onSelectionChanged(Object newSelection) {
                    setResponsePage(DataStoreConfiguration.class);
                }
            };
            form.add(storeChoice);
            form.add(indent);
            form.add(link);
        }
        
    }

}
