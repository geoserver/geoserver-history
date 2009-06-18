package org.geoserver.web.importer;

import static org.geoserver.web.importer.ImportSummaryProvider.*;

import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.ResourceReference;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.geoserver.importer.ImportStatus;
import org.geoserver.importer.ImportSummary;
import org.geoserver.importer.LayerSummary;
import org.geoserver.web.CatalogIconFactory;
import org.geoserver.web.GeoServerSecuredPage;
import org.geoserver.web.data.resource.ResourceConfigurationPage;
import org.geoserver.web.demo.PreviewLayer;
import org.geoserver.web.wicket.GeoServerTablePanel;
import org.geoserver.web.wicket.ParamResourceModel;
import org.geoserver.web.wicket.GeoServerDataProvider.Property;

@SuppressWarnings("serial")
public class ImportSummaryPage extends GeoServerSecuredPage {

    public ImportSummaryPage(ImportSummary summary) {
        // the synthetic results
        if(summary.getFailures() > 0) {
            add(new Label("summary", new ParamResourceModel("summaryFailures", this, summary.getTotalLayers(), summary.getFailures())));
        } else {
            add(new Label("summary", new ParamResourceModel("summarySuccess", this, summary.getTotalLayers())));
        }

        GeoServerTablePanel<LayerSummary> table = new GeoServerTablePanel<LayerSummary>("importSummary", new ImportSummaryProvider(
                summary.getLayers())) {

            @Override
            protected Component getComponentForProperty(String id, IModel itemModel,
                    Property<LayerSummary> property) {
                final LayerSummary layerSummary = (LayerSummary) itemModel.getObject();
                if(property == LAYER) {
                    Fragment f = new Fragment(id, "edit", ImportSummaryPage.this);
                    
                    Link editLink = editLink(layerSummary);
                    editLink.setEnabled(layerSummary.getLayer() != null);
                    f.add(editLink);
                    
                    return f;
                } else if(property == STATUS) {
                    final CatalogIconFactory icons = CatalogIconFactory.get();
                    ResourceReference icon = layerSummary.getStatus().successful() ? 
                            icons.getEnabledIcon() : icons.getDisabledIcon();
                    Fragment f = new Fragment(id, "iconFragment", ImportSummaryPage.this);
                    f.add(new Image("icon", icon));
                    return f;
                } else if(property == COMMANDS) {
                    Fragment f = new Fragment(id, "preview", ImportSummaryPage.this);

                    ExternalLink link = new ExternalLink("preview", "#");
                    if(layerSummary.getStatus().successful()) {
                        // TODO: move the preview link generation ability to some utility object
                        PreviewLayer preview = new PreviewLayer(layerSummary.getLayer());
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
        if(layerSummary.getLayer() != null)
            link.add(new Label("name", layerSummary.getLayer().getName()));
        else
            link.add(new Label("name", layerSummary.getLayerName()));
        
        return link;
    }

}
