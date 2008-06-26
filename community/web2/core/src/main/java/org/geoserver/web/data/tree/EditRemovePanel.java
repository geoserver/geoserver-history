/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.web.data.tree;

import java.util.HashMap;
import java.util.Map;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.ResourceReference;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.geoserver.catalog.Catalog;
import org.geoserver.catalog.CatalogFactory;
import org.geoserver.catalog.DataStoreInfo;
import org.geoserver.catalog.FeatureTypeInfo;
import org.geoserver.catalog.NamespaceInfo;
import org.geoserver.catalog.ResourceInfo;
import org.geoserver.catalog.WorkspaceInfo;
import org.geoserver.web.GeoServerApplication;
import org.geoserver.web.data.NamespaceEditPage;
import org.geoserver.web.data.ResourceConfigurationPage;
import org.geoserver.web.data.datastore.DataStoreConfiguration;

/**
 * A simple component that can be used to edit/remove items from the tree
 * 
 * @author Andrea Aime
 * @author Gabriel Roldan
 */
public class EditRemovePanel extends Panel {

    /**
     * Per {@link CatalogNode} concrete subclass class type map of
     * strategies to handle edit and remove
     * 
     * @see #getAddRemoveStrategy(CatalogNode)
     */
    private static final Map<Class, EditRemoveStrategy> EDIT_REMOVE_STRATEGIES = new HashMap<Class, EditRemoveStrategy>();
    static {
        EDIT_REMOVE_STRATEGIES.put(WorkspaceNode.class, new WorkspaceEditRemoveStrategy());
        EDIT_REMOVE_STRATEGIES.put(DataStoreNode.class, new DataStoreEditRemoveStrategy());
        EDIT_REMOVE_STRATEGIES.put(CoverageStoreNode.class, new CoverageStoreEditRemoveStrategy());
        EDIT_REMOVE_STRATEGIES.put(ResourceNode.class, new ResourceEditRemoveStrategy());
        EDIT_REMOVE_STRATEGIES.put(UnconfiguredResourceNode.class,
                new UnconfiguredFeatureTypeEditRemoveStrategy());
    }

    private final CatalogNode node;

    public EditRemovePanel(String id, CatalogNode node) {
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
        final EditRemoveStrategy strategy = getEditRemoveStrategy(node);

        strategy.remove(this, node);
    }

    protected void onEditClick(AjaxRequestTarget target) {
        final EditRemoveStrategy strategy = getEditRemoveStrategy(node);
        strategy.edit(this, node);
    }

    public static void remove(final Component callingComponent, final CatalogNode node) {
        final EditRemoveStrategy strategy = getEditRemoveStrategy(node);
        strategy.remove(callingComponent, node);
    }

    /**
     * 
     * @param node
     *            the node currently selected on the tree panel
     * @return the strategy to handle edit and remove operations over the given
     *         node class type
     */
    private static EditRemoveStrategy getEditRemoveStrategy(final CatalogNode node) {
        final Class<? extends CatalogNode> nodeClass = node.getClass();
        final EditRemoveStrategy strategy = EDIT_REMOVE_STRATEGIES.get(nodeClass);
        if (strategy == null) {
            throw new IllegalArgumentException("Unknown node type, don't know how to handle it: "
                    + nodeClass.getName());
        }

        return strategy;
    }

    /**
     * Defines a strategy to get the edit and remove pages for a specific
     * {@link CatalogNode} subclass.
     * <p>
     * Implementations shall be stateless and are meant to be per node type
     * singletons.
     * </p>
     * 
     * @author Gabriel Roldan
     */
    private static interface EditRemoveStrategy {

        public void edit(Component callingComponent, CatalogNode node);

        public void remove(Component callingComponent, CatalogNode node);
    }
    
    

    /**
     * 
     * @author Gabriel Roldan
     */
    private static class WorkspaceEditRemoveStrategy implements EditRemoveStrategy {

        public void edit(final Component callingComponent, final CatalogNode node) {
            final WorkspaceNode wsNode = (WorkspaceNode) node;
            final WorkspaceInfo model = wsNode.getModel();
            final Catalog catalog = node.getCatalog();

            String prefix = model.getName();
            NamespaceInfo namespace = catalog.getNamespaceByPrefix(prefix);
            String nsId = namespace.getId();
            callingComponent.setResponsePage(new NamespaceEditPage(nsId));
        }

        public void remove(final Component callingComponent, final CatalogNode node) {
            System.out.println("remove " + node.getClass());
        }
    }

    /**
     * 
     * @author Gabriel Roldan
     */
    private static class DataStoreEditRemoveStrategy implements EditRemoveStrategy {

        public void edit(final Component callingComponent, final CatalogNode node) {
            final String datastoreUniqueName = node.getNodeLabel();

            final Catalog catalog = node.getCatalog();
            final DataStoreInfo dataStore = catalog.getDataStoreByName(datastoreUniqueName);
            final String dataStoreInfoId = dataStore.getId();

            callingComponent.setResponsePage(new DataStoreConfiguration(dataStoreInfoId));
        }

        public void remove(final Component callingComponent, final CatalogNode node) {
            System.out.println("remove " + node.getClass());
        }
    }

    /**
     * 
     * @author Gabriel Roldan
     */
    private static class CoverageStoreEditRemoveStrategy implements EditRemoveStrategy {

        public void edit(final Component callingComponent, final CatalogNode node) {
            System.out.println("edit " + node.getClass());
        }

        public void remove(final Component callingComponent, final CatalogNode node) {
            System.out.println("remove " + node.getClass());
        }
    }

    /**
     * 
     * @author Gabriel Roldan
     */
    private static class ResourceEditRemoveStrategy implements EditRemoveStrategy {

        public void edit(final Component callingComponent, final CatalogNode node) {
            ResourceInfo resourceInfo = (ResourceInfo) node.getModel();
            callingComponent.setResponsePage(new ResourceConfigurationPage(resourceInfo, false));
        }

        public void remove(final Component callingComponent, final CatalogNode node) {
            System.out.println("remove " + node.getClass());
        }
    }

    /**
     * 
     * @author Gabriel Roldan
     */
    private static class UnconfiguredFeatureTypeEditRemoveStrategy implements EditRemoveStrategy {

        /**
         * @param callingComponent
         * @param node
         *            shall be an instance of
         *            {@link UnconfiguredResourceNode}
         */
        public void edit(final Component callingComponent, final CatalogNode node) {

            final UnconfiguredResourceNode unconfiguredFTypeNode = ((UnconfiguredResourceNode) node);
            final String typeName = unconfiguredFTypeNode.getResourceName();
            final DataStoreInfo dataStore = (DataStoreInfo) unconfiguredFTypeNode.getModel();

            final Catalog catalog = node.getCatalog();
            CatalogFactory factory = catalog.getFactory();

            FeatureTypeInfo featureTypeInfo = factory.createFeatureType();
            featureTypeInfo.setName(typeName);
            featureTypeInfo.setStore(dataStore);

            Page responsePage = new ResourceConfigurationPage(featureTypeInfo, true);
            callingComponent.setResponsePage(responsePage);
        }

        public void remove(final Component callingComponent, final CatalogNode node) {
            System.out.println("remove " + node.getClass());
        }
    }
}
