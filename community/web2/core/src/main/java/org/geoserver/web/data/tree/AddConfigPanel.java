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
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.geoserver.catalog.Catalog;
import org.geoserver.catalog.CatalogFactory;
import org.geoserver.catalog.CoverageInfo;
import org.geoserver.catalog.DataStoreInfo;
import org.geoserver.catalog.FeatureTypeInfo;
import org.geoserver.catalog.LayerInfo;
import org.geoserver.catalog.ResourceInfo;
import org.geoserver.catalog.StoreInfo;
import org.geoserver.web.GeoServerApplication;
import org.geoserver.web.data.ResourceConfigurationPage;

/**
 * A simple component that can be used to edit/remove items from the tree
 * 
 * @author aaime
 * 
 */
@SuppressWarnings("serial")
public class AddConfigPanel extends Panel {
    
    /**
     * Per {@link CatalogNode} concrete subclass class type map of
     * strategies to handle add and config
     * 
     * @see #getAddRemoveStrategy(CatalogNode)
     */
    private static final Map<Class<?>, AddConfigStrategy> ADD_CONFIG_STRATEGIES = new HashMap<Class<?>, AddConfigStrategy>();
    static {
        ADD_CONFIG_STRATEGIES.put(UnconfiguredResourceNode.class, new UnconfiguredResourceStrategy());
    }

    private final CatalogNode node;

    public AddConfigPanel(String id, CatalogNode node) {
        super(id);
        this.node = node;

        AjaxLink link = new AjaxLink("config") {

            @Override
            public void onClick(AjaxRequestTarget target) {
                onConfigClick(target);
            }
        };
        Image icon = new Image("configIcon", new ResourceReference(
                GeoServerApplication.class, "img/icons/silk/pencil_add.png"));
        icon.add(new AttributeModifier("title", true, new StringResourceModel("addConfigure", this, 
                new Model(node))));
        link.add(icon);
        add(link);

        link = new AjaxLink("add") {

            @Override
            public void onClick(AjaxRequestTarget target) {
                addAddClick(target);
            }
        };
        icon = new Image("addIcon", new ResourceReference(
                GeoServerApplication.class, "img/icons/silk/add.png"));
        icon.add(new AttributeModifier("title", true, new StringResourceModel("add", this, 
                new Model(node))));
        link.add(icon);
        // notify people we still missing this functionality
        link.add(new SimpleAttributeModifier("onclick", "alert('Should auto configure the layer, " +
        		"but for the moment the functionality is missing.');"));
        add(link);
    }

    protected void addAddClick(AjaxRequestTarget target) {
        System.out.println("Add clicked!");
    }

    protected void onConfigClick(AjaxRequestTarget target) {
        getAddConfigStrategy(node).config(this, node);
    }
    
    /**
     * Grabs the most appropriate behavior for the  
     * @param node
     *            the node currently selected on the tree panel
     * @return the strategy to handle edit and remove operations over the given
     *         node class type
     */
    private static AddConfigStrategy getAddConfigStrategy(final CatalogNode node) {
        final Class<? extends CatalogNode> nodeClass = node.getClass();
        final AddConfigStrategy strategy = ADD_CONFIG_STRATEGIES.get(nodeClass);
        if (strategy == null) {
            throw new IllegalArgumentException("Unknown node type, don't know how to handle it: "
                    + nodeClass.getName());
        }

        return strategy;
    }
    
    
    /**
     * Defines a strategy to get the configure and add pages for a specific
     * {@link CatalogNode} subclass.
     * <p>
     * Implementations shall be stateless and are meant to be per node type
     * singletons.
     * </p>
     * 
     * @author Gabriel Roldan
     */
    private static interface AddConfigStrategy {

        public void add(Component callingComponent, CatalogNode node);

        public void config(Component callingComponent, CatalogNode node);
    }
    
    /**
     * 
     * @author Gabriel Roldan
     */
    private static class UnconfiguredResourceStrategy implements AddConfigStrategy {

        /**
         * @param callingComponent
         * @param node
         *            shall be an instance of
         *            {@link UnconfiguredResourceNode}
         */
        public void config(final Component callingComponent, final CatalogNode node) {
            final UnconfiguredResourceNode unconfiguredNode = ((UnconfiguredResourceNode) node);
            final String resourceName = unconfiguredNode.getResourceName();
            final StoreInfo store = unconfiguredNode.getModel();

            final Catalog catalog = node.getCatalog();
            CatalogFactory factory = catalog.getFactory();
            
            ResourceInfo ri;
            LayerInfo.Type type;
            if(store instanceof DataStoreInfo) {
                FeatureTypeInfo featureTypeInfo = factory.createFeatureType();
                featureTypeInfo.setName(resourceName);
                featureTypeInfo.setStore(store);
                ri = featureTypeInfo;
                type = LayerInfo.Type.VECTOR;
            } else {
                CoverageInfo coverageInfo = factory.createCoverage();
                coverageInfo.setName(resourceName);
                coverageInfo.setStore(store);
                ri = coverageInfo;
                type = LayerInfo.Type.RASTER;
            }
            LayerInfo li = factory.createLayer();
            li.setName(ri.getName());
            li.setType(type);
            li.setResource(ri);
            Page responsePage = new ResourceConfigurationPage(li, true);
            callingComponent.setResponsePage(responsePage);
        }

        public void add(final Component callingComponent, final CatalogNode node) {
            System.out.println("Meh, we still haven't coded this one");
        }
    }

}
