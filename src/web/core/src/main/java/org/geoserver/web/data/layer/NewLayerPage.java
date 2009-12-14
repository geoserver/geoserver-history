/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.web.data.layer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;

import org.apache.wicket.Component;
import org.apache.wicket.ResourceReference;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
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
import org.geoserver.web.CatalogIconFactory;
import org.geoserver.web.GeoServerSecuredPage;
import org.geoserver.web.data.resource.ResourceConfigurationPage;
import org.geoserver.web.wicket.GeoServerTablePanel;
import org.geoserver.web.wicket.ParamResourceModel;
import org.geoserver.web.wicket.SimpleAjaxLink;
import org.geoserver.web.wicket.GeoServerDataProvider.Property;

/**
 * A page listing the resources contained in a store, and whose links will bring
 * the user to a new resource configuration page
 * 
 * @author Andrea Aime - OpenGeo
 */
@SuppressWarnings("serial")
public class NewLayerPage extends GeoServerSecuredPage {

    String storeId;
    private NewLayerPageProvider provider;
    private GeoServerTablePanel<Resource> layers;
    private WebMarkupContainer layersContainer;
    private WebMarkupContainer selectLayersContainer;
    private WebMarkupContainer selectLayers;
    private Label storeName;
    
    public NewLayerPage() {
        this(null);
    }

    public NewLayerPage(String storeId) {
        this.storeId = storeId;
        
        // the store selector, used when no store is initially known
        Form selector = new Form("selector");
        selector.add(storesDropDown());
        selector.setVisible(storeId == null);
        add(selector);
        
        // the layer choosing block 
        // visible when in any  way a store has been chosen
        selectLayersContainer = new WebMarkupContainer("selectLayersContainer");
        selectLayersContainer.setOutputMarkupId(true);
        add(selectLayersContainer);
        selectLayers = new WebMarkupContainer("selectLayers");
        selectLayers.setVisible(storeId != null);
        selectLayersContainer.add(selectLayers);
        
        selectLayers.add(storeName = new Label("storeName", new Model()));
        if(storeId != null) {
            StoreInfo store = getCatalog().getStore(storeId, StoreInfo.class);
            storeName.setModelObject(store.getName());
        }
        
        provider = new NewLayerPageProvider();
        provider.setStoreId(storeId);
        provider.setShowPublished(true);
        layers = new GeoServerTablePanel<Resource>("layers", provider) {

            @Override
            protected Component getComponentForProperty(String id,
                    IModel itemModel, Property<Resource> property) {
                if (property == NewLayerPageProvider.NAME) {
                    return new Label(id, property.getModel(itemModel));
                } else if (property == NewLayerPageProvider.PUBLISHED) {
                    final Resource resource = (Resource) itemModel.getObject();
                    final CatalogIconFactory icons = CatalogIconFactory.get();
                    if(resource.isPublished()) {
                        ResourceReference icon = icons.getEnabledIcon();
                        Fragment f = new Fragment(id, "iconFragment", NewLayerPage.this);
                        f.add(new Image("layerIcon", icon));
                        return f;
                    } else {
                        return new Label(id);
                    }
                } else if(property == NewLayerPageProvider.ACTION) {
                    final Resource resource = (Resource) itemModel.getObject();
                    if(resource.isPublished()) {
                        return resourceChooserLink(id, itemModel, new ParamResourceModel("publishAgain", this));
                    } else {
                        return resourceChooserLink(id, itemModel, new ParamResourceModel("publish", this));
                    }
                } else {
                    throw new IllegalArgumentException(
                            "Don't know of property " + property.getName());
                }
            }

        };
        layers.setFilterVisible(true);
        
        selectLayers.add(layers);
    }
    
    private DropDownChoice storesDropDown() {
        final DropDownChoice stores = new DropDownChoice("storesDropDown", new Model(),
                new StoreListModel(), new StoreListChoiceRenderer());
        stores.setOutputMarkupId(true);
        stores.add(new AjaxFormComponentUpdatingBehavior("onchange") {
            
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                if (stores.getModelObject() != null) {
                    StoreInfo store = (StoreInfo) stores.getModelObject();
                    provider.setStoreId(store.getId());
                    storeName.setModelObject(store.getName());
                    selectLayers.setVisible(true);

                    // make sure we can actually list the contents, it may happen
                    // the store is actually unreachable, in that case we
                    // want to display an error message
                    try {
                        provider.getItems();
                    } catch(Exception e) {
                        LOGGER.log(Level.SEVERE, "Error retrieving layers for the specified store", e);
                        error(e.getMessage());
                        selectLayers.setVisible(false);
                    }
                    
                    
                } else {
                    selectLayers.setVisible(false);
                }
                target.addComponent(selectLayersContainer);
                target.addComponent(feedbackPanel);

            }

        });
        return stores;
    }

    SimpleAjaxLink resourceChooserLink(String id, IModel itemModel, IModel label) {
        return new SimpleAjaxLink(id, itemModel, label) {

            @Override
            protected void onClick(AjaxRequestTarget target) {
                Resource resource = (Resource) getModelObject();
                setResponsePage(new ResourceConfigurationPage(
                        buildLayerInfo(resource), true));
            }

        };
    }

    /**
     * Turns a resource name into a full {@link ResourceInfo}
     * 
     * @param resource
     * @return
     */
    LayerInfo buildLayerInfo(Resource resource) {
        Catalog catalog = getCatalog();
        StoreInfo store = catalog.getStore(getSelectedStoreId(), StoreInfo.class);

        // try to build from coverage store or data store
        try {
            CatalogBuilder builder = new CatalogBuilder(catalog);
            builder.setStore(store);
            if (store instanceof CoverageStoreInfo) {
                CoverageInfo ci = builder.buildCoverage();
                return builder.buildLayer(ci);
            } else if (store instanceof DataStoreInfo) {
                FeatureTypeInfo fti = builder.buildFeatureType(resource.getName());
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
    
    /**
     * Returns the storeId provided during construction, or the one pointed
     * by the drop down if none was provided during construction
     * @return
     */
    String getSelectedStoreId() {
        // the provider is always up to date 
        return provider.getStoreId();
    }

    final class StoreListModel extends LoadableDetachableModel {
        @Override
        protected Object load() {
            List<StoreInfo> stores = getCatalog().getStores(StoreInfo.class);
            stores = new ArrayList<StoreInfo>(stores);
            Collections.sort(stores, new Comparator<StoreInfo>() {
                public int compare(StoreInfo o1, StoreInfo o2) {
                    if (o1.getWorkspace().equals(o2.getWorkspace())) {
                        return o1.getName().compareTo(o2.getName());
                    }
                    return o1.getWorkspace().getName().compareTo(o2.getWorkspace().getName());
                }
            });
            return stores;
        }
    }
    
    static final class StoreListChoiceRenderer implements IChoiceRenderer {

        public Object getDisplayValue(Object store) {
            StoreInfo info = (StoreInfo) store;
            return new StringBuilder(info.getWorkspace().getName()).append(':').append(
                    info.getName());
        }

        public String getIdValue(Object store, int arg1) {
            return ((StoreInfo) store).getId();
        }

    }

}
