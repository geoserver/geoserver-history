/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.web.data.table;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.basic.MultiLineLabel;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.geoserver.catalog.Catalog;
import org.geoserver.catalog.CatalogBuilder;
import org.geoserver.catalog.CoverageInfo;
import org.geoserver.catalog.CoverageStoreInfo;
import org.geoserver.catalog.DataStoreInfo;
import org.geoserver.catalog.FeatureTypeInfo;
import org.geoserver.catalog.LayerInfo;
import org.geoserver.catalog.ResourceInfo;
import org.geoserver.catalog.StoreInfo;
import org.geoserver.web.GeoServerSecuredPage;
import org.geoserver.web.data.ResourceConfigurationPage;
import org.geoserver.web.wicket.GeoServerTablePanel;
import org.geoserver.web.wicket.SimpleAjaxLink;
import org.geoserver.web.wicket.GeoServerDataProvider.Property;
import org.geotools.coverage.grid.io.AbstractGridCoverage2DReader;

/**
 * A page listing the resources contained in a store, and whose links will bring
 * the user to a new resource configuration page
 * 
 * @author Andrea Aime - OpenGeo
 * 
 */
public class NewLayerPage extends GeoServerSecuredPage {

    String storeId;

    public NewLayerPage(String storeId) {
        this.storeId = storeId;
        StoreInfo store = getCatalog().getStore(storeId, StoreInfo.class);
        
        // add the basic labels
        add(new Label("storeName", store.getName()));
        
        // build the unconfigured layer provider and the table showing it
        final NewLayerPageProvider provider = new NewLayerPageProvider(storeId);
        final GeoServerTablePanel<Resource> layers = new GeoServerTablePanel<Resource>("layers", provider) {

            @Override
            protected Component getComponentForProperty(String id,
                    IModel itemModel, Property<Resource> property) {
                if (property == NewLayerPageProvider.NAME) {
                    return new SimpleAjaxLink(id, itemModel, property
                            .getModel(itemModel)) {

                        @Override
                        protected void onClick(AjaxRequestTarget target) {
                            Resource resource = (Resource) getModelObject();
                            setResponsePage(new ResourceConfigurationPage(
                                    buildLayerInfo(resource), true));
                        }

                    };
                } else if (property == NewLayerPageProvider.PUBLISHED) {
                    return new Label(id, property.getModel(itemModel));
                } else {
                    throw new IllegalArgumentException(
                            "Don't know of property " + property.getName());
                }
            }

        };
        layers.setFilterVisible(false);
        
        final WebMarkupContainer container = new WebMarkupContainer("layersContainer");
        container.setOutputMarkupId(true);
        add(container);
        container.add(layers);
        
        // add the "all layers are published" warning message
        String warnMessage = "All resources inside this store have " + 
        		"  been publised already.\nCheck \"show published resources\" if you want to" + 
        		"  see them, and publish a resource again (under a different" + 
        		"  name/configuration)";
        final MultiLineLabel allPublished = new MultiLineLabel("allPublished", warnMessage);
        container.add(allPublished);
        
        updateLayersVisibility(provider, layers, allPublished);
        
        // add the published layers switch
        final CheckBox showPublished = new CheckBox("showPublished", new Model(false));
        showPublished.add(new AjaxFormComponentUpdatingBehavior("onchange") {
            
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                Boolean selected = (Boolean) showPublished.getModelObject();
                provider.setShowPublished(selected);
                
                updateLayersVisibility(provider, layers, allPublished);
                
                target.addComponent(container);
            }
        });
        add(showPublished);
    }

    private void updateLayersVisibility(final NewLayerPageProvider provider,
            final GeoServerTablePanel<Resource> layers, final MultiLineLabel allPublished) {
        // decide what to show, the table or the warning message
        int providerSize = provider.size();
        layers.setVisible(providerSize > 0);
        allPublished.setVisible(providerSize == 0);
    }

    /**
     * Turns a resource name into a full {@link ResourceInfo}
     * 
     * @param resource
     * @return
     */
    LayerInfo buildLayerInfo(Resource resource) {
        Catalog catalog = getCatalog();
        StoreInfo store = catalog.getStore(storeId, StoreInfo.class);

        // try to build from coverage store or data store
        CatalogBuilder builder = new CatalogBuilder(catalog);
        builder.setStore(store);
        try {
            if (store instanceof CoverageStoreInfo) {
                CoverageStoreInfo cstore = (CoverageStoreInfo) store;
                AbstractGridCoverage2DReader reader = (AbstractGridCoverage2DReader) (cstore
                        .getFormat()).getReader(cstore.getURL());
                CoverageInfo ci = builder.buildCoverage(reader);
                return builder.buildLayer(ci);
            } else if (store instanceof DataStoreInfo) {
                DataStoreInfo dstore = (DataStoreInfo) store;
                FeatureTypeInfo fti = builder.buildFeatureType(dstore.getDataStore(null)
                        .getFeatureSource(resource.getName()));
                return builder.buildLayer(fti);
            }
        } catch (Exception e) {
            throw new RuntimeException(
                    "Error occurred while building the resources for the configuration page",
                    e);
        }

        // handle the case in which the store was not found anymore, or was not
        // of the expected type
        if (store == null)
            throw new IllegalArgumentException(
                    "Store is missing from configuration!");
        else
            throw new IllegalArgumentException(
                    "Don't know how to deal with this store " + store);
    }

}
