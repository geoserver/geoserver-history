/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.web.data.tree;

import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ResourceReference;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.extensions.markup.html.tree.table.AbstractColumn;
import org.apache.wicket.extensions.markup.html.tree.table.AbstractTreeColumn;
import org.apache.wicket.extensions.markup.html.tree.table.ColumnLocation;
import org.apache.wicket.extensions.markup.html.tree.table.IColumn;
import org.apache.wicket.extensions.markup.html.tree.table.IRenderable;
import org.apache.wicket.extensions.markup.html.tree.table.ColumnLocation.Alignment;
import org.apache.wicket.extensions.markup.html.tree.table.ColumnLocation.Unit;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.model.Model;
import org.geoserver.catalog.DataStoreInfo;
import org.geoserver.catalog.FeatureTypeInfo;
import org.geoserver.catalog.StoreInfo;
import org.geoserver.catalog.WorkspaceInfo;
import org.geoserver.web.GeoServerApplication;
import org.geoserver.web.GeoServerBasePage;
import org.geoserver.web.data.DataStorePanelInfo;
import org.geoserver.web.data.NewDataPage;
import org.geoserver.web.data.ResourceConfigurationPage;
import org.geoserver.web.util.WebUtils;
import org.geotools.data.DataAccessFactory;
import org.geotools.util.logging.Logging;

public class DataPage extends GeoServerBasePage {
    
    static final Logger LOGGER = Logging.getLogger(DataPage.class);

    DataTreeTable tree;
    CatalogRootNode root;
    private Form buttonForm;
    private AjaxButton addButton;
    private AjaxButton removeButton;
    private AjaxButton configureButton;

    public DataPage() {
        WebMarkupContainer treeContainer = new WebMarkupContainer("treeParent");
        treeContainer.setOutputMarkupId(true);
        root = new CatalogRootNode();
        tree = new DataTreeTable("dataTree", new DefaultTreeModel(
                root), new IColumn[] { new SelectionColumn(),
                new DataPageTreeColumn(), new ActionColumn() }) {
            @Override
            public ResourceReference getNodeIcon(TreeNode node) {
                if(node instanceof DataStoreNode) {
                    return getStoreIcon(((DataStoreNode) node).getModel());
                } else {
                    return super.getNodeIcon(node);
                }
            }
        };

        tree.setRootLess(true);
        tree.getTreeState().setAllowSelectMultiple(false);
        tree.getTreeState().addTreeStateListener(new DataTreeListener());

        treeContainer.add(tree);
        add(treeContainer);

        buttonForm = new Form("controlForm");
        addButton = new AjaxButton("addChecked") {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form form) {
                addButtonClicked(target, form);
            }
        };
        buttonForm.add(addButton);
        removeButton = new AjaxButton("removeChecked") {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form form) {
                removeButtonClicked(target, form);
            }
        };
        buttonForm.add(removeButton);
        configureButton = new AjaxButton("configureChecked") {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form form) {
                configureButtonClicked(target, form);
            }
        };
        configureButton.add(new SimpleAttributeModifier("onclick",
        "alert('This will allow you mass configure multiple feature types'); return false;"));

        buttonForm.add(configureButton);
        add(buttonForm);
        
        updateButtonState();
    }

    protected ResourceReference getStoreIcon(StoreInfo info) {
        if (info instanceof DataStoreInfo) {
            DataStoreInfo ds = (DataStoreInfo) info;

            // look for the associated panel info if there is one
            List<DataStorePanelInfo> infos = getGeoServerApplication()
                    .getBeansOfType(DataStorePanelInfo.class);
            for (DataStorePanelInfo panelInfo : infos) {
                try {
                    // we know if a factory is the right one if it can process the params
                    DataAccessFactory factory = (DataAccessFactory) panelInfo
                            .getFactoryClass().newInstance();
                    if (factory.canProcess(ds.getConnectionParameters())) {
                        return new ResourceReference(panelInfo.getIconBase(),
                                panelInfo.getIcon());
                    }
                } catch (Exception e) {
                    LOGGER.log(Level.WARNING,
                            "Could not create an instance of the data store factory "
                                    + panelInfo.getFactoryClass());
                }
            }

            // fall back on generic vector icon otherwise
            return new ResourceReference(GeoServerApplication.class,
                    "img/icons/geosilk/vector.png");

        } else {
            return null;
        }
    }

    protected void configureButtonClicked(AjaxRequestTarget target, Form form) {
        // TODO Auto-generated method stub

    }

    protected void removeButtonClicked(AjaxRequestTarget target, Form form) {
        // TODO Auto-generated method stub

    }

    protected void addButtonClicked(AjaxRequestTarget target, Form form) {
        // TODO Auto-generated method stub

    }

    class DataPageTreeColumn extends AbstractTreeColumn implements IColumn {

        public DataPageTreeColumn() {
            super(new ColumnLocation(Alignment.MIDDLE, 88, Unit.PROPORTIONAL), "Catalog");
        }

        @Override
        public String renderNode(TreeNode node) {
            return ((AbstractCatalogNode) node).getNodeLabel();
        }

        @Override
        public Component newCell(MarkupContainer parent, String id, TreeNode node, int level) {
            if (node instanceof UnconfiguredFeatureTypesNode) {
                return new UnconfiguredFeatureTypesPanel(id, tree, parent,
                        (UnconfiguredFeatureTypesNode) node, level);
            }
            if (node instanceof UnconfiguredFeatureTypeNode) {
                return new UnconfiguredFeatureTypePanel(id, tree, parent,
                        (AbstractCatalogNode) node, level);
            }
            if (node instanceof ResourceNode) {
                return new ResourcePanel(id, tree, parent, (AbstractCatalogNode) node, level);
            }
            if (node instanceof NewDatastoreNode) {
                return new NewDataStorePanel(id, tree, parent, (AbstractCatalogNode) node, level);
            } else {
                return super.newCell(parent, id, node, level);
            }

        }
    }

    class SelectionColumn extends AbstractColumn {

        public SelectionColumn() {
            super(new ColumnLocation(Alignment.LEFT, 24, Unit.PX), "");
        }

        public Component newCell(MarkupContainer parent, String id, TreeNode node, int level) {
            AbstractCatalogNode cn = (AbstractCatalogNode) node;
            if (!cn.isSelectable())
                return new EmptyPanel(id);
            else
                return new DataPageSelectionPanel(id, node, tree);
        }

        public IRenderable newCell(TreeNode node, int level) {
            return null;
        }

    }

    class ActionColumn extends AbstractColumn {

        public ActionColumn() {
            super(new ColumnLocation(Alignment.RIGHT, 50, Unit.PX), "");
        }

        public Component newCell(MarkupContainer parent, String id, TreeNode node, int level) {
            if (node instanceof UnconfiguredFeatureTypeNode)
                return new AddConfigPanel(id, (AbstractCatalogNode) node);
            else if (node instanceof AbstractPlaceholderNode)
                return new EmptyPanel(id);
            else
                return new EditRemovePanel(id, (AbstractCatalogNode) node);
        }

        public IRenderable newCell(TreeNode node, int level) {
            return null;
        }

    }

    final class DataTreeListener extends TreeAdapter implements Serializable {

        @Override
        public void nodeUnselected(TreeNode node) {
            if (!tree.getTreeState().isNodeExpanded(node))
                tree.getTreeState().expandNode(node);
            else
                tree.getTreeState().collapseNode(node);
        }

        public void nodeSelected(TreeNode selected) {

            if (!tree.getTreeState().isNodeExpanded(selected))
                tree.getTreeState().expandNode(selected);
            else
                tree.getTreeState().collapseNode(selected);
        }

    }

    protected TreeNode getWorkspaceNode(TreeNode selected) {
        TreeNode node = selected;
        while (node != null && !(node instanceof WorkspaceNode)) {
            node = node.getParent();
        }
        return node;
    }

    class UnconfiguredFeatureTypesPanel extends LinkPanel {

        public UnconfiguredFeatureTypesPanel(String id, DataTreeTable tree, MarkupContainer parent,
                UnconfiguredFeatureTypesNode node, int level) {
            super(id, tree, parent, node, level);
            label.add(new AttributeModifier("class", true, new Model("command")));
        }

        @Override
        protected void onClick(AjaxRequestTarget target) {
            ((DataStoreNode) node.getParent()).setUnconfiguredChildrenVisible(true);
            tree.getTreeState().expandNode(
                    (((DataStoreNode) node.getParent()).checkPartialSelection()));
            target.addComponent(tree.getParent());
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

    class UnconfiguredFeatureTypePanel extends LinkPanel {

        public UnconfiguredFeatureTypePanel(String id, DataTreeTable tree, MarkupContainer parent,
                AbstractCatalogNode node, int level) {
            super(id, tree, parent, node, level);
            label.add(new AttributeModifier("class", true, new Model("unconfiguredLayer")));
        }

        /**
         * Creates a new, detached from the catalog, {@link FeatureTypeInfo} and
         * pass it through to {@link ResourceConfigurationPage}
         */
        @Override
        protected void onClick(AjaxRequestTarget target) {
            EditRemovePanel.edit(this, (AbstractCatalogNode) node);
        }
    }
    
    class DataPageSelectionPanel extends SelectionPanel {

        public DataPageSelectionPanel(String id, TreeNode node,
                DataTreeTable tree) {
            super(id, node, tree);
        }

        @Override
        protected void onCheckboxClick(AjaxRequestTarget target) {
            // change the state of the current node
            catalogNode.nextSelectionState();
            icon.setImageResourceReference(getImageResource(catalogNode));
            
            AbstractCatalogNode lastChangedParent = catalogNode.getParent().checkPartialSelection();

            // force the tree refresh
            tree.refresh(lastChangedParent);
            target.addComponent(tree.getParent());
            
            updateButtonState();
            target.addComponent(removeButton);
            target.addComponent(configureButton);
            target.addComponent(addButton);
        }
    }
    
    class ResourcePanel extends LabelPanel {

        public ResourcePanel(String id, DataTreeTable tree,
                MarkupContainer parent, AbstractCatalogNode node, int level) {
            super(id, tree, parent, node, level);
        }
        
        @Override
        protected ResourceReference getNodeIcon(DataTreeTable tree,
                TreeNode node) {
            // a regular resource does not need an icon
            if(node.getParent() instanceof DataStoreNode || node.getParent() instanceof CoverageStoreNode) {
                return null;
            } else {
                ResourceNode rn = (ResourceNode) node;
                if(rn.getResourceType() == FeatureTypeInfo.class) {
                    return getStoreIcon(getCatalog().getDataStoreByName(rn.getStoreName()));
                } else {
                    return null;
                }
            }
        }
        
    }
    
    void updateButtonState() {
        // button state update
        List<AbstractCatalogNode> selection =  root.getSelectedNodes();
        boolean configured = false;
        boolean unconfigured = false;
        for (AbstractCatalogNode node : selection) {
            if(node instanceof UnconfiguredFeatureTypeNode) {
                unconfigured = true;
            } else {
                configured = true;
            }
            
        }
        removeButton.setEnabled(configured);
        configureButton.setEnabled(unconfigured);
        addButton.setEnabled(unconfigured);
    }
    
   

}
