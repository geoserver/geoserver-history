/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.web.data.tree;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

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
import org.geotools.data.FeatureSource;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.util.NullProgressListener;
import org.geotools.util.logging.Logging;
import org.opengis.feature.Feature;
import org.opengis.feature.type.FeatureType;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * A simple component that can be used to edit/remove items from the tree
 * 
 * @author Andrea Aime (TOPP)
 * @author David Winslow (TOPP)
 * @author Gabriel Roldan (TOPP)
 */
@SuppressWarnings("serial")
public class AddConfigPanel extends Panel {

    private static final Logger LOGGER = Logging.getLogger("org.geoserver.web.data.tree");

    /**
     * Per {@link CatalogNode} concrete subclass class type map of strategies to
     * handle add and config
     * 
     * @see #getAddRemoveStrategy(CatalogNode)
     */
    private static final Map<Class<?>, AddConfigStrategy> ADD_CONFIG_STRATEGIES = new HashMap<Class<?>, AddConfigStrategy>();
    static {
        ADD_CONFIG_STRATEGIES.put(UnconfiguredResourceNode.class,
                new UnconfiguredResourceStrategy());
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
        Image icon = new Image("configIcon", new ResourceReference(GeoServerApplication.class,
                "img/icons/silk/pencil_add.png"));
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
        icon = new Image("addIcon", new ResourceReference(GeoServerApplication.class,
                "img/icons/silk/add.png"));
        icon.add(new AttributeModifier("title", true, new StringResourceModel("add", this,
                new Model(node))));
        link.add(icon);
        // notify people we still missing this functionality
        link.add(new SimpleAttributeModifier("onclick", "alert('Should auto configure the layer, "
                + "but for the moment the functionality is missing.');"));
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
     * 
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
         *            shall be an instance of {@link UnconfiguredResourceNode}
         */
        public void config(final Component callingComponent, final CatalogNode node) {
            final UnconfiguredResourceNode unconfiguredNode = ((UnconfiguredResourceNode) node);
            final String resourceName = unconfiguredNode.getResourceName();
            final StoreInfo store = unconfiguredNode.getModel();

            final Catalog catalog = node.getCatalog();
            final CatalogFactory factory = catalog.getFactory();

            final ResourceInfo ri;
            LayerInfo.Type type;
            if (store instanceof DataStoreInfo) {
                FeatureTypeInfo featureTypeInfo = factory.createFeatureType();
                featureTypeInfo.setNativeName(resourceName);
                featureTypeInfo.setName(resourceName);
                featureTypeInfo.setStore(store);

                fillOutDefaultFTInfoMetadata(featureTypeInfo);

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

        /**
         * Fills out the passed in new {@link FeatureTypeInfo} with the default
         * metadata from the underlying {@link FeatureSource}.
         * 
         * <p>
         * Preconditions:
         * <ul>
         * <li>{@code featureTypeInfo.getNativeName() != null}
         * <li>{@code featureTypeInfo.getName() != null}
         * <li>{@code featureTypeInfo.getStore() != null}
         * </ul>
         * </p>
         * 
         * @param featureTypeInfo the feature type infor to fill in the default metadata for
         */
        private void fillOutDefaultFTInfoMetadata(FeatureTypeInfo featureTypeInfo) {
            // fill out default metadata from FeatureSource info
            LOGGER.finer("filling out default metadata for " + featureTypeInfo.getName());

            final FeatureSource<? extends FeatureType, ? extends Feature> source;
            try {
                source = featureTypeInfo.getFeatureSource(new NullProgressListener(), null);
            } catch (IOException e) {
                // hmmm... ignore?
                LOGGER.log(Level.WARNING, "Can't acquire FeatureSource to fill out "
                        + "default metadata", e);
                return;
            }
            final org.geotools.data.ResourceInfo info = source.getInfo();

            LOGGER.finest("Setting default title");
            featureTypeInfo.setTitle(info.getTitle());

            LOGGER.finest("Setting default abstract");
            featureTypeInfo.setAbstract(info.getDescription());

            final CoordinateReferenceSystem crs = info.getCRS();
            if (crs != null) {
                LOGGER.finest("Setting default CRS");
                //true would be way too slow
                final boolean fullLookUp = false;
                try {
                    String srsId = CRS.lookupIdentifier(crs, fullLookUp);
                    LOGGER.finest("Default CRS: " + srsId);
                    featureTypeInfo.setSRS(srsId);
                } catch (FactoryException e) {
                    LOGGER.info("Lookup filed for the CRS identifier of the FeatureType CRS: "
                            + crs);
                }
            } else {
                LOGGER.finest("FeaureSource info does not provide a CRS");
            }

            ReferencedEnvelope nativeBounds = info.getBounds();
            if (nativeBounds != null) {
                LOGGER.finest("Setting default native bounds");
                featureTypeInfo.setNativeBoundingBox(nativeBounds);

                try {
                    LOGGER.finest("Setting default latlon bbox");
                    final boolean lenient = true;
                    ReferencedEnvelope latLonBounds;
                    latLonBounds = nativeBounds.transform(DefaultGeographicCRS.WGS84, lenient);
                    featureTypeInfo.setLatLonBoundingBox(latLonBounds);
                } catch (Exception e) {
                    LOGGER.log(Level.INFO, "Failed to project native bbox to WGS84 "
                            + "in order to set the latLonBbox", e);
                }
            } else {
                LOGGER.fine("FeatureSource info does not provide the native bounds");
            }

            LOGGER.finest("Setting default keywords");
            Set<String> keywords = info.getKeywords();
            featureTypeInfo.getKeywords().addAll(keywords);

            LOGGER.finer("Default metadata for new feature type configured");
        }

        public void add(final Component callingComponent, final CatalogNode node) {
            System.out.println("Meh, we still haven't coded this one");
        }
    }

}
