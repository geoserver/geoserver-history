/* Copyright (c) 2001 - 2008 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.web.importer;

import static org.geoserver.web.importer.ImportSummaryProvider.*;

import java.util.logging.Level;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.PageParameters;
import org.apache.wicket.ResourceReference;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.geoserver.catalog.CascadeDeleteVisitor;
import org.geoserver.catalog.Catalog;
import org.geoserver.catalog.FeatureTypeInfo;
import org.geoserver.catalog.LayerInfo;
import org.geoserver.catalog.StoreInfo;
import org.geoserver.catalog.WorkspaceInfo;
import org.geoserver.importer.ImportStatus;
import org.geoserver.importer.ImportSummary;
import org.geoserver.importer.LayerSummary;
import org.geoserver.web.CatalogIconFactory;
import org.geoserver.web.GeoServerSecuredPage;
import org.geoserver.web.data.resource.ResourceConfigurationPage;
import org.geoserver.web.demo.PreviewLayer;
import org.geoserver.web.wicket.ConfirmationAjaxLink;
import org.geoserver.web.wicket.GeoServerTablePanel;
import org.geoserver.web.wicket.ParamResourceModel;
import org.geoserver.web.wicket.GeoServerDataProvider.Property;
import org.opengis.feature.type.GeometryDescriptor;

/**
 * Reports the import results in a table and allows the user to
 * edit the and to preview the layers
 * @author aaime
 *
 */
@SuppressWarnings("serial")
public class ImportSummaryPage extends GeoServerSecuredPage {

    public ImportSummaryPage(final ImportSummary summary) {
        // the synthetic results
        IModel summaryMessage;
        Exception error = summary.getError();
        if(error != null) {
            String errorSummary = error.getClass().getSimpleName() + ", " + error.getMessage();
            summaryMessage = new ParamResourceModel("summaryError", this, errorSummary);
        } else if(summary.getProcessedLayers() == 0) {
            summaryMessage = new ParamResourceModel("summaryCancelled", this);
        } else {
            if(summary.getFailures() > 0) {
                if(summary.isCompleted()) {
                    summaryMessage = new ParamResourceModel("summaryFailures", this, summary.getTotalLayers(), summary.getFailures()); 
                } else {
                    summaryMessage = new ParamResourceModel("summaryPartialFailures", this, summary.getTotalLayers(), 
                            summary.getProcessedLayers(), summary.getFailures());
                }
            } else {
                if(summary.isCompleted()) {
                    summaryMessage = new ParamResourceModel("summarySuccess", this, summary.getTotalLayers());
                } else {
                    summaryMessage = new ParamResourceModel("summaryPartialSuccess", this, summary.getTotalLayers(), 
                            summary.getProcessedLayers());
                }
            }
        }
        add(new Label("summary", summaryMessage));
        
        // the list of imported layers
        GeoServerTablePanel<LayerSummary> table = new GeoServerTablePanel<LayerSummary>("importSummary", new ImportSummaryProvider(
                summary.getLayers())) {

            @Override
            protected Component getComponentForProperty(String id, IModel itemModel,
                    Property<LayerSummary> property) {
                final LayerSummary layerSummary = (LayerSummary) itemModel.getObject();
                final CatalogIconFactory icons = CatalogIconFactory.get();
                LayerInfo layer = layerSummary.getLayer();
				if(property == LAYER) {
                    Fragment f = new Fragment(id, "edit", ImportSummaryPage.this);
                    
                    Link editLink = editLink(layerSummary);
                    editLink.setEnabled(layer != null);
                    f.add(editLink);
                    
                    return f;
                } else if(property == STATUS) {
                    ResourceReference icon = layerSummary.getStatus().successful() ? 
                            icons.getEnabledIcon() : icons.getDisabledIcon();
                    Fragment f = new Fragment(id, "iconFragment", ImportSummaryPage.this);
                    f.add(new Image("icon", icon));
                    return f;
                } else if(property == TYPE) {
                    if(layer != null) {
                        ResourceReference icon = icons.getSpecificLayerIcon(layer);
                        Fragment f = new Fragment(id, "iconFragment", ImportSummaryPage.this);
                        Image image = new Image("icon", icon);
                        image.add(new AttributeModifier("title", true, new Model(getTypeTooltip(layer))));
						f.add(image);
                        return f;
                    } else {
                        return new Label(id, "");
                    }
                } else if(property == COMMANDS) {
                    Fragment f = new Fragment(id, "preview", ImportSummaryPage.this);

                    ExternalLink link = new ExternalLink("preview", "#");
                    if(layerSummary.getStatus().successful()) {
                        // TODO: move the preview link generation ability to some utility object
                        PreviewLayer preview = new PreviewLayer(layer);
                        String url = "window.open(\"" + preview.getWmsLink() + "&format=application/openlayers\")";
                        link.add(new AttributeAppender("onclick", new Model(url), ";"));
                    } else {
                        link.setEnabled(false);
                    }
                    f.add(link);
                    
                    return f;
                }
                return null;
            }

            
        };
        table.setOutputMarkupId(true);
        table.setFilterable(false);
        add(table);
        
        // the rollback command
        add(new ConfirmationAjaxLink("rollback", null, 
                new ParamResourceModel("rollback", this), 
                new ParamResourceModel("confirmRollback", this)) {
            
            @Override
            protected void onClick(AjaxRequestTarget target) {
                Catalog catalog = getCatalog();
                CascadeDeleteVisitor deleteVisitor = new CascadeDeleteVisitor(catalog);
                String project = summary.getProject();
                if(summary.isWorkspaceNew()) {
                    WorkspaceInfo ws = catalog.getWorkspaceByName(project);
                    if(ws != null)
                        ws.accept(deleteVisitor);
                } else if(summary.isStoreNew()) {
                    StoreInfo si = catalog.getStoreByName(project, project, StoreInfo.class);
                    if(si != null)
                        si.accept(deleteVisitor);
                } else {
                    // just remove the layers we created
                    for (LayerSummary layer : summary.getLayers()) {
                        catalog.remove(layer.getLayer());
                        catalog.remove(layer.getLayer().getResource());
                    }
                }
                setResponsePage(ImportPage.class, new PageParameters("afterCleanup=true"));
            }
        });
    }
    
    Link editLink(final LayerSummary layerSummary) {
        Link link = new Link("edit") {

            @Override
            public void onClick() {
                Page p = new ResourceConfigurationPage(layerSummary.getLayer(), true) {
                    @Override
                    protected void onSuccessfulSave() {
                        setResponsePage(ImportSummaryPage.this);
                        layerSummary.setStatus(ImportStatus.SUCCESS);
                    }
                    
                    @Override
                    protected void onCancel() {
                        setResponsePage(ImportSummaryPage.this);
                    }
                };
                setResponsePage(p);
            }
            
        };
        
        // keep the last modified name if possible
        String name;
        if(layerSummary.getLayer() != null)
            name = layerSummary.getLayer().getName();
        else
            name =  layerSummary.getLayerName();
        link.add(new Label("name", name));
        
        // also set a tooltip explaining what this action does
        link.add(new AttributeModifier("title", true, new ParamResourceModel("edit", this, name)));
        
        return link;
    }
    
    String getTypeTooltip(LayerInfo layer) {
    	try {
	    	String type = null;
	    	FeatureTypeInfo fti = (FeatureTypeInfo) layer.getResource();
	        GeometryDescriptor gd = fti.getFeatureType().getGeometryDescriptor();
	        if(gd != null) {
	            type = gd.getType().getBinding().getSimpleName();
	        }
	        if(type != null)
	        	return new ParamResourceModel("geomtype." + type, ImportSummaryPage.this).getString();
	        else
	        	return "geomtype.null";
    	} catch(Exception e) {
    		LOGGER.log(Level.WARNING, "Could not compute the geom type tooltip", e);
    		return "geomtype.error";
    	}
    }

}
