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
import org.geoserver.catalog.ResourceInfo;
import org.geoserver.web.GeoServerApplication;
import org.geoserver.web.GeoServerHomePage;
import org.geoserver.web.data.ResourceConfigurationPage;
import org.geoserver.web.data.datastore.DataStoreConfiguration;

/**
 * A simple component that can be used to edit/remove items from the tree
 * 
 * @author aaime
 * 
 */
public class EditRemovePanel extends Panel {

    /**
     * Per {@link AbstractCatalogNode} concrete subclass class type map of
     * strategies to handle edit and remove
     * 
     * @see #getAddRemoveStrategy(AbstractCatalogNode)
     */
    private static final Map<Class, AddRemoveStrategy> ADD_REMOVE_STRATEGIES = new HashMap<Class, AddRemoveStrategy>();
    static {
        ADD_REMOVE_STRATEGIES.put(WorkspaceNode.class, new WorkspaceAddRemoveStrategy());
        ADD_REMOVE_STRATEGIES.put(DataStoreNode.class, new DataStoreAddRemoveStrategy());
        ADD_REMOVE_STRATEGIES.put(CoverageStoreNode.class, new CoverageStoreAddRemoveStrategy());
        ADD_REMOVE_STRATEGIES.put(ResourceNode.class, new ResourceAddRemoveStrategy());
        ADD_REMOVE_STRATEGIES.put(UnconfiguredFeatureTypeNode.class,
                new UnconfiguredFeatureTypeAddRemoveStrategy());
    }

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
        final AddRemoveStrategy strategy = getAddRemoveStrategy(node);

        strategy.remove(this, node);
    }

    protected void onEditClick(AjaxRequestTarget target) {
        edit(this, node);
    }

    public static void edit(final Component callingComponent, final AbstractCatalogNode node) {
        final AddRemoveStrategy strategy = getAddRemoveStrategy(node);
        strategy.edit(callingComponent, node);
    }

    public static void remove(final Component callingComponent, final AbstractCatalogNode node) {
        final AddRemoveStrategy strategy = getAddRemoveStrategy(node);
        strategy.remove(callingComponent, node);
    }

    /**
     * 
     * @param node
     *            the node currently selected on the tree panel
     * @return the strategy to handle edit and remove operations over the given
     *         node class type
     */
    private static AddRemoveStrategy getAddRemoveStrategy(final AbstractCatalogNode node) {
        final Class<? extends AbstractCatalogNode> nodeClass = node.getClass();
        final AddRemoveStrategy strategy = ADD_REMOVE_STRATEGIES.get(nodeClass);
        if (strategy == null) {
            throw new IllegalArgumentException("Unknown node type, don't know how to handle it: "
                    + nodeClass.getName());
        }

        return strategy;
    }

    /**
     * Defines a strategy to get the edit and remove pages for a specific
     * {@link AbstractCatalogNode} subclass.
     * <p>
     * Implementations shall be stateless and are meant to be per node type
     * singletons.
     * </p>
     * 
     * @author Gabriel Roldan
     */
    private static interface AddRemoveStrategy {

        public void edit(Component callingComponent, AbstractCatalogNode node);

        public void remove(Component callingComponent, AbstractCatalogNode node);
    }

    /**
     * 
     * @author Gabriel Roldan
     */
    private static class WorkspaceAddRemoveStrategy implements AddRemoveStrategy {

        public void edit(final Component callingComponent, final AbstractCatalogNode node) {
            System.out.println("Edit workspace node " + node.getNodeLabel());
        }

        public void remove(final Component callingComponent, final AbstractCatalogNode node) {
            System.out.println("remove " + node.getClass());
        }
    }

    /**
     * 
     * @author Gabriel Roldan
     */
    private static class DataStoreAddRemoveStrategy implements AddRemoveStrategy {

        public void edit(final Component callingComponent, final AbstractCatalogNode node) {
            final String datastoreUniqueName = node.getNodeLabel();

            final Catalog catalog = node.getCatalog();
            final DataStoreInfo dataStore = catalog.getDataStoreByName(datastoreUniqueName);
            final String dataStoreInfoId = dataStore.getId();

            callingComponent.setResponsePage(new DataStoreConfiguration(dataStoreInfoId));
        }

        public void remove(final Component callingComponent, final AbstractCatalogNode node) {
            System.out.println("remove " + node.getClass());
        }
    }

    /**
     * 
     * @author Gabriel Roldan
     */
    private static class CoverageStoreAddRemoveStrategy implements AddRemoveStrategy {

        public void edit(final Component callingComponent, final AbstractCatalogNode node) {
            System.out.println("edit " + node.getClass());
        }

        public void remove(final Component callingComponent, final AbstractCatalogNode node) {
            System.out.println("remove " + node.getClass());
        }
    }

    /**
     * 
     * @author Gabriel Roldan
     */
    private static class ResourceAddRemoveStrategy implements AddRemoveStrategy {

        public void edit(final Component callingComponent, final AbstractCatalogNode node) {
            ResourceInfo resourceInfo = (ResourceInfo) node.getModel();
            callingComponent.setResponsePage(new ResourceConfigurationPage(resourceInfo, false));
        }

        public void remove(final Component callingComponent, final AbstractCatalogNode node) {
            System.out.println("remove " + node.getClass());
        }
    }

    /**
     * 
     * @author Gabriel Roldan
     */
    private static class UnconfiguredFeatureTypeAddRemoveStrategy implements AddRemoveStrategy {

        /**
         * @param callingComponent
         * @param node
         *            shall be an instance of
         *            {@link UnconfiguredFeatureTypeNode}
         */
        public void edit(final Component callingComponent, final AbstractCatalogNode node) {

            final UnconfiguredFeatureTypeNode unconfiguredFTypeNode = ((UnconfiguredFeatureTypeNode) node);
            final String typeName = unconfiguredFTypeNode.getTypeName();
            final DataStoreInfo dataStore = unconfiguredFTypeNode.getModel();

            final Catalog catalog = node.getCatalog();
            CatalogFactory factory = catalog.getFactory();

            FeatureTypeInfo featureTypeInfo = factory.createFeatureType();
            featureTypeInfo.setName(typeName);
            featureTypeInfo.setStore(dataStore);

            Page responsePage = new ResourceConfigurationPage(featureTypeInfo, false);
            callingComponent.setResponsePage(responsePage);
        }

        public void remove(final Component callingComponent, final AbstractCatalogNode node) {
            System.out.println("remove " + node.getClass());
        }
    }
}
